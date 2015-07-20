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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openmrs.AccessRelationshipType;
import org.openmrs.action.Action;
import org.openmrs.action.Effect;
import org.openmrs.action.Participant;
import org.openmrs.action.Precondition;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.ActionEditor;
import org.openmrs.rebac.HybridFormula;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller for the Action Form
 */
@Controller
@SessionAttributes("Action")
public class ActionFormController {
	
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
	 * Loads the required attributes to the model
	 * @param model
	 * @param request
	 */
	@RequestMapping(value = "/admin/rebac/actionForm")
	public void showForm(ModelMap model, WebRequest request) {
		
		try {
			
			// Get all the Roles, HybridFormulas and AccessRelationshipTypes 
			// and load them to the model
			List<HybridFormula> hybridFormulas = Context.getAuthorizationPrincipalService().getAllHybridFormulas();
			List<AccessRelationshipType> relIds = Context.getAccessRelationshipService().getAllAccessRelationshipTypes();
			
			//Create a second list of hybrid formulas to be used for the availability precondtions
			//these hybrid formulas have no explicit free variables
			List<HybridFormula> avHybridFormulas = new ArrayList<HybridFormula>();
			
			for (HybridFormula hf : hybridFormulas) {
				if (hf.getVariableNames().isEmpty()) {
					avHybridFormulas.add(hf);
				}
			}
			
			model.addAttribute("hybridFormulas", hybridFormulas);
			model.addAttribute("relIds", relIds);
			model.addAttribute("avHybridFormulas", avHybridFormulas);
		}
		catch (APIException e) {
			// If APIEXception, then set the Access Denied message
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
	}
	
	/**
	 * Saves the action as specified by the form
	 * @param request
	 * @return
	 */
	@RequestMapping("/admin/rebac/actionAdd")
	public String saveAction(WebRequest request) {
		
		/*
		 * * = wildcard (# value)
		 * Important field names:
		 * actionName
		 * precondition0
		 * p*Name										(1..)
		 * precond										
		 * freeVariable*								(1..)
		 * effect*e1, effecr*e2, effect*, effect*relId	(1..)
		 */
		boolean valid = true;
		
		String actionName = request.getParameter("actionName");
		String precondition0 = request.getParameter("precondition0");
		
		HybridFormula hybridFormula = null;
		
		// Check required values
		
		if (!StringUtils.hasText(actionName)) {
			// If No name was provided, then the action is invalid
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.action.error.name"), WebRequest.SCOPE_SESSION);
			valid = false;
		}
		
		if (StringUtils.hasText(precondition0)) {
			hybridFormula = Context.getAuthorizationPrincipalService().getHybridFormula(Integer.valueOf(precondition0));
			
			if (hybridFormula == null) {
				// If getHybridFormula returned null
				valid = false;
			}
		} else {
			// Precondition0 cannot be null or empty
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.action.error.availabilityPrecondition"), WebRequest.SCOPE_SESSION);
			valid = false;
		}
		
		if (valid) {
			// Get Participants
			List<Participant> participants = null;
			try {
				participants = makeParticipants(request);
			}
			catch (IllegalArgumentException e) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.action.error.participant"), WebRequest.SCOPE_SESSION);
				valid = false;
				return "redirect:actionForm.htm";
			}
			
			// Get Preconditions
			Precondition precondition = null;
			try {
				precondition = makePrecondition(request, participants);
			}
			catch (IllegalArgumentException e) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.action.error.precondition"), WebRequest.SCOPE_SESSION);
				valid = false;
				return "redirect:actionForm.htm";
			}
			
			// Get Effects
			List<Effect> effects = null;
			try {
				effects = makeEffects(request, participants);
			}
			catch (IllegalArgumentException e) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.action.error.effect"), WebRequest.SCOPE_SESSION);
				valid = false;
				return "redirect:actionForm.htm";
			}
			
