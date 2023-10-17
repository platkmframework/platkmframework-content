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
package org.platkmframework.content.project;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.platkmframework.content.ioc.ContentPropertiesConstant; 



/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 * this class should be usefull for webservers o ms applications 
 *
 */
public class ProjectContent {
	
	/**private static final Object C_PROPERTIES_FILE_PATH  	= "PROPERTIES_FILE_PATH";    
	private static final String C_SYSTEM_EXCEPTION_CLASSES  = "SYSTEM_EXCEPTION_CLASSES";   
	private static final String C_APPLICATION_ENVIRONMENT   = "APPLICATION_ENVIRONMENT";
*/
	private static ProjectContent projectContent;
	
	//private Map<String, String> appProperties; //properties from the web.xml
	
	//private List<String> exceptions; //exceptions handler by the application to return result via rest
	//private Properties propertesFileInfo;  // resources properties of the system 
	
	Properties appProperties;
	private List<Object> filters;
	private List<Object> servlets;
	
	private ProjectContent()
	{ 
		appProperties   = new Properties();
		filters  = new ArrayList<>();
		servlets = new ArrayList<>();
		//this.exceptions = new ArrayList<>();
		//propertesFileInfo = new Properties(); 
	}
		
	public static ProjectContent instance()
	{
		if(projectContent == null)
			projectContent = new ProjectContent(); 
		return projectContent;
	}
	
