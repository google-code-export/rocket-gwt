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
package rocket.beans.rebind.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import rocket.beans.rebind.BeanFactoryGeneratorContext;
import rocket.beans.rebind.BeansHelper;
import rocket.beans.rebind.bean.BeanClassNameMissingException;
import rocket.beans.rebind.bean.BeanDefinition;
import rocket.beans.rebind.bean.BeanIdMissingException;
import rocket.beans.rebind.bean.BeanTypeNotConcreteException;
import rocket.beans.rebind.bean.BeanTypeNotFoundException;
import rocket.beans.rebind.bean.InvalidBeanScopeException;
import rocket.beans.rebind.init.CustomInitMethod;
import rocket.beans.rebind.init.InitMethod;
import rocket.beans.rebind.jsonandrpc.PropertyMissingException;
import rocket.beans.rebind.jsonandrpc.RemoteJsonServiceBeanDefinition;
import rocket.beans.rebind.jsonandrpc.RemoteRpcServiceBeanDefinition;
import rocket.beans.rebind.newinstance.DeferredBindingNewInstance;
import rocket.beans.rebind.newinstance.FactoryMethod;
import rocket.beans.rebind.newinstance.NewInstance;
import rocket.beans.rebind.property.PropertyDefinition;
import rocket.beans.rebind.values.BeanReference;
import rocket.beans.rebind.values.ListPropertyValueDefinition;
import rocket.beans.rebind.values.MapPropertyValueDefinition;
import rocket.beans.rebind.values.PropertyValueDefinition;
import rocket.beans.rebind.values.SetPropertyValueDefinition;
import rocket.beans.rebind.values.StringValueDefinition;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * This sax handler takes a xml file and builds a number of bean definitions.
 * 
 * @author Miroslav Pokorny
 */
public class SaxHandler extends DefaultHandler implements ContentHandler, ErrorHandler {

	public SaxHandler() {
		super();
	}

	/**
	 * Attempts to return a reference to the accompanying DTD file if the public id matches
	 * 
	 * @param publicId
	 * @param systemId
	 */
	public InputSource resolveEntity(final String publicId, final String systemId) throws IOException, SAXException {
		if (Constants.PUBLIC_ID.equals(publicId)) {
			final InputStream inputStream = this.getClass().getResourceAsStream(Constants.DTD_FILE_NAME);
			return new InputSource(inputStream);
		} else {
			return super.resolveEntity(publicId, systemId);
		}
	}

	/**
	 * This method dispatches to a number of methods depending onthe tag
	 * encountered.
	 */
	public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {

		while (true) {
			if (qName.equals(Constants.BEAN_FACTORY)) {
				this.handleBeanFactoryOpen();
				break;
			}
			if (qName.equals(Constants.BEAN)) {
				final String className = attributes.getValue(Constants.BEAN_CLASSNAME);
				final String id = attributes.getValue(Constants.BEAN_ID);
				final String scope = attributes.getValue(Constants.BEAN_SCOPE);
				final String factoryMethodName = attributes.getValue(Constants.BEAN_FACTORY_METHOD_NAME);
				final String initMethodName = attributes.getValue(Constants.BEAN_INIT_METHOD_NAME);
				this.handleBeanOpen(className, id, scope, factoryMethodName, initMethodName);
				break;
			}
			if (qName.equals(Constants.PROPERTY)) {
				final String propertyName = attributes.getValue(Constants.PROPERTY_NAME);
				this.handlePropertyOpen(propertyName);
				break;
			}
			if (qName.equals(Constants.VALUE)) {
				this.handleValueOpen();
				break;
			}
			if (qName.equals(Constants.BEAN_REFERENCE)) {
				final String id = attributes.getValue(Constants.BEAN_REFERENCE_ID);
				this.handleBeanReferenceOpen(id);
				break;
			}
			if (qName.equals(Constants.LIST)) {
				this.handleListOpen();
				break;
			}
			if (qName.equals(Constants.SET)) {
				this.handleSetOpen();
				break;
			}
			if (qName.equals(Constants.MAP)) {
				this.handleMapOpen();
				break;
			}
			if (qName.equals(Constants.MAP_ENTRY)) {
				final String key = attributes.getValue(Constants.MAP_ENTRY_KEY);
				this.handleMapEntryOpen(key);
				break;
			}
			if (qName.equals(Constants.REMOTE_RPC_SERVICE)) {
				final String id = attributes.getValue(Constants.REMOTE_RPC_SERVICE_ID);
				final String interfaceTypeName = attributes.getValue(Constants.REMOTE_RPC_SERVICE_INTERFACE);
				final String address = attributes.getValue(Constants.REMOTE_RPC_SERVICE_ADDRESS);

				this.handleRemoteRpcServiceOpen(id, interfaceTypeName, address);
				break;
			}
			if (qName.equals(Constants.REMOTE_JSON_SERVICE)) {
				final String id = attributes.getValue(Constants.REMOTE_JSON_SERVICE_ID);
				final String interfaceTypeName = attributes.getValue(Constants.REMOTE_JSON_SERVICE_INTERFACE);
				final String address = attributes.getValue(Constants.REMOTE_JSON_SERVICE_ADDRESS);

				this.handleRemoteJsonServiceOpen(id, interfaceTypeName, address);
				break;
			}
			break;
		}
	}

