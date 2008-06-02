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
package rocket.serialization.rebind;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.EmptyCodeBlock;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.constructor.NewConstructor;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.util.FieldComparator;
import rocket.generator.rebind.util.TypeComparator;
import rocket.generator.rebind.visitor.ReachableTypesVisitor;
import rocket.generator.rebind.visitor.SubTypesVisitor;
import rocket.serialization.client.SerializationException;
import rocket.serialization.client.reader.ObjectReaderImpl;
import rocket.serialization.rebind.newarrayinstance.NewArrayInstanceTemplatedFile;
import rocket.serialization.rebind.newinstance.NewInstanceTemplatedFile;
import rocket.serialization.rebind.read.ReadFieldsTemplatedFile;
import rocket.serialization.rebind.read.ReadTemplatedFile;
import rocket.serialization.rebind.read.SetFieldTemplatedFile;
import rocket.serialization.rebind.switchstatement.SwitchTemplatedFile;
import rocket.serialization.rebind.typematcher.TypeMatcher;
import rocket.serialization.rebind.typematcher.TypeMatcherFactory;
import rocket.serialization.rebind.write.GetFieldTemplatedFile;
import rocket.serialization.rebind.write.WriteFieldsTemplatedFile;
import rocket.serialization.rebind.write.WriteTemplatedFile;
import rocket.serialization.rebind.writearray.WriteArrayTemplatedFile;
import rocket.util.client.Checker;
import rocket.util.client.Tester;
import rocket.util.server.InputOutput;

/**
 * This generator creates not only the SerializationFactory but also creates on
 * demand all the ObjectReader/Writers for each serializable type if they dont
 * already exist.
 * 
 * @author Miroslav Pokorny
 */
public class SerializationFactoryGenerator extends Generator {

	protected String getGeneratedTypeNameSuffix() {
		return SerializationConstants.SERIALIZATION_FACTORY_GENERATED_TYPE_SUFFIX;
	}

	@Override
	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		this.setBlackList(this.loadBlackLists());

		// find all preexisting readers/writers
		final Set<Type> allSerializableTypes = this.getAllSerializableTypes();
		final Map<Type, Type> typesToReaders = this.findAllReaders(allSerializableTypes);
		final Map<Type, Type> typesToWriters = this.findAllWriters(allSerializableTypes);

		// get the root types to be serialized. all types referenced from the roots will be present in the built set.
		final Set<Type> readableTypes = this.getReadableTypesListFromAnnotation(type);
		final Set<Type> writeableTypes = this.getWritableTypesListFromAnnotation(type);

		// find all reachable readable/writable types
		final Set<Type> serializableReadableTypes = this.findSerializableTypes(readableTypes, typesToReaders);
		final Set<Type> serializableWritableTypes = this.findSerializableTypes(writeableTypes, typesToWriters);

		// after this can ignore typesToReaders and typesToWriters

		// generate readers and then writers
		this.createObjectReaders(serializableReadableTypes, typesToReaders);
		this.createObjectWriters(serializableWritableTypes, typesToWriters);

		// nows the time to create the SerializationFactory
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		final NewConcreteType serializationFactory = this.createSerializableFactory(newTypeName);

		this.overrideSerializationFactoryGetObjectReader(serializationFactory, typesToReaders);
		this.overrideSerializationFactoryGetObjectWriter(serializationFactory, typesToWriters);
		context.unbranch();
		
