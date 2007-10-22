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
package rocket.testing.client;

import java.util.Iterator;
import java.util.List;

import rocket.dom.client.Dom;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.Css;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StackTrace;
import rocket.widget.client.DivPanel;
import rocket.widget.client.ZebraFlexTable;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This test runner outputs results of running a suite of tests to a table.
 * 
 * If elements must be added a methods are provided to add the test name across
 * the screen {@link #addTestNameDivider()} and another to add the element
 * {@link #addElement(Element)}
 * 
 * @author Miroslav Pokorny (mP)
 */
public class WebPageTestRunner extends TestRunner {

	public WebPageTestRunner() {
		super();

		RootPanel.get().add(this.createTable());
	}

	/**
	 * A div panel is used to host any added Elements.
	 */
	private DivPanel divPanel;

	DivPanel getDivPanel() {
		if (false == this.hasDivPanel()) {
			RootPanel.get().add(this.createDivPanel());
		}

		ObjectHelper.checkNotNull("field:divPanel", divPanel);
		return divPanel;
	}

	boolean hasDivPanel() {
		return null != this.divPanel;
	}

	void setDivPanel(final DivPanel divPanel) {
		ObjectHelper.checkNotNull("parameter:divPanel", divPanel);
		this.divPanel = divPanel;
	}

	DivPanel createDivPanel() {
		final DivPanel panel = new DivPanel();
		this.setDivPanel(panel);
		return panel;
	}

	/**
	 * This method is typically called before any attempt is made to add an
	 * element that should be visible. This typically is achieved by overriding
	 * onTestStarted() calling super.onTestStarted() followed by a call to this
	 * method.
	 */
	protected void addTestNameDivider() {
		final HTML html = new HTML();
		final Element element = html.getElement();
		InlineStyle.setInteger(element, Css.WIDTH, 100, CssUnit.PERCENTAGE);
		InlineStyle.setInteger(element, Css.HEIGHT, 1, CssUnit.EM);
		InlineStyle.setInteger(element, Css.MARGIN, 4, CssUnit.PX);
		InlineStyle.setString(element, Css.COLOR, "black");
		InlineStyle.setString(element, Css.BACKGROUND_COLOR, "skyBlue");
		html.setText(this.getCurrentTestName());
		this.addWidget(html);
	}

	/**
	 * Adds an element to the document.
	 * 
	 * @param element
	 */
	protected void addElement(final Element element) {
		this.addWidget(new ElementWidget(element));
	}

	/**
	 * A simple widget that takes an Element and makes it its own.
	 */
	static class ElementWidget extends SimplePanel {
		public ElementWidget(final Element element) {
			super(element);
		}
	}

	protected void addWidget(final Widget widget) {
		this.getDivPanel().add(widget);
	}

	protected void onTestStarted(final Test test) {
		ObjectHelper.checkNotNull("parameter:test", test);

		final FlexTable table = this.getTable();
		final String testName = test.getName();

		final int row = table.getRowCount();
		table.setText(row, 0, testName);
		table.setWidget(row, 1, new HTML("<span style=\"color: blue;\">starting</span>"));
		table.setText(row, 2, "N/A");
	}

	protected void onTestPassed(final Test test) {
		ObjectHelper.checkNotNull("parameter:test", test);

		final FlexTable table = this.getTable();
		final int row = table.getRowCount() - 1;

		final Widget widget = new HTML("<span style=\"color: green;\">passed</span>");
		table.setWidget(row, 1, widget);

		final long start = test.getStartTimestap();
		final long end = test.getEndTimestap();
		final long timeTaken = end - start;
		table.setText(row, 2, "" + timeTaken);

		this.scrollToBottom();
	}

	protected void onTestFailed(final Test test) {
		ObjectHelper.checkNotNull("parameter:test", test);

		final Throwable cause = test.getThrowable();

		final FlexTable table = this.getTable();
		final int row = table.getRowCount() - 1;

		final HTML html = new HTML();
		html.setHTML("<span style=\"color: red;\">failed</span> " + cause.getMessage());
		html.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				Window.alert(WebPageTestRunner.this.buildFailedTestSummary(test));
			}
		});
		table.setWidget(row, 1, html);

		final long start = test.getStartTimestap();
		final long end = test.getEndTimestap();
		final long timeTaken = end - start;
		table.setText(row, 2, "" + timeTaken);

		this.scrollToBottom();
	}

	protected void scrollToBottom() {
		final Element body = Dom.getBody();
		final int childCount = DOM.getChildCount(body);
		final Element element = DOM.getChild(body, childCount - 1);
		DOM.scrollIntoView(element);
		Dom.setFocus(element);
	}

	protected String buildFailedTestSummary(final Test test) {
		ObjectHelper.checkNotNull("parameter:test", test);

		final StringBuffer buf = new StringBuffer();

		buf.append("TEST\n");
		buf.append(test.getName());
		buf.append("\n");

		final List messages = test.getMessages();
		if (false == messages.isEmpty()) {
			buf.append("\nMESSAGES LOGGED BEFORE FAILURE\n");
			final Iterator messagesIterator = test.getMessages().iterator();
			while (messagesIterator.hasNext()) {
				final String message = (String) messagesIterator.next();
				buf.append(message);
				buf.append("\n");
			}
		}

		buf.append("\nCAUSE\n");
		final Throwable cause = test.getThrowable();
		buf.append(StackTrace.asString(cause));

		return buf.toString();
	}

	protected void onTestAborted(final Test test) {
		ObjectHelper.checkNotNull("parameter:test", test);

		final Throwable cause = test.getThrowable();

		final FlexTable table = this.getTable();
		final int row = table.getRowCount() - 1;

		final HTML html = new HTML();
		html.setHTML("<span style=\"color: red;\">failed</span> " + cause.getMessage());
		html.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				Window.alert(WebPageTestRunner.this.buildFailedTestSummary(test));
			}
		});
		table.setWidget(row, 1, html);

		final long start = test.getStartTimestap();
		final long end = test.getEndTimestap();
		final long timeTaken = end - start;
		table.setText(row, 2, "" + timeTaken);
	}

	protected void onCompletion(final int started, final int passed, final int failed, final int aborted) {
		final int skipped = started - passed - failed - aborted;
		Window.alert("Completed!\nStarted:\t" + started + "\nPassed:\t" + passed + "\nFailed:\t" + failed + "\nAborted:\t" + aborted
				+ "\nSkipped:\t" + skipped);
	}

	/**
	 * A flex table is used to accumulate executed tests along with their
	 * outcomes and time taken.
	 */
	private FlexTable table;

	private FlexTable getTable() {
		ObjectHelper.checkNotNull("field:table", table);
		return this.table;
	}

	private void setTable(final FlexTable table) {
		ObjectHelper.checkNotNull("field:table", table);
		this.table = table;
	}

	private FlexTable createTable() {
		final ZebraFlexTable table = new ZebraFlexTable();
		table.setText(0, 0, "Test name");
		table.setText(0, 1, "Outcome");
		table.setText(0, 2, "Time taken (millis)");
		this.setTable(table);
		return table;
	}
}
