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

import java.util.Collections;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.EmptyCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.constructor.NewConstructor;
import rocket.generator.rebind.type.Type;

public class ConstructorPlaceHolderGenerator extends AbstractTemplatedFileCodeBlockGenerator {

	protected String getTemplateFilename() {
		return "ConstructorPlaceHolderGenerator.txt";
	}

	protected String getNewMethodName() {
		return "newObject";
	}

	protected boolean isNewMethodNative() {
		return false;
	}

	protected void addNewConstructor() {
		final Type type = this.getType();
		final Constructor constructor = type.getConstructor(Collections.EMPTY_LIST);

		final NewConstructor newConstructor = constructor.copy(this.getNewType());
		newConstructor.setBody(EmptyCodeBlock.INSTANCE);
	}

	protected void visitTemplacedFileCodeBlock(final TemplatedFileCodeBlock template) {
		final GeneratorContext context = this.getGeneratorContext();

		final Type type = this.getNewType();
		final Constructor constructor = type.getConstructor(Collections.EMPTY_LIST);

		template.setConstructor("constructor", constructor);
	}

	protected String getNewMethodReturnType() {
		return Object.class.getName();
	}
}
