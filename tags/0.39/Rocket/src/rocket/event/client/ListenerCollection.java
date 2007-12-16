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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.util.client.ObjectHelper;

abstract public class ListenerCollection {
	protected ListenerCollection() {
		super();

		this.setListeners(createListeners());
	}

	/**
	 * A list of registered listeners.
	 */
	private List listeners;

	private List getListeners() {
		ObjectHelper.checkNotNull("field:listeners", listeners);
		return this.listeners;
	}

	private void setListeners(final List listeners) {
		ObjectHelper.checkNotNull("parameter:listeners", listeners);
		this.listeners = listeners;
	}

	protected List createListeners() {
		return new ArrayList();
	}

	/**
	 * Sub classes will need to create a add method with an appropriately typed
	 * listener parameter
	 * 
	 * @param listener
	 */
	protected void add(final Object listener) {
		this.getListeners().add(listener);
	}

	/**
	 * Sub classes will need to create a remove method with an appropriately
	 * typed listener parameter
	 * 
	 * @param listener
	 */
	protected boolean remove(final Object listener) {
		return this.getListeners().remove(listener);
	}

	protected void fire(final EventFiringAdapter adapter) {
		final Iterator iterator = this.getListeners().iterator();
		while (iterator.hasNext()) {
			adapter.fire(iterator.next());
		}
	}

	static public interface EventFiringAdapter {
		void fire(Object listener);
	}
}
