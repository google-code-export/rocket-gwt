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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.type.NewNestedType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;
import rocket.util.client.Tester;
import rocket.util.client.Utilities;

/**
 * Represents a bean being constructed.
 * 
 * @author Miroslav Pokorny
 */
public class Bean{

	public Bean() {
		super();

		this.setAspects(this.createAspects());
		this.setConstructorValues( Collections.EMPTY_LIST );
		this.setProperties( Collections.EMPTY_SET );
	}
	
	/**
	 * Returns the ultimately delivered type of this bean, ie the type that is returned
	 * by the bean factory.
	 * @return The type of the bean thats actually the product of this definition.
	 */
	public Type getValueType(){		
		// if bean type is a factoryBean read get the bean's actual delivered type from the annotation.
		final Type type = this.getType();
		Type valueType = type;
		
		final GeneratorContext context = type.getGeneratorContext();
		final Type factoryBean = context.getType( Constants.FACTORY_BEAN );

		if (type.isAssignableTo(factoryBean)) {
			// locate the annotation and get the type from there...
			final List<String> factoryBeanObjectTypes = type.getMetadataValues(Constants.FACTORY_BEAN_OBJECT_TYPE);
			if (null == factoryBeanObjectTypes || factoryBeanObjectTypes.size() != 1) {
				throwFactoryBeanObjectTypeAnnotationMissing();
			}
			final String factoryBeanObjectTypeName = factoryBeanObjectTypes.get(0);
			valueType = context.getType(factoryBeanObjectTypeName);
		}
		
		return valueType;
	}
	
	protected void throwFactoryBeanObjectTypeAnnotationMissing() {
		throw new BeanFactoryGeneratorException("Unable to find \"" + Constants.FACTORY_BEAN_OBJECT_TYPE
				+ "\" annotation on the factoryBean type declared for bean: " + this );
	}
	
	/**
	 * The id of the bean
	 */
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * Will be true if this bean is a singleton
	 */
	private boolean singleton;

	public boolean isSingleton() {
		return this.singleton;
	}

	public void setSingleton(final boolean singleton) {
		this.singleton = singleton;
	}

	/**
	 * A flag which when true indicates that this singleton( its got to be a
	 * singleton ) is eagerly / non lazily loaded.
	 */
	private boolean eagerLoaded;

	public boolean isEagerLoaded() {
		return this.eagerLoaded;
	}

	public void setEagerLoaded(final boolean eagerLoaded) {
		this.eagerLoaded = eagerLoaded;
	}

	/**
	 * The class or type name of the bean.
	 */
	private String typeName;

	public String getTypeName() {
		Checker.notNull("field:typeName", typeName);
		return this.typeName;
	}

	public void setTypeName(final String typeName) {
		Checker.notNull("typeName:typeName", typeName);
		this.typeName = typeName;
	}

	/**
	 * The name of the factory (static) method
	 */
	private String factoryMethod;

	public String getFactoryMethod() {
		return this.factoryMethod;
	}

	public void setFactoryMethod(final String factoryMethod) {
		this.factoryMethod = factoryMethod;
	}

	/**
	 * The Method of the init (instance) method upon the bean.
	 */
	private String initMethod;

	public String getInitMethod() {
		return this.initMethod;
	}

	public void setInitMethod(final String initMethod) {
		this.initMethod = initMethod;
	}

	/**
	 * The Method of the destroy (instance) method upon the bean.
	 */
	private String destroyMethod;

	public String getDestroyMethod() {
		return this.destroyMethod;
	}

	public void setDestroyMethod(final String destroyMethod) {
		this.destroyMethod = destroyMethod;
	}

	/**
	 * A list of all constructor values if any exist.
	 */
	private List<Value> constructorValues;

	public List<Value> getConstructorValues() {
		Checker.notNull("field:constructorValues", constructorValues);
		return this.constructorValues;
	}

	public void setConstructorValues(final List<Value> constructorValues) {
		Checker.notNull("parameter:constructorValues", constructorValues);
		this.constructorValues = constructorValues;
	}

	/**
	 * A set of property for this bean.
	 */
	private Set<Property> properties;

	public Set<Property> getProperties() {
		Checker.notNull("field:properties", properties);
		return this.properties;
	}

	public void setProperties(final Set<Property> properties) {
		Checker.notNull("parameter:properties", properties);
		this.properties = properties;
	}

	/**
	 * The type of the bean
	 */
	private Type type;

	public Type getType() {
		Checker.notNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		Checker.notNull("parameter:type", type);
		this.type = type;
	}

	/**
	 * The factory type being created
	 */
	private NewNestedType factoryBean;

	public NewNestedType getFactoryBean() {
		Checker.notNull("field:factoryBean", factoryBean);
		return this.factoryBean;
	}

	public void setFactoryBean(final NewNestedType factoryBean) {
		Checker.notNull("parameter:factoryBean", factoryBean);
		this.factoryBean = factoryBean;
	}

	/**
	 * A list of aspects that apply to this bean.
	 */
	private List<Aspect> aspects;

	public List<Aspect> getAspects() {
		Checker.notNull("field:aspects", aspects);
		return this.aspects;
	}

	protected void setAspects(final List<Aspect> aspects) {
		Checker.notNull("parameter:aspects", aspects);
		this.aspects = aspects;
	}

	protected List<Aspect> createAspects() {
		return new ArrayList<Aspect>();
	}

	public void addAspect(final Aspect aspect) {
		Checker.notNull("parameter:aspect", aspect);

		this.getAspects().add(aspect);
	}

	/**
	 * Contains the generated proxy for this bean
	 */
	private NewNestedType proxy;

	public NewNestedType getProxy() {
		Checker.notNull("field:proxy", proxy);
		return this.proxy;
	}

	public boolean hasProxy() {
		return null != proxy;
	}

	public void setProxy(final NewNestedType proxy) {
		Checker.notNull("parameter:proxy", proxy);
		this.proxy = proxy;
	}

	/**
	 * If a proxy has been generated a proxy factory bean will also exist
	 */
	private NewNestedType proxyFactoryBean;

	public NewNestedType getProxyFactoryBean() {
		Checker.notNull("field:proxyFactoryBean", proxyFactoryBean);
		return this.proxyFactoryBean;
	}

	public void setProxyFactoryBean(final NewNestedType proxyFactoryBean) {
		Checker.notNull("parameter:proxyFactoryBean", proxyFactoryBean);
		this.proxyFactoryBean = proxyFactoryBean;
	}

	/**
	 * The source file that contained the bean definition.
	 */
	private String filename;
	
	protected String getFilename(){
		Checker.notEmpty("field:filename", filename );
		return this.filename;
	}
	
	public void setFilename( final String filename ){
		Checker.notEmpty("parameter:filename", filename );
		this.filename = filename;
	}
	
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		
		buf.append( "Bean " );
		
		if( false == Tester.isNullOrEmpty( this.id )){
			buf.append( "id: ");
			buf.append( Utilities.quotedEscape(this.id) );
			buf.append( ", ");
		}
		
		if( false == Tester.isNullOrEmpty( this.typeName ) && null == this.type ){
			buf.append( "typeName: ");
			buf.append( Utilities.quotedEscape(this.typeName ));
			buf.append( ", ");
		}
		
		
		if( null != this.type ){
			buf.append( this.type );
			buf.append( ", ");
		}
		
		if( this.isSingleton() ){
			buf.append( this.isEagerLoaded() ? "eager" : "lazy");
			buf.append( " singleton, ");
		} else {
			buf.append( "prototype");
		}
		
		return buf.toString();
	}
}
