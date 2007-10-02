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

public class MouseEventListenerCollection extends ListenerCollection {
	public void add( final MouseEventListener listener ){
		super.add( listener );
	}
	public boolean remove( final MouseEventListener listener ){
		return super.remove( listener );
	}
	
	public void fireClick( final MouseClickEvent event ){
		this.fire( new ListenerCollection.EventFiringAdapter(){
			public void fire( final Object listener ){
				final MouseEventListener listener0 = (MouseEventListener)listener;
				listener0.onClick( event );
			}
		});
	}
	

	public void fireDoubleClick( final MouseDoubleClickEvent event ){
		this.fire( new ListenerCollection.EventFiringAdapter(){
			public void fire( final Object listener ){
				final MouseEventListener listener0 = (MouseEventListener)listener;
				listener0.onDoubleClick( event );
			}
		});
	}
	
	public void fireMouseDown( final MouseDownEvent event ){
		this.fire( new ListenerCollection.EventFiringAdapter(){
			public void fire( final Object listener ){
				final MouseEventListener listener0 = (MouseEventListener)listener;
				listener0.onMouseDown( event );
			}
		});
	}
	

	public void fireMouseMove( final MouseMoveEvent event ){
		this.fire( new ListenerCollection.EventFiringAdapter(){
			public void fire( final Object listener ){
				final MouseEventListener listener0 = (MouseEventListener)listener;
				listener0.onMouseMove( event );
			}
		});
	}
	
	public void fireMouseOut( final MouseOutEvent event ){
		this.fire( new ListenerCollection.EventFiringAdapter(){
			public void fire( final Object listener ){
				final MouseEventListener listener0 = (MouseEventListener)listener;
				listener0.onMouseOut( event );
			}
		});
	}
	
	public void fireMouseOver( final MouseOverEvent event ){
		this.fire( new ListenerCollection.EventFiringAdapter(){
			public void fire( final Object listener ){
				final MouseEventListener listener0 = (MouseEventListener)listener;
				listener0.onMouseOver( event );
			}
		});
	}
	public void fireMouseUp( final MouseUpEvent event ){
		this.fire( new ListenerCollection.EventFiringAdapter(){
			public void fire( final Object listener ){
				final MouseEventListener listener0 = (MouseEventListener)listener;
				listener0.onMouseUp( event );
			}
		});
	}
	public void fireMouseWheel( final MouseWheelEvent event ){
		this.fire( new ListenerCollection.EventFiringAdapter(){
			public void fire( final Object listener ){
				final MouseEventListener listener0 = (MouseEventListener)listener;
				listener0.onMouseWheel( event );
			}
		});
	}
}
