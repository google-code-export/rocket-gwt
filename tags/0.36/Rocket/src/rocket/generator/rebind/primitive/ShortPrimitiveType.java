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
package rocket.generator.rebind.primitive;

import rocket.generator.rebind.type.Type;

/**
 * A common base class to represent the short primitive
 * 
 * @author Miroslav Pokorny
 */
abstract public class ShortPrimitiveType extends PrimitiveType {

	public boolean isAssignableFrom(final Type type) {
		return type.equals(this.getByte()) || this.getShort().equals(type);
	}

	public boolean isAssignableTo(final Type type) {
		return this.equals(type) || this.getInt().equals(type) || this.getLong().equals(type) || this.getFloat().equals(type)
				|| this.getDouble().equals(type);
	}

	public Type getWrapper() {
		return this.getType(Constants.SHORT_WRAPPER);
	}

	public String toString() {
		return "short";
	}
}
