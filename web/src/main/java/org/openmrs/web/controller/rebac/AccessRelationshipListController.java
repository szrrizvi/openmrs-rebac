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

import org.openmrs.Person;
import org.openmrs.AccessRelationship;
import org.openmrs.AccessRelationshipType;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.AccessRelationshipEditor;
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
 * Controller for managing {@link AccessRelationship}s
 */
@Controller
@SessionAttributes("Relationship")
public class AccessRelationshipListController {
	
	/**
	 * Set up automatic primitive-to-class mappings
	 * 
	 * @param wdb
	 */
	@InitBinder
	public void initBinder(WebDataBinder wdb) {
		wdb.registerCustomEditor(AccessRelationship.class, new AccessRelationshipEditor());
	}
	
	/**
	 * List all access relationships with the matching person
	 */
	@RequestMapping("/admin/rebac/accessRelationshipList")
	public void list(ModelMap model, WebRequest req, @RequestParam(value = "name", required = false) String name) {
		
		try {
			List<AccessRelationship> relationships = getRelationships(name);
			
			model.addAttribute("relationships", relationships);
		}
		catch (APIException e) {
			// If APIException thrown, then set Access Denied message
			req.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
		
	}
	
	/**
	 * Add a new access relationship (quickly, without a dedicated page)
	 */
	@RequestMapping("/admin/rebac/accessRelationshipAdd")
	public String add(@RequestParam("personA") String personA, @RequestParam("personB") String personB,
	        @RequestParam("relationshipType") String relationshipType, WebRequest request) {
		
		try {
			if (!StringUtils.hasText(personA)) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.accessRelationship.personA.empty"), WebRequest.SCOPE_SESSION);
			} else if (!StringUtils.hasText(personB)) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.accessRelationship.personB.empty"), WebRequest.SCOPE_SESSION);
			} else if (!StringUtils.hasText(relationshipType)) {
				request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
				    "Rebac.accessRelationshipType.error"), WebRequest.SCOPE_SESSION);
			} else {
				
				AccessRelationship rel = createAccessRelationship(personA, personB, relationshipType, request);
				
				if (rel != null) {
					Context.getAccessRelationshipService().saveAccessRelationship(rel);
					request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage(
					    "Rebac.accessRelationship.saved"), WebRequest.SCOPE_SESSION);
				}
			}
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
		
		return "redirect:accessRelationshipList.htm";
	}
	
	/**
	 * Purge an access relationship
	 */
	@RequestMapping("/admin/rebac/accessRelationshipPurge")
	public String purge(WebRequest request, @RequestParam("id") AccessRelationship relationship) {
		
		try {
			Context.getAccessRelationshipService().purgeAccessRelationship(relationship);
			request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.accessRelationship.purged"), WebRequest.SCOPE_SESSION);
		}
		catch (APIException e) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "general.accessDenied"), WebRequest.SCOPE_SESSION);
		}
		
		return "redirect:accessRelationshipList.htm";
	}
	
	private List<AccessRelationship> getRelationships(String name) {
		List<AccessRelationship> list = new ArrayList<AccessRelationship>();
		
		if (StringUtils.hasText(name)) { // Search for non empty strings
			// Get all matching people
			List<Person> people = (ArrayList<Person>) Context.getPersonService().getPeople(name, null);
			for (Person p : people) { // Loop through each person
				// Get relationships for the current person
				List<AccessRelationship> tempList = Context.getAccessRelationshipService().getAccessRelationshipsByPerson(p);
				for (AccessRelationship r : tempList) { // Loop through each relationship
					// Add relationship to list only if it is not already in the list
					// This avoids duplication
					if (!list.contains(r)) {
						list.add(r);
					}
				}
			}
		}
		
		//Collections.sort(list, new MetadataComparator(Context.getLocale()));   // skip sorting
		return list;
	}
	
	// Creates the AccessRelationship object based on the given inputs
	private AccessRelationship createAccessRelationship(String personA, String personB, String relationshipType,
	        WebRequest request) {
		
		Person pa = Context.getPersonService().getPerson(Integer.valueOf(personA));
		Person pb = Context.getPersonService().getPerson(Integer.valueOf(personB));
		AccessRelationshipType rt = Context.getAccessRelationshipService().getAccessRelationshipType(
		    Integer.valueOf(relationshipType));
		
		// Get the necessary components; personA, personB, relationshipType
		if (pa == null) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.accessRelationship.personA.error"), WebRequest.SCOPE_SESSION);
		} else if (pb == null) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.accessRelationship.personB.error"), WebRequest.SCOPE_SESSION);
		} else if (rt == null) {
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
			    "Rebac.accessRelationshipType.error"), WebRequest.SCOPE_SESSION);
		}
		
		AccessRelationship rel = new AccessRelationship(pa, pb, rt);
		return rel;
	}
	
	public class SelectOption {
		
		private String text;
		
		private String value;
		
		public SelectOption(String text, String value) {
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
