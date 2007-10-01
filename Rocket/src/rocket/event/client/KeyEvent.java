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
 * @author Miroslav Pokorny
 */
abstract class KeyEvent extends Event {
	public KeyEvent(){		
	}
	
	public char getKey(){
		return (char) DOM.eventGetKeyCode( this.getEvent() );
	}
	
	public void setKey( final char key ){
		DOM.eventSetKeyCode( this.getEvent(), key);
	}
	
	public boolean isShift(){
		return DOM.eventGetShiftKey( this.getEvent() );
	}
	
	public boolean isControl(){
		return DOM.eventGetCtrlKey( this.getEvent() );
	}
	
	public boolean isAlt(){
		return DOM.eventGetAltKey( this.getEvent() );
	}
	public boolean isMeta(){
		return DOM.eventGetMetaKey( this.getEvent() );
	}
	
	public boolean isRepeatedKey(){
		return DOM.eventGetRepeat( this.getEvent() );
	}
	
	public String toString(){
		final StringBuffer buf = new StringBuffer();
		
		buf.append( super.toString() );
		buf.append( ", key '" );
		buf.append( this.getKey() );
		buf.append( "' ");
		
		boolean addSeparator = false;
		if( this.isShift() ){
			buf.append( "shift");
			addSeparator = true;
		}
		if( this.isControl() ){
			if( addSeparator ){
				buf.append( '+');
			}
			buf.append( "control");
			addSeparator = true;
		}
		if( this.isAlt() ){
			if( addSeparator ){
				buf.append( '+');
			}
			buf.append( "alt+");
			addSeparator = true;
		}
		if( this.isMeta() ){
			if( addSeparator ){
				buf.append( '+');
			}
			buf.append( "meta");
			addSeparator = true;
		}
		return buf.toString();			
	}
}
