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
package org.openmrs.rebac.implicitrelations;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.AccessRelationship;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Person;

import ca.ucalgary.ispia.rebac.Direction;

/**
 * Represents an implicit relation identifier for the resource owners
 */
public class Owner implements ImplicitRelationIdentifier {
	
	@Override
	public Iterable<? extends BaseOpenmrsObject> findNeighbors(Object vertex, Direction direction) {
		
		if (!(vertex instanceof BaseOpenmrsData))
			return new ArrayList<BaseOpenmrsData>();
		
		BaseOpenmrsData bod = (BaseOpenmrsData) vertex;
		
		List<Person> neighbour = new ArrayList<Person>();
		neighbour.addAll(bod.getResourceOwners());
		
		return neighbour;
	}
	
	@Override
	public boolean supportsBackwardsDirection() {
		return false;
	}
}
