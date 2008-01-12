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
package rocket.remoting.rebind.rpc.json.requestparameters;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.util.client.Checker;

/**
 * An abstraction for the add-parameter template
 * 
 * @author Miroslav Pokorny
 */
class AddParameterTemplatedFile extends TemplatedFileCodeBlock {

	public AddParameterTemplatedFile() {
		super();
	}

	/**
	 * The source method parameter
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

	/**
	 * The target request parameter name
	 */
	private String httpRequestParameterName;

	protected String getHttpRequestParameterName() {
		Checker.notEmpty("httpRequestParameterName:httpRequestParameterName", httpRequestParameterName);
		return this.httpRequestParameterName;
	}

	public void setHttpRequestParameterName(final String httpRequestParameterName) {
		Checker.notEmpty("parameter:httpRequestParameterName", httpRequestParameterName);
		this.httpRequestParameterName = httpRequestParameterName;
	}

	protected String getResourceName(){
		return RequestParametersConstants.ADD_PARAMETER_TEMPLATE;
	}
	
	public InputStream getInputStream(){
		return super.getInputStream();
	}
	
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (RequestParametersConstants.ADD_PARAMETER_HTTP_REQUEST_PARAMETER_NAME.equals(name)) {
				value = new StringLiteral(this.getHttpRequestParameterName());
				break;
			}
			if (RequestParametersConstants.ADD_PARAMETER_PARAMETER.equals(name)) {
				value = this.getParameter();
				break;
			}
			break;
		}
		return value;
	}
}
