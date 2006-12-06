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
package rocket.remoting.server;

import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.remoting.client.FailedWebRequestException;
import rocket.remoting.client.Headers;
import rocket.remoting.client.WebRequest;
import rocket.remoting.client.WebResponse;
import rocket.util.client.HttpHelper;
import rocket.util.client.StringHelper;
import rocket.util.server.ObjectHelper;

public class WebHelper {

    public static WebResponse doWebRequest(final HttpServletRequest request, final HttpServletResponse response,
            final WebRequest webRequest) throws FailedWebRequestException {
        ObjectHelper.checkNotNull("parameter:request", request);
        ObjectHelper.checkNotNull("parameter:response", response);
        ObjectHelper.checkNotNull("parameter:webRequest", webRequest);

        try {
            // if url includes contxt path drop it.
            final String url = dropContextPathIfNecessary(webRequest.getUrl(), request);

            final Headers headers = webRequest.getHeaders();

            AbstractHttpServletRequest request0 = null;
            while (true) {
                if (HttpHelper.isGet(webRequest.getMethod())) {
                    request0 = new GetHttpServletRequest(request, url, headers, webRequest.getParameters());
                    break;
                }

                if (webRequest.hasParameters()) {
                    request0 = new PostHttpServletRequest(request, url, headers, webRequest.getParameters());
                    break;
                }

                request0 = new PostHttpServletRequest(request, url, headers, webRequest.getData().getBytes());
                break;
            }

            final ContentCapturingResponse response0 = new ContentCapturingResponse(response);
            response0.setBufferSize(response.getBufferSize());

            final RequestDispatcher dispatcher = request0.getRequestDispatcher(url);
            dispatcher.include(request0, response0);
            response0.flushBuffer();

            final WebResponse webResponse = new WebResponse();
            webResponse.setCode(response0.getStatus());
            webResponse.setMessage(response0.getMessage());
            // @temp not needed for now
            // webResponse.setHeaders(response0.getHeaders());

            final byte[] bytes = response0.toByteArray();
            final String characterEncoding = response0.getCharacterEncoding();
            final String text = null == characterEncoding ? new String(bytes) : new String(bytes, characterEncoding);
            webResponse.setBody(text); // was text @todo
            return webResponse;
        } catch (final Exception caught) {
            caught.printStackTrace();
            throw new FailedWebRequestException(caught.getMessage(), caught);
        }
    }

    public void copyHeaders(final HttpServletRequest source, final Headers destination) {
        ObjectHelper.checkNotNull("parameter:source", source);
        ObjectHelper.checkNotNull("parameter:destination", destination);

        final Enumeration names = source.getHeaderNames();
        while (names.hasMoreElements()) {
            final String name = (String) names.nextElement();
            final Enumeration values = source.getHeaders(name);
            while (values.hasMoreElements()) {
                String value = (String) values.nextElement();

                destination.add(name, value);
            }
        }
    }

    public void copyHeaders(final Headers source, final HttpServletResponse destination) {
        ObjectHelper.checkNotNull("parameter:source", source);
        ObjectHelper.checkNotNull("parameter:destination", destination);

        final Iterator names = source.names();
        while (names.hasNext()) {
            final String name = (String) names.next();
            final String[] values = source.getValues(name);
            for (int i = 0; i < values.length; i++) {
                String value = (String) values[i];

                destination.addHeader(name, value);
            }
        }
    }

    public static String dropContextPathIfNecessary(final String url, final HttpServletRequest request) {
        StringHelper.checkNotNull("parameter:url", url);
        ObjectHelper.checkNotNull("parameter:request", request);

        String checkedUrl = url;
        final String contextPath = request.getContextPath();
        if (url.startsWith(contextPath)) {
            checkedUrl = url.substring(contextPath.length());
        }
        return checkedUrl;
    }

    public static String toString(final long date) {
        return String.valueOf(date);
    }
}
