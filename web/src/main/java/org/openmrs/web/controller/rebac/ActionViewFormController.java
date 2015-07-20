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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller for Action View Form
 */

@Controller
@SessionAttributes("Action")
public class ActionViewFormController {
	
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
	 * Loads the attributes to the page
	 * @param actionId
	 * @param model
	 * @param request
	 */
	@RequestMapping(value = "/admin/rebac/actionViewForm")
	public void showForm(@RequestParam("id") String actionId, ModelMap model, WebRequest request) {
		
		try {
			Action action = Context.getActionService().getAction(Integer.parseInt(actionId));
			model.addAttribute("action", action);
			
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
		catch (NumberFormatException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "rebac.illegalId: " + actionId), WebRequest.SCOPE_SESSION);
		}
	}
	
	/**
	 * Deletes the given Action object from the database
	 * @param action
	 * @param request
	 * @return
	 */
	@RequestMapping("/admin/rebac/actionPurge")
	public String deleteAction(@RequestParam(value = "actionId") Action action, WebRequest request) {
		
		try {
			
			// Delete action
			Context.getActionService().purgeAction(action);
			
			request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage(
			    "general.deleted"), WebRequest.SCOPE_SESSION);
			
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
		
		return "redirect:actionList.htm";
	}
}
