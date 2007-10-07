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

/**
 * This event is fired prior to a tab close happening..
 * 
 * @author Miroslav Pokorny
 */
public class BeforeTabCloseEvent {

	/**
	 * The tabItem about to be closed.
	 */
	private TabItem closing;

	public TabItem getClosing() {
		return closing;
	}

	void setClosing(final TabItem closing) {
		this.closing = closing;
	}

	private boolean cancelled;

	boolean isCancelled() {
		return this.cancelled;
	}

	void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * Invoking this method cancels the tab from being closing.
	 */
	public void stop() {
		this.setCancelled(true);
	}
}
