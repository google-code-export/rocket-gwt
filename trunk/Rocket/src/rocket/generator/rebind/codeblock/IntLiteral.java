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

/**
 * A CodeBlock that contains a int literal.
 * 
 * @author Miroslav Pokorny
 */
public class IntLiteral implements Literal {

	public IntLiteral() {
		super();
	}

	public IntLiteral(final int value) {
		this.setValue(value);
	}

	public boolean isEmpty() {
		return false;
	}

	public void write(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);
		writer.print("(int)" + String.valueOf(this.getValue()));
	}

	private int value;

	public int getValue() {
		return this.value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	public String toString() {
		return "int: " + value;
	}
}
