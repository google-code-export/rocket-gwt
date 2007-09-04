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
import rocket.generator.rebind.codeblock.StringCodeBlock;
import rocket.generator.rebind.initializer.InitializerImpl;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;

public class NewConstructorWithInitializerGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type superType, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final NewConcreteType newType = context.newConcreteType();
		newType.setName(newTypeName);

		newType.setSuperType(superType);

		final InitializerImpl staticInitializer = new InitializerImpl();
		staticInitializer.setBody(new StringCodeBlock(superType.getName() + ".staticInitializerRun = true;"));
		staticInitializer.setGeneratorContext(context);
		staticInitializer.setStatic(true);
		newType.addInitializer(staticInitializer);

		final InitializerImpl instanceInitializer = new InitializerImpl();
		instanceInitializer.setBody(new StringCodeBlock(superType.getName() + ".instanceInitializerRun = true;"));
		instanceInitializer.setGeneratorContext(context);
		newType.addInitializer(instanceInitializer);

		return newType;
	}
}
