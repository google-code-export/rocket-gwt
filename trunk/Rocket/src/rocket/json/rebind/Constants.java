/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.json.rebind;

import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.json.client.JsonSerializer;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * A collection of constants used during the code generation process.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	static final String JAVA_UTIL_LIST = List.class.getName();

	static final String LIST_ELEMENT_TYPE = "jsonSerialization-listElementType";

	static final String JAVA_UTIL_SET = Set.class.getName();

	static final String SET_ELEMENT_TYPE = "jsonSerialization-setElementType";

	static final String JAVA_UTIL_MAP = Map.class.getName();

	static final String MAP_VALUE_TYPE = "jsonSerialization-mapValueType";

	static final String JAVASCRIPT_PROPERTY_NAME_ANNOTATION = "jsonSerialization-javascriptPropertyName";

	static final String SERIALIZER_SUFFIX = "__JsonSerializer";

	static final String OBJECT_TYPE = Object.class.getName();

	static final String JSON_VALUE_TYPE = JSONValue.class.getName();

	static final String JSON_OBJECT_TYPE = JSONObject.class.getName();

	static final String JSON_SERIALIZER_TYPE = JsonSerializer.class.getName();

	static final String AS_OBJECT_TEMPLATE = "as-object.txt";

	static final String AS_OBJECT_JSON_VALUE_PARAMETER = "jsonValueParameter";

	static final String AS_OBJECT_DESERIALIZER_TYPE = "deserializerType";

	static final String FIELD_SETTER_TEMPLATE = "field-setter.txt";

	static final String FIELD_SETTER_INSTANCE = "instance";

	static final String FIELD_SETTER_FIELD = "field";

	static final String FIELD_SETTER_VALUE = "value";

	static final String INVOKE_PRIMITIVE_OR_STRING_FIELD_SETTER_TEMPLATE = "invoke-primitive-or-string-field-setter.txt";

	static final String INVOKE_PRIMITIVE_OR_STRING_FIELD_SETTER_FIELD_SETTER = "fieldSetter";

	static final String INVOKE_PRIMITIVE_OR_STRING_FIELD_SETTER_INSTANCE = "instance";

	static final String INVOKE_PRIMITIVE_OR_STRING_FIELD_SETTER_AS_METHOD = "asMethod";

	static final String INVOKE_PRIMITIVE_OR_STRING_FIELD_SETTER_JSON_OBJECT = "jsonObject";

	static final String INVOKE_PRIMITIVE_OR_STRING_FIELD_SETTER_JAVASCRIPT_PROPERTY_NAME = "javascriptPropertyName";

	static final String INVOKE_OBJECT_FIELD_SETTER_TEMPLATE = "invoke-object-field-setter.txt";

	static final String INVOKE_OBJECT_FIELD_SETTER_FIELD_SETTER = "fieldSetter";

	static final String INVOKE_OBJECT_FIELD_SETTER_INSTANCE = "instance";

	static final String INVOKE_OBJECT_FIELD_SETTER_FIELD_TYPE = "fieldType";

	static final String INVOKE_OBJECT_FIELD_SETTER_FIELD_TYPE_DESERIALIZER = "fieldTypeDeserializer";

	static final String INVOKE_OBJECT_FIELD_SETTER_AS_METHOD = "asMethod";

	static final String INVOKE_OBJECT_FIELD_SETTER_JSON_OBJECT = "jsonObject";

	static final String INVOKE_OBJECT_FIELD_SETTER_JAVASCRIPT_PROPERTY_NAME = "javascriptPropertyName";
}
