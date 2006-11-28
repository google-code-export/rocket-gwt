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
package rocket.widget.client;

import rocket.style.client.StyleHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * An extension of a regular spinner that includes two extra buttons to assist with modifying the value.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 */
public class SuperSpinner extends Spinner {

    public SuperSpinner() {
        super();

        this.setDownImageUrl(WidgetConstants.SUPER_SPINNER_DOWN_IMAGE_URL);
        this.setUpImageUrl(WidgetConstants.SUPER_SPINNER_UP_IMAGE_URL);
        this.setBigDownImageUrl(WidgetConstants.SUPER_SPINNER_BIG_DOWN_IMAGE_URL);
        this.setBigUpImageUrl(WidgetConstants.SUPER_SPINNER_BIG_UP_IMAGE_URL);
    }

    /**
     * The left button that when clicked decreases the spinners value.
     */
    private Image bigDownWidget;

    public Image getBigDownWidget() {
        ObjectHelper.checkNotNull("field:bigDownWidget", bigDownWidget);
        return this.bigDownWidget;
    }

    public boolean hasBigDownWidget() {
        return this.bigDownWidget != null;
    }

    public void setBigDownWidget(final Image bigDownWidget) {
        ObjectHelper.checkNotNull("parameter:bigDown", bigDownWidget);
        this.bigDownWidget = bigDownWidget;
    }

    public Widget createBigDownWidget() {
        ObjectHelper.checkPropertyNotSet("bigDownWidget", this, this.hasBigDownWidget());

        final SuperSpinner that = this;

        final Image image = new Image();
        image.setUrl(this.getBigDownImageUrl());
        image.addStyleName(WidgetConstants.SUPER_SPINNER_BIG_DOWN_STYLE);

        image.addClickListener(new ClickListener() {

            public void onClick(final Widget widget) {
                that.onBigDownClick();
            }
        });
        this.setBigDownWidget(image);
        return image;
    }

    protected void onDownClick() {
        this.updateValue(this.getValue() - this.getDelta());
    }

    /**
     * . The url of the bigDown icon
     */
    private String bigDownImageUrl;

    public String getBigDownImageUrl() {
        StringHelper.checkNotEmpty("field:bigDownImageUrl", bigDownImageUrl);
        return bigDownImageUrl;
    }

    public void setBigDownImageUrl(final String bigDownImageUrl) {
        StringHelper.checkNotEmpty("parameter:bigDownImageUrl", bigDownImageUrl);
        this.bigDownImageUrl = bigDownImageUrl;

        if (this.hasBigDownWidget()) {
            final Image image = this.getBigDownWidget();
            image.setUrl(bigDownImageUrl);
        }
    }

    /**
     * The right button that when clicked decreases the spinners value.
     */
    private Image bigUpWidget;

    public Image getBigUpWidget() {
        ObjectHelper.checkNotNull("field:bigUpWidget", bigUpWidget);
        return this.bigUpWidget;
    }

    public boolean hasBigUpWidget() {
        return this.bigUpWidget != null;
    }

    public void setBigUpWidget(final Image bigUpWidget) {
        ObjectHelper.checkNotNull("parameter:bigUpWidget", bigUpWidget);
        this.bigUpWidget = bigUpWidget;
    }

    public Widget createBigUpWidget() {
        ObjectHelper.checkPropertyNotSet("bigUpWidget", this, this.hasBigUpWidget());

        final SuperSpinner that = this;

        final Image image = new Image();
        image.setUrl(this.getBigUpImageUrl());
        image.addStyleName(WidgetConstants.SUPER_SPINNER_BIG_UP_STYLE);

        image.addClickListener(new ClickListener() {

            public void onClick(final Widget widget) {
                that.onBigUpClick();
            }
        });
        this.setBigUpWidget(image);
        return image;
    }

    public void onUpClick() {
        this.updateValue(this.getValue() + this.getDelta());
    }

    public void onBigUpClick() {
        this.updateValue(this.getValue() + this.getBigDelta());
    }

    public void onBigDownClick() {
        this.updateValue(this.getValue() - this.getBigDelta());
    }

    /**
     * . The url of the bigUp icon
     */
    private String bigUpImageUrl;

    public String getBigUpImageUrl() {
        StringHelper.checkNotEmpty("field:bigUpImageUrl", bigUpImageUrl);
        return bigUpImageUrl;
    }

    public void setBigUpImageUrl(final String bigUpImageUrl) {
        StringHelper.checkNotEmpty("parameter:bigUpImageUrl", bigUpImageUrl);
        this.bigUpImageUrl = bigUpImageUrl;

        if (this.hasBigUpWidget()) {
            final Image image = this.getBigUpWidget();
            image.setUrl(bigUpImageUrl);
        }
    }

    /**
     * Creates a new HorizontalPanel and fills it with various clickable widgets which may be used adjust the value.
     * 
     * @return
     */
    public Panel createPanel() {
        if (this.hasPanel()) {
            SystemHelper.handleAssertFailure("An flexTable has already been created, this: " + this);
        }

        final HorizontalPanel panel = new HorizontalPanel();
        panel.addStyleName(StyleHelper.buildCompound(WidgetConstants.SUPER_SPINNER_STYLE,
                WidgetConstants.SUPER_SPINNER_HORIZONTAL_PANEL));
        this.setPanel(panel);

        panel.add(this.createDownWidget());
        panel.add(this.createUpWidget());

        panel.add(this.createBigUpWidget());
        panel.add(this.createBigDownWidget());

        this.updateValue(this.getValue());

        return panel;
    }

    /**
     * The amount the value is increased/decreased each time an up or down button is clicked.
     */
    private int bigDelta;

    private boolean bigDeltaSet;

    public int getBigDelta() {
        if (false == bigDeltaSet) {
            SystemHelper.handleAssertFailure("field:bigDelta", "The field:bigDelta has not been set, this: " + this);
        }
        return this.bigDelta;
    }

    public void setBigDelta(final int bigDelta) {
        this.bigDelta = bigDelta;
        this.bigDeltaSet = true;
    }

    public String toString() {
        return super.toString() + ", bigUpWidget: " + bigUpWidget + ", bigDownWidget: " + bigDownWidget
                + ", bigDelta: " + bigDelta + ", bigDeltaSet: " + bigDeltaSet + ", bigUpImageUrl[" + bigUpImageUrl
                + "], bigDownImageUrl[" + bigDownImageUrl + "]";
    }

}