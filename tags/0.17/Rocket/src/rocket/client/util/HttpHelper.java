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
package rocket.client.util;

import com.google.gwt.user.client.Element;

/**
 * This helper includes a collection of miscellaneous useful methods relating to HTTP.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HttpHelper extends SystemHelper {

    /**
     * This takes an string and encodes it so that it contains valid url characters.
     * 
     * @param input
     * @return
     */
    public static String urlEncode(final String input) {
        ObjectHelper.checkNotNull("field:input", input);

        final StringBuffer encoded = new StringBuffer();
        final char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            final char c = chars[i];

            // alphas
            if (c >= 'A' && c <= 'Z') {
                encoded.append(c);
                continue;
            }
            if (c >= 'a' && c <= 'z') {
                encoded.append(c);
                continue;
            }

            // numerics
            if (c >= '0' && c <= '9') {
                encoded.append(c);
                continue;
            }

            // $-_.+!*'(),",
            if (c == '$' || c == '-' || c == '_' || c == '.' || c == '+' || c == '!' || c == '*' || c == '\''
                    || c == '(' || c == ')' || c == ',' || c == '\"') {
                encoded.append(c);
                continue;
            }

            // remaining characters need to be encoded...
            encoded.append('%');
            encoded.append(Integer.toHexString((int) c));
        }

        return encoded.toString();
    }

    /**
     * Encodes all the elements belonging to form into a url encoded safe String.
     * 
     * @param form
     * @return
     */
    public static String urlEncodeForm(final Element form) {
        ObjectHelper.checkNotNull("parameter:form", form);

        return urlEncodeForm0(form);
    }

    /**
     * Loops thru all the elements belonging to form. Had to be done this way because I am unsure how to return form.elements. Attempting to
     * return an array of Elements causes the compiler to generate broken javascript. This method currently only encodes INPUT tags by
     * reading their VALUE attribute.
     * 
     * @todo read and encoded selected fields etc.
     * @param form
     * @return
     */
    private static native String urlEncodeForm0(final Element form) /*-{
     var encoded = "";
     var addSeparator = false;

     var elements = form.elements;
     for( var i = 0; i < elements.length; i++ ){
     if( addSeparator ){
     encoded = encoded + "&";
     }
     addSeparator = true;

     var element = elements[ i ];
     var elementName = element.name;
     var elementValue = element.value;
     var elementValueEncoded = @rocket.client.util.HttpHelper::urlEncode(Ljava/lang/String;)( elementValue );

     encoded = encoded + elementName + "=" + elementValueEncoded;
     }

     return encoded;
     }-*/;

    public static void checkPath(final String name, final String path) {
        ObjectHelper.checkNotNull("parameter:path", path);

        if (path.length() > 0 && path.charAt(0) != HttpConstants.PATH_SEPARATOR) {
            HttpHelper.handleAssertFailure(name, "The " + name + " if not empty must start with a '/', path: [" + path
                    + "]");
        }
        if (path.indexOf(HttpConstants.QUERY_STRING) != -1 || path.indexOf(HttpConstants.ANCHOR) != -1) {
            HttpHelper.handleAssertFailure(name, "The " + name + " if not empty must not include a '?' or '#', path: ["
                    + path + "]");
        }
    }

    public static void checkPortNumber(final String name, final int port) {
        if (port < 0 || port > 65536) {
            HttpHelper.handleAssertFailure(name, "The " + name + " must be between 0 and port, port: " + port);
        }
    }

    public static void checkProtocol(final String name, final String protocol) {
        ObjectHelper.checkNotNull(name, protocol);
        if (false == isHttp(protocol) && false == isHttps(protocol)) {
            HttpHelper.handleAssertFailure(name, "The " + name + " is not a protocol (" + HttpConstants.HTTP + ','
                    + HttpConstants.HTTPS + "), protocol[" + protocol + "]");
        }
    }

    public static boolean isHttp(final String protocol) {
        ObjectHelper.checkNotNull("parameter:protocol", protocol);
        return HttpConstants.HTTP.equals(protocol);
    }

    public static boolean isHttps(final String protocol) {
        ObjectHelper.checkNotNull("parameter:protocol", protocol);
        return HttpConstants.HTTPS.equals(protocol);
    }

    public static boolean isGet(final String method) {
        ObjectHelper.checkNotNull("parameter:method", method);
        return HttpConstants.GET.equals(method);
    }

    public static boolean isPost(final String method) {
        ObjectHelper.checkNotNull("parameter:method", method);
        return HttpConstants.POST.equals(method);
    }

    public static void checkMethod(final String name, final String method) {
        if (false == isGet(method) && false == isPost(method)) {
            HttpHelper.handleAssertFailure(name, "The " + name + " is not a method (" + HttpConstants.GET + ','
                    + HttpConstants.POST + "), method[" + method + "]");
        }
    }

}