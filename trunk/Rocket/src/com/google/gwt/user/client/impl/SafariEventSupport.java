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
package com.google.gwt.user.client.impl;

import rocket.event.client.EventConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;

public class SafariEventSupport extends DOMImplSafari {

	public SafariEventSupport(){
		super();
		
		GWT.log( "Using SafariEventSupport instead of DOMImplSafari with extra key translation...", null );
	}
	
	  public int eventGetKeyCode(Event evt) {
		  int code = super.eventGetKeyCode(evt);

		  // translate some safari codes into standard codes...
		  while( code > 60000 ){
			  if( code == SafariConstants.CURSOR_LEFT){
				  code = EventConstants.CURSOR_LEFT;
				  break;
			  }
			  if( code == SafariConstants.CURSOR_UP){
				  code = EventConstants.CURSOR_UP;
				  break;
			  }
			  if( code == SafariConstants.CURSOR_RIGHT){
				  code = EventConstants.CURSOR_RIGHT;
				  break;
			  }
			  if( code == SafariConstants.CURSOR_DOWN){
				  code = EventConstants.CURSOR_DOWN;
				  break;
			  }
			  if( code == SafariConstants.DELETE){
				  code = EventConstants.DELETE;
				  break;
			  }			  
			  if( code == SafariConstants.END){
				  code = EventConstants.END;
				  break;
			  }
			  if( code == SafariConstants.HOME){
				  code = EventConstants.HOME;
				  break;
			  }
			  if( code == SafariConstants.HOME){
				  code = EventConstants.HOME;
				  break;
			  }	
			  if( code == SafariConstants.PAGE_UP){
				  code = EventConstants.PAGE_UP;
				  break;
			  }	
			  if( code == SafariConstants.PAGE_DOWN){
				  code = EventConstants.PAGE_DOWN;
				  break;
			  }		
			  if( code >= SafariConstants.FUNCTION_F1 && code <= SafariConstants.FUNCTION_F12 ){
				  code = code - SafariConstants.FUNCTION_F1 + EventConstants.FUNCTION_F1;
				  break;
			  }
			  
			  GWT.log( "Unknown safari code [" + code + "]", null );
			  break;
		  }
		  return code;
	  }
}