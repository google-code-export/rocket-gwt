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

import rocket.generator.rebind.codeblock.BooleanLiteral;
import rocket.generator.test.templatedfilecodeblock.client.TemplatedFileCodeBlockTestConstants;

public class BooleanPlaceHolderGenerator extends AbstractTemplatedFileCodeBlockGenerator {

	protected String getTemplateFilename() {
		return "BooleanPlaceHolderGenerator.txt";
	}

	protected String getNewMethodName() {
		return "getBooleanValue";
	}

	protected boolean isNewMethodNative() {
		return false;
	}

	protected void visitTemplate(final Template template) {
		template.set("booleanValue", new BooleanLiteral(TemplatedFileCodeBlockTestConstants.BOOLEAN));
	}

	protected String getNewMethodReturnType() {
		return Boolean.TYPE.getName();
	}
}
