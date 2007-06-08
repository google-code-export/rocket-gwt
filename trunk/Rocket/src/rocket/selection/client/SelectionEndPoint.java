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
package rocket.selection.client;

import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;
/**
 * An end point uses a combination of a textNode and offset to mark the start/end of a selection 
 * @author Miroslav Pokorny (mP)
 */
public class SelectionEndPoint {
    /**
     * The textNode containing the start/end of the selection.
     */
    private JavaScriptObject textNode;
       
    public JavaScriptObject getTextNode(){
        ObjectHelper.checkNotNull("field:textNode", textNode );
        return textNode;
    }
    public void setTextNode( final JavaScriptObject textNode ){
        ObjectHelper.checkNotNull("parameter:textNode", textNode );
        this.textNode = textNode;
    }
    /**
     * The number of characters starting from the beginning of the textNode where the selection begins/ends. 
     */
    public int offset;
    
    public int getOffset(){
        return offset;
    }
    public void setOffset( final int offset ){
        this.offset = offset;
    }
    
    public String toString(){
        return super.toString() + ", textNode[" + this.textNode + "], offset: " + offset;
    }
}
