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
 * This exception is called whenever a rpc service invocation fails for whatever
 * reason.
 * 
 * @author Miroslav Pokorny
 */
public class RpcException extends RuntimeException {

	/**
	 * Default no arguments constructor
	 */
	public RpcException() {
	}

	/**
	 * @param message
	 *            A message
	 */
	public RpcException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            The cause being wrapped
	 */
	public RpcException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 *            An accompanying message
	 * @param cause
	 *            The cause
	 */
	public RpcException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
