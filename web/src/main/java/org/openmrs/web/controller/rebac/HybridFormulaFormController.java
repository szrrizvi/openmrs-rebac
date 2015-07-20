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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.AccessRelationshipType;
import org.openmrs.AuthorizationPrincipal;
import org.openmrs.PrivilegeAssignment;
import org.openmrs.RelationshipType;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.AuthorizationPrincipalEditor;
import org.openmrs.propertyeditor.HybridFormulaEditor;
import org.openmrs.rebac.AtImpl;
import org.openmrs.rebac.BindImpl;
import org.openmrs.rebac.BoxImpl;
import org.openmrs.rebac.ConjunctionImpl;
import org.openmrs.rebac.DiamondImpl;
import org.openmrs.rebac.DisjunctionImpl;
import org.openmrs.rebac.FalseImpl;
import org.openmrs.rebac.HybridFormula;
import org.openmrs.rebac.IllegalRebacPolicyException;
import org.openmrs.rebac.NegationImpl;
import org.openmrs.rebac.OwnerImpl;
import org.openmrs.rebac.PolicyImpl;
import org.openmrs.rebac.RebacSettings;
import org.openmrs.rebac.RequestorImpl;
import org.openmrs.rebac.TrueImpl;
import org.openmrs.rebac.VariableImpl;
import org.openmrs.rebac.implicitrelations.ImplicitRelationIdentifier;
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

import ca.ucalgary.ispia.rebac.Direction;
import ca.ucalgary.ispia.rebac.util.PolicyUtil;

@Controller
@SessionAttributes("HybridFormula")
public class HybridFormulaFormController {
	
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
	 *  Loads the attributes for the page
	 * @param name
	 * @param model
	 * @param request
	 */
	@RequestMapping(value = "/admin/rebac/hybridFormulaForm")
	public void showForm(ModelMap model, WebRequest request) {
		
		try {
			
			// Add relation identifiers as attribute (access relationship types and implicit relation identifiers)
			List<Option> relationIdentifiers = new ArrayList<Option>();
			
			// Adding AccessRelationshipType relation identifiers
			List<AccessRelationshipType> arts = Context.getAccessRelationshipService().getAllAccessRelationshipTypes();
			for (AccessRelationshipType art : arts) {
				relationIdentifiers.add(new Option("ACC: " + art.getName(), "a: "
				        + art.getAccessRelationshipTypeId().toString()));
			}
			
			// Adding PatientRecord type relation identifiers
			List<RelationshipType> rts = Context.getPersonService().getAllRelationshipTypes();
			for (RelationshipType rt : rts) {
				relationIdentifiers.add(new Option("PTR: " + rt.getaIsToB(), "p: " + rt.getRelationshipTypeId().toString()));
			}
			
			// Adding ImplicitRelationIdentifier relation identifiers
			Map<String, ImplicitRelationIdentifier> iris = RebacSettings.getImplicitRelIds();
			for (String iri : iris.keySet()) {
				// Note the "imp: " prefix
				relationIdentifiers.add(new Option("IMP: " + iri, "i: " + iri));
			}
			
			model.addAttribute("relationIdentifiers", relationIdentifiers);
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "require.unauthorized"), WebRequest.SCOPE_SESSION);
		}
		
	}
	
	/**
	 * Creates an HybridFormula object and saves it to the database
	 * @param name
	 * @param request
	 * @return
	 */
	@RequestMapping("/admin/rebac/hybridFormulaAdd")
	public String saveHybridFormula(@RequestParam(value = "hfName") String name, WebRequest request) {
		
		try {
			if (!StringUtils.hasText(name)) {
				// Check name
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.hybridFormula.error.name"), WebRequest.SCOPE_SESSION);
			} else {
				// Else all required parameters are valid
				
				if (Context.getAuthorizationPrincipalService().getHybridFormula(name) != null) {
					// HybridFormula with same name already exists in database
					request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
					    "Rebac.hybridFormula.error.duplicateName"), WebRequest.SCOPE_SESSION);
				} else {
					
					// Get the policy
					String policyStr = request.getParameter("policyTree");
					
					if (policyStr == null || policyStr.isEmpty()) {
						request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
						    "Rebac.policy.error.empty"), WebRequest.SCOPE_SESSION);
						
						return "redirect:hybridFormulaList.htm";
					}
					
					PolicyImpl policyImpl = PolicyParser.parsePolicyString(request, policyStr);
					
					// Get the free variables used
					Set<String> freeVars = PolicyUtil.findFreeVars(policyImpl, new HashSet<String>());
					// Remove the requestor and resource as free variables
					freeVars.remove("req");
					freeVars.remove("own");
					
					// Create HybridFormula
					HybridFormula hf = new HybridFormula(name, policyImpl, freeVars);
					
					// Save hybrid formula to the db
					Context.getAuthorizationPrincipalService().saveHybridFormula(hf);
					
					return "redirect:hybridFormulaList.htm";
				}
			}
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
		catch (IllegalRebacPolicyException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.hybridFormula.error.policy"), WebRequest.SCOPE_SESSION);
		}
		
		return "redirect:hybridFormulaList.htm";
	}
	
	// Contained class to add attributes with text and value fields.
	public class Option {
		
		private String text;
		
		private String value;
		
		public Option(String text, String value) {
			this.text = text;
			this.value = value;
		}
		
		public String getText() {
			return this.text;
		}
		
		public String getValue() {
			return this.value;
		}
	}
}
