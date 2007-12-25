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
import rocket.beans.client.DisposableBean;
import rocket.beans.client.FactoryBean;
import rocket.beans.client.PrototypeFactoryBean;
import rocket.beans.client.SingletonFactoryBean;
import rocket.beans.client.aop.Advice;
import rocket.beans.client.aop.MethodInterceptor;
import rocket.beans.client.aop.MethodInvocation;
import rocket.beans.client.aop.ProxyFactoryBean;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A collection constants used by various classes within this package.
 * 
 * @author Miroslav Pokorny
 */
class Constants {

	static final String CONFIG_FILE_SUFFIX = ".xml";

	final static String BEAN_FACTORY = BeanFactory.class.getName();

	final static String BEAN_FACTORY_IMPL = BeanFactoryImpl.class.getName();

	final static String BEAN_FACTORY_SUFFIX = "__BeanFactory";

	static final String BEAN_FILE_SUFFIX = "xml";

	final static String FACTORY_BEAN = FactoryBean.class.getName();;
	
	final static String FACTORY_BEAN_SUFFIX = "__FactoryBean";

	final static String SINGLETON_FACTORY_BEAN = SingletonFactoryBean.class.getName();

	final static String PROTOTYPE_FACTORY_BEAN = PrototypeFactoryBean.class.getName();

	final static String FACTORY_BEAN_OBJECT_TYPE = "factoryBean-objectType";
	
	final static String SERVICE_DEF_TARGET = ServiceDefTarget.class.getName();

	final static String CREATE_INSTANCE = "createInstance";

	final static String SATISFY_PROPERTIES = "satisfyProperties";
	final static String SATISFY_PROPERTIES_INSTANCE_PARAMETER = "instance"; 

	final static String SATISFY_INIT = "satisfyInit";
	final static String SATISFY_INIT_INSTANCE_PARAMETER = "instance";
	
	final static String REGISTER_FACTORY_BEANS = "registerFactoryBeans";

	final static String SET_SERVICE_ENTRY_POINT = "setServiceEntryPoint";

	final static String ADVICE = Advice.class.getName();

	final static String PROXY_FACTORY_BEAN = ProxyFactoryBean.class.getName();

	final static String PROXY_FACTORY_GET_TARGET_FACTORY_BEAN_METHOD_NAME = "getTargetFactoryBean";
	
	final static String PROXY_FACTORY_BEAN_SUFFIX = "__ProxyFactoryBean";

	final static String CREATE_PROXY = "createProxy0";

	final static String PROXY_SUFFIX = "__Proxy";

	final static String PROXY_TARGET_FIELD = "target";

	final static String METHOD_INTERCEPTOR = MethodInterceptor.class.getName();

	final static String METHOD_INTERCEPTOR_INVOKE = "invoke";

	final static String METHOD_INVOCATION = MethodInvocation.class.getName();
	
	final static String ENCODED_DOT_WITHIN_TYPE_NAME = "_" + StringHelper.padLeft( Integer.toHexString( '.' ), 4, '0' );
	
	final static String ASYNC_SUFFIX = "Async";
	
	final static String GET_EAGER_SINGELTON_BEAN_NAMES_METHOD = "getEagerSingletonBeanNames";
	
	final static String GET_ALIASES_TO_BEANS_METHOD = "getAliasesToBeans";
	
	final static String DISPOSABLE_BEAN = DisposableBean.class.getName();
	
	final static String DESTROY = "destroy";
	final static String DESTROY_INSTANCE_PARAMETER = "instance";
}
