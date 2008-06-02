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
import rocket.generator.rebind.initializer.Initializer;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;

public class NewConstructorWithInitializerGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type superType, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final NewConcreteType newType = context.newConcreteType(newTypeName);
		newType.setSuperType(superType);
		newType.setVisibility(Visibility.PUBLIC);

		final Initializer staticInitializer = newType.newInitializer();
		staticInitializer.setBody(new StringCodeBlock(superType.getName() + ".staticInitializerRun = true;"));
		staticInitializer.setStatic(true);

		final Initializer instanceInitializer = newType.newInitializer();
		instanceInitializer.setBody(new StringCodeBlock(superType.getName() + ".instanceInitializerRun = true;"));
		instanceInitializer.setStatic(false);

		return newType;
	}
}
