/*******************************************************************************
 *   Copyright(c) 2023 the original author or authors.
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *  
 *        https://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *******************************************************************************/
package org.platkmframework.content.ioc;
 
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class ObjectReferece {
	 
	private Map<String, Object> allAnnotationObj;
	private Map<String, Class<?>> mEntities;
	private Map<String, Class<?>> mSearchFilters;
	private Map<String, ApiMethodInfo> apiMethod; //the key  is the api path
	private Properties prop;
	private List<String> exceptions;
	private List<Object> limits;
	 
	public ObjectReferece() {
		super(); 
	}
	 
	public void addObject(Object ob) {
		getAllAnnotationObj().put(ob.getClass().getName(), ob); 
	}
	
	public void addApiInfo(String key, Object obj,  Method method, String[] roles) {  
		getApiMethod().put(key, new ApiMethodInfo(obj, method, roles)); 
	}
	 
	public Map<String, Object> getAllAnnotationObj() {
		if(this.allAnnotationObj == null) this.allAnnotationObj =  new HashMap<>();
		return allAnnotationObj;
	}
	
	public void setAllAnnotationObj(Map<String, Object> allAnnotationObj) {
		this.allAnnotationObj = allAnnotationObj;
	}
	
	public Map<String, Class<?>> getEntities() {
		if(this.mEntities == null) this.mEntities = new HashMap<>();
		return mEntities;
	}
	
	public void setEntities(Map<String, Class<?>> mEntities) {
		this.mEntities = mEntities;
	}
	 
	public Map<String, Class<?>> getmSearchFilters() {
		if(this.mSearchFilters == null) this.mSearchFilters = new HashMap<>();
		return mSearchFilters;
	}

	public void setmSearchFilters(Map<String, Class<?>> mSearchFilters) {
		this.mSearchFilters = mSearchFilters;
	}

	public Map<String, ApiMethodInfo> getApiMethod() {
		if(this.apiMethod == null) this.apiMethod =  new HashMap<>();
		return apiMethod;
	}
 
	public Class getEntity(String key) {
		return getEntities().get(key); 
	}
	
	public Object getObject(String key) {
		return getAllAnnotationObj().get(key); 
	}
	
	public ApiMethodInfo getApiMethod(String key) {
		return getApiMethod().get(key); 
	}

	public Properties getProp() { 
		if(prop == null)
			prop = new Properties();
		
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public List<String> getExceptions() {
		if(this.exceptions == null) this.exceptions = new  ArrayList<>();
		return exceptions;
	}

	public void setExceptions(List<String> exceptions) {
		this.exceptions = exceptions;
	} 

	public List<Object> getLimits() {
		if(this.limits == null) this.limits = new  ArrayList<>();
		return limits;
	}

	public void setLimits(List<Object> limits) {
		this.limits = limits;
	}


}
