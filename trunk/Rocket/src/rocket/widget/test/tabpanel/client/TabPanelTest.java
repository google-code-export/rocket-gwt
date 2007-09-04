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
import rocket.util.client.SystemHelper;
import rocket.widget.client.tabpanel.BottomTabPanel;
import rocket.widget.client.tabpanel.LeftTabPanel;
import rocket.widget.client.tabpanel.RightTabPanel;
import rocket.widget.client.tabpanel.TabItem;
import rocket.widget.client.tabpanel.TabListener;
import rocket.widget.client.tabpanel.TabPanel;
import rocket.widget.client.tabpanel.TopTabPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TabPanelTest implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		rootPanel.add(createTopTabPanelButton());
		rootPanel.add(createBottomTabPanelButton());
		rootPanel.add(createLeftTabPanelButton());
		rootPanel.add(createRightTabPanelButton());
	}

	protected Button createTopTabPanelButton() {
		final Button button = new Button("Create TopTabPanel");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final TabPanel panel = new TopTabPanel();
				completeTabPanel(panel);
				RootPanel.get().add(panel);
			}
		});
		return button;
	}

	/**
	 * Adds a tabListener and creates a InteractiveList control enabling
	 * manipulation of the TabPanel
	 * 
	 * @param tabPanel
	 */
	protected void completeTabPanel(final TabPanel tabPanel) {
		ObjectHelper.checkNotNull("parameter:tabPanel", tabPanel);

		tabPanel.setCloseButtonImageUrl("close.gif");

		final TabItem item = new TabItem();
		item.setCaption("Unremovable TabItem");
		item.setContent(new HTML(TabPanelTest.createContent()));
		item.addTabWidget(new HTML("1A"), false);
		item.addTabWidget(new HTML("2A"), false);
		item.addTabWidget(new HTML("1B"), true);
		item.addTabWidget(new HTML("2B"), true);

		tabPanel.add(item, false);
		tabPanel.select(0);

		final InterativeList control = new InterativeList();
		control.setTabPanel(tabPanel);
		RootPanel.get().add(control);

		tabPanel.addTabListener(new TabListener() {
			public boolean onBeforeTabSelected(final TabItem item) {
				final String caption = item.getCaption();
				final Widget content = item.getContent();
				return Window.confirm("tabSelected caption[" + caption + "]\ncontent: " + content + "\n ? Cancel=vetoes");
			}

			public void onTabSelected(final TabItem item) {
				final String caption = item.getCaption();
				final HTML content = (HTML) item.getContent();
				control.addMessage("tabSelected caption[" + caption + "]" + content.getText().substring(0, 50));
			}

			public boolean onBeforeTabClosed(final TabItem item) {
				final String caption = item.getCaption();
				final Widget content = item.getContent();
				return Window.confirm("beforeTabClosed caption[" + caption + "]\ncontent: " + content + "\n ? Cancel=vetoes");
			}

			public void onTabClosed(final TabItem item) {
				final String caption = item.getCaption();
				final HTML content = (HTML) item.getContent();
				control.addMessage("tabClosed [" + caption + "] content: " + content.getText().substring(0, 50));
			}
		});
	}

	protected Button createBottomTabPanelButton() {
		final Button button = new Button("Create BottomTabPanel");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final TabPanel panel = new BottomTabPanel();
				completeTabPanel(panel);
				RootPanel.get().add(panel);
			}
		});
		return button;
	}

	protected Button createLeftTabPanelButton() {
		final Button button = new Button("Create LeftTabPanel");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final TabPanel panel = new LeftTabPanel();
				completeTabPanel(panel);
				RootPanel.get().add(panel);

			}
		});
		return button;
	}

	protected Button createRightTabPanelButton() {
		final Button button = new Button("Create RightTabPanel");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final TabPanel panel = new RightTabPanel();
				completeTabPanel(panel);
				RootPanel.get().add(panel);
			}
		});
		return button;
	}

	final static String createContent() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 1000; i++) {
			if ((i % 32) == 0) {
				buf.append("<br/>");
			}
			buf.append((char) (32 + (Random.nextInt() & 95)));
		}
		return buf.toString();
	}

	final static String createContent(final String text) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			buf.append(text);
			buf.append("<br/>");
		}
		return buf.toString();
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

			item.addTabWidget(new HTML("A1"), false);
			item.addTabWidget(new HTML("A2"), false);
			item.addTabWidget(new HTML("B1"), true);
			item.addTabWidget(new HTML("B2"), true);

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

		protected int getMessageLineCount() {
			return 10;
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

		public void addMessage(final String message) {
			super.addMessage(message);
		}
	}

}
