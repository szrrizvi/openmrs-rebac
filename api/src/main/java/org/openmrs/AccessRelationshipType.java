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

/**
 * Defines a type of access control relationship between two persons in the database.
 *
 */
public class AccessRelationshipType extends BaseOpenmrsMetadata implements java.io.Serializable {
	
	public static final long serialVersionUID = 1L;
	
	// Fields
	
	private Integer accessRelationshipTypeId;
	
	private String name;
	
	/**
	 * Default constructor
	 */
	public AccessRelationshipType() {
	}
	
	/**
	 * Constructor. Initializes the fields
	 */
	public AccessRelationshipType(String name, String description, Program program, ProgramWorkflow workflow) {
		
		this.name = name;
		setDescription(description);
	}
	
	public AccessRelationshipType(String name, String description) {
		
		this.name = name;
		setDescription(description);
	}
	
	/**
	 * Compares two objects for equality
	 * 
	 * @param obj AccessRelationshipType to compare to this object
	 * @return boolean true/false whether or not they are the same objects
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (accessRelationshipTypeId != null && obj instanceof AccessRelationshipType) {
			AccessRelationshipType relType = (AccessRelationshipType) obj;
			return (accessRelationshipTypeId.equals(relType.getAccessRelationshipTypeId()));
		}
		return false;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (this.getAccessRelationshipTypeId() == null)
			return super.hashCode();
		return this.getAccessRelationshipTypeId().hashCode();
	}
	
	/**
	 * @return the accessRelationshipTypeId
	 */
	public Integer getAccessRelationshipTypeId() {
		return accessRelationshipTypeId;
	}
	
	/**
	 * @param accessRelationshipTypeId the access relationship type Id to set
	 */
	public void setAccessRelationshipTypeId(Integer accessRelationshipTypeId) {
		this.accessRelationshipTypeId = accessRelationshipTypeId;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the the access relationship id number
	 */
	public Integer getId() {
		return getAccessRelationshipTypeId();
	}
	
	/**
	 * @param id the access relationship id number
	 */
	public void setId(Integer id) {
		setAccessRelationshipTypeId(id);
	}
	
	/**
	 * @return The name
	 */
	public String toString() {
		return getName();
	}
}
