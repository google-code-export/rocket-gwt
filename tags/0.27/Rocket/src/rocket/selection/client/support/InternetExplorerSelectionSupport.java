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
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A specialised SelectionSupport class that is adapted to handle InternetExplorer differences from the standard implementation. 
 * @author Miroslav Pokorny (mP) 
 */
public class InternetExplorerSelectionSupport extends SelectionSupport {

    final static String PARENT_NODE = "parentNode";  
    
    native protected JavaScriptObject getNativeSelection()/*-{
        return $doc.selection;
    }-*/;

    native public void clear()/*-{
        $doc.selection.empty();
    }-*/;

    native public boolean isEmpty()/*-{
        return $doc.selection.type == "None";
    }-*/;

    public void delete(){
        this.delete0( this.getNativeSelection() );
    }
    
    native private void delete0(final JavaScriptObject selection )/*-{
        selection.clear();
    }-*/;
    
    native public void clearAnySelectedText0( final JavaScriptObject selection )/*-{
        selection.empty();
    }-*/;
    
    public SelectionEndPoint getStart(){
        return this.getStart0();        
    }
    
    protected native SelectionEndPoint getStart0() /*-{
        var selection = $doc.selection;
        var selectionRange = selection.createRange();        
        var element = selectionRange.parentElement();

        return this.@rocket.selection.client.support.InternetExplorerSelectionSupport::getStart1(Lcom/google/gwt/user/client/Element;)(element);       
    }-*/;

    protected native SelectionEndPoint getStart1( final Element element )/*-{
        var endPoint = null;
    
        var selection = $doc.selection;
        var selectionRange = selection.createRange();

        var range = selectionRange.duplicate();
        range.moveToElementText( element );
        range.collapse();        
                
        // loop thru all the childNodes belonging to element.
        var childNodes = element.childNodes;
        for( var i = 0; i < childNodes.length; i++ ){
            var node = childNodes[ i ];
            var nodeType = node.nodeType;
            
            // found an element check its child nodes...
            if( 1 == nodeType ){
                endPoint = this.@rocket.selection.client.support.InternetExplorerSelectionSupport::getStart1(Lcom/google/gwt/user/client/Element;)(node);
                
                if( null == endPoint ){
                    range.move( "character", node.innerText.toString().length );
                    continue;
                }
                // endPoint found stop searching....
                break;                
            }
            
            // found a textNode...
            if( 3 == nodeType ){
                var text = node.data;
                for( var j = 0; j < text.length; j++ ){
                    // found selection start stop searching!
                    if( selectionRange.compareEndPoints( "StartToStart", range ) == 0 ){
                        endPoint = this.@rocket.selection.client.support.InternetExplorerSelectionSupport::createSelectionEndPoint(Lcom/google/gwt/core/client/JavaScriptObject;I)(node,j);
                        break;
                    }
                    range.move("character", 1 );
                }
                // did the above for loop find the start ? if so stop escape!
                if( null != endPoint ){
                    break;
                }
            }         
        }
        
        return endPoint;
    }-*/;
    
    /**
     * Factory method which creates a SelectionEndPoint and sets its textNode and offset properties.
     * @param textNode
     * @param offset
     * @return
     */
    protected SelectionEndPoint createSelectionEndPoint(final JavaScriptObject textNode, final int offset ){
        final SelectionEndPoint endPoint = new SelectionEndPoint();
        endPoint.setOffset( offset );
        endPoint.setTextNode(textNode);
        return endPoint;
    }
    
    public void setStart( final SelectionEndPoint start ){      
        ObjectHelper.checkNotNull( "parameter:start", start );        

        final JavaScriptObject textNode = start.getTextNode();
        final int offset = start.getOffset();
        this.setStart0( textNode, offset );
    }

    native private void setStart0( final JavaScriptObject textNode, final int offset )/*-{
        var rangeOffset = offset;
        var moveToElement = null;
        
        // try an element before $textNode counting the number of characters one has moved backwards...
        var node = textNode.previousSibling;
        
        while( node ){
            // if a textNode is try its previous sibling...
            if( node.nodeType == 3 ){
                rangeOffset = rangeOffset + node.data.length;
                continue;
            }
        
            // found an element stop searching...
            if( node.nodeType == 1 ){
                moveToElement = node; 
                rangeOffset = rangeOffset + node.innerText.toString().length;
                break;
            }
            
            // ignore other types...
            node = node.previousSibling;
        }
    
        // if moveToElement is null use textNode's parent.
        if( ! moveToElement ){
            moveToElement = textNode.parentNode;
        }
    
        // update the start of selection range...
        var range = $doc.selection.createRange();
        range.moveToElementText( moveToElement );
        range.moveStart( "character", rangeOffset );
        range.select();                        
    }-*/;

    public void setEnd( final SelectionEndPoint end ){      
        ObjectHelper.checkNotNull( "parameter:end", end );        

        final JavaScriptObject textNode = end.getTextNode();
        final int offset = end.getOffset();
        this.setEnd0( textNode, offset );
    }

