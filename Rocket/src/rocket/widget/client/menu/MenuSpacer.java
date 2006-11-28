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
package rocket.widget.client.menu;

import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Menu spacers may be used to add a break within a menu list.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class MenuSpacer extends AbstractMenuItem {
    public MenuSpacer() {
        this.initWidget(this.createWidget());
    }

    // EVENT HANDLING............................

    protected void handleMouseClick(final Event event) {
    }

    protected void handleMouseOver(final Event event) {
    }

    protected void handleMouseOut(final Event event) {
    }

    // ACTIONS
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void open() {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + ".open()");
    }

    /**
     * MenuSpacers do nothing when asked to hide...
     */
    public void hide() {
    }

    protected void addHighlight() {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + ".addHighlight()");
    }

    protected void removeHighlight() {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + ".removeHighlight()");
    }

    protected String getSelectedStyle() {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + ".getSelectedStyle()");
    }

    protected String getDisabledStyle() {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + ".getDisabledStyle()");
    }

    protected Widget createWidget() {
        final HTML html = new HTML(MenuConstants.SPACER_HTML);
        html.addStyleName(MenuConstants.SPACER_STYLE);
        return html;
    }

    public String toString() {
        return ObjectHelper.defaultToString(this);
    }
}
