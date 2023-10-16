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
 
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.platkmframework.annotation.db.SearchFilter;


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class ObjectReferece {
	 
	private Map<String, List<ObjectInfo>> allAnnotationObj;
	private List<Class<?>> entities;
	private Map<String, ApiMethodInfo> apiMethod; //the key  is the api path
	private Properties prop;
	private List<String> exceptions;
	private List<Object> limits;
	private List<BeanMethodInfo> beanMethods;
	private List<BeanFieldInfo> beanFields;
	private Map<String, SearchFilter> mSearchFilter;
	 
	public ObjectReferece() {
		super(); 
	}
	 
	public void addObject(String key, Object ob) {
		List<ObjectInfo> list = getAllAnnotationObj().get(ob.getClass().getName());
		if(list == null) {
			list = new ArrayList<ObjectInfo>();
			getAllAnnotationObj().put(ob.getClass().getName(), list); 
		}
		getAllAnnotationObj().get(ob.getClass().getName()).add(new ObjectInfo(key, ob)); 
	}
	
	public Object getObject(String className, String key) {
		
		List<ObjectInfo> list = getAllAnnotationObj().get(className);
		if(list == null || list.size() == 0) return null;
		if(list.size() == 1)
			return list.get(0).getObj();
		else if(StringUtils.isBlank(key)){
			return list.get(0).getObj(); // the first one 
		}
		
		for (ObjectInfo objectInfo : list) {
			if(key.equalsIgnoreCase(objectInfo.getKey())) return objectInfo.getObj();
		}
		return null; 
	}
	
	public List<Object>  getObjectsByAnnotation(Class<? extends Annotation> annotation) {
		
		List<Object> objectList = new ArrayList<>();
		for (Map.Entry<String,  List<ObjectInfo> > entry : getAllAnnotationObj().entrySet())
		{ 
			for (ObjectInfo objInfo : entry.getValue()) {
				if(objInfo.getObj().getClass().isAnnotationPresent(annotation))
					objectList.add(objInfo.getObj()); 
			}
		}
		return objectList;
	}
	
	public List<Object> getListObjectByAnnontationAndInstance(Class<? extends Annotation> annotation, Class<?> classInstance) 
	{ 
		List<Object> objectList = new ArrayList<>();
		for (Map.Entry<String,  List<ObjectInfo> > entry : getAllAnnotationObj().entrySet())
		{ 
			for (ObjectInfo objInfo : entry.getValue()) {
				if(objInfo.getObj().getClass().isAnnotationPresent(annotation) && classInstance.isInstance(objInfo.getObj()) )
					objectList.add(objInfo.getObj()); 
			}
		}
		return objectList;
	}	
	
	public List<Object> getListObjectByInstance(Class<?> interfaceClass) 
	{ 
		List<Object> objectList = new ArrayList<>();
		for (Map.Entry<String,  List<ObjectInfo> > entry : getAllAnnotationObj().entrySet())
		{ 
			for (ObjectInfo objInfo : entry.getValue()) {
				if(interfaceClass.isInstance(objInfo) )
					objectList.add(objInfo.getObj()); 
			}
		}
		return objectList;
	}	
	
	
	public void addApiInfo(String api, Object obj,  Map<String, List<Method>>  methodMap) {  
		getApiMethod().put(api, new ApiMethodInfo(obj, methodMap)); 
	}
	
	public void addBeanMethod(Object obj, Method m) {
		getBeanMethods().add(new BeanMethodInfo(obj, m));
	}
	
	public void addBeanField(Object obj, Field f) {
		getBeanFields().add(new BeanFieldInfo(obj, f));
	}
	 
	private Map<String, List<ObjectInfo>> getAllAnnotationObj() {
		if(this.allAnnotationObj == null) this.allAnnotationObj =  new HashMap<>();
		return allAnnotationObj;
	}
	
	public List<Class<?>> getEntities() {
		if(this.entities == null) this.entities = new ArrayList<>();
		return entities;
	}
	
	public void setEntities(List<Class<?>> entities) {
		this.entities = entities;
	} 
	
	public Map<String, SearchFilter> getmSearchFilter() {
		if(this.mSearchFilter == null) this.mSearchFilter = new HashMap<>();
		return mSearchFilter;
	}

	public void setmSearchFilter(Map<String, SearchFilter> mSearchFilter) {
		this.mSearchFilter = mSearchFilter;
	}

	public Map<String, ApiMethodInfo> getApiMethod() {
		if(this.apiMethod == null) this.apiMethod =  new HashMap<>();
		return apiMethod;
	}
	 

	public List<BeanMethodInfo> getBeanMethods() {
		if(this.beanMethods == null) this.beanMethods =  new ArrayList<>();
		return beanMethods;
	}
	
	public List<BeanFieldInfo> getBeanFields(){
		if(this.beanFields == null) this.beanFields =  new ArrayList<>();
		return beanFields;
	}
  
	public Object getObject(String className) {
		return getObject(className, null); 
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

	public List<BeanMethodInfo> getBeansMethodByAnnotations(List<Class<? extends Annotation>> annotations) {
		List<BeanMethodInfo> list = new ArrayList<>();
		
		List<BeanMethodInfo> beanMethodList = getBeanMethods();
		for (BeanMethodInfo beanMethodInfo : beanMethodList) {
			
			for (Class<? extends Annotation> annotation : annotations) {
				if(beanMethodInfo.getMethod().isAnnotationPresent(annotation)) {
					list.add(beanMethodInfo);
					break;
				}
			}
		}
		return list;
	}
	
	public List<BeanFieldInfo> getBeansFieldByAnnotations(List<Class<? extends Annotation>> annotations) {
		List<BeanFieldInfo> list = new ArrayList<>();
		
		List<BeanFieldInfo> beanFieldInfoList = getBeanFields();
		for (BeanFieldInfo beanMethodInfo : beanFieldInfoList) {
			
			for (Class<? extends Annotation> annotation : annotations) {
				if(beanMethodInfo.getField().isAnnotationPresent(annotation)) {
					list.add(beanMethodInfo);
					break;
				}
			}
		}
		return list;
	}


}
