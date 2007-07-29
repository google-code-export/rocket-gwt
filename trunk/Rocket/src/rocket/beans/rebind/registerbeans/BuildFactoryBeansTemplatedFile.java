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

import rocket.generator.rebind.codeblock.ManyCodeBlocks;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.type.NewNestedType;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the invoker add template
 * 
 * @author Miroslav Pokorny
 */
public class BuildFactoryBeansTemplatedFile extends TemplatedCodeBlock {

	public BuildFactoryBeansTemplatedFile() {
		super();
		setNative(false);
		this.setBeans(this.createBeans());
	}

	private ManyCodeBlocks beans;

	protected ManyCodeBlocks getBeans() {
		ObjectHelper.checkNotNull("field:beans", beans);
		return this.beans;
	}

	protected void setBeans(final ManyCodeBlocks beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);
		this.beans = beans;
	}

	protected ManyCodeBlocks createBeans() {
		return new ManyCodeBlocks();
	}

	public void addBean(final String beanId, final NewNestedType factoryBean) {
		final RegisterBeanTemplatedFile register = new RegisterBeanTemplatedFile();
		register.setBeanId(beanId);
		register.setFactoryBean(factoryBean);

		this.getBeans().add(register);
	}

	protected InputStream getInputStream() {
		final String filename = Constants.BUILD_FACTORY_BEANS_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.BUILD_FACTORY_BEANS_REGISTER_BEANS.equals(name)) {
				value = this.getBeans();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found in ["
				+ Constants.BUILD_FACTORY_BEANS_TEMPLATE + "]");
	}
}
