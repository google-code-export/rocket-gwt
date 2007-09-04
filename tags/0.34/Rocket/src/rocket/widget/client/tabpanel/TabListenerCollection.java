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

	public List getListeners() {
		ObjectHelper.checkNotNull("field:listeners", listeners);
		return listeners;
	}

	public void setListeners(final List listeners) {
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

	public boolean fireBeforeTabSelected(final TabItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);

		boolean doSelect = true;
		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = (TabListener) listeners.next();
			if (!listener.onBeforeTabSelected(item)) {
				doSelect = false;
				break;
			}
		}
		return doSelect;
	}

	public void fireTabSelected(final TabItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);
		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = (TabListener) listeners.next();
			listener.onTabSelected(item);
		}
	}

	public boolean fireBeforeTabClosed(final TabItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);

		boolean removed = true;
		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = (TabListener) listeners.next();
			if (!listener.onBeforeTabClosed(item)) {
				removed = false;
				break;
			}
		}
		return removed;
	}

	public void fireTabClosed(final TabItem item) {
		ObjectHelper.checkNotNull("parameter:item", item);

		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final TabListener listener = (TabListener) listeners.next();
			listener.onTabClosed(item);
		}
	}
}
