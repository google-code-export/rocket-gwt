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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rocket.collection.client.CollectionHelper;
import rocket.style.client.CssUnit;
import rocket.style.client.StyleConstants;
import rocket.style.client.StyleHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget combines a TextBox and VerticalList ( the drop down list portion ). Clients should register a KeyboardListener and update the
 * VerticalList with auto complete match candidates. The component takes care of hiding/showing the list.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class AutoCompleteTextBox extends TextBox {

    public AutoCompleteTextBox() {
        super();
        this.setDropDownList( createDropDownList() );
        this.sinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
    }

    protected void onAttach() {
        super.onAttach();

        RootPanel.get().add(this.getDropDownList(), 0, 0);
    }

    protected void onDetach() {
        super.onDetach();

        final DropDownList dropDown = this.getDropDownList();
        dropDown.clear();
        dropDown.removeFromParent();
    }

    // PANEL
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public boolean add(final String text) {
        StringHelper.checkNotEmpty("parameter:text", text);

        final HTML entry = createListEntry(text);
        int insertBefore = 0;

        // create a list and add the new widget, followed by a sort which will
        // give its ultimate insertion point.
        final DropDownList dropDownList = this.getDropDownList();
        final List htmlWidgets = CollectionHelper.toList(dropDownList.iterator());
        htmlWidgets.add(entry);
        Collections.sort(htmlWidgets, new Comparator() {
            public int compare(final Object object, final Object otherObject) {
                final HTML html = (HTML) object;
                final HTML otherHtml = (HTML) otherObject;

                return html.getText().toLowerCase().compareTo(otherHtml.getText().toLowerCase());
            }
        });

        insertBefore = htmlWidgets.indexOf(entry);
        dropDownList.insert(entry, insertBefore);

        return true;
    }

    protected HTML createListEntry(final String text) {
        StringHelper.checkNotEmpty("parameter:text", text);

        return new HTML(text);
    }

    public void clear() {
        this.getDropDownList().clear();
    }

    public int getCount() {
        return this.getDropDownList().getWidgetCount();
    }

    // EVENT HANDLING
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void onBrowserEvent(final Event event) {
        while (true) {
            final int eventType = DOM.eventGetType(event);
            if (eventType == Event.ONKEYDOWN) {
                this.handleTextBoxKeyDown(event);
                break;
            }
            if (eventType == Event.ONKEYUP) {
                this.handleTextBoxKeyUp(event);
                break;
            }
            super.onBrowserEvent(event);
            break;
        }
    }

    protected void handleTextBoxKeyUp(final Event event) {
        while (true) {
            final char key = (char) DOM.eventGetKeyCode(event);

            if (key == WidgetConstants.AUTO_COMPLETE_TEXT_BOX_CANCEL_KEY
                    || key == WidgetConstants.AUTO_COMPLETE_TEXT_BOX_ACCEPT_KEY
                    || key == WidgetConstants.AUTO_COMPLETE_TEXT_BOX_UP_KEY
                    || key == WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DOWN_KEY || key == KeyboardListener.KEY_LEFT
                    || key == KeyboardListener.KEY_RIGHT) {
                break;
            }
            super.onBrowserEvent(event);
            if (key == KeyboardListener.KEY_BACKSPACE) {
                break;
            }

            // if the list is empty hide it...
            final DropDownList dropDownList = this.getDropDownList();
            if (dropDownList.getWidgetCount() == 0) {
                this.hideDropDownList();
                break;
            }

            // show the list...
            this.showDropDownList();

            // select the difference between the textbox and the first match.
            final Widget widget = this.getDropDownList().get(0);
            final HTML html = (HTML) widget;
            final String firstMatch = html.getText();

            final int cursor = this.getCursorPos();
            this.setText(firstMatch);
            this.setSelectionRange(cursor, firstMatch.length() - cursor);
            break;
        }
    }

    protected void handleTextBoxKeyDown(final Event event) {
        while (true) {
            final char key = (char) DOM.eventGetKeyCode(event);

            if (key == WidgetConstants.AUTO_COMPLETE_TEXT_BOX_CANCEL_KEY) {
                this.cancelDropDown();
                DOM.eventPreventDefault(event);
                break;
            }
            if (key == WidgetConstants.AUTO_COMPLETE_TEXT_BOX_ACCEPT_KEY) {
                if (this.hasSelected()) {
                    this.copyValue(this.getSelected());
                }
                this.hideDropDownList();
                DOM.eventPreventDefault(event);
                break;
            }

            if (key == WidgetConstants.AUTO_COMPLETE_TEXT_BOX_UP_KEY) {
                this.moveUpOneItem();
                break;
            }
            if (key == WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DOWN_KEY) {
                this.moveDownOneItem();
                break;
            }
            super.onBrowserEvent(event);
            break;
        }
    }

    /**
     * Shows the dropDownList. This is accomplished by absolutely positioning the dropDownList( a VerticalPanel)below this very textbox.
     */
    public void showDropDownList() {    	
    	final int left = this.getAbsoluteLeft();
        final int top = this.getAbsoluteTop() + this.getOffsetHeight();
        
        final Widget dropDownList = this.getDropDownList();
        final Element element = dropDownList.getElement();
        StyleHelper.setInlineIntegerStyleProperty(element, StyleConstants.LEFT, left, CssUnit.PX );
        StyleHelper.setInlineIntegerStyleProperty(element, StyleConstants.TOP, top, CssUnit.PX );       
        
        dropDownList.setWidth(this.getOffsetWidth() + "px");
        dropDownList.setVisible(true);

        DOM.addEventPreview(this.getEventPreviewer());
    }

    /**
     * Hides the dropDownList as well as cancelling any selection which may have resulted as part of an auto complete match. The cursor is
     * then positioned after the recently cancelled selection.
     */
    public void hideDropDownList() {
        this.getDropDownList().setVisible(false);
        this.clearSelected();

        DOM.removeEventPreview(this.getEventPreviewer());
    }

    /**
     * This widget provides the drop down list portion containing matches for the text within the TextBox.
     */
    private DropDownList dropDownList;

    protected DropDownList getDropDownList() {
        ObjectHelper.checkNotNull("field:dropDownList", dropDownList);
        return this.dropDownList;
    }

    protected boolean hasDropDownList() {
        return null != this.dropDownList;
    }

    protected void setDropDownList(final DropDownList dropDownList) {
        ObjectHelper.checkNotNull("field:dropDownList", dropDownList);
        this.dropDownList = dropDownList;
    }

    protected DropDownList createDropDownList() {
        final DropDownList list = new DropDownList();
        list.setVisible(false);
        list.setStyleName(WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE);
        this.setEventPreviewer( this.createEventPreviewer() );
        return list;
    }

    class DropDownList extends DivPanel {
        public void insert(final Widget widget, final int indexBefore) {
            super.insert(widget, indexBefore);

            String add = WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_ODD_ROW_STYLE;
            String remove = WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_EVEN_ROW_STYLE;
            if ((indexBefore & 1) == 0) {
                add = WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_EVEN_ROW_STYLE;
                remove = WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_ODD_ROW_STYLE;
            }
            widget.addStyleName(add);

            // fix the classnames of all widgets after $indexBefore...
            final int widgetCount = this.getWidgetCount();

            for (int i = indexBefore + 1; i < widgetCount; i++) {
                // do the swap before because add/remove are set for $indexBefore
                final String swap = add;
                add = remove;
                remove = swap;

                final Widget after = this.get(i);
                after.removeStyleName(remove);
                after.addStyleName(add);
            }
        }

        public void remove(final int index) {
            super.remove(index);

            // fix the classnames of all widgets after $index
            final int widgetCount = this.getWidgetCount();

            String add = WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_ODD_ROW_STYLE;
            String remove = WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_EVEN_ROW_STYLE;
            if ((index & 1) == 0) {
                add = WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_EVEN_ROW_STYLE;
                remove = WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_ODD_ROW_STYLE;
            }

            for (int i = index; i < widgetCount; i++) {
                final Widget after = this.get(i);
                after.removeStyleName(remove);
                after.addStyleName(add);

                // do the swap before because add/remove are set for $indexBefore
                final String swap = add;
                add = remove;
                remove = swap;
            }
        }
    }

    /**
     * This eventPreview is activated whenever the user hits the down key to navigate the drop down list.
     */
    private EventPreview eventPreviewer;

    protected EventPreview getEventPreviewer() {
        ObjectHelper.checkNotNull("field:eventPreviewer", eventPreviewer);
        return this.eventPreviewer;
    }

    protected boolean hasEventPreviewer() {
        return null != this.eventPreviewer;
    }

    protected void setEventPreviewer(final EventPreview eventPreviewer) {
        ObjectHelper.checkNotNull("field:eventPreviewer", eventPreviewer);
        this.eventPreviewer = eventPreviewer;
    }

    protected EventPreview createEventPreviewer() {
        final EventPreview watcher = new EventPreview() {
            public boolean onEventPreview(final Event event) {
                return AutoCompleteTextBox.this.onEventPreview(event);
            }
        };
        return watcher;
    }

    /**
     * This method is fired to handle or preview all global events and needs to be present when the dropDownList is visible. It is both
     * responsible for mouse highlighting as well as detecting when the component loses focus or the user clicks on something else.
     * 
     * @param event
     * @return
     */
    protected boolean onEventPreview(final Event event) {
        boolean dontCancel = true;
        while (true) {
            // if the target element is not the TextBox or DropDownList and the
            // eventType is either click/focus hideDropDown
            final Element target = DOM.eventGetTarget(event);
            final int eventType = DOM.eventGetType(event);
            final DropDownList dropDownList = this.getDropDownList();
            if ((eventType == Event.ONCLICK || eventType == Event.ONFOCUS)
                    & false == (target == this.getElement() || DOM.isOrHasChild(dropDownList.getElement(), target))) {
                this.hideDropDownList();
                break;
            }

            // ignore event if is not a click/mouseOut/mouseOver.
            final boolean click = eventType == Event.ONCLICK;
            final boolean err = eventType == Event.ONMOUSEOUT;
            final boolean over = eventType == Event.ONMOUSEOVER;
            if (false == (click | err | over)) {
                break;
            }

            // if a widget wasnt found ignore the event - because cant determine
            // which mouse event applies too.
            final Widget widget = WidgetHelper.findWidget(DOM.eventGetTarget(event), dropDownList.iterator());
            if (null == widget) {
                break;
            }

            if (click) {
                copyValue(widget);
                dontCancel = false;
                break;
            }

            if (over) {
                this.select(widget);
                dontCancel = false;
                break;
            }
            if (over) {
                this.unselect(widget);
                dontCancel = false;
                break;
            }
            break;
        }
        return dontCancel;
    }

    protected void clearEventPreviewerer() {
        DOM.removeEventPreview(this.getEventPreviewer());
        this.dropDownList = null;
    }

    /**
     * Closes the drop down list.
     * 
     */
    protected void cancelDropDown() {
        final int cursor = this.getCursorPos();
        final int selectionLength = this.getSelectionLength();
        final String text = this.getText();

        this.setText(text.substring(0, cursor) + text.substring(cursor + selectionLength));
        this.setCursorPos(cursor);

        this.hideDropDownList();
    }

    /**
     * Copies the text from the selected item within the drop down list to the text box.
     * 
     * @param widget
     */
    protected void copyValue(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final HTML html = (HTML) widget;
        this.setText(html.getText());
    }

    /**
     * Handles the case when the up key is pressed and the selected item within the drop down list is changed.
     */
    protected void moveUpOneItem() {
        while (true) {
            // there is no selected item - hide
            if (!this.hasSelected()) {
                this.hideDropDownList();
                break;
            }
            final DropDownList dropDownList = this.getDropDownList();
            final int index = dropDownList.indexOf(this.getSelected()) - 1;
            if (index < 0) {
                this.hideDropDownList();
                break;
            }
            final Widget newSelectedItem = dropDownList.get(index);
            this.select(newSelectedItem);
            break;
        }
    }

    /**
     * Handles the case when the down key is pressed and the selected item within the drop down list is changed.
     */
    protected void moveDownOneItem() {
        final DropDownList dropDownList = this.getDropDownList();
        final int index = this.hasSelected() ? dropDownList.indexOf(this.getSelected()) + 1 : 0;

        if (index < this.getCount()) {
            final Widget newSelectedItem = dropDownList.get(index);
            this.select(newSelectedItem);

            if (false == dropDownList.isVisible()) {
                this.showDropDownList();
            }
        }
    }

    /**
     * This method is used to select another widget and usually occurs whenever the user moves the select cursor via the keyboard or a mouse
     * over. Listeners are notified prior to changing the selected item.
     * 
     * @param widget
     */
    protected void select(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        // unselect the previously selected widget.
        if (this.hasSelected()) {
            this.unselect(this.getSelected());
        }

        widget.addStyleName(WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_SELECTED_STYLE);
        this.setSelected(widget);
    }

    protected void unselect(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);
        widget.removeStyleName(WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_SELECTED_STYLE);
    }

    /**
     * The widget that is currently being highlighted
     */
    private Widget selected;

    protected Widget getSelected() {
        ObjectHelper.checkNotNull("field:selected", selected);
        return this.selected;
    }

    protected boolean hasSelected() {
        return null != this.selected;
    }

    protected void setSelected(final Widget selected) {
        ObjectHelper.checkNotNull("field:selected", selected);
        this.selected = selected;
    }

    protected void clearSelected() {
        if (null != this.selected) {
            selected.removeStyleName(WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_SELECTED_STYLE);
        }
        this.selected = null;
    }

    public String toString() {
        return super.toString() + ", dropDownList: " + dropDownList + ", eventPreviewer: " + this.eventPreviewer
                + ", selected: " + selected;
    }
}
