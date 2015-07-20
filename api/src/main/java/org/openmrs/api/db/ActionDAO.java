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

import org.openmrs.action.Action;

public interface ActionDAO {
	
	/**
	 * @see org.openmrs.api.ActionService#getAction(Integer)
	 */
	public Action getAction(Integer actionId) throws DAOException;
	
	/**
	 * @see org.openmrs.api.ActionService#getActionByUuid(String)
	 */
	public Action getActionByUuid(String uuid) throws DAOException;
	
	/**
	 * @see org.openmrs.api.ActionService#getAllActions()
	 */
	public List<Action> getAllActions() throws DAOException;
	
	/**
	 * @see org.openmrs.api.ActionService#saveAction(Action)
	 */
	public Action saveAction(Action action) throws DAOException;
	
	/**
	 * @see org.openmrs.api.ActionService#purgeAction(Action)
	 */
	public void purgeAction(Action action) throws DAOException;
}
