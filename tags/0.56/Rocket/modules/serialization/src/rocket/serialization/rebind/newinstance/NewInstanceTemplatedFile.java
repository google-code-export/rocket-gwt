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
package rocket.serialization.rebind.newinstance;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.constructor.Constructor;
import rocket.util.client.Checker;

/**
 * An abstraction for the new-instance.txt template
 * 
 * @author Miroslav Pokorny
 */
public class NewInstanceTemplatedFile extends TemplatedFileCodeBlock {

	public NewInstanceTemplatedFile() {
		super();
	}

	private Constructor constructor;

	protected Constructor getConstructor() {
		Checker.notNull("field:type", constructor);
		return this.constructor;
	}

	public void setConstructor(final Constructor constructor) {
		Checker.notNull("parameter:constructor", constructor);
		this.constructor = constructor;
	}

	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.CONSTRUCTOR.equals(name)) {
				value = this.getConstructor();
				break;
			}
			break;
		}
		return value;
	}
}
