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

package org.openmrs.api.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.AccessRelationship;
import org.openmrs.AccessRelationshipType;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.action.Action;
import org.openmrs.action.Effect;
import org.openmrs.action.Participant;
import org.openmrs.action.Precondition;
import org.openmrs.api.APIException;
import org.openmrs.api.ActionExecutionService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.AccessRelationshipDAO;
import org.openmrs.api.db.PatientDAO;
import org.openmrs.api.db.PersonDAO;
import org.openmrs.api.db.UserDAO;
import org.openmrs.rebac.FrameImpl;
import org.openmrs.rebac.PolicyImpl;

import ca.ucalgary.ispia.rebac.Environment;
import ca.ucalgary.ispia.rebac.Frame;
import ca.ucalgary.ispia.rebac.ModelChecker;
import ca.ucalgary.ispia.rebac.Policy;
import ca.ucalgary.ispia.rebac.util.Cache;
import ca.ucalgary.ispia.rebac.util.Constants;
import ca.ucalgary.ispia.rebac.util.SimpleCache;
import ca.ucalgary.ispia.rebac.util.Triple;

/**
 * Default implementation of ActionExecutionService
 */
public class ActionExecutionServiceImpl extends BaseOpenmrsService implements ActionExecutionService {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	// DAO's used to bypass authorization checking, which would lead to an infinite loop
	private PersonDAO personDAO;
	
	private AccessRelationshipDAO accessRelationshipDAO;
	
	private UserDAO userDAO;
	
	private PatientDAO patientDAO;
	
