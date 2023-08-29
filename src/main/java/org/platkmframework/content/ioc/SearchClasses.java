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

import java.io.File;
import java.io.FileInputStream; 
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.platkmframework.annotation.AfterStartUp;
import org.platkmframework.annotation.Api;
import org.platkmframework.annotation.ApplicationConfig;
import org.platkmframework.annotation.AutoWired;
import org.platkmframework.annotation.ClassMethod;
import org.platkmframework.annotation.Component;
import org.platkmframework.annotation.Controller;
import org.platkmframework.annotation.CustomFilter;
import org.platkmframework.annotation.CustomServlet;
import org.platkmframework.annotation.Factory;
import org.platkmframework.annotation.PropertyFileInfo;
import org.platkmframework.annotation.Repository;
import org.platkmframework.annotation.Service;
import org.platkmframework.annotation.TruslyException;
import org.platkmframework.annotation.db.SearchFilterInfo;
import org.platkmframework.annotation.db.TableInfo;
import org.platkmframework.annotation.limit.ApplicationLimit;
import org.platkmframework.annotation.security.SecurityCofing;
import org.platkmframework.annotation.security.SecurityRole;
import org.platkmframework.content.ioc.exception.IoDCException;
import org.platkmframework.util.Util;
import org.platkmframework.util.error.InvocationException;
import org.platkmframework.util.reflection.ReflectionUtil; 
 


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
 * @author Eduardo Iglesias Taylor
 *
 */
public class SearchClasses implements IoDProcess
{
	public static final String C_SERVICE_ANNNOTATION_ACTIVE = "service.annotation.active";

	//private Properties prop; 
	//private List<String> servicePropertyValues;
	
	//private List<Class<?>> listController;
	//private Map<String, Object> mInterface;
	
	public SearchClasses() { 
		//this.servicePropertyValues = new ArrayList<>();
		
		//mInterface = new HashMap<>();
		//listController = new ArrayList<>();
		
	}
 
	@Override
	public Map<Object, List<Method>> process(String packageNames,
												String[]  packagesPrefix,
													ObjectReferece objectReferece) throws IoDCException{ 
		
		Map<Object, List<Method>> methods = new HashMap<>();
		Map<String, Object> mInterface = new HashMap<>();
		//checkServicePropertyValues();
		
		Map<Field, List<Object>> mField = new HashMap<>();  
		List<Constructor<?>> listPendingClass = new ArrayList<>();
		try{		 	
 
			if(StringUtils.isNotEmpty(packageNames)){
				
				//packageNames = packageNames.trim().replaceAll("\\s+",""); solo si se buscan los paquetes por l informacion en web.xml
				String[] arrPackages = packageNames.split(";"); //packageNames.split(",") solo si se buscan los paquetes por l informacion en web.xml
				if(arrPackages != null) {
					for (int i = 0; i < arrPackages.length; i++) {
						process(arrPackages[i], packagesPrefix, objectReferece, 
								mField, mInterface, listPendingClass, methods);
					}
				}
			}
		  
			//process pending classes
			_processPendingClasses(objectReferece, mField, mInterface, listPendingClass, true);
		     
			 //process autowired
			 _processAutowired(objectReferece, mField, mInterface); 
	        
		        
		} catch (Exception e) 
		{ 
			e.printStackTrace();
		}	
		mField.clear();
		mField = null;
		 
		return methods;
	}
		 

