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

import org.openmrs.Patient;
import org.openmrs.action.Action;
import org.openmrs.action.Participant;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.ActionExecutionServiceImpl.ReturnObject;
import org.openmrs.propertyeditor.ActionEditor;
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
 * Controlled for Action Execution Form
 */

@Controller
@SessionAttributes("Action")
public class ActionExecuteFormController {
	
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
	 * Add the Action and PatientId to the model
	 * @param patientId
	 * @param actionId
	 * @param model
	 * @param request
	 */
	@RequestMapping(value = "/admin/rebac/actionExecuteForm")
	public void showForm(@RequestParam("patientId") String patientId, @RequestParam("actionId") String actionId,
	        ModelMap model, WebRequest request) {
		
		try {
			// Get the Action by the ID #
			Action action = Context.getActionService().getAction(Integer.parseInt(actionId));
			
			// Add attributes to the model
			model.addAttribute("action", action);
			model.addAttribute("patientId", patientId);
			
			//Convert patient id to int
			int patId = Integer.parseInt(patientId);
			
		}
		catch (APIException e) {
			// If APIEXception, then set the Access Denied message
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
	}
	
	/**
	 * Executes the Action based on the information provided by the form
	 * @param patientId
	 * @param actionId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin/rebac/actionExecuteSubmit")
	public String actionExecuteSubmit(@RequestParam("patientId") String patientId,
	        @RequestParam("actionId") String actionId, ModelMap model, WebRequest request) {
		
		// Check patientId is not null, if it is then return with error message
		if (!StringUtils.hasText(patientId)) {
			// If patientId was empty
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.action.execute.submit.patient.notFound"), WebRequest.SCOPE_SESSION);
			return "redirect:index.htm";
		}
		
		// Get action and patient
		Action action;
		Patient patient;
		
		try {
			// Get the Action by the actionId
			action = Context.getActionService().getAction(Integer.parseInt(actionId));
			// Get the patient by the patientId
			patient = Context.getPatientService().getPatient(Integer.parseInt(patientId));
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
			return ("redirect:patientDashboard.from?patientId=" + patientId);
		}
		
		// If couldn't get the patient, then return with error message
		if (patient == null) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.action.execute.submit.patient.notFound"), WebRequest.SCOPE_SESSION);
			return "redirect:index.htm";
		}
		
		// Check actionID is not null, if it is then return with error message
		if (!StringUtils.hasText(actionId)) {
			// If actionId was empty
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.action.execute.submit.action.notFound"), WebRequest.SCOPE_SESSION);
			return ("redirect:patientDashboard.from?patientId=" + patientId);
		}
		
		// If couldn't get the Action, then return with error message
		if (action == null) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.action.execute.submit.action.notFound"), WebRequest.SCOPE_SESSION);
			return ("redirect:patientDashboard.from?patientId=" + patientId);
		}
		
		// Get the assigned Participants
		List<Participant> participants = action.getParticipants();
		List<Integer> participantIds = new ArrayList<Integer>();
		
		participants.get(0).setPerson(patient); // First participant is patient
		participants.get(1).setUser(Context.getAuthenticatedUser()); // Second participant is the current user
		for (int i = 2; i < participants.size(); i++) {
			
			// If the Participant wasn't assigned, return with error message
			if (request.getParameter("participant_" + (i + 1)).isEmpty()) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.action.execute.submit.participant.notFound"), WebRequest.SCOPE_SESSION);
				return ("redirect:actionExecuteForm.htm?patientId=" + patientId + "&actionId=" + actionId);
			}
			
			participantIds.add(Integer.parseInt(request.getParameter("participant_" + (i + 1))));
		}
		
		// Execute Action
		ReturnObject ro = Context.getActionExecutionService().executeAction(action, participantIds);
		
		if (ro.getReturnValue()) {
			// If the action executed properly, go to confirmation page
			return ("redirect:actionConfirm.htm?patientId=" + patientId);
		} else {
			// If the action didn't execute properly, return with error message
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    ro.getErrorMessage()), WebRequest.SCOPE_SESSION);
			return ("redirect:actionExecuteForm.htm?patientId=" + patientId + "&actionId=" + actionId);
		}
	}
}
