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
import rocket.generator.rebind.type.Type;

public class MethodTestCase extends TestCase {

	public void testGetOverridesDoesntOverriddenMethod() {
		final GeneratorContext context = this.createGeneratorContext();
		final Type subClass = context.getType(SubClassWithDummyMethodThatOverridesNothing.class.getName());

		final Method method = subClass.getMethod("doesntOverrideAnything", Collections.EMPTY_LIST);
		assertNull(method.findOverriddenMethod());
	}

	static class SuperClass {
	}

	static class SubClassWithDummyMethodThatOverridesNothing extends SuperClass {
		public void doesntOverrideAnything() {
		}
	}

	public void testGetOverridesOverriddesMethod() {
		final GeneratorContext context = this.createGeneratorContext();
		final Type subClass = context.getType(SubClassWithOverridingDummyMethod.class.getName());

		final Method method = subClass.getMethod("dummy", Collections.EMPTY_LIST);
		assertNotNull(method.getOverriddenMethod());
	}

	static class SuperClassWithDummyMethod {
		public void dummy() {
		}
	}

	static class SubClassWithOverridingDummyMethod extends SuperClassWithDummyMethod {
		public void dummy() {
		}
	}

	public void testGetOverridesOverriddesAbstractMethod() {
		final GeneratorContext context = this.createGeneratorContext();
		final Type subClass = context.getType(SubClassWithOverridingAbstractDummyMethod.class.getName());

		final Method method = subClass.getMethod("dummy", Collections.EMPTY_LIST);
		assertNotNull(method.getOverriddenMethod());
	}

	static abstract class SuperClassWithAbstractDummyMethod {
		abstract public void dummy();
	}

	static class SubClassWithOverridingAbstractDummyMethod extends SuperClassWithAbstractDummyMethod {
		public void dummy() {
		}
	}

	public void testGetOverridesVirtualThatDoesntOverridePrivateMethodMethod() {
		final GeneratorContext context = this.createGeneratorContext();
		final Type subClass = context.getType(SubClassWithNonOverridingDummyMethod.class.getName());

		final Method method = subClass.getMethod("dummy", Collections.EMPTY_LIST);
		assertNull(method.findOverriddenMethod());
	}

	static class SuperClassWithPrivateDummyMethod {
		private void dummy() {
		}
	}

	static class SubClassWithNonOverridingDummyMethod extends SuperClassWithPrivateDummyMethod {
		public void dummy() {
		}
	}

	GeneratorContext createGeneratorContext() {
		return new JavaGeneratorContext();
	}
}
