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
import rocket.widget.client.accordion.AccordionListener;
import rocket.widget.client.accordion.AccordionPanel;
import rocket.widget.client.accordion.LeftSideAccordionPanel;
import rocket.widget.client.accordion.RightSideAccordionPanel;
import rocket.widget.client.accordion.VerticalAccordionPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
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
        rootPanel.add(createVerticalAccordionPanelButton());
        rootPanel.add(createLeftSideAccordionPanelButton());
        rootPanel.add(createRightSideAccordionPanelButton());
    }

    protected Button createRightSideAccordionPanelButton() {
        final Button button = new Button("Create RightSideAccordionPanel");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                final AccordionPanel panel = new RightSideAccordionPanel();
                completeAccordionPanel(panel);
                RootPanel.get().add(panel);
            }
        });
        return button;
    }

    protected Button createLeftSideAccordionPanelButton() {
        final Button button = new Button("Create LeftSideAccordionPanel");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                final AccordionPanel panel = new LeftSideAccordionPanel();
                completeAccordionPanel(panel);
                RootPanel.get().add(panel);
            }
        });
        return button;
    }

    protected Button createVerticalAccordionPanelButton() {
        final Button button = new Button("Create VerticalAccordionPanel");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                final AccordionPanel panel = new VerticalAccordionPanel();
                completeAccordionPanel(panel);
                RootPanel.get().add(panel);
            }
        });
        return button;
    }

    /**
     * Adds a accordionListener and creates a InteractiveList control enabling manipulation of the AccordionPanel
     * 
     * @param accordionPanel
     */
    protected void completeAccordionPanel(final AccordionPanel accordionPanel) {
        ObjectHelper.checkNotNull("parameter:accordionPanel", accordionPanel);

        final AccordionItem item = new AccordionItem();
        item.setCaption("AccordionItem");
        item.setContent(new HTML(AccordionPanelTest.createContent()));
        accordionPanel.add(item);
        accordionPanel.select(0);

        final InterativeList control = new InterativeList();
        control.setAccordionPanel(accordionPanel);
        RootPanel.get().add(control);

        accordionPanel.addAccordionListener(new AccordionListener() {
            public boolean onBeforeItemSelected(final AccordionItem item) {
                final String caption = item.getCaption();
                final Widget content = item.getContent();
                return Window.confirm("accordionSelected caption[" + caption + "]\ncontent: " + content
                        + "\n ? Cancel=vetoes");
            }

            public void onItemSelected(final AccordionItem item) {
                final String caption = item.getCaption();
                final HTML content = (HTML) item.getContent();
                control.addMessage("accordionSelected caption[" + caption + "]" + content.getText().substring(0, 50));
            }
        });
    }

    final static String createContent() {
        final StringBuffer buf = new StringBuffer();

        buf.append("<b>");
        buf.append(new Date());
        buf.append("</b> ");

        for (int i = 0; i < 1000; i++) {
            if ((i % 128) == 0) {
                buf.append("<br/>");
            }
            final char c = (char) (32 + (Random.nextInt() & 95));
            if (c == '<' || c == '>' || c == '&') {
                continue;
            }
            buf.append(c);
        }
        return buf.toString();
    }

    final static String createContent(final String text) {
        final StringBuffer buf = new StringBuffer();

        buf.append("<b>");
        buf.append(new Date());
        buf.append("</b> ");

        for (int i = 0; i < 20; i++) {
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
            item.setContent(new HTML(AccordionPanelTest.createContent()));
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

        public void addMessage(final String message) {
            super.addMessage(message);
        }
    }

}