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
package org.openmrs;

import org.openmrs.rebac.PolicyImpl;

public class AuthorizationPrincipal extends BaseOpenmrsMetadata implements java.io.Serializable {
	
	public static final long serialVersionUID = 1L;
	
	private String authorizationPrincipal;
	
	private PolicyImpl policy;
	
	/**
	 *  Default constructor
	 */
	public AuthorizationPrincipal() {
	}
	
	/**
	 * Constructor to initialize fields
	 * @param authorizationPrincipal
	 * @param policy
	 */
	public AuthorizationPrincipal(String authorizationPrincipal, PolicyImpl policy) {
		this.authorizationPrincipal = authorizationPrincipal;
		this.policy = policy;
	}
	
	/**
	 * Copy Constructor
	 * @param ap
	 */
	public AuthorizationPrincipal(AuthorizationPrincipal ap) {
		this.authorizationPrincipal = new String(ap.getAuthorizationPrincipal());
		this.policy = ap.getPolicy().copy();
	}
	
	/**
	 * @param authorizationPrincipal The name of AuthorizationPrincipal to set
	 */
	public void setAuthorizationPrincipal(String authorizationPrincipal) {
		this.authorizationPrincipal = authorizationPrincipal;
	}
	
	/**
	 * @return the authorization principal name
	 */
	public String getAuthorizationPrincipal() {
		return this.authorizationPrincipal;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		setAuthorizationPrincipal(name);
	}
	
	/**
	 * @return the policy
	 */
	public PolicyImpl getPolicy() {
		return policy;
	}
	
	/**
	 * @param policy the policy to set
	 */
	public void setPolicy(PolicyImpl policy) {
		this.policy = policy;
	}
	
	@Override
	public Integer getId() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setId(Integer id) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((authorizationPrincipal == null) ? 0 : authorizationPrincipal.hashCode());
		result = prime * result + ((policy == null) ? 0 : policy.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthorizationPrincipal other = (AuthorizationPrincipal) obj;
		if (authorizationPrincipal == null) {
			if (other.authorizationPrincipal != null)
				return false;
		} else if (!authorizationPrincipal.equals(other.authorizationPrincipal))
			return false;
		if (policy == null) {
			if (other.policy != null)
				return false;
		} else if (!policy.equals(other.policy))
			return false;
		return true;
	}
	
}
