package org.openmrs.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.AuthorizationPrincipalService;
import org.openmrs.api.context.Context;
import org.openmrs.rebac.HybridFormula;
import org.springframework.util.StringUtils;

public class HybridFormulaEditor extends PropertyEditorSupport {
	
	private static Log log = LogFactory.getLog(HybridFormulaEditor.class);
	
	/**
	 * Default constructor.
	 */
	public HybridFormulaEditor() {
	}
	
	/**
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		AuthorizationPrincipalService apc = Context.getAuthorizationPrincipalService();
		
		if (Context.isAuthenticated() && StringUtils.hasText(text)) {
			try {
				setValue(apc.getHybridFormula(Integer.valueOf(text)));
			}
			catch (Exception ex) {
				log.error("Error setting text: " + text, ex);
				throw new IllegalArgumentException("Authorization Principal not found: " + ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}
	
	/**
	 * @see java.beans.PropertyEditorSupport#getAsText()
	 */
	public String getAsText() {
		HybridFormula hf = (HybridFormula) getValue();
		return hf == null ? null : hf.getHybridFormulaId().toString();
	}
}
