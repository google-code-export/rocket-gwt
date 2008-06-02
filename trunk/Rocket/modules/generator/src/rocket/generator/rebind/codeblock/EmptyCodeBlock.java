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

/**
 * Represents an empty code block. Any attempt to write it out will result in an
 * exception being thrown.
 * 
 * @author Miroslav Pokorny
 */
public class EmptyCodeBlock implements CodeBlock {
	public static CodeBlock INSTANCE = new EmptyCodeBlock();

	private EmptyCodeBlock() {
		super();
	}

	public boolean isEmpty() {
		return true;
	}

	public void write(final SourceWriter writer) {
		throw new UnsupportedOperationException(this.getClass().getName() + ".write()");
	}
}
