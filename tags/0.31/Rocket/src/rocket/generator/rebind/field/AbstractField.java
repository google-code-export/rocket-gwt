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
package rocket.generator.rebind.field;

import rocket.generator.rebind.util.AbstractConstructorMethodOrField;

/**
 * A convenient base class for any field implementation.
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractField extends AbstractConstructorMethodOrField implements Field {
	/**
	 * Copies all of the properties of this field to a NewField. The
	 * enclosingType property remains to be set.
	 * 
	 * @return A NewField.
	 */
	public NewField copy() {
		final NewFieldImpl field = new NewFieldImpl();
		field.setGeneratorContext(this.getGeneratorContext());
		field.setFinal(this.isFinal());
		field.setName(this.getName());
		field.setStatic(this.isStatic());
		field.setTransient(this.isTransient());
		field.setType(this.getType());
		field.setVisibility(this.getVisibility());
		return field;
	}
}
