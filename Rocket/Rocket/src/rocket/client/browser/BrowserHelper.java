/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.browser;

import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A collection of useful browser application related methods.
 * JSNI is heavily used to query or modify values somewhere in the DOM.
 *
 * @author Miroslav Pokorny (mP)
 */
public class BrowserHelper extends ObjectHelper {

	public static native String getStatus()/*-{
	 return $wnd.status;
	 }-*/;

	public static native void setStatus(final String status)/*-{
	 $wnd.status = status;
	 }-*/;

	public static native int getScrollX()/*-{
	 var x = $wnd.scrollX;
	 if(! x ){
	  x = 0;
	 }
	 return x;
	 }-*/;

	public static native int getScrollY()/*-{
	 var y = $wnd.scrollY;
	 if(! y ){
	  y = 0;
	 }
	 return y;
	 }-*/;

	/**
	 * Scrolls the top left of the window to the position denoted by the given
	 * x/y coordinates
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
			final String location = BrowserHelper.location();
			final int afterScheme = location.indexOf("//");
			final int webContextStart = location.indexOf('/', afterScheme + 3);
			final int webContextEnd = location
					.indexOf('/', webContextStart + 1);

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
	 * Helper which returns the current location. This is particularly useful
	 * when one wishes to build other urls to the same server.
	 *
	 * @return
	 */
	public static native String location()/*-{
	 return $wnd.location.href;
	 }-*/;

	/**
	 * because of a bug in some browsers the reported width and height of the popup panel is incorrect
	 * before the initial setPopupPosition. THe initial width actually matches the window width.
	 *
	 * To avoid this it is necessary to calculate the width/height and set the position twice,
	 * as after the first set the width will be correct.
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

		final int browserWidth = Window.getClientWidth();
		final int browserHeight = Window.getClientHeight();

		final int left = browserWidth / 2 - width / 2;
		final int top = browserHeight / 2 - height / 2;

		popupPanel.setPopupPosition(left, top);
		popupPanel.setPopupPosition(left, top);
	}

	/**
	 * This method may be used to center any widget in the middle of the screen.
	 * It is especially useful for dialog boxes.
	 *
	 * @param widget
	 */
	public static void screenCenter(final Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		final int width = widget.getOffsetWidth();
		final int height = widget.getOffsetHeight();

		final int browserWidth = Window.getClientWidth();
		final int browserHeight = Window.getClientHeight();

		final int left = browserWidth / 2 - width / 2;
		final int top = browserHeight / 2 - height / 2;

		final Element element = widget.getElement();
		DOM.setStyleAttribute(element, "left", String.valueOf(left));
		DOM.setStyleAttribute(element, "top", String.valueOf(top));
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
		return getUserAgent().indexOf(
				BrowserConstants.INTERNET_EXPLORER_USER_AGENT) != -1;
	}

	public static boolean isFireFox() {
		return getUserAgent().indexOf(BrowserConstants.FIREFOX_USER_AGENT) != -1;
	}

	/**
	 * Retrieves the userAgent or browser identifying string using JSNI.
	 * @return
	 */
	public static native String getUserAgent()/*-{
	 return $wnd.navigator.userAgent;
	 }-*/;

	// COOKIES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * Checks and throws an exception if the given cookieName is not valid.
	 * @param name
	 * @param cookieName
	 */
	public static void checkCookieName(final String name,
			final String cookieName) {
		StringHelper.checkNotEmpty(name, cookieName);

		final int length = cookieName.length();
		for (int i = 0; i < length; i++) {
			final char c = cookieName.charAt(i);

			if (i == 0 && c == '$') {
				SystemHelper.handleAssertFailure(name, "The " + name + " cannot begin with a $, " + name + "[" + cookieName + "]");
			}
			if( c == ' ' || c == ';'){
				SystemHelper.handleAssertFailure(name, "The " + name + " cannot include a space or semicolon, " + name + "[" + cookieName + "]");
			}
		}
	}

	/**
	 * JSNI method which returns all cookies for this browser as a single String.
	 */
	public native static String getCookies()/*-{
	 var cookies = $doc.cookie;
	 if( ! cookies ){
	   cookies = "";
	 }
	 return cookies;
	 }-*/;

	/**
	 * JSNI method which updates the browser cookie collection.
	 * @param cookie
	 */
	public native static void setCookie(final String cookie)/*-{
	 	$doc.cookie = cookie;
	 }-*/;

	/**
	 * JSNI method which removes a cookie from the browser's cookie collection.
	 * This achieved by setting a cookie with an expires Date attribute set to 1970.
	 * @param name
	 */
	public static void removeCookie(String name) {
		setCookie(name + BrowserConstants.COOKIE_REMOVE_SUFFIX);
	}
}