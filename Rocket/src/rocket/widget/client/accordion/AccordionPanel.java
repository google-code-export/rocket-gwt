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
package rocket.widget.client.accordion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.event.client.EventBitMaskConstants;
import rocket.util.client.ObjectHelper;
import rocket.widget.client.CompositeWidget;

/**
 * An accordian is a stack of titles and their panels following immediately
 * below. Only one slot is active at any time, ie only one panel is shown with
 * all others hidden.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class AccordionPanel extends CompositeWidget {

	protected AccordionPanel() {
		super();
	}

	protected void beforeCreateWidget(){
		super.beforeCreateWidget();
		
		this.setItems(createItems());
		this.setAccordionListeners(createAccordionListeners());	
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS;
	}

	/**
	 * Sub-classes must implement a means to replace an existing Content widget
	 * with the content widget within the given AccordionItem.
	 * 
	 * @param item
	 */
	protected abstract void replaceContentWidget(final AccordionItem item);

	/**
	 * The currently selected AccordionItem.
	 */
	private AccordionItem selected;

	public AccordionItem getSelected() {
		ObjectHelper.checkNotNull("field:selected", selected);
		return this.selected;
	}

	protected boolean hasSelected() {
		return null != this.selected;
	}

	protected void setSelected(final AccordionItem selected) {
		ObjectHelper.checkNotNull("parameter:selected", selected);
		this.selected = selected;
	}

	protected void clearSelected() {
		this.selected = null;
	}

	public int getSelectedIndex() {
		return this.getItems().indexOf(this.getSelected());
	}

	public void select(final int index) {
		// remove the style & hide the content of the previously selected
		// item...
				
		while( true ){
			final AccordionListenerCollection listeners = this.getAccordionListeners();
			AccordionItem previouslySelected = null;	
			
			if (this.hasSelected()) {
				previouslySelected = this.getSelected();
				this.removeSelectedStyle(previouslySelected );
			}
			
			
			final BeforeAccordionItemSelectEvent beforeSelectedEvent = new BeforeAccordionItemSelectEvent();
			beforeSelectedEvent.setCurrentSelection( previouslySelected );
			
			final AccordionItem newSelection = this.get(index);
			beforeSelectedEvent.setNewSelection( newSelection );			
					
			listeners.fireBeforeSelected(beforeSelectedEvent );
			if( beforeSelectedEvent.isCancelled() ){
				break;
			}
			
			final AccordionItem newSelectionEvent = this.get(index);
			this.addSelectedStyle(newSelectionEvent);					
			this.setSelected(newSelectionEvent);
			
			final AccordionItemSelectEvent selectedEvent = new AccordionItemSelectEvent();
			selectedEvent.setNewSelection(newSelection);
			selectedEvent.setPreviouslySelected(previouslySelected);
			
			listeners.fireSelected(selectedEvent );		
			break;
		}			
	}

	protected abstract void removeSelectedStyle(final AccordionItem item);

	protected abstract void addSelectedStyle(final AccordionItem item);

	public void select(final AccordionItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);
		this.select(this.getIndex(item));
	}

	public int getCount() {
		return this.getItems().size();
	}

	public AccordionItem get(final int index) {
		return (AccordionItem) this.getItems().get(index);
	}

	public int getIndex(final AccordionItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);

		return this.getItems().indexOf(item);
	}

	public void add(final AccordionItem item) {
		this.insert(this.getCount(), item);
	}

	public void insert(final int insertBefore, final AccordionItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);

		this.insert0(insertBefore, item);
		item.setAccordionPanel(this);
		this.getItems().add(insertBefore, item);

		// if this is the only item in the panel select it...
		if (this.getCount() == 0) {
			this.select(0);
		}
	}

	protected abstract void insert0(final int insertBefore, final AccordionItem item);

	public void remove(final int index) {
		final AccordionItem item = this.get(index);

		while (true) {
			if (false == this.hasSelected()) {
				break;
			}
			final int selectedIndex = this.getSelectedIndex();
			if (index != selectedIndex) {
				break;
			}

			this.removeSelectedStyle(item);
			int newIndex = selectedIndex + 1;
			final int count = this.getCount();
			if (count == 1) {
				this.clearSelected();
				break;
			}

			if (newIndex == count) {
				newIndex = 0;
			}
			this.select(newIndex);
			break;
		}

		this.remove0(index);
		item.clearAccordionPanel();
		this.getItems().remove(index);
	}

	protected abstract void remove0(final int index);

	public boolean remove(final AccordionItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);

		final int index = this.getIndex(item);
		if (-1 != index) {
			this.remove(index);
		}
		return index != -1;
	}

	public Iterator iterator() {
		return this.getItems().iterator();
	}

	/**
	 * This list contains the individual AccordionItems
	 */
	private List items;

	protected List getItems() {
		ObjectHelper.checkNotNull("field:items", this.items);
		return this.items;
	}

	protected void setItems(final List items) {
		ObjectHelper.checkNotNull("parameter:items", items);
		this.items = items;
	}

	protected List createItems() {
		return new ArrayList();
	}

	// LISTENERS
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * A collection of AccordionListeners who will in turn be notified of all
	 * AccordionListener events.
	 */
	private AccordionListenerCollection accordionListeners;

	protected AccordionListenerCollection getAccordionListeners() {
		ObjectHelper.checkNotNull("field:accordionListeners", this.accordionListeners);
		return this.accordionListeners;
	}

	protected void setAccordionListeners(final AccordionListenerCollection accordionListeners) {
		ObjectHelper.checkNotNull("parameter:accordionListeners", accordionListeners);
		this.accordionListeners = accordionListeners;
	}

	protected AccordionListenerCollection createAccordionListeners() {
		return new AccordionListenerCollection();
	}

	public void addAccordionListener(final AccordionListener listener) {
		ObjectHelper.checkNotNull("parameter:listener", listener);
		this.getAccordionListeners().add(listener);
	}

	public void removeAccordionListener(final AccordionListener listener) {
		this.getAccordionListeners().remove(listener);
	}
}
