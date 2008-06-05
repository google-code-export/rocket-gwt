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
import rocket.generator.rebind.constructorparameter.AbstractConstructorParameter;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * A bridge between a java Constructor parameter and a ConstructorParameter
 * 
 * @author Miroslav Pokorny
 */
public class JavaConstructorParameterAdapter extends AbstractConstructorParameter implements ConstructorParameter {

	@SuppressWarnings("unchecked")
	protected Set<Type> createThrownTypes() {
		return JavaAdapterHelper.asSetOfTypes(this.getGeneratorContext(), this.getJavaConstructor().getExceptionTypes());
	}

	@Override
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
	 * The enclosing java constructor.
	 */
	protected java.lang.reflect.Constructor javaConstructor;

	protected java.lang.reflect.Constructor getJavaConstructor() {
		Checker.notNull("field:class", javaConstructor);
		return this.javaConstructor;
	}

	protected void setJavaConstructor(final java.lang.reflect.Constructor javaConstructor) {
		Checker.notNull("parameter:javaConstructor", javaConstructor);
		this.javaConstructor = javaConstructor;
	}
}
