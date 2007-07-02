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
package rocket.beans.rebind;


/**
 * This exception is thrown whenever the generator is asked to create a
 * BeanFactory implementation for a class or an interface that is not a
 * BeanFactory.
 * 
 * @author Miroslav Pokorny
 * 
 */
public class NotABeanFactoryException extends BeanFactoryGeneratorException {

	/**
	 * 
	 */
	public NotABeanFactoryException() {
	}

	/**
	 * @param message
	 */
	public NotABeanFactoryException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NotABeanFactoryException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotABeanFactoryException(String message, Throwable cause) {
		super(message, cause);
	}

}
