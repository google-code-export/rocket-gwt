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


/**
 * Convenient base class that implements all {@link MenuListener} methods with
 * do nothing methods.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class MenuListenerAdapter implements MenuListener {

	protected MenuListenerAdapter() {
		super();
	}

	public void onMenuCancelled(final MenuOpenCancelledEvent event ) {
	}

	public void onBeforeMenuOpened(final BeforeMenuOpenedEvent event ) {
	}

	public void onMenuOpened(final MenuOpenedEvent event ) {
	}
}
