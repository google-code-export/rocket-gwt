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

import rocket.browser.client.Browser;
import rocket.util.client.Checker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget compromises a panel with clickable buttons which in turn let a
 * user test all the features of a panel. It is not meant to be used within
 * applications but rather is purely provided for testing purposes.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class InteractivePanel extends Composite {

	public InteractivePanel() {
		this.initWidget(this.createWidget());
		this.setStyleName(Constants.INTERACTIVE_PANEL_STYLE);
	}

	protected Widget createWidget() {
		final VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(this.createClassNameLabel());
		verticalPanel.add(this.createButtons());
		return verticalPanel;
	}

	protected Label createClassNameLabel() {
		return new Label(this.getCollectionTypeName());
	}

	protected abstract String getCollectionTypeName();

	protected FlowPanel createButtons() {
		final FlowPanel panel = new FlowPanel();
		panel.add(this.createPanelWidgetCountButton());
		panel.add(this.createPanelAddButton());
		panel.add(this.createPanelInsertButton());
		panel.add(this.createPanelGetButton());
		panel.add(this.createPanelRemoveButton());
		panel.add(this.createPanelIteratorButton());
		panel.add(this.createIteratorHasNextButton());
		panel.add(this.createIteratorNextButton());
		panel.add(this.createIteratorRemoveButton());
		return panel;
	}

	protected Button createPanelWidgetCountButton() {
		final Button button = new Button("panel.getWidgetCount()");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				onPanelWidgetCountClick();
			}
		});
		return button;
	}

	protected void onPanelWidgetCountClick() {
		String message = "panel.getWidgetCount() ";
		int size = -1;
		try {
			size = getPanelWidgetCount();
			message = message + "returned " + size;
		} catch (final Exception caught) {
			caught.printStackTrace();
			message = message + "threw " + caught.getMessage();
		}
		this.log(message);
	}

	/**
	 * Sub-classes must delegate to the panel implementation and fetch the
	 * list's size.
	 * 
	 * @return
	 */
	protected abstract int getPanelWidgetCount();

	protected Button createPanelAddButton() {
		final Button button = new Button("panel.add()");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				onPanelAddClick();
			}
		});
		return button;
	}

	protected void onPanelAddClick() {
		String message = "panel.add(";
		Widget widget = null;
		try {

			widget = this.createElement();
			this.panelAdd(widget);
			message = message + this.toString(widget) + ") returned";
		} catch (final Exception caught) {
			caught.printStackTrace();
			message = message + this.toString(widget) + ") threw " + caught.getMessage();
		}
		this.log(message);
	}

	/**
	 * Sub-classes must delegate to the panel implementation and add the given
	 * widget
	 */
	protected abstract void panelAdd(Widget widget);

	protected Button createPanelInsertButton() {
		final Button button = new Button("panel.insert(int)");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				onPanelInsertClick();
			}
		});
		return button;
	}

	protected void onPanelInsertClick() {
		String message = "panel.insert(";
		int index = -1;
		Widget widget = null;
		try {
			index = Integer.parseInt(Browser.prompt("insert beforeIndex", "0"));
			widget = this.createElement();
			this.panelInsert(widget, index);
			message = message + index + ", " + this.toString(widget) + ") returned";
		} catch (final Exception caught) {
			caught.printStackTrace();
			message = message + index + "," + this.toString(widget) + ") threw " + caught.getMessage();
		}
		this.log(message);
	}

	/**
	 * Sub-classes must delegate to the panel implementation and insert the
	 * given widget at the given slot
	 */
	protected abstract void panelInsert(Widget widget, int index);

	protected Button createPanelGetButton() {
		final Button button = new Button("panel.get()");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				onPanelGetClick();
			}
		});
		return button;
	}

	protected void onPanelGetClick() {
		String message = "panel.get(";
		int index = -1;
		try {
			index = Integer.parseInt(Browser.prompt("get index", "0"));
			final Widget widget = this.panelGet(index);
			this.checkType(widget);
			message = message + index + ") returned " + this.toString(widget);
		} catch (final Exception caught) {
			caught.printStackTrace();
			message = message + index + ") threw " + caught.getMessage();
		}
		this.log(message);
	}

	/**
	 * Sub-classes must delegate to the panel implementation and get the widget
	 * at the given slot.
	 */
	protected abstract Widget panelGet(int index);

	protected Button createPanelRemoveButton() {
		final Button button = new Button("panel.remove(Widget)");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				onPanelRemoveClick();
			}
		});
		return button;
	}

	protected void onPanelRemoveClick() {
		String message = "panel.remove(";
		int index = -1;
		try {
			index = Integer.parseInt(Browser.prompt("remove index", "0"));
			this.panelRemove(this.panelGet(index));
			message = message + index + ") returned";
		} catch (final Exception caught) {
			caught.printStackTrace();
			message = message + index + ") threw " + caught.getMessage();
		}
		this.log(message);
	}

	/**
	 * Sub-classes must delegate to the panel implementation and remove the
	 * widget at the given slot.
	 */
	protected abstract void panelRemove(Widget widget);

	/**
	 * Sub-classes must create a new Widgets whenever this factory method is
	 * called.
	 * 
	 * @return
	 */
	protected abstract Widget createElement();

	protected Button createPanelIteratorButton() {
		final Button button = new Button("panel.iterator()");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				onPanelIteratorClick();
			}
		});
		return button;
	}

	protected void onPanelIteratorClick() {
		String message = "panel.iterator()";
		Iterator iterator = null;
		try {
			iterator = this.panelIterator();
			this.setIterator(iterator);
			message = message + " returned " + iterator;
		} catch (final Exception caught) {
			caught.printStackTrace();
			message = message + " threw " + GWT.getTypeName(caught) + " with a message of \"" + caught.getMessage() + "\".";
		}
		this.log(message);
	}

	/**
	 * Sub-classes must delegate to the panel implementation and fetch the
	 * iterator
	 */
	protected abstract Iterator panelIterator();

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
			message = message + " threw " + GWT.getTypeName(caught) + " with a message of \"" + caught.getMessage() + "\".";
		}
		this.log(message);
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
			message = message + " threw " + GWT.getTypeName(caught) + " with a message of \"" + caught.getMessage() + "\".";
		}
		this.log(message);
	}

	protected Button createIteratorRemoveButton() {
		final Button button = new Button("iterator.remove()");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				onIteratorNextClick();
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
			message = message + " threw " + GWT.getTypeName(caught) + " with a message of \"" + caught.getMessage() + "\".";
		}
		this.log(message);
	}

	/**
	 * Sub-classes should implement check that the given element is valid for
	 * this list.
	 * 
	 * @param element
	 */
	protected abstract void checkType(Object element);

	/**
	 * Contains the iterator being iterated over.
	 */
	private Iterator iterator;

	protected Iterator getIterator() {
		Checker.notNull("field:iterator", iterator);
		return this.iterator;
	}

	protected boolean hasIterator() {
		return null != this.iterator;
	}

	protected void setIterator(final Iterator iterator) {
		Checker.notNull("parameter:iterator", iterator);
		this.iterator = iterator;
	}

	protected void log(final String message) {
		Window.alert(message);
	}

	protected abstract String toString(Object element);
}
