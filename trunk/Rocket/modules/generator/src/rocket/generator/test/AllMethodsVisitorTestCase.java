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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.java.JavaGeneratorContext;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.AllMethodsVisitor;

public class AllMethodsVisitorTestCase extends TestCase {

	public void testVisitAllJavaLangObjectMethods() {
		final GeneratorContext context = this.createGeneratorContext();

		final List<Method> methodsVisited = new ArrayList<Method>();

		final AllMethodsVisitor visitor = new AllMethodsVisitor() {
			protected boolean visit(final Method method) {
				methodsVisited.add(method);
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return false;
			}
		};

		final Type object = context.getObject();
		visitor.start(object);

		final List noArguments = Collections.EMPTY_LIST;
		assertTrue(methodsVisited.contains(object.getMethod("clone", noArguments)));

		assertTrue(methodsVisited.contains(object.getMethod("equals", Arrays.asList(new Type[] { object }))));

		assertTrue(methodsVisited.contains(object.getMethod("finalize", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("getClass", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("hashCode", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("notify", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("notifyAll", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("toString", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("wait", noArguments)));

		final Type longType = context.getLong();
		assertTrue(methodsVisited.contains(object.getMethod("wait", Arrays.asList(new Type[] { longType }))));
		assertTrue(methodsVisited.contains(object.getMethod("wait", Arrays.asList(new Type[] { longType, context.getInt() }))));
	}

	public void testVisitSubClassOfObjectMethods() {
		final GeneratorContext context = this.createGeneratorContext();

		final List<Method> methodsVisited = new ArrayList<Method>();

		final AllMethodsVisitor visitor = new AllMethodsVisitor() {
			protected boolean visit(final Method method) {
				methodsVisited.add(method);
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return false;
			}
		};

		final Type test = context.getType(Test.class.getName());
		visitor.start(test);

		final Type object = context.getObject();
		final List noArguments = Arrays.asList(new Type[0]);

		assertTrue(methodsVisited.contains(test.getMethod("dummy", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("clone", noArguments)));

		assertTrue(methodsVisited.contains(object.getMethod("equals", Arrays.asList(new Type[] { object }))));

		assertTrue(methodsVisited.contains(object.getMethod("finalize", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("getClass", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("hashCode", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("notify", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("notifyAll", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("toString", noArguments)));
		assertTrue(methodsVisited.contains(object.getMethod("wait", noArguments)));

		final Type longType = context.getLong();
		assertTrue(methodsVisited.contains(object.getMethod("wait", Arrays.asList(new Type[] { longType }))));
		assertTrue(methodsVisited.contains(object.getMethod("wait", Arrays.asList(new Type[] { longType, context.getInt() }))));
	}

	public void testSkipJavaLangObjectMethods() {
		final GeneratorContext context = this.createGeneratorContext();

		final List<Method> methodsVisited = new ArrayList<Method>();

		final AllMethodsVisitor visitor = new AllMethodsVisitor() {
			protected boolean visit(final Method method) {
				methodsVisited.add(method);
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};

		final Type test = context.getType(Test.class.getName());
		visitor.start(test);

		assertEquals(1, methodsVisited.size());
		assertTrue(methodsVisited.contains(test.getMethod("dummy", Collections.EMPTY_LIST)));
	}

	public void testSkipRemainingMethods() {
		final GeneratorContext context = this.createGeneratorContext();

		final List<Method> methodsVisited = new ArrayList<Method>();

		final AllMethodsVisitor visitor = new AllMethodsVisitor() {
			protected boolean visit(final Method method) {
				methodsVisited.add(method);
				return true;
			}

			protected boolean skipJavaLangObjectMethods() {
				return false;
			}
		};

		final Type test = context.getType(Test.class.getName());
		visitor.start(test);

		assertEquals(1, methodsVisited.size());
		assertTrue(methodsVisited.contains(test.getMethod("dummy", Collections.EMPTY_LIST)));
	}

	static class Test extends Object {
		public void dummy() {
		}
	}

	GeneratorContext createGeneratorContext() {
		return new JavaGeneratorContext();
	}
}
