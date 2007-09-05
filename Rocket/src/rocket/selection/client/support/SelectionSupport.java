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
package rocket.selection.client.support;

import rocket.selection.client.SelectionEndPoint;
import rocket.style.client.ComputedStyle;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

/**
 * This class provides the standard implementation of
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class SelectionSupport {

	/**
	 * Updates the selectability of the given element.
	 * 
	 * @param element
	 * @param enable
	 */
	public void setEnabled(final Element element, final boolean enable) {
		if (enable) {
			InlineStyle.remove(element, StyleConstants.USER_SELECT);
		} else {
			InlineStyle.setString(element, StyleConstants.USER_SELECT, "none");
		}
	}

	/**
	 * Tests if the given element has selection enabled.
	 * 
	 * @param element
	 * @return
	 */
	public boolean isEnabled(final Element element) {
		return false == StyleConstants.USER_SELECT_DISABLED.equals(ComputedStyle.getString(element, StyleConstants.USER_SELECT));
	}

	public SelectionEndPoint getStart() {
		final JavaScriptObject selection = this.getNativeSelection();

		final SelectionEndPoint start = new SelectionEndPoint();
		start.setTextNode(ObjectHelper.castToElement(ObjectHelper.getObject(selection, Constants.ANCHOR_NODE)));
		start.setOffset(ObjectHelper.getInteger(selection, Constants.ANCHOR_OFFSET));
		return start;
	}

	public void setStart(final SelectionEndPoint start) {
		ObjectHelper.checkNotNull("parameter:start", start);

		setStart0(this.getNativeSelection(), start.getTextNode(), start.getOffset());
	}

	native private void setStart0(final JavaScriptObject selection, final JavaScriptObject textNode, final int offset)/*-{                        
	 // if an existing end exists use that otherwise set the end to the new start
	 var endNode = selection.focusNode;
	 var endOffset = selection.focusOffset;
	 if( ! endNode ){
	 endNode = textNode;
	 endOffset = offset;
	 }
	 
	 var range = $doc.createRange();
	 range.setStart( textNode, offset );        

	 range.setEnd( endNode, endOffset );
	 
	 // delete all ranges then recreate...
	 selection.removeAllRanges();
	 selection.addRange( range );
	 }-*/;

	public SelectionEndPoint getEnd() {
		final JavaScriptObject selection = this.getNativeSelection();

		final SelectionEndPoint end = new SelectionEndPoint();
		end.setTextNode(ObjectHelper.castToElement(ObjectHelper.getObject(selection, Constants.FOCUS_NODE)));
		end.setOffset(ObjectHelper.getInteger(selection, Constants.FOCUS_OFFSET));
		return end;
	}

	public void setEnd(final SelectionEndPoint end) {
		ObjectHelper.checkNotNull("parameter:end", end);

		setEnd0(this.getNativeSelection(), end.getTextNode(), end.getOffset());
	}

	native private void setEnd0(final JavaScriptObject selection, final JavaScriptObject textNode, final int offset)/*-{
	 // if an existing selection exists use that otherwise set the start to the new end.
	 var startNode = selection.anchorNode;
	 var startOffset = selection.anchorOffset;
	 if( ! startNode ){
	 startNode = textNode;
	 startOffset = offset;
	 }

	 // create a new range that will join the old end and the new start...
	 var range = $doc.createRange();
	 range.setStart( startNode, startOffset );
	 
	 range.setEnd( textNode, offset );        
	 
	 // delete all ranges then recreate...
	 selection.removeAllRanges();
	 selection.addRange( range );
	 }-*/;

	public boolean isEmpty() {
		return ObjectHelper.getBoolean(this.getNativeSelection(), Constants.IS_COLLAPSED);
	}

	public void clear() {
		final JavaScriptObject selection = this.getNativeSelection();
		this.clear0(selection);
	}

	native private void clear0(final JavaScriptObject selection)/*-{
	 selection.removeAllRanges();
	 }-*/;

	/**
	 * Sub-classes must implement this method to return a reference to the
	 * documents selection object.
	 * 
	 * An abstract method is necessary because each of the browsers have their
	 * respective selection object in different parts of the DOM.
	 * 
	 * @return
	 */
	abstract protected JavaScriptObject getNativeSelection();

	/**
	 * Extracts any selection and makes it a child of an element not attached to
	 * the DOM.
	 * 
	 * @param element
	 */
	public Element extract() {
		return this.extract0(this.getNativeSelection());
	}

	native private Element extract0(final JavaScriptObject selection)/*-{
	 var element = $doc.createElement( "SPAN" );
	 var range = selection.getRangeAt( 0 );
	 if( range ){
	 range.surroundContents( element );
	 }
	 return element;
	 }-*/;

	final public void surround(final Element element) {
		if (this.isEmpty()) {
			throw new IllegalStateException("No selection exists unable to perform surround");
		}
		this.surround0(element);
	}

	/**
	 * This method and the native javascript assume that a selection exists.
	 * 
	 * @param element
	 */
	protected void surround0(final Element element) {
		this.surround1(this.getNativeSelection(), element);
	}

	native protected void surround1(final JavaScriptObject selection, final Element element)/*-{
	 var range = selection.getRangeAt( 0 );
	 range.surroundContents( element );        
	 }-*/;

	/**
	 * Deletes or removes the selected dom objects from the object.
	 * 
	 */
	public void delete() {
		if (this.isEmpty()) {
			throw new IllegalStateException("No selection exists unable to perform delete");
		}
		this.delete0(this.getNativeSelection());
		this.clear();
	}

	native private void delete0(final JavaScriptObject selection)/*-{
	 var range = selection.getRangeAt( 0 );
	 range.deleteContents();     
	 }-*/;

	public void clearAnySelectedText() {
		this.clearAnySelectedText0(this.getNativeSelection());
	}

	native private void clearAnySelectedText0(final JavaScriptObject selection)/*-{
	 selection.removeAllRanges();
	 }-*/;
}
