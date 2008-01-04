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

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.type.Type;
import rocket.generator.test.templatedfilecodeblock.client.ConstructorParameterPlaceHolder.NestedTypeWithByteParameter;

public class ConstructorParameterPlaceHolderGenerator extends AbstractTemplatedFileCodeBlockGenerator {

	final String NESTED_TYPE = NestedTypeWithByteParameter.class.getName().replace('$', '.');

	protected String getTemplateFilename() {
		return "ConstructorParameterPlaceHolderGenerator.txt";
	}

	protected String getNewMethodName() {
		return "newInstance";
	}

	protected boolean isNewMethodNative() {
		return false;
	}

	protected void visitTemplate(final Template template) {
		final GeneratorContext context = this.getGeneratorContext();

		final Type returnType = context.getType(NESTED_TYPE);
		template.set("type", returnType);

		final Constructor constructor = (Constructor) returnType.getConstructors().iterator().next();
		final ConstructorParameter parameter = (ConstructorParameter) constructor.getParameters().get(0);

		template.set("constructorParameter", parameter);
	}

	protected String getNewMethodReturnType() {
		return NESTED_TYPE;
	}

	private NewMethod newMethod;

	protected NewMethod getNewMethod() {
		return this.newMethod;
	}

	protected void setNewMethod(final NewMethod newMethod) {
		this.newMethod = newMethod;
	}
}
