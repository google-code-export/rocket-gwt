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
package rocket.remoting.client;

/**
 * This callback contains numerous methods that are related to a comet session.
 * @author Miroslav Pokorny
 */
public interface CometCallback {
	/**
	 * This method is invoked each time an object payload is received from the server
	 * @param object
	 */
	void onPayload( Object object );
	
	/**
	 * This method is invoked when the server disconnects or terminates a comet session.
	 */
	void onTerminate();
	
	/**
	 * This method is fired whenever something goes wrong, including stuff like a dropped
	 * connection.
	 * @param cause An exception representing the problem.
	 */
	void onFailure( Throwable cause );
	
}
