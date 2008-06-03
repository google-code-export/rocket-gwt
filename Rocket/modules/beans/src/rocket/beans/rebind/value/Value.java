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

import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.type.Type;

/**
 * Concrete values are values for constructor or properties these typically
 * include.
 * <ul>
 * <li>null references</li>
 * <li>String literals</li>
 * <li>bean references</li>
 * <li>nested beans</li>
 * <li>lists</li>
 * <li>sets</li>
 * <li>maps</li>
 * </ul>
 * 
 * @author Miroslav Pokorny
 */
public interface Value extends CodeBlock {
	/**
	 * Tests if this property value entry is compatible with its field type.
	 * 
	 * @param type
	 *            The type being tested
	 * @return true if the type is compatible with the value.
	 */
	abstract boolean isCompatibleWith(Type type);

	/**
	 * The explicit value type.
	 * 
	 * @param type
	 *            The value type.
	 */
	void setType(Type type);

	/**
	 * This method should be called after the property type has been identified.
	 * 
	 * @param type
	 */
	void setPropertyType(Type type);

	/**
	 * The source file that contained this value.
	 * 
	 * @param filename
	 *            The filename
	 */
	void setFilename(String filename);
}
