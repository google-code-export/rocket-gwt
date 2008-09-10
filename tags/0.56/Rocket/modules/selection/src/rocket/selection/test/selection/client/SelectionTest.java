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
package rocket.selection.test.selection.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rocket.dom.client.Dom;
import rocket.selection.client.Selection;
import rocket.selection.client.SelectionEndPoint;
import rocket.style.client.Css;
import rocket.style.client.InlineStyle;
import rocket.testing.client.Test;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.WebPageTestRunner;
import rocket.util.client.Checker;
import rocket.util.client.JavaScript;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Text;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This test creates a single button which when clicked initiates a number of
 * tests that tests the functionality of the Selection class.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class SelectionTest extends WebPageTestRunner implements EntryPoint {

	public void onModuleLoad() {
		this.addButton();

	}

	protected void onTestFailed(final Test test) {
		super.onTestFailed(test);
	}

	protected void addButton() {
		final RootPanel rootPanel = RootPanel.get();

		final Button button = new Button("RunTests");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				SelectionTest.this.executeTests((TestBuilder) GWT.create(TestMethodFinder.class));
			}
		});
		rootPanel.add(button);

		button.setFocus(true);
	}

	static interface TestMethodFinder extends TestBuilder {
		/**
		 * @testing-testRunner rocket.selection.test.selection.client.SelectionTest
		 */
		abstract public List buildCandidates();
	}

	/**
	 * @testing-testMethodOrder 0
	 */
	public void testClearTextSelection() {
		// ask the user to attempt to select some text ?
		final Button clearTextSelection = new Button("Clear Text Selection");
		clearTextSelection.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final Selection selection = Selection.getSelection();
				selection.clear();
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		rootPanel.add(clearTextSelection);

		// ask the user to attempt to select some text ?
		final Button continueButton = new Button("Continue");
		continueButton.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				continueButton.removeFromParent();
				clearTextSelection.removeFromParent();

				final boolean passed = Window.confirm("Did the CLEAR TEXT SELECTION button work as expected ?");
				if (false == passed) {
					Test.fail("User confirmed that text selection clearing did not work.");
				}

				SelectionTest.finishTest();
			}
		});
		rootPanel.add(continueButton);

		Window.alert("Select some text using the mouse and then click on the CLEAR button to clear the selection...");
		SelectionTest.postponeCurrentTest(60 * 1000);
	}

	/**
	 * @testing-testMethodOrder 1
	 */
	public void testTextSelectionDisabled() {
		Selection.disableTextSelection();

		// ask the user to attempt to select some text ?
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				button.removeFromParent();

				final boolean passed = Window.confirm("Was it impossible to select text anywhere within the document ?");
				if (false == passed) {
					Test
							.fail("User confirmed that text selection was still possible even though selection had been disabled for the document.");
				}

				SelectionTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("Text selection has been disabled, try and select text anywhere and then click on CONTINUE...");
		SelectionTest.postponeCurrentTest(60 * 1000);
	}

	/**
	 * @testing-testMethodOrder 2
	 */
	public void testTextSelectionEnabled() {
		Selection.enableTextSelection();

		// ask the user to attempt to select some text ?
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				button.removeFromParent();

				final boolean passed = Window.confirm("Was it possible to select text anywhere within the document ?");
				if (false == passed) {
					Test
							.fail("User confirmed that text selection was NOT still possible even though selection had been enabled for the document.");
				}
				SelectionTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("Text selection has been enabled, try and select text anywhere and then click on CONTINUE...");
		SelectionTest.postponeCurrentTest(60 * 1000);
	}

	/**
	 * @testing-testMethodOrder 3
	 */
	public void testIsTextSelectionEnabled() {
		final Element body = RootPanel.getBodyElement();

		Selection.enableTextSelection();
		Test.assertTrue("selection should be enabled.", Selection.isEnabled(body));

		Selection.disableTextSelection();
		Test.assertFalse("selection should be disabled.", Selection.isEnabled(body));

		Selection.enableTextSelection();
		Test.assertTrue("selection should be enabled.", Selection.isEnabled(body));

		Selection.disableTextSelection();
		Test.assertFalse("selection should be disabled.", Selection.isEnabled(body));

		Selection.enableTextSelection();
	}

	/**
	 * @testing-testMethodOrder 4
	 */
	public void testIsSelectionEmpty() {
		final Button isTextSelectionEmpty = new Button("Is Text Selection Empty");
		isTextSelectionEmpty.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final Selection selection = Selection.getSelection();
				Window.alert("Text selection is empty: " + selection.isEmpty());
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		rootPanel.add(isTextSelectionEmpty);

		// ask the user to attempt to select some text ?
		final Button continueButton = new Button("Continue");
		continueButton.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				continueButton.removeFromParent();
				isTextSelectionEmpty.removeFromParent();

				final boolean passed = Window.confirm("Did the IS TEXT SELECTION EMPTY button return correct results ?");
				if (false == passed) {
					Test.fail("User confirmed that Selection.isEmpty() did not return correct results.");
				}

				SelectionTest.finishTest();
			}
		});
		rootPanel.add(continueButton);

		Window
				.alert("Try selecting and not selecting text and confirm that the results reported by the IS TEXT SELECTION EMPTY button are correct...");
		SelectionTest.postponeCurrentTest(60 * 1000);
	}

	/**
	 * @testing-testMethodOrder 5
	 */
	public void testSetSelection0() {
		final Selection selection = Selection.getSelection();
		selection.clear();

		final String TEXT = "LOREM ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

		final Element div = DOM.createDiv();
		InlineStyle.getInlineStyle( div ).setString( Css.BACKGROUND_COLOR, "skyblue");
		DOM.setInnerText(div, TEXT);
		RootPanel.getBodyElement().appendChild(div);

		final SelectionEndPoint start = new SelectionEndPoint();
		start.setTextNode(this.findFirstTextNode(div));
		start.setOffset(0);
		selection.setStart(start);

		final SelectionEndPoint end = new SelectionEndPoint();
		end.setTextNode(this.findFirstTextNode(div));
		end.setOffset(TEXT.indexOf(" "));
		selection.setEnd(end);

		// ask the user to confirm if the first word of TEXT is selected.
		final boolean passed = Window.confirm("Is the first word [LOREM] of the text inside the sky blue area selected ?");
		if (!passed) {
			Test.fail("User confirmed that the programmatic select attempt failed.");
		}
	}

	/**
	 * @testing-testMethodOrder 6
	 */
	public void testSetSelection1() {
		final Selection selection = Selection.getSelection();
		selection.clear();

		final String HTML = "lorem <b>iPSUM <i>DOLOR <u>SIT<u> AMET,</i> CONSECTETuer</b> adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

		final Element div = DOM.createDiv();
		InlineStyle.getInlineStyle( div ).setString( Css.BACKGROUND_COLOR, "lightGreen");
		div.setInnerHTML(HTML);
		RootPanel.getBodyElement().appendChild(div);

		final Text startTextNode = this.findTextNode(div, "PSUM");

		final String startTextNodeText = this.getTextNodeText(startTextNode);
		final int startTextNodeOffset = startTextNodeText.indexOf("PSUM");

		final SelectionEndPoint start = new SelectionEndPoint();
		start.setTextNode(startTextNode);
		start.setOffset(startTextNodeOffset);
		selection.setStart(start);

		final Text endTextNode = this.findTextNode(div, "uer");
		final String endTextNodeText = this.getTextNodeText(endTextNode);
		final int endTextNodeOffset = endTextNodeText.indexOf("uer");

		final SelectionEndPoint end = new SelectionEndPoint();
		end.setTextNode(endTextNode);
		end.setOffset(endTextNodeOffset);
		selection.setEnd(end);

		// ask the user to confirm if the first word of TEXT is selected.
		final boolean passed = Window.confirm("Within the light green text are only capital letters selected ?");
		if (!passed) {
			Test.fail("User confirmed that the programmatic select attempt failed.");
		}
	}

	/**
	 * @testing-testMethodOrder 7
	 */
	public void testSetSelection2() {
		final Selection selection = Selection.getSelection();
		selection.clear();

		final String HTML = "lorem <b>ipsum <i>doLOR <u>SIT<u> AMET,</i> CONSECTETuer</b> adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

		final Element div = DOM.createDiv();
		InlineStyle.getInlineStyle( div ).setString( Css.BACKGROUND_COLOR, "yellow");
		div.setInnerHTML(HTML);
		RootPanel.getBodyElement().appendChild(div);

		final Element bold = SelectionTest.findFirstChild(div, "B");
		final Element italic = SelectionTest.findFirstChild(bold, "I");
		final Text italicTextNode = this.findTextNode(italic, "LOR");
		final int startTextNodeOffset = this.getTextNodeText(italicTextNode).indexOf("LOR");

		final SelectionEndPoint start = new SelectionEndPoint();
		start.setTextNode(italicTextNode);
		start.setOffset(startTextNodeOffset);
		selection.setStart(start);

		final Text endTextNode = this.findTextNode(div, "uer");
		final String endTextNodeText = this.getTextNodeText(endTextNode);
		final int endTextNodeOffset = endTextNodeText.indexOf("uer");

		final SelectionEndPoint end = new SelectionEndPoint();
		end.setTextNode(endTextNode);
		end.setOffset(endTextNodeOffset);
		selection.setEnd(end);

		// ask the user to confirm if the first word of TEXT is selected.
		final boolean passed = Window.confirm("Within the yellow text are only capital letters selected ?");
		if (!passed) {
			Test.fail("User confirmed that the programmatic select attempt failed.");
		}
	}

	protected String getTextNodeText(final JavaScriptObject textNode) {
		return JavaScript.getString(textNode, "data");
	}

	/**
	 * @testing-testMethodOrder 8
	 */
	public void testGetSelection0() {
		final Selection selection = Selection.getSelection();
		selection.clear();

		final String HTML = "LOREM <b>ipsum <i>dolor <u>sit<u> amet,</i> consectetuer</b> adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

		final Element div = DOM.createDiv();
		InlineStyle.getInlineStyle( div ).setString( Css.BACKGROUND_COLOR, "lightBlue");
		div.setInnerHTML(HTML);
		RootPanel.getBodyElement().appendChild(div);

		final SelectionEndPoint start = new SelectionEndPoint();
		start.setTextNode(this.findFirstTextNode(div));
		start.setOffset(0);
		selection.setStart(start);

		final SelectionEndPoint end = new SelectionEndPoint();
		end.setTextNode(this.findFirstTextNode(div));
		end.setOffset(5);
		selection.setEnd(end);

		final Text textNode = this.getTextNode(div, 0);

		final SelectionEndPoint actualStart = selection.getStart();
		Test.assertSame(textNode, actualStart.getTextNode());
		Test.assertEquals(0, actualStart.getOffset());

		final SelectionEndPoint actualEnd = selection.getEnd();
		Test.assertSame(textNode, actualEnd.getTextNode());
		Test.assertEquals(5, actualEnd.getOffset());
	}

	/**
	 * @testing-testMethodOrder 9
	 */
	public void testGetSelection1() {
		final Selection selection = Selection.getSelection();
		selection.clear();

		final String HTML = "lorum <b>iPSUM <i>DOlor </i>sit</b> amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

		final Element div = DOM.createDiv();
		InlineStyle.getInlineStyle( div ).setString( Css.BACKGROUND_COLOR, "cyan");
		div.setInnerHTML(HTML);
		RootPanel.getBodyElement().appendChild(div);

		final Element bold = SelectionTest.findFirstChild(div, "B");

		final SelectionEndPoint start = new SelectionEndPoint();
		start.setTextNode(this.findFirstTextNode(bold));
		start.setOffset(1);
		selection.setStart(start);

		final Element italic = SelectionTest.findFirstChild(bold, "I");

		final SelectionEndPoint end = new SelectionEndPoint();
		end.setTextNode(this.findFirstTextNode(italic));
		end.setOffset(2);
		selection.setEnd(end);

		final Text boldTextNode = this.getTextNode(bold, 0);
		final Text italicTextNode = this.getTextNode(italic, 0);

		final SelectionEndPoint actualStart = selection.getStart();
		Test.assertSame(boldTextNode, actualStart.getTextNode());
		Test.assertEquals(1, actualStart.getOffset());

		final SelectionEndPoint actualEnd = selection.getEnd();
		Test.assertSame(italicTextNode, actualEnd.getTextNode());
		Test.assertEquals(2, actualEnd.getOffset());
	}

	/**
	 * @testing-testMethodOrder 10
	 */
	public void testGetSelection2() {
		final Selection selection = Selection.getSelection();
		selection.clear();

		final String HTML = "lorum <b>ipsum <i>dOLOR </i>SIT</b> AMET, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

		final Element div = DOM.createDiv();
		InlineStyle.getInlineStyle( div ).setString( Css.BACKGROUND_COLOR, "turquoise");
		div.setInnerHTML(HTML);
		RootPanel.getBodyElement().appendChild(div);

		final Element bold = SelectionTest.findFirstChild(div, "B");
		final Element italic = SelectionTest.findFirstChild(bold, "I");
		final Text italicTextNode = this.findFirstTextNode(italic);

		final SelectionEndPoint start = new SelectionEndPoint();
		start.setTextNode(italicTextNode);
		start.setOffset(1);
		selection.setStart(start);

		final Text ametTextNode = this.findTextNode(div, "AMET");

		final SelectionEndPoint end = new SelectionEndPoint();
		end.setTextNode(ametTextNode);
		end.setOffset(1);
		selection.setEnd(end);

		final SelectionEndPoint actualStart = selection.getStart();
		Test.assertSame(italicTextNode, actualStart.getTextNode());
		Test.assertEquals(1, actualStart.getOffset());

		final SelectionEndPoint actualEnd = selection.getEnd();
		Test.assertSame(ametTextNode, actualEnd.getTextNode());
		Test.assertEquals(1, actualEnd.getOffset());
	}

	/**
	 * @testing-testMethodOrder 11
	 */
	public void testExtractSelection() {
		final Button extractSelection = new Button("Extract Selection");
		extractSelection.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final Selection selection = Selection.getSelection();

				final Element element = selection.extract();
				Test.assertNotNull("element", element);

				RootPanel.getBodyElement().appendChild(element);

				if (element.getInnerHTML().length() == 0) {
					if (false == Window.confirm("The innerHTML of the EXTRACTED element is empty is this correct ?")) {
						Test.assertNotEquals("element.innerHTML not empty", element.getInnerHTML().length(), 0);
					}
				}
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		rootPanel.add(extractSelection);

		// ask the user to attempt to select some text ?
		final Button continueButton = new Button("Continue");
		continueButton.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				continueButton.removeFromParent();
				extractSelection.removeFromParent();

				final boolean passed = Window
						.confirm("Did the EXTRACT button work correctly and extract the selection and append it to the bottom of the document ?");
				if (false == passed) {
					Test.fail("User confirmed that Selection.extract() did not work correctly.");
				}

				SelectionTest.finishTest();
			}
		});
		rootPanel.add(continueButton);

		Window
				.alert("Try selecting text and then clicking on the EXTRACT button to extract the selection and append it to the bottom of the document...");
		SelectionTest.postponeCurrentTest(60 * 1000);
	}

	/**
	 * @testing-testMethodOrder 12
	 */
	public void testSurroundSelection() {
		final Button surroundSelection = new Button("Surround Selection");
		surroundSelection.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final Selection selection = Selection.getSelection();

				final Element element = DOM.createSpan();
				final InlineStyle inlineStyle = InlineStyle.getInlineStyle(element);
				inlineStyle.setString(Css.FONT_SIZE, "larger");
				inlineStyle.setString(Css.BACKGROUND_COLOR, "#eee");
				selection.surround(element);
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		rootPanel.add(surroundSelection);

		// ask the user to attempt to select some text ?
		final Button continueButton = new Button("Continue");
		continueButton.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				continueButton.removeFromParent();
				surroundSelection.removeFromParent();

				final boolean passed = Window
						.confirm("Did the SURROUND button work correctly and make the selected text larger and with a light gray background?");
				if (false == passed) {
					Test.fail("User confirmed that Selection.surround() did not work correctly.");
				}

				SelectionTest.finishTest();
			}
		});
		rootPanel.add(continueButton);

		Window
				.alert("Try selecting text and then clicking on the SURROUND button to surround the selection inside a span which makes the text larger and gives it a light gray background...");
		SelectionTest.postponeCurrentTest(60 * 1000);
	}

	/**
	 * @testing-testMethodOrder 13
	 */
	public void testDeleteSelection() {
		final Button deleteSelection = new Button("Delete Selection");
		deleteSelection.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final Selection selection = Selection.getSelection();
				selection.delete();
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		rootPanel.add(deleteSelection);

		// ask the user to attempt to select some text ?
		final Button continueButton = new Button("Continue");
		continueButton.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				continueButton.removeFromParent();
				deleteSelection.removeFromParent();

				final boolean passed = Window.confirm("Did the DELETE SELECTION button work correctly ?");
				if (false == passed) {
					Test.fail("User confirmed that Selection.delete() did not work correctly.");
				}

				SelectionTest.finishTest();
			}
		});
		rootPanel.add(continueButton);

		Window.alert("Try selecting text and then clicking on the DELETE SELECTION button which should delete the selection...");
		SelectionTest.postponeCurrentTest(60 * 1000);
	}

	protected Text getTextNode(final Element element, final int index) {
		Checker.notNull("parameter:element", element);

		return element.getChildNodes().getItem(index).cast();
	}

	protected Text findFirstTextNode(final Element element) {
		Checker.notNull("parameter:element", element);

		final NodeList<Node> children = element.getChildNodes();
		Text text = null;

		for (int i = 0; i < children.getLength(); i++) {
			final Node node = children.getItem(i);
			final int type = node.getNodeType();

			if (type == Node.TEXT_NODE) {
				text = (Text) node.cast();
				break;
			}
		}
		Checker.notNull("Unable to find a text node child of " + element.toString(), text);
		return text;
	}

	protected Text findTextNode(final Element element, final String searchText) {
		Checker.notNull("parameter:element", element);
		Checker.notEmpty("parameter:searchText", searchText);

		final Text text = this.findTextNode0(element, searchText);
		Checker.notNull(searchText, text);
		return text;
	}

	protected Text findTextNode0(final Element element, final String searchText) {
		final NodeList<Node> children = element.getChildNodes();
		Text text = null;

		for (int i = 0; i < children.getLength(); i++) {
			final Node node = children.getItem(i);
			final int type = node.getNodeType();

			if (type == Node.ELEMENT_NODE) {
				text = this.findTextNode0((Element) node.cast(), searchText);
				if (null != text) {
					break;
				}
			}

			if (type == Node.TEXT_NODE) {
				final Text test = (Text) node.cast();
				if (test.getData().indexOf(searchText) != -1) {
					text = test;
					break;
				}
			}
		}
		return text;
	}

	native static Element getParent(final JavaScriptObject object)/*-{
					 return object.parentNode || object.parentElement || null;
					 }-*/;

	public static Element findFirstChild(final Element parent, final String tagName) {
		Checker.notNull("parameter:parent", parent);
		Checker.notEmpty("parameter:tagName", tagName);

		return findImmediateChildren(parent, tagName).get(0);
	}

	/**
	 * Creates a list and populates it with all immediate child elements of the
	 * given element.
	 * 
	 * @param parent
	 * @param tagName
	 * @return A read only list of Elements.
	 */
	public static List<Element> findImmediateChildren(final Element parent, final String tagName) {
		Checker.notNull("parameter:parent", parent);
		Checker.notEmpty("parameter:tagName", tagName);

		final List<Element> found = new ArrayList<Element>();
		final NodeList<Node> children = parent.getChildNodes();
		final int childCount = children.getLength();
		for (int i = 0; i < childCount; i++) {
			final Node child = children.getItem(i).cast();
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				final Element element = child.cast();
				if (Dom.isTag(element, tagName)) {
					found.add(element);
				}
			}
		}
		return Collections.unmodifiableList(found);
	}
}