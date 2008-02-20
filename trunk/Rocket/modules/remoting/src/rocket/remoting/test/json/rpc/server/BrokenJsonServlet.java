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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet builds an invalid json encoded response.
 * 
 * @author Miroslav Pokorny
 */
public class BrokenJsonServlet extends HttpServlet {
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		this.handleRequest(request, response);
	}

	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		this.handleRequest(request, response);
	}

	protected void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		System.out.println("SERVER - entering: " + request.getQueryString());

		final String json = "JUNK";
		response.getWriter().print(json);

		System.out.println("SERVER - returning json\"" + json + "\".");
	}
}
