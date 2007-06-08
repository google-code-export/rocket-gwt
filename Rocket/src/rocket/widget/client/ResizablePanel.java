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
import java.util.Map;

import rocket.browser.client.BrowserHelper;
import rocket.dom.client.DomHelper;
import rocket.dragndrop.client.DragNDropHelper;
import rocket.selection.client.SelectionHelper;
import rocket.style.client.CssUnit;
import rocket.style.client.StyleConstants;
import rocket.style.client.StyleHelper;
import rocket.style.client.StylePropertyValue;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A ResizablePanel is a special panel that contains a single child widget which can be resized by the user dragging any of the handles.
 * 
 * The width/height may be constrained by the following properties
 * <ul>
 * <li>minimum width/height</li>
 * <li>maximum width/height</li>
 * </ul>
 * 
 * <h6>Gotchas</h6>
 * <ul>
 * <li>For widgets that are not floating it only makes sense to add east, southEast and south handles (right& down) as it is not possible
 * to follow the mouse when stretching in the other directions (up & left) </li>
 * 
 * <li> To avoid problems with IE the child widget should have its overflow css style property set to "scroll" or "invisible"
 * ChangeListeners may be registered with this panel and are fired during each resize attempt. </li>
 * </ul>
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ResizablePanel extends Composite {

    public ResizablePanel() {
        super();

        this.createChangeListeners();
        this.initWidget(createAbsolutePanel());
        this.setStyleName(WidgetConstants.RESIZABLE_PANEL_STYLE);
    }

    /**
     * An absolute panel is used to hold the child widget as well as the handles.
     */
    private AbsolutePanel absolutePanel;

    protected AbsolutePanel getAbsolutePanel() {
        ObjectHelper.checkNotNull("field:absolutePanel", absolutePanel);
        return this.absolutePanel;
    }

    protected void setAbsolutePanel(final AbsolutePanel absolutePanel) {
        ObjectHelper.checkNotNull("panel:absolutePanel", absolutePanel);
        this.absolutePanel = absolutePanel;
    }

    protected AbsolutePanel createAbsolutePanel() {
        final AbsolutePanel panel = new AbsolutePanel();
        this.setAbsolutePanel(panel);
        return panel;
    }

    /**
     * The child widget which is the candidate for resizing.
     */
    private Widget widget;

    public Widget getWidget() {
        ObjectHelper.checkNotNull("field:widget", widget);
        return this.widget;
    }

    public boolean hasWidget() {
        return null != this.widget;
    }

    public void setWidget(final Widget widget) {
        ObjectHelper.checkNotNull("panel:widget", widget);

        final AbsolutePanel panel = this.getAbsolutePanel();

        // remove the previous widget
        if (null != this.widget) {
            panel.remove(this.widget);
        }
        // add the new widget.
        panel.add(widget);

        final Element element = widget.getElement();
        DOM.setStyleAttribute(element, StyleConstants.WIDTH, "100%");
        DOM.setStyleAttribute(element, StyleConstants.HEIGHT, "100%");
        this.widget = widget;

        if (this.isAttached()) {
            this.layout();
        }
    }

    /**
     * When this panel is attached recalculate and layout any attached handles.
     */
    public void onAttach() {
        super.onAttach();

        this.unsinkEvents(-1);
        this.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
        DOM.setEventListener(this.getElement(), this);

        this.layout();
    }

    /**
     * Sets the width of this panel as well as recalculating the layout of any attached handles.
     */
    public void setWidth(final String width) {
        super.setWidth(width);

        final int width0 = DomHelper.getClientWidth(this.getElement());
        int width1 = width0;
        while (true) {
            final int minimumWidth = this.getMinimumWidth();
            if (width0 < minimumWidth) {
                width1 = minimumWidth;
                break;
            }
            final int maximumWidth = this.getMaximumWidth();
            if (width0 > maximumWidth) {
                width1 = maximumWidth;
                break;
            }
            break;
        }
        if (width0 != width1) {
            super.setWidth(width1 + "px");
        }
        if (this.isAttached()) {
            this.layout();
        }
    }

    /**
     * Sets the height of this panel as well as recalculating the layout of any attached handles.
     */
    public void setHeight(final String height) {
        super.setHeight(height);

        final int height0 = DomHelper.getClientHeight(this.getElement());

        int height1 = height0;
        while (true) {
            final int minimumHeight = this.getMinimumHeight();
            if (height0 < minimumHeight) {
                height1 = minimumHeight;
                break;
            }
            final int maximumHeight = this.getMaximumHeight();
            if (height0 > maximumHeight) {
                height1 = maximumHeight;
                break;
            }
            break;
        }
        if (height0 != height1) {
            super.setHeight(height1 + "px");
        }

        if (this.isAttached()) {
            this.layout();
        }
    }

    /**
     * Adjusts the size of the panel using the given delta values. Positive values make that length larger which negative values make it
     * smaller. The min/max constraints for both width and height are respected.
     * 
     * @param deltaX
     *            The amount in pixels to increase/decrease the panel's width
     * @param deltaY
     *            The amount in pixels to increase/decrease the panel's height
     */
    protected void resizeBy(final int deltaX, final int deltaY) {
        final Widget widget = this.getWidget();
        boolean changed = false;
        if (0 != deltaX) {
            final int width = widget.getOffsetWidth() + deltaX;
            this.setWidth(width + "px");
            changed = true;
        }
        if (0 != deltaY) {
            final int height = widget.getOffsetHeight() + deltaY;
            this.setHeight(height + "px");
            changed = true;
        }
        if (changed) {
            this.fireChange();
        }
    }

    /**
     * Schedules a layout of the everything in the near future.
     */
    protected void layout() {
        if (this.hasWidget()) {
            this.layoutWidget();

            if (this.hasNorthEastHandle()) {
                this.layoutNorthEastHandle();
            }
            if (this.hasNorthWestHandle()) {
                this.layoutNorthWestHandle();
            }
            if (this.hasNorthHandle()) {
                this.layoutNorthHandle();
            }
            if (this.hasSouthEastHandle()) {
                this.layoutSouthEastHandle();
            }
            if (this.hasSouthWestHandle()) {
                this.layoutSouthWestHandle();
            }
            if (this.hasSouthHandle()) {
                this.layoutSouthHandle();
            }
            if (this.hasEastHandle()) {
                this.layoutEastHandle();
            }
            if (this.hasWestHandle()) {
                this.layoutWestHandle();
            }
        }
    }

    /**
     * Recalculates the coordinates of the enclosed widget. The margins, borders and padding are respected.
     */
    protected void layoutWidget() {
        int left = 0;
        int top = 0;
        int width = this.getOffsetWidth();
        int height = this.getOffsetHeight();

        Map panelStyle = null;

        final Widget widget = this.getWidget();
        final Element widgetElement = widget.getElement();
        Map widgetStyle = null;
        try {
            panelStyle = StyleHelper.getComputedStyle(this.getElement());
            widgetStyle = StyleHelper.getComputedStyle(widgetElement);

            final int borderLeftWidth = this.getComputedStyle(panelStyle, StyleConstants.BORDER_LEFT_WIDTH);
            final int paddingLeft = this.getComputedStyle(panelStyle, StyleConstants.PADDING_LEFT);
            final int marginLeft = this.getComputedStyle(widgetStyle, StyleConstants.MARGIN_LEFT);
            left = borderLeftWidth + paddingLeft + marginLeft;

            width = width - borderLeftWidth;
            width = width - paddingLeft;
            width = width - marginLeft;
            width = width - this.getComputedStyle(panelStyle, StyleConstants.BORDER_RIGHT_WIDTH);
            width = width - this.getComputedStyle(panelStyle, StyleConstants.PADDING_RIGHT);
            width = width - this.getComputedStyle(widgetStyle, StyleConstants.MARGIN_RIGHT);

            final int borderTopWidth = this.getComputedStyle(panelStyle, StyleConstants.BORDER_TOP_WIDTH);
            final int paddingTop = this.getComputedStyle(panelStyle, StyleConstants.PADDING_TOP);
            final int marginTop = this.getComputedStyle(widgetStyle, StyleConstants.MARGIN_TOP);

            top = borderTopWidth + paddingTop + marginTop;

            height = height - borderTopWidth;
            height = height - paddingTop;
            height = height - this.getComputedStyle(panelStyle, StyleConstants.BORDER_BOTTOM_WIDTH);
            height = height - this.getComputedStyle(panelStyle, StyleConstants.PADDING_BOTTOM);
            height = height - this.getComputedStyle(widgetStyle, StyleConstants.MARGIN_BOTTOM);

        } finally {
            ObjectHelper.destroyIfNecessary(panelStyle);
            ObjectHelper.destroyIfNecessary(widgetStyle);
        }

        DOM.setStyleAttribute(widgetElement, StyleConstants.POSITION, "absolute");
        DOM.setStyleAttribute(widgetElement, StyleConstants.LEFT, left + "px");
        DOM.setStyleAttribute(widgetElement, StyleConstants.TOP, top + "px");

        widget.setWidth(width + "px");
        widget.setHeight(height + "px");

        if ((width + "px").equals("NaNpx")) {
            Window.alert("layoutWidget" + width);
        }
    }

    /**
     * Helper method which returns 0 if the property identified by the given name was not found
     * 
     * @param style
     * @param propertyName
     * @return
     */
    protected int getComputedStyle(final Map style, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = (StylePropertyValue) style.get(propertyName);
        return value == null ? 0 : value.getInteger(CssUnit.PX);
    }

    /**
     * This resize handle appears along the right edge of the child widget
     */
    private Widget eastHandle;

    public Widget getEastHandle() {
        ObjectHelper.checkNotNull("field:eastHandle", eastHandle);
        return this.eastHandle;
    }

    public boolean hasEastHandle() {
        return null != this.eastHandle;
    }

    public void setEastHandle(final Widget eastHandle) {
        ObjectHelper.checkNotNull("parameter:eastHandle", eastHandle);

        this.clearEastHandle();
        eastHandle.setStyleName(WidgetConstants.RESIZABLE_PANEL_EAST_HANDLE_STYLE);
        this.eastHandle = eastHandle;
        this.getAbsolutePanel().add(new HandleWrapper(eastHandle) {

            protected void handleMouseMove0(final int deltaX, final int deltaY) {
                ResizablePanel.this.handleEastHandleMouseMove(deltaX, deltaY);
            }
        });
        this.layout();
    }

    protected void handleEastHandleMouseMove(final int deltaX, final int deltaY) {
        this.resizeBy(deltaX, 0);
    }

    public void clearEastHandle() {
        this.removeHandleFromPanel(this.eastHandle);
        this.eastHandle = null;
    }

    protected void layoutEastHandle() {
        final Widget handle = this.getEastHandle();
        this.setHandleStyle(handle);
        this.setHandleRight(handle);
        this.setHandleMiddle(handle);
    }

    /**
     * This resize handle appears along the left edge of the child widget
     */
    private Widget westHandle;

    public Widget getWestHandle() {
        ObjectHelper.checkNotNull("field:westHandle", westHandle);
        return this.westHandle;
    }

    public boolean hasWestHandle() {
        return null != this.westHandle;
    }

    public void setWestHandle(final Widget westHandle) {
        ObjectHelper.checkNotNull("parameter:westHandle", westHandle);

        this.clearWestHandle();
        westHandle.setStyleName(WidgetConstants.RESIZABLE_PANEL_WEST_HANDLE_STYLE);
        this.westHandle = westHandle;

        this.getAbsolutePanel().add(new HandleWrapper(westHandle) {
            protected void handleMouseMove0(final int deltaX, final int deltaY) {
                ResizablePanel.this.handleWestHandleMouseMove(deltaX, deltaY);
            }
        });
        this.layout();
    }

    protected void handleWestHandleMouseMove(final int deltaX, final int deltaY) {
        this.resizeBy(deltaX, 0);
    }

    public void clearWestHandle() {
        this.removeHandleFromPanel(this.westHandle);
        this.westHandle = null;
    }

    protected void layoutWestHandle() {
        final Widget handle = this.getWestHandle();
        this.setHandleStyle(handle);
        this.setHandleLeft(handle);
        this.setHandleMiddle(handle);
    }

    /**
     * This resize handle appears in the top right corner of the child widget
     */
    private Widget northEastHandle;

    public Widget getNorthEastHandle() {
        ObjectHelper.checkNotNull("field:northEastHandle", northEastHandle);
        return this.northEastHandle;
    }

    public boolean hasNorthEastHandle() {
        return null != this.northEastHandle;
    }

    public void setNorthEastHandle(final Widget northEastHandle) {
        ObjectHelper.checkNotNull("parameter:northEastHandle", northEastHandle);

        this.clearNorthEastHandle();

        this.northEastHandle = northEastHandle;
        northEastHandle.setStyleName(WidgetConstants.RESIZABLE_PANEL_NORTHEAST_HANDLE_STYLE);

        this.getAbsolutePanel().add(new HandleWrapper(northEastHandle) {

            protected void handleMouseMove0(final int deltaX, final int deltaY) {
                ResizablePanel.this.handleNorthEastHandleMouseMove(deltaX, deltaY);
            }
        });

        this.layout();
    }

    protected void handleNorthEastHandleMouseMove(final int deltaX, final int deltaY) {
        this.resizeBy(deltaX, -deltaY);
    }

    public void clearNorthEastHandle() {
        this.removeHandleFromPanel(this.northEastHandle);
        this.northEastHandle = null;
    }

    protected void layoutNorthEastHandle() {
        final Widget handle = this.getNorthEastHandle();
        this.setHandleStyle(handle);
        this.setHandleRight(handle);
        this.setHandleTop(handle);
    }

    /**
     * This resize handle appears in the top right corner
     */
    private Widget northHandle;

    public Widget getNorthHandle() {
        ObjectHelper.checkNotNull("field:northHandle", northHandle);
        return this.northHandle;
    }

    public boolean hasNorthHandle() {
        return null != this.northHandle;
    }

    public void setNorthHandle(final Widget northHandle) {
        ObjectHelper.checkNotNull("parameter:northHandle", northHandle);

        this.clearNorthHandle();
        northHandle.setStyleName(WidgetConstants.RESIZABLE_PANEL_NORTH_HANDLE_STYLE);
        this.northHandle = northHandle;

        this.getAbsolutePanel().add(new HandleWrapper(northHandle) {
            protected void handleMouseMove0(final int deltaX, final int deltaY) {
                ResizablePanel.this.handleNorthHandleMouseMove(deltaY, deltaY);
            }
        });
        this.layout();
    }

    protected void handleNorthHandleMouseMove(final int deltaX, final int deltaY) {
        this.resizeBy(0, -deltaY);
    }

    public void clearNorthHandle() {
        this.removeHandleFromPanel(this.northHandle);
        this.northHandle = null;
    }

    protected void layoutNorthHandle() {
        final Widget handle = this.getNorthHandle();
        this.setHandleStyle(handle);
        this.setHandleCenter(handle);
        this.setHandleTop(handle);
    }

    /**
     * This resize handle appears in the top west corner
     */
    private Widget northWestHandle;

    public Widget getNorthWestHandle() {
        ObjectHelper.checkNotNull("field:northWestHandle", northWestHandle);
        return this.northWestHandle;
    }

    public boolean hasNorthWestHandle() {
        return null != this.northWestHandle;
    }

    public void setNorthWestHandle(final Widget northWestHandle) {
        ObjectHelper.checkNotNull("parameter:northWestHandle", northWestHandle);

        this.clearNorthWestHandle();

        northWestHandle.setStyleName(WidgetConstants.RESIZABLE_PANEL_NORTHWEST_HANDLE_STYLE);
        this.northWestHandle = northWestHandle;

        this.getAbsolutePanel().add(new HandleWrapper(northWestHandle) {
            protected void handleMouseMove0(final int deltaX, final int deltaY) {
                ResizablePanel.this.handleNorthWestHandleMouseMove(deltaX, deltaY);
            }
        });
        this.layout();
    }

    protected void handleNorthWestHandleMouseMove(final int deltaX, final int deltaY) {
        this.resizeBy(deltaX, -deltaY);
    }

    public void clearNorthWestHandle() {
        this.removeHandleFromPanel(this.northWestHandle);
        this.northWestHandle = null;
    }

    protected void layoutNorthWestHandle() {
        final Widget handle = this.getNorthWestHandle();
        this.setHandleStyle(handle);
        this.setHandleLeft(handle);
        this.setHandleTop(handle);
    }

    /**
     * This resize handle appears in the top right corner
     */
    private Widget southEastHandle;

    public Widget getSouthEastHandle() {
        ObjectHelper.checkNotNull("field:southEastHandle", southEastHandle);
        return this.southEastHandle;
    }

    public boolean hasSouthEastHandle() {
        return null != this.southEastHandle;
    }

    public void setSouthEastHandle(final Widget southEastHandle) {
        ObjectHelper.checkNotNull("parameter:southEastHandle", southEastHandle);

        this.clearSouthEastHandle();

        this.southEastHandle = southEastHandle;
        southEastHandle.setStyleName(WidgetConstants.RESIZABLE_PANEL_SOUTHEAST_HANDLE_STYLE);

        this.getAbsolutePanel().add(new HandleWrapper(southEastHandle) {

            protected void handleMouseMove0(final int deltaX, final int deltaY) {
                ResizablePanel.this.handleSouthEastHandleMouseMove(deltaX, deltaY);
            }
        });
        this.layout();
    }

    protected void handleSouthEastHandleMouseMove(final int deltaX, final int deltaY) {
        this.resizeBy(deltaX, deltaY);
    }

    public void clearSouthEastHandle() {
        this.removeHandleFromPanel(this.southEastHandle);
        this.southEastHandle = null;
    }

    protected void layoutSouthEastHandle() {
        final Widget handle = this.getSouthEastHandle();
        this.setHandleStyle(handle);
        this.setHandleRight(handle);
        this.setHandleBottom(handle);
    }

    /**
     * This resize handle appears in the top right corner
     */
    private Widget southHandle;

    public Widget getSouthHandle() {
        ObjectHelper.checkNotNull("field:southHandle", southHandle);
        return this.southHandle;
    }

    public boolean hasSouthHandle() {
        return null != this.southHandle;
    }

    public void setSouthHandle(final Widget southHandle) {
        ObjectHelper.checkNotNull("parameter:southHandle", southHandle);

        this.clearSouthHandle();
        southHandle.setStyleName(WidgetConstants.RESIZABLE_PANEL_SOUTH_HANDLE_STYLE);
        this.southHandle = southHandle;

        this.getAbsolutePanel().add(new HandleWrapper(southHandle) {

            protected void handleMouseMove0(final int deltaX, final int deltaY) {
                ResizablePanel.this.handleSouthHandleMouseMove(deltaX, deltaY);
            }
        });
        this.layout();

    }

    protected void handleSouthHandleMouseMove(final int deltaX, final int deltaY) {
        this.resizeBy(0, deltaY);
    }

    public void clearSouthHandle() {
        this.removeHandleFromPanel(this.southHandle);
        this.southHandle = null;
    }

    protected void layoutSouthHandle() {
        final Widget handle = this.getSouthHandle();
        this.setHandleStyle(handle);
        this.setHandleCenter(handle);
        this.setHandleBottom(handle);
    }

    /**
     * This resize handle appears in the bottom left corner
     */
    private Widget southWestHandle;

    public Widget getSouthWestHandle() {
        ObjectHelper.checkNotNull("field:southWestHandle", southWestHandle);
        return this.southWestHandle.getParent();
    }

    public boolean hasSouthWestHandle() {
        return null != this.southWestHandle;
    }

    public void setSouthWestHandle(final Widget southWestHandle) {
        ObjectHelper.checkNotNull("parameter:southWestHandle", southWestHandle);

        this.clearSouthWestHandle();

        southWestHandle.setStyleName(WidgetConstants.RESIZABLE_PANEL_SOUTHWEST_HANDLE_STYLE);
        this.southWestHandle = southWestHandle;

        this.getAbsolutePanel().add(new HandleWrapper(southWestHandle) {

            protected void handleMouseMove0(final int deltaX, final int deltaY) {
                ResizablePanel.this.handleSouthWestHandleMouseMove(deltaX, deltaY);
            }
        });

        this.layout();
    }

    protected void handleSouthWestHandleMouseMove(final int deltaX, final int deltaY) {
        this.resizeBy(deltaX, deltaY);
    }

    public void clearSouthWestHandle() {
        this.removeHandleFromPanel(this.southWestHandle);
        this.southWestHandle = null;
    }

    protected void layoutSouthWestHandle() {
        final Widget handle = this.getSouthWestHandle();
        this.setHandleStyle(handle);
        this.setHandleLeft(handle);
        this.setHandleBottom(handle);
    }

    protected void setHandleStyle(final Widget handle) {
        ObjectHelper.checkNotNull("parameter:handle", handle);
        final Element element = handle.getElement();
        DOM.setStyleAttribute(element, StyleConstants.POSITION, "absolute");
        DOM.setStyleAttribute(element, StyleConstants.Z_INDEX, "100"); // TODO should get zIndex of widget and +1.
        DOM.setStyleAttribute(element, StyleConstants.OVERFLOW, "visible");
    }

    /**
     * Sets the left offset in pixels of the handle so that it is hovering over the left edge of the child widget.
     * 
     * @param handle
     * @return
     */
    protected void setHandleLeft(final Widget handle) {
        ObjectHelper.checkNotNull("parameter:handle", handle);

        int left = DomHelper.getOffsetLeft(this.getWidget().getElement());
        left = left - handle.getOffsetWidth() / 2;
        DOM.setStyleAttribute(handle.getElement(), StyleConstants.LEFT, left + "px");
    }

    protected void setHandleRight(final Widget handle) {
        ObjectHelper.checkNotNull("parameter:handle", handle);

        final Widget widget = this.getWidget();
        int right = this.getOffsetWidth();
        right = right - DomHelper.getOffsetLeft(widget.getElement());
        right = right - widget.getOffsetWidth();
        right = right - handle.getOffsetWidth() / 2;
        DOM.setStyleAttribute(handle.getElement(), StyleConstants.RIGHT, right + "px");
    }

    protected void setHandleCenter(final Widget handle) {
        ObjectHelper.checkNotNull("parameter:handle", handle);

        final Widget widget = this.getWidget();
        int left = DomHelper.getOffsetLeft(widget.getElement());
        left = left + widget.getOffsetWidth() / 2;
        left = left - handle.getOffsetWidth() / 2;
        DOM.setStyleAttribute(handle.getElement(), StyleConstants.LEFT, left + "px");
    }

    protected void setHandleTop(final Widget handle) {
        ObjectHelper.checkNotNull("parameter:handle", handle);

        int top = DomHelper.getOffsetTop(this.getWidget().getElement());
        top = top - handle.getOffsetHeight() / 2;
        DOM.setStyleAttribute(handle.getElement(), StyleConstants.TOP, top + "px");
    }

    protected void setHandleBottom(final Widget handle) {
        ObjectHelper.checkNotNull("parameter:handle", handle);

        final Widget widget = this.getWidget();
        int top = this.getOffsetHeight();
        top = top - DomHelper.getOffsetTop(widget.getElement());
        top = top - widget.getOffsetHeight();
        top = top - handle.getOffsetHeight() / 2;
        DOM.setStyleAttribute(handle.getElement(), StyleConstants.BOTTOM, top + "px");
    }

    protected void setHandleMiddle(final Widget handle) {
        ObjectHelper.checkNotNull("parameter:handle", handle);

        final Widget widget = this.getWidget();
        int top = DomHelper.getOffsetTop(widget.getElement());
        top = top + widget.getOffsetHeight() / 2;
        top = top - handle.getOffsetHeight() / 2;
        DOM.setStyleAttribute(handle.getElement(), StyleConstants.TOP, top + "px");
    }

    /**
     * Helper which removes the parent of the handle widget when it is attached so that both are disconnected from this panel
     * 
     * @param widget
     */
    protected void removeHandleFromPanel(final Widget widget) {
        if (null != widget) {
            final Widget parent = widget.getParent();
            if (null != parent) {
                parent.removeFromParent();
            }
        }
    }

    /**
     * The minimum width in pixels that the child widget may be set.
     */
    private int minimumWidth;

    public int getMinimumWidth() {
        PrimitiveHelper.checkGreaterThan("field:minimumWidth", minimumWidth, 0);
        return this.minimumWidth;
    }

    public void setMinimumWidth(final int minimumWidth) {
        PrimitiveHelper.checkGreaterThan("parameter:minimumWidth", minimumWidth, 0);
        this.minimumWidth = minimumWidth;
    }

    /**
     * The maximum width in pixels that the child widget may be set.
     */
    private int maximumWidth;

    public int getMaximumWidth() {
        PrimitiveHelper.checkGreaterThan("field:maximumWidth", maximumWidth, 0);
        return this.maximumWidth;
    }

    public void setMaximumWidth(final int maximumWidth) {
        PrimitiveHelper.checkGreaterThan("parameter:maximumWidth", maximumWidth, 0);
        this.maximumWidth = maximumWidth;
    }

    /**
     * The minimum height in pixels that the child widget may be set.
     */
    private int minimumHeight;

    public int getMinimumHeight() {
        PrimitiveHelper.checkGreaterThan("field:minimumHeight", minimumHeight, 0);
        return this.minimumHeight;
    }

    public void setMinimumHeight(final int minimumHeight) {
        PrimitiveHelper.checkGreaterThan("parameter:minimumHeight", minimumHeight, 0);
        this.minimumHeight = minimumHeight;
    }

    /**
     * The maximum height in pixels that the child widget may be set.
     */
    private int maximumHeight;

    public int getMaximumHeight() {
        PrimitiveHelper.checkGreaterThan("field:maximumHeight", maximumHeight, 0);
        return this.maximumHeight;
    }

    public void setMaximumHeight(final int maximumHeight) {
        PrimitiveHelper.checkGreaterThan("parameter:maximumHeight", maximumHeight, 0);
        this.maximumHeight = maximumHeight;
    }

    /**
     * When this flag is true any increases/decreases in width or height also change the other coordinate maintaining the aspect ratio for
     * the child widget. This is especially useful for images where the aspect ratio is especially important.
     */
    private boolean keepAspectRatio;

    public boolean isKeepAspectRatio() {
        return this.keepAspectRatio;
    }

    public void setKeepAspectRatio(final boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
    }

    public void onBrowserEvent(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            final int type = DOM.eventGetType(event);
            if (Event.ONMOUSEOVER == type) {
                this.handleMouseOver(event);
                break;
            }
            if (Event.ONMOUSEOUT == type) {
                this.handleMouseOut(event);
            }
            break;
        }
    }

    /**
     * When the mouse moves over this panel make sure all handles are visible. After making handle widgets visible it is necessary to
     * recalculate the layout.
     * 
     * @param event
     */
    protected void handleMouseOver(final Event event) {
        if (false == this.isAlwaysShowHandles()) {
            this.setHandleWidgetVisibility(true);
            this.layout();
        }
    }

    /**
     * When the mouse leaves the panel make sure all handles are invisible.
     * 
     * @param event
     */
    protected void handleMouseOut(final Event event) {
        if (false == this.isAlwaysShowHandles()) {
            final Element panel = this.getElement();
            final Element target = DOM.eventGetToElement(event);
            if (false == DOM.isOrHasChild(panel, target)) {
                this.setHandleWidgetVisibility(false);
            }
        }
    }

    /**
     * This helper loops thru all widgets (skipping the child widget) updating its visibility.
     * 
     * @param newVisibleValue
     *            The new state of all handle visible flags.
     */
    protected void setHandleWidgetVisibility(final boolean newVisibleValue) {
        final Widget childWidget = this.hasWidget() ? this.getWidget() : null;
        final Iterator widgets = this.getAbsolutePanel().iterator();
        while (widgets.hasNext()) {
            final Widget widget = (Widget) widgets.next();
            if (widget == childWidget) {
                continue;
            }
            widget.setVisible(newVisibleValue);
        }
    }

    /**
     * When this flag is true handles are always visible above the child widget. When false handles are only made visible when the mouse
     * hovers near the panel and child widget edge.
     */
    private boolean alwaysShowHandles;

    public boolean isAlwaysShowHandles() {
        return this.alwaysShowHandles;
    }

    public void setAlwaysShowHandles(final boolean alwaysShowHandles) {
        this.alwaysShowHandles = alwaysShowHandles;
        this.setHandleWidgetVisibility(alwaysShowHandles);
    }

    /**
     * A collection of listeners that are interested in all events (eg when this panel is resized).
     */
    private ChangeListenerCollection changeListeners;

    protected ChangeListenerCollection getChangeListeners() {
        ObjectHelper.checkNotNull("field:changeListeners", changeListeners);
        return this.changeListeners;
    }

    protected void setChangeListeners(final ChangeListenerCollection changeListeners) {
        ObjectHelper.checkNotNull("parameter:changeListeners", changeListeners);
        this.changeListeners = changeListeners;
    }

    protected void createChangeListeners() {
        this.setChangeListeners(new ChangeListenerCollection());
    }

    protected void fireChange() {
        this.getChangeListeners().fireChange(this);
    }

    public void addChangeListener(final ChangeListener changeListener) {
        ObjectHelper.checkNotNull("parameter:changeListener", changeListener);
        this.getChangeListeners().add(changeListener);
    }

    public void removeChangeListener(final ChangeListener changeListener) {
        ObjectHelper.checkNotNull("parameter:changeListener", changeListener);
        this.getChangeListeners().remove(changeListener);
    }

    public String toString() {
        return super.toString() + ", minimumWidth: " + this.minimumWidth + ", maximumWidth: " + maximumWidth
                + ", minimumHeight: " + this.minimumHeight + ", maximumHeight: " + maximumHeight
                + ", keepAspectRatio: " + this.keepAspectRatio + ", alwaysShowHandles: " + alwaysShowHandles
                + ", absolutePanel: " + this.absolutePanel;
    }

    /**
     * This class provides an easy start to handle any handle dragging event. It takes care of all the boring repetitive bits like
     * dispatching based on event type, calculating the amount of pixels the mouse has moved etc.
     */
    abstract class HandleWrapper extends Composite implements EventListener, EventPreview {

        protected HandleWrapper(final Widget widget) {
            super();

            this.initWidget(widget);
        }

        protected void initWidget(final Widget widget) {
            super.initWidget(widget);
            this.setWidget(widget);
        }

        /**
         * A reference to the widget being wrapped.
         */
        private Widget widget;

        protected Widget getWidget() {
            ObjectHelper.checkNotNull("field:widget", widget);
            return this.widget;
        }

        protected void setWidget(final Widget widget) {
            ObjectHelper.checkNotNull("parameter:widget", widget);
            this.widget = widget;
        }

        /**
         * After calling onAttach() on the composite sink the events and listener to this class.
         */
        public void onAttach() {
            super.onAttach();

            DOM.setEventListener(this.getElement(), this);
            this.unsinkEvents(-1);
            this.sinkEvents(Event.ONMOUSEDOWN);
        }

        public void onBrowserEvent(final Event event) {
            ObjectHelper.checkNotNull("parameter:event", event);

            final int type = DOM.eventGetType(event);
            if (Event.ONMOUSEDOWN == type) {
                this.handleMouseDown(event);
            }
        }

        /**
         * Initializes a handle drag when a mouse down event over one occurs.
         * 
         * @param event
         */
        protected void handleMouseDown(final Event event) {
            ObjectHelper.checkNotNull("parameter:event", event);

            this.setX(DOM.eventGetClientX(event) + BrowserHelper.getScrollX());
            this.setY(DOM.eventGetClientY(event) + BrowserHelper.getScrollY());
            this.setWidth(ResizablePanel.this.getOffsetWidth());
            this.setHeight(ResizablePanel.this.getOffsetHeight());

            SelectionHelper.disableTextSelection(DomHelper.getBody());
            SelectionHelper.clearAnySelectedText();

            DOM.addEventPreview(this);
            this.getWidget().addStyleName(WidgetConstants.RESIZABLE_PANEL_HANDLE_SELECTED_STYLE);
        }

        public boolean onEventPreview(final Event event) {
            ObjectHelper.checkNotNull("parameter:event", event);

            while (true) {
                final int type = DOM.eventGetType(event);
                if (Event.ONMOUSEMOVE == type) {
                    this.handleMouseMove(event);
                    break;
                }

                if (Event.ONMOUSEUP == type) {
                    this.handleMouseUp();
                }

                break;
            }

            return true;
        }

        /**
         * When the mouse button is released dragging of the handle stops.
         */
        protected void handleMouseUp() {
            DOM.removeEventPreview(this);
            SelectionHelper.enableTextSelection(DomHelper.getBody());
            SelectionHelper.clearAnySelectedText();
            this.getWidget().removeStyleName(WidgetConstants.RESIZABLE_PANEL_HANDLE_SELECTED_STYLE);
        }

        /**
         * THis method calculates the deltaX/Y values to help adjust the panel width/height so that it catches up to the moving mouse.
         * 
         * @param event
         */
        protected void handleMouseMove(final Event event) {
            ObjectHelper.checkNotNull("parameter:event", event);

            int deltaX = DOM.eventGetClientX(event) + BrowserHelper.getScrollX();
            deltaX = deltaX - this.getX();
            deltaX = deltaX - ResizablePanel.this.getOffsetWidth();
            deltaX = deltaX + this.getWidth();

            int deltaY = DOM.eventGetClientY(event) + BrowserHelper.getScrollY();
            deltaY = deltaY - this.getY();
            deltaY = deltaY - ResizablePanel.this.getOffsetHeight();
            deltaY = deltaY + this.getHeight();

            this.handleMouseMove0(deltaX, deltaY);
        }

        /**
         * This method is fired each time the dragged handle is moved.
         * 
         * @param x
         * @param y
         */
        abstract protected void handleMouseMove0(final int x, final int y);

        /**
         * The y coordinate of the mouse when the handle drag started.
         */
        int x;

        int getX() {
            return this.x;
        }

        void setX(final int x) {
            this.x = x;
        }

        /**
         * The y coordinate of the mouse when the handle drag started.
         */
        int y;

        int getY() {
            return this.y;
        }

        void setY(final int y) {
            this.y = y;
        }

        /**
         * The original panel width when the resize started
         */
        int width;

        int getWidth() {
            PrimitiveHelper.checkGreaterThan("parameter:width", width, 0);
            return this.width;
        }

        void setWidth(final int width) {
            PrimitiveHelper.checkGreaterThan("parameter:width", width, 0);
            this.width = width;
        }

        /**
         * The original panel height when the drag started
         */
        int height;

        int getHeight() {
            PrimitiveHelper.checkGreaterThan("field:height", height, 0);
            return this.height;
        }

        void setHeight(final int height) {
            PrimitiveHelper.checkGreaterThan("parameter:height", height, 0);
            this.height = height;
        }

        public String toString() {
            return super.toString() + ", x: " + x + ", y: " + y + ", width: " + width + ", height: " + height;
        }
    }
}