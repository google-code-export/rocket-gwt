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
package rocket.generator.rebind.java;

import rocket.generator.rebind.primitive.BytePrimitiveType;

/**
 * Represents a primitive char type
 * 
 * @author Miroslav Pokorny
 */
public class JavaCharClassTypeAdapter extends BytePrimitiveType {
	public String getName() {
		return this.getJavaClass().getName();
	}

	public String getSimpleName() {
		return this.getJavaClass().getSimpleName();
	}

	public String getJsniNotation() {
		throw new UnsupportedOperationException();
	}

	protected Class getJavaClass() {
		return Character.TYPE;
	}
}