	/**
	 * This method dispatches to a number of methods based on the element being
	 * left
	 */
	public void endElement(final String uri, final String localName, final String qName) throws SAXException {

		while (true) {
			if (qName.equals(Constants.BEAN_FACTORY)) {
				this.handleBeanFactoryClose();
				break;
			}
			if (qName.equals(Constants.BEAN)) {
				this.handleBeanClose();
				break;
			}
			if (qName.equals(Constants.PROPERTY)) {
				this.handlePropertyClose();
				break;
			}
			if (qName.equals(Constants.VALUE)) {
				this.handleValueClose(this.getBuffer());
				break;
			}
			if (qName.equals(Constants.BEAN_REFERENCE)) {
				this.handleBeanReferenceClose();
				break;
			}
			if (qName.equals(Constants.LIST)) {
				this.handleListClose();
				break;
			}

			if (qName.equals(Constants.SET)) {
				this.handleSetClose();
				break;
			}
			if (qName.equals(Constants.MAP)) {
				this.handleMapClose();
				break;
			}
			if (qName.equals(Constants.MAP_ENTRY)) {
				this.handleMapEntryClose();
				break;
			}
			if (qName.equals(Constants.REMOTE_RPC_SERVICE)) {
				this.handleRemoteRpcServiceClose();
				break;
			}
			if (qName.equals(Constants.REMOTE_JSON_SERVICE)) {
				this.handleRemoteJsonServiceClose();
				break;
			}
			break;
		}
	}

	protected void handleBeanFactoryOpen() {
	}

	protected void handleBeanFactoryClose() {
	}

