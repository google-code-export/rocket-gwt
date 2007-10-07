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
 * A convenience that implements do nothing methods of the
 * {@link MouseEventListener} interface. Sub classes can then select which
 * method they wish to override.
 * 
 * @author Miroslav Pokorny
 */
abstract public class MouseEventAdapter implements MouseEventListener {

	public void onClick(MouseClickEvent event) {
	}

	public void onDoubleClick(MouseDoubleClickEvent event) {
	}

	public void onMouseDown(MouseDownEvent event) {
	}

	public void onMouseMove(MouseMoveEvent event) {
	}

	public void onMouseOut(MouseOutEvent event) {
	}

	public void onMouseOver(MouseOverEvent event) {
	}

	public void onMouseUp(MouseUpEvent event) {
	}

	public void onMouseWheel(MouseWheelEvent event) {
	}
}
