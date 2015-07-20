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
package org.openmrs.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openmrs.AccessRelationship;
import org.openmrs.AccessRelationshipType;
import org.openmrs.Person;
import org.openmrs.annotation.Authorized;
import org.openmrs.annotation.Resource;
import org.openmrs.util.PrivilegeConstants;
import org.openmrs.api.db.AccessRelationshipDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains service layer methods for AccessRelationship and AccessRelationshipType.
 */

@Transactional
public interface AccessRelationshipService extends OpenmrsService {
	
	public AccessRelationshipDAO getDAO();
	
	/**
	 * Sets the DAO for this service. This is done through spring injection
	 * 
	 * @param dao DAO for this service
	 */
	public void setAccessRelationshipDAO(AccessRelationshipDAO dao);
	
	// METHODS FOR ACCESS RELATIONSHIPS
	
	/**
	 * Get access relationship by internal access relationship identifier
	 * 
	 * @param accessRelationshipId
	 * @return The Access Relationship to match on or null if none found
	 * @throws APIException
	 * @should return access relationship with given id
	 * @should return null when access relationship with given id does not exist
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIPS })
	@Resource
	public AccessRelationship getAccessRelationship(Integer accessRelationshipId) throws APIException;
	
	/**
	 * Get AccessRelationship by its UUID
	 * 
	 * @param uuid
	 * @return
	 * @should find object given valid uuid
	 * @should return null if no object found with given uuid
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIPS })
	@Resource
	public AccessRelationship getAccessRelationshipByUuid(String uuid) throws APIException;
	
	/**
	 * Get list of AccessRelationships that are not voided
	 * 
	 * @return non-voided AccessRelationship list
	 * @throws APIException
	 * @return list of all unvoided AccessRelationship
	 * @should return all unvoided AccessRelationships
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIPS })
	@Resource
	public List<AccessRelationship> getAllAccessRelationships() throws APIException;
	
	/**
	 * Get list of AccessRelationships optionally including the voided ones or not
	 * 
	 * @param includeVoided true/false whether to include the voided AccessRelationships
	 * @return non-voided AccessRelationship list
	 * @throws APIException
	 * @should return all AccessRelationship including voided when include voided equals true
	 * @should return all AccessRelationship excluding voided when include voided equals false
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIPS })
	@Resource
	public List<AccessRelationship> getAllAccessRelationships(boolean includeVoided) throws APIException;
	
	/**
	 * Get list of AccessRelationships that include Person in person_id or relative_id Does not include
	 * voided AccessRelationships
	 * 
	 * @param p person object listed on either side of the AccessRelationship
	 * @return AccessRelationship list
	 * @throws APIException
	 * @should only get unvoided AccessRelationships
	 * @should fetch AccessRelationships associated with the given person
	 * @should fetch unvoided AccessRelationships only
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIPS })
	@Resource
	public List<AccessRelationship> getAccessRelationshipsByPerson(Person p) throws APIException;
	
	/**
	 * Get list of AccessRelationships that include Person in person_id or relative_id.
	 * Does not include voided AccessRelationships. 
	 * Accepts an effectiveDate parameter which, if supplied, will limit the returned
	 * AccessRelationships to those that were active on the given date.  Such active AccessRelationships
	 * include those that have a startDate that is null or less than or equal to the effectiveDate, 
	 * and that have an endDate that is null or greater than or equal to the effectiveDate.
	 * 
	 * @param p person object listed on either side of the AccessRelationship
	 * @param effectiveDate effective date of AccessRelationship
	 * @return AccessRelationship list
	 * @throws APIException
	 * @should only get unvoided AccessRelationships
	 * @should only get unvoided AccessRelationships regardless of effective date
	 * @should fetch AccessRelationships associated with the given person
	 * @should fetch AccessRelationships that were active during effectiveDate
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIPS })
	@Resource
	public List<AccessRelationship> getAccessRelationshipsByPerson(Person p, Date effectiveDate) throws APIException;
	
	/**
	 * Get AccessRelationships stored in the database that
	 * 
	 * @param fromPerson (optional) Person to in the person_id column
	 * @param toPerson (optional) Person in the relative_id column
	 * @param accessRelType (optional) The AccessRelationshipType to match
	 * @return AccessRelationships matching the given parameters
	 * @throws APIException
	 * @should fetch AccessRelationships matching the given from person
	 * @should fetch AccessRelationships matching the given to person
	 * @should fetch AccessRelationships matching the given acRel type
	 * @should return empty list when no AccessRelationship matching given parameters exist
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIPS })
	@Resource
	public List<AccessRelationship> getAccessRelationships(Person fromPerson, Person toPerson,
	        AccessRelationshipType accessRelType) throws APIException;
	
	/**
	 * Get AccessRelationships stored in the database that are active on the passed date
	 * 
	 * @param fromPerson (optional) Person to in the person_id column
	 * @param toPerson (optional) Person in the relative_id column
	 * @param accessRelType (optional) The AccessRelationshipType to match
	 * @param effectiveDate (optional) The date during which the AccessRelationship was effective
	 * @return AccessRelationships matching the given parameters
	 * @throws APIException
	 * @should fetch AccessRelationships matching the given from person
	 * @should fetch AccessRelationships matching the given to person
	 * @should fetch AccessRelationships matching the given accessRel type
	 * @should return empty list when no AccessRelationship matching given parameters exist
	 * @should fetch AccessRelationships that were active during effectiveDate
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIPS })
	@Resource
	public List<AccessRelationship> getAccessRelationships(Person fromPerson, Person toPerson,
	        AccessRelationshipType accessRelType, Date effectiveDate) throws APIException;
	
	/**
	 * Get AccessRelationships stored in the database that were active during the specified date range
	 * 
	 * @param fromPerson (optional) Person to in the person_id column
	 * @param toPerson (optional) Person in the relative_id column
	 * @param accessRelType (optional) The AccessRelationshipType to match
	 * @param startEffectiveDate (optional) The date during which the AccessRelationship was effective (lower bound)
	 * @param endEffectiveDate (optional) The date during which the AccessRelationship was effective (upper bound)
	 * @return AccessRelationships matching the given parameters
	 * @throws APIException
	 * @should fetch AccessRelationships matching the given from person
	 * @should fetch AccessRelationships matching the given to person
	 * @should fetch AccessRelationships matching the given accessRel type
	 * @should return empty list when no AccessRelationship matching given parameters exist
	 * @should fetch AccessRelationships that were active during the specified date range
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIPS })
	@Resource
	public List<AccessRelationship> getAccessRelationships(Person fromPerson, Person toPerson,
	        AccessRelationshipType accessRelType, Date startEffectiveDate, Date endEffectiveDate) throws APIException;
	
	/**
	 * Create or update a AccessRelationship between people. Saves the given <code>AccessRelationship</code> to
	 * the database
	 * 
	 * @param accessRelationship AccessRelationship to be created or updated
	 * @return AccessRelationship that was created or updated
	 * @throws APIException
	 * @should create new object when AccessRelationship id is null
	 * @should update existing object when AccessRelationship id is not null
	 */
	@Authorized( { PrivilegeConstants.ADD_RELATIONSHIPS, PrivilegeConstants.EDIT_RELATIONSHIPS })
	public AccessRelationship saveAccessRelationship(@Resource AccessRelationship accessRelationship) throws APIException;
	
