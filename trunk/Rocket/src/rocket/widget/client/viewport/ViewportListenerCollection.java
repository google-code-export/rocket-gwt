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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A collection of view port listeners, includes a number of methods to fire all
 * events.
 * 
 * @author Miroslav Pokorny
 */
class ViewportListenerCollection extends ArrayList {

	public void fireBeforeMoveStarted(final BeforeViewportDragStartEvent event ) {		
		final Iterator listeners = this.iterator();
		
		while (listeners.hasNext()) {
			final ViewportListener listener = (ViewportListener) listeners.next();
			listener.onBeforeDragStart( event );

			if (event.isCancelled()) {
				break;
			}
		}
	}

	public void fireMoveStarted(final ViewportDragStartEvent event ) {				
		final Iterator listeners = this.iterator();
		while (listeners.hasNext()) {
			final ViewportListener listener = (ViewportListener) listeners.next();
			
			listener.onDragStart(event);
		}
	}

	public void fireBeforeMove( final BeforeViewportMoveEvent event ) {		
		final Iterator listeners = this.iterator();
		
		while (listeners.hasNext()) {
			final ViewportListener listener = (ViewportListener) listeners.next();
			listener.onBeforeMove(event);

			if (event.isCancelled()) {
				break;
			}
		}
	}

	public void fireMoved(final ViewportMoveEvent event ) {
		final Iterator listeners = this.iterator();
		
		while (listeners.hasNext()) {
			final ViewportListener listener = (ViewportListener) listeners.next();
			listener.onMoved(event);
		}
	}

	public void fireMoveStopped(final ViewportDragStopEvent event) {
		final Iterator listeners = this.iterator();
		
		while (listeners.hasNext()) {
			final ViewportListener listener = (ViewportListener) listeners.next();
			listener.onDragStop(event);
		}
	}
}