	/**
	 * Adds a new bean definition after checking that the type className is
	 * concrete and the scope is valid. Adding the bean definition throws an
	 * exception is the id is already allocated.
	 * 
	 * @param className
	 * @param id
	 * @param scope
	 * @param factoryMethodName
	 * @param initFactoryMethodName
	 */
	protected void handleBeanOpen(final String className, final String id, final String scope, final String factoryMethodName,
			final String initMethodName) {
		if (StringHelper.isNullOrEmpty(className)) {
			throwBeanClassNameMissingException();
		}
		if (StringHelper.isNullOrEmpty(id)) {
			throwBeanIdMissingException();
		}
		if (false == BeansHelper.isScope(scope)) {
			throwInvalidBeanScopeException(id, scope);
		}

		final BeanFactoryGeneratorContext context = this.getBeanFactoryGeneratorContext();
		final BeanDefinition bean = new BeanDefinition();
		bean.setBeanFactoryGeneratorContext(context);
		bean.setId(id);
		bean.setScope(scope);

		final JClassType type = (JClassType) context.findType(className);
		if (null == type) {
			this.throwBeanTypeNotFoundException(className);
		}

		if (type.isAbstract() || type.isInterface() != null) {
			throwBeanTypeMustBeConcrete(className, id, scope);
		}
		bean.setType(type);
		
		// handle factoryMethod
		NewInstance newInstance = null;
		while( true ){
			if (StringHelper.isNullOrEmpty(factoryMethodName)) {
				newInstance = new NewInstance();				
				newInstance.setBeanDefinition(bean );
				break;
			}
			final FactoryMethod factoryMethod = new FactoryMethod();
			factoryMethod.setBeanDefinition( bean );
			factoryMethod.setMethodName(factoryMethodName);
			newInstance = factoryMethod;
			break;
		}
		newInstance.setBeanFactoryGeneratorContext( this.getBeanFactoryGeneratorContext() );			
		bean.setNewInstance( newInstance );
		
		// handle initMethod
		InitMethod initMethod = null;
		while( true ){
			if (StringHelper.isNullOrEmpty(initMethodName)) {
				initMethod = new InitMethod();				
				break;
			}
			final CustomInitMethod customInitMethod = new CustomInitMethod();
			customInitMethod.setBeanDefinition( bean );
			customInitMethod.setMethodName(initMethodName);
			initMethod = customInitMethod;
			break;
		}
		bean.setInitMethod( initMethod );
		
		context.addBeanDefinition(bean);

		this.push(bean);
	}

	protected void throwBeanIdMissingException() {
		throw new BeanIdMissingException("Bean id missing," + this.buildLineAndColumnFromLocator());
	}

	protected void throwInvalidBeanScopeException(final String id, final String scope) {
		throw new InvalidBeanScopeException("The bean with an id of [" + id + "] contains an invalid scope [" + scope + "]"
				+ this.buildLineAndColumnFromLocator());
	}

	protected void throwBeanClassNameMissingException() {
		throw new BeanClassNameMissingException("Bean type missing," + this.buildLineAndColumnFromLocator());
	}

	protected void throwBeanTypeNotFoundException(final String className) {
		throw new BeanTypeNotFoundException("Bean type [" + className + "] not found, " + this.buildLineAndColumnFromLocator());
	}

	protected void throwBeanTypeMustBeConcrete(final String className, final String id, final String scope) {
		throw new BeanTypeNotConcreteException("The bean with an id of [" + id + "] and scope[" + scope + "] type [" + className
				+ "] is not a concrete class ");
	}

	protected void handleBeanClose() {
		this.pop();
	}

	protected void handlePropertyOpen(final String propertyName) {
		final PropertyDefinition property = new PropertyDefinition();
		property.setName(propertyName);

		this.push(property);
	}

	protected void handlePropertyClose() {
		final PropertyValueDefinition value = (PropertyValueDefinition) this.pop();
		final PropertyDefinition property = (PropertyDefinition) this.pop();
		property.setPropertyValueDefinition(value);

		final BeanDefinition bean = (BeanDefinition) this.peek();

		bean.addProperty(property);
	}

	protected void handleValueOpen() {
		this.startBuffer();
	}

	protected void handleValueClose(final String value) {
		final StringValueDefinition string = new StringValueDefinition();

		final BeanFactoryGeneratorContext context = this.getBeanFactoryGeneratorContext();
		string.setBeanFactoryGeneratorContext(context);
		string.setValue(value);
		string.setType(context.getJavaLangString());
		this.push(string); // popped by property, list, set, map
	}

	protected void handleBeanReferenceOpen(final String id) {
		final BeanReference reference = new BeanReference();
		reference.setBeanFactoryGeneratorContext(this.getBeanFactoryGeneratorContext());
		reference.setId(id);

		this.push(reference);// this is popped by property
	}

	protected void handleBeanReferenceClose() {
	}

	protected void handleListOpen() {
		final ListPropertyValueDefinition list = new ListPropertyValueDefinition();
		list.setBeanFactoryGeneratorContext(this.getBeanFactoryGeneratorContext());
		this.push(list);// this is popped by
		// property
	}

