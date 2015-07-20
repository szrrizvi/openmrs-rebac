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

/**
 * Exception to be thrown when the ReBAC policy is not created properly.
 */
public class IllegalRebacPolicyException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default empty constructor. It is more common to use the
	 * {@link #IllegalRebacPolicyException(String)} constructor to provide some context to the user
	 * as to where/why the ReBAC policy is illegal
	 */
	public IllegalRebacPolicyException() {
		super();
	}
	
	/**
	 * Common constructor taking in a message to give the user some context as to where/why the
	 * ReBAC policy is illegal
	 * 
	 * @param message String describing where/why the ReBAC policy is illegal
	 */
	public IllegalRebacPolicyException(String message) {
		super(message);
	}
	
	/**
	 * Common constructor taking in a message to give the user some context as to where/why the
	 * ReBAC policy is illegal.
	 * 
	 * @param message String describing where/why the ReBAC policy is illegal
	 * @param cause error further up the stream that caused this failure
	 */
	public IllegalRebacPolicyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor giving the user a further cause exception reason that caused this
	 * failure
	 * 
	 * @param cause error further up the stream that caused this failure
	 */
	public IllegalRebacPolicyException(Throwable cause) {
		super(cause);
	}
	
}
