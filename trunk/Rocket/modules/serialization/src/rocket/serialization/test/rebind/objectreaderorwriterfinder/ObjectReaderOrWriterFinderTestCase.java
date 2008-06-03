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
package rocket.serialization.test.rebind.objectreaderorwriterfinder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import rocket.generator.rebind.java.JavaClassTypeAdapter;
import rocket.generator.rebind.java.JavaGeneratorContext;
import rocket.generator.rebind.type.Type;
import rocket.serialization.rebind.ObjectReaderOrWriterFinder;
import rocket.serialization.rebind.SerializationConstants;
import rocket.serialization.rebind.SerializationFactoryGeneratorException;

public class ObjectReaderOrWriterFinderTestCase extends TestCase {

	public void testFindObjectReaderWithConcreteTypeWithType() {
		final TestGeneratorContext context = new TestGeneratorContext() {
			boolean includeSecondConcreteInterfaceInTypeHeirarchy() {
				return false;
			}

			String getObjectReaderWriterSerializableTypeName() {
				return CONCRETE_SUB_CLASS_1;
			}

			boolean includeObjectReaderWriter2InTypeHeirarchy() {
				return false;
			}

			String getObjectReaderWriter2SerializableTypeName() {
				throw new UnsupportedOperationException();
			}
		};

		final ObjectReaderOrWriterFinder finder = this.createFinder(context.getObjectReaderWriter());
		final Set serializableTypes = context.getSerializableTypes();
		final Map readersWriters = finder.build(serializableTypes);
		assertNotNull(readersWriters);

		assertEquals("" + readersWriters, 1, readersWriters.size());

		final Type concreteSubClass1 = context.getConcreteSubClass1();
		final Type objectReaderWriter1 = context.getObjectReaderWriter1();
		assertEquals("" + readersWriters, objectReaderWriter1, readersWriters.get(concreteSubClass1));
	}

	public void testFindObjectReaderMatchingInterfaceWhichMatchesOneType() {
		final TestGeneratorContext context = new TestGeneratorContext() {
			boolean includeSecondConcreteInterfaceInTypeHeirarchy() {
				return false;
			}

			String getObjectReaderWriterSerializableTypeName() {
				return INTERFACE;
			}

			boolean includeObjectReaderWriter2InTypeHeirarchy() {
				return false;
			}

			String getObjectReaderWriter2SerializableTypeName() {
				throw new UnsupportedOperationException();
			}
		};

		final ObjectReaderOrWriterFinder finder = this.createFinder(context.getObjectReaderWriter());
		final Set serializableTypes = context.getSerializableTypes();
		final Map readersWriters = finder.build(serializableTypes);
		assertNotNull(readersWriters);

		assertEquals("" + readersWriters, 1, readersWriters.size());

		final Type concreteInterface = context.getConcreteInterface1();
		final Type objectReaderWriter1 = context.getObjectReaderWriter1();
		assertEquals("" + readersWriters, objectReaderWriter1, readersWriters.get(concreteInterface));
	}

	public void testFindObjectReaderMatchingInterfaceWhichMatchesSeveralTypes() {
		final TestGeneratorContext context = new TestGeneratorContext() {
			boolean includeSecondConcreteInterfaceInTypeHeirarchy() {
				return true;
			}

			String getObjectReaderWriterSerializableTypeName() {
				return INTERFACE;
			}

			boolean includeObjectReaderWriter2InTypeHeirarchy() {
				return false;
			}

			String getObjectReaderWriter2SerializableTypeName() {
				throw new UnsupportedOperationException();
			}
		};

		final ObjectReaderOrWriterFinder finder = this.createFinder(context.getObjectReaderWriter());
		final Set serializableTypes = context.getSerializableTypes();
		final Map readersWriters = finder.build(serializableTypes);
		assertNotNull(readersWriters);

		assertEquals("" + readersWriters, 2, readersWriters.size());

		final Type concreteInterface = context.getConcreteInterface1();
		final Type objectReaderWriter = context.getObjectReaderWriter1();
		assertEquals("" + readersWriters, objectReaderWriter, readersWriters.get(concreteInterface));

		final Type concreteInterface2 = context.getConcreteInterface2();
		assertEquals("" + readersWriters, objectReaderWriter, readersWriters.get(concreteInterface2));
	}

