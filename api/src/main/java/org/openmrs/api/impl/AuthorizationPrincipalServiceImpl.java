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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.AuthorizationPrincipal;
import org.openmrs.PrivilegeAssignment;
import org.openmrs.api.APIException;
import org.openmrs.api.AuthorizationPrincipalService;
import org.openmrs.api.db.AuthorizationPrincipalDAO;
import org.openmrs.rebac.HybridFormula;
import org.openmrs.rebac.PolicyImpl;

/**
 * Contains methods pertaining to Authorization Principals in the system
 */
public class AuthorizationPrincipalServiceImpl extends BaseOpenmrsService implements AuthorizationPrincipalService {
	
	private static Log log = LogFactory.getLog(AuthorizationPrincipalServiceImpl.class);
	
	private AuthorizationPrincipalDAO dao;
	
	@Override
	public void setAuthorizationPrincipalDAO(AuthorizationPrincipalDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAuthorizationPrincipal(String)
	 */
	@Override
	public AuthorizationPrincipal getAuthorizationPrincipal(String authorizationPrincipal) throws APIException {
		
		return this.dao.getAuthorizationPrincipal(authorizationPrincipal);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAllAuthorizationPrincipals()
	 */
	@Override
	public List<AuthorizationPrincipal> getAllAuthorizationPrincipals() throws APIException {
		
		return this.dao.getAllAuthorizationPrincipals();
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#saveAuthorizationPrincipal(AuthorizationPrincipal)
	 */
	@Override
	public AuthorizationPrincipal saveAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal)
	        throws APIException {
		
		// Save the authorization principal
		AuthorizationPrincipal ap = this.dao.saveAuthorizationPrincipal(authorizationPrincipal);
		
		// Create the associated Privilege Assignment and save it
		PrivilegeAssignment pa = new PrivilegeAssignment(ap, null);
		savePrivilegeAssignment(pa);
		
		return ap;
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgeAuthorizationPrincipal(AuthorizationPrincipal)
	 */
	@Override
	public void purgeAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal) throws APIException {
		
		// First delete the privilege assignment associated with the authorization principal
		PrivilegeAssignment pa = getPrivilegeAssignmentByAuthorizationPrincipal(authorizationPrincipal);
		purgePrivilegeAssignment(pa);
		
		// Then delete the authorization principal
		this.dao.purgeAuthorizationPrincipal(authorizationPrincipal);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getPrivilegeAssignment(Integer)
	 */
	@Override
	public PrivilegeAssignment getPrivilegeAssignment(Integer privilegeAssignmentId) throws APIException {
		return this.dao.getPrivilegeAssignment(privilegeAssignmentId);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getPrivilegeAssignmentByAuthorizationPrincipal(AuthorizationPrincipal)
	 */
	@Override
	public PrivilegeAssignment getPrivilegeAssignmentByAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal)
	        throws APIException {
		return this.dao.getPrivilegeAssignmentByAuthorizationPrincipal(authorizationPrincipal);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAllPrivilegeAssignments()
	 */
	@Override
	public List<PrivilegeAssignment> getAllPrivilegeAssignments() throws APIException {
		return this.dao.getAllPrivilegeAssignments();
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#savePrivilegeAssignment(PrivilegeAssignment)
	 */
	@Override
	public PrivilegeAssignment savePrivilegeAssignment(PrivilegeAssignment privilegeAssignment) throws APIException {
		return this.dao.savePrivilegeAssignment(privilegeAssignment);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgePrivilegeAssignment(PrivilegeAssignment)
	 */
	@Override
	public void purgePrivilegeAssignment(PrivilegeAssignment privilegeAssignment) throws APIException {
		this.dao.purgePrivilegeAssignment(privilegeAssignment);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getPolicyImpl(Integer)
	 */
	@Override
	public PolicyImpl getPolicyImpl(Integer policyImplId) throws APIException {
		return this.dao.getPolicyImpl(policyImplId);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#savePolicyImpl(PolicyImpl)
	 */
	@Override
	public PolicyImpl savePolicyImpl(PolicyImpl policyImpl) throws APIException {
		return this.dao.savePolicyImpl(policyImpl);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgePolicyImpl(PolicyImpl)
	 */
	@Override
	public void purgePolicyImpl(PolicyImpl policyImpl) throws APIException {
		this.dao.purgePolicyImpl(policyImpl);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getHybridFormula(Integer)
	 */
	@Override
	public HybridFormula getHybridFormula(Integer hybridFormulaId) throws APIException {
		return this.dao.getHybridFormula(hybridFormulaId);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getHybridFormula(String)
	 */
	@Override
	public HybridFormula getHybridFormula(String hybridFormula) throws APIException {
		return this.dao.getHybridFormula(hybridFormula);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAllHybridFormulas()
	 */
	@Override
	public List<HybridFormula> getAllHybridFormulas() throws APIException {
		return this.dao.getAllHybridFormulas();
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#saveHybridFormula(HybridFormula)
	 */
	@Override
	public HybridFormula saveHybridFormula(HybridFormula hybridFormula) throws APIException {
		return this.dao.saveHybridFormula(hybridFormula);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgeHybridFormula(HybridFormula)
	 */
	@Override
	public void purgeHybridFormula(HybridFormula hybridFormula) throws APIException {
		this.dao.purgeHybridFormula(hybridFormula);
		
	}
	
}
