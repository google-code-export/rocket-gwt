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

/**
 * A common base class for any mouse button event.
 * 
 * @author Miroslav Pokorny
 */
class MouseButtonEvent extends MouseEvent {

	public MouseButtonEvent() {
	}

	public boolean isLeftButton() {
		return com.google.gwt.user.client.Event.BUTTON_LEFT == this.getEvent().getButton();
	}

	public boolean isMiddleButton() {
		return com.google.gwt.user.client.Event.BUTTON_MIDDLE == this.getEvent().getButton();
	}

	public boolean isRightButton() {
		return com.google.gwt.user.client.Event.BUTTON_RIGHT == this.getEvent().getButton();
	}

	@Override
	public String toString() {
		return super.toString() + ", button="
				+ (this.isLeftButton() ? " Left" : (this.isMiddleButton() ? " Middle" : (this.isRightButton() ? " Right" : "???")));
	}
}
