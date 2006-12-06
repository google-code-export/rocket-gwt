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
package rocket.dragndrop.client;

import rocket.dom.client.DomHelper;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A variety of helper methods directly related to drag n drop operations
 * 
 * @author mP
 * 
 */
public class DragNDropHelper {
    /**
     * Creates a clone of the element belonging to the given Widget.
     * 
     * @param widget
     * @return
     */
    public static Element createClone(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        return DomHelper.cloneElement(widget.getElement(), true);
    }

    /**
     * Creates a rectangle that is the same size as the given widget.
     * 
     * @param widget
     * @return
     */
    public static Element createRectangle(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final Element element = DOM.createDiv();

        final int width = widget.getOffsetWidth();
        DOM.setStyleAttribute(element, StyleConstants.WIDTH, width + "px");

        final int height = widget.getOffsetHeight();
        DOM.setStyleAttribute(element, StyleConstants.HEIGHT, height + "px");

        final StringBuffer buf = new StringBuffer();
        buf.append(DOM.getAttribute(element, "className"));
        if (buf.length() > 0) {
            buf.append(' ');
        }
        buf.append(DragNDropConstants.DRAG_N_DROP_BOX_OUTLINE_STYLE);

        DOM.setAttribute(element, "className", buf.toString());
        return element;
    }

    /**
     * Disables the selecting of text that occurs when the mouse is dragged across the browser.
     * 
     * @param element
     */
    public static void disableTextSelection(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        disableTextSelection0(element);
    }

    native private static void disableTextSelection0(final Element element)/*-{
     var style = element.style;
     var cssText = style.cssText;
     if( cssText.length > 0 ){
     cssText = cssText + "; ";
     }
     cssText = cssText + "-moz-user-select: none; -khtml-user-select: none; user-select: none;";
     style.cssText = cssText; 
     
     element.ondrag = function () { return false; };
     element.onselectstart = function () { return false; };
     }-*/;

    /**
     * Reenables the default behaviour that occurs when a mouse is dragged across the browser.
     * 
     * @param element
     */
    public static void enableTextSelection(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        enableTextSelection0(element);
    }

    native private static void enableTextSelection0(final Element element)/*-{
     var style = element.style;
     var cssText = style.cssText;
     
     var remove = [ "-moz-user-select:", "-khtml-user-select:", "-user-select:" ];
     
     for( var i = 0; i < remove.length; i++ ){
     var index = cssText.indexOf( remove[ i ] );
     
     // not found test for next...
     if( index == -1 ){
     continue;
     }
     
     // find the trailing semi colon...
     var semiColon = cssText.indexOf(";", index );
     if( semiColon == -1 ){
     
     // was last entry on line keep start to index...
     cssText = cssText.substring( 0, index );
     continue;
     }
     
     // somewhere in the middle of cssText...
     cssText = cssText.substring( 0, index ) + cssText.substring( semiColon + 1);
     }
     style.cssText = cssText; 
     
     element.ondrag = null;
     element.onselectstart = null;
     }-*/;

    public native static void clearAnySelectedText()/*-{
     if ($wnd.getSelection) {
     var selection = $wnd.getSelection();
     if (selection)
     selection.removeAllRanges();
     }
     else {
     if ($doc.selection) {
     $doc.selection.empty();
     }
     }
     }-*/;

}
