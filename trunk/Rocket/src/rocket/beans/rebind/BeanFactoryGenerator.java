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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import rocket.beans.client.FactoryBean;
import rocket.beans.rebind.alias.Alias;
import rocket.beans.rebind.alias.GetAliasesToBeans;
import rocket.beans.rebind.aop.MethodMatcher;
import rocket.beans.rebind.aop.MethodMatcherFactory;
import rocket.beans.rebind.aop.addadvice.Advice;
import rocket.beans.rebind.aop.createproxy.CreateProxyTemplatedFile;
import rocket.beans.rebind.aop.gettargetfactorybean.GetTargetFactoryBeanTemplatedFile;
import rocket.beans.rebind.aop.proxyinterceptedmethod.ProxyInterceptedMethodTemplatedFile;
import rocket.beans.rebind.aop.proxymethod.ProxyMethodTemplatedFile;
import rocket.beans.rebind.beanreference.BeanReference;
import rocket.beans.rebind.constructor.ConstructorTemplatedFile;
import rocket.beans.rebind.deferredbinding.DeferredBinding;
import rocket.beans.rebind.factorymethod.FactoryMethodTemplatedFile;
import rocket.beans.rebind.invokemethod.InvokeMethodTemplatedFile;
import rocket.beans.rebind.list.ListValue;
import rocket.beans.rebind.loadeagersingletons.GetEagerSingletonBeanNames;
import rocket.beans.rebind.map.MapValue;
import rocket.beans.rebind.properties.SetPropertiesTemplatedFile;
import rocket.beans.rebind.registerfactorybeans.RegisterFactoryBeansTemplatedFile;
import rocket.beans.rebind.set.SetValue;
import rocket.beans.rebind.stringvalue.StringValue;
import rocket.beans.rebind.value.Value;
import rocket.beans.rebind.xml.AdviceTag;
import rocket.beans.rebind.xml.AliasTag;
import rocket.beans.rebind.xml.BeanFactoryDtdEntityResolver;
import rocket.beans.rebind.xml.BeanReferenceTag;
import rocket.beans.rebind.xml.BeanTag;
import rocket.beans.rebind.xml.DocumentWalker;
import rocket.beans.rebind.xml.ListTag;
import rocket.beans.rebind.xml.MapTag;
import rocket.beans.rebind.xml.PropertyTag;
import rocket.beans.rebind.xml.RemoteJsonServiceTag;
import rocket.beans.rebind.xml.RemoteRpcServiceTag;
import rocket.beans.rebind.xml.RethrowSaxExceptionsErrorHandler;
import rocket.beans.rebind.xml.SetTag;
import rocket.beans.rebind.xml.StringTag;
import rocket.beans.rebind.xml.ValueTag;
import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.EmptyCodeBlock;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.constructor.NewConstructor;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewNestedType;
import rocket.generator.rebind.type.NewType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.TypeConstructorsVisitor;
import rocket.generator.rebind.visitor.VirtualMethodVisitor;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * This code generator generates a BeanFactory which will create or provide the
 * beans defined in an xml file.
 * 
 * A standalone class is created to implement the BeanFactory. Within this
 * BeanFactory private nested inner FactoryBean classes are created for each
 * bean and proxy required to implement configured advices.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGenerator extends Generator {

	public BeanFactoryGenerator() {
	}
	
	/**
	 * Prepares to transform a xml file into a BeanFactory with beans.
	 * 
	 * @param type
	 *            The type passed to GWT.create()
	 * @param newTypeName
	 *            The name of the type being generated.
	 */
	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {				
		this.verifyBeanFactory(type);

		final DocumentWalker document = this.getDocumentWalker(type);

		final NewConcreteType beanFactory = this.createBeanFactory(newTypeName, type );
		this.setBeanFactory(beanFactory);

		final List beanTags = document.getBeans();
		this.setBeans(this.createBeans());
		this.buildFactoryBeans(beanTags);

		final List aliases = document.getAliases();
		this.setAliases( this.createAliases() );
		this.recordAliases( aliases );
		
		this.buildJsonRpcServiceFactoryBeans(document.getJsonRpcServices());
		this.buildJavaRpcServiceFactoryBeans(document.getJavaRpcServices());		
		
		this.overrideAllFactoryBeanCreateInstances(beanTags);
		this.overrideAllFactoryBeanSatisfyInits(beanTags);
		this.overrideAllFactoryBeanSatisfyProperties(beanTags);
		this.overrideAllSingletonFactoryBeanToInvokeCustomDestroy(beanTags);
		
		this.buildAdvices(document.getAdvices());
		this.applyAdvices();

		this.overrideBeanFactoryRegisterFactoryBeans();
		this.registerBeanAliases();
		this.overrideLoadEagerBeans();
		
		return beanFactory;
	}

	/**
	 * Factory method which creates a walker that may be used to walk around the
	 * document.
	 * 
	 * @param type
	 * @return
	 */
	protected DocumentWalker getDocumentWalker(final Type type) {
		final String fileName = this.getResourceName(type, Constants.BEAN_FILE_SUFFIX);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info( "Preparing to discover components within xml file(s)");
		
		final DocumentWalker document = new DocumentWalker();
		document.setEntityResolver(new BeanFactoryDtdEntityResolver());
		document.setErrorHandler(new RethrowSaxExceptionsErrorHandler());
		document.setGenerator(this);
		document.process(fileName);
		
		context.unbranch();
		
		return document;
	}

	/**
	 * Verifies and throws an exception if the given type is a BeanFactory
	 * 
	 * @param The
	 *            bean type interface
	 */
	protected void verifyBeanFactory(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.info("Verifying " + type + " is a bean factory.");

		final Type beanFactory = this.getBeanFactoryType();
		if (false == type.isAssignableTo(beanFactory)) {
			throwNotABeanFactory("The type \"" + type + "\" is not a " + beanFactory);
		}

		// verify has no public methods (ignore those belonging to
		// java.lang.Object
		final List publicMethods = new ArrayList();
		final VirtualMethodVisitor methodFinder = new VirtualMethodVisitor() {
			protected boolean visit(final Method method) {
				publicMethods.add(method);
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};
		methodFinder.start(type);
		if (false == publicMethods.isEmpty()) {
			throwBeanFactoryInterfaceHasPublicMethods(type, publicMethods.size());
		}
	}

	protected void throwNotABeanFactory(final String message) {
		throw new BeanFactoryGeneratorException(message);
	}

	protected void throwBeanFactoryInterfaceHasPublicMethods(final Type beanFactory, final int publicMethodCount) {
		throw new BeanFactoryGeneratorException("The bean factory interface \"" + beanFactory + "\" contains " + publicMethodCount
				+ " when it should contain 0 public methods (excluding those on java.lang.Object ignored in both cases).");
	}

	/**
	 * Factory method which creates a new BeanFactory read to accept methods and
	 * fields etc.
	 * 
	 * @param newTypeName
	 * @param implementsInterface
	 * @return
	 */
	protected NewConcreteType createBeanFactory(final String newTypeName, final Type implementsInterface ) {
		this.getGeneratorContext().info("Creating BeanFactory with a name of \"" + newTypeName + "\".");

		final NewConcreteType beanFactory = this.getGeneratorContext().newConcreteType( newTypeName );

		beanFactory.setAbstract(false);
		beanFactory.setFinal(true);
		beanFactory.setSuperType(this.getBeanFactoryImpl());
		beanFactory.setVisibility( Visibility.PUBLIC );
		beanFactory.addInterface( implementsInterface );

		return beanFactory;
	}

	/**
	 * Walks all the bean tags building a NewNestedType FactoryBean for each tag
	 * encountered.
	 * 
	 * @param beans A list of bean tags
	 */
	protected void buildFactoryBeans(final List beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Creating FactoryBean's for all beans.");

		final Iterator iterator = beans.iterator();
		while (iterator.hasNext()) {
			final BeanTag bean = (BeanTag)iterator.next();
			
			context.branch();
			context.debug( bean.getId() );
			
			this.createFactoryBean( bean);
			
			context.unbranch();
		}
		
		context.unbranch();
	}

	/**
	 * Creates the factory bean that will be responsible for producing beans on demand.
	 * @param beanTag The source bean tag.
	 */
	protected void createFactoryBean(final BeanTag beanTag) {
		ObjectHelper.checkNotNull("parameter:bean", beanTag);

		final GeneratorContext context = this.getGeneratorContext();
		
		final Bean bean = new Bean();
		final String id = beanTag.getId();
		bean.setId(id);
		
		this.checkBean( bean );					

		final String className = beanTag.getClassName();
		final Type beanType = this.getConcreteType(id, className);
		bean.setType(beanType);
		
		context.debug( beanType.toString() );

		final boolean singleton = beanTag.isSingleton();
		bean.setSingleton(singleton);
		context.debug( singleton ? "singleton" : "prototype");
		
		final boolean eager = beanTag.isEagerLoaded();
		if( false == singleton ){
			throwPrototypesCantBeEagerlyLoaded( bean );
		}
		bean.setEagerLoad( eager );
		
		if( singleton ){
			context.debug( eager ? "eager" : "lazyloaded");
		}
		
		if( false == singleton ){
			final String destroyMethod = beanTag.getDestroyMethod();			
			if( false == StringHelper.isNullOrEmpty(destroyMethod )){
				throwPrototypeCantBeContainerDestroyed( bean );
			}
			
			final Type disposableBean = this.getDisposableBean();
			if( beanType.isAssignableTo( disposableBean )){
				context.warn( "Ignoring the fact the bean implements DisposableBean (because its a prototype)" + ( context.isDebugEnabled() ? "." : ", bean: " + bean ));
			}
		}
		
		// start creating the factory bean...
		final Type superType = singleton ? this.getSingletonFactoryBean() : this.getPrototypeFactoryBean();
		final NewConcreteType beanFactory = this.getBeanFactory();
		final NewNestedType factoryBean = beanFactory.newNestedType();
		factoryBean.setStatic(false);
		factoryBean.setNestedName(this.escapeBeanIdToBeClassNameSafe(id) + Constants.FACTORY_BEAN_SUFFIX);
		factoryBean.setSuperType(superType);
		factoryBean.setVisibility(Visibility.PRIVATE);
		
		// add an annotation that declares the actual bean type.
		factoryBean.addMetaData( Constants.FACTORY_BEAN_OBJECT_TYPE, bean.getType().getName() );
		
		bean.setFactoryBean(factoryBean);
		context.debug("FactoryBean superType: " + superType );

		this.addBean(bean);
	}

	protected void throwPrototypesCantBeEagerlyLoaded( final Bean bean ){
		throw new BeanFactoryGeneratorException("Prototype beans cannot be eagerly loaded (only singleton's can be), bean: " + bean );
	}

	/**
	 * Without something in javascript equivalent to soft references supporting disposing prototypes would create a memory leak,
	 * because the container needs a reference to the bean. 
	 * @param bean The bean
	 */
	protected void throwPrototypeCantBeContainerDestroyed( final Bean bean ){
		throw new BeanFactoryGeneratorException("The container does not support disposing prototype beans(only singleton's ), bean: " + bean );
	}
	
	/**
	 * Visit all defined beans and overrides the createInstance of the factory
	 * bean.
	 * 
	 * A constructor with parameters or factory bean may be the source of the
	 * new bean instance.
	 * 
	 * @param bean A list of bean tag elements straight from any included xml documents.
	 */
	protected void overrideAllFactoryBeanCreateInstances(final List beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Overriding createInstance methods for all bean(s).");

		final Iterator iterator = beans.iterator();
		while (iterator.hasNext()) {
			final BeanTag bean = (BeanTag) iterator.next();
			context.branch();
			context.debug( bean.getId() );
			
			this.overrideFactoryBeanCreateInstance( bean );
			
			context.unbranch();
		}
		
		context.unbranch();
	}

	/**
	 * Depending on the bean tag overrides the appropriate
	 * {@link FactoryBean#createInstance()} with an inline constructor call or
	 * inserts a call to a static factory method.
	 * 
	 * @param beanTag
	 *            The bean
	 */
	protected void overrideFactoryBeanCreateInstance(final BeanTag beanTag) {
		ObjectHelper.checkNotNull("parameter:bean", beanTag);

		final String factoryMethodName = beanTag.getFactoryMethod();
		final Bean bean = this.getBean(beanTag.getId());

		if (StringHelper.isNullOrEmpty(factoryMethodName)) {
			final List constructorArguments = beanTag.getConstructorArguments();
			this.overrideFactoryBeanCreateInstanceNewConstructor(bean, constructorArguments);
		} else {
			this.overrideFactoryBeanCreateInstanceFactoryMethod(bean, factoryMethodName);
		}
	}

	/**
	 * Override the createInstance method of the given factory bean to invoke a
	 * constructor
	 * 
	 * @param bean
	 *            The bean
	 * @param constructorArguments
	 *            a list containing the constructor arguments
	 */
	protected void overrideFactoryBeanCreateInstanceNewConstructor(final Bean bean, final List constructorArguments) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		ObjectHelper.checkNotNull("parameter:constructorArguments", constructorArguments);
		
		final NewMethod newFactoryMethod = this.createCreateInstanceMethod(bean.getFactoryBean());
		newFactoryMethod.setAbstract(false);
		newFactoryMethod.setFinal(true);
		newFactoryMethod.setNative(false);

		final ConstructorTemplatedFile body = new ConstructorTemplatedFile();
		newFactoryMethod.setBody(body);

		// make a list of values from constructorArguments.
		final List constructorValues = new ArrayList();
		final Iterator argumentIterator = constructorArguments.iterator();
		
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();		
		context.debug( "Constructor arguments.");
		
		while (argumentIterator.hasNext()) {
			final Value value = this.asValue((ValueTag) argumentIterator.next());
			constructorValues.add(value);
			body.addArgument(value);
			
			context.debug( "" + value );
		}

		context.unbranch();
		
		context.branch();
		context.debug("Matching constructors.");
		
		final List matchingConstructors = new ArrayList();
		final TypeConstructorsVisitor visitor = new TypeConstructorsVisitor() {

			protected boolean visit(final Constructor constructor) {
				boolean match = false;

				// skip non public constructors
				if (constructor.getVisibility() == Visibility.PUBLIC) {
					final List constructorParameters = constructor.getParameters();

					// skip the given constructor with different numbers of
					// parameters.
					if (constructorParameters.size() == constructorValues.size()) {

						match = true;
						final Iterator valuesIterator = constructorValues.iterator();
						final Iterator parametersIterator = constructorParameters.iterator();

						while (valuesIterator.hasNext()) {
							final ConstructorParameter parameter = (ConstructorParameter) parametersIterator.next();
							final Value value0 = (Value) valuesIterator.next();
							if (false == value0.isCompatibleWith(parameter.getType())) {
								match = false;
							}
						}
					}
				}

				if (match) {
					matchingConstructors.add(constructor);
					context.debug( "" + constructor );
				}
				return false;
			}
		};

		final Type beanType = bean.getType();
		visitor.start(beanType);

		if (matchingConstructors.size() == 0) {
			this.throwUnableToFindConstructor(bean);
		}
		if (matchingConstructors.size() > 1) {
			this.throwTooManyConstructors(bean, matchingConstructors);
		}
		context.unbranch();
		
		final Constructor constructor = (Constructor) matchingConstructors.get(0);
		body.setBean(constructor);

		final Iterator constructorParameters = constructor.getParameters().iterator();
		final Iterator valuesIterator = constructorValues.iterator();
		while (constructorParameters.hasNext()) {
			final ConstructorParameter constructorParameter = (ConstructorParameter) constructorParameters.next();
			final Value value = (Value) valuesIterator.next();
			value.setType(constructorParameter.getType());
		}
		
		context.debug( "" + constructor );
	}

	/**
	 * This method throws an exception when more than one constructor satisfies
	 * the specified values for a bean type
	 * 
	 * @param bean The bean
	 * @param constructors A list containing the matching constructors.
	 */
	protected void throwTooManyConstructors(final Bean bean, final List constructors) {
		throw new BeanFactoryGeneratorException("Found more than one constructor that matches the specified constructor arguments: " + constructors + ", bean: "
				+ bean);
	}

	/**
	 * This method throws an exception when a suitable constructor cannot be
	 * found that matches the bean's constructor parameters or lack of them.
	 * 
	 * @param bean
	 */
	protected void throwUnableToFindConstructor(final Bean bean) {
		throw new BeanFactoryGeneratorException( "Unable to find a constructor that is suitable for the specified constructor arguments for bean: " + bean);
	}

	/**
	 * Overrides the createInstance method of the given factory bean to invoke a
	 * static factory method.
	 * 
	 * @param bean
	 *            A bean definition
	 * @param factoryMethodName
	 *            A fully qualified class and factory method
	 */
	protected void overrideFactoryBeanCreateInstanceFactoryMethod(final Bean bean, final String factoryMethodName) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		StringHelper.checkNotEmpty("parameter:factoryMethodName", factoryMethodName);

		final Type beanType = bean.getType();
		final Method factoryMethod = beanType.findMethod(factoryMethodName, Collections.EMPTY_LIST);
		if (null == factoryMethod || false == factoryMethod.isStatic() || factoryMethod.getVisibility() != Visibility.PUBLIC) {
			this.throwFactoryMethodNotFound(bean, factoryMethodName);
		}

		this.getGeneratorContext().debug("FactoryBean will create new instance by calling " + factoryMethod );

		final NewMethod newFactoryMethod = this.createCreateInstanceMethod(bean.getFactoryBean());

		final FactoryMethodTemplatedFile body = new FactoryMethodTemplatedFile();
		body.setFactoryType(beanType);
		body.setFactoryMethod(factoryMethod);

		newFactoryMethod.setBody(body);
	}

	protected void throwFactoryMethodNotFound(final Bean bean, final String method) {
		throw new BeanFactoryGeneratorException("Unable to find a public static method factory method \"" + method + "\" on " + bean);
	}

	protected NewMethod createCreateInstanceMethod(final NewType factoryBean) {
		ObjectHelper.checkNotNull("parameter:factoryBean", factoryBean);

		final Method method = factoryBean.getMostDerivedMethod("createInstance", Collections.EMPTY_LIST);

		final NewMethod newMethod = method.copy(factoryBean);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		return newMethod;
	}

	/**
	 * Visit all defined beans and implement the satisfyInit for each factory to
	 * set the properties.
	 * 
	 * @param beans A list of beans
	 */
	protected void overrideAllFactoryBeanSatisfyInits(final List beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Overriding satisfyInit methods for beans with custom initMethods.");

		final Iterator iterator = beans.iterator();
		while (iterator.hasNext()) {
			final BeanTag beanTag = (BeanTag) iterator.next();
			final String initMethodName = beanTag.getInitMethod();

			if (false == StringHelper.isNullOrEmpty(initMethodName)) {
				final Bean bean = this.getBean(beanTag.getId());
				context.branch();
				context.debug( bean.getId() );
								
				this.overrideFactoryBeanSatisfyInit(bean, initMethodName);
				
				context.unbranch();
			}
		}
		
		context.unbranch();
	}

	/**
	 * Overrides the satisfyInit method for the factory bean to call the init
	 * method of the enclosed bean
	 * 
	 * @param bean The bean
	 * @param initMethodName The custom init method name
	 */
	protected void overrideFactoryBeanSatisfyInit(final Bean bean, final String initMethodName) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		GeneratorHelper.checkJavaMethodName("parameter:initMethodName", initMethodName);

		final Type beanType = bean.getType();
		final Method initMethod = beanType.findMethod(initMethodName, Collections.EMPTY_LIST);
		if (null == initMethod || initMethod.isStatic() || initMethod.getVisibility() != Visibility.PUBLIC) {
			throwInitMethodNotFound(bean, initMethodName);
		}

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("Overriding satisfyInit to call " + initMethod + " for bean: " + bean);

		final NewType beanFactory = bean.getFactoryBean();
		final Method beanFactoryInitMethod = beanFactory.getMostDerivedMethod(Constants.SATISFY_INIT, this.getParameterListWithOnlyObject());
		final NewMethod newMethod = beanFactoryInitMethod.copy(beanFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final InvokeMethodTemplatedFile body = new InvokeMethodTemplatedFile();
		body.setType(beanType);
		body.setMethod(initMethod);

		final NewMethodParameter instanceParameter = (NewMethodParameter)newMethod.getParameters().get( 0 );
		instanceParameter.setFinal( true );
		instanceParameter.setName( Constants.SATISFY_INIT_INSTANCE_PARAMETER );
		
		newMethod.setBody(body);
	}

	/**
	 * This method throws an exception when the specified init method does not
	 * exist, is not public or is static.
	 * 
	 * @param bean
	 * @param initMethodName
	 */
	protected void throwInitMethodNotFound(final Bean bean, final String initMethodName) {
		throw new BeanFactoryGeneratorException("Unable to find a public method called \"" + initMethodName + "\" on the bean " + bean);
	}

	/**
	 * Visit all defined beans and implement the satisfyProperties for each
	 * factory to set the properties.
	 * 
	 * @param beans
	 *            A list of beans
	 */
	protected void overrideAllFactoryBeanSatisfyProperties(final List beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Overriding satisfyProperties method for all bean(s).");

		final Iterator iterator = beans.iterator();
		while (iterator.hasNext()) {
			final BeanTag beanTag = (BeanTag) iterator.next();

			final String id = beanTag.getId();
			final Bean bean = BeanFactoryGenerator.this.getBean(id);
			final List properties = beanTag.getProperties();
			
			this.possiblyOverrideFactoryBeanSatisfyProperties(bean, properties);			
		}
		
		context.unbranch();
	}

	/**
	 * Overrides the abstract satisfyProperties with statements that set each
	 * property.
	 * 
	 * @param bean The bean.
	 * @param properties A list holding all properties for the bean
	 */
	protected void possiblyOverrideFactoryBeanSatisfyProperties(final Bean bean, final List properties) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		ObjectHelper.checkNotNull("parameter:properties", properties);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug( bean.getId() );
		
		if( properties.size() == 0 ){
			context.debug( "No properties will be set.");
		} else {
			this.overrideFactoryBeanSatisfyProperties( bean, properties );
		}
		
		context.unbranch();
	}
	
	protected void overrideFactoryBeanSatisfyProperties( final Bean bean, final List properties ){
		final GeneratorContext context = this.getGeneratorContext();
		final Type voidType = this.getGeneratorContext().getVoid();
		final Type beanType = bean.getType();
		context.debug( beanType.toString() );

		final SetPropertiesTemplatedFile body = new SetPropertiesTemplatedFile();
		body.setBean(beanType);

		final NewType factoryBean = bean.getFactoryBean();
		final Method method = factoryBean.getMostDerivedMethod(Constants.SATISFY_PROPERTIES, this.getParameterListWithOnlyObject());

		final NewMethod newMethod = method.copy(factoryBean);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final NewMethodParameter instanceParameter = (NewMethodParameter)newMethod.getParameters().get( 0 );
		instanceParameter.setFinal( true );
		instanceParameter.setName( Constants.SATISFY_PROPERTIES_INSTANCE_PARAMETER );
		
		newMethod.setBody(body);

		// loop thru all properties
		final Iterator propertyIterator = properties.iterator();
		while (propertyIterator.hasNext()) {
			final PropertyTag propertyTag = (PropertyTag) propertyIterator.next();
			final String propertyName = propertyTag.getPropertyName();
			final String setterName = GeneratorHelper.buildSetterName(propertyName);

			final Value value = this.asValue(propertyTag.getValue());
			final List matching = new ArrayList();

			final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {
				protected boolean visit(final Method method) {

					while (true) {
						// names dont match
						if (false == method.getName().equals(setterName)) {
							break;
						}
						// return type must be void
						if (false == method.getReturnType().equals(voidType)) {
							break;
						}
						// parameter types must be compatible...
						final List parameters = method.getParameters();
						if (parameters.size() != 1) {
							break;
						}

						final MethodParameter parameter = (MethodParameter) parameters.get(0);
						final Type propertyType = parameter.getType();
						if (false == value.isCompatibleWith(propertyType)) {
							break;
						}
						value.setType(propertyType);
						matching.add(method);
						break;
					}

					return false;
				}

				protected boolean skipJavaLangObjectMethods() {
					return true;
				}
			};

			visitor.start(beanType);
			if (matching.isEmpty()) {
				this.throwUnableToFindSetter(bean, propertyName);
			}
			if (matching.size() != 1) {
				this.throwTooManySettersFound(bean, propertyName);
			}

			final Method setter = (Method) matching.get(0);
			body.addProperty(setter, value);

			context.debug( propertyName + "=" + value );
		}
		
		context.debug( properties.size() + " properties set." );
	}

	/**
	 * This method throws an exception when more than one constructor satisfies
	 * the specified values for a bean type
	 * 
	 * @param bean The bean
	 * @param propertyName The property that caused the problem.
	 */
	protected void throwTooManySettersFound(final Bean bean, final String propertyName) {
		throw new BeanFactoryGeneratorException("The bean " + bean + " contains more than one setter for the property \"" + propertyName + "\".");
	}

	protected void throwUnableToFindSetter(final Bean bean, final String propertyName) {
		throw new BeanFactoryGeneratorException("Unable to find a setter for the property \"" + propertyName + "\" on the bean, bean: " + bean );
	}

	protected ListValue asListValue(final ListTag tag) {
		ObjectHelper.checkNotNull("parameter:tag", tag);

		final ListValue list = new ListValue();
		list.setGeneratorContext(this.getGeneratorContext());

		final Iterator values = tag.getValues().iterator();
		while (values.hasNext()) {
			final ValueTag valueTag = (ValueTag) values.next();
			final Value value = this.asValue(valueTag);
			list.addElement(value);
		}
		return list;
	}

	protected SetValue asSetValue(final SetTag tag) {
		ObjectHelper.checkNotNull("parameter:tag", tag);

		final SetValue set = new SetValue();
		set.setGeneratorContext(this.getGeneratorContext());

		final Iterator values = tag.getValues().iterator();
		while (values.hasNext()) {
			final ValueTag valueTag = (ValueTag) values.next();
			final Value value = this.asValue(valueTag);
			set.addElement(value);
		}
		return set;
	}

	protected MapValue asMapValue(final MapTag tag) {
		ObjectHelper.checkNotNull("parameter:tag", tag);

		final MapValue map = new MapValue();
		map.setGeneratorContext(this.getGeneratorContext());

		final Iterator entries = tag.getValues().entrySet().iterator();
		while (entries.hasNext()) {
			final Map.Entry entry = (Map.Entry) entries.next();
			final String key = (String) entry.getKey();
			final ValueTag valueTag = (ValueTag) entry.getValue();

			final Value value = this.asValue(valueTag);

			map.addEntry(key, value);
		}
		return map;
	}

	/**
	 * Converts a BeanReferenceTag into a BeanReferenceValue
	 * 
	 * @param tag
	 * @return
	 */
	protected BeanReference asBeanReference(final BeanReferenceTag tag) {
		ObjectHelper.checkNotNull("parameter:tag", tag);

		final String id = tag.getId();
		
		// if the bean type is a factoryBean read get the bean's actual type from the annotation.
		final GeneratorContext context = this.getGeneratorContext();
		final Type factoryBean = this.getFactoryBean();
		final Bean bean = this.getBean( id );
		
		Type type = bean.getType();			
		if( type.isAssignableTo( factoryBean )){
			// locate the annotation and get the type from there...
			final List factoryBeanObjectTypes = type.getMetadataValues( Constants.FACTORY_BEAN_OBJECT_TYPE );
			if( null == factoryBeanObjectTypes || factoryBeanObjectTypes.size() != 1 ){
				throwFactoryBeanObjectTypeAnnotationMissing(bean);
			}
			final String factoryBeanObjectTypeName = (String) factoryBeanObjectTypes.get( 0 ); 
			type = context.getType( factoryBeanObjectTypeName );
		}
		
		final BeanReference beanReference = new BeanReference();
		beanReference.setId(id);
		beanReference.setType(type);
		beanReference.setGeneratorContext( context );
		return beanReference;
	}
	
	protected void throwFactoryBeanObjectTypeAnnotationMissing( final Bean bean ){
		throw new BeanFactoryGeneratorException( "Unable to find \"" + Constants.FACTORY_BEAN_OBJECT_TYPE + "\" annotation on the factoryBean declared for bean: " + bean );
	}

	protected StringValue asStringValue(final StringTag tag) {
		ObjectHelper.checkNotNull("parameter:tag", tag);

		final GeneratorContext context = this.getGeneratorContext();
		final StringValue string = new StringValue();
		string.setGeneratorContext(context);
		string.setValue(tag.getLiteral());
		string.setType(context.getString());

		return string;
	}

	/**
	 * Converts the given tag into a Value.
	 * 
	 * @param valueTag
	 * @return
	 */
	protected Value asValue(final ValueTag valueTag) {
		ObjectHelper.checkNotNull("parameter:valueTag", valueTag);

		Value value = null;

		while (true) {
			if (valueTag instanceof ListTag) {
				value = this.asListValue((ListTag) valueTag);
				break;
			}
			if (valueTag instanceof SetTag) {
				value = this.asSetValue((SetTag) valueTag);
				break;
			}
			if (valueTag instanceof MapTag) {
				value = this.asMapValue((MapTag) valueTag);
				break;
			}
			if (valueTag instanceof BeanReferenceTag) {
				value = this.asBeanReference((BeanReferenceTag) valueTag);
				break;
			}

			value = this.asStringValue((StringTag) valueTag);
			break;
		}

		return value;
	}
	
	/**
	 * Visit all defined beans singling singleton beans with custom destroy methods.
	 * 
	 * @param beans A list of beans
	 */
	protected void overrideAllSingletonFactoryBeanToInvokeCustomDestroy(final List beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Visiting singleton beans with custom destroyMethods.");

		final Iterator iterator = beans.iterator();
		int singletonCount = 0;
		int customDestroyMethodCount = 0;
		
		while (iterator.hasNext()) {
			final BeanTag beanTag = (BeanTag) iterator.next();
			if( beanTag.isPrototype() ){
				continue;
			}
			
			final String destroyMethodName = beanTag.getDestroyMethod();

			if (false == StringHelper.isNullOrEmpty(destroyMethodName)) {
				final Bean bean = this.getBean(beanTag.getId());
				context.branch();
				context.debug( bean.getId() );
								
				this.overrideSingletonFactoryBeanDestroy(bean, destroyMethodName);
				
				context.unbranch();
			}
		}
		context.debug( "Singletons: " + singletonCount + ", singletons with custom destroy methods: " + customDestroyMethodCount );
		
		context.unbranch();
	}

	/**
	 * Overrides the destroy method for the given singleton factory bean to call the custom destroy method.
	 * 
	 * @param bean The bean
	 * @param destroyMethodName The custom destroy method name
	 */
	protected void overrideSingletonFactoryBeanDestroy(final Bean bean, final String destroyMethodName) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		GeneratorHelper.checkJavaMethodName("parameter:destroyMethodName", destroyMethodName);

		final Type beanType = bean.getType();
		final Method destroyMethod = beanType.findMethod(destroyMethodName, Collections.EMPTY_LIST);
		if (null == destroyMethod || destroyMethod.isStatic() || destroyMethod.getVisibility() != Visibility.PUBLIC) {
			throwCustomMethodNotFound(bean, destroyMethodName);
		}

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("Overriding destroy to call " + destroyMethod + " for bean: " + bean);

		final NewType beanFactory = bean.getFactoryBean();
		final Method beanFactoryInitMethod = beanFactory.getMostDerivedMethod(Constants.DESTROY, this.getParameterListWithOnlyObject());
		final NewMethod newMethod = beanFactoryInitMethod.copy(beanFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final InvokeMethodTemplatedFile body = new InvokeMethodTemplatedFile();
		body.setType(beanType);
		body.setMethod(destroyMethod);

		final NewMethodParameter instanceParameter = (NewMethodParameter)newMethod.getParameters().get( 0 );
		instanceParameter.setFinal( true );
		instanceParameter.setName( Constants.DESTROY_INSTANCE_PARAMETER );
		
		newMethod.setBody(body);
	}

	/**
	 * This method throws an exception when the specified custom method does not exist, is not public or is static.
	 * 
	 * @param bean
	 * @param destroyMethodName
	 */
	protected void throwCustomMethodNotFound(final Bean bean, final String destroyMethodName) {
		throw new BeanFactoryGeneratorException("Unable to find a public method called \"" + destroyMethodName + "\" on the bean " + bean);
	}

	protected void recordAliases( final List aliases ){
		ObjectHelper.checkNotNull("parameter:aliases", aliases);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Processing all alias (" + aliases.size() + ") tags.");

		final Iterator iterator = aliases.iterator();
		while (iterator.hasNext()) {
			final AliasTag aliasTag = (AliasTag) iterator.next();
			final String name = aliasTag.getName();
			final String bean = aliasTag.getBean();
			
			context.debug( name + "=" + bean );
			
			final Alias alias = new Alias();
			alias.setName( name );
			alias.setBean( bean );
			
			this.checkAlias(alias);
			
			this.addAlias(alias);
		}
		
		context.unbranch();		
	}
	
	/**
	 * Builds all that factory beans that will return a remote json service
	 * client.
	 * 
	 * @param jsonTags
	 *            A read only list containing all the json tags in the document
	 */
	protected void buildJsonRpcServiceFactoryBeans(final List jsonTags) {
		ObjectHelper.checkNotNull("parameter:jsonTags", jsonTags);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Creating FactoryBean's for each of the remote json service (" + jsonTags.size() + ") tags.");

		final Iterator iterator = jsonTags.iterator();
		while (iterator.hasNext()) {
			final RemoteJsonServiceTag json = (RemoteJsonServiceTag) iterator.next();
			this.createJsonRpcServiceFactoryBean(json);
		}
		
		context.unbranch();
	}

	/**
	 * Factory method which creates the initial json FactoryBean
	 * 
	 * @param jsonTag The json service being processed.
	 */
	protected void createJsonRpcServiceFactoryBean(final RemoteJsonServiceTag jsonTag) {
		ObjectHelper.checkNotNull("parameter:jsonTag", jsonTag);

		final GeneratorContext context = this.getGeneratorContext();
		
		final Bean bean = new Bean();		
		final String id = jsonTag.getId();
		bean.setId(id);
		
		context.branch();
		context.debug( id );
		
		bean.setSingleton( true );
		bean.setEagerLoad( false );
		
		context.debug( "singleton");
		context.debug( "lazy load");
		
		final String interfaceName = jsonTag.getInterface();
		final Type beanType = this.getInterfaceType(id, interfaceName + Constants.ASYNC_SUFFIX );
		bean.setType(beanType);

		context.debug( "service interface: " + interfaceName );
		context.debug( "async service interface: " + beanType.getName() );
		
		final Type superType = this.getPrototypeFactoryBean();
		final NewConcreteType beanFactory = this.getBeanFactory();
		final NewNestedType factoryBean = beanFactory.newNestedType();
		factoryBean.setStatic(false);
		factoryBean.setNestedName(this.escapeBeanIdToBeClassNameSafe(id) + Constants.FACTORY_BEAN_SUFFIX);
		factoryBean.setSuperType(superType);
		factoryBean.setVisibility(Visibility.PRIVATE);
		bean.setFactoryBean(factoryBean);

		context.debug( "Factory bean super type " + superType );

		this.overrideFactoryBeanCreateInstanceViaDeferredBinding(bean);
		this.overrideFactoryBeanSatisfyPropertiesWithSetAddress(bean, jsonTag.getAddress());

		this.addBean(bean);
		
		context.unbranch();
	}

	/**
	 * Builds all the factory beans that will return remote rpc service clients.
	 * 
	 * @param rpcTags
	 *            A read only list containing all the rpc tags in the document
	 */
	protected void buildJavaRpcServiceFactoryBeans(final List rpcTags) {
		ObjectHelper.checkNotNull("parameter:rpcTags", rpcTags);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Creating FactoryBean's for each of the remote rpc service (" + rpcTags.size() + ") tags.");

		final Iterator iterator = rpcTags.iterator();
		while (iterator.hasNext()) {
			final RemoteRpcServiceTag rpc = (RemoteRpcServiceTag) iterator.next();
		
			this.createJavaRpcServiceFactoryBean(rpc);
		}
		
		context.unbranch();
	}

	/**
	 * Factory method which creates the initial rpc FactoryBean
	 * 
	 * @param rpcTag
	 */
	protected void createJavaRpcServiceFactoryBean(final RemoteRpcServiceTag rpcTag) {
		ObjectHelper.checkNotNull("parameter:rpcTag", rpcTag);

		final GeneratorContext context = this.getGeneratorContext();
		
		final Bean bean = new Bean();
		final String id = rpcTag.getId();
		bean.setId(id);
		
		context.branch();
		context.debug( id );
		
		bean.setSingleton( true );
		bean.setEagerLoad( false );

		context.debug( "singleton");
		context.debug( "lazy load");
		
		final String interfaceName = rpcTag.getInterface();
		final Type beanType = this.getInterfaceType(id, interfaceName + Constants.ASYNC_SUFFIX );
		bean.setType(beanType);

		context.debug( "service interface: " + interfaceName );
		context.debug( "async service interface: " + beanType.getName() );
		
		final Type superType = this.getPrototypeFactoryBean();
		final NewConcreteType beanFactory = this.getBeanFactory();
		final NewNestedType factoryBean = beanFactory.newNestedType();
		factoryBean.setStatic(false);
		factoryBean.setNestedName( this.escapeBeanIdToBeClassNameSafe(id) + Constants.FACTORY_BEAN_SUFFIX);
		factoryBean.setSuperType(superType);
		factoryBean.setVisibility(Visibility.PRIVATE);
		bean.setFactoryBean(factoryBean);

		context.debug( "FactoryBean super type: " + superType );

		this.overrideFactoryBeanCreateInstanceViaDeferredBinding(bean);
		this.overrideFactoryBeanSatisfyPropertiesWithSetAddress(bean, rpcTag.getAddress());

		this.addBean(bean);
		
		context.unbranch();
	}

	/**
	 * Overrides the create instance method of the factory bean to create a new
	 * instance via deferred binding.
	 * 
	 * @param bean
	 *            The bean definition
	 */
	protected void overrideFactoryBeanCreateInstanceViaDeferredBinding(final Bean bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);

		final NewType factoryBean = bean.getFactoryBean();
		
		// get the actual interface type from the async interface type.
		final Type asyncInterfaceType = bean.getType();
		final String asyncInterfaceName = asyncInterfaceType.getName(); 
		
		final String interfaceTypeName = asyncInterfaceName.substring( 0, asyncInterfaceName.length() - Constants.ASYNC_SUFFIX.length() );
		final Type interfaceType = this.getInterfaceType( bean.getId(), interfaceTypeName);
		
		final NewMethod createInstance = this.createCreateInstanceMethod(factoryBean);
		final DeferredBinding body = new DeferredBinding();
		body.setType(interfaceType);
		createInstance.setBody(body);
	}

	/**
	 * Overrides the satisfyProperties method of the factory bean to set the
	 * address property.
	 * 
	 * @param bean
	 *            The bean definition
	 * @param address The service entry point of the service.
	 */
	protected void overrideFactoryBeanSatisfyPropertiesWithSetAddress(final Bean bean, final String address) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		StringHelper.checkNotEmpty("parameter:address", address);

		final NewType factoryBean = bean.getFactoryBean();

		final Method method = factoryBean.getMostDerivedMethod(Constants.SATISFY_PROPERTIES, this.getParameterListWithOnlyObject());
		final NewMethod newMethod = method.copy(factoryBean);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final SetPropertiesTemplatedFile body = new SetPropertiesTemplatedFile();
		newMethod.setBody(body);

		final NewMethodParameter instanceParameter = (NewMethodParameter)newMethod.getParameters().get( 0 );
		instanceParameter.setFinal( true );
		instanceParameter.setName( Constants.SATISFY_PROPERTIES_INSTANCE_PARAMETER );

		final Type serviceDefTarget = this.getServiceDefTarget();
		body.setBean(serviceDefTarget);

		final GeneratorContext context = this.getGeneratorContext();

		final Type string = context.getString();
		final Method setter = serviceDefTarget
				.getMostDerivedMethod(Constants.SET_SERVICE_ENTRY_POINT, Arrays.asList(new Type[] { string }));
		final StringValue addressValue = new StringValue();
		addressValue.setGeneratorContext(context);
		addressValue.setType(string);
		addressValue.setValue(address);

		body.addProperty(setter, addressValue);

		context.debug("serviceEntryPoint: \"" + address + "\"");
	}

	/**
	 * Visits all advisors, checking they really are advisors and then adding
	 * them to the respective bean.
	 * 
	 * @param advices
	 */
	protected void buildAdvices(final List advices) {
		ObjectHelper.checkNotNull("parameter:advices", advices);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Processing and verifying " + advices.size() + " advice(s).");
		
		final MethodMatcherFactory methodMatcherFactory = createMethodMatcherFactor();

		final Iterator iterator = advices.iterator();
		while (iterator.hasNext()) {
			final AdviceTag adviceTag = (AdviceTag) iterator.next();

			final Advice advice = new Advice();

			final String adviceId = adviceTag.getAdvisorBeanId();
			advice.setAdvisorBeanId( adviceId );
			
			advice.setMethodMatcher(methodMatcherFactory.create(adviceTag.getMethodExpression()));
			
			final String targetBeanId = adviceTag.getTargetBeanId();
			advice.setTargetBeanId(targetBeanId);

			context.branch();
			context.debug( adviceId + "=" + targetBeanId );
			
			final Bean bean = this.getBean(targetBeanId);
			this.verifyProxyTarget(bean);
			this.verifyAdvisorBean(advice);
			this.verifyMethodExpression(advice);

			bean.addAdvice(advice);
		}
		
		context.unbranch();
	}

	protected MethodMatcherFactory createMethodMatcherFactor() {
		return new MethodMatcherFactory();
	}

	/**
	 * Verifies that the proxy target bean is not final so that it can be
	 * subclassed.
	 * 
	 * @param bean The bean
	 */
	protected void verifyProxyTarget(final Bean bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);

		final Type beanType = bean.getType();
		if (beanType.isFinal()) {
			this.throwProxyTargetCannotBeSubclassed(bean);
		}
	}

	protected void throwProxyTargetCannotBeSubclassed(final Bean bean) {
		throw new BeanFactoryGeneratorException("Unable to create proxy because target bean is final, bean: " + bean);
	}

	/**
	 * Checks that the advisor bean exists and is really an advice.
	 * 
	 * @param advice
	 *            The advice
	 */
	protected void verifyAdvisorBean(final Advice advice) {
		ObjectHelper.checkNotNull("parameter:advice", advice);

		final String id = advice.getAdvisorBeanId();
		final Bean bean = this.getBean(id);
		final Type type = bean.getType();

		if (false == type.isAssignableTo(this.getAdvice())) {
			throwNotAnAdviceException(bean);
		}
	}

	protected void throwNotAnAdviceException(final Bean bean) {
		throw new BeanFactoryGeneratorException("The bean is not an advice, bean: " + bean);
	}

	/**
	 * Verifies that the method expression matches at least one public method.
	 * 
	 * @param advice
	 */
	protected void verifyMethodExpression(final Advice advice) {
		ObjectHelper.checkNotNull("parameter:advice", advice);

		final String id = advice.getTargetBeanId();
		final Bean bean = this.getBean(id);
		final Type type = bean.getType();
		final MethodMatcher matcher = advice.getMethodMatcher();

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug("Discovering methods that match: " + matcher + " against " + type);
		
		final List matchedMethods = new ArrayList();
		final Type object = context.getObject();

		final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {
			protected boolean visit(final Method method) {
				while (true) {
					if (method.isFinal()) {
						if (false == method.getEnclosingType().equals(object)) {
							BeanFactoryGenerator.this.throwTargetMethodIsFinal(method);
						}
					}
					break;
				}

				if (method.getVisibility() == Visibility.PUBLIC && matcher.matches(method)) {
					matchedMethods.add(method);
					context.debug( method.toString() );
				}
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return false;
			}
		};
		visitor.start(type);

		if (matchedMethods.isEmpty()) {
			throwNoMatchedMethods(advice);
		}
		
		context.debug("Matched " + matchedMethods.size() + " methods(s).");
		context.unbranch();
	}

	protected void throwTargetMethodIsFinal(final Method method) {
		throw new BeanFactoryGeneratorException("The method " + method
				+ " which is final, prevents a proxy from being generated (this is achieved via subclassing).");
	}

	protected void throwNoMatchedMethods(final Advice advice) {
		throw new BeanFactoryGeneratorException("The advice expression does not match any methods, advice: " + advice);
	}

	/**
	 * Creates a factory bean for each bean that will be proxied.
	 * 
	 * @param advisors
	 */
	protected void applyAdvices() {		
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Checking for beans with one or more advices." );

		final Set beans = this.filterBeansRequiringInterceptors();
		
		context.branch();
		context.info("Processing beans that are advised." );
		final Iterator advisedIterator = beans.iterator();
		while( advisedIterator.hasNext()){
			final Bean bean = (Bean) advisedIterator.next();
			this.buildProxyFactoryBean(bean);
		}				
		context.unbranch();
		
		context.unbranch();
	}
	
	protected Set filterBeansRequiringInterceptors(){
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug( "Beans that dont have any advices.");

		final Set advised = new HashSet();
		final Iterator beans = this.getBeans().values().iterator();
				
		while (beans.hasNext()) {
			final Bean bean = (Bean) beans.next();
			final List advices = bean.getAdvices();
			if (advices.isEmpty()) {
				context.debug( bean.getId() );
				continue;
			}			
			advised.add(bean);
		}
		context.unbranch();
		
		return advised;
	}
	/**
	 * Creates a factory bean for the proxy for the given bean.
	 * 
	 * @param bean
	 *            The bean being proxied
	 */
	protected void buildProxyFactoryBean(final Bean bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		
		final String id = bean.getId();
		context.debug( id );
		
		final Type superType = this.getProxyFactoryBean();

		final NewConcreteType beanFactory = this.getBeanFactory();
		final NewNestedType proxyFactoryBean = beanFactory.newNestedType();
		proxyFactoryBean.setStatic(false);
		proxyFactoryBean.setNestedName(this.escapeBeanIdToBeClassNameSafe(id) + Constants.PROXY_FACTORY_BEAN_SUFFIX);
		proxyFactoryBean.setSuperType(superType);
		proxyFactoryBean.setVisibility(Visibility.PRIVATE);
		
		bean.setProxyFactoryBean(proxyFactoryBean);

		context.debug( "Proxy factory bean: " + superType );

		this.overrideProxyFactoryBeanGetTargetFactoryBean(bean);

		final NewNestedType proxy = this.createProxy(bean);
		bean.setProxy(proxy);

		overrideProxyFactoryBeanCreateProxy(bean);
	}

	/**
	 * Overrides the getTargetFactoryBean method of the proxy factory bean
	 * 
	 * @param bean
	 *            The bean being processed
	 */
	protected void overrideProxyFactoryBeanGetTargetFactoryBean(final Bean bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);

		this.getGeneratorContext().debug("Overriding proxy factory bean " + Constants.PROXY_FACTORY_GET_TARGET_FACTORY_BEAN_METHOD_NAME + ".");

		final NewNestedType proxyFactoryBean = bean.getProxyFactoryBean();
		final Method method = proxyFactoryBean.getMostDerivedMethod( Constants.PROXY_FACTORY_GET_TARGET_FACTORY_BEAN_METHOD_NAME, Collections.EMPTY_LIST);

		final NewMethod newMethod = method.copy(proxyFactoryBean);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final GetTargetFactoryBeanTemplatedFile body = new GetTargetFactoryBeanTemplatedFile();
		body.setTargetFactoryBean(bean.getFactoryBean());
		newMethod.setBody(body);
	}

	/**
	 * Creates a new nested type that sub classes the proxied bean. All public
	 * methods will be proxied to the target.
	 * 
	 * @param bean
	 *            The bean
	 * @return The new proxy
	 */
	protected NewNestedType createProxy(final Bean bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);

		final Type targetBeanType = bean.getType();
		final String id = bean.getId();

		// sub class the target...
		final NewConcreteType beanFactory = this.getBeanFactory();
		final NewNestedType proxy = beanFactory.newNestedType();
		proxy.setStatic(false);
		proxy.setNestedName( this.escapeBeanIdToBeClassNameSafe(id) + Constants.PROXY_SUFFIX);
		proxy.setSuperType(targetBeanType);
		proxy.setVisibility(Visibility.PRIVATE);

		// add a no arguments constructor...
		final NewConstructor constructor = proxy.newConstructor();
		constructor.setBody(EmptyCodeBlock.INSTANCE);
		constructor.setVisibility(Visibility.PUBLIC);

		// add a field of type target bean this will be set by the
		// ProxyFactoryBean.createProxy0 method.
		final NewField field = proxy.newField();
		field.setFinal(false);
		field.setName(Constants.PROXY_TARGET_FIELD);
		field.setStatic(false);
		field.setTransient(false);
		field.setType(targetBeanType);
		field.setValue(EmptyCodeBlock.INSTANCE);
		field.setVisibility(Visibility.PUBLIC);

		final List advices = bean.getAdvices();

		final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {
			protected boolean visit(final Method method) {

				while (true) {
					if (method.isFinal()) {
						break;
					}

					if (method.getVisibility() != Visibility.PUBLIC) {
						break;
					}

					// the public methods remain...
					final List matched = BeanFactoryGenerator.this.findMatchingAdvices(method, advices);
					if (matched.isEmpty()) {
						BeanFactoryGenerator.this.createProxyMethod(proxy, method);
						break;
					}

					BeanFactoryGenerator.this.createProxyMethodWithInterceptors(proxy, method, matched);
					break;
				}
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return false;
			}
		};
		visitor.start(targetBeanType);

		return proxy;
	}

	/**
	 * Finds all the advices that match the given method.
	 * 
	 * @param method
	 *            The method
	 * @param advices
	 *            A list of advices
	 * @return A list of matched advices
	 */
	protected List findMatchingAdvices(final Method method, final List advices) {
		final Iterator advicesIterator = advices.iterator();
		final List applicableAdvices = new ArrayList();

		while (advicesIterator.hasNext()) {
			final Advice advice = (Advice) advicesIterator.next();
			if (advice.getMethodMatcher().matches(method)) {
				applicableAdvices.add(advice);
			}
		}

		return applicableAdvices;
	}

	/**
	 * Creates a new method and adds it to the proxy type that simply delegates
	 * to the target.
	 * 
	 * @param proxy
	 *            The proxy being created
	 * @param method
	 *            The method
	 */
	protected void createProxyMethod(final NewNestedType proxy, final Method method) {
		ObjectHelper.checkNotNull("parameter:proxy", proxy);
		ObjectHelper.checkNotNull("parameter:method", method);

		final NewMethod newMethod = method.copy(proxy);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		GeneratorHelper.makeAllParametersFinal(newMethod);

		final ProxyMethodTemplatedFile body = new ProxyMethodTemplatedFile();
		newMethod.setBody(body);
		body.setMethod(newMethod);
	}

	/**
	 * Creates a new method upon the proxy type that invokes interceptors for
	 * the matched advices.
	 * 
	 * @param proxy
	 *            The proxy being created
	 * @param method
	 *            The method
	 * @param advices
	 *            A list of advices for this method
	 */
	protected void createProxyMethodWithInterceptors(final NewNestedType proxy, final Method method, final List advices) {
		ObjectHelper.checkNotNull("parameter:proxy", proxy);
		ObjectHelper.checkNotNull("parameter:method", method);
		ObjectHelper.checkNotNull("parameter:advices", advices);

		final NewMethod newMethod = method.copy(proxy);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		GeneratorHelper.renameParametersToParameterN(newMethod);
		GeneratorHelper.makeAllParametersFinal(newMethod);

		final ProxyInterceptedMethodTemplatedFile body = new ProxyInterceptedMethodTemplatedFile();
		body.setAdvices(advices);
		body.setBeanFactory(this.getBeanFactory());
		body.setMethod(newMethod);

		final Type methodInterceptor = this.getMethodInterceptor();
		final List methodInvocationParameterList = Arrays.asList(new Type[] { this.getMethodInvocation() });
		final Method invoke = methodInterceptor.getMethod(Constants.METHOD_INTERCEPTOR_INVOKE, methodInvocationParameterList);
		final MethodParameter target = (MethodParameter) invoke.getParameters().get(0);

		body.setTarget(target);
		newMethod.setBody(body);
	}

	/**
	 * Overrides the createProxy method of the subclassed ProxyFactoryBean class
	 * 
	 * @param bean
	 *            The bean being proxied.
	 */
	protected void overrideProxyFactoryBeanCreateProxy(final Bean bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);

		final NewNestedType proxyFactoryBean = bean.getProxyFactoryBean();
		final List objectParameterList = this.getParameterListWithOnlyObject();
		final Method method = proxyFactoryBean.getMostDerivedMethod(Constants.CREATE_PROXY, objectParameterList);

		final NewMethod newMethod = method.copy(proxyFactoryBean);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		// add a new constructor...
		final MethodParameter targetBeanParameter = (MethodParameter) newMethod.getParameters().get(0);

		final Type proxy = bean.getProxy();
		final Constructor constructor = proxy.getConstructor(Collections.EMPTY_LIST);

		final CreateProxyTemplatedFile body = new CreateProxyTemplatedFile();
		body.setProxyConstructor(constructor);
		body.setTargetBeanParameter(targetBeanParameter);
		body.setTargetBeanType(bean.getType());

		newMethod.setBody(body);
	}

	/**
	 * Adds a new method to the bean factory being built that populates a map
	 * with all the factory beans keyed by bean id.
	 */
	protected void overrideBeanFactoryRegisterFactoryBeans() {		
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		
		final NewType beanFactory = this.getBeanFactory();
		final Method abstractMethod = beanFactory.getSuperType().getMostDerivedMethod(Constants.REGISTER_FACTORY_BEANS, Collections.EMPTY_LIST);

		final NewMethod newMethod = abstractMethod.copy(beanFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final RegisterFactoryBeansTemplatedFile body = new RegisterFactoryBeansTemplatedFile();
		newMethod.setBody(body);
		
		context.branch();
		context.info( "Overriding " + newMethod + " to register all beans.");
		
		final Iterator beansIterator = beans.values().iterator();
		
		while (beansIterator.hasNext()) {
			final Bean bean = (Bean) beansIterator.next();
			body.addBean(bean);
			
			context.debug( bean.getId() );
		}
		context.unbranch();
	}
	
	/**
	 * Overrides the {@link rocket.beans.client.BeanFactoryImpl#getAliasesToBeans} to return a list of alias to bean mappings.
	 * This list may be empty if no aliases were present in config files.
	 */
	protected void registerBeanAliases(){
		this.overrideBeanFactoryGetAliasesToBeans();
	}
	protected void overrideBeanFactoryGetAliasesToBeans(){
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		
		final NewType beanFactory = this.getBeanFactory();
		final Method abstractMethod = beanFactory.getSuperType().getMostDerivedMethod(Constants.GET_ALIASES_TO_BEANS_METHOD, Collections.EMPTY_LIST);

		final NewMethod newMethod = abstractMethod.copy(beanFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final GetAliasesToBeans body = new GetAliasesToBeans();
		newMethod.setBody(body);

		context.branch();
		context.info( "Overriding " + newMethod + " to register all aliases.");
		
		final Iterator beansIterator = this.getAliases().values().iterator();
		int aliasCount = 0;
		
		while (beansIterator.hasNext()) {
			final Alias alias = (Alias) beansIterator.next();
			final String from = alias.getName();
			final String to = alias.getBean();
			body.register(from,to);
			
			context.debug( from + "=" + to );
			aliasCount++;
		}
		
		context.debug( "Registered " + aliasCount + " aliases.");
		context.unbranch();
	}
	
	/**
	 * Overrides the {@link rocket.beans.client.BeanFactoryImpl#getEagerSingletonBeanNames()
	 * which contains a comma separated list of singletons that need to initialized on factory startup.
	 */
	protected void overrideLoadEagerBeans(){		
		final Map beans = this.getBeans();

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Overriding BeanFactory." + Constants.GET_EAGER_SINGELTON_BEAN_NAMES_METHOD + "() to initialize eager singleton beans on factory startup.");
		context.branch();
		
		final NewType beanFactory = this.getBeanFactory();
		final Method abstractMethod = beanFactory.getSuperType().getMostDerivedMethod(Constants.GET_EAGER_SINGELTON_BEAN_NAMES_METHOD, Collections.EMPTY_LIST);

		final NewMethod newMethod = abstractMethod.copy(beanFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final GetEagerSingletonBeanNames body = new GetEagerSingletonBeanNames();
		newMethod.setBody(body);
		
		int eagerSingletonBeanCount = 0;
		int lazySingletonBeanCount = 0;
		
		final Iterator beansIterator = beans.values().iterator();
		while (beansIterator.hasNext()) {
			final Bean bean = (Bean) beansIterator.next();
			
			if( false == bean.isSingleton() ){
				continue;
			}
			
			// only singletons can be singletons.
			final boolean eager = bean.isEagerLoad();
			if( eager ){
				body.addBean(bean.getId());	
				eagerSingletonBeanCount++;
				
				context.debug( bean.toString() );
			} else {
				lazySingletonBeanCount++;
			}
		}
		context.unbranch();
		context.debug( "When instantiated " + eagerSingletonBeanCount + " singletons will be eaglerly loaded, the remaining " + lazySingletonBeanCount + " will be lazily loaded on first request.");
		context.unbranch();
	}
	
	protected String getGeneratedTypeNameSuffix() {
		return Constants.BEAN_FACTORY_SUFFIX;
	}

	/**
	 * The beanFactoryImpl being assembled
	 */
	private NewConcreteType beanFactory;

	public NewConcreteType getBeanFactory() {
		ObjectHelper.checkNotNull("field:beanFactory", beanFactory);
		return this.beanFactory;
	}

	protected void setBeanFactory(final NewConcreteType beanFactory) {
		ObjectHelper.checkNotNull("parameter:beanFactory", beanFactory);
		this.beanFactory = beanFactory;
	}

	/**
	 * A map that contains all the NewType factory beans for each defined bean keyed on the bean's id.
	 */
	private Map beans;

	public Map getBeans() {
		ObjectHelper.checkNotNull("field:beans", beans);
		return this.beans;
	}

	protected void setBeans(final Map beans) {
		ObjectHelper.checkNotNull("parameter:beans", beans);
		this.beans = beans;
	}

	/**
	 * A sorted TreeMap is used to guarantee that beans are processed in bean id sorted order which should make
	 * reading of logging info much easier.
	 * @return A map
	 */
	protected Map createBeans() {
		return new TreeMap( String.CASE_INSENSITIVE_ORDER );
	}

	protected void checkBean( final Bean bean ){
		final String id = bean.getId();
		if( this.getBeans().containsKey( id )){
			throwBeanIdAlreadyUsed( bean );
		}	
	}
	
	protected void throwBeanIdAlreadyUsed( final Bean bean ){
		final String id = bean.getId();
		throw new BeanFactoryGeneratorException( "The id \"" + id + "\" is used by more than bean: " + bean + ", other bean: " + this.getBean(id)); 
	}
	
	/**
	 * Adds a new bean to the beans registry
	 * 
	 * @param bean The new bean
	 */
	protected void addBean(final Bean bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);

		this.getBeans().put( bean.getId(), bean);
	}

	/**
	 * Retrieves a bean given an id searching both beans and alias to beans, throwing an exception if the id is not found.
	 * 
	 * @param id The id
	 * @return The bean
	 */
	protected Bean getBean(final String id) {
		String resolvedId = id;
		
		// check if $id is actually an alias.
		Alias alias = (Alias) this.getAliases().get(id);
		if( null != alias ){
			resolvedId = alias.getBean();
		}
		
		// now try and find the bean...
		final Bean bean = (Bean) this.getBeans().get(resolvedId);
		if( null == bean ){
			this.throwUnableToFindBean(id);
		}
		return bean;
	}

	protected void throwUnableToFindBean(final String id) {
		throw new BeanFactoryGeneratorException("Unable to locate a bean with an id of \"" + id + "\".");
	}
	
	/**
	 * A map that contains all the NewType factory aliases for each defined alias
	 * keyed on the alias's id.
	 */
	private Map aliases;

	public Map getAliases() {
		ObjectHelper.checkNotNull("field:aliases", aliases);
		return this.aliases;
	}

	protected void setAliases(final Map aliases) {
		ObjectHelper.checkNotNull("parameter:aliases", aliases);
		this.aliases = aliases;
	}

	protected Map createAliases() {
		return new HashMap();
	}

	protected void checkAlias( final Alias alias ){
		final String name = alias.getName();
		if( this.getBeans().containsKey( name )){
			throwAliasIdAlreadyUsed( alias );
		}	
		this.getBean( alias.getBean() );			
	}
	
	protected void throwAliasIdAlreadyUsed( final Alias alias ){
		final String id = alias.getName();
		throw new BeanFactoryGeneratorException( "The id \"" + id + "\" used by alias " + alias + " is already used by another bean: " + this.getBean( id ));
	}
	
	/**
	 * Adds a new alias to the aliases registry
	 * 
	 * @param alias The new alias
	 */
	protected void addAlias(final Alias alias) {
		ObjectHelper.checkNotNull("parameter:alias", alias);

		this.getAliases().put( alias.getName(), alias);
	}
	
	protected Type getSingletonFactoryBean() {
		return this.getGeneratorContext().getType(Constants.SINGLETON_FACTORY_BEAN);
	}

	protected Type getPrototypeFactoryBean() {
		return this.getGeneratorContext().getType(Constants.PROTOTYPE_FACTORY_BEAN);
	}

	protected Type getBeanFactoryType() {
		return this.getGeneratorContext().getType(Constants.BEAN_FACTORY);
	}

	protected Type getBeanFactoryImpl() {
		return this.getGeneratorContext().getType(Constants.BEAN_FACTORY_IMPL);
	}

	protected Type getServiceDefTarget() {
		return this.getGeneratorContext().getType(Constants.SERVICE_DEF_TARGET);
	}

	protected Type getAdvice() {
		return this.getGeneratorContext().getType(Constants.ADVICE);
	}

	protected Type getProxyFactoryBean() {
		return this.getGeneratorContext().getType(Constants.PROXY_FACTORY_BEAN);
	}

	protected Type getMethodInterceptor() {
		return this.getGeneratorContext().getType(Constants.METHOD_INTERCEPTOR);
	}

	protected Type getMethodInvocation() {
		return this.getGeneratorContext().getType(Constants.METHOD_INVOCATION);
	}

	protected Type getFactoryBean() {
		return this.getGeneratorContext().getType(Constants.FACTORY_BEAN );
	}
	
	protected Type getDisposableBean() {
		return this.getGeneratorContext().getType(Constants.DISPOSABLE_BEAN );
	}
	
	/**
	 * Fetches the interface type for the given bean throwing an excecption if the type is not
	 * found or not an interface.
	 * 
	 * @param id The id of a bean
	 * @param className
	 * @return The type
	 */
	protected Type getInterfaceType(final String id, final String className) {
		final Type type = this.getGeneratorContext().findType(className);
		if (null == type) {
			throwBeanTypeNotFound(id, className);
		}
		if (false == type.isInterface() || type.isPrimitive()) {
			throwBeanTypeIsNotAnInterface(id, type);
		}
		return type;
	}

	protected void throwBeanTypeIsNotAnInterface(final String id, final Type type) {
		throw new BeanFactoryGeneratorException("The type \"" + type + "\" is not an interface for the bean id\"" + id + "\".");
	}

	/**
	 * Fetches the concrete type. If the type is not found or is an interface
	 * exceptions are thrown.
	 * 
	 * @param id
	 * @param className
	 * @return
	 */
	protected Type getConcreteType(final String id, final String className) {
		final Type type = this.getGeneratorContext().findType(className);
		if (null == type) {
			throwBeanTypeNotFound(id, className);
		}
		if (type.isInterface() || type.isAbstract() || type.isPrimitive()) {
			throwBeanTypeIsNotConcrete(id, type);
		}
		return type;
	}

	protected List getParameterListWithOnlyObject() {
		return Collections.nCopies( 1, this.getGeneratorContext().getObject() );
	}

	protected void throwBeanTypeNotFound(final String id, final String className) {
		throw new BeanFactoryGeneratorException("Unable to find the type \"" + className + "\" for the bean with an id of \"" + id + "\".");
	}

	protected void throwBeanTypeIsNotConcrete(final String id, final Type type) {
		throw new BeanFactoryGeneratorException("The type \"" + type + "\" is not concrete for the bean id\"" + id + "\".");
	}

	protected String escapeBeanIdToBeClassNameSafe(final String beanId) {
		StringHelper.checkNotEmpty("parameter:beanId", beanId);

		final StringBuffer safeName = new StringBuffer();

		final char[] chars = beanId.toCharArray();

		for (int j = 0; j < chars.length; j++) {
			final char c = chars[j];

			// escape underscore to double underscore...
			if (c == '_') {
				safeName.append("__");
				continue;
			}
			// if $c a valid chacter simply add it...
			if ((j == 0 && Character.isJavaIdentifierStart(c)) || Character.isJavaIdentifierPart(c)) {
				safeName.append(c);
				continue;
			}

			// not a safe character encode it as underscore + hex
			// value of $c.
			safeName.append('_');

			final String hexEncoded = StringHelper.padLeft(Integer.toHexString(c), 4, '0');
			safeName.append(hexEncoded);
		} // for j
		return safeName.toString();
	}
}