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
 * This exception is thrown whenever an attempt is made to reference a bean that
 * does not exist.
 * 
 * @author Miroslav Pokorny
 */
public class BeanIdNotFoundException extends BeanFactoryGeneratorException {

	/**
	 * 
	 */
	public BeanIdNotFoundException() {
	}

	/**
	 * @param message
	 */
	public BeanIdNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public BeanIdNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BeanIdNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
