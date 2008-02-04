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

import junit.framework.TestCase;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.java.JavaGeneratorContext;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.VirtualMethodVisitor;
import rocket.generator.test.subpackage.SuperPackageTest;

public class VirtualMethodTestCase extends TestCase {

	public void testSkipsPrivateMethods() {
		final GeneratorContext context = this.createGeneratorContext();
		final Type test = context.getType(Test.class.getName());

		final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {
			protected boolean visit(final Method method) {
				TestCase.assertTrue(method.getEnclosingType().equals(test));

				TestCase.assertFalse("private methods should be skipped", method.getVisibility() == Visibility.PRIVATE);

				while (true) {
					final String name = method.getName();
					if (name.equals("publicMethod")) {
						break;
					}
					if (name.equals("protectedMethod")) {
						break;
					}
					if (name.equals("packagePrivateMethod")) {
						break;
					}

					TestCase.fail("Unknown method " + method);
				}

				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};
		visitor.start(test);
	}

	static class Test {
		static public void staticMethod() {
		}

		public void publicMethod() {

		}

		protected void protectedMethod() {

		}

		void packagePrivateMethod() {

		}

		private void privateMethod() {

		}
	}

	public void testSkipsPackagePrivateMethodsForClassInDifferentPackage() {
		final GeneratorContext context = this.createGeneratorContext();
		final Type test = context.getType(SubClassOfSubPackageTest.class.getName());

		final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {
			protected boolean visit(final Method method) {
				TestCase.assertTrue(method.getEnclosingType().equals(test));

				final Visibility visibility = method.getVisibility();
				TestCase.assertFalse("private methods should be skipped", visibility == Visibility.PRIVATE);

				while (true) {
					final String name = method.getName();
					if (name.equals("subClassPackagePrivateMethod")) {
						break;
					}
					TestCase.fail("Unknown method " + method);
				}

				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};
		visitor.start(test);
	}

	static class SubClassOfSubPackageTest extends SuperPackageTest {
		void subClassPackagePrivateMethod() {
		}
	}

	public void testSkipsOverriddenMethods() {
		final GeneratorContext context = this.createGeneratorContext();
		final Type test = context.getType(SubClass.class.getName());

		final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {
			protected boolean visit(final Method method) {
				TestCase.assertTrue(method.getEnclosingType().equals(test));
				TestCase.assertEquals("virtualMethod", method.getName());
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};
		visitor.start(test);
	}

	static class SubClass extends SuperClass {
		public void virtualMethod() {
		}
	}

	static class SuperClass {
		public void virtualMethod() {

		}
	}

	GeneratorContext createGeneratorContext() {
		return new JavaGeneratorContext();
	}
}
