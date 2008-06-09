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
package rocket.beans.rebind.registerfactorybeans;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.beans.rebind.Bean;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.util.client.Checker;

/**
 * An abstraction for the register-factory-beans template
 * 
 * @author Miroslav Pokorny
 */
public class RegisterFactoryBeansTemplatedFile extends TemplatedFileCodeBlock {

	public RegisterFactoryBeansTemplatedFile() {
		super();
		this.setBeans(this.createBeans());
	}

	protected String getResourceName() {
		return Constants.REGISTER_FACTORY_BEANS_TEMPLATE;
	}

	private List beans;

	protected List<Bean> getBeans() {
		Checker.notNull("field:beans", beans);
		return this.beans;
	}

	protected void setBeans(final List<Bean> beans) {
		Checker.notNull("parameter:beans", beans);
		this.beans = beans;
	}

	protected List<Bean> createBeans() {
		return new ArrayList<Bean>();
	}

	public void addBean(final Bean bean) {
		Checker.notNull("parameter:bean", bean);
		this.getBeans().add(bean);
	}

	protected CodeBlock getBeansCodeBlock() {
		final RegisterBeanTemplatedFile registerBean = new RegisterBeanTemplatedFile();

		return new CollectionTemplatedCodeBlock<Bean>() {

			@Override
			public InputStream getInputStream() {
				return registerBean.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return registerBean.getValue0(name);
			}

			@Override
			protected Collection<Bean> getCollection() {
				return RegisterFactoryBeansTemplatedFile.this.getBeans();
			}

			@Override
			protected void prepareToWrite(Bean bean) {
				registerBean.setBeanId(bean.getId());
				registerBean.setFactoryBean(bean.hasProxy() ? bean.getProxyFactoryBean() : bean.getFactoryBean());
			}

			@Override
			protected void writeBetweenElements(SourceWriter writer) {
				writer.println();
			}
		};
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.REGISTER_FACTORY_BEANS_REGISTER_BEANS.equals(name)) {
				value = this.getBeansCodeBlock();
				break;
			}
			break;
		}
		return value;
	}
}
