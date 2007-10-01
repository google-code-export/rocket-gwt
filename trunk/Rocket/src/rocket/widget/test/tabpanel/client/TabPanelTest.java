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
package rocket.widget.test.tabpanel.client;

import java.util.Iterator;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StackTrace;
import rocket.util.client.SystemHelper;
import rocket.widget.client.tabpanel.BeforeTabCloseEvent;
import rocket.widget.client.tabpanel.BeforeTabSelectEvent;
import rocket.widget.client.tabpanel.BottomTabPanel;
import rocket.widget.client.tabpanel.LeftTabPanel;
import rocket.widget.client.tabpanel.RightTabPanel;
import rocket.widget.client.tabpanel.TabCloseEvent;
import rocket.widget.client.tabpanel.TabItem;
import rocket.widget.client.tabpanel.TabListener;
import rocket.widget.client.tabpanel.TabPanel;
import rocket.widget.client.tabpanel.TabSelectEvent;
import rocket.widget.client.tabpanel.TopTabPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabPanelTest implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert( caught.getMessage() + "\n" + StackTrace.asString( caught ));
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		
		final TabPanel topPanel = new TopTabPanel();
		completeTabPanel(topPanel);
		rootPanel.add(topPanel);

		final TabPanel bottomPanel = new BottomTabPanel();
		completeTabPanel(bottomPanel);
		rootPanel.add(bottomPanel);

		final TabPanel leftPanel = new LeftTabPanel();
		completeTabPanel(leftPanel);
		rootPanel.add(leftPanel);

		final TabPanel rightPanel = new RightTabPanel();
		completeTabPanel(rightPanel);
		rootPanel.add(rightPanel);			
	}

	/**
	 * Adds a tabListener and creates a InteractiveList control enabling
	 * manipulation of the TabPanel
	 * 
	 * @param tabPanel
	 */
	protected void completeTabPanel(final TabPanel tabPanel) {
		ObjectHelper.checkNotNull("parameter:tabPanel", tabPanel);

		tabPanel.setCloseButtonImageUrl("close.png");

		final TabItem item = new TabItem();
		item.setCaption("Unremovable TabItem");
		item.setContent(new HTML(TabPanelTest.createContent()));
		addTabItemWidgets( item );
		tabPanel.add(item, false);
		tabPanel.select(0);

		final InterativeList control = new InterativeList();
		control.setTabPanel(tabPanel);
		RootPanel.get().add(control);

		tabPanel.addTabListener(new TabListener() {
			public void onBeforeTabSelect(final BeforeTabSelectEvent event ) {
				ObjectHelper.checkNotNull( "TabSelectEvent.currentSelection", event.getCurrentSelection() );
				
				final TabItem item = event.getNewSelection();
				final String caption = item.getCaption();
				final boolean stop = ! Window.confirm("tabSelected caption[" + caption + "]\n ? Cancel=vetoes");
				if( stop ){
					event.stop();
				}
			}

			public void onTabSelect(final TabSelectEvent event ) {
				ObjectHelper.checkNotNull( "TabSelectEvent.previouslySelected", event.getPreviouslySelected() );
				final TabItem item = event.getCurrentSelection();
				final String caption = item.getCaption();
				control.log("tabSelected caption[" + caption + "]");
			}

			public void onBeforeTabClose(final BeforeTabCloseEvent event ) {
				final TabItem item = event.getClosing();
				final String caption = item.getCaption();
				final boolean stop = ! Window.confirm("beforeTabClosed caption[" + caption + "]\n ? Cancel=vetoes");
				if( stop ){
					event.stop();
				}
			}

			public void onTabClose(final TabCloseEvent event ) {
				final TabItem item = event.getClosed();
				final String caption = item.getCaption();
				control.log("tabClosed [" + caption + "]");
			}
		});
	}

	final static String createContent() {
		final Element element = DOM.getElementById( "lorem");
		ObjectHelper.checkNotNull("hidden div with lorem text", element );
		return DOM.getInnerHTML( element );
	}

	class InterativeList extends rocket.testing.client.InteractiveList {
		InterativeList() {
			super();
		}

		protected String getCollectionTypeName() {
			return "TabPanel";
		}

		protected int getListSize() {
			return this.getTabPanel().getCount();
		}

		protected boolean getListIsEmpty() {
			throw new UnsupportedOperationException("isEmpty()");
		}

		protected boolean listAdd(final Object element) {
			this.getTabPanel().add((TabItem) element, closablePrompt());
			return true;
		}

		protected void listInsert(final int index, final Object element) {
			this.getTabPanel().insert(index, (TabItem) element, closablePrompt());
		}

		protected boolean closablePrompt() {
			return Window.confirm("Should the new tabPanel be closable ?\nOk=YES / Cancel=NO");
		}

		protected Object listGet(final int index) {
			return this.getTabPanel().get(index);
		}

		protected Object listRemove(final int index) {
			final TabPanel tabPanel = this.getTabPanel();
			final TabItem tabItem = tabPanel.get(index);
			tabPanel.remove(index);
			return tabItem;
		}

		protected Object listSet(final int index, final Object element) {
			throw new UnsupportedOperationException("set()");
		}

		protected Object createElement() {
			final TabItem item = new TabItem();
			item.setCaption("" + System.currentTimeMillis());
			item.setContent(new HTML(TabPanelTest.createContent()));

			addTabItemWidgets( item );
			return item;
		}
	

		protected Iterator listIterator() {
			return this.getTabPanel().iterator();
		}

		protected void checkType(Object element) {
			if (false == (element instanceof TabItem)) {
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
			final TabItem tabItem = (TabItem) element;
			return tabItem.getCaption();
		}

		/**
		 * Contains the tabPanel being interactively controlled.
		 */
		private TabPanel tabPanel;

		protected TabPanel getTabPanel() {
			ObjectHelper.checkNotNull("field:tabPanel", tabPanel);
			return this.tabPanel;
		}

		protected void setTabPanel(final TabPanel tabPanel) {
			ObjectHelper.checkNotNull("parameter:tabPanel", tabPanel);
			this.tabPanel = tabPanel;
		}

		public void log(final String message) {
			super.log(message);
		}
	}

	void addTabItemWidgets( final TabItem tabItem ){
		tabItem.addTabWidget( createTabItemWidget( "application_form_magnify.png", "1st icon before"), false);
		tabItem.addTabWidget( createTabItemWidget( "application_get.png", "2nd icon before"), false);
		tabItem.addTabWidget( createTabItemWidget( "application_go.png", "1st icon after" ), true);;
		tabItem.addTabWidget( createTabItemWidget( "application_home.png", "2nd icon after"), true);
	}
	
	HTML createTabItemWidget( final String url, final String altText ){
		final HTML html = new HTML();
		html.addClickListener( new ClickListener(){
			public void onClick( final Widget sender ){
				Window.alert( "Click!");
			}
		});
		html.setHTML( "<img src=\"" + url +"\" alt=\"" + altText + "\">");
		return html;
	}	
}
