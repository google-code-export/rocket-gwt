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
package rocket.generator.test.generator.rebind;

import java.util.ArrayList;
import java.util.List;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.StringCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;

public class NewJsniMethodGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type superType, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final Type intType = context.getInt();

		final List parameterTypes = new ArrayList();
		parameterTypes.add(intType);
		parameterTypes.add(intType);

		final Method method = superType.getMethod("add0", parameterTypes);

		final NewConcreteType newType = context.newConcreteType();
		newType.setName(newTypeName);
		newType.setSuperType(superType);

		final NewMethod newMethod = newType.newMethod();

		final NewMethodParameter firstParameter = newMethod.newParameter();
		firstParameter.setFinal(false);
		firstParameter.setName("parameter0");
		firstParameter.setType(intType);

		final NewMethodParameter secondParameter = newMethod.newParameter();
		secondParameter.setFinal(false);
		secondParameter.setName("parameter1");
		secondParameter.setType(intType);

		newMethod.setAbstract(false);

		newMethod.setBody(new StringCodeBlock("return this." + method.getJsniNotation() + "( " + firstParameter.getName() + ","
				+ secondParameter.getName() + ");"));

		newMethod.setFinal(false);
		newMethod.setName("add");
		newMethod.setNative(true);
		newMethod.setReturnType(intType);
		newMethod.setStatic(false);
		newMethod.setVisibility(Visibility.PUBLIC);

		return newType;
	}
}
