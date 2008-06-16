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
package rocket.widget.test.splitterpanel.client;

import java.util.Date;
import java.util.Iterator;

import rocket.testing.client.InteractiveList;
import rocket.util.client.Checker;
import rocket.util.client.StackTrace;
import rocket.widget.client.splitter.HorizontalSplitterPanel;
import rocket.widget.client.splitter.SplitterItem;
import rocket.widget.client.splitter.VerticalSplitterPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Tests both Horizontal and Vertical HorizontalSplitterPanel widgets.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class SplitterPanelTest implements EntryPoint {

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();

				Window.alert("Caught:" + caught + "\n" + StackTrace.asString(caught));
			}
		});

		final RootPanel rootPanel = RootPanel.get( "main");
		rootPanel.add(this.createHorizontalSplitterPanelListView());
		rootPanel.add(this.createHorizontalSplitterPanel());

		rootPanel.add(this.createVerticalSplitterPanelListView());
		rootPanel.add(this.createVerticalSplitterPanel());

		rootPanel.getElement().scrollIntoView();
	}

	HorizontalSplitterPanel horizontalSplitterPanel;

	HorizontalSplitterPanel getHorizontalSplitterPanel() {
		Checker.notNull("field:horizontalSplitterPanel", horizontalSplitterPanel);
		return this.horizontalSplitterPanel;
	}

	void setHorizontalSplitterPanel(final HorizontalSplitterPanel horizontalSplitterPanel) {
		Checker.notNull("parameter:horizontalSplitterPanel", horizontalSplitterPanel);
		this.horizontalSplitterPanel = horizontalSplitterPanel;
	}

	HorizontalSplitterPanel createHorizontalSplitterPanel() {
		final HorizontalSplitterPanel panel = new HorizontalSplitterPanel();
		panel.setWidth("95%");
		panel.setHeight("300px");
		panel.setSplitterSize(6);
		this.setHorizontalSplitterPanel(panel);
		return panel;
	}

	/**
	 * This method creates a widget which allows interactive manipulation of a
	 * new HorizontalSplitterPanel as if it were a list.
	 * 
	 * @return
	 */
	protected Widget createHorizontalSplitterPanelListView() {

		final InteractiveList list = new InteractiveList<SplitterItem>() {

			protected String getCollectionTypeName() {
				return "HorizontalSplitterPanel";
			}

			protected int getListSize() {
				return SplitterPanelTest.this.getHorizontalSplitterPanel().getCount();
			}

			protected boolean getListIsEmpty() {
				throw new UnsupportedOperationException();
			}

			protected boolean listAdd(SplitterItem element) {
				SplitterPanelTest.this.getHorizontalSplitterPanel().add((SplitterItem) element);
				return true;
			}

			protected void listInsert(final int index, final SplitterItem element) {
				SplitterPanelTest.this.getHorizontalSplitterPanel().insert(index, (SplitterItem) element);
			}

			protected SplitterItem listGet(final int index) {
				return SplitterPanelTest.this.getHorizontalSplitterPanel().get(index);
			}

			protected SplitterItem listRemove(final int index) {
				final SplitterItem item = SplitterPanelTest.this.getHorizontalSplitterPanel().get(index);
				SplitterPanelTest.this.getHorizontalSplitterPanel().remove(index);
				return item;
			}

			protected SplitterItem listSet(int index, SplitterItem element) {
				throw new UnsupportedOperationException();
			}

			protected SplitterItem createElement() {
				final SplitterItem item = new SplitterItem();
				item.setMinimumSize(Integer.parseInt(Window.prompt("MinimumSize in pixels", "100")));
				item.setSizeShare(Integer.parseInt(Window.prompt("ShareSize", "100")));
				item.setWidget(SplitterPanelTest.createWidget());
				return item;
			}

			protected Iterator<SplitterItem> listIterator() {
				return SplitterPanelTest.this.getHorizontalSplitterPanel().iterator();
			}

			protected void checkType(SplitterItem element) {
				if (false == (element instanceof SplitterItem)) {
					Checker.fail("");
				}
			}

			protected String toString(final SplitterItem splitterItem) {
				final Widget widget = splitterItem.getWidget();
				return "minimumSize: " + splitterItem.getMinimumSize() + ", sizeShare: " + splitterItem.getSizeShare() + ", innerText\""
						+ DOM.getInnerText(widget.getElement()).substring(0, 100) + "...]";
			}
		};

		return list;
	}

	VerticalSplitterPanel verticalSplitterPanel;

	VerticalSplitterPanel getVerticalSplitterPanel() {
		Checker.notNull("field:verticalSplitterPanel", verticalSplitterPanel);
		return this.verticalSplitterPanel;
	}

	void setVerticalSplitterPanel(final VerticalSplitterPanel verticalSplitterPanel) {
		Checker.notNull("parameter:verticalSplitterPanel", verticalSplitterPanel);
		this.verticalSplitterPanel = verticalSplitterPanel;
	}

	VerticalSplitterPanel createVerticalSplitterPanel() {
		final VerticalSplitterPanel panel = new VerticalSplitterPanel();
		panel.setWidth("95%");
		panel.setHeight("600px");
		panel.setSplitterSize(6);
		this.setVerticalSplitterPanel(panel);
		return panel;
	}

	/**
	 * This method creates a widget which allows interactive manipulation of a
	 * new VerticalSplitterPanel as if it were a list.
	 * 
	 * @return
	 */
	protected Widget createVerticalSplitterPanelListView() {

		final InteractiveList list = new InteractiveList<SplitterItem>() {

			protected String getCollectionTypeName() {
				return "VerticalSplitterPanel";
			}

			protected int getListSize() {
				return SplitterPanelTest.this.getVerticalSplitterPanel().getCount();
			}

			protected boolean getListIsEmpty() {
				throw new UnsupportedOperationException();
			}

			protected boolean listAdd(SplitterItem element) {
				SplitterPanelTest.this.getVerticalSplitterPanel().add((SplitterItem) element);
				return true;
			}

			protected void listInsert(final int index, final SplitterItem element) {
				SplitterPanelTest.this.getVerticalSplitterPanel().insert(index, (SplitterItem) element);
			}

			protected SplitterItem listGet(final int index) {
				return SplitterPanelTest.this.getVerticalSplitterPanel().get(index);
			}

			protected SplitterItem listRemove(final int index) {
				final SplitterItem item = SplitterPanelTest.this.getVerticalSplitterPanel().get(index);
				SplitterPanelTest.this.getVerticalSplitterPanel().remove(index);
				return item;
			}

			protected SplitterItem listSet(int index, SplitterItem element) {
				throw new UnsupportedOperationException();
			}

			protected SplitterItem createElement() {
				final SplitterItem item = new SplitterItem();

				item.setMinimumSize(Integer.parseInt(Window.prompt("MinimumSize in pixels", "100")));
				item.setSizeShare(Integer.parseInt(Window.prompt("SizeShare", "100")));
				item.setWidget(SplitterPanelTest.createWidget());
				return item;
			}

			protected Iterator<SplitterItem> listIterator() {
				return SplitterPanelTest.this.getVerticalSplitterPanel().iterator();
			}

			protected void checkType(SplitterItem element) {
				if (false == (element instanceof SplitterItem)) {
					Checker.fail("");
				}
			}

			protected String toString(final SplitterItem splitterItem) {
				final Widget widget = splitterItem.getWidget();
				return "minimumSize: " + splitterItem.getMinimumSize() + ", sizeShare: " + splitterItem.getSizeShare() + ", innerText\""
						+ DOM.getInnerText(widget.getElement()).substring(0, 100) + "...]";
			}
		};

		return list;
	}

	/**
	 * Factory method which creates a TEXT widget full of 200 jibberish words
	 * 
	 * @return
	 */
	static Widget createWidget() {
		final StringBuffer buf = new StringBuffer();

		buf.append("<b>");
		buf.append(new Date());
		buf.append("</b> ");

		for (int i = 0; i < 200; i++) {
			buf.append(i);
			buf.append(' ');

			final int wordLength = 2 + Random.nextInt(10);
			for (int j = 0; j < wordLength; j++) {
				final char c = (char) ('a' + Random.nextInt('z' - 'a'));
				buf.append(c);
			}

			buf.append(' ');
		}

		return new HTML(buf.toString());
	}
}
