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
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.openmrs.api.context.Context;
import org.openmrs.rebac.Algorithm;
import org.openmrs.rebac.RebacSettings;

/**
 * Controller for RebacEnableDisable page
 */
@Controller
public class RebacEnableDisableController {
	
	/**
	 * Loads the attributes to the page
	 * @param model
	 * @param req
	 */
	@RequestMapping("/admin/rebac/rebacEnableDisable")
	public void enableDisable(ModelMap model, WebRequest req) {
		
		// Set attribute rebacEnable to true if ReBAC authorization checking is
		// enabled, else false
		Boolean rebacEnable = RebacSettings.isRebacEnabled();
		model.addAttribute("rebacEnable", rebacEnable);
		
		//Obtain the current ReBAC Authorization Checking Algorithm, and add it to the model
		Algorithm currentAlgorithm = RebacSettings.getAlgorithm();
		model.addAttribute("currentAlgorithm", currentAlgorithm);
		
		//Obtain the list of Algorithms in enum Algorithm, and add it to the model
		List<Algorithm> algorithms = new ArrayList<Algorithm>(Arrays.asList(Algorithm.values()));
		model.addAttribute("algorithms", algorithms);
	}
	
	/**
	 * Disables ReBAC authorization checking
	 * @return
	 */
	@RequestMapping("/admin/rebac/disableRebac")
	public String disableRebac() {
		RebacSettings.disableRebac(); // disable ReBAC authorization checking
		
		return "redirect:rebacEnableDisable.htm";
	}
	
	/**
	 * Enables ReBAC authorization checking
	 * @return
	 */
	@RequestMapping("/admin/rebac/enableRebac")
	public String enableRebac() {
		RebacSettings.enableRebac(); // enable ReBAC authorization checking
		
		return "redirect:rebacEnableDisable.htm";
	}
	
	/**
	 * Sets the ReBAC Authorization Checking algorithm flag
	 * @param request
	 * @return
	 */
	@RequestMapping("/admin/rebac/setAlgorithm")
	public String setAlgorithm(WebRequest request) {
		String newAlgorithm = request.getParameter("algorithm");
		
		if (newAlgorithm.equals("singleaplazy")) {
			// "Single Authorization Principal" Lazy algorithm
			RebacSettings.setAlgorithm(Algorithm.SingleAPLazy);
		} else if (newAlgorithm.equals("singleapeager")) {
			// "Single Authorization Principal" Eager algorithm
			RebacSettings.setAlgorithm(Algorithm.SingleAPEager);
		} else if (newAlgorithm.equals("multiaplazy")) {
			// "Multi Authorization Principal" Lazy algorithm
			RebacSettings.setAlgorithm(Algorithm.MultiAPLazy);
		} else if (newAlgorithm.equals("multiapeager")) {
			// "Multi Authorization Principal" Eager algorithm
			RebacSettings.setAlgorithm(Algorithm.MultiAPEager);
		}
		
		return "redirect:rebacEnableDisable.htm";
	}
}
