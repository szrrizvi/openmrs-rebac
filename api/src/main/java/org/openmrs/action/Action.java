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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.rebac.HybridFormula;

/**
 * The Action class allows administrative users to define a set of transactions, for adding and/or removing access relationships 
 * that are used for the ReBAC authorization. An Action contains the following properties;
 * Name: To identify the Action.
 * Availability Precondition: A ReBAC policy (Hybrid Formula) to be satisfied between the user (who is executing the Action) and the patient.
 * 							  If this precondition is not satisfied, then the Action will not be available to the user.
 * Participants: The members involved in the execution of the Action. They are chosen when the user is executing the Action.
 * Execution Precondition: A Hybrid Formula, defined by the administrative user that created the Action, to be satisfied
 * 							between the participants of the Action.
 * Effects: The set of transactions to occur when the Action is executed. The possible transactions include, adding and/or removing
 * 			access relationships.
 */

public class Action extends BaseOpenmrsMetadata implements Serializable {
	
	public static final long serialVersionUID = 1L;
	
	// Fields
	private Integer actionId;
	
	private String name; // Name
	
	private HybridFormula hybridFormula; // Availability Precondition
	
	private List<Participant> participants; // Participants
	
	private Precondition precondition; // Execution Precondition
	
	private List<Effect> effects; // Effects
	
	/**
	 * Default constructor
	 */
	public Action() {
	}
	
	/**
	 * Constructor to instantiate the object.
	 * @param name
	 * @param authorizationPrincipal
	 * @param participants
	 * @param precondition
	 * @param effects
	 */
	public Action(String name, HybridFormula hybridFormula, List<Participant> participants, Precondition precondition,
	    List<Effect> effects) {
		//Assign the fields
		this.name = name;
		this.hybridFormula = hybridFormula;
		this.precondition = precondition;
		
		//Instantiate the lists
		this.participants = new ArrayList<Participant>();
		this.effects = new ArrayList<Effect>();
		
		//Populate the lists
		addAllParticipants(participants);
		addAllEffects(effects);
	}
	
	/**
	 * Adds the given Participant to the list of Participants of the Action
	 * @param participant The participant to add to the list
	 */
	public void addParticipant(Participant participant) {
		participant.setAction(this); // Set the Action for the participant (bi-directional relationship)
		participants.add(participant); // Add the participant to the list
	}
	
	/**
	 * Adds the given list of Participants to the list of Participants of the Action
	 * @param participants The list of participants to add to the list of participants of the Action
	 */
	public void addAllParticipants(List<Participant> participants) {
		
		// Iterate through each member of the given list, and add it to the main list
		Iterator<Participant> itr = participants.iterator();
		while (itr.hasNext()) {
			addParticipant(itr.next());
		}
	}
	
	/**
	 * Adds the given Effect to the list of Effects of the Action
	 * @param effect The effect to add to the list
	 */
	public void addEffect(Effect effect) {
		effect.setAction(this); // Set the Action for the effect (bi-directional relationship)
		effects.add(effect); // Add the effect to the list
	}
	
	/**
	 * Adds the given list of Effects to the list of Effects of the Action
	 * @param effects The list of effects to add to the list of effects of the Action
	 */
	public void addAllEffects(List<Effect> effects) {
		// Iterate through each member of the given list, and add it to the main list
		Iterator<Effect> itr = effects.iterator();
		while (itr.hasNext()) {
			addEffect(itr.next());
		}
	}
	
	// Field Accessors
	
	/**
	 * @return the actionId
	 */
	public Integer getActionId() {
		return actionId;
	}
	
	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
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
	 * @return the hybridFormula
	 */
	public HybridFormula getHybridFormula() {
		return hybridFormula;
	}
	
	/**
	 * @param hybridFormula the hybridFormula to set
	 */
	public void setHybridFormula(HybridFormula hybridFormula) {
		this.hybridFormula = hybridFormula;
	}
	
	/**
	 * @return the participants
	 */
	public List<Participant> getParticipants() {
		return participants;
	}
	
	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	
	/**
	 * @return the precondition
	 */
	public Precondition getPrecondition() {
		return precondition;
	}
	
	/**
	 * @param precondition the precondition to set
	 */
	public void setPrecondition(Precondition precondition) {
		this.precondition = precondition;
	}
	
	/**
	 * @return the effects
	 */
	public List<Effect> getEffects() {
		return effects;
	}
	
	/**
	 * @param effects the effects to set
	 */
	public void setEffects(List<Effect> effects) {
		this.effects = effects;
	}
	
	@Override
	public Integer getId() {
		return getActionId();
	}
	
	@Override
	public void setId(Integer id) {
		setActionId(id);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Action [actionId=" + actionId + ", name=" + name + ", hybridFormula=" + hybridFormula + ", participants="
		        + participants + ", precondition=" + precondition + ", effects=" + effects + "]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((effects == null) ? 0 : effects.hashCode());
		result = prime * result + ((hybridFormula == null) ? 0 : hybridFormula.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((participants == null) ? 0 : participants.hashCode());
		result = prime * result + ((precondition == null) ? 0 : precondition.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Action)) {
			return false;
		}
		Action other = (Action) obj;
		if (actionId == null) {
			if (other.actionId != null) {
				return false;
			}
		} else if (!actionId.equals(other.actionId)) {
			return false;
		}
		if (effects == null) {
			if (other.effects != null) {
				return false;
			}
		} else if (!effects.equals(other.effects)) {
			return false;
		}
		if (hybridFormula == null) {
			if (other.hybridFormula != null) {
				return false;
			}
		} else if (!hybridFormula.equals(other.hybridFormula)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (participants == null) {
			if (other.participants != null) {
				return false;
			}
		} else if (!participants.equals(other.participants)) {
			return false;
		}
		if (precondition == null) {
			if (other.precondition != null) {
				return false;
			}
		} else if (!precondition.equals(other.precondition)) {
			return false;
		}
		return true;
	}
	
}
