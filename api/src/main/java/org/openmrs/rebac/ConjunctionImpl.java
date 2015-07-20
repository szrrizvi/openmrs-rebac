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

import ca.ucalgary.ispia.rebac.Conjunction;

/**
 * The concrete implementation of the Conjuction variant, for ReBAC policy formula
 */
public class ConjunctionImpl extends PolicyImpl implements Conjunction {
	
	public static final long serialVersionUID = 1L;
	
	/**
	 * The first policy to be considered.
	 */
	private PolicyImpl policyA;
	
	/**
	 * The second policy to be considered.
	 */
	private PolicyImpl policyB;
	
	/**
	 * Default Constructor
	 */
	public ConjunctionImpl() {
	}
	
	/**
	 * Initializes the field members.
	 * @param policyA The first policy to be considered.
	 * @param policyB The second policy to  be considered.
	 */
	public ConjunctionImpl(PolicyImpl policyA, PolicyImpl policyB) {
		this.policyA = policyA;
		this.policyB = policyB;
	}
	
	/**
	 * @return the policyA
	 */
	@Override
	public PolicyImpl getPolicyA() {
		return policyA;
	}
	
	/**
	 * @param policyA the policyA to set
	 */
	public void setPolicyA(PolicyImpl policyA) {
		this.policyA = policyA;
	}
	
	/**
	 * @return the policyB
	 */
	@Override
	public PolicyImpl getPolicyB() {
		return policyB;
	}
	
	/**
	 * @param policyB the policyB to set
	 */
	public void setPolicyB(PolicyImpl policyB) {
		this.policyB = policyB;
	}
	
	/**
	 * Prints out a readable version of variant.
	 */
	@Override
	public String toString() {
		String str = "";
		
		str = str + ("(");
		str = str + policyA; // print contained policyA
		str = str + (" and ");
		str = str + policyB; // print contained policyB
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
		result = prime * result + "Conjunction".hashCode();
		result = prime * result + ((policyA == null) ? 0 : policyA.hashCode());
		result = prime * result + ((policyB == null) ? 0 : policyB.hashCode());
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
		if (!(obj instanceof ConjunctionImpl)) {
			return false; // False if obj is not of same instance
		}
		ConjunctionImpl other = (ConjunctionImpl) obj;
		if (policyA == null) {
			if (other.policyA != null) {
				return false; // False if contained policyA is null, and obj's policyA is not null
			}
		} else if (!policyA.equals(other.getPolicyA())) {
			return false; // False if policyA does not match
		}
		if (policyB == null) {
			if (other.policyB != null) {
				return false; // False if contained policyB is null, and obj's policyB is not null 
			}
		} else if (!policyB.equals(other.getPolicyB())) {
			return false; // False if policyB does not match
		}
		return true; // If reached here, true
	}
	
	public PolicyImpl copy() {
		PolicyImpl tempA = policyA.copy();
		PolicyImpl tempB = policyB.copy();
		
		return new ConjunctionImpl(tempA, tempB);
	}
	
}
