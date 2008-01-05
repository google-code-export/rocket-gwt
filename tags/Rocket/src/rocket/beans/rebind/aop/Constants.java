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

	// invoke target method
	final static String INVOKE_TARGET_METHOD_TEMPLATE = "invoke-target-method.txt";

	final static String INVOKE_TARGET_METHOD_VOID_TEMPLATE = "invoke-target-method-void.txt";

	final static String INVOKE_TARGET_METHOD_METHOD = "method";

	final static String INVOKE_TARGET_METHOD_UNWRAP_PARAMETERS = "unwrapParameters";


	
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
}
