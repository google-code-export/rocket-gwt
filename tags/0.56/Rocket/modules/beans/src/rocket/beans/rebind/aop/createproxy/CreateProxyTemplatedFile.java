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
package rocket.beans.rebind.aop.createproxy;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the create proxyConstructor template
 * 
 * @author Miroslav Pokorny
 */
public class CreateProxyTemplatedFile extends TemplatedFileCodeBlock {

	public CreateProxyTemplatedFile() {
		super();
	}

	protected Type getProxyType() {
		return this.getProxyConstructor().getEnclosingType();
	}

	private Constructor proxyConstructor;

	protected Constructor getProxyConstructor() {
		Checker.notNull("field:proxyConstructor", proxyConstructor);
		return this.proxyConstructor;
	}

	public void setProxyConstructor(final Constructor proxy) {
		Checker.notNull("parameter:proxyConstructor", proxy);
		this.proxyConstructor = proxy;
	}

	/**
	 * The parameter which contains the target bean.
	 */
	private MethodParameter targetBeanParameter;

	protected MethodParameter getTargetBeanParameter() {
		Checker.notNull("field:targetBeanParameter", targetBeanParameter);
		return this.targetBeanParameter;
	}

	public void setTargetBeanParameter(final MethodParameter targetBeanParameter) {
		Checker.notNull("parameter:targetBeanParameter", targetBeanParameter);
		this.targetBeanParameter = targetBeanParameter;
	}

	private Type targetBeanType;

	protected Type getTargetBeanType() {
		Checker.notNull("field:targetBeanType", targetBeanType);
		return this.targetBeanType;
	}

	public void setTargetBeanType(final Type targetBeanType) {
		Checker.notNull("parameter:targetBeanType", targetBeanType);
		this.targetBeanType = targetBeanType;
	}

	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.PROXY_CONSTRUCTOR.equals(name)) {
				value = this.getProxyConstructor();
				break;
			}
			if (Constants.PROXY_TYPE.equals(name)) {
				value = this.getProxyConstructor();
				break;
			}
			if (Constants.TARGET_BEAN_PARAMETER.equals(name)) {
				value = this.getTargetBeanParameter();
				break;
			}
			if (Constants.TARGET_BEAN_TYPE.equals(name)) {
				value = this.getTargetBeanType();
				break;
			}
			break;
		}
		return value;
	}
}
