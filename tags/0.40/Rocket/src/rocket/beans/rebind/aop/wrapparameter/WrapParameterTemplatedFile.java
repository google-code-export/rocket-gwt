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
package rocket.beans.rebind.aop.wrapparameter;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.util.client.Checker;

/**
 * An abstraction for the wrap parameter template
 * 
 * @author Miroslav Pokorny
 */
public class WrapParameterTemplatedFile extends TemplatedFileCodeBlock {

	public WrapParameterTemplatedFile() {
		super();
	}

	/**
	 * The method parameter being wrapped.
	 */
	private MethodParameter parameter;

	protected MethodParameter getParameter() {
		Checker.notNull("field:parameter", parameter);
		return this.parameter;
	}

	public void setParameter(final MethodParameter parameter) {
		Checker.notNull("parameter:parameter", parameter);
		this.parameter = parameter;
	}

	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	public InputStream getInputStream(){
		return super.getInputStream(); // TODO Delete when merged into same package as parent template.
	}

	public Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.PARAMETER.equals(name)) {
				value = this.getParameter();
				break;
			}
			break;
		}
		return value;
	}
}