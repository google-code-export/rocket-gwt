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
package rocket.remoting.test.remotejsonservice.client;

import rocket.json.client.JsonSerializable;

/**
 * This class is used by the test case found in this package.
 * 
 * @author Miroslav Pokorny
 * 
 */
public class ClassWith3StringFields implements JsonSerializable {
	/**
	 * @jsonSerialization-javascriptPropertyName field1
	 */
	public String field1;

	/**
	 * @jsonSerialization-javascriptPropertyName field2
	 */
	public String field2;

	/**
	 * @jsonSerialization-javascriptPropertyName field3
	 */
	public String field3;
}
