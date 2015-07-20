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

import org.openmrs.AuthorizationPrincipal;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.AuthorizationPrincipalEditor;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller for AuthorizationPrincipal List page
 */

@Controller
@SessionAttributes("AuthorizationPrincipal")
public class AuthorizationPrincipalListController {
	
	/**
	 * Set up automatic primitive-to-class mappings
	 * 
	 * @param wdb
	 */
	@InitBinder
	public void initBinder(WebDataBinder wdb) {
		wdb.registerCustomEditor(AuthorizationPrincipal.class, new AuthorizationPrincipalEditor());
	}
	
	/**
	 * Loads all the AuthorizationPrincipal objects to the model
	 * @param model
	 * @param request
	 */
	@RequestMapping(value = "/admin/rebac/authorizationPrincipalList")
	public void showForm(ModelMap model, WebRequest request) {
		
		try {
			// Get the list of all AuthorizationPrincipal objects
			List<AuthorizationPrincipal> authorizationPrincipals = Context.getAuthorizationPrincipalService()
			        .getAllAuthorizationPrincipals();
			// Load the list to the model
			model.addAttribute("authorizationPrincipals", authorizationPrincipals);
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
	}
}
