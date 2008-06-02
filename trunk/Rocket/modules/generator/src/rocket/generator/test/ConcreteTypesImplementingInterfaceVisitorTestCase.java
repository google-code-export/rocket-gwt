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
import rocket.generator.rebind.visitor.ConcreteTypesImplementingInterfaceVisitor;

public class ConcreteTypesImplementingInterfaceVisitorTestCase extends TestCase {

	public void testFind() {
		final GeneratorContext context = this.createGeneratorContext();

		final TestConcreteTypesImplementingInterfaceFinder finder = new TestConcreteTypesImplementingInterfaceFinder();
		finder.start(context.getType(Interface.class.getName()));

		final Set types = finder.getTypes();
		assertNotNull("types", types);

		assertEquals(2, types.size());
		assertTrue("" + SubClassThatImplementsInterface.class, types.contains(context.getType(SubClassThatImplementsInterface.class
				.getName())));
		assertTrue("" + SubSubClassThatImplementsInterface.class, types.contains(context
				.getType(SubSubClassThatImplementsInterface.class.getName())));
	}

	class TestConcreteTypesImplementingInterfaceFinder extends ConcreteTypesImplementingInterfaceVisitor {
		protected boolean visit(final Type type) {
			this.types.add(type);
			return false;
		}

		protected boolean skipAbstractTypes() {
			return true;
		}

		Set types = new HashSet();

		Set getTypes() {
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
						if (false == name.equals("java.lang.Object")) {
							throw new RuntimeException(name);
						}
						adapter = new ObjectJavaClassTypeAdapter();
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

	static final String INTERFACE = Interface.class.getName();
	static final String SUB_INTERFACE = SubInterface.class.getName();
	final static String SUB_CLASS_THAT_IMPLEMENTS_INTERFACE = SubClassThatImplementsInterface.class.getName();
	final static String SUB_SUB_CLASS_THAT_IMPLEMENTS_INTERFACE = SubSubClassThatImplementsInterface.class.getName();
	final static String CONCRETE_CLASS = ConcreteClass.class.getName();
	final static String ANOTHER_CONCRETE_CLASS = AnotherConcreteClass.class.getName();

	static abstract class TestJavaClassTypeAdapter extends JavaClassTypeAdapter {
		public void setJavaClass(final Class javaClass) {
			super.setJavaClass(javaClass);
		}

		protected Set createSubTypes() {
			throw new UnsupportedOperationException(this.getName() + ".createSubTypes() , adapter: " + this.getClass());
		}
	}

	static class ObjectJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public void setJavaClass(final Class javaClass) {
			super.setJavaClass(javaClass);
		}

		protected Set createSubTypes() {
			final Set subTypes = new HashSet();
			subTypes.add(getType(INTERFACE));
			subTypes.add(getType(CONCRETE_CLASS));
			subTypes.add(getType(ANOTHER_CONCRETE_CLASS));
			return subTypes;
		}
	}

	static interface Interface {
	}

	static class InterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			return new HashSet(Collections.nCopies(1, getType(SUB_INTERFACE)));
		}
	}

	static interface SubInterface extends Interface {
	}

	static class SubInterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			return Collections.EMPTY_SET;
		}
	}

	static class ConcreteClass {
	}

	static class ConcreteClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			return new HashSet(Collections.nCopies(1, getType(SUB_CLASS_THAT_IMPLEMENTS_INTERFACE)));
		}
	}

	static class SubClass extends ConcreteClass {
	}

	static class SubClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			return Collections.EMPTY_SET;
		}
	}

	static class SubClassThatImplementsInterface extends ConcreteClass implements Interface {
	}

	static class SubClassThatImplementsInterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			return new HashSet(Collections.nCopies(1, getType(SUB_SUB_CLASS_THAT_IMPLEMENTS_INTERFACE)));
		}
	}

	static class SubSubClassThatImplementsInterface extends SubClassThatImplementsInterface {
	}

	static class SubSubClassThatImplementsInterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			return Collections.EMPTY_SET;
		}
	}

	static class AnotherConcreteClass {
	}

	static class AnotherConcreteClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			return Collections.EMPTY_SET;
		}
	}
}
