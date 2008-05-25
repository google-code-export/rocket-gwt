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
package rocket.generator.rebind.constructor;

import java.util.Iterator;

import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewNestedType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.util.AbstractConstructorOrMethod;

/**
 * Base class for any constructor implementation.
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractConstructor extends AbstractConstructorOrMethod implements Constructor {
	public NewConstructor copy(final NewConcreteType newConcreteType) {
		final NewConstructor constructor = newConcreteType.newConstructor();
		this.copy0(constructor);
		return constructor;
	}

	public NewConstructor copy(final NewNestedType newNestedType) {
		final NewConstructor constructor = newNestedType.newConstructor();
		this.copy0(constructor);
		return constructor;
	}

	/**
	 * Copies the parameters of this constructor to the given constructor
	 * 
	 * @param constructor
	 *            A new constructor being built.
	 */
	protected void copy0(final NewConstructor constructor) {
		final Iterator parameters = this.getParameters().iterator();
		while (parameters.hasNext()) {
			final ConstructorParameter parameter = (ConstructorParameter) parameters.next();
			constructor.addParameter(parameter.copy());
		}

		final Iterator thrownTypes = this.getThrownTypes().iterator();
		while (thrownTypes.hasNext()) {
			constructor.addThrownType((Type) thrownTypes.next());
		}

		constructor.setVisibility(this.getVisibility());
	}
}
