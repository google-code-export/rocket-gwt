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

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.type.NewNestedType;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the get target factory bean template
 * 
 * @author Miroslav Pokorny
 */
public class GetTargetFactoryBeanTemplatedFile extends TemplatedCodeBlock {

	public GetTargetFactoryBeanTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The target factory bean type
	 */
	private NewNestedType targetFactoryBean;

	protected NewNestedType getTargetFactoryBean() {
		ObjectHelper.checkNotNull("field:targetFactoryBean", targetFactoryBean);
		return this.targetFactoryBean;
	}

	public void setTargetFactoryBean(final NewNestedType targetFactoryBean) {
		ObjectHelper.checkNotNull("parameter:targetFactoryBean",
				targetFactoryBean);
		this.targetFactoryBean = targetFactoryBean;
	}

	protected String getFileName() {
		return Constants.GET_TARGET_FACTORY_BEAN_TEMPLATE;
	}

	protected InputStream getInputStream() {
		final String filename = this.getFileName();
		final InputStream inputStream = this.getClass().getResourceAsStream(
				filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException(
					"Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.GET_TARGET_FACTORY_BEAN_TARGET_FACTORY_BEAN
					.equals(name)) {
				value = this.getTargetFactoryBean();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name
				+ "] not found, template file [" + this.getFileName() + "]");
	}
}
