/**
 * ****************************************************************************
 *  Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	 https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Contributors:
 *  	Eduardo Iglesias Taylor - initial API and implementation
 * *****************************************************************************
 */
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
import org.platkmframework.content.init.BootInitializer;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 * this class should be usefull for webservers o ms applications
 */
public class ProjectContent {

    /**
     * Atributo C_DEFAULT_PROTOCOL
     */
    private static final String C_DEFAULT_PROTOCOL = "http";

    /**
     * Atributo C_DEFAULT_SERVERNAME
     */
    private static final String C_DEFAULT_SERVERNAME = "localhost";

    /**
     * Atributo projectContent
     */
    private static ProjectContent projectContent;

    /**
     * Atributo appProperties
     */
    Properties appProperties;

    /**
     * Atributo filters
     */
    private List<Object> filters;

    /**
     * Atributo servlets
     */
    private List<Object> servlets;

    /**
     * Atributo initializer
     */
    private List<BootInitializer> initializer;

    /**
     * Constructor ProjectContent
     */
    private ProjectContent() {
        appProperties = new Properties();
        filters = new ArrayList<>();
        servlets = new ArrayList<>();
        initializer = new ArrayList<>();
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_PROTOCOL, C_DEFAULT_PROTOCOL);
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_APPNAME, C_DEFAULT_SERVERNAME);
    }

    /**
     * instance
     * @return ProjectContent
     */
    public static ProjectContent instance() {
        if (projectContent == null)
            projectContent = new ProjectContent();
        return projectContent;
    }

    /**
     * projectName
     * @param projectName projectName
     * @return ProjectContent
     */
    public ProjectContent projectName(String projectName) {
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_APPNAME, projectName);
        return this;
    }

    /**
     * protocol
     * @param protocol protocol
     * @return ProjectContent
     */
    public ProjectContent protocol(String protocol) {
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_PROTOCOL, protocol);
        return this;
    }

    /**
     * server
     * @param server server
     * @return ProjectContent
     */
    public ProjectContent server(String server) {
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_NAME, server);
        return this;
    }

    /**
     * port
     * @param port port
     * @return ProjectContent
     */
    public ProjectContent port(String port) {
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_PORT, port);
        return this;
    }

    /**
     * webSocketPort
     * @param port port
     * @return ProjectContent
     */
    public ProjectContent webSocketPort(String port) {
        appProperties.put(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_WEBSOKET_SERVER_PORT, port);
        return this;
    }

    /**
     * contentPath
     * @param contentPath contentPath
     * @return ProjectContent
     */
    public ProjectContent contentPath(String contentPath) {
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_CONTENT_PATH, contentPath);
        return this;
    }

    /**
     * servletPath
     * @param servletPath servletPath
     * @return ProjectContent
     */
    public ProjectContent servletPath(String servletPath) {
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVLET_PLATH, servletPath);
        return this;
    }

    /**
     * publicPath
     * @param publicPath publicPath
     * @return ProjectContent
     */
    public ProjectContent publicPath(String publicPath) {
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_PUBLIC_PATH, publicPath);
        return this;
    }

    /**
     * stopKey
     * @param stopKey stopKey
     * @return ProjectContent
     */
    public ProjectContent stopKey(String stopKey) {
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_STOPKEY, stopKey);
        return this;
    }

    /**
     * corsOrigin
     * @param origin origin
     * @return ProjectContent
     */
    public ProjectContent corsOrigin(String origin) {
        appProperties.put(CorePropertyConstant.System_Access_Control_Allow_Origin, origin);
        return this;
    }

    /**
     * corsMethod
     * @param methods methods
     * @return ProjectContent
     */
    public ProjectContent corsMethod(String methods) {
        appProperties.put(CorePropertyConstant.System_Access_Control_Allow_Methods, methods);
        return this;
    }

    /**
     * corsHeader
     * @param header header
     * @return ProjectContent
     */
    public ProjectContent corsHeader(String header) {
        appProperties.put(CorePropertyConstant.System_Access_Control_Allow_Headers, header);
        return this;
    }

    /**
     * IvD
     * @param packages packages
     * @return ProjectContent
     */
    public ProjectContent IvD(String packages) {
        appProperties.put(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_CONFIGURATION_PACKAGE_PREFIX, packages);
        return this;
    }

    /**
     * index
     * @param indexPage indexPage
     * @return ProjectContent
     */
    public ProjectContent index(String indexPage) {
        appProperties.put(CorePropertyConstant.ORG_PLATKMFRAMEWORK_CONFIGURATION_INDEX_PAGE, indexPage);
        return this;
    }

    /**
     * addInitializer
     * @param gPABootInitializer gPABootInitializer
     * @return ProjectContent
     */
    public ProjectContent addInitializer(BootInitializer gPABootInitializer) {
        initializer.add(gPABootInitializer);
        return this;
    }

    /**
     * addPropertyFiles
     * @param propertyfiles propertyfiles
     * @return ProjectContent
     */
    public ProjectContent addPropertyFiles(String propertyfiles) {
        if (StringUtils.isNotBlank(propertyfiles)) {
            try {
                loadApplicationProperties(propertyfiles.split(","));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return this;
    }

    /**
     * datetimeFormat
     * @param format format
     * @return ProjectContent
     */
    public ProjectContent datetimeFormat(String format) {
        appProperties.put(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_FORMAT_DATETIME, format);
        return this;
    }

    /**
     * dateFormat
     * @param format format
     * @return ProjectContent
     */
    public ProjectContent dateFormat(String format) {
        appProperties.put(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_FORMAT_DATE, format);
        return this;
    }

    /**
     * timeFormat
     * @param format format
     * @return ProjectContent
     */
    public ProjectContent timeFormat(String format) {
        appProperties.put(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_FORMAT_TIME, format);
        return this;
    }

    /**
     * getDateTimeFormat
     * @return String
     */
    public String getDateTimeFormat() {
        return appProperties.getOrDefault(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_FORMAT_DATETIME, ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_JDBC_FORMAT_DATE_TIME_DEFAULT).toString();
    }

    /**
     * getDateFormat
     * @return String
     */
    public String getDateFormat() {
        return appProperties.getOrDefault(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_FORMAT_DATE, ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_JDBC_FORMAT_DATE_DEFAULT).toString();
    }

    /**
     * getTimeFormat
     * @return String
     */
    public String getTimeFormat() {
        return appProperties.getOrDefault(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_FORMAT_TIME, ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_JDBC_FORMAT_TIME_DEFAULT).toString();
    }

    /**
     * getProjectName
     * @return String
     */
    public String getProjectName() {
        return appProperties.getOrDefault(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_APPNAME, "").toString();
    }

    /**
     * add
     * @param key key
     * @param obj obj
     * @return ProjectContent
     */
    public ProjectContent add(String key, Object obj) {
        appProperties.put(key, obj);
        return this;
    }

    /**
     * addFilter
     * @param filter filter
     * @return ProjectContent
     */
    public ProjectContent addFilter(Object filter) {
        filters.add(filter);
        return this;
    }

    /**
     * getFilters
     * @return List
     */
    public List<Object> getFilters() {
        return filters;
    }

    /**
     * addServlet
     * @param servlet servlet
     * @return ProjectContent
     */
    public ProjectContent addServlet(Object servlet) {
        servlets.add(servlet);
        return this;
    }

    /**
     * getServlet
     * @return List
     */
    public List<Object> getServlet() {
        return servlets;
    }

    /**
     * get
     * @param key key
     * @return Object
     */
    public Object get(String key) {
        return appProperties.get(key);
    }

    /**
     * loadApplicationProperties
     * @param files files
     * @throws IOException IOException
     */
    private void loadApplicationProperties(String[] files) throws IOException {
        if (files != null) {
            Properties properties = new Properties();
            InputStream inputStream;
            File file;
            for (int i = 0; i < files.length; i++) {
                inputStream = this.getClass().getResourceAsStream(files[i].trim());
                if (inputStream != null) {
                    properties.load(inputStream);
                    appProperties.putAll(properties);
                } else {
                    file = new File(files[i].trim());
                    if (file != null && file.isFile()) {
                        InputStream targetStream = new FileInputStream(file);
                        properties.load(targetStream);
                        appProperties.putAll(properties);
                    } else
                        throw new IOException(files[i] + "  file not found -> ");
                }
                properties.clear();
            }
        }
    }

    /**
     * getAppProperties
     * @return Properties
     */
    public Properties getAppProperties() {
        return appProperties;
    }

    /**
     * parseValue
     * @param value value
     * @return String
     */
    public String parseValue(String value) {
        if (StringUtils.isBlank(value))
            return null;
        for (Map.Entry<Object, Object> e : appProperties.entrySet()) {
            value = value.replace("${" + e.getKey().toString() + "}", e.getValue().toString());
        }
        return value;
    }

    /**
     * getProperty
     * @param key key
     * @return String
     */
    public String getProperty(String key) {
        return appProperties.getProperty(key, "");
    }

    /**
     * addProperty
     * @param key key
     * @param value value
     * @return ProjectContent
     */
    public ProjectContent addProperty(String key, String value) {
        getAppProperties().put(key, value);
        return this;
    }

    /**
     * getInitializer
     * @return List
     */
    public List<BootInitializer> getInitializer() {
        return initializer;
    }
}