	/**
	 * Purges a AccessRelationship from the database (cannot be undone)
	 * 
	 * @param accessRelationship AccessRelationship to be purged from the database
	 * @throws APIException
	 * @should delete ACRelationship from the database
	 */
	@Authorized( { PrivilegeConstants.PURGE_RELATIONSHIPS })
	public void purgeAccessRelationship(@Resource AccessRelationship accessRelationship) throws APIException;
	
	/**
	 * Voids the given AccessRelationship, effectively removing it from openmrs.
	 * 
	 * @param accessRelationship AccessRelationship to void
	 * @param voidReason String reason the AccessRelationship is being voided.
	 * @return the newly saved AccessRelationship
	 * @throws APIException
	 * @should void AccessRelationship with the given reason
	 */
	@Authorized( { PrivilegeConstants.DELETE_RELATIONSHIPS })
	public AccessRelationship voidAccessRelationship(@Resource AccessRelationship accessRelationship, String voidReason)
	        throws APIException;
	
	/**
	 * Unvoid AccessRelationship in the database, effectively marking this as a valid AccessRelationship again
	 * 
	 * @param accessRelationship AccessRelationship to unvoid
	 * @return the newly unvoided AccessRelationship
	 * @throws APIException
	 * @should unvoid voided AccessRelationship
	 */
	@Authorized( { PrivilegeConstants.EDIT_RELATIONSHIPS })
	public AccessRelationship unvoidAccessRelationship(@Resource AccessRelationship accessRelationship) throws APIException;
	
