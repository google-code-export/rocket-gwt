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
package rocket.serialization.rebind.switchstatement;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the case-statement.txt template
 * 
 * @author Miroslav Pokorny
 */
class CaseStatementTemplatedFile extends TemplatedCodeBlock {

	public CaseStatementTemplatedFile() {
		super();
	}

	public boolean isNative(){
		return true;
	}
	
	public void setNative( final boolean ignored ){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * The serializedType whose object writer is being registered
	 */
	private Type serializedType;

	protected Type getSerializedType() {
		ObjectHelper.checkNotNull("field:serializedType", serializedType);
		return this.serializedType;
	}

	public void setSerializedType(final Type serializedType) {
		ObjectHelper.checkNotNull("parameter:serializedType", serializedType);
		this.serializedType = serializedType;
	}

	/**
	 * The singleton field holding the object writer
	 */
	private Field objectWriterSingleton;

	protected Field getObjectWriterSingleton() {
		ObjectHelper.checkNotNull("field:objectWriter", objectWriterSingleton);
		return this.objectWriterSingleton;
	}

	public void setObjectWriterSingleton(final Field objectWriterSingleton) {
		ObjectHelper.checkNotNull("parameter:objectWriterSingleton", objectWriterSingleton);
		this.objectWriterSingleton = objectWriterSingleton;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.CASE_STATEMENT_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.CASE_STATEMENT_SERIALIZED_TYPE.equals(name)) {
				value = new StringLiteral(this.getSerializedType().getRuntimeName());
				break;
			}
			if (Constants.CASE_STATEMENT_SINGLETON.equals(name)) {
				value = this.getObjectWriterSingleton();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \""
				+ Constants.CASE_STATEMENT_TEMPLATE + "\".");
	}
}