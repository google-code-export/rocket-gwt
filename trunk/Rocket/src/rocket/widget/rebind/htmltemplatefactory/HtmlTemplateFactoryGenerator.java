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
package rocket.widget.rebind.htmltemplatefactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.VirtualMethodVisitor;
import rocket.util.client.Checker;
import rocket.util.client.Tester;
import rocket.widget.rebind.htmltemplatefactory.bindwidget.BindWidgetTemplatedFile;
import rocket.widget.rebind.htmltemplatefactory.createhtmlfromtemplate.CreateHtmlFromTemplateTemplatedFile;

/**
 * This generator creates a new factory that binds elements identified by id
 * with widget instances.
 * 
 * The developer needs to define an interface with one or more getters. Each
 * getter must have a subclass of widget as its return type. The method must
 * also declare no parameters. Using deferred binding this generator will create
 * the implementation.
 * 
 * @author Miroslav Pokorny
 */
public class HtmlTemplateFactoryGenerator extends Generator {

	protected NewConcreteType assembleNewType(final Type interfacee, final String newTypeName) {
		this.verifyImplementsHtmlTemplate(interfacee);

		final NewConcreteType newType = this.getGeneratorContext().newConcreteType( newTypeName );		
		newType.setAbstract(false);
		newType.setFinal(true);
		newType.setSuperType(this.getHtmlTemplateFactoryImpl());
		newType.addInterface(interfacee);

		this.implementInterfaceMethods(interfacee, newType);

		return newType;
	}

	/**
	 * Verifies that the given interface type implements HtmlTemplateFactory
	 * throwing an exception if it doesnt.
	 * 
	 * @param interfacee
	 *            The type being tested.
	 */
	protected void verifyImplementsHtmlTemplate(final Type interfacee) {
		if (false == interfacee.isAssignableTo(this.getHtmlTemplateFactory())) {
			throwDoesntImplementHtmlTemplate(interfacee);
		}
	}

	protected void throwDoesntImplementHtmlTemplate(final Type interfacee) {
		throwException( new HtmlTemplateFactoryGeneratorException("The type " + interfacee + " is not an interface or doesnt implement " + Constants.HTML_TEMPLATE_FACTORY));
	}

