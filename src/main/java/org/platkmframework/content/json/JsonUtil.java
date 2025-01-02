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
package org.platkmframework.content.json;

import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.platkmframework.content.project.ProjectContent;
import org.platkmframework.util.JsonException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 * description: JSON util
 */
public class JsonUtil {

    /**
     * Atributo gson
     */
    private static Gson gson;

    /**
     * description: constructor
     */
    private JsonUtil() {
        super();
    }

    /**
     * description: init
     */
    public static void init() {
        init(null);
    }

    /**
     * init
     * @param adapters adapters
     */
    public static void init(Function<GsonBuilder, GsonBuilder> adapters) {
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) new JsonSerializer<LocalDate>() {

            //from   w  w  w.ja  va 2 s. c o  m
            @Override
            public JsonElement serialize(LocalDate t, java.lang.reflect.Type type, JsonSerializationContext jsc) {
                if (t != null) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ProjectContent.instance().getDateFormat());
                    return new JsonPrimitive(t.format(dateTimeFormatter));
                } else {
                    return null;
                }
            }
        }).registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) new JsonDeserializer<LocalDate>() {

            @Override
            public LocalDate deserialize(JsonElement je, java.lang.reflect.Type type, JsonDeserializationContext jdc) {
                if (StringUtils.isNotBlank(je.getAsJsonPrimitive().getAsString())) {
                    return LocalDate.parse(je.getAsJsonPrimitive().getAsString().substring(0, 10).trim(), DateTimeFormatter.ofPattern(ProjectContent.instance().getDateFormat()));
                } else {
                    return null;
                }
            }
        }).registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) new JsonSerializer<LocalDateTime>() {

            @Override
            public JsonElement serialize(LocalDateTime t, java.lang.reflect.Type type, JsonSerializationContext jsc) {
                if (t != null) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ProjectContent.instance().getDateTimeFormat());
                    return new JsonPrimitive(t.format(dateTimeFormatter));
                } else {
                    return null;
                }
            }
        }).registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (JsonElement je, java.lang.reflect.Type type, JsonDeserializationContext jdc) -> {
            if (StringUtils.isNotBlank(je.getAsJsonPrimitive().getAsString())) {
                return LocalDateTime.parse(je.getAsJsonPrimitive().getAsString().trim(), DateTimeFormatter.ofPattern(ProjectContent.instance().getDateTimeFormat()));
            } else {
                return null;
            }
        }).registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) new JsonSerializer<LocalTime>() {

            @Override
            public JsonElement serialize(LocalTime t, java.lang.reflect.Type type, JsonSerializationContext jsc) {
                if (t != null) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ProjectContent.instance().getTimeFormat());
                    return new JsonPrimitive(t.format(dateTimeFormatter));
                } else {
                    return null;
                }
            }
        }).registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (JsonElement je, java.lang.reflect.Type type, JsonDeserializationContext jdc) -> {
            if (je.getAsJsonPrimitive().getAsString() != null) {
                return LocalTime.parse(je.getAsJsonPrimitive().getAsString().trim(), DateTimeFormatter.ofPattern(ProjectContent.instance().getTimeFormat()));
            } else {
                return null;
            }
        }).registerTypeAdapter(Timestamp.class, (JsonSerializer<Timestamp>) new JsonSerializer<Timestamp>() {

            @Override
            public JsonElement serialize(Timestamp t, java.lang.reflect.Type type, JsonSerializationContext jsc) {
                if (t != null) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ProjectContent.instance().getDateTimeFormat());
                    return new JsonPrimitive(dateTimeFormatter.format(t.toLocalDateTime()));
                } else {
                    return null;
                }
            }
        }).registerTypeAdapter(Timestamp.class, (JsonDeserializer<Timestamp>) (JsonElement je, java.lang.reflect.Type type, JsonDeserializationContext jdc) -> {
            if (je.getAsJsonPrimitive().getAsString() != null) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ProjectContent.instance().getDateTimeFormat());
                LocalDateTime localDateTime = LocalDateTime.from(dateTimeFormatter.parse(je.getAsJsonPrimitive().getAsString().trim()));
                return Timestamp.valueOf(localDateTime);
            } else {
                return null;
            }
        });
        if (adapters != null) {
            adapters.apply(gsonBuilder);
        }
        gson = gsonBuilder.serializeNulls().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();
    }

    /**
     * description: string to object
     * @param <E>: object
     * @param json: string
     * @param class1: class
     * @return object result
     * @throws JsonException : error
     */
    public static <E> E jsonToObject(String json, Class<E> class1) throws JsonException {
        try {
            return gson.fromJson(json, class1);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new JsonException(e.getMessage());
        }
    }

    /**
     * description: create object from references
     * @param <E>: class result
     * @param json: JSON string
     * @param TypeReference: reference
     * @return object from JSON string
     * @throws JsonException : error
     */
    public static <E> E jsonToObjectTypeReference(String json, TypeToken<E> TypeReference) throws JsonException {
        try {
            return gson.fromJson(json, TypeReference);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new JsonException(e.getMessage());
        }
    }

    /**
     * jsonToObjectTypeReference
     * @param jsonReader jsonReader
     * @param TypeReference TypeReference
     * @return E
     * @throws JsonException JsonException
     */
    public static <E> E jsonToObjectTypeReference(JsonReader jsonReader, TypeToken<E> TypeReference) throws JsonException {
        try {
            return gson.fromJson(jsonReader, TypeReference);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new JsonException(e.getMessage());
        }
    }

    /**
     * description: jston to object list
     * @param <E>: class type
     * @param json: string json
     * @param class1: class type
     * @return list result
     * @throws JsonException - error
     */
    public static <E> List<E> jsonToListObject(String json, Class<E> class1) throws JsonException {
        try {
            return gson.fromJson(json, TypeToken.getParameterized(ArrayList.class, class1).getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new JsonException(e.getMessage());
        }
    }

    /**
     * description: object to json
     * @param result: objcet
     * @return json string
     * @throws JsonException : error
     */
    public static String objectToJson(Object result) throws JsonException {
        try {
            if (result == null || StringUtils.isEmpty(result.toString().trim()))
                return "";
            if (result instanceof String)
                return result.toString();
            return gson.toJson(result);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new JsonException(e.getMessage());
        }
    }

    /**
     * description: object to json
     * @param result: objcet
     * @return json string
     * @throws JsonException : error
     */
    public static String objectToJsonByType(Object result, Type type) throws JsonException {
        try {
            return gson.toJson(result, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new JsonException(e.getMessage());
        }
    }

    /**
     * toJson
     * @param object object
     * @param fileWriter fileWriter
     */
    public static void toJson(Object object, FileWriter fileWriter) {
        gson.toJson(object, fileWriter);
    }

    /**
     * fromJson
     * @param reader reader
     * @param classType classType
     * @return E
     */
    public static <E> E fromJson(Reader reader, Class<E> classType) {
        return gson.fromJson(reader, classType);
    }
}
