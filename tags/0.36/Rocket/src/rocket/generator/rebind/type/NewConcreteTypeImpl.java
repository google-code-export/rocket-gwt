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
package rocket.generator.rebind.type;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorException;
import rocket.generator.rebind.Visibility;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Base class for any generated concrete type that is not an anonymous inner
 * class.
 * 
 * @author Miroslav Pokorny
 */
public class NewConcreteTypeImpl extends NewConcreteNestedTypeOrInterfaceType implements NewConcreteType {

	public NewConcreteTypeImpl() {
		super();
	}

	/**
	 * Requests this generated type to write out its definition including its
	 * constructors, methods and fields. This operation may only be attempted
	 * once.
	 * 
	 * @param printWriter
	 *            The printwriter returned by
	 *            context.tryCreateTypePrintWriter(packageName,
	 *            simpleClassName);
	 */
	public void write(final PrintWriter printWriter) {
		ObjectHelper.checkNotNull("parameter:printWriter", printWriter);

		final String packageName = this.getPackage().getName();
		final String simpleClassName = this.getSimpleName();

		final ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, simpleClassName);
		this.setSuperClassUponClassSourceFileComposerFactory(composerFactory);
		this.addImplementedInterfacesToClassSourceFileComposerFactory(composerFactory);
		this.setClassJavaDoc(composerFactory);

		final GeneratorContext context = this.getGeneratorContext();
		final SourceWriter writer = context.createSourceWriter(composerFactory, printWriter);

		try {
			this.writeLogger();

			this.writeInitializers(writer);
			this.writeConstructors(writer);
			this.writeFields(writer);
			this.writeMethods(writer);
			this.writeNestedTypes(writer);
		} catch (final GeneratorException caught) {
			this.handleWriteFailure(writer, caught);

			throw caught;
		} finally {
			context.commitWriter(writer);
		}

		// update the subTypes of all superTypes that have just been
		// generated...
		this.updateSuperTypeSubTypes(this);
	}

	/**
	 * Captures the complete stacktrace of the given exception and writes it
	 * within a javadoc comment.
	 * 
	 * @param writer
	 *            The source writer of the file being generated.
	 * @param cause
	 *            The cause must not be null.
	 */
	protected void handleWriteFailure(final SourceWriter writer, final Throwable cause) {
		ObjectHelper.checkNotNull("parameter:writer", writer);
		ObjectHelper.checkNotNull("parameter:cause", cause);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		cause.printStackTrace(printWriter);
		printWriter.flush();
		printWriter.close();

		writer.println();
		writer.beginJavaDocComment();
		writer.println(stringWriter.toString());
		writer.endJavaDocComment();
	}

	protected void writeLogger() {
		this.getGeneratorContext().branch("Writing class: " + this);
	}

	protected void updateSuperTypeSubTypes(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		final Iterator nestedTypes = type.getNestedTypes().iterator();
		while (nestedTypes.hasNext()) {
			this.updateSuperTypeSubTypes((Type) nestedTypes.next());
		}
	}

	public void write(final SourceWriter writer) {
		throw new UnsupportedOperationException();
	}

	/**
	 * GeneratorHelper which sets the super type to the given
	 * ClassSourceFileComposerFactory
	 * 
	 * @param composerFactory
	 */
	protected void setSuperClassUponClassSourceFileComposerFactory(final ClassSourceFileComposerFactory composerFactory) {
		ObjectHelper.checkNotNull("parameter:composerFactory", composerFactory);

		composerFactory.setSuperclass(this.getSuperType().getName());
	}

	/**
	 * GeneratorHelper which adds all implemented interfaces to the given
	 * ClassSourceFileComposerFactory
	 * 
	 * @param composerFactory
	 */
	protected void addImplementedInterfacesToClassSourceFileComposerFactory(final ClassSourceFileComposerFactory composerFactory) {
		ObjectHelper.checkNotNull("parameter:composerFactory", composerFactory);

		final Iterator interfaces = this.getInterfaces().iterator();
		while (interfaces.hasNext()) {
			final Type interfacee = (Type) interfaces.next();
			composerFactory.addImplementedInterface(interfacee.getName());
		}
	}

	/**
	 * Adds a java doc comment that includes a variety of statistics about the
	 * class thats about to be generated.
	 * 
	 * @param composerFactory
	 */
	protected void setClassJavaDoc(final ClassSourceFileComposerFactory composerFactory) {
		ObjectHelper.checkNotNull("parameter:composerFactory", composerFactory);

		final String date = DateFormat.getInstance().format(new Date());
		final String comment = "Generated at " + date + " by " + this.getClass().getName();
		composerFactory.setJavaDocCommentForClass(comment);
	}
}