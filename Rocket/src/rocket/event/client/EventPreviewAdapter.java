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
package rocket.event.client;

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;

/**
 * Convenient method which takes care of adapting a GWt raw event into a rocket
 * event and then dispatching to the appropriate handleXXX method.
 * 
 * Sub classes only need to override the appropriate handle method.
 * 
 * @author Miroslav Pokorny
 */
public class EventPreviewAdapter extends EventDispatcher implements com.google.gwt.user.client.EventPreview {

	public EventPreviewAdapter() {
	}

	/**
	 * Installs this EventPreview so that it will preview all incoming events.
	 * 
	 */
	public void install() {
		DOM.addEventPreview(this);
	}

	/**
	 * Removes this EventPreview from recieving all incoming events.
	 */
	public void uninstall() {
		DOM.removeEventPreview(this);
	}

	/**
	 * This method builds an Event and then dispatches to the appropriate event
	 * handler method. The {@link #beforeDispatching(Event)} and
	 * {@link #afterDispatching(Event)} are called before and after dispatching
	 * based on the event type.
	 */
	final public boolean onEventPreview(final com.google.gwt.user.client.Event rawEvent) {
		Event event = null;
		boolean cancelled = false;

		try {
			event = Event.getEvent(rawEvent);

			this.beforeDispatching(event);
			this.dispatch(event);
			this.afterDispatching(event);

			cancelled = event.isCancelled(); // FIXME is a hack should use
												// another method. stopProp eg
												// ???

		} finally {
			ObjectHelper.destroyIfNecessary(event);
		}

		return !cancelled;
	}
}
