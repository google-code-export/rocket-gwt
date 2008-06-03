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
package rocket.widget.rebind.htmltemplatefactory.createhtmlfromtemplate;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.util.client.Checker;

public class CreateHtmlFromTemplateTemplatedFile extends TemplatedFileCodeBlock {

	public CreateHtmlFromTemplateTemplatedFile(final String statements) {
		super();

		this.setStatements(statements);
	}

	@Override
	protected InputStream getInputStream() {
		final String fileName = this.getResourceName();
		return this.getClass().getResourceAsStream(fileName);
	}

	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;

		if (Constants.STATEMENTS.equals(name)) {
			value = new StringCodeBlock(this.getStatements());
		}
		return value;
	}

	private String statements;

	protected String getStatements() {
		Checker.notEmpty("field:statements", statements);
		return this.statements;
	}

	public void setStatements(final String statements) {
		Checker.notEmpty("parameter:statements", statements);
		this.statements = statements;
	}
}
