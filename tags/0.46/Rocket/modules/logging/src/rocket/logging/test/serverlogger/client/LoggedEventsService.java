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
package rocket.logging.test.serverlogger.client;

import java.util.List;

import rocket.remoting.client.JavaRpcService;

/**
 * This service only provides a mechanism for the gwt test case to learn about
 * what logging has actually reached the server to be logged
 * 
 * @author Miroslav Pokorny
 */
public interface LoggedEventsService extends JavaRpcService {
	/**
	 * Returns a list of LoggingEvents
	 * 
	 * @return
	 * 
	 * @serialization-type rocket.logging.client.LoggingEvent
	 */
	List getLoggedEvents();

	void clearLoggedEvents();
}
