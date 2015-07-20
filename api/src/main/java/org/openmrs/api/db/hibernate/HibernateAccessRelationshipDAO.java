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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.AccessRelationship;
import org.openmrs.AccessRelationshipType;
import org.openmrs.Person;
import org.openmrs.api.db.AccessRelationshipDAO;
import org.openmrs.api.db.DAOException;

public class HibernateAccessRelationshipDAO implements AccessRelationshipDAO {
	
	protected final static Log log = LogFactory.getLog(HibernateAccessRelationshipDAO.class);
	
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
	 * @see org.openmrs.api.db.AccessRelationshipDAO#getAccessRelationship(java.lang.Integer)
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationship(java.lang.Integer)
	 */
	@Override
	public AccessRelationship getAccessRelationship(Integer accessRelationshipId) throws DAOException {
		
		return (AccessRelationship) sessionFactory.getCurrentSession().get(AccessRelationship.class, accessRelationshipId);
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#getAccessRelationshipByUuid(java.lang.String)
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipByUuid(java.lang.String)
	 */
	@Override
	public AccessRelationship getAccessRelationshipByUuid(String uuid) throws DAOException {
		return (AccessRelationship) sessionFactory.getCurrentSession().createQuery(
		    "from AccessRelationship acr where acr.uuid = :uuid").setString("uuid", uuid).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#getAllAccessRelationships(boolean)
	 * @see org.openmrs.api.AccessRelationshipService#getAllAccessRelationships(boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AccessRelationship> getAllAccessRelationships(boolean includeVoided) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AccessRelationship.class, "acr");
		
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#getAccessRelationships(org.openmrs.Person, 
	 * 		org.openmrs.Person, org.openmrs.AccessRelationshipType)
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationships(org.openmrs.Person, 
	 * 		org.openmrs.Person, org.openmrs.AccessRelationshipType)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AccessRelationship> getAccessRelationships(Person fromPerson, Person toPerson,
	        AccessRelationshipType accessRelType) throws DAOException {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AccessRelationship.class, "acr");
		
		if (fromPerson != null)
			criteria.add(Restrictions.eq("personA", fromPerson));
		if (toPerson != null)
			criteria.add(Restrictions.eq("personB", toPerson));
		if (accessRelType != null)
			criteria.add(Restrictions.eq("accessRelationshipType", accessRelType));
		
		criteria.add(Restrictions.eq("voided", false));
		
		List<AccessRelationship> list = criteria.list();
		
		return list;
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#getAccessRelationships(org.openmrs.Person, 
	 * 		org.opemnrs.Person, org.openmrs.AccessRelationshipType, java.util.Date, java.util.Date)
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationships(org.openmrs.Person, 
	 * 		org.openmrs.Person, org.openmrs.AccessRelationshipType, java.util.Date, java.util.Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AccessRelationship> getAccessRelationships(Person fromPerson, Person toPerson,
	        AccessRelationshipType accessRelType, Date startEffectiveDate, Date endEffectiveDate) throws DAOException {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AccessRelationship.class, "acr");
		
		if (fromPerson != null)
			criteria.add(Restrictions.eq("personA", fromPerson));
		if (toPerson != null)
			criteria.add(Restrictions.eq("personB", toPerson));
		if (accessRelType != null)
			criteria.add(Restrictions.eq("relationshipType", accessRelType));
		if (startEffectiveDate != null) {
			criteria.add(Restrictions.disjunction().add(
			    Restrictions.and(Restrictions.le("startDate", startEffectiveDate), Restrictions.ge("endDate",
			        startEffectiveDate))).add(
			    Restrictions.and(Restrictions.le("startDate", startEffectiveDate), Restrictions.isNull("endDate"))).add(
			    Restrictions.and(Restrictions.isNull("startDate"), Restrictions.ge("endDate", startEffectiveDate))).add(
			    Restrictions.and(Restrictions.isNull("startDate"), Restrictions.isNull("endDate"))));
		}
		if (endEffectiveDate != null) {
			criteria.add(Restrictions.disjunction().add(
			    Restrictions.and(Restrictions.le("startDate", endEffectiveDate), Restrictions
			            .ge("endDate", endEffectiveDate))).add(
			    Restrictions.and(Restrictions.le("startDate", endEffectiveDate), Restrictions.isNull("endDate"))).add(
			    Restrictions.and(Restrictions.isNull("startDate"), Restrictions.ge("endDate", endEffectiveDate))).add(
			    Restrictions.and(Restrictions.isNull("startDate"), Restrictions.isNull("endDate"))));
		}
		
		criteria.add(Restrictions.eq("voided", false));
		
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#getAllAccessRelationshipTypes()
	 * @see org.openmrs.api.AccessRelationshipService#getAllAccessRelationshipTypes()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AccessRelationshipType> getAllAccessRelationshipTypes() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AccessRelationshipType.class, "acrt");
		
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#getAccessRelationshipType(java.lang.Integer)
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipType(java.lang.Integer)
	 */
	@Override
	public AccessRelationshipType getAccessRelationshipType(Integer accessRelationshipTypeId) throws DAOException {
		return (AccessRelationshipType) sessionFactory.getCurrentSession().get(AccessRelationshipType.class,
		    accessRelationshipTypeId);
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#getAccessRelationshipTypeByUuid(java.lang.String)
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipTypeByUuid(java.lang.String)
	 */
	@Override
	public AccessRelationshipType getAccessRelationshipTypeByUuid(String uuid) throws DAOException {
		return (AccessRelationshipType) sessionFactory.getCurrentSession().createQuery(
		    "from AccessRelationshipType acrt where acrt.uuid = :uuid").setString("uuid", uuid).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#getAccessRelationshipTypes(java.lang.String)
	 * @see org.openmrs.api.AccessRelationshipService#getAccessRelationshipTypes(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AccessRelationshipType> getAccessRelationshipTypes(String accessRelationshipTypeName) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AccessRelationshipType.class);
		
		criteria.add(Restrictions.eq("name", accessRelationshipTypeName));
		
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#saveAccessRelationship(org.openmrs.AccessRelationship)
	 * @see org.openmrs.api.AccessRelationshipService#saveAccessRelationship(org.openmrs.AccessRelationship)
	 */
	@Override
	public AccessRelationship saveAccessRelationship(AccessRelationship accessRelationship) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(accessRelationship);
		return accessRelationship;
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#deleteAccessRelationship(org.openmrs.AccessRelationship)
	 * @see org.openmrs.api.AccessRelationshipService#purgeAccessRelationship(org.openmrs.AccessRelationship)
	 */
	@Override
	public void deleteAccessRelationship(AccessRelationship accessRelationship) throws DAOException {
		sessionFactory.getCurrentSession().delete(accessRelationship);
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#saveAccessRelationshipType(org.openmrs.AccessRelationshipType)
	 * @see org.openmrs.api.AccessRelationshipService#saveAccessRelationshipType(org.openmrs.AccessRelationshipType)
	 */
	@Override
	public AccessRelationshipType saveAccessRelationshipType(AccessRelationshipType accessRelationshipType)
	        throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(accessRelationshipType);
		System.out.println("SAVING: " + accessRelationshipType.getName());
		return accessRelationshipType;
	}
	
	/**
	 * @see org.openmrs.api.db.AccessRelationshipDAO#deleteAccessRelationshipType(org.openmrs.AccessRelationshipType)
	 * @see org.openmrs.api.AccessRelationshipService#purgeAccessRelationshipType(org.openmrs.AccessRelationshipType)
	 */
	@Override
	public void deleteAccessRelationshipType(AccessRelationshipType accessRelationshipType) throws DAOException {
		sessionFactory.getCurrentSession().delete(accessRelationshipType);
	}
}
