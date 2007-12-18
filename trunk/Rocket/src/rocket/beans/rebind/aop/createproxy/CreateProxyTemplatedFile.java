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

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the create proxyConstructor template
 * 
 * @author Miroslav Pokorny
 */
public class CreateProxyTemplatedFile extends TemplatedCodeBlock {

	public CreateProxyTemplatedFile() {
		super();
		setNative(false);
	}

	protected Type getProxyType() {
		return this.getProxyConstructor().getEnclosingType();
	}

	private Constructor proxyConstructor;

	protected Constructor getProxyConstructor() {
		ObjectHelper.checkNotNull("field:proxyConstructor", proxyConstructor);
		return this.proxyConstructor;
	}

	public void setProxyConstructor(final Constructor proxy) {
		ObjectHelper.checkNotNull("parameter:proxyConstructor", proxy);
		this.proxyConstructor = proxy;
	}

	/**
	 * The parameter which contains the target bean.
	 */
	private MethodParameter targetBeanParameter;

	protected MethodParameter getTargetBeanParameter() {
		ObjectHelper.checkNotNull("field:targetBeanParameter", targetBeanParameter);
		return this.targetBeanParameter;
	}

	public void setTargetBeanParameter(final MethodParameter targetBeanParameter) {
		ObjectHelper.checkNotNull("parameter:targetBeanParameter", targetBeanParameter);
		this.targetBeanParameter = targetBeanParameter;
	}

	private Type targetBeanType;

	protected Type getTargetBeanType() {
		ObjectHelper.checkNotNull("field:targetBeanType", targetBeanType);
		return this.targetBeanType;
	}

	public void setTargetBeanType(final Type targetBeanType) {
		ObjectHelper.checkNotNull("parameter:targetBeanType", targetBeanType);
		this.targetBeanType = targetBeanType;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

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

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \""
				+ Constants.TEMPLATE + "\".");
	}
}
