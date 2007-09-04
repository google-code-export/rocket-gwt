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
package rocket.widget.client.viewport;

/**
 * This interface defines a number of methods that allow fine grained control
 * over the viewport and attempts by the user to scroll the viewport by drag its
 * contents.
 * 
 * @author Miroslav Pokorny
 */
public interface ViewportListener {
	/**
	 * This method is invoked before dragging starts. It gives an opportunity
	 * for listeners to potentially cancel any drag.
	 * 
	 * @param viewport
	 *            The viewport
	 * @return Returning true cancels the attempt to drag.
	 */
	boolean onBeforeDragStarted(Viewport viewport);

	/**
	 * This method is invoked whenever the user starts a drag.
	 * 
	 * @param viewport
	 *            The viewport
	 */
	void onDragStarted(Viewport viewport);

	/**
	 * This method is invoked each time the user moves or drags a tile.
	 * 
	 * @param viewport
	 *            The viewport
	 * @return Returning true cancels or ignores the pending scrolling update.
	 */
	boolean onBeforeDragMove(Viewport viewport);

	/**
	 * Each time the viewport moves this method is called.
	 * 
	 * @param viewport
	 */
	void onDragMoved(Viewport viewport);

	/**
	 * This method is invoked whenever the user stops a drag.
	 * 
	 * @param viewport
	 *            The viewport
	 */
	void onDragStopped(Viewport viewport);

}
