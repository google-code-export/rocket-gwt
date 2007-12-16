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

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the add advice template
 * 
 * @author Miroslav Pokorny
 */
public class AddAdviceTemplatedFile extends TemplatedCodeBlock {

	public AddAdviceTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The outter bean factory class
	 */
	private Type beanFactory;

	protected Type getBeanFactory() {
		ObjectHelper.checkNotNull("field:beanFactory", beanFactory);
		return this.beanFactory;
	}

	public void setBeanFactory(final Type beanFactory) {
		ObjectHelper.checkNotNull("parameter:beanFactory", beanFactory);
		this.beanFactory = beanFactory;
	}

	/**
	 * The id of the advisor bean being fetched.
	 */
	private String beanId;

	protected String getBeanId() {
		ObjectHelper.checkNotNull("field:beanId", beanId);
		return this.beanId;
	}

	public void setBeanId(final String beanId) {
		ObjectHelper.checkNotNull("parameter:beanId", beanId);
		this.beanId = beanId;
	}

	/**
	 * The actual template file is selected depending on whether the method
	 * returns void or not.
	 * 
	 * @return
	 */
	protected String getFileName() {
		return Constants.ADD_ADVICE_TEMPLATE;
	}

	protected InputStream getInputStream() {
		final String filename = this.getFileName();
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.ADD_ADVICE_BEAN_ID.equals(name)) {
				value = new StringLiteral(this.getBeanId());
				break;
			}
			if (Constants.ADD_ADVICE_BEAN_FACTORY.equals(name)) {
				value = this.getBeanFactory();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \"" + this.getFileName() + "\".");
	}
}
