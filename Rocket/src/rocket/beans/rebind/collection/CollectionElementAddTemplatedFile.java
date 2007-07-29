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
package rocket.beans.rebind.collection;

import java.io.InputStream;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for adding an element to a collection
 * 
 * @author Miroslav Pokorny
 */
public class CollectionElementAddTemplatedFile extends TemplatedCodeBlock {

	public CollectionElementAddTemplatedFile() {
		super();
		setNative(false);
	}

	private Value value;

	protected Value getValue() {
		ObjectHelper.checkNotNull("field:value", value);
		return this.value;
	}

	protected void setValue(final Value value) {
		ObjectHelper.checkNotNull("parameter:value", value);
		this.value = value;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.VALUE.equals(name)) {
				value = this.getValue();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found in [" + Constants.TEMPLATE + "]");
	}
}
