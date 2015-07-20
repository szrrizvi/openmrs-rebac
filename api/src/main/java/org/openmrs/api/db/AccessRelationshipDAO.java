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
package org.openmrs.api.db;

import java.util.Date;
import java.util.List;

import org.openmrs.AccessRelationship;
import org.openmrs.AccessRelationshipType;
import org.openmrs.Person;

/**
 * AccessRelationship-related database functions
 * <p>
 * This is used by the AccessRelationshipService. This should not be used directly, but rather used through the
 * methods on the AccessRelationshipService.
 * <p>
 * Use case: <code>
 *   AccessRelationshipService ars = Context.getAccessRelationshipService();
 *   ars....
 *   
 * </code>
 * 
 * @see org.openmrs.api.AccessRelationshipService
 * @see org.openmrs.api.context.Context
 */
public interface AccessRelationshipDAO {
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationship(Integer)
	 */
	public AccessRelationship getAccessRelationship(Integer accessRelationshipId) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipByUuid(String)
	 */
	public AccessRelationship getAccessRelationshipByUuid(String uuid) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAllAccessRelationships(boolean)
	 */
	public List<AccessRelationship> getAllAccessRelationships(boolean includeVoided) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationships(Person, Person, AccessRelationshipType)
	 */
	public List<AccessRelationship> getAccessRelationships(Person fromPerson, Person toPerson,
	        AccessRelationshipType accessRelType) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationships(Person, Person, AccessRelationshipType, Date, Date)
	 */
	public List<AccessRelationship> getAccessRelationships(Person fromPerson, Person toPerson,
	        AccessRelationshipType accessRelType, Date startEffectiveDate, Date endEffectiveDate) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAllAccessRelationshipTypes()
	 */
	public List<AccessRelationshipType> getAllAccessRelationshipTypes() throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipType(Integer)
	 */
	public AccessRelationshipType getAccessRelationshipType(Integer accessRelationshipTypeId) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipTypeByUuid(String)
	 */
	public AccessRelationshipType getAccessRelationshipTypeByUuid(String uuid) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipTypes(String)
	 */
	public List<AccessRelationshipType> getAccessRelationshipTypes(String accessRelationshipTypeName) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#saveAccessRelationshipType(AccessRelationshipType)
	 */
	public AccessRelationship saveAccessRelationship(AccessRelationship accessRelationship) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#purgeAccessRelationship(AccessRelationship)
	 */
	public void deleteAccessRelationship(AccessRelationship accessRelationship) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#saveAccessRelationshipType(AccessRelationshipType)
	 */
	public AccessRelationshipType saveAccessRelationshipType(AccessRelationshipType accessRelationshipType)
	        throws DAOException;
	
	/**
	 * @see org.openmrs.api.AccessRelationshipService#purgeAccessRelationshipType(AccessRelationshipType)
	 */
	public void deleteAccessRelationshipType(AccessRelationshipType accessRelationshipType) throws DAOException;
	
}
