package org.openmrs.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.action.Action;
import org.openmrs.api.ActionService;
import org.openmrs.api.context.Context;
import org.springframework.util.StringUtils;

public class ActionEditor extends PropertyEditorSupport {
	
	private static Log log = LogFactory.getLog(ActionEditor.class);
	
	/**
	 * Default constructor.
	 */
	public ActionEditor() {
	}
	
	/**
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		ActionService as = Context.getActionService();
		
		if (Context.isAuthenticated() && StringUtils.hasText(text)) {
			try {
				setValue(as.getAction(Integer.parseInt(text)));
			}
			catch (Exception ex) {
				log.error("Error setting text: " + text, ex);
				throw new IllegalArgumentException("Action not found: " + ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}
	
	/**
	 * @see java.beans.PropertyEditorSupport#getAsText()
	 */
	public String getAsText() {
		Action action = (Action) getValue();
		return action == null ? null : action.getActionId().toString();
	}
}
