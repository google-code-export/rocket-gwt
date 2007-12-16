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
package rocket.remoting.test.json.rpc.server;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.util.server.IoHelper;

/**
 * This servlet simply echos the post data from the incoming request.
 * 
 * @author Miroslav Pokorny
 */
public class JsonRpcEchoServlet extends HttpServlet {
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Only posts are supported.");
	}

	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		this.handleRequest(request, response);
	}

	protected void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		System.out.println("SERVER - entering: " + request.getQueryString());

		Reader reader = null;
		Writer writer = null;
		final StringBuffer captured = new StringBuffer();

		try {
			reader = request.getReader();
			writer = response.getWriter();

			final char[] buffer = new char[4096];
			while (true) {
				final int readCount = reader.read(buffer);
				if (-1 == readCount) {
					break;
				}
				writer.write(buffer, 0, readCount);
				captured.append(buffer, 0, readCount);
			}
		} finally {
			IoHelper.closeIfNecessary(reader);
			IoHelper.closeIfNecessary(writer);
		}
		System.out.println("SERVER - echoing json\"" + captured + "\".");
	}

}
