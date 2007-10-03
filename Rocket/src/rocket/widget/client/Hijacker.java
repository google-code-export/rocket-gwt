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
package rocket.widget.client;

import rocket.dom.client.Dom;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * This class provides a simple mechanism to store the dom location of a given element, and have the same element
 * restored to recorded location.
 * 
 * This class is used to implement hijacking of dom elements to a supported wrapper widget etc. passing a 
 * TEXTAREA element to a TextArea widget. The TextArea typically adds the wrapped TextArea widget to the RootPanel.
 * The RootPanel class then adds the TEXTAREA element to the document body. The hijacker then restores the TEXTAREA
 * element to its original dom location, thus making everything just right.
 * 
 * @author Miroslav Pokorny
 */
public class Hijacker {

	public Hijacker(final Element element) {
		this.setElement(element);
		this.save();
	}

	private Element element;

	Element getElement() {
		ObjectHelper.checkNotNull("field:element", element);
		return element;
	}

	void setElement(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.element = element;
	}

	/**
	 * The parent of the element
	 */
	private Element parent;

	Element getParent() {
		return this.parent;
	}	

	void setParent(final Element parent) {
		ObjectHelper.checkNotNull("parameter:parent", parent);
		this.parent = parent;
	}

	void clearHijackParent() {
		this.parent = null;
	}

	/**
	 * The child index of element as a child of parent.
	 */
	private int childIndex;

	int getChildIndex() {
		PrimitiveHelper.checkGreaterThanOrEqual("field:childIndex", childIndex, 0);
		return this.childIndex;
	}

	void setChildIndex(final int childIndex) {
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:childIndex", childIndex, 0);
		this.childIndex = childIndex;
	}

	void save() {
		final Element element = this.getElement();
		if (Dom.isAttached(element)) {

			final Element parent = DOM.getParent(element);
			final int childIndex = DOM.getChildIndex(parent, element);

			this.setParent(parent);
			this.setChildIndex(childIndex);
		}
	}

	public void restore() {
		final Element parent = this.getParent();
		if( null != parent ){
			DOM.insertChild(parent, this.getElement(), this.getChildIndex());
		}
	}
}
