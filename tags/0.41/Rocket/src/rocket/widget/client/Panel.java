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
package rocket.widget.client;

import java.util.Iterator;

import rocket.collection.client.CollectionsHelper;
import rocket.event.client.Event;
import rocket.event.client.EventListener;
import rocket.util.client.Checker;
import rocket.util.client.Utilities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

/**
 * Convenient base class for any Panel implementation. It includes support for
 * hijacking an element sourced from the dom.
 * 
 * Creating a new panel including a new element.
 * <ul>
 * <li>{@link #beforeCreatePanelElement()}</li>
 * <li>{@link #createPanelElement()}</li>
 * <li>{@link #afterCreatePanelElement()}</li>
 * <li>{@link #applyStyleName()} Override this to do nothing if this panel has
 * no initial style.</li>
 * </li>
 * 
 * Create a new panel with an element hijacked from the dom.
 * <ul>
 * <li>{@link #checkElement(Element)} Check that the element is of the correct
 * type</li>
 * <li>{@link #beforeCreatePanelElement()}</li>
 * <li>{@link #setElement( Element from constructor )}</li>
 * <li>{@link #afterCreatePanelElement()}</li>
 * <li>{@link #applyStyleName()} Override this to do nothing if this panel has
 * no initial style.</li>
 * </li>
 * 
 * The initial style of the root element from this panel is taken from
 * {@link #getInitialStyleName()}
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class Panel extends com.google.gwt.user.client.ui.Panel implements HasWidgets, EventListener {

	/**
	 * This constructor should be used when creating a new panel from scratch
	 */
	protected Panel() {
		prepare();
	}

	protected void prepare() {
		this.setWidgetCollection(this.createWidgetCollection());

		this.beforeCreatePanelElement();
		this.setElement(this.createPanelElement());
		this.afterCreatePanelElement();
		this.applyStyleName();
	}

	/**
	 * This constructor should be used when creating a new widget but using an
	 * element taken from the dom.
	 * 
	 * @param element
	 */
	protected Panel(final Element element) {
		prepare(element);
	}

	protected void prepare(final Element element) {
		this.checkElement(element);

		this.setWidgetCollection(this.createWidgetCollection());

		final Hijacker hijacker = new Hijacker(element);
		hijacker.save();

		this.beforeCreatePanelElement();
		this.setElement(element);
		this.afterCreatePanelElement();

		RootPanel.get().add(this);
		hijacker.restore();

		this.applyStyleName();
	}

	/**
	 * This special constructor should only be used by {@link CompositePanel}.
	 * 
	 * @param ignored
	 */
	Panel(final boolean ignored) {
		super();
	}

	/**
	 * This method is called when the lets wrap an existing DOM element
	 * constructor is used. Sub classes should check that the element is the
	 * appropriate type.
	 * 
	 * @param element
	 */
	abstract protected void checkElement(Element element);

	protected void beforeCreatePanelElement() {
		final int eventBitsSunk = this.getSunkEventsBitMask();

		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);
		dispatcher.prepareListenerCollections(eventBitsSunk);
	}

	/**
	 * Sub classes must create the root panel element.
	 * 
	 * @return The new element
	 */
	abstract protected Element createPanelElement();

	/**
	 * This method provides an opportunity for sub classes to do stuff after the
	 * element has been created.
	 */
	protected void afterCreatePanelElement() {
	}

	protected void applyStyleName() {
		this.setStyleName(this.getInitialStyleName());
	}

	/**
	 * Sub classes must override this method to return the stylename applied to
	 * the primary element.
	 * 
	 * @return
	 */
	abstract protected String getInitialStyleName();

	protected void onAttach() {
		super.onAttach();

		this.doSinkEvents();
	}

	/**
	 * Sets event sinking and listener.
	 */
	protected void doSinkEvents() {
		final Element original = this.getElement();
		final Element target = this.getSunkEventsTarget();

		if (false == DOM.compare(original, target)) {
			DOM.sinkEvents(original, 0);
		}
		final int mask = this.getSunkEventsBitMask();
		if (0 != mask) {
			DOM.sinkEvents(target, mask);
			DOM.setEventListener(target, this);
		}
	}

	/**
	 * Sub classes must return a bit mask that indicates which events are being
	 * sunk.
	 * 
	 * @return
	 */
	abstract protected int getSunkEventsBitMask();

	protected Element getSunkEventsTarget() {
		return this.getElement();
	}

	/**
	 * The complement of onAttach. This method removes the event listener for
	 * the sunk event target.
	 */
	protected void onDetach() {
		if (0 != this.getSunkEventsBitMask()) {
			DOM.setEventListener(this.getSunkEventsTarget(), this);
		}
		super.onDetach();
	}

	/**
	 * Returns the number of widgets within this panel.
	 * 
	 * @return
	 */
	public int getWidgetCount() {
		return this.getWidgetCollection().size();
	}

	public int indexOf(final Widget widget) {
		return this.getWidgetCollection().indexOf(widget);
	}

	/**
	 * Retrieves a widget within this panel.
	 * 
	 * @param index
	 * @return
	 */
	public Widget get(final int index) {
		return this.getWidgetCollection().get(index);
	}

	/**
	 * Adds a new widget to the end of this panel.
	 */
	public void add(final Widget widget) {
		this.insert(widget, this.getWidgetCount());
	}

	/**
	 * Sub-classes need to insert the given widget into the DOM
	 * 
	 * @param widget
	 * @param indexBefore
	 */
	public void insert(final Widget widget, int indexBefore) {
		if (widget.getParent() != null) {
			throw new IllegalArgumentException(
					"The parameter:widget already has a parent, remove from that first and then add/insert again to this "
							+ GWT.getTypeName(this));
		}

		this.insert0(widget, indexBefore);
		this.adopt(widget);
		this.getWidgetCollection().insert(widget, indexBefore);
	}

	protected void insert0(final Widget widget, int indexBefore) {
		Checker.notNull("parameter:widget", widget);
		this.insert0(widget.getElement(), indexBefore);
	}

	/**
	 * Sub-classes need to create/find the element which will become the parent
	 * of the Widget's element
	 * 
	 * @param element
	 * @param indexBefore
	 */
	protected abstract void insert0(Element element, int indexBefore);

	/**
	 * Attempts to remove an existing widget from this panel if it is a child.
	 * 
	 * @return true if the widget was a child and was successfully removed,
	 *         otehrwise returns false.
	 */
	public boolean remove(final Widget widget) {
		Checker.notNull("parameter:widget", widget);

		boolean removed = false;
		final WidgetCollection widgets = this.getWidgetCollection();
		final int index = widgets.indexOf(widget);
		if (-1 != index) {
			this.remove(index);
			removed = true;
		}

		return removed;
	}

	/**
	 * Removes the widget at the given slot.
	 * 
	 * @param index
	 */
	public boolean remove(final int index) {
		final WidgetCollection widgets = this.getWidgetCollection();

		final Widget widget = widgets.get(index);
		this.remove0(widget.getElement(), index);// cleanup opportunity
		this.orphan(widget);
		widgets.remove(index);

		return true;
	}

	protected void remove0(final Widget widget, final int index) {
		Checker.notNull("parameter:widget", widget);
		this.remove0(widget.getElement(), index);
	}

	/**
	 * Cleanup opportunity for sub-classes to remove other outstanding elements
	 * from the dom.
	 * 
	 * @param element
	 * @param index
	 */
	protected abstract void remove0(Element element, int index);

	/**
	 * Clears or removes all widgets from this panel.
	 */
	public void clear() {
		CollectionsHelper.removeAll(this.iterator());
	}

	/**
	 * Returns an iterator which may be used to visit all the widgets belonging
	 * to this panel.
	 */
	public Iterator iterator() {
		return this.getWidgetCollection().iterator();
	}

	/**
	 * This collection includes all the widgest that belong to each of the
	 * individual cells.
	 */
	private WidgetCollection widgetCollection;

	protected WidgetCollection getWidgetCollection() {
		Checker.notNull("field:widgetCollection", widgetCollection);
		return widgetCollection;
	}

	protected void setWidgetCollection(final WidgetCollection widgetCollection) {
		Checker.notNull("parameter:widgetCollection", widgetCollection);
		this.widgetCollection = widgetCollection;
	}

	protected WidgetCollection createWidgetCollection() {
		return new WidgetCollection(this);
	}

	/**
	 * Dispatches the and fires the appropriate listeners based on the event
	 * type
	 */
	public void onBrowserEvent(final com.google.gwt.user.client.Event rawEvent) {
		Event event = null;
		try {
			event = Event.getEvent(rawEvent);
			event.setWidget(this);
			this.onBrowserEvent(event);
		} finally {
			Utilities.destroyIfNecessary(event);
		}
	}

	public void onBrowserEvent(final Event event) {
		this.getEventListenerDispatcher().onBrowserEvent(event);
	}

	/**
	 * The dispatcher that fires the appropriate listener event for any
	 * registered event listeners.
	 */
	private EventListenerDispatcher eventListenerDispatcher;

	protected EventListenerDispatcher getEventListenerDispatcher() {
		Checker.notNull("field:eventListenerDispatcher", this.eventListenerDispatcher);
		return this.eventListenerDispatcher;
	}

	protected void setEventListenerDispatcher(final EventListenerDispatcher eventListenerDispatcher) {
		Checker.notNull("parameter:eventListenerDispatcher", eventListenerDispatcher);
		this.eventListenerDispatcher = eventListenerDispatcher;
	}

	protected EventListenerDispatcher createEventListenerDispatcher() {
		return new EventListenerDispatcher();
	}
}
