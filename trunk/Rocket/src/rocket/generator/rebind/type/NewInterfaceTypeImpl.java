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
package rocket.generator.rebind.type;

import rocket.generator.rebind.constructor.NewConstructor;

/**
 * Base class for any generated concrete type that is not an anonymous inner
 * class.
 * 
 * @author Miroslav Pokorny
 */
public class NewInterfaceTypeImpl extends NewConcreteOrInterfaceType implements NewInterfaceType {

	public NewInterfaceTypeImpl() {
		super();
	}

	public void addInterface(Type interfacee) {
		throw new UnsupportedOperationException("Interfaces do not implement other interfaces, interface: " + this);
	}

	public NewConstructor newConstructor() {
		throw new UnsupportedOperationException("Interfaces do not have constructors, interface: " + this);
	}

	public void addConstructor(final NewConstructor constructor) {
		throw new UnsupportedOperationException("Interfaces do not have constructors, interface: " + this);
	}

	public NewAnonymousNestedType newAnonymousNestedType() {
		throw new UnsupportedOperationException("Interfaces cannot have anonymous nested types, interface: " + this);
	}
}
