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
 */
public class ObjectContainer {

    /**
     * Atributo C_APP_OBJECT_PACKAGE
     */
    public static final String C_APP_OBJECT_PACKAGE = "APP_OBJECT_PACKAGE";

    /**
     * Atributo C_APP_OBJECT_JAR
     */
    public static final String C_APP_OBJECT_JAR = "APP_OBJECT_JAR";

    /**
     * Atributo objectContainer
     */
    private static ObjectContainer objectContainer;

    /**
     * Atributo objectReferece
     */
    private ObjectReferece objectReferece;

    /**
     * Constructor ObjectContainer
     */
    private ObjectContainer() {
    }

    /**
     * instance
     * @return ObjectContainer
     */
    public static ObjectContainer instance() {
        if (objectContainer == null)
            objectContainer = new ObjectContainer();
        return objectContainer;
    }

    /**
     * setReference
     * @param ref ref
     */
    public void setReference(ObjectReferece ref) {
        objectReferece = ref;
    }

    /**
     * geApptScopeObj
     * @param key key
     * @return Object
     */
    public Object geApptScopeObj(String key) {
        return this.objectReferece.getObject(key);
    }

    /**
     * geApptScopeObj
     * @param class1 class1
     * @return Object
     */
    public Object geApptScopeObj(Class<?> class1) {
        return geApptScopeObj(class1.getName());
    }

    /**
     * getListObjectByAnnontation
     * @param annotation annotation
     * @return List
     */
    public List<Object> getListObjectByAnnontation(Class<? extends Annotation> annotation) {
        return this.objectReferece.getObjectsByAnnotation(annotation);
    }

    /**
     * getListObjectByAnnontationAndInstance
     * @param annotation annotation
     * @param classInstance classInstance
     * @return List
     */
    public List<Object> getListObjectByAnnontationAndInstance(Class<? extends Annotation> annotation, Class<?> classInstance) {
        return this.objectReferece.getListObjectByAnnontationAndInstance(annotation, classInstance);
    }

    /**
     * getListObjectByInstance
     * @param classInstance classInstance
     * @return List
     */
    public List<Object> getListObjectByInstance(Class<?> classInstance) {
        return this.objectReferece.getListObjectByInstance(classInstance);
    }

    /**
     * getPropertyValue
     * @param key key
     * @return String
     */
    public String getPropertyValue(String key) {
        return this.objectReferece.getProp().getProperty(key);
    }

    /**
     * getPropertyValue
     * @param key key
     * @param defaultValue defaultValue
     * @return String
     */
    public String getPropertyValue(String key, String defaultValue) {
        return this.objectReferece.getProp().getProperty(key, defaultValue);
    }

    /**
     * getApiControllerInfo
     * @param path path
     * @return String
     */
    public String getApiControllerInfo(String path) {
        return objectReferece.getApiMethod().get(path);
    }

    /**
     * getApiPathVariable
     * @param pathVariable pathVariable
     * @return List
     */
    public List<String> getApiPathVariable(String pathVariable) {
        return objectReferece.getPathVarialbeApiMethod().get(pathVariable);
    }

    /**
     * getController
     * @param controllerClassName controllerClassName
     * @return Object
     */
    public Object getController(String controllerClassName) {
        return objectReferece.getController(controllerClassName);
    }

    /**
     * getBeansMethodByAnnotation
     * @param annotation annotation
     * @return List
     */
    public List<BeanMethodInfo> getBeansMethodByAnnotation(Class<? extends Annotation> annotation) {
        List<Class<? extends Annotation>> list = new ArrayList<>();
        list.add(annotation);
        return getBeansMethodByAnnotations(list);
    }

    /**
     * getBeansMethodByAnnotations
     * @param annotations annotations
     * @return List
     */
    public List<BeanMethodInfo> getBeansMethodByAnnotations(List<Class<? extends Annotation>> annotations) {
        return this.objectReferece.getBeansMethodByAnnotations(annotations);
    }

    /**
     * getObjectByKey
     * @param key key
     * @return Object
     */
    public Object getObjectByKey(String key) {
        return this.objectReferece.getObject(key);
    }

    /**
     * getBeanFieldInfoByAnnotation
     * @param annotation annotation
     * @return List
     */
    public List<BeanFieldInfo> getBeanFieldInfoByAnnotation(Class<? extends Annotation> annotation) {
        List<Class<? extends Annotation>> list = new ArrayList<>();
        list.add(annotation);
        return getBeanFieldInfoByAnnotations(list);
    }

    /**
     * getBeanFieldInfoByAnnotations
     * @param annotations annotations
     * @return List
     */
    public List<BeanFieldInfo> getBeanFieldInfoByAnnotations(List<Class<? extends Annotation>> annotations) {
        return this.objectReferece.getBeansFieldByAnnotations(annotations);
    }

    /**
     * setException
     * @param exceptionClass exceptionClass
     */
    public void setException(String exceptionClass) {
        this.objectReferece.getExceptions().add(exceptionClass);
    }

    /**
     * containsException
     * @param exception exception
     * @return boolean
     */
    public boolean containsException(Object exception) {
        return this.objectReferece.getExceptions().contains(exception.getClass().getName());
    }

    /**
     * getLimit
     * @param limitClass limitClass
     * @return Object
     */
    public Object getLimit(Class<?> limitClass) {
        for (Object obj : this.objectReferece.getLimits()) {
            if (obj.getClass().getName().equals(limitClass.getName()))
                return obj;
        }
        return null;
    }

    /**
     * getSearchMapInfo
     * @param code code
     * @return Object
     */
    public Object getSearchMapInfo(String code) {
        return this.objectReferece.getSearchFilter().get(code);
    }

    /**
     * getFunctionals
     * @return List
     */
    public List<Object> getFunctionals() {
        return objectReferece.getFunctionals();
    }

    /**
     * getCustomInfoByKey
     * @param key key
     * @return Object
     */
    public Object getCustomInfoByKey(String key) {
        return objectReferece.getCustomInfo().get(key);
    }
}
