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

/**
 * A template that provides the body of the getImageFactory method.
 * 
 * @author Miroslav Pokorny
 */
public class ImageFactorySetterTemplatedFile extends TemplatedFileCodeBlock {

	public ImageFactorySetterTemplatedFile() {
		super();
	}
	
	protected String getResourceName() {
		return Constants.SETTER_TEMPLATE;
	}
	
	public InputStream getInputStream(){
		return super.getInputStream();
	}

	protected Object getValue0(final String name) {
		throw new UnsupportedOperationException();
	}
}
