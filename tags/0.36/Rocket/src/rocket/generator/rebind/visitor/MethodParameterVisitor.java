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
package rocket.generator.rebind.visitor;

import java.util.Iterator;

import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.util.client.ObjectHelper;

/**
 * A template that makes it easy to visit all the parameters belonging to a
 * method.
 * 
 * @author Miroslav Pokorny
 */
abstract public class MethodParameterVisitor {

	public void start(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final Iterator parameters = method.getParameters().iterator();
		while (parameters.hasNext()) {
			final MethodParameter parameter = (MethodParameter) parameters.next();
			if (this.visit(parameter)) {
				break;
			}
		}
	}

	/**
	 * Each parameter belonging to the method is passed to this method.
	 * 
	 * @param parameter
	 * @return true to stop visiting remaining parameters, false continues.
	 */
	abstract protected boolean visit(MethodParameter parameter);
}
