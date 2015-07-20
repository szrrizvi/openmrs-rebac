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

import ca.ucalgary.ispia.rebac.Requestor;

/**
 * The concrete implementation of the Requestor variant, for ReBAC policy formula
 */
public class RequestorImpl extends PolicyImpl implements Requestor {
	
	public static final long serialVersionUID = 1L;
	
	public static final int hashCode;
	
	/**
	 * Default Constructor
	 */
	public RequestorImpl() {
		
	}
	
	/**
	 *Prints out a readable version of variant.
	 */
	@Override
	public String toString() {
		return "Requestor";
	}
	
	/**
	 * @return variable "req"
	 */
	@Override
	public String getVariable() {
		return "req";
	}
	
	static {
		final int prime = 31;
		int result = 1;
		hashCode = prime * result + "RequestorImpl".hashCode();
	}
	
	public int hashCode() {
		return hashCode;
	}
	
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		
		if (obj instanceof RequestorImpl) {
			return true;
		}
		
		return false;
	}
	
	public PolicyImpl copy() {
		return new RequestorImpl();
	}
}
