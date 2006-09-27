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
package rocket.server.rpc;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import rocket.client.rpc.Headers;
import rocket.client.rpc.RequestParameters;

/**
 * This Request supports simulating a GET request within the same web application
 *
 * @author Miroslav Pokorny (mP)
 */
public class GetHttpServletRequest extends AbstractHttpServletRequest implements HttpServletRequest {

	public GetHttpServletRequest(final HttpServletRequest request, final String url, final Headers headers) {
		super(request, url, headers);

		final RequestParameters parameters = new RequestParameters();
		parameters.buildFromUrl( url );
		this.setRequestParameters(parameters);
	}

	public GetHttpServletRequest(final HttpServletRequest request, final String url, final Headers headers, final RequestParameters parameters) {
		super(request, url, headers);

		this.setRequestParameters(parameters);
	}


	public String getMethod() {
		return "GET";
	}

	/**
	 * Gets by definition dont have any body.
	 */
	public int getContentLength() {
		throw new UnsupportedOperationException("getContentLength() is not supported for GET requests");
	}

	/**
	 * There is no body and no content type.
	 */
	public String getContentType() {
		throw new UnsupportedOperationException("getContentType() is not supported for GET requests");
	}

	/**
	 * @todo Might be the wrong behaviour might need to throw an exception.
	 */
	public ServletInputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException("getInputStream() is not supported for GET requests");
	}

	public BufferedReader getReader() throws IOException {
		throw new UnsupportedOperationException("getReader() is not supported for GET requests");
	}
}
