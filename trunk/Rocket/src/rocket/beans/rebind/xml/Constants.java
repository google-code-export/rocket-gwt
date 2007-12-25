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
class Constants {

	static final String CONFIG_FILE_SUFFIX = ".xml";

	static final String BEAN_FILE_SUFFIX = "xml";

	static final String PUBLIC_ID = "-//rocket-gwt//Beans 1.0//EN";

	static final String DTD_FILE_NAME = "bean-factory.dtd";

	static final String BEAN_FACTORY_TAG = "bean-factory";

	static final String PLACE_HOLDERS_TAG = "place-holders";

	static final String PLACE_HOLDERS_FILE_ATTRIBUTE = "file";

	static final String BEANS_TAG = "beans";

	static final String BEAN_TAG = "bean";

	static final String BEAN_CLASSNAME_ATTRIBUTE = "class";

	static final String BEAN_ID_ATTRIBUTE = "id";

	static final String BEAN_SCOPE_ATTRIBUTE = "scope";

	static final String BEAN_FACTORY_METHOD_NAME_ATTRIBUTE = "factoryMethod";

	static final String BEAN_INIT_METHOD_NAME_ATTRIBUTE = "initMethod";
	
	static final String BEAN_DESTROY_METHOD_NAME_ATTRIBUTE = "destroyMethod";

	static final String SINGLETON = "singleton";

	static final String PROTOTYPE = "prototype";

	static final String CONSTRUCTOR_TAG = "constructor";

	static final String PROPERTIES_TAG = "properties";

	static final String PROPERTY_TAG = "property";

	static final String PROPERTY_NAME_ATTRIBUTE = "name";

	static final String VALUE_TAG = "value";

	static final String BEAN_REFERENCE_TAG = "bean-reference";

	static final String BEAN_REFERENCE_ID_ATTRIBUTE = "reference-id";

	static final String LIST_TAG = "list";

	static final String SET_TAG = "set";

	static final String MAP_TAG = "map";

	static final String MAP_ENTRY_TAG = "map-entry";

	static final String MAP_ENTRY_KEY_ATTRIBUTE = "key";

	final static String REMOTE_RPC_SERVICE_TAG = "remote-rpc-service";

	final static String REMOTE_RPC_SERVICE_INTERFACE = "interface";

	final static String REMOTE_RPC_SERVICE_ADDRESS = "address";

	final static String REMOTE_RPC_SERVICE_ID = "id";

	final static String REMOTE_JSON_SERVICE_TAG = "remote-json-service";

	final static String REMOTE_JSON_SERVICE_INTERFACE = "interface";

	final static String REMOTE_JSON_SERVICE_ADDRESS = "address";

	final static String REMOTE_JSON_SERVICE_ID = "id";

	final static String ADVICE_TAG = "advice";

	final static String ADVICE_ADVISOR_BEAN_ID = "advisorBeanId";

	final static String ADVICE_TARGET_BEAN_ID = "targetBeanId";

	final static String ADVICE_METHOD_EXPRESSION = "methodExpression";
	
	final static String INCLUDE_TAG = "include";
	final static String INCLUDE_FILE_ATTRIBUTE = "file";
	
	final static String LAZYLOADED = "true";
	final static String EAGERLY_LOADED = "false";
	final static String LAZY_LOADED_ATTRIBUTE = "lazyInit";
	
	final static String ALIAS_TAG = "alias";
	final static String ALIAS_NAME_ATTRIBUTE = "name";
	final static String ALIAS_BEAN_ATTRIBUTE = "bean";
}
