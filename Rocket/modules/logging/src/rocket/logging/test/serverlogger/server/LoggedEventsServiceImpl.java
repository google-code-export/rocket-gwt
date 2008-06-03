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
package rocket.logging.test.serverlogger.server;

import java.util.ArrayList;
import java.util.List;

import rocket.logging.client.LoggingEvent;
import rocket.logging.server.LoggingLevelWriter;
import rocket.logging.test.serverlogger.client.LoggedEventsService;
import rocket.remoting.server.java.JavaRpcServiceMethodInvoker;
import rocket.remoting.server.java.JavaRpcServiceServlet;
import rocket.remoting.server.java.ServerSerializationFactory;
import rocket.serialization.client.ObjectWriter;

public class LoggedEventsServiceImpl extends JavaRpcServiceServlet implements LoggedEventsService {

	public void clearLoggedEvents() {
		TestAppender.clear();
	}

	public List<LoggingEvent> getLoggedEvents() {
		return TestAppender.getEvents();
	}

	protected JavaRpcServiceMethodInvoker createRpcServiceMethodInvoker() {
		return new JavaRpcServiceMethodInvoker() {

			@Override
			protected ServerSerializationFactory createSerializationFactory() {
				return new ServerSerializationFactory() {
					protected List<ObjectWriter> createObjectWriters() {
						final List<ObjectWriter> writers = new ArrayList<ObjectWriter>();
						writers.add(new LoggingLevelWriter());
						writers.addAll(super.createObjectWriters());
						return writers;
					}
				};
			}
		};
	}
}
