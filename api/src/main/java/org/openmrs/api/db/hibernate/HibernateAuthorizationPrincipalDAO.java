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
package org.openmrs.api.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.AuthorizationPrincipal;
import org.openmrs.PrivilegeAssignment;
import org.openmrs.action.Action;
import org.openmrs.api.db.AuthorizationPrincipalDAO;
import org.openmrs.api.db.DAOException;
import org.openmrs.rebac.HybridFormula;
import org.openmrs.rebac.PolicyImpl;

public class HibernateAuthorizationPrincipalDAO implements AuthorizationPrincipalDAO {
	
	protected final static Log log = LogFactory.getLog(HibernateAuthorizationPrincipalDAO.class);
	
	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Set session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) throws DAOException {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAuthorizationPrincipal(String)
	 */
	@Override
	public AuthorizationPrincipal getAuthorizationPrincipal(String authorizationPrincipal) throws DAOException {
		
		return (AuthorizationPrincipal) sessionFactory.getCurrentSession().get(AuthorizationPrincipal.class,
		    authorizationPrincipal);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAllAuthorizationPrincipals()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AuthorizationPrincipal> getAllAuthorizationPrincipals() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AuthorizationPrincipal.class);
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#saveAuthorizationPrincipal(AuthorizationPrincipal)
	 */
	@Override
	public AuthorizationPrincipal saveAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal)
	        throws DAOException {
		
		sessionFactory.getCurrentSession().saveOrUpdate(authorizationPrincipal);
		return authorizationPrincipal;
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgeAuthorizationPrincipal(AuthorizationPrincipal)
	 */
	@Override
	public void purgeAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal) throws DAOException {
		
		sessionFactory.getCurrentSession().delete(authorizationPrincipal);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getPrivilegeAssignment(Integer)
	 */
	@Override
	public PrivilegeAssignment getPrivilegeAssignment(Integer privilegeAssignmentId) throws DAOException {
		
		return (PrivilegeAssignment) sessionFactory.getCurrentSession()
		        .get(PrivilegeAssignment.class, privilegeAssignmentId);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getPrivilegeAssignmentByAuthorizationPrincipal(AuthorizationPrincipal)
	 */
	@Override
	public PrivilegeAssignment getPrivilegeAssignmentByAuthorizationPrincipal(AuthorizationPrincipal authorizationPrincipal)
	        throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PrivilegeAssignment.class);
		criteria.add(Restrictions.eq("authorizationPrincipal", authorizationPrincipal));
		return (PrivilegeAssignment) criteria.uniqueResult();
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAllPrivilegeAssignments()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PrivilegeAssignment> getAllPrivilegeAssignments() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PrivilegeAssignment.class);
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#savePrivilegeAssignment(PrivilegeAssignment)
	 */
	@Override
	public PrivilegeAssignment savePrivilegeAssignment(PrivilegeAssignment privilegeAssignment) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(privilegeAssignment);
		return privilegeAssignment;
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgePrivilegeAssignment(PrivilegeAssignment)
	 */
	@Override
	public void purgePrivilegeAssignment(PrivilegeAssignment privilegeAssignment) throws DAOException {
		sessionFactory.getCurrentSession().delete(privilegeAssignment);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getPolicyImpl(Integer)
	 */
	@Override
	public PolicyImpl getPolicyImpl(Integer policyImplId) throws DAOException {
		return (PolicyImpl) sessionFactory.getCurrentSession().get(PolicyImpl.class, policyImplId);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#savePolicyImpl(PolicyImpl)
	 */
	@Override
	public PolicyImpl savePolicyImpl(PolicyImpl policyImpl) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(policyImpl);
		return policyImpl;
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgePolicyImpl(PolicyImpl)
	 */
	@Override
	public void purgePolicyImpl(PolicyImpl policyImpl) throws DAOException {
		sessionFactory.getCurrentSession().delete(policyImpl);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getHybridFormula(Integer)
	 */
	@Override
	public HybridFormula getHybridFormula(Integer hybridFormulaId) throws DAOException {
		
		return (HybridFormula) sessionFactory.getCurrentSession().get(HybridFormula.class, hybridFormulaId);
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getHybridFormula(String)
	 */
	@Override
	public HybridFormula getHybridFormula(String hybridFormula) throws DAOException {
		
		return (HybridFormula) sessionFactory.getCurrentSession().createQuery(
		    "from HybridFormula a where a.hybridFormula = :name").setString("name", hybridFormula).uniqueResult();
		
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#getAllHybridFormulas()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<HybridFormula> getAllHybridFormulas() throws DAOException {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HybridFormula.class);
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#saveHybridFormula(HybridFormula)
	 */
	@Override
	public HybridFormula saveHybridFormula(HybridFormula hybridFormula) throws DAOException {
		
		sessionFactory.getCurrentSession().saveOrUpdate(hybridFormula);
		return hybridFormula;
	}
	
	/**
	 * @see org.openmrs.api.AuthorizationPrincipalService#purgeHybridFormula(HybridFormula)
	 */
	@Override
	public void purgeHybridFormula(HybridFormula hybridFormula) throws DAOException {
		sessionFactory.getCurrentSession().delete(hybridFormula);
	}
	
}
