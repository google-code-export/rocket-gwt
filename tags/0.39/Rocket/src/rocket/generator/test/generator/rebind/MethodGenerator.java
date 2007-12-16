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
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

public class MethodGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final String typeName, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final Type type = context.getType(MethodTest.class.getName());

		PrimitiveHelper.checkEquals("method count", 5, type.getMethods().size());

		final Type booleanType = context.getBoolean();
		final Method publicBooleanMethod = type.getMethod("publicBooleanMethod", Arrays.asList(new Type[] { booleanType }));

		ObjectHelper.checkSame("publicBooleanMethod visibility", Visibility.PUBLIC, publicBooleanMethod.getVisibility());
		ObjectHelper.checkEquals("publicBooleanMethod name", "publicBooleanMethod", publicBooleanMethod.getName());
		ObjectHelper.checkSame("publicBooleanMethod returnType", booleanType, publicBooleanMethod.getReturnType());
		ObjectHelper.checkSame("publicBooleanMethod enclosing type", type, publicBooleanMethod.getEnclosingType());
		PrimitiveHelper.checkFalse("publicBooleanMethod static", publicBooleanMethod.isStatic());
		PrimitiveHelper.checkFalse("publicBooleanMethod final", publicBooleanMethod.isFinal());
		PrimitiveHelper.checkFalse("publicBooleanMethod native", publicBooleanMethod.isNative());

		final Type byteType = context.getByte();
		final Method protectedByteMethod = type.getMethod("protectedByteMethod", Arrays.asList(new Type[] { byteType }));

		ObjectHelper.checkSame("protectedByteMethod visibility", Visibility.PROTECTED, protectedByteMethod.getVisibility());
		ObjectHelper.checkEquals("protectedByteMethod name", "protectedByteMethod", protectedByteMethod.getName());
		ObjectHelper.checkSame("protectedByteMethod returnType", byteType, protectedByteMethod.getReturnType());
		ObjectHelper.checkSame("protectedByteMethod enclosing type", type, protectedByteMethod.getEnclosingType());
		PrimitiveHelper.checkFalse("protectedByteMethod static", protectedByteMethod.isStatic());
		PrimitiveHelper.checkFalse("protectedByteMethod final", protectedByteMethod.isFinal());
		PrimitiveHelper.checkFalse("protectedByteMethod native", protectedByteMethod.isNative());

		final Type shortType = context.getShort();
		final Method packagePrivateShortMethod = type.getMethod("packagePrivateShortMethod", Arrays.asList(new Type[] { shortType }));

		ObjectHelper.checkSame("packagePrivateShortMethod visibility", Visibility.PACKAGE_PRIVATE, packagePrivateShortMethod
				.getVisibility());
		ObjectHelper.checkEquals("packagePrivateShortMethod name", "packagePrivateShortMethod", packagePrivateShortMethod.getName());
		ObjectHelper.checkSame("packagePrivateShortMethod returnType", shortType, packagePrivateShortMethod.getReturnType());
		ObjectHelper.checkSame("packagePrivateShortMethod enclosing type", type, packagePrivateShortMethod.getEnclosingType());
		PrimitiveHelper.checkFalse("packagePrivateShortMethod static", packagePrivateShortMethod.isStatic());
		PrimitiveHelper.checkFalse("packagePrivateShortMethod final", packagePrivateShortMethod.isFinal());
		PrimitiveHelper.checkFalse("protectedByteMethod native", protectedByteMethod.isNative());

		final Type intType = context.getInt();
		final Method privateIntMethod = type.getMethod("privateIntMethod", Arrays.asList(new Type[] { intType }));

		ObjectHelper.checkSame("privateIntMethod visibility", Visibility.PRIVATE, privateIntMethod.getVisibility());
		ObjectHelper.checkEquals("privateIntMethod name", "privateIntMethod", privateIntMethod.getName());
		ObjectHelper.checkSame("privateIntMethod returnType", intType, privateIntMethod.getReturnType());
		ObjectHelper.checkSame("privateIntMethod enclosing type", type, privateIntMethod.getEnclosingType());
		PrimitiveHelper.checkFalse("privateIntMethod static", privateIntMethod.isStatic());
		PrimitiveHelper.checkFalse("privateIntMethod final", privateIntMethod.isFinal());
		PrimitiveHelper.checkFalse("privateIntMethod native", privateIntMethod.isNative());

		final Type voidType = context.getVoid();
		final Method staticFinalMethod = type.getMethod("staticFinalMethod", Arrays.asList(new Type[] {}));

		ObjectHelper.checkSame("staticFinalMethod visibility", Visibility.PUBLIC, staticFinalMethod.getVisibility());
		ObjectHelper.checkEquals("staticFinalMethod name", "staticFinalMethod", staticFinalMethod.getName());
		ObjectHelper.checkSame("staticFinalMethod returnType", voidType, staticFinalMethod.getReturnType());
		ObjectHelper.checkSame("staticFinalMethod enclosing type", type, staticFinalMethod.getEnclosingType());
		PrimitiveHelper.checkTrue("staticFinalMethod static", staticFinalMethod.isStatic());
		PrimitiveHelper.checkTrue("staticFinalMethod final", staticFinalMethod.isFinal());
		PrimitiveHelper.checkFalse("staticFinalMethod native", staticFinalMethod.isNative());

		final Set thrownTypes = staticFinalMethod.getThrownTypes();
		PrimitiveHelper.checkEquals("staticFinalMethod thrownTypes", 1, thrownTypes.size());
		ObjectHelper.checkSame("staticFinalMethod thrownType", context.getType(Exception.class.getName()), thrownTypes.iterator().next());

		return null;
	}
}
