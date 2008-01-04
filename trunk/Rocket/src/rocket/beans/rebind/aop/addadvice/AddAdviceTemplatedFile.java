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
package rocket.beans.rebind.aop.addadvice;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the add advice template
 * 
 * @author Miroslav Pokorny
 */
public class AddAdviceTemplatedFile extends TemplatedFileCodeBlock {

	public AddAdviceTemplatedFile() {
		super();
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
	 * Returns the name of the template resource
	 * 
	 * @return The filename
	 */
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}
	
	public InputStream getInputStream(){
		return super.getInputStream(); // TODO delete when merged into parent template package
	}

	public Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.BEAN_ID.equals(name)) {
				value = new StringLiteral(this.getBeanId());
				break;
			}
			if (Constants.BEAN_FACTORY.equals(name)) {
				value = this.getBeanFactory();
			}
			break;
		}
		return value;
	}
}
