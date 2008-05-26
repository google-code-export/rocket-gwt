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
package rocket.widget.client.menu;

import rocket.event.client.MouseEvent;
import rocket.util.client.Checker;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;


/**
 * This event is fired just prior to a context menu opening.
 * @author Miroslav Pokorny
 */
public class BeforeContextMenuOpenEvent extends BeforeMenuOpenEvent{
	
	public ContextMenu getContextMenu(){
		return (ContextMenu)this.getMenu();
	}
	
	/**
	 * May be used to test and retrieve the context menu widget if its clicking triggered the showing of the context menu.
	 * 
	 * @return May be null.
	 */
	public Widget getContextMenuWidget() {
		final Widget widget = this.getWidget();
		return widget instanceof MenuItem || widget instanceof SubMenuItem ? null : widget; 
	}
	
	/**
	 * The element that recieved the initial right mouse click.
	 * 
	 * To learn of the widget that was clicked upon get the MenuItem 
	 */
	private Element initialTargetElement;
	
	public Element getInitialTargetElement(){
		Checker.notNull( "field:initialTargetElement", initialTargetElement );
		return this.initialTargetElement;
	}
	
	void setInitialTargetElement( final Element initialTargetElement ){
		Checker.notNull( "parameter:initialTargetElement", initialTargetElement );
		this.initialTargetElement = initialTargetElement;
	}
	
	/**
	 * The widget that recieved the initial right mouse click. 
	 */
	private Widget initialTargetWidget;
	
	public Widget getInitialTargetWidget(){
		Checker.notNull( "field:initialTargetWidget", initialTargetWidget );
		return this.initialTargetWidget;
	}
	
	void setInitialTargetWidget( final Widget initialTargetWidget ){
		Checker.notNull( "parameter:initialTargetWidget", initialTargetWidget );
		this.initialTargetWidget = initialTargetWidget;
	}
	
	
	public int getClientX(){
		return this.getMouseEvent().getClientX();
	}
	public int getClientY(){
		return this.getMouseEvent().getClientY();
	}
	
	public int getPageX(){
		return this.getMouseEvent().getPageX();
	}
	public int getPageY(){
		return this.getMouseEvent().getPageY();
	}
	
	public int getScreenX(){
		return this.getMouseEvent().getScreenX();
	}
	public int getScreenY(){
		return this.getMouseEvent().getScreenY();
	}
	
	public int getElementX(){
		return this.getMouseEvent().getTargetElementX();
	}
	public int getElementY(){
		return this.getMouseEvent().getTargetElementY();
	}
	
	/**
	 * The source MouseEvent provider of all mouse event details.
	 */
	protected MouseEvent mouseEvent;
	
	protected MouseEvent getMouseEvent(){
		Checker.notNull( "field:mouseEvent", mouseEvent );
		return this.mouseEvent;
	}
	
	void setMouseEvent( final MouseEvent mouseEvent){
		Checker.notNull( "parameter:mouseEvent", mouseEvent );
		this.mouseEvent = mouseEvent;
	}
}
