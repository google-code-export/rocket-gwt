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
package rocket.beans.rebind.registerbeans;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.type.NewNestedType;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * An abstraction for the invoker add template
 * 
 * @author Miroslav Pokorny
 */
public class RegisterBeanTemplatedFile extends TemplatedCodeBlock {

	public RegisterBeanTemplatedFile() {
		super();
		setNative(false);
	}

	private String beanId;

	protected String getBeanId() {
		StringHelper.checkNotEmpty("beanId:beanId", beanId);
		return this.beanId;
	}

	public void setBeanId(final String beanId) {
		StringHelper.checkNotEmpty("parameter:beanId", beanId);
		this.beanId = beanId;
	}

	private NewNestedType factoryBean;

	protected NewNestedType getFactoryBean() {
		ObjectHelper.checkNotNull("field:factoryBean", factoryBean);
		return this.factoryBean;
	}

	public void setFactoryBean(final NewNestedType factoryBean) {
		ObjectHelper.checkNotNull("factoryBean:factoryBean", factoryBean);
		this.factoryBean = factoryBean;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.REGISTER_BEAN_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.REGISTER_BEAN_BEAN_ID.equals(name)) {
				value = new StringLiteral(this.getBeanId());
				break;
			}
			if (Constants.REGISTER_BEAN_FACTORY_BEAN.equals(name)) {
				value = this.getFactoryBean();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \""
				+ Constants.REGISTER_BEAN_TEMPLATE + "\".");
	}
}
