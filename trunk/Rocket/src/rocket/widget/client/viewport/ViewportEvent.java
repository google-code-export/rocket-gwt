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

import com.google.gwt.user.client.ui.Widget;

class ViewportEvent {
	/**
	 * The parent Viewport
	 */
	private Viewport viewport;
	
	public Viewport getViewport(){
		return this.viewport;
	}
	
	void setViewport( final Viewport viewport ){
		this.viewport = viewport;
	}
	
	/**
	 * The tile actually being dragged.
	 */
	private Widget tile;
	
	public Widget getTile(){
		return tile;
	}
	
	void setTile( final Widget tile ){
		this.tile = tile;
	}
}
