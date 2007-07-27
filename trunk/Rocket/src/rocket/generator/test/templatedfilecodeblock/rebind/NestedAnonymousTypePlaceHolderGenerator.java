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

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.type.NewAnonymousNestedType;
import rocket.generator.rebind.type.Type;
import rocket.generator.test.templatedfilecodeblock.client.Anonymous;

public class NestedAnonymousTypePlaceHolderGenerator extends AbstractTemplatedFileCodeBlockGenerator {

	protected String getTemplateFilename() {
		return "NestedAnonymousTypePlaceHolderGenerator.txt";
	}

	protected String getNewMethodName() {
		return "getAnonymousInstance";
	}

	protected boolean isNewMethodNative() {
		return false;
	}

	protected void visitTemplacedFileCodeBlock(final TemplatedFileCodeBlock template) {
		final GeneratorContext context = this.getGeneratorContext();
		final Type interfaceType = context.getType(Anonymous.class.getName());

		final NewAnonymousNestedType nested = this.getNewType().newAnonymousNestedType();
		nested.addInterface(interfaceType);

		template.setType("nestedType", nested);
	}

	protected String getNewMethodReturnType() {
		return Anonymous.class.getName();
	}
}
