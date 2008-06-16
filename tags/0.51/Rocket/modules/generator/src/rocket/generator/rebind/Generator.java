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

import java.io.InputStream;

import rocket.generator.rebind.gwt.TypeOracleGeneratorContext;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

import com.google.gwt.core.ext.TreeLogger;

/**
 * Convenient base class for any generator. It includes code to setup a context,
 * the initial test if a generated type for the given typeName exists etc.
 * 
 * @author Miroslav Pokorny
 */
abstract public class Generator extends com.google.gwt.core.ext.Generator {

	/**
	 * Starts the code generation process including the construction of a new
	 * Context. This method is called by GWT for each deferred binding request.
	 */
	@Override
	public String generate(final TreeLogger logger, final com.google.gwt.core.ext.GeneratorContext generatorContext,
			final String typeName) {
		final GeneratorContext context = this.createGeneratorContext(generatorContext, logger);
		return this.generate(context, typeName);
	}

	/**
	 * This method is called typically from another generator reusing an
	 * existing context.
	 * 
	 * @param context
	 *            An existing context
	 * @param typeName
	 *            The name of the type being rebound.
	 * @return The name of the type just that was generated.
	 */
	public String generate(final GeneratorContext context, final String typeName) {
		try {
			this.setGeneratorContext(context);
			return this.createNewTypeIfNecessary(typeName);
		} finally {
			this.clearGeneratorContext();
		}
	}

	/**
	 * Tests if a Type already exists and if it doesnt invokes
	 * {@link #assembleNewType(String, String)} which will build a
	 * {@link NewConcreteType}
	 * 
	 * @param typeName
	 *            The name of type which will be used to generate a new type.
	 * @return The name of the new type
	 */
	public String createNewTypeIfNecessary(final String typeName) {
		final GeneratorContext context = this.getGeneratorContext();
		context.info("Recieved type \"" + typeName + "\".");

		final String newTypeName = this.getGeneratedTypeName(typeName);
		String bindTypeName = newTypeName;
		final long started = System.currentTimeMillis();

		if (null == context.findType(newTypeName)) {

			try {
				final NewConcreteType newType = this.assembleNewType(typeName, newTypeName);
				if (null != newType) {
					context.branch();
					context.info("Assembling source for new type(s)...");
					newType.write();
					context.unbranch();

					final long now = System.currentTimeMillis();

					context.info("Finished writing new type in " + (now - started) + " millis.");
					bindTypeName = newTypeName;
				} else {
					context.info("Definition previously existed will use that instead..");
					bindTypeName = null;
				}

			} catch (final GeneratorException rethrow) {
				context.error("Problem whilst running generator for type \"" + typeName + "\".", rethrow);
				throw rethrow;

			} catch (final Throwable caught) {
				context.error("Unexpected problem whilst running generator for type \"" + typeName + "\".", caught);
				throw new GeneratorException(caught);
			}
		} else {
			context.info("Skipping generation step, will use existing type instead.");
		}

		context.info("Will bind \"" + typeName + "\" to \"" + bindTypeName + "\".");
		return bindTypeName;
	}

	public String getGeneratedTypeName(final String name) {
		return this.getGeneratorContext().getGeneratedTypeName(name, this.getGeneratedTypeNameSuffix());
	}

	/**
	 * The hardcoded suffix that gets appended to each generated type
	 * 
	 * @return The suffix to appended. This typically starts with an underscore
	 *         or two ...
	 */
	abstract protected String getGeneratedTypeNameSuffix();

	protected NewConcreteType assembleNewType(final String typeName, final String newTypeName) {
		return this.assembleNewType(this.getGeneratorContext().getType(typeName), newTypeName);
	}

	/**
	 * Sub-classes must create a new NewConcreteType, add constructors, methods,
	 * fields etc.
	 * 
	 * @param type
	 * @param newTypeName
	 * @return The new type
	 */
	abstract protected NewConcreteType assembleNewType(final Type type, final String newTypeName);

	private GeneratorContext generatorContext;

	public GeneratorContext getGeneratorContext() {
		Checker.notNull("field:generatorContext", generatorContext);
		return this.generatorContext;
	}

