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
import rocket.generator.rebind.constructor.NewConstructorImpl;
import rocket.generator.rebind.constructorparameter.NewConstructorParameterImpl;
import rocket.generator.rebind.field.NewFieldImpl;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;

public class NewConcreteClassWithConstructorGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final NewConcreteType newType = context.newConcreteType( newTypeName );
		newType.setName(newTypeName);
		newType.setSuperType(type);
		newType.setVisibility( Visibility.PUBLIC );

		final Type exception = context.getType(Exception.class.getName());

		final NewConstructorImpl noArgumentsConstructor = new NewConstructorImpl();
		noArgumentsConstructor.setVisibility(Visibility.PUBLIC);
		noArgumentsConstructor.setGeneratorContext(context);
		noArgumentsConstructor.setEnclosingType(newType);
		noArgumentsConstructor.setBody(EmptyCodeBlock.INSTANCE);
		noArgumentsConstructor.addThrownType(exception);
		newType.addConstructor(noArgumentsConstructor);

		final NewConstructorImpl booleanParameterConstructor = new NewConstructorImpl();
		booleanParameterConstructor.setVisibility(Visibility.PUBLIC);
		booleanParameterConstructor.setGeneratorContext(context);
		booleanParameterConstructor.setEnclosingType(newType);
		booleanParameterConstructor.setBody(EmptyCodeBlock.INSTANCE);
		booleanParameterConstructor.addThrownType(exception);

		final NewConstructorParameterImpl booleanParameter = new NewConstructorParameterImpl();
		booleanParameter.setGeneratorContext(context);
		booleanParameter.setFinal(false);
		booleanParameter.setName("booleanParameter");
		booleanParameter.setType(context.getType(Boolean.TYPE.getName()));

		booleanParameterConstructor.addParameter(booleanParameter);

		newType.addConstructor(booleanParameterConstructor);

		final NewFieldImpl instanceField = new NewFieldImpl();
		instanceField.setGeneratorContext(context);
		instanceField.setEnclosingType(newType);
		instanceField.setName("instanceField");
		instanceField.setStatic(false);
		instanceField.setTransient(false);
		instanceField.setType(context.getString());
		instanceField.setValue(EmptyCodeBlock.INSTANCE);
		instanceField.setVisibility(Visibility.PRIVATE);

		newType.addField(instanceField);

		final NewFieldImpl staticField = new NewFieldImpl();
		staticField.setGeneratorContext(context);
		staticField.setEnclosingType(newType);
		staticField.setName("staticField");
		staticField.setStatic(true);
		staticField.setTransient(false);
		staticField.setType(context.getString());
		staticField.setValue(EmptyCodeBlock.INSTANCE);
		staticField.setVisibility(Visibility.PRIVATE);

		newType.addField(staticField);

		return newType;
	}

}
