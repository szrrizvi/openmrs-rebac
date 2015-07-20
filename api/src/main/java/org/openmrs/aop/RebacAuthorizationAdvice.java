/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.AuthorizationPrincipal;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.Person;
import org.openmrs.Privilege;
import org.openmrs.PrivilegeAssignment;
import org.openmrs.User;
import org.openmrs.annotation.AuthorizedAnnotationAttributes;
import org.openmrs.annotation.Resource;
import org.openmrs.annotation.ResourceAnnotationAttributes;
import org.openmrs.api.APIInstantiationException;
import org.openmrs.api.APIRebacAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.api.db.AccessRelationshipDAO;
import org.openmrs.api.db.AuthorizationPrincipalDAO;
import org.openmrs.api.db.PersonDAO;
import org.openmrs.rebac.Algorithm;
import org.openmrs.rebac.FrameImpl;
import org.openmrs.rebac.PolicyImpl;
import org.openmrs.rebac.RebacSettings;
import org.openmrs.util.OpenmrsUtil;

import ca.ucalgary.ispia.rebac.Environment;
import ca.ucalgary.ispia.rebac.Frame;
import ca.ucalgary.ispia.rebac.ModelChecker;
import ca.ucalgary.ispia.rebac.Policy;
import ca.ucalgary.ispia.rebac.util.Cache;
import ca.ucalgary.ispia.rebac.util.Constants;
import ca.ucalgary.ispia.rebac.util.SimpleCache;
import ca.ucalgary.ispia.rebac.util.Triple;

/**
 * This class provides the authorization AOP advice performed for the service layer method.
 * The authorization advice is performed before the method call for setters, and it is performed
 * after the method call for the getters. For the getters, the result is filtered based on
 * the authorization process.
 */
public class RebacAuthorizationAdvice implements MethodInterceptor {
	
	// DAOs required to perform authorization checking.
	// Accessing these resources through the Service layer methods would 
	// cause an infinite loop
	private AccessRelationshipDAO accessRelationshipDao;
	
	private AuthorizationPrincipalDAO authorizationPrincipalDao;
	
	private PersonDAO personDao;
	
	/**
	 * Logger for this class. Uses the name "org.openmrs.api" so that it seems to fit into the
	 * log4j.xml configuration
	 */
	protected static final Log log = LogFactory.getLog("org.openmrs.api");
	
	/**
	 * List of all method name prefixes that result in INFO-level log messages
	 */
	private static final String[] SETTER_METHOD_PREFIXES = { "save", "create", "update", "void", "unvoid", "retire",
	        "unretire", "delete", "purge", "req" };
	
	/**
	 * Allows us to check weather the user is authorized to access a particular method based on the resource owner.
	 * The authorization is by Relationship-Based Access Control.
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// Allow access if daemon thread 
		if (Daemon.isDaemonThread()) {
			return invocation.proceed();
		}
		
		// If Rebac is not enabled, then no rebac authorization checking
		if (!RebacSettings.isRebacEnabled()) {
			return invocation.proceed();
		}
		
		// Get method information
		Method method = invocation.getMethod();
		String name = method.getName();
		
		// Get resource owner annotation attributes
		ResourceAnnotationAttributes roAttributes = new ResourceAnnotationAttributes();
		
		// select method type. setter or getter. (getter = not setter)
		boolean isSetterTypeOfMethod = OpenmrsUtil.stringStartsWith(name, SETTER_METHOD_PREFIXES);
		
		// If the method has Resource annotation
		if (roAttributes.hasResourceOwner(method)) {
			if (isSetterTypeOfMethod) {
				// for setter type method, call checking before
				// if authorization fails, an exception is thrown
				
				before(invocation);
				return invocation.proceed();
			} else {
				// for getter type method, call checking after 
				// and filter the results
				Object result = invocation.proceed();
				return after(result, method);
			}
		} else {
			// the method did not have Resource annotation
			// authorization granted
			return invocation.proceed();
		}
	}
	
	/**
	 * Before access control checking for setter methods. 
	 * @param invocation
	 */
	private void before(MethodInvocation invocation) {
		
		Method method = invocation.getMethod();
		String name = method.getName();
		Object[] args = invocation.getArguments();
		
		if (log.isDebugEnabled())
			log.debug("Calling rebac authorization advice before " + name);
		
		Annotation[][] annotations = method.getParameterAnnotations();
		
		// Find the parameter with the Resource annotation
		int i = 0; // Index for the parameter
		OUTER: // Breaking label
		for (Annotation[] paramAnnotations : annotations) {
			// Loop through all parameters
			for (Annotation annotation : paramAnnotations) {
				// Loop through all annotations on the parameter
				if (annotation instanceof Resource) {
					break OUTER;
				}
			}
			i++; // Next Parameter
		}
		
		Object resource = args[i]; // get the argument that is the resource
		
		if (log.isDebugEnabled())
			log.debug("Resource: " + resource.getClass());
		
		checkPrivileges(method, resource); // check privilege
	}
	
