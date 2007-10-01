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
package rocket.widget.test.accordionpanel.client;

import java.util.Date;
import java.util.Iterator;

import rocket.util.client.ObjectHelper;
import rocket.util.client.SystemHelper;
import rocket.widget.client.accordion.AccordionItem;
import rocket.widget.client.accordion.AccordionItemSelectEvent;
import rocket.widget.client.accordion.AccordionListener;
import rocket.widget.client.accordion.AccordionPanel;
import rocket.widget.client.accordion.BeforeAccordionItemSelectEvent;
import rocket.widget.client.accordion.LeftSideAccordionPanel;
import rocket.widget.client.accordion.RightSideAccordionPanel;
import rocket.widget.client.accordion.VerticalAccordionPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class AccordionPanelTest implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
			}
		});

		final RootPanel rootPanel = RootPanel.get();

		final AccordionPanel leftSidePanel = new LeftSideAccordionPanel();
		completeAccordionPanel(leftSidePanel);
		rootPanel.add(leftSidePanel);
		
		final AccordionPanel rightSidePanel = new RightSideAccordionPanel();
		completeAccordionPanel(rightSidePanel);
		rootPanel.add(rightSidePanel);

		final AccordionPanel vertical = new VerticalAccordionPanel();
		completeAccordionPanel(vertical);
		rootPanel.add(vertical);
	}

	/**
	 * Adds a accordionListener and creates a InteractiveList control enabling
	 * manipulation of the AccordionPanel
	 * 
	 * @param accordionPanel
	 */
	protected void completeAccordionPanel(final AccordionPanel accordionPanel) {
		ObjectHelper.checkNotNull("parameter:accordionPanel", accordionPanel);

		String text = GWT.getTypeName( accordionPanel );
		text = text.substring( 1 + text.lastIndexOf( '.'));
		
		final AccordionItem item = new AccordionItem();
		item.setCaption( text );
		item.setContent(new HTML(this.getContent()));
		accordionPanel.add(item);
		accordionPanel.select(0);

		final AccordionPanelTestInterativeList control = new AccordionPanelTestInterativeList();
		control.setAccordionPanel(accordionPanel);
		RootPanel.get().add(control);

		accordionPanel.addAccordionListener(new AccordionListener() {
			public void onBeforeSelect(final BeforeAccordionItemSelectEvent event) {
				ObjectHelper.checkNotNull( "BeforeAccordionItemSelectEvent.currentSelection", event.getCurrentSelection() );				
				
				final AccordionItem newSelection = event.getNewSelection();
				final String caption = newSelection.getCaption();
				final Widget content = newSelection.getContent();
				final boolean cancel = ! Window.confirm("accordionSelected caption[" + caption + "]\ncontent: " + content + "\n ? Cancel=vetoes");
				if( cancel ){
					event.stop();
				}
			}

			public void onSelect(final AccordionItemSelectEvent event ) {
				ObjectHelper.checkNotNull( "AccordionItemSelectEvent.previouslySelected", event.getPreviouslySelected() );
				
				final AccordionItem selected = event.getNewSelection();
				final String caption = selected.getCaption();
				final HTML content = (HTML) selected.getContent();
				control.log("accordionSelected caption[" + caption + "]" + content.getText().substring(0, 50));
			}
		});
	}
	
	String getContent(){
		final Element element = DOM.getElementById( "Lorem");
		return DOM.getInnerHTML(element);
	}

	class AccordionPanelTestInterativeList extends rocket.testing.client.InteractiveList {
		AccordionPanelTestInterativeList() {
			super();
		}

		protected String getCollectionTypeName() {
			return "AccordionPanel";
		}

		protected int getListSize() {
			return this.getAccordionPanel().getCount();
		}

		protected boolean getListIsEmpty() {
			throw new UnsupportedOperationException("isEmpty()");
		}

		protected boolean listAdd(final Object element) {
			this.getAccordionPanel().add((AccordionItem) element);
			return true;
		}

		protected void listInsert(final int index, final Object element) {
			this.getAccordionPanel().insert(index, (AccordionItem) element);
		}

		protected Object listGet(final int index) {
			return this.getAccordionPanel().get(index);
		}

		protected Object listRemove(final int index) {
			final AccordionPanel accordionPanel = this.getAccordionPanel();
			final AccordionItem accordionItem = accordionPanel.get(index);
			accordionPanel.remove(index);
			return accordionItem;
		}

		protected Object listSet(final int index, final Object element) {
			throw new UnsupportedOperationException("set()");
		}

		protected Object createElement() {
			final AccordionItem item = new AccordionItem();
			item.setCaption("" + new Date());
			item.setContent(new HTML(AccordionPanelTest.this.getContent()));
			return item;
		}

		protected Iterator listIterator() {
			return this.getAccordionPanel().iterator();
		}

		protected void checkType(Object element) {
			if (false == (element instanceof AccordionItem)) {
				SystemHelper.fail("Unknown element type. element ");
			}
		}

		/**
		 * Creates a listbox friendly string form for the given element.
		 * 
		 * @param element
		 * @return
		 */
		protected String toString(final Object element) {
			final AccordionItem accordionItem = (AccordionItem) element;
			return accordionItem.getCaption();
		}

		/**
		 * Contains the accordionPanel being interactively controlled.
		 */
		private AccordionPanel accordionPanel;

		protected AccordionPanel getAccordionPanel() {
			ObjectHelper.checkNotNull("field:accordionPanel", accordionPanel);
			return this.accordionPanel;
		}

		protected void setAccordionPanel(final AccordionPanel accordionPanel) {
			ObjectHelper.checkNotNull("parameter:accordionPanel", accordionPanel);
			this.accordionPanel = accordionPanel;
		}

		public void log(final String message) {
			super.log(message);
		}
	}

}