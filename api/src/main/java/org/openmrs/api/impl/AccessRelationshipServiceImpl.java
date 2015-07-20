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
package org.openmrs.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.AccessRelationship;
import org.openmrs.AccessRelationshipType;
import org.openmrs.Person;
import org.openmrs.api.AccessRelationshipService;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.AccessRelationshipDAO;
import org.openmrs.api.db.PersonDAO;

/**
 * Contains methods pertaining to Access Relationships in the system
 */
public class AccessRelationshipServiceImpl extends BaseOpenmrsService implements AccessRelationshipService {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private AccessRelationshipDAO dao;
	
	public AccessRelationshipDAO getDAO() {
		return this.dao;
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#setAccessRelationshipDAO(org.openmrs.api.db.AccessRelationshipDAO)
	 */
	@Override
	public void setAccessRelationshipDAO(AccessRelationshipDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationship(java.lang.Integer)
	 */
	@Override
	public AccessRelationship getAccessRelationship(Integer accessRelationshipId) throws APIException {
		return dao.getAccessRelationship(accessRelationshipId);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipByUuin(java.lang.String)
	 */
	@Override
	public AccessRelationship getAccessRelationshipByUuid(String uuid) throws APIException {
		return dao.getAccessRelationshipByUuid(uuid);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAllAccessRelationships()
	 */
	@Override
	public List<AccessRelationship> getAllAccessRelationships() throws APIException {
		return dao.getAllAccessRelationships(false);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAllAccessRelationships(Boolean)
	 */
	@Override
	public List<AccessRelationship> getAllAccessRelationships(boolean includeVoided) throws APIException {
		return dao.getAllAccessRelationships(includeVoided);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipByPerson(org.openmrs.Person)
	 */
	@Override
	public List<AccessRelationship> getAccessRelationshipsByPerson(Person p) throws APIException {
		// search both the left side and the right side of the AccessRelationship
		// for this person
		List<AccessRelationship> rels = getAccessRelationships(p, null, null);
		rels.addAll(getAccessRelationships(null, p, null));
		
		return rels;
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipsByPerson(org.openmrs.Person, java.util.Date)
	 */
	@Override
	public List<AccessRelationship> getAccessRelationshipsByPerson(Person p, Date effectiveDate) throws APIException {
		// search both the left side and the right side of the ACRelationship
		// for this person
		List<AccessRelationship> rels = getAccessRelationships(p, null, null, effectiveDate);
		rels.addAll(getAccessRelationships(null, p, null, effectiveDate));
		
		return rels;
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationships(org.openmrs.Person, org.openmrs.Person,
	 * 		org.openmrs.AccessRelationshipType)
	 */
	@Override
	public List<AccessRelationship> getAccessRelationships(Person fromPerson, Person toPerson,
	        AccessRelationshipType accessRelType) throws APIException {
		
		return dao.getAccessRelationships(fromPerson, toPerson, accessRelType);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationships(org.openmrs.Person, org.openmrs.Person,
	 * 		org.openmrs.AccessRelationshipType, java.util.Date)
	 */
	@Override
	public List<AccessRelationship> getAccessRelationships(Person fromPerson, Person toPerson,
	        AccessRelationshipType accessRelType, Date effectiveDate) throws APIException {
		
		return getAccessRelationships(fromPerson, toPerson, accessRelType, effectiveDate, null);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationships(org.openmrs.Person, org.openmrs.Person,
	 * 		org.openmrs.AccessRelationshipType, java.util.Date, java.util.Date)
	 */
	@Override
	public List<AccessRelationship> getAccessRelationships(Person fromPerson, Person toPerson,
	        AccessRelationshipType accessRelType, Date startEffectiveDate, Date endEffectiveDate) throws APIException {
		
		return dao.getAccessRelationships(fromPerson, toPerson, accessRelType, startEffectiveDate, endEffectiveDate);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAllAccessRelationshipTypes()
	 */
	@Override
	public List<AccessRelationshipType> getAllAccessRelationshipTypes() throws APIException {
		return dao.getAllAccessRelationshipTypes();
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipType(java.lang.Integer)
	 */
	@Override
	public AccessRelationshipType getAccessRelationshipType(Integer accessRelationshipTypeId) throws APIException {
		return dao.getAccessRelationshipType(accessRelationshipTypeId);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationsihpTypeUuid(java.lang.String)
	 */
	@Override
	public AccessRelationshipType getAccessRelationshipTypeByUuid(String uuid) throws APIException {
		return dao.getAccessRelationshipTypeByUuid(uuid);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipTypeByName(java.lang.String)
	 */
	@Override
	public AccessRelationshipType getAccessRelationshipTypeByName(String accessRelationshipTypeName) throws APIException {
		
		List<AccessRelationshipType> types = dao.getAccessRelationshipTypes(accessRelationshipTypeName);
		
		if (types.size() < 1) {
			return null;
		} else {
			for (Object obj : types) {
				AccessRelationshipType type = (AccessRelationshipType) obj;
				if (type.getName().equals(accessRelationshipTypeName)) {
					return type;
				}
			}
			return null;
		}
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipTypes(java.lang.String)
	 */
	@Override
	public List<AccessRelationshipType> getAccessRelationshipTypes(String searchString) throws APIException {
		return dao.getAccessRelationshipTypes(searchString);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#saveAccessRelationship(org.openmrs.AccessRelationship)
	 */
	@Override
	public AccessRelationship saveAccessRelationship(AccessRelationship accessRelationship) throws APIException {
		return dao.saveAccessRelationship(accessRelationship);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#purgeAccessRelationship(org.openmrs.AccessRelationship)
	 */
	@Override
	public void purgeAccessRelationship(AccessRelationship accessRelationship) throws APIException {
		dao.deleteAccessRelationship(accessRelationship);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#voidAccessRelationship(org.openmrs.AccessRelationship,
	 * 		java.lang.String)
	 */
	@Override
	public AccessRelationship voidAccessRelationship(AccessRelationship accessRelationship, String voidReason)
	        throws APIException {
		if (accessRelationship.isVoided())
			return accessRelationship;
		
		accessRelationship.setVoided(true);
		if (accessRelationship.getVoidedBy() == null)
			accessRelationship.setVoidedBy(Context.getAuthenticatedUser());
		if (voidReason != null)
			accessRelationship.setVoidReason(voidReason);
		accessRelationship.setDateVoided(new Date());
		
		return saveAccessRelationship(accessRelationship);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#unvoidAccessRelationship(org.openmrs.AccessRelationship)
	 */
	@Override
	public AccessRelationship unvoidAccessRelationship(AccessRelationship accessRelationship) throws APIException {
		accessRelationship.setVoided(false);
		accessRelationship.setVoidedBy(null);
		accessRelationship.setDateVoided(null);
		accessRelationship.setVoidReason(null);
		
		return saveAccessRelationship(accessRelationship);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#saveAccessRelationshipType(org.openmrs.AccessRelationshipType)
	 */
	@Override
	public AccessRelationshipType saveAccessRelationshipType(AccessRelationshipType accessRelationshipType)
	        throws APIException {
		return dao.saveAccessRelationshipType(accessRelationshipType);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#purgeAccessRelationshipType(org.openmrs.AccessRelationshipType)
	 */
	@Override
	public void purgeAccessRelationshipType(AccessRelationshipType accessRelationshipType) throws APIException {
		dao.deleteAccessRelationshipType(accessRelationshipType);
	}
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAllRelationshipMap(org.openmrs.AccessRelationshipType)
	 */
	@Override
	public Map<Person, List<Person>> getAccessRelationshipMap(AccessRelationshipType accessRelationshipType)
	        throws APIException {
		// get all relationships with this type
		List<AccessRelationship> accessRelationships = getAccessRelationships(null, null, accessRelationshipType);
		
		// the map to return
		Map<Person, List<Person>> ret = new HashMap<Person, List<Person>>();
		
		if (accessRelationships != null) {
			for (AccessRelationship rel : accessRelationships) {
				Person from = rel.getPersonA();
				Person to = rel.getPersonB();
				
				List<Person> relList = ret.get(from);
				if (relList == null)
					relList = new ArrayList<Person>();
				relList.add(to);
				
				ret.put(from, relList);
			}
		}
		
		return ret;
	}
}
