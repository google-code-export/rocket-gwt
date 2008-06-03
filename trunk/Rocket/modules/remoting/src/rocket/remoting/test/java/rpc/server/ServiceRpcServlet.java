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
package rocket.remoting.test.java.rpc.server;

import rocket.remoting.server.java.JavaRpcServiceServlet;
import rocket.remoting.test.java.rpc.client.DeclaredException;
import rocket.remoting.test.java.rpc.client.Payload;
import rocket.remoting.test.java.rpc.client.Service;

public class ServiceRpcServlet extends JavaRpcServiceServlet implements Service {
	public Payload echo(final Payload payload) {
		return payload;
	}

	public void throwsDeclaredException() throws DeclaredException {
		throw new DeclaredException();
	}

	public void throwsUndeclaredException() {
		throw new RuntimeException();
	}
}
