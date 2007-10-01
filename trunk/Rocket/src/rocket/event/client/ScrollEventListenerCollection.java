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

public class ScrollEventListenerCollection extends ListenerCollection {
	public void add( final ScrollEventListener listener ){
		super.add( listener );
	}
	public void remove( final ScrollEventListener listener ){
		super.remove( listener );
	}
	
	public void fireScroll( final ScrollEvent event ){
		this.fire( new ListenerCollection.EventFiringAdapter(){
			public void fire( final Object listener ){
				final ScrollEventListener listener0 = (ScrollEventListener)listener;
				listener0.onScroll( event );
			}
		});
	}
}
