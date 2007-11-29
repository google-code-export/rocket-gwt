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
package rocket.beans.rebind.loadeagersingletons;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.util.client.StringHelper;

/**
 * An abstraction for the eager-singleton-bean-name.txt template
 * 
 * @author Miroslav Pokorny
 */
class EagerSingletonBeanNameTemplatedFile extends TemplatedCodeBlock {

	public EagerSingletonBeanNameTemplatedFile() {
		super();
	}

	public boolean isNative(){
		return false;
	}
	
	public void setNative( final boolean ignored ){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * The name of a singleton bean.
	 */
	private String beanId;

	protected String getBeanId() {
		StringHelper.checkNotEmpty("beanId:beanId", beanId);
		return this.beanId;
	}

	public void setBeanId(final String beanId) {
		StringHelper.checkNotEmpty("parameter:beanId", beanId);
		this.beanId = beanId;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.EAGER_SINGELTON_BEAN_NAME_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.EAGER_SINGELTON_BEAN_NAME_BEAN_ID.equals(name)) {
				value = new StringLiteral(this.getBeanId());
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \""
				+ Constants.EAGER_SINGELTON_BEAN_NAME_TEMPLATE + "\".");
	}
}
