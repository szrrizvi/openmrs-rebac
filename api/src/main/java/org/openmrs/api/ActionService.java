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
import org.openmrs.annotation.Authorized;
import org.openmrs.api.db.ActionDAO;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains service layer methods for Action.
 */

@Transactional
public interface ActionService extends OpenmrsService {
	
	/**
	 * Sets the DAO for this service. This is done through spring injection
	 * 
	 * @param dao DAO for this service
	 */
	public void setActionDAO(ActionDAO dao);
	
	/**
	 * Get action by internal relationship identifier
	 * 
	 * @param actionId
	 * @return Action the action to match on or null if none found
	 * @throws APIException
	 * @should return action with given id
	 * @should return null when action with given id does not exist
	 */
	@Transactional(readOnly = true)
	public Action getAction(Integer actionId) throws APIException;
	
	/**
	 * Get Action by its UUID
	 * 
	 * @param uuid
	 * @return
	 * @should find object given valid uuid
	 * @should return null if no object found with given uuid
	 */
	@Transactional(readOnly = true)
	public Action getActionByUuid(String uuid) throws APIException;
	
	/**
	 * Get list of actions
	 * 
	 * @return Action list
	 * @throws APIException
	 * @return list of all actions
	 * @should return all actions
	 */
	@Transactional(readOnly = true)
	public List<Action> getAllActions() throws APIException;
	
	/**
	 * Create or update an action. Saves the given <code>action</code> to
	 * the database
	 * 
	 * @param action action to be created or updated
	 * @return action that was created or updated
	 * @throws APIException
	 * @should create new object when action id is null
	 * @should update existing object when action id is not null
	 */
	@Authorized( { PrivilegeConstants.MANAGE_ACTIONS })
	public Action saveAction(Action action) throws APIException;
	
	/**
	 * Purges an action from the database (cannot be undone)
	 * 
	 * @param action action to be purged from the database
	 * @throws APIException
	 * @should delete action from the database
	 */
	@Authorized( { PrivilegeConstants.MANAGE_ACTIONS })
	public void purgeAction(Action action) throws APIException;
	
}
