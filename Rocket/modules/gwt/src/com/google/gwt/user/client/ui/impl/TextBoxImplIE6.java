/*
 * Copyright 2007 Google Inc.
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
package com.google.gwt.user.client.ui.impl;

import com.google.gwt.user.client.Element;

/**
 * IE6-specific implementation of
 * {@link com.google.gwt.user.client.ui.impl.TextBoxImpl}.
 */
public class TextBoxImplIE6 extends TextBoxImpl {

	@Override
	public native int getCursorPos(Element elem) /*-{
	   try {
	     var tr = elem.document.selection.createRange();
	     if (tr.parentElement() !== elem)
	       return -1;
	     return -tr.move("character", -65535);
	   }
	   catch (e) {
	     return 0;
	   }
	 }-*/;

	@Override
	public native int getSelectionLength(Element elem) /*-{
	   try {
	     var tr = elem.document.selection.createRange();
	     if (tr.parentElement() !== elem)
	       return 0;
	     return tr.text.length;
	   }
	   catch (e) {
	     return 0;
	   }
	 }-*/;

	// @Override
	// public native int getTextAreaCursorPos(Element elem) /*-{
	// try {
	// var tr = elem.document.selection.createRange();
	// var tr2 = tr.duplicate();
	// tr2.moveToElementText(elem);
	// tr.setEndPoint('EndToStart', tr2);
	// return tr.text.length;
	// }
	// catch (e) {
	// return 0;
	// }
	// }-*/;

	@Override
	public native void setSelectionRange(Element elem, int pos, int length) /*-{
	   try {
	     var tr = elem.createTextRange();
	     tr.collapse(true);
	     tr.moveStart('character', pos);
	     tr.moveEnd('character', length);
	     tr.select();
	   }
	   catch (e) {
	   }
	 }-*/;

	// ROCKET The fields/methods below were added to assist the Rocket
	// framework. When upgrading from GWT 1.5.2 reapply changes

	/**
	 * Because of the changes to getTextAreaCursor it is possible for
	 * TextArea.getSelectedText() to throw exceptions because the cursor
	 * position + selection length end up being greater than the actual length
	 * of the text area text.
	 * 
	 * This method includes a simple guard and adjusts selection length to
	 * ensure the above mentioned exceptions never happen.
	 */
	native public int getTextAreaSelectionLength(final Element elem)/*-{
	  	var selectionLength = 0;
	    try {
	      var tr = elem.document.selection.createRange();
	      if (tr.parentElement().uniqueID != elem.uniqueID){
	        return 0;
	      }
	      selectionLength = tr.text.length;           
	      
	      // if text + pos is over the end of elem.value adjust selectionLength.
	      var pos = this.@com.google.gwt.user.client.ui.impl.TextBoxImplIE6::getTextAreaCursorPos(Lcom/google/gwt/user/client/Element;)(elem);
	      var textLength = elem.value.length;
	      var end = pos + selectionLength;
	      if( end >= textLength ){
	      	selectionLength = textLength - pos;
	      }
	      
	    }
	    catch (e) {
	      selectionLength = 0;
	    }
	    return selectionLength;
	  }-*/;

	/**
	 * Retrieving the cursor position within a TextArea is troublesome
	 * particularly if the cursor is after one or more CR or NL. Unfortunately
	 * IE reports the position of a cursor at the end of a line and at the start
	 * of the next line as the same value, making it impossible to say advance
	 * the cursor by a position of 1.
	 * 
	 * This hack fixes the case described above so the reported cursor position
	 * will point to the first char on a new line, rather than the end of the
	 * previous. The down side is you end of line cursors appear to be on the
	 * start of the next line.
	 * 
	 * This has the added advantage it is possible to insert text into a text
	 * area at the cursor position without any fudges.
	 */
	@Override
	public native int getTextAreaCursorPos(final Element elem) /*-{
	   try {         
	     var tr = elem.document.selection.createRange();
	     var tr2 = tr.duplicate();
	     tr2.moveToElementText(elem);
	     tr.setEndPoint('EndToStart', tr2);
	     var position = tr.text.length;
	     
	     var text = elem.value;
	     var lengthLessOne = text.length -1; 
	     
	     while( position < lengthLessOne){
	     	var c = text.charAt( position );      	
	     	if( c == '\r' || c == '\n' ){
	     		position++;
	     		continue;
	     	}
	     	
	     	// not nl or cr
	     	break;
	     }
	     
	     return position;
	   }
	   catch (e) {
	     return 0;
	   }
	 }-*/;

}
