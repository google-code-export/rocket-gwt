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
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewNestedType;
import rocket.generator.rebind.type.Type;
import rocket.generator.test.generator.client.ConcreteClass;

public class NewNestedClassGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final NewConcreteType newType = context.newConcreteType();
		newType.setName(newTypeName);
		newType.setSuperType(type);

		final String concreteClassClassname = "ConcreteClass1";

		final NewMethod getNested = newType.newMethod();

		final Type concreteClass = context.getType(ConcreteClass.class.getName());

		getNested.setBody(new StringCodeBlock("return new " + concreteClassClassname + "();"));
		getNested.setFinal(false);
		getNested.setName("getNested");
		getNested.setVisibility(Visibility.PUBLIC);
		getNested.setReturnType(concreteClass);
		getNested.setStatic(false);

		final NewNestedType nested = newType.newNestedType();
		nested.setAbstract(false);
		nested.setFinal(false);
		nested.setName(concreteClassClassname);
		nested.setStatic(true);
		nested.setSuperType(concreteClass);

		return newType;
	}
}
