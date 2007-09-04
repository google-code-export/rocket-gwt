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
package rocket.generator.rebind.constructorparameter;

import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.util.AbstractParameter;
import rocket.util.client.ObjectHelper;

/**
 * Convenient base for any parameter implementation.
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractConstructorParameter extends AbstractParameter implements ConstructorParameter {

	/**
	 * The enclosingConstructor that parameter belongs too.
	 */
	private Constructor enclosingConstructor;

	public Constructor getEnclosingConstructor() {
		ObjectHelper.checkNotNull("field:enclosingConstructor", enclosingConstructor);
		return enclosingConstructor;
	}

	protected boolean hasEnclosingConstructor() {
		return null != this.enclosingConstructor;
	}

	protected void setEnclosingConstructor(final Constructor enclosingConstructor) {
		ObjectHelper.checkNotNull("field:enclosingConstructor", enclosingConstructor);
		this.enclosingConstructor = enclosingConstructor;
	}

	public int getIndex() {
		return this.getEnclosingConstructor().getParameters().indexOf(this);
	}

	public NewConstructorParameter copy() {
		final NewConstructorParameterImpl parameter = new NewConstructorParameterImpl();
		parameter.setGeneratorContext(this.getGeneratorContext());
		parameter.setFinal(this.isFinal());
		parameter.setName(this.getName());
		parameter.setType(this.getType());
		return parameter;
	}

	public String toString() {
		return "NewConstructorParameterImpl " + super.toString();
	}
}
