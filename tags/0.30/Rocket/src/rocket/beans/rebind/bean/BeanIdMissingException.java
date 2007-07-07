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
package rocket.beans.rebind.bean;

import rocket.beans.rebind.BeanFactoryGeneratorException;

/**
 * This exception is thrown whenever the id attribute is missing for a bean tag.
 * 
 * @author Miroslav Pokorny
 * 
 */
public class BeanIdMissingException extends BeanFactoryGeneratorException {

	public BeanIdMissingException() {
	}

	public BeanIdMissingException(String message) {
		super(message);
	}

	public BeanIdMissingException(Throwable cause) {
		super(cause);
	}

	public BeanIdMissingException(String message, Throwable cause) {
		super(message, cause);
	}

}
