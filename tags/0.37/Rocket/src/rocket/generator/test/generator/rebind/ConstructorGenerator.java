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

import java.util.Arrays;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

public class ConstructorGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();
		PrimitiveHelper.checkEquals("constructor count", 3, type.getConstructors().size());

		final Type booleanType = context.findType(Boolean.TYPE.getName());
		final Constructor booleanParameterConstructor = type.findConstructor(Arrays.asList(new Type[] { booleanType }));
		ObjectHelper.checkNotNull("booleanParameterConstructor", booleanParameterConstructor);
		ObjectHelper.checkSame("booleanParameterConstructor enclosing type", type, booleanParameterConstructor.getEnclosingType());

		final Type byteType = context.getType(Byte.TYPE.getName());
		final Constructor byteParameterConstructor = type.getConstructor(Arrays.asList(new Type[] { byteType }));
		ObjectHelper.checkSame("byteParameterConstructor enclosing type", type, byteParameterConstructor.getEnclosingType());

		return null;
	}
}