	public void testFindTwoObjectReadersEachHandlingSeparateConcreteTypes() {
		final TestGeneratorContext context = new TestGeneratorContext() {
			boolean includeSecondConcreteInterfaceInTypeHeirarchy() {
				return true;
			}

			String getObjectReaderWriterSerializableTypeName() {
				return CONCRETE_SUB_CLASS_1;
			}

			boolean includeObjectReaderWriter2InTypeHeirarchy() {
				return true;
			}

			String getObjectReaderWriter2SerializableTypeName() {
				return CONCRETE_SUB_CLASS_2;
			}
		};

		final ObjectReaderOrWriterFinder finder = this.createFinder(context.getObjectReaderWriter());
		final Set serializableTypes = context.getSerializableTypes();
		final Map readersWriters = finder.build(serializableTypes);
		assertNotNull(readersWriters);

		assertEquals("" + readersWriters, 2, readersWriters.size());

		final Type concreteSubClass1 = context.getConcreteSubClass1();
		final Type objectReaderWriter1 = context.getObjectReaderWriter1();
		assertEquals("" + readersWriters, objectReaderWriter1, readersWriters.get(concreteSubClass1));

		final Type concreteSubClass2 = context.getConcreteSubClass2();
		final Type objectReaderWriter2 = context.getObjectReaderWriter2();
		assertEquals("" + readersWriters, objectReaderWriter2, readersWriters.get(concreteSubClass2));
	}

	public void testFindTypeWithTwoDifferentObjectReaders() {
		final TestGeneratorContext context = new TestGeneratorContext() {
			boolean includeSecondConcreteInterfaceInTypeHeirarchy() {
				return true;
			}

			String getObjectReaderWriterSerializableTypeName() {
				return CONCRETE_CLASS;
			}

			boolean includeObjectReaderWriter2InTypeHeirarchy() {
				return true;
			}

			String getObjectReaderWriter2SerializableTypeName() {
				return CONCRETE_CLASS;
			}
		};

		final ObjectReaderOrWriterFinder finder = this.createFinder(context.getObjectReaderWriter());
		final Set serializableTypes = context.getSerializableTypes();

		try {
			final Map readersWriters = finder.build(serializableTypes);
			fail("An exception should have been thrown because a type had an ambiguous reference to more than one ObjectReaderWriter and not: "
					+ readersWriters);
		} catch (SerializationFactoryGeneratorException expected) {
		}
	}

	public void testFindTypeWithTwoPossibleObjectReaderWritersOneViaATypeAndTheOtherAnInterface() {
		final TestGeneratorContext context = new TestGeneratorContext() {
			boolean includeSecondConcreteInterfaceInTypeHeirarchy() {
				return true;
			}

			String getObjectReaderWriterSerializableTypeName() {
				return CONCRETE_INTERFACE_1;
			}

			boolean includeObjectReaderWriter2InTypeHeirarchy() {
				return true;
			}

			String getObjectReaderWriter2SerializableTypeName() {
				return INTERFACE;
			}
		};

		final ObjectReaderOrWriterFinder finder = this.createFinder(context.getObjectReaderWriter());
		final Set serializableTypes = context.getSerializableTypes();
		final Map readersWriters = finder.build(serializableTypes);
		assertNotNull(readersWriters);

		final Type concreteInterface1 = context.getConcreteInterface1();
		final Type objectReaderWriter1 = context.getObjectReaderWriter1();
		assertEquals("" + readersWriters, objectReaderWriter1, readersWriters.get(concreteInterface1));
	}

	ObjectReaderOrWriterFinder createFinder(final Type interfacee) {
		return new ObjectReaderOrWriterFinder() {

			protected Type getImplementingInterface() {
				return interfacee;
			}

			protected void throwAmbiguousMatches(final Type type, final Type readerOrWriter, final Type secondReaderOrWriter) {
				throw new SerializationFactoryGeneratorException("Ambiguous match to more than one ObjectReader/Writer for the type "
						+ type);
			}

			protected boolean shouldBeSerialized(final Type type) {
				return true;
			}
		};// //
	}

	static final String[] EMPTY_STRING_ARRAY = new String[0];