    native private void setEnd0( final JavaScriptObject textNode, final int offset )/*-{    
        var rangeOffset = offset;
        var moveToElement = null;
        
        // try an element before $textNode counting the number of characters one has moved backwards...
        var node = textNode.previousSibling;
        
        while( node ){
            // if textNode is try its previous sibling...
            if( node.nodeType == 3 ){        
                rangeOffset = rangeOffset + node.data.length;
                continue;
            }
        
            // found an element stop searching...
            if( node.nodeType == 1 ){
                moveToElement = node;
                rangeOffset = rangeOffset + node.innerText.toString().length;
                break;
            }
            
            // ignore other types...
            node = node.previousSibling;
        }           
    
        // if moveToElement is null use textNode's parent.
        if( ! moveToElement ){
            moveToElement = textNode.parentNode;
        }
    
        // update the end of selection range...
        var range = $doc.selection.createRange();
        range.moveToElementText( moveToElement );
        range.moveStart( "character", rangeOffset );
        range.collapse();
        
        var selectionRange = $doc.selection.createRange();
        selectionRange.setEndPoint( "EndToStart", range );
        selectionRange.select();                                 
  }-*/;

    public SelectionEndPoint getEnd(){
        return this.getEnd0();        
    }
    
    protected native SelectionEndPoint getEnd0() /*-{
    var selection = $doc.selection;
    var selectionRange = selection.createRange();        
    var element = selectionRange.parentElement();

    return this.@rocket.selection.client.support.InternetExplorerSelectionSupport::getEnd1(Lcom/google/gwt/user/client/Element;)(element);       
    }-*/;

    protected native SelectionEndPoint getEnd1( final Element element )/*-{
        var endPoint = null;
    
        var selection = $doc.selection;
        var selectionRange = selection.createRange();

        var range = selectionRange.duplicate();
        range.moveToElementText( element );
        range.collapse( true );        
        
        // loop thru all the childNodes belonging to element.
        var childNodes = element.childNodes;
        for( var i = 0; i < childNodes.length; i++ ){
            var node = childNodes[ i ];
            var nodeType = node.nodeType;
            
            // found an element check its child nodes...
            if( 1 == nodeType ){
                endPoint = this.@rocket.selection.client.support.InternetExplorerSelectionSupport::getEnd1(Lcom/google/gwt/user/client/Element;)(node);
                
                if( null == endPoint ){
                    range.move( "character", node.innerText.toString().length );
                    continue;
                }
                // endPoint found stop searching....
                break;                
            }
            
            // found a textNode...
            if( 3 == nodeType ){
                var text = node.data;
                for( var j = 0; j < text.length; j++ ){
                    // found selection end stop searching!
                    if( selectionRange.compareEndPoints( "EndToStart", range ) == 0 ){
                        endPoint = this.@rocket.selection.client.support.InternetExplorerSelectionSupport::createSelectionEndPoint(Lcom/google/gwt/core/client/JavaScriptObject;I)(node,j);
                        break;
                    }
                    range.move( "character", 1 );
                }
                // did the above for loop find the end ? if so stop escape!
                if( null != endPoint ){
                    break;
                }
            }         
        }
        
        return endPoint;
    }-*/;
    
    public Element extract(){
        final Element element = DOM.createSpan();
        
        if( false == this.isEmpty() ){
            this.extract0( this.getNativeSelection(), element );
            this.delete();
        }
        return element;
    }   

    /**
     * Extracts the html that represents the selected area and sets the innerHTML attribute of the given element.
     * @param selection
     * @param element
     */
    native private void extract0( final JavaScriptObject selection, final Element element )/*-{        
        // extract the selected area and assign to $html.
        var html = selection.createRange().htmlText;
        
        // set the innerHTML of $element to $html
        element.innerHTML = html;                
    }-*/;
    
    protected void surround0( final Element element ){
        // get child index of selection start textNode from its parent.
        final SelectionEndPoint selectionStart =this.getStart();
        final JavaScriptObject textNode = selectionStart.getTextNode();
        final Element parentOfTextNode = ObjectHelper.getElement( textNode, PARENT_NODE );
                
        int insertIndex = 0;
        if( selectionStart.getOffset() > 0 ){
            insertIndex = this.getChildIndexOfTextNode(textNode) + 1;
        }
        
        // extract selection to become a child of element.
        this.extract0( this.getNativeSelection(), element );
     
        // delete the 
        this.delete();
     
        // insert $element just after the original start of selection textNode
        this.insertChild(parentOfTextNode, element, insertIndex );        
    }

    native private int getChildIndexOfTextNode( final JavaScriptObject textNode )/*-{
        var i = 0;
        
        var children = textNode.parentNode.childNodes;
        var count = children.length;        
        for( var j = 0; j < count; j++ ){           
            if( textNode === children[ j ] ){
                i = j;
                break;
            }            
        }
                
        return i;
    }-*/;


    /**
     * This method is necessary because GWT's DOM.insertChild( Element parent, Element element, int index ) fails
     * because it skip text nodes when inserting.  
     * @param textNode
     * @return
     */
    native private void insertChild( final Element parent, final Element element, final int index )/*-{
        var childNodes = parent.childNodes;
        if( index >= childNodes.length ){
            parent.appendChild( element );
        } else {
            parent.insertBefore( element, childNodes[ index ]);
        }
    }-*/;

}