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

import rocket.dom.client.Dom;
import rocket.event.client.Event;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.EventPreviewAdapter;
import rocket.event.client.MouseDownEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.event.client.MouseMoveEvent;
import rocket.event.client.MouseUpEvent;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.Checker;
import rocket.widget.client.CompositeWidget;
import rocket.widget.client.EventListenerDispatcher;
import rocket.widget.client.Panel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.Widget;

/**
 * Convenient base class that includes the common functionality found within
 * HorizontalSplitterPanel and VerticalSplitterPanel.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class SplitterPanel extends CompositeWidget {

	protected SplitterPanel() {
		super();
	}

	@Override
	protected Widget createWidget() {
		final InternalPanel panel = createPanel();
		this.setPanel(panel);
		return panel;
	}

	@Override
	protected int getSunkEventsBitMask() {
		return 0;
	}

	@Override
	public void onAttach() {
		super.onAttach();
		this.redraw();
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
		Checker.notNull("parameter:item", item);

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
		Checker.notNull("parameter:item", item);

		while (true) {
			final List<SplitterItem> items = this.getItems();
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

		this.redraw();
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
		Checker.notNull("parameter:item", item);

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
		final List<SplitterItem> items = this.getItems();

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

		this.redraw();
	}

	/**
	 * Checks if this widget is attached and if it has does the actual laying
	 * out.
	 */
	protected void redraw() {
		if (this.isAttached()) {
			this.redraw0();
		}
	}

	protected abstract void redraw0();

	/**
	 * Loops thru all added items summing their weights and returning that
	 * value.
	 * 
	 * @return The sum
	 */
	protected int sumWeights() {
		int weightSum = 0;
		final Iterator<SplitterItem> items = this.getItems().iterator();
		while (items.hasNext()) {
			final SplitterItem item = items.next();
			weightSum = weightSum + item.getSizeShare();
		}
		return weightSum;
	}

	/**
	 * This list contains the individual items
	 */
	private List<SplitterItem> items;

	protected List<SplitterItem> getItems() {
		Checker.notNull("field:items", this.items);
		return this.items;
	}

	protected void setItems(final List<SplitterItem> items) {
		Checker.notNull("parameter:items", items);
		this.items = items;
	}

	protected List<SplitterItem> createItems() {
		return new ArrayList<SplitterItem>();
	}

	/**
	 * This is the actual panel that contains the added widgets and splitters.
	 */
	private InternalPanel panel;

	protected InternalPanel getPanel() {
		Checker.notNull("field:panel", panel);
		return this.panel;
	}

	protected void setPanel(final InternalPanel panel) {
		Checker.notNull("parameter:panel", panel);
		this.panel = panel;
	}

	protected InternalPanel createPanel() {
		return new InternalPanel();
	}

	static class InternalPanel extends rocket.widget.client.Panel {

		protected void checkElement(final Element element) {
			throw new UnsupportedOperationException();
		}

		/**
		 * Factory method which creates the parent DIV element for this entire
		 * panel
		 * 
		 * @return The new Panel element.
		 */
		@Override
		protected Element createPanelElement() {
			final Element parent = DOM.createDiv();

			// the enclosing div - with no border or margins...this makes it
			// easy to calculate the available width for widgets.
			final Element child = DOM.createDiv();

			final InlineStyle childInlineStyle = InlineStyle.getInlineStyle(child);
			childInlineStyle.setInteger(Css.MARGIN, 0, CssUnit.PX);
			childInlineStyle.setInteger(Css.BORDER, 0, CssUnit.PX);
			childInlineStyle.setInteger(Css.WIDTH, 100, CssUnit.PERCENTAGE);
			childInlineStyle.setInteger(Css.HEIGHT, 100, CssUnit.PERCENTAGE);
			childInlineStyle.setString(Css.POSITION, "relative");

			parent.appendChild(child);

			return parent;
		}

	@Override
		protected void applyStyleName() {
		}

	@Override
		protected String getInitialStyleName() {
			throw new UnsupportedOperationException("getWidgetStyleName");
		}

	@Override
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
		@Override
		protected void insert0(final Element element, final int indexBefore) {
			Checker.notNull("parameter:element", element);

			this.getParentElement().appendChild(element);
		}

		/**
		 * Remove the given element from the parent DIV.
		 * 
		 * This method does nothing letting the disown() method remove the
		 * widget's element
		 */
		@Override
		protected void remove0(final Element element, final int index) {
			Dom.removeFromParent(element);
		}
	};

	public Iterator<SplitterItem> iterator() {
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
	abstract class Splitter extends rocket.widget.client.Widget {
		Splitter() {
			super();
		}

		@Override
		protected Element createElement() {
			final Element div = DOM.createDiv();
			div.setInnerHTML("&nbsp;");
			InlineStyle.getInlineStyle( div ).setString(Css.OVERFLOW, "hidden");
			return div;
		}

		@Override
		protected void afterCreateElement() {
			final EventListenerDispatcher dispatcher = this.getEventListenerDispatcher();
			dispatcher.addMouseEventListener(new MouseEventAdapter() {
				public void onMouseDown(final MouseDownEvent event) {
					Splitter.this.handleMouseDown(event);
				}
			});
		}

		@Override
		protected void checkElement(Element element) {
			throw new UnsupportedOperationException("checkElement");
		}

		@Override
		protected int getSunkEventsBitMask() {
			return EventBitMaskConstants.MOUSE_DOWN;
		}

		/**
		 * Installs a EventPreview which will exist until the mouse button is
		 * let go. The primary purpose of this EventPreview is to delegate to
		 * SplitterPanel to handle dragging of the selected splitter.
		 * 
		 * @param event
		 */
		protected void handleMouseDown(final MouseDownEvent event) {
			Checker.notNull("parameter:event", event);

			final EventPreview preview = this.createEventPreview();
			this.setEventPreview(preview);
			DOM.addEventPreview(preview);

			this.addStyleName(this.getDraggingStyleName());
			event.stop(); // cancel event so text selection doesnt happen in
			// Opera.
		}

		private EventPreview eventPreview;

		protected EventPreview getEventPreview() {
			Checker.notNull("field:eventPreview", eventPreview);
			return this.eventPreview;
		}

		protected void setEventPreview(final EventPreview eventPreview) {
			Checker.notNull("field:eventPreview", eventPreview);
			this.eventPreview = eventPreview;
		}

		protected void clearEventPreview() {
			this.eventPreview = null;
		}

		/**
		 * This EventPreview dispatches to the either
		 * {@link #onMouseMove(Event)} or {@link #onMouseUp(Event)} depending on
		 * the event type.
		 * 
		 * @return
		 */
		protected EventPreview createEventPreview() {
			return new EventPreviewAdapter() {

				protected void beforeDispatching(final Event event) {
					event.setWidget(Splitter.this);
				}

				protected void onMouseMove(final MouseMoveEvent event) {
					SplitterPanel.this.onMouseMove(event);
				}

				protected void onMouseUp(final MouseUpEvent event) {
					Splitter.this.onMouseUp(event);
				}
			};
		}

		/**
		 * When the mouse button is released remove the dragging style and clear
		 * the custom EventPreview.
		 * 
		 * @param event
		 */
		protected void onMouseUp(final MouseUpEvent event) {
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
	 * @param event
	 */
	abstract protected void onMouseMove(MouseMoveEvent event);

	/**
	 * The size in pixels allocated to each splitter widget that separated two
	 * widgets.
	 */
	private int splitterSize;

	public int getSplitterSize() {
		Checker.greaterThan("field:splitterSize", 0, splitterSize);
		return this.splitterSize;
	}

	public void setSplitterSize(final int splitterSize) {
		Checker.greaterThan("parameter:splitterSize", 0, splitterSize);
		this.splitterSize = splitterSize;
	}

	public String toString() {
		return super.toString() + ", splitterSize: " + splitterSize;
	}
}
