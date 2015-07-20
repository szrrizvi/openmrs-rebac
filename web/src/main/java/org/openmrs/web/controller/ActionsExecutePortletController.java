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

package org.openmrs.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Patient;
import org.openmrs.action.Action;
import org.openmrs.api.context.Context;

/**
 * Controller for ActionsExecutePortlet
 */
public class ActionsExecutePortletController extends PortletController {
	
	/**
	 * @see org.openmrs.web.controller.PortletController#populateModel(javax.servlet.http.HttpServletRequest,
	 *      java.util.Map)
	 */
	protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
		// Get the patientId, and all the Actions
		int patientId = (Integer) model.get("patientId");
		List<Action> actions = Context.getActionService().getAllActions();
		
		// If the Action is active between the User and Patient, then add it to the list active Actions
		List<Action> activeActions = new ArrayList<Action>();
		for (Action action : actions) {
			if (Context.getActionExecutionService().isActionActive(action, patientId)) {
				activeActions.add(action);
			}
		}
		
		model.put("actions", activeActions);
	}
}
