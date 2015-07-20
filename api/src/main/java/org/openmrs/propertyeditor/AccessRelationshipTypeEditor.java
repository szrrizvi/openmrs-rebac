package org.openmrs.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.AccessRelationshipType;
import org.openmrs.api.AccessRelationshipService;
import org.openmrs.api.context.Context;
import org.springframework.util.StringUtils;

public class AccessRelationshipTypeEditor extends PropertyEditorSupport {
	
	private static Log log = LogFactory.getLog(AccessRelationshipTypeEditor.class);
	
	public AccessRelationshipTypeEditor() {
	}
	
	/**
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		AccessRelationshipService ars = Context.getAccessRelationshipService();
		if (Context.isAuthenticated() && StringUtils.hasText(text)) {
			try {
				setValue(ars.getAccessRelationshipType(Integer.valueOf(text)));
			}
			catch (Exception ex) {
				log.error("Error setting text: " + text, ex);
				throw new IllegalArgumentException("Access Relationship Type not found: " + ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}
	
	/**
	 * @see java.beans.PropertyEditorSupport#getAsText()
	 */
	public String getAsText() {
		AccessRelationshipType art = (AccessRelationshipType) getValue();
		return art == null ? null : art.getAccessRelationshipTypeId().toString();
	}
	
}