	/**
	 * After access control checking for getter methods. 
	 * @param result the return value of the method invocation
	 * @param method the target method
	 * @return the filtered result
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	public Object after(Object result, Method method) {
		
		// The method's return object could be null, a single, lone, object, or some sort of collection.
		// The collection could be a Collection or a Map.
		if (result == null) {
			// result is null
			
			if (log.isDebugEnabled())
				log.debug("Result is null");
			return null;
		} else if (result instanceof Collection) {
			// result is a collection
			
			if (log.isDebugEnabled())
				log.debug("Result is instance of Collection");
			
			// Get the class of the result and instantiate a copy of the same class
			Collection res = (Collection) result;
			Class resClass = result.getClass();
			
			Collection filtered = null;
			try {
				filtered = (Collection) resClass.newInstance();
			}
			catch (InstantiationException e1) {
				log.error("Instantiation Exception thrown while " + "instantiating class " + resClass.getName(), e1);
				throw new APIInstantiationException("Instantiation Exception thrown while " + "instantiating class "
				        + resClass.getName(), e1);
			}
			catch (IllegalAccessException e1) {
				log
				        .error("Illegal Access Exception thrown while " + "instantiating class " + resClass.getName() + ". "
				                + e1);
				throw new APIInstantiationException("Illegal Access Exception thrown while " + "instantiating class "
				        + resClass.getName() + ". " + e1);
			}
			
			if (filtered != null) {
				for (Object obj : res) {
					
					try {
						// Try calling checkPrivileges(), if the method passes that means
						// that the authorization passes, and result should be kept.
						// If checkPrivileges() doesn't pass, it throws an exception.
						checkPrivileges(method, obj);
						filtered.add(obj);
					}
					catch (APIRebacAuthenticationException e) {
						// If an APIAuthenticationException is caught, it means that
						// the authorization failed, and result should not be kept.
						if (log.isDebugEnabled())
							log.debug("Result discarded: " + obj.toString());
					}
				}
				return filtered;
			} else {
				throwUnauthorized("Collection instantiation failed");
			}
		} else if (result instanceof Map) {
			// result is a map
			if (log.isDebugEnabled())
				log.debug("Result is instance of Map");
			
			// Get the class of the result and instantiate a copy of the same class
			Map res = (Map) result;
			Class resClass = result.getClass();
			
			Map filtered = null;
			try {
				filtered = (Map) resClass.newInstance();
			}
			catch (InstantiationException e1) {
				log.error("Instantiation Exception thrown while " + "instantiating class " + resClass.getName(), e1);
				throw new APIInstantiationException("Instantiation Exception thrown while " + "instantiating class "
				        + resClass.getName(), e1);
			}
			catch (IllegalAccessException e1) {
				log
				        .error("Illegal Access Exception thrown while " + "instantiating class " + resClass.getName() + ". "
				                + e1);
				throw new APIInstantiationException("Illegal Access Exception thrown while " + "instantiating class "
				        + resClass.getName() + ". " + e1);
			}
			if (filtered != null) {
				for (Object key : res.keySet()) {
					
					Object obj = res.get(key);
					
					try {
						// Try calling checkPrivileges(), if the method passes that means
						// that the authorization passes, and result should be kept.
						// If checkPrivileges() doesn't pass, it throws an exception.
						checkPrivileges(method, obj);
						filtered.put(key, obj);
					}
					catch (APIRebacAuthenticationException e) {
						// If an APIAuthenticationException is caught, it means that
						// the authorization failed, and result should not be kept.
						if (log.isDebugEnabled())
							log.debug("Result discarded: " + obj.toString());
					}
				}
				return filtered;
			} else {
				throwUnauthorized("Map instantiation failed");
			}
		} else {
			// result is a single lone object
			
			// Try calling checkPrivileges(), if the method passes that means
			// that the authorization passes, and result should be kept.
			// If checkPrivileges() doesn't pass, it throws an exception.
			checkPrivileges(method, result);
			return result;
		}
		return null;
	}
	
	/**
	 * Checks if the user is authorized to access the method.
	 * @param method the target method
	 * @param resourceOwners the list of resource owners
	 * @throws APIRebacAuthenticationException if the authorization failed
	 */
	private void checkPrivileges(Method method, Object resource) {
		//The resource must be an instance of BaseOpenmrsData and have at least 1 resource owner
		if (!(resource instanceof BaseOpenmrsData)) {
			return;
		}
		
		// Get the list of required privileges and requireAll
		AuthorizedAnnotationAttributes auAttributes = new AuthorizedAnnotationAttributes();
		Collection<String> attrs = auAttributes.getAttributes(method);
		boolean requireAll = auAttributes.getRequireAll(method);
		
		if (attrs.size() <= 0) {
			// Skip if no privileges are required
			return;
		}

		else { // (attrs.size() > 0)
			// Skip if null privileges
			for (String privilege : attrs) {
				if (privilege == null || privilege.length() < 1) {
					return;
				}
			}
		}
		
		//Get authenticated user
		User user = Context.getAuthenticatedUser();
		//Get the person object from the user
		Object vertexAU = user.getPerson();
		
		boolean isAuthorized = false;
		Algorithm alg = RebacSettings.getAlgorithm(); // Obtain the algorithm flag
		
		switch (alg) {
			case SingleAPLazy:
				if (singleAPLazy(resource, vertexAU, attrs, requireAll)) {
					isAuthorized = true;
				}
				break;
			case SingleAPEager:
				if (singleAPEager(resource, vertexAU, attrs, requireAll)) {
					isAuthorized = true;
				}
				break;
			case MultiAPLazy:
				if (multiAPLazy(resource, vertexAU, attrs, requireAll)) {
					isAuthorized = true;
				}
				break;
			case MultiAPEager:
				if (multiAPEager(resource, vertexAU, attrs, requireAll)) {
					isAuthorized = true;
				}
				break;
			default:
				isAuthorized = false;
				break;
		}
		
		if (!isAuthorized) {
			// If isAuthorized is false, throw exception
			throwUnauthorized(user, method, attrs);
		}
	}
	
