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
 * A common base class to represent the double primitive type
 * 
 * @author Miroslav Pokorny
 */
abstract public class DoublePrimitiveType extends PrimitiveType {

	@Override
	public String getRuntimeName() {
		return Constants.DOUBLE_RUNTIME_NAME;
	}

	public boolean isAssignableFrom(final Type type) {
		return this.equals(type);
	}

	public boolean isAssignableTo(final Type type) {
		return this.equals(type);
	}

	public Type getWrapper() {
		return this.getType(Constants.DOUBLE_WRAPPER);
	}

	@Override
	public String toString() {
		return "double";
	}
}