	/**private void checkServicePropertyValues() {
		
		if(servicePropertyValues.isEmpty() && prop!=null)
		{
			String serviceAnnotationActive = (String) prop.get(C_SERVICE_ANNNOTATION_ACTIVE);
			if(serviceAnnotationActive != null && !serviceAnnotationActive.trim().isEmpty())
			{
				String[] arrayServiceAnnotationActive = serviceAnnotationActive.split(";");
				if(arrayServiceAnnotationActive!=null && arrayServiceAnnotationActive.length>0)
				{
					for (int i = 0; i < arrayServiceAnnotationActive.length; i++) {
						servicePropertyValues.add(arrayServiceAnnotationActive[i]);
					}
				}
			}
		}
		
	}
*/

/**	private boolean _containJarName(String jarsNames, String fileName) {
		
		return StringUtils.isNotEmpty(fileName) && fileName.endsWith(".jar") && jarsNames.contains(fileName);
	}
*/

 
	/**
	 * 
	 * @param m
	 * @param mInterface
	 * @param mPendingClass
	 * @throws IoDCException 
	 * @throws InvocationException 
	 */
	private void _processPendingClasses(ObjectReferece objectReferece, 
										Map<Field, List<Object>> mField,
										Map<String, Object> mInterface,
										List<Constructor<?>> listPendingClass, boolean firstCheck) throws IoDCException, InvocationException 
	{
		System.out.println("PENDING...");
		Class<?>[] parameters;
		List<Object> listParam = new ArrayList<>();
		List<Constructor<?>> listPendingClassSecondCheck  = new ArrayList<>();
		Object obj;
		for (Constructor<?> constructor : listPendingClass)
		{
			parameters = constructor.getParameterTypes();
			if(parameters != null)
			{
				listParam.clear();
				for (int i = 0; i < parameters.length; i++) 
				{
					obj = objectReferece.getAllAnnotationObj().get(parameters[i].getName());
					if (obj == null){ 
						obj = mInterface.get(parameters[i].getName());
					}	
					if (obj == null){ 	
						if(firstCheck) {
							listPendingClassSecondCheck.add(constructor);
						}else {
							throw new IoDCException("Configuration error, there  are not and object to set in constructor parameter " + constructor.getName() + "-" +parameters[i].getName());
						}
					}else{
						listParam.add(obj); 
					}
				} 
				if(listParam.size() == parameters.length) {
					obj = ReflectionUtil.createInstance(constructor, listParam.toArray());
					
					System.out.println("PENDING... " + obj.getClass().getName() );
					System.out.println(firstCheck);
					objectReferece.addObject(obj);
					_checkAutoWired(obj, mField);
					_processPropertyInfo(obj, mField, objectReferece);
					_referencesByInterface(obj, mInterface);
				}
			}  
		} 
		
		if(!listPendingClassSecondCheck.isEmpty()) {
			_processPendingClasses(objectReferece, mField, mInterface, listPendingClassSecondCheck, false);
		}
		
	}
 
 
	private void process(String packageName,
							String[]  packagesPrefix,
						 ObjectReferece objectReferece,
						Map<Field, List<Object>> mField,
						Map<String, Object> mInterface,
						List<Constructor<?>> listPendingClass,
						Map<Object, List<Method>> methods) throws IoDCException, InvocationException, URISyntaxException{
		
		//solo si se lee la informacion de paquetes en el web.xml
		//String pathPackageName = "/" + packageName.replace(".", "/");
		//URL url = SearchClasses.class.getResource(pathPackageName);
		//URL url = SearchClasses.class.getResource(packageName);
		
		File url = new File(packageName);
		//if(url != null)
		if(url != null && url.exists())
			//if(!"jar".equals(url.getProtocol()))
			if(!url.getName().endsWith(".jar"))
			{
				try {
					//if("com.platkm.app.account.system.domain.service".equals(packageName)) {
					//	int a = 1;
					//}
					//solo si se lee la informacion de paquetes en el web.xml
					//System.out.println(packageName + " " + url.toURI());
					System.out.println(packageName);
					//File folder = new File(url.toURI()); 
					//_process(packageName, folder, m, mField, mInterface, listPendingClass, mEntities);	
					_process(packageName, packagesPrefix, url, objectReferece, mField, mInterface, listPendingClass, methods);	
				}catch(Exception e) {
					throw new IoDCException(e.getMessage() + "- " + url.getName());
				} 
			}else {
				try {
					//solo si se lee la informacion de paquetes en el web.xml
					//String zipFilePath = url.getFile().replace("file:/", "").replace("!" + pathPackageName, ""); //TODO PARCHE no se sabe como agarrar el camino limpiamente
					//System.out.println(zipFilePath);
					//ZipInputStream zip = new ZipInputStream(new FileInputStream(zipFilePath));
					
					ZipInputStream zip = new ZipInputStream(new FileInputStream(packageName));
					String className;
					for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
					    if (!entry.isDirectory() && entry.getName().endsWith(".class")){ 
					    	className = entry.getName().substring(0, entry.getName().lastIndexOf(".")).replace('/', '.'); 
					    	if(containsPrefix(packagesPrefix, className)) {
						    	try {
						    		Class<?> class1 = Class.forName(className, true, Thread.currentThread().getContextClassLoader()); 
							        _classProcess(class1, objectReferece, mField, mInterface, listPendingClass, methods); 
						    	}catch (ClassNotFoundException e) {
									 System.out.println(className +  " - Exception NOT FOUND");
								} 
					    	}
					    }
					}
					zip.close(); 
				} catch (Exception e) { 
					e.printStackTrace();
				}
			}
	}
 
 
	/**
	 * 
	 * @param packageName
	 * @param folder
	 * @param m
	 * @param aux
	 * @throws URISyntaxException
	 * @throws ClassNotFoundException
	 * @throws InvocationException
	 * @throws IoDCException 
	 */
	protected  void _process(String packageName, 
							String[]  packagesPrefix,
						  File folder, 
						  ObjectReferece objectReferece, 
						  Map<Field, List<Object>> mField,
						  Map<String, Object> mInterface,
						  List<Constructor<?>> listPendingClass,
						  Map<Object, List<Method>> methods) throws InvocationException, IoDCException{  
		try 
		{
			File[] files = folder.listFiles();
			if(files != null) 
				for (int i = 0; i < files.length; i++) 
				{
					File file = files[i];
					if(file.exists()) 
						if(file.isDirectory()) 
							_process(packageName + "." + file.getName(), packagesPrefix, file, objectReferece,
									 mField, mInterface, listPendingClass, methods);
						else if(file.getName().endsWith(".class"))
						{
							String classNamePackage = packageName + "." + file.getName().substring(0, file.getName().lastIndexOf("."));
							//esto es porque no se esta buscando la informacion en el web.xml,  sale el camino con paquete y nombre de clase
							classNamePackage = classNamePackage.substring(classNamePackage.indexOf(".") + 1);
							if( containsPrefix(packagesPrefix, classNamePackage) ) {
								Class<?> class1 = Class.forName(classNamePackage,true, Thread.currentThread().getContextClassLoader()); 
								_classProcess(class1, objectReferece, mField, mInterface, listPendingClass, methods); 
							} 
						} 
				}
			 
		} catch (ClassNotFoundException e) 
		{ 
			e.printStackTrace();
			throw new InvocationException("Error loading the project objetcts");
		}
	}

	private boolean containsPrefix(String[] packagesPrefix, String classNamePackage) {
		
		if( packagesPrefix != null && StringUtils.isNotBlank(classNamePackage)) { 
			for (int i = 0; i < packagesPrefix.length; i++) {
				if(classNamePackage.startsWith(packagesPrefix[i])) return true;
			}
		} 
		return false;
	}

	private void _classProcess(Class<?> class1,
								ObjectReferece objectReferece, 
								Map<Field, List<Object>> mField,
								Map<String, Object> mInterface,
								List<Constructor<?>> listPendingClass,
								Map<Object, List<Method>> methods) throws InvocationException, IoDCException {
		/**if(class1.isAnnotationPresent(Controller.class) && 
				!Controller.Scope.APPLICATION.equals(class1.getAnnotation(Controller.class).scope()))
			//listController.add(class1);
		else*/
		{
			if(class1.isAnnotationPresent(Factory.class) ||  
			   class1.isAnnotationPresent(Component.class) ||
			   isServicesAndActiveChecked(class1, objectReferece) ||
			   class1.isAnnotationPresent(Api.class) || 
			   class1.isAnnotationPresent(Controller.class)|| 
			   class1.isAnnotationPresent(Repository.class)|| 
			   class1.isAnnotationPresent(CustomServlet.class)|| 
			   class1.isAnnotationPresent(CustomFilter.class)|| 
			   isSecurityCofingAndType(class1, objectReferece)){
				
				if(!_hasCosntructorParam(class1,listPendingClass)){ 
					Object ob = ReflectionUtil.createInstance(class1); 
					if(class1.isAnnotationPresent(Api.class)) {
						processApiKey(objectReferece, ob);
					}else {
						objectReferece.addObject(ob); 
					}
						
					_checkAutoWired(ob, mField);
					_processPropertyInfo(ob, mField, objectReferece);
					_referencesByInterface(ob, mInterface);
					System.out.println(class1.getName());
				}
			}else{
				
				if(class1.isAnnotationPresent(TableInfo.class)) {
					objectReferece.getEntities().put(class1.getAnnotation(TableInfo.class).code(), class1);
				}
				
				if(class1.isAnnotationPresent(ApplicationLimit.class)) { 
					Optional<Object> obLimit = objectReferece.getLimits().stream().filter(o -> o.getClass().getName().equals(class1.getName())).findFirst(); 
					if(!obLimit.isPresent()) {
						objectReferece.getLimits().add(ReflectionUtil.createInstance(class1));
					} 
				}
				
				if(class1.isAnnotationPresent(SearchFilterInfo.class)) {
					objectReferece.getmSearchFilters().put(class1.getAnnotation(SearchFilterInfo.class).code(), class1);
				}
				
				if(class1.isAnnotationPresent(TruslyException.class)) {
					objectReferece.getExceptions().add(class1.getName());
				}
			} 
			
			if(class1.isAnnotationPresent(ApplicationConfig.class)){
				findMethodsToExecute(class1, methods);
			}
			
		}
		
	}


	private void processApiKey(ObjectReferece objectReferece, Object ob) {
		
		Method[] methods = ob.getClass().getMethods();
		if(methods != null) {
			Method method;
			ClassMethod classMethod; 
			String key = "";
			String[] roles = new String[] {};
			Api api = ob.getClass().getAnnotation(Api.class);
			
			if(ob.getClass().isAnnotationPresent(SecurityRole.class) &&
					ob.getClass().getAnnotation(SecurityRole.class).name() != null){
				roles = ArrayUtils.addAll(roles, ob.getClass().getAnnotation(SecurityRole.class).name());
			}
			
			for (int i = 0; i < methods.length; i++) {
				method = methods[i];
				if(method.isAnnotationPresent(ClassMethod.class)) {
					classMethod = method.getAnnotation(ClassMethod.class);
					key =  "{" + classMethod.method().name() + ":" +  api.path() + "/" + classMethod.name() + "}";
					
					if(method.isAnnotationPresent(SecurityRole.class) &&
							method.getAnnotation(SecurityRole.class).name() != null){
						roles = ArrayUtils.addAll(roles, method.getAnnotation(SecurityRole.class).name());
					}
					objectReferece.addApiInfo(key, ob, method, roles);  
				}
			}
		}
	}


	private void findMethodsToExecute(Class<?> class1, Map<Object, List<Method>> methods) throws InvocationException {
		Object ob = ReflectionUtil.createInstance(class1); 
		List<Method> list = new  ArrayList<>();
		for (Method method : ob.getClass().getMethods()) {
			if(method.isAnnotationPresent(AfterStartUp.class)){
				list.add(method);
			}
		}
		if(list.size() >0) {
			methods.put(ob, list);
		}
	}


	private boolean isServicesAndActiveChecked(Class<?> class1, ObjectReferece objectReferece) {
		
		if(!class1.isAnnotationPresent(Service.class)) return false;
		
		Service service = class1.getAnnotation(Service.class);
		if(service.key().isEmpty() || service.value().isEmpty()) return true;
		
		Object value = objectReferece.getProp().get(service.key());
		
		return value !=null && service.value().equals(value.toString()); 
	}
	
	
	private boolean isSecurityCofingAndType(Class<?> class1, ObjectReferece objectReferece) {
		
		if(!class1.isAnnotationPresent(SecurityCofing.class)) return false;
		
		SecurityCofing securityCofing = class1.getAnnotation(SecurityCofing.class);
		if(securityCofing.type().isEmpty()) return false;
		
		Object value = objectReferece.getProp().get("org.platkmframework.security.type");
		
		return value !=null && securityCofing.type().equals(value.toString()); 
	}


	/**
	 * return the info to the user session
	 * @param listController
	 * @return
	 * @throws InvocationException 
	 * @throws IoDCException 
	 */
