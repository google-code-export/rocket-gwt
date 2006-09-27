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

import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A spinner is a simple widget which allows a user to increase or decrease a number keeping it within a defined range. Two methods are
 * available if sub-classes wish to handle the clicking of either the up or down control
 *
 * To display the value a separate Label needs to be created and a listener registered to receive NumberValueChanged events. It is
 * thus possible to also create a separate text field and have it also update the spinner and vice versa. This widget only includes and
 * controls the up/down htmls.
 *
 * @author Miroslav Pokorny (mP)
 */
public class Spinner extends AbstractNumberHolder implements NumberHolder {

    public Spinner() {
    	this.initWidget( this.createPanel() );
    	
        this.setDownImageUrl(WidgetConstants.SPINNER_DOWN_IMAGE_URL);
        this.setUpImageUrl(WidgetConstants.SPINNER_UP_IMAGE_URL);
        this.setDelta(1);
    }

    /**
     * The current value of the spinner;
     */
    private int value;

    public int getValue() {
        return this.value;
    }

    public void setValue(final int value) {
        this.value = value;
        this.fireValueChanged();
    }

    /**
     * The minimum value of the spinner;
     */
    private int lowerBounds;

    public int getLowerBounds() {
        return this.lowerBounds;
    }

    public void setLowerBounds(final int lowerBounds) {
        this.lowerBounds = lowerBounds;
    }

    /**
     * The maximum value of the spinner.
     */
    private int upperBounds;

    public int getUpperBounds() {
        return this.upperBounds;
    }

    public void setUpperBounds(final int upperBounds) {
        this.upperBounds = upperBounds;
    }

    /**
     * The up html that when clicked increases the spinners value.
     */
    private Image upWidget;

    public Image getUpWidget() {
        ObjectHelper.checkNotNull("field:upWidget", upWidget);
        return this.upWidget;
    }

    public boolean hasUpWidget() {
        return this.upWidget != null;
    }

    public void setUpWidget(final Image upWidget) {
        ObjectHelper.checkNotNull("parameter:upWidget", upWidget);
        this.upWidget = upWidget;
    }

    public Widget createUpWidget() {
        WidgetHelper.checkNotAlreadyCreated("upWidget", this.hasUpWidget());

        final Spinner that = this;

        final Image image = new Image();
        image.setUrl(this.getUpImageUrl());
        image.addStyleName( WidgetConstants.SPINNER_UP_STYLE);
        image.addClickListener(new ClickListener() {

            public void onClick(final Widget widget) {
                that.onUpClick();
            }
        });
        this.setUpWidget(image);
        return image;
    }

    /**
     * Clicking on the up html increases the spinner's value.
     */
    protected void onUpClick() {
        final int value = this.getValue() + this.getDelta();
        this.updateValue(value);
    }

    protected void onUpperBoundsReached() {
    }

    /**
     * . The url of the up icon
     */
    private String upImageUrl;

    public String getUpImageUrl() {
        StringHelper.checkNotEmpty("field:upImageUrl", upImageUrl);
        return upImageUrl;
    }

    public void setUpImageUrl(final String upImageUrl) {
        StringHelper.checkNotEmpty("parameter:upImageUrl", upImageUrl);
        this.upImageUrl = upImageUrl;

        if (this.hasUpWidget()) {
            final Image image = this.getUpWidget();
            image.setUrl(upImageUrl);
        }
    }

    /**
     * The down html that when clicked decreases the spinners value.
     */
    private Image downWidget;

    public Image getDownWidget() {
        ObjectHelper.checkNotNull("field:downWidget", downWidget);
        return this.downWidget;
    }

    public boolean hasDownWidget() {
        return this.downWidget != null;
    }

    public void setDownWidget(final Image downWidget) {
        ObjectHelper.checkNotNull("parameter:downWidget", downWidget);
        this.downWidget = downWidget;
    }

    public Widget createDownWidget() {
        WidgetHelper.checkNotAlreadyCreated("downWidget", this.hasDownWidget());

        final Spinner that = this;

        final Image image = new Image();
        image.addStyleName( WidgetConstants.SPINNER_DOWN_STYLE);
        image.setUrl(this.getDownImageUrl());

        image.addClickListener(new ClickListener() {

            public void onClick(final Widget widget) {
                that.onDownClick();
            }
        });
        this.setDownWidget(image);
        return image;
    }

    /**
     * . The url of the down icon
     */
    private String downImageUrl;

    public String getDownImageUrl() {
        StringHelper.checkNotEmpty("field:downImageUrl", downImageUrl);
        return downImageUrl;
    }

    public void setDownImageUrl(final String downImageUrl) {
        StringHelper.checkNotEmpty("parameter:downImageUrl", downImageUrl);
        this.downImageUrl = downImageUrl;

        if (this.hasDownWidget()) {
            final Image image = this.getDownWidget();
            image.setUrl(downImageUrl);
        }
    }

    /**
     * Clicking on the down html decreass the value.
     *
     */
    protected void onDownClick() {
        final int value = this.getValue() - this.getDelta();
        this.updateValue(value);
    }

    protected void onLowerLimitReached() {
    }

    /**
     * Updates both the value and the label showing the value to the user.
     *
     * @param value
     */
    protected void updateValue(int value) {
        while (true) {
            final int lowerBounds = this.getLowerBounds();
            if (value < lowerBounds) {
                value = lowerBounds;
            }

            final int upperBounds = this.getUpperBounds();
            if (value > upperBounds) {
                value = upperBounds;
            }
            break;
        }

        this.fireValueChanged();
        this.setValue(value);
    }

    /**
     * The down html that when clicked increases the spinners value.
     */
    private Panel panel;

    public Panel getPanel() {
        ObjectHelper.checkNotNull("field:panel", panel);
        return this.panel;
    }

    public boolean hasPanel() {
        return this.panel != null;
    }

    public void setPanel(final Panel panel) {
        ObjectHelper.checkNotNull("parameter:panel", panel);
        this.panel = panel;
    }

    /**
     * Creates a new panel and positions the valueLabel, and the two htmls.
     *
     * @return
     */
    public Panel createPanel() {
    	WidgetHelper.checkNotAlreadyCreated( "panel", this.hasPanel() );

        final VerticalPanel panel = new VerticalPanel();
        panel.addStyleName( WidgetConstants.SPINNER_STYLE );
        panel.addStyleName( WidgetConstants.SPINNER_VERTICAL_PANEL_STYLE );
        this.setPanel(panel);

        panel.add(this.createUpWidget());
        panel.add(this.createDownWidget());
        this.updateValue(this.getValue());

        return panel;
    }

    /**
     * The amount the value is increased/decreased each time an up or down html is clicked.
     */
    private int delta;

    private boolean deltaSet;

    public int getDelta() {
        if (false == deltaSet) {
            SystemHelper.handleAssertFailure("field:delta", "The field:delta has not been set, this: " + this);
        }
        return this.delta;
    }

    public void setDelta(final int delta) {
        this.delta = delta;
        this.deltaSet = true;
    }

    public String toString() {
        return super.toString() + ", value: " + value + ", lowerBounds: " + lowerBounds + ", upperBounds: "
                + upperBounds + ", upDown: " + upWidget + ", downWidget:" + downWidget + ", panel: " + this.panel
                + ", delta: " + delta + ", deltaSet: " + deltaSet + ", upImageUrl[" + upImageUrl + "], downImageUrl[" + downImageUrl + "]";
    }
}