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

import ca.ucalgary.ispia.rebac.At;

/**
 * A concrete implementation of {@link At}.
 * @see ca.ucalgary.ispia.rebac.At
 */
public class AtImpl extends PolicyImpl implements At {
	
	public static final long serialVersionUID = 1L;
	
	/**
	 * The policy to relate the accessor.
	 */
	private PolicyImpl policy;
	
	/**
	 * the variable which is the name of the target vertex.
	 */
	private String variable;
	
	/**
	 * Default Constructor
	 */
	public AtImpl() {
		
	}
	
	/**
	 * Initializes the field members
	 * @param policy: the policy to relate the accessor
	 * @param variabel: the name of the target node
	 */
	public AtImpl(String variable, PolicyImpl policy) {
		this.policy = policy;
		this.variable = variable;
	}
	
	@Override
	public PolicyImpl getPolicy() {
		return this.policy;
	}
	
	public void setPolicy(PolicyImpl policy) {
		this.policy = policy;
	}
	
	@Override
	public String getVariable() {
		return this.variable;
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	@Override
	public String toString() {
		String str = "";
		str = str + "(@";
		
		str = str + variable;
		str = str + policy;
		str = str + ")";
		
		return str;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + "At".hashCode();
		result = prime * result + ((variable == null) ? 0 : variable.hashCode());
		result = prime * result + ((policy == null) ? 0 : policy.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Compare pointers
		}
		if (obj == null) {
			return false; // False if obj is null
		}
		if (!(obj instanceof AtImpl)) {
			return false; // obj and this are not of the same instance
		}
		AtImpl other = (AtImpl) obj;
		
		if (policy == null) {
			if (other.policy != null) {
				return false; // False if contained policy is null, and obj's policy is not null
			}
		} else if (!policy.equals(other.policy)) {
			return false; // False if contained policy does not match
		}
		if (variable == null) {
			if (other.variable != null) {
				return false; // False if contained variable is null, and obj's variable is not null
			}
		} else if (!variable.equals(other.variable)) {
			return false; // False if variable does not match
		}
		return true; // if reached here, true
	}
	
	public PolicyImpl copy() {
		PolicyImpl temp = policy.copy();
		return new AtImpl(new String(variable), temp);
	}
	
}
