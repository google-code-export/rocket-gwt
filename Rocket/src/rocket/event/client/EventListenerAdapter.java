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

/**
 * Convenient method which takes care of adapting a GWt raw event into a rocket
 * event and then dispatching to the appropriate handleXXX method.
 * 
 * Sub classes only need to override the appropriate handle method.
 * 
 * @author Miroslav Pokorny
 */
public class EventListenerAdapter extends EventDispatcher implements com.google.gwt.user.client.EventListener {

	public void onBrowserEvent(final com.google.gwt.user.client.Event rawEvent) {
		Event event = null;

		try {
			event = Event.getEvent(rawEvent);

			this.beforeDispatching(event);
			this.dispatch(event);
			this.afterDispatching(event);

		} finally {
			ObjectHelper.destroyIfNecessary(event);
		}
	}
}
