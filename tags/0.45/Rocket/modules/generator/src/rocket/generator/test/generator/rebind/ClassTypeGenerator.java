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
package rocket.generator.test.generator.rebind;

import java.util.Set;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.test.generator.client.ClassType;
import rocket.util.client.Checker;

public class ClassTypeGenerator extends TestGenerator {

	final static String STRING = String.class.getName();

	protected NewConcreteType assembleNewType(final String typeName, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();
		final Type type = context.getType(ClassType.class.getName());

		Checker.falseValue("TestType type is a not abstract", type.isAbstract());
		Checker.falseValue("TestType type is not an interface", type.isInterface());
		Checker.falseValue("TestType type is a final", type.isFinal());
		Checker.equals("TestType type name", ClassType.class.getName(), type.getName());
		Checker.falseValue("TestType type is not an array", type.isArray());
		Checker.falseValue("TestType type is not a primitive", type.isPrimitive());

		final Set inner = type.getNestedTypes();
		Checker.equals("nested inner types", 1, inner.size());

		final Type nestedType = (Type) inner.iterator().next();
		Checker.notNull("nested type", nestedType);

		return null;
	}
}
