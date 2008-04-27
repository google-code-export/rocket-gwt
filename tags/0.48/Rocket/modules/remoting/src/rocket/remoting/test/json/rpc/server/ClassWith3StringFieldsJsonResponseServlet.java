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

import rocket.remoting.test.json.rpc.client.ClassWith3StringFields;
import flexjson.JSONSerializer;

/**
 * This servlet reads a single parameter and uses it to set the field on a
 * ClassWithStringField instance. This same instance is serialized back to json
 * using the FLEXJson library with the encoded string then written to the
 * response.
 * 
 * @author Miroslav Pokorny
 * 
 */
public class ClassWith3StringFieldsJsonResponseServlet extends HttpServlet {
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		this.handleRequest(request, response);
	}

	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		this.handleRequest(request, response);
	}

	protected void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		System.out.println("SERVER - entering: " + request.getQueryString());

		final ClassWith3StringFields instance = new ClassWith3StringFields();
		instance.field1 = request.getParameter("string1");
		instance.field2 = request.getParameter("string2");
		instance.field3 = request.getParameter("string3");

		final JSONSerializer serializer = new JSONSerializer();
		final String json = serializer.deepSerialize(instance);

		response.getWriter().println(json);
		response.flushBuffer();

		System.out.println("SERVER - returning json\"" + json + "\".");
	}

}
