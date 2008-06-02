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
package rocket.serialization.rebind.read;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the read.txt template
 * 
 * @author Miroslav Pokorny
 */
public class ReadTemplatedFile extends TemplatedFileCodeBlock {

	public ReadTemplatedFile() {
		super();
	}
	
	/**
	 * The type having its fields read
	 */
	private Type type;

	protected Type getType() {
		Checker.notNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		Checker.notNull("parameter:type", type);
		this.type = type;
	} 

	@Override
	protected String getResourceName() {
		return Constants.READ_TEMPLATE;
	}
	
	@Override
	public InputStream getInputStream(){
		return super.getInputStream();
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;

		if (Constants.READ_TYPE.equals(name)) {
			value = this.getType();
		}
		
			return value;
	}
};
