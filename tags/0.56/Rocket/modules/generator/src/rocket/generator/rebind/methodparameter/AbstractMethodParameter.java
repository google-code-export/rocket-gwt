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
package rocket.generator.rebind.methodparameter;

import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.util.AbstractParameter;
import rocket.util.client.Checker;

/**
 * Convenient base for any parameter implementation.
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractMethodParameter extends AbstractParameter implements MethodParameter {

	/**
	 * The enclosingMethod that parameter belongs too.
	 */
	private Method enclosingMethod;

	public Method getEnclosingMethod() {
		Checker.notNull("field:enclosingMethod", enclosingMethod);
		return enclosingMethod;
	}

	protected boolean hasEnclosingMethod() {
		return null != this.enclosingMethod;
	}

	protected void setEnclosingMethod(final Method enclosingMethod) {
		Checker.notNull("field:enclosingMethod", enclosingMethod);
		this.enclosingMethod = enclosingMethod;
	}

	public int getIndex() {
		return this.getEnclosingMethod().getParameters().indexOf(this);
	}

	public NewMethodParameter copy() {
		final NewMethodParameterImpl parameter = new NewMethodParameterImpl();
		parameter.setGeneratorContext(this.getGeneratorContext());
		parameter.setFinal(this.isFinal());
		parameter.setName(this.getName());
		parameter.setType(this.getType());
		return parameter;
	}
}
