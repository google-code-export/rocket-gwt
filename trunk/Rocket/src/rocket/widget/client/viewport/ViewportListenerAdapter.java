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
 * Adapter that overrides all the ViewportListener methods with do nothing
 * methods
 * 
 * @author Miroslav Pokorny
 */
abstract public class ViewportListenerAdapter implements ViewportListener {
	
	public void onBeforeDragStart( BeforeViewportDragStartEvent event){		
	}
	public void onDragStart(ViewportDragStartEvent event){	
	}
	public void onBeforeMove(BeforeViewportMoveEvent event){
	}
	public void onMoved(ViewportMoveEvent event){		
	}
	public void onDragStop(ViewportDragStopEvent event){	
	}
}