		return serializationFactory;

	}

	/**
	 * Checks and reports if a type matches any blacklist.
	 * 
	 * @param type
	 * @return Returns true if the type or any of its super types are
	 *         blacklisted from serialization.
	 */
	protected boolean isBlackListed(final Type type) {
		Checker.notNull("parameter:type", type);

		boolean blacklisted = false;

		// check black list...
		final Iterator<TypeMatcher> blackListers = this.getBlackList().iterator();
		final Type object = this.getGeneratorContext().getObject();

		while (blackListers.hasNext()) {
			final TypeMatcher matcher = blackListers.next();

			// scan entire type heirarchy just in case type or any super is
			// blacklisted.
			Type current = type;
			while (true) {
				if (matcher.matches(current)) {
					blacklisted = true;
					break;
				}

				current = current.getSuperType();
				if (null == current) {
					break;
				}
				// stop when we hit Object.
				if (object.equals(current)) {
					break;
				}
			}

			// blacklisted stop searching
			if (blacklisted) {
				break;
			}
		}

		return blacklisted;
	}

	/**
	 * Scans each and every package for a blacklist file and loads the embedded
	 * expressions.
	 * 
	 * @return A set containing all blacklisted packages.
	 */
	protected Set<TypeMatcher> loadBlackLists() {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Attempting to load and merge all blacklists (unsorted).");

		final Set<TypeMatcher> blackLists = new HashSet<TypeMatcher>();

		final SubTypesVisitor packageVisitor = new SubTypesVisitor() {

			@Override
			protected boolean visit(final Type type) {
				final Package packagee = type.getPackage();
				if (false == this.packages.contains(packagee)) {
					this.packages.add(packagee);

					final Set<TypeMatcher> loaded = SerializationFactoryGenerator.this.loadBlackListFromPackage(packagee);
					blackLists.addAll(loaded);
				}
				return false;
			}

			@Override
			protected boolean skipInitialType() {
				return false;
			}

			Set<Package> packages = new HashSet<Package>();
		};
		packageVisitor.start(context.getObject());

		context.unbranch();

		return blackLists;
	}

	/**
	 * Attempts to load the black list file from the given package.
	 * 
	 * @param packagee
	 * @return A set of types or an empty set if the black list was not found.
	 */
	protected Set<TypeMatcher> loadBlackListFromPackage(final Package packagee) {
		Checker.notNull("parameter:package", packagee);

		final GeneratorContext context = this.getGeneratorContext();
		context.delayedBranch();
		context.debug(packagee.getName());

		final Set<TypeMatcher> expressions = new HashSet<TypeMatcher>();

		final String fileName = this.getResourceName(packagee, SerializationConstants.BLACKLIST_FILENAME);
		InputStream inputStream = null;

		while (true) {
			try {
				inputStream = this.getClass().getResourceAsStream(fileName);
				if (null == inputStream) {
					break;
				}
				// context.debug(fileName);

				// use a BufferedReader to read a line at a time skipping
				// comments and empty lines.
				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				while (true) {
					final String line = reader.readLine();
					// eof reached...
					if (null == line) {
						break;
					}

					// skip empty / comment lines...
					final String typeName = line.trim();
					if (Tester.isNullOrEmpty(typeName) || typeName.startsWith("#")) {
						continue;
					}
					context.debug(line);

					final TypeMatcher typeNameMatcher = TypeMatcherFactory.createTypeNameMatcher(typeName);
					expressions.add(typeNameMatcher);
				}

				break;

			} catch (final RuntimeException caught) {
				throw caught;
			} catch (final Exception caught) {
				throw new SerializationFactoryGeneratorException("Failed to load black list, message: " + caught.getMessage(), caught);
			} finally {
				InputOutput.closeIfNecessary(inputStream);
			}
		}

		context.unbranch();
		return expressions;
	}

	/**
	 * A set of back list expressions. One may iterate thru this set and ask
	 * each whether it matches a given type, if it does that type is
	 * blacklisted.
	 */
	private Set<TypeMatcher> blackList;

	protected Set<TypeMatcher> getBlackList() {
		Checker.notNull("field:blackList", blackList);
		return this.blackList;
	}

	protected void setBlackList(final Set<TypeMatcher> blackList) {
		Checker.notNull("parameter:blackList", blackList);
		this.blackList = blackList;
	}

	/**
	 * Returns all types that are candidates for serialization less those that
	 * have been blacklisted.
	 * 
	 * @return
	 */
	protected Set<Type> getAllSerializableTypes() {
		final Set<Type> allTypes = new HashSet<Type>();

		final GeneratorContext context = this.getGeneratorContext();
		final Iterator<Type> types = context.getObject().getSubTypes().iterator();
		while (types.hasNext()) {
			final Type type = types.next();
			if (this.isBlackListed(type)) {
				continue;
			}

			allTypes.add(type);
		}
		
		// TODO hack need to add proper support for arrays
		allTypes.add( context.getType( "boolean[]"));
		allTypes.add( context.getType( "byte[]"));
		allTypes.add( context.getType( "short[]"));
		allTypes.add( context.getType( "int[]"));
		allTypes.add( context.getType( "long[]"));
		allTypes.add( context.getType( "float[]"));
		allTypes.add( context.getType( "double[]"));
		allTypes.add( context.getType( "char[]"));
		return allTypes;
	}

	/**
	 * Builds a map that contains for each of the input types its corresponding
	 * ObjectReader.
	 * 
	 * @param types
	 * @return
	 */
	protected Map<Type, Type> findAllReaders(final Set<Type> types) {
		Checker.notNull("parameter:types", types);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Finding existing ObjectReaders...");

		final ObjectReaderFinder finder = new ObjectReaderFinder() {
			@Override
			protected Type getImplementingInterface() {
				return SerializationFactoryGenerator.this.getObjectReader();
			}

			@Override
			protected boolean shouldBeSerialized(final Type type) {
				return SerializationFactoryGenerator.this.isOrHasSerializableSubType(type);
			}
		};

		final Map<Type, Type> typesToReaders = finder.build(types);

		this.logMapping(typesToReaders);

		context.unbranch();

		return typesToReaders;
	}

	protected Map<Type, Type> findAllWriters(final Set<Type> types) {
		Checker.notNull("parameter:types", types);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Finding existing ObjectWriters...");

		final ObjectWriterFinder finder = new ObjectWriterFinder() {
			@Override
			protected Type getImplementingInterface() {
				return SerializationFactoryGenerator.this.getObjectWriter();
			}

			@Override
			protected boolean shouldBeSerialized(final Type type) {
				return SerializationFactoryGenerator.this.isOrHasSerializableSubType(type);
			}
		};

		final Map<Type, Type> typesToWriters = finder.build(types);

		this.logMapping(typesToWriters);

		context.unbranch();

		return typesToWriters;
	}

	protected void logMapping(final Map<Type, Type> types) {
		Checker.notNull("parameter:types", types);

		final GeneratorContext context = this.getGeneratorContext();

		if (context.isDebugEnabled()) {
			final TreeMap<Type, Type> sorted = new TreeMap<Type, Type>(TypeComparator.INSTANCE);
			sorted.putAll(types);

			final Iterator<Map.Entry<Type,Type>> iterator = sorted.entrySet().iterator();
			while (iterator.hasNext()) {
				final Map.Entry<Type,Type> entry = iterator.next();

				final Type type = entry.getKey();
				final Type type1 = entry.getValue();

				String message = type.getName();
				if (null != type1) {
					message = message + " = " + type1.getName();
				}
				context.debug(message);
			}
		}
	}

	protected Set<Type> getReadableTypesListFromAnnotation(final Type type) {
		return this.getTypesFromAnnotation("Listing readable annotated types", type, SerializationConstants.SERIALIZABLE_READABLE_TYPES);
	}

	protected Set<Type> getWritableTypesListFromAnnotation(final Type type) {
		return this.getTypesFromAnnotation("Listing writable annotated types", type, SerializationConstants.SERIALIZABLE_WRITABLE_TYPES);
	}

	protected Set<Type> getTypesFromAnnotation(final String title, final Type type, final String annotation) {
		Checker.notNull("parameter:type", type);
		Checker.notEmpty("parameter:annotation", annotation);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug(title);

		final TreeSet<Type> types = new TreeSet<Type>( TypeComparator.INSTANCE );

		final Iterator<String> typeNames = type.getMetadataValues(annotation).iterator();
		while (typeNames.hasNext()) {
			final String typeName = typeNames.next();
			final Type listedType = context.findType(typeName);
			if (null == listedType) {
				this.throwUnableToFindAnnotatedType(typeName, type);
			}

			types.add(listedType);
			context.debug(listedType.getName());
		}

		context.unbranch();
		return types;
	}

	protected void throwUnableToFindAnnotatedType(final String annotatedTypeName, final Type parentType) {
		throw new SerializationFactoryGeneratorException("Unable to find annotated type \"" + annotatedTypeName
				+ "\" which was listed on \"" + parentType.getName() + "\".");
	}

	/**
	 * Builds a map that includes all reachable serializable types from the
	 * incoming set. For each type if an existing ObjectReader or ObjectWriter
	 * exists its type will be the value of the map entry.
	 * 
	 * @param types
	 * @param typesToReadersOrWriters
	 * @return
	 */

	protected Set<Type> findSerializableTypes(final Set<Type> types, final Map<Type, Type> typesToReadersOrWriters) {
		final Set<Type> serializableTypes = new HashSet<Type>();

		final Iterator<Type> i = types.iterator();
		while (i.hasNext()) {
			final Type type = i.next();
			final Set<Type> newSerializableTypes = this.findSerializableTypes(type, typesToReadersOrWriters);
			serializableTypes.addAll(newSerializableTypes);
		}

		return serializableTypes;
	}

	/**
	 * This method attempts to find all serializable types including special
	 * cases for list,set and map that need to be serialized. An exception will
	 * be thrown if any unserializable type is encountered.
	 * 
	 * @param type
	 * @param typesToReadersOrWriters
	 * @return A set of serializable types
	 */
	protected Set<Type> findSerializableTypes(final Type type, final Map<Type, Type> typesToReadersOrWriters) {
		Checker.notNull("parameter:type", type);
		Checker.notNull("typesToReadersOrWriters", typesToReadersOrWriters);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Finding all serializable types reachable from " + type);

		final Type map = this.getMap();
		final Type list = this.getList();
		final Type set = this.getSet();

		final ReachableTypesVisitor visitor = new ReachableTypesVisitor() {

			@Override
			protected boolean skipTypeThatImplementsInterface(final Type type, final Type interfacee) {
				return false == SerializationFactoryGenerator.this.isOrHasSerializableSubType(type);
			}

			@Override
			protected boolean skipArray(final Type array) {
				Checker.notNull("parameter:array", array);

				return false;
			}

			@Override
			protected boolean skipType(final Type type) {
				boolean skip = false;

				while (true) {
					// we dont want to visit the fields of $type we already have a reader/writer.
					if (typesToReadersOrWriters.containsKey(type)) {
						skip = true;
						break;
					}
					if (SerializationFactoryGenerator.this.isOrHasSerializableSubType(type)) {
						skip = false;
						break;
					}
					skip = false;
					break;
				}

				return skip;
			}

			@Override
			protected void visitType(final Type type) {
				if (false == SerializationFactoryGenerator.this.isOrHasSerializableSubType(type)) {
					SerializationFactoryGenerator.this.throwEncounteredUnserializableType(type);
				}
				context.branch();
				context.debug(type.getName());
				super.visitType(type);
				context.unbranch();
			}

			@Override
			protected boolean skipSuperType(final Type superType) {
				return !SerializationFactoryGenerator.this.isOrHasSerializableSubType(superType);
			}

			@Override
			protected void visitSuperType(final Type superType) {
				context.branch();
				context.debug(superType.getName());
				super.visitSuperType(superType);
				context.unbranch();
			}

			@Override
			protected boolean skipSubType(final Type subType) {
				return !SerializationFactoryGenerator.this.isOrHasSerializableSubType(subType);
			}

			@Override
			protected void visitSubType(final Type subType) {
				context.branch();
				context.debug(subType.getName());
				super.visitSubType(subType);
				context.unbranch();
			}

			@Override
			protected void visitFields(final Type type) {
				if (false == typesToReadersOrWriters.containsKey(type)) {
					super.visitFields(type);
				}
			}

			/**
			 * Skip transient or static fields.
			 */
			@Override
			protected boolean skipField(final Field field) {
				Checker.notNull("parameter:field", field);

				return field.isStatic() || field.isTransient();
			}

			/**
			 * This method includes special tests to ensure that list/set/map
			 * element types are visited.
			 * 
			 * @param field
			 */
			@Override
			protected void visitField(final Field field) {
				Checker.notNull("parameter:field", field);

				context.branch();
				context.debug("Field: " + field.getName());

				while (true) {
					final Type fieldType = field.getType();

					if (field.isFinal()) {
						SerializationFactoryGenerator.this.throwFinalFieldEncountered(field);
					}

					if (list.equals(fieldType)) {
						this.processInterface(fieldType);

						final Type elementType = SerializationFactoryGenerator.this.getTypeFromAnnotation(field);
						context.debug(elementType + " (List)");
						this.visitType(elementType);
						break;
					}
					if (set.equals(fieldType)) {
						this.processInterface(fieldType);

						final Type elementType = SerializationFactoryGenerator.this.getTypeFromAnnotation(field);
						context.debug(elementType + " (Set)");
						this.visitType(elementType);
						break;
					}
					if (map.equals(fieldType)) {
						this.processInterface(fieldType);

						final Type keyType = SerializationFactoryGenerator.this.getTypeFromAnnotation(field, 0);
						final Type valueType = SerializationFactoryGenerator.this.getTypeFromAnnotation(field, 1);
						context.debug(keyType + " (Map key)");
						context.debug(valueType + " (Map value)");

						this.visitType(keyType);
						this.visitType(valueType);
						break;
					}

					this.visitType(fieldType);
					break;
				}

				context.unbranch();
			}
		};
		visitor.start(type);

		final Set<Type> found = visitor.getConcreteTypes();

		found.remove(context.getString());
		found.remove(context.getObject());
		context.debug("Removing special cases types \"java.lang.Object\" and \"java.lang.String\".");

		context.unbranch();
		return found;
	}

	protected void throwEncounteredUnserializableType(final Type type) {
		throw new SerializationFactoryGeneratorException("Encountered type that cannot be serialized, type: " + type);
	}

	/**
	 * Tests if the given type is serializable or has a serializable sub type.
	 * 
	 * @param type
	 * @return
	 */
	protected boolean isOrHasSerializableSubType(final Type type) {
		Checker.notNull("parameter:type", type);

		boolean serialize = false;

		while (true) {
			if (this.isBlackListed(type)) {
				serialize = false;
				break;
			}

			if (this.isSerializable(type)) {
				serialize = true;
				break;
			}

			// is not blacklisted search sub types for one serializable type.
			final SerializableSubTypeFinder serializableSubTypeFinder = new SerializableSubTypeFinder();
			serializableSubTypeFinder.start(type);

			serialize = serializableSubTypeFinder.hasSerializableSubType();
			break;
		}

		return serialize;
	}

	/**
	 * Tests if the given type is serializable.
	 * 
	 * @param type
	 * @return
	 */
	protected boolean isSerializable(final Type type) {
		boolean serialize = false;

		while (true) {
			if (type.isPrimitive()) {
				serialize = true;
				break;
			}

			// special test for arrays, all array are considered to be
			// serializable.
			if (type.isArray()) {
				serialize = true;
				break;
			}

			// is not blacklisted and is serializable...
			if (this.implementsSerializable(type)) {
				serialize = true;
			}
			break;
		}

		return serialize;
	}

	/**
	 * This class will continue visit all sub types of the given type until it
	 * runs out of sub types or a serializable type is found. If a serializable
	 * sub type is found thats also not blacklisted then the
	 * {@link #hasSerializableSubType()} will return true.
	 */
	class SerializableSubTypeFinder extends SubTypesVisitor {

		@Override
		protected boolean visit(final Type type) {
			boolean skipRemainder = false;
			// only visit if sub type is not blacklisted and is also
			// serializable.
			if (false == SerializationFactoryGenerator.this.isBlackListed(type)
					&& SerializationFactoryGenerator.this.implementsSerializable(type)) {
				this.setSerializableSubType(true);
				skipRemainder = true;
			}
			return skipRemainder;
		}

		@Override
		protected boolean skipInitialType() {
			return true;
		}

		private boolean serializableSubType = false;

		public boolean hasSerializableSubType() {
			return this.serializableSubType;
		}

		void setSerializableSubType(final boolean serializableSubType) {
			this.serializableSubType = serializableSubType;
		}
	}

	protected boolean implementsSerializable(final Type type) {
		Checker.notNull("parameter:type", type);
		return type.isAssignableTo(this.getSerializable());
	}

	protected Type getSerializable() {
		return this.getGeneratorContext().getType(SerializationConstants.SERIALIZABLE);
	}

	protected void throwTypeIsNotSerializable(final Type type) {
		throw new SerializationFactoryGeneratorException("The type \"" + type + "\" is not serializable (doesnt implement "
				+ SerializationConstants.SERIALIZABLE + ")");
	}

	protected void throwFinalFieldEncountered(final Field field) {
		throw new SerializationFactoryGeneratorException("Fields that are final cannot be deserialized, " + field);
	}

	protected Type getList() {
		return this.getGeneratorContext().getType(SerializationConstants.LIST);
	}

	protected Type getSet() {
		return this.getGeneratorContext().getType(SerializationConstants.SET);
	}

	protected Type getMap() {
		return this.getGeneratorContext().getType(SerializationConstants.MAP);
	}

	// TODO Need to read any attached annotations to find out the exact types
	protected Type getTypeFromAnnotation(final Field field) {
		return this.getTypeFromAnnotation(field, 0);
	}

	protected Type getTypeFromAnnotation(final Field field, final int index) {
		Checker.notNull("parameter:field", field);

		final List<String> values = field.getMetadataValues(SerializationConstants.CONTAINER_TYPE);
		if (values.size() < index) {
			throw new SerializationException("Unable to locate \"" + SerializationConstants.CONTAINER_TYPE + "\" on field " + field);
		}
		final String typeName = (String) values.get(index);
		final Type type = this.getGeneratorContext().findType(typeName);
		if (null == type) {
			throw new SerializationException("Unable to find type \"" + typeName + "\" which was taken from the annotation \""
					+ SerializationConstants.CONTAINER_TYPE + "\" from field: " + field);
		}
		return type;
	}

	protected Type getObjectReader() {
		return this.getGeneratorContext().getType(SerializationConstants.OBJECT_READER);
	}

	protected Type getObjectWriter() {
		return this.getGeneratorContext().getType(SerializationConstants.OBJECT_WRITER);
	}

	
	
	/**
	 * Loops thru the map of serializable types and creates an ObjectReader for
	 * each every type that requires one.
	 * 
	 * @param serializables
	 *            A set of types needing to be serialized
	 * @param typesToReaders
	 *            A map containing all types and their corresponding existing
	 *            readers
	 */
	protected void createObjectReaders(final Set<Type> serializables, final Map<Type, Type> typesToReaders) {
		Checker.notNull("parameter:serializables", serializables);
		Checker.notNull("parameter:typesToReaders", typesToReaders);

		final Set<Type> orderedSerializables = this.sortSerializablesIntoHeirarchyOrder(serializables);
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Creating necessary ObjectReaders.");

		final Iterator<Type> types = orderedSerializables.iterator();
		int skippedGeneratingCount = 0;
		final Set<Type> newReaders = new TreeSet<Type>( TypeComparator.INSTANCE );
		
		while (types.hasNext()) {
			final Type type = (Type) types.next();
			final Object reader = typesToReaders.get( type );
			if (null != reader) {
				skippedGeneratingCount++;
				continue;
			}

			context.branch();
			final NewConcreteType newReader = this.createObjectReader(type, typesToReaders );
			typesToReaders.put(type, newReader);
	
			context.branch();
			this.overrideObjectReaderNewInstanceMethod(type, newReader );
			
			if( false == type.isArray() ){
				this.overrideObjectReaderReadFieldsMethod(newReader, type);
				this.overrideObjectReaderReadMethod(newReader, type);
			}
			this.addSingletonField( newReader );
			context.unbranch();
			context.unbranch();
			
			newReaders.add( newReader );
		}
		
		this.writeTypes( newReaders );
		context.unbranch();
	}

	/**
	 * Factory method that creates a new NewConcreteType that will become the
	 * ObjectReader for the given type.
	 * 
	 * @param type
	 * @param newtypesToReaders
	 * @return
	 */
	protected NewConcreteType createObjectReader(final Type type, final Map<Type, Type> typesToReaders ) {
		final GeneratorContext context = this.getGeneratorContext();
		context.debug(type.getName());

		final String newTypeName = this.getGeneratedTypeName(type, SerializationConstants.OBJECT_READER_GENERATED_TYPE_SUFFIX, "rocket.serialization.client.reader");
		final NewConcreteType newConcreteType = context.newConcreteType(newTypeName);
		newConcreteType.setAbstract(false);
		newConcreteType.setFinal(false);

		context.debug(newTypeName);

		// pick the right super type.
		Type objectReaderSuperType = null;

		while (true) {
			// if its an array simply extend ObjectArrayReader
			if (type.isArray()) {
				objectReaderSuperType = this.getArrayReader();
				break;
			}

			// if super type is object extend ObjectReaderImpl
			Type superType = type.getSuperType();
			if (superType.equals(context.getObject())) {
				objectReaderSuperType = this.getObjectReaderImpl();
				break;
			}

			// find the super types object reader and extend that...
			objectReaderSuperType = (Type) typesToReaders.get(superType);

			if (null == objectReaderSuperType) {
				throw new IllegalStateException("Unable to fetch the reader for the super type \"" + superType + "\" whilst processing \"" + type.getName() + "\".");
			}
			break;
		}

		newConcreteType.setSuperType(objectReaderSuperType);
		context.debug("extends " + objectReaderSuperType);

		// add an annotation that marks the type being handled by this
		// ObjectReader
		newConcreteType.addMetaData(SerializationConstants.SERIALIZABLE_TYPE, type.getName());

		// create a public no arguments constructor.
		final NewConstructor constructor = newConcreteType.newConstructor();
		constructor.setBody(EmptyCodeBlock.INSTANCE);
		constructor.setVisibility(Visibility.PUBLIC);

		return newConcreteType;
	}


	/**
	 * Overrides the abstract {@link ObjectReaderImpl#newInstance} method to
	 * create a new instance of the type being deserialized using its no
	 * arguments constructor.
	 */
	protected void overrideObjectReaderNewInstanceMethod(final Type type, final NewConcreteType reader) {
		while (true) {
			if (type.isArray()) {
				this.overrideObjectReaderNewInstanceMethodForArrayType(type, reader);
				break;
			}
			if (false == type.hasNoArgumentsConstructor()) {
				break;
			}

			if (type.isAbstract()) {
				break;
			}
			this.overrideObjectReaderNewInstanceMethodForNonArrayType(type, reader);
			break;
		}
	}

	protected void overrideObjectReaderNewInstanceMethodForArrayType(final Type arrayType, final NewConcreteType reader) {
		Checker.notNull("parameter:type", arrayType);
		Checker.notNull("parameter:reader", reader);

		final GeneratorContext context = this.getGeneratorContext();

		final List<Type> parameters = new ArrayList<Type>();
		parameters.add(context.getString());
		parameters.add(this.getObjectInputStream());

		final Method method = reader.getMostDerivedMethod(SerializationConstants.CLIENT_OBJECT_READER_IMPL_NEW_INSTANCE_METHOD, parameters);
		final NewMethod newMethod = method.copy(reader);
		newMethod.setAbstract(false);

		final NewArrayInstanceTemplatedFile populateNewArray = new NewArrayInstanceTemplatedFile();
		populateNewArray.setType(arrayType.getComponentType());
		newMethod.setBody(populateNewArray);

		context.debug("Overridden " + newMethod);
	}

	protected void overrideObjectReaderNewInstanceMethodForNonArrayType(final Type type, final NewConcreteType reader) {
		Checker.notNull("parameter:type", type);
		Checker.notNull("parameter:reader", reader);

		final GeneratorContext context = this.getGeneratorContext();
		final List<Type> parameters = new ArrayList<Type>();
		parameters.add(context.getString());
		parameters.add(this.getObjectInputStream());

		final Method method = reader.getMostDerivedMethod(SerializationConstants.CLIENT_OBJECT_READER_IMPL_NEW_INSTANCE_METHOD,parameters);
		final NewMethod newMethod = method.copy(reader);
		newMethod.setAbstract(false);

		final Constructor constructor = type.getConstructor(Collections.EMPTY_LIST);
		final NewInstanceTemplatedFile newInstanceStatement = new NewInstanceTemplatedFile();
		newInstanceStatement.setConstructor(constructor);

		final CodeBlock returnStatement = new CodeBlock() {
			public boolean isEmpty() {
				return false;
			}

			public void write(final SourceWriter writer) {
				writer.print("return ");
				newInstanceStatement.write(writer);
			}
		};

		newMethod.setBody(returnStatement);

		context.debug("Overridden " + newMethod);
	}
	
	protected void overrideObjectReaderReadMethod(final NewConcreteType reader, final Type type) {
		Checker.notNull("parameter:reader", reader);
		Checker.notNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();

		// locate the writeFields method that will be overridden.
		final List<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(context.getObject());
		final Type objectInputStreamType = this.getObjectInputStream();
		parameterTypes.add(objectInputStreamType);

		final Method method = reader.getMostDerivedMethod(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_METHOD, parameterTypes);
		final NewMethod newMethod = method.copy(reader);
		newMethod.setNative(false);

		// rename parameters to the same names used in templates...
		final List<MethodParameter> newMethodParameters = (List<MethodParameter>)newMethod.getParameters();
		final NewMethodParameter object = (NewMethodParameter)newMethodParameters.get(0);
		object.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_INSTANCE_PARAMETER);
		object.setFinal(true);

		final NewMethodParameter objectInputStreamParameter = (NewMethodParameter) newMethodParameters.get(1);
		objectInputStreamParameter.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_OBJECT_INPUT_STREAM_PARAMETER);
		objectInputStreamParameter.setFinal(true);

		final ReadTemplatedFile body = new ReadTemplatedFile();
		body.setType(type);
		newMethod.setBody(body);

		context.debug("Overridden " + newMethod);
	}

	protected void overrideObjectReaderReadFieldsMethod(final NewConcreteType reader, final Type type) {
		Checker.notNull("parameter:reader", reader);
		Checker.notNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();

		final NewMethod newMethod = reader.newMethod();
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_FIELDS_METHOD);
		newMethod.setReturnType(context.getVoid());
		newMethod.setStatic(false);
		newMethod.setVisibility(Visibility.PUBLIC);

		// rename parameters to the same names used in templates...
		final NewMethodParameter object = newMethod.newParameter();
		object.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_INSTANCE_PARAMETER);
		object.setFinal(true);
		object.setType(type);

		final NewMethodParameter objectInputStream = newMethod.newParameter();
		objectInputStream.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_FIELDS_OBJECT_INPUT_STREAM_PARAMETER);
		objectInputStream.setFinal(true);
		objectInputStream.setType(this.getObjectInputStream());

		final ReadFieldsTemplatedFile body = new ReadFieldsTemplatedFile();
		newMethod.setBody(body);
		body.setType(type);

		// add all fields to the template
		final Iterator<Field> fields = this.filterSerializableFields(type.getFields()).iterator();
		int fieldCount = 0;
		context.branch();

		while (fields.hasNext()) {
			final Field field = fields.next();
			final Method setter = this.createFieldSetter(reader, field);
			body.addFieldSetter(setter);

			fieldCount++;
		}

		context.unbranch();
		context.debug("Overridden " + newMethod);
	}

	/**
	 * Creates a private method that uses jsni to retrieve the value of a field.
	 * Each and every serializable field will have a typed getter to retrieve
	 * the value/reference which is then written using a ObjectOutputStream
	 * 
	 * @param reader
	 * @param field
	 * @return The newly created setter method
	 */
	protected Method createFieldSetter(final NewConcreteType reader, final Field field) {
		Checker.notNull("parameter:reader", reader);
		Checker.notNull("parameter:field", field);

		final GeneratorContext context = this.getGeneratorContext();

		final SetFieldTemplatedFile body = new SetFieldTemplatedFile();
		body.setField(field);

		final NewMethod method = reader.newMethod();
		method.setAbstract(false);
		method.setBody(body);
		method.setFinal(true);
		method.setName(GeneratorHelper.buildSetterName(field.getName()));
		method.setNative(true);
		method.setReturnType(context.getVoid());
		method.setStatic(false);
		method.setVisibility(Visibility.PRIVATE);

		final NewMethodParameter instance = method.newParameter();
		instance.setFinal(true);
		instance.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_FIELD_SETTER_INSTANCE_PARAMETER);
		instance.setType(field.getEnclosingType());

		final NewMethodParameter value = method.newParameter();
		value.setFinal(true);
		value.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_FIELD_SETTER_VALUE_PARAMETER);

		final Type fieldType = field.getType();
		value.setType(fieldType);

		if (fieldType.equals(context.getLong())) {
			method.addMetaData("com.google.gwt.core.client.UnsafeNativeLong", "");
		}

		context.debug(field.getName());

		return method;
	}

	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * Loops thru the map of serializable types and creates an ObjectWriter for
	 * each every type that requires one.
	 * 
	 * @param serializables
	 *            A set of types needing to be serialized
	 * @param typesToWriters
	 *            A map containing all types and their corresponding existing
	 *            readers
	 */
	protected void createObjectWriters(final Set<Type> serializables, final Map<Type, Type> typesToWriters) {
		Checker.notNull("parameter:serializables", serializables);
		Checker.notNull("parameter:typesToWriters", typesToWriters);

		final Set<Type> orderedSerializables = this.sortSerializablesIntoHeirarchyOrder(serializables);
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Creating necessary ObjectWriters.");

		final Iterator<Type> types = orderedSerializables.iterator();
		int skippedGeneratingCount = 0;
		final Set<Type> newWriters = new TreeSet<Type>( TypeComparator.INSTANCE );
		
		while (types.hasNext()) {
			final Type type = (Type) types.next();
			final Object writer = typesToWriters.get( type );

			if (null != writer) {
				skippedGeneratingCount++;
				continue;
			}

			context.branch();
			final NewConcreteType newWriter = this.createObjectWriter(type, typesToWriters );
			typesToWriters.put(type, newWriter);
			
			context.branch();
			this.overrideObjectWriterWrite0Method(newWriter, type);
			this.addSingletonField( newWriter );
			
			context.unbranch();
			context.unbranch();
			
			newWriters.add( newWriter );
		}
		
		this.writeTypes( newWriters );
		context.unbranch();
	}

	protected NewConcreteType createObjectWriter(final Type type, final Map serializables) {
		Checker.notNull("parameter:type", type);
		Checker.notNull("parameter:serializables", serializables);

		final GeneratorContext context = this.getGeneratorContext();
		context.debug(type.getName());

		final String newTypeName = this.getGeneratedTypeName(type, SerializationConstants.OBJECT_WRITER_GENERATED_TYPE_SUFFIX, "rocket.serialization.client.writer");
		final NewConcreteType newConcreteType = context.newConcreteType(newTypeName);
		newConcreteType.setAbstract(false);
		newConcreteType.setFinal(false);

		context.debug(newTypeName);

		// check super class of type. if its not object extend its object writer
		// rather than cowi.
		Type objectWriterSuperType = null;

		while (true) {
			if (type.isArray()) {
				objectWriterSuperType = this.getArrayWriter();
				break;
			}
			final Type superType = type.getSuperType();
			if (superType.equals(context.getObject())) {
				objectWriterSuperType = this.getObjectWriterImpl();
				break;
			}

			objectWriterSuperType = (Type) serializables.get(type.getSuperType());
			if (null == objectWriterSuperType) {
				throw new IllegalStateException("Unable to fetch the writer for the super type \"" + superType
						+ "\" whilst processing \"" + type.getName() + "\".");
			}
			break;
		}

		newConcreteType.setSuperType(objectWriterSuperType);
		context.debug("extends " + objectWriterSuperType);

		newConcreteType.addMetaData(SerializationConstants.SERIALIZABLE_TYPE, type.getName());

		final NewConstructor constructor = newConcreteType.newConstructor();
		constructor.setBody(EmptyCodeBlock.INSTANCE);
		constructor.setVisibility(Visibility.PUBLIC);
		return newConcreteType;
	}

	protected void overrideObjectWriterWrite0Method(final NewConcreteType writer, final Type type) {
		Checker.notNull("parameter:writer", writer);
		Checker.notNull("parameter:type", type);

		if (type.isArray()) {
			this.overrideObjectWriterWriteMethod0ForArrayType(writer, type);
		} else {
			this.overrideObjectWriterWriteFieldsMethod(writer, type);
			this.overrideObjectWriterWrite0MethodForNonArrayType(writer, type);
		}
	}

	protected void overrideObjectWriterWriteMethod0ForArrayType(final NewConcreteType writer, final Type type) {
		Checker.notNull("parameter:writer", writer);
		Checker.notNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();

		// locate the writeFields method that will be overridden.
		final List<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(context.getObject());
		parameterTypes.add(this.getObjectOutputStream());

		final Method method = writer.getMostDerivedMethod(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE0_METHOD, parameterTypes);
		final NewMethod newMethod = method.copy(writer);
		newMethod.setAbstract(false);

		// rename parameters to the same names used in templates...
		final List newMethodParameters = newMethod.getParameters();
		final NewMethodParameter object = (NewMethodParameter) newMethodParameters.get(0);
		object.setFinal(true);
		object.setName(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE0_INSTANCE_PARAMETER);

		final NewMethodParameter objectOutputStream = (NewMethodParameter) newMethodParameters.get(1);
		objectOutputStream.setFinal(true);
		objectOutputStream.setName(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE0_OBJECT_OUTPUT_STREAM_PARAMETER);

		final WriteArrayTemplatedFile body = new WriteArrayTemplatedFile();
		body.setType(type);
		newMethod.setBody(body);

		if ( type.getComponentType() == context.getLong() ){
			newMethod.addMetaData("com.google.gwt.core.client.UnsafeNativeLong", "");
		}
		
		context.debug("Overridden " + newMethod);
	}

	protected void overrideObjectWriterWrite0MethodForNonArrayType(final NewConcreteType writer, final Type type) {
		Checker.notNull("parameter:writer", writer);
		Checker.notNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();

		// locate the writeFields method that will be overridden.
		final List<Type> parameterTypes = new ArrayList<Type>();
		parameterTypes.add(context.getObject());
		parameterTypes.add(this.getObjectOutputStream());

		final Method method = writer
				.getMostDerivedMethod(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE0_METHOD, parameterTypes);
		final NewMethod newMethod = method.copy(writer);
		newMethod.setAbstract(false);

		// rename parameters to the same names used in templates...
		final List<MethodParameter> newMethodParameters = newMethod.getParameters();
		final NewMethodParameter object = (NewMethodParameter) newMethodParameters.get(0);
		object.setFinal(true);
		object.setName(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE0_INSTANCE_PARAMETER);

		final NewMethodParameter objectOutputStream = (NewMethodParameter) newMethodParameters.get(1);
		objectOutputStream.setFinal(true);
		objectOutputStream.setName(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE0_OBJECT_OUTPUT_STREAM_PARAMETER);

		final WriteTemplatedFile body = new WriteTemplatedFile();
		body.setType(type);
		newMethod.setBody(body);

		newMethod.addMetaData(com.google.gwt.core.client.UnsafeNativeLong.class.getName(), "");
		context.debug("Overridden " + newMethod);
	}

	protected void overrideObjectWriterWriteFieldsMethod(final NewConcreteType writer, final Type type) {
		Checker.notNull("parameter:writer", writer);
		Checker.notNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();

		final NewMethod newMethod = writer.newMethod();
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setName(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE_FIELDS_METHOD);
		newMethod.setNative(false);
		newMethod.setReturnType(context.getVoid());
		newMethod.setStatic(false);
		newMethod.setVisibility(Visibility.PUBLIC);

		final NewMethodParameter instance = newMethod.newParameter();
		instance.setFinal(true);
		instance.setName(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE_FIELDS_INSTANCE_PARAMETER);
		instance.setType(type);

		final NewMethodParameter objectOutputStream = newMethod.newParameter();
		objectOutputStream.setFinal(true);
		objectOutputStream.setName(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE_FIELDS_OBJECT_OUTPUT_STREAM_PARAMETER);
		objectOutputStream.setType(this.getObjectOutputStream());

		final WriteFieldsTemplatedFile body = new WriteFieldsTemplatedFile();
		body.setType(type);
		newMethod.setBody(body);

		// add all fields to the template
		final Iterator fields = this.filterSerializableFields(type.getFields()).iterator();
		int fieldCount = 0;

		context.branch();

		while (fields.hasNext()) {
			final Field field = (Field) fields.next();
			final Method getter = this.createFieldGetter(writer, field);
			body.addFieldGetter(getter);

			fieldCount++;
		}

		context.unbranch();
		context.debug("Overridden " + newMethod);
	}

	/**
	 * Creates a private method that uses jsni to retrieve the value of a field.
	 * Each and every serializable field will have a typed getter to retrieve
	 * the value/reference which is then written using a ObjectOutputStream
	 * 
	 * @param writer
	 * @param field
	 * @return
	 */
	protected Method createFieldGetter(final NewConcreteType writer, final Field field) {
		Checker.notNull("parameter:writer", writer);
		Checker.notNull("parameter:field", field);

		final GeneratorContext context = this.getGeneratorContext();
		final GetFieldTemplatedFile body = new GetFieldTemplatedFile();
		body.setField(field);

		final NewMethod method = writer.newMethod();
		method.setAbstract(false);
		method.setBody(body);
		method.setFinal(true);
		method.setName(GeneratorHelper.buildGetterName(field.getName()));
		method.setNative(true);

		final Type fieldType = field.getType();
		method.setReturnType(fieldType);
		method.setStatic(false);
		method.setVisibility(Visibility.PRIVATE);

		if (fieldType.equals(context.getLong())) {
			method.addMetaData("com.google.gwt.core.client.UnsafeNativeLong", "");
		}
		// add a parameter
		final NewMethodParameter instance = method.newParameter();
		instance.setFinal(true);
		instance.setName(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_FIELD_GETTER_INSTANCE_PARAMETER);
		instance.setType(field.getEnclosingType());

		context.debug(field.getName());
		return method;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This comparator may be used to sort a collection so that types closer to
	 * Object appear first. SubTypes will always appear after their super type.
	 */
	static Comparator TYPE_HEIRARCHY_SORTER = new Comparator() {
		public int compare(final Object object, final Object otherObject) {
			return compare((Type) object, (Type) otherObject);
		}

		int compare(final Type type, final Type otherType) {
			int difference = 0;

			if (false == type.equals(otherType)) {
				final int i = this.getDepthFromObject(type);
				final int j = this.getDepthFromObject(otherType);

				difference = i - j;

				// ensure stable ordering.
				if (0 == difference) {
					difference = type.getName().compareTo(otherType.getName());
				}
			}
			return difference;
		}

		int getDepthFromObject(final Type type) {
			int depth = 0;

			Type temp = type;
			while (null != temp) {
				if (temp.getName().equals(OBJECT)) {
					break;
				}
				depth++;
				temp = temp.getSuperType();
			}
			return depth;
		}
	};

	static String OBJECT = Object.class.getName();

	/**
	 * Creates a new map that is ordered so that it is a heirarchical view of
	 * types.
	 * 
	 * @param serializables
	 * @return
	 */
	protected Set<Type> sortSerializablesIntoHeirarchyOrder(final Set<Type> serializables) {
		Checker.notNull("parameter:serializables", serializables);

		final Set<Type> sorted = new TreeSet(TYPE_HEIRARCHY_SORTER);
		sorted.addAll(serializables);
		Checker.equals("sorted", serializables.size(), sorted.size());
		return sorted;
	}
	
	
	/**
	 * This helper builds a set that contains only serializable fields skipping
	 * static and transient fields. The set is also sorted in alphabetical order
	 * using the individual field names.
	 * 
	 * @param fields
	 * @return
	 */
	protected Set<Field> filterSerializableFields(final Set<Field> fields) {
		Checker.notNull("parameter:fields", fields);

		final Set<Field> sorted = new TreeSet<Field>(FieldComparator.INSTANCE);

		final Iterator<Field> iterator = fields.iterator();
		while (iterator.hasNext()) {
			final Field field = iterator.next();

			if (field.isStatic()) {
				continue;
			}
			if (field.isTransient()) {
				continue;
			}

			sorted.add(field);
		}
		return sorted;
	}


	/**
	 * Adds a singleton method to the access a 
	 * {@link rocket.serialization.client.reader.ObjectReader} or
	 * {@link rocket.serialization.client.writer.ObjectWriter}
	 * 
	 * @param type
	 */
	protected void addSingletonField(final NewConcreteType type) {
		Checker.notNull("parameter:type", type);

		final NewField singleton = type.newField();

		final NewInstanceTemplatedFile body = new NewInstanceTemplatedFile();
		final Constructor constructor = type.getConstructor(Collections.EMPTY_LIST);
		body.setConstructor(constructor);

		singleton.setValue(body);

		singleton.setFinal(true);
		singleton.setName(SerializationConstants.SINGLETON);
		final Type returnType = type;
		singleton.setType(returnType);
		singleton.setStatic(true);
		singleton.setVisibility(Visibility.PUBLIC);

		this.getGeneratorContext().debug(type.getName());
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Creates a NewConcreteType which contains the serialization factory type
	 * being assembled. At this stage no methods have been overridden or changed
	 * on the new type. Any abstract methods will be overridden at a later
	 * stage.
	 * 
	 * @param serializables
	 * @param newTypeName
	 * @return
	 */
	protected NewConcreteType createSerializableFactory(final String newTypeName) {
		Checker.notEmpty("parameter:newTypeName", newTypeName);

		final GeneratorContext context = this.getGeneratorContext();
		context.info("Creating serialization factory " + newTypeName);

		final NewConcreteType serializationFactory = context.newConcreteType(newTypeName);
		serializationFactory.setAbstract(false);
		serializationFactory.setFinal(true);
		serializationFactory.setSuperType(this.getSerializationFactory());
		return serializationFactory;
	}

	/**
	 * Overrides the generated SerializationFactory to return a anonymous ClientObjectOutputStream which implements the getObjectReader( String typeName ) method.
	 * @param serializationFactory
	 * @param objectReaders
	 */
	protected void overrideSerializationFactoryGetObjectReader(final NewConcreteType serializationFactory, final Map<Type,Type> objectReaders) {
		Checker.notNull("parameter:serializationFactory", serializationFactory);
		Checker.notNull("parameter:objectReaders", objectReaders);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug("Overriding \"" + SerializationConstants.SERIALIZATION_FACTORY_GET_OBJECT_READER + "\" to register needed ObjectReaders.");

		final Method method = serializationFactory.getMostDerivedMethod(SerializationConstants.SERIALIZATION_FACTORY_GET_OBJECT_READER, Collections.nCopies(1, context.getString()));
		final NewMethod newMethod = method.copy(serializationFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(true);

		final SwitchTemplatedFile body = new SwitchTemplatedFile();

		final Iterator<Map.Entry<Type,Type>> iterator = objectReaders.entrySet().iterator();
		while (iterator.hasNext()) {
			final Map.Entry<Type,Type> entry = iterator.next();

			final Type type = entry.getKey();
			if( false == this.isSerializable(type)){
				continue;
			}
			
			
			final Type objectReader = entry.getValue();
			final Field objectReaderSingleton = objectReader.getField(SerializationConstants.SINGLETON);

			body.register(type, objectReaderSingleton);
			
			context.debug( type.getName() + " = " + objectReader.getName() );
		}

		newMethod.setBody(body);

		context.unbranch();
	}

	/**
	 * Overrides the generated SerializationFactory to return a anonymous ClientObjectOutputStream which implements the getObjectWriter( String typeName ) method.
	 * @param serializationFactory
	 * @param objectWriters
	 */
	protected void overrideSerializationFactoryGetObjectWriter(final NewConcreteType serializationFactory, final Map<Type,Type> objectWriters) {
		Checker.notNull("parameter:serializationFactory", serializationFactory);
		Checker.notNull("parameter:objectWriters", objectWriters);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug("Overriding \"" + SerializationConstants.SERIALIZATION_FACTORY_GET_OBJECT_WRITER + "\" to register needed ObjectWriters.");

		final Method method = serializationFactory.getMostDerivedMethod(SerializationConstants.SERIALIZATION_FACTORY_GET_OBJECT_WRITER, Collections.nCopies(1, context.getString()));
		final NewMethod newMethod = method.copy(serializationFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(true);

		final SwitchTemplatedFile body = new SwitchTemplatedFile();

		final Iterator<Map.Entry<Type, Type>> iterator = objectWriters.entrySet().iterator();
		while (iterator.hasNext()) {
			final Map.Entry<Type,Type> entry = iterator.next();

			final Type type = entry.getKey();
			if( false == this.isSerializable(type)){
				continue;
			}
			
			final Type objectWriter = entry.getValue();
			final Field objectWriterSingleton = objectWriter.getField(SerializationConstants.SINGLETON);

			body.register(type, objectWriterSingleton);
			
			context.debug( type.getName() + " = " + objectWriter.getName() );
		}

		newMethod.setBody(body);

		context.unbranch();
	}

	protected Type getSerializationFactory() {
		return this.getGeneratorContext().getType(SerializationConstants.SERIALIZATION_FACTORY);
	}

	

	protected Type getObjectReaderImpl() {
		return this.getGeneratorContext().getType(SerializationConstants.OBJECT_READER_IMPL);
	}

	protected Type getArrayReader() {
		return this.getGeneratorContext().getType(SerializationConstants.ARRAY_READER);
	}

	protected Type getObjectInputStream() {
		return this.getGeneratorContext().getType(SerializationConstants.OBJECT_INPUT_STREAM);
	}

	protected Type getObjectWriterImpl() {
		return this.getGeneratorContext().getType(SerializationConstants.OBJECT_WRITER_IMPL);
	}

	protected Type getArrayWriter() {
		return this.getGeneratorContext().getType(SerializationConstants.ARRAY_WRITER);
	}

	protected Type getObjectOutputStream() {
		return this.getGeneratorContext().getType(SerializationConstants.OBJECT_OUTPUT_STREAM);
	}

	/**
	 * Helper which includes a special case if the the type being generated is
	 * part of java.lang or java.util.
	 * 
	 * @param type
	 * @param suffix
	 * @param packageName
	 * @return
	 */
	public String getGeneratedTypeName(final Type type, final String suffix, final String packageName) {
		Checker.notNull("parameter:type", type);
		Checker.notEmpty("parameter:suffix", suffix);
		Checker.notNull("parameter:packageName", packageName);

		String generatedTypeName = this.getGeneratorContext().getGeneratedTypeName(type.getName(), suffix);
		if (generatedTypeName.startsWith(SerializationConstants.JAVA_LANG)) {
			generatedTypeName = packageName + '.' + generatedTypeName.replace('.', '_');
		}
		return generatedTypeName;
	}
	

	/**
	 * Helper which writes out each and every NewConcreteType found in the given collection
	 * @param types A collection of NewConcreteTypes that need to be written.
	 */
	protected void writeTypes(final Collection types) {
		Checker.notNull("parameter:newTypes", types);

		final Iterator iterator = types.iterator();
		while (iterator.hasNext()) {
			final NewConcreteType newConcreteType = (NewConcreteType) iterator.next();
			newConcreteType.write();
		}
	}

}