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

import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * Template that visits all the constructors belonging to a type.
 * 
 * @author Miroslav Pokorny
 */
abstract public class TypeConstructorsVisitor {

	public void start(final Type type) {
		ObjectHelper.checkNotNull("constructor:type", type);

		final Iterator constructors = type.getConstructors().iterator();
		while (constructors.hasNext()) {
			final Constructor constructor = (Constructor) constructors.next();
			if (this.visit(constructor)) {
				break;
			}
		}
	}

	/**
	 * Each constructor belonging to the given type is presented to this
	 * constructor.
	 * 
	 * @param constructor
	 * @return return true to skip remaining constructors.
	 */
	abstract protected boolean visit(final Constructor constructor);
}
