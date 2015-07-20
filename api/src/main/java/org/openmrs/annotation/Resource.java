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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use to specify a method name to get the resource owners. 
 * ex. If you are dealing with the PersonName object, then this annotation
 * would be used to specify the getPerson method in PersonName object by setting 
 * Resource({ "getPerson" }).
 * If the annotation is not used correctly then an APIException is thrown while
 * Rebac Authorization is being checked.
 * 
 * The value can be used to specify multiple methods.
 * ex. If you are dealing with the Relationship object, then there are 2 possible
 * resource owners, and to check both the annotation should be used the following way
 * ResourceOWner({ "getPersonA" , "getPersonB" }).
 */

@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Resource {

}
