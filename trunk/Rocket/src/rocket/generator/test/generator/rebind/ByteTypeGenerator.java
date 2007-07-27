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

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

public class ByteTypeGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final String typeName, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final Type type = context.getType(Byte.TYPE.getName());

		PrimitiveHelper.checkFalse("Byte type is a not abstract", type.isAbstract());
		PrimitiveHelper.checkFalse("Byte type is not an interface", type.isInterface());
		PrimitiveHelper.checkTrue("Byte type is a final", type.isFinal());
		ObjectHelper.checkEquals("Byte type name", Byte.TYPE.getName(), type.getName());
		PrimitiveHelper.checkFalse("Byte type is not an array", type.isArray());
		PrimitiveHelper.checkTrue("Byte type is a primitive", type.isPrimitive());

		final Type wrapper = type.getWrapper();
		ObjectHelper.checkEquals("Byte wrapper type name", Byte.class.getName(), wrapper.getName());

		return null;
	}
}
