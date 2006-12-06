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

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import rocket.remoting.client.Headers;
import rocket.remoting.client.RequestParameters;
import rocket.util.client.StringHelper;
import rocket.util.server.ObjectHelper;

/**
 * This Request supports the ability to capture output from included/forwarded web resources.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class AbstractHttpServletRequest extends HttpServletRequestWrapper implements HttpServletRequest {

    protected AbstractHttpServletRequest(final HttpServletRequest request, final String url, final Headers headers) {
        super(request);

        this.setUrl(url);
        this.setHeaders(headers);
    }

    /**
     * The url of the
     */
    private String url;

    protected String getUrl() {
        StringHelper.checkNotEmpty("field:url", url);
        return url;
    }

    protected void setUrl(final String url) {
        StringHelper.checkNotEmpty("parameter:url", url);
        this.url = url;
    }

    private Headers headers;

    public Headers getHeaders() {
        ObjectHelper.checkNotNull("field:headers", headers);
        return this.headers;
    }

    public void setHeaders(final Headers headers) {
        ObjectHelper.checkNotNull("parameter:headers", headers);
        this.headers = headers;
    }

    // public String getQueryString() {
    // final String url = this.getUrl();
    // final int queryStringIndex = url.indexOf('?');
    //
    // return queryStringIndex == -1 ? url : url.substring(queryStringIndex +
    // 1);
    // }
    //
    // public String getRequestURI() {
    // return this.getUrl();
    // }
    //
    // public StringBuffer getRequestURL() {
    // return new StringBuffer(this.getUrl());
    // }
    //
    // public String getPathInfo() {
    // String pathInfo = null;
    // if (this.hasServletPath()) {
    // pathInfo = this.getUrl().substring(this.getServletPath().length());
    // }
    // return pathInfo;
    // }
    //
    // public String getPathTranslated() {
    // return this.hasServletPath() ? super.getRealPath(this.getServletPath()) :
    // null;
    // }
    //
    // protected boolean hasServletPath() {
    // return false;
    // }

    private String characterEncoding;

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(final String characterEncoding) throws UnsupportedEncodingException {
        this.characterEncoding = characterEncoding;
    }

    /**
     * A container for request parameters taken from a url.
     */
    private RequestParameters requestParameters;

    protected RequestParameters getRequestParameters() {
        ObjectHelper.checkNotNull("field:requestParameters", this.requestParameters);
        return this.requestParameters;
    }

    protected boolean hasRequestParameters() {
        return null != this.requestParameters;
    }

    protected void setRequestParameters(final RequestParameters requestParameters) {
        ObjectHelper.checkNotNull("parameter:requestParameters", requestParameters);
        this.requestParameters = requestParameters;
    }

    public String getParameter(final String name) {
        return this.getRequestParameters().getValue(name);
    }

    public Enumeration getParameterNames() {
        final Iterator iterator = this.getRequestParameters().names();
        return new Enumeration() {
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            public Object nextElement() {
                return iterator.next();
            }
        };
    }

    public String[] getParameterValues(final String name) {
        return this.getRequestParameters().getValues(name);
    }

    /**
     * A read only view of this map is used by getParameterMap();
     */
    private Map parameterMap;

    /**
     * Uses a lazy approach to creating a map with keys/values ( as String arrays ).
     * 
     * @return
     */
    public Map getParameterMap() {
        if (false == this.hasParameterMap()) {
            this.createParameterMap();
        }

        ObjectHelper.checkNotNull("field:parameterMap", parameterMap);
        return this.parameterMap;
    }

    protected boolean hasParameterMap() {
        return this.parameterMap != null;
    }

    protected void setParameterMap(final Map parameterMap) {
        ObjectHelper.checkNotNull("parameter:parameterMap", parameterMap);
        this.parameterMap = parameterMap;
    }

    protected void createParameterMap() {
        ObjectHelper.checkPropertyNotSet("parameterMap", this, this.hasParameterMap());

        final Map view = new HashMap();
        final RequestParameters parameters = this.getRequestParameters();
        final Iterator keys = parameters.names();

        while (keys.hasNext()) {
            final String key = (String) keys.next();
            final String[] value = parameters.getValues(key);
            view.put(key, value);
        }

        this.setParameterMap(Collections.unmodifiableMap(view));
    }

    public String toString() {
        return super.toString() + ", url[" + url + "], headers: " + headers + ", requestParameters:"
                + requestParameters;
    }
}