	protected void handleListClose() {
		final Stack listElements = new Stack();
		while (true) {
			final Object top = this.peek();
			if (top instanceof ListPropertyValueDefinition) {
				break;
			}
			listElements.push(this.pop());
		}

		final ListPropertyValueDefinition list = (ListPropertyValueDefinition) this.peek();
		while (false == listElements.isEmpty()) {
			final PropertyValueDefinition property = (PropertyValueDefinition) listElements.pop();
			list.add(property);
		}
	}

	protected void handleSetOpen() {
		final SetPropertyValueDefinition set = new SetPropertyValueDefinition();
		set.setBeanFactoryGeneratorContext(this.getBeanFactoryGeneratorContext());
		this.push(set);// this is popped by
		// property
	}

	protected void handleSetClose() {
		final Stack setElements = new Stack();
		while (true) {
			final Object top = this.peek();
			if (top instanceof SetPropertyValueDefinition) {
				break;
			}
			setElements.push(this.pop());
		}

		final SetPropertyValueDefinition list = (SetPropertyValueDefinition) this.peek();
		while (false == setElements.isEmpty()) {
			list.add((PropertyValueDefinition) setElements.pop());
		}
	}

	protected void handleMapOpen() {
		final MapPropertyValueDefinition map = new MapPropertyValueDefinition();
		map.setBeanFactoryGeneratorContext(this.getBeanFactoryGeneratorContext());
		this.push(map);// this is popped by
	}

	protected void handleMapClose() {
	}

	protected void handleMapEntryOpen(final String key) {
		this.push(key);// this is popped by handleMapEntryOpen
	}

	protected void handleMapEntryClose() {
		final PropertyValueDefinition value = (PropertyValueDefinition) this.pop();
		final String key = (String) this.pop();

		final MapPropertyValueDefinition map = (MapPropertyValueDefinition) this.peek();
		map.addMapEntry(key, value);
	}

	protected void handleRemoteRpcServiceOpen(final String id, final String interfaceTypeName, final String address) {
		if (StringHelper.isNullOrEmpty(id)) {
			throwBeanIdMissingException();
		}
		if (StringHelper.isNullOrEmpty(address)) {
			throwRemoteRpcServiceAddressMissingException();
		}

		final BeanFactoryGeneratorContext context = this.getBeanFactoryGeneratorContext();

		final JClassType interfaceType = (JClassType) context.getType(interfaceTypeName);

		final StringValueDefinition value = new StringValueDefinition();
		value.setType(context.getJavaLangString());
		value.setValue(address);

		final PropertyDefinition property = new PropertyDefinition();
		property.setName("address");
		property.setPropertyValueDefinition(value);

		final RemoteRpcServiceBeanDefinition beanDefinition = new RemoteRpcServiceBeanDefinition();
		beanDefinition.setId(id);
		beanDefinition.setType(interfaceType);
		beanDefinition.addProperty(property);
		
		final DeferredBindingNewInstance newInstance = new DeferredBindingNewInstance();
		newInstance.setBeanDefinition(beanDefinition);
		newInstance.setBeanFactoryGeneratorContext( context );		
		beanDefinition.setNewInstance(newInstance);
		
		beanDefinition.setInitMethod( new InitMethod() );

		context.addBeanDefinition(beanDefinition);
	}

	protected void throwRemoteRpcServiceAddressMissingException() {
		throw new PropertyMissingException("The " + Constants.REMOTE_RPC_SERVICE_ADDRESS + " property is missing "
				+ this.buildLineAndColumnFromLocator());
	}

	protected void handleRemoteRpcServiceClose() {

	}

