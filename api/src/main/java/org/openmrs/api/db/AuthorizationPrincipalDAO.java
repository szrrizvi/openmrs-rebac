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
package org.openmrs.api.db;

import java.util.List;

import org.openmrs.AuthorizationPrincipal;
import org.openmrs.PrivilegeAssignment;
import org.openmrs.api.APIException;
import org.openmrs.rebac.HybridFormula;
import org.openmrs.rebac.PolicyImpl;

public interface AuthorizationPrincipalDAO {
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAuthorizationPrincipal(String)
	 */
	public AuthorizationPrincipal getAuthorizationPrincipal(String authorizationPrincipal) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAllAuthorizationPrincipals()
	 */
	public List<AuthorizationPrincipal> getAllAuthorizationPrincipals() throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#saveAuthorizationPrincipal(AuthorizationPrincipal)
	 */
	public AuthorizationPrincipal saveAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal)
	        throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgeAuthorizationPrincipal(AuthorizationPrincipal)
	 */
	public void purgeAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getPrivilegeAssignment(Integer)
	 */
	public PrivilegeAssignment getPrivilegeAssignment(Integer privilegeAssignmentId) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getPrivilegeAssignmentByAuthorizationPrincipal(AuthorizationPrincipal)
	 */
	public PrivilegeAssignment getPrivilegeAssignmentByAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal)
	        throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAllPrivilegeAssignments()
	 */
	public List<PrivilegeAssignment> getAllPrivilegeAssignments() throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#savePrivilegeAssignment(PrivilegeAssignment)
	 */
	public PrivilegeAssignment savePrivilegeAssignment(PrivilegeAssignment privilegeAssignment) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgePrivilegeAssignment(PrivilegeAssignment)
	 */
	public void purgePrivilegeAssignment(PrivilegeAssignment privilegeAssignment) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getPolicyImpl(Integer)
	 */
	public PolicyImpl getPolicyImpl(Integer policyImplId) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#savePolicyImpl(PolicyImpl)
	 */
	public PolicyImpl savePolicyImpl(PolicyImpl policyImpl) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgePolicyImpl(PolicyImpl)
	 */
	public void purgePolicyImpl(PolicyImpl policyImpl) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getHybridFormula(Integer)
	 */
	public HybridFormula getHybridFormula(Integer hybridFormulaId) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getHybridFormula(String)
	 */
	public HybridFormula getHybridFormula(String hybridFormula) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAllHybridFormulas()
	 */
	public List<HybridFormula> getAllHybridFormulas() throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#saveHybridFormula(HybridFormula)
	 */
	public HybridFormula saveHybridFormula(HybridFormula hybridFormula) throws DAOException;
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgeHybridFormula(HybridFormula)
	 */
	public void purgeHybridFormula(HybridFormula hybridFormula) throws DAOException;
}
