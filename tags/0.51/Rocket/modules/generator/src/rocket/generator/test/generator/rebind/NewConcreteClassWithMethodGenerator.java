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

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.StringCodeBlock;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;

public class NewConcreteClassWithMethodGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final NewConcreteType newType = context.newConcreteType(newTypeName);
		newType.setSuperType(type);
		newType.setVisibility(Visibility.PUBLIC);

		final NewMethod instanceMethod = newType.newMethod();

		instanceMethod.setBody(new StringCodeBlock("return a+b;"));
		instanceMethod.setFinal(false);
		instanceMethod.setName("add");
		instanceMethod.setVisibility(Visibility.PUBLIC);
		instanceMethod.setReturnType(context.getType(Integer.TYPE.getName()));
		instanceMethod.setStatic(false);

		final Type intType = context.getInt();

		final NewMethodParameter first = instanceMethod.newParameter();
		first.setFinal(false);
		first.setName("a");
		first.setType(intType);

		final NewMethodParameter second = instanceMethod.newParameter();
		second.setFinal(false);
		second.setName("b");
		second.setType(intType);

		return newType;
	}
}
