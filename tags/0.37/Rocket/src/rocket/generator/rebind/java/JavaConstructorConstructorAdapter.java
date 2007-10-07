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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.AbstractConstructor;
import rocket.generator.rebind.constructor.Constructor;
import rocket.util.client.ObjectHelper;

/**
 * An adapter between a java constructor parameter and a Constructor
 * 
 * @author Miroslav Pokorny
 */
public class JavaConstructorConstructorAdapter extends AbstractConstructor implements Constructor {

	public Visibility getVisibility() {
		if (false == this.hasVisibility()) {
			this.setVisibility(this.createVisibility());
		}
		return super.getVisibility();
	}

	protected Visibility createVisibility() {
		return JavaAdapterHelper.getVisibility(this.getJavaConstructor().getModifiers());
	}

	protected List createParameters() {
		final GeneratorContext context = this.getGeneratorContext();
		final List parameters = new ArrayList();

		final Class[] parameterTypes = this.getJavaConstructor().getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			final JavaConstructorParameterAdapter parameter = new JavaConstructorParameterAdapter();
			parameter.setGeneratorContext(this.getGeneratorContext());
			parameter.setType(context.getType(parameterTypes[i].getName()));
			parameter.setName("parameter" + i);

			parameters.add(parameter);
		}

		return parameters;
	}

	protected Set createThrownTypes() {
		return JavaAdapterHelper.asSetOfTypes(this.getGeneratorContext(), this.getJavaConstructor().getExceptionTypes());
	}

	private java.lang.reflect.Constructor javaConstructor;

	public java.lang.reflect.Constructor getJavaConstructor() {
		ObjectHelper.checkNotNull("field:javaConstructor", javaConstructor);
		return this.javaConstructor;
	}

	public void setJavaConstructor(final java.lang.reflect.Constructor javaConstructor) {
		ObjectHelper.checkNotNull("parameter:javaConstructor", javaConstructor);
		this.javaConstructor = javaConstructor;
	}

	public String toString() {
		return "Constructor: " + this.javaConstructor;
	}
}
