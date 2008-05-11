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
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.gwt.TypeOracleGeneratorContext;
import rocket.generator.rebind.metadata.MetaData;
import rocket.generator.rebind.util.StringBufferSourceWriter;
import rocket.util.client.Checker;
import rocket.util.client.Tester;

import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;

abstract class NewConcreteOrInterfaceType extends NewConcreteNestedTypeOrInterfaceType{
	
	public NewConcreteOrInterfaceType(){
		super();
	}
	
	/**
	 * NewConcrete and NewInterfaceTypeImpls must be written using the GWT way using a ComposerFactory etc.
	 */
	public void write(final SourceWriter writer) {
		throw new UnsupportedOperationException();
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
	public void write() {
		final String packageName = this.getPackage().getName();
		final String simpleClassName = this.getSimpleName();

		final ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, simpleClassName);
		if( this.isInterface() ){
			composerFactory.makeInterface();
		}
		
		this.setSuperClassUponClassSourceFileComposerFactory(composerFactory);
		this.addImplementedInterfacesToClassSourceFileComposerFactory(composerFactory);
		this.setClassJavaDoc(composerFactory);

		final TypeOracleGeneratorContext context = (TypeOracleGeneratorContext)this.getGeneratorContext();
		final PrintWriter printWriter = this.getPrintWriter();
		final SourceWriter writer = context.createSourceWriter(composerFactory, printWriter);

		try {
			context.branch();
			this.log();

			this.writeInitializers(writer);
			this.writeConstructors(writer);
			this.writeFields(writer);
			this.writeMethods(writer);
			this.writeNestedTypes(writer);
			context.unbranch();
			
		} catch (final GeneratorException caught) {
			this.handleWriteFailure(writer, caught);

			throw caught;
		} finally {
			writer.commit();
		}
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
		Checker.notNull("parameter:writer", writer);
		Checker.notNull("parameter:cause", cause);

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

	protected void log() {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info(this.getVisibility().getName() + ( this.isInterface() ? " class " : " interface " ) + this.getName() );
		
		this.logSuperType();
		this.logImplementedInterfaces();
		
		context.unbranch();
	}

	/**
	 * GeneratorHelper which sets the super type to the given
	 * ClassSourceFileComposerFactory
	 * 
	 * @param composerFactory
	 */
	protected void setSuperClassUponClassSourceFileComposerFactory(final ClassSourceFileComposerFactory composerFactory) {
		Checker.notNull("parameter:composerFactory", composerFactory);

		composerFactory.setSuperclass(this.getSuperType().getName());
	}

	/**
	 * GeneratorHelper which adds all implemented interfaces to the given
	 * ClassSourceFileComposerFactory
	 * 
	 * @param composerFactory
	 */
	protected void addImplementedInterfacesToClassSourceFileComposerFactory(final ClassSourceFileComposerFactory composerFactory) {
		Checker.notNull("parameter:composerFactory", composerFactory);

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
		Checker.notNull("parameter:composerFactory", composerFactory);

		String comments = this.getComments();
		final String date = DateFormat.getInstance().format(new Date());
		final String generatorName = this.getGeneratorContext().getGenerator().getClass().getName();
		comments = comments + "\n\nGenerated at " + date + " by " + generatorName;

		final MetaData metaData = this.getMetaData();

		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		while( true ){
			final boolean noComments = Tester.isNullOrEmpty(comments);			
			final boolean noAnnotations = metaData.isEmpty();
			
			if( noComments && noAnnotations ){
				break;
			}
			
			// only has annotations...
			if( noComments && false == noAnnotations ){
				metaData.write(writer);
				break;
			}
			//only has comments...
			if( noComments && false == noAnnotations ){
				writer.println( comments );
				break;
			}
			
			// must have both annotations and comments...
				writer.println( comments );
				writer.println();
				metaData.write(writer);
				break;
		}			

		composerFactory.setJavaDocCommentForClass(writer.getBuffer().toString());
	}
	
	/**
	 * The name of the type being generated.
	 */
	private String name;

	public String getName() {
		GeneratorHelper.checkJavaTypeName("field:name", name);
		return name;
	}

	public boolean hasName() {
		return this.name != null;
	}

	public void setName(final String name) {		
		GeneratorHelper.checkJavaTypeName("parameter:name", name);
		this.name = name;
	}
	
	private PrintWriter printWriter;
	
	protected PrintWriter getPrintWriter(){
		Checker.notNull("field:printWriter", printWriter );
		return this.printWriter;
	}
	
	public void setPrintWriter( final PrintWriter printWriter ){
		Checker.notNull("parameter:printWriter", printWriter );
		this.printWriter = printWriter;
	}
	
	public String toString(){
		return super.toString() + ' ' + this.getName();
	}
}
