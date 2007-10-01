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

import rocket.event.client.Event;
import rocket.event.client.EventListener;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A base class for any widget that makes it easier to author new composite
 * widgets.
 * 
 * This class includes event listener collections for all the basic events. On
 * top of sinking events sub classes must also make the corresponding
 * addXXXListener and removeXXXListener methods public (they are all currently
 * protected).
 * 
 * Wrapping an existing element taken from the dom is trivial and simply
 * requires the {@link #Widget(Element)} to be called.
 *
 * Creating a new widget including a new element.
 * <ul>
 * <li>{@link #beforeCreateElement()}</li>
 * <li>{@link #createElement()}</li>
 * <li>{@link #afterCreateElement()}</li>
 * <li>{@link #applyStyleName()} Override this to do nothing if this widget has no initial style.</li>
 * </li>
 * 
 * Create a new widget with an element hijacked from the dom.
 * <ul>
 * <li>{@link #checkElement(Element)} Check that the element is of the correct type</li>
 * <li>{@link #beforeCreateElement()}</li>
 * <li>{@link #setElement( Element from constructor )}</li>
 * <li>{@link #afterCreateElement()}</li>
 * <li>{@link #applyStyleName()} Override this to do nothing if this widget has no initial style.</li>
 * </li>
 * 
 * The initial style of the root element from this widget is taken from {@link #getInitialStyleName()}
 * 
 * @author Miroslav Pokorny
 */
abstract public class Widget extends com.google.gwt.user.client.ui.Widget implements EventListener {

	/**
	 * This constructor should be used when creating a new widget from scratch
	 */
	protected Widget() {
		super();

		prepare();
	}

	protected void prepare() {
		this.beforeCreateElement();
		this.setElement(this.createElement());
		this.afterCreateElement();
		this.applyStyleName();
	}

	/**
	 * This constructor should be used when creating a new widget but using an
	 * element taken from the dom.
	 * 
	 * @param element
	 */
	protected Widget(final Element element) {
		super();

		prepare(element);
	}

	protected void prepare(final Element element) {
		this.checkElement(element);

		final Hijacker hijacker = new Hijacker(element);
		hijacker.save();

		this.beforeCreateElement();
		this.setElement(element);
		this.afterCreateElement();

		RootPanel.get().add(this);
		hijacker.restore();

		this.applyStyleName();
	}

	/**
	 * This method is called prior to {@link #createElement()} Sub classes
	 * should always call this method.
	 */
	protected void beforeCreateElement() {
		final int eventsSunk = this.getSunkEventsBitMask();

		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);
		dispatcher.prepareListenerCollections(eventsSunk);
	}

	/**
	 * This method is called when necessary to create a new widget.
	 * 
	 * @return The new wrapped widget
	 */
	abstract protected Element createElement();

	/**
	 * This method is called when the lets wrap an existing DOM element
	 * constructor is used. Sub classes should check that the element is the
	 * appropriate type.
	 * 
	 * @param element
	 */
	abstract protected void checkElement(Element element);

	/**
	 * This method provides an opportunity for sub classes to register listener
	 * collections etc
	 * 
	 */
	protected void afterCreateElement() {
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

		this.setSinkEvents();
	}

	/**
	 * Sets event sinking and listener.
	 */
	protected void setSinkEvents() {
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
			ObjectHelper.destroyIfNecessary(event);
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
		ObjectHelper.checkNotNull("field:eventListenerDispatcher", this.eventListenerDispatcher);
		return this.eventListenerDispatcher;
	}

	protected void setEventListenerDispatcher(final EventListenerDispatcher eventListenerDispatcher) {
		ObjectHelper.checkNotNull("parameter:eventListenerDispatcher", eventListenerDispatcher);
		this.eventListenerDispatcher = eventListenerDispatcher;
	}

	protected EventListenerDispatcher createEventListenerDispatcher() {
		return new EventListenerDispatcher();
	}
}
