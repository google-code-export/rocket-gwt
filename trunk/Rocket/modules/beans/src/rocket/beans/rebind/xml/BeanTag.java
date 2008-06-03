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
package rocket.beans.rebind.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import rocket.util.client.Checker;

/**
 * A bean like view of a bean tag, as well as providing a number of helper
 * methods relating to understanding the xml document structure.
 * 
 * @author Miroslav Pokorny
 */
class BeanTag extends XmlDocumentComponent {

	public String getClassName() {
		return this.getAttribute(Constants.BEAN_CLASSNAME_ATTRIBUTE);
	}

	public String getId() {
		return this.getAttribute(Constants.BEAN_ID_ATTRIBUTE);
	}

	protected String getScope() {
		return this.getAttribute(Constants.BEAN_SCOPE_ATTRIBUTE);
	}

	public boolean isSingleton() {
		return Constants.SINGLETON.equals(this.getScope());
	}

	public boolean isPrototype() {
		return Constants.PROTOTYPE.equals(this.getScope());
	}

	public boolean isLazyLoaded() {
		return false == this.isEagerLoaded();
	}

	public boolean isEagerLoaded() {
		// if the attribute is missing this will return false.
		return Constants.EAGERLY_LOADED.equals(this.getLazyLoaded());
	}

	public String getFactoryMethod() {
		return this.getAttribute(Constants.BEAN_FACTORY_METHOD_NAME_ATTRIBUTE);
	}

	public String getInitMethod() {
		return this.getAttribute(Constants.BEAN_INIT_METHOD_NAME_ATTRIBUTE);
	}

	public String getDestroyMethod() {
		return this.getAttribute(Constants.BEAN_DESTROY_METHOD_NAME_ATTRIBUTE);
	}

	public String getLazyLoaded() {
		return this.getAttribute(Constants.LAZY_LOADED_ATTRIBUTE);
	}

	/**
	 * Returns a list of all the values found under the constructor element of
	 * this bean element
	 * 
	 * @return A list of elements which are all values.
	 */
	public List<Element> getConstructorValues() {
		List<Element> values = new ArrayList<Element>();

		final Element constructor = this.getFirstChildByTagName(this.getElement(), Constants.CONSTRUCTOR_TAG);
		if (null != constructor) {
			values = this.getElements(constructor);
		}

		return values;
	}

	/**
	 * Returns a list of all the property elements found under the properties
	 * element belonging to this bean element.
	 * 
	 * @return A list of property elements.
	 */
	public List<Element> getProperties() {
		List<Element> properties = new ArrayList<Element>();

		final Element propertiesElement = this.getFirstChildByTagName(this.getElement(), Constants.PROPERTIES_TAG);
		if (null != propertiesElement) {
			properties = this.getElements(propertiesElement);			
		}

		return properties;
	}

	/**
	 * The original file that contained this very tag.
	 */
	private String filename;

	public String getFilename() {
		Checker.notEmpty("field:filename", filename);
		return this.filename;
	}

	public void setFilename(final String filename) {
		Checker.notEmpty("parameter:filename", filename);
		this.filename = filename;
	}
}
