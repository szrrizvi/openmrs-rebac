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

import org.openmrs.AuthorizationPrincipal;
import org.openmrs.PrivilegeAssignment;
import org.openmrs.annotation.Authorized;
import org.openmrs.annotation.Resource;
import org.openmrs.api.db.AuthorizationPrincipalDAO;
import org.openmrs.rebac.HybridFormula;
import org.openmrs.rebac.PolicyImpl;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AuthorizationPrincipalService extends OpenmrsService {
	
	/**
	 * Setter for the AuthorizationPrincipal DataAccessObject (DAO). The DAO is used for saving and
	 * retrieving from the database
	 * 
	 * @param dao - The DAO for this service
	 */
	public void setAuthorizationPrincipalDAO(AuthorizationPrincipalDAO dao);
	
	//*****Authorization Principal*****//
	
	/**
	 * Gets authroization principal by name
	 * @param name
	 * @return The authorization principal with the given name
	 * @throws APIException
	 * @should return the authorization principal with the given name
	 * @should return null if the given name does not match any authorization principals in the db
	 */
	@Transactional(readOnly = true)
	public AuthorizationPrincipal getAuthorizationPrincipal(String authorizationPrincipal) throws APIException;
	
	/**
	 * Returns all the authorization principals in the db, does not include voided
	 * @return All the authorization principals in db
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	public List<AuthorizationPrincipal> getAllAuthorizationPrincipals() throws APIException;
	
	/**
	 * Saves the given authorization principal to the db
	 * @param authorizationPrincipal The authorization principal to save in the db
	 * @return The authorization principal saved in the db
	 * @throws APIException
	 * @should Save the given authorization principal to the db
	 */
	@Authorized( { PrivilegeConstants.MANAGE_AUTHORIZATION_PRINCIPALS })
	public AuthorizationPrincipal saveAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal)
	        throws APIException;
	
	/**
	 * Deletes the given authorization principal from the db (cannot be undone)
	 * @param authorizationPrincipal The authorization principal to delete from the database
	 * @throws APIException
	 * @should delete the given authorization principal from the database
	 */
	@Authorized( { PrivilegeConstants.MANAGE_AUTHORIZATION_PRINCIPALS })
	public void purgeAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal) throws APIException;
	
	//*****Privilege Assignment*****//
	/**
	 * Gets privilege assignment by id
	 * @param privilegeAssignmentId
	 * @return The privilege assignment with the given id
	 * @throws APIException
	 * @should return the privilege assignment with the given id
	 * @should return null if the given id does not match any privilege assignments in the db
	 */
	@Transactional(readOnly = true)
	public PrivilegeAssignment getPrivilegeAssignment(Integer privilegeAssignmentId) throws APIException;
	
	/**
	 * Gets the privilege assignment associated with the given authorization principal
	 * @param authorizationPrincipal
	 * @return the privilege assignment that contains the given authorization principal
	 * @should return null if no privilege assignment contains the given authorization principal
	 */
	@Transactional(readOnly = true)
	public PrivilegeAssignment getPrivilegeAssignmentByAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal)
	        throws APIException;
	
	/**
	 * Returns all the privilege assignments in the db
	 * @return All the privilege assignments in db
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	public List<PrivilegeAssignment> getAllPrivilegeAssignments() throws APIException;
	
	/**
	 * Saves the given privilege assignment to the db
	 * @param privilegeAssignment the privilegeAssignment to save in the db
	 * @return The privilegeAssignment saved in the db
	 * @throws APIException
	 * @should Save the given privilegeAssignment to the db
	 */
	@Authorized( { PrivilegeConstants.MANAGE_PRIVILEGE_ASSIGNMENTS })
	public PrivilegeAssignment savePrivilegeAssignment(PrivilegeAssignment privilegeAssignment) throws APIException;
	
	/**
	 * Deletes the given privilegeAssignment from the db (cannot be undone)
	 * @param privilegeAssignment The privilegeAssignment to delete from the database
	 * @throws APIException
	 * @should delete the given privilegeAssignment from the database
	 */
	@Authorized( { PrivilegeConstants.MANAGE_PRIVILEGE_ASSIGNMENTS })
	public void purgePrivilegeAssignment(PrivilegeAssignment privilegeAssignment) throws APIException;
	
	//*****PolicyImpl*****//
	
	/**
	 * Gets the policyImpl by Id
	 * @param policyImplId 
	 * @return The policyImpl object with the given id
	 * @throws APIException
	 * @should return the policyImpl with the given id if it is in the database
	 * @should return null if there is no policyImpl object in the database, with the given id
	 */
	public PolicyImpl getPolicyImpl(Integer policyImplId) throws APIException;
	
	/**
	 * Saves the given policyImpl object to the database
	 * @param policyImpl The policyImpl object to save
	 * @return The policyImpl saved in the database
	 * @throws APIException
	 * @should Save the given policyImpl object in the database
	 */
	public PolicyImpl savePolicyImpl(PolicyImpl policyImpl) throws APIException;
	
	/**
	 * Deletes the given policyImpl object from the database (NOTE: this is not reversible)
	 * @param policyImpl The policyImpl object to delete from the database
	 * @throws APIException
	 * @should Delete the given policyImpl object from the database
	 */
	public void purgePolicyImpl(PolicyImpl policyImpl) throws APIException;
	
	//*****HybridFormula*****//
	
	/**
	 * Gets Hybrid Formula by id
	 * @param hybridFormulaId
	 * @return The hybrid formula with the given id
	 * @should return the hybrid formula with the given id
	 * @should return null if the given id does not match any hybrid formula in the db
	 */
	@Transactional(readOnly = true)
	public HybridFormula getHybridFormula(Integer hybridFormulaId) throws APIException;
	
	/**
	 * Gets Hybrid Formula by name
	 * @param hybridFormula the name of the hybrid formula
	 * @return The hybrid formula with the given name
	 * @should return the hybrid formula with the given name
	 * @should return null if the given name does not match any hybrid formula in the db
	 */
	@Transactional(readOnly = true)
	public HybridFormula getHybridFormula(String hybridFormula) throws APIException;
	
	/**
	 * Returns all the Hybrid Formulas in the db
	 * @return All the Hybrid Formulas in db
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	public List<HybridFormula> getAllHybridFormulas() throws APIException;
	
	/**
	 * Saves the given hybrid formula to the db
	 * @param hybridFormula the hybrid formula to save in the db
	 * @return The hybridFormula saved in the db
	 * @throws APIException
	 * @should Save the given hybrid formula to the db
	 */
	@Authorized( { PrivilegeConstants.MANAGE_HYBRID_FORMULAS })
	public HybridFormula saveHybridFormula(HybridFormula hybridFormula) throws APIException;
	
	/**
	 * Deletes the given hybrid formula from the db (cannot be undone)
	 * @param hybridFormula The hybrid formula to delete from the database
	 * @throws APIException
	 * @should delete the given hybrid formula from the database
	 */
	@Authorized( { PrivilegeConstants.MANAGE_HYBRID_FORMULAS })
	public void purgeHybridFormula(HybridFormula hybridFormula) throws APIException;
}
