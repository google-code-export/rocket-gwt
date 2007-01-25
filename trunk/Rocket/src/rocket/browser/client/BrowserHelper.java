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
package rocket.browser.client;

import rocket.browser.client.support.BrowserSupport;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A collection of useful browser application related methods. JSNI is heavily used to query or modify values somewhere in the DOM.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class BrowserHelper {

    /**
     * A BrowserHelperSupport class is used to implement several BrowserHelper methods where different browsers have different mechanisms
     * supporting that feature.
     */
    private final static BrowserSupport support = (BrowserSupport) GWT.create(BrowserSupport.class);

    protected static BrowserSupport getSupport() {
        return support;
    }

    /**
     * Retrieves the document object for the current page
     * 
     * @return
     */
    public static native JavaScriptObject getDocument() /*-{
     return $doc;
     }-*/;

    /**
     * Retrieves the window object for the current page.
     * 
     * @return
     */
    public static native JavaScriptObject getWindow() /*-{
     return $wnd;
     }-*/;

    /**
     * Retrieves the current window status.
     * 
     * @return
     */
    public static native String getStatus()/*-{
     return $wnd.status;
     }-*/;

    /**
     * Sets or replaces the browser status.
     * 
     * @param status
     */
    public static native void setStatus(final String status)/*-{
     $wnd.status = status;
     }-*/;

    /**
     * Prompts the user for a string allowing an initial value which may in turn be modified by the user.
     * 
     * @param message
     * @param initialValue
     * @return
     */
    public static String prompt(final String message, final String initialValue) {
        StringHelper.checkNotEmpty("parameter:message", message);
        StringHelper.checkNotNull("parameter:initialValue", initialValue);

        return prompt0(message, initialValue);
    }

    private static native String prompt0(final String message, final String initialValue)/*-{
     return $wnd.prompt( message, initialValue );
     }-*/;

    public static int getScrollX() {
        return BrowserHelper.getSupport().getScrollX();
    }

    public static int getScrollY() {
        return BrowserHelper.getSupport().getScrollY();
    }

    /**
     * Scrolls the top left of the window to the position denoted by the given x/y coordinates
     * 
     * @param x
     * @param y
     */
    public static native void scrollTo(final int x, final int y)/*-{
     $wnd.scroll( x, y );
     }-*/;

    /**
     * Returns the contextPath of this web application, this concept is particularly useful for working with J2EE web applications.
     * 
     * @return
     */
    public static String getContextPath() {
        String url = GWT.getModuleBaseURL();
        if (GWT.isScript()) {
            final String location = BrowserHelper.getLocation();
            final int afterScheme = location.indexOf("//");
            final int webContextStart = location.indexOf('/', afterScheme + 3);
            final int webContextEnd = location.indexOf('/', webContextStart + 1);

            url = location.substring(webContextStart, webContextEnd);
        }
        return url;
    }

    /**
     * Adds the base url of the standard images directory on the server.
     * 
     * @param url
     * @return
     */
    public static String buildImageUrl(final String url) {
        StringHelper.checkNotEmpty("parameter:url", url);

        return BrowserHelper.getContextPath() + BrowserConstants.IMAGES + url;
    }

    /**
     * Helper which returns the current location. This is particularly useful when one wishes to build other urls to the same server.
     * 
     * @return
     */
    public static native String getLocation()/*-{
     return $wnd.location.href;
     }-*/;

    public static void setLocation(final String location) {
        StringHelper.checkNotEmpty("parameter:location", location);

        setLocation0(location);
    }

    private static native String setLocation0(final String location)/*-{
     $wnd.location.href = location;
     }-*/;

    /**
     * because of a bug in some browsers the reported width and height of the popup panel is incorrect before the initial setPopupPosition.
     * THe initial width actually matches the window width.
     * 
     * To avoid this it is necessary to calculate the width/height and set the position twice, as after the first set the width will be
     * correct.
     * 
     * @param popupPanel
     */
    public static void screenCenterPopupPanel(final PopupPanel popupPanel) {
        screenCenterPopupPanel0(popupPanel);
        screenCenterPopupPanel0(popupPanel);
    }

    protected static void screenCenterPopupPanel0(final PopupPanel popupPanel) {
        ObjectHelper.checkNotNull("parameter:dialogBox", popupPanel);

        final int width = popupPanel.getOffsetWidth();
        final int height = popupPanel.getOffsetHeight();

        final int browserWidth = getClientWidth();
        final int browserHeight = getClientHeight();

        final int left = browserWidth / 2 - width / 2;
        final int top = browserHeight / 2 - height / 2;

        popupPanel.setPopupPosition(left, top);
        popupPanel.setPopupPosition(left, top);
    }

    /**
     * This method may be used to center any widget in the middle of the screen. It is especially useful for dialog boxes.
     * 
     * @param widget
     */
    public static void screenCenter(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final int width = widget.getOffsetWidth();
        final int height = widget.getOffsetHeight();

        final int browserWidth = getClientWidth();
        final int browserHeight = getClientHeight();

        final int left = browserWidth / 2 - width / 2;
        final int top = browserHeight / 2 - height / 2;

        final Element element = widget.getElement();
        DOM.setStyleAttribute(element, "left", String.valueOf(left) + "px");
        DOM.setStyleAttribute(element, "top", String.valueOf(top) + "px");
    }

    /**
     * Returns the available screen area within the browser, width in pixels
     * 
     * @return
     */
    public native static int getAvailableScreenWidth()/*-{
     return $wnd.screen.availWidth;
     }-*/;

    /**
     * Returns the available screen area within the browser, height in pixels
     * 
     * @return
     */
    public native static int getAvailableScreenHeight()/*-{
     return $wnd.screen.availHeight;
     }-*/;

    protected static int getClientWidth() {
        return BrowserHelper.getSupport().getClientWidth();
    }

    protected static int getClientHeight() {
        return BrowserHelper.getSupport().getClientHeight();
    }

    /**
     * THis method uses embedded javascript to update the title of the browser.
     * 
     * @param title
     */
    public static native void setTitle(final String title) /*-{
     $doc.title = title;
     }-*/;

    public static boolean isInternetExplorer6() {
        return getUserAgent().indexOf(BrowserConstants.INTERNET_EXPLORER_USER_AGENT) != -1;
    }

    public static boolean isFireFox() {
        return getUserAgent().indexOf(BrowserConstants.FIREFOX_USER_AGENT) != -1;
    }

    public static boolean isOpera8() {
        return getUserAgent().indexOf(BrowserConstants.OPERA8_USER_AGENT) != -1;
    }

    public static boolean isOpera9() {
        return getUserAgent().indexOf(BrowserConstants.OPERA9_USER_AGENT) != -1;
    }

    /**
     * Retrieves the userAgent or browser identifying string using JSNI.
     * 
     * @return
     */
    public static native String getUserAgent()/*-{
     return $wnd.navigator.userAgent;
     }-*/;

    /**
     * Returns the host operating system that the browser is running under.
     * 
     * @return
     */
    public static String getOperatingSystem() {
        final String userAgent = BrowserHelper.getUserAgent();
        final int leftParenthesis = userAgent.indexOf('(');
        final int semiColon = userAgent.indexOf(leftParenthesis, ';');
        return userAgent.substring(leftParenthesis + 1, semiColon);
    }
}