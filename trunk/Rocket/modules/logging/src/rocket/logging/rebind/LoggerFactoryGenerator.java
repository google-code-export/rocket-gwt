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
package rocket.logging.rebind;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.logging.client.LoggingConstants;
import rocket.logging.client.LoggingLevel;
import rocket.logging.rebind.createrootlogger.CreateRootLoggerTemplatedFile;
import rocket.logging.rebind.findlogger.FindLoggerTemplatedFile;
import rocket.logging.util.LoggingFactoryConfig;
import rocket.logging.util.LoggingPropertyReader;
import rocket.logging.util.NoneLoggingFactoryConfig;
import rocket.util.client.Checker;

/**
 * This generator is responsible for providing a class that can answer all
 * requests providing loggers that satisfy the logging properties file.
 * 
 * @author Miroslav Pokorny
 */
public class LoggerFactoryGenerator extends Generator {

	@Override
	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final NewConcreteType loggingFactory = this.subClassLoggingFactory(newTypeName);
		this.overrideLoggingFactoryImplFindLogger(loggingFactory);
		this.overrideLoggingFactoryImplCreateDefaultLogger(loggingFactory);
		return loggingFactory;
	}

	@Override
	protected String getGeneratedTypeNameSuffix() {
		// return Constants.GENERATED_TYPE_SUFFIX;
		return "_" + this.getGeneratorContext().getProperty("rocket.logging.Logging.enable");
	}

	/**
	 * Creates a new type that will form the basis of the implemented
	 * LoggingFactory
	 * 
	 * @param newTypeName
	 *            Its new name
	 * @return The new concrete type
	 */
	protected NewConcreteType subClassLoggingFactory(final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Creating type that sub classes LoggingFactoryImpl");

		final NewConcreteType newType = context.newConcreteType(newTypeName);
		newType.setAbstract(false);
		newType.setFinal(true);
		newType.setSuperType(this.getLoggerFactoryImpl());
		newType.setVisibility(Visibility.PUBLIC);

		context.unbranch();

		return newType;
	}

	protected Type getLoggerFactoryImpl() {
		return this.getGeneratorContext().getType(Constants.LOGGER_FACTORY_IMPL);
	}

	/**
	 * Overrides the findLogger method which will eventually contain a switch
	 * statement that returns
	 * 
	 * @param loggerFactory
	 *            The type being assembled.
	 */
	protected void overrideLoggingFactoryImplFindLogger(final NewConcreteType loggerFactory) {
		Checker.notNull("parameter:loggerFactory", loggerFactory);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Overriding " + Constants.FIND_LOGGER_METHOD);

		final List findLoggerMethodArguments = Collections.nCopies(1, context.getString());
		final Method method = loggerFactory.findMostDerivedMethod(Constants.FIND_LOGGER_METHOD, findLoggerMethodArguments);

		final NewMethod newMethod = method.copy(loggerFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final FindLoggerTemplatedFile template = new FindLoggerTemplatedFile();
		newMethod.setBody(template);

		final LoggingFactoryConfig config = this.getLoggingFactoryConfig();
		context.debug("Config: " + config);

		final Iterator names = config.getNames();
		while (names.hasNext()) {
			final String name = (String) names.next();
			final String loggerTypeName = config.getTypeName(name);
			final LoggingLevel loggingLevel = config.getLoggingLevel(name);

			context.debug(name + "=" + loggerTypeName + " (" + loggingLevel + ")");

			final Constructor loggingLevelLogger = this.getConstructorForLoggingLevel(loggingLevel);
			final Constructor logger = this.getTargetLoggerConstructor(loggerTypeName);

			template.register(name, loggingLevelLogger, logger);
		}

		// rename parameter to a known name which matches the variable named
		// used in templates.
		final NewMethodParameter parameter = (NewMethodParameter) newMethod.getParameters().get(0);
		parameter.setName(Constants.FIND_LOGGER_NAME_PARAMETER);
		parameter.setFinal(true);

		context.unbranch();
	}

	/**
	 * Overrides the createDefaultLogger method which will return the root
	 * logger when a more specific match cannot be found in the accompanying
	 * properties file.
	 * 
	 * @param loggerFactory
	 *            The type being assembled.
	 */
	protected void overrideLoggingFactoryImplCreateDefaultLogger(final NewConcreteType loggerFactory) {
		Checker.notNull("parameter:loggerFactory", loggerFactory);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Overriding " + Constants.CREATE_ROOT_LOGGER_METHOD);

		final List createDefaultLoggerMethodArguments = Collections.nCopies(1, context.getString());
		final Method method = loggerFactory.findMostDerivedMethod(Constants.CREATE_ROOT_LOGGER_METHOD,
				createDefaultLoggerMethodArguments);

		final NewMethod newMethod = method.copy(loggerFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final String category = LoggingConstants.ROOT_LOGGER_NAME;
		final LoggingFactoryConfig config = this.getLoggingFactoryConfig();

		final CreateRootLoggerTemplatedFile createLogger = new CreateRootLoggerTemplatedFile();
		newMethod.setBody(createLogger);

		final LoggingLevel loggingLevel = config.getLoggingLevel(category);
		createLogger.setLevelLogger(this.getConstructorForLoggingLevel(loggingLevel));

		final String typeName = config.getTypeName(category);
		createLogger.setLogger(this.getTargetLoggerConstructor(typeName));

		context.debug("Type: " + typeName);

		// rename parameter to a known name.
		final NewMethodParameter parameter = (NewMethodParameter) newMethod.getParameters().get(0);
		parameter.setName(Constants.CREATE_ROOT_NAME_PARAMETER);
		parameter.setFinal(true);

		context.unbranch();
	}

	/**
	 * Returns the appropriate LoggingFactoryConfig depending on whether this
	 * permutation is including or excluding logging statements.
	 * 
	 * @return A LoggingFactoryConfig
	 */
	protected LoggingFactoryConfig getLoggingFactoryConfig() {
		final LoggingPropertyReader reader = new LoggingPropertyReader() {
			@Override
			protected String getPropertyValue() {
				return LoggerFactoryGenerator.this.getGeneratorContext().getProperty(this.getPropertyName());
			}

			@Override
			protected void throwInvalidPropertyValue(final String propertyValue) {
				throw new RuntimeException("Invalid " + this.getPropertyName() + " value of \"" + propertyValue + "\" encountered.");
			}

			@Override
			protected Object handleDisableLoggingValue() {
				return createNoneLoggingFactoryConfig();
			}

			protected NoneLoggingFactoryConfig createNoneLoggingFactoryConfig() {
				return new NoneLoggingFactoryConfig();
			}

			protected Object handleEnableLoggingValue() {
				return this.createPropertiesFileLoggingConfig();
			}
		};

		return (LoggingFactoryConfig) reader.readProperty();
	}

	/**
	 * Fetches the constructor that takes a String argument for the logger class
	 * that matches the given logging level.
	 * 
	 * @param loggingLevel
	 * @return The constructor, will never be null.
	 */
	protected Constructor getConstructorForLoggingLevel(final LoggingLevel loggingLevel) {
		Checker.notNull("parameter:loggingLevel", loggingLevel);

		Type type = null;
		while (true) {
			if (LoggingLevel.DEBUG == loggingLevel) {
				type = this.getDebugLevelLogger();
				break;
			}
			if (LoggingLevel.INFO == loggingLevel) {
				type = this.getInfoLevelLogger();
				break;
			}
			if (LoggingLevel.WARN == loggingLevel) {
				type = this.getWarnLevelLogger();
				break;
			}
			if (LoggingLevel.ERROR == loggingLevel) {
				type = this.getErrorLevelLogger();
				break;
			}
			if (LoggingLevel.FATAL == loggingLevel) {
				type = this.getFatalLevelLogger();
				break;
			}
			if (LoggingLevel.NONE == loggingLevel) {
				type = this.getNoneLevelLogger();
				break;
			}

			throw new LoggerFactoryGeneratorException("Unknown logging level " + loggingLevel);
		}

		// find constructor
		return type.getConstructor(Collections.nCopies(1, this.getLogger()));
	}

	protected Type getDebugLevelLogger() {
		return this.getGeneratorContext().getType(Constants.DEBUG_LEVEL_LOGGER);
	}

	protected Type getInfoLevelLogger() {
		return this.getGeneratorContext().getType(Constants.INFO_LEVEL_LOGGER);
	}

	protected Type getWarnLevelLogger() {
		return this.getGeneratorContext().getType(Constants.WARN_LEVEL_LOGGER);
	}

	protected Type getErrorLevelLogger() {
		return this.getGeneratorContext().getType(Constants.ERROR_LEVEL_LOGGER);
	}

	protected Type getFatalLevelLogger() {
		return this.getGeneratorContext().getType(Constants.FATAL_LEVEL_LOGGER);
	}

	protected Type getNoneLevelLogger() {
		return this.getGeneratorContext().getType(Constants.NONE_LEVEL_LOGGER);
	}

	protected Type getLogger() {
		return this.getGeneratorContext().getType(Constants.LOGGER);
	}

	/**
	 * Finds a string constructor belonging to the given type. It also ensures
	 * that the logger can be instantiated and contains a String parameter.
	 * 
	 * @param typeName
	 *            The name of the target logger.
	 * @return A Constructor that takes a String parameter.
	 */
	protected Constructor getTargetLoggerConstructor(final String typeName) {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug(typeName);

		final Type type = context.getType(typeName);
		context.debug(type.toString());

		// make sure type is really a Logger.
		final Type logger = this.getLogger();
		if (false == type.isAssignableTo(logger)) {
			throwTargetLoggerIsNotALogger(type);
		}
		if (type.isInterface()) {
			throwTargetLoggerIsAnInterface(type);
		}
		if (type.isAbstract()) {
			throwTargetLoggerIsAbstract(type);
		}

		// find a constructor with a string parameter.
		final List stringParameter = Collections.nCopies(1, context.getString());
		final Constructor constructor = type.getConstructor(stringParameter);
		if (constructor.getVisibility() != Visibility.PUBLIC) {
			throwTargetLoggerConstructorIsNotPublic(constructor);
		}

		context.debug("Found " + constructor);
		context.unbranch();

		return constructor;
	}

	protected void throwTargetLoggerIsNotALogger(final Type logger) {
		throw new LoggerFactoryGeneratorException("The logger " + logger.getName() + " doesnt implement " + Constants.LOGGER);
	}

	protected void throwTargetLoggerIsAnInterface(final Type logger) {
		throw new LoggerFactoryGeneratorException("The type " + logger.getName()
				+ " may not be instantiated because it is an interface, type: " + logger);
	}

	protected void throwTargetLoggerIsAbstract(final Type logger) {
		throw new LoggerFactoryGeneratorException("The type " + logger.getName()
				+ " may not be instantiated because it is abstract, type: " + logger);
	}

	protected void throwTargetLoggerConstructorIsNotPublic(final Constructor constructor) {
		throw new LoggerFactoryGeneratorException("The type " + constructor.getEnclosingType().getName()
				+ " does not contain a public constructor, constructor: " + constructor);
	}
}
