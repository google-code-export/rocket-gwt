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

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import rocket.beans.rebind.placeholder.PlaceHolderResolver;
import rocket.util.client.StringHelper;

/**
 * A bean like view of a bean tag
 * 
 * @author Miroslav Pokorny
 */
public class BeanTag extends XmlDocumentComponent {

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

	public boolean isLazyLoaded(){
		return false == this.isEagerLoaded();
	}
	
	public boolean isEagerLoaded(){
		// if the attribute is missing this will return false.
		return Constants.EAGERLY_LOADED.equals(this.getLazyLoaded());
	}
	
	public String getFactoryMethod() {
		return this.getAttribute(Constants.BEAN_FACTORY_METHOD_NAME_ATTRIBUTE);
	}

	public String getInitMethod() {
		return this.getAttribute(Constants.BEAN_INIT_METHOD_NAME_ATTRIBUTE);
	}

	public String getLazyLoaded() {
		return this.getAttribute(Constants.LAZY_LOADED_ATTRIBUTE);
	}
	
	public List getConstructorArguments() {
		List arguments = Collections.EMPTY_LIST;

		final NodeList constructorsNodeList = this.getElement().getElementsByTagName(Constants.CONSTRUCTOR_TAG);
		final Element constructorElement = (Element) constructorsNodeList.item(0);
		if (null != constructorElement) {

			final List valueElements = this.getElements(constructorElement.getChildNodes());

			arguments = new AbstractList() {
				public Object get(final int index) {
					final Element element = (Element) valueElements.get(index);
					return BeanTag.this.getValue(element);
				}

				public int size() {
					return valueElements.size();
				}
			};
		}

		return arguments;
	}

	public List getProperties() {
		List properties = Collections.EMPTY_LIST;

		final NodeList propertiesNodeList = this.getElement().getElementsByTagName(Constants.PROPERTIES_TAG);
		final Element propertiesElement = (Element) propertiesNodeList.item(0);
		if (null != propertiesElement) {

			final NodeList propertys = propertiesElement.getElementsByTagName(Constants.PROPERTY_TAG);
			final PlaceHolderResolver placeHolderResolver = this.getPlaceHolderResolver();

			properties = new AbstractList() {
				public Object get(final int index) {
					final PropertyTag propertyTag = new PropertyTag();
					propertyTag.setElement((Element) propertys.item(index));
					propertyTag.setPlaceHolderResolver(placeHolderResolver);
					return propertyTag;
				}

				public int size() {
					return propertys.getLength();
				}
			};
		}

		return properties;
	}
	
	/**
	 * The original file that contained this very tag.
	 */
	private String filename;
	
	public String getFilename(){
		StringHelper.checkNotEmpty( "field:filename", filename );
		return this.filename;
	}
	
	public void setFilename( final String filename ){
		StringHelper.checkNotEmpty( "parameter:filename", filename );
		this.filename = filename;
	}
}
