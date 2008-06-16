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
import rocket.util.client.Checker;
import rocket.util.client.Tester;

/**
 * A code block that contains a String literal
 * 
 * @author Miroslav Pokorny
 */
public class StringCodeBlock implements Literal {

	public StringCodeBlock() {
		super();
	}

	public StringCodeBlock(final String content) {
		super();
		this.setContent(content);
	}

	public void write(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		writer.print(this.getContent());
	}

	/**
	 * A string that contains all the statements etc of a constructor, method or
	 * field assignment.
	 */
	private String content;

	public String getContent() {
		Checker.notEmpty("field:content", content);
		return content;
	}

	protected boolean hasContent() {
		return null != content;
	}

	public void setContent(final String content) {
		Checker.notEmpty("parameter:content", content);
		this.content = content;
	}

	public boolean isEmpty() {
		return false == (this.hasContent() && false == Tester.isNullOrEmpty(this.getContent()));
	}

	public String toString() {
		return "StringLiteral \"" + this.content + "\".";
	}
}
