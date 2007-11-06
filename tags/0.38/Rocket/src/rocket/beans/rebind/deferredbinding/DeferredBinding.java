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
package rocket.beans.rebind.deferredbinding;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * Holds a statement that creates an instance using deferred binding.
 * 
 * @author Miroslav Pokorny
 */
public class DeferredBinding implements CodeBlock {

	public boolean isEmpty() {
		return false;
	}

	public void write(final SourceWriter writer) {
		final DeferredBindingTemplatedFile template = new DeferredBindingTemplatedFile();

		template.setType(this.getType());
		template.write(writer);
	}

	/**
	 * The interface
	 */
	private Type type;

	protected Type getType() {
		ObjectHelper.checkNotNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		ObjectHelper.checkNotNull("type:type", type);
		this.type = type;
	}
}
