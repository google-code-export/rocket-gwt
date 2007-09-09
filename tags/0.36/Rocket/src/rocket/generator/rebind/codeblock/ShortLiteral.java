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

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * A CodeBlock that contains a short literal.
 * 
 * @author Miroslav Pokorny
 */
public class ShortLiteral implements Literal {

	public ShortLiteral() {
		super();
	}

	public ShortLiteral(final short value) {
		this.setValue(value);
	}

	public boolean isEmpty() {
		return false;
	}

	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);
		writer.print("(short)" + String.valueOf(this.getValue()));
	}

	private short value;

	public short getValue() {
		return this.value;
	}

	public void setValue(final short value) {
		this.value = value;
	}

	public String toString() {
		return "short: " + value;
	}
}