	public void setGeneratorContext(final GeneratorContext generatorContext) {
		Checker.notNull("parameter:generatorContext", generatorContext);
		this.generatorContext = generatorContext;
	}

	protected void clearGeneratorContext() {
		this.generatorContext = null;
	}

	/**
	 * Creates a default GeneratorContext.
	 * 
	 * @return
	 */
	protected GeneratorContext createGeneratorContext(final com.google.gwt.core.ext.GeneratorContext generatorContext,
			final TreeLogger logger) {
		final TypeOracleGeneratorContext context = new TypeOracleGeneratorContext();
		context.setGenerator(this);
		context.setGeneratorContext(generatorContext);
		context.setRootTreeLogger(logger);
		return context;
	}

	/**
	 * Helper which converts a type into a filename that may be loaded from the
	 * classpath.
	 * 
	 * @param type
	 * @param fileExtension
	 * @return
	 */
	public String getResourceName(final Class type, final String fileExtension) {
		Checker.notNull("parameter:type", type);
		Checker.notNull("parameter:fileExtension", fileExtension);

		return this.getResourceName(type.getPackage(), type.getSimpleName() + '.' + fileExtension);
	}

	/**
	 * Helper which takes a package and filename within that package returning a
	 * filename that may be loaded from the classpath.
	 * 
	 * If the fileName is absolute aka starts with a slash the package parameter
	 * is ignored. If the filename is relative the package is used to form an
	 * absolute classpath name.
	 * 
	 * @param javaLangPackage
	 * @param fileName
	 * @return
	 */
	public String getResourceName(final java.lang.Package javaLangPackage, final String fileName) {
		Checker.notNull("parameter:javaLangPackage", javaLangPackage);
		Checker.notNull("parameter:fileName", fileName);

		String resourceName = fileName;
		if (false == fileName.startsWith("/")) {
			resourceName = '/' + javaLangPackage.getName().replace('.', '/') + '/' + fileName;
		}
		return resourceName;
	}

	/**
	 * Helper which converts a type into a filename that may be loaded from the
	 * classpath.
	 * 
	 * @param type
	 * @param fileExtension
	 * @return The resource name as a fully qualified class name (with dots
	 *         replaced by slashes).
	 */
	public String getResourceName(final Type type, final String fileExtension) {
		Checker.notNull("parameter:type", type);
		Checker.notNull("parameter:fileExtension", fileExtension);

		return this.getResourceName(type.getPackage(), type.getSimpleName() + '.' + fileExtension);
	}

	/**
	 * Helper which takes a package and filename within that package returning a
	 * filename that may be loaded from the classpath. If filename is absolute
	 * aka starts with a slash the package is ignored when forming the absolute
	 * classpath resource name. If the filename is relative the package is used
	 * to form an absolute classpath name.
	 * 
	 * @param packagee
	 * @param fileName
	 * @return The resource name as a fully qualified class name (with dots
	 *         replaced by slashes).
	 */
	public String getResourceName(final Package packagee, final String fileName) {
		Checker.notNull("parameter:package", packagee);
		Checker.notNull("parameter:fileName", fileName);

		String resourceName = fileName;
		if (false == fileName.startsWith("/")) {
			resourceName = '/' + packagee.getName().replace('.', '/') + '/' + fileName;
		}
		return resourceName;
	}

	public String getResourceNameFromGeneratorPackage(final String fileName) {
		return this.getResourceName(this.getClass().getPackage(), fileName);
	}

	/**
	 * Retrieves a resource by name. If the resource is not located on as a
	 * classpath resource an exception will be thrown.
	 * 
	 * @param resourceName
	 * @return The InputStream for the given resourceName.
	 */
	public InputStream getResource(final String resourceName) {
		Checker.notNull("parameter:resourceName", resourceName);

		final InputStream inputStream = Object.class.getResourceAsStream(resourceName);
		if (null == inputStream) {
			this.throwUnableToLoadResource(resourceName);
		}
		return inputStream;
	}

	protected void throwUnableToLoadResource(final String resourceName) {
		throw new GeneratorException("Unable to load resource with a filename \"" + resourceName + "\".");
	}
}
