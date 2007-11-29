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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * An abstraction for the eager-singleton-bean-names.txt template
 * 
 * @author Miroslav Pokorny
 */
public class EagerSingletonBeanNamesTemplatedFile extends TemplatedCodeBlock {

	public EagerSingletonBeanNamesTemplatedFile() {
		super();
		this.setBeans(this.createBeans());
	}
	
	public boolean isNative(){
		return false;
	}
	
	public void setNative( final boolean ignored){
		throw new UnsupportedOperationException();
	}

	/**
	 * A list of bean names.
	 */
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

	public void addBean(final String beanId ) {
		StringHelper.checkNotEmpty("parameter:beanId", beanId );
		
		this.getBeans().add( beanId );
	}

	protected CodeBlock getBeansCodeBlock() {
		final EagerSingletonBeanNameTemplatedFile singletonName = new EagerSingletonBeanNameTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return singletonName.getInputStream();
			}

			protected Object getValue0(final String name) {
				return singletonName.getValue0(name);
			}

			protected Collection getCollection() {
				return EagerSingletonBeanNamesTemplatedFile.this.getBeans();
			}

			protected void prepareToWrite(final Object element) {
				final String beanId = (String) element;
				singletonName.setBeanId( beanId );
			}

			protected void writeBetweenElements(final SourceWriter writer) {
				writer.println( ",");
			}
		};
	}

	protected InputStream getInputStream() {
		final String filename = Constants.EAGER_SINGLETON_BEAN_NAMES_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.EAGER_SINGLETON_BEAN_NAMES_ARRAY.equals(name)) {
				value = this.getBeansCodeBlock();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \""
				+ Constants.EAGER_SINGLETON_BEAN_NAMES_TEMPLATE + "\".");
	}
}
