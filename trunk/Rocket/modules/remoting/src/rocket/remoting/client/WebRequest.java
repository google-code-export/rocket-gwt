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
package rocket.remoting.client;

import java.io.Serializable;

import rocket.util.client.Checker;
import rocket.util.client.Tester;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Used to package a web request for a servlet or other web resource with the
 * same web application. This is particularly useful when integrating with third
 * party jsps/tags or servlets that produce output that cannot be gathered in
 * another way.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class WebRequest implements IsSerializable, Serializable {

	/**
	 * The url of the server resource being requested
	 */
	private String url;

	public String getUrl() {
		Checker.notNull("field:url", url);
		return url;
	}

	public void setUrl(final String url) {
		Checker.notNull("parameter:url", url);
		this.url = url;
	}

	/**
	 * A collection of requesta parameter name/value pairs.
	 */
	private RequestParameters parameters;

	public RequestParameters getParameters() {
		Checker.notNull("field:parameters", this.parameters);
		return parameters;
	}

	public boolean hasParameters() {
		return null != this.parameters;
	}

	public void setParameters(final RequestParameters parameters) {
		Checker.notNull("parameter:parameters", parameters);
		if (this.hasData()) {
			Checker.fail("Data has already been set, parameters cannot also be set.");
		}

		this.parameters = parameters;
	}

	/**
	 * None or more headers that should be sent when requesting the server
	 * resource
	 */
	private Headers headers;

	public Headers getHeaders() {
		Checker.notNull("field:headers", headers);
		return this.headers;
	}

	public void setHeaders(final Headers headers) {
		Checker.notNull("parameter:headers", headers);
		this.headers = headers;
	}

	/**
	 * Any post data
	 */
	private String data;

	public String getData() {
		Checker.notNull("field:data", this.data);
		return data;
	}

	public boolean hasData() {
		return null != data;
	}

	public void setData(final String data) {
		Checker.notNull("parameter:data", data);
		if (false == Tester.isPost(this.getMethod())) {
			Checker.fail("ContentType/Data may only be set on a POST WebRequest");
		}
		this.data = data;
	}

	/**
	 * The request method be it GET or POST.
	 */
	private String method;

	public String getMethod() {
		Checker.httpMethod("field:method", method);
		return method;
	}

	public void setMethod(final String method) {
		Checker.httpMethod("parameter:method", method);
		this.method = method;
	}

	public String toString() {
		return super.toString() + ", method\"" + method + "\", url\"" + url + "\", headers: " + headers + ", parameters:" + parameters
				+ ", data: " + data;
	}
}
