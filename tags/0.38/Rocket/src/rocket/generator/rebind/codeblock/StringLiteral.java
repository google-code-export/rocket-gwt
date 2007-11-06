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
package rocket.generator.rebind.codeblock;

import rocket.generator.rebind.SourceWriter;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.Generator;

/**
 * A code block that contains a String literal
 * 
 * @author Miroslav Pokorny
 */
public class StringLiteral implements Literal {

	public StringLiteral() {
		super();
	}

	public StringLiteral(final String content) {
		super();
		this.setValue(content);
	}

	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		writer.print('"' + Generator.escape(this.getValue()) + '"');
	}

	/**
	 * A string that contains all the statements etc of a constructor, method or
	 * field assignment.
	 */
	private String value;

	public String getValue() {
		StringHelper.checkNotEmpty("field:value", value);
		return value;
	}

	protected boolean hasContent() {
		return null != value;
	}

	public void setValue(final String value) {
		StringHelper.checkNotEmpty("parameter:value", value);
		this.value = value;
	}

	public boolean isEmpty() {
		return false == (this.hasContent() && false == StringHelper.isNullOrEmpty(this.getValue()));
	}

	public String toString() {
		return "String [" + this.value + "]";
	}
}
