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
package rocket.beans.rebind.xml;

/**
 * A collection constants used by various classes within this package.
 * 
 * @author Miroslav Pokorny
 */
public class Constants {

	static final String CONFIG_FILE_SUFFIX = ".xml";

	static final String BEAN_FILE_SUFFIX = "xml";

	static final String PUBLIC_ID = "-//rocket-gwt//Beans 1.0//EN";

	static final String DTD_FILE_NAME = "bean-factory.dtd";

	public static final String BEAN_FACTORY_TAG = "bean-factory";

	public static final String PLACE_HOLDERS_TAG = "place-holders";

	public static final String PLACE_HOLDERS_FILE_ATTRIBUTE = "file";

	public static final String BEANS_TAG = "beans";

	public static final String BEAN_TAG = "bean";

	public static final String BEAN_CLASSNAME_ATTRIBUTE = "class";

	public static final String BEAN_ID_ATTRIBUTE = "id";

	public static final String BEAN_SCOPE_ATTRIBUTE = "scope";

	public static final String BEAN_FACTORY_METHOD_NAME_ATTRIBUTE = "factoryMethod";

	public static final String BEAN_INIT_METHOD_NAME_ATTRIBUTE = "initMethod";

	public static final String SINGLETON = "singleton";

	public static final String PROTOTYPE = "prototype";

	public static final String CONSTRUCTOR_TAG = "constructor";

	public static final String PROPERTIES_TAG = "properties";

	public static final String PROPERTY_TAG = "property";

	public static final String PROPERTY_NAME_ATTRIBUTE = "name";

	public static final String VALUE_TAG = "value";

	public static final String BEAN_REFERENCE_TAG = "bean-reference";

	public static final String BEAN_REFERENCE_ID_ATTRIBUTE = "reference-id";

	public static final String LIST_TAG = "list";

	public static final String SET_TAG = "set";

	public static final String MAP_TAG = "map";

	public static final String MAP_ENTRY_TAG = "map-entry";

	public static final String MAP_ENTRY_KEY_ATTRIBUTE = "key";

	public final static String REMOTE_RPC_SERVICE_TAG = "remote-rpc-service";

	public final static String REMOTE_RPC_SERVICE_INTERFACE = "interface";

	public final static String REMOTE_RPC_SERVICE_ADDRESS = "address";

	public final static String REMOTE_RPC_SERVICE_ID = "id";

	public final static String REMOTE_JSON_SERVICE_TAG = "remote-json-service";

	public final static String REMOTE_JSON_SERVICE_INTERFACE = "interface";

	public final static String REMOTE_JSON_SERVICE_ADDRESS = "address";

	public final static String REMOTE_JSON_SERVICE_ID = "id";

	public final static String ADVICE_TAG = "advice";

	public final static String ADVICE_ADVISOR_BEAN_ID = "advisorBeanId";

	public final static String ADVICE_TARGET_BEAN_ID = "targetBeanId";

	public final static String ADVICE_METHOD_EXPRESSION = "methodExpression";
}
