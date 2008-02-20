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
import java.util.LinkedList;
import java.util.List;

import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * Visits all the super types belonging to the type starting at java.lang.Object
 * towards the given type.
 * 
 * @author Miroslav Pokorny
 */
abstract public class ReverseSuperTypesVisitor {

	public void start(final Type type) {
		Checker.notNull("type:type", type);

		final List superTypes = this.accumulateSuperTypes(type);

		final Iterator iterator = superTypes.iterator();
		boolean first = true;
		while (iterator.hasNext()) {
			final Type visit = (Type) iterator.next();

			if (first && this.skipInitialType()) {
				first = false;
				continue;
			}

			if (this.visit(visit)) {
				break;
			}
		}
	}

	private final static String OBJECT = Object.class.getName();

	/**
	 * Accumulates all the super types for type into a list.
	 * 
	 * @param type
	 * @return
	 */
	protected List accumulateSuperTypes(final Type type) {
		Checker.notNull("parameter:type", type);

		final List types = new LinkedList();

		Type type0 = type;

		while (true) {
			if (null == type0) {
				break;
			}

			// insert at the front of the list this builds a reversed list.
			types.add(0, type0);

			if (type0.getName().equals(OBJECT)) {
				break;
			}

			type0 = type0.getSuperType();
		}

		return types;
	}

	/**
	 * Each type belonging to the given super type is presented to this type.
	 * 
	 * @param type
	 * @return return true to skip remaining types.
	 */
	abstract protected boolean visit(final Type type);

	/**
	 * If this method returns true the initial type passed to
	 * {@link #start(Type)} is skipped and not visited.
	 * 
	 * @return
	 */
	abstract protected boolean skipInitialType();
}
