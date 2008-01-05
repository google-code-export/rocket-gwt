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
import java.util.Set;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.test.generator.client.MethodTest;
import rocket.util.client.Checker;

public class MethodGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final String typeName, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final Type type = context.getType(MethodTest.class.getName());

		Checker.equals("method count", 5, type.getMethods().size());

		final Type booleanType = context.getBoolean();
		final Method publicBooleanMethod = type.getMethod("publicBooleanMethod", Arrays.asList(new Type[] { booleanType }));

		Checker.same("publicBooleanMethod visibility", Visibility.PUBLIC, publicBooleanMethod.getVisibility());
		Checker.equals("publicBooleanMethod name", "publicBooleanMethod", publicBooleanMethod.getName());
		Checker.same("publicBooleanMethod returnType", booleanType, publicBooleanMethod.getReturnType());
		Checker.same("publicBooleanMethod enclosing type", type, publicBooleanMethod.getEnclosingType());
		Checker.falseValue("publicBooleanMethod static", publicBooleanMethod.isStatic());
		Checker.falseValue("publicBooleanMethod final", publicBooleanMethod.isFinal());
		Checker.falseValue("publicBooleanMethod native", publicBooleanMethod.isNative());

		final Type byteType = context.getByte();
		final Method protectedByteMethod = type.getMethod("protectedByteMethod", Arrays.asList(new Type[] { byteType }));

		Checker.same("protectedByteMethod visibility", Visibility.PROTECTED, protectedByteMethod.getVisibility());
		Checker.equals("protectedByteMethod name", "protectedByteMethod", protectedByteMethod.getName());
		Checker.same("protectedByteMethod returnType", byteType, protectedByteMethod.getReturnType());
		Checker.same("protectedByteMethod enclosing type", type, protectedByteMethod.getEnclosingType());
		Checker.falseValue("protectedByteMethod static", protectedByteMethod.isStatic());
		Checker.falseValue("protectedByteMethod final", protectedByteMethod.isFinal());
		Checker.falseValue("protectedByteMethod native", protectedByteMethod.isNative());

		final Type shortType = context.getShort();
		final Method packagePrivateShortMethod = type.getMethod("packagePrivateShortMethod", Arrays.asList(new Type[] { shortType }));

		Checker.same("packagePrivateShortMethod visibility", Visibility.PACKAGE_PRIVATE, packagePrivateShortMethod
				.getVisibility());
		Checker.equals("packagePrivateShortMethod name", "packagePrivateShortMethod", packagePrivateShortMethod.getName());
		Checker.same("packagePrivateShortMethod returnType", shortType, packagePrivateShortMethod.getReturnType());
		Checker.same("packagePrivateShortMethod enclosing type", type, packagePrivateShortMethod.getEnclosingType());
		Checker.falseValue("packagePrivateShortMethod static", packagePrivateShortMethod.isStatic());
		Checker.falseValue("packagePrivateShortMethod final", packagePrivateShortMethod.isFinal());
		Checker.falseValue("protectedByteMethod native", protectedByteMethod.isNative());

		final Type intType = context.getInt();
		final Method privateIntMethod = type.getMethod("privateIntMethod", Arrays.asList(new Type[] { intType }));

		Checker.same("privateIntMethod visibility", Visibility.PRIVATE, privateIntMethod.getVisibility());
		Checker.equals("privateIntMethod name", "privateIntMethod", privateIntMethod.getName());
		Checker.same("privateIntMethod returnType", intType, privateIntMethod.getReturnType());
		Checker.same("privateIntMethod enclosing type", type, privateIntMethod.getEnclosingType());
		Checker.falseValue("privateIntMethod static", privateIntMethod.isStatic());
		Checker.falseValue("privateIntMethod final", privateIntMethod.isFinal());
		Checker.falseValue("privateIntMethod native", privateIntMethod.isNative());

		final Type voidType = context.getVoid();
		final Method staticFinalMethod = type.getMethod("staticFinalMethod", Arrays.asList(new Type[] {}));

		Checker.same("staticFinalMethod visibility", Visibility.PUBLIC, staticFinalMethod.getVisibility());
		Checker.equals("staticFinalMethod name", "staticFinalMethod", staticFinalMethod.getName());
		Checker.same("staticFinalMethod returnType", voidType, staticFinalMethod.getReturnType());
		Checker.same("staticFinalMethod enclosing type", type, staticFinalMethod.getEnclosingType());
		Checker.trueValue("staticFinalMethod static", staticFinalMethod.isStatic());
		Checker.trueValue("staticFinalMethod final", staticFinalMethod.isFinal());
		Checker.falseValue("staticFinalMethod native", staticFinalMethod.isNative());

		final Set thrownTypes = staticFinalMethod.getThrownTypes();
		Checker.equals("staticFinalMethod thrownTypes", 1, thrownTypes.size());
		Checker.same("staticFinalMethod thrownType", context.getType(Exception.class.getName()), thrownTypes.iterator().next());

		return null;
	}
}