/**	public Map<String, Object> processController(Map<String, Object> mScopeApp, 
												  Map<String, Object> mInterface,
									              List<Class<?>>  listController) throws InvocationException, IoDCException{
		Map<String, Object> mapController = new HashMap<>();
		
		if(listController != null){
			Class<?> class1;
			Object ob;
			Map<Field, List<Object>> mField = new HashMap<>();
			
			for (int i = 0; i < listController.size(); i++) 
			{
				class1 = listController.get(i);
				ob = ReflectionUtil.createInstance(class1);  
				mapController.put(ob.getClass().getName(), ob);
				
				_checkAutoWired(ob, mField);  
			 }
			
			 _processAutowired(mScopeApp, mField,  mInterface);
		}
		return mapController;
		
	}
	*/
	/**
	 * 
	 * @param class1
	 * @param mPendingClass
	 * @return
	 */
	private boolean _hasCosntructorParam(Class<?> class1, List<Constructor<?>> listPendingClass){
		Constructor<?>[] constructors = class1.getConstructors();
		if(constructors != null && constructors.length>0)
		{
			Constructor<?> constuctor = constructors[0];
			Class<?>[] param = constuctor.getParameterTypes();
			if(param != null && param.length>0)
			{
				listPendingClass.add(constructors[0]);
				return true;
			}
		}
		return false;
	}

	
	/**
	 * 
	 * @param ob
	 * @param mInterface
	 */
	private void _referencesByInterface(Object ob, Map<String, Object> mInterface){
		Class<?>[] interfaces = ob.getClass().getInterfaces();
		if(interfaces!=null)
			for (int i = 0; i < interfaces.length; i++) 
				mInterface.put(interfaces[i].getName(), ob);  
	}

	/**
	 * 
	 * @param ob
	 * @param aux
	 * @throws IoDCException 
	 */
	private void _checkAutoWired(Object ob, Map<Field, List<Object>> aux) throws IoDCException{
 
		List<Field> fields = ReflectionUtil.getAllFieldHeritage(ob.getClass());
		for (int i = 0; i < fields.size(); i++) 
		{
			Field f = fields.get(i);
			if(f.isAnnotationPresent(AutoWired.class)) 
			{
				if(!aux.containsKey(f)) aux.put(f, new ArrayList<>()); 
				aux.get(f).add(ob); 
			}else if(f.getAnnotations() != null && f.getAnnotations().length>0) {
				checkCustomIoD(ob, f);
			}
		} 
	}	
 

	protected void checkCustomIoD(Object ob, Field f) throws IoDCException{ 
		
	}


	private void _processPropertyInfo(Object obj, Map<Field, List<Object>> mField, ObjectReferece objectReferece) throws InvocationException {
		
		if(objectReferece.getProp() !=null){
			List<Field> fields = ReflectionUtil.getAllFieldHeritage(obj.getClass());
			PropertyFileInfo propertyFileInfo; 
			boolean accessValue;
			Field f;
			for (int i = 0; i < fields.size(); i++){
				f = fields.get(i);
				if(f.isAnnotationPresent(PropertyFileInfo.class)){
					propertyFileInfo = f.getAnnotation(PropertyFileInfo.class);
					accessValue = f.canAccess(obj);
					f.setAccessible(true); 
				    try {
						f.set(obj, getPropertyFileInfoValue(f, propertyFileInfo.name(), objectReferece));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace(); 
						throw new InvocationException("setting property error-> " + e.getMessage());
					} 
				    f.setAccessible(accessValue); 
				}	 
			} 
		}
		
	}
	
	
	private Object getPropertyFileInfoValue(Field field, String propertyKey, ObjectReferece objectReferece) throws InvocationException {
		 
		if(propertyKey.indexOf("$")>=0) 
			propertyKey = Util.checkingArgs(propertyKey, objectReferece.getProp());
			
		String value = objectReferece.getProp().getProperty(propertyKey);
		if(StringUtils.isBlank(value)) return null; 
		 
		return  ReflectionUtil.getRealAttributeValue(field, Util.checkingArgs(value, objectReferece.getProp()));
		
	}


	/**
	 * 
	 * @param aux: Field to set information autowired
	 * @throws IoDCException 
	 * @throws InvocationException 
	 */
	private void _processAutowired(ObjectReferece objectReferece, 
								   Map<Field, List<Object>> mField,
								   Map<String, Object> mInterface) throws IoDCException, InvocationException 
	{
		try 
		{
			Field f   = null;
			List<Object> objList = null;
			boolean accessValue;
			for (Map.Entry<Field, List<Object>> entry : mField.entrySet())
			{
				f  = entry.getKey();
				objList = entry.getValue();
				
				String obClassName = f.getType().getName();
				Object obAutowire  = objectReferece.getAllAnnotationObj().get(obClassName);
				if(obAutowire == null)
					obAutowire = mInterface.get(obClassName);
				if(obAutowire == null)
					   throw new IoDCException("Configuration error, there  are not and object to set in field " + f.getDeclaringClass() + " " + obClassName + " " +f.getName() );
  	 
				//set objeto to the field
				
				for (int i = 0; i < objList.size(); i++)
				{
					accessValue = f.canAccess(objList.get(i));
					f.setAccessible(true);
					f.set(objList.get(i), obAutowire); 
					f.setAccessible(accessValue);
				} 
				
			}	
			
		} catch (IllegalArgumentException | IllegalAccessException e) 
		{
			e.printStackTrace();
			throw new InvocationException("autowired error-> " + e.getMessage());
		}
		
	}
	
 
	
}
