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
package rocket.generator.rebind.java;

import java.util.Set;

import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.methodparameter.AbstractMethodParameter;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * A bridge between a java method and a Method.
 * 
 * @author Miroslav Pokorny
 */
public class JavaMethodParameterAdapter extends AbstractMethodParameter implements MethodParameter {
	protected Set<Type> createThrownTypes() { 
		final JavaGeneratorContext context = (JavaGeneratorContext) this.getGeneratorContext();
		return context.asTypes(this.getJavaMethod().getExceptionTypes());
	}

	public void setType(final Type type) {
		super.setType(type);
	}

	private String name;

	public String getName() {
		GeneratorHelper.checkJavaVariableName("field:name", name);
		return this.name;
	}

	public void setName(final String name) {
		GeneratorHelper.checkJavaVariableName("parameter:name", name);
		this.name = name;
	}

	/**
	 * The enclosing java method.
	 */
	protected java.lang.reflect.Method javaMethod;

	protected java.lang.reflect.Method getJavaMethod() {
		Checker.notNull("field:class", javaMethod);
		return this.javaMethod;
	}

	protected void setJavaMethod(final java.lang.reflect.Method javaMethod) {
		Checker.notNull("parameter:javaMethod", javaMethod);
		this.javaMethod = javaMethod;
	}

}
