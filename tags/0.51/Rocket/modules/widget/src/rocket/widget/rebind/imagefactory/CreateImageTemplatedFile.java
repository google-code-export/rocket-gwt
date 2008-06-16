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
package rocket.widget.rebind.imagefactory;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

public class CreateImageTemplatedFile extends TemplatedFileCodeBlock {

	protected String getResourceName() {
		return ImageFactoryConstants.TEMPLATE;
	}

	protected Object getValue0(final String name) {
		Object value = null;

		if (ImageFactoryConstants.URL_TEMPLATE_PLACEHOLDER.equals(name)) {
			value = new StringLiteral(this.getUrl());
		}
		if (ImageFactoryConstants.IMAGE_TYPE_TEMPLATE_PLACEHOLDER.equals(name)) {
			value = this.getType();
		}

		return value;
	}

	/**
	 * The image type which is typically either a {
	 * 
	 * @link com.google.gwt.user.client.ui.Image } or
	 *       {@link rocket.widget.client.Image }
	 */
	private Type type;

	protected Type getType() {
		Checker.notNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		Checker.notNull("parameter:type", type);
		this.type = type;
	}

	private String url;

	protected String getUrl() {
		Checker.notEmpty("field:url", url);
		return this.url;
	}

	public void setUrl(final String url) {
		Checker.notEmpty("parameter:url", url);
		this.url = url;
	}
}
