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
import rocket.util.client.Checker;

public class FieldGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final String typeName, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();
		final Type type = context.getType(FieldTest.class.getName());

		Checker.equals("field count", 5, type.getFields().size());

		final Field publicBooleanField = type.getField("publicBooleanField");

		Checker.same("publicBooleanField visibility", Visibility.PUBLIC, publicBooleanField.getVisibility());
		Checker.equals("publicBooleanField name", "publicBooleanField", publicBooleanField.getName());
		Checker.same("publicBooleanField type", context.findType(Boolean.TYPE.getName()), publicBooleanField.getType());
		Checker.same("publicBooleanField enclosing type", type, publicBooleanField.getEnclosingType());
		Checker.falseValue("publicBooleanField static", publicBooleanField.isStatic());
		Checker.falseValue("publicBooleanField transient", publicBooleanField.isTransient());
		Checker.falseValue("publicBooleanField final", publicBooleanField.isFinal());

		final Field protectedByteField = type.getField("protectedByteField");

		Checker.same("protectedByteField visibility", Visibility.PROTECTED, protectedByteField.getVisibility());
		Checker.equals("protectedByteField name", "protectedByteField", protectedByteField.getName());
		Checker.same("protectedByteField type", context.findType(Byte.TYPE.getName()), protectedByteField.getType());
		Checker.same("protectedByteField enclosing type", type, protectedByteField.getEnclosingType());
		Checker.falseValue("protectedByteField static", protectedByteField.isStatic());
		Checker.falseValue("protectedByteField transient", protectedByteField.isTransient());
		Checker.falseValue("protectedByteField final", protectedByteField.isFinal());

		final Field packagePrivateShortField = type.getField("packagePrivateShortField");

		Checker.same("packagePrivateShortField visibility", Visibility.PACKAGE_PRIVATE, packagePrivateShortField.getVisibility());
		Checker.equals("packagePrivateShortField name", "packagePrivateShortField", packagePrivateShortField.getName());
		Checker.same("packagePrivateShortField type", context.findType(Short.TYPE.getName()), packagePrivateShortField.getType());
		Checker.same("packagePrivateShortField enclosing type", type, packagePrivateShortField.getEnclosingType());
		Checker.falseValue("packagePrivateShortField static", packagePrivateShortField.isStatic());
		Checker.falseValue("packagePrivateShortField transient", packagePrivateShortField.isTransient());
		Checker.falseValue("packagePrivateShortField final", packagePrivateShortField.isFinal());

		final Field privateIntField = type.getField("privateIntField");

		Checker.same("privateIntField visibility", Visibility.PRIVATE, privateIntField.getVisibility());
		Checker.equals("privateIntField name", "privateIntField", privateIntField.getName());
		Checker.same("privateIntField type", context.findType(Integer.TYPE.getName()), privateIntField.getType());
		Checker.same("privateIntField enclosing type", type, privateIntField.getEnclosingType());
		Checker.falseValue("privateIntField static", privateIntField.isStatic());
		Checker.falseValue("privateIntField transient", privateIntField.isTransient());
		Checker.falseValue("privateIntField final", privateIntField.isFinal());

		final Field finalStaticTransientLongField = type.getField("finalStaticTransientLongField");

		Checker.same("finalStaticTransientLongField visibility", Visibility.PUBLIC, finalStaticTransientLongField.getVisibility());
		Checker.equals("finalStaticTransientLongField name", "finalStaticTransientLongField", finalStaticTransientLongField.getName());
		Checker.same("finalStaticTransientLongField type", context.findType(Long.TYPE.getName()), finalStaticTransientLongField.getType());
		Checker.same("finalStaticTransientLongField enclosing type", type, finalStaticTransientLongField.getEnclosingType());
		Checker.trueValue("finalStaticTransientLongField static", finalStaticTransientLongField.isStatic());
		Checker.trueValue("finalStaticTransientLongField transient", finalStaticTransientLongField.isTransient());
		Checker.trueValue("finalStaticTransientLongField final", finalStaticTransientLongField.isFinal());

		return null;
	}
}
