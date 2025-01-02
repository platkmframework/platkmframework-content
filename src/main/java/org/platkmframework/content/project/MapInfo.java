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

import java.util.HashMap;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class MapInfo extends HashMap<String, String> {

    /**
     * Atributo serialVersionUID
     */
    private static final long serialVersionUID = 5327010767630664654L;

    /**
     * create
     * @return MapInfo
     */
    public static MapInfo create() {
        return new MapInfo();
    }

    /**
     * add
     * @param key key
     * @param value value
     * @return MapInfo
     */
    public MapInfo add(String key, String value) {
        super.put(key, value);
        return this;
    }
}
