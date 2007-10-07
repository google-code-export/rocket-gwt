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

import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * This visitor visits all the methods for the entire type heirarchy of the
 * gtiven type.
 * 
 * @author Miroslav Pokorny
 */
abstract public class AllMethodsVisitor {

	/**
	 * Starts the visiting process starting at the most derived type towards
	 * java.lang.Object.
	 * 
	 * @param derivedType
	 */
	public void start(final Type derivedType) {
		ObjectHelper.checkNotNull("parameter:derivedType", derivedType);

		final AllMethodsVisitorSuperTypesVisitor visitor = new AllMethodsVisitorSuperTypesVisitor();
		visitor.start(derivedType);
	}

	/**
	 * Private inner class which visits all super types for the given type. FOr
	 * each type all its methods are then visited.
	 */
	private class AllMethodsVisitorSuperTypesVisitor extends SuperTypesVisitor {

		protected boolean visit(final Type type) {
			ObjectHelper.checkNotNull("parameter:type", type);

			if (false == (AllMethodsVisitor.this.skipJavaLangObjectMethods() && type.equals(type.getGeneratorContext().getObject()))) {

				final TypeMethodsVisitor methodVisitor = new TypeMethodsVisitor() {
					protected boolean visit(final Method method) {
						final boolean skipRemaining = AllMethodsVisitor.this.visit(method);
						AllMethodsVisitorSuperTypesVisitor.this.setSkipRemaining(skipRemaining);
						return skipRemaining;
					}
				};
				methodVisitor.start(type);
			}
			return this.isSkipRemaining();
		}

		protected boolean skipInitialType() {
			return false;
		}

		boolean skipRemaining;

		protected boolean isSkipRemaining() {
			return this.skipRemaining;
		}

		protected void setSkipRemaining(final boolean escape) {
			this.skipRemaining = escape;
		}
	};

	/**
	 * Each method is presented to this method.
	 * 
	 * @param method
	 * @return Return true to skip remaining methods, false continues
	 */
	abstract protected boolean visit(Method method);

	/**
	 * When true indicates that all methods belonging to java.lang.Object are
	 * not visited.
	 * 
	 * @return
	 */
	abstract protected boolean skipJavaLangObjectMethods();
}