	static abstract class TestGeneratorContext extends JavaGeneratorContext {
		protected Type createClassType(final String name) {
			JavaClassTypeAdapter type = null;

			while (true) {
				if (OBJECT.equals(name)) {
					type = new TestJavaClassTypeAdapter() {
						String[] getSubTypeNames() {
							final List subTypes = new ArrayList();
							subTypes.add(SERIALIZABLE);
							subTypes.add(CONCRETE_CLASS);
							subTypes.add(INTERFACE);

							subTypes.add(CONCRETE_INTERFACE_1);
							if (TestGeneratorContext.this.includeSecondConcreteInterfaceInTypeHeirarchy()) {
								subTypes.add(CONCRETE_INTERFACE_2);
							}

							subTypes.add(OBJECT_READER_WRITER);
							subTypes.add(OBJECT_READER_WRITER_IMPL1);
							if (TestGeneratorContext.this.includeObjectReaderWriter2InTypeHeirarchy()) {
								subTypes.add(OBJECT_READER_WRITER_IMPL2);
							}
							return (String[]) subTypes.toArray(EMPTY_STRING_ARRAY);
						}
					};
					break;
				}
				if (SERIALIZABLE.equals(name)) {
					type = new TestJavaClassTypeAdapter() {
						String[] getSubTypeNames() {
							return EMPTY_STRING_ARRAY;
						}
					};
					break;
				}
				if (CONCRETE_CLASS.equals(name)) {
					type = new TestJavaClassTypeAdapter() {
						String[] getSubTypeNames() {
							return new String[] { CONCRETE_SUB_CLASS_1, CONCRETE_SUB_CLASS_2 };
						}
					};
					break;
				}
				if (CONCRETE_SUB_CLASS_1.equals(name)) {
					type = new TestJavaClassTypeAdapter() {
						String[] getSubTypeNames() {
							return EMPTY_STRING_ARRAY;
						}
					};
					break;
				}
				if (CONCRETE_SUB_CLASS_2.equals(name)) {
					type = new TestJavaClassTypeAdapter() {
						String[] getSubTypeNames() {
							return EMPTY_STRING_ARRAY;
						}
					};
					break;
				}
				if (INTERFACE.equals(name)) {
					type = new TestJavaClassTypeAdapter() {
						String[] getSubTypeNames() {
							return EMPTY_STRING_ARRAY;
						}
					};
					break;
				}
				if (CONCRETE_INTERFACE_1.equals(name)) {
					type = new TestJavaClassTypeAdapter() {
						String[] getSubTypeNames() {
							return EMPTY_STRING_ARRAY;
						}
					};
					break;
				}
				if (CONCRETE_INTERFACE_2.equals(name)) {
					type = new TestJavaClassTypeAdapter() {
						String[] getSubTypeNames() {
							return EMPTY_STRING_ARRAY;
						}
					};
					break;
				}
				if (OBJECT_READER_WRITER.equals(name)) {
					type = new TestJavaClassTypeAdapter() {
						String[] getSubTypeNames() {
							return EMPTY_STRING_ARRAY;
						}
					};
					break;
				}
				if (OBJECT_READER_WRITER_IMPL1.equals(name)) {
					type = new TestJavaClassTypeAdapter() {
						String[] getSubTypeNames() {
							return EMPTY_STRING_ARRAY;
						}

						String getSerializableTypeName() {
							return TestGeneratorContext.this.getObjectReaderWriterSerializableTypeName();
						}
					};
					break;
				}
				if (this.includeObjectReaderWriter2InTypeHeirarchy()) {
					if (OBJECT_READER_WRITER_IMPL2.equals(name)) {
						type = new TestJavaClassTypeAdapter() {
							String[] getSubTypeNames() {
								return EMPTY_STRING_ARRAY;
							}

							String getSerializableTypeName() {
								return TestGeneratorContext.this.getObjectReaderWriter2SerializableTypeName();
							}
						};
						break;
					}
				}
				throw new UnsupportedOperationException("Unknown type " + name);
			}

			type.setGeneratorContext(this);

			// set java class...
			try {
				final Class javaClass = Class.forName(name);
				type.setJavaClass(javaClass);

			} catch (final ExceptionInInitializerError caught) {
				throwTypeNotFoundException(name, caught);
			} catch (final ClassNotFoundException caught) {
				throwTypeNotFoundException(name, caught);
			} catch (final LinkageError caught) {
				throwTypeNotFoundException(name, caught);
			}

			return type;

		}

