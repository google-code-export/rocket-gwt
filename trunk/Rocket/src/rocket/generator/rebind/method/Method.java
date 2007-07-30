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
package rocket.generator.rebind.method;

import java.util.List;
import java.util.Set;

import rocket.generator.rebind.ClassComponent;
import rocket.generator.rebind.HasMetadata;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.type.NewType;
import rocket.generator.rebind.type.Type;

/**
 * Each method represents a method belonging to a type.
 * 
 * @author Miroslav Pokorny
 */
public interface Method extends ClassComponent, HasMetadata {

	String getName();

	String getJsniNotation();

	Type getReturnType();

	boolean returnsVoid();
	
	List getParameters();

	Visibility getVisibility();

	Type getEnclosingType();

	boolean isFinal();

	boolean isStatic();

	boolean isAbstract();

	boolean isNative();

	Set getThrownTypes();

	Method findOverriddenMethod();

	/**
	 * Exactly the same as {@link #findOverriddenMethod()} except a
	 * MethodNotFoundException is thrown if no method was found.
	 * 
	 * @return Always returns a Method.
	 */
	Method getOverriddenMethod();

	/**
	 * Copies this method into a NewMethod. The body of the new method is not
	 * copied.
	 * 
	 * @param the
	 *            type this method will belong too.
	 * @return
	 */
	NewMethod copy(NewType enclosingType);
}
