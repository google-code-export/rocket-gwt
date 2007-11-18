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
package rocket.generator.rebind;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.packagee.PackageNotFoundException;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewConcreteTypeImpl;
import rocket.generator.rebind.type.NewInterfaceType;
import rocket.generator.rebind.type.NewInterfaceTypeImpl;
import rocket.generator.rebind.type.NewType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.type.TypeNotFoundException;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;

/**
 * Convenient base class for all generator contexts.
 * 
 * @author Miroslav Pokorny
 */
abstract public class GeneratorContextImpl implements GeneratorContext {

	public GeneratorContextImpl() {
		super();

		this.setPackages(this.createPackages());
		this.setTypes(this.createTypes());
		this.setNewTypes(this.createNewTypes());
	}

	/**
	 * A reference back to the generator that started the whole code generation
	 * process.
	 */
	private Generator generator;

	public Generator getGenerator() {
		ObjectHelper.checkNotNull("field:generator", generator);
		return this.generator;
	}

	public void setGenerator(final Generator generator) {
		ObjectHelper.checkNotNull("parameter:generator", generator);
		this.generator = generator;
	}

	/**
	 * Builds the name for the generated type for the given type name. The
	 * returned class name will be that of a standalone and not a nested type,
	 * even if the original name was nested.
	 * 
	 * @param name
	 * @return The name of a standalone class.
	 */
	public String getGeneratedTypeName(final String name, final String suffix) {
		final String packageName = this.getPackageName(name);

		String simpleClassName = this.getSimpleClassName(name);
		simpleClassName.replace("_", "__");
		simpleClassName.replace('.', '_');

		return packageName + '.' + simpleClassName + suffix;
	}

	/**
	 * Fetches the package for the given name.
	 * 
	 * @param name
	 * @return
	 */
	public Package findPackage(final String name) {
		StringHelper.checkNotEmpty("parameter:name", name);

		Package packagee = (Package) this.getPackages().get(name);
		if (null == packagee) {
			packagee = this.createPackage(name);
			if (null != packagee) {
				this.addPackage(packagee);
			}
		}
		return packagee;
	}

	public Package getPackage(final String name) {
		final Package packagee = this.findPackage(name);
		if (null == packagee) {
			throwPackageNotFoundException(name);
		}
		return packagee;
	}

	protected void throwPackageNotFoundException(final String name) {
		throw new PackageNotFoundException("Unable to find a package with the name [" + name + "]");
	}

	/**
	 * Factory method which creates a package instance the first time a request
	 * is made.
	 * 
	 * @param name
	 * @return
	 */
	abstract protected Package createPackage(final String name);
	
	protected JPackage findJPackage(final String name) {
		return this.getTypeOracle().findPackage(name);
	}

	/**
	 * Adds a new package to the cache of known packages.
	 * 
	 * @param package
	 */
	public void addPackage(final Package packagee) {
		this.getPackages().put(packagee.getName(), packagee);
	}

	/**
	 * A cache of all packages.
	 */
	private Map packages;

	protected Map getPackages() {
		ObjectHelper.checkNotNull("field:packages", packages);
		return this.packages;
	}

	protected void setPackages(final Map packages) {
		ObjectHelper.checkNotNull("parameter:packages", packages);
		this.packages = packages;
	}

	protected Map createPackages() {
		return new HashMap();
	}

	/**
	 * Fetches the type for the given name.
	 * 
	 * @param name
	 * @return
	 */
	public Type findType(final String name) {
		StringHelper.checkNotEmpty("parameter:name", name);

		Type type = null;
		while (true) {
			// check cache first...
			type = (Type) this.getTypes().get(name);
			if (null != type) {
				break;
			}
			// iterate thru all types recently created...
			type = this.getNewType(name);
			if (null != type) {
				break;
			}

			// create time...
			type = this.createType(name);
			if (null == type) {
				break;
			}

			this.addType(type);
			break;
		}

		return type;
	}

	protected Type getNewType(final String name) {
		Type type = null;

		final Iterator newTypes = this.getNewTypes().iterator();
		while (newTypes.hasNext()) {
			final NewType newType = (NewType) newTypes.next();
			if (false == newType.hasName()) {
				continue;
			}
			if (newType.getName().equals(name)) {
				type = newType;
				break;
			}
		}

		return type;
	}

	public Type getType(final String name) {
		final Type type = this.findType(name);
		if (null == type) {
			throw new TypeNotFoundException("Unable to find type [" + name + "]");
		}
		return type;
	}

	/**
	 * Factory method which creates a type instance the first time a request is
	 * made.
	 * 
	 * @param name
	 * @return
	 */
	protected Type createType(final String name) {
		return name.endsWith("[]") ? this.createArrayType(name) : this.createClassType(name);
	}

	/**
	 * Registers each of the primitive types
	 */
	abstract protected void preloadTypes();
	
	public Type getBoolean() {
		return this.getType(GeneratorConstants.BOOLEAN);
	}

