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
package rocket.beans.test.generator.beanreference;

import rocket.beans.client.BeanFactory;
import rocket.beans.test.generator.beanreference.alias.ReferencesAlias;
import rocket.beans.test.generator.beanreference.alias.ReferencesAliasBeanFactory;
import rocket.beans.test.generator.beanreference.beanreference.BeanReferenceBeanFactory;
import rocket.beans.test.generator.beanreference.beanreference.HasNamedBeanReferences;
import rocket.beans.test.generator.beanreference.complex.ComplexBeanFactory;
import rocket.beans.test.generator.beanreference.doublenestedbean.DoubleNestedBeanBeanFactory;
import rocket.beans.test.generator.beanreference.doublenestedbean.HasDoubleNestedBean;
import rocket.beans.test.generator.beanreference.nestedbean.NestedBeanBeanFactory;
import rocket.beans.test.generator.beanreference.nestedbeanhasid.NestedBeanHasIdBeanFactory;
import rocket.beans.test.generator.beanreference.productoffactorybean.FactoryBeanImpl;
import rocket.beans.test.generator.beanreference.productoffactorybean.HasProductOfFactoryBean;
import rocket.beans.test.generator.beanreference.productoffactorybean.ReferencesFactoryBeanBeanFactory;
import rocket.beans.test.generator.beanreference.productofnestedfactorybean.HasProductOfNestedFactoryBean;
import rocket.beans.test.generator.beanreference.productofnestedfactorybean.NestedFactoryBeanBeanFactory;
import rocket.beans.test.generator.beanreference.productofnestedfactorybean.NestedFactoryBeanImpl;
import rocket.beans.test.generator.beanreference.singletonwithprototype.HasAnotherBean;
import rocket.beans.test.generator.beanreference.singletonwithprototype.SingletonWithPrototypeBeanFactory;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorBeanReferenceGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN = "bean";

	public String getModuleName() {
		return "rocket.beans.test.generator.beanreference.BeanFactoryGeneratorBeanReference";
	}
	
	public void testHasNamedBeanReference() {
		final BeanFactory factory = (BeanFactory) GWT.create(BeanReferenceBeanFactory.class);
		final HasNamedBeanReferences bean = (HasNamedBeanReferences) factory.getBean(BEAN);
		assertNotNull(bean);

		assertNotNull("bean reference (subclass) was not set", bean.getConcreteClass() );
		assertNotNull("bean reference (interface) was not set", bean.getInterface() );
	}
	
	public void testReferencesAlias(){
		final ReferencesAliasBeanFactory factory = (ReferencesAliasBeanFactory) GWT.create(ReferencesAliasBeanFactory.class);
		final ReferencesAlias bean = (ReferencesAlias)factory.getBean( BEAN );
		assertNotNull(bean);
		
		final Bean aliasedBean = bean.getAliasedBean();
		assertNotNull(aliasedBean);
	}

	public void testNestedBeanHasIdWhenItShouldnt(){
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
		final HasNestedBean bean = (HasNestedBean) beanFactory.getBean( BEAN );
		assertNotNull( "BeanWithNestedBean", bean );
		
		final Bean nestedBean = (Bean) bean.getNestedBean();
		assertNotNull( "BeanWithNestedBean.nestedBean", nestedBean );
		
		final Object $nestedBean = beanFactory.getBean( BEAN + "-nestedBean0");
		assertSame( nestedBean, $nestedBean );
	}
	

	public void testDoubleNestedBean(){
		final BeanFactory beanFactory = (BeanFactory)GWT.create( DoubleNestedBeanBeanFactory.class );
		final HasDoubleNestedBean bean = (HasDoubleNestedBean) beanFactory.getBean( BEAN );
		assertNotNull( "HasDoubleNestedBean", bean );	
		
		final HasNestedBean hasNestedBean = bean.getNestedBean();
		assertNotNull( "HasDoubleNestedBean.hasNestedBean", hasNestedBean );
		
		assertNotNull( "HasDoubleNestedBean.hasNestedBean.bean", hasNestedBean.getNestedBean() );
		
		final Object nestedBean = beanFactory.getBean( BEAN + "-nestedBean0");
		assertSame( bean.getNestedBean(), nestedBean );
		
		final Object doubleNestedBean = beanFactory.getBean( BEAN + "-nestedBean0-nestedBean0");
		assertSame( bean.getNestedBean().getNestedBean(), doubleNestedBean );
	}

	public void testReferencesBeanWhichIsAProductOfFactoryBean(){
		final ReferencesFactoryBeanBeanFactory beanFactory = (ReferencesFactoryBeanBeanFactory) GWT.create( ReferencesFactoryBeanBeanFactory.class );
		final HasProductOfFactoryBean bean = (HasProductOfFactoryBean)beanFactory.getBean( BEAN );
		assertNotNull( bean );
		assertEquals( "string - if this null factory bean didnt have its property set.", "string", FactoryBeanImpl.string );
		assertNotNull( "reference to bean produced by factory bean", bean.getBean() );
	}
	
	public void testNestedBeanWhichIsAProductOfFactoryBean(){
		final NestedFactoryBeanBeanFactory beanFactory = (NestedFactoryBeanBeanFactory) GWT.create( NestedFactoryBeanBeanFactory.class );
		final HasProductOfNestedFactoryBean bean = (HasProductOfNestedFactoryBean)beanFactory.getBean( BEAN );
		assertNotNull( bean );
		assertEquals( "string - if null factory bean didnt have its property set.", "string", NestedFactoryBeanImpl.string );
		assertNotNull( "reference to bean produced by factory bean", bean.getBean() );
	}
	
	public void testComplex(){
		final ComplexBeanFactory beanFactory = (ComplexBeanFactory) GWT.create(ComplexBeanFactory.class);
		
		final rocket.beans.test.generator.beanreference.complex.HasProductOfFactoryBean hasProductOfFactoryBean = (rocket.beans.test.generator.beanreference.complex.HasProductOfFactoryBean)beanFactory.getBean( "hasProductOfFactoryBean");
		assertNotNull( "HasProductOfFactoryBean", hasProductOfFactoryBean );
		assertNotNull( "HasProductOfFactoryBean.bean", hasProductOfFactoryBean.getBean() );
		
		final rocket.beans.test.generator.beanreference.complex.HasProductOfFactoryBean hasProductOfNestedFactoryBean = (rocket.beans.test.generator.beanreference.complex.HasProductOfFactoryBean)beanFactory.getBean( "hasProductOfNestedFactoryBean");
		assertNotNull( "HasProductOfFactoryBean.bean", hasProductOfNestedFactoryBean.getBean() );
	}
	
	public void testSingletonWithPrototype(){
		final BeanFactory beanFactory = (BeanFactory) GWT.create(SingletonWithPrototypeBeanFactory.class);
		
		final HasAnotherBean singleton = (HasAnotherBean)beanFactory.getBean( "singleton");
		assertNotNull( "singleton", singleton );
		
		final Bean prototype = singleton.getBean();
		assertNotNull( "singleton.prototype", singleton );

		final Bean prototype2 = (Bean)beanFactory.getBean( "prototype");
		assertNotNull( "prototype2", prototype2 );
		
		assertNotSame( prototype, prototype2 );
	}
	
	
}
