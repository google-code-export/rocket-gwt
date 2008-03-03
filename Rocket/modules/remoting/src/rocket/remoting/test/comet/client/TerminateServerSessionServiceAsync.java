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
package rocket.remoting.test.comet.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of {@link TerminateServerSessionService}
 * Defines a service which invokes tells the test comet service to request a terminate.
 * @author Miroslav Pokorny
 */
public interface TerminateServerSessionServiceAsync{
	void terminate( AsyncCallback callback );
}
