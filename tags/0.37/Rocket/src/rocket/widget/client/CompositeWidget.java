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
import com.google.gwt.user.client.ui.Widget;

/**
 * A base class for any composite that makes it easier to author new composite
 * widgets.
 * 
 * This class includes event listener collections for all the basic events. On
 * top of sinking events sub classes must also make the corresponding
 * addXXXListener and removeXXXListener methods public.
 * 
 * Creating a new widget including a new element.
 * <ul>
 * <li>{@link #beforeCreateElement()}</li>
 * <li>{@link #initWidget( createWidget() )}</li>
 * <li>{@link #afterCreateElement()}</li>
 * <li>{@link #applyStyleName()} Override this to do nothing if this widget has
 * no initial style.</li>
 * </li>
 * 
 * The initial style of the root element from this composite widget is taken
 * from {@link #getInitialStyleName()}
 * 
 * @author Miroslav Pokorny
 */
abstract public class CompositeWidget extends com.google.gwt.user.client.ui.Composite implements EventListener {

	protected CompositeWidget() {
		super();

		prepare();
	}

	protected void prepare() {
		this.beforeCreateWidget();
		this.initWidget(this.createWidget());
		this.afterCreateWidget();

		this.applyStyleName();
	}

	/**
	 * This method is called prior to {@link #createWidget()}
	 * 
	 */
	protected void beforeCreateWidget() {
		final int eventBitsSunk = this.getSunkEventsBitMask();
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);
		dispatcher.prepareListenerCollections(eventBitsSunk);
	}

	/**
	 * This method is called when necessary to create a new widget.
	 * 
	 * @return The new wrapped widget
	 */
	abstract protected Widget createWidget();

	/**
	 * This method provides an opportunity for sub classes to register listener
	 * collections etc
	 */
	protected void afterCreateWidget() {
	}

	protected void applyStyleName() {
		this.setStyleName(this.getInitialStyleName());
	}

	/**
	 * Sub classes must override this method to return the stylename that will
	 * be set upon the root element of this widget.
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