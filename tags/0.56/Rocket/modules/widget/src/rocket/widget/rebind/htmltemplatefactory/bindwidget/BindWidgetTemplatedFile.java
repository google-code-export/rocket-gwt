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
package rocket.widget.rebind.htmltemplatefactory.bindwidget;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.util.client.Checker;

public class BindWidgetTemplatedFile extends TemplatedFileCodeBlock {

	public BindWidgetTemplatedFile(final String resourceName, final String id) {
		super();

		this.setId(id);
		this.setResourceName(resourceName);
	}

	@Override
	protected InputStream getInputStream() {
		final String fileName = this.getResourceName();
		return this.getClass().getResourceAsStream(fileName);
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;

		if (Constants.ID.equals(name)) {
			value = new StringLiteral(this.getId());
		}
		return value;
	}

	private String resourceName;

	protected String getResourceName() {
		Checker.notEmpty("field:resourceName", resourceName);
		return this.resourceName;
	}

	public void setResourceName(final String resourceName) {
		Checker.notEmpty("parameter:resourceName", resourceName);
		this.resourceName = resourceName;
	}

	private String id;

	protected String getId() {
		Checker.notEmpty("field:id", id);
		return this.id;
	}

	public void setId(final String id) {
		Checker.notEmpty("parameter:id", id);
		this.id = id;
	}
}