	/**
	 * Checks if the user can satisfy the privilege requirement against the resource owners.
	 * This is the default algorithm.
	 * The "Single Authorization Principal" lazy algorithm.
	 * First, gets the Authorization Principals (AP) that offer that required privileges.
	 * If requireAll == true, then each AP in the list offers all the required privileges.
	 * If requireAll == false, then each AP in the list offers at least one of the required privileges.
	 * Then checks if any one of the APs in the list can be satisfied between the user and all of the 
	 * resource owners. If yes, then returns true, else returns false.
	 * @param resource The resource
	 * @param vertexAU The user (requestor)
	 * @param attrs The list of required privileges
	 * @param requireAll Flag for required privileges, true is all privileges from attrs is required, false is any one
	 * 		  the privileges from attrs is required
	 * @return True if the user can satisfy the privilege requirement against the resource owners, else false.
	 */
	private boolean singleAPLazy(Object resource, Object vertexAU, Collection<String> attrs, boolean requireAll) {
		
		// get authorization principles associated with the privileges
		// if requireAll is true, then each AP in the list contains all of the required privileges
		// else (requireAll is flase), then each AP in the list contains at least one of the required privleges
		Iterable<AuthorizationPrincipal> authorizationPrincipals = getAssociatedAuthorizationPrincipals(attrs, requireAll);
		
		Set<String> cache = new HashSet<String>();
		
		for (AuthorizationPrincipal ap : authorizationPrincipals) {
			
			// Call model checking for each authorization principle, and allow authorization to succeed if any
			// one of the authorization principle policies can be fulfilled.
			
			// Loop through all resource owners, if the model checking passes for all resource owners
			// Then return from method
			
			if (!cache.contains(ap.getPolicy().toString())) {
				if (modelCheck(ap, resource, vertexAU)) {
					return true;
				}
			} else {
				cache.add(ap.getPolicy().toString());
			}
		}
		// Looped through all the authorization principals, but non could be satisfied.
		// So return false
		return false;
	}
	
