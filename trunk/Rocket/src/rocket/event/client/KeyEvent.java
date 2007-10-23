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

import com.google.gwt.user.client.DOM;

/**
 * Package private class that adds the shared behaviour for all key events.
 * 
 * @author Miroslav Pokorny
 */
public class KeyEvent extends Event {
	public KeyEvent() {
	}

	public int getKey() {
	 return DOM.eventGetKeyCode(this.getEvent());
	}
	
	public char getChar(){
		return this.getChar0( this.getEvent() );
	}
	
	native private char getChar0( final com.google.gwt.user.client.Event event )/*-{
		return event.charCode || 0;
	}-*/;

	public void setKey(final int key) {
		DOM.eventSetKeyCode(this.getEvent(), (char)key);
	}

	public boolean isBackspace(){
		return this.getKey() == EventConstants.BACKSPACE;
	}
	public boolean isCursorLeft(){
		return this.getKey() == EventConstants.CURSOR_LEFT;
	}
	public boolean isCursorUp(){
		return this.getKey() == EventConstants.CURSOR_UP;
	}
	public boolean isCursorRight(){
		return this.getKey() == EventConstants.CURSOR_RIGHT;
	}
	public boolean isCursorDown(){
		return this.getKey() == EventConstants.CURSOR_DOWN;
	}
	public boolean isDelete(){
		return this.getKey() == EventConstants.DELETE;
	}
	public boolean isEnd(){
		return this.getKey() == EventConstants.END;
	}
	public boolean isEnter(){
		return this.getKey() == EventConstants.ENTER;
	}
	public boolean isEscape(){
		return this.getKey() == EventConstants.ESCAPE;
	}
	public boolean isHome(){
		return this.getKey() == EventConstants.HOME;
	}
	public boolean isInsert(){
		return this.getKey() == EventConstants.INSERT;
	}
	public boolean isPageDown(){
		return this.getKey() == EventConstants.PAGE_DOWN;
	}
	public boolean isPageUp(){
		return this.getKey() == EventConstants.PAGE_UP;
	}
	public boolean isTab(){
		return this.getKey() == EventConstants.TAB;
	}
	/**
	 * If the key was a function key return 1 thru 12 otherwise returns -1
	 * @return
	 */
	
	public int getFunctionKey(){
		final int key = this.getKey();
		return key < EventConstants.FUNCTION_F1 ? -1 : key > EventConstants.FUNCTION_F12 ? -1 : key - EventConstants.FUNCTION_F1 + 1;
	}
	
	public boolean isShift() {
		return DOM.eventGetShiftKey(this.getEvent());
	}

	public boolean isControl() {
		return DOM.eventGetCtrlKey(this.getEvent());
	}

	public boolean isAlt() {
		return DOM.eventGetAltKey(this.getEvent());
	}

	public boolean isMeta() {
		return DOM.eventGetMetaKey(this.getEvent());
	}

	public boolean isRepeatedKey() {
		return DOM.eventGetRepeat(this.getEvent());
	}

	public String toString() {
		final StringBuffer buf = new StringBuffer();

		buf.append(super.toString());
		buf.append(", key '");
		buf.append(this.getKey());
		buf.append("' ");

		boolean addSeparator = false;
		if (this.isShift()) {
			buf.append("shift");
			addSeparator = true;
		}
		if (this.isControl()) {
			if (addSeparator) {
				buf.append('+');
			}
			buf.append("control");
			addSeparator = true;
		}
		if (this.isAlt()) {
			if (addSeparator) {
				buf.append('+');
			}
			buf.append("alt+");
			addSeparator = true;
		}
		if (this.isMeta()) {
			if (addSeparator) {
				buf.append('+');
			}
			buf.append("meta");
			addSeparator = true;
		}
		return buf.toString();
	}
}