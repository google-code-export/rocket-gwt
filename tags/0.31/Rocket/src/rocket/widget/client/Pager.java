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

import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget provides a simple list of pages shown as numbers. THe user may click on any page and which time an event is fired to notify
 * listeners. This widget allows easy creation of the page numbering that appears at the bottom of all Google searches.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Pager extends AbstractNumberHolder implements NumberHolder {

    public Pager() {
    	final HorizontalPanel horizontalPanel = this.createHorizontalPanel();
    	this.setHorizontalPanel(horizontalPanel);
        this.initWidget( horizontalPanel );
    }

    // NUMBER HOLDER
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public int getValue() {
        return this.getCurrentPage();
    }

    public void setValue(final int value) {
        this.setCurrentPage(value);
    }

    /**
     * Clears the dockPanels nd repaints all the buttons.
     * 
     * The current ordering of buttons is PREVIOUS | FIRST | IN BETWEENS LAST | NEXT
     */
    public void redraw() {
        final HorizontalPanel panel = this.getHorizontalPanel();
        panel.clear();

        panel.add(this.createPreviousButton());

        // create the in between buttons.
        final int currentPage = this.getCurrentPage();
        final int firstPage = this.getFirstPage();
        final int lastPage = this.getLastPage();
        final int pagesInBetweenCount = this.getPagesInBetweenCount();

        int first = currentPage - pagesInBetweenCount / 2;
        int last = first + pagesInBetweenCount;
        if (first <= firstPage) {
            first = firstPage;
            last = first + pagesInBetweenCount;
        }
        if (last >= lastPage) {
            last = lastPage;
            first = Math.max(first, last - pagesInBetweenCount);
        }

        for (int j = first; j < last; j++) {
            // instead of adding the first add a jump to firstButton
            int pageNumber = j;
            if (j == first) {
                pageNumber = firstPage;
            }
            if (j == (last - 1)) {
                pageNumber = lastPage - 1;
            }

            final Button button = this.createPage(pageNumber);
            panel.add(button);
        }

        panel.add(this.createNextButton());
    }

    /**
     * The horizontalPanel which is used to display the table.
     */
    private HorizontalPanel horizontalPanel;

    protected HorizontalPanel getHorizontalPanel() {
        ObjectHelper.checkNotNull("field:horizontalPanel", horizontalPanel);
        return horizontalPanel;
    }

    protected boolean hasHorizontalPanel() {
        return this.horizontalPanel != null;
    }

    protected void setHorizontalPanel(final HorizontalPanel horizontalPanel) {
        ObjectHelper.checkNotNull("parameter:horizontalPanel", horizontalPanel);
        this.horizontalPanel = horizontalPanel;
    }

    protected HorizontalPanel createHorizontalPanel() {
        final HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName(WidgetConstants.PAGER_STYLE);
        return panel;
    }

    protected Button createPreviousButton() {
        final Button button = new Button(WidgetConstants.PAGER_PREVIOUS_BUTTON_TEXT);
        button.setStyleName(WidgetConstants.PAGER_PREVIOUS_BUTTON_STYLE);

        // if already on the first page disable button
        final int currentPage = this.getCurrentPage();
        final int firstPage = this.getFirstPage();
        if (currentPage > firstPage) {
            button.addClickListener(this.createButtonListener(currentPage - 1));
        } else {
            button.setEnabled(false);
        }
        return button;
    }

    protected Button createNextButton() {
        final Button button = new Button(WidgetConstants.PAGER_NEXT_BUTTON_TEXT);
        button.setStyleName(WidgetConstants.PAGER_PREVIOUS_BUTTON_STYLE);

        // if already on the last page disable button
        final int currentPage = this.getCurrentPage();
        final int lastPage = this.getLastPage();
        if (currentPage + 1 < lastPage) {
            button.addClickListener(this.createButtonListener(currentPage + 1));
        } else {
            button.setEnabled(false);
        }
        return button;
    }

    protected Button createPage(final int pageNumber) {
        return this.createPage(String.valueOf(pageNumber), pageNumber);
    }

    protected Button createPage(final String label, final int pageNumber) {
        StringHelper.checkNotEmpty("parameter:label", label);
        PrimitiveHelper.checkBetween("parameter:pageNumber", pageNumber, this.firstPage, this.lastPage);

        final Button button = new Button(String.valueOf(pageNumber));
        final boolean belongsToCurrentPage = this.getCurrentPage() == pageNumber;
        final String style = belongsToCurrentPage ? WidgetConstants.PAGER_CURRENT_PAGE_STYLE
                : WidgetConstants.PAGER_GOTO_PAGE_STYLE;
        button.setStyleName(style);

        if (false == belongsToCurrentPage) {
            button.addClickListener(this.createButtonListener(pageNumber));
        }

        return button;
    }

    /**
     * The first possible page.(inclusive)
     */
    private int firstPage;

    public int getFirstPage() {
        PrimitiveHelper.checkGreaterThan("field:firstPage", this.firstPage, 0);
        return this.firstPage;
    }

    public void setFirstPage(final int firstPage) {
        PrimitiveHelper.checkGreaterThan("parameter:firstPage", firstPage, 0);
        this.firstPage = firstPage;
    }

    /**
     * The last possible page(exclusive)
     */
    private int lastPage;

    public int getLastPage() {
        PrimitiveHelper.checkGreaterThan("field:lastPage", this.lastPage, 0);
        return this.lastPage;
    }

    public void setLastPage(final int lastPage) {
        PrimitiveHelper.checkGreaterThan("parameter:lastPage", lastPage, 0);
        this.lastPage = lastPage;
    }

    /**
     * The currently selected page.
     */
    private int currentPage;

    public int getCurrentPage() {
        PrimitiveHelper.checkBetween("field:currentPage", this.currentPage, this.firstPage, this.lastPage);
        return this.currentPage;
    }

    public void setCurrentPage(final int currentPage) {
        PrimitiveHelper.checkBetween("parameter:currentPage", currentPage, this.firstPage, this.lastPage);
        this.currentPage = currentPage;

        this.fireValueChanged();
    }

    /**
     * Factory method which creates the ClickListener which fires any registered listeners.
     * 
     * @param pageNumber
     * @return
     */
    protected ClickListener createButtonListener(final int pageNumber) {
        final Pager that = this;

        return new ClickListener() {
            public void onClick(Widget ignored) {
                that.setCurrentPage(pageNumber);
                that.redraw();
            }
        };
    }

    /**
     * The number of page buttons that go between previous/first and last/next
     */
    private int pagesInBetweenCount;

    public int getPagesInBetweenCount() {
        PrimitiveHelper.checkGreaterThan("field:pagesInBetweenCount", pagesInBetweenCount, 0);
        return this.pagesInBetweenCount;
    }

    public void setPagesInBetweenCount(final int pagesInBetweenCount) {
        PrimitiveHelper.checkGreaterThan("parameter:pagesInBetweenCount", pagesInBetweenCount, 0);
        this.pagesInBetweenCount = pagesInBetweenCount;
    }

    public String toString() {
        return super.toString() + ", currentPage: " + currentPage + ", firstPage: " + firstPage + ", lastPage: "
                + lastPage + ", pagesInBetweenCount: " + pagesInBetweenCount;
    }
}