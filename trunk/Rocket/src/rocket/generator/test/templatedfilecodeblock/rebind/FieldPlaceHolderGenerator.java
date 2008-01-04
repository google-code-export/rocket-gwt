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

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.type.Type;
import rocket.generator.test.templatedfilecodeblock.client.TemplatedFileCodeBlockTestConstants;

public class FieldPlaceHolderGenerator extends AbstractTemplatedFileCodeBlockGenerator {

	protected String getTemplateFilename() {
		return "FieldPlaceHolderGenerator.txt";
	}

	final String FIELD = "field";

	protected void addNewField() {
		final NewField field = this.getNewType().newField();
		field.setFinal(false);
		field.setName(FIELD);
		field.setStatic(false);
		field.setTransient(false);
		field.setType(this.getGeneratorContext().getString());
		field.setValue(new StringLiteral(TemplatedFileCodeBlockTestConstants.STRING));
		field.setVisibility(Visibility.PACKAGE_PRIVATE);
	}

	protected String getNewMethodName() {
		return "getField";
	}

	protected boolean isNewMethodNative() {
		return false;
	}

	protected void visitTemplate(final Template template) {
		final Type type = this.getNewType();
		final Field field = type.getField(FIELD);
		template.set("field", field);
	}

	protected String getNewMethodReturnType() {
		return String.class.getName();
	}
}
