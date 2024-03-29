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
package rocket.generator.rebind.gwt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.AbstractMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.util.Parameter;
import rocket.util.client.Checker;

import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * An adapter between a JMethod and a Method
 * 
 * @author Miroslav Pokorny
 */
public class JMethodMethodAdapter extends AbstractMethod {

	public boolean isAbstract() {
		return this.getJMethod().isAbstract();
	}

	public boolean isFinal() {
		return this.getJMethod().isFinal();
	}

	public boolean isStatic() {
		return this.getJMethod().isStatic();
	}

	public boolean isNative() {
		return this.getJMethod().isNative();
	}

	public Visibility getVisibility() {
		if (false == this.hasVisibility()) {
			this.setVisibility(this.createVisibility());
		}
		return super.getVisibility();
	}

	protected Visibility createVisibility() {
		Visibility visibility = null;
		while (true) {
			final JMethod method = this.getJMethod();
			if (method.isPrivate()) {
				visibility = Visibility.PRIVATE;
				break;
			}
			if (method.isDefaultAccess()) {
				visibility = Visibility.PACKAGE_PRIVATE;
				break;
			}
			if (method.isProtected()) {
				visibility = Visibility.PROTECTED;
				break;
			}
			if (method.isPublic()) {
				visibility = Visibility.PUBLIC;
				break;
			}
			Checker.fail("Unknown visibility for field " + method);
		}
		return visibility;
	}

	public String getName() {
		return this.getJMethod().getName();
	}

	public String getJsniNotation() {
		final StringBuilder jsni = new StringBuilder();
		jsni.append('@');
		jsni.append(this.getEnclosingType().getName());
		jsni.append("::");
		jsni.append(this.getName());
		jsni.append('(');

		final Iterator<MethodParameter> parameters = this.getParameters().iterator();
		while (parameters.hasNext()) {
			final Parameter parameter = parameters.next();
			jsni.append(parameter.getJsniNotation());
		}

		jsni.append(')');

		return jsni.toString();
	}

	public List<MethodParameter> getParameters() {
		if (false == this.hasParameters()) {
			this.setParameters(this.createParameters());
		}
		return super.getParameters();
	}

	protected List<MethodParameter> createParameters() {
		final List<MethodParameter> list = new ArrayList<MethodParameter>();

		final JParameter[] parameters = this.getJMethod().getParameters();
		for (int i = 0; i < parameters.length; i++) {
			list.add(this.createParameter(parameters[i]));
		}

		return list;
	}

	/**
	 * Factory parameter which creates a
	 * {@link JParameterConstructorParameterAdapter} from a {@link JParameter}.
	 * 
	 * @param parameter
	 *            The source JParameter
	 * @return
	 */
	protected MethodParameter createParameter(final JParameter parameter) {
		final JParameterMethodParameterAdapter adapter = new JParameterMethodParameterAdapter();
		adapter.setGeneratorContext(this.getGeneratorContext());
		adapter.setEnclosingMethod(this);
		adapter.setJParameter(parameter);
		return adapter;
	}

	@Override
	public Type getReturnType() {
		if (false == this.hasReturnType()) {
			this.setReturnType(this.createReturnType());
		}
		return super.getReturnType();
	}

	public Type createReturnType() {
		final JType returnType = this.getJMethod().getReturnType();
		return this.getTypeOracleGeneratorContext().getType( returnType );
	}

	protected Set<Type> createThrownTypes() { 
		return this.getTypeOracleGeneratorContext().asTypes(this.getJMethod().getThrows());
	}

	public List<String> getMetadataValues(final String name) {
		return this.getAnnotationValues(this.getJMethod(), name);
	}

	/**
	 * The JMethod which provides all method info.
	 */
	private JMethod jMethod;

	protected JMethod getJMethod() {
		Checker.notNull("field:jMethod", jMethod);
		return jMethod;
	}

	public void setJMethod(final JMethod jMethod) {
		Checker.notNull("parameter:jMethod", jMethod);
		this.jMethod = jMethod;
	}
	
	protected TypeOracleGeneratorContext getTypeOracleGeneratorContext(){
		return (TypeOracleGeneratorContext) this.getGeneratorContext();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Method: ");

		final JMethod jMethod = this.jMethod;
		if (null != jMethod) {
			builder.append(this.jMethod.getEnclosingType());
			builder.append(": ");
			builder.append(this.jMethod.getReadableDeclaration());
		} else {
			builder.append(this.jMethod);
		}

		return builder.toString();
	}
}
