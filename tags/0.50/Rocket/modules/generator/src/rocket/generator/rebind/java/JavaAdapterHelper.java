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
package rocket.generator.rebind.java;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.util.client.Checker;

/**
 * A collection of helper methods used by various classes belonging to this
 * package.
 * 
 * @author Miroslav Pokorny
 */
class JavaAdapterHelper {
	/**
	 * GeneratorHelper which creates a set of Types from the given Class
	 * 
	 * @param generatorContext
	 * @param types
	 * @return
	 */
	static public Set asSetOfTypes(final GeneratorContext generatorContext, final Class[] types) {
		Checker.notNull("parameter:generatorContext", generatorContext);
		Checker.notNull("parameter:types", types);

		final Set set = new HashSet();
		for (int i = 0; i < types.length; i++) {
			set.add(generatorContext.findType(types[i].getName()));
		}

		return set;
	}

	static public Visibility getVisibility(final int modifiers) {
		Visibility visibility = null;

		while (true) {
			if (Modifier.isPublic(modifiers)) {
				visibility = Visibility.PUBLIC;
				break;
			}
			if (Modifier.isProtected(modifiers)) {
				visibility = Visibility.PROTECTED;
				break;
			}
			if (Modifier.isPrivate(modifiers)) {
				visibility = Visibility.PRIVATE;
				break;
			}
			visibility = Visibility.PACKAGE_PRIVATE;
			break;
		}

		return visibility;
	}
}
