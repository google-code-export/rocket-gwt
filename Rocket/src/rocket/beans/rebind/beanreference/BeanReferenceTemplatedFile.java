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
package rocket.beans.rebind.beanreference;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * An abstraction for the beanReference template
 * 
 * @author Miroslav Pokorny
 */
public class BeanReferenceTemplatedFile extends TemplatedCodeBlock {

	public BeanReferenceTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The type of the type
	 */
	private Type type;

	protected Type getType() {
		ObjectHelper.checkNotNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		ObjectHelper.checkNotNull("type:type", type);
		this.type = type;
	}

	/**
	 * The id of the type. This is used to fetch the type from the type factory
	 */
	private String id;

	protected String getId() {
		StringHelper.checkNotNull("field:id", id);
		return this.id;
	}

	public void setId(final String id) {
		StringHelper.checkNotNull("parameter:id", id);
		this.id = id;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.BEAN_ID.equals(name)) {
				value = new StringLiteral(this.getId());
				break;
			}
			if (Constants.BEAN_TYPE.equals(name)) {
				value = this.getType();
				break;
			}
			break;
		}
		return value;
	}
}
