package org.openmrs.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.PrivilegeAssignment;
import org.openmrs.api.AuthorizationPrincipalService;
import org.openmrs.api.context.Context;
import org.springframework.util.StringUtils;

public class PrivilegeAssignmentEditor extends PropertyEditorSupport {
	
	private static Log log = LogFactory.getLog(AuthorizationPrincipalEditor.class);
	
	/**
	 * Default constructor.
	 */
	public PrivilegeAssignmentEditor() {
	}
	
	/**
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		AuthorizationPrincipalService apc = Context.getAuthorizationPrincipalService();
		
		if (Context.isAuthenticated() && StringUtils.hasText(text)) {
			try {
				setValue(apc.getPrivilegeAssignment(Integer.parseInt(text)));
			}
			catch (Exception ex) {
				log.error("Error setting text: " + text, ex);
				throw new IllegalArgumentException("Authorization Rule not found: " + ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}
	
	/**
	 * @see java.beans.PropertyEditorSupport#getAsText()
	 */
	public String getAsText() {
		PrivilegeAssignment ar = (PrivilegeAssignment) getValue();
		return ar == null ? null : ar.getPrivilegeAssignmentId().toString();
	}
}
