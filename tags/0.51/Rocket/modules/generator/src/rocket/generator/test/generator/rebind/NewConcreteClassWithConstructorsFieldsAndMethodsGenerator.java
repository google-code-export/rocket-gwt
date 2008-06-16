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
import rocket.generator.rebind.codeblock.EmptyCodeBlock;
import rocket.generator.rebind.constructor.NewConstructor;
import rocket.generator.rebind.constructorparameter.NewConstructorParameter;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;

public class NewConcreteClassWithConstructorsFieldsAndMethodsGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final NewConcreteType newType = context.newConcreteType(newTypeName);
		newType.setSuperType(type);
		newType.setVisibility(Visibility.PUBLIC);

		final Type booleanType = context.getBoolean();

		for (int i = 0; i < 10; i++) {
			final NewConstructor constructor = newType.newConstructor();
			constructor.setVisibility(Visibility.PUBLIC);
			constructor.setBody(EmptyCodeBlock.INSTANCE);

			for (int j = 0; j < i; j++) {
				final NewConstructorParameter parameter = constructor.newParameter();
				parameter.setFinal(false);
				parameter.setName("booleanParameter" + j);
				parameter.setType(booleanType);
			}
		}

		for (int i = 0; i < 10; i++) {
			final NewField field = newType.newField();
			field.setName("field" + i);
			field.setStatic(i % 1 == 0);
			field.setTransient(false);
			field.setType(booleanType);
			field.setValue(EmptyCodeBlock.INSTANCE);
			field.setVisibility(Visibility.PRIVATE);
		}

		final Type voidType = context.getVoid();
		for (int i = 0; i < 10; i++) {
			final NewMethod method = newType.newMethod();
			method.setBody(EmptyCodeBlock.INSTANCE);
			method.setFinal(false);
			method.setName("method" + i);
			method.setVisibility(Visibility.PUBLIC);
			method.setReturnType(voidType);
			method.setStatic(false);
		}

		return newType;
	}
}
