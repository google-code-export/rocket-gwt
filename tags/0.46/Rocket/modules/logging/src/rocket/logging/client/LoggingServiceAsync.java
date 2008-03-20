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
package rocket.logging.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The logging service defines an interface between the client and server.
 * Typically if a throwable is not serialized a null will be sent to the server.
 * 
 * @author Miroslav Pokorny
 */
public interface LoggingServiceAsync {
	void log(LoggingEvent message, AsyncCallback callback);
}
