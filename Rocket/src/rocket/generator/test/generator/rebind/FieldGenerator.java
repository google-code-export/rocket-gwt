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
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.test.generator.client.FieldTest;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

public class FieldGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final String typeName, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();
		final Type type = context.getType(FieldTest.class.getName());

		PrimitiveHelper.checkEquals("field count", 5, type.getFields().size());

		final Field publicBooleanField = type.getField("publicBooleanField");

		ObjectHelper.checkSame("publicBooleanField visibility", Visibility.PUBLIC, publicBooleanField.getVisibility());
		ObjectHelper.checkEquals("publicBooleanField name", "publicBooleanField", publicBooleanField.getName());
		ObjectHelper.checkSame("publicBooleanField type", context.findType(Boolean.TYPE.getName()), publicBooleanField.getType());
		ObjectHelper.checkSame("publicBooleanField enclosing type", type, publicBooleanField.getEnclosingType());
		PrimitiveHelper.checkFalse("publicBooleanField static", publicBooleanField.isStatic());
		PrimitiveHelper.checkFalse("publicBooleanField transient", publicBooleanField.isTransient());
		PrimitiveHelper.checkFalse("publicBooleanField final", publicBooleanField.isFinal());

		final Field protectedByteField = type.getField("protectedByteField");

		ObjectHelper.checkSame("protectedByteField visibility", Visibility.PROTECTED, protectedByteField.getVisibility());
		ObjectHelper.checkEquals("protectedByteField name", "protectedByteField", protectedByteField.getName());
		ObjectHelper.checkSame("protectedByteField type", context.findType(Byte.TYPE.getName()), protectedByteField.getType());
		ObjectHelper.checkSame("protectedByteField enclosing type", type, protectedByteField.getEnclosingType());
		PrimitiveHelper.checkFalse("protectedByteField static", protectedByteField.isStatic());
		PrimitiveHelper.checkFalse("protectedByteField transient", protectedByteField.isTransient());
		PrimitiveHelper.checkFalse("protectedByteField final", protectedByteField.isFinal());

		final Field packagePrivateShortField = type.getField("packagePrivateShortField");

		ObjectHelper.checkSame("packagePrivateShortField visibility", Visibility.PACKAGE_PRIVATE, packagePrivateShortField.getVisibility());
		ObjectHelper.checkEquals("packagePrivateShortField name", "packagePrivateShortField", packagePrivateShortField.getName());
		ObjectHelper.checkSame("packagePrivateShortField type", context.findType(Short.TYPE.getName()), packagePrivateShortField.getType());
		ObjectHelper.checkSame("packagePrivateShortField enclosing type", type, packagePrivateShortField.getEnclosingType());
		PrimitiveHelper.checkFalse("packagePrivateShortField static", packagePrivateShortField.isStatic());
		PrimitiveHelper.checkFalse("packagePrivateShortField transient", packagePrivateShortField.isTransient());
		PrimitiveHelper.checkFalse("packagePrivateShortField final", packagePrivateShortField.isFinal());

		final Field privateIntField = type.getField("privateIntField");

		ObjectHelper.checkSame("privateIntField visibility", Visibility.PRIVATE, privateIntField.getVisibility());
		ObjectHelper.checkEquals("privateIntField name", "privateIntField", privateIntField.getName());
		ObjectHelper.checkSame("privateIntField type", context.findType(Integer.TYPE.getName()), privateIntField.getType());
		ObjectHelper.checkSame("privateIntField enclosing type", type, privateIntField.getEnclosingType());
		PrimitiveHelper.checkFalse("privateIntField static", privateIntField.isStatic());
		PrimitiveHelper.checkFalse("privateIntField transient", privateIntField.isTransient());
		PrimitiveHelper.checkFalse("privateIntField final", privateIntField.isFinal());

		final Field finalStaticTransientLongField = type.getField("finalStaticTransientLongField");

		ObjectHelper
				.checkSame("finalStaticTransientLongField visibility", Visibility.PUBLIC, finalStaticTransientLongField.getVisibility());
		ObjectHelper.checkEquals("finalStaticTransientLongField name", "finalStaticTransientLongField", finalStaticTransientLongField
				.getName());
		ObjectHelper.checkSame("finalStaticTransientLongField type", context.findType(Long.TYPE.getName()), finalStaticTransientLongField
				.getType());
		ObjectHelper.checkSame("finalStaticTransientLongField enclosing type", type, finalStaticTransientLongField.getEnclosingType());
		PrimitiveHelper.checkTrue("finalStaticTransientLongField static", finalStaticTransientLongField.isStatic());
		PrimitiveHelper.checkTrue("finalStaticTransientLongField transient", finalStaticTransientLongField.isTransient());
		PrimitiveHelper.checkTrue("finalStaticTransientLongField final", finalStaticTransientLongField.isFinal());

		return null;
	}
}
