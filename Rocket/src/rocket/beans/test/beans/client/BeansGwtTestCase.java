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
import rocket.beans.client.aop.MethodInvocation;
import rocket.beans.test.beans.client.advisedrpc.AdvisedGwtRpcAsync;
import rocket.beans.test.beans.client.advisedrpc.AdvisedRpcBeanFactory;
import rocket.beans.test.beans.client.advisedrpc.AdvisedRpcMethodInterceptor;
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
import rocket.beans.test.beans.client.doublenestedbean.BeanWithDoubleNestedBean;
import rocket.beans.test.beans.client.doublenestedbean.DoubleNestedBeanBeanFactory;
import rocket.beans.test.beans.client.doublenestedbean.NestedBeanWithNestedBean;
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
import rocket.beans.test.beans.client.gwtrpc.GwtRpcServiceAsync;
import rocket.beans.test.beans.client.gwtrpc.GwtRpcServiceBeanFactory;
import rocket.beans.test.beans.client.initmethod.BeanWithCustomInit;
import rocket.beans.test.beans.client.initmethod.InitMethodBeanFactory;
import rocket.beans.test.beans.client.initmethodnotfound.InitMethodNotFoundBeanFactory;
import rocket.beans.test.beans.client.interceptedbooleanmethod.BooleanMethodInterceptor;
import rocket.beans.test.beans.client.interceptedbooleanmethod.ClassWithBooleanMethod;
import rocket.beans.test.beans.client.interceptedbooleanmethod.InterceptedBooleanMethodBeanFactory;
import rocket.beans.test.beans.client.interceptedbytemethod.ByteMethodInterceptor;
import rocket.beans.test.beans.client.interceptedbytemethod.ClassWithByteMethod;
import rocket.beans.test.beans.client.interceptedbytemethod.InterceptedByteMethodBeanFactory;
import rocket.beans.test.beans.client.interceptedcharmethod.CharMethodInterceptor;
import rocket.beans.test.beans.client.interceptedcharmethod.ClassWithCharMethod;
import rocket.beans.test.beans.client.interceptedcharmethod.InterceptedCharMethodBeanFactory;
import rocket.beans.test.beans.client.interceptedcomplex.Complex;
import rocket.beans.test.beans.client.interceptedcomplex.ComplexMethodInterceptor;
import rocket.beans.test.beans.client.interceptedcomplex.InterceptedComplexBeanFactory;
import rocket.beans.test.beans.client.intercepteddoublemethod.ClassWithDoubleMethod;
import rocket.beans.test.beans.client.intercepteddoublemethod.DoubleMethodInterceptor;
import rocket.beans.test.beans.client.intercepteddoublemethod.InterceptedDoubleMethodBeanFactory;
import rocket.beans.test.beans.client.interceptedfloatmethod.ClassWithFloatMethod;
import rocket.beans.test.beans.client.interceptedfloatmethod.FloatMethodInterceptor;
import rocket.beans.test.beans.client.interceptedfloatmethod.InterceptedFloatMethodBeanFactory;
import rocket.beans.test.beans.client.interceptedintmethod.ClassWithIntMethod;
import rocket.beans.test.beans.client.interceptedintmethod.IntMethodInterceptor;
import rocket.beans.test.beans.client.interceptedintmethod.InterceptedIntMethodFactory;
import rocket.beans.test.beans.client.interceptedlongmethod.ClassWithLongMethod;
import rocket.beans.test.beans.client.interceptedlongmethod.InterceptedLongMethodBeanFactory;
import rocket.beans.test.beans.client.interceptedlongmethod.LongMethodInterceptor;
import rocket.beans.test.beans.client.interceptedobjectmethod.ClassWithObjectMethod;
import rocket.beans.test.beans.client.interceptedobjectmethod.InterceptedObjectMethodBeanFactory;
import rocket.beans.test.beans.client.interceptedobjectmethod.ObjectMethodInterceptor;
import rocket.beans.test.beans.client.interceptedshortmethod.ClassWithShortMethod;
import rocket.beans.test.beans.client.interceptedshortmethod.InterceptedShortMethodBeanFactory;
import rocket.beans.test.beans.client.interceptedshortmethod.ShortMethodInterceptor;
import rocket.beans.test.beans.client.interceptedvoidmethod.ClassWithVoidMethod;
import rocket.beans.test.beans.client.interceptedvoidmethod.InterceptedVoidMethodBeanFactory;
import rocket.beans.test.beans.client.interceptedvoidmethod.VoidMethodInterceptor;
import rocket.beans.test.beans.client.intproperty.ClassWithIntProperty;
import rocket.beans.test.beans.client.intproperty.IntPropertyBeanFactory;
import rocket.beans.test.beans.client.invalidbeanscope.InvalidScopeBeanFactory;
import rocket.beans.test.beans.client.jsonrpcproperty.BeanWithJsonService;
import rocket.beans.test.beans.client.jsonrpcproperty.JsonServiceAsync;
import rocket.beans.test.beans.client.jsonrpcproperty.JsonServicePropertyBeanFactory;
import rocket.beans.test.beans.client.jsonrpcservice.JsonRpcServiceAsync;
import rocket.beans.test.beans.client.jsonrpcservice.JsonRpcServiceBeanFactory;
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
import rocket.beans.test.beans.client.methodinvocation.MethodInterceptorImpl;
import rocket.beans.test.beans.client.methodinvocation.MethodInvocationBeanFactory;
import rocket.beans.test.beans.client.methodinvocation.MethodInvocationTestTarget;
import rocket.beans.test.beans.client.missingbeanid.MissingBeanIdBeanFactory;
import rocket.beans.test.beans.client.morethanonebeanfactory.FirstBeanFactory;
import rocket.beans.test.beans.client.morethanonebeanfactory.SecondBeanFactory;
import rocket.beans.test.beans.client.multipleargumentsconstructor.ClassWithMultipleArgumentsConstructor;
import rocket.beans.test.beans.client.multipleargumentsconstructor.MultipleArgumentsConstructorBeanFactory;
import rocket.beans.test.beans.client.multipleincludedfiles.MultipleIncludedFilesBeanFactory;
import rocket.beans.test.beans.client.multipleincludedfilescycle.MultipleIncludedFilesCycleBeanFactory;
import rocket.beans.test.beans.client.nestedbean.BeanWithNestedBean;
import rocket.beans.test.beans.client.nestedbean.NestedBeanBeanFactory;
import rocket.beans.test.beans.client.nestedbeanhasid.NestedBeanHasIdBeanFactory;
import rocket.beans.test.beans.client.noargumentsconstructor.ClassWithNoArgumentsConstructor;
import rocket.beans.test.beans.client.noargumentsconstructor.NoArgumentsConstructorBeanFactory;
import rocket.beans.test.beans.client.noproperties.ClassWithNoProperties;
import rocket.beans.test.beans.client.noproperties.NoPropertiesBeanFactory;
import rocket.beans.test.beans.client.notabeanfactory.ClassIsNotABeanFactory;
import rocket.beans.test.beans.client.notanadvice.NotAnAdviceBeanFactory;
import rocket.beans.test.beans.client.nullliteral.NullLiteralBeanFactory;
import rocket.beans.test.beans.client.nullliteral.NullTestBean;
import rocket.beans.test.beans.client.placeholders.PlaceHolderBean;
import rocket.beans.test.beans.client.placeholders.PlaceHolderBeanFactory;
import rocket.beans.test.beans.client.prototypewithdestroymethod.PrototypeWithDestroyMethodBeanFactory;
import rocket.beans.test.beans.client.proxyclasswithfinalpublicmethod.ProxyFinalPublicMethodBeanFactory;
import rocket.beans.test.beans.client.proxyfinalclass.ProxyFinalClassBeanFactory;
import rocket.beans.test.beans.client.proxytargetthrowscheckedexception.CheckedException;
import rocket.beans.test.beans.client.proxytargetthrowscheckedexception.ClassWithMethodThatThrowsCheckedException;
import rocket.beans.test.beans.client.proxytargetthrowscheckedexception.ProxyTargetThrowsCheckedExceptionBeanFactory;
import rocket.beans.test.beans.client.proxytargetthrowscheckedexception.ProxyTargetThrowsCheckedExceptionMethodInterceptor;
import rocket.beans.test.beans.client.proxytargetthrowsuncheckedexception.ClassWithMethodThatThrowsUncheckedException;
import rocket.beans.test.beans.client.proxytargetthrowsuncheckedexception.ProxyTargetThrowsUncheckedExceptionBeanFactory;
import rocket.beans.test.beans.client.proxytargetthrowsuncheckedexception.UncheckedException;
import rocket.beans.test.beans.client.rpcproperty.BeanWithGwtRpcService;
import rocket.beans.test.beans.client.rpcproperty.BeanWithRpcPropertyBeanFactory;
import rocket.beans.test.beans.client.setproperty.ClassWithSetProperty;
import rocket.beans.test.beans.client.setproperty.SetPropertyBeanFactory;
import rocket.beans.test.beans.client.shortproperty.ClassWithShortProperty;
import rocket.beans.test.beans.client.shortproperty.ShortPropertyBeanFactory;
import rocket.beans.test.beans.client.stringproperty.ClassWithStringProperty;
import rocket.beans.test.beans.client.stringproperty.StringPropertyBeanFactory;
import rocket.beans.test.beans.client.toplevelbeanmissingid.TopLevelBeanMissingIdBeanFactory;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeansGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN = "bean";

	final static String ADVISOR = "advisor";

	final static byte BYTE = 1;

	final static short SHORT = 23;

	final static int INT = 345;

	final static long LONG = 6789;

	final static float FLOAT = 12.34f;

	final static double DOUBLE = 56.789;

	final static int DELAY_TIMEOUT = 10 * 1000;
	
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

	public void testLazilyLoadedSingletonBean() {
		final LazyLoadedBeanFactory beanFactory = (LazyLoadedBeanFactory)GWT.create(LazyLoadedBeanFactory.class);
		assertFalse("singleton is not yet loaded", LazySingletonBean.loaded );
		beanFactory.getBean( BEAN );
		assertTrue("singleton is now loaded", LazySingletonBean.loaded );		
	}

	public void testEagerlyLoadedSingletonBean() {
		assertFalse("singleton should have not been have been loaded until factory is created.", EagerlyLoadedSingletonBean.loaded );
		final EagerlyLoadedSingletonBeanFactory beanFactory = (EagerlyLoadedSingletonBeanFactory)GWT.create(EagerlyLoadedSingletonBeanFactory.class);
		assertNotNull( beanFactory );
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
		final Bean bean = (Bean) factory.getBean(BEAN);
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
		final BeanWithCustomInit bean = (BeanWithCustomInit) factory.getBean(BEAN);
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
		final DisposableBeanImpl bean = (DisposableBeanImpl) factory.getBean(BEAN);
		assertNotNull(bean);
		
		factory.shutdown();
		
		assertEquals( "bean should have been destroyed", 1, bean.destroyed );
	}
	
	public void testDestroyMethod() {
		final BeanFactoryImpl factory = (BeanFactoryImpl) GWT.create(DestroyMethodBeanFactory.class);
		final BeanWithCustomDestroy bean = (BeanWithCustomDestroy) factory.getBean(BEAN);
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
		final ClassWithNoArgumentsConstructor bean = (ClassWithNoArgumentsConstructor) factory.getBean(BEAN);
		assertNotNull(bean);
	}

	public void testConstructorWithMultipleParameters() {
		final BeanFactory factory = (BeanFactory) GWT.create(MultipleArgumentsConstructorBeanFactory.class);
		final ClassWithMultipleArgumentsConstructor bean = (ClassWithMultipleArgumentsConstructor) factory.getBean(BEAN);
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
		final ClassWithNoProperties bean = (ClassWithNoProperties) factory.getBean(BEAN);
		assertNotNull(bean);
	}

	public void testNullLiteral() {
		final BeanFactory factory = (BeanFactory) GWT.create(NullLiteralBeanFactory.class);
		final NullTestBean bean = (NullTestBean) factory.getBean(BEAN);
		assertNull(bean.getProperty());
	}
	
	public void testBooleanProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(BooleanPropertyBeanFactory.class);
		final ClassWithBooleanProperty bean = (ClassWithBooleanProperty) factory.getBean(BEAN);
		assertTrue(bean.getBooleanProperty());
	}

	public void testByteProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(BytePropertyBeanFactory.class);
		final ClassWithByteProperty bean = (ClassWithByteProperty) factory.getBean(BEAN);
		assertEquals(123, bean.getByteProperty());
	}

	public void testShortProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(ShortPropertyBeanFactory.class);
		final ClassWithShortProperty bean = (ClassWithShortProperty) factory.getBean(BEAN);
		assertEquals(123, bean.getShortProperty());
	}

	public void testIntProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(IntPropertyBeanFactory.class);
		final ClassWithIntProperty bean = (ClassWithIntProperty) factory.getBean(BEAN);
		assertEquals(123, bean.getIntProperty());
	}

	public void testLongProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(LongPropertyBeanFactory.class);
		final ClassWithLongProperty bean = (ClassWithLongProperty) factory.getBean(BEAN);
		assertEquals(123, bean.getLongProperty());
	}

	public void testFloatProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(FloatPropertyBeanFactory.class);
		final ClassWithFloatProperty bean = (ClassWithFloatProperty) factory.getBean(BEAN);
		assertEquals(123.45f, bean.getFloatProperty(), 0.01f);
	}

	public void testDoubleProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(DoublePropertyBeanFactory.class);
		final ClassWithDoubleProperty bean = (ClassWithDoubleProperty) factory.getBean(BEAN);
		assertEquals(123.45, bean.getDoubleProperty(), 0.01);
	}

	public void testCharProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(CharPropertyBeanFactory.class);
		final ClassWithCharProperty bean = (ClassWithCharProperty) factory.getBean(BEAN);
		assertEquals('a', bean.getCharProperty());
	}

	public void testStringProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(StringPropertyBeanFactory.class);
		final ClassWithStringProperty bean = (ClassWithStringProperty) factory.getBean(BEAN);
		assertEquals("apple", bean.getStringProperty());
	}

	public void testListProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(ListPropertyBeanFactory.class);
		final ClassWithListProperty bean = (ClassWithListProperty) factory.getBean(BEAN);
		final List list = bean.getListProperty();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("apple", list.get(0));
		assertEquals("banana", list.get(1));
		assertEquals("carrot", list.get(2));
	}

	public void testSetProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(SetPropertyBeanFactory.class);
		final ClassWithSetProperty bean = (ClassWithSetProperty) factory.getBean(BEAN);
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
		final ClassWithMapProperty bean = (ClassWithMapProperty) factory.getBean(BEAN);
		final Map map = bean.getMapProperty();
		assertNotNull(map);
		assertEquals(3, map.size());
		assertEquals("apple=green", "green", map.get("apple"));
		assertEquals("banana=yellow", "yellow", map.get("banana"));
		assertEquals("carrot=orange", "orange", map.get("carrot"));
		assertEquals("dog", null, map.get("dog"));
	}

	public void testManyProperties() {
		final BeanFactory factory = (BeanFactory) GWT.create(ManyValuesBeanFactory.class);
		final ClassWithManyValues bean = (ClassWithManyValues) factory.getBean(BEAN);
		assertEquals("green", bean.getApple());
		assertEquals("yellow", bean.getBanana());
		assertEquals("orange", bean.getCarrot());
	}

	public void testBeanReference() {
		final BeanFactory factory = (BeanFactory) GWT.create(BeanReferenceBeanFactory.class);
		final ClassWithReferences bean = (ClassWithReferences) factory.getBean(BEAN);
		assertNotNull(bean);

		assertNotNull("bean reference (subclass) was not set", bean.getConcreteClass() );
		assertNotNull("bean reference (interface) was not set", bean.getInterface() );
	}

	public void testBeanReferenceWithFactoryBean() {
		final BeanFactory factory = (BeanFactory) GWT.create(FactoryBeanBeanReferenceBeanFactory.class);
		final FactoryBeanProducedBean bean = (FactoryBeanProducedBean) factory.getBean(BEAN);
		assertNotNull(bean);
	}

	public void testTopLevelBeanMissingId(){
		try {
			assertBindingFailed(GWT.create(TopLevelBeanMissingIdBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testNestedBeanHasId(){
		try {
			assertBindingFailed(GWT.create(NestedBeanHasIdBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void testNestedBean(){
		final BeanFactory beanFactory = (BeanFactory)GWT.create( NestedBeanBeanFactory.class );
		final BeanWithNestedBean bean = (BeanWithNestedBean) beanFactory.getBean( BEAN );
		assertNotNull( "BeanWithNestedBean", bean );	
		assertNotNull( "BeanWithNestedBean.nestedBean", bean.getNestedBean() );
	}

	public void testDoubleNestedBean(){
		final BeanFactory beanFactory = (BeanFactory)GWT.create( DoubleNestedBeanBeanFactory.class );
		final BeanWithDoubleNestedBean bean = (BeanWithDoubleNestedBean) beanFactory.getBean( BEAN );
		assertNotNull( "BeanWithDoubleNestedBean", bean );	
		
		final NestedBeanWithNestedBean nestedBeanWithNestedBean = bean.getNestedBeanWithNestedBean();
		assertNotNull( "BeanWithDoubleNestedBean.nestedBeanWithNestedBean", nestedBeanWithNestedBean );
		
		assertNotNull( "BeanWithDoubleNestedBean.nestedBeanWithNestedBean.nestedBean", nestedBeanWithNestedBean.getNestedBean() );
	}
	
	public void testAliasNameIsNotUnique(){
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
		
		final Object bean = factory.getBean( BEAN );
		assertNotNull(bean);
		assertSame( bean, aliased );
	}
	
	public void testBeanWithAliasReference(){
		final BeanFactory factory = (BeanFactory) GWT.create(AliasedBeanReferenceBeanFactory.class);
		final ClassWithAliasedBeanReference bean = (ClassWithAliasedBeanReference)factory.getBean( BEAN );
		assertNotNull(bean);
		
		final AliasedBean aliasedBean = bean.getAliasedBean();
		assertNotNull(aliasedBean);
	}
	
	public void testGwtRpcService() {
		final BeanFactory factory = (BeanFactory) GWT.create(GwtRpcServiceBeanFactory.class);
		final GwtRpcServiceAsync bean = (GwtRpcServiceAsync) factory.getBean(BEAN);
		assertNotNull(bean);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) bean;
		assertTrue("/rpc", serviceDefTarget.getServiceEntryPoint().endsWith("/rpc"));
	}

	public void testJsonRpcService() {
		final BeanFactory factory = (BeanFactory) GWT.create(JsonRpcServiceBeanFactory.class);
		final JsonRpcServiceAsync bean = (JsonRpcServiceAsync) factory.getBean(BEAN);
		assertNotNull(bean);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) bean;
		assertTrue("/json", serviceDefTarget.getServiceEntryPoint().endsWith("/json"));
	}

	public void testBeanWithGwtRpcProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(BeanWithRpcPropertyBeanFactory.class);
		final BeanWithGwtRpcService bean = (BeanWithGwtRpcService) factory.getBean(BEAN);
		assertNotNull(bean);

		final Object rpc = bean.getRpc();
		assertNotNull("rpc", rpc);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) rpc;
		assertTrue("/rpc", serviceDefTarget.getServiceEntryPoint().endsWith("/rpc"));
	}

	public void testBeanWithJsonRpcServiceProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(JsonServicePropertyBeanFactory.class);
		final BeanWithJsonService bean = (BeanWithJsonService) factory.getBean(BEAN);
		assertNotNull(bean);

		final JsonServiceAsync service = bean.getService();

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) service;
		assertTrue("/json", serviceDefTarget.getServiceEntryPoint().endsWith("/json"));
	}

	public void testMoreThanOneBeanFactory() {
		final FirstBeanFactory firstBeanFactory = (FirstBeanFactory) GWT.create(FirstBeanFactory.class);
		assertNotNull("firstBeanFactory", firstBeanFactory);
		assertNotNull("firstBeanFactory", firstBeanFactory.getBean( BEAN));

		final SecondBeanFactory secondBeanFactory = (SecondBeanFactory) GWT.create(SecondBeanFactory.class);
		assertNotNull("secondBeanFactory", secondBeanFactory);
		assertNotNull("secondBeanFactory", secondBeanFactory.getBean(BEAN));
	}

	public void testPlaceHolders() {
		final BeanFactory factory = (BeanFactory) GWT.create(PlaceHolderBeanFactory.class);
		final PlaceHolderBean bean = (PlaceHolderBean) factory.getBean(BEAN);
		assertEquals("orange yellow green green", bean.getStringProperty());
	}

	public void testMultipleIncludedFiles() {
		final BeanFactory factory = (BeanFactory) GWT.create(MultipleIncludedFilesBeanFactory.class);
		final Object bean = factory.getBean(BEAN);
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

	public void testAttemptToAdviseFinalClass() throws Exception {
		try {
			assertBindingFailed(GWT.create(ProxyFinalClassBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testAttemptToAdviseClassWithFinalPublicMethod() throws Exception {
		try {
			assertBindingFailed(GWT.create(ProxyFinalPublicMethodBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testInterceptedMethodWithBooleanParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedBooleanMethodBeanFactory.class);
		final ClassWithBooleanMethod proxy = (ClassWithBooleanMethod) factory.getBean(BEAN);
		assertEquals("proxy true ^ false", true ^ false, proxy.xor(true, false));
		assertEquals("proxy true ^ true", true ^ true, proxy.xor(true, true));
		assertEquals("proxy false ^ false", false ^ false, proxy.xor(false, false));

		final BooleanMethodInterceptor advisor = (BooleanMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(3, advisor.executedCount);
	}

	public void testInterceptedMethodWithByteParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedByteMethodBeanFactory.class);
		final ClassWithByteMethod proxy = (ClassWithByteMethod) factory.getBean(BEAN);
		assertEquals("proxy sum of " + BYTE + "+" + BYTE, BYTE + BYTE, proxy.add(BYTE, BYTE));

		final ByteMethodInterceptor advisor = (ByteMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithShortParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedShortMethodBeanFactory.class);
		final ClassWithShortMethod proxy = (ClassWithShortMethod) factory
				.getBean(BEAN);
		assertEquals("proxy sum of " + SHORT + "+" + SHORT, SHORT + SHORT, proxy.add(SHORT, SHORT));

		final ShortMethodInterceptor advisor = (ShortMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithIntParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedIntMethodFactory.class);
		final ClassWithIntMethod proxy = (ClassWithIntMethod) factory.getBean(BEAN);
		assertEquals("proxy sum of " + INT + "+" + INT, INT + INT, proxy.add(INT, INT));

		final IntMethodInterceptor advisor = (IntMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithLongParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedLongMethodBeanFactory.class);
		final ClassWithLongMethod proxy = (ClassWithLongMethod) factory
				.getBean(BEAN);
		assertEquals("proxy sum of " + LONG + "+" + LONG, LONG + LONG, proxy.add(LONG, LONG));

		final LongMethodInterceptor advisor = (LongMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithFloatParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedFloatMethodBeanFactory.class);
		final ClassWithFloatMethod proxy = (ClassWithFloatMethod) factory
				.getBean(BEAN);
		assertEquals("proxy sum of " + FLOAT + "+" + FLOAT, FLOAT + FLOAT, proxy.add(FLOAT, FLOAT), 0.1f);

		final FloatMethodInterceptor advisor = (FloatMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithDoubleParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedDoubleMethodBeanFactory.class);
		final ClassWithDoubleMethod proxy = (ClassWithDoubleMethod) factory
				.getBean(BEAN);
		assertEquals("proxy sum of " + DOUBLE + "+" + DOUBLE, DOUBLE + DOUBLE, proxy.add(DOUBLE, DOUBLE), 0.1f);

		final DoubleMethodInterceptor advisor = (DoubleMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithCharParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedCharMethodBeanFactory.class);
		final ClassWithCharMethod proxy = (ClassWithCharMethod) factory.getBean(BEAN);
		assertEquals("proxy.toUpperCase()", Character.toUpperCase('a'), proxy.toUpperCase('a'));

		final CharMethodInterceptor advisor = (CharMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithObjectParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedObjectMethodBeanFactory.class);
		final ClassWithObjectMethod proxy = (ClassWithObjectMethod) factory.getBean(BEAN);

		final String object = "apple";
		assertSame("proxy passes object to target and back...", object, proxy.returnParameter(object));

		final ObjectMethodInterceptor advisor = (ObjectMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedVoidMethod() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedVoidMethodBeanFactory.class);
		final ClassWithVoidMethod proxy = (ClassWithVoidMethod) factory.getBean(BEAN);

		proxy.returnsVoid();

		final VoidMethodInterceptor advisor = (VoidMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testAdviseRpc() {
		final String string = "apple";
		
		final BeanFactory factory = (BeanFactory) GWT.create(AdvisedRpcBeanFactory.class);
		final AdvisedGwtRpcAsync proxy = (AdvisedGwtRpcAsync) factory.getBean(BEAN);
		proxy.addStar(string, new AsyncCallback(){
			public void onSuccess( final Object result ){
				assertEquals( string + "*", result );
				
				final AdvisedRpcMethodInterceptor interceptor = (AdvisedRpcMethodInterceptor) factory.getBean( ADVISOR );
				assertEquals( 1, interceptor.executedCount );
				finishTest();
			}
			public void onFailure( final Throwable cause ){
				fail( cause.getMessage() );
			}
		});
		
		this.delayTestFinish( DELAY_TIMEOUT );
	}
	
	public void testMethodInvocation() {
		final BeanFactory factory = (BeanFactory) GWT.create(MethodInvocationBeanFactory.class);
		final MethodInvocationTestTarget proxy = (MethodInvocationTestTarget) factory.getBean(BEAN);
		proxy.method(false, (byte)0, (short)1, (int)2, (long)3, 4f, 5.0, 'a', "string" );
		
		final MethodInterceptorImpl interceptor = (MethodInterceptorImpl) factory.getBean( ADVISOR );
		final MethodInvocation methodInvocation = interceptor.methodInvocation;
		assertEquals( "method", methodInvocation.getMethod() );
		assertEquals( false, methodInvocation.isNative() );
		assertEquals( "java.lang.Object", methodInvocation.getReturnType() );
		assertEquals( "rocket.beans.test.beans.client.methodinvocation.SuperClassOfMethodInvocationTestTarget", methodInvocation.getEnclosingType() );		
		
		final String[] parameterTypes = methodInvocation.getParameterTypes(); 
		assertEquals( 9, parameterTypes.length );
		assertEquals( "Z", parameterTypes[ 0 ]);
		assertEquals( "B", parameterTypes[ 1 ]);
		assertEquals( "S", parameterTypes[ 2 ]);
		assertEquals( "I", parameterTypes[ 3 ]);
		assertEquals( "J", parameterTypes[ 4 ]);
		assertEquals( "F", parameterTypes[ 5 ]);
		assertEquals( "D", parameterTypes[ 6 ]);
		assertEquals( "C", parameterTypes[ 7 ]);
		assertEquals( "java.lang.String", parameterTypes[ 8 ]);
	}

	public void testProxyTargetThrowsCheckedException() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyTargetThrowsCheckedExceptionBeanFactory.class);
		final ClassWithMethodThatThrowsCheckedException proxy = (ClassWithMethodThatThrowsCheckedException) factory.getBean(BEAN);

		try {
			proxy.throwCheckedException();
			fail("Was expecting a CheckedException to be thrown");
		} catch (final CheckedException expected) {
		}

		final ProxyTargetThrowsCheckedExceptionMethodInterceptor advisor = (ProxyTargetThrowsCheckedExceptionMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testProxyTargetThrowsCheckedExceptionOnUnadvicedMethod() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyTargetThrowsCheckedExceptionBeanFactory.class);
		final ClassWithMethodThatThrowsCheckedException proxy = (ClassWithMethodThatThrowsCheckedException) factory.getBean(BEAN);

		try {
			proxy.unadvicedThrowCheckedException();
			fail("Was expecting a CheckedException to be thrown");
		} catch (final CheckedException expected) {
		}

		final ProxyTargetThrowsCheckedExceptionMethodInterceptor advisor = (ProxyTargetThrowsCheckedExceptionMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(0, advisor.executedCount);
	}

	public void testProxyTargetThrowsCheckedExceptionLetsRuntimeExceptionsThrough() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyTargetThrowsCheckedExceptionBeanFactory.class);
		final ClassWithMethodThatThrowsCheckedException proxy = (ClassWithMethodThatThrowsCheckedException) factory.getBean(BEAN);

		try {
			proxy.throwRuntimeException();
			fail("Was expecting a RuntimeException to be thrown");
		} catch (final RuntimeException expected) {
		}
	}

	public void testProxyTargetThrowsUncheckedException() {
		final BeanFactory factory = (BeanFactory) GWT.create(ProxyTargetThrowsUncheckedExceptionBeanFactory.class);
		final ClassWithMethodThatThrowsUncheckedException proxy = (ClassWithMethodThatThrowsUncheckedException) factory.getBean(BEAN);

		try {
			proxy.throwUncheckedException();
			fail("Was expecting a UncheckedException to be thrown");
		} catch (final UncheckedException expected) {
		}
	}

	public void testMixtureOfAdvisedAndUnadvisedMethods() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedComplexBeanFactory.class);
		final Complex proxy = (Complex) factory.getBean(BEAN);
		final ComplexMethodInterceptor interceptor = (ComplexMethodInterceptor) factory.getBean(ADVISOR);

		interceptor.executedCount = 0;
		int expectedInterceptorExecutedCount = 1;
		assertEquals("advised", true ^ false, proxy.advisedXor(true, false));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		assertEquals("advised", BYTE + BYTE, proxy.advisedAdd(BYTE, BYTE));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		assertEquals("advised", SHORT + SHORT, proxy.advisedAdd(SHORT, SHORT));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		assertEquals("advised", INT + INT, proxy.advisedAdd(INT, INT));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		assertEquals("advised", LONG + LONG, proxy.advisedAdd(LONG, LONG));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		assertEquals("advised", FLOAT + FLOAT, proxy.advisedAdd(FLOAT, FLOAT), 0.1f);
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		assertEquals("advised", DOUBLE + DOUBLE, proxy.advisedAdd(DOUBLE, DOUBLE), 0.1f);
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		assertEquals("advised", Character.toUpperCase('a'), proxy.advisedToUpperCase('a'));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		assertEquals("advised", "APPLE".toLowerCase(), proxy.advisedToLowerCase("APPLE"));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		assertEquals("advised", null, proxy.advisedReturnsNull(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, new Object()));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		final Object object = new Object();
		assertSame("advised", object, proxy.advisedReturnsObject(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, object));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		try {
			proxy.advisedThrowCheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final Exception expected ) {
		}
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;
		
		try {
			proxy.advisedThrowUncheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final RuntimeException expected ) {
		}
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
		
		assertEquals("unadvised", true ^ false, proxy.unadvisedXor(true, false));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", BYTE + BYTE, proxy.unadvisedAdd(BYTE, BYTE));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", SHORT + SHORT, proxy.unadvisedAdd(SHORT, SHORT));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", INT + INT, proxy.unadvisedAdd(INT, INT));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", LONG + LONG, proxy.unadvisedAdd(LONG, LONG));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", FLOAT + FLOAT, proxy.unadvisedAdd(FLOAT, FLOAT), 0.1f);
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", DOUBLE + DOUBLE, proxy.unadvisedAdd(DOUBLE, DOUBLE), 0.1f);
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", Character.toUpperCase('a'), proxy.unadvisedToUpperCase('a'));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", "APPLE".toLowerCase(), proxy.unadvisedToLowerCase("APPLE"));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", null, proxy.unadvisedReturnsNull(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, new Object()));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		assertSame("unadvised", object, proxy.unadvisedReturnsObject(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, object));
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		try {
			proxy.unadvisedThrowCheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final Exception expected) {
		}
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);

		try {
			proxy.unadvisedThrowUncheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final RuntimeException expected) {
		}
		assertEquals( expectedInterceptorExecutedCount, interceptor.executedCount);
	}
}
