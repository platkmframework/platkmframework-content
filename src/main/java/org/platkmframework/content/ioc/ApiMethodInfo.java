/*******************************************************************************
 * Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	 https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Eduardo Iglesias Taylor - initial API and implementation
 *******************************************************************************/
package org.platkmframework.content.ioc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.platkmframework.annotation.Api;
import org.platkmframework.annotation.security.SecurityRole;


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class ApiMethodInfo {
	
	private Object contObject;
	private Map<String, List<Method>> mapMethod;
	
	public ApiMethodInfo(Object contObject, Map<String, List<Method>> mapMethod) {
		super();
		this.contObject = contObject;
		this.mapMethod  = mapMethod;
	}

	public Map<String, List<Method>> getMapMethod() {
		if(mapMethod == null) mapMethod = new HashMap<>();
		return mapMethod;
	}

	public void setMapMethod(Map<String, List<Method>> mapMethod) {
		this.mapMethod = mapMethod;
	}

	public Object getContObject() {
		return contObject;
	}

	public void setContObject(Object contObject) {
		this.contObject = contObject;
	}
	
	public String getControllerPath() {
		return this.contObject.getClass().getAnnotation(Api.class).path();
	}
	
	public String[] getControllerRoles() {
		if(contObject.getClass().isAnnotationPresent(SecurityRole.class) &&
				contObject.getClass().getAnnotation(SecurityRole.class).name() != null){
			return contObject.getClass().getAnnotation(SecurityRole.class).name();
		}else return new String[] {};
	}
	
	
	public String[] getMethodRoles(Method method) {
		if(method.getClass().isAnnotationPresent(SecurityRole.class) &&
				method.getClass().getAnnotation(SecurityRole.class).name() != null){
			return method.getClass().getAnnotation(SecurityRole.class).name();
		}else return new String[] {};
	}


}
