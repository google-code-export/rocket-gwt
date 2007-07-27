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
package rocket.generator.rebind.primitive;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.AbstractType;
import rocket.generator.rebind.type.Type;

/**
 * A convenient base class for any primitive type.
 * 
 * @author Miroslav Pokorny
 */
abstract public class PrimitiveType extends AbstractType {
	protected Set createInterfaces() {
		throw new UnsupportedOperationException();
	}

	protected Set createSubTypes() {
		throw new UnsupportedOperationException();
	}

	public Type getSuperType() {
		return null;
	}

	public Package getPackage() {
		return null;
	}

	public Set getConstructors() {
		return Collections.EMPTY_SET;
	}

	protected Set createConstructors() {
		throw new UnsupportedOperationException();
	}

	public Constructor findConstructor(final List parameterTypes) {
		return null;
	}

	public Set getFields() {
		return Collections.EMPTY_SET;
	}

	protected Set createFields() {
		throw new UnsupportedOperationException();
	}

	public Field findField(String name) {
		return null;
	}

	public Set getMethods() {
		return Collections.EMPTY_SET;
	}

	protected Set createMethods() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Primitive types dont have methods so this method always fails returning
	 * null.
	 */
	public Method findMethod(final String method, final List parameterTypes) {
		return null;
	}

	public boolean isAbstract() {
		return false;
	}

	public boolean isArray() {
		return false;
	}

	public Type getComponentType() {
		return null;
	}

	public boolean isFinal() {
		return true;
	}

	public boolean isInterface() {
		return false;
	}

	public boolean isPrimitive() {
		return true;
	}

	public List getMetadataValues(final String name) {
		return Collections.EMPTY_LIST;
	}

	protected Set createNestedTypes() {
		return Collections.EMPTY_SET;
	}
}
