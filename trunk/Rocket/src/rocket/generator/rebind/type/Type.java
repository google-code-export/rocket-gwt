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

import java.util.List;
import java.util.Set;

import rocket.generator.rebind.ClassComponent;
import rocket.generator.rebind.HasMetadata;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.packagee.Package;

/**
 * Types represent a type during compilation.
 * 
 * @author Miroslav Pokorny
 */
public interface Type extends HasMetadata, ClassComponent {

	Visibility getVisibility();

	String getName();

	String getSimpleName();

	String getJsniNotation();

	String getRuntimeName();
	
	Package getPackage();

	Set getInterfaces();

	Type getSuperType();

	Set getSubTypes();

	Set getConstructors();

	Constructor findConstructor(List parameterTypes);

	Constructor getConstructor(List parameterTypes);

	boolean hasNoArgumentsConstructor();

	Set getMethods();

	Method findMethod(String methodName, List parameterTypes);

	Method getMethod(String methodName, List parameterTypes);

	Method findMostDerivedMethod(String methodName, List parameterTypes);

	Method getMostDerivedMethod(String methodName, List parameterTypes);

	Set getFields();

	Field findField(String name);

	Field getField(String name);

	boolean isAssignableTo(Type type);

	boolean isAssignableFrom(Type type);

	boolean isPrimitive();

	boolean isArray();

	Type getComponentType();

	boolean isFinal();

	boolean isAbstract();

	boolean isInterface();

	/**
	 * If type is a primitive returns the corresponding wrapper type.
	 * 
	 * @return For non primitives will return null.
	 */
	Type getWrapper();

	Set getNestedTypes();
}
