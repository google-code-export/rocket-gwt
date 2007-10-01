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

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.ui.Widget;

class MenuListenerCollection {

	public MenuListenerCollection() {
		this.setListeners(this.createListeners());
	}

	// FIRE EVENTS ::::::::::::::::::::::::::::::::::::::

	public void fireMenuCancelled(final Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		final MenuOpenCancelledEvent event = new MenuOpenCancelledEvent();
		event.setWidget( widget );
		
		final Iterator listeners = this.iterator();

		while (listeners.hasNext()) {
			final MenuListener listener = (MenuListener) listeners.next();
			listener.onMenuCancelled(event);
		}
	}

	public boolean fireBeforeMenuOpened(final Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		final BeforeMenuOpenedEvent event = new BeforeMenuOpenedEvent();
		event.setWidget( widget );
		
		final Iterator listeners = this.iterator();
		boolean open = true;

		while (listeners.hasNext()) {
			final MenuListener listener = (MenuListener) listeners.next();
			listener.onBeforeMenuOpened(event);
			if ( event.isCancelled() ) {
				open = false;
				break;
			}
		}
		return open;
	}

	public void fireMenuOpened(final Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		final MenuOpenedEvent event = new MenuOpenedEvent();
		event.setWidget(widget);
		
		final Iterator listeners = this.iterator();

		while (listeners.hasNext()) {
			final MenuListener listener = (MenuListener) listeners.next();
			listener.onMenuOpened(event);
		}
	}

	public void add(final MenuListener menuListener) {
		this.getListeners().add(menuListener);
	}

	public void remove(final MenuListener menuListener) {
		this.getListeners().remove(menuListener);
	}

	protected Iterator iterator() {
		return this.getListeners().iterator();
	}

	private List listeners;

	protected List getListeners() {
		return listeners;
	}

	protected void setListeners(final List listeners) {
		this.listeners = listeners;
	}

	protected List createListeners() {
		return new ArrayList();
	}
}
