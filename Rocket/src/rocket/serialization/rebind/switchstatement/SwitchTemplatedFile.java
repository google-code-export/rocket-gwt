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
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the create-client-object-stream.txt template
 * 
 * @author Miroslav Pokorny
 */
public class SwitchTemplatedFile extends TemplatedCodeBlock {

	public SwitchTemplatedFile() {
		super();
		this.setRegistered(this.createRegistered());
	}

	public boolean isNative() {
		return false;
	}

	public void setNative(final boolean ignored) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A map that aggregates type names to ObjectWriters
	 */
	private Map registered;

	protected Map getRegistered() {
		ObjectHelper.checkNotNull("field:registered", registered);
		return this.registered;
	}

	protected void setRegistered(final Map registered) {
		ObjectHelper.checkNotNull("parameter:registered", registered);
		this.registered = registered;
	}

	protected Map createRegistered() {
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
		ObjectHelper.checkNotNull("parameter:type", type);
		ObjectHelper.checkNotNull("parameter:objectWriterSingleton", objectWriterSingleton);

		this.getRegistered().put(type, objectWriterSingleton);
	}

	protected CodeBlock getCaseStatementsAsCodeBlock() {
		final CaseStatementTemplatedFile template = new CaseStatementTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			public boolean isNative(){
				return template.isNative();
			}
			
			public InputStream getInputStream() {
				return template.getInputStream();
			}

			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			protected Collection getCollection() {
				return SwitchTemplatedFile.this.getRegistered().entrySet();
			}

			protected void prepareToWrite(final Object element) {
				final Map.Entry entry = (Map.Entry) element;

				template.setSerializedType((Type) entry.getKey());
				template.setObjectWriterSingleton((Field) entry.getValue());
			}

			protected void writeBetweenElements(final SourceWriter writer) {
				writer.println("");
			}
		};
	}
//
//	private Field staticField;
//
//	protected Field getStaticField() {
//		ObjectHelper.checkNotNull("field:staticField", staticField);
//		return this.staticField;
//	}
//
//	public void setStaticField(final Field staticField) {
//		ObjectHelper.checkNotNull("parameter:staticField", staticField);
//		this.staticField = staticField;
//	}

	protected InputStream getInputStream() {
		final String filename = Constants.SWITCH_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;

		while (true) {
			if (Constants.SWITCH_STATEMENTS.equals(name)) {
				value = this.getCaseStatementsAsCodeBlock();
				break;
			}
//			if (JsonConstants.CREATE_OBJECT_WRITERS_STATIC_INITIALIZER_OBJECT_WRITERS_STATIC_FIELD.equals(name)) {
//				value = this.getStaticField();
//				break;
//			}
			break;
		}

		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \""
				+ Constants.SWITCH_TEMPLATE + "\".");
	}
};
