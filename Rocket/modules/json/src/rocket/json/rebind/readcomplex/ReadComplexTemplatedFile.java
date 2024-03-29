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
package rocket.json.rebind.readcomplex;

import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the read-complex template
 * 
 * @author Miroslav Pokorny
 */
public class ReadComplexTemplatedFile extends TemplatedFileCodeBlock {

	public ReadComplexTemplatedFile() {
		super();
	}

	/**
	 * The type being deserialized
	 */
	private Type deserializerType;

	protected Type getDeserializerType() {
		Checker.notNull("deserializerType:deserializerType", deserializerType);
		return this.deserializerType;
	}

	public void setDeserializerType(final Type deserializerType) {
		Checker.notNull("parameter:deserializerType", deserializerType);
		this.deserializerType = deserializerType;
	}

	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.TYPE.equals(name)) {
				value = this.getDeserializerType();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found in file \"" + Constants.TEMPLATE + "\".");
	}
}
