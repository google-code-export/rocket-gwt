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
import rocket.generator.rebind.initializer.Initializer;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
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
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;
import rocket.util.server.IoHelper;

/**
 * This generator creates not only the SerializationFactory but also creates on
 * demand all the ObjectReader/Writers for each serializable type if they dont already exist.
 * 
 * @author Miroslav Pokorny
 */
public class SerializationFactoryGenerator extends Generator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		this.setBlackList(this.loadBlackLists());

		// readable types
		final Set readableTypeNames = this.getReadableTypesListFromAnnotation(type);
		final Set writeableTypeNames = this.getWritableTypesListFromAnnotation(type);
		if( readableTypeNames.isEmpty() && writeableTypeNames.isEmpty() ){
			this.throwUnableToFindBothReadableAndWritableTypeNameAnnotations( type );
		}
		
		final Set readableTypes = this.findReadableSerializableTypes(readableTypeNames);
		this.warnIfNativeMethodsFound(readableTypes);

		// find any preexisting readers/writers.
		final Map typesToObjectReaders = this.findObjectReaders(readableTypes);

		// process writers...		
		final Set writeableTypes = this.findWritableSerializableTypes(writeableTypeNames);
		this.warnIfNativeMethodsFound(writeableTypes);
		final Map typesToObjectWriters = this.findObjectWriters(writeableTypes);

		// generate readers and then writers
		this.generateObjectReaders(typesToObjectReaders);
		this.generateObjectWriters(typesToObjectWriters);

		// nows the time to create the SerializationFactory
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		final NewConcreteType serializationFactory = this.createSerializableFactory(newTypeName);
		
		this.overrideSerializationFactoryGetObjectReader( serializationFactory, typesToObjectReaders );
		this.overrideSerializationFactoryGetObjectWriter( serializationFactory, typesToObjectWriters );
		
		context.unbranch();
		
		return serializationFactory;
	}

	/**
	 * Checks and reports if a type matches any blacklist.
	 * @param type
	 * @return
	 */
	protected boolean isBlackListed(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		boolean blacklisted = false;

		// check black list...
		final Iterator blackListers = this.getBlackList().iterator();
		while (blackListers.hasNext()) {
			final TypeMatcher matcher = (TypeMatcher) blackListers.next();
			if (matcher.matches(type)) {
				blacklisted = true;
				break;
			}
		}

		return blacklisted;
	}

	/**
	 * Scans each and every package for a blacklist file and loads the embedded expressions. 
	 * @return
	 */
	protected Set loadBlackLists() {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Attempting to load and merge all expressions within all located blacklists with all packages.");

		final Set blackLists = new HashSet();

		final SubTypesVisitor packageVisitor = new SubTypesVisitor() {
			protected boolean visit(final Type type) {
				final Package packagee = type.getPackage();
				if (false == this.packages.contains(packagee)) {
					this.packages.add(packagee);

					final Set loaded = SerializationFactoryGenerator.this.loadBlackListFromPackage(packagee);
					blackLists.addAll(loaded);
				}
				return false;
			}

			protected boolean skipInitialType() {
				return false;
			}

			Set packages = new HashSet();
		};
		packageVisitor.start(context.getObject());

		context.info("Located " + blackLists.size() + " expressions in total after searching all packages for \""
				+ SerializationConstants.BLACKLIST_FILENAME + "\" files.");
		context.unbranch();
		return blackLists;
	}

	/**
	 * Attempts to load the black list file from the given package. 
	 * @param packagee
	 * @return A set of types or an empty set if the black list was not found.
	 */
	protected Set loadBlackListFromPackage(final Package packagee) {
		ObjectHelper.checkNotNull("parameter:package", packagee);

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("Package: " + packagee.getName());

		final Set expressions = new HashSet();

		final String fileName = this.getResourceName(packagee, SerializationConstants.BLACKLIST_FILENAME);
		InputStream inputStream = null;

		while (true) {
			try {
				inputStream = this.getClass().getResourceAsStream(fileName);
				if (null == inputStream) {
					break;
				}

				// use a BufferedReader to read a line at a time skipping comments and empty lines. 
				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				while (true) {
					final String line = reader.readLine();
					// eof reached...
					if (null == line) {
						break;
					}

					// skip empty / comment lines...
					final String typeName = line.trim();
					if (StringHelper.isNullOrEmpty(typeName) || typeName.startsWith("#")) {
						continue;
					}

					final TypeMatcher typeNameMatcher = TypeMatcherFactory.createTypeNameMatcher(typeName);
					expressions.add(typeNameMatcher);
				}

				context.info("Located " + expressions.size() + " blacklist expressions within \"" + fileName + "\".");
				break;

			} catch (final RuntimeException caught) {
				throw caught;
			} catch (final Exception caught) {
				throw new SerializationFactoryGeneratorException("Failed to load black list, message: " + caught.getMessage(), caught);
			} finally {
				IoHelper.closeIfNecessary(inputStream);
			}
		}
		return expressions;
	}

	/**
	 * A set of back list expressi
	 */
	private Set blackList;

	protected Set getBlackList() {
		ObjectHelper.checkNotNull("field:blackList", blackList);
		return this.blackList;
	}

	protected void setBlackList(final Set blackList) {
		ObjectHelper.checkNotNull("parameter:blackList", blackList);
		this.blackList = blackList;
	}

	protected Set getReadableTypesListFromAnnotation(final Type type) {
		final Set readableTypeNames = this.getTypeNamesListFromAnnotation(type, SerializationConstants.SERIALIZABLE_READABLE_TYPES);
		this.logTypeNames("Listing ReadableTypes from annotation", readableTypeNames);
		return readableTypeNames;
	}

	protected Set getWritableTypesListFromAnnotation(final Type type) {
		final Set writableTypeNames = this.getTypeNamesListFromAnnotation(type, SerializationConstants.SERIALIZABLE_WRITABLE_TYPES);
		this.logTypeNames("Listing WritableTypes from annotation", writableTypeNames);
		return writableTypeNames;
	}

	/**
	 * Helper which takes a list of types from an annotation from the given type.
	 * @param type
	 * @param annotation
	 * @return
	 */
	protected Set getTypeNamesListFromAnnotation(final Type type, final String annotation) {
		ObjectHelper.checkNotNull("parameter:type", type);

		final List typeNames = type.getMetadataValues(annotation);
		return new TreeSet( typeNames );
	}
	
	protected void throwUnableToFindBothReadableAndWritableTypeNameAnnotations( final Type type ){
		throw new SerializationFactoryGeneratorException( "Unable to find any types to read or write, the \"" + SerializationConstants.SERIALIZABLE_READABLE_TYPES + "\" and \"" + SerializationConstants.SERIALIZABLE_WRITABLE_TYPES + "\" are both missing from " + type );
	}

	protected Set findReadableSerializableTypes(final Set typeNames) {
		final Set types = this.findSerializablesTypes(typeNames);
		
		this.logTypes("Listing readable serializable types", types);
		return types;
	}

	protected Set findWritableSerializableTypes(final Set typeNames) {
		final Set types = this.findSerializablesTypes(typeNames);
		
		this.logTypes("Listing writable serializable types", types);
		return types;
	}

	/**
	 * Builds up a set that contains all the reachable types branching from the types found in the given list.
	 * @param typeNames
	 * @return A set of types
	 */
	protected Set findSerializablesTypes(final Set typeNames) {
		ObjectHelper.checkNotNull("parameter:typeNames", typeNames);

		final GeneratorContext context = this.getGeneratorContext();
		final Set serializables = new TreeSet(TypeComparator.INSTANCE);
		final Iterator iterator = typeNames.iterator();

		while (iterator.hasNext()) {
			final String typeName = (String) iterator.next();
			final Type type = context.getType(typeName);
			final Set serializablesFoundForSerializableType = this.findSerializableTypes(type);

			serializables.addAll(serializablesFoundForSerializableType);
		}

		// removing java.lang.Object
		final Type object = context.getObject();
		serializables.remove(object);
		context.debug("Removed java.lang.Object from serializable types (java.lang.Object has no fields to serialize anyway).");

		final Type string = context.getString();
		serializables.remove(string);
		context.debug("Removed java.lang.String from serializable types (inbuilt support for java.lang.String already present).");
	
		return serializables;
	}

	/**
	 * This method attempts to find all serializable types including special
	 * cases for list,set and map that need to be serialized. An exception will
	 * be thrown if any unserializable type is encountered.
	 * 
	 * @param type
	 * @return A set containing all reachable serializable types.
	 */
	protected Set findSerializableTypes(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Finding all serializable types reachable from " + type);

		final ReachableTypesVisitor visitor = new ReachableTypesVisitor() {

			protected boolean skipTypeThatImplementsInterface(final Type type, final Type interfacee) {
				return false == SerializationFactoryGenerator.this.isOrHasSerializableSubType(type);
			}
			
			protected boolean skipArray(final Type array) {
				return false;
			}

			/**
			 * Skip transient or static fields.
			 */
			protected boolean skipField(final Field field) {
				return field.isStatic() || field.isTransient();
			}

			/**
			 * This method includes special tests to ensure that list/set/map element types are visited.
			 * 
			 * @param field
			 */
			protected void visitField(final Field field) {
				while (true) {
					final Type fieldType = field.getType();
					final String fieldTypeName = fieldType.getName();
					context.debug("Visiting field " + field);				
					
					if (field.isFinal()) {
						SerializationFactoryGenerator.this.throwFinalFieldEncountered(field);
					}

					if (SerializationConstants.LIST.equals(fieldTypeName)) {
						this.processInterface(fieldType);

						final Type elementType = SerializationFactoryGenerator.this.getListElementType(field);
						context.debug(elementType + " (List)");
						this.visitType(elementType);
						break;
					}
					if (SerializationConstants.SET.equals(fieldTypeName)) {
						this.processInterface(fieldType);

						final Type elementType = SerializationFactoryGenerator.this.getSetElementType(field);
						context.debug(elementType + " (Set)");
						this.visitType(elementType);
						break;
					}
					if (SerializationConstants.MAP.equals(fieldTypeName)) {
						this.processInterface(fieldType);

						final Type keyType = SerializationFactoryGenerator.this.getMapKeyType(field);
						final Type valueType = SerializationFactoryGenerator.this.getMapValueType(field);
						context.debug( keyType  + " (Map key)");
						context.debug( keyType  + " (Map value)");

						this.visitType(keyType);
						this.visitType(valueType);
						break;
					}

					// all other types...
					this.visitType(fieldType);
					break;
				}
			}
			
			protected void visitType( final Type type ){
				if( false == SerializationFactoryGenerator.this.isOrHasSerializableSubType( type ) ){
					SerializationFactoryGenerator.this.throwEncounteredUnserializableType( type);
				}
				
				super.visitType(type);
			}

			protected boolean skipType(final Type type) {
				final boolean skip = !SerializationFactoryGenerator.this.isOrHasSerializableSubType(type);
				return skip;
			}

			protected boolean skipSuperType(final Type superType) {
				return !SerializationFactoryGenerator.this.isOrHasSerializableSubType(superType);
			}

			protected boolean skipSubType(final Type subType) {
				return !SerializationFactoryGenerator.this.isOrHasSerializableSubType(subType);
			}

			protected void addConcreteType(final Type type) {
				context.debug( type.toString() );
				super.addConcreteType(type);
			}
		};
		visitor.start(type);

		final Set found = visitor.getConcreteTypes();

		context.info("Found " + found.size() + " reachable type(s) starting with " + type);
		context.unbranch();
		return found;
	}
	
	protected void throwEncounteredUnserializableType( final Type type ){
		throw new SerializationFactoryGeneratorException("Encountered type that cannot be serialized, type: " + type );
	}

	/**
	 * Tests if the given type is serializable or has a serializable sub type. 
	 * 
	 * @param type
	 * @return
	 */
	protected boolean isOrHasSerializableSubType(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		boolean serialize = false;

		while (true) {
			if (this.isBlackListed(type)) {
				serialize = false;
				break;
			}
			
			if( this.isSerializable(type)){
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
	 * @param type
	 * @return
	 */
	protected boolean isSerializable( final Type type ){
		boolean serialize = false;

		while (true) {
			if (type.isPrimitive()) {
				serialize = true;
				break;
			}

			// special test for arrays, all array are considered to be serializable.
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
	 * This class will continue visit all sub types of the given type until it runs out of sub types or a serializable type is found.
	 * If a serializable sub type is found thats also not blacklisted then the {@link #hasSerializableSubType()} will return true. 
	 */
	class SerializableSubTypeFinder extends SubTypesVisitor {
		protected boolean visit(final Type type) {
			boolean skipRemainder = false;
			// only visit if sub type is not blacklisted and is also serializable.
			if (false == SerializationFactoryGenerator.this.isBlackListed(type)
					&& SerializationFactoryGenerator.this.implementsSerializable(type)) {
				this.setSerializableSubType(true);
				skipRemainder = true;
			}
			return skipRemainder;
		}

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
		ObjectHelper.checkNotNull("parameter:type", type);
		return type.isAssignableTo(this.getSerializable());
	}

	protected Type getSerializable() {
		return this.getGeneratorContext().getType(SerializationConstants.SERIALIZABLE);
	}

	protected void throwTypeIsNotSerializable(final Type type) {
		throw new SerializationFactoryGeneratorException("The type \"" + type + "\" is not serializable (doesnt implement "
				+ this.getSerializable() + ")");
	}

	protected void throwFinalFieldEncountered(final Field field) {
		throw new SerializationFactoryGeneratorException("The field " + field + " is final and thus cannot be (de)serialized.");
	}

	/**
	 * Visits all methods for all types and spits out warning messages for each
	 * native method found.
	 * 
	 * @param types
	 */
	protected void warnIfNativeMethodsFound(final Set types) {
		ObjectHelper.checkNotNull("parameter:types", types);

		int nativeMethodTotal = 0;
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug( "Listing native jsni methods.");
		
		final Iterator typesIterator = types.iterator();
		while (typesIterator.hasNext()) {
			final Type type = (Type) typesIterator.next();
			nativeMethodTotal = nativeMethodTotal + this.warnIfNativeMethodsFound(type);
		}

		if (nativeMethodTotal > 0) {
			context.warn(
					"The " + types.size() + " serializables types contain " + nativeMethodTotal + " native (jsni) methods in total.");
		}
		context.unbranch();
	}

	/**
	 * This method only checks declared in the current type and not methods that
	 * belong to super types.
	 * 
	 * @param type
	 * @return The number of native methods found on the given type.
	 */
	protected int warnIfNativeMethodsFound(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		final Iterator methods = type.getMethods().iterator();

		int nativeMethodCount = 0;
		int methodCount = 0;
		final GeneratorContext context = this.getGeneratorContext();

		while (methods.hasNext()) {
			final Method method = (Method) methods.next();
			if (method.isNative()) {
				nativeMethodCount++;

				context.debug( method.toString() );
			}
			methodCount++;
		}

		if (nativeMethodCount > 0) {
			context.debug("Type: " + type + " " + nativeMethodCount + "("  + methodCount + ")" );
		}

		return nativeMethodCount;
	}

	/**
	 * Returns a map that is keyed on type and the value is the existing
	 * ObjectReader
	 * 
	 * @param serializables
	 * @return
	 */
	protected Map findObjectReaders(final Set serializables) {
		ObjectHelper.checkNotNull("parameter:serializables", serializables);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Finding existing ObjectReaders...");

		final ObjectReaderFinder finder = new ObjectReaderFinder() {
			protected Type getImplementingInterface() {
				return SerializationFactoryGenerator.this.getObjectReader();
			}

			protected boolean shouldBeSerialized(final Type type) {
				return SerializationFactoryGenerator.this.isOrHasSerializableSubType(type);
			}
		};

		final Map objectReaders = createMapFromSet(serializables);
		final Map existingObjectReaders = finder.build(serializables);

		this.logBoundTypes("Listing discovered existing ObjectReaders.", existingObjectReaders);

		objectReaders.putAll(existingObjectReaders);
		this.logBoundTypes("Listing all types requiring an ObjectReaders after merging existing and types requiring a ObjectReader.", objectReaders);

		context.unbranch();
		
		return objectReaders;
	}

	/**
	 * Returns a map that is keyed on type and the value is the existing
	 * ObjectWriter
	 * 
	 * @param serializables
	 * @return
	 */
	protected Map findObjectWriters(final Set serializables) {
		ObjectHelper.checkNotNull("parameter:serializables", serializables);

		final GeneratorContext context = this.getGeneratorContext();
		context.info("Finding existing ObjectWriters...");

		final ObjectWriterFinder finder = new ObjectWriterFinder() {
			protected Type getImplementingInterface() {
				return SerializationFactoryGenerator.this.getObjectWriter();
			}

			protected boolean shouldBeSerialized(final Type type) {
				return SerializationFactoryGenerator.this.isOrHasSerializableSubType(type);
			}
		};
		final Map objectWriters = createMapFromSet(serializables);
		
		final Map existingObjectWriters = finder.build(serializables);
		this.logBoundTypes("Listing discovered existing ObjectWriters.", existingObjectWriters);

		objectWriters.putAll(existingObjectWriters);
		this.logBoundTypes("Listing all types requiring an ObjectWriters after merging existing and types requiring a ObjectWriter.", objectWriters);

		return objectWriters;
	}

	/**
	 * Creates a map and proceeds to add the elements of the set with null
	 * values to a new map.
	 * 
	 * @param set
	 * @return
	 */
	protected Map createMapFromSet(final Set set) {
		ObjectHelper.checkNotNull("parameter:set", set);

		final Map map = new TreeMap( TypeComparator.INSTANCE );
		final Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			map.put(iterator.next(), null);
		}
		return map;
	}

	/**
	 * Generates ObjectReaders if necessary for each of the types found in the
	 * given set
	 * 
	 * @param serializables
	 *            A map of types and possibly preexisting ObjectReaders
	 */
	protected void generateObjectReaders(final Map serializables) {
		ObjectHelper.checkNotNull("parameter:serializables", serializables);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info( "Creating necessary ObjectReaders for reachable all types...");
		
		final Map newObjectReaders = this.createObjectReaders(serializables);
		context.branch();
		
		this.overrideObjectReadersNewInstanceMethods(newObjectReaders);
		this.overrideObjectReadersReadMethod(newObjectReaders);
		this.addSingletonFields(newObjectReaders.values(), this.getObjectReader());
		this.writeTypes( newObjectReaders.values() );
		
		context.unbranch();
		context.unbranch();
	}

	/**
	 * Loops thru the map of serializable types and creates an ObjectReader for each every type that requires one.
	 * @param serializables
	 * @return An set of generated ObjectReaders
	 */
	protected Map createObjectReaders(final Map serializables) {
		ObjectHelper.checkNotNull("parameter:serializables", serializables);	
		
		final Map orderedSerializables = this.sortSerializablesIntoHeirarchyOrder(serializables);
		
		final Iterator types = orderedSerializables.entrySet().iterator();
		final Map newObjectReaders = new TreeMap( TypeComparator.INSTANCE );
		int skippedGeneratingCount = 0;
		
		while (types.hasNext()) {
			final Map.Entry entry = (Map.Entry) types.next();
			
			final Type type = (Type) entry.getKey();
			final Object reader = entry.getValue();			
			if ( null != reader ) {
				skippedGeneratingCount++;
				continue;
			}

			final NewConcreteType newReader = this.createObjectReader(type, serializables );
			newObjectReaders.put(type, newReader);
			serializables.put(type, newReader);
		}		
		this.getGeneratorContext().info( "Created " + newObjectReaders.size() + " and skipped " + skippedGeneratingCount + " ObjectReaders as these already existed.");
		
		return newObjectReaders;
	}

	/**
	 * Factory method that creates a new NewConcreteType that will become the ObjectReader for the given type.
	 * @param type
	 * @param newTypeName
	 * @return
	 */
	protected NewConcreteType createObjectReader(final Type type, final Map serializables ) {
		final GeneratorContext context = this.getGeneratorContext();
		
		final String newTypeName = this.getGeneratedTypeName( type, SerializationConstants.OBJECT_READER_GENERATED_TYPE_SUFFIX, "rocket.serialization.client.reader");
		final NewConcreteType newConcreteType = context.newConcreteType( newTypeName );
		newConcreteType.setAbstract(false);
		newConcreteType.setFinal(false);		

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
			objectReaderSuperType = (Type) serializables.get( type.getSuperType() );
			break;
		}

		newConcreteType.setSuperType(objectReaderSuperType);

		// add an annotation that marks the type being handled by this ObjectReader
		newConcreteType.addMetaData(SerializationConstants.SERIALIZABLE_TYPE, type.getName());

		// create a public no arguments constructor.
		final NewConstructor constructor = newConcreteType.newConstructor();
		constructor.setBody(EmptyCodeBlock.INSTANCE);
		constructor.setVisibility(Visibility.PUBLIC);

		context.debug( "Created " + newConcreteType+ " for the type " + type + "." );
		return newConcreteType;
	}
	
	/**
	 * Loops thru and overrides the newInstance method belonging to each and every ObjectReader
	 * @param objectReadersToTypes
	 */
	protected void overrideObjectReadersNewInstanceMethods(final Map objectReadersToTypes) {
		final Iterator iterator = objectReadersToTypes.entrySet().iterator();
		while (iterator.hasNext()) {
			final Map.Entry entry = (Map.Entry) iterator.next();

			final Type type = (Type) entry.getKey();
			final NewConcreteType reader = (NewConcreteType) entry.getValue();
			
			this.overrideObjectReaderNewInstanceMethod(type, reader);
		}
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
			if (type.isAbstract()) {
				break;
			}
			this.overrideObjectReaderNewInstanceMethodForNonArrayType(type, reader);
			break;
		}
	}

	protected void overrideObjectReaderNewInstanceMethodForArrayType(final Type arrayType, final NewConcreteType reader) {
		ObjectHelper.checkNotNull("parameter:type", arrayType);
		ObjectHelper.checkNotNull("parameter:reader", reader);

		final List parameters = new ArrayList();
		parameters.add(this.getGeneratorContext().getString());
		parameters.add(this.getObjectInputStream());

		final Method method = reader.getMostDerivedMethod(SerializationConstants.CLIENT_OBJECT_READER_IMPL_NEW_INSTANCE_METHOD, parameters);
		final NewMethod newMethod = method.copy(reader);
		newMethod.setAbstract(false);

		final NewArrayInstanceTemplatedFile populateNewArray = new NewArrayInstanceTemplatedFile();
		populateNewArray.setType(arrayType.getComponentType());
		newMethod.setBody(populateNewArray);

		this.getGeneratorContext().debug("Overridden " + newMethod );
	}

	protected void overrideObjectReaderNewInstanceMethodForNonArrayType(final Type type, final NewConcreteType reader) {
		ObjectHelper.checkNotNull("parameter:type", type);
		ObjectHelper.checkNotNull("parameter:reader", reader);

		final GeneratorContext context = this.getGeneratorContext();
		final List parameters = new ArrayList();
		parameters.add(context.getString());
		parameters.add(this.getObjectInputStream());

		final Method method = reader.getMostDerivedMethod(SerializationConstants.CLIENT_OBJECT_READER_IMPL_NEW_INSTANCE_METHOD, parameters);
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

		context.debug("Overridden " + newMethod );
	}

	/**
	 * Loops thru and overrides the read method belonging to each and every ObjectReader
	 * @param typesAndObjectReaders
	 */
	protected void overrideObjectReadersReadMethod(final Map typesAndObjectReaders) {
		final Iterator iterator = typesAndObjectReaders.entrySet().iterator();
		while (iterator.hasNext()) {
			final Map.Entry entry = (Map.Entry) iterator.next();
			
			final Type type = (Type) entry.getKey();
			final NewConcreteType reader = (NewConcreteType) entry.getValue();			

			this.overrideObjectReaderReadMethod(type, reader);
		}
	}

	protected void overrideObjectReaderReadMethod(final Type type, final NewConcreteType reader) {
		if (false == reader.isArray()) {
			this.overrideObjectReaderReadFieldsMethod( reader, type );			
			this.overrideObjectReaderReadMethod(reader, type);
		}
	}

	protected void overrideObjectReaderReadMethod(final NewConcreteType reader, final Type type) {
		ObjectHelper.checkNotNull("parameter:reader", reader);
		ObjectHelper.checkNotNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();

		// locate the writeFields method that will be overridden.
		final List parameterTypes = new ArrayList();
		parameterTypes.add(context.getObject());
		final Type objectInputStreamType = this.getObjectInputStream();
		parameterTypes.add( objectInputStreamType );

		final Method method = reader.getMostDerivedMethod(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_METHOD, parameterTypes);
		final NewMethod newMethod = method.copy(reader);
		newMethod.setNative( false );
		
		// rename parameters to the same names used in templates...
		final List newMethodParameters = newMethod.getParameters();
		final NewMethodParameter object = (NewMethodParameter) newMethodParameters.get(0);
		object.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_INSTANCE_PARAMETER);
		object.setFinal(true);

		final NewMethodParameter objectInputStreamParameter = (NewMethodParameter) newMethodParameters.get(1);
		objectInputStreamParameter.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_OBJECT_INPUT_STREAM_PARAMETER);
		objectInputStreamParameter.setFinal(true);

		final ReadTemplatedFile body = new ReadTemplatedFile();
		body.setType(type);
		newMethod.setBody(body);
		
		context.info("Overridden " + newMethod );
	}
	
	protected void overrideObjectReaderReadFieldsMethod(final NewConcreteType reader, final Type type) {
		ObjectHelper.checkNotNull("parameter:reader", reader);
		ObjectHelper.checkNotNull("parameter:type", type);
		
		final GeneratorContext context = this.getGeneratorContext();
		
		final NewMethod newMethod = reader.newMethod();
		newMethod.setAbstract( false );
		newMethod.setFinal( true );
		newMethod.setName( SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_FIELDS_METHOD );
		newMethod.setReturnType( context.getVoid() );
		newMethod.setStatic( false );
		newMethod.setVisibility( Visibility.PUBLIC );

		// rename parameters to the same names used in templates...
		final NewMethodParameter object = newMethod.newParameter();
		object.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_INSTANCE_PARAMETER);
		object.setFinal(true);
		object.setType( type );

		final NewMethodParameter objectInputStream = newMethod.newParameter();
		objectInputStream.setName(SerializationConstants.CLIENT_OBJECT_READER_IMPL_READ_FIELDS_OBJECT_INPUT_STREAM_PARAMETER);
		objectInputStream.setFinal(true);
		objectInputStream.setType( this.getObjectInputStream() );

		final ReadFieldsTemplatedFile body = new ReadFieldsTemplatedFile();
		newMethod.setBody(body);
		body.setType(type);

		// add all fields to the template
		final Iterator fields = this.filterSerializableFields(type.getFields()).iterator();
		int fieldCount = 0;
		while (fields.hasNext()) {
			final Field field = (Field) fields.next();
			final Method setter = this.createFieldSetter(reader, field);
			body.addFieldSetter(setter);

			fieldCount++;
		}

		context.info("Overridden " + newMethod );
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
		ObjectHelper.checkNotNull("parameter:reader", reader);
		ObjectHelper.checkNotNull("parameter:field", field);

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
		value.setType(field.getType());

		context.debug("Created setter method " + method + " for field: " + field);

		return method;
	}

	/**
	 * Generates ObjectWriters if necessary for each of the types found in the
	 * given set
	 * 
	 * @param serializables
	 *            A map of types and possibly preexisting ObjectWriters
	 */
	protected void generateObjectWriters(final Map serializables) {
		ObjectHelper.checkNotNull("parameter:serializables", serializables);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info( "Creating necessary ObjectWriters for reachable all types...");		
		context.branch();
		
		final Map newObjectWriters = this.createObjectWriters(serializables);
		this.overrideObjectWritersWriteMethods(newObjectWriters);
		this.addSingletonFields(newObjectWriters.values(), this.getObjectWriter());
		this.writeTypes( newObjectWriters.values() );
		
		context.unbranch();
		context.unbranch();
	}

	/**
	 * Loops thru the map of serializable types and creates an ObjectWriter for each every type that requires one.
	 * @param serializables
	 * @return A map containing any new generated ObjectWriters
	 */
	protected Map createObjectWriters(final Map serializables) {
		ObjectHelper.checkNotNull("parameter:serializables", serializables);

		final Map orderedSerializables = this.sortSerializablesIntoHeirarchyOrder(serializables);
		final Iterator types = orderedSerializables.entrySet().iterator();
		final Map newObjectWriters = new TreeMap( TypeComparator.INSTANCE );
		int skippedGeneratingCount = 0;
				
		while (types.hasNext()) {
			final Map.Entry entry = (Map.Entry) types.next();
			
			final Type type = (Type) entry.getKey();
			final Object writer = entry.getValue();			
			if ( null != writer ) {
				skippedGeneratingCount++;
				continue;
			}

			final NewConcreteType newWriter = this.createObjectWriter(type, serializables );
			newObjectWriters.put(type, newWriter);
			serializables.put(type, newWriter);
		}
		
		this.getGeneratorContext().info( "Created " + newObjectWriters.size() + " and skipped generating for " + skippedGeneratingCount + " ObjectWriters as these already existed.");
		
		return newObjectWriters;
	}

	protected NewConcreteType createObjectWriter(final Type type, final Map serializables ) {
		ObjectHelper.checkNotNull("parameter:type", type);
				
		final GeneratorContext context = this.getGeneratorContext();
		
		final String newTypeName = this.getGeneratedTypeName(type, SerializationConstants.OBJECT_WRITER_GENERATED_TYPE_SUFFIX, "rocket.serialization.client.writer");
		final NewConcreteType newConcreteType = context.newConcreteType( newTypeName );
		newConcreteType.setAbstract(false);
		newConcreteType.setFinal(false);

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

			objectWriterSuperType = (Type) serializables.get( type.getSuperType() ); 
			break;
		}

		newConcreteType.setSuperType(objectWriterSuperType);

		newConcreteType.addMetaData(SerializationConstants.SERIALIZABLE_TYPE, type.getName());

		final NewConstructor constructor = newConcreteType.newConstructor();
		constructor.setBody(EmptyCodeBlock.INSTANCE);
		constructor.setVisibility(Visibility.PUBLIC);

		context.info( "Created " + newConcreteType+ " for the type " + type + "." );
		return newConcreteType;
	}

	/**
	 * Loops thru and overrides the write method belonging to each and every ObjectWriter
	 * @param objectWritersToTypes
	 */
	protected void overrideObjectWritersWriteMethods(final Map objectWritersToTypes) {
		final Iterator iterator = objectWritersToTypes.entrySet().iterator();
		while (iterator.hasNext()) {
			final Map.Entry entry = (Map.Entry) iterator.next();
			
			final Type type = (Type) entry.getKey();
			final NewConcreteType writer = (NewConcreteType) entry.getValue();			

			this.overrideObjectWriterWrite0Method(writer, type);
		}
	}

	protected void overrideObjectWriterWrite0Method(final NewConcreteType writer, final Type type) {
		if (type.isArray()) {
			this.overrideObjectWriterWriteMethod0ForArrayType(writer, type);
		} else {
			this.overrideObjectWriterWriteFieldsMethod(writer, type);			
			this.overrideObjectWriterWrite0MethodForNonArrayType(writer, type);
		}
	}

	protected void overrideObjectWriterWriteMethod0ForArrayType(final NewConcreteType writer, final Type type) {
		final GeneratorContext context = this.getGeneratorContext();

		// locate the writeFields method that will be overridden.
		final List parameterTypes = new ArrayList();
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

		context.info("Overridden " + SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE0_METHOD + "() method to write array elements.");
	}

	protected void overrideObjectWriterWrite0MethodForNonArrayType(final NewConcreteType writer, final Type type) {
		final GeneratorContext context = this.getGeneratorContext();

		// locate the writeFields method that will be overridden.
		final List parameterTypes = new ArrayList();
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

		final WriteTemplatedFile body = new WriteTemplatedFile();
		body.setType(type);
		newMethod.setBody(body);

		context.info("Overridden " + newMethod );
	}

	protected void overrideObjectWriterWriteFieldsMethod(final NewConcreteType writer, final Type type) {
		final GeneratorContext context = this.getGeneratorContext();
		
		final NewMethod newMethod = writer.newMethod();
		newMethod.setAbstract(false);
		newMethod.setFinal( true );
		newMethod.setName( SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE_FIELDS_METHOD );
		newMethod.setNative( false );
		newMethod.setReturnType( context.getVoid() );
		newMethod.setStatic( false );
		newMethod.setVisibility( Visibility.PUBLIC );
		
		final NewMethodParameter instance = newMethod.newParameter();
		instance.setFinal( true );
		instance.setName( SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE_FIELDS_INSTANCE_PARAMETER );
		instance.setType( type );

		final NewMethodParameter objectOutputStream = newMethod.newParameter();
		objectOutputStream.setFinal( true );
		objectOutputStream.setName( SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_WRITE_FIELDS_OBJECT_OUTPUT_STREAM_PARAMETER );
		objectOutputStream.setType( this.getObjectOutputStream() );

		final WriteFieldsTemplatedFile body = new WriteFieldsTemplatedFile();
		body.setType(type);
		newMethod.setBody(body);

		// add all fields to the template
		final Iterator fields = this.filterSerializableFields(type.getFields()).iterator();
		int fieldCount = 0;

		while (fields.hasNext()) {
			final Field field = (Field) fields.next();
			final Method getter = this.createFieldGetter(writer, field);
			body.addFieldGetter(getter);

			fieldCount++;
		}

		context.info("Overridden " + newMethod );
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
		ObjectHelper.checkNotNull("parameter:writer", writer);
		ObjectHelper.checkNotNull("parameter:field", field);

		final GetFieldTemplatedFile body = new GetFieldTemplatedFile();
		body.setField(field);

		final NewMethod method = writer.newMethod();
		method.setAbstract(false);
		method.setBody(body);
		method.setFinal(true);
		method.setName(GeneratorHelper.buildGetterName(field.getName()));
		method.setNative(true);
		method.setReturnType(field.getType());
		method.setStatic(false);
		method.setVisibility(Visibility.PRIVATE);

		// add a parameter
		final NewMethodParameter instance = method.newParameter();
		instance.setFinal(true);
		instance.setName(SerializationConstants.CLIENT_OBJECT_WRITER_IMPL_FIELD_GETTER_INSTANCE_PARAMETER);
		instance.setType(field.getEnclosingType());

		this.getGeneratorContext().debug("Created getter method " + method + " for field: " + field);
		return method;
	}

	/**
	 * Loops thru and adds a singleton to each and every ObjectReader/Writer.
	 * @param readerOrWriters
	 * @param fieldType
	 */
	protected void addSingletonFields(final Collection readerOrWriters, Type fieldType) {
		ObjectHelper.checkNotNull("parameter:readerOrWriters", readerOrWriters);

		final Iterator iterator = readerOrWriters.iterator();

		while (iterator.hasNext()) {
			final NewConcreteType readerOrWriter = (NewConcreteType) iterator.next();

			this.addSingletonField(readerOrWriter, fieldType);
		}
	}

	/**
	 * Adds a singleton method to the given
	 * {@link rocket.serialization.client.reader.ObjectReader} or
	 * {@link rocket.serialization.client.writer.ObjectWriter}
	 * 
	 * @param readerOrWriter
	 * @param fieldType
	 */
	protected void addSingletonField(final NewConcreteType readerOrWriter, final Type fieldType) {
		ObjectHelper.checkNotNull("parameter:readerOrWriter", readerOrWriter);
		ObjectHelper.checkNotNull("parameter:fieldType", fieldType);

		final NewField singleton = readerOrWriter.newField();

		final NewInstanceTemplatedFile body = new NewInstanceTemplatedFile();
		final Constructor constructor = readerOrWriter.getConstructor(Collections.EMPTY_LIST);
		body.setConstructor(constructor);

		singleton.setValue(body);

		singleton.setFinal(true);
		singleton.setName(SerializationConstants.SINGLETON);
		final Type returnType = fieldType;
		singleton.setType(returnType);
		singleton.setStatic(true);
		singleton.setVisibility(Visibility.PUBLIC);

		this.getGeneratorContext().info("Added static field to hold " + readerOrWriter.getName() + " singleton.");
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
		StringHelper.checkNotEmpty("parameter:newTypeName", newTypeName);

		final GeneratorContext context = this.getGeneratorContext();
		context.info("Creating serialization factory " + newTypeName);

		final NewConcreteType serializationFactory = context.newConcreteType( newTypeName );
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
	protected void overrideSerializationFactoryGetObjectReader( final NewConcreteType serializationFactory, final Map objectReaders ){
		ObjectHelper.checkNotNull("parameter:serializationFactory", serializationFactory);
		ObjectHelper.checkNotNull("parameter:objectReaders", objectReaders);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug( "Listing ObjectReaders that are visible to this serialization factory.");

		final Method method = serializationFactory.getMostDerivedMethod(SerializationConstants.SERIALIZATION_FACTORY_GET_OBJECT_READER, Collections.nCopies(1, context.getString()));
		final NewMethod newMethod = method.copy( serializationFactory );
		newMethod.setAbstract( false );
		newMethod.setFinal( true );
		newMethod.setNative( true );

		final SwitchTemplatedFile body = new SwitchTemplatedFile();
		
		boolean first = true;		
		final Iterator iterator = objectReaders.entrySet().iterator();
		while (iterator.hasNext()) {
			final Map.Entry entry = (Map.Entry) iterator.next();

			final Type type = (Type) entry.getKey();			
			if( type.isAbstract() || false == this.isSerializable(type)){
				if( first ){
					context.debug( "Types that dont need a ObjectReader follow (type is abstract or instances are not serializable).");
					first = false;
				}
				context.debug( "Type " + type );
				continue;
			}
			
			final Type objectReader = (Type) entry.getValue();
			final Field objectReaderSingleton = objectReader.getField( SerializationConstants.SINGLETON );
			
			body.register(type, objectReaderSingleton);
		}

		newMethod.setBody(body);
		
		context.info( "Overridden " + newMethod );
		context.unbranch();
	}
	
	/**
	 * Overrides the generated SerializationFactory to return a anonymous ClientObjectOutputStream which implements the getObjectWriter( String typeName ) method.
	 * @param serializationFactory
	 * @param objectWriters
	 */
	protected void overrideSerializationFactoryGetObjectWriter( final NewConcreteType serializationFactory, final Map objectWriters ){
		ObjectHelper.checkNotNull("parameter:serializationFactory", serializationFactory);
		ObjectHelper.checkNotNull("parameter:objectWriters", objectWriters);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug( "Listing ObjectWriters that are visible to this serialization factory.");

		final Method method = serializationFactory.getMostDerivedMethod(SerializationConstants.SERIALIZATION_FACTORY_GET_OBJECT_WRITER, Collections.nCopies(1, context.getString()));
		final NewMethod newMethod = method.copy( serializationFactory );
		newMethod.setAbstract( false );
		newMethod.setFinal( true );
		newMethod.setNative( true );

		final SwitchTemplatedFile body = new SwitchTemplatedFile();
		
		boolean first = true;		
		final Iterator iterator = objectWriters.entrySet().iterator();
		while (iterator.hasNext()) {
			final Map.Entry entry = (Map.Entry) iterator.next();

			final Type type = (Type) entry.getKey();			
			if( type.isAbstract() || false == this.isSerializable(type)){
				if( first ){
					context.debug( "Types that dont need a ObjectWriter follow (type is abstract or instances are not serializable).");
					first = false;
				}
				context.debug( "Type " + type );
				continue;
			}
			
			final Type objectWriter = (Type) entry.getValue();
			final Field objectWriterSingleton = objectWriter.getField( SerializationConstants.SINGLETON );
			
			body.register(type, objectWriterSingleton);
		}

		newMethod.setBody(body);
		
		context.info( "Overridden " + newMethod );
		context.unbranch();
	}

	protected Type getSerializationFactory() {
		return this.getGeneratorContext().getType(SerializationConstants.SERIALIZATION_FACTORY);
	}

	protected Type getList() {
		return this.getGeneratorContext().getType(SerializationConstants.LIST);
	}

	protected Type getListElementType(final Field field) {
		return this.getTypeFromAnnotation(field, SerializationConstants.LIST_ELEMENT_TYPE);
	}

	protected Type getSet() {
		return this.getGeneratorContext().getType(SerializationConstants.SET);
	}

	protected Type getSetElementType(final Field field) {
		return this.getTypeFromAnnotation(field, SerializationConstants.SET_ELEMENT_TYPE);
	}

	protected Type getMap() {
		return this.getGeneratorContext().getType(SerializationConstants.MAP);
	}

	protected Type getMapKeyType(final Field field) {
		return this.getTypeFromAnnotation(field, SerializationConstants.MAP_KEY_TYPE);
	}

	protected Type getMapValueType(final Field field) {
		return this.getTypeFromAnnotation(field, SerializationConstants.MAP_VALUE_TYPE);
	}

	protected Type getTypeFromAnnotation(final Field field, final String annotation) {
		final List values = field.getMetadataValues(annotation);
		if (values.size() == 0) {
			throw new SerializationException("Unable to locate \"" + annotation + "\" on field " + field);
		}
		final String typeName = (String) values.get(0);
		final Type type = this.getGeneratorContext().findType(typeName);
		if (null == type) {
			throw new SerializationException("Unable to find type \"" + typeName + "\" which was taken from the annotation \"" + annotation
					+ "\" from field: " + field);
		}
		return type;
	}

	protected String getGeneratedTypeNameSuffix() {
		return SerializationConstants.SERIALIZATION_FACTORY_GENERATED_TYPE_SUFFIX;
	}

	protected Type getObjectReader() {
		return this.getGeneratorContext().getType(SerializationConstants.OBJECT_READER);
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

	protected Type getObjectWriter() {
		return this.getGeneratorContext().getType(SerializationConstants.OBJECT_WRITER);
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
	 * Helper which includes a special case if the the type being generated is part of java.lang or java.util.
	 * @param type
	 * @param suffix
	 * @param packageName
	 * @return
	 */
	public String getGeneratedTypeName(final Type type, final String suffix, final String packageName ) {
		String generatedTypeName = this.getGeneratorContext().getGeneratedTypeName( type.getName(), suffix);
		if (generatedTypeName.startsWith("java.lang")) {
			generatedTypeName = packageName + generatedTypeName.substring("java.lang".length()).replace('.', '_');
		}
		return generatedTypeName;
	}
	
	/**
	 * This comparator may be used to sort a collection so that types closer to Object appear first.
	 * SubTypes will always appear after their super type.
	 */
	static Comparator TYPE_HEIRARCHY_SORTER = new Comparator() {
		public int compare(final Object object, final Object otherObject) {
			return compare((Type) object, (Type) otherObject);
		}

		int compare(final Type type, final Type otherType) {
			int difference = 0;
			
			if( false == type.equals( otherType )){
				final int i = this.getDepthFromObject(type);
				final int j = this.getDepthFromObject( otherType );
				difference = i <= j ? -1 : + 1;				
			}
			return difference;
		}
		
		int getDepthFromObject( final Type type ){
			int depth = 0;
			
			Type temp = type;
			while( null != temp ){
				if( temp.getName().equals( OBJECT )){
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
	 * Creates a new map that is ordered so that it is a heirarchical view of types.
	 * @param serializables
	 * @return
	 */
	protected Map sortSerializablesIntoHeirarchyOrder(final Map serializables) {
		ObjectHelper.checkNotNull("parameter:serializables", serializables);
		
		final Map sorted = new TreeMap( TYPE_HEIRARCHY_SORTER );
		
		final Iterator iterator = serializables.entrySet().iterator();
		while( iterator.hasNext() ){
			final Map.Entry entry = (Map.Entry)iterator.next();	
			sorted.put( entry.getKey(), entry.getValue() );
		}		
				
		PrimitiveHelper.checkEquals( "sorted", serializables.size(), sorted.size());
		return sorted;
	}

	/**
	 * This comparator sorts two fields using their names.
	 */
	static Comparator FIELD_ALPHABETICAL_ORDER_SORTER = new Comparator() {
		public int compare(final Object object, final Object otherObject) {
			return compare((Field) object, (Field) otherObject);
		}

		int compare(final Field field, final Field otherField) {
			return field.getName().compareTo(otherField.getName());
		}
	};

	/**
	 * This helper builds a set that contains only serializable fields skipping
	 * static and transient fields. The set is also sorted in alphabetical order
	 * using the individual field names.
	 * 
	 * @param fields
	 * @return
	 */
	protected Set filterSerializableFields(final Set fields) {
		final Set sorted = new TreeSet(SerializationFactoryGenerator.FIELD_ALPHABETICAL_ORDER_SORTER);

		final Iterator iterator = fields.iterator();
		while (iterator.hasNext()) {
			final Field field = (Field) iterator.next();

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
	 * Helper which writes out each and every NewConcreteType found in the given collection
	 * @param types A collection of NewConcreteTypes that need to be written.
	 */
	protected void writeTypes( final Collection types ){
		ObjectHelper.checkNotNull("parameter:newTypes", types );
		
		final Iterator iterator = types.iterator();
		while( iterator.hasNext() ){
				final NewConcreteType newConcreteType = (NewConcreteType) iterator.next();
				newConcreteType.write();
		}		
	}

	protected void logTypes(final String title, final Collection types) {
		final GeneratorContext context = this.getGeneratorContext();
		
		if (context.isDebugEnabled()) {			
			context.branch();
			context.debug( title );
			
			final Iterator iterator = types.iterator();
			while (iterator.hasNext()) {						
				final Type type = (Type) iterator.next();
				context.debug( "Type \"" + type.getName() + "\"");
			}
			
			context.unbranch();
		}
	}
	
	protected void logTypeNames(final String title, final Collection typeNames) {
		final GeneratorContext context = this.getGeneratorContext();				
		if (context.isDebugEnabled()) {
			context.branch();
			context.debug( title );
						
			final Iterator iterator = typeNames.iterator();
			while (iterator.hasNext()) {						
				final String typeName = (String) iterator.next();
				context.debug( "Type \"" + typeName + "\"");
			}
			
			context.unbranch();
		}		
	}

	protected void logBoundTypes(final String title, final Map types) {
		final GeneratorContext context = this.getGeneratorContext();
		
		if (context.isDebugEnabled()) {
			context.branch();
			context.debug( title );
			
			final Iterator iterator = types.entrySet().iterator();
			while (iterator.hasNext()) {
				final Map.Entry entry = (Map.Entry) iterator.next();
				
				String message = entry.getKey().toString(); 
				final Object value = entry.getValue();
				if( null != value){
					message = message + "=" + value;
				}
				context.debug( message );
			}
			
			context.unbranch();
		}
	}
}