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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.PrivilegeAssignment;
import org.openmrs.Privilege;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.PrivilegeAssignmentEditor;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller for PrivilegeAssignment Form
 */
@Controller
@SessionAttributes("PrivilegeAssignment")
public class PrivilegeAssignmentFormController {
	
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
	 *  Loads the attributes for the page
	 * @param pdId
	 * @param model
	 * @param request
	 */
	@RequestMapping(value = "/admin/rebac/privilegeAssignmentForm")
	public void showForm(@RequestParam(value = "paId") String paId, ModelMap model, WebRequest request) {
		
		try {
			// Add list of all privileges as attribute
			List<Privilege> privileges = Context.getUserService().getAllPrivileges();
			model.addAttribute("privileges", privileges);
			
			// Load the specific privilege assignment
			if (StringUtils.hasText(paId)) {
				PrivilegeAssignment pa = Context.getAuthorizationPrincipalService().getPrivilegeAssignment(
				    Integer.parseInt(paId));
				if (pa == null) {
					request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
					    "Rebac.privilegeAssignment.error.notFound"), WebRequest.SCOPE_SESSION);
				} else {
					model.addAttribute("privilegeAssignment", pa);
					String policyString = pa.getAuthorizationPrincipal().getPolicy().toString();
					
					model.addAttribute("policyString", policyString);
				}
			}
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "require.unauthorized"), WebRequest.SCOPE_SESSION);
		}
	}
	
	/**
	 * Edits the privilege assignment object and saves it to the database
	 * @param paId
	 * @param privileges
	 * @param request
	 * @return
	 */
	@RequestMapping("/admin/rebac/privilegeAssignmentEdit")
	public String saveAuthorizationPrincipal(
	        @RequestParam(value = "privilegeAssignment") PrivilegeAssignment privilegeAssignment,
	        @RequestParam(value = "paPrivileges") Collection privileges, WebRequest request) {
		
		try {
			// Get the set of privileges
			Set<Privilege> privs = new HashSet<Privilege>();
			for (Object p : privileges) {
				privs.add(Context.getUserService().getPrivilege((String) p));
			}
			// Add privileges to the privilege assignment and save to database
			privilegeAssignment.setPrivileges(privs);
			Context.getAuthorizationPrincipalService().savePrivilegeAssignment(privilegeAssignment);
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
		
		return "redirect:privilegeAssignmentForm.htm?paId=" + privilegeAssignment.getPrivilegeAssignmentId();
	}
}
