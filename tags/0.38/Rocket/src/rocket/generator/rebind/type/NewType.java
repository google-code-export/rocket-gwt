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
package rocket.generator.rebind.type;

import rocket.generator.rebind.CodeGenerator;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.initializer.Initializer;
import rocket.generator.rebind.method.NewMethod;

/**
 * A NewType represents a mutable Type that is also able to generate code to
 * represent itself.
 * 
 * @author Miroslav Pokorny
 */
public interface NewType extends Type, CodeGenerator {

	void addInterface(Type interfacee);

	void addInitializer(Initializer initializer);

	boolean hasName();

	NewField newField();

	void addField(NewField field);

	NewMethod newMethod();

	void addMethod(NewMethod method);

	void setSuperType(Type superType);

	NewNestedType newNestedType();

	void addNestedType(NewNestedType type);

	void addNestedInterfaceType(NewNestedInterfaceType type);

	NewAnonymousNestedType newAnonymousNestedType();

	NewNestedInterfaceType newNestedInterfaceType();
}
