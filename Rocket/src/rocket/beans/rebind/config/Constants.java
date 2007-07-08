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
package rocket.beans.rebind.config;

/**
 * A collection constants used by various classes within this package.
 * 
 * @author Miroslav Pokorny 
 */
public class Constants {

	static final String SUFFIX = ".xml";

	static final String PUBLIC_ID = "-//rocket-gwt//Beans 1.0//EN";

	static final String DTD_FILE_NAME = "bean-factory.dtd";

	public static final String BEAN_FACTORY_IMPL = "__BeanFactoryImpl";
	
	public static final String BEAN_FACTORY = "bean-factory";
	
	public static final String PLACE_HOLDERS = "place-holders";
	
	public static final String PLACE_HOLDERS_FILE = "file";

	public static final String BEAN = "bean";

	public static final String BEAN_CLASSNAME = "class";

	public static final String BEAN_ID = "id";

	public static final String BEAN_SCOPE = "scope";

	public static final String BEAN_INIT_METHOD_NAME = "initMethod";

	public static final String SINGLETON = "singleton";

	public static final String PROTOTYPE = "prototype";

	public static final String CONSTRUCTOR = "constructor";

	public static final String FACTORY ="factory";
	
	public static final String FACTORY_BEAN_ID ="reference-id";
	
	public static final String FACTORY_METHOD_NAME ="method";
	
	public static final String PROPERTIES = "properties";
	
	public static final String PROPERTY = "property";

	public static final String PROPERTY_NAME = "name";

	public static final String VALUE = "value";

	public static final String BEAN_REFERENCE = "bean-reference";

	public static final String BEAN_REFERENCE_ID = "reference-id";

	public static final String LIST = "list";

	public static final String SET = "set";

	public static final String MAP = "map";

	public static final String MAP_ENTRY = "map-entry";

	public static final String MAP_ENTRY_KEY = "key";

	public final static String REMOTE_RPC_SERVICE = "remote-rpc-service";

	public final static String REMOTE_RPC_SERVICE_INTERFACE = "interface";

	public final static String REMOTE_RPC_SERVICE_ADDRESS = "address";

	public final static String REMOTE_RPC_SERVICE_ID = "id";

	public final static String REMOTE_JSON_SERVICE = "remote-json-service";

	public final static String REMOTE_JSON_SERVICE_INTERFACE = "interface";

	public final static String REMOTE_JSON_SERVICE_ADDRESS = "address";

	public final static String REMOTE_JSON_SERVICE_ID = "id";
}