	//Strict eager algorithm
	
	/**
	 * Checks if the user can satisfy the privilege requirement against the resource owners
	 * The "Single Authorization Principal" eager algorithm.
	 * First, gets the Authorization Principals (AP) that can be satisfied between the user and all the resource owners.
	 * Then check if any one of the APs in the list offers the required privileges.
	 * If requireAll == true, then a single AP should provide all of the required privileges.
	 * If requireAll == false, then a single AP should provide at least one of the required privileges 
	 * If such an AP exists, then returns true, else returns false.
	 * @param resourceOwners The list of resource owners
	 * @param vertexAU The user
	 * @param attrs The list of required privileges
	 * @param requireAll Flag for required privileges, true is all privileges from attrs is required, false is any one
	 * 		  the privileges from attrs is required
	 * @return True if the user can satisfy the privilege requirement against the resource owners, else false.
	 */
	private boolean singleAPEager(Object resource, Object vertexAU, Collection<String> attrs, boolean requireAll) {
		
		// Get the list of all Privilege Assignments for which the associated Authorization Principals
		// are satisfied between the resource owners and the requestor
		Iterable<PrivilegeAssignment> pas = getAllSatisfiedPAs(resource, vertexAU);
		
		//Loop through each Privilege Assignment
		for (PrivilegeAssignment pa : pas) {
			if (requireAll) {
				if (pa.constaintsAllPrivileges(attrs)) {
					// If the current Privilege Assignment contains all the required privielges, return true
					return true;
				}
			} else {
				// requireAll = false
				if (pa.constaintsAnyPrivileges(attrs)) {
					// If the current Privilege Assignment contains any one of the required privileges, return true
					return true;
				}
			}
		}
		
		//Could not satisfy required privileges, so return false
		return false;
	}
	
	/**
	 * Checks if the user can satisfy the privilege requirement against the resource owners
	 * The "Multi Authorization Principal" lazy algorithm.
	 * First, gets all the Authorization Principals (AP) in the system, and iteratively checks;
	 * If the AP can be satisfied between the user and resource owners, and its associated
	 * Privileges to the set of accumulated Privileges.
	 * Check if the set of accumulated Privileges can satisfy the privilege requirement;
	 * If requireAll == true, then the set of accumulated privileges should contain all the of required privileges.
	 * If requireAll == false, then the set of accumulated privileges should contain at least one of the of required privileges.
	 * If the check passes, then returns true, else move to the next AP.
	 * If all the APs have been exhausted and the privilege requirement is still not satisfied, return false.
	 * @param resourceOwners The list of resource owners
	 * @param vertexAU The user
	 * @param attrs The list of required privileges
	 * @param requireAll Flag for required privileges, true is all privileges from attrs is required, false is any one
	 * 		  the privileges from attrs is required
	 * @return True if the user can satisfy the privilege requirement against the resource owners, else false.
	 */
	private boolean multiAPLazy(Object resource, Object vertexAU, Collection<String> attrs, boolean requireAll) {
		// long start = System.nanoTime();
		//List of accumulated privileges
		Set<Privilege> setPrivs = new HashSet<Privilege>();
		
		//Get all privilege assignments
		//long s1 = System.nanoTime();
		//List<PrivilegeAssignment> privilegeAssignments = RebacAAUtil.getPrivilegeAssignments();
		List<PrivilegeAssignment> privilegeAssignments = authorizationPrincipalDao.getAllPrivilegeAssignments();
		//long e1 = System.nanoTime();
		//System.out.println("getPAS: " + (e1 - s1));
		
		Map<String, Boolean> cache = new HashMap<String, Boolean>();
		
		for (PrivilegeAssignment pa : privilegeAssignments) {
			
			//s1 = System.nanoTime();
			boolean helpful = isPaHelpful(pa.getPrivileges(), setPrivs, attrs);
			//e1 = System.nanoTime();
			//System.out.println("isPAHelpful: " + (e1 - s1));
			
			if (helpful) {
				
				AuthorizationPrincipal ap = pa.getAuthorizationPrincipal();
				
				boolean modelCheckingResult;
				
				if (cache.containsKey(ap.getPolicy().toString())) {
					//s1 = System.nanoTime();
					modelCheckingResult = cache.get(ap.getPolicy().toString());
					//e1 = System.nanoTime();
					//System.out.println("useCache: " + (e1 - s1));
					
				} else {
					//s1 = System.nanoTime();
					modelCheckingResult = modelCheck(ap, resource, vertexAU);
					//e1 = System.nanoTime();
					//System.out.println("model check: " + (e1 - s1));
					
					//s1 = System.nanoTime();
					cache.put(ap.getPolicy().toString(), modelCheckingResult);
					//e1 = System.nanoTime();
					//System.out.println("hash put: " + (e1 - s1));
				}
				
				//Check if the AP can be satisfied against all resource owners
				if (modelCheckingResult) {
					//Add the privileges of PA to the accumulated list
					//s1 = System.nanoTime();
					setPrivs.addAll(pa.getPrivileges());
					//e1 = System.nanoTime();
					//System.out.println("Set add all: " + (e1 - s1));
					
					//Check if the current set of accumulated privileges can satisfy the
					//privilege requirement
					if (requireAll) {
						if (hasAllPrivileges(setPrivs, attrs)) {
							// If the accumulated set has all the required privileges, return true
							// long end = System.nanoTime();
							//System.out.println("multiAPLaze: " + (end - start));
							return true;
						}
					} else {
						//requireAll = false
						//s1 = System.nanoTime();
						boolean any = hasAnyPrivilege(setPrivs, attrs);
						//e1 = System.nanoTime();
						//System.out.println("has any: " + (e1 - s1));
						if (any) {
							// If the accumulated set has any one of the required privileges, return true
							// long end = System.nanoTime();
							//System.out.println("multiAPLaze: " + (end - start));
							return true;
						}
					}
				}
			}
		}
		
		//If reached here, then the privileges were not satisfied
		// long end = System.nanoTime();
		//System.out.println("multiAPLaze: " + (end - start));
		return false;
	}
	
