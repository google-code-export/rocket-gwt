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
package rocket.generator.rebind.constructor;

import java.util.List;
import java.util.Set;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewNestedType;
import rocket.generator.rebind.type.Type;

/**
 * Represents a constructor for a type.
 * 
 * @author Miroslav Pokorny
 */
public interface Constructor {

	Visibility getVisibility();

	Type getEnclosingType();

	List<ConstructorParameter> getParameters();

	Set<Type> getThrownTypes();

	NewConstructor copy(NewConcreteType newConcreteType);

	NewConstructor copy(NewNestedType newNestedType);
}
