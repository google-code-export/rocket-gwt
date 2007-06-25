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
package rocket.remoting.rebind.json;

/**
 * This exception is thrown whenever the code generator finds a problem between
 * the service and async interfaces. Problems include
 * <ul>
 * <li>Missing Async interface</li>
 * <li>Async method doesnt return void</li>
 * <li>Unable to find corresponding method on Async interface</li>
 * </ul>
 * 
 * @author Miroslav Pokorny
 * 
 */
public class IncompatibleInterfacesException extends RemoteJsonServiceGeneratorException {

	public IncompatibleInterfacesException(final String message) {
		super(message);
	}
}
