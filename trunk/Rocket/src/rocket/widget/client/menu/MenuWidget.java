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

import java.util.Stack;

import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.event.client.MouseOutEvent;
import rocket.event.client.MouseOverEvent;
import rocket.util.client.ObjectHelper;
import rocket.widget.client.CompositeWidget;

/**
 * Base class for all MenuItem type classes including MenuItem, SubMenuItem and
 * MenuSpacer.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract class MenuWidget extends CompositeWidget {

	protected MenuWidget() {
		super();
	}
	
	protected void afterCreateWidget() {
		this.getEventListenerDispatcher().addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				MenuWidget.this.handleMouseClick(event);
			}

			public void onMouseOut(final MouseOutEvent event) {
				MenuWidget.this.handleMouseOut(event);
			}

			public void onMouseOver(final MouseOverEvent event) {
				MenuWidget.this.handleMouseOver(event);
			}
		});
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.MOUSE_CLICK | EventBitMaskConstants.MOUSE_OVER | EventBitMaskConstants.MOUSE_OUT;
	}

	// ACTION :::::::::::::::::::::::::::::::::::::::::::::::::::

	abstract protected void hide();

	/**
	 * This method is fired whenever a menuWidget receives a mouse click
	 * 
	 * @param event
	 */
	abstract protected void handleMouseClick(final MouseClickEvent event);

	/**
	 * This method is fired whenever this menu widget receives a mouse out event
	 * 
	 * @param event
	 */
	abstract protected void handleMouseOut(final MouseOutEvent event);

	/**
	 * This method is fired whenever this menu widget receives a mouse over
	 * event
	 * 
	 * @param event
	 */
	abstract protected void handleMouseOver(final MouseOverEvent event);


	protected void onDetach() {
		super.onDetach();

		this.hide();
	}

	// ACTIONS
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	protected void addHighlight() {
		this.addStyleName(this.getSelectedStyle());
	}

	protected void removeHighlight() {
		this.removeStyleName(this.getSelectedStyle());
	}

	/**
	 * This style is added or removed depending on whether the widget is
	 * highlighted.
	 * 
	 * @return
	 */
	protected abstract String getSelectedStyle();

	/**
	 * WHen a widget is disabled all events are ignored.
	 */
	private boolean disabled;

	public boolean isDisabled() {
		return this.disabled;
	}

	public void setDisabled(final boolean disabled) {
		final String disabledStyle = this.getDisabledStyle();
		if (this.isDisabled()) {
			this.removeStyleName(disabledStyle);
		}
		this.disabled = disabled;
		if (disabled) {
			this.addStyleName(disabledStyle);
		}
	}

	/**
	 * Retrieves the style that is added or removed depending on whether the
	 * widget is disabled.
	 * 
	 * @return
	 */
	protected abstract String getDisabledStyle();

	/**
	 * The parent menuList that contains this MenuItem.
	 */
	private MenuList parentMenuList;

	protected MenuList getParentMenuList() {
		ObjectHelper.checkNotNull("field:parentMenuList", parentMenuList);
		return this.parentMenuList;
	}
	protected boolean hasParentMenuList(){
		return null != this.parentMenuList;
	}
	protected void setParentMenuList(final MenuList parentMenuList) {
		ObjectHelper.checkNotNull("parameter:parentMenuList", parentMenuList);
		this.parentMenuList = parentMenuList;
	}
	protected void clearParentMenuList(){
		this.parentMenuList = null;
	}
	
	public String toString(){
		final StringBuffer buf = new StringBuffer();
		buf.append( ObjectHelper.defaultToString( this ));
		buf.append( " [");	
		
		if( this.hasParentMenuList() ){
			MenuList parent = this.getParentMenuList();
					
			final Stack stack = new Stack();
			while( true ){
				if( false == parent.hasParentMenuList() ){
					break;
				}
				parent = parent.getParentMenuList();
				
				if( false == parent.hasOpened() ){
					break;
				}
				stack.push( parent.getOpened().getText() );				
			}
						
			while( false == stack.isEmpty() ){
				buf.append( stack.pop() );
				buf.append( ">");
			}
		}
		
		buf.append( this.toString0() );
		buf.append( "]");
		return buf.toString();
	}
	
	abstract String toString0();
}
