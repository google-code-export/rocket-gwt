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
import java.util.Collections;
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

	/**
	 * The source element from the xml document.
	 */
	private Element element;

	protected Element getElement() {
		ObjectHelper.checkNotNull("field:element", element);
		return this.element;
	}

	public void setElement(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.element = element;
	}

	/**
	 * A placeholder which will be used when resolving text to a final form or
	 * text value.
	 */
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
	 * Helper which finds the first child (ignoring grandchildren etc) of parent
	 * with the given tag name.
	 * 
	 * @param parent
	 *            The parent element
	 * @param tagName
	 *            The child tag to scan for.
	 * @return May be null if no child exists or the element.
	 */
	protected Element getFirstChildByTagName(final Element parent, final String tagName) {
		Element element = null;

		final NodeList nodeList = parent.getChildNodes();
		final int nodeCount = nodeList.getLength();
		for (int i = 0; i < nodeCount; i++) {
			final Node node = nodeList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			final Element child = (Element) node;
			if (false == child.getTagName().equals(tagName)) {
				continue;
			}
			element = child;
			break;
		}

		return element;
	}

	/**
	 * Helper which finds child element belonging to the given parent.
	 * 
	 * @param parent
	 *            The parent element
	 * @return May be null if no child exists or the element.
	 */
	protected Element getFirstElement(final Element parent) {
		return (Element) this.getElements(parent).get(0);
	}

	/**
	 * Returns a list that contains only child elements of the given parent
	 * element.
	 * 
	 * @param parent
	 *            The parent element
	 * @return A list which may be empty if the parent has no child elements.
	 */
	protected List getElements(final Element parent) {
		final List elements = new ArrayList();

		final NodeList nodeList = parent.getChildNodes();
		final int nodeCount = nodeList.getLength();
		for (int i = 0; i < nodeCount; i++) {
			final Node node = nodeList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			elements.add((Element) node);
		}

		return Collections.unmodifiableList(elements);
	}

	/**
	 * The original file that contained this very tag.
	 */
	private String filename;

	public String getFilename() {
		StringHelper.checkNotEmpty("field:filename", filename);
		return this.filename;
	}

	public void setFilename(final String filename) {
		StringHelper.checkNotEmpty("parameter:filename", filename);
		this.filename = filename;
	}

	public String toString() {
		return this.getElement().toString();
	}
}
