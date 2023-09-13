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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.platkmframework.content.ioc.exception.IoDCException;
    
  


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
		objectReferece = new ObjectReferece();
		//mInterface = new HashMap<>();
		//listScopeSession = new ArrayList<>();
	}
	
	public static ObjectContainer instance()
	{
		if(objectContainer == null)
			objectContainer = new ObjectContainer();
		
		return objectContainer;
	}
	
	public Object geApptScopeObj(String key)
	{
		return this.objectReferece.getObject(key);
	}

	public void process(String packageNames, String[] packagesPrefix, Properties prop, IoDProcess ioDProcess) throws IoDCException 
	{
		this.objectReferece.setProp(prop); 
		Map<Object, List<Method>> methods = ioDProcess.process(packageNames, packagesPrefix, this.objectReferece); 
		methods.forEach((o,l)-> {
			l.forEach(m->{
				try {
					m.invoke(o, new Object[]{}); 
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					System.out.print(-1);
				}
			});
		});
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

	public String getPropertyValue(String key) {
		return this.objectReferece.getProp().getProperty(key);
	}	
	
	public String getPropertyValue(String key, String defaultValue) {
		return this.objectReferece.getProp().getProperty(key, defaultValue);
	}
	 
	public Class<?> getEntityClassByCode(String code) {
		return this.objectReferece.getEntity(code);
	}
    
	public ApiMethodInfo getApiMehtodByKey(String key) {
		return this.objectReferece.getApiMethod(key);
	}
	
	public List<BeanMethodInfo> getBeansMethodByAnnotation(Class<? extends Annotation> annotation) {
		List<Class<? extends Annotation>> list = new ArrayList<>();
		list.add(annotation);
		return getBeansMethodByAnnotations(list);
	}
	
	public List<BeanMethodInfo> getBeansMethodByAnnotations(List<Class<? extends Annotation>> annotations) {
		return this.objectReferece.getBeansMethodByAnnotations(annotations);
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
	}*/

	public Class<?> getFilterInfoClassByCode(String code) { 
		return this.objectReferece.getmSearchFilters().get(code);
	}
	
	public Collection<Class<?>> getEntities() {
		return this.objectReferece.getEntities().values();
	}
	
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


	 
}
