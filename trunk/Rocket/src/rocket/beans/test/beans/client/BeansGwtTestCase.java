/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.beans.test.beans.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.beans.client.BeanFactory;
import rocket.beans.client.BeanFactoryImpl;
import rocket.beans.test.beans.client.alias.AliasBeanFactory;
import rocket.beans.test.beans.client.aliasedbeandoesntexist.AliasBeanDoesntExistBeanFactory;
import rocket.beans.test.beans.client.aliasnamealreadyexists.AliasFromBeanIdIsNotUniqueBeanFactory;
import rocket.beans.test.beans.client.ambiguousconstructors.AmbiguousConstructorsBeanFactory;
import rocket.beans.test.beans.client.ambiguoussetters.AmbiguousSettersBeanFactory;
import rocket.beans.test.beans.client.beanclassnotfound.NotFoundBeanFactory;
import rocket.beans.test.beans.client.beanreference.BeanReferenceBeanFactory;
import rocket.beans.test.beans.client.beanreference.ClassWithReferences;
import rocket.beans.test.beans.client.beanreferencetoalias.AliasedBean;
import rocket.beans.test.beans.client.beanreferencetoalias.AliasedBeanReferenceBeanFactory;
import rocket.beans.test.beans.client.beanreferencetoalias.ClassWithAliasedBeanReference;
import rocket.beans.test.beans.client.beantypenotconcrete.BeanTypeThatIsNotConcreteBeanFactory;
import rocket.beans.test.beans.client.booleanproperty.BooleanPropertyBeanFactory;
import rocket.beans.test.beans.client.booleanproperty.ClassWithBooleanProperty;
import rocket.beans.test.beans.client.byteproperty.BytePropertyBeanFactory;
import rocket.beans.test.beans.client.byteproperty.ClassWithByteProperty;
import rocket.beans.test.beans.client.charproperty.CharPropertyBeanFactory;
import rocket.beans.test.beans.client.charproperty.ClassWithCharProperty;
import rocket.beans.test.beans.client.constructornotfound.ConstructorNotFoundBeanFactory;
import rocket.beans.test.beans.client.destroymethod.BeanWithCustomDestroy;
import rocket.beans.test.beans.client.destroymethod.DestroyMethodBeanFactory;
import rocket.beans.test.beans.client.destroymethodnotfound.DestroyMethodNotFoundBeanFactory;
import rocket.beans.test.beans.client.disposablebean.DisposableBeanBeanFactory;
import rocket.beans.test.beans.client.disposablebean.DisposableBeanImpl;
import rocket.beans.test.beans.client.doubleproperty.ClassWithDoubleProperty;
import rocket.beans.test.beans.client.doubleproperty.DoublePropertyBeanFactory;
import rocket.beans.test.beans.client.eagerlyloadedsingleton.EagerlyLoadedSingletonBean;
import rocket.beans.test.beans.client.eagerlyloadedsingleton.EagerlyLoadedSingletonBeanFactory;
import rocket.beans.test.beans.client.escapebeanidintosafeclassnamecomponents.EscapeBeanIdIntoSafeClassNamesBeanFactory;
import rocket.beans.test.beans.client.factorybeanbeanreference.FactoryBeanBeanReferenceBeanFactory;
import rocket.beans.test.beans.client.factorybeanbeanreference.FactoryBeanProducedBean;
import rocket.beans.test.beans.client.factorymethod.Bean;
import rocket.beans.test.beans.client.factorymethod.FactoryMethodBeanFactory;
import rocket.beans.test.beans.client.factorymethodnotfound.FactoryMethodNotFoundBeanFactory;
import rocket.beans.test.beans.client.floatproperty.ClassWithFloatProperty;
import rocket.beans.test.beans.client.floatproperty.FloatPropertyBeanFactory;
import rocket.beans.test.beans.client.initmethod.BeanWithCustomInit;
import rocket.beans.test.beans.client.initmethod.InitMethodBeanFactory;
import rocket.beans.test.beans.client.initmethodnotfound.InitMethodNotFoundBeanFactory;
import rocket.beans.test.beans.client.intproperty.ClassWithIntProperty;
import rocket.beans.test.beans.client.intproperty.IntPropertyBeanFactory;
import rocket.beans.test.beans.client.invalidbeanscope.InvalidScopeBeanFactory;
import rocket.beans.test.beans.client.jsonrpcproperty.BeanWithJsonService;
import rocket.beans.test.beans.client.jsonrpcproperty.JsonServiceAsync;
import rocket.beans.test.beans.client.jsonrpcproperty.JsonServicePropertyBeanFactory;
import rocket.beans.test.beans.client.lazyloadedsingleton.LazyLoadedBeanFactory;
import rocket.beans.test.beans.client.lazyloadedsingleton.LazySingletonBean;
import rocket.beans.test.beans.client.listproperty.ClassWithListProperty;
import rocket.beans.test.beans.client.listproperty.ListPropertyBeanFactory;
import rocket.beans.test.beans.client.longproperty.ClassWithLongProperty;
import rocket.beans.test.beans.client.longproperty.LongPropertyBeanFactory;
import rocket.beans.test.beans.client.manyvalues.ClassWithManyValues;
import rocket.beans.test.beans.client.manyvalues.ManyValuesBeanFactory;
import rocket.beans.test.beans.client.mapproperty.ClassWithMapProperty;
import rocket.beans.test.beans.client.mapproperty.MapPropertyBeanFactory;
import rocket.beans.test.beans.client.missingbeanid.MissingBeanIdBeanFactory;
import rocket.beans.test.beans.client.morethanonebeanfactory.FirstBeanFactory;
import rocket.beans.test.beans.client.morethanonebeanfactory.SecondBeanFactory;
import rocket.beans.test.beans.client.multipleargumentsconstructor.ClassWithMultipleArgumentsConstructor;
import rocket.beans.test.beans.client.multipleargumentsconstructor.MultipleArgumentsConstructorBeanFactory;
import rocket.beans.test.beans.client.multipleincludedfiles.MultipleIncludedFilesBeanFactory;
import rocket.beans.test.beans.client.multipleincludedfilescycle.MultipleIncludedFilesCycleBeanFactory;
import rocket.beans.test.beans.client.noargumentsconstructor.ClassWithNoArgumentsConstructor;
import rocket.beans.test.beans.client.noargumentsconstructor.NoArgumentsConstructorBeanFactory;
import rocket.beans.test.beans.client.noproperties.ClassWithNoProperties;
import rocket.beans.test.beans.client.noproperties.NoPropertiesBeanFactory;
import rocket.beans.test.beans.client.notabeanfactory.ClassIsNotABeanFactory;
import rocket.beans.test.beans.client.notanadvice.NotAnAdviceBeanFactory;
import rocket.beans.test.beans.client.placeholders.PlaceHolderBean;
import rocket.beans.test.beans.client.placeholders.PlaceHolderBeanFactory;
import rocket.beans.test.beans.client.prototypewithdestroymethod.PrototypeWithDestroyMethodBeanFactory;
import rocket.beans.test.beans.client.proxybooleanparameterreturntype.ClassWithMethodWithBooleanParameterAndReturnType;
import rocket.beans.test.beans.client.proxybooleanparameterreturntype.ProxyBooleanBeanFactory;
import rocket.beans.test.beans.client.proxybooleanparameterreturntype.ProxyBooleanMethodInterceptor;
import rocket.beans.test.beans.client.proxybyteparameterreturntype.ClassWithMethodWithByteParameterAndReturnType;
import rocket.beans.test.beans.client.proxybyteparameterreturntype.ProxyByteBeanFactory;
import rocket.beans.test.beans.client.proxybyteparameterreturntype.ProxyByteMethodInterceptor;
import rocket.beans.test.beans.client.proxycharparameterreturntype.ClassWithMethodWithCharParameterAndReturnType;
import rocket.beans.test.beans.client.proxycharparameterreturntype.ProxyCharBeanFactory;
import rocket.beans.test.beans.client.proxycharparameterreturntype.ProxyCharMethodInterceptor;
import rocket.beans.test.beans.client.proxyclasswithfinalpublicmethod.ProxyFinalPublicMethodBeanFactory;
import rocket.beans.test.beans.client.proxycomplex.Complex;
import rocket.beans.test.beans.client.proxycomplex.ComplexMethodInterceptor;
import rocket.beans.test.beans.client.proxycomplex.ProxyComplexBeanFactory;
import rocket.beans.test.beans.client.proxydoubleparameterreturntype.ClassWithMethodWithDoubleParameterAndReturnType;
import rocket.beans.test.beans.client.proxydoubleparameterreturntype.ProxyDoubleBeanFactory;
import rocket.beans.test.beans.client.proxydoubleparameterreturntype.ProxyDoubleMethodInterceptor;
import rocket.beans.test.beans.client.proxyfinalclass.ProxyFinalClassBeanFactory;
import rocket.beans.test.beans.client.proxyfloatparameterreturntype.ClassWithMethodWithFloatParameterAndReturnType;
import rocket.beans.test.beans.client.proxyfloatparameterreturntype.ProxyFloatBeanFactory;
import rocket.beans.test.beans.client.proxyfloatparameterreturntype.ProxyFloatMethodInterceptor;
import rocket.beans.test.beans.client.proxyintparameterreturntype.ClassWithMethodWithIntParameterAndReturnType;
import rocket.beans.test.beans.client.proxyintparameterreturntype.ProxyIntBeanFactory;
import rocket.beans.test.beans.client.proxyintparameterreturntype.ProxyIntMethodInterceptor;
import rocket.beans.test.beans.client.proxylongparameterreturntype.ClassWithMethodWithLongParameterAndReturnType;
import rocket.beans.test.beans.client.proxylongparameterreturntype.ProxyLongBeanFactory;
import rocket.beans.test.beans.client.proxylongparameterreturntype.ProxyLongMethodInterceptor;
import rocket.beans.test.beans.client.proxyobjectparameterreturntype.ClassWithMethodWithObjectParameterAndReturnType;
import rocket.beans.test.beans.client.proxyobjectparameterreturntype.ProxyObjectBeanFactory;
import rocket.beans.test.beans.client.proxyobjectparameterreturntype.ProxyObjectMethodInterceptor;
import rocket.beans.test.beans.client.proxyreturnsvoid.ClassWithMethodThatReturnsVoid;
import rocket.beans.test.beans.client.proxyreturnsvoid.ProxyVoidBeanFactory;
import rocket.beans.test.beans.client.proxyreturnsvoid.ProxyVoidMethodInterceptor;
import rocket.beans.test.beans.client.proxyshortparameterreturntype.ClassWithMethodWithShortParameterAndReturnType;
import rocket.beans.test.beans.client.proxyshortparameterreturntype.ProxyShortBeanFactory;
import rocket.beans.test.beans.client.proxyshortparameterreturntype.ProxyShortMethodInterceptor;
import rocket.beans.test.beans.client.proxytargetthrowscheckedexception.CheckedException;
import rocket.beans.test.beans.client.proxytargetthrowscheckedexception.ClassWithMethodThatThrowsCheckedException;
import rocket.beans.test.beans.client.proxytargetthrowscheckedexception.ProxyTargetThrowsCheckedExceptionBeanFactory;
import rocket.beans.test.beans.client.proxytargetthrowscheckedexception.ProxyTargetThrowsCheckedExceptionMethodInterceptor;
import rocket.beans.test.beans.client.proxytargetthrowsuncheckedexception.ClassWithMethodThatThrowsUncheckedException;
import rocket.beans.test.beans.client.proxytargetthrowsuncheckedexception.ProxyTargetThrowsUncheckedExceptionBeanFactory;
import rocket.beans.test.beans.client.proxytargetthrowsuncheckedexception.UncheckedException;
import rocket.beans.test.beans.client.remotejsonservice.RemoteJsonServiceAsync;
import rocket.beans.test.beans.client.remotejsonservice.RemoteJsonServiceBeanFactory;
import rocket.beans.test.beans.client.remoterpcservice.RemoteRpcServiceAsync;
import rocket.beans.test.beans.client.remoterpcservice.RemoteRpcServiceBeanFactory;
import rocket.beans.test.beans.client.rpcproperty.BeanWithGwtRpcService;
import rocket.beans.test.beans.client.rpcproperty.GwtRpcServiceAsync;
import rocket.beans.test.beans.client.rpcproperty.GwtRpcServicePropertyBeanFactory;
import rocket.beans.test.beans.client.setproperty.ClassWithSetProperty;
import rocket.beans.test.beans.client.setproperty.SetPropertyBeanFactory;
import rocket.beans.test.beans.client.shortproperty.ClassWithShortProperty;
import rocket.beans.test.beans.client.shortproperty.ShortPropertyBeanFactory;
import rocket.beans.test.beans.client.stringproperty.ClassWithStringProperty;
import rocket.beans.test.beans.client.stringproperty.StringPropertyBeanFactory;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeansGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN_ID = "bean";

	final static String ADVISOR_BEAN_ID = "advisorBean";

	final static byte BYTE = 1;

	final static short SHORT = 23;

	final static int INT = 345;

	final static long LONG = 6789;

	final static float FLOAT = 12.34f;

	final static double DOUBLE = 56.789;

	public String getModuleName() {
		return "rocket.beans.test.beans.Beans";
	}
	
	public void testNotABeanFactory() {
		try {
			assertBindingFailed(GWT.create(ClassIsNotABeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void testMissingBeanId() {
		try {
			assertBindingFailed(GWT.create(MissingBeanIdBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			final String causeType = failed.getCauseType();
			assertTrue("" + failed, causeType.equals(SAX_PARSE_EXCEPTION) || causeType.equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testInvalidBeanScope() {
		try {
			assertBindingFailed(GWT.create(InvalidScopeBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			final String causeType = failed.getCauseType();
			assertTrue("" + failed, causeType.equals(SAX_PARSE_EXCEPTION) || causeType.equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testBeanClassNotFound() {
		try {
			assertBindingFailed(GWT.create(NotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testBeanTypeIsNotConcrete() {
		try {
			assertBindingFailed(GWT.create(BeanTypeThatIsNotConcreteBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testEscapeBeanIdIntoSafeClassNames() {
		final EscapeBeanIdIntoSafeClassNamesBeanFactory beanFactory = (EscapeBeanIdIntoSafeClassNamesBeanFactory) GWT
				.create(EscapeBeanIdIntoSafeClassNamesBeanFactory.class);
		assertNotNull(beanFactory);
	}

	public void testLazyLoadedSingletonBean() {
		final LazyLoadedBeanFactory beanFactory = (LazyLoadedBeanFactory)GWT.create(LazyLoadedBeanFactory.class);
		assertFalse("singleton is not yet loaded", LazySingletonBean.loaded );
		beanFactory.getBean( BEAN_ID );
		assertTrue("singleton is now loaded", LazySingletonBean.loaded );		
	}

	public void testEagerLoadedSingletonBean() {
		assertFalse("singleton should have not been have been loaded until factory is created.", EagerlyLoadedSingletonBean.loaded );
		final EagerlyLoadedSingletonBeanFactory beanFactory = (EagerlyLoadedSingletonBeanFactory)GWT.create(EagerlyLoadedSingletonBeanFactory.class);
		assertTrue("singleton should have been loaded", EagerlyLoadedSingletonBean.loaded );
	}

	public void testFactoryMethodNotFound() {
		try {
			assertBindingFailed(GWT.create(FactoryMethodNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testFactoryMethod() {
		final BeanFactory factory = (BeanFactory) GWT.create(FactoryMethodBeanFactory.class);
		final Bean bean = (Bean) factory.getBean(BEAN_ID);
		assertNotNull(bean);
	}

	public void testInitMethodNotFound() {
		try {
			assertBindingFailed(GWT.create(InitMethodNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testInitMethod() {
		final BeanFactory factory = (BeanFactory) GWT.create(InitMethodBeanFactory.class);
		final BeanWithCustomInit bean = (BeanWithCustomInit) factory.getBean(BEAN_ID);
		assertNotNull(bean);
		assertEquals(1, bean.initialized);
	}

	public void testDestroyMethodNotFound() {
		try {
			assertBindingFailed(GWT.create(DestroyMethodNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testPrototypeWithCustomDestroyMethod() {
		try {
			assertBindingFailed(GWT.create( PrototypeWithDestroyMethodBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals( BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testDisposableBean() {
		final BeanFactoryImpl factory = (BeanFactoryImpl) GWT.create(DisposableBeanBeanFactory.class);
		final DisposableBeanImpl bean = (DisposableBeanImpl) factory.getBean(BEAN_ID);
		assertNotNull(bean);
		
		factory.shutdown();
		
		assertEquals( "bean should have been destroyed", 1, bean.destroyed );
	}
	
	public void testDestroyMethod() {
		final BeanFactoryImpl factory = (BeanFactoryImpl) GWT.create(DestroyMethodBeanFactory.class);
		final BeanWithCustomDestroy bean = (BeanWithCustomDestroy) factory.getBean(BEAN_ID);
		assertNotNull(bean);
		
		factory.shutdown();
		
		assertEquals( "bean should have been destroyed", 1, bean.destroyed );
	}

	

	public void testConstructorNotFound() {
		try {
			assertBindingFailed(GWT.create(ConstructorNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testAmbiguousConstructors() {
		try {
			assertBindingFailed(GWT.create(AmbiguousConstructorsBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testNoArgumentsConstructor() {
		final BeanFactory factory = (BeanFactory) GWT.create(NoArgumentsConstructorBeanFactory.class);
		final ClassWithNoArgumentsConstructor bean = (ClassWithNoArgumentsConstructor) factory.getBean(BEAN_ID);
		assertNotNull(bean);
	}

	public void testConstructorWithMultipleParameters() {
		final BeanFactory factory = (BeanFactory) GWT.create(MultipleArgumentsConstructorBeanFactory.class);
		final ClassWithMultipleArgumentsConstructor bean = (ClassWithMultipleArgumentsConstructor) factory.getBean(BEAN_ID);
		assertNotNull(bean);

		assertEquals("foo", bean.getFirst());
		assertEquals("bar", bean.getSecond());
	}

	public void testAmbiguousPropertySetters() {
		try {
			assertBindingFailed(GWT.create(AmbiguousSettersBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testNoProperties() {
		final BeanFactory factory = (BeanFactory) GWT.create(NoPropertiesBeanFactory.class);
		final ClassWithNoProperties bean = (ClassWithNoProperties) factory.getBean(BEAN_ID);
		assertNotNull(bean);
	}

	public void testBooleanProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(BooleanPropertyBeanFactory.class);
		final ClassWithBooleanProperty bean = (ClassWithBooleanProperty) factory.getBean(BEAN_ID);
		assertTrue(bean.getBooleanProperty());
	}

	public void testByteProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(BytePropertyBeanFactory.class);
		final ClassWithByteProperty bean = (ClassWithByteProperty) factory.getBean(BEAN_ID);
		assertEquals(123, bean.getByteProperty());
	}

	public void testShortProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(ShortPropertyBeanFactory.class);
		final ClassWithShortProperty bean = (ClassWithShortProperty) factory.getBean(BEAN_ID);
		assertEquals(123, bean.getShortProperty());
	}

	public void testIntProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(IntPropertyBeanFactory.class);
		final ClassWithIntProperty bean = (ClassWithIntProperty) factory.getBean(BEAN_ID);
		assertEquals(123, bean.getIntProperty());
	}

	public void testLongProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(LongPropertyBeanFactory.class);
		final ClassWithLongProperty bean = (ClassWithLongProperty) factory.getBean(BEAN_ID);
		assertEquals(123, bean.getLongProperty());
	}

	public void testFloatProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(FloatPropertyBeanFactory.class);
		final ClassWithFloatProperty bean = (ClassWithFloatProperty) factory.getBean(BEAN_ID);
		assertEquals(123.45f, bean.getFloatProperty(), 0.01f);
	}

	public void testDoubleProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(DoublePropertyBeanFactory.class);
		final ClassWithDoubleProperty bean = (ClassWithDoubleProperty) factory.getBean(BEAN_ID);
		assertEquals(123.45, bean.getDoubleProperty(), 0.01);
	}

	public void testCharProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(CharPropertyBeanFactory.class);
		final ClassWithCharProperty bean = (ClassWithCharProperty) factory.getBean(BEAN_ID);
		assertEquals('a', bean.getCharProperty());
	}

	public void testStringProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(StringPropertyBeanFactory.class);
		final ClassWithStringProperty bean = (ClassWithStringProperty) factory.getBean(BEAN_ID);
		assertEquals("apple", bean.getStringProperty());
	}

	public void testListProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(ListPropertyBeanFactory.class);
		final ClassWithListProperty bean = (ClassWithListProperty) factory.getBean(BEAN_ID);
		final List list = bean.getListProperty();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("apple", list.get(0));
		assertEquals("banana", list.get(1));
		assertEquals("carrot", list.get(2));
	}

	public void testSetProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(SetPropertyBeanFactory.class);
		final ClassWithSetProperty bean = (ClassWithSetProperty) factory.getBean(BEAN_ID);
		final Set set = bean.getSetProperty();
		assertNotNull(set);
		assertEquals(3, set.size());
		assertTrue("apple", set.contains("apple"));
		assertTrue("banana", set.contains("banana"));
		assertTrue("carrot", set.contains("carrot"));
		assertFalse("dog", set.contains("dog"));
	}

	public void testMapProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(MapPropertyBeanFactory.class);
		final ClassWithMapProperty bean = (ClassWithMapProperty) factory.getBean(BEAN_ID);
		final Map map = bean.getMapProperty();
		assertNotNull(map);
		assertEquals(3, map.size());
		assertEquals("apple=green", "green", map.get("apple"));
		assertEquals("banana=yellow", "yellow", map.get("banana"));
		assertEquals("carrot=orange", "orange", map.get("carrot"));
		assertEquals("dog", null, map.get("dog"));
	}

	public void testManyValues() {
		final BeanFactory factory = (BeanFactory) GWT.create(ManyValuesBeanFactory.class);
		final ClassWithManyValues bean = (ClassWithManyValues) factory.getBean(BEAN_ID);
		assertEquals("green", bean.getApple());
		assertEquals("yellow", bean.getBanana());
		assertEquals("orange", bean.getCarrot());
	}

	public void testBeanReference() {
		final BeanFactory factory = (BeanFactory) GWT.create(BeanReferenceBeanFactory.class);
		final ClassWithReferences bean = (ClassWithReferences) factory.getBean(BEAN_ID);
		assertNotNull(bean);

		assertNotNull("bean reference (subclass) was not set", bean.getConcreteClass() );
		assertNotNull("bean reference (interface) was not set", bean.getInterface() );
	}

	public void testBeanReferenceWithFactoryBean() {
		final BeanFactory factory = (BeanFactory) GWT.create(FactoryBeanBeanReferenceBeanFactory.class);
		final FactoryBeanProducedBean bean = (FactoryBeanProducedBean) factory.getBean(BEAN_ID);
		assertNotNull(bean);
	}

	public void testAliasFromBeanIdIsNotUnique(){
		try {
			assertBindingFailed(GWT.create(AliasFromBeanIdIsNotUniqueBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}		
	}
	public void testAliasToBeanIdDoesntExist(){
		try {
			assertBindingFailed(GWT.create(AliasBeanDoesntExistBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}		
	}

	public void testAlias(){
		final BeanFactory factory = (BeanFactory) GWT.create(AliasBeanFactory.class);
		final Object aliased = factory.getBean( "alias");
		assertNotNull(aliased);
		
		final Object bean = factory.getBean( BEAN_ID );
		assertNotNull(bean);
		assertSame( bean, aliased );
	}
	
	public void testBeanWithAliasReference(){
		final BeanFactory factory = (BeanFactory) GWT.create(AliasedBeanReferenceBeanFactory.class);
		final ClassWithAliasedBeanReference bean = (ClassWithAliasedBeanReference)factory.getBean( BEAN_ID );
		assertNotNull(bean);
		
		final AliasedBean aliasedBean = bean.getAliasedBean();
		assertNotNull(aliasedBean);
	}
	
	public void testRemoteRpcService() {
		final BeanFactory factory = (BeanFactory) GWT.create(RemoteRpcServiceBeanFactory.class);
		final RemoteRpcServiceAsync bean = (RemoteRpcServiceAsync) factory.getBean(BEAN_ID);
		assertNotNull(bean);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) bean;
		assertTrue("/remoteRpcService", serviceDefTarget.getServiceEntryPoint().endsWith("/remoteRpcService"));
	}

	public void testRemoteJsonService() {
		final BeanFactory factory = (BeanFactory) GWT.create(RemoteJsonServiceBeanFactory.class);
		final RemoteJsonServiceAsync bean = (RemoteJsonServiceAsync) factory.getBean(BEAN_ID);
		assertNotNull(bean);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) bean;
		assertTrue("/remoteJsonService", serviceDefTarget.getServiceEntryPoint().endsWith("/remoteJsonService"));
	}

	public void testBeanWithRemoteRpcServiceProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(GwtRpcServicePropertyBeanFactory.class);
		final BeanWithGwtRpcService bean = (BeanWithGwtRpcService) factory.getBean(BEAN_ID);
		assertNotNull(bean);

		final GwtRpcServiceAsync service = bean.getService();
		assertNotNull("rpcService", service);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) service;
		assertTrue("/remoteRpcService", serviceDefTarget.getServiceEntryPoint().endsWith("/remoteRpcService"));
	}

	public void testBeanWithJsonRpcServiceProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(JsonServicePropertyBeanFactory.class);
		final BeanWithJsonService bean = (BeanWithJsonService) factory.getBean(BEAN_ID);
		assertNotNull(bean);

		final JsonServiceAsync service = bean.getService();

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) service;
		assertTrue("/remoteRpcService", serviceDefTarget.getServiceEntryPoint().endsWith("/remoteJsonService"));
	}

	public void testMoreThanOneBeanFactory() {
		final FirstBeanFactory firstBeanFactory = (FirstBeanFactory) GWT.create(FirstBeanFactory.class);
		assertNotNull("firstBeanFactory", firstBeanFactory);
		assertNotNull("firstBeanFactory", firstBeanFactory.getBean( BEAN_ID));

		final SecondBeanFactory secondBeanFactory = (SecondBeanFactory) GWT.create(SecondBeanFactory.class);
		assertNotNull("secondBeanFactory", secondBeanFactory);
		assertNotNull("secondBeanFactory", secondBeanFactory.getBean(BEAN_ID));
	}

	public void testPlaceHolders() {
		final BeanFactory factory = (BeanFactory) GWT.create(PlaceHolderBeanFactory.class);
		final PlaceHolderBean bean = (PlaceHolderBean) factory.getBean(BEAN_ID);
		assertEquals("orange yellow green green", bean.getStringProperty());
	}

	public void testMultipleIncludedFiles() {
		final BeanFactory factory = (BeanFactory) GWT.create(MultipleIncludedFilesBeanFactory.class);
		final Object bean = factory.getBean(BEAN_ID);
		assertNotNull(bean);

		final Object beanFromSecondFile = factory.getBean("beanFromSecondFile");
		assertNotNull(beanFromSecondFile);

		final Object beanFromThirdFile = factory.getBean("beanFromThirdFile");
		assertNotNull(beanFromThirdFile);
	}

	public void testMultipleIncludedFilesWithCycle() {
		try {
			assertBindingFailed(GWT.create(MultipleIncludedFilesCycleBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
			
			final String message = failed.getMessage();
			assertTrue( "" + message, message.indexOf("ActualBeanFactoryWithCycle.xml") != -1 );
		} 
	}
	
	public void testAdvisorIsNotAnAdvice() {
		try {
			assertBindingFailed(GWT.create(NotAnAdviceBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testProxyFinalClass() throws Exception {
		try {
			assertBindingFailed(GWT.create(ProxyFinalClassBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testProxyClassWithFinalPublicMethod() throws Exception {
		try {
			assertBindingFailed(GWT.create(ProxyFinalPublicMethodBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testProxyBeanWithMethodWithBooleanParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyBooleanBeanFactory.class);
		final ClassWithMethodWithBooleanParameterAndReturnType proxy = (ClassWithMethodWithBooleanParameterAndReturnType) factory.getBean(BEAN_ID);
		assertEquals("proxy true ^ false", true ^ false, proxy.xor(true, false));
		assertEquals("proxy true ^ true", true ^ true, proxy.xor(true, true));
		assertEquals("proxy false ^ false", false ^ false, proxy.xor(false, false));

		final ProxyBooleanMethodInterceptor advisor = (ProxyBooleanMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyBeanWithMethodWithByteParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyByteBeanFactory.class);
		final ClassWithMethodWithByteParameterAndReturnType proxy = (ClassWithMethodWithByteParameterAndReturnType) factory
				.getBean(BEAN_ID);
		assertEquals("proxy sum of " + BYTE + "+" + BYTE, BYTE + BYTE, proxy.add(BYTE, BYTE));

		final ProxyByteMethodInterceptor advisor = (ProxyByteMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyBeanWithMethodWithShortParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyShortBeanFactory.class);
		final ClassWithMethodWithShortParameterAndReturnType proxy = (ClassWithMethodWithShortParameterAndReturnType) factory
				.getBean(BEAN_ID);
		assertEquals("proxy sum of " + SHORT + "+" + SHORT, SHORT + SHORT, proxy.add(SHORT, SHORT));

		final ProxyShortMethodInterceptor advisor = (ProxyShortMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyBeanWithMethodWithIntParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyIntBeanFactory.class);
		final ClassWithMethodWithIntParameterAndReturnType proxy = (ClassWithMethodWithIntParameterAndReturnType) factory.getBean(BEAN_ID);
		assertEquals("proxy sum of " + INT + "+" + INT, INT + INT, proxy.add(INT, INT));

		final ProxyIntMethodInterceptor advisor = (ProxyIntMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyBeanWithMethodWithLongParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyLongBeanFactory.class);
		final ClassWithMethodWithLongParameterAndReturnType proxy = (ClassWithMethodWithLongParameterAndReturnType) factory
				.getBean(BEAN_ID);
		assertEquals("proxy sum of " + LONG + "+" + LONG, LONG + LONG, proxy.add(LONG, LONG));

		final ProxyLongMethodInterceptor advisor = (ProxyLongMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyBeanWithMethodWithFloatParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyFloatBeanFactory.class);
		final ClassWithMethodWithFloatParameterAndReturnType proxy = (ClassWithMethodWithFloatParameterAndReturnType) factory
				.getBean(BEAN_ID);
		assertEquals("proxy sum of " + FLOAT + "+" + FLOAT, FLOAT + FLOAT, proxy.add(FLOAT, FLOAT), 0.1f);

		final ProxyFloatMethodInterceptor advisor = (ProxyFloatMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyBeanWithMethodWithDoubleParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyDoubleBeanFactory.class);
		final ClassWithMethodWithDoubleParameterAndReturnType proxy = (ClassWithMethodWithDoubleParameterAndReturnType) factory
				.getBean(BEAN_ID);
		assertEquals("proxy sum of " + DOUBLE + "+" + DOUBLE, DOUBLE + DOUBLE, proxy.add(DOUBLE, DOUBLE), 0.1f);

		final ProxyDoubleMethodInterceptor advisor = (ProxyDoubleMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyBeanWithMethodWithCharParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyCharBeanFactory.class);
		final ClassWithMethodWithCharParameterAndReturnType proxy = (ClassWithMethodWithCharParameterAndReturnType) factory
				.getBean(BEAN_ID);
		assertEquals("proxy.toUpperCase()", Character.toUpperCase('a'), proxy.toUpperCase('a'));

		final ProxyCharMethodInterceptor advisor = (ProxyCharMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyBeanWithMethodWithObjectParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyObjectBeanFactory.class);
		final ClassWithMethodWithObjectParameterAndReturnType proxy = (ClassWithMethodWithObjectParameterAndReturnType) factory
				.getBean(BEAN_ID);

		final String object = "apple";
		assertSame("proxy passes object to target and back...", object, proxy.returnParameter(object));

		final ProxyObjectMethodInterceptor advisor = (ProxyObjectMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyBeanWithMethodThatReturnsVoid() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyVoidBeanFactory.class);
		final ClassWithMethodThatReturnsVoid proxy = (ClassWithMethodThatReturnsVoid) factory.getBean(BEAN_ID);

		proxy.returnsVoid();

		final ProxyVoidMethodInterceptor advisor = (ProxyVoidMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyTargetThrowsCheckedException() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyTargetThrowsCheckedExceptionBeanFactory.class);
		final ClassWithMethodThatThrowsCheckedException proxy = (ClassWithMethodThatThrowsCheckedException) factory.getBean(BEAN_ID);

		try {
			proxy.throwCheckedException();
			fail("Was expecting a CheckedException to be thrown");
		} catch (final CheckedException expected) {
		}

		final ProxyTargetThrowsCheckedExceptionMethodInterceptor advisor = (ProxyTargetThrowsCheckedExceptionMethodInterceptor) factory
				.getBean(ADVISOR_BEAN_ID);
		assertTrue(advisor.executed);
	}

	public void testProxyTargetThrowsCheckedExceptionOnUnadvicedMethod() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyTargetThrowsCheckedExceptionBeanFactory.class);
		final ClassWithMethodThatThrowsCheckedException proxy = (ClassWithMethodThatThrowsCheckedException) factory.getBean(BEAN_ID);

		try {
			proxy.unadvicedThrowCheckedException();
			fail("Was expecting a CheckedException to be thrown");
		} catch (final CheckedException expected) {
		}

		final ProxyTargetThrowsCheckedExceptionMethodInterceptor advisor = (ProxyTargetThrowsCheckedExceptionMethodInterceptor) factory
				.getBean(ADVISOR_BEAN_ID);
		assertFalse(advisor.executed);
	}

	public void testProxyTargetThrowsCheckedExceptionLetsRuntimeExceptionsThrough() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyTargetThrowsCheckedExceptionBeanFactory.class);
		final ClassWithMethodThatThrowsCheckedException proxy = (ClassWithMethodThatThrowsCheckedException) factory.getBean(BEAN_ID);

		try {
			proxy.throwRuntimeException();
			fail("Was expecting a RuntimeException to be thrown");
		} catch (final RuntimeException expected) {
		}
	}

	public void testProxyTargetThrowsUncheckedException() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyTargetThrowsUncheckedExceptionBeanFactory.class);
		final ClassWithMethodThatThrowsUncheckedException proxy = (ClassWithMethodThatThrowsUncheckedException) factory.getBean(BEAN_ID);

		try {
			proxy.throwUncheckedException();
			fail("Was expecting a UncheckedException to be thrown");
		} catch (final UncheckedException expected) {
		}
	}

	public void testProxyWithAdvisedAndUnadvisedMethods() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyComplexBeanFactory.class);
		final Complex proxy = (Complex) factory.getBean(BEAN_ID);
		final ComplexMethodInterceptor interceptor = (ComplexMethodInterceptor) factory.getBean(ADVISOR_BEAN_ID);

		interceptor.executed = false;
		assertEquals("advised", true ^ false, proxy.advisedXor(true, false));
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		assertEquals("advised", BYTE + BYTE, proxy.advisedAdd(BYTE, BYTE));
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		assertEquals("advised", SHORT + SHORT, proxy.advisedAdd(SHORT, SHORT));
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		assertEquals("advised", INT + INT, proxy.advisedAdd(INT, INT));
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		assertEquals("advised", LONG + LONG, proxy.advisedAdd(LONG, LONG));
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		assertEquals("advised", FLOAT + FLOAT, proxy.advisedAdd(FLOAT, FLOAT), 0.1f);
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		assertEquals("advised", DOUBLE + DOUBLE, proxy.advisedAdd(DOUBLE, DOUBLE), 0.1f);
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		assertEquals("advised", Character.toUpperCase('a'), proxy.advisedToUpperCase('a'));
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		assertEquals("advised", "APPLE".toLowerCase(), proxy.advisedToLowerCase("APPLE"));
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		assertEquals("advised", null, proxy.advisedReturnsNull(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, new Object()));
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		final Object object = new Object();
		assertSame("advised", object, proxy.advisedReturnsObject(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, object));
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		try {
			proxy.advisedThrowCheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final Exception expected) {
		}
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		try {
			proxy.advisedThrowUncheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final RuntimeException expected) {
		}
		assertTrue(interceptor.executed);

		interceptor.executed = false;
		assertEquals("unadvised", true ^ false, proxy.unadvisedXor(true, false));
		assertFalse(interceptor.executed);

		assertEquals("unadvised", BYTE + BYTE, proxy.unadvisedAdd(BYTE, BYTE));
		assertFalse(interceptor.executed);

		assertEquals("unadvised", SHORT + SHORT, proxy.unadvisedAdd(SHORT, SHORT));
		assertFalse(interceptor.executed);

		assertEquals("unadvised", INT + INT, proxy.unadvisedAdd(INT, INT));
		assertFalse(interceptor.executed);

		assertEquals("unadvised", LONG + LONG, proxy.unadvisedAdd(LONG, LONG));
		assertFalse(interceptor.executed);

		assertEquals("unadvised", FLOAT + FLOAT, proxy.unadvisedAdd(FLOAT, FLOAT), 0.1f);
		assertFalse(interceptor.executed);

		assertEquals("unadvised", DOUBLE + DOUBLE, proxy.unadvisedAdd(DOUBLE, DOUBLE), 0.1f);
		assertFalse(interceptor.executed);

		assertEquals("unadvised", Character.toUpperCase('a'), proxy.unadvisedToUpperCase('a'));
		assertFalse(interceptor.executed);

		assertEquals("unadvised", "APPLE".toLowerCase(), proxy.unadvisedToLowerCase("APPLE"));
		assertFalse(interceptor.executed);

		assertEquals("unadvised", null, proxy.unadvisedReturnsNull(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, new Object()));
		assertFalse(interceptor.executed);

		assertSame("unadvised", object, proxy.unadvisedReturnsObject(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, object));
		assertFalse(interceptor.executed);

		try {
			proxy.unadvisedThrowCheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final Exception expected) {
		}
		assertFalse(interceptor.executed);

		try {
			proxy.unadvisedThrowUncheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final RuntimeException expected) {
		}
		assertFalse(interceptor.executed);
	}
}
