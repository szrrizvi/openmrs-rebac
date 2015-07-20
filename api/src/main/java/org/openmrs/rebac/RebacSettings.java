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
package org.openmrs.rebac;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; //import org.neo4j.graphdb.GraphDatabaseService;
//import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.openmrs.AccessRelationshipType;
import org.openmrs.AuthorizationPrincipal;
import org.openmrs.Person;
import org.openmrs.Privilege;
import org.openmrs.PrivilegeAssignment;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.aop.RebacAAUtil;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.AccessRelationshipDAO;
import org.openmrs.api.db.PersonDAO; //import org.openmrs.rebac.FrameImpl.RelTypes;
import org.openmrs.rebac.implicitrelations.ImplicitRelationIdentifier;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import ca.ucalgary.ispia.rebac.Direction;

/**
 * This class holds the utility functions for working with the ReBAC features
 */
public class RebacSettings {
	
	private static final Log log = LogFactory.getLog(RebacSettings.class);
	
	// Flag to indicate if the ReBAC setting have been loaded
	private static boolean loaded = false;
	
	// Flag to indicate is ReBAC authorization checking is enabled or disabled
	private static boolean rebacEnabled = false;
	
	// Map of implicitly defined relation identifiers
	private static Map<String, ImplicitRelationIdentifier> implicitRelIds;
	
	// Algorithm flag. Specified which algorithm to use for Authorization Checking
	private static Algorithm algorithm;
	
	public static List<Person> persons = new ArrayList<Person>();
	
	public static List<User> users = new ArrayList<User>();
	
	public static AccessRelationshipType art = null;
	
	private static List<PrivilegeAssignment> privilegeAssignments = new ArrayList<PrivilegeAssignment>();
	
	//private static GraphDatabaseService graphDb;
	
	private static boolean graphDbLoaded = false;
	
	/**
	 * Loads the ReBAC settings. Called during startup
	 */
	public static void load() {
		if (!loaded) {
			log.info("Starting ReBAC Settings");
			
			loadImplicitRelIds(); // Load the implicitly defined relation identifiers
			checkPolicyImpRelIds(); // Check if all the required implicit relation identifiers have been loaded
			rebacEnabled = true; // Set ReBAC authorization checking flag to disabled
			algorithm = Algorithm.SingleAPLazy;
			RebacAAUtil.load(Context.getAuthorizationPrincipalService().getAllPrivilegeAssignments());
			loaded = true; // Indicate that the ReBAC setting have been loaded
			
			log.info("Finished ReBAC Settings");
		}
	}
	
	/**
	 * @return The list of privilege assignments
	 */
	public static List<PrivilegeAssignment> getPAs() {
		return privilegeAssignments;
	}
	
	/**
	 * Loads the implicit relation identifier from the implicit-relIds.xml config file
	 */
	private static void loadImplicitRelIds() {
		// Initialize the map of implicit relation identifiers
		// and read the xml file
		implicitRelIds = new HashMap<String, ImplicitRelationIdentifier>();
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("implicit-relIds.xml");
		
		log.info("Loading implific relation identifiers");
		
		// Get names of all beans in the xml file
		String[] relIds = ctx.getBeanDefinitionNames();
		
		// Loop through all the bean names. Find the corresponding bean by name and add it to the
		// map of implicit relation identifiers
		for (int i = 0; i < relIds.length; i++) {
			ImplicitRelationIdentifier relId = (ImplicitRelationIdentifier) ctx.getBean(relIds[i]);
			log.info("Adding implicit relation identifier: " + relIds[i]);
			
			Context.addProxyPrivilege(PrivilegeConstants.GET_RELATIONSHIP_TYPES);
			if (Context.getAccessRelationshipService().getAccessRelationshipTypeByName(relIds[i]) != null) {
				throw new IllegalRelationIdentifierException(
				        "Implicit Relation Identifier name already exists as AccessRelationshipType: " + relIds[i]);
			}
			Context.removeProxyPrivilege(PrivilegeConstants.GET_RELATIONSHIP_TYPES);
			
			try {
				implicitRelIds.put(relIds[i], relId);
			}
			catch (ClassCastException e) {
				log.error("Error at bean: " + relIds[i] + "\n" + relId.getClass().getName() + " should implement interface "
				        + "org.openmrs.rebac.implicitRelations.ImplicitRelationIdentifier");
				throw e;
			}
		}
	}
	
