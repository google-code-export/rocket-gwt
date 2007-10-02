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

import com.google.gwt.user.client.ui.Widget;

public class ChangeEventListenerCollection extends ListenerCollection {
	public void add( final ChangeEventListener listener ){
		super.add( listener );
	}
	public boolean remove( final ChangeEventListener listener ){
		return super.remove( listener );
	}
	
	public void fireChange( final Widget sender ){
		this.fireChange( (ChangeEvent) Event.getEvent( sender , EventBitMaskConstants.CHANGE ));
	}
	
	
	public void fireChange( final ChangeEvent event ){
		this.fire( new ListenerCollection.EventFiringAdapter(){
			public void fire( final Object listener ){
				final ChangeEventListener listener0 = (ChangeEventListener)listener;
				listener0.onChange( event );
			}
		});
	}
}
