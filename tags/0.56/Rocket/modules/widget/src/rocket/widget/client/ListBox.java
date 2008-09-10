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

import rocket.dom.client.Dom;
import rocket.event.client.ChangeEventListener;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.FocusEventListener;
import rocket.widget.client.support.ListBoxSupport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple widget that contains the same capabilities of the GWT ListBox widget
 * but also adds the ability to hijack select elements from the dom.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * ListBox.
 * 
 * ROCKET When upgrading from GWT 1.5 RC1 reapply changes
 * 
 * @author Miroslav Pokorny
 */
public class ListBox extends FocusWidget {

	/**
	 * Reuse the GWT TextBox support.
	 */
	static private ListBoxSupport support = createSupport();

	static ListBoxSupport getSupport() {
		return support;
	}

	static ListBoxSupport createSupport() {
		return (ListBoxSupport) GWT.create(ListBoxSupport.class);
	}

	public ListBox() {
		super();
	}

	public ListBox(Element element) {
		super(element);
	}

	@Override
	protected void checkElement(Element element) {
		Dom.checkTagName("parameter:element", element, WidgetConstants.LISTBOX_TAG);
	}

	@Override
	protected Element createElement() {
		return DOM.createSelect();
	}

	@Override
	protected void afterCreateElement() {
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);

