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
package rocket.serialization.test.rebind.typematcher;

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.Type;
import rocket.serialization.rebind.typematcher.TypeMatcher;
import rocket.serialization.rebind.typematcher.TypeMatcherFactory;

public class TypeMatcherTestCase extends TestCase {
	public void testExact() {
		final TypeMatcher matcher = TypeMatcherFactory.createTypeNameMatcher("a.b.C");
		final Type abC = createType("a.b.C");
		assertTrue(matcher.matches(abC));

		final Type deF = createType("d.e.F");
		assertFalse(matcher.matches(deF));
	}

	public void testWildcard() {
		final TypeMatcher matcher = TypeMatcherFactory.createTypeNameMatcher("a.b.*");
		final Type abC = createType("a.b.C");
		assertTrue(matcher.matches(abC));

		final Type deF = createType("d.e.F");
		assertFalse(matcher.matches(deF));
	}

	Type createType(final String name) {

		return new Type() {
			public Constructor findConstructor(List parameterTypes) {
				throw new UnsupportedOperationException();
			}

			public Field findField(String name) {
				throw new UnsupportedOperationException();
			}

			public Method findMethod(String methodName, List parameterTypes) {
				throw new UnsupportedOperationException();
			}

			public Method findMostDerivedMethod(String methodName, List parameterTypes) {
				throw new UnsupportedOperationException();
			}

			public Type getComponentType() {
				throw new UnsupportedOperationException();
			}

			public Constructor getConstructor(List parameterTypes) {
				throw new UnsupportedOperationException();
			}

			public Set getConstructors() {
				throw new UnsupportedOperationException();
			}

			public Field getField(String name) {
				throw new UnsupportedOperationException();
			}

			public Set getFields() {
				throw new UnsupportedOperationException();
			}

			public Set getInterfaces() {
				throw new UnsupportedOperationException();
			}

			public String getJsniNotation() {
				throw new UnsupportedOperationException();
			}

			public Method getMethod(String methodName, List parameterTypes) {
				throw new UnsupportedOperationException();
			}

			public Set getMethods() {
				throw new UnsupportedOperationException();
			}

			public Method getMostDerivedMethod(String methodName, List parameterTypes) {
				throw new UnsupportedOperationException();
			}

			public String getName() {
				return name;
			}

			public Set getNestedTypes() {
				throw new UnsupportedOperationException();
			}

			public Package getPackage() {
				throw new UnsupportedOperationException();
			}

			public String getRuntimeName() {
				throw new UnsupportedOperationException();
			}

			public String getSimpleName() {
				throw new UnsupportedOperationException();
			}

			public Set getSubTypes() {
				throw new UnsupportedOperationException();
			}

			public Type getSuperType() {
				throw new UnsupportedOperationException();
			}

			public Visibility getVisibility() {
				throw new UnsupportedOperationException();
			}

			public Type getWrapper() {
				throw new UnsupportedOperationException();
			}

			public boolean hasNoArgumentsConstructor() {
				throw new UnsupportedOperationException();
			}

			public boolean isAbstract() {
				throw new UnsupportedOperationException();
			}

			public boolean isArray() {
				throw new UnsupportedOperationException();
			}

			public boolean isAssignableFrom(Type type) {
				throw new UnsupportedOperationException();
			}

			public boolean isAssignableTo(Type type) {
				throw new UnsupportedOperationException();
			}

			public boolean isFinal() {
				throw new UnsupportedOperationException();
			}

			public boolean isInterface() {
				throw new UnsupportedOperationException();
			}

			public boolean isPrimitive() {
				throw new UnsupportedOperationException();
			}

			public List getMetadataValues(String name) {
				throw new UnsupportedOperationException();
			}

			public GeneratorContext getGeneratorContext() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
