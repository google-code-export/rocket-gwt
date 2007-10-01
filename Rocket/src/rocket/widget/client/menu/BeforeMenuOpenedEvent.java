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

import com.google.gwt.user.client.ui.Widget;

public class BeforeMenuOpenedEvent {
	
	private Widget widget;
	
	public Widget getWidget(){
		return this.widget;
	}
	void setWidget( final Widget widget ){
		this.widget = widget;
	}
	

	/**
	 * This flag indicates that the menu should not be opened.
	 */
	private boolean cancelled;
	
	boolean isCancelled(){
		return this.cancelled;
	}
	void setCancelled( final boolean cancelled ){
		this.cancelled = cancelled;
	}
	
	/**
	 * Invoking this method stops or ignores the menu opening from happening.
	 */
	public void stop(){
		this.setCancelled( true );
	}
}
