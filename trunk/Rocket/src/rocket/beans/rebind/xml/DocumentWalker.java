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
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import rocket.beans.rebind.Advice;
import rocket.beans.rebind.Bean;
import rocket.beans.rebind.BeanFactoryGeneratorException;
import rocket.beans.rebind.NestedBean;
import rocket.beans.rebind.Property;
import rocket.beans.rebind.Rpc;
import rocket.beans.rebind.alias.Alias;
import rocket.beans.rebind.beanreference.BeanReference;
import rocket.beans.rebind.beanreference.BeanReferenceImpl;
import rocket.beans.rebind.list.ListValue;
import rocket.beans.rebind.map.MapValue;
import rocket.beans.rebind.nullvalue.NullLiteral;
import rocket.beans.rebind.placeholder.PlaceHolderResolver;
import rocket.beans.rebind.set.SetValue;
import rocket.beans.rebind.stringvalue.StringValue;
import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * This factory is includes a number of methods to assist with walking through a
 * xml document.
 * 
 * The walker does not attempt to validate values eg it doesnt check if bean
 * id's are unique or even present.
 * 
 * @author Miroslav Pokorny
 */
public class DocumentWalker {

	/**
	 * Initializes this document walker so that it may be used to travel about
	 * the dom.
	 * 
	 * @param fileName
	 *            The name of the first xml document to be processed.
	 */
	public void process(final String fileName) {
		try {
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setValidating(true);

			final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			documentBuilder.setErrorHandler(this.getErrorHandler());
			documentBuilder.setEntityResolver(this.getEntityResolver());
			this.setDocumentBuilder( documentBuilder );

			this.setIncludedFiles(createIncludedFiles());
			this.setBeans(this.createBeans());
			this.setAliases(this.createAliases());
			this.setAdvices(this.createAdvices());

			this.setFilename(fileName);
			this.processDocument();

		} catch (final ParserConfigurationException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst preparing to read the file \"" + fileName + "\".",
					caught);
		}
	}
	

	/**
	 * The DocumentBuilder which is used to provide DocumentBuilders.
	 */
	private DocumentBuilder documentBuilder;

	protected DocumentBuilder getDocumentBuilder() {
		ObjectHelper.checkNotNull("field:documentBuilder", documentBuilder);
		return this.documentBuilder;
	}

	protected void setDocumentBuilder(final DocumentBuilder documentBuilder) {
		ObjectHelper.checkNotNull("parameter:documentBuilder", documentBuilder);
		this.documentBuilder = documentBuilder;
	}

	/**
	 * Simply wraps and catches any exceptions thrown by {@link #processDocument0()} rethrowing them 
	 * with additional detail.
	 */
	protected void processDocument() {
		try {
			this.processDocument0();
		} catch (final SAXParseException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst parsing the xml file \"" + this.getFilename() + "\" at line: "+ caught.getLineNumber() + ", column: " + caught.getColumnNumber(), caught);
		} catch (final SAXException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst parsing the xml file \"" + this.getFilename() + "\".", caught);
		} catch (final IOException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst reading the file \"" + this.getFilename() + "\".", caught);
		} catch (final RuntimeException caught) {
			throw caught;
		}
	}

	protected void processDocument0() throws SAXException, IOException {
		final String fileName = this.getFilename();
		final Set includedFiles = this.getIncludedFiles();
		if (includedFiles.contains(fileName)) {
			throwIncludedFileCycle(fileName);
		}
		includedFiles.add(fileName);

		final Generator generator = this.getGenerator();
		final GeneratorContext context = generator.getGeneratorContext();
		context.debug(fileName);

		final InputStream inputStream = generator.getResource(fileName);
		final Document document = this.getDocumentBuilder().parse(inputStream);
		this.setDocument(document);
		
		// process the local tags within this document
		final PlaceHolderResolver placeHolderResolver = this.loadPlaceholderFiles(document);
		this.setPlaceHolderResolver(placeHolderResolver);
		final List included = this.visitIncludedFiles(document, fileName, placeHolderResolver);

		this.visitBeans();
		this.visitRpcs();

		this.visitAliases();
		this.visitAdvices();

		// now include the included files...
		final Iterator includedFilesIterator = included.iterator();
		while (includedFilesIterator.hasNext()) {
			final IncludeTag includeFile = (IncludeTag) includedFilesIterator.next();
			final String includedFileFileName = includeFile.getFile();
			this.setFilename(includedFileFileName );
			this.processDocument();
		}
	}

	/**
	 * This method is invoked to report included file cycles.
	 * 
	 * @param fileName The cycle file.
	 */
	protected void throwIncludedFileCycle(final String fileName) {
		throw new BeanFactoryGeneratorException("The file \"" + fileName + "\" has previously been included causing a cycle.");
	}

	/**
	 * The document thats currently being processed.
	 */
	private Document document;

	protected Document getDocument() {
		ObjectHelper.checkNotNull("field:document", document);
		return this.document;
	}

	protected void setDocument(final Document document) {
		ObjectHelper.checkNotNull("parameter:document", document);
		this.document = document;
	}

	/**
	 * The filename of the document being processed.
	 */
	private String filename;

	protected String getFilename() {
		StringHelper.checkNotEmpty("field:filename", filename);
		return this.filename;
	}

	protected void setFilename(final String filename) {
		StringHelper.checkNotEmpty("parameter:filename", filename);
		this.filename = filename;
	}

	/**
	 * Loads all placeholders for the given document
	 * 
	 * @return A PlaceHolderResolver holding all values.
	 */
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

	private PlaceHolderResolver placeHolderResolver;

	protected PlaceHolderResolver getPlaceHolderResolver() {
		ObjectHelper.checkNotNull("field:placeHolderResolver", placeHolderResolver);
		return this.placeHolderResolver;
	}

	protected void setPlaceHolderResolver(final PlaceHolderResolver placeHolderResolver) {
		ObjectHelper.checkNotNull("parameter:placeHolderResolver", placeHolderResolver);
		this.placeHolderResolver = placeHolderResolver;
	}

	/**
	 * Builds a list containing BeanTags for each and every BEAN element within
	 * the given document.
	 */
	protected void visitBeans() {
		final NodeList nodeList = this.getDocument().getDocumentElement().getChildNodes();
		final int count = nodeList.getLength();

		for (int i = 0; i < count; i++) {
			final Node node = nodeList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			final Element element = (Element) node;
			if (false == element.getTagName().equals(Constants.BEAN_TAG)) {
				continue;
			}

			this.visitBean(element);
		}
	}

	/**
	 * Visits a single bean copying values from the xml document into the given bean.
	 * 
	 * @param element The bean element
	 */
	protected void visitBean(final Element element) {
		ObjectHelper.checkNotNull( "parameter:element", element );
		
		final BeanTag tag = new BeanTag();
		tag.setElement(element);
		tag.setPlaceHolderResolver( this.getPlaceHolderResolver() );

		final Bean bean = new Bean();
		bean.setEagerLoaded(tag.isEagerLoaded());
		bean.setId(tag.getId());
		bean.setSingleton(tag.isSingleton());
		bean.setTypeName(tag.getClassName());
		bean.setFactoryMethod(tag.getFactoryMethod());
		bean.setInitMethod(tag.getInitMethod());
		bean.setDestroyMethod(tag.getDestroyMethod());

		this.addBean(bean);

		bean.setConstructorValues(this.visitConstructorValues(tag.getConstructorValues()));
		bean.setProperties(this.visitProperties(tag.getProperties()));
	}

	/**
	 * A set which aggregates all beans encountered within all xml documents.
	 */
	private Set beans;

	public Set getBeans() {
		ObjectHelper.checkNotNull("field:beans", beans);
		return this.beans;
	}

	protected void setBeans(final Set beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);
		this.beans = beans;
	}

	protected Set createBeans() {
		return new HashSet();
	}

	protected void addBean(final Bean bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);

		this.getBeans().add(bean);
	}

	protected List visitConstructorValues(final List values) {
		return this.visitValues(values);
	}

	protected List visitProperties(final List propertys) {
		final List properties = new ArrayList();

		final Iterator iterator = propertys.iterator();
		while (iterator.hasNext()) {
			final Element element = (Element) iterator.next();

			final PropertyTag tag = new PropertyTag();
			tag.setElement(element);
			tag.setPlaceHolderResolver(placeHolderResolver);

			final Property property = new Property();
			property.setName(tag.getName());

			final Value value = this.visitConstructorOrPropertyValue(tag.getValue());
			property.setValue(value);

			properties.add(property);
		}

		return properties;
	}

	/**
	 * Factory method which creates a Value from the given element
	 * 
	 * @param element The source element
	 * @return The built value.
	 */
	protected Value visitConstructorOrPropertyValue(final Element element) {
		Value value = null;

		while (true) {
			final String tagName = element.getTagName();

			if (tagName.equals(Constants.NULL_TAG)) {
				value = this.visitNullValue(element);
				break;
			}

			if (tagName.equals(Constants.VALUE_TAG)) {
				value = this.visitValue(element);
				break;
			}

			if (tagName.equals(Constants.BEAN_REFERENCE_TAG)) {
				value = this.visitBeanReference(element);
				break;
			}

			if (tagName.equals(Constants.BEAN_TAG)) {
				value = this.visitNestedBean(element);
				break;
			}

			if (tagName.equals(Constants.LIST_TAG)) {
				value = this.visitList(element);
				break;
			}

			if (tagName.equals(Constants.SET_TAG)) {
				value = this.visitSet(element);
				break;
			}

			if (tagName.equals(Constants.MAP_TAG)) {
				value = this.visitMap(element);
				break;
			}

			throw new BeanFactoryGeneratorException("Unknown element \"" + tagName + "\".");
		}

		return value;
	}

	protected NullLiteral visitNullValue(final Element element) {
		final NullLiteral nullValue = new NullLiteral();

		final GeneratorContext context = this.getGenerator().getGeneratorContext();
		nullValue.setGeneratorContext(context);
		nullValue.setType(context.getString());

		return nullValue;
	}

	protected StringValue visitValue(final Element element) {
		final StringValue stringValue = new StringValue();
		final String text = this.getPlaceHolderResolver().resolve(element.getTextContent());

		final GeneratorContext context = this.getGenerator().getGeneratorContext();
		stringValue.setFilename( this.getFilename() );
		stringValue.setGeneratorContext(context);
		stringValue.setType(context.getString());
		stringValue.setValue(text);

		return stringValue;
	}

	protected BeanReference visitBeanReference(final Element element) {
		final BeanReferenceTag tag = new BeanReferenceTag();
		tag.setElement(element);
		tag.setPlaceHolderResolver(this.getPlaceHolderResolver());

		final BeanReferenceImpl beanReference = new BeanReferenceImpl();
		beanReference.setFilename( this.getFilename() );
		beanReference.setGeneratorContext(this.getGenerator().getGeneratorContext());
		beanReference.setId(tag.getId());

		return beanReference;
	}

	/**
	 * Visits a single bean copying values from the xml document into the given bean.
	 * 
	 * @param element The bean element
	 * @return The new nested bean
	 */
	protected NestedBean visitNestedBean(final Element element ) {
		final BeanTag tag = new BeanTag();
		tag.setElement(element);
		tag.setPlaceHolderResolver( this.getPlaceHolderResolver() );

		final NestedBean bean = new NestedBean();
		bean.setFilename( this.getFilename() );
		
		bean.setEagerLoaded(tag.isEagerLoaded());
		bean.setId(tag.getId());
		bean.setSingleton(tag.isSingleton());
		bean.setTypeName(tag.getClassName());
		bean.setFactoryMethod(tag.getFactoryMethod());
		bean.setInitMethod(tag.getInitMethod());
		bean.setDestroyMethod(tag.getDestroyMethod());

		this.addBean(bean);

		bean.setConstructorValues(this.visitConstructorValues(tag.getConstructorValues()));
		bean.setProperties(this.visitProperties(tag.getProperties()));

		return bean;
	}

	/**
	 * Creates a ListValue from a list value element
	 * 
	 * @param element The list element
	 * @return A list containing the list values
	 */
	protected ListValue visitList(final Element element) {
		final ListTag tag = new ListTag();
		tag.setElement(element);
		tag.setPlaceHolderResolver( this.getPlaceHolderResolver());

		final ListValue list = new ListValue();
		final List elements = this.visitValues(tag.getValues());
		list.setElements(elements);
		list.setFilename( this.getFilename() );
		list.setGeneratorContext(this.getGenerator().getGeneratorContext());
		return list;
	}

	/**
	 * Creates a SetValue from a set value element.
	 * 
	 * @param element The set element
	 * @return A list containing the set values
	 */
	protected SetValue visitSet(final Element element) {
		final SetTag tag = new SetTag();
		tag.setElement(element);
		tag.setPlaceHolderResolver(placeHolderResolver);

		final SetValue set = new SetValue();
		final List elements = this.visitValues(tag.getValues());
		set.setElements(elements);
		set.setFilename(this.getFilename());
		set.setGeneratorContext(this.getGenerator().getGeneratorContext());
		return set;
	}

	protected MapValue visitMap(final Element element) {
		final MapTag tag = new MapTag();
		tag.setElement(element);
		
		final PlaceHolderResolver placeHolderResolver = this.getPlaceHolderResolver();
		tag.setPlaceHolderResolver(placeHolderResolver);

		final MapValue map = new MapValue();
		map.setFilename( this.getFilename());
		map.setGeneratorContext(this.getGenerator().getGeneratorContext());

		final NodeList entriesNodeList = element.getChildNodes();
		final int count = entriesNodeList.getLength();
		for (int i = 0; i < count; i++) {
			final Node node = entriesNodeList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			final Element entryElement = (Element) node;
			final MapEntryTag entry = new MapEntryTag();
			entry.setElement(entryElement);
			entry.setPlaceHolderResolver(placeHolderResolver);

			final String key = entry.getKey();
			final Element valueElement = entry.getValue();
			final Value value = this.visitConstructorOrPropertyValue(valueElement);

			map.addEntry(key, value);
		}

		return map;
	}

	/**
	 * Visits all the value elements and builds a list containing the values
	 * found.
	 * 
	 * @param valueElements A nodelist of value elements.
	 * @return A list of values
	 */
	protected List visitValues(final List valueElements) {
		final List values = new ArrayList();

		final Iterator iterator = valueElements.iterator();
		while (iterator.hasNext()) {
			final Element element = (Element) iterator.next();
			final Value value = this.visitConstructorOrPropertyValue(element);
			values.add(value);
		}

		return values;
	}

	/**
	 * Visits all alias tags and verifies that the name and bean are valid.
	 */
	protected void visitAliases() {
		final PlaceHolderResolver placeHolderResolver = this.getPlaceHolderResolver();
		final String filename = this.getFilename();

		final NodeList nodeList = this.getDocument().getElementsByTagName(Constants.ALIAS_TAG);
		final int count = nodeList.getLength();

		for (int i = 0; i < count; i++) {
			final AliasTag tag = new AliasTag();
			tag.setElement((Element) nodeList.item(i));
			tag.setFilename(filename);
			tag.setPlaceHolderResolver(placeHolderResolver);

			final Alias alias = new Alias();
			alias.setBean(tag.getBean());
			alias.setName(tag.getName());

			this.addAlias(alias);
		}
	}

	/**
	 * This set aggregates all the aliases found whilst parsing.
	 */
	private Set aliases;

	public Set getAliases() {
		ObjectHelper.checkNotNull("field:aliases", aliases);
		return this.aliases;
	}

	protected void setAliases(final Set aliases) {
		ObjectHelper.checkNotNull("parameter:aliases", aliases);
		this.aliases = aliases;
	}

	protected Set createAliases() {
		return new TreeSet();
	}

	protected void addAlias(final Alias alias) {
		ObjectHelper.checkNotNull("parameter:alias", alias);

		this.getAliases().add(alias);
	}

	protected void throwDuplicateAliasName(final Alias alias) {
		throw new BeanFactoryGeneratorException("The alias name is a duplicate of an existing bean/alias, " + alias);
	}

	protected void throwInvalidAliasBean(final Alias alias) {
		throw new BeanFactoryGeneratorException("The alias contains an invalid bean reference, alias: " + alias);
	}

	/**
	 * Visits all the rpc tags creating beans for each element that is encountered.
	 */
	protected void visitRpcs() {
		final PlaceHolderResolver placeHolderResolver = this.getPlaceHolderResolver();
		final String filename = this.getFilename();

		final NodeList nodeList = this.getDocument().getElementsByTagName(Constants.RPC_TAG);
		final int count = nodeList.getLength();

		for (int i = 0; i < count; i++) {
			final Node node = nodeList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			final RpcTag tag = new RpcTag();
			tag.setElement((Element) node);
			tag.setFilename(filename);
			tag.setPlaceHolderResolver(placeHolderResolver);

			final Rpc service = new Rpc();
			service.setId(tag.getId());
			service.setServiceEntryPoint(tag.getServiceEntryPoint());
			service.setServiceInterface(tag.getServiceInterface());
			
			this.addBean(service);
		}
	}

	protected void visitAdvices() {
		final PlaceHolderResolver placeHolderResolver = this.getPlaceHolderResolver();
		final String filename = this.getFilename();
		
		final NodeList nodeList = this.getDocument().getElementsByTagName(Constants.ADVICE_TAG);
		final int count = nodeList.getLength();
		for (int i = 0; i < count; i++) {
			final Node node = nodeList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			final AdviceTag tag = new AdviceTag();
			tag.setElement((Element) nodeList.item(i));
			tag.setFilename(filename);
			tag.setPlaceHolderResolver(placeHolderResolver);

			final Advice advice = new Advice();
			advice.setAdvisor(tag.getAdvisor());
			advice.setTarget(tag.getTarget());
			advice.setMethodExpression(tag.getMethodExpression());
			this.addAdvice(advice);
		}
	}

	/**
	 * A set which aggregates all advices found
	 */
	private Set advices;

	public Set getAdvices() {
		ObjectHelper.checkNotNull("field:advices", advices);
		return this.advices;
	}

	protected void setAdvices(final Set advices) {
		ObjectHelper.checkNotNull("parameter:advices", advices);
		this.advices = advices;
	}

	protected Set createAdvices() {
		return new HashSet();
	}

	protected void addAdvice(final Advice advice) {
		this.getAdvices().add(advice);
	}

	protected List visitIncludedFiles(final Document document, final String filename, final PlaceHolderResolver placeHolderResolver) {
		ObjectHelper.checkNotNull("parameter:document", document);
		StringHelper.checkNotEmpty("parameter:filename", filename);
		ObjectHelper.checkNotNull("parameter:placeHolderResolver", placeHolderResolver);

		final NodeList nodeList = document.getElementsByTagName(Constants.INCLUDE_TAG);
		final int count = nodeList.getLength();

		final List includedFiles = new ArrayList();
		for (int i = 0; i < count; i++) {
			final IncludeTag includedFile = new IncludeTag();
			includedFile.setElement((Element) nodeList.item(i));
			includedFile.setFilename(filename);
			includedFile.setPlaceHolderResolver(placeHolderResolver);

			includedFiles.add(includedFile);
		}

		return Collections.unmodifiableList(includedFiles);
	}

	/**
	 * This set is used to maintain a list of included files in order to detect
	 * and complain about cycles.
	 */
	private Set includedFiles;

	protected Set getIncludedFiles() {
		ObjectHelper.checkNotNull("field:includedFiles", includedFiles);
		return this.includedFiles;
	}

	protected void setIncludedFiles(final Set includedFiles) {
		ObjectHelper.checkNotNull("parameter:includedFiles", includedFiles);
		this.includedFiles = includedFiles;
	}

	protected Set createIncludedFiles() {
		return new HashSet();
	}

	/**
	 * The entity resolver used by the parser
	 */
	private EntityResolver entityResolver;

	protected EntityResolver getEntityResolver() {
		ObjectHelper.checkNotNull("field:entityResolver", entityResolver);
		return entityResolver;
	}

	public void setEntityResolver(final EntityResolver entityResolver) {
		ObjectHelper.checkNotNull("parameter:entityResolver", entityResolver);
		this.entityResolver = entityResolver;
	}

	/**
	 * The SAX error handler
	 */
	private ErrorHandler errorHandler;

	protected ErrorHandler getErrorHandler() {
		ObjectHelper.checkNotNull("field:errorHandler", errorHandler);
		return errorHandler;
	}

	public void setErrorHandler(final ErrorHandler errorHandler) {
		ObjectHelper.checkNotNull("parameter:errorHandler", errorHandler);
		this.errorHandler = errorHandler;
	}

	/**
	 * The generator being run
	 */
	private Generator generator;

	protected Generator getGenerator() {
		ObjectHelper.checkNotNull("field:generator", generator);
		return this.generator;
	}

	public void setGenerator(final Generator generator) {
		ObjectHelper.checkNotNull("parameter:generator", generator);
		this.generator = generator;
	}
}
