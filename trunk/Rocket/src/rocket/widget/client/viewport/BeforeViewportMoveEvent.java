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


public class BeforeViewportMoveEvent extends ViewportEvent{	
	
	/**
	 * The deltaX
	 */
	private int deltaX;
	
	public int getDeltaX(){
		return this.deltaX;
	}
	
	void setDeltaX( final int deltaX ){
		this.deltaX = deltaX;
	}
	
	private int deltaY;
	
	public int getDeltaY(){
		return this.deltaY;
	}
	
	void setDeltaY( final int beforeY ){
		this.deltaY = beforeY;
	}
	
	private boolean cancelled;
	
	boolean isCancelled(){
		return this.cancelled;
	}
	void setCancelled( final boolean cancelled ){
		this.cancelled = cancelled;
	}
	
	/**
	 * Invoking this method cancels the start of a tile drag
	 */
	public void stop(){
		this.setCancelled( true );
	}
}
