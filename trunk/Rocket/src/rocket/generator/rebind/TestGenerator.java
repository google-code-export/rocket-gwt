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
import java.io.StringWriter;

import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;

/**
 * This class is part of a package of several classes that assist with
 * processing exceptions thrown by a generator in a deferred manner.
 * 
 * <ul>
 * <li>Gwt test cases must extend GeneratorGwtTestCase</li>
 * <li>GWT.create() must be surrounded by a call to
 * GeneratorGwtTestCase.assertBindingFailed()</li>
 * <li>must catch FailedGenerateAttemptException and process accordingly.</li>
 * </ul>
 * 
 * <pre>
 * public void testXXX() {
 * 	try {
 * 		Object object = assertBindingFailed(GWT.create(YYY.class));
 * 	} catch (FailedGenerateAttemptException expected) {
 * 		// test causeType etc...
 * 	}
 * }
 * </pre>
 * 
 * @author Miroslav Pokorny
 */
abstract public class TestGenerator extends Generator {

	final public String generate(final TreeLogger logger, final com.google.gwt.core.ext.GeneratorContext generatorContext,
			final String typeName) throws UnableToCompleteException {

		try {
			return this.createGenerator().generate(logger, generatorContext, typeName);
		} catch (final Throwable cause) {
			return generateFailedGenerateAttempt(logger, generatorContext, typeName, cause);
		}
	}

	abstract protected Generator createGenerator();

	/**
	 * Generates a dummy implementation.
	 * 
	 * @param logger
	 * @param generatorContext
	 * @param typeName
	 * @param cause
	 * @return
	 */
	public String generateFailedGenerateAttempt(final TreeLogger logger, final com.google.gwt.core.ext.GeneratorContext generatorContext,
			final String typeName, final Throwable cause) {

		final GeneratorContextImpl context = new GeneratorContextImpl(){
			protected Type createArrayType(String name) {
				throw new UnsupportedOperationException();
			}

			protected Type createClassType(String name) {
				throw new UnsupportedOperationException();
			}

			protected Package createPackage(String name) {
				throw new UnsupportedOperationException();
			}

			protected void preloadTypes() {
			}
		};
		context.setGeneratorContext(generatorContext);
		context.setLogger(logger);

		final String generatedClassName = context.getGeneratedTypeName(typeName, this.getGeneratedTypeNameSuffix() );
		final PrintWriter printWriter = context.tryCreateTypePrintWriter(generatedClassName);
		if (printWriter != null) {

			final String packageName = context.getPackageName(generatedClassName);
			final String simpleClassName = context.getSimpleClassName(generatedClassName);
			final ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, simpleClassName);
			composerFactory.setSuperclass(FailedGenerateAttemptException.class.getName());

			final SourceWriter sourceWriter = context.createSourceWriter(composerFactory, printWriter);

			Throwable cause0 = cause;
			while (true) {
				final Throwable cause1 = cause0.getCause();
				if (null == cause1) {
					break;
				}
				cause0 = cause1;
			}

			this.writeGetMessage(sourceWriter, typeName, cause0);
			this.writeGetCauseType(sourceWriter, cause0);
			this.writeGetCauseStackTrace(sourceWriter, cause0);

			sourceWriter.commit();
		}

		return generatedClassName;
	}

	protected String getGeneratedTypeNameSuffix() {
		return "__" + FailedGenerateAttemptException.class.getName().replace('.', '_');
	}
	
	protected void writeGetMessage(final SourceWriter writer, final String typeName, final Throwable cause) {
		writer.println("public String getMessage(){");
		writer.indent();
		writer
				.println("return \"Attempt to generate for [" + typeName + "] failed because " + Generator.escape(cause.getMessage())
						+ "\";");
		writer.outdent();
		writer.println("}");
	}

	protected void writeGetCauseType(final SourceWriter writer, final Throwable cause) {
		writer.println("public String getCauseType(){");
		writer.indent();
		writer.println("return \"" + cause.getClass().getName() + "\";");
		writer.outdent();
		writer.println("}");
	}

	protected void writeGetCauseStackTrace(final SourceWriter writer, final Throwable cause) {
		writer.println("public String getCauseStackTrace(){");
		writer.indent();
		final String stackTraceDump = this.getStackTraceAsString(cause);
		writer.println("return \"" + Generator.escape(stackTraceDump) + "\";");
		writer.outdent();
		writer.println("}");
	}

	/**
	 * GeneratorHelper which captures the stacktrace of a throwable as a String.
	 * 
	 * @param throwable
	 * @return
	 */
	protected String getStackTraceAsString(final Throwable throwable) {
		ObjectHelper.checkNotNull("parameter:throwable ", throwable);

		final StringWriter stringWriter = new StringWriter();
		// final PrintWriter printWriter = new PrintWriter(stringWriter);
		// final PrintStream printWriter = new PrintStream(stringWriter);
		// throwable.printStackTrace(printWriter);
		// printWriter.flush();
		// printWriter.close();
		// FIXME
		return stringWriter.toString();
	}

}