			if (valid) {
				Action action = new Action(actionName, hybridFormula, participants, precondition, effects);
				action = Context.getActionService().saveAction(action);
				return "redirect:actionViewForm.htm?id=" + action.getActionId();
			}
			
		}
		return "redirect:actionForm.htm";
	}
	
	/**
	 * Creates a list of Participants based on the given webRequest.
	 * List is imporant, because the ordering of objects is important
	 * @param request
	 * @return
	 */
	private List<Participant> makeParticipants(WebRequest request) {
		List<Participant> participants = new ArrayList<Participant>();
		
		// Add implicit participants (user and patient)
		Participant user = new Participant("patient");
		Participant patient = new Participant("user");
		participants.add(user);
		participants.add(patient);
		
		boolean done = false;
		int i = 1;
		while (!done) {
			// Keep looping till there are participants
			
			// Get participant name and the required role
			String name = request.getParameter("p" + i + "Name");
			
			if (name == null) {
				// If name was null, there are no more participants
				done = true;
			} else {
				// Create new participant
				Participant p = new Participant(name);
				
				participants.add(p); // Add participant to the set 
			}
			i++;
		}
		
		return participants;
	}
	
	/**
	 * Generates the Precondition for the Action, based on the information on the form
	 * @param request
	 * @param participants
	 * @return
	 */
	private Precondition makePrecondition(WebRequest request, List<Participant> participants) {
		
		// Get the HybridFormula by id
		String hybridFormulaId = request.getParameter("precondition");
		HybridFormula hybridFormula = null;
		if (hybridFormulaId != null) {
			hybridFormula = Context.getAuthorizationPrincipalService().getHybridFormula(Integer.valueOf(hybridFormulaId));
		}
		
		// Check that the hybrid formula is not null
		if (hybridFormula == null) {
			throw new IllegalArgumentException("HybridFormula null in Precondition");
		}
		
		Set<String> variableNames = hybridFormula.getVariableNames();
		for (int i = 1; i <= variableNames.size(); i++) {
			// For each explicit free variable
			
			//Get the variable name
			String varName = request.getParameter("varName" + i);
			
			//Get the selected participant
			String strParticipant = request.getParameter("freeVariable" + i);
			Participant participant = null;
			
			if (strParticipant == null) {
				throw new IllegalArgumentException("Variable name is null: " + strParticipant);
			}
			
			if (strParticipant.equals("patient")) {
				participant = participants.get(0);
			} else if (strParticipant.equals("user")) {
				participant = participants.get(1);
			} else {
				participant = participants.get((Integer.parseInt(strParticipant)) + 1);
			}
			participant.addFreeVariable(varName);
			
		}
		
		Precondition precondition = new Precondition(hybridFormula);
		return precondition;
		
	}
	
	/**
	 * Generates a list of Effects for the Action, based on the information on the form
	 * @param request
	 * @param participants
	 * @param program
	 * @param state
	 * @return
	 */
	private List<Effect> makeEffects(WebRequest request, List<Participant> participants) {
		List<Effect> effects = new ArrayList<Effect>();
		
		boolean done = false;
		int i = 1;
		
		while (!done) {
			// Keeping looping till there are effects
			// Get all fields for the Effect
			String e1 = request.getParameter("effect" + i + "e1");
			String e2 = request.getParameter("effect" + i + "e2");
			String type = request.getParameter("effect" + i + "Type");
			String relId = request.getParameter("effect" + i + "RelId");
			
			if (e1 == null) {
				// All 4 values exist together, if e1 is null 
				// it means there are no more preconditions
				done = true;
			} else {
				// Get participantA
				Participant participantA = null;
				if (e1.equals("patient")) {
					participantA = participants.get(0);
				} else if (e1.equals("user")) {
					participantA = participants.get(1);
				} else {
					participantA = participants.get((Integer.parseInt(e1)) + 1);
				}
				
				// Get participantB
				Participant participantB = null;
				if (e2.equals("patient")) {
					participantB = participants.get(0);
				} else if (e2.equals("user")) {
					participantB = participants.get(1);
				} else {
					participantB = participants.get((Integer.parseInt(e2)) + 1);
				}
				
				// Get the AccessRelationshipType, relationship identifier
				AccessRelationshipType art = Context.getAccessRelationshipService().getAccessRelationshipType(
				    Integer.parseInt(relId));
				
				// If the AccessRealtionshipType is null, throw exception
				if (art == null) {
					throw new IllegalArgumentException("Relation Identifier null for Effect");
				}
				
				// Get the effect type, add or remove
				Effect.EffectType effectType = null;
				if (type.equals("add")) {
					effectType = Effect.EffectType.ADD;
				} else if (type.equals("remove")) {
					effectType = Effect.EffectType.REMOVE;
				}
				
				// Create the new Effect object, and add it to the list
				Effect effect = new Effect(participantA, participantB, effectType, art);
				effects.add(effect);
			}
			
			i++;
		}
		
		return effects;
	}
	
	/**
	 * Inner class to be used as a return object for some methods. Used to represent a pair of
	 * String values. User for DropDown menus (key, value)
	 */
	public class SelectOption {
		
		// The pair for string values
		private String text;
		
		private String value;
		
		/**
		 * Private constructor, so no other classes can instantiate this object
		 * @param text
		 * @param value
		 */
		private SelectOption(String text, String value) {
			this.text = text;
			this.value = value;
		}
		
		// Field accessors
		
		/**
		 * @return the text
		 */
		public String getText() {
			return this.text;
		}
		
		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
	}
}
