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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.AbstractMethod;
import rocket.generator.rebind.method.Method;
import rocket.util.client.ObjectHelper;

/**
 * An adapter between java methods and Methods
 * 
 * @author Miroslav Pokorny
 */
public class JavaMethodMethodAdapter extends AbstractMethod implements Method {

	public Visibility getVisibility() {
		if (false == this.hasVisibility()) {
			this.setVisibility(this.createVisibility());
		}
		return super.getVisibility();
	}

	public Visibility createVisibility() {
		return JavaAdapterHelper.getVisibility(this.getJavaMethod().getModifiers());
	}

	protected List createParameters() {
		final GeneratorContext context = this.getGeneratorContext();
		final List parameters = new ArrayList();
		final Class[] parameterTypes = this.getJavaMethod().getParameterTypes();
		final java.lang.reflect.Method javaMethod = this.getJavaMethod();

		for (int i = 0; i < parameterTypes.length; i++) {
			final Class parameterType = parameterTypes[i];

			final JavaMethodParameterAdapter adapter = new JavaMethodParameterAdapter();
			adapter.setGeneratorContext(context);
			adapter.setJavaMethod(javaMethod);
			adapter.setName("parameter" + i);
			adapter.setType(context.getType(parameterType.getName()));

			parameters.add(adapter);
		}
		return parameters;
	}

	protected Set createThrownTypes() {
		return JavaAdapterHelper.asSetOfTypes(this.getGeneratorContext(), this.getJavaMethod().getExceptionTypes());
	}

	public String getName() {
		return this.getJavaMethod().getName();
	}

	public boolean isAbstract() {
		return Modifier.isAbstract(this.getJavaMethod().getModifiers());
	}

	public boolean isFinal() {
		return Modifier.isFinal(this.getJavaMethod().getModifiers());
	}

	public boolean isNative() {
		return Modifier.isNative(this.getJavaMethod().getModifiers());
	}

	public boolean isStatic() {
		return Modifier.isStatic(this.getJavaMethod().getModifiers());
	}

	public List getMetadataValues(final String name) {
		return Collections.EMPTY_LIST;
	}

	private java.lang.reflect.Method javaMethod;

	public java.lang.reflect.Method getJavaMethod() {
		ObjectHelper.checkNotNull("field:javaMethod", javaMethod);
		return this.javaMethod;
	}

	public void setJavaMethod(final java.lang.reflect.Method javaMethod) {
		ObjectHelper.checkNotNull("parameter:javaMethod", javaMethod);
		this.javaMethod = javaMethod;
	}

	public String toString() {
		return "Method: " + this.javaMethod;
	}
}