		dispatcher.setChangeEventListeners(dispatcher.createChangeEventListeners());
		dispatcher.setFocusEventListeners(dispatcher.createFocusEventListeners());
	}

	@Override
	protected String getInitialStyleName() {
		return WidgetConstants.LISTBOX_STYLE;
	}

	@Override
	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.CHANGE;
	}

	/**
	 * Adds an item to the list box. This method has the same effect as
	 * 
	 * <pre>
	 * addItem(item, item)
	 * </pre>
	 * 
	 * @param item
	 *            the text of the item to be added
	 */
	public void addItem(final String item) {
		insertItem(item, WidgetConstants.LISTBOX_INSERT_AT_END);
	}

	/**
	 * Adds an item to the list box, specifying an initial value for the item.
	 * 
	 * @param item
	 *            the text of the item to be added
	 * @param value
	 *            the item's value, to be submitted if it is part of a
	 *            {@link FormPanel}; cannot be <code>null</code>
	 */
	public void addItem(final String item, final String value) {
		insertItem(item, value, WidgetConstants.LISTBOX_INSERT_AT_END);
	}

	/**
	 * Gets the number of items present in the list box.
	 * 
	 * @return the number of items
	 */
	public int getItemCount() {
		return ListBox.getSupport().getItemCount(getElement());
	}

	/**
	 * Gets the text associated with the item at the specified index.
	 * 
	 * @param index
	 *            the index of the item whose text is to be retrieved
	 * @return the text associated with the item
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 */
	public String getItemText(final int index) {
		checkIndex(index);
		return ListBox.getSupport().getItemText(getElement(), index);
	}

	/**
	 * Sets the text associated with the item at a given index.
	 * 
	 * @param index
	 *            the index of the item to be set
	 * @param text
	 *            the item's new text
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 */
	public void setItemText(int index, String text) {
		checkIndex(index);
		if (text == null) {
			throw new NullPointerException("Cannot set an option to have null text");
		}
		DOM.setOptionText(getElement(), text, index);
	}

	/**
	 * Gets the currently-selected item. If multiple items are selected, this
	 * method will return the first selected item ({@link #isItemSelected(int)}
	 * can be used to query individual items).
	 * 
	 * @return the selected index, or <code>-1</code> if none is selected
	 */
	public int getSelectedIndex() {
		return this.getElement().getPropertyInt("selectedIndex");
	}

	/**
	 * Sets whether an individual list item is selected.
	 * 
	 * <p>
	 * Note that setting the selection programmatically does <em>not</em>
	 * cause the {@link ChangeListener#onChange(Widget)} event to be fired.
	 * </p>
	 * 
	 * @param index
	 *            the index of the item to be selected or unselected
	 * @param selected
	 *            <code>true</code> to select the item
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 */
	public void setItemSelected(final int index, final boolean selected) {
		checkIndex(index);
		ListBox.getSupport().setItemSelected(getElement(), index, selected);
	}

	/**
	 * Gets the value associated with the item at a given index.
	 * 
	 * @param index
	 *            the index of the item to be retrieved
	 * @return the item's associated value
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 */
	public String getValue(final int index) {
		checkIndex(index);
		return ListBox.getSupport().getItemValue(getElement(), index);
	}

	/**
	 * Sets the value associated with the item at a given index. This value can
	 * be used for any purpose, but is also what is passed to the server when
	 * the list box is submitted as part of a {@link FormPanel}.
	 * 
	 * @param index
	 *            the index of the item to be set
	 * @param value
	 *            the item's new value; cannot be <code>null</code>
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 */
	public void setValue(final int index, final String value) {
		checkIndex(index);
		ListBox.getSupport().setValue(getElement(), index, value);
	}

	/**
	 * Inserts an item into the list box. Has the same effect as
	 * 
	 * <pre>
	 * insertItem(item, item, index)
	 * </pre>
	 * 
	 * @param item
	 *            the text of the item to be inserted
	 * @param index
	 *            the index at which to insert it
	 */
	public void insertItem(String item, int index) {
		insertItem(item, item, index);
	}

	/**
	 * Inserts an item into the list box, specifying an initial value for the
	 * item. If the index is less than zero, or greater than or equal to the
	 * length of the list, then the item will be appended to the end of the
	 * list.
	 * 
	 * @param item
	 *            the text of the item to be inserted
	 * @param value
	 *            the item's value, to be submitted if it is part of a
	 *            {@link FormPanel}.
	 * @param index
	 *            the index at which to insert it
	 */
	public void insertItem(String item, String value, int index) {
		DOM.insertListItem(getElement(), item, value, index);
	}

	/**
	 * Removes all items from the list box.
	 */
	public void clear() {
		ListBox.getSupport().clear(getElement());
	}

	/**
	 * Removes the item at the specified index.
	 * 
	 * @param index
	 *            the index of the item to be removed
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 */
	public void removeItem(int index) {
		checkIndex(index);
		ListBox.getSupport().removeItem(getElement(), index);
	}

	/**
	 * Gets whether this list allows multiple selection.
	 * 
	 * @return <code>true</code> if multiple selection is allowed
	 */
	public boolean isMultipleSelect() {
		return getElement().getPropertyBoolean("multiple");
	}

	/**
	 * Sets whether this list allows multiple selections.
	 * <em>NOTE: The preferred
	 * way of enabling multiple selections in a list box is by using the
	 * {@link #ListBox(boolean)} constructor. Using this method can spuriously
	 * fail on Internet Explorer 6.0.</em>
	 * 
	 * @param multiple
	 *            <code>true</code> to allow multiple selections
	 */
	public void setMultipleSelect(boolean multiple) {
		// TODO: we can remove the above doc admonition once we address issue
		// 1007
		getElement().setPropertyBoolean("multiple", multiple);
	}

	/**
	 * Gets the number of items that are visible. If only one item is visible,
	 * then the box will be displayed as a drop-down list.
	 * 
	 * @return the visible item count
	 */
	public int getVisibleItemCount() {
		return getElement().getPropertyInt("size");
	}

	/**
	 * Sets the number of items that are visible. If only one item is visible,
	 * then the box will be displayed as a drop-down list.
	 * 
	 * @param visibleItems
	 *            the visible item count
	 */
	public void setVisibleItemCount(int visibleItems) {
		getElement().setPropertyInt("size", visibleItems);
	}

	/**
	 * Determines whether an individual list item is selected.
	 * 
	 * @param index
	 *            the index of the item to be tested
	 * @return <code>true</code> if the item is selected
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range
	 */
	public boolean isItemSelected(int index) {
		checkIndex(index);
		return ListBox.getSupport().isItemSelected(getElement(), index);
	}

	/**
	 * Sets the currently selected index.
	 * 
	 * <p>
	 * Note that setting the selected index programmatically does <em>not</em>
	 * cause the {@link ChangeListener#onChange(Widget)} event to be fired.
	 * </p>
	 * 
	 * @param index
	 *            the index of the item to be selected
	 */
	public void setSelectedIndex(int index) {
		this.getElement().setPropertyInt("selectedIndex", index);
	}

	private void checkIndex(int index) {
		if (index < 0 || index >= getItemCount()) {
			throw new IndexOutOfBoundsException();
		}
	}

	public void addChangeEventListener(final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().addChangeEventListener(changeEventListener);
	}

	public void removeChangeEventListener(final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().removeChangeEventListener(changeEventListener);
	}

	public void addFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().addFocusEventListener(focusEventListener);
	}

	public void removeFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().removeFocusEventListener(focusEventListener);
	}

}
