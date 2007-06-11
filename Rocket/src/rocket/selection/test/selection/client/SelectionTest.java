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
import java.util.List;

import rocket.dom.client.DomHelper;
import rocket.selection.client.Selection;
import rocket.selection.client.SelectionEndPoint;
import rocket.selection.client.SelectionHelper;
import rocket.style.client.StyleConstants;
import rocket.testing.client.Test;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.WebPageTestRunner;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This test creates a single button which when clicked initiates a number of tests that tests the functionality of the Selection class.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class SelectionTest extends WebPageTestRunner implements EntryPoint {

    public void onModuleLoad() {
        this.addButton();

    }

    protected void onTestFailed(final Test test) {
        this.skipRemainingTests();
        super.onTestFailed(test);
    }

    protected void addButton() {
        final RootPanel rootPanel = RootPanel.get();

        final Button button = new Button("RunTests");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                SelectionTest.this.executeTests(new SelectionTestBuilder());
            }
        });
        rootPanel.add(button);

        DomHelper.setFocus(button.getElement());
    }

    class SelectionTestBuilder implements TestBuilder {
        public List buildCandidates() {
            final List tests = new ArrayList();

            tests.add(new Test() {
                public String getName() {
                    return "testClearTextSelection";
                }

                public void execute() {
                    SelectionTest.this.testClearTextSelection();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testTextSelectionDisabled";
                }

                public void execute() {
                    SelectionTest.this.testTextSelectionDisabled();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testTextSelectionEnabled";
                }

                public void execute() {
                    SelectionTest.this.testTextSelectionEnabled();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testIsTextSelectionEnabledWithinDisabledParent";
                }

                public void execute() {
                    SelectionTest.this.testIsTextSelectionEnabledWithinDisabledParent();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testIsSelectionEmpty";
                }

                public void execute() {
                    SelectionTest.this.testIsSelectionEmpty();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testSetSelection0";
                }

                public void execute() {
                    SelectionTest.this.testSetSelection0();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testSetSelection1";
                }

                public void execute() {
                    SelectionTest.this.testSetSelection1();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testSetSelection2";
                }

                public void execute() {
                    SelectionTest.this.testSetSelection2();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testGetSelection0";
                }

                public void execute() {
                    SelectionTest.this.testGetSelection0();
                }
            });
            tests.add(new Test() {
                public String getName() {
                    return "testGetSelection1";
                }

                public void execute() {
                    SelectionTest.this.testGetSelection1();
                }
            });
            tests.add(new Test() {
                public String getName() {
                    return "testGetSelection2";
                }

                public void execute() {
                    SelectionTest.this.testGetSelection2();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testExtractSelection";
                }

                public void execute() {
                    SelectionTest.this.testExtractSelection();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testSurroundSelection";
                }

                public void execute() {
                    SelectionTest.this.testSurroundSelection();
                }
            });

            tests.add(new Test() {
                public String getName() {
                    return "testDeleteSelection";
                }

                public void execute() {
                    SelectionTest.this.testDeleteSelection();
                }
            });
            return tests;
        }
    }

    protected void testClearTextSelection() {
        // ask the user to attempt to select some text ?
        final Button clearTextSelection = new Button("Clear Text Selection");
        clearTextSelection.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                final Selection selection = SelectionHelper.getSelection();
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

    protected void testTextSelectionDisabled() {
        // final Selection selection = SelectionHelper.getSelection();
        // selection.clear();
        // selection.setEnabled( false );
        SelectionHelper.disableTextSelection(DomHelper.getBody());

        // ask the user to attempt to select some text ?
        final Button button = new Button("Continue");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                button.removeFromParent();

                final boolean passed = Window
                        .confirm("Was it impossible to select text anywhere within the document ?");
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

    protected void testTextSelectionEnabled() {
        // final Selection selection = SelectionHelper.getSelection();
        // selection.clear();
        // selection.setEnabled( true );
        SelectionHelper.enableTextSelection(DomHelper.getBody());

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

    protected void testIsTextSelectionEnabledWithinDisabledParent() {
        final Selection selection = SelectionHelper.getSelection();
        selection.clear();

        final Element body = DOM.getElementById("body");
        ObjectHelper.checkNotNull("element with an id =\"body\"", body);
        final Element child = DOM.getElementById("child");
        ObjectHelper.checkNotNull("element with an id =\"child\"", child);

        SelectionHelper.disableTextSelection(body);
        SelectionHelper.enableTextSelection(child);

        // ask the user to attempt to select some text ?
        final Button button = new Button("Continue");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                button.removeFromParent();

                SelectionHelper.enableTextSelection(body);
                SelectionHelper.enableTextSelection(child);

                final boolean wasDisabled = Window
                        .confirm("Was it impossible to select text the outside the enabled element (element has a white background)?");
                if (false == wasDisabled) {
                    Test
                            .fail("User confirmed that text selection was still possible even though selection had been disabled for the document.");
                }

                final boolean wasEnabled = Window
                        .confirm("Was it possible to select text the inside the enabled element (element has a gray background)?");
                if (false == wasEnabled) {
                    Test
                            .fail("User confirmed that text selection is not possible even though selection had been enabled for the element.");
                }

                SelectionTest.finishTest();
            }
        });
        RootPanel.get().add(button);

        Window
                .alert("Text selection has been selectively enabled and disabled, enabled text has a gray background whilst disabled text has a white background."
                        + "Try selecting in both areas and then click on CONTINUE...");
        SelectionTest.postponeCurrentTest(60 * 1000);
    }

    protected void testIsTextSelectionEnabled() {
        final Element body = DomHelper.getBody();

        SelectionHelper.enableTextSelection(body);
        Test.assertTrue("selection should be enabled.", SelectionHelper.isEnabled(body));

        SelectionHelper.disableTextSelection(body);
        Test.assertFalse("selection should be disabled.", SelectionHelper.isEnabled(body));

        SelectionHelper.enableTextSelection(body);
        Test.assertTrue("selection should be enabled.", SelectionHelper.isEnabled(body));

        SelectionHelper.disableTextSelection(body);
        Test.assertFalse("selection should be disabled.", SelectionHelper.isEnabled(body));

        SelectionHelper.enableTextSelection(body);
    }

    protected void testIsSelectionEmpty() {
        final Button isTextSelectionEmpty = new Button("Is Text Selection Empty");
        isTextSelectionEmpty.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                final Selection selection = SelectionHelper.getSelection();
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

                final boolean passed = Window
                        .confirm("Did the IS TEXT SELECTION EMPTY button return correct results ?");
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

    protected void testSetSelection0() {
        final Selection selection = SelectionHelper.getSelection();
        selection.clear();

        final String TEXT = "LOREM ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

        final Element div = DOM.createDiv();
        DOM.setStyleAttribute(div, StyleConstants.BACKGROUND_COLOR, "skyblue");
        DOM.setInnerText(div, TEXT);
        DOM.appendChild(DomHelper.getBody(), div);

        final SelectionEndPoint start = new SelectionEndPoint();
        start.setTextNode(this.findFirstTextNode(div));
        start.setOffset(0);
        selection.setStart(start);

        final SelectionEndPoint end = new SelectionEndPoint();
        end.setTextNode(this.findFirstTextNode(div));
        end.setOffset(TEXT.indexOf(" "));
        selection.setEnd(end);

        // ask the user to confirm if the first word of TEXT is selected.
        final boolean passed = Window
                .confirm("Is the first word [LOREM] of the text inside the sky blue area selected ?");
        if (!passed) {
            Test.fail("User confirmed that the programmatic select attempt failed.");
        }
    }

    protected void testSetSelection1() {
        final Selection selection = SelectionHelper.getSelection();
        selection.clear();

        final String HTML = "lorem <b>iPSUM <i>DOLOR <u>SIT<u> AMET,</i> CONSECTETuer</b> adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

        final Element div = DOM.createDiv();
        DOM.setStyleAttribute(div, StyleConstants.BACKGROUND_COLOR, "lightGreen");
        DOM.setInnerHTML(div, HTML);
        DOM.appendChild(DomHelper.getBody(), div);

        final JavaScriptObject startTextNode = this.findTextNode(div, "PSUM");

        final String startTextNodeText = this.getTextNodeText(startTextNode);
        final int startTextNodeOffset = startTextNodeText.indexOf("PSUM");

        final SelectionEndPoint start = new SelectionEndPoint();
        start.setTextNode(startTextNode);
        start.setOffset(startTextNodeOffset);
        selection.setStart(start);

        final JavaScriptObject endTextNode = this.findTextNode(div, "uer");
        final String endTextNodeText = this.getTextNodeText(endTextNode);
        final int endTextNodeOffset = endTextNodeText.indexOf("uer");

        // System.out.println( "endTextNode[" + endTextNode + "] offset: " + endTextNodeOffset );

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

    protected void testSetSelection2() {
        final Selection selection = SelectionHelper.getSelection();
        selection.clear();

        final String HTML = "lorem <b>ipsum <i>doLOR <u>SIT<u> AMET,</i> CONSECTETuer</b> adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

        final Element div = DOM.createDiv();
        DOM.setStyleAttribute(div, StyleConstants.BACKGROUND_COLOR, "yellow");
        DOM.setInnerHTML(div, HTML);
        DOM.appendChild(DomHelper.getBody(), div);

        final Element bold = DomHelper.findFirstChildOfType(div, "B");
        final Element italic = DomHelper.findFirstChildOfType(bold, "I");
        final JavaScriptObject italicTextNode = this.findTextNode(italic, "LOR");
        final int startTextNodeOffset = this.getTextNodeText(italicTextNode).indexOf("LOR");

        // System.out.println( "startTextNode[" + italicTextNode + "] offset: " + startTextNodeOffset );

        final SelectionEndPoint start = new SelectionEndPoint();
        start.setTextNode(italicTextNode);
        start.setOffset(startTextNodeOffset);
        selection.setStart(start);

        final JavaScriptObject endTextNode = this.findTextNode(div, "uer");
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
        return ObjectHelper.getString(textNode, "data");
    }

    protected void testGetSelection0() {
        final Selection selection = SelectionHelper.getSelection();
        selection.clear();

        final String HTML = "LOREM <b>ipsum <i>dolor <u>sit<u> amet,</i> consectetuer</b> adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

        final Element div = DOM.createDiv();
        DOM.setStyleAttribute(div, StyleConstants.BACKGROUND_COLOR, "lightBlue");
        DOM.setInnerHTML(div, HTML);
        DOM.appendChild(DomHelper.getBody(), div);

        final SelectionEndPoint start = new SelectionEndPoint();
        start.setTextNode(this.findFirstTextNode(div));
        start.setOffset(0);
        selection.setStart(start);

        final SelectionEndPoint end = new SelectionEndPoint();
        end.setTextNode(this.findFirstTextNode(div));
        end.setOffset(5);
        selection.setEnd(end);

        final JavaScriptObject textNode = this.getTextNode(div, 0);

        final SelectionEndPoint actualStart = selection.getStart();
        Test.assertEquals(textNode, actualStart.getTextNode());
        Test.assertEquals(0, actualStart.getOffset());

        final SelectionEndPoint actualEnd = selection.getEnd();
        Test.assertEquals(textNode, actualEnd.getTextNode());
        Test.assertEquals(5, actualEnd.getOffset());
    }

    protected void testGetSelection1() {
        final Selection selection = SelectionHelper.getSelection();
        selection.clear();

        final String HTML = "lorum <b>iPSUM <i>DOlor </i>sit</b> amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

        final Element div = DOM.createDiv();
        DOM.setStyleAttribute(div, StyleConstants.BACKGROUND_COLOR, "cyan");
        DOM.setInnerHTML(div, HTML);
        DOM.appendChild(DomHelper.getBody(), div);

        final Element bold = DomHelper.findFirstChildOfType(div, "B");

        final SelectionEndPoint start = new SelectionEndPoint();
        start.setTextNode(this.findFirstTextNode(bold));
        start.setOffset(1);
        selection.setStart(start);

        final Element italic = DomHelper.findFirstChildOfType(bold, "I");

        final SelectionEndPoint end = new SelectionEndPoint();
        end.setTextNode(this.findFirstTextNode(italic));
        end.setOffset(2);
        selection.setEnd(end);

        final JavaScriptObject boldTextNode = this.getTextNode(bold, 0);
        final JavaScriptObject italicTextNode = this.getTextNode(italic, 0);

        final SelectionEndPoint actualStart = selection.getStart();
        Test.assertEquals(boldTextNode, actualStart.getTextNode());
        Test.assertEquals(1, actualStart.getOffset());

        final SelectionEndPoint actualEnd = selection.getEnd();
        Test.assertEquals(italicTextNode, actualEnd.getTextNode());
        Test.assertEquals(2, actualEnd.getOffset());
    }

    protected void testGetSelection2() {
        final Selection selection = SelectionHelper.getSelection();
        selection.clear();

        final String HTML = "lorum <b>ipsum <i>dOLOR </i>SIT</b> AMET, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.";

        final Element div = DOM.createDiv();
        DOM.setStyleAttribute(div, StyleConstants.BACKGROUND_COLOR, "turquoise");
        DOM.setInnerHTML(div, HTML);
        DOM.appendChild(DomHelper.getBody(), div);
        // System.out.println( div );

        final Element bold = DomHelper.findFirstChildOfType(div, "B");
        final Element italic = DomHelper.findFirstChildOfType(bold, "I");
        final JavaScriptObject italicTextNode = this.findFirstTextNode(italic);

        final SelectionEndPoint start = new SelectionEndPoint();
        start.setTextNode(italicTextNode);
        start.setOffset(1);
        selection.setStart(start);

        // System.out.println( div );

        final JavaScriptObject ametTextNode = this.findTextNode(div, "AMET");

        final SelectionEndPoint end = new SelectionEndPoint();
        end.setTextNode(ametTextNode);
        end.setOffset(1);
        selection.setEnd(end);

        final SelectionEndPoint actualStart = selection.getStart();
        Test.assertEquals(italicTextNode, actualStart.getTextNode());
        Test.assertEquals(1, actualStart.getOffset());

        final SelectionEndPoint actualEnd = selection.getEnd();
        Test.assertEquals(ametTextNode, actualEnd.getTextNode());
        Test.assertEquals(1, actualEnd.getOffset());
    }

    protected void testExtractSelection() {
        final Button extractSelection = new Button("Extract Selection");
        extractSelection.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                final Selection selection = SelectionHelper.getSelection();

                final Element element = selection.extract();
                Test.assertNotNull("element", element);

                DOM.appendChild(DomHelper.getBody(), element);

                if (DOM.getInnerHTML(element).length() == 0) {
                    if (false == Window.confirm("The innerHTML of the EXTRACTED element is empty is this correct ?")) {
                        Test.assertNotEquals("element.innerHTML not empty", DOM.getInnerHTML(element).length(), 0);
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

    protected void testDeleteSelection() {
        final Button deleteSelection = new Button("Delete Selection");
        deleteSelection.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                final Selection selection = SelectionHelper.getSelection();
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

        Window
                .alert("Try selecting text and then clicking on the DELETE SELECTION button which should delete the selection...");
        SelectionTest.postponeCurrentTest(60 * 1000);
    }

    protected void testSurroundSelection() {
        final Button surroundSelection = new Button("Surround Selection");
        surroundSelection.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                final Selection selection = SelectionHelper.getSelection();

                final Element element = DOM.createSpan();
                DOM.setStyleAttribute(element, StyleConstants.FONT_SIZE, "larger");
                DOM.setStyleAttribute(element, StyleConstants.BACKGROUND_COLOR, "#eee");
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

    protected JavaScriptObject getTextNode(final Element element, final int index) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return this.getTextNode0(element, index);
    }

    native private JavaScriptObject getTextNode0(final Element element, final int index)/*-{
     return element.childNodes[ index ];
     }-*/;

    protected JavaScriptObject findFirstTextNode(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return this.findFirstTextNode0(element);
    }

    native private JavaScriptObject findFirstTextNode0(final Element element)/*-{
     var textNode = null;
     var childNodes = element.childNodes;
     for( var i = 0; i < childNodes.length; i++ ){
     var childNode = childNodes[ i ];
     
     // found a textNode!
     if( childNode.nodeType == 3 ){
     textNode = childNode;
     break;
     }
     }
     return textNode;
     }-*/;

    protected JavaScriptObject findTextNode(final Element element, final String searchText) {
        ObjectHelper.checkNotNull("parameter:element", element);
        StringHelper.checkNotEmpty("parameter:searchText", searchText);
        final JavaScriptObject textNode = this.findTextNode0(element, searchText);
        if (textNode == null) {
            throw new RuntimeException("Unable to find a textNode that is a child of element with the text ["
                    + searchText + "], element: " + DOM.getInnerText(element));
        }
        return textNode;
    }

    native protected JavaScriptObject findTextNode0(final Element element, final String text)/*-{    
     var textNode = null;
     var childNodes = element.childNodes;
     
     for( var i = 0; i < childNodes.length; i++ ){
     var node = childNodes[ i ];                
     var type = node.nodeType;
     
     // if node is an element...
     if( type == 1 ){
     textNode = this.@rocket.selection.test.selection.client.SelectionTest::findTextNode0(Lcom/google/gwt/user/client/Element;Ljava/lang/String;)(node,text);
     
     // stop searching if a textNode was found...
     if( null != textNode ){
     break;
     }
     // otherwise continue searching...
     continue;
     }
     
     if( node.nodeType == 3 ){
     if( node.data.indexOf( text ) != -1 ){
     textNode = node;
     break;
     }
     }
     }        
     
     return textNode;
     }-*/;

}
