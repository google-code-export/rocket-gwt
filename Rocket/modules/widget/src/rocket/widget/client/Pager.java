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

import rocket.event.client.ChangeEventListener;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.event.client.MouseEventListener;
import rocket.util.client.Checker;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget provides a simple list of pages shown as numbers. THe user may
 * click on any page and which time an event is fired to notify listeners. This
 * widget allows easy creation of the page numbering that appears at the bottom
 * of all Google searches.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Pager extends CompositeWidget {

	public Pager() {
		super();
	}

	@Override
	protected Widget createWidget() {
		return this.createHorizontalPanel();
	}

	protected HorizontalPanel getHorizontalPanel() {
		return (HorizontalPanel) this.getWidget();
	}

	protected HorizontalPanel createHorizontalPanel() {
		return new HorizontalPanel();
	}

	@Override
	protected void afterCreateWidget() {
		final EventListenerDispatcher dispatcher = new EventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);
		dispatcher.prepareListenerCollections(EventBitMaskConstants.CHANGE);
	}

	@Override
	protected int getSunkEventsBitMask() {
		return 0;
	}

	@Override
	protected String getInitialStyleName() {
		return WidgetConstants.PAGER_STYLE;
	}

	@Override
	protected void onAttach() {
		super.onAttach();

		this.redraw();
	}

	/**
	 * Clears the dockPanels nd repaints all the buttons.
	 * 
	 * The current ordering of buttons is PREVIOUS | PAGES | NEXT
	 */
	protected void redraw() {
		if (this.isAttached()) {
			this.redraw0();
		}
	}

	protected void redraw0() {
		final HorizontalPanel panel = this.getHorizontalPanel();
		panel.clear();

		panel.add(this.createPreviousButton());

		// create the in between buttons.
		final int pagesInBetween = this.getPageLinksAcrossCount();
		final int firstPage = this.getFirstItem();
		final int lastPage = this.getLastItem();

		final int currentPage = this.getCurrentItem();
		final int itemsPerPage = this.getItemsPerPage();

		final int whole = pagesInBetween * itemsPerPage;
		final int half = whole / 2;

		int first = currentPage - half;
		int last = first + whole;

		if (first <= firstPage) {
			first = firstPage;
			last = firstPage + whole;
		}
		if (last > lastPage) {
			first = lastPage - whole;
			last = lastPage;

			if (first < firstPage) {
				first = firstPage;
			}
		}

		for (int k = first; k < last; k = k + itemsPerPage) {
			panel.add(this.createPage(k));
		}

		panel.add(this.createNextButton());
	}

	protected Widget createPreviousButton() {
		final Button button = new Button(this.getPreviousButtonText());
		button.setStyleName(this.getPreviousButtonStyle());

		// if already on the first page disable button
		final int previous = this.getCurrentItem() - this.getItemsPerPage();
		final int firstPage = this.getFirstItem();

		if (previous >= firstPage) {
			button.addMouseEventListener(this.createButtonListener(previous));
		} else {
			button.setEnabled(false);
		}
		return button;
	}

	protected String getPreviousButtonText() {
		return WidgetConstants.PAGER_PREVIOUS_BUTTON_TEXT;
	}

	protected String getPreviousButtonStyle() {
		return WidgetConstants.PAGER_PREVIOUS_BUTTON_STYLE;
	}

	protected Widget createNextButton() {
		final Button button = new Button(this.getNextButtonText());
		button.setStyleName(this.getNextButtonStyle());

		// if already on the last page disable button
		final int lastPage = this.getLastItem();
		final int next = this.getCurrentItem() + this.getItemsPerPage();

		if (next < lastPage) {
			button.addMouseEventListener(this.createButtonListener(next));
		} else {
			button.setEnabled(false);
		}
		return button;
	}

	protected String getNextButtonText() {
		return WidgetConstants.PAGER_NEXT_BUTTON_TEXT;
	}

	protected String getNextButtonStyle() {
		return WidgetConstants.PAGER_NEXT_BUTTON_STYLE;
	}

	protected Widget createPage(final int itemNumber) {
		final int pageNumber = itemNumber / this.getItemsPerPage();

		return this.createPage(String.valueOf(pageNumber + 1), itemNumber);
	}

	protected Widget createPage(final String label, final int itemNumber) {
		Checker.notEmpty("parameter:label", label);
		Checker.between("parameter:pageNumber", itemNumber, this.firstItem, this.lastItem);

		final Button button = new Button(label);
		final boolean onCurrentPage = this.getCurrentItem() == itemNumber;
		if (onCurrentPage) {
			button.setEnabled(false);
		}

		final String style = onCurrentPage ? this.getCurrentPageStyle() : this.getOtherPagesStyle();
		button.setStyleName(style);
		button.addMouseEventListener(this.createButtonListener(itemNumber));
		return button;
	}

	protected String getCurrentPageStyle() {
		return WidgetConstants.PAGER_CURRENT_PAGE_STYLE;
	}

	protected String getOtherPagesStyle() {
		return WidgetConstants.PAGER_GOTO_PAGE_STYLE;
	}

	/**
	 * The first possible page.(inclusive)
	 */
	private int firstItem;

	public int getFirstItem() {
		Checker.greaterThanOrEqual("field:firstItem", 0, this.firstItem);
		return this.firstItem;
	}

	public void setFirstItem(final int firstItem) {
		Checker.greaterThanOrEqual("parameter:firstItem", 0, firstItem);
		this.firstItem = firstItem;
	}

	/**
	 * The last possible page(exclusive)
	 */
	private int lastItem;

	public int getLastItem() {
		Checker.greaterThanOrEqual("field:lastItem", 0, this.lastItem);
		return this.lastItem;
	}

	public void setLastItem(final int lastItem) {
		Checker.greaterThanOrEqual("parameter:lastItem", 0, lastItem);
		this.lastItem = lastItem;
	}

	/**
	 * The currently selected page.
	 */
	private int currentItem;

	public int getCurrentItem() {
		this.checkCurrentItem("field:currentItem", currentItem);

		final int itemsPerPage = this.getItemsPerPage();
		return this.currentItem / itemsPerPage * itemsPerPage;
	}

	public void setCurrentItem(final int currentItem) {
		this.checkCurrentItem("parameter:currentItem", currentItem);

		this.currentItem = currentItem;

		this.getEventListenerDispatcher().getChangeEventListeners().fireChange(this);
		this.redraw();
	}

	protected void checkCurrentItem(final String message, final int currentPage) {
		Checker.between(message, currentPage, this.firstItem, this.lastItem == 0 ? 1 : this.lastItem);
	}

	/**
	 * Factory method which creates the ClickListener which fires any registered
	 * listeners.
	 * 
	 * @param itemNumber
	 *            The spot that the pager will jump to when this button is
	 *            clicked on.
	 * @return The new MouseEventListener
	 */
	protected MouseEventListener createButtonListener(final int itemNumber) {
		return new MouseEventAdapter() {
			public void onClick(final MouseClickEvent ignored) {
				Pager.this.setCurrentItem(itemNumber);
				Pager.this.redraw();
			}
		};
	}

	/**
	 * The number of page buttons that go between previous/first and last/next
	 */
	private int pageLinksAcrossCount;

	public int getPageLinksAcrossCount() {
		Checker.greaterThan("field:pageLinksAcrossCount", 0, pageLinksAcrossCount);
		return this.pageLinksAcrossCount;
	}

	public void setPageLinksAcrossCount(final int pageLinksAcrossCount) {
		Checker.greaterThan("parameter:pageLinksAcrossCount", 0, pageLinksAcrossCount);
		this.pageLinksAcrossCount = pageLinksAcrossCount;
	}

	/**
	 * The items that are placed per page.
	 */
	private int itemsPerPage;

	public int getItemsPerPage() {
		Checker.greaterThan("field:itemsPerPage", 0, itemsPerPage);
		return this.itemsPerPage;
	}

	public void setItemsPerPage(final int itemsPerPage) {
		Checker.greaterThan("parameter:itemsPerPage", 0, itemsPerPage);
		this.itemsPerPage = itemsPerPage;
	}

	public void addChangeEventListener(final ChangeEventListener changeListener) {
		this.getEventListenerDispatcher().addChangeEventListener(changeListener);
	}

	public void removeChangeEventListener(final ChangeEventListener changeListener) {
		this.getEventListenerDispatcher().removeChangeEventListener(changeListener);
	}
}
