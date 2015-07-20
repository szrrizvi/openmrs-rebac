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
package org.openmrs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Relationship for access control, not to be used as patient record.
 */
public class AccessRelationship extends BaseOpenmrsData implements java.io.Serializable {
	
	public static final long serialVersionUID = 1L;
	
	// Fields 
	
	private Integer accessRelationshipId;
	
	private Person personA;
	
	private Person personB;
	
	private AccessRelationshipType accessRelationshipType;
	
	private Date startDate;
	
	private Date endDate;
	
	/**
	 * Default constructor
	 */
	public AccessRelationship() {
	}
	
	/**
	 * Constructor to initialize
	 * @param personA
	 * @param personB
	 * @param type
	 */
	public AccessRelationship(Person personA, Person personB, AccessRelationshipType type) {
		
		this.personA = personA;
		this.personB = personB;
		this.accessRelationshipType = type;
	}
	
	/**
	 * Compares two objects for similarity
	 * 
	 * @param obj
	 * @return boolean true/false whether or not they are the same objects
	 */
	public boolean equals(Object obj) {
		
		// Compare by ID number
		if (obj instanceof AccessRelationship) {
			AccessRelationship rel = (AccessRelationship) obj;
			return (this.getAccessRelationshipId().equals(rel.getAccessRelationshipId()));
		}
		return false;
	}
	
	/**
	 * @return the hash code for the AccessRelationship object
	 */
	public int hashCode() {
		// Hash the ID number
		if (this.getAccessRelationshipId() == null)
			return super.hashCode();
		return this.getAccessRelationshipId().hashCode();
	}
	
	/**
	 * @return the accessRelationshipId
	 */
	public Integer getAccessRelationshipId() {
		return accessRelationshipId;
	}
	
	/**
	 * @param accessRelationshipId the access relationship Id to set
	 */
	public void setAccessRelationshipId(Integer accessRelationshipId) {
		this.accessRelationshipId = accessRelationshipId;
	}
	
	/**
	 * @return the personA
	 */
	public Person getPersonA() {
		return personA;
	}
	
	/**
	 * @param personA the personA to set
	 */
	public void setPersonA(Person personA) {
		this.personA = personA;
	}
	
	/**
	 * @return the accessRelationshipType
	 */
	public AccessRelationshipType getAccessRelationshipType() {
		return accessRelationshipType;
	}
	
	/**
	 * @param accessRelationshipType the access relationship type to set
	 */
	public void setAccessRelationshipType(AccessRelationshipType accessRelationshipType) {
		this.accessRelationshipType = accessRelationshipType;
	}
	
	/**
	 * @return the personB
	 */
	public Person getPersonB() {
		return personB;
	}
	
	/**
	 * @param personB the personB to set
	 */
	public void setPersonB(Person personB) {
		this.personB = personB;
	}
	
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * @since 1.5
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	public Integer getId() {
		return getAccessRelationshipId();
	}
	
	/**
	 * @since 1.5
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	public void setId(Integer id) {
		setAccessRelationshipId(id);
	}
	
	/**
	 * Returns a list of owners for the resource.
	 * The resource owners are the 2 Persons involved in the AccessRelationship
	 */
	@Override
	public List<Person> getResourceOwners() {
		
		List<Person> owners = new ArrayList<Person>();
		owners.add(personA);
		owners.add(personB);
		
		return owners;
	}
}
