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

import org.openmrs.action.Action;
import org.openmrs.api.APIException;
import org.openmrs.api.ActionService;
import org.openmrs.api.db.ActionDAO;

/**
 * Default implementation of ActionService
 * 
 * @see ActionService
 */

public class ActionServiceImpl extends BaseOpenmrsService implements ActionService {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private ActionDAO dao;
	
	/**
	 * @see org.openmrs.api.ActionService#setActionDao(ActionDAO)
	 */
	@Override
	public void setActionDAO(ActionDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @see org.openmrs.api.ActionService#getAction(Integer)
	 */
	@Override
	public Action getAction(Integer actionId) throws APIException {
		return dao.getAction(actionId);
	}
	
	/**
	 * @see org.openmrs.api.ActionService#getActionByUuid(String)
	 */
	@Override
	public Action getActionByUuid(String uuid) throws APIException {
		return dao.getActionByUuid(uuid);
	}
	
	/**
	 * @see org.openmrs.api.ActionService#getAllActions()
	 */
	@Override
	public List<Action> getAllActions() throws APIException {
		return dao.getAllActions();
	}
	
	/**
	 * @see org.openmrs.api.ActionService#saveAction(Action)
	 */
	@Override
	public Action saveAction(Action action) throws APIException {
		return dao.saveAction(action);
	}
	
	/**
	 * @see org.openmrs.api.ActionService#purgeAction(Action)
	 */
	@Override
	public void purgeAction(Action action) throws APIException {
		dao.purgeAction(action);
	}
}
