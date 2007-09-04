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
package rocket.widget.client.splitter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.widget.client.Composite;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Convenient base class that includes the common functionality found within
 * HorizontalSplitterPanel and VerticalSplitterPanel.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class SplitterPanel extends Composite {

	protected SplitterPanel() {
		super();
	}

	protected Widget createWidget() {
		final Panel panel = createPanel();
		this.setPanel(panel);
		return panel;
	}

	protected int getSunkEventsBitMask() {
		return 0;
	}

	public void onAttach() {
		super.onAttach();
		this.layoutWidgets();
	}

	/**
	 * Returns the number of widgets belonging to this splitter
	 * 
	 * @return The count
	 */
	public int getCount() {
		return this.getItems().size();
	}

	/**
	 * Returns the SplitterItem at the given index.
	 * 
	 * @param index
	 * @return The splitter Item
	 */
	public SplitterItem get(final int index) {
		return (SplitterItem) this.getItems().get(index);
	}

	/**
	 * Returns the index of the given item if it has been added to this
	 * HorizontalSplitterPanel
	 * 
	 * @param item
	 * @return The index of item
	 */
	public int getIndex(final SplitterItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);

		return this.getItems().indexOf(item);
	}

	/**
	 * Adds a new SplitterItem
	 * 
	 * @param item
	 */
	public void add(final SplitterItem item) {
		this.insert(this.getCount(), item);
	}

	/**
	 * Inserts the given HorizonalSplitterItem and its widget.
	 * 
	 * @param beforeIndex
	 * @param item
	 */
	public void insert(final int beforeIndex, final SplitterItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);

		while (true) {
			final List items = this.getItems();
			items.add(beforeIndex, item);

			final Panel panel = this.getPanel();
			final Widget widget = item.getWidget();

			// if this is the only widget no need to add a splitter before or
			// after...
			if (items.size() == 1) {
				panel.insert(widget, 0);
				break;
			}

			int panelIndex = beforeIndex * 2;

			// if its the new first widget insert the widget then a splitter
			Widget first = widget;
			Widget second = this.createSplitter();

			if (beforeIndex > 0) {
				// not the first widget insert the splitter then the widget...
				Widget swap = first;
				first = second;
				second = swap;

				panelIndex--;
			}

			panel.insert(first, panelIndex + 0);
			panel.insert(second, panelIndex + 1);
			break;
		}

		this.layoutWidgets();
	}

	/**
	 * Factory method which creates the splitter widget that is used to divide
	 * widgets appearing within this panel.
	 * 
	 * @return A new Splitter
	 */
	protected abstract Widget createSplitter();

	/**
	 * Removes the given SplitterItem if it belongs to this HorizonalSplitter
	 * 
	 * @param item
	 * @return true if the item was removed otherwise returns false.
	 */
	public boolean remove(final SplitterItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);

		final int index = this.getIndex(item);
		if (-1 != index) {
			this.remove(index);
		}
		return index != -1;
	}

	/**
	 * Removes an existing widget from this splitter
	 * 
	 * @param index
	 */
	public void remove(final int index) {
		final List items = this.getItems();

		// remove the item from list...
		items.remove(index);

		// remove the widget from the panel...
		final Panel panel = this.getPanel();
		final int panelIndex = index * 2;

		// remove the widget, this may be the widget(when index==0) or
		// slider(when index>0)
		panel.remove(panelIndex);

		// if the widget that was removed was the only widget then there will be
		// no splitter...
		if (panel.getWidgetCount() > 0) {
			panel.remove(panelIndex);
		}

		this.layoutWidgets();
	}

	/**
	 * Checks if this widget is attached and if it has does the actual laying
	 * out.
	 */
	protected void layoutWidgets() {
		if (this.isAttached()) {
			this.layoutWidgets0();
		}
	}

	protected abstract void layoutWidgets0();

	/**
	 * Loops thru all added items summing their weights and returning that
	 * value.
	 * 
	 * @return
	 */
	protected int sumWeights() {
		int weightSum = 0;
		final Iterator items = this.getItems().iterator();
		while (items.hasNext()) {
			final SplitterItem item = (SplitterItem) items.next();
			weightSum = weightSum + item.getSizeShare();
		}
		return weightSum;
	}

	/**
	 * This list contains the individual items
	 */
	private List items;

	protected List getItems() {
		ObjectHelper.checkNotNull("field:items", this.items);
		return this.items;
	}

	protected void setItems(final List items) {
		ObjectHelper.checkNotNull("parameter:items", items);
		this.items = items;
	}

	protected List createItems() {
		return new ArrayList();
	}

	/**
	 * This is the actual panel that contains the added widgets and splitters.
	 */
	private Panel panel;

	protected Panel getPanel() {
		ObjectHelper.checkNotNull("field:panel", panel);
		return this.panel;
	}

	protected void setPanel(final Panel panel) {
		ObjectHelper.checkNotNull("parameter:panel", panel);
		this.panel = panel;
	}

	protected Panel createPanel() {
		return new Panel();
	}

	static class Panel extends rocket.widget.client.Panel {

		/**
		 * Factory method which creates the parent DIV element for this entire
		 * panel
		 * 
		 * @return
		 */
		protected Element createPanelElement() {
			final Element parent = DOM.createDiv();

			// the enclosing div - with no border or margins...this makes it
			// easy to calculate the available width for widgets.
			final Element child = DOM.createDiv();
			InlineStyle.setInteger(child, StyleConstants.MARGIN, 0, CssUnit.PX);
			InlineStyle.setInteger(child, StyleConstants.BORDER, 0, CssUnit.PX);
			InlineStyle.setInteger(child, StyleConstants.WIDTH, 100, CssUnit.PERCENTAGE);
			InlineStyle.setInteger(child, StyleConstants.HEIGHT, 100, CssUnit.PERCENTAGE);
			InlineStyle.setString(child, StyleConstants.POSITION, "relative");

			DOM.appendChild(parent, child);

			return parent;
		}

		protected int getSunkEventsBitMask() {
			return 0;
		}

		/**
		 * Returns the element which will house each of the new widget's
		 * elements.
		 * 
		 * @return
		 */
		public Element getParentElement() {
			return DOM.getFirstChild(this.getElement());
		}

		/**
		 * Add the given element to the parent DIV element
		 */
		protected Element insert0(final Element element, final int indexBefore) {
			ObjectHelper.checkNotNull("parameter:element", element);

			final Element parent = this.getParentElement();
			DOM.appendChild(parent, element);
			return parent;
		}

		/**
		 * Remove the given element from the parent DIV.
		 * 
		 * This method does nothing letting the disown() method remove the
		 * widget's element
		 */
		protected void remove0(final Element element, final int index) {
		}
	};

	public Iterator iterator() {
		return this.getItems().iterator();
	}

	/**
	 * A splitter widget's primary function is to wait for a mouse down event.
	 * When a mouseDown event is received it then begins drag mode and registers
	 * a EventPreview.
	 * 
	 * This EventPreview then follows mouseDown events re assigning widths to
	 * each of the child widgets belonging to this splitter.
	 * 
	 * When the mouse button is let go the EventPreview is unregistered.
	 * 
	 * @author mP
	 */
	abstract class Splitter extends HTML {
		Splitter() {
			super("&nbsp;");

			this.sinkEvents(Event.ONMOUSEDOWN);

			InlineStyle.setString(this.getElement(), StyleConstants.OVERFLOW, "hidden");
		}

		public void onBrowserEvent(final Event event) {
			ObjectHelper.checkNotNull("parameter:event", event);
			if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
				Splitter.this.handleMouseDown(event);
			}
		}

		/**
		 * Installs a EventPreview which will exist until the mouse button is
		 * let go. The primary purpose of this EventPreview is to delegate to
		 * SplitterPanel to handle dragging of the selected splitter.
		 * 
		 * @param event
		 */
		protected void handleMouseDown(final Event event) {
			ObjectHelper.checkNotNull("parameter:event", event);

			final EventPreview preview = this.createEventPreview();
			this.setEventPreview(preview);
			DOM.addEventPreview(preview);

			this.addStyleName(this.getDraggingStyleName());
		}

		private EventPreview eventPreview;

		protected EventPreview getEventPreview() {
			ObjectHelper.checkNotNull("field:eventPreview", eventPreview);
			return this.eventPreview;
		}

		protected void setEventPreview(final EventPreview eventPreview) {
			ObjectHelper.checkNotNull("field:eventPreview", eventPreview);
			this.eventPreview = eventPreview;
		}

		protected void clearEventPreview() {
			this.eventPreview = null;
		}

		/**
		 * This EventPreview dispatches to the either
		 * {@link #handleMouseMove(Event)} or {@link #handleMouseUp(Event)}
		 * depending on the event type.
		 * 
		 * @return
		 */
		protected EventPreview createEventPreview() {
			final EventPreview preview = new EventPreview() {
				public boolean onEventPreview(final Event event) {
					ObjectHelper.checkNotNull("parameter:event", event);

					while (true) {
						final int type = DOM.eventGetType(event);
						if (Event.ONMOUSEMOVE == type) {
							Splitter.this.handleMouseMove(event);
							break;
						}
						if (Event.ONMOUSEUP == type) {
							Splitter.this.handleMouseUp(event);
							break;
						}
						break;
					}

					// always cancel events...
					return false;
				}
			};

			return preview;
		}

		protected void handleMouseMove(final Event event) {
			SplitterPanel.this.handleMouseMove(this, event);
		}

		/**
		 * When the mouse button is released remove the dragging style and clear
		 * the custom EventPreview.
		 * 
		 * @param event
		 */
		protected void handleMouseUp(final Event event) {
			this.removeStyleName(this.getDraggingStyleName());
			DOM.removeEventPreview(this.getEventPreview());
			this.clearEventPreview();
		}

		protected abstract String getDraggingStyleName();
	}

	/**
	 * This method is implemented by both {@link HorizontalSplitterPanel} and
	 * {@link VerticalSplitterPanel}
	 * 
	 * @param splitter
	 * @param event
	 */
	abstract protected void handleMouseMove(Splitter splitter, Event event);

	/**
	 * The size in pixels allocated to each splitter widget that separated two
	 * widgets.
	 */
	private int splitterSize;

	public int getSplitterSize() {
		PrimitiveHelper.checkGreaterThan("field:splitterSize", splitterSize, 0);
		return this.splitterSize;
	}

	public void setSplitterSize(final int splitterSize) {
		PrimitiveHelper.checkGreaterThan("parameter:splitterSize", splitterSize, 0);
		this.splitterSize = splitterSize;
	}

	public String toString() {
		return super.toString() + ", splitterSize: " + splitterSize;
	}
}
