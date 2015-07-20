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

import org.openmrs.AccessRelationshipType;
import org.openmrs.RelationshipType;

import ca.ucalgary.ispia.rebac.Diamond;
import ca.ucalgary.ispia.rebac.Direction;

/**
 * Concrete implementation of the Diamond variant, for ReBAC policy formula
 */
public class DiamondImpl extends PolicyImpl implements Diamond {
	
	public static final long serialVersionUID = 1L;
	
	/**
	 * The policy to relate the requestor.
	 */
	private PolicyImpl policy;
	
	/**
	 * The relationship direction indicator.
	 */
	private String directionStr;
	
	/**
	 * The relation identifiers (1 and only 1 must be non-null)
	 */
	private String implicitRelId;
	
	private AccessRelationshipType explicitRelId;
	
	private RelationshipType patientRecordRelId;
	
	/**
	 * Default Constructor
	 */
	public DiamondImpl() {
	}
	
	/**
	 * Initializes field members.<p>
	 * @param policy The policy to relate the requestor.
	 * @param implicitRelId The relation identifier.
	 * @param direction The indicator for the direction of relationship.
	 */
	public DiamondImpl(PolicyImpl policy, String implicitRelId, Direction direction) {
		this.policy = policy;
		this.implicitRelId = implicitRelId;
		
		switch (direction) {
			case FORWARD:
				this.directionStr = "forward";
				break;
			case BACKWARD:
				this.directionStr = "backward";
				break;
			case EITHER:
				this.directionStr = "either";
				break;
		}
	}
	
	/**
	 * Initializes field members.<p>
	 * @param policy The policy to relate the requestor.
	 * @param explicitRelId The relation identifier.
	 * @param direction The indicator for the direction of relationship.
	 */
	public DiamondImpl(PolicyImpl policy, AccessRelationshipType explicitRelId, Direction direction) {
		this.policy = policy;
		this.explicitRelId = explicitRelId;
		
		switch (direction) {
			case FORWARD:
				this.directionStr = "forward";
				break;
			case BACKWARD:
				this.directionStr = "backward";
				break;
			case EITHER:
				this.directionStr = "either";
				break;
		}
	}
	
	/**
	 * Initializes field members.<p>
	 * @param policy The policy to relate the requestor.
	 * @param patientRecordRelId The relation identifier.
	 * @param direction The indicator for the direction of relationship.
	 */
	public DiamondImpl(PolicyImpl policy, RelationshipType patientRecordRelId, Direction direction) {
		this.policy = policy;
		this.patientRecordRelId = patientRecordRelId;
		
		switch (direction) {
			case FORWARD:
				this.directionStr = "forward";
				break;
			case BACKWARD:
				this.directionStr = "backward";
				break;
			case EITHER:
				this.directionStr = "either";
				break;
		}
	}
	
	/**
	 * @return the policy
	 */
	@Override
	public PolicyImpl getPolicy() {
		return policy;
	}
	
	/**
	 * @param policy the policy to set
	 */
	public void setPolicy(PolicyImpl policy) {
		this.policy = policy;
	}
	
	/**
	 * @return the direction
	 */
	@Override
	public Direction getDirection() {
		if (directionStr.equals("forward")) {
			return Direction.FORWARD;
		} else if (directionStr.equals("backward")) {
			return Direction.BACKWARD;
		} else if (directionStr.equals("either")) {
			return Direction.EITHER;
		} else {
			return null;
		}
	}
	
	/**
	 * @return the directionStr
	 */
	public String getDirectionStr() {
		return directionStr;
	}
	
	/**
	 * @param directionStr the directionStr to set
	 */
	public void setDirectionStr(String directionStr) throws IllegalRebacPolicyException {
		
		if (!(directionStr.equals("forward")) && !(directionStr.equals("backward")) && !(directionStr.equals("either"))) {
			throw new IllegalRebacPolicyException("Illegal direction: " + directionStr);
		}
		
		this.directionStr = directionStr;
		
	}
	
	/**
	 * @return the implicitRelId
	 */
	public String getImplicitRelId() {
		return implicitRelId;
	}
	
	/**
	 * @param implicitRelId the implicitRelId to set
	 */
	public void setImplicitRelId(String implicitRelId) {
		this.implicitRelId = implicitRelId;
	}
	
	/**
	 * @return the explicitRelId
	 */
	public AccessRelationshipType getExplicitRelId() {
		return explicitRelId;
	}
	
	/**
	 * @param explicitRelId the explicitRelId to set
	 */
	public void setExplicitRelId(AccessRelationshipType explicitRelId) {
		this.explicitRelId = explicitRelId;
	}
	
	/**
	 * @return the patientRecordRelId
	 */
	public RelationshipType getPatientRecordRelId() {
		return patientRecordRelId;
	}
	
	/**
	 * @param patientRecordRelId the patientRecordRelId to set
	 */
	public void setPatientRecordRelId(RelationshipType patientRecordRelId) {
		this.patientRecordRelId = patientRecordRelId;
	}
	
	@Override
	public Object getRelationIdentifier() {
		
		if (implicitRelId != null) {
			return implicitRelId;
		} else if (explicitRelId != null) {
			return explicitRelId;
		} else {
			return patientRecordRelId;
		}
	}
	
	/**
	 * Prints out a readable version of variant.
	 */
	@Override
	public String toString() {
		
		String str = "";
		
		str = str + ("(<");
		
		Direction direction = getDirection();
		
		switch (direction) {
			case FORWARD:
				break;
			case BACKWARD:
				str = str + ("-");
				break;
			case EITHER:
				str = str + ("%");
				break;
		}
		
		str = str + (getRelationIdentifier() + "> ");
		str = str + policy; // print contained policy
		str = str + (")");
		
		return str;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + "Diamond".hashCode();
		result = prime * result + ((directionStr == null) ? 0 : directionStr.hashCode());
		result = prime * result + ((policy == null) ? 0 : policy.hashCode());
		result = prime * result + ((getRelationIdentifier() == null) ? 0 : getRelationIdentifier().hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Compare pointers
		}
		if (obj == null) {
			return false; // False if obj is null
		}
		if (!(obj instanceof DiamondImpl)) {
			return false; // False is obj is not of same instance
		}
		DiamondImpl other = (DiamondImpl) obj;
		if (directionStr != other.getDirectionStr()) {
			return false; // False if direction does not match
		}
		if (policy == null) {
			if (other.policy != null) {
				return false; // False if contained policy is null, and obj's policy is not null
			}
		} else if (!policy.equals(other.getPolicy())) {
			return false; // False if contained policy does not match
		}
		if (getRelationIdentifier() == null) {
			if (other.getRelationIdentifier() != null) {
				return false; // False if contained relation identifier is null, and obj's relation identifier is not null
			}
		} else if (!getRelationIdentifier().equals(other.getRelationIdentifier())) {
			return false; // False if relationIdentifier does not match
		}
		return true; // If reached here, true
	}
	
	public PolicyImpl copy() {
		PolicyImpl temp = policy.copy();
		
		if (implicitRelId != null) {
			return new DiamondImpl(temp, new String(implicitRelId), getDirection());
		} else if (explicitRelId != null) {
			return new DiamondImpl(temp, explicitRelId, getDirection());
		} else {
			return new DiamondImpl(temp, patientRecordRelId, getDirection());
		}
	}
	
}
