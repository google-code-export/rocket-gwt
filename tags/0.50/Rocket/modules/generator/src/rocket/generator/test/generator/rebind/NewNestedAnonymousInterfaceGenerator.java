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
import rocket.generator.rebind.codeblock.CodeBlockList;
import rocket.generator.rebind.codeblock.StringCodeBlock;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.type.NewAnonymousNestedType;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.test.generator.client.Interface;

public class NewNestedAnonymousInterfaceGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final NewConcreteType newType = context.newConcreteType( newTypeName );
		newType.setSuperType(type);
		newType.setVisibility( Visibility.PUBLIC );

		final Type interfaceType = context.getType(Interface.class.getName());

		final CodeBlockList body = new CodeBlockList();

		body.add(new StringCodeBlock("return new "));

		final NewAnonymousNestedType nested = newType.newAnonymousNestedType();
		nested.addInterface(interfaceType);
		body.add(nested);

		body.add(new StringCodeBlock(";"));

		final NewMethod getNested = newType.newMethod();
		getNested.setBody(body);
		getNested.setFinal(false);
		getNested.setName("getNested");
		getNested.setVisibility(Visibility.PUBLIC);
		getNested.setReturnType(interfaceType);
		getNested.setStatic(false);

		return newType;
	}
}
