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

import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.AbstractMethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

import com.google.gwt.core.ext.typeinfo.JParameter;

/**
 * Represents a parameter acting as an adapter between a JParameter and
 * ConstructorParameter.
 * 
 * @author Miroslav Pokorny
 */
public class JParameterMethodParameterAdapter extends AbstractMethodParameter {

	public String getName() {
		return this.getJParameter().getName();
	}

	public String getJsniNotation() {
		return this.getType().getJsniNotation();
	}

	public Type getType() {
		if (false == this.hasType()) {
			this.setType(this.createType());
		}

		return super.getType();
	}

	protected Type createType() {
		return this.findType(this.getJParameter().getType().getQualifiedSourceName());
	}

	public void setEnclosingMethod(final Method method) {
		super.setEnclosingMethod(method);
	}

	/**
	 * The JParameter which provides all type parameter info.
	 */
	private JParameter jParameter;

	protected JParameter getJParameter() {
		Checker.notNull("field:jParameter", jParameter);
		return jParameter;
	}

	public void setJParameter(final JParameter jParameter) {
		Checker.notNull("parameter:jParameter", jParameter);
		this.jParameter = jParameter;
	}

	public String toString() {
		return null == jParameter ? super.toString() : this.jParameter.toString();
	}
}
