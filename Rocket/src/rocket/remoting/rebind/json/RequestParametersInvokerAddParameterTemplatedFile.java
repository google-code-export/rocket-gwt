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
package rocket.remoting.rebind.json;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * An abstraction for the request-parameters-transport-invoker-add-parameter
 * template
 * 
 * @author Miroslav Pokorny
 */
class RequestParametersInvokerAddParameterTemplatedFile extends TemplatedCodeBlock {

	public RequestParametersInvokerAddParameterTemplatedFile() {
		super();
		setNative(false);
	}

	private String httpRequestParameterName;

	protected String getHttpRequestParameterName() {
		StringHelper.checkNotEmpty("httpRequestParameterName:httpRequestParameterName", httpRequestParameterName);
		return this.httpRequestParameterName;
	}

	public void setHttpRequestParameterName(final String httpRequestParameterName) {
		StringHelper.checkNotEmpty("parameter:httpRequestParameterName", httpRequestParameterName);
		this.httpRequestParameterName = httpRequestParameterName;
	}

	private MethodParameter parameter;

	protected MethodParameter getParameter() {
		ObjectHelper.checkNotNull("field:parameter", parameter);
		return this.parameter;
	}

	public void setParameter(final MethodParameter parameter) {
		ObjectHelper.checkNotNull("parameter:parameter", parameter);
		this.parameter = parameter;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.REQUEST_PARAMETERS_INVOKER_ADD_PARAMETER_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.REQUEST_PARAMETERS_INVOKER_ADD_PARAMETER_HTTP_REQUEST_PARAMETER_NAME.equals(name)) {
				value = new StringLiteral(this.getHttpRequestParameterName());
				break;
			}
			if (Constants.REQUEST_PARAMETERS_INVOKER_ADD_PARAMETER_PARAMETER.equals(name)) {
				value = this.getParameter();
				break;
			}
			break;
		}
		return value;
	}
}
