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

import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * Visits all the super types belonging to the type starting with the given type
 * working towards java.lang.Object
 * 
 * @author Miroslav Pokorny
 */
abstract public class SuperTypesVisitor {

	private final static String OBJECT = Object.class.getName();
	
	public void start(final Type type) {
		ObjectHelper.checkNotNull("type:type", type);

		Type type0 = type;
		if (this.skipInitialType()) {
			type0 = type.getSuperType();
		}

		while (true) {
			if (null == type0) {
				break;
			}

			if (this.visit(type0)) {
				break;
			}

			if (type0.getName().equals(OBJECT)) {
				break;
			}

			type0 = type0.getSuperType();
		}
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
