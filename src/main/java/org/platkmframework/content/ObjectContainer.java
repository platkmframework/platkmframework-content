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
package org.platkmframework.content;
    
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.platkmframework.doi.data.BeanFieldInfo;
import org.platkmframework.doi.data.BeanMethodInfo;
import org.platkmframework.doi.data.ObjectReferece; 

/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 *  Read sources to seeking classes with annotations
 *  Particular annotations
 *  -Controller:used to session scope
 *  -Path:used to application scope
 *  
 *  Controller: They are instanced in user login successed
 *
 */
public class ObjectContainer{
	 
	//llave paquete a partir de donde leer clases para el proceso HTTP desde el IDE
	public static final  String C_APP_OBJECT_PACKAGE  = "APP_OBJECT_PACKAGE";
	public static final  String C_APP_OBJECT_JAR      = "APP_OBJECT_JAR";
	
	private static ObjectContainer objectContainer;
	private ObjectReferece objectReferece;
	

	//private List<Class<?>> listScopeSession;
	//private Map<String, Object> mInterface;
	
	private ObjectContainer()
	{
		
		
		//mInterface = new HashMap<>();
		//listScopeSession = new ArrayList<>();
	}
	
	public static ObjectContainer instance()
	{
		if(objectContainer == null)
			objectContainer = new ObjectContainer();
		
		return objectContainer;
	}
	
	public void setReference(ObjectReferece ref) {
		objectReferece = ref;
	}
	
	public Object geApptScopeObj(String key)
	{
		return this.objectReferece.getObject(key);
	}

	public Object geApptScopeObj(Class<?> class1) 
	{ 
		return geApptScopeObj(class1.getName());
	}
	public List<Object> getListObjectByAnnontation(Class<? extends Annotation> annotation) 
	{ 
		return this.objectReferece.getObjectsByAnnotation(annotation);
	}
 
	public List<Object> getListObjectByAnnontationAndInstance(Class<? extends Annotation> annotation, Class<?> classInstance) 
	{ 
		return  this.objectReferece.getListObjectByAnnontationAndInstance(annotation, classInstance);
	} 
	
	public List<Object> getListObjectByInstance(Class<?> classInstance) 
	{ 
		return  this.objectReferece.getListObjectByInstance(classInstance);
	} 

	public String getPropertyValue(String key) {
		return this.objectReferece.getProp().getProperty(key);
	}	
	
	public String getPropertyValue(String key, String defaultValue) {
		return this.objectReferece.getProp().getProperty(key, defaultValue);
	}
    
	public String getApiControllerInfo(String path){
		return objectReferece.getApiMethod().get(path);
	}
	
	public List<String> getApiPathVariable(String pathVariable){
		 return objectReferece.getPathVarialbeApiMethod().get(pathVariable);
	}
	
	public Object getController(String controllerClassName) {
		return objectReferece.getController(controllerClassName);
	}
	
	/*
	 * public List<Object> getControllersByAnnotation(Class<? extends Annotation>
	 * annotation) {
	 * 
	 * List<Object> list = new ArrayList<Object>(); Map<String, ApiMethodInfo> map =
	 * this.objectReferece.getApiMethod(); map.forEach((k,v)-> {
	 * if(!list.contains(v.getContObject()) &&
	 * v.getContObject().getClass().isAnnotationPresent(annotation))list.add(v.
	 * getContObject()); });
	 * 
	 * return list; }
	 */
	
	public List<BeanMethodInfo> getBeansMethodByAnnotation(Class<? extends Annotation> annotation) {
		List<Class<? extends Annotation>> list = new ArrayList<>();
		list.add(annotation);
		return getBeansMethodByAnnotations(list);
	}
	
	public List<BeanMethodInfo> getBeansMethodByAnnotations(List<Class<? extends Annotation>> annotations) {
		return this.objectReferece.getBeansMethodByAnnotations(annotations);
	}
	
	public Object getObjectByKey(String key) {
		return this.objectReferece.getObject(key);
	}
	
	public List<BeanFieldInfo> getBeanFieldInfoByAnnotation(Class<? extends Annotation> annotation) {
		List<Class<? extends Annotation>> list = new ArrayList<>();
		list.add(annotation);
		return getBeanFieldInfoByAnnotations(list);
	}
	
	public List<BeanFieldInfo> getBeanFieldInfoByAnnotations(List<Class<? extends Annotation>> annotations) {
		return this.objectReferece.getBeansFieldByAnnotations(annotations);
	}
	
	/**
	 * 
	 * @return
	 * @throws ReflectionError
	 * @throws IOCException
	 
	public Map<String, Object> getControllers() throws ReflectionError, IOCException
	{
		SearchClasses searchClasses = new SearchClasses();
		return searchClasses.processController(mScopeApp, mInterface, listScopeSession); 
	}
	
	public List<Class<?>> getEntities() {
		return this.objectReferece.getEntities();
	}
	*/
	public void setException(String exceptionClass) { 
		this.objectReferece.getExceptions().add(exceptionClass);
	}
	
	public boolean containsException(Object exception) { 
		return this.objectReferece.getExceptions().contains(exception.getClass().getName());
	}
	
	public Object getLimit(Class<?> limitClass) {
		
		for (Object obj : this.objectReferece.getLimits()) {
			if(obj.getClass().getName().equals(limitClass.getName())) return obj;
		} 
		return null;
	}

	public Object getSearchMapInfo(String code) { 
		return this.objectReferece.getSearchFilter().get(code);
	}
	public List<Object> getFunctionals() { 
		return objectReferece.getFunctionals();
	}
	
	public Object getCustomInfoByKey(String key) {
		return objectReferece.getCustomInfo().get(key);
	}
}
