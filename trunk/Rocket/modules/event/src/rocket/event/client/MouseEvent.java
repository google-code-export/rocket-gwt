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

import rocket.browser.client.Browser;

/**
 * Contains a number of common mouse event related methods including
 * <ul>
 * <li>Fetching the mouse coordinates relative to the client (visible browser window)</li>
 * <li>Fetching the mouse coordinates relative to the entire page.</li>
 * <li>Fetching the mouse screen coordinates (pretty useless...)</li>
 * <li>Fetching the mouse coordinates relative to the event's target element</li>
 * </ul>
 * @author Miroslav Pokorny
 */
public class MouseEvent extends Event {
	public MouseEvent() {
	}

	public int getClientX() {
		return this.getEvent().getClientX();
	}

	public int getClientY() {
		return this.getEvent().getClientY();
	}

	public int getPageX() {
		return Browser.getMousePageX(this.getEvent());
	}

	public int getPageY() {
		return Browser.getMousePageY(this.getEvent());
	}

	public int getScreenX() {
		return this.getEvent().getScreenX(); 
	}

	public int getScreenY() {
		return this.getEvent().getScreenY(); 
	}
	
	/**
	 * Returns the coordinates of the mouse relative to the element that was the target of the mouse event.
	 * @return The relative x coordinate value.
	 */
	public int getTargetElementX(){
		return this.getPageX() - this.getTarget().getAbsoluteLeft();
	}
	public int getTargetElementY(){
		return this.getPageY() - this.getTarget().getAbsoluteTop();
	}

	@Override
	public String toString(){
		return super.toString() + ", pageCoordinates: " + this.getPageX() + "," + this.getPageY();
	}
}
