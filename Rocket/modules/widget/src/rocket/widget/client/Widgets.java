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

import java.util.Iterator;

import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.Checker;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This Helper contains a number of useful methods related to working with GWT
 * widgets and the browser in general.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Widgets{

	/**
	 * Given an element attempts to find which widget it is a child of. This is
	 * particularly useful when a panel contains many widgets which in
	 * themselves are made up of many elements and one needs to determine which
	 * widget the event belongs too.
	 * 
	 * @param target
	 * @param widgets
	 * @return The widget or null if a match was not possible.
	 */
	public static Widget findWidget(final Element target, final Iterator widgets) {
		Checker.notNull("parameter:target", target);
		Checker.notNull("parameter:widgets", widgets);

		Widget widget = null;
		while (widgets.hasNext()) {
			final Widget otherWidget = (Widget) widgets.next();
			if (DOM.isOrHasChild(target, otherWidget.getElement())) {
				widget = otherWidget;
				break;
			}
		}
		return widget;
	}

	/**
	 * Helper used by CompositePanel to invoke the non visible
	 * Widget.setParent() method.
	 * 
	 * @param widget
	 * @param panel
	 */
	static native void widgetSetParent(final com.google.gwt.user.client.ui.Widget widget, final com.google.gwt.user.client.ui.Panel panel)/*-{
	 widget.@com.google.gwt.user.client.ui.Widget::setParent(Lcom/google/gwt/user/client/ui/Widget;)(panel);
	 }-*/;

	/**
	 * This method exists purely to assist testing of widgets that rely on absolutely or relatively positioning of elements/widgets.
	 * 
	 * By inserting a new element to body and the scrolling past it a demo and have it work correctly a demo can be confident
	 * that it works! 
	 * @param y The size of the padding element.
	 */
	public static void forceDocumentContentsToScroll( final int y ){		
		final Element before = DOM.createDiv();
		DOM.setInnerHTML(before, "This element should not be visible, if it is scroll the window until its out of view." );
		
		InlineStyle.setDouble(before, Css.WIDTH, 90, CssUnit.PERCENTAGE );
		InlineStyle.setInteger(before, Css.HEIGHT, 100, CssUnit.PX);
		InlineStyle.setString(before, Css.BACKGROUND_COLOR, "white" );
		InlineStyle.setString(before, Css.BORDER_COLOR, "red" );
		InlineStyle.setInteger(before, Css.BORDER_WIDTH, 1, CssUnit.PX );
		InlineStyle.setString(before, Css.BORDER_STYLE, "dotted" );
		InlineStyle.setInteger(before, Css.PADDING, 4, CssUnit.PX );
		InlineStyle.setString(before, Css.COLOR, "black" );
		//InlineStyle.setString(before, Css.WORD_SPACING, "nowrap" );
		
		final Element body = RootPanel.getBodyElement();
		DOM.insertChild( body, before, 0 );
		
		// scroll into the element after $before.
		DOM.scrollIntoView( DOM.getChild(body, 1 ) );

		final Element after = DOM.createDiv();
		DOM.setInnerHTML(after, "." );
		
		InlineStyle.setDouble(after, Css.WIDTH, 90, CssUnit.PERCENTAGE );
		InlineStyle.setInteger(after, Css.HEIGHT, 100, CssUnit.PX);
		InlineStyle.setString(after, Css.BACKGROUND_COLOR, "white" );
		InlineStyle.setString(after, Css.COLOR, "black" );
		
		DOM.appendChild( body, after );		
	}
}