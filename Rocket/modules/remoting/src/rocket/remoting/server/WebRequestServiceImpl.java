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
import rocket.remoting.client.WebRequestService;
import rocket.remoting.client.WebResponse;
import rocket.util.client.Checker;
import rocket.util.client.Tester;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This server side service simply does the WebRequest and returns the
 * WebResponse to the client.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class WebRequestServiceImpl extends RemoteServiceServlet implements WebRequestService {

	public WebResponse doRequest(final WebRequest webRequest) throws FailedWebRequestException {
		try {
			final HttpServletRequest request = this.getThreadLocalRequest();
			final HttpServletResponse response = this.getThreadLocalResponse();

			final WebResponse webResponse = this.doRequest0(request, response, webRequest);
			return webResponse;
		} catch (final FailedWebRequestException caught) {
			this.log(caught.getMessage(), caught);
			throw caught;
		}
	}

	/**
	 * Performs all the necessary boring bits involved with setting up the
	 * necessary fascade to execute and capture any output from the requested
	 * target.
	 * 
	 * @param request
	 * @param response
	 * @param webRequest
	 * @return
	 * @throws FailedWebRequestException
	 */
	protected WebResponse doRequest0(final HttpServletRequest request, final HttpServletResponse response, final WebRequest webRequest)
			throws FailedWebRequestException {
		Checker.notNull("parameter:request", request);
		Checker.notNull("parameter:response", response);
		Checker.notNull("parameter:webRequest", webRequest);

		try {
			// if url includes contxt path drop it.
			final String url = this.dropContextPathIfNecessary(webRequest.getUrl(), request);

			final Headers headers = webRequest.getHeaders();

			GetOrPostHttpServletRequest request0 = null;
			while (true) {
				if (Tester.isGet(webRequest.getMethod())) {
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

			final RequestDispatcher dispatcher = request.getRequestDispatcher(url);
			dispatcher.include(request0, response0);
			response0.flushBuffer();

			final WebResponse webResponse = new WebResponse();
			webResponse.setCode(response0.getStatus());
			webResponse.setMessage(response0.getMessage());
			// @originalWidget not needed for now
			// webResponse.setHeaders(response0.getHeaders());

			final byte[] bytes = response0.toByteArray();
			final String characterEncoding = response0.getCharacterEncoding();
			final String text = null == characterEncoding ? new String(bytes) : new String(bytes, characterEncoding);
			webResponse.setBody(text);
			return webResponse;
		} catch (final Exception caught) {
			caught.printStackTrace();
			throw new FailedWebRequestException(caught.getMessage(), caught);
		}
	}

	/**
	 * Helper which copies all headers from a HttpServletRequest to a Headers
	 * destination.
	 * 
	 * @param source
	 * @param destination
	 */
	protected void copyHeaders(final HttpServletRequest source, final Headers destination) {
		Checker.notNull("parameter:source", source);
		Checker.notNull("parameter:destination", destination);

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

	/**
	 * Helper which copies all headers from a Headers to a HttpServletResponse
	 * 
	 * @param source
	 * @param destination
	 */
	protected void copyHeaders(final Headers source, final HttpServletResponse destination) {
		Checker.notNull("parameter:source", source);
		Checker.notNull("parameter:destination", destination);

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

	/**
	 * Removes the context path from the given url if necessary.
	 * 
	 * @param url
	 * @param request
	 * @return
	 */
	protected String dropContextPathIfNecessary(final String url, final HttpServletRequest request) {
		Checker.notNull("parameter:url", url);
		Checker.notNull("parameter:request", request);

		String checkedUrl = url;
		final String contextPath = request.getContextPath();
		if (url.startsWith(contextPath)) {
			checkedUrl = url.substring(contextPath.length());
		}
		return checkedUrl;
	}

}
