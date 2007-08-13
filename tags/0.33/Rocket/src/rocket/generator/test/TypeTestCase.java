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
package rocket.generator.test;

import java.util.Collections;

import junit.framework.TestCase;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.java.JavaGeneratorContext;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.MethodNotFoundException;
import rocket.generator.rebind.type.Type;

public class TypeTestCase extends TestCase {

	public void testFindMostDerivedWhereMethodExistsInMostDerivedType() {
		final GeneratorContext context = this.createGeneratorContext();

		final Type type = context.getType(SuperClass.class.getName());
		final Method method = type.getMethod("dummy", Collections.EMPTY_LIST);
		final Method mostDerived = type.findMostDerivedMethod("dummy", Collections.EMPTY_LIST);
		assertSame(method, mostDerived);
	}

	static class MostDerived extends SuperClass {
		public void dummy() {
		}
	}

	static class SuperClass {
		public void dummy() {
		}
	}

	public void testFindMostDerivedWhereMethodExistsInSuperType() {
		final GeneratorContext context = this.createGeneratorContext();

		final Type subType = context.getType(SubClassOfSuperClass.class.getName());
		final Type superType = context.getType(SuperClass.class.getName());
		final Method method = superType.getMethod("dummy", Collections.EMPTY_LIST);
		final Method mostDerived = subType.findMostDerivedMethod("dummy", Collections.EMPTY_LIST);
		assertSame(method, mostDerived);
	}

	static class SubClassOfSuperClass extends SuperClass {
	}

	public void testFindMostDerivedWhereMethodDoesntExist() {
		final GeneratorContext context = this.createGeneratorContext();

		final Type type = context.getType(ClassWithNoMethods.class.getName());
		final Method mostDerived = type.findMostDerivedMethod("dummy", Collections.EMPTY_LIST);
		assertNull(mostDerived);
	}

	public void testGetMostDerivedWhereMethodDoesntExist() {
		final GeneratorContext context = this.createGeneratorContext();

		final Type type = context.getType(ClassWithNoMethods.class.getName());
		try {
			final Method mostDerived = type.getMostDerivedMethod("dummy", Collections.EMPTY_LIST);
			fail("An MethodNotFoundException should have been thrown.");
		} catch (final MethodNotFoundException expected) {
		}
	}

	static class ClassWithNoMethods {

	}

	GeneratorContext createGeneratorContext() {
		return new JavaGeneratorContext();
	}
}
