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

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import rocket.compiler.TreeLoggers;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.packagee.PackageNotFoundException;
import rocket.generator.rebind.type.NewType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.type.TypeNotFoundException;
import rocket.util.client.Checker;
import rocket.util.server.InputOutput;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.util.Util;

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
		Checker.notNull("field:generator", generator);
		return this.generator;
	}

	public void setGenerator(final Generator generator) {
		Checker.notNull("parameter:generator", generator);
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
		final StringBuffer buf = new StringBuffer();

		final String packageName = this.getPackageName(name);
		buf.append(packageName);
		buf.append('.');

		final char[] chars = this.getSimpleClassName(name).toCharArray();
		for (int i = 0; i < chars.length; i++) {
			final char c = chars[i];

			if ('_' == c) {
				buf.append("__");
				continue;
			}
			if ('.' == c) {
				buf.append("_0");
				continue;
			}
			if ('[' == c) {
				buf.append("_1");
				continue;
			}
			if (']' == c) {
				buf.append("_2");
				continue;
			}
			buf.append(c);
		}

		buf.append(suffix);

		return buf.toString();
	}

	/**
	 * Fetches the package for the given name.
	 * 
	 * @param name
	 * @return The located package or null if it wasnt found
	 */
	public Package findPackage(final String name) {
		Checker.notEmpty("parameter:name", name);

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
		throw new PackageNotFoundException("Unable to find a package with the name \"" + name + "\".");
	}

	/**
	 * Factory method which creates a package instance the first time a request
	 * is made.
	 * 
	 * @param name
	 * @return The new Package object
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
	private Map<String, Package> packages;

	protected Map<String, Package> getPackages() {
		Checker.notNull("field:packages", packages);
		return this.packages;
	}

	protected void setPackages(final Map<String, Package> packages) {
		Checker.notNull("parameter:packages", packages);
		this.packages = packages;
	}

	protected Map<String, Package> createPackages() {
		return new HashMap<String, Package>();
	}

	/**
	 * Fetches the type for the given name.
	 * 
	 * @param name
	 * @return
	 */
	public Type findType(final String name) {
		Checker.notEmpty("parameter:name", name);

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
			throw new TypeNotFoundException("Unable to find type \"" + name + "\".");
		}
		return type;
	}

	/**
	 * Factory method which creates a type instance the first time a request is
	 * made.
	 * 
	 * @param name
	 * @return The new type.
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
	 * Factory method which creates an adapter for any array type. This method
	 * should only ever be called once for each array type after which all
	 * references are cached.
	 * 
	 * @param name
	 * @return A Type
	 */
	abstract protected Type createArrayType(final String name);

	/**
	 * Factory method which creates an adapter for any type. This method should
	 * only ever be called once for each array type after which all references
	 * are cached.
	 * 
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
	private Map<String, Type> types;

	protected Map<String, Type> getTypes() {
		Checker.notNull("field:types", types);
		return this.types;
	}

	protected void setTypes(final Map<String, Type> types) {
		Checker.notNull("parameter:types", types);
		this.types = types;
	}

	protected Map<String, Type> createTypes() {
		return new HashMap<String, Type>();
	}

	/**
	 * A cache of all NewTypes.
	 */
	private Set<Type> newTypes;

	public Set<Type> getNewTypes() {
		Checker.notNull("field:newTypes", newTypes);
		return this.newTypes;
	}

	protected void setNewTypes(final Set<Type> newTypes) {
		Checker.notNull("parameter:newTypes", newTypes);
		this.newTypes = newTypes;
	}

	protected Set<Type> createNewTypes() {
		return new HashSet<Type>();
	}

	protected void addNewType(final NewType type) {
		Checker.notNull("parameter:type", type);

		this.getNewTypes().add(type);
	}

	/**
	 * Retrieves the package name portion of the fully qualified class name.
	 * 
	 * @param fullyQualifiedClassName
	 * @return
	 */
	public String getPackageName(final String fullyQualifiedClassName) {
		Checker.notEmpty("parameter:fullyQualifiedClassName", fullyQualifiedClassName);

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
		Checker.notEmpty("parameter:fullyQualifiedClassName", fullyQualifiedClassName);

		final String packageName = this.getPackageName(fullyQualifiedClassName);
		final String simpleClassNameWhichIsPossiblyAInnerClass = fullyQualifiedClassName.substring(packageName.length() + 1);
		return simpleClassNameWhichIsPossiblyAInnerClass.replace('.', '_');
	}

	/**
	 * Takes a type and returns a fully qualified class name even if the input
	 * type is an inner class.
	 * 
	 * @param type
	 * @return A fully qualified name of the given type.
	 */
	public String getStandaloneTypeName(final JClassType type) {
		Checker.notNull("parameter:type", type);

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
	 * Retrieves the value for a given property name.
	 * 
	 * @param name
	 *            The property name
	 * @return The value
	 * @throws GeneratorException
	 *             if the property was not found.
	 */
	public String getProperty(final String name) throws GeneratorException {
		try {
			final TreeLogger logger = this.getLogger().getTreeLogger();
			return this.getPropertyOracle().getPropertyValue(logger, name);
		} catch (final BadPropertyValueException caught) {
			throw new GeneratorException("Unable to get property value for \"" + name + "\", message: " + caught.getMessage(), caught);
		}
	}

	/**
	 * The GeneratorContext for this code generation session.
	 */
	private com.google.gwt.core.ext.GeneratorContext generatorContext;

	protected com.google.gwt.core.ext.GeneratorContext getGeneratorContext() {
		Checker.notNull("field:generatorContext", generatorContext);
		return this.generatorContext;
	}

	public void setGeneratorContext(final com.google.gwt.core.ext.GeneratorContext generatorContext) {
		Checker.notNull("parameter:generatorContext", generatorContext);
		this.generatorContext = generatorContext;
	}

	protected TypeOracle getTypeOracle() {
		return this.getGeneratorContext().getTypeOracle();
	}

	protected PropertyOracle getPropertyOracle() {
		return this.getGeneratorContext().getPropertyOracle();
	}

	/**
	 * Only GWT backed TypeOracleGenerator actually support creating resources.
	 */
	public OutputStream createResource(final String filename) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Helper which writes a resource if it doesnt already exist generating a
	 * strong filename to guarantee uniqueness.
	 * 
	 * @param contents
	 * @param suffix
	 *            A suffix which is appended to the hash. Typically this will
	 *            include "nocache." + the file extension.
	 * @return The partial path of the written file.
	 */
	public String createResource(final byte[] contents, final String suffix) {
		Checker.notNull("parameter:contents", contents);
		Checker.notEmpty("parameter:suffix", suffix);

		final String hash = Util.computeStrongName(contents);
		final String filename = hash + suffix;

		OutputStream outputStream = this.createResource(filename);
		if (null != outputStream) {
			try {
				outputStream.write(contents);
				outputStream.flush();
			} catch (final IOException io) {
				InputOutput.throwIOException(io);
			} finally {
				InputOutput.closeIfNecessary(outputStream);
			}
		}
		return filename;
	}

	/**
	 * The root logger
	 */
	private Logger rootLogger;

	protected Logger getRootLogger() {
		Checker.notNull("field:rootLogger", rootLogger);
		return this.rootLogger;
	}

	protected void setRootLogger(final Logger rootLogger) {
		Checker.notNull("parameter:rootLogger", rootLogger);

		this.rootLogger = rootLogger;
	}

	public void setRootTreeLogger(final TreeLogger rootLogger) {
		Checker.notNull("parameter:rootLogger", rootLogger);

		this.setRootLogger(new Logger(rootLogger));
	}

	/**
	 * Retrieves the most distant leaf node
	 * 
	 * @return
	 */
	protected Logger getLogger() {
		Logger logger = this.getRootLogger();
		while (logger.hasLogger()) {
			logger = logger.getLogger();
		}

		return logger;
	}

	public TreeLogger getTreeLogger() {
		return this.getLogger().getTreeLogger();
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

	protected void log(final TreeLogger.Type level, final String message, final Throwable throwable) {
		this.getLogger().log(level, message, throwable);
	}

	public boolean isTraceEnabled() {
		return this.getLogger().isLoggable(TreeLogger.TRACE);
	}

	public boolean isDebugEnabled() {
		return this.getLogger().isLoggable(TreeLogger.DEBUG);
	}

	public boolean isInfoEnabled() {
		return this.getLogger().isLoggable(TreeLogger.INFO);
	}

	public void branch() {
		this.getLogger().setBranch(true);
	}

	public void delayedBranch() {
		this.getLogger().setDelayed(true);
	}

	public void unbranch() {
		Logger logger = this.getRootLogger();
		Logger hasLogger = null;

		while (logger.hasLogger()) {
			hasLogger = logger;
			logger = logger.getLogger();
		}

		if (null != hasLogger) {
			hasLogger.clearLogger();
		}
	}

	/**
	 * A specialised logger that bridges the logger api with the gwt TreeLogger
	 * api.
	 */
	static private class Logger {
		Logger(final TreeLogger treeLogger) {
			super();
			this.setTreeLogger(treeLogger);
			this.setBranch(false);
			this.setDelayed(false);
		}

		boolean isLoggable(final TreeLogger.Type type) {
			return this.getTreeLogger().isLoggable(type);
		}

		void log(final TreeLogger.Type level, final String message, final Throwable throwable) {
			while (true) {
				final TreeLogger treeLogger = this.getTreeLogger();
				final boolean branch = this.isBranch();
				final boolean delayed = this.isDelayed();

				if (false == branch && false == delayed) {
					treeLogger.log(level, message, throwable);
					break;
				}

				this.setDelayed(false);
				this.setBranch(false);

				final TreeLogger newTreeLogger = delayed ?
				/* delayed branch */
				TreeLoggers.delayedBranch(treeLogger, level, message, throwable, null) :
				/* not delayed - immediate */
				treeLogger.branch(level, message, throwable);

				this.setLogger(new Logger(newTreeLogger));
				break;
			}
		}

		/**
		 * The current logger being wrapped.
		 */
		TreeLogger treeLogger;

		TreeLogger getTreeLogger() {
			Checker.notNull("field:treeLogger", treeLogger);
			return this.treeLogger;
		}

		void setTreeLogger(final TreeLogger treeLogger) {
			Checker.notNull("parameter:treeLogger", treeLogger);
			this.treeLogger = treeLogger;
		}

		/**
		 * When true the next logging message will cause a branch.
		 */
		boolean branch;

		private boolean isBranch() {
			return this.branch;
		}

		private void setBranch(final boolean branch) {
			this.branch = branch;
		}

		/**
		 * If this logger has a branched logger exists this is it.
		 */
		Logger logger;

		Logger getLogger() {
			Checker.notNull("field:logger", logger);
			return this.logger;
		}

		boolean hasLogger() {
			return null != this.logger;
		}

		void setLogger(final Logger logger) {
			Checker.notNull("parameter:logger", logger);
			this.logger = logger;
		}

		void clearLogger() {
			if (null == this.logger) {
				throw new IllegalStateException("Attempt to unbranch without initial matching branch");
			}
			this.logger = null;
		}

		/**
		 * When true the next logging message will cause a delayed branch
		 */
		boolean delayed;

		private boolean isDelayed() {
			return this.delayed;
		}

		private void setDelayed(final boolean delayed) {
			this.delayed = delayed;
		}
	}
}