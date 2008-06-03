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
package rocket.beans.test.generator.bean;

import rocket.beans.client.BeanFactory;
import rocket.beans.client.BeanFactoryImpl;
import rocket.beans.test.generator.bean.destroymethod.DestroyMethodBeanFactory;
import rocket.beans.test.generator.bean.destroymethod.HasCustomDestroy;
import rocket.beans.test.generator.bean.destroymethodnotfound.DestroyMethodNotFoundBeanFactory;
import rocket.beans.test.generator.bean.disposable.DisposableBeanBeanFactory;
import rocket.beans.test.generator.bean.disposable.DisposableBeanImpl;
import rocket.beans.test.generator.bean.eagersingleton.EagerlyLoadedSingleton;
import rocket.beans.test.generator.bean.eagersingleton.EagerlyLoadedSingletonBeanFactory;
import rocket.beans.test.generator.bean.escapeunsafeidintosafefactorybeanclassname.EscapeUnsafeIdIntoSafeFactoryBeanClassName;
import rocket.beans.test.generator.bean.factorymethod.FactoryMethodBeanFactory;
import rocket.beans.test.generator.bean.factorymethod.FactoryMethodProduct;
import rocket.beans.test.generator.bean.factorymethodnotfound.FactoryMethodNotFoundBeanFactory;
import rocket.beans.test.generator.bean.initmethod.HasCustomInit;
import rocket.beans.test.generator.bean.initmethod.InitMethodBeanFactory;
import rocket.beans.test.generator.bean.initmethodnotfound.InitMethodNotFoundBeanFactory;
import rocket.beans.test.generator.bean.invalidscope.InvalidScopeBeanFactory;
import rocket.beans.test.generator.bean.lazilyloadedsingleton.LazyLoadedSingletonBeanFactory;
import rocket.beans.test.generator.bean.lazilyloadedsingleton.LazySingleton;
import rocket.beans.test.generator.bean.missingbeanid.MissingIdBeanFactory;
import rocket.beans.test.generator.bean.notconcrete.NotConcreteBeanFactory;
import rocket.beans.test.generator.bean.prototypewithdestroymethod.PrototypeWithDestroyMethodBeanFactory;
import rocket.beans.test.generator.bean.typenotfound.TypeNotFoundBeanFactory;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorBeanGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN = "bean";

	public String getModuleName() {
		return "rocket.beans.test.generator.bean.BeanFactoryGeneratorBean";
	}

	public void testMissingBeanId() {
		try {
			assertBindingFailed(GWT.create(MissingIdBeanFactory.class));
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
			assertBindingFailed(GWT.create(TypeNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testBeanTypeIsNotConcrete() {
		try {
			assertBindingFailed(GWT.create(NotConcreteBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testEscapeUnsafeIdIntoSafeFactoryBeanClassName() {
		final EscapeUnsafeIdIntoSafeFactoryBeanClassName beanFactory = (EscapeUnsafeIdIntoSafeFactoryBeanClassName) GWT
				.create(EscapeUnsafeIdIntoSafeFactoryBeanClassName.class);
		assertNotNull(beanFactory);
	}

	public void testLazilyLoadedSingletonBean() {
		final LazyLoadedSingletonBeanFactory beanFactory = (LazyLoadedSingletonBeanFactory) GWT
				.create(LazyLoadedSingletonBeanFactory.class);
		assertFalse("singleton is not yet loaded", LazySingleton.loaded);
		beanFactory.getBean(BEAN);
		assertTrue("singleton is now loaded", LazySingleton.loaded);
	}

	public void testEagerlyLoadedSingletonBean() {
		assertFalse("singleton should have not been have been loaded until factory is created.", EagerlyLoadedSingleton.loaded);
		final EagerlyLoadedSingletonBeanFactory beanFactory = (EagerlyLoadedSingletonBeanFactory) GWT
				.create(EagerlyLoadedSingletonBeanFactory.class);
		assertNotNull(beanFactory);
		assertTrue("singleton should have been loaded", EagerlyLoadedSingleton.loaded);
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
		final FactoryMethodProduct bean = (FactoryMethodProduct) factory.getBean(BEAN);
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
		final InitMethodBeanFactory factory = (InitMethodBeanFactory) GWT.create(InitMethodBeanFactory.class);
		final HasCustomInit bean = (HasCustomInit) factory.getBean(BEAN);
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
			assertBindingFailed(GWT.create(PrototypeWithDestroyMethodBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testDisposableBean() {
		final DisposableBeanBeanFactory factory = (DisposableBeanBeanFactory) GWT.create(DisposableBeanBeanFactory.class);
		final DisposableBeanImpl bean = (DisposableBeanImpl) factory.getBean(BEAN);
		assertNotNull(bean);

		((BeanFactoryImpl) factory).shutdown();

		assertEquals("bean should have been destroyed", 1, bean.destroyed);
	}

	public void testDestroyMethod() {
		final DestroyMethodBeanFactory factory = (DestroyMethodBeanFactory) GWT.create(DestroyMethodBeanFactory.class);
		final HasCustomDestroy bean = (HasCustomDestroy) factory.getBean(BEAN);
		assertNotNull(bean);

		((BeanFactoryImpl) factory).shutdown();

		assertEquals("bean should have been destroyed", 1, bean.destroyed);
	}

}
