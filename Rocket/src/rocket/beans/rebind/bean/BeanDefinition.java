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
import rocket.beans.rebind.newinstance.NewInstance;
import rocket.beans.rebind.property.PropertyDefinition;
import rocket.beans.rebind.property.PropertyNameAlreadyUsedException;
import rocket.beans.rebind.property.PropertyNotFoundException;
import rocket.beans.rebind.values.PropertyValueDefinition;
import rocket.generator.rebind.RebindHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Holds meta data about a bean definied with the xml file. It also is able to
 * generate methods that will result in the creation of the bean, its properties
 * and so on.
 * 
 * @author Miroslav Pokorny
 */
public class BeanDefinition extends HasBeanFactoryGeneratorContext{

	public BeanDefinition() {
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
		return Constants.FACTORY_METHOD_PREFIX + this.getId() + Constants.FACTORY_METHOD_SUFFIX;
	}

	/**
	 * Generates a method that will create a new BeanFactory for this bean
	 * definition.
	 * 
	 * @param writer
	 */
	public void write(final SourceWriter writer) {
		writer.println("protected " + FactoryBean.class.getName() + " " + this.getBeanFactoryMethodName() + "(){");
		writer.indent();
		writer.indent();

		final String factoryBeanType = this.getFactoryBeanSuperClass();

		final StringBuilder beanFactoryDeclaration = new StringBuilder();
		beanFactoryDeclaration.append("final ");
		beanFactoryDeclaration.append(factoryBeanType);
		beanFactoryDeclaration.append(" factoryBean = new ");
		beanFactoryDeclaration.append(factoryBeanType);
		beanFactoryDeclaration.append("(){");
		writer.println(beanFactoryDeclaration.toString());

		writer.indent();
		this.getNewInstance().write(writer );
		this.writeBeanFactorySatisfyProperties(writer);
		this.getInitMethod().write(writer);
		writer.outdent();

		writer.println("};"); // close inner class
		
		writer.println("factoryBean.setBeanFactory( this );");
		writer.println("return factoryBean;");
		writer.outdent();
		writer.outdent();
		writer.println("}");
	}

	protected String getFactoryBeanSuperClass() {
		return this.isSingleton() ? SingletonFactoryBean.class.getName() : PrototypeFactoryBean.class.getName();
	}

	/**
	 * This generator will generate code that creates a new bean instance.
	 */
	private NewInstance newInstance;

	protected NewInstance getNewInstance(){
		ObjectHelper.checkNotNull("field:newInstance", newInstance );
		return this.newInstance;
	}
	
	public void setNewInstance( final NewInstance newInstance ){
		ObjectHelper.checkNotNull("parameter:newInstance", newInstance );
		this.newInstance = newInstance;
	}
	
	private InitMethod initMethod;
	
	public InitMethod getInitMethod(){
		ObjectHelper.checkNotNull("field:initMethod", initMethod);
		return initMethod;
	}
	
	public void setInitMethod(final InitMethod initMethod ){
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

		// cast instance to its type.
		final String type = this.getType().getQualifiedSourceName();
		writer.println("final " + type + " " + instance0 + " = (" + type + ") " + instance + ";");

		final Iterator properties = this.getProperties().values().iterator();
		while (properties.hasNext()) {
			final PropertyDefinition property = (PropertyDefinition) properties.next();
			final String setterName = RebindHelper.getSetterName((String) property.getName());
			final PropertyValueDefinition propertyValue = (PropertyValueDefinition) property.getPropertyValueDefinition();

			final StringBuilder statement = new StringBuilder();
			statement.append(instance0);
			statement.append(".");
			statement.append(setterName);
			statement.append("(");

			statement.append(propertyValue.generatePropertyValueCodeBlock());

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
	public void addProperty(final PropertyDefinition property) {
		final String propertyName = property.getName();
		final JClassType type = this.getType();
		final String setterMethodName = BeansHelper.createSetterName(propertyName);

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
		// if possible make sure the setterMethod parameter is compatible with
		// the property
		final PropertyValueDefinition valueDefinition = property.getPropertyValueDefinition();
		valueDefinition.setType(setterMethod.getParameters()[0].getType());
		if (false == valueDefinition.isCompatibleWith()) {
			throwPropertySetterNotFoundException(propertyName);
		}

		// setter found...make sure property not already set.
		final Map properties = this.getProperties();
		if (properties.containsKey(propertyName)) {
			this.throwPropertyAlreadyUsedException(propertyName);
		}
		// everythings ok save the property.
		properties.put(propertyName, property);
	}

	protected void throwPropertySetterNotFoundException(final String propertyName) {
		throw new PropertyNotFoundException(propertyName);
	}

	protected void throwPropertyAlreadyUsedException(final String propertyName) {
		throw new PropertyNameAlreadyUsedException("An attempt is being made to add a second value for the property [" + propertyName + "]");
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
		return BeansHelper.isSingleton( this.getScope() );
	}

	public void setScope(final String scope) {
		BeansHelper.checkScope("parameter:scope", scope);
		this.scope = scope;
	}

	/**
	 * The JClassType that corresponds to the BeanFactory being generated.
	 */
	private JClassType type;

	public JClassType getType() {
		ObjectHelper.checkNotNull("field:type", type);
		return type;
	}

	public void setType(final JClassType type) {
		ObjectHelper.checkNotNull("parameter:type", type);
		this.type = type;
	}
}
