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

package org.openmrs.action;

import java.io.Serializable;

import org.openmrs.AccessRelationshipType;
import org.openmrs.BaseOpenmrsMetadata;

/**
 * This class represents a single transaction to occur when the associated Action is executed.
 * The transaction could be, adding or removing an AccessRelationship.
 */
public class Effect extends BaseOpenmrsMetadata implements Serializable {
	
	public enum EffectType {
		ADD, REMOVE
	}
	
	public static final long serialVersionUID = 1L;
	
	// Fields
	
	private Integer effectId;
	
	private Action action; // The associated Action
	
	// The participants
	private Participant participantA;
	
	private Participant participantB;
	
	private String effectType; // The type of effect, add or remove relationship
	
	private AccessRelationshipType accessRelationshipType; // The relationship identifier
	
	/**
	 * Default constructor
	 */
	public Effect() {
	}
	
	/**
	 * Constructor to instantiate the object. Specified the context to be of type Program
	 * @param participantA
	 * @param participantB
	 * @param effectType
	 * @param accessRelationshipType
	 * @param contextVertex
	 */
	public Effect(Participant participantA, Participant participantB, EffectType effectType,
	    AccessRelationshipType accessRelationshipType) {
		
		this.participantA = participantA; //Set participant A
		this.participantB = participantB; //Set participant B
		this.accessRelationshipType = accessRelationshipType; //Set the access relationship type
		
		switch (effectType) { // Set the effect type
			case ADD:
				this.effectType = "add";
				break;
			case REMOVE:
				this.effectType = "remove";
				break;
		}
	}
	
	// Field accessors and mutators
	
	/**
	 * @return the effectId
	 */
	public Integer getEffectId() {
		return effectId;
	}
	
	/**
	 * @param effectId the effectId to set
	 */
	public void setEffectId(Integer effectId) {
		this.effectId = effectId;
	}
	
	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * @param action the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}
	
	/**
	 * @return the participantA
	 */
	public Participant getParticipantA() {
		return participantA;
	}
	
	/**
	 * @param participantA the participantA to set
	 */
	public void setParticipantA(Participant participantA) {
		this.participantA = participantA;
	}
	
	/**
	 * @return the participantB
	 */
	public Participant getParticipantB() {
		return participantB;
	}
	
	/**
	 * @param participantB the participantB to set
	 */
	public void setParticipantB(Participant participantB) {
		this.participantB = participantB;
	}
	
	/**
	 * @return the effectType
	 */
	public String getEffectType() {
		return effectType;
	}
	
	/**
	 * @param effectType the effectType to set
	 */
	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}
	
	/**
	 * @return the accessRelationshipType
	 */
	public AccessRelationshipType getAccessRelationshipType() {
		return accessRelationshipType;
	}
	
	/**
	 * @param accessRelationshipType the accessRelationshipType to set
	 */
	public void setAccessRelationshipType(AccessRelationshipType accessRelationshipType) {
		this.accessRelationshipType = accessRelationshipType;
	}
	
	/**
	 * @return The id
	 */
	public Integer getId() {
		return getEffectId();
	}
	
	/**
	 * @param id The id to set
	 */
	public void setId(Integer id) {
		setEffectId(id);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Effect [effectId=" + effectId + ", participantA=" + participantA + ", participantB=" + participantB
		        + ", effectType=" + effectType + ", accessRelationshipType=" + accessRelationshipType + "]";
	}
	
	/**
	 * Returns a hash value of the object
	 * @return The hash value of the object
	 */
	public int hashCode() {
		//Auto-Generated by eclipse
		
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((accessRelationshipType == null) ? 0 : accessRelationshipType.hashCode());
		result = prime * result + ((effectId == null) ? 0 : effectId.hashCode());
		result = prime * result + ((effectType == null) ? 0 : effectType.hashCode());
		result = prime * result + ((participantA == null) ? 0 : participantA.hashCode());
		result = prime * result + ((participantB == null) ? 0 : participantB.hashCode());
		return result;
	}
	
	/**
	 * Indicates whether the given object is "equal to" this one.
	 * @return If the given object is equal to this one, else false.
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Effect other = (Effect) obj;
		
		if (accessRelationshipType == null) {
			if (other.accessRelationshipType != null) {
				return false;
			}
		} else if (!accessRelationshipType.equals(other.accessRelationshipType)) {
			return false;
		}
		
		if (effectId == null) {
			if (other.effectId != null) {
				return false;
			}
		} else if (!effectId.equals(other.effectId)) {
			return false;
		}
		
		if (effectType == null) {
			if (other.effectType != null) {
				return false;
			}
		} else if (!effectType.equals(other.effectType)) {
			return false;
		}
		
		if (participantA == null) {
			if (other.participantA != null) {
				return false;
			}
		} else if (!participantA.equals(other.participantA)) {
			return false;
		}
		
		if (participantB == null) {
			if (other.participantB != null) {
				return false;
			}
		} else if (!participantB.equals(other.participantB)) {
			return false;
		}
		return true;
	}
}
