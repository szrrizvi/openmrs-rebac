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
package org.openmrs.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Provides tools to be used with the {@link Resource} annotation
 */

public class ResourceAnnotationAttributes {
	
	/**
	 * Determine if this method has the Resource annotation even on it
	 * 
	 * @param method the target method
	 * @return boolean true/false whether this method is annotated for OpenMRS
	 */
	public boolean hasResourceOwner(Method method) {
		
		// Check if method has annotation
		for (Annotation annotation : method.getAnnotations()) {
			if (annotation instanceof Resource) {
				return true;
			}
		}
		
		// Check if any parameter has annotation
		Annotation[][] annotations = method.getParameterAnnotations();
		for (Annotation[] paramAnnotations : annotations) {
			for (Annotation annotation : paramAnnotations) {
				if (annotation instanceof Resource) {
					return true;
				}
			}
		}
		
		return false;
	}
}
