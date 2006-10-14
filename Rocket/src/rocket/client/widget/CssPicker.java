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
package rocket.client.widget;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rocket.client.style.StyleSheet;
import rocket.client.style.StyleSheetsCollection;
import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.util.StringHelper;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget presents a series of buttons with the title of available LINKED stylesheets for a web application. For a LINKed stylesheet to
 * be recognized and made a candidate it must have a title, href. Only one LINKed stylesheet should be active with all others disabled.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class CssPicker extends Composite {

    public CssPicker() {
        this.setLabel(WidgetConstants.CSS_PICKER_LABEL_TEXT);
        this.setMappings(new HashMap());
        this.initWidget(this.createHorizontalPanel());
    }

    /**
     * This panel contains the buttons created for each of the found external stylesheets.
     */
    private HorizontalPanel horizontalPanel;

    protected HorizontalPanel getHorizontalPanel() {
        ObjectHelper.checkNotNull("field:horizontalPanel", horizontalPanel);
        return horizontalPanel;
    }

    protected boolean hasHorizontalPanel() {
        return null != this.horizontalPanel;
    }

    protected void setHorizontalPanel(final HorizontalPanel horizontalPanel) {
        ObjectHelper.checkNotNull("parameter:horizontalPanel", horizontalPanel);
        this.horizontalPanel = horizontalPanel;
    }

    protected HorizontalPanel createHorizontalPanel() {
        final HorizontalPanel panel = new HorizontalPanel();
        this.setHorizontalPanel(panel);
        panel.addStyleName(WidgetConstants.CSS_PICKER_STYLE);
        panel.addStyleName(WidgetConstants.CSS_PICKER_HORIZONTAL_PANEL_STYLE);

        final Widget label = this.createLabel();
        panel.add(label);
        panel.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_BOTTOM);

        this.createButtons(panel);
        this.selectStyleSheet(this.getButton(0));
        return panel;
    }

    /**
     * Creates the label that preceeds the available css stylesheet boxes/buttons
     * 
     * @return
     */
    protected Widget createLabel() {
        final Label label = new Label(this.getLabel());
        label.addStyleName(WidgetConstants.CSS_PICKER_LABEL_STYLE);
        return label;
    }

    /**
     * This text is used as the preceeding label for this css picker. It must be set before an attempt is made to fetch or create the
     * associated widget.
     */
    private String label;

    public String getLabel() {
        StringHelper.checkNotEmpty("field:label", label);
        return label;
    }

    public void setLabel(final String label) {
        StringHelper.checkNotEmpty("parameter:label", label);
        this.label = label;
    }

    protected void createButtons(final HorizontalPanel panel) {
        ObjectHelper.checkNotNull("parameter:panel", panel);

        final StyleSheetsCollection styleSheets = new StyleSheetsCollection();

        // loop thru creating a button for each available stylesheet.
        final Iterator iterator = styleSheets.iterator();
        while (iterator.hasNext()) {
            final StyleSheet styleSheet = (StyleSheet) iterator.next();
            if (false == styleSheet.isExternalFile()) {
                continue;
            }

            final Button button = this.createButton(styleSheet.getTitle());
            this.setStyleSheet(button, styleSheet);
            panel.add(button);
            panel.setCellVerticalAlignment(button, HasVerticalAlignment.ALIGN_BOTTOM);
        }
    }

    public Button getButton(final int index) {
        PrimitiveHelper.checkIsPositive("parameter:index", index);
        return (Button) this.getHorizontalPanel().getWidget(index + 1);// skip
        // the
        // first
        // Label
        // widget
    }

    public int getButtonCount() {
        return this.getHorizontalPanel().getWidgetCount() - 1; // less 1
        // because the
        // count
        // shouldnt
        // include the
        // Label widget
    }

    /**
     * This map contains a mapping between Buttons and their corresponding StyleSheet DOM objects.
     */
    private Map mappings;

    protected Map getMappings() {
        ObjectHelper.checkNotNull("field:mappings", mappings);
        return mappings;
    }

    protected void setMappings(final Map mappings) {
        ObjectHelper.checkNotNull("parameter:mappings", mappings);
        this.mappings = mappings;
    }

    public StyleSheet getStyleSheet(final int index) {
        return this.getStyleSheet(this.getButton(index));
    }

    protected StyleSheet getStyleSheet(final Button button) {
        ObjectHelper.checkNotNull("parameter:button", button);
        final StyleSheet styleSheet = (StyleSheet) this.getMappings().get(button);
        if (null == styleSheet) {
            ObjectHelper.handleAssertFailure("parameter:button", "Unable find the styleSheet for the parameter:button");
        }
        return styleSheet;
    }

    protected void setStyleSheet(final Button button, final StyleSheet styleSheet) {
        ObjectHelper.checkNotNull("parameter:button", button);
        ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);

        final Map mappings = this.getMappings();
        if (mappings.containsKey(button)) {
            ObjectHelper.handleAssertFailure("parameter:button", "The parameter:button has already been mapped.");
        }
        mappings.put(button, styleSheet);
    }

    protected Button createButton(final String title) {
        StringHelper.checkNotNull("parameter:title", title);

        final Button button = new Button(title);
        button.addStyleName(WidgetConstants.CSS_ITEM_STYLE);

        final CssPicker that = this;

        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                that.selectStyleSheet(button);
            }
        });

        return button;
    }

    public void selectStyleSheet(final int index) {
        this.selectStyleSheet(this.getButton(index));
    }

    /**
     * Makes a stylesheet active and also changes its corresponding button to show this.
     * 
     * @param button
     */
    protected void selectStyleSheet(final Button button) {
        ObjectHelper.checkNotNull("parameter:button", button);

        this.unselectAllStyleSheets();
        button.addStyleName(WidgetConstants.CSS_ITEM_SELECTED_STYLE);

        final StyleSheet styleSheet = this.getStyleSheet(button);
        styleSheet.setDisabled(false);
    }

    /**
     * Unselects all stylesheets and deselects their corresponding button
     * 
     */
    protected void unselectAllStyleSheets() {
        final Iterator iterator = this.getMappings().keySet().iterator();
        while (iterator.hasNext()) {
            final Button button = (Button) iterator.next();
            this.unselectStyleSheet(button);
        }
    }

    /**
     * Unselects the given stylesheet and its corresponding button
     * 
     * @param button
     */
    protected void unselectStyleSheet(final Button button) {
        ObjectHelper.checkNotNull("parameter:button", button);

        button.removeStyleName(WidgetConstants.CSS_ITEM_SELECTED_STYLE);

        final StyleSheet styleSheet = this.getStyleSheet(button);
        styleSheet.setDisabled(true);
    }

    public String toString() {
        return super.toString() + ", horizontalPanel: " + horizontalPanel + ", mappings: " + mappings;
    }
}