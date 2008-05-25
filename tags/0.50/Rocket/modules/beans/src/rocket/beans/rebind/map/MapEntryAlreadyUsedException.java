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
package rocket.beans.rebind.map;

import rocket.beans.rebind.BeanFactoryGeneratorException;

/**
 * THis exception is thrown whenever a map entry is added to a map using a
 * previous defined key.
 * 
 * @author Miroslav Pokorny
 * 
 */
public class MapEntryAlreadyUsedException extends BeanFactoryGeneratorException {

	/**
	 * 
	 */
	public MapEntryAlreadyUsedException() {
	}

	/**
	 * @param message
	 */
	public MapEntryAlreadyUsedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MapEntryAlreadyUsedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MapEntryAlreadyUsedException(String message, Throwable cause) {
		super(message, cause);
	}

}
