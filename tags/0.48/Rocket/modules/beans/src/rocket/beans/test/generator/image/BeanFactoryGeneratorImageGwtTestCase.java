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
package rocket.beans.test.generator.image;

import rocket.beans.client.BeanFactory;
import rocket.beans.test.generator.image.gwtimage.HasGwtImage;
import rocket.beans.test.generator.image.gwtimage.HasGwtImageBeanFactory;
import rocket.beans.test.generator.image.rocketimage.HasRocketImage;
import rocket.beans.test.generator.image.rocketimage.HasRocketImageBeanFactory;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;

/**
 * A series of tests for the BeanFactoryGenerator.
 *
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorImageGwtTestCase extends GeneratorGwtTestCase {

	final static String BEAN = "bean";

	public String getModuleName() {
		return "rocket.beans.test.generator.image.BeanFactoryGeneratorImage";
	}

	public void testGwtImage(){
		final BeanFactory factory = (BeanFactory) GWT.create( HasGwtImageBeanFactory.class );
		assertNotNull(factory);
		
		final HasGwtImage bean = (HasGwtImage) factory.getBean( BEAN );
		assertNotNull( bean );
		
		final com.google.gwt.user.client.ui.Image image = bean.getImage();
		assertNotNull( bean );
		
		final String url = image.getUrl();
		assertTrue( url, url.length() > 0 );
	}
	
	public void testRocketImage(){
		final BeanFactory factory = (BeanFactory) GWT.create( HasRocketImageBeanFactory.class );
		assertNotNull(factory);
		
		final HasRocketImage bean = (HasRocketImage) factory.getBean( BEAN );
		assertNotNull( bean );
		
		final rocket.widget.client.Image image = bean.getImage();
		assertNotNull( bean );
		
		final String url = image.getUrl();
		assertTrue( url, url.length() > 0 );
	}
}
