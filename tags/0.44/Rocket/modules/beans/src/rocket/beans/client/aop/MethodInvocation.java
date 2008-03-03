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
 * Provides a handle for an interceptor for the method being proxied.
 * 
 * @author Miroslav Pokorny
 */
public interface MethodInvocation {

	/**
	 * Returns the this reference for the object being proxied
	 * 
	 * @return
	 */
	Object getTarget();

	/**
	 * Returns an array of the parameters for this method invocation. Primitive
	 * values will be wrapped inside their respective wrappers
	 * 
	 * @return
	 */
	Object[] getParameters();

	/**
	 * Requests that the next interceptor or the proxy itself be executed.
	 * 
	 * @return The returned object.
	 * @throws Throwable
	 *             any exception may be thrown
	 */
	Object proceed() throws Throwable;
	
	/**
	 * Retrieves the name of the current method.
	 * @return
	 */
	String getMethod();
	
	/**
	 * Returns the actual parameter types for the method not the typs of the incoming parameters.
	 * @return An array of Strings.
	 */
	String[] getParameterTypes();
	
	/**
	 * Returns the fully qualified name of the return type for this method.
	 * @return
	 */
	String getReturnType();
		
	/**
	 * Tests if the current method is native (jsni)
	 * @return True if it is false otherwise.
	 */
	boolean isNative();
	
	/**
	 * Returns the name of the class containing this method.
	 * @return The fully qualified name of the class
	 */
	String getEnclosingType();
}
