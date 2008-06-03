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

import rocket.beans.rebind.value.AbstractValue;
import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * Represents an image being fetched from a ImageFactory
 * 
 * @author Miroslav Pokorny
 * 
 */
public class ImageValue extends AbstractValue implements Value {

	public ImageValue() {
		super();
	}

	@Override
	public boolean isCompatibleWith(final Type type) {
		Checker.notNull("parameter:type", type);

		final String name = type.getName();
		return rocket.widget.client.Image.class.getName().equals(name)
				|| com.google.gwt.user.client.ui.Image.class.getName().equals(name);
	}

	public void write(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final ImageFactoryGetImageTemplatedFile template = new ImageFactoryGetImageTemplatedFile();
		template.setImageGetter(this.getImageFactoryGetter());

		template.write(writer);
	}

	private String file;

	public String getFile() {
		Checker.notEmpty("field:file", file);
		return this.file;
	}

	public void setFile(final String file) {
		Checker.notEmpty("parameter:file", file);
		this.file = file;
	}

	private boolean local;

	public boolean isLocal() {
		return this.local;
	}

	public void setLocal(final boolean local) {
		this.local = local;
	}

	private boolean lazy;

	public boolean isLazy() {
		return this.lazy;
	}

	public void setLazy(final boolean lazy) {
		this.lazy = lazy;
	}

	/**
	 * The index of this particular image within the image factory being
	 * generated. This will be used to form the name of the image factory
	 * getter.
	 */
	private int imageIndex;

	public int getImageIndex() {
		Checker.greaterThanOrEqual("field:imageIndex", 0, imageIndex);
		return this.imageIndex;
	}

	public void setImageIndex(final int imageIndex) {
		Checker.greaterThanOrEqual("parameter:imageIndex", 0, imageIndex);
		this.imageIndex = imageIndex;
	}

	/**
	 * The image factory getter that will fetch the image.
	 */
	private Method imageFactoryGetter;

	public Method getImageFactoryGetter() {
		Checker.notNull("field:imageFactoryGetter", imageFactoryGetter);
		return this.imageFactoryGetter;
	}

	public void setImageFactoryGetter(final Method imageFactoryGetter) {
		Checker.notNull("parameter:imageFactoryGetter", imageFactoryGetter);
		this.imageFactoryGetter = imageFactoryGetter;
	}

	public Type getPropertyType() {
		return super.getType();
	}

	@Override
	public void setPropertyType(final Type propertyType) {
		super.setType(propertyType);
	}

}
