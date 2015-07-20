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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the association between an Authorization Principal and a set of privileges
 */
public class PrivilegeAssignment extends BaseOpenmrsMetadata implements java.io.Serializable {
	
	public static final long serialVersionUID = 1L;
	
	private Integer privilegeAssignmentId;
	
	private AuthorizationPrincipal authorizationPrincipal; // The associated authorization principal
	
	private Set<Privilege> privileges; // The set of privileges
	
	/**
	 * Default Contstructor
	 */
	public PrivilegeAssignment() {
	}
	
	/**
	 * Copy Constructor
	 */
	public PrivilegeAssignment(PrivilegeAssignment pa) {
		this.authorizationPrincipal = new AuthorizationPrincipal(pa.getAuthorizationPrincipal());
		privileges = new HashSet<Privilege>();
		
		for (Privilege p : pa.getPrivileges()) {
			privileges.add(p);
		}
		
	}
	
	/**
	 * Constructor to initilize the fields
	 * @param authorizationPrincipal The associated authorization principal
	 * @param privileges The set of privileges
	 */
	public PrivilegeAssignment(AuthorizationPrincipal authorizationPrincipal, Set<Privilege> privileges) {
		this.authorizationPrincipal = authorizationPrincipal;
		this.privileges = privileges;
	}
	
	/**
	 * @return the privilegeAssignmentId
	 */
	public Integer getPrivilegeAssignmentId() {
		return privilegeAssignmentId;
	}
	
	/**
	 * @param privilegeAssignmentId the privilegeAssignmentId to set
	 */
	public void setPrivilegeAssignmentId(Integer privilegeAssignmentId) {
		this.privilegeAssignmentId = privilegeAssignmentId;
	}
	
	/**
	 * @return the authorizationPrincipal
	 */
	public AuthorizationPrincipal getAuthorizationPrincipal() {
		return authorizationPrincipal;
	}
	
	/**
	 * @param authorizationPrincipal the authorizationPrincipal to set
	 */
	public void setAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal) {
		this.authorizationPrincipal = authorizationPrincipal;
	}
	
	/**
	 * @return the privileges
	 */
	public Set<Privilege> getPrivileges() {
		return privileges;
	}
	
	/**
	 * @param privileges the privileges to set
	 */
	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}
	
	/**
	 * Checks if the PrivilegeAssignment contains the given privilege
	 * @param privilege The privilege to check
	 * @return True if the PrivilegeAssignment contains the given privilege, else false
	 */
	public boolean containsPrivilege(String privilege) {
		for (Privilege priv : privileges) {
			if (priv.getPrivilege().equals(privilege)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if the PrivilegeAssignment contains all of the given privileges
	 * @param privs The set of privileges to check
	 * @return True if the PrivilegeAssignment contains all the given privileges, else false
	 */
	public boolean constaintsAllPrivileges(Collection<String> privs) {
		for (String p : privs) {
			if (!containsPrivilege(p)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if the PrivilegeAssignment contains any one of the given privileges
	 * @param privs The set of privileges to check
	 * @return True if the PrivilegeAssignment contains any one of the given privileges, else false
	 */
	public boolean constaintsAnyPrivileges(Collection<String> privs) {
		for (String p : privs) {
			if (containsPrivilege(p)) {
				return true;
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((authorizationPrincipal == null) ? 0 : authorizationPrincipal.hashCode());
		result = prime * result + ((privilegeAssignmentId == null) ? 0 : privilegeAssignmentId.hashCode());
		result = prime * result + ((privileges == null) ? 0 : privileges.hashCode());
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
		PrivilegeAssignment other = (PrivilegeAssignment) obj;
		if (authorizationPrincipal == null) {
			if (other.authorizationPrincipal != null)
				return false;
		} else if (!authorizationPrincipal.equals(other.authorizationPrincipal))
			return false;
		if (privilegeAssignmentId == null) {
			if (other.privilegeAssignmentId != null)
				return false;
		} else if (!privilegeAssignmentId.equals(other.privilegeAssignmentId))
			return false;
		if (privileges == null) {
			if (other.privileges != null)
				return false;
		} else if (!privileges.equals(other.privileges))
			return false;
		return true;
	}
	
	@Override
	public Integer getId() {
		return getPrivilegeAssignmentId();
	}
	
	@Override
	public void setId(Integer id) {
		setPrivilegeAssignmentId(id);
	}
	
}
