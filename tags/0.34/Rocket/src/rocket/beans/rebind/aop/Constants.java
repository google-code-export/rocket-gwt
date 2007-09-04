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
package rocket.beans.rebind.aop;

/**
 * A collection constants used by various classes within this package.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	final static String CREATE_PROXY_TEMPLATE = "create-proxy.txt";

	final static String CREATE_PROXY_PROXY_CONSTRUCTOR = "proxyConstructor";

	final static String CREATE_PROXY_PROXY_TYPE = "proxyType";

	final static String CREATE_PROXY_TARGET_BEAN_PARAMETER = "targetBeanParameter";

	final static String CREATE_PROXY_TARGET_BEAN_TYPE = "targetBeanType";

	final static String GET_TARGET_FACTORY_BEAN_TEMPLATE = "get-target-factory-bean.txt";

	final static String GET_TARGET_FACTORY_BEAN_TARGET_FACTORY_BEAN = "targetFactoryBean";

	final static String PROXY_METHOD_TEMPLATE = "proxy-method.txt";

	final static String PROXY_METHOD_VOID_TEMPLATE = "proxy-void-method.txt";

	final static String PROXY_METHOD_METHOD = "method";

	final static String PROXY_METHOD_PARAMETERS = "parameters";

	final static String PROXY_METHOD_TARGET_BEAN_FIELD = "targetBeanField";

	final static String PROXY_INTERCEPTED_METHOD_TEMPLATE = "proxy-intercepted-method.txt";

	final static String PROXY_INTERCEPTED_METHOD_ADD_ADVICES = "addAdvices";

	final static String PROXY_INTERCEPTED_METHOD_TARGET = "target";

	final static String PROXY_INTERCEPTED_METHOD_WRAP_PARAMETERS = "wrapParameters";

	final static String PROXY_INTERCEPTED_METHOD_INVOKE_TARGET_METHOD = "invokeTargetMethod";

	final static String PROXY_INTERCEPTED_METHOD_INTERCEPTOR_CHAIN_INVOKE_PROCEED = "interceptorChainInvokeProceed";

	final static String PROXY_INTERCEPTED_METHOD_RETHROW_EXPECTED_EXCEPTIONS = "rethrowExpectedExceptions";

	// add advice template
	final static String ADD_ADVICE_TEMPLATE = "add-advice.txt";

	final static String ADD_ADVICE_BEAN_FACTORY = "beanFactory";

	final static String ADD_ADVICE_BEAN_ID = "beanId";

	// wrap parameter template
	final static String WRAP_PARAMETER_TEMPLATE = "wrap-parameter.txt";

	final static String WRAP_PARAMETER_PARAMETER = "parameter";

	// invoke target method
	final static String INVOKE_TARGET_METHOD_TEMPLATE = "invoke-target-method.txt";

	final static String INVOKE_TARGET_METHOD_VOID_TEMPLATE = "invoke-target-method-void.txt";

	final static String INVOKE_TARGET_METHOD_METHOD = "method";

	final static String INVOKE_TARGET_METHOD_UNWRAP_PARAMETERS = "unwrapParameters";

	// unwrap parameter templates
	final static String UNWRAP_PARAMETER_TEMPLATE = "unwrap-parameter.txt";

	final static String UNWRAP_PARAMETER_BOOLEAN = "unwrap-parameter-boolean.txt";

	final static String UNWRAP_PARAMETER_BYTE = "unwrap-parameter-byte.txt";

	final static String UNWRAP_PARAMETER_SHORT = "unwrap-parameter-short.txt";

	final static String UNWRAP_PARAMETER_INT = "unwrap-parameter-int.txt";

	final static String UNWRAP_PARAMETER_LONG = "unwrap-parameter-long.txt";

	final static String UNWRAP_PARAMETER_FLOAT = "unwrap-parameter-float.txt";

	final static String UNWRAP_PARAMETER_DOUBLE = "unwrap-parameter-double.txt";

	final static String UNWRAP_PARAMETER_CHAR = "unwrap-parameter-char.txt";

	final static String UNWRAP_PARAMETER_OBJECT = "unwrap-parameter-object.txt";

	final static String UNWRAP_PARAMETER_PARAMETER_INDEX = "parameterIndex";

	final static String UNWRAP_PARAMETER_PARAMETER_TYPE = "parameterType";

	// interceptor proceed template 1 for each primitive, void, object return
	// type.
	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_BOOLEAN_TEMPLATE = "invoke-interceptor-chain-proceed-boolean.txt";

	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_BYTE_TEMPLATE = "invoke-interceptor-chain-proceed-byte.txt";

	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_SHORT_TEMPLATE = "invoke-interceptor-chain-proceed-short.txt";

	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_INT_TEMPLATE = "invoke-interceptor-chain-proceed-int.txt";

	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_LONG_TEMPLATE = "invoke-interceptor-chain-proceed-long.txt";

	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_FLOAT_TEMPLATE = "invoke-interceptor-chain-proceed-float.txt";

	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_DOUBLE_TEMPLATE = "invoke-interceptor-chain-proceed-double.txt";

	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_CHAR_TEMPLATE = "invoke-interceptor-chain-proceed-char.txt";

	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_OBJECT_TEMPLATE = "invoke-interceptor-chain-proceed-object.txt";

	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_VOID_TEMPLATE = "invoke-interceptor-chain-proceed-void.txt";

	final static String INVOKE_INTERCEPTOR_CHAIN_PROCEED_METHOD_RETURN_TYPE = "methodReturnType";

	final static String RETHROW_DECLARED_EXCEPTION_TEMPLATE = "rethrow-declared-exception.txt";

	final static String RETHROW_DECLARED_EXCEPTION_EXCEPTION_TYPE = "exception";

	final static String EXCEPTION = Exception.class.getName();

	final static String RUNTIME_EXCEPTION = RuntimeException.class.getName();
}
