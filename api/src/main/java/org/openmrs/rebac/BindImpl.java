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

import ca.ucalgary.ispia.rebac.Bind;

/**
 * A concrete implementation of {@link Bind}.
 * @see ca.ucalgary.ispia.rebac.Bind
 */
public class BindImpl extends PolicyImpl implements Bind {
	
	public static final long serialVersionUID = 1L;
	
	/**
	 * the variable that is being binded to a state
	 */
	private String variable;
	
	/**
	 * The policy to relate the accessor.
	 */
	private PolicyImpl policy;
	
	/**
	 * Default Constructor
	 */
	public BindImpl() {
		
	}
	
	/**
	 * Iniatializes field members
	 * @param policy: The policy to relate the requestor.
	 * @param variable: the variable going to be binded to the current state in the graph at 
	 * the time of model checking.
	 */
	public BindImpl(String variable, PolicyImpl policy) {
		
		this.policy = policy;
		this.variable = variable;
		
	}
	
	/**

	 * @return The policy to relate the accessor.

	 * @see ca.ucalgary.ispia.rebac.Bind#getPolicy()

	 */
	
	@Override
	public PolicyImpl getPolicy() {
		
		return this.policy;
		
	}
	
	/**

	 * @return The variable that is being binded to a state

	 * @see ca.ucalgary.ispia.rebac.Bind#getVariable()

	 */
	@Override
	public String getVariable() {
		
		return this.variable;
		
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public void setPolicy(PolicyImpl policy) {
		this.policy = policy;
	}
	
	@Override
	public String toString() {
		
		String str = "";
		str = str + "(!";
		
		str = str + variable;
		str = str + policy;
		
		str = str + ")";
		return str;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + "Bind".hashCode();
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
		if (!(obj instanceof BindImpl)) {
			return false; // obj and this are not of the same instance
		}
		BindImpl other = (BindImpl) obj;
		
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
		return new BindImpl(new String(variable), temp);
	}
	
}
