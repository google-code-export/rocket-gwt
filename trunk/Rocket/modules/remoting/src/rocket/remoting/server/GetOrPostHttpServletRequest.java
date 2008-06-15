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
import rocket.util.client.Checker;

/**
 * This Request supports the ability to capture output from included/forwarded
 * web resources.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract class GetOrPostHttpServletRequest extends HttpServletRequestWrapper implements HttpServletRequest {

	protected GetOrPostHttpServletRequest(final HttpServletRequest request, final String url, final Headers headers) {
		super(request);

		this.setUrl(url);
		this.setHeaders(headers);
	}

	/**
	 * The url of the
	 */
	private String url;

	protected String getUrl() {
		Checker.notEmpty("field:url", url);
		return url;
	}

	protected void setUrl(final String url) {
		Checker.notEmpty("parameter:url", url);
		this.url = url;
	}

	private Headers headers;

	public Headers getHeaders() {
		Checker.notNull("field:headers", headers);
		return this.headers;
	}

	public void setHeaders(final Headers headers) {
		Checker.notNull("parameter:headers", headers);
		this.headers = headers;
	}

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
		Checker.notNull("field:requestParameters", this.requestParameters);
		return this.requestParameters;
	}

	protected boolean hasRequestParameters() {
		return null != this.requestParameters;
	}

	protected void setRequestParameters(final RequestParameters requestParameters) {
		Checker.notNull("parameter:requestParameters", requestParameters);
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
	 * Uses a lazy approach to creating a map with keys/values ( as String
	 * arrays ).
	 * 
	 * @return
	 */
	public Map<String,String[]> getParameterMap() {
		if (false == this.hasParameterMap()) {
			this.createParameterMap();
		}

		Checker.notNull("field:parameterMap", parameterMap);
		return this.parameterMap;
	}

	protected boolean hasParameterMap() {
		return this.parameterMap != null;
	}

	protected void setParameterMap(final Map<String,String[]> parameterMap) {
		Checker.notNull("parameter:parameterMap", parameterMap);
		this.parameterMap = parameterMap;
	}

	protected void createParameterMap() {
		final Map<String,String[]> view = new HashMap<String,String[]>();
		final RequestParameters parameters = this.getRequestParameters();
		final Iterator<String> keys = parameters.names();

		while (keys.hasNext()) {
			final String key = keys.next();
			final String[] value = parameters.getValues(key);
			view.put(key, value);
		}

		this.setParameterMap(Collections.unmodifiableMap(view));
	}

	public String toString() {
		return super.toString() + ", url\"" + url + "\", headers: " + headers + ", requestParameters:" + requestParameters;
	}
}
