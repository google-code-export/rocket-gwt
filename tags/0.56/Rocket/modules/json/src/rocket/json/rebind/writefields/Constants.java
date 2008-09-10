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
package rocket.json.rebind.writefields;

/**
 * A collection of constants used during the code generation process.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	static final String WRITE_FIELDS_WRITE_METHODS = "writeFields";

	static final String WRITE_FIELDS_TEMPLATE = "write-fields.txt";

	static final String WRITE_FIELDS_INSTANCE_PARAMETER = "instance";

	static final String WRITE_FIELDS_INSTANCE_TYPE = "instanceType";

	static final String WRITE_FIELD_TEMPLATE = "write-field.txt";

	static final String WRITE_FIELD_FIELD_GETTER = "getter";

	static final String WRITE_FIELD_JSON_OBJECT_PARAMETER = "jsonObject";

	static final String WRITE_FIELD_JAVASCRIPT_PROPERTY_NAME = "javascriptPropertyName";

	static final String WRITE_FIELD_SERIALIZER = "serializer";
}
