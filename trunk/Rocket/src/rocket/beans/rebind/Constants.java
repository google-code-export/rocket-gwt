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
package rocket.beans.rebind;

import rocket.beans.client.BeanFactory;
import rocket.beans.client.BeanFactoryImpl;
import rocket.beans.client.PrototypeFactoryBean;
import rocket.beans.client.RemoteRpcOrJsonServiceFactoryBean;
import rocket.beans.client.SingletonFactoryBean;

import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A collection constants used by various classes within this package.
 * 
 * @author Miroslav Pokorny
 */
public class Constants {

	static final String CONFIG_FILE_SUFFIX = ".xml";
	
	public final static String BEAN_FACTORY = BeanFactory.class.getName();
	public final static String BEAN_FACTORY_IMPL = BeanFactoryImpl.class.getName();
	public final static String BEAN_FACTORY_SUFFIX = "__BeanFactory";
	
	static final String BEAN_FILE_SUFFIX = "xml";	
	public final static String FACTORY_BEAN_SUFFIX = "__FactoryBean";

	public final static String SINGLETON_FACTORY_BEAN = SingletonFactoryBean.class.getName();
	public final static String PROTOTYPE_FACTORY_BEAN = PrototypeFactoryBean.class.getName();
	public final static String REMOTE_RPC_OR_JSON_SERVICE_FACTORY_BEAN = RemoteRpcOrJsonServiceFactoryBean.class.getName();
	public final static String SERVICE_DEF_TARGET = ServiceDefTarget.class.getName();
	
	public final static String CREATE_INSTANCE = "createInstance";
	public final static String SATISFY_PROPERTIES = "satisfyProperties";
	public final static String SATISFY_INIT = "satisfyInit";
	public final static String BUILD_FACTORY_BEANS = "buildFactoryBeans";
	public final static String SET_SERVICE_ENTRY_POINT = "setServiceEntryPoint";

}
