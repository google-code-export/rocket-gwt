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
package rocket.widget.client.menu;

import java.util.Iterator;

import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.widget.client.DivPanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A VerticalMenuList contains menu items that grow down the screen.
 * VerticalMenuLists are mostly only used as the MenuList for non Menu widgets.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class VerticalMenuList extends MenuList {

	public VerticalMenuList() {
		super();
	}

	protected Panel createPanel() {
		return this.createDivPanel();
	}

	protected String getInitialStyleName() {
		return Constants.VERTICAL_MENU_LIST_STYLE;
	}
	
	protected void open() {
		final Element element = this.getElement();
		if (this.isHideable()) {
			InlineStyle.setInteger(element, StyleConstants.Z_INDEX, 1, CssUnit.NONE);			
			InlineStyle.setString(element, StyleConstants.DISPLAY, "block" );
			InlineStyle.setString(element, StyleConstants.VISIBILITY, "visible");
		}
	}

	// PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	public Widget get(final int index) {
		return this.getDivPanel().get(index);
	}

	public void insert(final Widget widget, final int beforeIndex) {
		this.getDivPanel().insert(widget, beforeIndex);
		
		final MenuWidget menuItem = (MenuWidget) widget;
		menuItem.setParentMenuList(this);
	}

	protected boolean remove0(final Widget widget) {
		return this.getDivPanel().remove(widget);
	}

	public int getWidgetCount() {
		return this.getDivPanel().getWidgetCount();
	}

	public Iterator iterator() {
		return this.getDivPanel().iterator();
	}

	protected DivPanel getDivPanel() {
		return (DivPanel) this.getPanel();
	}

	protected DivPanel createDivPanel() {
		return new DivPanel();
	}
}