	/**
	 * Checks if all the required implicit relation identifiers are configured in
	 * implicit-relIds.xml.
	 * If an AuthorizationPrincipal (ReBAC policy formula) requires an implicit relation identifier,
	 * then that implicit relation identifier should have been configured in implicit-relIds.xml,
	 * and loaded into the system at startup.
	 */
	private static void checkPolicyImpRelIds() {
		List<AuthorizationPrincipal> authorizationPrincipals = Context.getAuthorizationPrincipalService()
		        .getAllAuthorizationPrincipals();
		// Get all authorization principals and loop through them
		
		for (AuthorizationPrincipal ap : authorizationPrincipals) {
			checkPolicyImpRelIds(ap.getPolicy());
		}
	}
	
	private static void checkPolicyImpRelIds(PolicyImpl policy) {
		
		if (policy instanceof BoxImpl) {
			// Policy is box variant
			BoxImpl temp = (BoxImpl) policy;
			
			if (StringUtils.hasText(temp.getImplicitRelId())) {
				if (implicitRelIds.get(temp.getImplicitRelId()) == null) {
					// If the implicitRelId field is not empty in the policy,
					// and the value of implicitRelationIdentifier is not a key in implicitRelIds, then
					// throw an exception
					throw new IllegalRelationIdentifierException("Implicit Relation Identifier not found: "
					        + temp.getImplicitRelId());
				}
			}
			// Recurse on contained policy
			checkPolicyImpRelIds(temp.getPolicy());
		} else if (policy instanceof DiamondImpl) {
			// Policy is diamond variant
			DiamondImpl temp = (DiamondImpl) policy;
			
			if (StringUtils.hasText(temp.getImplicitRelId())) {
				if (implicitRelIds.get(temp.getImplicitRelId()) == null) {
					// If the implicitRelId field is not empty in the policy,
					// and the value of implicitRelationIdentifier is not a key in implicitRelIds, then
					// throw an exception
					throw new IllegalRelationIdentifierException("Implicit Relation Identifier not found: "
					        + temp.getImplicitRelId());
				}
			}
			// Recurse on contained policy
			checkPolicyImpRelIds(temp.getPolicy());
		} else if (policy instanceof ConjunctionImpl) {
			// Policy is conjunction varaint
			ConjunctionImpl temp = (ConjunctionImpl) policy;
			// Recurse on contained policies
			checkPolicyImpRelIds(temp.getPolicyA());
			checkPolicyImpRelIds(temp.getPolicyB());
		} else if (policy instanceof DisjunctionImpl) {
			// Policy is disjunction variant
			DisjunctionImpl temp = (DisjunctionImpl) policy;
			// Recurse on contained policies
			checkPolicyImpRelIds(temp.getPolicyA());
			checkPolicyImpRelIds(temp.getPolicyB());
		} else if (policy instanceof NegationImpl) {
			// Policy is negation variant
			NegationImpl temp = (NegationImpl) policy;
			// Recurse on contained policy
			checkPolicyImpRelIds(temp.getPolicy());
		}
	}
	
	/**
	 * @return rebacEnabled
	 */
	public static boolean isRebacEnabled() {
		return rebacEnabled;
	}
	
	/**
	 * Sets rebacEnabled to true
	 * Meaning rebac authorization checking will be active
	 */
	public static void enableRebac() {
		if (Context.getAuthenticatedUser().hasPrivilege(PrivilegeConstants.MANAGE_REBAC_AUTHORIZATION_TOGGLE)) {
			log.info("Enabling ReBAC");
			rebacEnabled = true;
		}
	}
	
	/**
	 * Sets rebacEnabled to false
	 * Meaning rebac authorization checking will not be active
	 */
	public static void disableRebac() {
		if (Context.getAuthenticatedUser().hasPrivilege(PrivilegeConstants.MANAGE_REBAC_AUTHORIZATION_TOGGLE)) {
			log.info("Disabling ReBAC");
			rebacEnabled = false;
		}
	}
	
	/**
	 * @return The Map of implicit relation identifiers configured in implicit-relIds.xml
	 */
	public static Map<String, ImplicitRelationIdentifier> getImplicitRelIds() {
		return implicitRelIds;
	}
	
	/**
	 * Returns the ImplicitRelationIdentifier object with the given name
	 * @param name
	 * @return
	 */
	public static ImplicitRelationIdentifier getImplicitRelationIdentifier(String name) {
		return implicitRelIds.get(name);
	}
	
	/**
	 * @return algorithm flag
	 */
	public static Algorithm getAlgorithm() {
		return algorithm;
	}
	
	/**
	 * Update the algorithm flag
	 * @param newAlgorithm The new algorithm flag
	 */
	public static void setAlgorithm(Algorithm newAlgorithm) {
		if (Context.getAuthenticatedUser().hasPrivilege(PrivilegeConstants.MANAGE_REBAC_AUTHORIZATION_TOGGLE)) {
			log.info("Setting ReBAC Authorization Checking Algorithm to:" + newAlgorithm);
			algorithm = newAlgorithm;
		}
	}
}