	/**
	 * Finds and implements all public methods
	 * 
	 * @param interface
	 *            The interface implemented by the new type
	 * @param newType
	 *            The new type being generated
	 */
	protected void implementInterfaceMethods(final Type interfacee, final NewConcreteType newType) {
		Checker.notNull("parameter:interface", interfacee);
		Checker.notNull("parameter:newType", newType);

		final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {
			protected boolean visit(final Method method) {
				if (method.getVisibility() == Visibility.PUBLIC) {
					HtmlTemplateFactoryGenerator.this.implementMethod(method, newType);
				}
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};
		visitor.start(interfacee);
	}

	/**
	 * Implements the method giving it a body depending on the method's return
	 * type.
	 * 
	 * @param method
	 *            The method being implemented
	 * @param newType
	 *            The recipient of all new methods.
	 */
	protected void implementMethod(final Method method, final NewConcreteType newType) {
		Checker.notNull("parameter:method", method);
		Checker.notNull("parameter:newType", newType);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info( "Implementing " + method.getName() );
		
		final NewMethod newMethod = method.copy(newType);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		CodeBlock body = null;
		while (true) {
			final Type widgetType = method.getReturnType();

			if (widgetType.equals(this.getTextBox())) {
				body = buildTextBoxGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getTextArea())) {
				body = buildTextAreaGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getRadioButton())) {
				body = buildRadioButtonGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getCheckBox())) {
				body = buildCheckBoxGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getListBox())) {
				body = buildListBoxGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getLabel())) {
				body = buildLabelGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getButton())) {
				body = buildButtonGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getImage())) {
				body = buildImageGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getHyperlink())) {
				body = buildHyperlinkGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getHtml())) {
				body = buildHtmlGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getFormPanel())) {
				body = buildFormPanelGetterMethodBody(method);
				break;
			}

			this.throwUnsupportedWidgetType(method);
			break;
		}
		newMethod.setBody(body);
		
		context.unbranch();
	}


	protected Type getTextBox() {
		return this.getGeneratorContext().getType(Constants.TEXTBOX);
	}

	protected Type getTextArea() {
		return this.getGeneratorContext().getType(Constants.TEXTAREA);
	}

	protected Type getRadioButton() {
		return this.getGeneratorContext().getType(Constants.RADIO_BUTTON);
	}

	protected Type getCheckBox() {
		return this.getGeneratorContext().getType(Constants.CHECKBOX);
	}

	protected Type getListBox() {
		return this.getGeneratorContext().getType(Constants.LISTBOX);
	}

	protected Type getLabel() {
		return this.getGeneratorContext().getType(Constants.LABEL);
	}

	protected Type getButton() {
		return this.getGeneratorContext().getType(Constants.BUTTON);
	}

	protected Type getImage() {
		return this.getGeneratorContext().getType(Constants.IMAGE);
	}

	protected Type getHyperlink() {
		return this.getGeneratorContext().getType(Constants.HYPERLINK);
	}

	protected Type getHtml() {
		return this.getGeneratorContext().getType(Constants.HTML);
	}

	protected Type getFormPanel() {
		return this.getGeneratorContext().getType(Constants.FORM_PANEL);
	}
	
	/**
	 * Builds a codeblock that can be used to bind a textbox to a input element
	 * of type after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildTextBoxGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);
		
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.TEXTBOX );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.TEXTBOX_TEMPLATE, id );
	}

	/**
	 * Builds a codeblock that can be used to bind a password to a input element
	 * of type after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildPasswordTextBoxGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);
		
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.PASSWORD_TEXTBOX );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.PASSWORD_TEXTBOX_TEMPLATE, id );
	}

	/**
	 * Builds a codeblock that can be used to bind a textarea to a textarea
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildTextAreaGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);
		
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.TEXTAREA );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.TEXTAREA_TEMPLATE, id );
	}

	/**
	 * Builds a codeblock that can be used to bind a list box to a select
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildListBoxGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);
		
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.LISTBOX );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.LISTBOX_TEMPLATE, id );
	}

	/**
	 * Builds a codeblock that can be used to bind a check box to an input
	 * element of type radio after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildCheckBoxGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);
		
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.CHECKBOX );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.CHECKBOX_TEMPLATE, id );
	}

	/**
	 * Builds a codeblock that can be used to bind a radio button to a input
	 * element of type radio after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildRadioButtonGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);
		
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.RADIO_BUTTON );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.RADIO_BUTTON_TEMPLATE, id );
	}

	/**
	 * Builds a codeblock that can be used to bind a label (a div element) to an
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildLabelGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);
		
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.LABEL );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.LABEL_TEMPLATE, id );
	}

	/**
	 * Builds a codeblock that can be used to bind a button to a button element
	 * after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildButtonGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);
		
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.BUTTON );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.BUTTON_TEMPLATE, id );
	}

	/**
	 * Builds a codeblock that can be used to bind an Image to an img element
	 * after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildImageGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);
		
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.IMAGE );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.IMAGE_TEMPLATE, id );
	}

	/**
	 * Builds a codeblock that can be used to bind an Hyperlink to a anchor
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildHyperlinkGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);
		
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.HYPERLINK );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.HYPERLINK_TEMPLATE, id );
	}

	/**
	 * Builds a codeblock that can be used to bind an FormPanel to a form
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildFormPanelGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		this.checkMethodHasNoParameters(method);			
		final String id = this.getId(method);
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.FORM_PANEL );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.FORM_TEMPLATE, id );
	}
	
	/**
	 * Builds a codeblock that can be used to bind a html (a div element) to an
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildHtmlGetterMethodBody(final Method method) {
		Checker.notNull("parameter:method", method);

		CodeBlock body = null;
		
		// need to test if Html is coming from id or file.
		while( true ){
			String file = null;
			String id = null;
			try{
				file = this.getFile( method );
			} catch ( final Exception ignored ){
			}
			try{
				id = this.getId( method );
			} catch ( final Exception ignored ){
			}
					
			if( null != file && null != id ){
				throwMethodReturningHtmlHasBothFileAndIdAnnotations( method );
				break;
			}
			if( null != id ){
				body = this.buildHtmlGetHtmlFromId( id );
				break;
			} 
			if( null != file ){
				body = this.buildHtmlGetterMethodBodyFromATemplate( file, method.getEnclosingType() );
				break;
			}
			throwMethodReturningHtmlIsMissingBothFileAndIdAnnotations( method );
			break;
		} 
		
		return body;
	}
	
	protected void throwMethodReturningHtmlIsMissingBothFileAndIdAnnotations( final Method method ){
		this.throwException( new HtmlTemplateFactoryGeneratorException( "Unable to find a \"" + Constants.FILE_ANNOTATION + "\" or \"" + Constants.ID_ANNOTATION + "\" annotation upon method \"" + this.toString( method ) + "\"."));
	}
	
	protected void throwMethodReturningHtmlHasBothFileAndIdAnnotations( final Method method ){
		this.throwException( new HtmlTemplateFactoryGeneratorException( "A factory method returning a Html widget cannot have both a \"" + Constants.FILE_ANNOTATION + "\" or \"" + Constants.ID_ANNOTATION + "\" annotation, method \"" + this.toString( method ) + "\"."));
	}
	/**
	 * Helper which locates an File value from an annotation.
	 * 
	 * @param method The interface method
	 * @return The Filename
	 */
	protected String getFile(final Method method) {
		Checker.notNull("parameter:method", method);

		final List values = method.getMetadataValues(Constants.FILE_ANNOTATION);
		if (null == values || values.size() == 0 ) {
			throwUnableToFindFileAnnotation(method);
		}
		if (values.size() != 1) {
			this.throwFileAnnotationHasMoreThanOneValue(method, values);
		}
		return (String) values.get(0);
	}

	protected void throwUnableToFindFileAnnotation(final Method method) {
		throwException( new HtmlTemplateFactoryGeneratorException("Unable to find an \"" + Constants.FILE_ANNOTATION + "\" annotation upon the method \"" + this.toString( method ) + "\"."));
	}

	protected void throwFileAnnotationHasMoreThanOneValue(final Method method, final List values ) {
		throwException( new HtmlTemplateFactoryGeneratorException("The \"" + Constants.FILE_ANNOTATION + "\" annotation has more than one value \"" + values + "\" upon the method \"" + this.toString( method ) + "\"."));
	}
	
	/**
	 * This method creates a CodeBlock which contains the body of a method which when executed returns a Html which hijackes an existing element identified by an id.
	 * @param id
	 * @return
	 */
	protected CodeBlock buildHtmlGetHtmlFromId( final String id ){
		Checker.notEmpty( "parameter:id", id );
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.HTML );
		context.debug( "id=" + id );
		
		return new BindWidgetTemplatedFile( Constants.HTML_TEMPLATE, id );
	}
	
	/**
	 * This method creates a CodeBlock which contains as its body a template which is a combination of html/text with values and java code. 
	 * @param fileName an absolute or relative classpath reference
	 * @return
	 */
	protected CodeBlock buildHtmlGetterMethodBodyFromATemplate( final String fileName, final Type type ){
		Checker.notEmpty( "parameter:fileName", fileName );
		
		final GeneratorContext context = this.getGeneratorContext();
		context.debug( Constants.HTML );
		context.debug( "file=" + fileName );
		
		final String resolvedFileName = this.getResourceName( type.getPackage(), fileName );
		final String fileContents = this.loadFile( resolvedFileName );
		context.debug( "File loaded.");
		
		final String statements = this.convertToJavaStatements(fileContents);
		
		return new CreateHtmlFromTemplateTemplatedFile( statements );
	}
	
	/**
	 * This method locates and locates the entire contents of a file identified by fileName.
	 * @param fileName
	 * @param type
	 * @return
	 */
	protected String loadFile( final String resourceName ){
		Checker.notEmpty( "parameter:resourceName", resourceName );
		final InputStream inputStream = this.getResource( resourceName );
		if( null == inputStream ){
			throwUnableToReadTemplate( resourceName );
		}
		
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try{
			reader = new InputStreamReader( inputStream );	
			writer = new StringWriter();
			final char[] buffer = new char[ 4096 ];
			while( true ){
				final int readCount = reader.read( buffer );
				if( -1 == readCount ){
					break;
				}
				writer.write( buffer, 0, readCount );
			}
		} catch ( final IOException ioException ){
			this.throwUnableToReadTemplate(resourceName);
		}
		
		return writer.toString();
	}
	
	protected void throwUnableToReadTemplate( final String resourceName ){
		throwException( new HtmlTemplateFactoryGeneratorException( "Unable to read the template file \"" + resourceName + "\"."));
	}	
	
	/**
	 * This method is responsible for converting the entire file into a series of java statements. These statements will print html or values
	 * and include java code verbatim.
	 * It also includes as its first statement a local variable which creates a new HtmlWriter instance.
	 * @param template
	 * @return
	 */
	protected String convertToJavaStatements( final String template ){
		Checker.notEmpty("parameter:template", template );
		
		final StringBuffer statements = new StringBuffer();
		
		int pos = 0;
		final int length = template.length();
		
		while( pos < length ){
						
				// try and find a <%
				final int open = template.indexOf( "<%", pos );
				
				int endOfHtml = open;
				if( -1 == open ){
					endOfHtml = length;
				}
				
				// insert a print statement to print the current html block.
				final String html = template.substring( pos, endOfHtml );
				if( false == Tester.isNullOrEmpty( html )){
					statements.append( Constants.HTML_WRITER_PRINT );
					statements.append( "(\"");
					statements.append( Generator.escape( html ));
					statements.append( "\");" + Constants.EOL );
				}
				
				// if theres no more template to process finish.
				if( -1 == open ){
					break;
				}
				
				// advance pos to the character after <%
				pos = open + 2;
				
				// try and find the closing %>
				final int close = template.indexOf( "%>", open );
				
				// if unable to find %> template is incomplete complain
				if( -1 == close ){
					final int blockStartingLineNumber = this.countLines( template, pos );
					this.throwTemplateContainsUnclosedBlock( blockStartingLineNumber );
				}
				
				// get the entire block contents.
				final String block = template.substring( open, close );
				if( block.length() == 0 ){
					final int blockLineNumber = this.countLines( template, pos );
					this.throwTemplateContainsEmptyBlock( blockLineNumber );
				}
				
				// found a value ?
				if( block.charAt( 2 ) == '='){
					int start = 3;
					
					// find the first non whitespace/nl/cr char
					int end = block.length();
					for( int i = 3; i < end; i++ ){
						final char c = block.charAt( i );
						if( Character.isWhitespace( c )){
							continue;
						}
						if( c == '\n'){
							continue;
						}
						if( c == '\r'){
							continue;
						}
						
						start = i;
						break;
					}
					
					while( end > start ){
						end--;
						final char c = block.charAt( end );
						if( Character.isWhitespace( c )){
							continue;
						}
						if( c == '\n'){
							continue;
						}
						if( c == '\r'){
							continue;
						}
						
						// adjust because we dont want $end to be inclusive
						 end++;
						break;						
					}
										
					final String value = block.substring( start, end );					
					
					statements.append( Constants.HTML_WRITER_PRINT );
					statements.append( "(");
					statements.append( value );
					statements.append( ");" );
					statements.append( Constants.EOL );
					// advance pointers...
					pos = close + 2;
					continue;
				}
			
				// insert java code block as is - no attempt is made to parse, let the compiler do that.
				statements.append( block.substring( 2 ));
				statements.append( Constants.EOL );
				pos = close + 2;			
		}		
		
		return statements.toString();
	}
	
	/**
	 * Simply counts the number of lines between the start of the content and the given character position.
	 * @param contents
	 * @param upToCharPosition
	 * @return The number of lines start at 1.
	 */
	protected int countLines( final String contents, final int upToCharPosition ){		
		int lineNumber = 0;
		try{
			final String textBeforeCharPosition = contents.substring( 0, upToCharPosition );
			final BufferedReader reader = new BufferedReader( new StringReader( textBeforeCharPosition ));
			while( true ){
				final String line = reader.readLine();
				if( null == line ){
					break;
				}
				lineNumber++;
			}
		} catch ( final IOException shouldntHappen ){
			throw new RuntimeException( shouldntHappen );
		}
		
		return lineNumber + 1;// the first line is 1 not 0.
	}
	
	protected void throwTemplateContainsUnclosedBlock( final int lineNumber ){
		this.throwException( new HtmlTemplateFactoryGeneratorException("Unable to find the closing %> for a value or java code block starting at line: " + lineNumber ));
	}
	protected void throwTemplateContainsEmptyBlock(final int lineNumber ){
		this.throwException( new HtmlTemplateFactoryGeneratorException("A block within the template is empty on line: " + lineNumber ));
	}
	
	/**
	 * A checker method that verifies that the given method has no parameters.
	 * @param method The method to check
	 */
	protected void checkMethodHasNoParameters( final Method method ){
		if (method.getParameters().size() != 0) {
			throwMethodHasParameters(method);
		} 
	}

	protected void throwMethodHasParameters(final Method method) {
		throwException( new HtmlTemplateFactoryGeneratorException("HtmlTemplateFactory methods such as \"" + this.toString( method ) + "\" must not have any parameters."));
	}

	protected void throwUnsupportedWidgetType(final Method method) {
		throwException( new HtmlTemplateFactoryGeneratorException("The return type of the method \"" + this.toString( method ) + "\" is not of a supported widget type."));
	}

	/**
	 * Helper which locates an id value from an annotation.
	 * 
	 * @param method
	 *            The interface method
	 * @return The id.
	 */
	protected String getId(final Method method) {
		Checker.notNull("parameter:method", method);

		final List values = method.getMetadataValues(Constants.ID_ANNOTATION);
		if (null == values || values.size() == 0 ) {
			throwUnableToFindIdAnnotation(method);
		}
		if (values.size() != 1) {
			this.throwIdAnnotationHasMoreThanOneValue(method, values);
		}
		return (String) values.get(0);
	}

	protected void throwUnableToFindIdAnnotation(final Method method) {
		throwException( new HtmlTemplateFactoryGeneratorException("Unable to find an \"" + Constants.ID_ANNOTATION + "\" annotation upon the method: \"" + this.toString( method) + "\"."));
	}

	protected void throwIdAnnotationHasMoreThanOneValue(final Method method, final List values ) {
		throwException( new HtmlTemplateFactoryGeneratorException("The \"" + Constants.ID_ANNOTATION + "\" annotation has more than one value \"" + values + "\" upon the method: \"" + this.toString( method) + "\"."));
	}
	
	protected String getGeneratedTypeNameSuffix() {
		return Constants.SUFFIX;
	}
	
	protected Type getHtmlTemplateFactory() {
		return this.getGeneratorContext().getType(Constants.HTML_TEMPLATE_FACTORY);
	}

	protected Type getHtmlTemplateFactoryImpl() {
		return this.getGeneratorContext().getType(Constants.HTML_TEMPLATE_FACTORY_IMPL);
	}
	
	protected void throwException( final HtmlTemplateFactoryGeneratorException exception ){
		throw exception;
	}
	
	protected String toString( final Method method ){
		return method.getEnclosingType() + "." + method.getName();
	}
}
