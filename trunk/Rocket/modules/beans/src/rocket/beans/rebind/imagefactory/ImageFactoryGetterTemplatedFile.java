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
package rocket.beans.rebind.imagefactory;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * A template that provides the body of the setImageFactory method.
 * 
 * @author Miroslav Pokorny
 */
public class ImageFactoryGetterTemplatedFile extends TemplatedFileCodeBlock {

	public ImageFactoryGetterTemplatedFile() {
		super();
	}

	@Override
	protected String getResourceName() {
		return Constants.GETTER_TEMPLATE;
	}
	
	@Override
	public InputStream getInputStream(){
		return super.getInputStream();
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		
		if (Constants.GETTER_IMAGE_FACTORY_TYPE.equals(name)) {
			value = this.getImageFactory();
		}
		
		return value;
	}
	
	private Type imageFactory;
	
	protected Type getImageFactory(){
		Checker.notNull("field:imageFactory", imageFactory );
		return this.imageFactory;
	}
	
	public void setImageFactory( final Type imageFactory ){
		Checker.notNull("parameter:imageFactory", imageFactory );
		this.imageFactory = imageFactory;
	}
}
