/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.widget.menu;

import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;

import com.google.gwt.user.client.ui.HTML;

public class MenuItem extends AbstractMenuWidget implements MenuWidget {

    public MenuItem() {
        super();

        this.setWidget(this.createHtml());
    }

    // MENU WIDGET ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void open() {
        this.unselect();
    }

    public void close() {
        this.unselect();
    }

    public void select() {
        if (false == this.isDisabled()) {
            this.addStyleName(MenuConstants.MENU_ITEM_SELECTED_STYLE);
        }
    }

    public void unselect() {
        this.removeStyleName(MenuConstants.MENU_ITEM_SELECTED_STYLE);
    }

    public void setDisabled(final boolean disabled) {
        if (this.isDisabled()) {
            this.removeStyleName(MenuConstants.MENU_ITEM_DISABLED_STYLE);
        }
        super.setDisabled(disabled);
        if (disabled) {
            this.addStyleName(MenuConstants.MENU_ITEM_DISABLED_STYLE);
        }
    }

    // COMPOSITE ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public String getText() {
        return this.getHtml().getText();
    }

    public void setText(final String text) {
        StringHelper.checkNotEmpty("parameter:text", text);
        this.getHtml().setText(text);
    }

    /**
     * A HTML widget contains the menuItem.
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
        html.addStyleName(MenuConstants.MENU_ITEM_STYLE);
        this.setHtml(html);
        return html;
    }

}
