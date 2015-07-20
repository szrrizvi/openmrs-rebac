package org.openmrs.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.AccessRelationship;
import org.openmrs.api.AccessRelationshipService;
import org.openmrs.api.context.Context;
import org.springframework.util.StringUtils;

public class AccessRelationshipEditor extends PropertyEditorSupport {
	
	private static Log log = LogFactory.getLog(AccessRelationshipEditor.class);
	
	public AccessRelationshipEditor() {
	}
	
	/**
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		AccessRelationshipService ars = Context.getAccessRelationshipService();
		if (Context.isAuthenticated() && StringUtils.hasText(text)) {
			try {
				setValue(ars.getAccessRelationship(Integer.valueOf(text)));
			}
			catch (Exception ex) {
				log.error("Error setting text: " + text, ex);
				throw new IllegalArgumentException("Access Relationship not found: " + ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}
	
	/**
	 * @see java.beans.PropertyEditorSupport#getAsText()
	 */
	public String getAsText() {
		AccessRelationship ar = (AccessRelationship) getValue();
		return ar == null ? null : ar.getAccessRelationshipId().toString();
	}
	
}
