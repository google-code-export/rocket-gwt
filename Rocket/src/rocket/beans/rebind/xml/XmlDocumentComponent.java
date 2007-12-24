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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rocket.beans.rebind.placeholder.PlaceHolderResolver;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Convenient base class that includes helpers to read attributes and the tag
 * body with support for replacing placeholders with actual values.
 * 
 * @author Miroslav Pokorny
 */
class XmlDocumentComponent {

	protected String getAttribute(final String attributeName) {
		return getAttribute(this.getElement(), attributeName);
	}

	protected String getAttribute(final Element element, final String attributeName) {
		String value = element.getAttribute(attributeName);
		if (false == StringHelper.isNullOrEmpty(value)) {
			value = this.getPlaceHolderResolver().resolve(value);
		}
		return value;
	}

	protected String getBody() {
		String value = this.getElement().getTextContent();
		if (false == StringHelper.isNullOrEmpty(value)) {
			value = this.getPlaceHolderResolver().resolve(value);
		}
		return value;
	}

	private Element element;

	protected Element getElement() {
		ObjectHelper.checkNotNull("field:element", element);
		return this.element;
	}

	public void setElement(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.element = element;
	}

	private PlaceHolderResolver placeHolderResolver;

	protected PlaceHolderResolver getPlaceHolderResolver() {
		ObjectHelper.checkNotNull("field:placeHolderResolver", placeHolderResolver);
		return this.placeHolderResolver;
	}

	public void setPlaceHolderResolver(final PlaceHolderResolver placeHolderResolver) {
		ObjectHelper.checkNotNull("parameter:placeHolderResolver", placeHolderResolver);
		this.placeHolderResolver = placeHolderResolver;
	}

	/**
	 * Filters out returning a list containing only elements from the given
	 * NodeList
	 * 
	 * @param nodeList
	 * @return A list of Elements
	 */
	protected List getElements(final NodeList nodeList) {
		ObjectHelper.checkNotNull("parameter:nodeList", nodeList);

		final List elements = new ArrayList();
		final int nodeCount = nodeList.getLength();

		for (int i = 0; i < nodeCount; i++) {
			final Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				elements.add(node);
			}
		}

		return elements;
	}

	protected ValueTag getValue(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		ValueTag value = null;

		while (true) {
			final String tagName = element.getTagName();

			if (tagName.equals(Constants.VALUE_TAG)) {
				value = createStringTag(element);
				break;
			}
			if (tagName.equals(Constants.LIST_TAG)) {
				value = createListTag(element);
				break;
			}
			if (tagName.equals(Constants.SET_TAG)) {
				value = createSetTag(element);
				break;
			}
			if (tagName.equals(Constants.MAP_TAG)) {
				value = createMapTag(element);
				break;
			}
			if (tagName.equals(Constants.BEAN_REFERENCE_TAG)) {
				value = createBeanReferenceTag(element);
				break;
			}
			throw new UnsupportedOperationException(tagName);
		}

		return value;
	}

	protected StringTag createStringTag(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final StringTag tag = new StringTag();
		tag.setElement(element);
		tag.setPlaceHolderResolver(this.getPlaceHolderResolver());
		return tag;
	}

	protected ListTag createListTag(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final ListTag tag = new ListTag();
		tag.setElement(element);
		tag.setPlaceHolderResolver(this.getPlaceHolderResolver());
		return tag;
	}

	protected SetTag createSetTag(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final SetTag tag = new SetTag();
		tag.setElement(element);
		tag.setPlaceHolderResolver(this.getPlaceHolderResolver());
		return tag;
	}

	protected MapTag createMapTag(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final MapTag tag = new MapTag();
		tag.setElement(element);
		tag.setPlaceHolderResolver(this.getPlaceHolderResolver());
		return tag;
	}

	protected BeanReferenceTag createBeanReferenceTag(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final BeanReferenceTag tag = new BeanReferenceTag();
		tag.setElement(element);
		tag.setPlaceHolderResolver(this.getPlaceHolderResolver());
		return tag;
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
	
	public String toString(){
		return this.getElement().toString();
	}
}
