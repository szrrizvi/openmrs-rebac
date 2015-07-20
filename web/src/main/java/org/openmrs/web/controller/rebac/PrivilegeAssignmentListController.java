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

import org.openmrs.PrivilegeAssignment;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.PrivilegeAssignmentEditor;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller for Privilege Assignment List page
 */
@Controller
@SessionAttributes("PrivilegeAssignment")
public class PrivilegeAssignmentListController {
	
	/**
	 * Set up automatic primitive-to-class mappings
	 * 
	 * @param wdb
	 */
	@InitBinder
	public void initBinder(WebDataBinder wdb) {
		wdb.registerCustomEditor(PrivilegeAssignment.class, new PrivilegeAssignmentEditor());
	}
	
	/**
	 * Loads the list of PrivilegeAssignments to the model
	 * @param model
	 * @param request
	 */
	@RequestMapping(value = "/admin/rebac/privilegeAssignmentList")
	public void showForm(ModelMap model, WebRequest request) {
		
		try {
			// Get the list of all PrivilegeAssignment objects
			List<PrivilegeAssignment> privilegeAssignments = Context.getAuthorizationPrincipalService()
			        .getAllPrivilegeAssignments();
			
			// Load the list of the model
			model.addAttribute("privilegeAssignments", privilegeAssignments);
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
	}
}