	/**
	 * @see org.openmrs.api.ActionExecutionService#setPersonDAO(PersonDAO)
	 */
	@Override
	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}
	
	/**
	 * @see org.openmrs.api.ActionExecutionService#setAccessRelationshipDAO(AccessRelationshipDAO)
	 */
	@Override
	public void setAccessRelationshipDAO(AccessRelationshipDAO accessRelationshipDAO) {
		this.accessRelationshipDAO = accessRelationshipDAO;
	}
	
	/**
	 * @see org.openmrs.api.ActionExecutionService#setUserDAO(UserDAO)
	 */
	@Override
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	/**
	 * @see org.openmrs.api.ActionExecutionService#setPatientDAO(PatientDAO)
	 */
	@Override
	public void setPatientDAO(PatientDAO patientDAO) {
		this.patientDAO = patientDAO;
	}
	
	/**
	 * @see org.openmrs.api.ActionExecutionService#isActionActive(Action, Patient)
	 */
	@Override
	public boolean isActionActive(Action action, int patientId) {
		// Get the patient
		Patient pat = patientDAO.getPatient(patientId);
		Person patient = personDAO.getPerson(pat.getPersonId());
		// Get the user
		Person user = Context.getAuthenticatedUser().getPerson();
		// Initialize the frame
		Frame frame = new FrameImpl(accessRelationshipDAO, personDAO);
		
		// Check availability precondition
		Cache<Triple<Policy, Environment, Object>, Boolean> cache = new SimpleCache<Triple<Policy, Environment, Object>, Boolean>();
		
		if (ModelChecker.check(cache, frame, Constants.VariableConvention.OwnerRequestor, patient, user, PolicyImpl
		        .translate(action.getHybridFormula().getPolicyImpl()))) {
			return true;
		}
		// If availability precondition fails, return false
		return false;
	}
	
	/**
	 * @see org.openmrs.api.ActionExecutionService#executeAction(Action)
	 */
	@Override
	public ReturnObject executeAction(Action action, List<Integer> participantIds) throws APIException {
		
		// Get the required objects first
		// Patient, User, PatientState (context), PatientProgram(context), Frame
		
		// Get the patient and user, as Person objects
		Person pat = action.getParticipants().get(0).retrievePerson();
		//Patient patient = Context.getPatientService().getPatient(pat.getPersonId());
		Person user = action.getParticipants().get(1).retrievePerson();
		
		// Initialize the frame
		Frame frame = new FrameImpl(accessRelationshipDAO, personDAO);
		
		// Check availability precondition
		Cache<Triple<Policy, Environment, Object>, Boolean> cache = new SimpleCache<Triple<Policy, Environment, Object>, Boolean>();
		if (!ModelChecker.check(cache, frame, Constants.VariableConvention.OwnerRequestor, pat, user, PolicyImpl
		        .translate(action.getHybridFormula().getPolicyImpl()))) {
			return new ReturnObject("Rebac.action.execute.submit.error.availabilityPrecondition", false);
		}
		
		// Load participants
		if (!(loadParticipants(action.getParticipants(), participantIds))) {
			return new ReturnObject("Rebac.action.execute.submit.error.participants", false);
		}
		
		// Check execution preconditions
		if (!(checkExecutionPrecondition(frame, action))) {
			return new ReturnObject("Rebac.action.execute.submit.error.preconditions", false);
		}
		
		// Try to Execute effects
		try {
			executeEffects(action.getEffects());
		}
		catch (APIException e) {
			// Fail if catch exception
			return new ReturnObject("Rebac.action.execute.submit.error.effects", false);
		}
		
		return new ReturnObject(null, true);
	}
	
	/**
	 * Given lists of Participants and UserIDs, assigns the users to the participants
	 * @param participants The list of participants
	 * @param userIds The user Ids
	 * @return True if the users were assigned to participants,
	 * 		   return false if there was some issue (ex. user not found)
	 */
	private boolean loadParticipants(List<Participant> participants, List<Integer> userIds) throws APIException {
		// The patient and user should be loaded through the form controller.
		if (participants.get(0).getPerson() == null || participants.get(1).getUser() == null) {
			return false;
		}
		
		// Loop through the participantIds, and set the participants
		for (int i = 0; i < (participants.size() - 2); i++) {
			// Get the assigned user
			User user = userDAO.getUser(userIds.get(i));
			
			// If the user is null, return false
			if (user == null) {
				return false;
			}
			// Assign the user for the participant
			participants.get(i + 2).setUser(user);
		}
		
		return true;
	}
	
	/**
	 * Given a Frame and a Precondition, this method checks if the precondition can be
	 * satisfied within the given frame
	 * @param frame The frame 
	 * @param precondition The preconditions
	 * @param patient The patient; the starting point for model checking
	 * @return True if the precondition can be satisfied, else false
	 */
	private boolean checkExecutionPrecondition(Frame frame, Action action) {
		
		// Get the precondition
		Precondition precondition = action.getPrecondition();
		
		// Get the set of participants associated with the precondition
		List<Participant> participants = action.getParticipants();
		
		// Get the user and patient
		Person patient = action.getParticipants().get(0).retrievePerson();
		Person user = action.getParticipants().get(1).retrievePerson();
		
		// Create and populate the ReBAC environment with each participant
		Environment environment = null;
		environment = Environment.insert(Constants.owner, patient, environment);
		environment = Environment.insert(Constants.requestor, user, environment);
		
		for (Participant p : participants) {
			// For each participant, get the associated Person
			// And add each associate free variable to the environment
			Person person = p.retrievePerson();
			Set<String> freeVars = p.getFreeVariables();
			if (freeVars != null && !freeVars.isEmpty()) {
				for (String str : freeVars) {
					environment = Environment.insert(str, person, environment);
				}
			}
		}
		
		// Generate the cache
		Cache<Triple<Policy, Environment, Object>, Boolean> cache = new SimpleCache<Triple<Policy, Environment, Object>, Boolean>();
		
		//Get the ReBAC policy from the precondition
		PolicyImpl policy = precondition.getHybridFormula().getPolicyImpl();
		
		//Perform model checking
		if (ModelChecker.check(cache, frame, environment, patient, policy)) {
			return true;
		}
		return false;
		
	}
	
	/**
	 * Given a list of Effects, this method executes those effects relative to the given contexts
	 * @param effects The effects to execute
	 * @param patientProgram The patientProgram context
	 * @param patientState The patientState context
	 */
	private void executeEffects(List<Effect> effects) throws APIException {
		for (Effect effect : effects) {
			// Obtain the two Persons chosen for the effect
			Person personA = effect.getParticipantA().retrievePerson();
			Person personB = effect.getParticipantB().retrievePerson();
			// Get the AccessRelationshipType object for the effect
			AccessRelationshipType accessRelationshipType = effect.getAccessRelationshipType();
			
			// If the effect is to "add" an access relationship
			if (effect.getEffectType().equals("add")) {
				
				// Check if the relationship already exists in the system
				AccessRelationship accessRelationship = getAccessRelationship(personA, personB, accessRelationshipType);
				
				if (accessRelationship == null) {
					// Create the new AccessRelationship object
					AccessRelationship ar = new AccessRelationship(personA, personB, accessRelationshipType);
					// Save the relationship
					accessRelationshipDAO.saveAccessRelationship(ar);
				} else {
					throw new APIException("Relationship already exists: PersonA: " + personA + ", PersonB: " + personB
					        + ", AccessRelationshipType: " + accessRelationshipType);
				}
			} else {
				// Else the effect is to "remove" an access relationship
				
				// Get the AccessRelationship that matches the parameters
				AccessRelationship accessRelationship = getAccessRelationship(personA, personB, accessRelationshipType);
				
				if (accessRelationship != null) {
					// If such an AccessRelationship exists, delete it.
					accessRelationshipDAO.deleteAccessRelationship(accessRelationship);
				} else {
					throw new APIException("Deleting non-existent AccessRelationship object: PersonA: " + personA
					        + ", PersonB: " + personB + ", AccessRelationshipType: " + accessRelationshipType);
				}
			}
		}
	}
	
	/**
	 * Given specifications for an AccessRelationship object, finds an AccessRelationshipObject that matches them.
	 * @param personA PersonA for the AccessRelationship
	 * @param personB PersonB for the AccessRelationship
	 * @param accessRelationshipType The AccessRelationshipType

	 * @return The first AccessRelationship object that matches the parameters, returns null if no active
	 * 		   AccessRelationship objects found
	 */
	private AccessRelationship getAccessRelationship(Person personA, Person personB,
	        AccessRelationshipType accessRelationshipType) throws APIException {
		
		// Get the list of AccessRelationship objects that match the parameters
		List<AccessRelationship> acRels = accessRelationshipDAO.getAccessRelationships(personA, personB,
		    accessRelationshipType);
		if (acRels != null && !acRels.isEmpty()) {
			// If some AccessRelationship objects found, return the first one
			return acRels.get(0);
		}
		
		// If no match AccessRelationship objects found, or none active, return null
		return null;
	}
	
	/**
	 * Inner class to be used as a return object for the executeAction method.
	 */
	public class ReturnObject {
		
		private String errorMessage; // The error message		
		
		// Null if the action executed properly
		
		private boolean returnValue; // The return value
		
		// True if the action executed properly
		// False if the action didn't execute properly
		
		/**
		 * Private constructor, so this class cannot be instantiated by other classes
		 * @param errorMessage the error message
		 * @param returnValue the return value
		 */
		private ReturnObject(String errorMessage, boolean returnValue) {
			this.errorMessage = errorMessage;
			this.returnValue = returnValue;
		}
		
		/**
		 * @return The error message
		 */
		public String getErrorMessage() {
			return this.errorMessage;
		}
		
		/**
		 * @return The return value
		 */
		public boolean getReturnValue() {
			return this.returnValue;
		}
	}
}