	protected void handleRemoteJsonServiceOpen(final String id, final String interfaceTypeName, final String address) {
		if (StringHelper.isNullOrEmpty(id)) {
			throwBeanIdMissingException();
		}
		if (StringHelper.isNullOrEmpty(address)) {
			throwRemoteJsonServiceAddressMissingException();
		}

		final BeanFactoryGeneratorContext context = this.getBeanFactoryGeneratorContext();

		final JClassType interfaceType = (JClassType) context.getType(interfaceTypeName);

		final StringValueDefinition value = new StringValueDefinition();
		value.setType(context.getJavaLangString());
		value.setValue(address);

		final PropertyDefinition property = new PropertyDefinition();
		property.setName("address");
		property.setPropertyValueDefinition(value);

		final RemoteJsonServiceBeanDefinition beanDefinition = new RemoteJsonServiceBeanDefinition();
		beanDefinition.setId(id);
		beanDefinition.setType(interfaceType);
		
		final DeferredBindingNewInstance newInstance = new DeferredBindingNewInstance();
		newInstance.setBeanDefinition(beanDefinition);
		newInstance.setBeanFactoryGeneratorContext( context );
		beanDefinition.setNewInstance(newInstance);
		
		beanDefinition.setInitMethod( new InitMethod() );
		
		beanDefinition.addProperty(property);

		context.addBeanDefinition( beanDefinition );
	}

	protected void throwRemoteJsonServiceAddressMissingException() {
		throw new PropertyMissingException("The " + Constants.REMOTE_JSON_SERVICE_ADDRESS + " property is missing "
				+ this.buildLineAndColumnFromLocator());
	}

	protected void handleRemoteJsonServiceClose() {

	}

	/**
	 * Merely accumulates any text found between elements.
	 */
	public void characters(final char[] chars, final int start, final int length) throws SAXException {
		if (false == this.stackIsEmpty()) {
			final Object top = this.getStack().peek();
			if (top instanceof StringBuilder) {
				final StringBuilder builder = (StringBuilder) top;
				builder.append(chars, start, length);
			}
		}
	}

	/**
	 * Typically read when leaving an element, giving the contents of the tag.
	 * 
	 * @return
	 */
	protected String getBuffer() {
		final StringBuilder builder = (StringBuilder) this.getStack().pop();
		return builder.toString().trim();
	}

	protected void startBuffer() {
		this.push(new StringBuilder());
	}

	public void setDocumentLocator(final Locator locator) {
		this.locator = locator;
	}

	protected Locator getLocator() {
		return this.locator;
	}

	private Locator locator;

	protected String buildLineAndColumnFromLocator() {
		StringBuffer buffer = new StringBuffer();

		final Locator locator = this.getLocator();
		if (null != locator) {
			buffer.append("at line: ");
			buffer.append(locator.getLineNumber());
			buffer.append(" and column: ");
			buffer.append(locator.getColumnNumber());
		}

		return buffer.toString();
	}

	/**
	 * Any error is simply rethrown. Parsing will stop on any error.
	 */
	public void error(final SAXParseException exception) throws SAXException {
		throw exception;
	}

	/**
	 * Any fatal error is simply rethrown. Parsing will stop on any error.
	 */
	public void fatalError(final SAXParseException exception) throws SAXException {
		throw exception;
	}

	/**
	 * Any warning is simply rethrown. Parsing will stop on any error.
	 */
	public void warning(final SAXParseException exception) throws SAXException {
		throw exception;
	}

	/**
	 * The stack is used to store temporary values when parsing.
	 */
	private Stack stack = new Stack();

	protected Stack getStack() {
		ObjectHelper.checkNotNull("field:stack", stack);
		return stack;
	}

	protected void push(final Object object) {
		this.getStack().push(object);
	}

	protected Object pop() {
		return this.getStack().pop();
	}

	protected Object peek() {
		return this.getStack().peek();
	}

	protected boolean stackIsEmpty() {
		return this.getStack().isEmpty();
	}

	/**
	 * A reference to the context for this code generation session.
	 */
	private BeanFactoryGeneratorContext beanFactoryGeneratorContext;

	protected BeanFactoryGeneratorContext getBeanFactoryGeneratorContext() {
		ObjectHelper.checkNotNull("field:beanFactoryGeneratorContext", beanFactoryGeneratorContext);
		return this.beanFactoryGeneratorContext;
	}

	public void setBeanFactoryGeneratorContext(final BeanFactoryGeneratorContext beanFactoryGeneratorContext) {
		ObjectHelper.checkNotNull("parameter:beanFactoryGeneratorContext", beanFactoryGeneratorContext);
		this.beanFactoryGeneratorContext = beanFactoryGeneratorContext;
	}
}
