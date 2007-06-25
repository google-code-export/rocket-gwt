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

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;

/**
 * A menu item is an item that may appear in a list which may be clicked if its not disabled. When
 * 
 * @author Miroslav Pokorny (mP)
 */
public class MenuItem extends AbstractMenuItem {

    public MenuItem() {
        super();

        this.initWidget(this.createHtml());
    }

    // ACTIONS :::::::::::::::::::::::::::::::::::::::::::::::

    public void open() {
        final MenuList menuList = this.getParentMenuList();

        final Menu menu = menuList.getMenu();
        final MenuListenerCollection listeners = menu.getMenuListeners();
        if (listeners.fireBeforeMenuOpened(this)) {
            listeners.fireMenuOpened(this);
        }
        menu.hide();
    }

    public void hide() {
        this.removeHighlight();
    }

    protected String getSelectedStyle() {
        return MenuConstants.MENU_ITEM_SELECTED_STYLE;
    }

    protected String getDisabledStyle() {
        return MenuConstants.MENU_ITEM_DISABLED_STYLE;
    }

    // EVENT HANDLING ::::::::::::::::::::::::::::::::::::

    /**
     * This event is only fired if the MenuItem is not disabled.
     */
    protected void handleMouseClick(final Event event) {
        if (false == this.isDisabled()) {
            this.open();
        }
    }

    /**
     * Highlights this widget
     */
    protected void handleMouseOver(final Event event) {
        if (false == this.isDisabled()) {
            this.addHighlight();
        }
    }

    /**
     * Unhighlights this widget.
     */
    protected void handleMouseOut(final Event event) {
        if (false == this.isDisabled()) {
            this.removeHighlight();
        }
    }

    // COMPOSITE
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public String getText() {
        return this.getHtml().getText();
    }

    public void setText(final String text) {
        StringHelper.checkNotEmpty("parameter:text", text);
        this.getHtml().setText(text);
    }

    /**
     * A TEXT widget contains the text or label for this item.
     */
    private HTML html;

    protected HTML getHtml() {
        ObjectHelper.checkNotNull("field:html", html);
        return html;
    }

    protected void setHtml(final HTML html) {
        ObjectHelper.checkNotNull("parameter:html", html);
        this.html = html;
    }

    protected HTML createHtml() {
        final HTML html = new HTML();
        html.setWidth("100%");
        html.setStyleName(MenuConstants.MENU_ITEM_STYLE);
        this.setHtml(html);
        return html;
    }

    public String toString() {
        return ObjectHelper.defaultToString(this) + ", text[" + this.getText() + "]";
    }
}
