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
package rocket.beans.client.aop;

/**
 * This exception is a wrapper around any checked exception not declared by a
 * method that may occur during a interceptor chain invocation.
 * 
 * @author Miroslav Pokorny
 */
public class AopInvocationException extends RuntimeException {

	/**
	 * 
	 */
	public AopInvocationException() {
	}

	/**
	 * @param string
	 */
	public AopInvocationException(String string) {
		super(string);
	}

	/**
	 * @param throwable
	 */
	public AopInvocationException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * @param string
	 * @param throwable
	 */
	public AopInvocationException(String string, Throwable throwable) {
		super(string, throwable);
	}

}
