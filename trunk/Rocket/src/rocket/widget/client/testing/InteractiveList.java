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
package rocket.widget.client.testing;

import java.util.Iterator;

import rocket.browser.client.BrowserHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget compromises a panel with clickable buttons which in turn let a user test all the features of a List. It is not meant to be
 * used within applications but rather is purely provided for testing purposes.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class InteractiveList extends Composite {

    public InteractiveList() {
        this.initWidget(this.createWidget());
        this.setStyleName(TestConstants.INTERACTIVE_LIST_STYLE);
    }

    protected Widget createWidget() {
        final VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(this.createClassNameLabel());
        verticalPanel.add(this.createMessages());
        verticalPanel.add(this.createButtons());
        return verticalPanel;
    }

    protected Label createClassNameLabel() {
        return new Label(this.getCollectionTypeName());
    }

    protected abstract String getCollectionTypeName();

    protected FlowPanel createButtons() {
        final FlowPanel panel = new FlowPanel();
        panel.add(this.createListSizeButton());
        panel.add(this.createListIsEmptyButton());
        panel.add(this.createListAddButton());
        panel.add(this.createListInsertButton());
        panel.add(this.createListGetButton());
        panel.add(this.createListRemoveButton());
        panel.add(this.createListSetButton());
        panel.add(this.createListIteratorButton());
        panel.add(this.createIteratorHasNextButton());
        panel.add(this.createIteratorNextButton());
        panel.add(this.createIteratorRemoveButton());
        return panel;
    }

    protected Button createListSizeButton() {
        final Button button = new Button("list.size()");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onListSizeClick();
            }
        });
        return button;
    }

    protected void onListSizeClick() {
        String message = "list.size() ";
        int size = -1;
        try {
            size = getListSize();
            message = message + "returned " + size;
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + "threw " + GWT.getTypeName(caught) + ", message[" + caught.getMessage() + "]";
        }
        this.addMessage(message);
    }

    /**
     * Sub-classes must delegate to the list implementation and fetch the list's size.
     * 
     * @return
     */
    protected abstract int getListSize();

    protected Button createListIsEmptyButton() {
        final Button button = new Button("list.isEmpty()");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onListIsEmptyClick();
            }
        });
        return button;
    }

    protected void onListIsEmptyClick() {
        String message = "list.isEmpty() ";
        boolean empty = false;
        try {
            empty = this.getListIsEmpty();
            message = message + "returned " + empty;
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + " threw " + GWT.getTypeName(caught) + " with a message of [" + caught.getMessage()
                    + "]";
        }
        this.addMessage(message);
    }

    /**
     * Sub-classes must delegate to the list implementation and test if the list is empty.
     * 
     * @return
     */
    protected abstract boolean getListIsEmpty();

    protected Button createListAddButton() {
        final Button button = new Button("list.add()");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onListAddClick();
            }
        });
        return button;
    }

    protected void onListAddClick() {
        String message = "list.add(";
        Object element = null;
        boolean added = false;
        try {

            element = this.createElement();
            added = this.listAdd(element);
            message = message + this.toString(element) + ") returned " + added;
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + this.toString(element) + ") threw " + GWT.getTypeName(caught) + ", message["
                    + caught.getMessage() + "]";
        }
        this.addMessage(message);
    }

    /**
     * Sub-classes must delegate to the list implementation and add the given element
     */
    protected abstract boolean listAdd(Object element);

    protected Button createListInsertButton() {
        final Button button = new Button("list.add(int)");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onListInsertClick();
            }
        });
        return button;
    }

    protected void onListInsertClick() {
        String message = "list.add(";
        int index = -1;
        Object element = null;
        try {
            index = Integer.parseInt(BrowserHelper.prompt("add index", "0"));
            element = this.createElement();
            this.listInsert(index, element);
            message = message + index + ", " + this.toString(element) + ") returned";
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + index + "," + this.toString(element) + ") threw " + GWT.getTypeName(caught)
                    + ", message[" + caught.getMessage() + "]";
        }
        this.addMessage(message);
    }

    /**
     * Sub-classes must delegate to the list implementation and insert the given element at the given slot
     */
    protected abstract void listInsert(int index, Object element);

    protected Button createListGetButton() {
        final Button button = new Button("list.get()");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onListGetClick();
            }
        });
        return button;
    }

    protected void onListGetClick() {
        String message = "list.get(";
        int index = -1;
        try {
            index = Integer.parseInt(BrowserHelper.prompt("get index", "0"));
            final Object element = this.listGet(index);
            this.checkType(element);
            message = message + index + ") returned " + this.toString(element);
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + index + ") threw " + GWT.getTypeName(caught) + ", message[" + caught.getMessage() + "]";
        }
        this.addMessage(message);
    }

    /**
     * Sub-classes must delegate to the list implementation and get the element at the given slot.
     */
    protected abstract Object listGet(int index);

    protected Button createListRemoveButton() {
        final Button button = new Button("list.remove()");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onListRemoveClick();
            }
        });
        return button;
    }

    protected void onListRemoveClick() {
        String message = "list.remove(";
        int index = -1;
        try {
            index = Integer.parseInt(BrowserHelper.prompt("remove index", "0"));
            final Object element = this.listRemove(index);
            this.checkType(element);
            message = message + index + ") returned " + this.toString(element);
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + index + ") threw " + GWT.getTypeName(caught) + ", message[" + caught.getMessage() + "]";
        }
        this.addMessage(message);
    }

    /**
     * Sub-classes must delegate to the list implementation and remove the element at the given slot.
     */
    protected abstract Object listRemove(int index);

    protected Button createListSetButton() {
        final Button button = new Button("list.set()");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onListSetClick();
            }
        });
        return button;
    }

    protected void onListSetClick() {
        String message = "list.set(";
        int index = -1;
        Object element = null;
        try {
            index = Integer.parseInt(BrowserHelper.prompt("set index", "0"));
            element = this.createElement();
            final Object previous = this.listSet(index, element);
            this.checkType(previous);
            message = message + index + ", " + this.toString(element) + ") returned " + this.toString(previous);
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + index + "," + this.toString(element) + ") threw " + GWT.getTypeName(caught)
                    + ", message[" + caught.getMessage() + "]";
        }
        this.addMessage(message);
    }

    /**
     * Sub-classes must delegate to the list implementation and set the element at the given slot.
     */
    protected abstract Object listSet(int index, Object element);

    /**
     * Sub-classes must create a new element whenever this factory method is called.
     * 
     * @return
     */
    protected abstract Object createElement();

    protected Button createListIteratorButton() {
        final Button button = new Button("list.iterator()");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onListIteratorClick();
            }
        });
        return button;
    }

    protected void onListIteratorClick() {
        String message = "list.iterator()";
        Iterator iterator = null;
        try {
            iterator = this.listIterator();
            this.setIterator(iterator);
            message = message + " returned " + iterator;
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + " threw " + GWT.getTypeName(caught) + " with a message of [" + caught.getMessage()
                    + "]";
        }
        this.addMessage(message);
    }

    /**
     * Sub-classes must delegate to the list implementation and fetch the iterator
     */
    protected abstract Iterator listIterator();

    protected Button createIteratorHasNextButton() {
        final Button button = new Button("iterator.hasNext()");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onIteratorHasNextClick();
            }
        });
        return button;
    }

    protected void onIteratorHasNextClick() {
        String message = "iterator.hasNext()";
        try {
            final boolean hasNext = this.getIterator().hasNext();
            message = message + " returned " + hasNext;
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + " threw " + GWT.getTypeName(caught) + " with a message of [" + caught.getMessage()
                    + "]";
        }
        this.addMessage(message);
    }

    protected Button createIteratorNextButton() {
        final Button button = new Button("iterator.next()");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onIteratorNextClick();
            }
        });
        return button;
    }

    protected void onIteratorNextClick() {
        String message = "iterator.next()";
        try {
            final Object element = this.getIterator().next();
            this.checkType(element);
            message = message + " returned " + this.toString(element);
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + " threw " + GWT.getTypeName(caught) + " with a message of [" + caught.getMessage()
                    + "]";
        }
        this.addMessage(message);
    }

    protected Button createIteratorRemoveButton() {
        final Button button = new Button("iterator.remove()");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                onIteratorRemoveClick();
            }
        });
        return button;
    }

    protected void onIteratorRemoveClick() {
        String message = "iterator.remove()";
        try {
            this.getIterator().remove();
            message = message + " returned";
        } catch (final Exception caught) {
            caught.printStackTrace();
            message = message + " threw " + GWT.getTypeName(caught) + " with a message of [" + caught.getMessage()
                    + "]";
        }
        this.addMessage(message);
    }

    /**
     * Sub-classes should implement check that the given element is valid for this list.
     * 
     * @param element
     */
    protected abstract void checkType(Object element);

    /**
     * Contains the iterator being iterated over.
     */
    private Iterator iterator;

    protected Iterator getIterator() {
        ObjectHelper.checkNotNull("field:iterator", iterator);
        return this.iterator;
    }

    protected boolean hasIterator() {
        return null != this.iterator;
    }

    protected void setIterator(final Iterator iterator) {
        ObjectHelper.checkNotNull("parameter:iterator", iterator);
        this.iterator = iterator;
    }

    /**
     * This panel is used to display messages returned as part of the users using the widget.
     */
    private VerticalPanel messages;

    public VerticalPanel getMessages() {
        ObjectHelper.checkNotNull("field:messages", messages);
        return this.messages;
    }

    public void setMessages(final VerticalPanel messages) {
        ObjectHelper.checkNotNull("parameter:messages", messages);
        this.messages = messages;
    }

    protected VerticalPanel createMessages() {
        final VerticalPanel panel = new VerticalPanel();
        panel.setWidth("100%");
        DOM.setStyleAttribute(panel.getElement(), "white-space", "nowrap");
        this.setMessages(panel);
        panel.setStyleName(TestConstants.INTERACTIVE_LIST_WIDGET_LOG_STYLE);
        return panel;
    }

    protected abstract int getMessageLineCount();

    protected void addMessage(final String message) {
        StringHelper.checkNotEmpty("parameter:message", message);
        final VerticalPanel messages = this.getMessages();
        messages.add(new HTML(message));

        // if the messages list box is full remove the oldest message...
        if (messages.getWidgetCount() >= this.getMessageLineCount()) {
            messages.remove(0);
        }
    }

    protected abstract String toString(Object element);
}
