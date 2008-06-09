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
import java.util.Collection;
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
import rocket.beans.rebind.aop.createproxy.CreateProxyTemplatedFile;
import rocket.beans.rebind.aop.proxyinterceptedmethod.ProxyInterceptedMethodTemplatedFile;
import rocket.beans.rebind.aop.proxymethod.ProxyMethodTemplatedFile;
import rocket.beans.rebind.beanreference.BeanReferenceImpl;
import rocket.beans.rebind.constructor.ConstructorTemplatedFile;
import rocket.beans.rebind.deferredbinding.DeferredBinding;
import rocket.beans.rebind.factorymethod.FactoryMethodTemplatedFile;
import rocket.beans.rebind.image.ImageValue;
import rocket.beans.rebind.imagefactory.ImageFactoryGetterTemplatedFile;
import rocket.beans.rebind.imagefactory.ImageFactorySetterTemplatedFile;
import rocket.beans.rebind.invokemethod.InvokeMethodTemplatedFile;
import rocket.beans.rebind.list.ListValue;
import rocket.beans.rebind.loadeagersingletons.GetEagerSingletonBeanNames;
import rocket.beans.rebind.map.MapValue;
import rocket.beans.rebind.properties.SetPropertiesTemplatedFile;
import rocket.beans.rebind.registerfactorybeans.RegisterFactoryBeansTemplatedFile;
import rocket.beans.rebind.set.SetValue;
import rocket.beans.rebind.stringvalue.StringValue;
import rocket.beans.rebind.value.Value;
import rocket.beans.rebind.xml.BeanFactoryDtdEntityResolver;
import rocket.beans.rebind.xml.DocumentWalker;
import rocket.beans.rebind.xml.RethrowSaxExceptionsErrorHandler;
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
import rocket.generator.rebind.type.NewNestedInterfaceType;
import rocket.generator.rebind.type.NewNestedType;
import rocket.generator.rebind.type.NewType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.TypeConstructorsVisitor;
import rocket.generator.rebind.visitor.VirtualMethodVisitor;
import rocket.util.client.Checker;
import rocket.util.client.Tester;
import rocket.util.client.Utilities;
import rocket.widget.rebind.imagefactory.ImageFactoryConstants;

