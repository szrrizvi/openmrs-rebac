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

package org.openmrs.serialization;

import java.util.List;
import java.util.Map;

public class Foo {
	
	private String attributeString;
	
	private int attributeInt;
	
	private List<String> attributeList;
	
	private Map<Integer, String> attributeMap;
	
	Foo(String attributeString, int attributeInt) {
		this.attributeString = attributeString;
		this.attributeInt = attributeInt;
	}
	
	public String getAttributeString() {
		return attributeString;
	}
	
	public int getAttributeInt() {
		return attributeInt;
	}
	
	public List<String> getAttributeList() {
		return attributeList;
	}
	
	public void setAttributeList(List<String> attributeList) {
		this.attributeList = attributeList;
	}
	
	public Map<Integer, String> getAttributeMap() {
		return attributeMap;
	}
	
	public void setAttributeMap(Map<Integer, String> attributeMap) {
		this.attributeMap = attributeMap;
	}
	
}