	public Type getByte() {
		return this.getType(GeneratorConstants.BYTE);
	}

	public Type getShort() {
		return this.getType(GeneratorConstants.SHORT);
	}

	public Type getInt() {
		return this.getType(GeneratorConstants.INT);
	}

	public Type getLong() {
		return this.getType(GeneratorConstants.LONG);
	}

	public Type getFloat() {
		return this.getType(GeneratorConstants.FLOAT);
	}

	public Type getDouble() {
		return this.getType(GeneratorConstants.DOUBLE);
	}

	public Type getChar() {
		return this.getType(GeneratorConstants.CHAR);
	}

	public Type getVoid() {
		return this.getType(GeneratorConstants.VOID);
	}

	public Type getObject() {
		return this.getType(GeneratorConstants.OBJECT);
	}

	public Type getString() {
		return this.getType(GeneratorConstants.STRING);
	}

	/**
	 * Factory method which creates an adapter for any array type. This method should only ever be called once for each array type after which all references are cached.
	 * @param name
	 * @return A Type
	 */
	abstract protected Type createArrayType(final String name);

	/**
	 * Factory method which creates an adapter for any type. This method should only ever be called once for each array type after which all references are cached.
	 * @param name
	 * @return a new JClassTypeTypeAdapter
	 */
	abstract protected Type createClassType(final String name);

	/**
	 * Adds a new type to the cache of known types.
	 * 
	 * @param type
	 */
	public void addType(final Type type) {
		if (type instanceof NewType) {
			this.addNewType((NewType) type);
		} else {
			this.getTypes().put(type.getName(), type);
		}
	}

	/**
	 * A cache of all types.
	 */
	private Map types;

	protected Map getTypes() {
		ObjectHelper.checkNotNull("field:types", types);
		return this.types;
	}

	protected void setTypes(final Map types) {
		ObjectHelper.checkNotNull("parameter:types", types);
		this.types = types;
	}

	protected Map createTypes() {
		return new HashMap();
	}

	/**
	 * A cache of all NewTypes.
	 */
	private Set newTypes;

	public Set getNewTypes() {
		ObjectHelper.checkNotNull("field:newTypes", newTypes);
		return this.newTypes;
	}

	protected void setNewTypes(final Set newTypes) {
		ObjectHelper.checkNotNull("parameter:newTypes", newTypes);
		this.newTypes = newTypes;
	}

	protected Set createNewTypes() {
		return new HashSet();
	}

	protected void addNewType(final NewType type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		this.getNewTypes().add(type);
	}

	/**
	 * Factory method which creates a new concrete type.
	 * 
	 * @return The new concrete type.
	 */
	public NewConcreteType newConcreteType() {
		final NewConcreteTypeImpl type = new NewConcreteTypeImpl();
		type.setGeneratorContext(this);
		type.setSuperType(this.getObject());
		type.setVisibility( Visibility.PUBLIC );

		this.addNewType(type);
		return type;
	}

	/**
	 * Factory method which creates a new interface type.
	 * 
	 * @return The new interface type.
	 */
	public NewInterfaceType newInterfaceType() {
		final NewInterfaceTypeImpl type = new NewInterfaceTypeImpl();
		type.setGeneratorContext(this);
		type.setSuperType(this.getObject());
		type.setVisibility( Visibility.PUBLIC );
		
		this.addNewType(type);
		return type;
	}

	/**
	 * Tests if a class has already been generated. If the class does not exist
	 * a PrintWriter is returned which may eventually be used to create the new
	 * class.
	 * 
	 * @param typeName
	 * @return Null if the class does not exist otherwise returns a PrintWriter
	 */
	public PrintWriter tryCreateTypePrintWriter(final String typeName) {
		GeneratorHelper.checkJavaTypeName("parameter:typeName", typeName);

		final String packageName = this.getPackageName(typeName);
		final String simpleClassName = this.getSimpleClassName(typeName);
		return this.getGeneratorContext().tryCreate(this.getLogger(), packageName, simpleClassName);
	}