/**
 * This code generator generates a BeanFactory which will create or provide
 * theCreating FactoryBean's for all beans beans defined in an xml file.
 * 
 * A standalone class is created to implement the BeanFactory. Within this
 * BeanFactory private nested inner FactoryBean classes are created for each
 * bean and proxy required to implement configured aspects
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
	@Override
	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		this.verifyBeanFactory(type);

		final DocumentWalker document = this.getDocumentWalker(type);

		final NewConcreteType beanFactory = this.createBeanFactory(newTypeName, type);
		this.setBeanFactory(beanFactory);

		final Set<Bean> beans = document.getBeans();

		this.setBeans(this.createBeans());
		this.setAliases(this.createAliases());

		this.buildFactoryBeans(beans);

		final Set<Alias> aliases = document.getAliases();
		this.recordAliases(aliases);

		this.setImageValues(this.createImageValues());

		this.overrideAllFactoryBeanCreateInstances(beans);
		this.overrideAllFactoryBeanSatisfyInits(beans);
		this.overrideAllFactoryBeanSatisfyProperties(beans);

		this.createImageFactoryIfNecessary();

		this.overrideAllSingletonFactoryBeanToInvokeCustomDestroy(beans);

		this.buildAspects(document.getAspects());
		this.applyAspects();

		this.overrideBeanFactoryRegisterFactoryBeans();
		this.registerBeanAliases();
		this.overrideLoadEagerBeans();

		return beanFactory;
	}

	/**
	 * Factory method which creates a walker that may be used to walk around the
	 * xml document.
	 * 
	 * @param type
	 *            The type passed to the generator via deferred binding. This is
	 *            used to locate the xml file.
	 * @return A document walker containing the converted contents of the xml
	 *         document to beans.
	 */
	protected DocumentWalker getDocumentWalker(final Type type) {
		final String fileName = this.getResourceName(type, Constants.BEAN_FILE_SUFFIX);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Preparing to discover components within \"" + fileName + "\".");

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
		Checker.notNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.info("Verifying " + type + " is a bean factory.");

		final Type beanFactory = this.getBeanFactoryType();
		if (false == type.isAssignableTo(beanFactory)) {
			throwNotABeanFactory("The type \"" + type + "\" is not a " + beanFactory);
		}

		// verify has no public methods (ignore those belonging to
		// java.lang.Object)
		final List<Method> publicMethods = new ArrayList<Method>();
		final VirtualMethodVisitor methodFinder = new VirtualMethodVisitor() {
			@Override
			protected boolean visit(final Method method) {
				publicMethods.add(method);
				return false;
			}

			@Override
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

	protected void throwBeanFactoryInterfaceHasPublicMethods(final Type beanFactory, final int methodCount) {
		throw new BeanFactoryGeneratorException("The type \"" + beanFactory.getName() + "\" contains " + methodCount
				+ " when it should not contain any methods.");
	}

	/**
	 * Factory method which creates a new BeanFactory read to accept methods and
	 * fields etc.
	 * 
	 * @param newTypeName
	 *            The name of the new type
	 * @param implementsInterface
	 *            The interface to implement
	 * @return A new type.
	 */
	protected NewConcreteType createBeanFactory(final String newTypeName, final Type implementsInterface) {
		this.getGeneratorContext().info("Creating BeanFactory with a name of \"" + newTypeName + "\".");

		final NewConcreteType beanFactory = this.getGeneratorContext().newConcreteType(newTypeName);

		beanFactory.setAbstract(false);
		beanFactory.setFinal(true);
		beanFactory.setSuperType(this.getBeanFactoryImpl());
		beanFactory.setVisibility(Visibility.PUBLIC);
		beanFactory.addInterface(implementsInterface);

		return beanFactory;
	}

	/**
	 * Walks all the bean tags building a NewNestedType FactoryBean for each
	 * bean.
	 * 
	 * @param beans
	 *            A set containing all beans
	 */
	protected void buildFactoryBeans(final Set<Bean> beans) {
		Checker.notNull("parameter:beans", beans);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Creating FactoryBean's for all beans.");

		final Iterator<Bean> iterator = beans.iterator();
		int nestedBeanCounter = 0;
		int rpcCounter = 0;
		int beansCounter = 0;

		while (iterator.hasNext()) {
			final Bean bean = iterator.next();
			context.branch();

			context.debug(bean.getId());

			while (true) {
				if (bean instanceof Rpc) {
					this.createRpcFactoryBean((Rpc) bean);
					rpcCounter++;
					break;
				}
				if (bean instanceof NestedBean) {
					this.createBeanFactoryBean(bean);
					nestedBeanCounter++;
					break;
				}

				this.createBeanFactoryBean(bean);
				beansCounter++;
				break;
			}

			context.unbranch();
		}

		context.debug("Total: " + beans.size() + ", beans: " + beansCounter + ", nested(anonymous): " + nestedBeanCounter + ", rpcs: "
				+ rpcCounter);
		context.unbranch();
	}

	/**
	 * Creates the factory bean that will be responsible for producing the bean
	 * on demand.
	 * 
	 * The strategy pattern is used with separate factory beans used to
	 * implement the singleton and prototype bean scopes.
	 * 
	 * @param bean
	 *            The bean under construction.
	 */
	protected void createBeanFactoryBean(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		final GeneratorContext context = this.getGeneratorContext();

		final String beanName = bean.getId();
		if (Tester.isNullOrEmpty(beanName)) {
			throwBeanIdIsMissing(bean);
		}
		this.checkIdIsUnique(bean);

		final String className = bean.getTypeName();
		final Type beanType = this.getConcreteType(beanName, className);
		bean.setType(beanType);

		context.debug(beanType.getName());

		final boolean singleton = bean.isSingleton();
		bean.setSingleton(singleton);
		context.debug(singleton ? "singleton" : "prototype");

		// process the eager/lazy attribute
		final boolean eager = bean.isEagerLoaded();
		bean.setEagerLoaded(eager);
		this.checkPrototypesAreNotMarkedAsEager(bean);

		if (singleton) {
			context.debug(eager ? "eager" : "lazyloaded");
		}

		if (false == singleton) {
			final String destroyMethod = bean.getDestroyMethod();
			if (false == Tester.isNullOrEmpty(destroyMethod)) {
				throwPrototypeCantBeContainerDestroyed(bean);
			}

			final Type disposableBean = this.getDisposableBean();
			if (beanType.isAssignableTo(disposableBean)) {
				context.warn("Ignoring the fact the bean implements DisposableBean (because its a prototype)"
						+ (context.isDebugEnabled() ? "." : ", bean: " + bean));
			}
		}

		// start creating the factory bean...
		final Type superType = singleton ? this.getSingletonFactoryBean() : this.getPrototypeFactoryBean();
		final NewConcreteType beanFactory = this.getBeanFactory();
		final NewNestedType factoryBean = beanFactory.newNestedType();
		factoryBean.setStatic(false);
		factoryBean.setNestedName(this.escapeBeanIdToBeClassNameSafe(beanName) + Constants.FACTORY_BEAN_SUFFIX);
		factoryBean.setSuperType(superType);
		factoryBean.setVisibility(Visibility.PRIVATE);

		// add an annotation that declares the actual bean type.
		factoryBean.addMetaData(Constants.FACTORY_BEAN_OBJECT_TYPE, bean.getType().getName());

		bean.setFactoryBean(factoryBean);
		context.debug("FactoryBean: " + factoryBean.getName());

		this.addBean(bean);
	}

	protected void throwBeanIdIsMissing(final Bean bean) {
		throw new BeanFactoryGeneratorException("Bean is missing id, bean: " + bean);
	}

	protected void throwPrototypesCantBeEagerlyLoaded(final Bean bean) {
		throw new BeanFactoryGeneratorException(
				"Prototype beans cannot be eagerly loaded (only singleton's can be - remove \"lazyInit\" attribute from its bean definition), bean: "
						+ bean);
	}

	/**
	 * Without something in javascript equivalent to soft references supporting
	 * disposing prototypes would create a memory leak, because the container
	 * needs a reference to the bean.
	 * 
	 * @param bean
	 *            The prototype bean
	 */
	protected void throwPrototypeCantBeContainerDestroyed(final Bean bean) {
		throw new BeanFactoryGeneratorException("The container does not support disposing prototype beans(only singleton's ), bean: "
				+ bean);
	}

	/**
	 * Visit all defined beans and overrides the createInstance of the factory
	 * bean.
	 * 
	 * A constructor with parameters or factory bean may be the source of the
	 * new bean instance.
	 * 
	 * @param bean
	 *            A list of bean tag elements straight from any included xml
	 *            documents.
	 */
	protected void overrideAllFactoryBeanCreateInstances(final Set<Bean> beans) {
		Checker.notNull("parameter:beans", beans);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Overriding createInstance methods for all bean(s).");

		final Iterator<Bean> iterator = beans.iterator();
		while (iterator.hasNext()) {
			final Bean bean = iterator.next();
			context.branch();
			context.debug(bean.getId());

			this.overrideFactoryBeanCreateInstance(bean);

			context.unbranch();
		}

		context.unbranch();
	}

	/**
	 * Depending on the bean tag overrides the appropriate
	 * {@link FactoryBean#createInstance()} with an inline constructor call or
	 * inserts a call to a static factory method.
	 * 
	 * @param bean
	 *            The bean
	 */
	protected void overrideFactoryBeanCreateInstance(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		while (true) {
			if (bean instanceof Rpc) {
				this.overrideFactoryBeanCreateInstanceViaDeferredBinding((Rpc) bean);
				break;
			}

			final String factoryMethodName = bean.getFactoryMethod();

			if (Tester.isNullOrEmpty(factoryMethodName)) {
				this.overrideFactoryBeanCreateInstanceNewConstructor(bean);
				break;
			}
			this.overrideFactoryBeanCreateInstanceFactoryMethod(bean);
			break;
		}
	}

	/**
	 * Overrides the create instance method of the factory bean to create a new
	 * instance via deferred binding.
	 * 
	 * @param rpc
	 *            The bean definition
	 */
	protected void overrideFactoryBeanCreateInstanceViaDeferredBinding(final Rpc rpc) {
		Checker.notNull("parameter:bean", rpc);

		final NewType factoryBean = rpc.getFactoryBean();

		// get the actual interface type from the async interface type.
		final Type asyncInterfaceType = rpc.getType();
		final String asyncInterfaceName = asyncInterfaceType.getName();

		final String interfaceTypeName = asyncInterfaceName.substring(0, asyncInterfaceName.length() - Constants.ASYNC_SUFFIX.length());
		final Type interfaceType = this.getInterfaceType(rpc.getId(), interfaceTypeName);

		final NewMethod createInstance = this.createCreateInstanceMethod(factoryBean);
		final DeferredBinding body = new DeferredBinding();
		body.setType(interfaceType);
		createInstance.setBody(body);
	}

	/**
	 * Override the createInstance method of the given factory bean to invoke a
	 * constructor
	 * 
	 * @param bean
	 *            The bean
	 */
	protected void overrideFactoryBeanCreateInstanceNewConstructor(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		final NewMethod newFactoryMethod = this.createCreateInstanceMethod(bean.getFactoryBean());
		newFactoryMethod.setAbstract(false);
		newFactoryMethod.setFinal(true);
		newFactoryMethod.setNative(false);

		final ConstructorTemplatedFile body = new ConstructorTemplatedFile();
		newFactoryMethod.setBody(body);

		final GeneratorContext context = this.getGeneratorContext();
		context.delayedBranch();
		context.debug("Constructor parameter values.");

		final List<Value> arguments = bean.getConstructorValues();
		final Iterator<Value> argumentsIterator = arguments.iterator();
		while (argumentsIterator.hasNext()) {
			final Value value = argumentsIterator.next();
			body.addArgument(value);

			if (context.isDebugEnabled()) {
				context.debug(value.toString());
			}
		}

		context.unbranch();

		context.branch();
		context.debug("Matching constructors.");

		final List<Constructor> matchingConstructors = new ArrayList<Constructor>();
		final TypeConstructorsVisitor visitor = new TypeConstructorsVisitor() {

			protected boolean visit(final Constructor constructor) {
				boolean match = false;

				// skip non public constructors
				if (constructor.getVisibility() == Visibility.PUBLIC) {
					final List<ConstructorParameter> constructorParameters = constructor.getParameters();

					// skip the given constructor with different numbers of
					// parameters.
					if (constructorParameters.size() == arguments.size()) {

						match = true;
						final Iterator<Value> valuesIterator = arguments.iterator();
						final Iterator<ConstructorParameter> parametersIterator = constructorParameters.iterator();

						while (valuesIterator.hasNext()) {
							final ConstructorParameter parameter = parametersIterator.next();
							final Value value0 = valuesIterator.next();
							if (false == value0.isCompatibleWith(parameter.getType())) {
								match = false;
							}
						}
					}
				}

				if (match) {
					matchingConstructors.add(constructor);
					context.debug("" + constructor);
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

		final Iterator<ConstructorParameter> constructorParameters = constructor.getParameters().iterator();
		final Iterator<Value> valuesIterator = arguments.iterator();
		while (constructorParameters.hasNext()) {
			final ConstructorParameter constructorParameter = constructorParameters.next();
			final Value value = valuesIterator.next();
			value.setPropertyType(constructorParameter.getType());

			this.prepareValue(value);
		}

		if (context.isDebugEnabled()) {
			context.debug(constructor.toString());
		}
	}

	/**
	 * This method throws an exception when more than one constructor satisfies
	 * the specified values for a bean type
	 * 
	 * @param bean
	 *            The bean in error
	 * @param constructorValues
	 *            A list containing the matching constructors.
	 */
	protected void throwTooManyConstructors(final Bean bean, final List<Constructor> constructorValues) {
		throw new BeanFactoryGeneratorException("Found more than one constructor that matches the specified constructor arguments: "
				+ constructorValues + ", bean: " + bean);
	}

	/**
	 * This method throws an exception when a suitable constructor cannot be
	 * found that matches the bean's constructor parameters or lack of them.
	 * 
	 * @param bean
	 *            The bean in error
	 */
	protected void throwUnableToFindConstructor(final Bean bean) {
		throw new BeanFactoryGeneratorException(
				"Unable to find a constructor that is suitable for the specified constructor values for bean: " + bean);
	}

	/**
	 * Overrides the createInstance method of the given factory bean to invoke a
	 * static factory method.
	 * 
	 * @param bean
	 *            The bean being processed.
	 */
	protected void overrideFactoryBeanCreateInstanceFactoryMethod(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		final Type beanType = bean.getType();
		final String factoryMethodName = bean.getFactoryMethod();
		final Method factoryMethod = beanType.findMethod(factoryMethodName, Collections.<Type>emptyList() );
		if (null == factoryMethod || false == factoryMethod.isStatic() || factoryMethod.getVisibility() != Visibility.PUBLIC) {
			this.throwFactoryMethodNotFound(bean, factoryMethodName);
		}

		this.getGeneratorContext().debug("FactoryBean will create new instance by calling " + factoryMethod);

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
		Checker.notNull("parameter:factoryBean", factoryBean);

		final Method method = factoryBean.getMostDerivedMethod(Constants.CREATE_INSTANCE, Collections.<Type>emptyList());

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
	 * @param beans
	 *            The beans
	 */
	protected void overrideAllFactoryBeanSatisfyInits(final Set<Bean> beans) {
		Checker.notNull("parameter:beans", beans);

		final GeneratorContext context = this.getGeneratorContext();
		context.delayedBranch();
		context.info("Overriding satisfyInit methods for beans with custom initMethods.");

		final Iterator<Bean> iterator = beans.iterator();
		while (iterator.hasNext()) {
			final Bean bean = iterator.next();
			final String initMethodName = bean.getInitMethod();

			if (false == Tester.isNullOrEmpty(initMethodName)) {
				context.branch();
				context.debug(bean.getId());

				this.overrideFactoryBeanSatisfyInit(bean);

				context.unbranch();
			}
		}

		context.unbranch();
	}

	/**
	 * Overrides the satisfyInit method for the factory bean to call the init
	 * method of the enclosed bean
	 * 
	 * @param bean
	 *            The bean about to be processed.
	 */
	protected void overrideFactoryBeanSatisfyInit(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		final Type beanType = bean.getType();
		final String initMethodName = bean.getInitMethod();
		final Method initMethod = beanType.findMethod(initMethodName, Collections.<Type>emptyList());
		if (null == initMethod || initMethod.isStatic() || initMethod.getVisibility() != Visibility.PUBLIC) {
			throwInitMethodNotFound(bean, initMethodName);
		}

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("Overriding satisfyInit to call " + initMethod);

		final NewType beanFactory = bean.getFactoryBean();
		final Method beanFactoryInitMethod = beanFactory.getMostDerivedMethod(Constants.SATISFY_INIT, this
				.getParameterListWithOnlyObject());
		final NewMethod newMethod = beanFactoryInitMethod.copy(beanFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final InvokeMethodTemplatedFile body = new InvokeMethodTemplatedFile();
		body.setType(beanType);
		body.setMethod(initMethod);

		final NewMethodParameter instanceParameter = (NewMethodParameter) newMethod.getParameters().get(0);
		instanceParameter.setFinal(true);
		instanceParameter.setName(Constants.SATISFY_INIT_INSTANCE_PARAMETER);

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
	 *            The beans
	 */
	protected void overrideAllFactoryBeanSatisfyProperties(final Set<Bean> beans) {
		Checker.notNull("parameter:beans", beans);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Overriding satisfyProperties method for bean(s) with 1 or more properties.");

		final Iterator<Bean> iterator = beans.iterator();
		while (iterator.hasNext()) {

			context.delayedBranch();

			final Bean bean = iterator.next();
			final String id = bean.getId();
			context.debug(id);

			while (true) {
				if (bean instanceof Rpc) {
					this.overrideFactoryBeanSatisfyPropertiesWithSettingServiceEntryPoint((Rpc) bean);
					break;
				}
				this.overrideFactoryBeanSatisfyProperties(bean);
				break;
			}

			context.unbranch();
		}

		context.unbranch();
	}

	/**
	 * Overrides the satisfyProperties method of the factory bean to set the
	 * address property.
	 * 
	 * @param rpc
	 *            The rpc
	 */
	protected void overrideFactoryBeanSatisfyPropertiesWithSettingServiceEntryPoint(final Rpc rpc) {
		Checker.notNull("parameter:rpc", rpc);

		final NewType factoryBean = rpc.getFactoryBean();

		final Method method = factoryBean.getMostDerivedMethod(Constants.SATISFY_PROPERTIES, this.getParameterListWithOnlyObject());
		final NewMethod newMethod = method.copy(factoryBean);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final SetPropertiesTemplatedFile body = new SetPropertiesTemplatedFile();
		newMethod.setBody(body);

		final NewMethodParameter instanceParameter = (NewMethodParameter) newMethod.getParameters().get(0);
		instanceParameter.setFinal(true);
		instanceParameter.setName(Constants.SATISFY_PROPERTIES_INSTANCE_PARAMETER);

		final Type serviceDefTarget = this.getServiceDefTarget();
		body.setBean(serviceDefTarget);

		final GeneratorContext context = this.getGeneratorContext();

		final Type string = context.getString();
		final Method setter = serviceDefTarget.getMostDerivedMethod(Constants.SET_SERVICE_ENTRY_POINT, Collections.nCopies(1, string));
		final StringValue addressValue = new StringValue();
		addressValue.setGeneratorContext(context);
		addressValue.setType(string);

		final String serviceEntryPoint = rpc.getServiceEntryPoint();
		addressValue.setValue(serviceEntryPoint);

		body.addProperty(setter, addressValue);

		context.debug("serviceEntryPoint: \"" + serviceEntryPoint + "\"");
	}

	/**
	 * Override the factory bean method to set all property values after
	 * validating a single unambiguous setter exists
	 * 
	 * @param bean
	 *            The bean being processed.
	 */
	protected void overrideFactoryBeanSatisfyProperties(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		final GeneratorContext context = this.getGeneratorContext();
		final Type voidType = this.getGeneratorContext().getVoid();
		final Type beanType = bean.getType();
		context.debug(beanType.getName());

		final SetPropertiesTemplatedFile body = new SetPropertiesTemplatedFile();
		body.setBean(beanType);

		final NewType factoryBean = bean.getFactoryBean();
		final Method method = factoryBean.getMostDerivedMethod(Constants.SATISFY_PROPERTIES, this.getParameterListWithOnlyObject());

		final NewMethod newMethod = method.copy(factoryBean);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final NewMethodParameter instanceParameter = (NewMethodParameter) newMethod.getParameters().get(0);
		instanceParameter.setFinal(true);
		instanceParameter.setName(Constants.SATISFY_PROPERTIES_INSTANCE_PARAMETER);

		newMethod.setBody(body);

		// loop thru all properties
		final Set<Property> properties = bean.getProperties();
		final Iterator<Property> propertyIterator = properties.iterator();
		while (propertyIterator.hasNext()) {
			final Property property = propertyIterator.next();
			final String propertyName = property.getName();
			final String setterName = GeneratorHelper.buildSetterName(propertyName);

			final Value value = property.getValue();
			this.prepareValue(value);

			final List<Method> matchingSetters = new ArrayList<Method>();

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
						final List<MethodParameter> parameters = method.getParameters();
						if (parameters.size() != 1) {
							break;
						}

						final MethodParameter parameter = (MethodParameter) parameters.get(0);
						final Type propertyType = parameter.getType();
						if (false == value.isCompatibleWith(propertyType)) {
							break;
						}

						value.setPropertyType(propertyType);
						matchingSetters.add(method);
						break;
					}

					return false;
				}

				protected boolean skipJavaLangObjectMethods() {
					return true;
				}
			};

			visitor.start(beanType);
			if (matchingSetters.isEmpty()) {
				this.throwUnableToFindSetter(bean, property);
			}
			if (matchingSetters.size() != 1) {
				this.throwTooManySettersFound(bean, property);
			}

			final Method setter = (Method) matchingSetters.get(0);
			body.addProperty(setter, value);

			context.debug(propertyName + "=" + value);
		}
	}

	protected void prepareValue(final Value value) {
		while (true) {
			if (value instanceof BeanReferenceImpl) {
				this.prepareBeanReferenceImpl((BeanReferenceImpl) value);
				break;
			}
			if (value instanceof ListValue) {
				this.prepareListValue((ListValue) value);
				break;
			}
			if (value instanceof SetValue) {
				this.prepareSetValue((SetValue) value);
				break;
			}
			if (value instanceof MapValue) {
				this.prepareMapValue((MapValue) value);
				break;
			}
			if (value instanceof ImageValue) {
				this.prepareImageValue((ImageValue) value);
				break;
			}
			break;
		}
	}

	/**
	 * Allocates the getter name (getImage#) for the given imageValue, and also
	 * adds the imageValue to a list.
	 * 
	 * @param imageValue
	 */
	protected void prepareImageValue(final ImageValue imageValue) {
		final List<ImageValue> imageValues = this.getImageValues();
		final int index = imageValues.size();
		imageValue.setImageIndex(index);

		imageValues.add(imageValue);
	}

	private List<ImageValue> imageValues;

	protected List<ImageValue> getImageValues() {
		Checker.notNull("field:imageValues", this.imageValues);
		return this.imageValues;
	}

	protected void setImageValues(final List<ImageValue> imageValues) {
		Checker.notNull("parameter:imageValues", imageValues);
		this.imageValues = imageValues;
	}

	protected List<ImageValue> createImageValues() {
		return new ArrayList<ImageValue>();
	}

	protected void prepareBeanReferenceImpl(final BeanReferenceImpl beanReference) {
		final Bean referencedBean = (Bean) this.getBean(beanReference.getId());
		beanReference.setType(referencedBean.getValueType());
	}

	protected void prepareListValue(final ListValue list) {
		this.prepareCollection(list.getElements());
	}

	protected void prepareSetValue(final SetValue set) {
		this.prepareCollection(set.getElements());
	}

	protected void prepareMapValue(final MapValue map) {
		this.prepareCollection(map.getEntries().values());
	}

	protected void prepareCollection(final Collection<Value> values) {
		final Iterator<Value> iterator = values.iterator();
		while (iterator.hasNext()) {
			this.prepareValue(iterator.next());
		}
	}

	protected void throwTooManySettersFound(final Bean bean, final Property property) {
		//final String name = property.getName();
		final Value value = property.getValue();
		throw new BeanFactoryGeneratorException("The bean \"" + bean.getId() + "\" contains more than one setter for the property "
				+ property + " with a value of " + value + " on type \"" + bean.getTypeName() + "\".");
	}

	protected void throwUnableToFindSetter(final Bean bean, final Property property) {
		final String name = property.getName();
		//final Value value = property.getValue();
		throw new BeanFactoryGeneratorException("Unable to find a setter for the property \"" + name + "\" on the bean \""
				+ bean.getId() + "\" which is a \"" + bean.getTypeName() + "\".");
	}

	/**
	 * Creates an inner class belonging to the BeanFactory being generated with
	 * getters for each image property.
	 */
	protected void createImageFactoryIfNecessary() {
		final GeneratorContext context = this.getGeneratorContext();
		context.delayedBranch();
		context.info("Creating an ImageFactory which will supply all images.");

		final Iterator<ImageValue> i = this.getImageValues().iterator();
		NewType imageFactory = null;
		while (i.hasNext()) {
			if (null == imageFactory) {
				imageFactory = this.createImageFactory();

				context.branch();
				context.debug("Creating abstract getters on image factory");
			}

			final ImageValue imageValue = i.next();

			// create abstract getter
			this.addImageFactoryAbstractImageGetter(imageValue, imageFactory);
		}

		if (null != imageFactory) {
			context.unbranch();
		}
		context.unbranch();
	}

	/**
	 * Creates a ImageFactory as nested type of the bean factory being
	 * generated.
	 * 
	 * @return
	 */
	protected NewType createImageFactory() {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Creating Image Factory");

		final NewType beanFactory = this.getBeanFactory();

		final NewNestedInterfaceType imageFactory = beanFactory.newNestedInterfaceType();
		imageFactory.setNestedName(Constants.IMAGE_FACTORY_NESTED_CLASS_NAME);
		imageFactory.setStatic(true);
		imageFactory.setSuperType(this.getImageFactory());
		imageFactory.setVisibility(Visibility.PACKAGE_PRIVATE);

		this.addImageFactoryField(imageFactory);
		this.addImageFactoryGetter(imageFactory);
		this.addImageFactorySetter(imageFactory);

		context.unbranch();

		return imageFactory;
	}

	/**
	 * Adds a field to the bean factory to hold the image factory.
	 * 
	 * @param imageFactory
	 */
	protected void addImageFactoryField(final Type imageFactory) {
		Checker.notNull("parameter:imageFactory", imageFactory);

		this.getGeneratorContext().debug("Created field.");

		// add a field to hold the new imageFactory type.
		final NewType beanFactory = this.getBeanFactory();
		final NewField field = beanFactory.newField();
		field.setFinal(false);
		field.setName(Constants.IMAGE_FACTORY_FIELDNAME);
		field.setStatic(false);
		field.setTransient(false);
		field.setType(imageFactory);
		field.setValue(EmptyCodeBlock.INSTANCE);
		field.setVisibility(Visibility.PRIVATE);
	}

	/**
	 * Creates and adds a setter for the image factory belonging to the bean
	 * factory.
	 * 
	 * @param imageFactory
	 */
	protected void addImageFactorySetter(final Type imageFactory) {
		Checker.notNull("parameter:imageFactory", imageFactory);

		this.getGeneratorContext().debug("Created setter.");

		final NewType beanFactory = this.getBeanFactory();

		final NewMethod setter = beanFactory.newMethod();
		setter.setAbstract(false);
		setter.setBody(new ImageFactorySetterTemplatedFile());
		setter.setFinal(true);
		setter.setName(Constants.IMAGE_FACTORY_SETTER_NAME);
		setter.setNative(false);
		setter.setReturnType(this.getGeneratorContext().getVoid());
		setter.setStatic(false);
		setter.setVisibility(Visibility.PROTECTED);

		final NewMethodParameter setterParameter = setter.newParameter();
		setterParameter.setFinal(true);
		setterParameter.setName(Constants.IMAGE_FACTORY_SETTER_PARAMETER_NAME);
		setterParameter.setType(imageFactory);
	}

	/**
	 * Creates and adds a getter method which lazy loads the created image
	 * factory via deferred binding.
	 * 
	 * @param imageFactory
	 */
	protected void addImageFactoryGetter(final Type imageFactory) {
		Checker.notNull("parameter:imageFactory", imageFactory);

		this.getGeneratorContext().debug("Created getter.");

		final NewType beanFactory = this.getBeanFactory();

		final NewMethod getter = beanFactory.newMethod();
		getter.setAbstract(false);
		getter.setFinal(true);
		getter.setName(Constants.IMAGE_FACTORY_GETTER_NAME);
		getter.setNative(false);
		getter.setReturnType(imageFactory);
		getter.setStatic(false);
		getter.setVisibility(Visibility.PROTECTED);

		final ImageFactoryGetterTemplatedFile body = new ImageFactoryGetterTemplatedFile();
		body.setImageFactory(imageFactory);
		getter.setBody(body);
	}

	/**
	 * Helper to fetch the ImageFactory type.
	 * 
	 * @return
	 */
	protected Type getImageFactory() {
		return this.getGeneratorContext().getType(Constants.IMAGE_FACTORY);
	}

	/**
	 * This method adds an abstract method to the provided image factory marking
	 * it with annotations containing the image details.
	 * 
	 * @param imageValue
	 * @param imageFactory
	 */
	protected void addImageFactoryAbstractImageGetter(final ImageValue imageValue, final NewType imageFactory) {
		Checker.notNull("parameter:imageValue", imageValue);
		Checker.notNull("parameter:imageFactory", imageFactory);

		final NewMethod newMethod = imageFactory.newMethod();
		newMethod.setAbstract(true);
		newMethod.setFinal(false);
		newMethod.setName(Constants.IMAGE_GETTER_PREFIX + imageValue.getImageIndex());
		newMethod.setNative(false);
		newMethod.setReturnType(imageValue.getPropertyType());
		newMethod.setStatic(false);
		newMethod.setVisibility(Visibility.PUBLIC);

		// add annotations
		newMethod.addMetaData(ImageFactoryConstants.IMAGE_FILE, imageValue.getFile());
		newMethod.addMetaData(ImageFactoryConstants.LOCATION, imageValue.isLocal() ? ImageFactoryConstants.LOCATION_LOCAL
				: ImageFactoryConstants.LOCATION_SERVER);
		newMethod.addMetaData(ImageFactoryConstants.SERVER_REQUEST, imageValue.isLazy() ? ImageFactoryConstants.SERVER_REQUEST_LAZY
				: ImageFactoryConstants.SERVER_REQUEST_EAGER);

		imageValue.setImageFactoryGetter(newMethod);
	}

	/**
	 * Visit all defined beans singling singleton beans with custom destroy
	 * methods.
	 * 
	 * @param beans
	 *            The beans
	 */
	protected void overrideAllSingletonFactoryBeanToInvokeCustomDestroy(final Set<Bean> beans) {
		Checker.notNull("parameter:beans", beans);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Visiting singleton beans.");

		final Iterator<Bean> iterator = beans.iterator();
		int singletonCount = 0;
		int customDestroyMethodCount = 0;

		while (iterator.hasNext()) {
			final Bean bean = (Bean) iterator.next();
			if (false == bean.isSingleton()) {
				continue;
			}

			final String destroyMethodName = bean.getDestroyMethod();

			if (false == Tester.isNullOrEmpty(destroyMethodName)) {
				context.branch();
				context.debug(bean.getId());

				this.overrideSingletonFactoryBeanDestroy(bean);

				context.unbranch();
			}
		}
		context.debug(singletonCount == 0 ? "No singletons" : customDestroyMethodCount + " singletons have custom destroy methods.");

		context.unbranch();
	}

	/**
	 * Overrides the destroy method for the given singleton factory bean to call
	 * the custom destroy method.
	 * 
	 * @param bean
	 *            The bean
	 */
	protected void overrideSingletonFactoryBeanDestroy(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		final Type beanType = bean.getType();
		final String destroyMethodName = bean.getDestroyMethod();
		final Method destroyMethod = beanType.findMethod(destroyMethodName, Collections.<Type>emptyList());
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

		final NewMethodParameter instanceParameter = (NewMethodParameter) newMethod.getParameters().get(0);
		instanceParameter.setFinal(true);
		instanceParameter.setName(Constants.DESTROY_INSTANCE_PARAMETER);

		newMethod.setBody(body);
	}

	/**
	 * This method throws an exception when the specified custom method does not
	 * exist, is not public or is static.
	 * 
	 * @param bean
	 * @param destroyMethodName
	 */
	protected void throwCustomMethodNotFound(final Bean bean, final String destroyMethodName) {
		throw new BeanFactoryGeneratorException("Unable to find a public method called \"" + destroyMethodName + "\" on the bean "
				+ bean);
	}

	/**
	 * Records and registers all the given aliases
	 * 
	 * @param aliases
	 *            A set containing all the aliases encountered in all combined
	 *            xml documents.
	 */
	protected void recordAliases(final Set<Alias> aliases) {
		Checker.notNull("parameter:aliases", aliases);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Processing all alias (" + aliases.size() + ") tags.");

		final Iterator<Alias> iterator = aliases.iterator();
		while (iterator.hasNext()) {
			final Alias alias = iterator.next();
			final String name = alias.getName();
			final String bean = alias.getBean();

			context.debug(name + "=" + bean);

			this.checkAlias(alias);

			this.addAlias(alias);
		}

		context.unbranch();
	}

	/**
	 * Factory method which creates the rpc FactoryBean
	 * 
	 * @param rpc
	 *            The rpc bean under construction.
	 */
	protected void createRpcFactoryBean(final Rpc rpc) {
		Checker.notNull("parameter:rpc", rpc);

		final GeneratorContext context = this.getGeneratorContext();

		rpc.setSingleton(true);
		rpc.setEagerLoaded(false);

		context.debug("singleton");
		context.debug("lazy load");

		final String interfaceName = rpc.getServiceInterface();
		final String id = rpc.getId();
		final Type beanType = this.getInterfaceType(id, interfaceName + Constants.ASYNC_SUFFIX);
		rpc.setType(beanType);

		context.debug("service interface: " + interfaceName);
		context.debug("async service interface: " + beanType.getName());

		final Type superType = this.getSingletonFactoryBean();
		final NewConcreteType beanFactory = this.getBeanFactory();
		final NewNestedType factoryBean = beanFactory.newNestedType();
		factoryBean.setStatic(false);
		factoryBean.setNestedName(this.escapeBeanIdToBeClassNameSafe(id) + Constants.FACTORY_BEAN_SUFFIX);
		factoryBean.setSuperType(superType);
		factoryBean.setVisibility(Visibility.PRIVATE);
		rpc.setFactoryBean(factoryBean);

		context.debug("FactoryBean: " + factoryBean.getName());

		this.addBean(rpc);
	}

	/**
	 * Visits all advisors, checking they really are advisors and then adding
	 * them to the respective bean.
	 * 
	 * @param aspects
	 *            All the aspects within all xml files.
	 */
	protected void buildAspects(final Set<Aspect> aspects) {
		Checker.notNull("parameter:aspects", aspects);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Processing and verifying " + aspects.size() + " aspect(s).");

		final MethodMatcherFactory methodMatcherFactory = createMethodMatcherFactory();

		final Iterator<Aspect> iterator = aspects.iterator();
		while (iterator.hasNext()) {
			final Aspect aspect = iterator.next();

			context.branch();
			final String advisorId = aspect.getAdvisor();
			final String targetBeanId = aspect.getTarget();
			context.debug(advisorId + "=" + targetBeanId);

			aspect.setMethodMatcher(methodMatcherFactory.create(aspect.getMethodExpression()));

			final Bean bean = this.getBean(targetBeanId);
			this.verifyProxyTarget(bean);
			this.verifyAdvisorBean(aspect);
			this.verifyMethodExpression(aspect);

			bean.addAspect(aspect);

			context.unbranch();
		}

		context.unbranch();
	}

	protected MethodMatcherFactory createMethodMatcherFactory() {
		return new MethodMatcherFactory();
	}

	/**
	 * Verifies that the proxy target bean is not final so that it can be
	 * subclassed.
	 * 
	 * @param bean
	 *            The bean
	 */
	protected void verifyProxyTarget(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		final Type beanType = bean.getType();
		if (beanType.isFinal()) {
			this.throwProxyTargetCannotBeSubclassed(bean);
		}
	}

	protected void throwProxyTargetCannotBeSubclassed(final Bean bean) {
		throw new BeanFactoryGeneratorException("Unable to create proxy because target bean is final, bean: " + bean);
	}

	/**
	 * Checks that the advisor bean exists and is really an aspect of some sort
	 * 
	 * @param aspect
	 *            The aspect containing the advisor
	 */
	protected void verifyAdvisorBean(final Aspect aspect) {
		Checker.notNull("parameter:aspect", aspect);

		final String id = aspect.getAdvisor();
		final Bean bean = this.getBean(id);
		final Type type = bean.getType();

		if (false == type.isAssignableTo(this.getAdvice())) {
			throwNotAnAdviceException(bean);
		}
	}

	protected void throwNotAnAdviceException(final Bean bean) {
		throw new BeanFactoryGeneratorException("The so called advisor bean is not actually an aspect, bean: " + bean);
	}

	/**
	 * Verifies that the method expression matches at least one public method.
	 * 
	 * @param aspect
	 *            The aspect
	 */
	protected void verifyMethodExpression(final Aspect aspect) {
		Checker.notNull("parameter:aspect", aspect);

		final String id = aspect.getTarget();
		final Bean bean = this.getBean(id);
		final Type type = bean.getType();
		final MethodMatcher matcher = aspect.getMethodMatcher();

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug("Discovering methods that match: " + matcher + " against " + type);

		final List<Method> matchedMethods = new ArrayList<Method>();
		final Type object = context.getObject();

		final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {

			@Override
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
					context.debug(method.toString());
				}
				return false;
			}

			@Override
			protected boolean skipJavaLangObjectMethods() {
				return false;
			}
		};
		visitor.start(type);

		if (matchedMethods.isEmpty()) {
			throwNoMatchedMethods(aspect);
		}

		context.debug("Matched " + matchedMethods.size() + " methods(s).");
		context.unbranch();
	}

	protected void throwTargetMethodIsFinal(final Method method) {
		throw new BeanFactoryGeneratorException("The method " + method
				+ " which is final, prevents a proxy from being generated (this is achieved via subclassing).");
	}

	protected void throwNoMatchedMethods(final Aspect aspect) {
		throw new BeanFactoryGeneratorException("The aspect expression does not match any methods, aspect: " + aspect);
	}

	/**
	 * Creates a factory bean for each bean that will be proxied.
	 */
	protected void applyAspects() {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Checking for beans with one or more aspects.");

		final Set<Bean> beans = this.filterBeansRequiringInterceptors();

		final Iterator<Bean> advisedIterator = beans.iterator();
		while (advisedIterator.hasNext()) {
			final Bean bean = advisedIterator.next();
			this.buildProxyFactoryBean(bean);

			this.registerProxiedBean(bean);
		}

		context.unbranch();
	}

	protected Set<Bean> filterBeansRequiringInterceptors() {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.debug("Filtering out Beans that dont have any aspects.");

		final Set<Bean> advised = new HashSet<Bean>();

		final Map<String,Bean> beans = this.getBeans();
		final Iterator<Bean> beansIterator = beans.values().iterator();

		while (beansIterator.hasNext()) {
			final Bean bean = beansIterator.next();
			final List<Aspect> aspectss = bean.getAspects();
			if (aspectss.isEmpty()) {
				context.debug(bean.getId());
				continue;
			}
			advised.add(bean);

			context.debug(bean.getId());
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
		Checker.notNull("parameter:bean", bean);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();

		final String id = bean.getId();
		context.debug(id);

		final Type superType = this.getProxyFactoryBean();

		final NewConcreteType beanFactory = this.getBeanFactory();
		final NewNestedType proxyFactoryBean = beanFactory.newNestedType();
		proxyFactoryBean.setStatic(false);
		proxyFactoryBean.setNestedName(this.escapeBeanIdToBeClassNameSafe(id) + Constants.PROXY_FACTORY_BEAN_SUFFIX);
		proxyFactoryBean.setSuperType(superType);
		proxyFactoryBean.setVisibility(Visibility.PRIVATE);

		bean.setProxyFactoryBean(proxyFactoryBean);

		context.debug("ProxyFactoryBean superType: " + superType);

		final NewNestedType proxy = this.createProxy(bean);
		bean.setProxy(proxy);

		this.overrideProxyFactoryBeanCreateProxy(bean);
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
		Checker.notNull("parameter:bean", bean);

		final Type targetBeanType = bean.getType();
		final String id = bean.getId();

		// sub class the target...
		final NewConcreteType beanFactory = this.getBeanFactory();
		final NewNestedType proxy = beanFactory.newNestedType();
		proxy.setStatic(false);
		proxy.setNestedName(this.escapeBeanIdToBeClassNameSafe(id) + Constants.PROXY_SUFFIX);

		if (bean instanceof Rpc) {
			proxy.addInterface(targetBeanType);
		} else {
			proxy.setSuperType(targetBeanType);
		}
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

		final List<Aspect> aspects = bean.getAspects();

		final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {

			@Override
			protected boolean visit(final Method method) {

				while (true) {
					if (method.isFinal()) {
						break;
					}

					if (method.getVisibility() != Visibility.PUBLIC) {
						break;
					}

					// dont proxy getClass()
					if (method.getName().equals("getClass") && method.getParameters().isEmpty()) {
						break;
					}

					// the public methods remain...
					final List<Aspect> matched = BeanFactoryGenerator.this.findMatchingAdvices(method, aspects);
					if (matched.isEmpty()) {
						BeanFactoryGenerator.this.createProxyMethod(proxy, method);
						break;
					}

					BeanFactoryGenerator.this.createProxyMethodWithInterceptors(proxy, method, matched);
					break;
				}
				return false;
			}

			@Override
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
	 *            The method being processed
	 * @param aspects
	 *            A list of all advices for the bean
	 * @return A list of only matching advices
	 */
	protected List<Aspect> findMatchingAdvices(final Method method, final List<Aspect> aspects) {
		final Iterator<Aspect> advicesIterator = aspects.iterator();
		final List<Aspect> applicable = new ArrayList<Aspect>();

		while (advicesIterator.hasNext()) {
			final Aspect aspect = advicesIterator.next();
			if (aspect.getMethodMatcher().matches(method)) {
				applicable.add(aspect);
			}
		}

		return applicable;
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
		Checker.notNull("parameter:proxy", proxy);
		Checker.notNull("parameter:method", method);

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
	 * @param aspects
	 *            A list of aspects for this method
	 */
	protected void createProxyMethodWithInterceptors(final NewNestedType proxy, final Method method, final List<Aspect> aspects) {
		Checker.notNull("parameter:proxy", proxy);
		Checker.notNull("parameter:method", method);
		Checker.notNull("parameter:advices", aspects);

		final NewMethod newMethod = method.copy(proxy);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		GeneratorHelper.renameParametersToParameterN(newMethod);
		GeneratorHelper.makeAllParametersFinal(newMethod);

		final ProxyInterceptedMethodTemplatedFile body = new ProxyInterceptedMethodTemplatedFile();
		body.setAspects(aspects);
		body.setBeanFactory(this.getBeanFactory());
		body.setMethod(newMethod);
		body.setTargetMethod(method);

		final Type methodInterceptor = this.getMethodInterceptor();
		final List<Type> methodInvocationParameterList = Arrays.asList(new Type[] { this.getMethodInvocation() });
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
		Checker.notNull("parameter:bean", bean);

		final NewNestedType proxyFactoryBean = bean.getProxyFactoryBean();
		final List<Type> objectParameterList = this.getParameterListWithOnlyObject();
		final Method method = proxyFactoryBean.getMostDerivedMethod(Constants.CREATE_PROXY, objectParameterList);

		final NewMethod newMethod = method.copy(proxyFactoryBean);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		// add a new constructor...
		final MethodParameter targetBeanParameter = (MethodParameter) newMethod.getParameters().get(0);

		final Type proxy = bean.getProxy();
		final Constructor constructor = proxy.getConstructor(Collections.<Type>emptyList());

		final CreateProxyTemplatedFile body = new CreateProxyTemplatedFile();
		body.setProxyConstructor(constructor);
		body.setTargetBeanParameter(targetBeanParameter);
		body.setTargetBeanType(bean.getType());

		newMethod.setBody(body);
	}

	/**
	 * Re ads the given proxy target under a new name. The proxy factory bean
	 * will retain the original name and will refer to this factory bean as the
	 * source of the bean being advised.
	 * 
	 * @param bean
	 *            The proxy bean.
	 */
	protected void registerProxiedBean(final Bean bean) {
		Bean copy = null;
		while (true) {
			if (bean instanceof NestedBean) {
				copy = this.copyNestedBean((NestedBean) bean);
				break;
			}
			if (bean instanceof Rpc) {
				copy = this.copyRpc((Rpc) bean);
				break;
			}

			copy = this.copyBean(bean);
			break;
		}

		this.addBean(copy);
	}

	protected Bean copyRpc(final Rpc rpc) {
		Checker.notNull("parameter:rpc", rpc);

		final Rpc copy = new Rpc();
		copy.setEagerLoaded(rpc.isEagerLoaded());
		copy.setFactoryBean(rpc.getFactoryBean());
		copy.setFactoryMethod(rpc.getFactoryMethod());
		copy.setId(Constants.PROXY_TARGET_FACTORY_BEAN_PREFIX + rpc.getId());
		copy.setSingleton(rpc.isSingleton());
		copy.setServiceEntryPoint(rpc.getServiceEntryPoint());
		copy.setServiceInterface(rpc.getServiceInterface());
		return copy;
	}

	protected NestedBean copyNestedBean(final NestedBean nestedBean) {
		Checker.notNull("parameter:nestedBean", nestedBean);

		final NestedBean copy = new NestedBean();
		this.copyProxiedBeanProperties(nestedBean, copy);
		return copy;
	}

	protected Bean copyBean(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		final Bean copy = new Bean();
		this.copyProxiedBeanProperties(bean, copy);
		return copy;
	}

	protected void copyProxiedBeanProperties(final Bean source, final Bean target) {
		target.setEagerLoaded(target.isEagerLoaded());
		target.setFactoryBean(source.getFactoryBean());
		target.setFactoryMethod(source.getFactoryMethod());
		target.setId(Constants.PROXY_TARGET_FACTORY_BEAN_PREFIX + source.getId());
		target.setSingleton(source.isSingleton());
		target.setType(source.getType());
		target.setTypeName(source.getTypeName());
	}

	/**
	 * Adds a new method to the bean factory being built that populates a map
	 * with all the factory beans keyed by bean id.
	 */
	protected void overrideBeanFactoryRegisterFactoryBeans() {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();

		final NewType beanFactory = this.getBeanFactory();
		final Method abstractMethod = beanFactory.getSuperType().getMostDerivedMethod(Constants.REGISTER_FACTORY_BEANS, Collections.<Type>emptyList());

		final NewMethod newMethod = abstractMethod.copy(beanFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final RegisterFactoryBeansTemplatedFile body = new RegisterFactoryBeansTemplatedFile();
		newMethod.setBody(body);

		context.branch();
		context.info("Overriding " + newMethod + " to register all beans.");

		final Iterator<Bean> beansIterator = this.getBeans().values().iterator();

		while (beansIterator.hasNext()) {
			final Bean bean = beansIterator.next();
			body.addBean(bean);

			context.debug(bean.getId());
		}
		context.unbranch();
	}

	/**
	 * Overrides the
	 * {@link rocket.beans.client.BeanFactoryImpl#getAliasesToBeans} to return a
	 * list of alias to bean mappings. This list may be empty if no aliases were
	 * present in config files.
	 */
	protected void registerBeanAliases() {
		this.overrideBeanFactoryGetAliasesToBeans();
	}

	protected void overrideBeanFactoryGetAliasesToBeans() {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();

		final NewType beanFactory = this.getBeanFactory();
		final Method abstractMethod = beanFactory.getSuperType().getMostDerivedMethod(Constants.GET_ALIASES_TO_BEANS_METHOD, Collections.<Type>emptyList());

		final NewMethod newMethod = abstractMethod.copy(beanFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final GetAliasesToBeans body = new GetAliasesToBeans();
		newMethod.setBody(body);

		context.branch();
		context.info("Overriding " + newMethod + " to register all aliases.");

		final Iterator<Alias> beansIterator = this.getAliases().values().iterator();
		int aliasCount = 0;

		while (beansIterator.hasNext()) {
			final Alias alias = beansIterator.next();
			final String from = alias.getName();
			final String to = alias.getBean();
			body.register(from, to);

			context.debug(from + "=" + to);
			aliasCount++;
		}

		context.debug("Registered " + aliasCount + " aliases.");
		context.unbranch();
	}

	/**
	 * Overrides the
	 * {@link rocket.beans.client.BeanFactoryImpl#getEagerSingletonBeanNames()
	 * which contains a comma separated list of singletons that need to
	 * initialized on factory startup.
	 */
	protected void overrideLoadEagerBeans() {
		final Map<String, Bean> beans = this.getBeans();

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Overriding BeanFactory." + Constants.GET_EAGER_SINGELTON_BEAN_NAMES_METHOD
				+ "() to initialize eager singleton beans on factory startup.");
		context.branch();

		final NewType beanFactory = this.getBeanFactory();
		final Method abstractMethod = beanFactory.getSuperType().getMostDerivedMethod(Constants.GET_EAGER_SINGELTON_BEAN_NAMES_METHOD,Collections.<Type>emptyList());

		final NewMethod newMethod = abstractMethod.copy(beanFactory);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final GetEagerSingletonBeanNames body = new GetEagerSingletonBeanNames();
		newMethod.setBody(body);

		int eagerSingletonBeanCount = 0;
		int lazySingletonBeanCount = 0;

		final Iterator<Bean> beansIterator = beans.values().iterator();
		while (beansIterator.hasNext()) {
			final Bean bean = (Bean) beansIterator.next();

			if (false == bean.isSingleton()) {
				continue;
			}

			// only singletons can be singletons.
			final boolean eager = bean.isEagerLoaded();
			if (eager) {
				body.addBean(bean.getId());
				eagerSingletonBeanCount++;

				context.debug(bean.toString());
			} else {
				lazySingletonBeanCount++;
			}
		}
		context.unbranch();
		context.debug("When instantiated " + eagerSingletonBeanCount + " singletons will be eaglerly loaded, the remaining "
				+ lazySingletonBeanCount + " will be lazily loaded on first request.");
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
		Checker.notNull("field:beanFactory", beanFactory);
		return this.beanFactory;
	}

	protected void setBeanFactory(final NewConcreteType beanFactory) {
		Checker.notNull("parameter:beanFactory", beanFactory);
		this.beanFactory = beanFactory;
	}

	/**
	 * A map that contains all the NewType factory beans for each defined bean
	 * keyed on the bean's id.
	 */
	private Map<String,Bean> beans;

	public Map<String,Bean> getBeans() {
		Checker.notNull("field:beans", beans);
		return this.beans;
	}

	protected void setBeans(final Map<String,Bean> beans) {
		Checker.notNull("parameter:beans", beans);
		this.beans = beans;
	}

	/**
	 * A sorted TreeMap is used to guarantee that beans are processed in bean id
	 * sorted order which should make reading of logging info much easier.
	 * 
	 * @return A map
	 */
	protected Map<String,Bean> createBeans() {
		return new TreeMap<String,Bean>(String.CASE_INSENSITIVE_ORDER);
	}

	protected void checkIdIsUnique(final Bean bean) {
		final String id = bean.getId();
		if (this.getBeans().containsKey(id)) {
			throwBeanIdAlreadyUsed(bean);
		}
	}

	protected void throwBeanIdAlreadyUsed(final Bean bean) {
		final String id = bean.getId();
		throw new BeanFactoryGeneratorException("The id \"" + id + "\" is used by more than bean: " + bean + ", other bean: "
				+ this.getBean(id));
	}

	/**
	 * Simply asserts that the given bean if it itsa prototype has not been
	 * marked as eager=true.
	 * 
	 * @param bean
	 */
	protected void checkPrototypesAreNotMarkedAsEager(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		if (false == bean.isSingleton()) {
			if (bean.isEagerLoaded()) {
				this.throwPrototypesCantBeEagerlyLoaded(bean);
			}
		}
	}

	/**
	 * Adds a new bean to the beans registry
	 * 
	 * @param bean
	 *            The new bean
	 */
	protected void addBean(final Bean bean) {
		Checker.notNull("parameter:bean", bean);

		this.getBeans().put(bean.getId(), bean);
	}

	/**
	 * Retrieves a bean given an id searching both beans and alias to beans,
	 * throwing an exception if the id is not found.
	 * 
	 * @param id
	 *            The id
	 * @return The bean
	 */
	protected Bean getBean(final String id) {
		String resolvedId = id;

		// check if $id is actually an alias.
		Alias alias = (Alias) this.getAliases().get(id);
		if (null != alias) {
			resolvedId = alias.getBean();
		}

		// now try and find the bean...
		final Bean bean = (Bean) this.getBeans().get(resolvedId);
		if (null == bean) {
			this.throwUnableToFindBean(id);
		}
		return bean;
	}

	protected void throwUnableToFindBean(final String id) {
		throw new BeanFactoryGeneratorException("Unable to locate a bean with an id of \"" + id + "\".");
	}

	protected String getNextNestedBeanName() {
		final int i = this.getNestedBeanCount();
		this.setNestedBeanCount(i + 1);

		return Constants.NESTED_BEAN_NAME_PREFIX + i;
	}

	/**
	 * This counter is used to generate a unique bean name for each
	 * anonymous/nested bean.
	 */
	private int nestedBeanCount = 0;

	protected int getNestedBeanCount() {
		return this.nestedBeanCount;
	}

	protected void setNestedBeanCount(final int nestedBeanCount) {
		this.nestedBeanCount = nestedBeanCount;
	}

	/**
	 * A map that contains all the NewType factory aliases for each defined
	 * alias keyed on the alias's id.
	 */
	private Map<String,Alias> aliases;

	public Map<String,Alias> getAliases() {
		Checker.notNull("field:aliases", aliases);
		return this.aliases;
	}

	protected void setAliases(final Map<String,Alias> aliases) {
		Checker.notNull("parameter:aliases", aliases);
		this.aliases = aliases;
	}

	protected Map<String,Alias> createAliases() {
		return new HashMap<String,Alias>();
	}

	protected void checkAlias(final Alias alias) {
		final String name = alias.getName();
		if (this.getBeans().containsKey(name)) {
			throwAliasIdAlreadyUsed(alias);
		}
		this.getBean(alias.getBean());
	}

	protected void throwAliasIdAlreadyUsed(final Alias alias) {
		final String id = alias.getName();
		throw new BeanFactoryGeneratorException("The id \"" + id + "\" used by alias " + alias + " is already used by another bean: "
				+ this.getBean(id));
	}

	/**
	 * Adds a new alias to the aliases registry
	 * 
	 * @param alias
	 *            The new alias
	 */
	protected void addAlias(final Alias alias) {
		Checker.notNull("parameter:alias", alias);

		this.getAliases().put(alias.getName(), alias);
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
		return this.getGeneratorContext().getType(Constants.FACTORY_BEAN);
	}

	protected Type getDisposableBean() {
		return this.getGeneratorContext().getType(Constants.DISPOSABLE_BEAN);
	}

	/**
	 * Fetches the interface type for the given bean throwing an excecption if
	 * the type is not found or not an interface.
	 * 
	 * @param id
	 *            The id of a bean
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
	 *            The bean name
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

	protected List<Type> getParameterListWithOnlyObject() {
		return Collections.nCopies(1, this.getGeneratorContext().getObject());
	}

	protected void throwBeanTypeNotFound(final String id, final String className) {
		throw new BeanFactoryGeneratorException("Unable to find the type \"" + className + "\" for the bean with an id of \"" + id
				+ "\".");
	}

	protected void throwBeanTypeIsNotConcrete(final String id, final Type type) {
		throw new BeanFactoryGeneratorException("The type \"" + type + "\" is not concrete for the bean id\"" + id + "\".");
	}

	/**
	 * This helper accepts a bean name and outputs a name which is safe to use
	 * as the class name of a FactoryBean. THis is achieved by escaping invalid
	 * characters etc.
	 * 
	 * @param beanId
	 *            The bean name,
	 * @return The safe java class name.
	 */
	protected String escapeBeanIdToBeClassNameSafe(final String beanId) {
		Checker.notEmpty("parameter:beanId", beanId);

		final StringBuffer safeName = new StringBuffer();

		final char[] chars = beanId.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			final char c = chars[i];

			// escape underscore to double underscore...
			if (c == '_') {
				safeName.append("__");
				continue;
			}
			// if $c a valid chacter simply add it...
			if ((i == 0 && Character.isJavaIdentifierStart(c)) || Character.isJavaIdentifierPart(c)) {
				safeName.append(c);
				continue;
			}

			// not a safe character encode it as underscore + hex
			// value of $c.
			safeName.append('_');

			final String hexEncoded = Utilities.padLeft(Integer.toHexString(c), 4, '0');
			safeName.append(hexEncoded);
		} // for i
		return safeName.toString();
	}
}