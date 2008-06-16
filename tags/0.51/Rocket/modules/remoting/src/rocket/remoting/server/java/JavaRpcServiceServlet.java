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
package rocket.remoting.server.java;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.util.client.Checker;
import rocket.util.server.InputOutput;

/**
 * This servlet performs a similar task to
 * {@link com.google.gwt.user.server.rpc.RemoteServiceServlet}, in that the
 * developer is required to implement the service interface they wish to expose.
 * 
 * Unlike the GWT servlet this serlvet does not attempt to compress any output
 * leaving that responsibility to something else such as the Apache HTTPd or a
 * separate filter.
 * 
 * @author Miroslav Pokorny
 */
public class JavaRpcServiceServlet extends HttpServlet {

	/**
	 * Handles GET requests.
	 */
	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		this.reportUnsupportedMethod(request, response);
	}

	protected void reportUnsupportedMethod(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	/**
	 * Accepts a rpc via the post method, invokes the method and writes the
	 * result returned by the invoker
	 * 
	 * @param request
	 *            The request
	 * @param response
	 *            The response
	 * @throws ServletException
	 *             if anything goes wrong.
	 * @throws IOException
	 *             if anything goes wrong.
	 */
	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		try {
			this.doPost0(request, response);
		} catch (final Throwable caught) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
			response.flushBuffer();
		}
	}

	protected void doPost0(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final String serializedRequest = consumePostData(request);
		final String output = this.invoke(serializedRequest.toString());

		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().print(output);
		response.flushBuffer();
	}

	/**
	 * Helper which consumes all of the post data from the given request and
	 * returns a string.
	 * 
	 * @param request
	 * @return The posted data as a String
	 * @throws IOException
	 */
	protected String consumePostData(final HttpServletRequest request) throws IOException {
		Checker.notNull("parameter:request", request);

		final StringBuffer buf = new StringBuffer();
		final char[] chars = new char[1024];
		BufferedReader reader = null;

		try {
			reader = request.getReader();

			while (true) {
				final int readCount = reader.read(chars);
				if (-1 == readCount) {
					break;
				}
				buf.append(chars, 0, readCount);
			}

			return buf.toString();
		} finally {
			InputOutput.closeIfNecessary(reader);
		}

	}

	/**
	 * Invokes the service method taking care of both deserializing and
	 * serializing of objects.
	 * 
	 * @param stream
	 * @return
	 */
	protected String invoke(final String stream) {
		final JavaRpcServiceMethodInvoker invoker = this.createRpcServiceMethodInvoker();
		final String output = invoker.invoke(stream, this);
		return output;
	}

	/**
	 * Factory method which creates a {@link JavaRpcServiceMethodInvoker}
	 * 
	 * @return The invoker
	 */
	protected JavaRpcServiceMethodInvoker createRpcServiceMethodInvoker() {
		return new JavaRpcServiceMethodInvoker();
	}
}
