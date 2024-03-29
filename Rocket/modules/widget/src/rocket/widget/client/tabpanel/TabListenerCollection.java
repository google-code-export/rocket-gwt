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
package rocket.widget.client.tabpanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.util.client.Checker;

class TabListenerCollection {
	public TabListenerCollection() {
		this.setListeners(new ArrayList<TabListener>());
	}

	/**
	 * A list containing listeners to the various page change events.
	 */
	private List<TabListener> listeners;

	protected List<TabListener> getListeners() {
		Checker.notNull("field:listeners", listeners);
		return listeners;
	}

	protected void setListeners(final List<TabListener> listeners) {
		Checker.notNull("parameter:listeners", listeners);
		this.listeners = listeners;
	}

	public void add(final TabListener tabListener) {
		Checker.notNull("parameter:tabListener", tabListener);

		this.getListeners().add(tabListener);
	}

	public void remove(final TabListener tabListener) {
		Checker.notNull("parameter:tabListener", tabListener);

		this.getListeners().remove(tabListener);
	}

	public void fireBeforeTabSelected(final BeforeTabSelectEvent event) {
		final Iterator<TabListener> listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = listeners.next();
			listener.onBeforeTabSelect(event);

			if (event.isCancelled()) {
				break;
			}
		}
	}

	public void fireTabSelected(final TabSelectEvent event) {
		final Iterator<TabListener> listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = listeners.next();
			listener.onTabSelect(event);
		}
	}

	public void fireBeforeTabClosed(final BeforeTabCloseEvent event) {
		final Iterator<TabListener> listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = listeners.next();
			listener.onBeforeTabClose(event);

			if (event.isCancelled()) {
				break;
			}
		}
	}

	public void fireTabClosed(final TabCloseEvent event) {
		final Iterator<TabListener> listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = listeners.next();
			listener.onTabClose(event);
		}
	}
}
