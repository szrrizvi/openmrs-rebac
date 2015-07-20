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

import ca.ucalgary.ispia.rebac.Negation;

/**
 * The concrete implementation of the negation variant, for ReBAC policy formula
 */
public class NegationImpl extends PolicyImpl implements Negation {
	
	public static final long serialVersionUID = 1L;
	
	/**
	 * The policy whose negation is asserted.
	 */
	private PolicyImpl policy;
	
	/**
	 * Default Constructor
	 */
	public NegationImpl() {
	}
	
	/**
	 * Initialize the field members.
	 * @param policy The policy whose negation is asserted.
	 */
	public NegationImpl(PolicyImpl policy) {
		this.policy = policy;
	}
	
	/**
	 * @return the policy
	 */
	@Override
	public PolicyImpl getPolicy() {
		return policy;
	}
	
	public void setPolicy(PolicyImpl policy) {
		this.policy = policy;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + "Negation".hashCode();
		result = prime * result + ((policy == null) ? 0 : policy.hashCode());
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
		if (!(obj instanceof NegationImpl)) {
			return false; // False if obj is not of same instance
		}
		NegationImpl other = (NegationImpl) obj;
		if (policy == null) {
			if (other.policy != null) {
				return false; // False if contained policy is null and obj's policy is not null
			}
		} else if (!policy.equals(other.policy)) {
			return false; // False if policy does not match
		}
		return true; // If reached here, true
	}
	
	/**
	 * Prints out a readable version of variant.
	 */
	@Override
	public String toString() {
		String str = "(not ";
		str = str + policy; // print contained policy
		str = str + ")";
		return str;
	}
	
	public PolicyImpl copy() {
		PolicyImpl temp = policy.copy();
		
		return new NegationImpl(temp);
	}
}
