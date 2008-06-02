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
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the switch template
 * 
 * @author Miroslav Pokorny
 */
public class SwitchTemplatedFile extends TemplatedFileCodeBlock {

	public SwitchTemplatedFile() {
		super();
		this.setRegistered(this.createRegistered());
	}

	/**
	 * A map that aggregates type names to ObjectWriters
	 */
	private Map<Type,Field> registered;

	protected Map<Type,Field> getRegistered() {
		Checker.notNull("field:registered", registered);
		return this.registered;
	}

	protected void setRegistered(final Map<Type,Field> registered) {
		Checker.notNull("parameter:registered", registered);
		this.registered = registered;
	}

	protected Map<Type,Field> createRegistered() {
		return new TreeMap( new Comparator(){
			public int compare( final Object object, final Object otherObject ){
				return compare( (Type) object, (Type) otherObject );
			}
			int compare( final Type type, final Type otherType ){
				return type.getName().compareTo( otherType.getName() );
			}
		});
	}

	public void register(final Type type, final Field objectWriterSingleton) {
		Checker.notNull("parameter:type", type);
		Checker.notNull("parameter:objectWriterSingleton", objectWriterSingleton);

		this.getRegistered().put(type, objectWriterSingleton);
	}

	protected CodeBlock getCaseStatementsAsCodeBlock() {
		final CaseStatementTemplatedFile template = new CaseStatementTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			@Override
			public boolean isNative(){
				return template.isNative();
			}
			
			@Override
			public InputStream getInputStream() {
				return template.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			@Override
			protected Collection getCollection() {
				return SwitchTemplatedFile.this.getRegistered().entrySet();
			}

			@Override
			protected void prepareToWrite(final Object element) {
				final Map.Entry entry = (Map.Entry) element;

				template.setSerializedType((Type) entry.getKey());
				template.setObjectWriterSingleton((Field) entry.getValue());
			}

			@Override
			protected void writeBetweenElements(final SourceWriter writer) {
				writer.println("");
			}
		};
	}

	@Override
	protected String getResourceName() {
		return Constants.SWITCH_TEMPLATE;
	}

	protected Object getValue0(final String name) {
		Object value = null;

		while (true) {
			if (Constants.SWITCH_STATEMENTS.equals(name)) {
				value = this.getCaseStatementsAsCodeBlock();
				break;
			}
			break;
		}

		return value;
	}
};
