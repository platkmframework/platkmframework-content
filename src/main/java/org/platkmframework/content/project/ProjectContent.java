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
package org.platkmframework.content.project;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.util.Properties; 
 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 



/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 * this class should be usefull for webservers o ms applications 
 *
 */
public class ProjectContent {
	
	private static final Logger logger = LogManager.getLogger(ProjectContent.class);
	
	/**private static final Object C_PROPERTIES_FILE_PATH  	= "PROPERTIES_FILE_PATH";    
	private static final String C_SYSTEM_EXCEPTION_CLASSES  = "SYSTEM_EXCEPTION_CLASSES";   
	private static final String C_APPLICATION_ENVIRONMENT   = "APPLICATION_ENVIRONMENT";
*/
	private static ProjectContent projectContent;
	
	//private Map<String, String> appProperties; //properties from the web.xml
	
	//private List<String> exceptions; //exceptions handler by the application to return result via rest
	//private Properties propertesFileInfo;  // resources properties of the system 
	
	Properties appProperties;
	
	private ProjectContent()
	{ 
		//this.exceptions = new ArrayList<>();
		appProperties   = new Properties();
		//propertesFileInfo = new Properties(); 
	}
		
	public static ProjectContent instance()
	{
		if(projectContent == null)
			projectContent = new ProjectContent(); 
		return projectContent;
	}
	
	public void loadApplicationProperties() throws IOException {  
		 
		InputStream inputStream = this.getClass().getResourceAsStream("/application.properties");
		if(inputStream == null) {
			throw new IOException("application.properties file not found -> ");
		}
		appProperties.load(inputStream); 
	}
	
	public void loadApplicationProperties(String[] files)  throws IOException {  
		 
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

	public void setAppProperties(Properties appProperties) {
		this.appProperties = appProperties;
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
