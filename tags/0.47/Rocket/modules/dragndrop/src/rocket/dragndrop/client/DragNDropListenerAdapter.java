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
package rocket.dragndrop.client;

/**
 * Convenient start for any DragNDropListener in which all its methods do
 * nothing. This is particularly useful when only one method needs to be
 * implemented.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class DragNDropListenerAdapter implements DragNDropListener {

	public void onDragStart(DragStartEvent event) {
	}

	public void onDragMove(final DragMoveEvent event) {
	}

	public void onDrop(final DropEvent event) {
	}
}
