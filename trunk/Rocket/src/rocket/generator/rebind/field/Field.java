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

import rocket.generator.rebind.ClassComponent;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.metadata.HasMetadata;
import rocket.generator.rebind.type.Type;

/**
 * Represents a field that belongs to a type.
 * 
 * @author Miroslav Pokorny
 */
public interface Field extends ClassComponent, HasMetadata {

	String getName();

	String getJsniNotation();

	Type getType();

	Visibility getVisibility();

	Type getEnclosingType();

	boolean isFinal();

	boolean isStatic();

	boolean isTransient();

	/**
	 * Copies all of the properties of this field into a NewField. Only the
	 * enclosingType and body properties must be set
	 * 
	 * @return A {@link NewField}
	 */
	NewField copy();
}