	/**
	 * Checks if the user can satisfy the privilege requirement against the resource owners
	 * The "Multi Authorization Principal" eager algorithm.
	 * First, gets the Authorization Principals (AP) that can be satisfied between the user and all the resource owners.
	 * Then obtain all the privileges provided by all the APs in the list.
	 * Then check if the set of accumulated privileges can satisfy the privilege requirement
	 * If requireAll == true, then the set of accumulated privileges should contain all of the required privileges.
	 * If requireAll == false, then the set of accumulated privileges should contain at least one of the required privileges.
	 * If the check passes, then returns true, else returns false
	 * @param resourceOwners The list of resource owners
	 * @param vertexAU The user
	 * @param attrs The list of required privileges
	 * @param requireAll Flag for required privileges, true is all privileges from attrs is required, false is any one
	 * 		  the privileges from attrs is required
	 * @return True if the user can satisfy the privilege requirement against the resource owners, else false.
	 */
	private boolean multiAPEager(Object resource, Object vertexAU, Collection<String> attrs, boolean requireAll) {
		
		// Get the list of all Privilege Assignments for which the associated Authorization Principals
		// are satisfied between the resource owners and the requestor
		Iterable<PrivilegeAssignment> pas = getAllSatisfiedPAs(resource, vertexAU);
		
		// Get the list of all privileges associated with all the satisfied PAs
		Iterable<Privilege> setPrivs = getPrivileges(pas);
		
		if (requireAll) {
			if (hasAllPrivileges(setPrivs, attrs)) {
				// If the accumulated set has all the required privileges, return true
				return true;
			}
		} else {
			//requireAll = false
			if (hasAnyPrivilege(setPrivs, attrs)) {
				// If the accumulated set has any one of the required privileges, return true
				return true;
			}
		}
		
		//If reached here, then the privileges were not satisfied
		return false;
	}
	
