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

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import org.openmrs.AccessRelationshipType;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.AccessRelationshipTypeEditor;
import org.openmrs.rebac.RebacSettings;
import org.openmrs.web.WebConstants;

@Controller
@SessionAttributes("AccessRelationshipType")
public class AccessRelationshipTypeListController {
	
	/**
	 * Set up automatic primitive-to-class mappings
	 * 
	 * @param wdb
	 */
	@InitBinder
	public void initBinder(WebDataBinder wdb) {
		wdb.registerCustomEditor(AccessRelationshipType.class, new AccessRelationshipTypeEditor());
	}
	
	/**
	 * List all access relationship types
	 */
	@RequestMapping("/admin/rebac/accessRelationshipTypeList")
	public void list(ModelMap model, WebRequest req) {
		
		try {
			List<AccessRelationshipType> list = new ArrayList<AccessRelationshipType>(Context.getAccessRelationshipService()
			        .getAllAccessRelationshipTypes());
			
			model.addAttribute("accessRelationshipTypes", list);
		}
		catch (APIException e) {
			// If APIException thrown, then set Access Denied message
			req.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
	}
	
	@RequestMapping("/admin/rebac/accessRelationshipTypeAdd")
	public String add(WebRequest request, @RequestParam("name") String name, @RequestParam("description") String description) {
		try {
			if (!StringUtils.hasText(name)) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.AccessRelationshipType.error.name"), WebRequest.SCOPE_SESSION);
			} else if (!StringUtils.hasText(description)) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.AccessRelationshipType.error.description"), WebRequest.SCOPE_SESSION);
			} else if (Context.getAccessRelationshipService().getAccessRelationshipTypeByName(name) != null) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.AccessRelationshipType.error.exists"), WebRequest.SCOPE_SESSION);
			} else if (RebacSettings.getImplicitRelIds().containsKey(name)) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.AccessRelationshipType.error.exists.implicit"), WebRequest.SCOPE_SESSION);
			} else {
				AccessRelationshipType art = new AccessRelationshipType(name, description);
				Context.getAccessRelationshipService().saveAccessRelationshipType(art);
				request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.accessRelationshipType.saved"), WebRequest.SCOPE_SESSION);
			}
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
		
		return "redirect:accessRelationshipTypeList.htm";
	}
	
	/**
	 * Purge an access relationship type
	 */
	@RequestMapping("/admin/rebac/accessRelationshipTypePurge")
	public String purge(WebRequest request, @RequestParam("id") AccessRelationshipType relationshipType) {
		
		try {
			Context.getAccessRelationshipService().purgeAccessRelationshipType(relationshipType);
			request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.accessRelationshipType.purged"), WebRequest.SCOPE_SESSION);
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
		return "redirect:accessRelationshipTypeList.htm";
	}
	
}
