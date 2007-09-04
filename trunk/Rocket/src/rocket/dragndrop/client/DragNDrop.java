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
package rocket.dragndrop.client;

import rocket.dom.client.Dom;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A variety of helper methods directly related to drag n drop operations
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DragNDrop {
	/**
	 * Creates a clone of the element belonging to the given Widget.
	 * 
	 * @param widget
	 * @return
	 */
	public static Element createClone(final Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		return Dom.cloneElement(widget.getElement(), true);
	}

	/**
	 * Creates a rectangle that is the same size as the given widget.
	 * 
	 * @param widget
	 * @return
	 */
	public static Element createRectangle(final Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		final Element element = DOM.createDiv();

		final int width = widget.getOffsetWidth();
		InlineStyle.setInteger(element, StyleConstants.WIDTH, width, CssUnit.PX);

		final int height = widget.getOffsetHeight();
		InlineStyle.setInteger(element, StyleConstants.HEIGHT, height, CssUnit.PX);

		final StringBuffer buf = new StringBuffer();
		buf.append(DOM.getElementProperty(element, "className"));
		if (buf.length() > 0) {
			buf.append(' ');
		}
		buf.append(Constants.DRAG_N_DROP_BOX_OUTLINE_STYLE);

		DOM.setElementProperty(element, "className", buf.toString());
		return element;
	}
}
