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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.rebac.HybridFormula;

/**
 * This class represents the precondition to be satisfied before the associated Action can execute.
 * The precondition is a ReBAC policy, to be satisfied between the participants. 
 */

public class Precondition extends BaseOpenmrsMetadata implements Serializable {
	
	public static final long serialVersionUID = 1L;
	
	// Fields
	
	private Integer preconditionId; // The id
	
	private HybridFormula hybridFormula; // The relation to satisfy
	
	/**
	 * Default constructor
	 */
	public Precondition() {
	}
	
	/**
	 * Constructor to instantiate the object.
	 * @param hybridFormula The HybridFormula
	 */
	public Precondition(HybridFormula hybridFormula) {
		this.hybridFormula = hybridFormula;
	}
	
	// Field accessors
	
	/**
	 * @return the preconditionId
	 */
	public Integer getPreconditionId() {
		return preconditionId;
	}
	
	/**
	 * @param preconditionId the preconditionId to set
	 */
	public void setPreconditionId(Integer preconditionId) {
		this.preconditionId = preconditionId;
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
	 * @return The id
	 */
	public Integer getId() {
		return getPreconditionId();
	}
	
	/**
	 * @param id The id to set
	 */
	public void setId(Integer id) {
		setPreconditionId(id);
	}
	
	/**
	 * @return The String representation of the object
	 */
	public String toString() {
		return "Precondition [preconditionId=" + preconditionId + ", hybridFormula=" + hybridFormula + "]";
	}
	
	/**
	 * Computes and returns the hashcode of the object. The value is depended on the fiels.
	 * @return The hashcode of the object
	 */
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((hybridFormula == null) ? 0 : hybridFormula.hashCode());
		result = prime * result + ((preconditionId == null) ? 0 : preconditionId.hashCode());
		return result;
	}
	
	/**
	 * Compares this object to the give object.
	 * @return True if this object is the same as the give object, else false.
	 */
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true; // Check pointer
		}
		if (!super.equals(obj)) {
			return false; // Check the parent
		}
		if (getClass() != obj.getClass()) {
			return false; // Check the class
		}
		
		Precondition other = (Precondition) obj;
		
		// Check field hybridFormula
		if (hybridFormula == null) {
			if (other.hybridFormula != null) {
				return false;
			}
		} else if (!hybridFormula.equals(other.hybridFormula)) {
			return false;
		}
		
		// Check field preconditionId
		if (preconditionId == null) {
			if (other.preconditionId != null) {
				return false;
			}
		} else if (!preconditionId.equals(other.preconditionId)) {
			return false;
		}
		
		return true;
	}
}
