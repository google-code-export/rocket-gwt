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
import rocket.generator.rebind.GeneratorHelper;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * Accumulates or holds multiple code blocks.
 * 
 * @author Miroslav Pokorny
 */
public class ManyCodeBlocks implements CodeBlock, CodeGenerator {

	public ManyCodeBlocks() {
		super();

		this.setCodeBlocks(this.createCodeBlocks());
	}

	public void add(final CodeBlock codeBlock) {
		ObjectHelper.checkNotNull("parameter:codeBlock", codeBlock);

		this.getCodeBlocks().add(codeBlock);
	}

	private List codeBlocks;

	protected List getCodeBlocks() {
		ObjectHelper.checkNotNull("field:codeBlocks", codeBlocks);
		return this.codeBlocks;
	}

	protected void setCodeBlocks(final List codeBlocks) {
		ObjectHelper.checkNotNull("parameter:codeBlocks", codeBlocks);
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
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final Iterator iterator = this.getCodeBlocks().iterator();
		while (iterator.hasNext()) {
			final CodeBlock codeBlock = (CodeBlock) iterator.next();
			if (codeBlock.isEmpty()) {
				continue;
			}

			GeneratorHelper.writeClassComponent(codeBlock, writer);
		}
	}
}
