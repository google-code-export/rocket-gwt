/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.test.widget.tabpanel.client;

import java.util.Iterator;

import rocket.client.util.ObjectHelper;
import rocket.client.widget.tab.BottomTabPanel;
import rocket.client.widget.tab.LeftTabPanel;
import rocket.client.widget.tab.RightTabPanel;
import rocket.client.widget.tab.TabListener;
import rocket.client.widget.tab.TabPanel;
import rocket.client.widget.tab.TopTabPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TabPanelTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        try {
            final RootPanel root = RootPanel.get();
            root.add(createFeedback());
            root.add(createTopTabPanelButton());
            root.add(createBottomTabPanelButton());
            root.add(createLeftTabPanelButton());
            root.add(createRightTabPanelButton());

            final Button adder = new Button("Add panel with a randomly generated title.");
            root.add(adder);
            root.add(new HTML("<br/>"));

            final TextBox newTabTitle = new TextBox();
            newTabTitle.addKeyboardListener(new KeyboardListenerAdapter() {
                public void onKeyPress(final Widget widget, final char c, final int modifiers) {
                    if (c == KeyboardListener.KEY_ENTER) {
                        final String title = newTabTitle.getText();
                        if (title.length() == 0) {
                            Window.alert("TabTitle is empty.");
                        } else {
                            getTabPanel().addTab(title, true, new HTML(createContent()));
                            getFeedback().setText("Tab created with a title of [" + title + "]");
                        }
                    }
                }
            });

            root.add(new HTML("Enter the title of a <b>*new*</b> tab and hit enter to create it."));
            root.add(newTabTitle);
            root.add(new HTML("<br/>"));

            adder.addClickListener(new ClickListener() {
                public void onClick(final Widget sender) {
                    final String title = "tab-" + System.currentTimeMillis();
                    getTabPanel().addTab(title, true, new HTML(createContent()));
                    getFeedback().setText("tabPanel.addTab title[" + title + "]");
                }
            });

            final Button createContentIterator = new Button("tabPanel.tabContentsIterator()");
            createContentIterator.addClickListener(new ClickListener() {
                public void onClick(Widget ignored) {
                    TabPanelTest.contentIterator = tabPanel.tabContentsIterator();
                    getFeedback().setText("TabContentsIterator created.");
                }
            });
            root.add(createContentIterator);

            final Button contentIteratorHasNext = new Button("tabContentIterator.hasNext()");
            contentIteratorHasNext.addClickListener(new ClickListener() {
                public void onClick(Widget ignored) {
                    final HTML feedback = getFeedback();
                    try {
                        feedback.setText("contentIterator.hasNext() ->" + contentIterator.hasNext());
                    } catch (Exception caught) {
                        feedback.setText("contentIterator.hasNext() threw " + caught);
                    }
                }
            });
            root.add(contentIteratorHasNext);

            final Button contentIteratorNext = new Button("tabContentIterator.next()");
            contentIteratorNext.addClickListener(new ClickListener() {
                public void onClick(Widget ignored) {
                    final HTML feedback = getFeedback();
                    try {
                        feedback.setText("contentIterator.next() ->" + contentIterator.next());
                    } catch (Exception caught) {
                        feedback.setText("contentIterator.next() threw " + caught);
                    }
                }
            });
            root.add(contentIteratorNext);

            final Button contentIteratorRemoved = new Button("tabContentIterator.remove()");
            contentIteratorRemoved.addClickListener(new ClickListener() {
                public void onClick(Widget ignored) {
                    final HTML feedback = getFeedback();
                    try {
                        final int beforeSelectedIndex = tabPanel.getSelectedTabIndex();
                        contentIterator.remove();
                        final int afterSelectedIndex = tabPanel.getSelectedTabIndex();
                        feedback.setText("contentIterator.remove() -> beforeSelectedIndex: " + beforeSelectedIndex
                                + ", afterSelectedIndex: " + afterSelectedIndex + ", iterator: " + contentIterator);

                    } catch (Exception caught) {
                        feedback.setText("contentIterator.remove() threw " + caught);
                    }
                }
            });
            root.add(contentIteratorRemoved);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * The feedback widget is a recipient for messages.
     */
    HTML feedback;

    HTML getFeedback() {
        return feedback;
    }

    void setFeedback(final HTML feedback) {
        this.feedback = feedback;
    }

    HTML createFeedback() {
        final HTML feedback = new HTML();
        feedback.addStyleName("feedback");
        this.setFeedback(feedback);

        return feedback;
    }

    /**
     * The tabPanel being tested.
     */
    TabPanel tabPanel = null;

    TabPanel getTabPanel() {
        ObjectHelper.checkNotNull("field:tabPanel", tabPanel);
        return tabPanel;
    }

    void setTabPanel(final TabPanel tabPanel) {
        ObjectHelper.checkNotNull("parameter:tabPanel", tabPanel);
        this.tabPanel = tabPanel;

        tabPanel.setCloseButtonImageUrl("close.gif");

        final HTML firstTabContents = new HTML("First<br/>" + createContent());
        firstTabContents.setSize("100%", "100%");

        tabPanel.addTab("First", false, firstTabContents);
        tabPanel.addTab("2222222222222222", true, new HTML(createContent("second tab contents ")));
        tabPanel.addTab("3333333333333333", true, new HTML(createContent("third tab contents ")));
        tabPanel.selectTab(0);

        tabPanel.addTabListener(new TabListener() {
            public boolean onBeforeTabSelected(String title, final Widget widget) {
                return Window.confirm("tabSelected title[" + title + "]\nwidget: " + widget + "\n ? Cancel=vetoes");
            }

            public void onTabSelected(final String title, final Widget widget) {
                getFeedback().setText("tabSelected [" + title + "], widget: " + widget);
            }

            public boolean onBeforeTabClosed(final String title, final Widget widget) {
                return Window.confirm("beforeTabClosed title[" + title + "]\nwidget: " + widget + "\n ? Cancel=vetoes");
            }

            public void onTabClosed(final String title, final Widget widget) {
                getFeedback().setText("tabClosed [" + title + "], widget: " + widget);
            }
        });

        RootPanel.get().add(tabPanel);
    }

    protected Button createTopTabPanelButton() {
        final Button button = new Button("Create TopTabPanel");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                setTabPanel(new TopTabPanel());
            }
        });
        return button;
    }

    protected Button createBottomTabPanelButton() {
        final Button button = new Button("Create BottomTabPanel");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                setTabPanel(new BottomTabPanel());
            }
        });
        return button;
    }

    protected Button createLeftTabPanelButton() {
        final Button button = new Button("Create LeftTabPanel");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                setTabPanel(new LeftTabPanel());
            }
        });
        return button;
    }

    protected Button createRightTabPanelButton() {
        final Button button = new Button("Create RightTabPanel");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                setTabPanel(new RightTabPanel());
            }
        });
        return button;
    }

    static Iterator contentIterator;

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
}