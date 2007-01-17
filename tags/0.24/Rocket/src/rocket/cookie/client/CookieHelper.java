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
package rocket.cookie.client;

import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;

/**
 * A collection of useful methods related to working with cookies.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class CookieHelper {
    /**
     * JSNI method which returns all cookies for this browser as a single String.
     */
    public native static String getCookies()/*-{
     var cookies = $doc.cookie;
     return cookies ? cookies : "";
     }-*/;

    /**
     * Checks and throws an exception if the given cookieName is not valid.
     * 
     * @param name
     * @param cookieName
     */
    public static void checkCookieName(final String name, final String cookieName) {
        StringHelper.checkNotEmpty(name, cookieName);

        final int length = cookieName.length();
        for (int i = 0; i < length; i++) {
            final char c = cookieName.charAt(i);

            if (i == 0 && c == '$') {
                SystemHelper.fail(name, "The " + name + " cannot begin with a $, " + name + "[" + cookieName + "]");
            }
            if (c == ' ' || c == ';') {
                SystemHelper.fail(name, "The " + name + " cannot include a space or semicolon, " + name + "["
                        + cookieName + "]");
            }
        }
    }

    /**
     * Updates the browser cookie collection.
     * 
     * @param cookie
     */
    public static void setCookie(final String cookie) {
        StringHelper.checkNotEmpty("parameter:cookie", cookie);
        CookieHelper.setCookie0(cookie);
    }

    private native static void setCookie0(final String cookie)/*-{
     $doc.cookie = cookie;
     }-*/;

    /**
     * JSNI method which removes a cookie from the browser's cookie collection. This achieved by setting a cookie with an expires Date
     * attribute set to 1970.
     * 
     * @param name
     */
    public static void removeCookie(final String name) {
        setCookie(name + CookieConstants.COOKIE_REMOVE_SUFFIX);
    }

    /**
     * This method may be invoked to query whether the browser has cookies enabled.
     * 
     * @return
     */
    native public static boolean areEnabled()/*-{
     return $wnd.navigator.cookieEnabled;
     }-*/;
}
