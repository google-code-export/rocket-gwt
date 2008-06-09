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
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.java.JavaClassTypeAdapter;
import rocket.generator.rebind.java.JavaGeneratorContext;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.SubTypesVisitor;

public class SubClassVisitorTestCase extends TestCase {

	public void testVisitTypeWithNoSubClasses() {
		final GeneratorContext context = this.createGeneratorContext();

		final TestSubTypesVisitor finder = new TestSubTypesVisitor();
		finder.start(context.getType(ClassWithSubClasses.class.getName()));

		final Set<Type> types = finder.getTypes();
		assertNotNull("types", types);

		assertEquals("" + types, 1, types.size());
		assertTrue("" + types, types.contains(context.getType(ClassWithSubClasses.class.getName())));
	}

	public void testVisitTypeWithSubClasses() {
		final GeneratorContext context = this.createGeneratorContext();

		final TestSubTypesVisitor finder = new TestSubTypesVisitor();
		finder.start(context.getType(ConcreteClass.class.getName()));

		final Set<Type> types = finder.getTypes();
		assertNotNull("types", types);

		assertEquals("" + types, 3, types.size());
		assertTrue("" + types, types.contains(context.getType(ConcreteClass.class.getName())));
		assertTrue("" + types, types.contains(context.getType(SubClass.class.getName())));
		assertTrue("" + types, types.contains(context.getType(SubSubClass.class.getName())));
	}

	public void testVisitInterfacesWithSubInterfaces() {
		final GeneratorContext context = this.createGeneratorContext();

		final TestSubTypesVisitor finder = new TestSubTypesVisitor();
		finder.start(context.getType(Interface.class.getName()));

		final Set<Type> types = finder.getTypes();
		assertNotNull("types", types);

		assertEquals("" + types, 2, types.size());
		assertTrue("" + types, types.contains(context.getType(Interface.class.getName())));
		assertTrue("" + types, types.contains(context.getType(SubInterface.class.getName())));
	}

	class TestSubTypesVisitor extends SubTypesVisitor {
		@Override
		protected boolean visit(final Type type) {
			this.types.add(type);
			return false;
		}

		@Override
		protected boolean skipInitialType() {
			return false;
		}

		Set<Type> types = new HashSet<Type>();

		Set<Type> getTypes() {
			return this.types;
		}
	}

	GeneratorContext createGeneratorContext() {
		return new JavaGeneratorContext() {

			protected Type createClassType(final String name) {
				TestJavaClassTypeAdapter adapter = null;

				try {
					final Class javaClass = Class.forName(name);
					Class adapterClass = null;
					try {
						adapterClass = Class.forName(name + "JavaClassTypeAdapter");
						adapter = (TestJavaClassTypeAdapter) adapterClass.newInstance();
					} catch (final Exception useDefault) {
						// adapter = new TestJavaClassTypeAdapter();
						throw new RuntimeException(name);
					}
					adapter.setGeneratorContext(this);
					adapter.setJavaClass(javaClass);

				} catch (final ExceptionInInitializerError caught) {
					throwTypeNotFoundException(name, caught);
				} catch (final ClassNotFoundException caught) {
					throwTypeNotFoundException(name, caught);
				} catch (final LinkageError caught) {
					throwTypeNotFoundException(name, caught);
				}
				return adapter;
			}
		};
	}

	static class TestJavaClassTypeAdapter extends JavaClassTypeAdapter {
		public void setJavaClass(final Class javaClass) {
			super.setJavaClass(javaClass);
		}

		@Override
		protected Set<Type> createSubTypes() {
			throw new UnsupportedOperationException(this.getName() + ".createSubTypes() , adapter: " + this.getClass());
		}
	}

	static interface Interface {
	}

	static class InterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		@Override
		public Set<Type> getSubTypes() {
			return new HashSet<Type>(Collections.<Type>nCopies(1, this.getType(SubInterface.class.getName())));
		}
	}

	static interface SubInterface extends Interface {
	}

	static class SubInterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		@Override
		public Set<Type> createSubTypes() {
			return Collections.<Type>emptySet();
		}
	}

	static class ConcreteClass {
	}

	static class ConcreteClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		@Override
		public Set<Type> getSubTypes() {
			return new HashSet<Type>(Collections.<Type>nCopies(1, this.getType(SubClass.class.getName())));
		}
	}

	static class SubClass extends ConcreteClass {
	}

	static class SubClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		@Override
		public Set<Type> getSubTypes() {
			return new HashSet<Type>(Collections.<Type>nCopies(1, this.getType(SubSubClass.class.getName())));
		}
	}

	static class SubSubClass extends ConcreteClass {
	}

	static class SubSubClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		@Override
		public Set<Type> createSubTypes() {
			return Collections.<Type>emptySet();
		}
	}

	static class ClassWithSubClasses {
	}

	static class ClassWithSubClassesJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		@Override
		public Set<Type> createSubTypes() {
			return Collections.<Type>emptySet();
		}
	}

}
