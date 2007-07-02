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

package rocket.beans.rebind;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import rocket.beans.client.BeanFactory;
import rocket.beans.rebind.config.SaxHandler;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * This code generator generates a BeanFactory which will create or provide the
 * beans defined in an xml file.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGenerator extends Generator {

	/**
	 * Begins the code generation process that will eventually guarantee that a
	 * BeanFactory exists for the given typeName.
	 * 
	 * @see com.google.gwt.core.ext.Generator#generate(com.google.gwt.core.ext.TreeLogger,
	 *      com.google.gwt.core.ext.GeneratorContext, java.lang.String)
	 */
	public String generate(final TreeLogger logger, final com.google.gwt.core.ext.GeneratorContext generatorContext, final String typeName)
			throws UnableToCompleteException {
		logger.log(TreeLogger.INFO, "generating stub for " + typeName, null);

		final BeanFactoryGeneratorContext context = new BeanFactoryGeneratorContext();
		context.setGeneratorContext(generatorContext);
		context.setLogger(logger);

		try {
			// make sure is an interface
			final JClassType type = context.findType(typeName);
			if (null == type.isInterface()) {
				throwNotABeanFactoryException("The type passed is not an interface " + type.getQualifiedSourceName());
			}
			if (false == type.isAssignableTo(context.getBeanFactoryType())) {
				throwNotABeanFactoryException("The interface type [" + typeName + "] doesnt implement BeanFactory "
						+ BeanFactory.class.getName());
			}

			final SaxHandler saxHandler = this.createSaxHandler();
			saxHandler.setBeanFactoryGeneratorContext(context);

			final InputStream xmlFile = context.getResource(typeName);
			this.readXml(saxHandler, xmlFile);

			final BeanFactoryImplGenerator writer = this.createBeanFactoryWriter();
			writer.setBeanFactoryGeneratorContext(context);
			writer.setType(type);
			return writer.generate();

		} catch (final BeanFactoryGeneratorException rethrow) {
			rethrow.printStackTrace();
			logger.log(TreeLogger.ERROR, rethrow.getMessage(), rethrow);
			throw rethrow;
		} catch (final Throwable caught) {
			caught.printStackTrace();// FIXME remove

			logger.log(TreeLogger.ERROR, caught.getMessage(), caught);
			throw new UnableToCompleteException();
		}
	}

	protected void throwNotABeanFactoryException(final String message) {
		throw new NotABeanFactoryException(message);
	}

	/**
	 * Automates the process of creating a parser and parsing the content of the
	 * given file. A variety of properties will be set during the parsing
	 * process.
	 * 
	 * @param saxHandler;
	 * @param xmlFile
	 * @throws BeanFactoryGeneratorException
	 */
	public void readXml(final SaxHandler saxHandler, final InputStream xmlFile) throws BeanFactoryGeneratorException {
		try {
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);
			final XMLReader parser = factory.newSAXParser().getXMLReader();

			parser.setDTDHandler(saxHandler);
			parser.setErrorHandler(saxHandler);
			parser.setContentHandler(saxHandler);
			parser.setEntityResolver(saxHandler);

			parser.parse(new InputSource(xmlFile));
		} catch ( final BeanFactoryGeneratorException caught ){
			throw caught;
		} catch (final IOException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage(), caught);
		} catch (final ParserConfigurationException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage(), caught);
		} catch (final SAXException caught) {
			
			Throwable cause = caught;
			while( true ){
				final Throwable cause0 = cause.getCause();
				if( null == cause0 ){
					break;
				}
				cause = cause0;
			}
			
			if (cause instanceof BeanFactoryGeneratorException) {
				throw (BeanFactoryGeneratorException) cause;
			}
			throw new BeanFactoryGeneratorException( cause.getClass().getName() + " message[" + cause.getMessage()+ "]", cause);
		}
	}

	/**
	 * Factory method which creates a SaxHandler which can be used to parse any
	 * bean factory xml file.
	 * 
	 * @return
	 */
	protected SaxHandler createSaxHandler() {
		return new SaxHandler();
	}

	/**
	 * Factory method which creates a new BeanFactoryWriter
	 * 
	 * @return
	 */
	protected BeanFactoryImplGenerator createBeanFactoryWriter() {
		return new BeanFactoryImplGenerator();
	}
}
