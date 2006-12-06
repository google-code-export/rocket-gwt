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
package rocket.server.browser;

import javax.servlet.http.HttpServletRequest;

import rocket.util.server.Base64Encoder;
import rocket.util.server.ObjectHelper;

/**
 * A variety of methods that help detect what browser is running, creating of absolute urls as well as testing if the browser can take data
 * urls containing an embedded image.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class BrowserHelper {
    /**
     * This method takes care of crafting urls for images cehcking if it is possible to send back the image encoded using a data scheme If
     * not it will default to returning a http:// url to a servlet on this server.
     * 
     * @param request
     * @param imageSource
     * @return
     */
    public static String buildImageSrcUrl(final HttpServletRequest request, final ImageSource imageSource) {
        ObjectHelper.checkNotNull("parameter:request", request);
        ObjectHelper.checkNotNull("parameter:imageSource", imageSource);

        String url = null;
        while (true) {
            if (canImageTakeDataUrls(request)) {
                final String mimeType = imageSource.getMimeType();
                final byte[] image = imageSource.getImage();

                final String encodedImage = Base64Encoder.encode(image);

                url = "data:" + mimeType + ";base64," + encodedImage;
                break;
            }

            final String scheme = request.getScheme();
            final String host = request.getServerName();
            final int port = request.getServerPort();
            final String contextPath = request.getContextPath();
            final String servletPath = imageSource.getUrl();
            final String queryParameters = imageSource.getQueryString();

            url = scheme + "://" + host + ':' + port + contextPath + servletPath + '?' + queryParameters;
            break;
        }
        return url;
    }

    /**
     * Tests if the browser making the request is capable of making displaying images with data urls.
     * 
     * @param request
     * @return
     */
    public static boolean canImageTakeDataUrls(final HttpServletRequest request) {
        ObjectHelper.checkNotNull("parameter:request", request);

        return isFireFox1x(request);
    }

    /**
     * Tests if the browser making the request is Internet Explorer 6.0.
     * 
     * @param request
     * @return
     */
    public static boolean isInternetExplorer6(final HttpServletRequest request) {
        ObjectHelper.checkNotNull("parameter:request", request);

        return request.getHeader(BrowserConstants.USER_AGENT).indexOf(BrowserConstants.INTERNET_EXPLORER_6) != -1;
    }

    /**
     * Tests if the browser making the request is FireFox 1.x
     * 
     * @param request
     * @return
     */
    public static boolean isFireFox1x(final HttpServletRequest request) {
        ObjectHelper.checkNotNull("parameter:request", request);

        return request.getHeader(BrowserConstants.USER_AGENT).indexOf(BrowserConstants.FIREFOX_1_X) != -1;
    }
}