	// METHODS FOR ACCESS RELATIONSHIP TYPES
	
	/**
	 * Get all AccessRelationshipTypes Includes retired AccessRelationship types
	 * 
	 * @return AccessRelationshipType list
	 * @throws APIException
	 * @should return all AccessRelationship types
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIP_TYPES })
	public List<AccessRelationshipType> getAllAccessRelationshipTypes() throws APIException;
	
	/**
	 * Get AccessRelationshipType by internal identifier
	 * 
	 * @param accessRelationshipTypeId
	 * @return AccessRelationshipType with given internal identifier or null if none found
	 * @throws APIException
	 * @should return AccessRelationship type with the given accessRelationship type id
	 * @should return null when no access relationship type matches given access relationship type id
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIP_TYPES })
	public AccessRelationshipType getAccessRelationshipType(Integer accessRelationshipTypeId) throws APIException;
	
	/**
	 * Gets the AccessRelationship type with the given uuid.
	 * 
	 * @param uuid
	 * @return
	 * @throws APIException
	 * @should find object given valid uuid
	 * @should return null if no object found with given uuid
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIP_TYPES })
	public AccessRelationshipType getAccessRelationshipTypeByUuid(String uuid) throws APIException;
	
	/**
	 * Find AccessRelationshipType by exact name match
	 * 
	 * @param accessRelationshipTypeName name to match on
	 * @return AccessRelationshipType with given name or null if none found
	 * @throws APIException
	 * @should return null when no AccessRelationship type match the given name
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIP_TYPES })
	public AccessRelationshipType getAccessRelationshipTypeByName(String accessRelationshipTypeName) throws APIException;
	
	/**
	 * Get AccessRelationshipTypes by searching through the names and loosely matching to the given
	 * searchString
	 * 
	 * @param searchString string to match to a AccessRelationship type name
	 * @return list of AccessRelationship types or empty list if none found
	 * @throws APIException
	 * @should return empty list when no AccessRelationship type match the search string
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIP_TYPES })
	public List<AccessRelationshipType> getAccessRelationshipTypes(String searchString) throws APIException;
	
	/**
	 * Inserts or updates the given AccessRelationship type object in the database
	 * 
	 * @param accessRelationshipType type to be created or updated
	 * @return AccessRelationship type that was created or updated
	 * @throws APIException
	 * @should create new object when AccessRelationship type id is null
	 * @should update existing object when AccessRelationship type id is not null
	 */
	@Authorized( { PrivilegeConstants.MANAGE_RELATIONSHIP_TYPES })
	public AccessRelationshipType saveAccessRelationshipType(AccessRelationshipType accessRelationshipType)
	        throws APIException;
	
	/**
	 * Purge AccessRelationship type from the database (cannot be undone)
	 * 
	 * @param accessRelationshipType AccessRelationship type to be purged
	 * @throws APIException
	 * @should delete AccessRelationship type from the database
	 */
	@Authorized( { PrivilegeConstants.PURGE_RELATIONSHIP_TYPES })
	public void purgeAccessRelationshipType(AccessRelationshipType accessRelationshipType) throws APIException;
	
	/**
	 * Get all AccessRelationships for a given type of AccessRelationship mapped from the personA to all of the
	 * personB's
	 * 
	 * @param accessRelationshipType type of AccessRelationship for which to retrieve all AccessRelationships
	 * @return all AccessRelationships for the given type of AccessRelationship
	 * @throws APIException
	 * @should return empty map when no AccessRelationship has the matching AccessRelationship type
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.GET_RELATIONSHIPS })
	public Map<Person, List<Person>> getAccessRelationshipMap(AccessRelationshipType accessRelationshipType)
	        throws APIException;
	
}
