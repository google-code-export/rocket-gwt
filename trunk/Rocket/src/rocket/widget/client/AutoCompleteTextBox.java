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
import rocket.event.client.Event;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.KeyDownEvent;
import rocket.event.client.KeyEventAdapter;
import rocket.event.client.KeyUpEvent;
import rocket.style.client.ComputedStyle;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.Css;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget combines a TextBox and VerticalList ( the drop down list portion ).
 * Each time text is typed a call to the {@link #buildDropDownList} is which
 * should be used to add entries to the drop down list. The widget takes care of
 * hiding and showing the list, as well as key navigation.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class AutoCompleteTextBox extends TextBox {

	public AutoCompleteTextBox() {
		super();
	}

	public AutoCompleteTextBox(final Element element) {
		super(element);
	}

	public void afterCreateElement() {
		super.afterCreateElement();

		this.addKeyEventListener(new KeyEventAdapter() {
			public void onKeyDown(final KeyDownEvent event) {
				AutoCompleteTextBox.this.onTextBoxKeyDown(event);
			}

			public void onKeyUp(final KeyUpEvent event) {
				AutoCompleteTextBox.this.onTextBoxKeyUp(event);
			}

		});
	}

	protected String getInitialStyleName() {
		return WidgetConstants.AUTO_COMPLETE_TEXT_BOX_STYLE;
	}

	public boolean add(final String text) {
		StringHelper.checkNotEmpty("parameter:text", text);

		final Html entry = createListEntry(text);
		int insertBefore = 0;

		// create a list and add the new widget, followed by a sort which will
		// give its ultimate insertion point.
		final DropDownList dropDownList = this.getDropDownList();
		final List htmlWidgets = CollectionHelper.toList(dropDownList.iterator());
		htmlWidgets.add(entry);
		Collections.sort(htmlWidgets, new Comparator() {
			public int compare(final Object object, final Object otherObject) {
				final Html html = (Html) object;
				final Html otherHtml = (Html) otherObject;

				return html.getText().toLowerCase().compareTo(otherHtml.getText().toLowerCase());
			}
		});

		insertBefore = htmlWidgets.indexOf(entry);
		dropDownList.insert(entry, insertBefore);

		return true;
	}

	protected Html createListEntry(final String text) {
		StringHelper.checkNotEmpty("parameter:text", text);

		return new Html(text);
	}

	public void clear() {
		this.getDropDownList().clear();
	}

	public int getCount() {
		return this.getDropDownList().getWidgetCount();
	}

	/**
	 * Shows the dropDownList. This is accomplished by absolutely positioning
	 * the dropDownList( a VerticalPanel)below this very textbox.
	 */
	public void showDropDownList() {
		final int left = this.getAbsoluteLeft();
		final int top = this.getAbsoluteTop() + this.getOffsetHeight();

		final DropDownList dropDownList = this.getDropDownList();
		this.setDropDownList(dropDownList);

		final Element element = dropDownList.getElement();
		InlineStyle.setInteger(element, Css.LEFT, left, CssUnit.PX);
		InlineStyle.setInteger(element, Css.TOP, top, CssUnit.PX);

		final int paddingLeft = ComputedStyle.getInteger(element, Css.PADDING_LEFT, CssUnit.PX, 0);
		final int paddingRight = ComputedStyle.getInteger(element, Css.PADDING_RIGHT, CssUnit.PX, 0);
		final int borderLeftWidth = ComputedStyle.getInteger(element, Css.BORDER_LEFT_WIDTH, CssUnit.PX, 0);
		final int borderRightWidth = ComputedStyle.getInteger(element, Css.BORDER_RIGHT_WIDTH, CssUnit.PX, 0);

		final int newWidth = this.getOffsetWidth() - paddingLeft - paddingRight - borderLeftWidth - borderRightWidth;

		dropDownList.setWidth(newWidth + "px");
		dropDownList.setVisible(true);

		DOM.addEventPreview(this.getEventPreviewer());
	}

	/**
	 * Hides the dropDownList as well as cancelling any selection which may have
	 * resulted as part of an auto complete match. The cursor is then positioned
	 * after the recently cancelled selection.
	 */
	public void hideDropDownList() {
		this.clearDropDownList();
		this.clearSelected();

		DOM.removeEventPreview(this.getEventPreviewer());
	}

	/**
	 * This widget provides the drop down list portion containing matches for
	 * the text within the TextBox.
	 */
	private DropDownList dropDownList;

	protected DropDownList getDropDownList() {
		if (false == this.hasDropDownList()) {
			this.setDropDownList(this.createDropDownList());
		}

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
		list.setStyleName(this.getDropDownListStyle());
		this.setEventPreviewer(this.createEventPreviewer());

		RootPanel.get().add(list, 0, 0);
		return list;
	}

	protected String getDropDownListStyle() {
		return WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE;
	}

	protected void clearDropDownList() {
		if (this.hasDropDownList()) {
			this.getDropDownList().removeFromParent();
			this.dropDownList = null;
		}
	}

	protected String getDropDownListOddRowStyle() {
		return WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_ODD_ROW_STYLE;
	}

	protected String getDropDownListEvenRowStyle() {
		return WidgetConstants.AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_EVEN_ROW_STYLE;
	}

	class DropDownList extends DivPanel {
		public void insert(final Widget widget, final int indexBefore) {
			super.insert(widget, indexBefore);

			final String oddRowStyle = AutoCompleteTextBox.this.getDropDownListOddRowStyle();
			final String evenRowStyle = AutoCompleteTextBox.this.getDropDownListEvenRowStyle();

			String add = oddRowStyle;
			String remove = evenRowStyle;
			if ((indexBefore & 1) == 0) {
				add = evenRowStyle;
				remove = oddRowStyle;
			}
			widget.addStyleName(add);

			// fix the classnames of all widgets after $indexBefore...
			final int widgetCount = this.getWidgetCount();

			for (int i = indexBefore + 1; i < widgetCount; i++) {
				// do the swap before because add/remove are set for
				// $indexBefore
				final String swap = add;
				add = remove;
				remove = swap;

				final Widget after = this.get(i);
				after.removeStyleName(remove);
				after.addStyleName(add);
			}
		}

		public boolean remove(final int index) {
			super.remove(index);

			// fix the classnames of all widgets after $index
			final int widgetCount = this.getWidgetCount();

			final String oddRowStyle = AutoCompleteTextBox.this.getDropDownListOddRowStyle();
			final String evenRowStyle = AutoCompleteTextBox.this.getDropDownListEvenRowStyle();

			String add = oddRowStyle;
			String remove = evenRowStyle;
			if ((index & 1) == 0) {
				add = evenRowStyle;
				remove = oddRowStyle;
			}

			for (int i = index; i < widgetCount; i++) {
				final Widget after = this.get(i);
				after.removeStyleName(remove);
				after.addStyleName(add);

				// do the swap before because add/remove are set for
				// $indexBefore
				final String swap = add;
				add = remove;
				remove = swap;
			}
			return true;
		}
	}

	/**
	 * This method is fired whenever the textbox receives a key up event.
	 * 
	 * @param event
	 */
	protected void onTextBoxKeyUp(final KeyUpEvent event) {
		while (true) {
			if( event.isEscape() || event.isEnter() || event.isCursorUp() || event.isCursorDown() || event.isCursorLeft() || event.isCursorRight() ){
				break;
			}			
			event.stop();

			this.buildDropDownList();

			// if the list is empty hide it...
			final DropDownList dropDownList = this.getDropDownList();
			if (dropDownList.getWidgetCount() == 0) {
				this.hideDropDownList();
				break;
			}

			// show the list...
			this.showDropDownList();

			if ( event.isBackspace()) {
				event.stop();
				break;
			}

			// select the difference between the textbox and the first match.
			final Html html = (Html) this.getDropDownList().get(0);
			final String firstMatch = html.getText();

			final int cursor = this.getCursorPos();
			this.setText(firstMatch);
			this.setSelectionRange(cursor, firstMatch.length() - cursor);
			break;
		}
	}

	/**
	 * Sub classes must override this method to build up the appropriate drop
	 * down list. Typically the first step involves clearing or removing
	 * existing items and then proceeding to add.
	 */
	abstract protected void buildDropDownList();

	/**
	 * This method is fired each time the textbox receives a key down event.
	 * 
	 * @param event
	 */
	protected void onTextBoxKeyDown(final KeyDownEvent event) {
		while (true) {
			if ( event.isEscape()) {
				this.cancelDropDown();
				event.stop();
				break;
			}
			if ( event.isEnter()) {
				if (this.hasSelected()) {
					this.copyValue(this.getSelected());
				}
				this.hideDropDownList();
				event.stop();
				break;
			}

			if ( event.isCursorUp()) {
				this.moveUpOneItem();
				event.stop();
				break;
			}
			if ( event.isCursorDown() ) {
				this.moveDownOneItem();
				event.stop();
				break;
			}
			break;
		}
	}

	/**
	 * Closes the drop down list.
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
	 * This eventPreview is activated whenever the user hits the down key to
	 * navigate the drop down list.
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
			public boolean onEventPreview(final com.google.gwt.user.client.Event rawEvent) {
				return AutoCompleteTextBox.this.onDropDownListEventPreview(Event.getEvent(rawEvent));
			}
		};
		return watcher;
	}

	/**
	 * This method is fired to handle or preview all global events and needs to
	 * be present when the dropDownList is visible. It is both responsible for
	 * mouse highlighting as well as detecting when the component loses focus or
	 * the user clicks on something else.
	 * 
	 * @param event
	 * @return
	 */
	protected boolean onDropDownListEventPreview(final Event event) {
		boolean dontCancel = true;
		while (true) {
			// if the target element is not the TextBox or DropDownList and the
			final Element target = event.getTarget();
			final int eventType = event.getBitMask();
			final DropDownList dropDownList = this.getDropDownList();
			if ((eventType == EventBitMaskConstants.MOUSE_CLICK || eventType == EventBitMaskConstants.FOCUS)
					& false == (target == this.getElement() || DOM.isOrHasChild(dropDownList.getElement(), target))) {
				this.hideDropDownList();
				break;
			}

			// ignore event if is not a click/mouseOut/mouseOver.
			final boolean click = eventType == EventBitMaskConstants.MOUSE_CLICK;
			final boolean err = eventType == EventBitMaskConstants.MOUSE_OUT;
			final boolean over = eventType == EventBitMaskConstants.MOUSE_OVER;
			if (false == (click | err | over)) {
				break;
			}

			// if a widget wasnt found ignore the event - because cant determine
			// which mouse event applies too.
			final Widget widget = WidgetHelper.findWidget(target, dropDownList.iterator());
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

	/**
	 * This method is used to select another widget and usually occurs whenever
	 * the user moves the select cursor via the keyboard or a mouse over.
	 * Listeners are notified prior to changing the selected item.
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
	 * Copies the text from the selected item within the drop down list to the
	 * text box.
	 * 
	 * @param widget
	 */
	protected void copyValue(final Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		final Html html = (Html) widget;
		this.setText(html.getText());
	}

	/**
	 * Handles the case when the up key is pressed and the selected item within
	 * the drop down list is changed.
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
	 * Handles the case when the down key is pressed and the selected item
	 * within the drop down list is changed.
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
}
