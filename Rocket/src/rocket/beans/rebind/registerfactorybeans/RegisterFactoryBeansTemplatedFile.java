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
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.util.client.ObjectHelper;

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
	
	protected String getResourceName(){
		return Constants.REGISTER_FACTORY_BEANS_TEMPLATE;
	}

	private List beans;

	protected List getBeans() {
		ObjectHelper.checkNotNull("field:beans", beans);
		return this.beans;
	}

	protected void setBeans(final List beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);
		this.beans = beans;
	}

	protected List createBeans() {
		return new ArrayList();
	}

	public void addBean(final Bean bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		this.getBeans().add(bean);
	}
	
	protected CodeBlock getBeansCodeBlock() {
		final RegisterBeanTemplatedFile registerBean = new RegisterBeanTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return registerBean.getInputStream();
			}

			protected Object getValue0(final String name) {
				return registerBean.getValue0(name);
			}

			protected Collection getCollection() {
				return RegisterFactoryBeansTemplatedFile.this.getBeans();
			}

			protected void prepareToWrite(Object element) {
				final Bean bean = (Bean) element;
				registerBean.setBeanId(bean.getId());
				registerBean.setFactoryBean(bean.hasProxy() ? bean.getProxyFactoryBean() : bean.getFactoryBean());
			}

			protected void writeBetweenElements(SourceWriter writer) {
				writer.println();
			}
		};
	}
	
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
