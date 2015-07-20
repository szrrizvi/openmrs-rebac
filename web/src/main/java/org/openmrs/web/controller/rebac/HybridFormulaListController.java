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

package org.openmrs.web.controller.rebac;

import java.util.List;

import org.openmrs.AccessRelationshipType;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.HybridFormulaEditor;
import org.openmrs.rebac.HybridFormula;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller for AuthorizationPrincipal List page
 */

@Controller
@SessionAttributes("HybridFormula")
public class HybridFormulaListController {
	
	/**
	 * Set up automatic primitive-to-class mappings
	 * 
	 * @param wdb
	 */
	@InitBinder
	public void initBinder(WebDataBinder wdb) {
		wdb.registerCustomEditor(HybridFormula.class, new HybridFormulaEditor());
	}
	
	/**
	 * Loads all the HybridFormula objects to the model
	 * @param model
	 * @param request
	 */
	@RequestMapping(value = "/admin/rebac/hybridFormulaList")
	public void showForm(ModelMap model, WebRequest request) {
		
		try {
			// Get the list of all HybridFormula objects
			List<HybridFormula> hybridFormulas = Context.getAuthorizationPrincipalService().getAllHybridFormulas();
			// Load the list to the model
			model.addAttribute("hybridFormulas", hybridFormulas);
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
	}
	
	/**
	 * Purge an hybrid formula
	 */
	@RequestMapping("/admin/rebac/hybridFormulaPurge")
	public String purge(WebRequest request, @RequestParam("id") HybridFormula hybridFormula) {
		
		try {
			Context.getAuthorizationPrincipalService().purgeHybridFormula(hybridFormula);
			request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.hybridFormula.purged"), WebRequest.SCOPE_SESSION);
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
		return "redirect:hybridFormulaList.htm";
	}
}
