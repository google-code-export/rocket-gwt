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
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * Template that visits all the methods belonging to a type.
 * 
 * @author Miroslav Pokorny
 */
abstract public class TypeMethodsVisitor {

	public void start(final Type type) {
		Checker.notNull("method:type", type);

		final Iterator<Method> methods = type.getMethods().iterator();
		while (methods.hasNext()) {
			final Method method = methods.next();
			if (this.visit(method)) {
				break;
			}
		}
	}

	/**
	 * Each method belonging to the given type is presented to this method.
	 * 
	 * @param method
	 * @return return true to skip remaining methods.
	 */
	abstract protected boolean visit(final Method method);
}
