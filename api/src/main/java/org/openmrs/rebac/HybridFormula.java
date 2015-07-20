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

import java.util.Set;

import org.openmrs.BaseOpenmrsMetadata;

public class HybridFormula extends BaseOpenmrsMetadata implements java.io.Serializable {
	
	public static final long serialVersionUID = 1L;
	
	private Integer hybridFormulaId; // The id
	
	private String hybridFormula; // The name
	
	private PolicyImpl policyImpl; // The contain policy
	
	private Set<String> variableNames; // Set of free variable names
	
	/**
	 * Default constructor
	 */
	public HybridFormula() {
	}
	
	/**
	 * Constructor to initialize fields
	 * @param hybridFormula The name
	 * @param policy The policy
	 * @param freeVariables The set of free variable names
	 */
	public HybridFormula(String hybridFormula, PolicyImpl policyImpl, Set<String> variableNames) {
		this.hybridFormula = hybridFormula;
		this.policyImpl = policyImpl;
		this.variableNames = variableNames;
	}
	
	// Setters and getters
	
	public Integer getHybridFormulaId() {
		return hybridFormulaId;
	}
	
	public void setHybridFormulaId(Integer hybridFormulaId) {
		this.hybridFormulaId = hybridFormulaId;
	}
	
	public void setName(String name) {
		this.hybridFormula = name;
	}
	
	public String getName() {
		return this.hybridFormula;
	}
	
	public String getHybridFormula() {
		return hybridFormula;
	}
	
	public void setHybridFormula(String hybridFormula) {
		this.hybridFormula = hybridFormula;
	}
	
	public PolicyImpl getPolicyImpl() {
		return policyImpl;
	}
	
	public void setPolicyImpl(PolicyImpl policyImpl) {
		this.policyImpl = policyImpl;
	}
	
	public Set<String> getVariableNames() {
		return this.variableNames;
	}
	
	public void setVariableNames(Set<String> variableNames) {
		this.variableNames = variableNames;
	}
	
	@Override
	public Integer getId() {
		return getHybridFormulaId();
	}
	
	@Override
	public void setId(Integer id) {
		setHybridFormulaId(id);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((variableNames == null) ? 0 : variableNames.hashCode());
		result = prime * result + ((hybridFormula == null) ? 0 : hybridFormula.hashCode());
		result = prime * result + ((hybridFormulaId == null) ? 0 : hybridFormulaId.hashCode());
		result = prime * result + ((policyImpl == null) ? 0 : policyImpl.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HybridFormula other = (HybridFormula) obj;
		if (variableNames == null) {
			if (other.variableNames != null)
				return false;
		} else if (!variableNames.equals(other.variableNames))
			return false;
		if (hybridFormula == null) {
			if (other.hybridFormula != null)
				return false;
		} else if (!hybridFormula.equals(other.hybridFormula))
			return false;
		if (hybridFormulaId == null) {
			if (other.hybridFormulaId != null)
				return false;
		} else if (!hybridFormulaId.equals(other.hybridFormulaId))
			return false;
		if (policyImpl == null) {
			if (other.policyImpl != null)
				return false;
		} else if (!policyImpl.equals(other.policyImpl))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "HybridFormula [hybridFormulaId=" + hybridFormulaId + ", hybridFormula=" + hybridFormula + ", policyImpl="
		        + policyImpl + ", variableNames=" + variableNames + "]";
	}
	
}
