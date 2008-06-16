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
package rocket.beans.rebind.image;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * This template provides a code snippet to fetch an image from the image
 * factory belonging to the bean factory being generated.
 * 
 * @author Miroslav Pokorny
 */
class ImageFactoryGetImageTemplatedFile extends TemplatedFileCodeBlock {

	public ImageFactoryGetImageTemplatedFile() {
		super();
	}

	@Override
	public boolean isNative() {
		return false;
	}

	@Override
	public void setNative(final boolean nativee) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The bean factory being created type.
	 */
	private Type factoryType;

	protected Type getFactoryType() {
		Checker.notNull("field:factoryType", factoryType);
		return this.factoryType;
	}

	public void setFactoryType(final Type factoryType) {
		Checker.notNull("factoryType:factoryType", factoryType);
		this.factoryType = factoryType;
	}

	/**
	 * The image imageGetter image getter
	 */
	private Method imageGetter;

	protected Method getImageGetter() {
		Checker.notNull("field:imageGetter", imageGetter);
		return this.imageGetter;
	}

	public void setImageGetter(final Method imageGetter) {
		Checker.notNull("imageGetter:imageGetter", imageGetter);
		this.imageGetter = imageGetter;
	}

	protected Type getImageType() {
		return this.getImageGetter().getReturnType();
	}

	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.IMAGE_GETTER_METHOD.equals(name)) {
				value = this.getImageGetter();
				break;
			}
			if (Constants.IMAGE_TYPE.equals(name)) {
				value = this.getImageType();
				break;
			}
			break;
		}
		return value;
	}
}
