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
package rocket.generator.test.templatedfilecodeblock.rebind;

import java.util.Arrays;
import java.util.List;

import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;

public class MethodParameterPlaceHolderGenerator extends AbstractTemplatedFileCodeBlockGenerator {

	protected String getTemplateFilename() {
		return "MethodParameterPlaceHolderGenerator.txt";
	}

	protected String getNewMethodName() {
		return "returnParameter";
	}

	protected boolean isNewMethodNative() {
		return false;
	}

	protected void postNewMethodCreate(final NewMethod method) {
		final Method overridden = this.getType().getMethod(method.getName(), this.getObjectType());
		final MethodParameter parameter = (MethodParameter) overridden.getParameters().get(0);
		method.addParameter(parameter.copy());
	}

	protected void visitTemplate(final Template template) {
		final Type type = this.getType();
		final Method method = type.getMethod(getNewMethodName(), this.getObjectType());
		final MethodParameter parameter = (MethodParameter) method.getParameters().get(0);
		template.set("parameter", parameter);
	}

	protected String getNewMethodReturnType() {
		return Object.class.getName();
	}

	protected List getObjectType() {
		final Type object = this.getGeneratorContext().getObject();
		return Arrays.asList(new Type[] { object });
	}
}
