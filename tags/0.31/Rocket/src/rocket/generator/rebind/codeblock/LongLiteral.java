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
 * A CodeBlock that contains a long literal.
 * 
 * @author Miroslav Pokorny
 */
public class LongLiteral implements Literal {

	public LongLiteral() {
		super();
	}

	public LongLiteral(final long value) {
		this.setValue(value);
	}

	public boolean isEmpty() {
		return false;
	}

	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);
		writer.print("(long)" + String.valueOf(this.getValue()) + "L");
	}

	private long value;

	public long getValue() {
		return this.value;
	}

	public void setValue(final long value) {
		this.value = value;
	}

	public String toString() {
		return "long: " + value;
	}
}