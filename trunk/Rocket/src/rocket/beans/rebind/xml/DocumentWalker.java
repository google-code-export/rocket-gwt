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
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import rocket.beans.rebind.BeanFactoryGeneratorException;
import rocket.beans.rebind.placeholder.PlaceHolderResolver;
import rocket.generator.rebind.Generator;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * This factory is includes a number of methods to assist with walking through a
 * xml document
 * 
 * @author Miroslav Pokorny
 */
public class DocumentWalker {

	/**
	 * Initializes this document walker so that it may be used to travel about
	 * the dom.
	 * 
	 * @param generator
	 * @param fileName
	 */
	public void prepare(final Generator generator, final String fileName) {
		final InputStream inputStream = generator.getResource(fileName);

		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);

			final DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(this.getErrorHandler());
			builder.setEntityResolver(this.getEntityResolver());

			final Document document = builder.parse(inputStream);
			this.setDocument(document);
			this.loadPlaceholderFiles();

		} catch (final ParserConfigurationException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst preparing to read the file [" + fileName + "]", caught);
		} catch (final SAXException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst parsing the xml file [" + fileName + "]", caught);
		} catch (final IOException caught) {
			throw new BeanFactoryGeneratorException(caught.getMessage() + " whilst reading the file [" + fileName + "]", caught);
		}
	}

	protected void loadPlaceholderFiles() {
		final PlaceHolderResolver placeHolderResolver = new PlaceHolderResolver();

		final NodeList tags = this.getDocument().getElementsByTagName(Constants.PLACE_HOLDERS_TAG);
		final int count = tags.getLength();
		for (int i = 0; i < count; i++) {
			final Element element = (Element) tags.item(i);
			final String fileName = element.getAttribute(Constants.PLACE_HOLDERS_FILE_ATTRIBUTE);
			if (StringHelper.isNullOrEmpty(fileName)) {
				continue;
			}

			placeHolderResolver.load(fileName);
		}

		this.setPlaceHolderResolver(placeHolderResolver);
	}

	private Document document;

	protected Document getDocument() {
		ObjectHelper.checkNotNull("field:document", document);
		return document;
	}

	public void setDocument(final Document document) {
		ObjectHelper.checkNotNull("parameter:document", document);
		this.document = document;
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

	private PlaceHolderResolver placeHolderResolver;

	protected PlaceHolderResolver getPlaceHolderResolver() {
		ObjectHelper.checkNotNull("field:placeHolderResolver", placeHolderResolver);
		return this.placeHolderResolver;
	}

	public void setPlaceHolderResolver(final PlaceHolderResolver placeHolderResolver) {
		ObjectHelper.checkNotNull("parameter:placeHolderResolver", placeHolderResolver);
		this.placeHolderResolver = placeHolderResolver;
	}

	/**
	 * Returns a read only list view of all the bean tags for this document.
	 * 
	 * @return
	 */
	public List getBeanElements() {
		final NodeList nodeList = this.getDocument().getElementsByTagName(Constants.BEAN_TAG);
		final PlaceHolderResolver placeHolderResolver = this.getPlaceHolderResolver();

		return Collections.unmodifiableList(new AbstractList() {
			public Object get(final int index) {
				final BeanTag bean = new BeanTag();
				bean.setElement((Element) nodeList.item(index));
				bean.setPlaceHolderResolver(placeHolderResolver);
				return bean;
			}

			public int size() {
				return nodeList.getLength();
			}
		});
	}

	public List getRemoteJsonElements() {
		final NodeList nodeList = this.getDocument().getElementsByTagName(Constants.REMOTE_JSON_SERVICE_TAG);
		final PlaceHolderResolver placeHolderResolver = this.getPlaceHolderResolver();

		return Collections.unmodifiableList(new AbstractList() {
			public Object get(final int index) {
				final RemoteJsonServiceTag json = new RemoteJsonServiceTag();
				json.setElement((Element) nodeList.item(index));
				json.setPlaceHolderResolver(placeHolderResolver);
				return json;
			}

			public int size() {
				return nodeList.getLength();
			}
		});
	}

	public List getRemoteRpcElements() {
		final NodeList nodeList = this.getDocument().getElementsByTagName(Constants.REMOTE_RPC_SERVICE_TAG);
		final PlaceHolderResolver placeHolderResolver = this.getPlaceHolderResolver();

		return Collections.unmodifiableList(new AbstractList() {
			public Object get(final int index) {
				final RemoteRpcServiceTag rpc = new RemoteRpcServiceTag();
				rpc.setElement((Element) nodeList.item(index));
				rpc.setPlaceHolderResolver(placeHolderResolver);
				return rpc;
			}

			public int size() {
				return nodeList.getLength();
			}
		});
	}

}