	/**
	 * Consumes a Collection of privileges and returns an Iterable of authorization principals that 
	 * are associated with all those privileges.
	 * @param privileges
	 * @return Iterable of authorization principals that are associated with all the privileges
	 */
	private Iterable<AuthorizationPrincipal> getAssociatedAuthorizationPrincipals(Collection<String> privileges,
	        boolean requireAll) {
		
		List<AuthorizationPrincipal> result = new ArrayList<AuthorizationPrincipal>();
		
		List<PrivilegeAssignment> privilegeAssignments = RebacAAUtil.getPrivilegeAssignments();
		
		// Loop through all privilege assignments
		for (PrivilegeAssignment pa : privilegeAssignments) {
			if (requireAll) {
				//if all privileges are required
				if (pa.constaintsAllPrivileges(privileges)) {
					//if pa contains all privileges, then add pa to result
					result.add(pa.getAuthorizationPrincipal());
				}
			} else {
				//only one of the provided privileges are required
				if (pa.constaintsAnyPrivileges(privileges)) {
					//if pa contains any 1 privilege, then add pa to result
					result.add(pa.getAuthorizationPrincipal());
				}
			}
		}
		//return the accumulated list
		return result;
	}
	
	/**
	 * Returns the list of all Privilege Assignments for which associated the Authorization
	 * Principal that can be satisfied between the resource owners and the requestor
	 * @param resourceOwners List of resource owners
	 * @param vertexAU The requestor
	 * @return Iterable of all Privilege Assignments for which associated the Authorization
	 * Principal that can be satisfied between the resource owners and the requestor
	 */
	private Iterable<PrivilegeAssignment> getAllSatisfiedPAs(Object resource, Object vertexAU) {
		
		//List to hold the PrivilegeAssignments for which the AuthorizationPrincipals are satisfied
		List<PrivilegeAssignment> result = new ArrayList<PrivilegeAssignment>();
		
		//List of all PAs from the database
		//List<PrivilegeAssignment> pas = RebacAAUtil.getPrivilegeAssignments();
		List<PrivilegeAssignment> pas = authorizationPrincipalDao.getAllPrivilegeAssignments();
		
		//Perform model checking on AP for each PA, and add it to the result
		//if the model checking was valid against the requestor and all the owners of the resource
		for (PrivilegeAssignment pa : pas) {
			
			AuthorizationPrincipal ap = pa.getAuthorizationPrincipal();
			
			if (modelCheck(ap, resource, vertexAU)) {
				result.add(pa);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the accumulated list of all Privileges associated with the given PrivilegeAssignments
	 * @param pas
	 * @return the accumulated list of all Privileges associated with the given PrivilegeAssignments
	 */
	private Iterable<Privilege> getPrivileges(Iterable<PrivilegeAssignment> pas) {
		// long start = System.nanoTime();
		Set<Privilege> result = new HashSet<Privilege>();
		
		//Accumulate the set of privileges in result
		for (PrivilegeAssignment pa : pas) {
			result.addAll(pa.getPrivileges());
		}
		// long end = System.nanoTime();
		//	//System.out.println("getPrivileges: " + (end - start));
		return result;
	}
	
	/**
	 * Checks if the set of privileges contains the privilege
	 * @param setPrivileges
	 * @param privilege
	 * @return True if setPrivileges contains privilege, else false.
	 */
	private boolean hasPrivilege(Iterable<Privilege> setPrivileges, String privilege) {
		// long start = System.nanoTime();
		// Check if setPrivileges contains privilege
		for (Privilege p : setPrivileges) {
			if (p.getPrivilege().equals(privilege)) {
				// long end = System.nanoTime();
				//	//System.out.println("has Privilege: " + (end - start));
				return true;
			}
		}
		
		// If setPrivileges does not contain privilege, return false
		// long end = System.nanoTime();
		//	//System.out.println("hasPrivilege: " + (end - start));
		return false;
	}
	
	/**
	 * Check if the set of privileges contains any one of the privileges
	 * @param setPrivileges 
	 * @param privileges
	 * @return True is setPrivileges contains any of the privileges
	 */
	private boolean hasAnyPrivilege(Iterable<Privilege> setPrivileges, Collection<String> privileges) {
		// long start = System.nanoTime();
		//Loop through each privilege
		for (String privilege : privileges) {
			//Check if setPrivileges contains privilege
			if (hasPrivilege(setPrivileges, privilege)) {
				// long end = System.nanoTime();
				//	//System.out.println("hasAnyPRivilege: " + (end - start));
				return true;
			}
		}
		
		// If setPrivileges does not contain contain any of the privileges, return false
		// long end = System.nanoTime();
		//	//System.out.println("hasAnyPRivilege: " + (end - start));
		return false;
	}
	
	/**
	 * Check if the set of privileges contains all of the privileges
	 * @param setPrivileges 
	 * @param privileges
	 * @return True is setPrivileges contains all of the privileges
	 */
	private boolean hasAllPrivileges(Iterable<Privilege> setPrivileges, Collection<String> privileges) {
		// long start = System.nanoTime();
		//Loop through each privilege
		for (String privilege : privileges) {
			//Check if setPrivileges do not contain privilege
			if (!hasPrivilege(setPrivileges, privilege)) {
				// long end = System.nanoTime();
				/////System.out.println("hasAllPRivileges: " + (end - start));
				return false;
			}
		}
		
		//If reached here, the setPrivileges contained all the privileges
		// long end = System.nanoTime();
		////System.out.println("hasAllPRivileges: " + (end - start));
		return true;
	}
	
	/**
	 * Checks if newPrivs has anything in common with attrs that is not already constained in
	 * privs
	 * @param newPrivs
	 * @param privs
	 * @param attrs
	 * @return
	 */
	private boolean isPaHelpful(Set<Privilege> newPrivs, Set<Privilege> privs, Collection<String> attrs) {
		// long start = System.nanoTime();
		for (String attr : attrs) {
			if (hasPrivilege(newPrivs, attr)) {
				if (!hasPrivilege(privs, attr)) {
					// if newPrivs constains attr and privs does not contain attr
					// long end = System.nanoTime();
					////System.out.println("isPaHelpful: " + (end - start));
					return true;
				}
			}
		}
		// long end = System.nanoTime();
		////System.out.println("isPaHelpful: " + (end - start));
		return false;
	}
	
	/**
	 * Creates the appropriate frames and calls the model checker
	 * @param authorizationPrincipal
	 * @param resource
	 * @param requestor
	 * @return
	 */
	private boolean modelCheck(AuthorizationPrincipal authorizationPrincipal, Object resource, Object requestor) {
		// long start = System.nanoTime();
		Frame frame = null;
		PolicyImpl policy = null;
		
		// Get the rebac policy from the authorization principal and translate it to primitive forms
		policy = authorizationPrincipal.getPolicy();
		policy = PolicyImpl.translate(policy);
		
		Person vertex = (Person) requestor;
		BaseOpenmrsData res = (BaseOpenmrsData) resource;
		
		// First perform the model checking in the top context.
		// Create the frame for top context
		frame = new FrameImpl(this.accessRelationshipDao, this.personDao);
		Cache<Triple<Policy, Environment, Object>, Boolean> cache = new SimpleCache<Triple<Policy, Environment, Object>, Boolean>();
		//System.out.println(authorizationPrincipal.getAuthorizationPrincipal() + " " + authorizationPrincipal.getPolicy());
		boolean result = ModelChecker.check(cache, frame, Constants.VariableConvention.ResourceRequestor, res, vertex,
		    policy);
		
		if (result) {
			
			// Model Checker passed in top context
			// long end = System.nanoTime();
			////System.out.println("modelCheck: " + (end - start));
			return true;
		}
		
		// The policy could not have been satisfied
		// long end = System.nanoTime();
		////System.out.println("modelCheck: " + (end - start));
		return false;
	}
	
	/**
	 * Throws an APIAuthorization exception stating why the user failed
	 * 
	 * @param user authenticated user
	 * @param method acting method
	 * @param attrs Collection of String privilege names that the user must have
	 */
	private void throwUnauthorized(User user, Method method, Collection<String> attrs) {
		if (log.isDebugEnabled())
			log.debug("User " + user + " is not authorized to access " + method.getName());
		throw new APIRebacAuthenticationException("Could not fulfill Authorization Priniciples for privileges: " + attrs);
	}
	
	/**
	 * Throws an APIRebacAuthorization exception stating why the authentication failed
	 * 
	 * @param message message indicating why the authentication failed
	 */
	private void throwUnauthorized(String message) {
		log.debug(message);
		throw new APIRebacAuthenticationException(message);
	}
	
	// Field Setters
	
	public void setAccessRelationshipDao(AccessRelationshipDAO dao) {
		this.accessRelationshipDao = dao;
	}
	
	public void setAuthorizationPrincipalDao(AuthorizationPrincipalDAO dao) {
		this.authorizationPrincipalDao = dao;
	}
	
	public void setPersonDao(PersonDAO dao) {
		this.personDao = dao;
	}
}
