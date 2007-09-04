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
package rocket.remoting.test.webrequest.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.util.server.ObjectHelper;

/**
 * This servlet simply echos all request parameters in alphabetical order using
 * a line for each parameter. The first line however contains the method type.
 * GET and POST requests are processed in the same manner.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RequestParametersEchoServlet extends HttpServlet {
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		this.processRequest(request, response);
	}

	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		this.processRequest(request, response);
	}

	public void processRequest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		ObjectHelper.checkNotNull("parameter:request", request);
		ObjectHelper.checkNotNull("parameter:response", response);

		final Map sorted = new TreeMap();
		final Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			final String name = (String) names.nextElement();
			final String[] values = request.getParameterValues(name);
			sorted.put(name, values);
		}

		final PrintWriter writer = response.getWriter();
		writer.println(request.getMethod());

		final Iterator entries = sorted.entrySet().iterator();
		while (entries.hasNext()) {
			final Map.Entry entry = (Map.Entry) entries.next();
			final String name = (String) entry.getKey();
			final String[] values = (String[]) entry.getValue();

			for (int i = 0; i < values.length; i++) {
				writer.print(name);
				writer.print('=');
				writer.println(values[i]);
			}
		}

	}
}
