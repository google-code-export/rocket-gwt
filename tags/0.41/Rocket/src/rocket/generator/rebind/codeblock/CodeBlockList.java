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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.CodeGenerator;
import rocket.generator.rebind.SourceWriter;
import rocket.util.client.Checker;

/**
 * Accumulates or holds multiple code blocks.
 * 
 * @author Miroslav Pokorny
 */
public class CodeBlockList implements CodeBlock, CodeGenerator {

	public CodeBlockList() {
		super();

		this.setCodeBlocks(this.createCodeBlocks());
	}

	public void add(final CodeBlock codeBlock) {
		Checker.notNull("parameter:codeBlock", codeBlock);

		this.getCodeBlocks().add(codeBlock);
	}

	private List codeBlocks;

	protected List getCodeBlocks() {
		Checker.notNull("field:codeBlocks", codeBlocks);
		return this.codeBlocks;
	}

	protected void setCodeBlocks(final List codeBlocks) {
		Checker.notNull("parameter:codeBlocks", codeBlocks);
		this.codeBlocks = codeBlocks;
	}

	protected List createCodeBlocks() {
		return new ArrayList();
	}

	public boolean isEmpty() {
		boolean empty = true;
		final Iterator iterator = this.getCodeBlocks().iterator();
		while (iterator.hasNext()) {
			final CodeBlock codeBlock = (CodeBlock) iterator.next();
			if (false == codeBlock.isEmpty()) {
				empty = false;
				break;
			}
		}
		return empty;
	}

	public void write(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final Iterator iterator = this.getCodeBlocks().iterator();
		while (iterator.hasNext()) {
			final CodeBlock codeBlock = (CodeBlock) iterator.next();
			if (codeBlock.isEmpty()) {
				continue;
			}

			codeBlock.write(writer);
		}
	}
}
