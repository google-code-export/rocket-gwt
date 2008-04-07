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
import rocket.util.client.Checker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Event;

/**
 * A collection of helper methods related to the browser, often reporting values retrieved from
 * the known browser properties.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Browser {

	/**
	 * A BrowserSupport class is used to implement several Browser methods where
	 * different browsers have different mechanisms supporting that feature.
	 */
	private final static BrowserSupport support = (BrowserSupport) GWT.create(BrowserSupport.class);

	protected static BrowserSupport getSupport() {
		return support;
	}

	/**
	 * Retrieves the document object for the current page
	 * 
	 * @return The document
	 */
	public static native JavaScriptObject getDocument() /*-{
	 return $doc;
	 }-*/;

	/**
	 * Retrieves the window object for the current page.
	 * 
	 * @return The window
	 */
	public static native JavaScriptObject getWindow() /*-{
	 return $wnd;
	 }-*/;

	/**
	 * Retrieves the current window status.
	 * 
	 * @return The status text
	 */
	public static native String getStatus()/*-{
	 return $wnd.status;
	 }-*/;

	/**
	 * Sets or replaces the browser status.
	 * 
	 * @param status The new text
	 */
	public static native void setStatus(final String status)/*-{
	 $wnd.status = status;
	 }-*/;

	/**
	 * Prompts the user for a string allowing an initial value which may in turn
	 * be modified by the user.
	 * 
	 * @param message The message that will be displayed
	 * @param initialValue The initial value which may be overridden by the user
	 * @return The actual entered text.
	 */
	public static String prompt(final String message, final String initialValue) {
		Checker.notEmpty("parameter:message", message);
		Checker.notNull("parameter:initialValue", initialValue);

		return prompt0(message, initialValue);
	}

	private static native String prompt0(final String message, final String initialValue)/*-{
	 return "" + $wnd.prompt( message, initialValue );
	 }-*/;

	/**
	 * The horizontal scroll offset of the client window relative to the actual document
	 * @return The value in pixels.
	 */
	public static int getScrollX() {
		return Browser.getSupport().getScrollX();
	}

	/**
	 * The vertical scroll offset of the client window relative to the actual document
	 * @return The value in pixels.
	 */
	public static int getScrollY() {
		return Browser.getSupport().getScrollY();
	}

	/**
	 * Scrolls the top left of the window to the position denoted by the given
	 * x/y coordinates
	 * 
	 * @param x The horizontal offset in pixels
	 * @param y The vertical offset in pixels
	 */
	public static native void scrollTo(final int x, final int y)/*-{
	 $wnd.scroll( x, y );
	 }-*/;

	/**
	 * Returns the contextPath of this web application, this concept is
	 * particularly useful for working with J2EE web applications.
	 * 
	 * @return The context path for this application.
	 */
	public static String getContextPath() {
		String url = GWT.getModuleBaseURL();
		if (GWT.isScript()) {
			final String location = Browser.getLocation();
			final int afterScheme = location.indexOf("//");
			final int webContextStart = location.indexOf('/', afterScheme + 3);
			final int webContextEnd = location.indexOf('/', webContextStart + 1);

			url = location.substring(webContextStart, webContextEnd);
		}

		// drop trailing slash if one is present.
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}

	/**
	 * Adds the base url of the standard images directory on the server.
	 */
	public static String buildImageUrl(final String url) {
		Checker.notEmpty("parameter:url", url);

		return Browser.getContextPath() + Constants.IMAGES + url;
	}

	/**
	 * Retrieves the current location.
	 * 
	 * @return The location
	 */
	public static native String getLocation()/*-{
	 return $wnd.location.href;
	 }-*/;

	/**
	 * Sets the location of the browser, which will trigger a new page load.
	 * @param location
	 */
	public static void setLocation(final String location) {
		Checker.notEmpty("parameter:location", location);

		setLocation0(location);
	}

	private static native String setLocation0(final String location)/*-{
	 $wnd.location.href = location;
	 }-*/;

	/**
	 * Returns the available screen area within the browser
	 * 
	 * @return The width in pixels
	 */
	public native static int getAvailableScreenWidth()/*-{
	 return $wnd.screen.availWidth;
	 }-*/;

	/**
	 * Returns the available screen area within the browser
	 * 
	 * @return The height in pixels.
	 */
	public native static int getAvailableScreenHeight()/*-{
	 return $wnd.screen.availHeight;
	 }-*/;

	/**
	 * Retrieves the client area width
	 * @return The width in pixels
	 */
	public static int getClientWidth() {
		return Browser.getSupport().getClientWidth();
	}


	/**
	 * Retrieves the client area height
	 * @return The height in pixels
	 */
	public static int getClientHeight() {
		return Browser.getSupport().getClientHeight();
	}

	/**
	 * Sets the title of the window.
	 * 
	 * @param title The new title text
	 */
	public static native void setTitle(final String title) /*-{
	 $doc.title = title;
	 }-*/;

	public static boolean isInternetExplorer() {
		return getUserAgent().indexOf(Constants.INTERNET_EXPLORER_USER_AGENT) != -1 && false == isOpera();
	}

	public static boolean isFireFox() {
		return getUserAgent().indexOf(Constants.FIREFOX_USER_AGENT) != -1 && false == isOpera();
	}

	public static boolean isOpera8() {
		return getUserAgent().indexOf(Constants.OPERA8_USER_AGENT) != -1;
	}

	public static boolean isOpera9() {
		return getUserAgent().indexOf(Constants.OPERA9_USER_AGENT) != -1;
	}

	public static boolean isOpera() {
		return getUserAgent().indexOf(Constants.OPERA_USER_AGENT) == -1;
	}

	public static boolean isSafari() {
		return getUserAgent().indexOf(Constants.SAFARI_USER_AGENT) != -1 && false == isOpera();
	}

	/**
	 * Retrieves the userAgent of the browser
	 * 
	 * @return the reported user agent
	 */
	public static native String getUserAgent()/*-{
	 return $wnd.navigator.userAgent;
	 }-*/;

	/**
	 * Returns the host operating system that the browser is running under.
	 * 
	 * @return The host operating system.
	 */
	public static String getOperatingSystem() {
		final String userAgent = Browser.getUserAgent();
		final int leftParenthesis = userAgent.indexOf('(');
		final int semiColon = userAgent.indexOf(leftParenthesis, ';');
		return userAgent.substring(leftParenthesis + 1, semiColon);
	}

	/**
	 * Only warn the user if in hosted mode and the browser host page causes the
	 * document to be rendered in quirks mode.
	 */
	static {
		if (false == GWT.isScript() && Browser.isQuirksMode()) {
			GWT.log(Constants.QUIRKS_MODE_WARNING, null);
		}
	}

	/**
	 * This method tests if the browser is in quirks mode.
	 * 
	 * @return true if the browser is operating in quirks mode otherwise returns false
	 */
	native static public boolean isQuirksMode()/*-{        
	 return "BackCompat" == $doc.compatMode;
	 }-*/;

	/**
	 * Retrieves the mouse x position
	 * @param event The source event
	 * @return The x coordinate in pixels.
	 */
	static public int getMousePageX(final Event event) {
		return Browser.getSupport().getMousePageX(event);
	}

	/**
	 * Retrieves the mouse y position
	 * @param event The source event
	 * @return The y coordinate in pixels.
	 */
	static public int getMousePageY(final Event event) {
		return Browser.getSupport().getMousePageY(event);
	}

}