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

import rocket.util.client.ObjectHelper;

/**
 * A collection of view port listeners, includes a number of methods to fire all events.
 * @author Miroslav Pokorny
 */
class ViewportListenerCollection extends ArrayList {
	
	public boolean fireBeforeDragStarted( final Viewport viewport ){
		ObjectHelper.checkNotNull( "parameter:viewport", viewport );
		
		boolean cancel = false;
		final Iterator listeners = this.iterator();
		while( listeners.hasNext() ){
			final ViewportListener listener = (ViewportListener) listeners.next();
			cancel = listener.onBeforeDragStarted( viewport );
			
			if( cancel ){
				break;
			}
		}
		return cancel;
	}
	
	public void fireDragStarted( final Viewport viewport ){
		ObjectHelper.checkNotNull( "parameter:viewport", viewport );
		
		final Iterator listeners = this.iterator();
		while( listeners.hasNext() ){
			final ViewportListener listener = (ViewportListener) listeners.next();
			listener.onDragStarted( viewport );			
		}
	}

	public boolean fireBeforeDragMove( final Viewport viewport ){
		ObjectHelper.checkNotNull( "parameter:viewport", viewport );
		
		boolean cancel = false;
		final Iterator listeners = this.iterator();
		while( listeners.hasNext() ){
			final ViewportListener listener = (ViewportListener) listeners.next();
			cancel = listener.onBeforeDragMove( viewport );
			
			if( cancel ){
				break;
			}
		}
		
		return cancel;
	}
	
	public void fireDragMoved( final Viewport viewport ){
		ObjectHelper.checkNotNull( "parameter:viewport", viewport );
		
		final Iterator listeners = this.iterator();
		while( listeners.hasNext() ){
			final ViewportListener listener = (ViewportListener) listeners.next();
			listener.onDragMoved( viewport );			
		}
	}
		
	public void fireDragStopped( final Viewport viewport ){
		ObjectHelper.checkNotNull( "parameter:viewport", viewport );
		
		final Iterator listeners = this.iterator();
		while( listeners.hasNext() ){
			final ViewportListener listener = (ViewportListener) listeners.next();
			listener.onDragStopped( viewport );			
		}
	}
}
