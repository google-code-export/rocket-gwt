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

package rocket.beans.rebind.bean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rocket.beans.client.FactoryBean;
import rocket.beans.client.PrototypeFactoryBean;
import rocket.beans.client.SingletonFactoryBean;
import rocket.beans.rebind.BeansHelper;
import rocket.beans.rebind.Constants;
import rocket.beans.rebind.HasBeanFactoryGeneratorContext;
import rocket.beans.rebind.init.InitMethod;
import rocket.beans.rebind.newinstance.NewInstanceProvider;
import rocket.beans.rebind.property.Property;
import rocket.beans.rebind.property.PropertyNameAlreadyUsedException;
import rocket.beans.rebind.property.PropertyNotFoundException;
import rocket.beans.rebind.values.Value;
import rocket.generator.rebind.CodeGenerator;
import rocket.generator.rebind.RebindHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Holds meta data about a bean definied with the xml file. It also is able to
 * generate methods that will result in the creation of the bean, its properties
 * and so on.
 * 
 * @author Miroslav Pokorny
 */
public class Bean extends HasBeanFactoryGeneratorContext implements
		CodeGenerator {

	public Bean() {
		super();
		this.setProperties(this.createPropertiesMap());
	}

	/**
	 * Returns the name of the factory method that will be written by
	 * {@link #write}
	 * 
	 * @return
	 */
	public String getBeanFactoryMethodName() {
		return Constants.FACTORY_METHOD_PREFIX + this.getId()
				+ Constants.FACTORY_METHOD_SUFFIX;
	}

	/**
	 * Generates a method that will create a new BeanFactory for this bean
	 * definition.
	 * 
	 * @param writer
	 */
	public void write(final SourceWriter writer) {
		this.checkType();

		final String factoryBean = "factoryBean";
		
		writer.println("protected " + FactoryBean.class.getName() + " "
				+ this.getBeanFactoryMethodName() + "(){");
		writer.indent();
		writer.indent();

		final String factoryBeanType = this.getFactoryBeanSuperClass();

		final StringBuilder beanFactoryDeclaration = new StringBuilder();
		beanFactoryDeclaration.append("final ");
		beanFactoryDeclaration.append(factoryBeanType);
		beanFactoryDeclaration.append(" " );
		beanFactoryDeclaration.append( factoryBean );
		beanFactoryDeclaration.append("= new ");
		beanFactoryDeclaration.append(factoryBeanType);
		beanFactoryDeclaration.append("(){");
		writer.println(beanFactoryDeclaration.toString());

		writer.indent();
		this.getNewInstanceProvider().write(writer);
		this.writeBeanFactorySatisfyProperties(writer);
		this.getInitMethod().write(writer);
		writer.outdent();

		writer.println("};"); // close inner class

		writer.println( factoryBean + ".setBeanFactory( this );");
		writer.println("return " + factoryBean + ";");
		writer.outdent();
		writer.outdent();
		writer.println("}");
	}

	protected void checkType() {
		final String className = this.getTypeName();
		final JClassType type = (JClassType) this.getBeanFactoryGeneratorContext().findType(className);
		this.checkType0( type );
	}
	
	protected void checkType0( final JClassType type ){
		if (null == type) {
			this.throwBeanTypeNotFoundException();
		}

		if (type.isAbstract() || type.isInterface() != null) {
			throwBeanTypeMustBeConcrete();
		}
	}

	protected void throwBeanTypeNotFoundException() {
		throw new BeanTypeNotFoundException("PlaceHolderBean type [" + this.getTypeName() + "] not found.");
	}

	protected void throwBeanTypeMustBeConcrete() {
		throw new BeanTypeNotConcreteException("The bean with an id of [" + this.getId() 
				+ "] and scope[" + this.getScope() + "] type [" + this.getTypeName()
				+ "] is not a concrete class ");
	}

	protected String getFactoryBeanSuperClass() {
		return this.isSingleton() ? SingletonFactoryBean.class.getName()
				: PrototypeFactoryBean.class.getName();
	}

	/**
	 * This generator will generate code that creates a new bean instance.
	 */
	private NewInstanceProvider newInstanceProvider;

	public NewInstanceProvider getNewInstanceProvider() {
		ObjectHelper.checkNotNull("field:newInstanceProvider", newInstanceProvider);
		return this.newInstanceProvider;
	}

	public void setNewInstanceProvider(final NewInstanceProvider newInstanceProvider) {
		ObjectHelper.checkNotNull("parameter:newInstanceProvider", newInstanceProvider);
		this.newInstanceProvider = newInstanceProvider;
	}

	private InitMethod initMethod;

	public InitMethod getInitMethod() {
		ObjectHelper.checkNotNull("field:initMethod", initMethod);
		return initMethod;
	}

	public void setInitMethod(final InitMethod initMethod) {
		ObjectHelper.checkNotNull("parameter:initMethod", initMethod);
		this.initMethod = initMethod;
	}

	/**
	 * This method writes out property setters for each of the properties found
	 * in this bean definition.
	 * 
	 * Primitives and String properties will have their values written as
	 * literals whilst object references will be satisfied by calls to a bean
	 * factory. Collection types such as List, Set and Map will be satisfied by
	 * the generation of further helper methods each populating a new List, Set
	 * or Map.
	 * 
	 * @param writer
	 */
	protected void writeBeanFactorySatisfyProperties(final SourceWriter writer) {
		final String instance = "instance";

		writer.println("protected void satisfyProperties( final " + Object.class.getName() + " " + instance + "){");
		writer.indent();

		this.writeBeanFactorySatisfyProperty0(writer);

		writer.outdent();
		writer.println("}");
	}

	protected void writeBeanFactorySatisfyProperty0(final SourceWriter writer) {
		final String instance = "instance";
		final String instance0 = instance + '0';

		// cast instance to its typeName.
		final String typeName = this.getTypeName();
		writer.println("final " + typeName + " " + instance0 + " = (" + typeName + ") " + instance + ";");

		final JClassType type = (JClassType) this.getBeanFactoryGeneratorContext().getType(this.getTypeName());
		
		final Iterator properties = this.getProperties().values().iterator();
		while (properties.hasNext()) {
			final Property property = (Property) properties.next();
			final String setterMethodName = RebindHelper.getSetterName((String) property.getName());
			final Value propertyValue = (Value) property.getValue();

			final String propertyName = property.getName();

			JMethod setterMethod = null;
			final JMethod[] methods = type.getMethods();
			for (int i = 0; i < methods.length; i++) {
				final JMethod method = methods[i];
				if (method.getName().equals(setterMethodName)) {
					setterMethod = method;
					break;
				}
			}
			// if a setter is not found...
			if (null == setterMethod || setterMethod.getParameters().length != 1) {
				throwPropertySetterNotFoundException("Setter for [" + propertyName + "] not found");
			}

			// update the value type...
			final Value value = property.getValue();
			final JType valueType = setterMethod.getParameters()[0].getType();
			value.setType( valueType );
			if (false == value.isCompatibleWith()) {
				throwPropertySetterNotFoundException(propertyName);
			}

			final StringBuilder statement = new StringBuilder();
			statement.append(instance0);
			statement.append(".");
			statement.append(setterMethodName);
			statement.append("(");

			statement.append(propertyValue.generateValue());

			statement.append(");");
			writer.println(statement.toString());
		}
	}

	/**
	 * Adds a new property definition for this bean. Typically this is done
	 * whilst parsing the xml file.
	 * 
	 * @param property
	 */
	public void addProperty(final Property property) {
		final String name = property.getName();

		final Map properties = this.getProperties();
		if (properties.containsKey(name)) {
			this.throwPropertyAlreadyUsedException(name);
		}
		// everythings ok save the property.
		properties.put(name, property);
	}

	protected void throwPropertySetterNotFoundException(
			final String propertyName) {
		throw new PropertyNotFoundException(propertyName);
	}

	protected void throwPropertyAlreadyUsedException(final String propertyName) {
		throw new PropertyNameAlreadyUsedException(
				"An attempt is being made to add a second value for the property ["
						+ propertyName + "]");
	}

	/**
	 * A map that accumulates all the properties defined for this bean.
	 */
	private Map properties;

	protected Map getProperties() {
		ObjectHelper.checkNotNull("field:properties", properties);
		return this.properties;
	}

	protected void setProperties(final Map properties) {
		ObjectHelper.checkNotNull("parameter:properties", properties);
		this.properties = properties;
	}

	protected Map createPropertiesMap() {
		return new HashMap();
	}

	/**
	 * The name or id of the bean
	 */
	private String id;

	public String getId() {
		StringHelper.checkNotEmpty("field:id", id);
		return id;
	}

	public void setId(final String id) {
		StringHelper.checkNotEmpty("parameter:id", id);
		this.id = id;
	}

	/**
	 * The name or scope of the bean
	 */
	private String scope;

	public String getScope() {
		BeansHelper.checkScope("field:scope", scope);
		return scope;
	}

	protected boolean isSingleton() {
		return BeansHelper.isSingleton(this.getScope());
	}

	public void setScope(final String scope) {
		BeansHelper.checkScope("parameter:scope", scope);
		this.scope = scope;
	}

	/**
	 * The typeName name of the bean.
	 */
	private String typeName;

	public String getTypeName() {
		ObjectHelper.checkNotNull("field:typeName", typeName);
		return typeName;
	}

	public void setTypeName(final String typeName) {
		ObjectHelper.checkNotNull("parameter:typeName", typeName);
		this.typeName = typeName;
	}
}
