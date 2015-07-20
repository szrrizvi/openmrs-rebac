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

package org.openmrs.api;

import java.util.List;

import org.openmrs.action.Action;
import org.openmrs.api.OpenmrsService;
import org.openmrs.api.db.AccessRelationshipDAO;
import org.openmrs.api.db.PatientDAO;
import org.openmrs.api.db.PersonDAO;
import org.openmrs.api.db.UserDAO;
import org.openmrs.api.impl.ActionExecutionServiceImpl.ReturnObject;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains methods relating to execution of Actions.
 */

@Transactional
public interface ActionExecutionService extends OpenmrsService {
	
	/**
	 * Sets the PersonDAO for this service. This is done through spring injection
	 * 
	 * @param personDAO DAO for this service
	 */
	public void setPersonDAO(PersonDAO personDAO);
	
	/**
	 * Sets the AccessRelationshipDAO for this service. This is done through spring injection
	 * 
	 * @param accessRelationshipDAO DAO for this service
	 */
	public void setAccessRelationshipDAO(AccessRelationshipDAO accessRelationshipDAO);
	
	/**
	 * Sets the UserDAO for this service. This is done through spring injection
	 * 
	 * @param userDAO DAO for this service
	 */
	public void setUserDAO(UserDAO userDAO);
	
	/**
	 * Sets the PatientDAO for this service. This is done through spring injection
	 * 
	 * @param patientDAO DAO for this service
	 */
	public void setPatientDAO(PatientDAO patientDAO);
	
	/**
	 * Checks if the availability precondition, for the given action, is satisfied if
	 * against the patient (specified by the given patientId)
	 * @param action 
	 * @param patientId
	 * @return True if the availability precondition is satisfied, else false
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	public boolean isActionActive(Action action, int patientId) throws APIException;
	
	/**
	 * Executes the given Action
	 * @param action The action to execute
	 * @param participantIds The list of IDs for the specified participants
	 * @return The ReturnObject with ReturnValue = true if the action executed properly, else 
	 *         ReturnValue = false and ErrorMessage contains the error message to display.
	 * @throws APIException
	 */
	public ReturnObject executeAction(Action action, List<Integer> participantIds) throws APIException;
}
