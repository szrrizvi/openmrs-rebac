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

import org.openmrs.action.Action;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.ActionEditor;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller for Action List page
 */
@Controller
@SessionAttributes("Action")
public class ActionListController {
	
	/**
	 * Set up automatic primitive-to-class mappings
	 * 
	 * @param wdb
	 */
	@InitBinder
	public void initBinder(WebDataBinder wdb) {
		wdb.registerCustomEditor(Action.class, new ActionEditor());
	}
	
	/**
	 * Loads all the Action objects to the model
	 * @param model
	 * @param request
	 */
	@RequestMapping(value = "/admin/rebac/actionList")
	public void showForm(ModelMap model, WebRequest request) {
		
		try {
			// Get the list of all the Actions and load them to the model
			List<Action> actions = Context.getActionService().getAllActions();
			model.addAttribute("actions", actions);
		}
		catch (APIException e) {
			// If APIException thrown, then set Access Denied message
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
	}
}
