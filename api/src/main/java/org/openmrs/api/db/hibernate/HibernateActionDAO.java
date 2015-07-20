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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.openmrs.action.Action;
import org.openmrs.action.Effect;
import org.openmrs.action.Participant;
import org.openmrs.action.Precondition;
import org.openmrs.api.db.ActionDAO;
import org.openmrs.api.db.DAOException;

public class HibernateActionDAO implements ActionDAO {
	
	protected final static Log log = LogFactory.getLog(HibernateActionDAO.class);
	
	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Set session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.api.db.ActionDAO#getAction(Integer)
	 */
	@Override
	public Action getAction(Integer actionId) throws DAOException {
		Action action = (Action) sessionFactory.getCurrentSession().get(Action.class, actionId);
		
		return action;
	}
	
	/**
	 * @see org.openmrs.api.db.ActionDAO#getActionByUuid(String)
	 */
	@Override
	public Action getActionByUuid(String uuid) throws DAOException {
		return (Action) sessionFactory.getCurrentSession().createQuery("from Action a where a.uuid = :uuid").setString(
		    "uuid", uuid).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.api.db.ActionDAO#getAllActions()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Action> getAllActions() throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Action.class);
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.api.db.ActionDAO#saveAction(Action)
	 */
	@Override
	public Action saveAction(Action action) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(action);
		return action;
	}
	
	/**
	 * @see org.openmrs.api.db.ActionDAO#purgeAction(Action)
	 */
	@Override
	public void purgeAction(Action action) throws DAOException {
		
		// Delete all Effects
		Iterator<Effect> effects = action.getEffects().iterator();
		while (effects.hasNext()) {
			sessionFactory.getCurrentSession().delete(effects.next());
		}
		
		// Delete all Participants
		Iterator<Participant> participants = action.getParticipants().iterator();
		while (participants.hasNext()) {
			sessionFactory.getCurrentSession().delete(participants.next());
		}
		// Delete the action
		sessionFactory.getCurrentSession().delete(action);
	}
	
}
