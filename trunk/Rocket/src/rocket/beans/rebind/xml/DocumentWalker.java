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
package rocket.beans.rebind.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import rocket.beans.rebind.BeanFactoryGeneratorException;
import rocket.beans.rebind.placeholder.PlaceHolderResolver;
import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;

/**
 * This factory is includes a number of methods to assist with walking through a
 * xml document
 * 
 * @author Miroslav Pokorny
 */
public class DocumentWalker {

	/**
	 * Initializes this document walker so that it may be used to travel about the dom.
	 * 
	 * @param fileName The name of the first xml document to be processed.
	 */
	public void process(final String fileName) {
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);

			final DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(this.getErrorHandler());
			builder.setEntityResolver(this.getEntityResolver());

			this.setAdvices(new ArrayList());
			this.setBeans(new ArrayList());
			this.setAliases(new ArrayList());
			this.setJsonRpcServices(new ArrayList());
			this.setJavaRpcServices(new ArrayList());

			this.setIncludedFiles(new HashSet());

			this.processDocument(builder, fileName);

		} catch (final ParserConfigurationException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst preparing to read the file \"" + fileName + "\".",
					caught);
		}
	}

	protected void processDocument(final DocumentBuilder builder, final String fileName) {
		try {			
			this.processDocument0(builder, fileName);
		} catch (final SAXParseException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst parsing the xml file \"" + fileName + "\" at line: "
					+ caught.getLineNumber() + ", column: " + caught.getColumnNumber(), caught);
		} catch (final SAXException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst parsing the xml file \"" + fileName + "\".", caught);
		} catch (final IOException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst reading the file \"" + fileName + "\".", caught);
		} catch (final RuntimeException caught) {
			throw caught;
		}
	}

	protected void processDocument0(final DocumentBuilder builder, final String fileName) throws SAXException, IOException {
		ObjectHelper.checkNotNull("parameter:builder", builder);
		StringHelper.checkNotEmpty("parameter:fileName", fileName);
		if (fileName.charAt(0) != '/') {
			SystemHelper.fail("parameter:file", "Only classpath absolute and not relative fileName paths may be passed, fileName \""
					+ fileName + "\".");
		}

		final Set includedFiles = this.getIncludedFiles();
		if (includedFiles.contains(fileName)) {
			throwIncludedFileCycle(fileName);
		}
		includedFiles.add(fileName);

		final Generator generator = this.getGenerator();
		final GeneratorContext context = generator.getGeneratorContext();
		context.debug( fileName );

		final InputStream inputStream = generator.getResource(fileName);
		final Document document = builder.parse(inputStream);

		// process the local tags
		final PlaceHolderResolver placeHolderResolver = this.loadPlaceholderFiles(document);
		final List included = this.findIncludedFiles(document, fileName, placeHolderResolver);

		this.getBeans().addAll(this.findBeans(document, fileName, placeHolderResolver));
		this.getAliases().addAll(this.findAliases(document, fileName, placeHolderResolver));
		this.getJsonRpcServices().addAll(this.findJsonRpcServices(document, fileName, placeHolderResolver));
		this.getJavaRpcServices().addAll(this.findJavaRpcServices(document, fileName, placeHolderResolver));
		this.getAdvices().addAll(this.findAdvices(document, fileName, placeHolderResolver));

		// now include the included files...
		final Iterator includedFilesIterator = included.iterator();
		while (includedFilesIterator.hasNext()) {
			final IncludeTag includeFile = (IncludeTag) includedFilesIterator.next();
			final String includedFileFileName = includeFile.getFile();

			this.processDocument(builder, includedFileFileName);
		}
	}

	protected void throwIncludedFileCycle(final String fileName) {
		throw new BeanFactoryGeneratorException("The file \"" + fileName + "\" has previously been included causing a cycle.");
	}

	protected PlaceHolderResolver loadPlaceholderFiles(final Document document) {
		final PlaceHolderResolver placeHolderResolver = new PlaceHolderResolver();

		final NodeList tags = document.getElementsByTagName(Constants.PLACE_HOLDERS_TAG);
		final int count = tags.getLength();
		for (int i = 0; i < count; i++) {
			final Element element = (Element) tags.item(i);
			final String fileName = element.getAttribute(Constants.PLACE_HOLDERS_FILE_ATTRIBUTE);
			if (StringHelper.isNullOrEmpty(fileName)) {
				continue;
			}

			placeHolderResolver.load(fileName);
		}

		return placeHolderResolver;
	}

	/**
	 * Builds a list containing BeanTags for each and every BEAN element within the given document.
	 * @param document The source document.
	 * @param filename The filename is used to construct any exception messages
	 * @param placeHolderResolver 
	 * @return A list of beans
	 */
	protected List findBeans(final Document document, final String filename, final PlaceHolderResolver placeHolderResolver) {
		ObjectHelper.checkNotNull("parameter:document", document);
		StringHelper.checkNotEmpty("parameter:filename", filename);
		ObjectHelper.checkNotNull("parameter:placeHolderResolver", placeHolderResolver);

		final NodeList nodeList = document.getElementsByTagName(Constants.BEAN_TAG);
		final int count = nodeList.getLength();
		
		final List beans = new ArrayList();
		for( int i = 0; i < count; i++  ){
			final BeanTag bean = new BeanTag();
			bean.setElement((Element) nodeList.item(i));
			bean.setFilename(filename);
			bean.setPlaceHolderResolver(placeHolderResolver);
			
			beans.add( bean );				
		}		
		
		return Collections.unmodifiableList(beans);
	}
	
	protected List findAliases(final Document document, final String filename, final PlaceHolderResolver placeHolderResolver) {
		ObjectHelper.checkNotNull("parameter:document", document);
		StringHelper.checkNotEmpty("parameter:filename", filename);
		ObjectHelper.checkNotNull("parameter:placeHolderResolver", placeHolderResolver);

		final NodeList nodeList = document.getElementsByTagName(Constants.ALIAS_TAG);
		final int count = nodeList.getLength();
		
		final List aliases = new ArrayList();
		for( int i = 0; i < count; i++  ){
			final AliasTag alias = new AliasTag();
			alias.setElement((Element) nodeList.item(i));
			alias.setFilename(filename);
			alias.setPlaceHolderResolver(placeHolderResolver);
			
			aliases.add( alias );				
		}		
		
		return Collections.unmodifiableList(aliases);
	}

	protected List findJsonRpcServices(final Document document, final String filename, final PlaceHolderResolver placeHolderResolver) {
		ObjectHelper.checkNotNull("parameter:document", document);
		StringHelper.checkNotEmpty("parameter:filename", filename);
		ObjectHelper.checkNotNull("parameter:placeHolderResolver", placeHolderResolver);

		final NodeList nodeList = document.getElementsByTagName(Constants.REMOTE_JSON_SERVICE_TAG);
		final int count = nodeList.getLength();
		
		final List jsonServices = new ArrayList();
		for( int i = 0; i < count; i++  ){
			final RemoteJsonServiceTag jsonService = new RemoteJsonServiceTag();
			jsonService.setElement((Element) nodeList.item(i));
			jsonService.setFilename(filename);
			jsonService.setPlaceHolderResolver(placeHolderResolver);
			
			jsonServices.add( jsonService );				
		}		
		
		return Collections.unmodifiableList(jsonServices);
	}

	protected List findJavaRpcServices(final Document document, final String filename, final PlaceHolderResolver placeHolderResolver) {
		ObjectHelper.checkNotNull("parameter:document", document);
		StringHelper.checkNotEmpty("parameter:filename", filename);
		ObjectHelper.checkNotNull("parameter:placeHolderResolver", placeHolderResolver);

		final NodeList nodeList = document.getElementsByTagName(Constants.REMOTE_RPC_SERVICE_TAG);
		final int count = nodeList.getLength();
		
		final List rpcServices = new ArrayList();
		for( int i = 0; i < count; i++  ){
			final RemoteRpcServiceTag rpcService = new RemoteRpcServiceTag();
			rpcService.setElement((Element) nodeList.item(i));
			rpcService.setFilename(filename);
			rpcService.setPlaceHolderResolver(placeHolderResolver);
			
			rpcServices.add( rpcService );				
		}		
		
		return Collections.unmodifiableList(rpcServices);
	}

	protected List findAdvices(final Document document, final String filename, final PlaceHolderResolver placeHolderResolver) {
		ObjectHelper.checkNotNull("parameter:document", document);
		StringHelper.checkNotEmpty("parameter:filename", filename);
		ObjectHelper.checkNotNull("parameter:placeHolderResolver", placeHolderResolver);

		final NodeList nodeList = document.getElementsByTagName(Constants.ADVICE_TAG);
		final int count = nodeList.getLength();
		
		final List advices = new ArrayList();
		for( int i = 0; i < count; i++  ){
			final AdviceTag advice = new AdviceTag();
			advice.setElement((Element) nodeList.item(i));
			advice.setFilename(filename);
			advice.setPlaceHolderResolver(placeHolderResolver);
			
			advices.add( advice );				
		}		
		
		return Collections.unmodifiableList(advices);
	}

	protected List findIncludedFiles(final Document document, final String filename, final PlaceHolderResolver placeHolderResolver) {
		ObjectHelper.checkNotNull("parameter:document", document);
		StringHelper.checkNotEmpty("parameter:filename", filename);
		ObjectHelper.checkNotNull("parameter:placeHolderResolver", placeHolderResolver);

		final NodeList nodeList = document.getElementsByTagName(Constants.INCLUDE_TAG);
		final int count = nodeList.getLength();
		
		final List includedFiles = new ArrayList();
		for( int i = 0; i < count; i++  ){
			final IncludeTag includedFile = new IncludeTag();
			includedFile.setElement((Element) nodeList.item(i));
			includedFile.setFilename(filename);
			includedFile.setPlaceHolderResolver(placeHolderResolver);
			
			includedFiles.add( includedFile );				
		}		
		
		return Collections.unmodifiableList( includedFiles );
	}

	private EntityResolver entityResolver;

	protected EntityResolver getEntityResolver() {
		ObjectHelper.checkNotNull("field:entityResolver", entityResolver);
		return entityResolver;
	}

	public void setEntityResolver(final EntityResolver entityResolver) {
		ObjectHelper.checkNotNull("parameter:entityResolver", entityResolver);
		this.entityResolver = entityResolver;
	}

	private ErrorHandler errorHandler;

	protected ErrorHandler getErrorHandler() {
		ObjectHelper.checkNotNull("field:errorHandler", errorHandler);
		return errorHandler;
	}

	public void setErrorHandler(final ErrorHandler errorHandler) {
		ObjectHelper.checkNotNull("parameter:errorHandler", errorHandler);
		this.errorHandler = errorHandler;
	}

	private Generator generator;

	protected Generator getGenerator() {
		ObjectHelper.checkNotNull("field:generator", generator);
		return this.generator;
	}

	public void setGenerator(final Generator generator) {
		ObjectHelper.checkNotNull("parameter:generator", generator);
		this.generator = generator;
	}

	private List beans;

	public List getBeans() {
		ObjectHelper.checkNotNull("field:beans", beans);
		return this.beans;
	}

	protected void setBeans(final List beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);
		this.beans = beans;
	}

	private List jsonRpcServices;

	public List getJsonRpcServices() {
		ObjectHelper.checkNotNull("field:jsonRpcServices", jsonRpcServices);
		return this.jsonRpcServices;
	}

	protected void setJsonRpcServices(final List jsonRpcServices) {
		ObjectHelper.checkNotNull("parameter:jsonRpcServices", jsonRpcServices);
		this.jsonRpcServices = jsonRpcServices;
	}

	private List javaRpcServices;

	public List getJavaRpcServices() {
		ObjectHelper.checkNotNull("field:javaRpcServices", javaRpcServices);
		return this.javaRpcServices;
	}

	protected void setJavaRpcServices(final List javaRpcServices) {
		ObjectHelper.checkNotNull("parameter:javaRpcServices", javaRpcServices);
		this.javaRpcServices = javaRpcServices;
	}

	private List advices;

	public List getAdvices() {
		ObjectHelper.checkNotNull("field:advices", advices);
		return this.advices;
	}

	protected void setAdvices(final List advices) {
		ObjectHelper.checkNotNull("parameter:advices", advices);
		this.advices = advices;
	}

	private Set includedFiles;

	protected Set getIncludedFiles() {
		ObjectHelper.checkNotNull("field:includedFiles", includedFiles);
		return this.includedFiles;
	}

	protected void setIncludedFiles(final Set includedFiles) {
		ObjectHelper.checkNotNull("parameter:includedFiles", includedFiles);
		this.includedFiles = includedFiles;
	}
	
	private List aliases;

	public List getAliases() {
		ObjectHelper.checkNotNull("field:aliases", aliases);
		return this.aliases;
	}

	protected void setAliases(final List aliases) {
		ObjectHelper.checkNotNull("parameter:aliases", aliases);
		this.aliases = aliases;
	}
}