	public ProjectContent projectName(String projectName) {
		appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_APPNAME, projectName);
		return this;
	}
	
	public ProjectContent server(String server) {
		appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_NAME, server);
		return this;
	}
 
	
	public ProjectContent port(String port) {
		appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_PORT, port);
		return this;
	}
	
	public ProjectContent contentPath(String contentPath) {
		appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_CONTENT_PATH, contentPath);
		return this;
	}
	
	public ProjectContent servletPath(String servletPath) {
		appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVLET_PLATH, servletPath);
		return this;
	}
	
	public ProjectContent publicPath(String publicPath) {
		appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_PUBLIC_PATH, publicPath);
		return this;
	}
	
	public ProjectContent stopKey(String stopKey) {
		appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_STOPKEY, stopKey);
		return this;
	}
	
	public ProjectContent corsOrigin(String origin) {
		appProperties.put(CorePropertyConstant.System_Access_Control_Allow_Origin, origin);
		return this;
	}
	
	public ProjectContent corsMethod(String methods) {
		appProperties.put(CorePropertyConstant.System_Access_Control_Allow_Methods, methods);
		return this;
	}
	
	public ProjectContent corsHeader(String header) {
		appProperties.put(CorePropertyConstant.System_Access_Control_Allow_Headers, header);
		return this;
	}
	
	public ProjectContent IvD(String packages) {
		appProperties.put(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_CONFIGURATION_PACKAGE_PREFIX, packages);
		return this;
	}
	
	public ProjectContent addPropertyFiles(String propertyfiles) {
		
		 if(StringUtils.isNotBlank(propertyfiles)){
			 try {
				loadApplicationProperties(propertyfiles.split(","));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return this;
	}
	
	public ProjectContent datetimeFormat(String format) {
		appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_FORMAT_DATETIME, format);
		return this;
	}
	
	public ProjectContent dateFormat(String format) {
		appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_FORMAT_DATE, format);
		return this;
	}
	
	public ProjectContent timeFormat(String format) {
		appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_FORMAT_TIME, format);
		return this;
	}
	
	public ProjectContent add(String key, Object obj) {
		appProperties.put(key, obj);
		return this;
	}
	
	public ProjectContent addFilter(Object filter) {
		filters.add(filter);
		return this;
	}
	
	public List<Object> getFilters() {
		return filters;
	}
	
	public ProjectContent addServlet(Object servlet) {
		servlets.add(servlet);
		return this;
	}
	
	public List<Object> getServlet() {
		return servlets;
	}

	/**	public void loadApplicationProperties() throws IOException {  
			 
			InputStream inputStream = this.getClass().getResourceAsStream("/application.properties");
			if(inputStream == null) {
				throw new IOException("application.properties file not found -> ");
			}
			appProperties.load(inputStream); 
		}
	*/	
	
	private void loadApplicationProperties(String[] files)  throws IOException {  
		if(files != null) {
			Properties properties = new Properties();
			InputStream inputStream;
			File file;
			for (int i = 0; i < files.length; i++) {
				inputStream = this.getClass().getResourceAsStream(files[i].trim());
				if(inputStream != null) {
					properties.load(inputStream); 
					appProperties.putAll(properties);
				}else{
					file = new File(files[i].trim());
					if(file != null && file.isFile()) {
						InputStream targetStream = new FileInputStream(file);
						properties.load(targetStream);
						appProperties.putAll(properties);
					}else
						throw new IOException(files[i] + "  file not found -> ");
				}
				properties.clear();
			}
		}
	}

	public Properties getAppProperties() {
		return appProperties;
	}

	/**public void setAppProperties(Properties appProperties) {
		this.appProperties = appProperties;
	}
*/
	public String parseValue(String value) {
		if(StringUtils.isBlank(value)) return null;
		
		for(Map.Entry<Object, Object> e : appProperties.entrySet()) {
			value = value.replace("${" + e.getKey().toString()  + "}", e.getValue().toString());
        }
		return value;
	}

	public String getProperty(String key) {
		return appProperties.getProperty(key, "");
	}
	
	public ProjectContent addProperty(String key, String value) {
		getAppProperties().put(key, value);
		return this;
	}
	
	
	
	/**public void init(ServletContextEvent servletContextEvent) throws Exception {
		  
		//by default, we load the init properties from web.xml for all the application
		loadInitProperties(servletContextEvent);
		
		//load  properties
		loadProperties(servletContextEvent); 
		
		//system controller exceptions
		loadExceptionsInfo(servletContextEvent); 
		 
	}
	
	public boolean existsException(Throwable e) {
		if(e == null) return false;
		return exceptions.contains(e.getClass().getName());
	}
   
/**	private void loadInitProperties(ServletContextEvent servletContextEvent){ 
		Enumeration<String> enumInitParam = servletContextEvent.getServletContext().getInitParameterNames();
		if(enumInitParam!=null) {
			String initParamKey; 
			while(enumInitParam.hasMoreElements()){
				initParamKey = enumInitParam.nextElement();
				initParam.put(initParamKey, servletContextEvent.getServletContext().getInitParameter(initParamKey));
			}
		}
	}
	
	private void loadProperties(ServletContextEvent servletContextEvent) throws Exception { 
		try {
			 
			String propertiesFilePath = initParam.get(C_PROPERTIES_FILE_PATH);
			if(StringUtils.isNotBlank(propertiesFilePath)) {
				String[] propPathArray = propertiesFilePath.split(",");
				if(propPathArray != null && propPathArray.length > 0){
					//String webRootPath = servletContextEvent.getServletContext().getRealPath("");
					for (int i = 0; i < propPathArray.length; i++) { 
						_loadApplicationProperties(propPathArray[i]);
					} 
				}
			} 
		}catch(Exception e)
		{
			logger.error(e); 
			throw e;
		} 
	}
	
	private void _loadApplicationProperties(String applicationPropertiesFilePath) throws IOException { 
		Properties prop = new Properties();
		
		logger.info(applicationPropertiesFilePath);
		InputStream inputStream = this.getClass().getResourceAsStream(applicationPropertiesFilePath);
		if(inputStream == null) {
			throw new IOException("file path not found -> " + applicationPropertiesFilePath);
		}
		
		prop.load(inputStream);
		propertesFileInfo.putAll(prop);
	}
	 
 
  
	private void loadExceptionsInfo(ServletContextEvent servletContextEvent) {
		String exceptions = servletContextEvent.getServletContext().getInitParameter(C_SYSTEM_EXCEPTION_CLASSES);
		if(StringUtils.isNotEmpty(exceptions))
		{
			String[] exceptionSplit = exceptions.split(","); 
			if(exceptionSplit!=null)
				for (int i = 0; i < exceptionSplit.length; i++)  
					this.exceptions.add(exceptionSplit[i]); 
		} 
	}	
 
	 
	public String getInitParam(String key) {
		return initParam.get(key);
	}
	
	public String getEnvironment() {
		String env =  getInitParam(C_APPLICATION_ENVIRONMENT);
		if(StringUtils.isBlank(env)) env = EnvironmentType.DEV.name();
		return env;
	}


	public Properties getPropertesFileInfo() {
		return propertesFileInfo;
	}
	*/
	
	  
}