		Set getSerializableTypes() {
			final Set types = new HashSet();
			types.add(this.getType(CONCRETE_CLASS));
			types.add(this.getType(CONCRETE_SUB_CLASS_1));
			types.add(this.getType(CONCRETE_SUB_CLASS_2));
			types.add(this.getType(CONCRETE_INTERFACE_1));
			types.add(this.getType(CONCRETE_INTERFACE_2));
			return types;
		}

		abstract boolean includeSecondConcreteInterfaceInTypeHeirarchy();

		abstract String getObjectReaderWriterSerializableTypeName();

		abstract boolean includeObjectReaderWriter2InTypeHeirarchy();

		abstract String getObjectReaderWriter2SerializableTypeName();

		Type getConcrete() {
			return this.getType(CONCRETE_CLASS);
		}

		Type getConcreteSubClass1() {
			return this.getType(CONCRETE_SUB_CLASS_1);
		}

		Type getConcreteSubClass2() {
			return this.getType(CONCRETE_SUB_CLASS_2);
		}

		Type getConcreteInterface1() {
			return this.getType(CONCRETE_INTERFACE_1);
		}

		Type getConcreteInterface2() {
			return this.getType(CONCRETE_INTERFACE_2);
		}

		Type getInterface() {
			return this.getType(INTERFACE);
		}

		Type getObjectReaderWriter() {
			return this.getType(OBJECT_READER_WRITER);
		}

		Type getObjectReaderWriter1() {
			return this.getType(OBJECT_READER_WRITER_IMPL1);
		}

		Type getObjectReaderWriter2() {
			return this.getType(OBJECT_READER_WRITER_IMPL2);
		}
	};

	static String OBJECT = Object.class.getName();

	static abstract class TestJavaClassTypeAdapter extends JavaClassTypeAdapter {
		protected Set createSubTypes() {
			final Set subTypes = new HashSet();
			final String[] subTypeNames = this.getSubTypeNames();
			for (int i = 0; i < subTypeNames.length; i++) {
				final String subTypeName = subTypeNames[i];
				final Type type = this.getType(subTypeName);
				subTypes.add(type);
			}
			return subTypes;
		}

		abstract String[] getSubTypeNames();

		public List getMetadataValues(final String annotation) {
			List values = null;
			while (true) {
				if (SerializationConstants.SERIALIZABLE_TYPE.equals(annotation)) {
					values = Collections.nCopies(1, this.getSerializableTypeName());
					break;
				}
				throw new UnsupportedOperationException("Unknown annotation \"" + annotation + "\".");
			}
			return values;
		}

		String getSerializableTypeName() {
			throw new UnsupportedOperationException();
		}
	}

	static String CONCRETE_CLASS = ConcreteClass.class.getName();

	static class ConcreteClass implements Serializable {
	}

	static String CONCRETE_SUB_CLASS_1 = ConcreteSubClass.class.getName();

	static class ConcreteSubClass extends ConcreteClass {
	}

	static String CONCRETE_SUB_CLASS_2 = ConcreteSubClass2.class.getName();

	static class ConcreteSubClass2 extends ConcreteClass {
	}

	static String INTERFACE = Interface.class.getName();

	static interface Interface {
	}

	static String CONCRETE_INTERFACE_1 = ConcreteInterface1.class.getName();

	static class ConcreteInterface1 implements Interface, Serializable {
	}

	static String CONCRETE_INTERFACE_2 = ConcreteInterface2.class.getName();

	static class ConcreteInterface2 implements Interface, Serializable {
	}

	static String OBJECT_READER_WRITER = ObjectReaderWriter.class.getName();

	static interface ObjectReaderWriter {
	}

	static String OBJECT_READER_WRITER_IMPL1 = ObjectReaderWriter1.class.getName();

	static class ObjectReaderWriter1 implements ObjectReaderWriter {
	}

	static String OBJECT_READER_WRITER_IMPL2 = ObjectReaderWriter2.class.getName();

	static class ObjectReaderWriter2 implements ObjectReaderWriter {
	}

	static String SERIALIZABLE = Serializable.class.getName();
}
