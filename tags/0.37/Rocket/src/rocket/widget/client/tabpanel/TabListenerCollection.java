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

import rocket.util.client.ObjectHelper;

class TabListenerCollection {
	public TabListenerCollection() {
		this.setListeners(new ArrayList());
	}

	/**
	 * A list containing listeners to the various page change events.
	 */
	private List listeners;

	protected List getListeners() {
		ObjectHelper.checkNotNull("field:listeners", listeners);
		return listeners;
	}

	protected void setListeners(final List listeners) {
		ObjectHelper.checkNotNull("parameter:listeners", listeners);
		this.listeners = listeners;
	}

	public void add(final TabListener tabListener) {
		ObjectHelper.checkNotNull("parameter:tabListener", tabListener);

		this.getListeners().add(tabListener);
	}

	public void remove(final TabListener tabListener) {
		ObjectHelper.checkNotNull("parameter:tabListener", tabListener);

		this.getListeners().remove(tabListener);
	}

	// TODO accept event not TabItem
	public void fireBeforeTabSelected(final BeforeTabSelectEvent event) {
		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = (TabListener) listeners.next();
			listener.onBeforeTabSelect(event);

			if (event.isCancelled()) {
				break;
			}
		}
	}

	public void fireTabSelected(final TabSelectEvent event) {
		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = (TabListener) listeners.next();
			listener.onTabSelect(event);
		}
	}

	public void fireBeforeTabClosed(final BeforeTabCloseEvent event) {
		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = (TabListener) listeners.next();
			listener.onBeforeTabClose(event);

			if (event.isCancelled()) {
				break;
			}
		}
	}

	public void fireTabClosed(final TabCloseEvent event) {
		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = (TabListener) listeners.next();
			listener.onTabClose(event);
		}
	}
}
