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
package rocket.beans.rebind;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import rocket.beans.client.BeanFactory;
import rocket.beans.rebind.bean.Bean;
import rocket.beans.rebind.bean.BeanIdAlreadyUsedException;
import rocket.beans.rebind.bean.BeanIdNotFoundException;
import rocket.generator.rebind.GeneratorContext;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;

/**
 * A Context that assists with the generation of a BeanFactory implementation. A
 * number of common methods and other data relating to the code generation
 * process are included in this class.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorContext extends GeneratorContext {

	public BeanFactoryGeneratorContext() {
		super();

		this.setBeans(new HashMap());
	}

	protected String getGeneratedClassNameSuffix() {
		return Constants.BEAN_FACTORY_IMPL;
	}

	public JClassType getBeanFactoryType() {
		return (JClassType) this.getType(BeanFactory.class.getName());
	}

	public InputStream getResource(final String typeName) {
		final JClassType type = (JClassType) this.getType(typeName);
		final JPackage jPackage = type.getPackage();

		final String resourceName = '/' + jPackage.getName().replace('.', '/')
				+ '/' + type.getSimpleSourceName() + Constants.SUFFIX;
		try {
			final InputStream inputStream = Object.class
					.getResourceAsStream(resourceName);
			if (null == inputStream) {
				throw new BeanFactoryGeneratorException(
						"Unable to load resource with a filename ["
								+ resourceName + "] for the class [" + typeName
								+ "]");
			}
			return inputStream;
		} catch (final BeanFactoryGeneratorException rethrow) {
			throw rethrow;
		} catch (final Exception caught) {
			throw new BeanFactoryGeneratorException("Unable to load resource ["
					+ resourceName + "]");
		}
	}

	/**
	 * Adds a new bean definition to the map of already encountered Beans. If
	 * the id is already used a
	 * 
	 * @param bean
	 * @throws BeanIdAlreadyUsedException
	 */
	public void addBean(final Bean bean) throws BeanIdAlreadyUsedException {
		final String id = bean.getId();
		final Map beans = this.getBeans();
		if (beans.containsKey(id)) {
			this.throwBeanIdAlreadyUsedException(id);
		}

		beans.put(id, bean);
	}

	protected void throwBeanIdAlreadyUsedException(final String id)
			throws BeanIdAlreadyUsedException {
		throw new BeanIdAlreadyUsedException("The id[" + id
				+ "] has already been used ");
	}

	/**
	 * Fetches a bean definition by its id. If the id is not found a
	 * BeanIdNotFoundException is thrown.
	 * 
	 * @param id
	 * @return
	 * @throws BeanIdNotFoundException
	 */
	public Bean getBean(final String id) throws BeanIdNotFoundException {
		final Map beans = this.getBeans();
		final Bean bean = (Bean) beans.get(id);
		if (null == bean) {
			throwBeanIdNotFoundException(id);
		}
		return bean;
	}

	protected void throwBeanIdNotFoundException(final String id)
			throws BeanIdNotFoundException {
		throw new BeanIdNotFoundException("Unable to find a bean with the id["
				+ id + "].");
	}

	/**
	 * This map contains all known bean definitions encountered during parsing
	 * of the given xml file.
	 */
	private Map beans;

	public Map getBeans() {
		ObjectHelper.checkNotNull("field:beans", beans);
		return this.beans;
	}

	public void setBeans(final Map beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);
		this.beans = beans;
	}
}
