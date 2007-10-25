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

import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StackTrace;
import rocket.util.client.SystemHelper;
import rocket.widget.client.Button;
import rocket.widget.client.Html;
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
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
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
				Window.alert(caught.getMessage() + "\n" + StackTrace.asString(caught));
			}
		});
		
		final DockPanel topBottomDockPanel = new DockPanel();
		
		final TabPanel bottomPanel = new BottomTabPanel();
		completeTabPanel(bottomPanel);
		topBottomDockPanel.add( bottomPanel, DockPanel.NORTH );
		
		final TabPanel topPanel = new TopTabPanel();
		completeTabPanel(topPanel);
		topBottomDockPanel.add( topPanel, DockPanel.SOUTH );

		final Html topBottomCenter = new Html();
		topBottomCenter.setHeight( "100px");
		topBottomDockPanel.add( topBottomCenter, DockPanel.CENTER );
		
		final DockPanel leftRightDockPanel = new DockPanel();
		final TabPanel rightPanel = new RightTabPanel();
		completeTabPanel(rightPanel);
		leftRightDockPanel.add( rightPanel, DockPanel.WEST );

		final TabPanel leftPanel = new LeftTabPanel();
		completeTabPanel(leftPanel);
		leftRightDockPanel.add( leftPanel, DockPanel.EAST );
		
		final Html leftRightCenter = new Html();
		leftRightCenter.setWidth( "100px");
		leftRightDockPanel.add( leftRightCenter, DockPanel.CENTER );
		
		final DeckPanel deckPanel = new DeckPanel();
		deckPanel.add( topBottomDockPanel );
		deckPanel.add( leftRightDockPanel );
		deckPanel.showWidget( 0 );
		
		final RootPanel rootPanel = RootPanel.get();
		rootPanel.add( deckPanel );
		
		final TabPanelControl topPanelControl = new TabPanelControl( topPanel );
		final Element topPanelControlElement = topPanelControl.getElement();
		InlineStyle.setString( topPanelControlElement, Css.POSITION, "absolute" );
		InlineStyle.setInteger( topPanelControlElement, Css.LEFT, 10, CssUnit.PX );
		InlineStyle.setInteger( topPanelControlElement, Css.BOTTOM, 50, CssUnit.PX );
		InlineStyle.setInteger( topPanelControlElement, Css.Z_INDEX, 10, CssUnit.NONE );
		rootPanel.add( topPanelControl );
		
		final TabPanelControl bottomPanelControl = new TabPanelControl( bottomPanel );
		final Element bottomPanelControlElement = bottomPanelControl.getElement();
		InlineStyle.setString( bottomPanelControlElement, Css.POSITION, "absolute" );
		InlineStyle.setInteger( bottomPanelControlElement, Css.LEFT, 10, CssUnit.PX );
		InlineStyle.setInteger( bottomPanelControlElement, Css.TOP, 50, CssUnit.PX );
		InlineStyle.setInteger( bottomPanelControlElement, Css.Z_INDEX, 10, CssUnit.NONE );
		rootPanel.add( bottomPanelControl );

		final TabPanelControl leftPanelControl = new TabPanelControl( leftPanel );
		leftPanelControl.setVisible( false );
		final Element leftPanelControlElement = leftPanelControl.getElement();
		InlineStyle.setString( leftPanelControlElement, Css.POSITION, "absolute" );
		InlineStyle.setInteger( leftPanelControlElement, Css.LEFT, 10, CssUnit.PX );
		InlineStyle.setInteger( leftPanelControlElement, Css.BOTTOM, 50, CssUnit.PX );
		InlineStyle.setInteger( leftPanelControlElement, Css.Z_INDEX, 10, CssUnit.NONE );
		rootPanel.add( leftPanelControl );
		
		final TabPanelControl rightPanelControl = new TabPanelControl( rightPanel );
		rightPanelControl.setVisible( false );
		final Element rightPanelControlElement = rightPanelControl.getElement();
		InlineStyle.setString( rightPanelControlElement, Css.POSITION, "absolute" );
		InlineStyle.setInteger( rightPanelControlElement, Css.LEFT, 10, CssUnit.PX );
		InlineStyle.setInteger( rightPanelControlElement, Css.TOP, 50, CssUnit.PX );
		InlineStyle.setInteger( rightPanelControlElement, Css.Z_INDEX, 10, CssUnit.NONE );
		rootPanel.add( rightPanelControl );
		
		final Button button = new Button( "Swap");
		button.addMouseEventListener( new MouseEventAdapter(){
			public void onClick( final MouseClickEvent event) {
				final int index = deckPanel.getVisibleWidget();
				final boolean wasLeftRight = index == 1;
				
				topPanelControl.setVisible( wasLeftRight );
				bottomPanelControl.setVisible( wasLeftRight );
				leftPanelControl.setVisible( ! wasLeftRight );
				rightPanelControl.setVisible( ! wasLeftRight );
				
				deckPanel.showWidget( wasLeftRight ? 0 : 1 );
			}
		});
		rootPanel.add( button );
		final Element buttonElement = button.getElement();
		InlineStyle.setString( buttonElement, Css.POSITION, "absolute" );
		InlineStyle.setInteger( buttonElement, Css.RIGHT, 10, CssUnit.PX );
		InlineStyle.setInteger( buttonElement, Css.TOP, 10, CssUnit.PX );
		InlineStyle.setInteger( buttonElement, Css.Z_INDEX, 30, CssUnit.NONE );
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
		addTabItemWidgets(item);
		tabPanel.add(item, false);
		tabPanel.select(0);

		tabPanel.addTabListener(new TabListener() {
			public void onBeforeTabSelect(final BeforeTabSelectEvent event) {
				ObjectHelper.checkNotNull("TabSelectEvent.currentSelection", event.getCurrentSelection());

				final TabItem item = event.getNewSelection();
				final String caption = item.getCaption();
				final boolean stop = !Window.confirm("tabSelected caption[" + caption + "]\n ? Cancel=vetoes");
				if (stop) {
					event.stop();
				}
			}

			public void onTabSelect(final TabSelectEvent event) {
				ObjectHelper.checkNotNull("TabSelectEvent.previouslySelected", event.getPreviouslySelected());
				final TabItem item = event.getCurrentSelection();
				final String caption = item.getCaption();
				//control.log("tabSelected caption[" + caption + "]");
			}

			public void onBeforeTabClose(final BeforeTabCloseEvent event) {
				final TabItem item = event.getClosing();
				final String caption = item.getCaption();
				final boolean stop = !Window.confirm("beforeTabClosed caption[" + caption + "]\n ? Cancel=vetoes");
				if (stop) {
					event.stop();
				}
			}

			public void onTabClose(final TabCloseEvent event) {
				final TabItem item = event.getClosed();
				final String caption = item.getCaption();
				//control.log("tabClosed [" + caption + "]");
			}
		});
	}

	final static String createContent() {
		final Element element = DOM.getElementById("lorem");
		ObjectHelper.checkNotNull("hidden div with lorem text", element);
		return DOM.getInnerHTML(element);
	}

	class TabPanelControl extends rocket.testing.client.InteractiveList {
		TabPanelControl( final TabPanel tabPanel ) {
			super();
			
			this.setTabPanel(tabPanel);
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

			addTabItemWidgets(item);
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

	void addTabItemWidgets(final TabItem tabItem) {
		tabItem.addTabWidget(createTabItemWidget("application_form_magnify.png", "1st icon before"), false);
		tabItem.addTabWidget(createTabItemWidget("application_get.png", "2nd icon before"), false);
		tabItem.addTabWidget(createTabItemWidget("application_go.png", "1st icon after"), true);
		;
		tabItem.addTabWidget(createTabItemWidget("application_home.png", "2nd icon after"), true);
	}

	HTML createTabItemWidget(final String url, final String altText) {
		final HTML html = new HTML();
		html.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				Window.alert("Click!");
			}
		});
		html.setHTML("<img src=\"" + url + "\" alt=\"" + altText + "\">");
		return html;
	}
}
