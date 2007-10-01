/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */package rocket.remoting.rebind.comet;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public class Constants {
	final static String COMET_CLIENT_SUFFIX = "__CometClient";
	
	final static String COMET_PAYLOAD_TYPE_ANNOTATION = "comet-payloadType";
	
	final static String CREATE_PROXY_METHOD = "createProxy";
	
	final static String RPC_SERVICE_INTERFACE = "__ServiceInterface";
	final static String REMOTE_SERVICE = RemoteService.class.getName();
	
	final static String RPC_ASYNC_SERVICE_INTERFACE = RPC_SERVICE_INTERFACE + "Async";
	final static String ASYNC_CALLBACK = AsyncCallback.class.getName();
	final static String ASYNC_CALLBACK_PARAMETER_NAME = "callback";
	
	final static String CREATE_PROXY_TEMPLATE = "create-proxy.txt";
	final static String CREATE_PROXY_BEAN_TYPE = "beanType";
	
	final static String PAYLOAD_DECLARATION_METHOD = "__dummy";
	
	final static String PROXY_CREATOR = "com.google.gwt.user.rebind.rpc.ProxyCreator";
	final static String CUSTOMISED_PROXY_CREATOR_MARKER_FIELD = "ROCKET";
}
