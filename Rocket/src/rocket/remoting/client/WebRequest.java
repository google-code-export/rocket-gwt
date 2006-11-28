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
package rocket.remoting.client;

import rocket.util.client.HttpHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Used to package a web request for a servlet or other web resource with the same web application. This is particularly useful when
 * integrating with third party jsps/tags or servlets that produce output that cannot be gathered in another way.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class WebRequest implements IsSerializable {

    private String url;

    public String getUrl() {
        StringHelper.checkNotNull("field:url", url);
        return url;
    }

    public void setUrl(final String url) {
        StringHelper.checkNotNull("parameter:url", url);
        this.url = url;
    }

    private RequestParameters parameters;

    public RequestParameters getParameters() {
        ObjectHelper.checkNotNull("field:parameters", this.parameters);
        return parameters;
    }

    public boolean hasParameters() {
        return null != this.parameters;
    }

    public void setParameters(final RequestParameters parameters) {
        ObjectHelper.checkNotNull("parameter:parameters", parameters);
        if (this.hasData()) {
            SystemHelper.handleAssertFailure("Data has already been set, parameters cannot also be set.");
        }

        this.parameters = parameters;
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

    private String data;

    public String getData() {
        StringHelper.checkNotNull("field:data", this.data);
        return data;
    }

    public boolean hasData() {
        return null != data;
    }

    public void setData(final String data) {
        StringHelper.checkNotNull("parameter:data", data);
        if (false == HttpHelper.isPost(this.getMethod())) {
            SystemHelper.handleAssertFailure("ContentType/Data may only be set on a POST WebRequest");
        }
        this.data = data;
    }

    private String method;

    public String getMethod() {
        HttpHelper.checkMethod("field:method", method);
        return method;
    }

    public void setMethod(final String method) {
        HttpHelper.checkMethod("parameter:method", method);
        this.method = method;
    }

    public String toString() {
        return super.toString() + ", method[" + method + "], url[" + url + "], headers: " + headers + ", parameters:"
                + parameters + ", data: " + data;
    }
}
