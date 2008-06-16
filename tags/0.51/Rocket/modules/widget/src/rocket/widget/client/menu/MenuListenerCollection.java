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
package rocket.widget.client.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.util.client.Checker;

/**
 * A collection of menu event listeners.
 * 
 * @author Miroslav Pokorny
 */
class MenuListenerCollection {

	public MenuListenerCollection() {
		this.setListeners(this.createListeners());
	}

	public void fireMenuOpened(final MenuOpenEvent event) {
		Checker.notNull("parameter:event", event);

		final Iterator<MenuListener> listeners = this.iterator();

		while (listeners.hasNext()) {
			final MenuListener listener = (MenuListener) listeners.next();
			listener.onOpen(event);
		}
	}

	public void add(final MenuListener menuListener) {
		Checker.notNull("parameter:menuListener", menuListener);
		this.getListeners().add(menuListener);
	}

	public void remove(final MenuListener menuListener) {
		Checker.notNull("parameter:menuListener", menuListener);
		this.getListeners().remove(menuListener);
	}

	protected Iterator<MenuListener> iterator() {
		return this.getListeners().iterator();
	}

	/**
	 * A list that aggregates MenuListeners.
	 */
	private List<MenuListener> listeners;

	protected List<MenuListener> getListeners() {
		return listeners;
	}

	protected void setListeners(final List<MenuListener> listeners) {
		this.listeners = listeners;
	}

	protected List<MenuListener> createListeners() {
		return new ArrayList<MenuListener>();
	}
}
