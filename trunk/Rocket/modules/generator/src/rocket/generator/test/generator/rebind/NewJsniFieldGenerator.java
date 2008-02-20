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
import rocket.generator.rebind.codeblock.StringCodeBlock;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;

public class NewJsniFieldGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final NewConcreteType newType = context.newConcreteType( newTypeName );
		newType.setSuperType(type);
		newType.setVisibility( Visibility.PUBLIC );

		final Type stringType = context.getString();
		final Type voidType = context.getVoid();

		// create a String field called "field"
		final NewField field = newType.newField();
		field.setName("field");
		field.setStatic(false);
		field.setTransient(false);
		field.setType(stringType);
		field.setValue(EmptyCodeBlock.INSTANCE);
		field.setVisibility(Visibility.PRIVATE);

		// create a getter that uses jsni to return the field "field"
		final NewMethod getterMethod = newType.newMethod();

		getterMethod.setBody(new StringCodeBlock("return this." + field.getJsniNotation() + ";"));
		getterMethod.setFinal(false);
		getterMethod.setName("getField");
		getterMethod.setNative(true);
		getterMethod.setVisibility(Visibility.PUBLIC);
		getterMethod.setReturnType(stringType);
		getterMethod.setStatic(false);

		// create a setter that uses jsni to return the field "field"
		final NewMethod setterMethod = newType.newMethod();

		final NewMethodParameter parameter = setterMethod.newParameter();
		parameter.setFinal(false);
		parameter.setName("newValue");
		parameter.setType(stringType);

		setterMethod.setBody(new StringCodeBlock("this." + field.getJsniNotation() + "=" + parameter.getName() + ";"));
		setterMethod.setEnclosingType(newType);
		setterMethod.setFinal(false);
		setterMethod.setName("setField");
		setterMethod.setNative(true);
		setterMethod.setReturnType(voidType);
		setterMethod.setStatic(false);
		setterMethod.setVisibility(Visibility.PUBLIC);

		return newType;
	}
}