	/**
	 * Creates a sourceWriter. All attempts to create a SourceWriter eventually
	 * call this method once they have setup the ClassSourceFileComposerFactory
	 * and gotten a PrintWriter
	 * 
	 * @param composerFactory
	 * @param printWriter
	 * @return
	 */
	public SourceWriter createSourceWriter(final ClassSourceFileComposerFactory composerFactory, final PrintWriter printWriter) {
		final com.google.gwt.user.rebind.SourceWriter sourceWriter = composerFactory.createSourceWriter(this.getGeneratorContext(),
				printWriter);

		return new SourceWriter() {
			public void beginJavaDocComment() {
				sourceWriter.beginJavaDocComment();
			}

			public void endJavaDocComment() {
				sourceWriter.endJavaDocComment();
			}

			public void indent() {
				sourceWriter.indent();
			}

			public void outdent() {
				sourceWriter.outdent();
			}

			public void print(final String string) {
				sourceWriter.print(string);
			}

			public void println() {
				sourceWriter.println();
			}

			public void println(final String string) {
				sourceWriter.println(string);
			}

			public void commit() {
				sourceWriter.commit(GeneratorContextImpl.this.getLogger());
			}

			public void rollback() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Retrieves the package name portion of the fully qualified class name.
	 * 
	 * @param fullyQualifiedClassName
	 * @return
	 */
	public String getPackageName(final String fullyQualifiedClassName) {
		StringHelper.checkNotEmpty("parameter:fullyQualifiedClassName", fullyQualifiedClassName);

		String packageName = null;
		final TypeOracle typeOracle = this.getTypeOracle();
		String name = fullyQualifiedClassName;

		while (true) {
			// appear to be a class in the unnamed package...
			final int simpleNameStartIndex = name.lastIndexOf('.');
			if (-1 == simpleNameStartIndex) {
				break;
			}

			packageName = name.substring(0, simpleNameStartIndex);
			final JPackage jPackage = typeOracle.findPackage(packageName);
			if (null != jPackage) {
				break;
			}
			name = packageName;
		}

		return packageName;
	}

	/**
	 * Retrieves the simple class name from a fully qualified class name. This
	 * method also converts all inner classes into standalone classes.
	 * 
	 * @param fullyQualifiedClassName
	 * @return the simple classname
	 */
	public String getSimpleClassName(final String fullyQualifiedClassName) {
		StringHelper.checkNotEmpty("parameter:fullyQualifiedClassName", fullyQualifiedClassName);

		final String packageName = this.getPackageName(fullyQualifiedClassName);
		final String simpleClassNameWhichIsPossiblyAInnerClass = fullyQualifiedClassName.substring(packageName.length() + 1);
		return simpleClassNameWhichIsPossiblyAInnerClass.replace('.', '_');
	}

	/**
	 * Takes a type and returns a fully qualified class name even if the input
	 * type is an inner class.
	 * 
	 * @param type
	 * @return
	 */
	public String getStandaloneTypeName(final JClassType type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		final String packageName = type.getPackage().getName();

		String qualifiedClassName = type.getQualifiedSourceName();
		qualifiedClassName = qualifiedClassName.substring(packageName.length() + 1); // drop
		// the
		// packageName
		// + '.'
		qualifiedClassName = qualifiedClassName.replaceAll("_", "__");

		// make inner class into regular classes.
		qualifiedClassName = qualifiedClassName.replace('.', '_');

		return packageName + '.' + qualifiedClassName;
	}

	/**
	 * The GeneratorContext for this code generation session.
	 */
	private com.google.gwt.core.ext.GeneratorContext generatorContext;

	protected com.google.gwt.core.ext.GeneratorContext getGeneratorContext() {
		ObjectHelper.checkNotNull("field:generatorContext", generatorContext);
		return this.generatorContext;
	}

	public void setGeneratorContext(final com.google.gwt.core.ext.GeneratorContext generatorContext) {
		ObjectHelper.checkNotNull("parameter:generatorContext", generatorContext);
		this.generatorContext = generatorContext;
	}

	protected TypeOracle getTypeOracle() {
		return this.getGeneratorContext().getTypeOracle();
	}

	/**
	 * This treelogger may be used to log messages
	 */
	private TreeLogger logger;

	protected TreeLogger getLogger() {
		ObjectHelper.checkNotNull("field:logger", logger);
		return this.logger;
	}

	public void setLogger(final TreeLogger logger) {
		ObjectHelper.checkNotNull("parameter:logger", logger);
		this.logger = logger;
	}

	public void trace(final String message) {
		this.log(TreeLogger.TRACE, message);
	}

	public void trace(final String message, final Throwable throwable) {
		this.log(TreeLogger.TRACE, message, throwable);
	}

	public void debug(final String message) {
		this.log(TreeLogger.DEBUG, message);
	}

	public void debug(final String message, final Throwable throwable) {
		this.log(TreeLogger.DEBUG, message, throwable);
	}

	public void info(final String message) {
		this.log(TreeLogger.INFO, message);
	}

	public void info(final String message, final Throwable throwable) {
		this.log(TreeLogger.INFO, message, throwable);
	}

	public void warn(final String message) {
		this.log(TreeLogger.WARN, message);
	}

	public void warn(final String message, final Throwable throwable) {
		this.log(TreeLogger.WARN, message, throwable);
	}

	public void error(final String message) {
		this.log(TreeLogger.ERROR, message);
	}

	public void error(final String message, final Throwable throwable) {
		this.log(TreeLogger.ERROR, message, throwable);
	}

	protected void log(final TreeLogger.Type treeLoggerLevel, final String message) {
		this.log(treeLoggerLevel, message, null);
	}

	protected void log(final TreeLogger.Type treeLoggerLevel, final String message, final Throwable throwable) {
		this.getLogger().log(treeLoggerLevel, message, throwable);
	}

	public void branch(final String message) {
		this.getLogger().branch(TreeLogger.INFO, message, null);
	}
}
