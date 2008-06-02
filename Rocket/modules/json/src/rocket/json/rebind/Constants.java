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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.json.client.BooleanJsonSerializer;
import rocket.json.client.ByteJsonSerializer;
import rocket.json.client.CharJsonSerializer;
import rocket.json.client.DateJsonSerializer;
import rocket.json.client.DoubleJsonSerializer;
import rocket.json.client.FloatJsonSerializer;
import rocket.json.client.IntJsonSerializer;
import rocket.json.client.JsonSerializer;
import rocket.json.client.LongJsonSerializer;
import rocket.json.client.ShortJsonSerializer;
import rocket.json.client.StringJsonSerializer;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * A collection of constants used during the code generation process.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	static final String JSON_SERIALIZATION = "jsonSerialization";

	static final String JAVA_UTIL_LIST = List.class.getName();
	static final String JAVA_UTIL_SET = Set.class.getName();
	static final String JAVA_UTIL_MAP = Map.class.getName();

	static final String TYPE = JSON_SERIALIZATION + "-type";

	static final String JAVASCRIPT_PROPERTY_NAME_ANNOTATION = JSON_SERIALIZATION + "-javascriptPropertyName";

	static final String SERIALIZER_SUFFIX = "__JsonSerializer";

	static final String OBJECT_TYPE = Object.class.getName();

	static final String JSON_VALUE_TYPE = JSONValue.class.getName();

	static final String JSON_OBJECT_TYPE = JSONObject.class.getName();

	static final String JSON_SERIALIZER_TYPE = JsonSerializer.class.getName();

	static final String SERIALIZER_SINGLETON = "serializer";

	static final String READ_FIELDS_METHOD = "readFields";

	static final String READ_COMPLEX_METHOD_NAME = "readObject";

	static final String READ_COMPLEX_JSON_VALUE_PARAMETER = "jsonValue";
	static final String SET_FIELD_INSTANCE_PARAMETER = "instance";
	static final String SET_FIELD_VALUE_PARAMETER = "value";

	static final String SET_COMPLEX_READ_METHOD = "readMethod";

	static final String SET_COMPLEX_READ_OBJECT_METHOD = "readObject";

	static final String SET_COMPLEX_READ_LIST_METHOD = "readList";

	static final String SET_COMPLEX_READ_SET_METHOD = "readSet";

	static final String SET_COMPLEX_READ_MAP_METHOD = "readMap";

	static final String WRITE_JSON_METHOD_NAME = "writeJson";

	static final String WRITE_FIELDS_WRITE_METHODS = "writeFields";
	static final String WRITE_FIELDS_INSTANCE_PARAMETER = "instance";
	static final String WRITE_FIELD_JSON_OBJECT_PARAMETER = "jsonObject";

	static final String GET_FIELD_INSTANCE_PARAMETER = "instance";
	static final String GET_FIELD_METHOD_PREFIX = "get";

	static final String BOOLEAN_SERIALIZER = BooleanJsonSerializer.class.getName();

	static final String BYTE_SERIALIZER = ByteJsonSerializer.class.getName();

	static final String SHORT_SERIALIZER = ShortJsonSerializer.class.getName();

	static final String INT_SERIALIZER = IntJsonSerializer.class.getName();

	static final String LONG_SERIALIZER = LongJsonSerializer.class.getName();

	static final String FLOAT_SERIALIZER = FloatJsonSerializer.class.getName();

	static final String DOUBLE_SERIALIZER = DoubleJsonSerializer.class.getName();

	static final String CHAR_SERIALIZER = CharJsonSerializer.class.getName();

	static final String STRING_SERIALIZER = StringJsonSerializer.class.getName();

	static final String DATE = Date.class.getName();

	static final String DATE_SERIALIZER = DateJsonSerializer.class.getName();
}
