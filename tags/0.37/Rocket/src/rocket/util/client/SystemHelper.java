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
package rocket.util.client;

/**
 * The common base class for all helpers regardless of their categorised type.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class SystemHelper {

	final static String PARAMETER = "parameter:";

	final static String FIELD = "field:";

	final static String ASSERT = "assert:";

	/**
	 * Convenience method which builds the appropriate exception includes the
	 * message and throws the excection
	 * 
	 * @param name
	 *            The name of the variable. This is used to construct the error
	 *            message of the built exception
	 * @param message
	 *            The message
	 */
	public static void fail(final String name, final String message) {
		if (name != null) {
			if (name.startsWith(PARAMETER)) {
				throw new IllegalArgumentException(message);
			}
			if (name.startsWith(FIELD)) {
				throw new IllegalStateException(message);
			}
		}
		throw new AssertionError(message);
	}

	public static void fail(final String message) {
		throw new AssertionError(message);
	}

	protected SystemHelper() {
	}
}