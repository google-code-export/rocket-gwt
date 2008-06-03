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

import rocket.selection.client.Selection;
import rocket.selection.client.SelectionEndPoint;
import rocket.style.client.ComputedStyle;
import rocket.style.client.Css;
import rocket.style.client.InlineStyle;
import rocket.util.client.Checker;
import rocket.util.client.JavaScript;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Text;
import com.google.gwt.user.client.Element;

/**
 * This class provides the standard implementation of
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class SelectionSupport {

	abstract public Selection getSelection(JavaScriptObject window);

	/**
	 * Updates the selectability of the given element.
	 * 
	 * @param element
	 * @param enable
	 */
	public void setEnabled(final Element element, final boolean enable) {
		if (enable) {
			InlineStyle.remove(element, Css.USER_SELECT);
		} else {
			InlineStyle.setString(element, Css.USER_SELECT, "none");
		}
	}

	/**
	 * Tests if the given element has selection enabled.
	 * 
	 * @param element
	 * @return
	 */
	public boolean isEnabled(final Element element) {
		return false == Css.USER_SELECT_DISABLED.equals(ComputedStyle.getString(element, Css.USER_SELECT));
	}

	public SelectionEndPoint getStart(final Selection selection) {
		final SelectionEndPoint start = new SelectionEndPoint();
		start.setTextNode((Text) JavaScript.getObject(selection, Constants.ANCHOR_NODE).cast());
		start.setOffset(JavaScript.getInteger(selection, Constants.ANCHOR_OFFSET));
		return start;
	}

	public void setStart(final Selection selection, final SelectionEndPoint start) {
		Checker.notNull("parameter:selection", selection);
		Checker.notNull("parameter:start", start);

		setStart0(selection, start.getTextNode(), start.getOffset());
	}

	native private void setStart0(final Selection selection, final Text textNode, final int offset)/*-{                        
		 // if an existing end exists use that otherwise set the end to the new start
		 var endNode = selection.focusNode;
		 var endOffset = selection.focusOffset;
		 if( ! endNode ){
		 endNode = textNode;
		 endOffset = offset;
		 }
		 
		 var range = textNode.ownerDocument.createRange();
		 range.setStart( textNode, offset );        

		 range.setEnd( endNode, endOffset );
		 
		 // delete all ranges then recreate...
		 selection.removeAllRanges();
		 selection.addRange( range );
		 }-*/;

	public SelectionEndPoint getEnd(final Selection selection) {
		final SelectionEndPoint end = new SelectionEndPoint();
		end.setTextNode((Text) JavaScript.getObject(selection, Constants.FOCUS_NODE).cast());
		end.setOffset(JavaScript.getInteger(selection, Constants.FOCUS_OFFSET));
		return end;
	}

	public void setEnd(final Selection selection, final SelectionEndPoint end) {
		Checker.notNull("parameter:selection", selection);
		Checker.notNull("parameter:end", end);

		setEnd0(selection, end.getTextNode(), end.getOffset());
	}

	native private void setEnd0(final Selection selection, final Text textNode, final int offset)/*-{
		 // if an existing selection exists use that otherwise set the start to the new end.
		 var startNode = selection.anchorNode;
		 var startOffset = selection.anchorOffset;
		 if( ! startNode ){
		 startNode = textNode;
		 startOffset = offset;
		 }

		 // create a new range that will join the old end and the new start...
		 var range = textNode.ownerDocument.createRange();
		 range.setStart( startNode, startOffset );
		 
		 range.setEnd( textNode, offset );        
		 
		 // delete all ranges then recreate...
		 selection.removeAllRanges();
		 selection.addRange( range );
		 }-*/;

	public boolean isEmpty(final Selection selection) {
		return JavaScript.getBoolean(selection, Constants.IS_COLLAPSED);
	}

	native public void clear(final Selection selection)/*-{
		 selection.removeAllRanges();
		 }-*/;

	/**
	 * Extracts any selection and makes it a child of an element not attached to
	 * the DOM.
	 * 
	 * @param element
	 */
	public Element extract(final Selection selection) {
		return this.extract0(selection);
	}

	native private Element extract0(final Selection selection)/*-{
			var element = selection.anchorNode.ownerDocument.createElement("span");
		
		 var range = selection.getRangeAt( 0 );
		 if( range ){
		 range.surroundContents( element );
		 }
		 return element;
		 }-*/;

	final public void surround(final Selection selection, final Element element) {
		if (this.isEmpty(selection)) {
			throw new IllegalStateException("No selection exists unable to perform surround");
		}
		this.surround0(selection, element);
	}

	native protected void surround0(final Selection selection, final Element element)/*-{
		 var range = selection.getRangeAt( 0 );
		 range.surroundContents( element );        
		 }-*/;

	/**
	 * Deletes or removes the selected dom objects from the object.
	 * 
	 * @param selection
	 */
	public void delete(final Selection selection) {
		if (this.isEmpty(selection)) {
			throw new IllegalStateException("No selection exists unable to perform delete");
		}
		this.delete0(selection);
		this.clear(selection);
	}

	native private void delete0(final Selection selection)/*-{
		 var range = selection.getRangeAt( 0 );
		 range.deleteContents();     
		 }-*/;

	native public void clearAnySelectedText(final Selection selection)/*-{
		 selection.removeAllRanges();
		 }-*/;
}
