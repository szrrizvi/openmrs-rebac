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

import ca.ucalgary.ispia.rebac.Variable;

/**
 * A concrete implementation of {@link Virable}.
 * @see ca.ucalgary.ispia.rebac.Variable
 */
public class VariableImpl extends PolicyImpl implements Variable {
	
	public static final long serialVersionUID = 1L;
	
	private String variable;
	
	/**
	 * Default Constructor
	 */
	public VariableImpl() {
		
	}
	
	//public final Object vertex;
	
	public VariableImpl(String variable) {
		this.variable = variable;
	}
	
	@Override
	public String getVariable() {
		
		return this.variable;
		
	}
	
	@Override
	public String toString() {
		
		return variable.toString();
		
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	@Override
	public int hashCode() {
		
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + "variable".hashCode();
		
		result = prime * result + ((variable == null) ? 0 : variable.hashCode());
		
		return result;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			
			return true; // Compare pointers
			
		}
		
		if (obj == null) {
			
			return false;
			
		}
		
		if (!(obj instanceof Variable)) {
			
			return false;
			
		}
		
		Variable other = (Variable) obj;
		
		if (variable == null) {
			
			if (other.getVariable() != null) {
				
				return false; // False if contained proposition is null, and obj's proposition is not null
				
			}
			
		} else if (!(variable.equals(other.getVariable()))) {
			
			return false; // False if proposition does not match
			
		}
		
		return true;
		
	}
	
	@Override
	public PolicyImpl copy() {
		String str = new String(variable);
		
		PolicyImpl temp = new VariableImpl(str);
		return temp;
	}
	
}
