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
package rocket.beans.rebind.value;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * A convenient base class for any constructor or property value.
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractValue implements CodeBlock {
	/**
	 * Tests if this property value entry is compatible with its field type.
	 * 
	 * @param type
	 *            The type being tested
	 * @return True if the type is compatible false otherwise
	 */
	abstract public boolean isCompatibleWith(Type type);

	private Type type;

	protected Type getType() {
		Checker.notNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		Checker.notNull("parameter:type", type);
		this.type = type;
	}

	private GeneratorContext generatorContext;

	public GeneratorContext getGeneratorContext() {
		Checker.notNull("field:generatorContext", generatorContext);
		return this.generatorContext;
	}

	public void setGeneratorContext(final GeneratorContext generatorContext) {
		Checker.notNull("parameter:generatorContext", generatorContext);
		this.generatorContext = generatorContext;
	}

	public boolean isEmpty() {
		return false;
	}

	public void setPropertyType(final Type ignored) {
	}

	/**
	 * 
	 */
	private String filename;

	protected String getFilename() {
		return this.filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public String toString() {
		return super.toString() + ", type: " + type;
	}
}
