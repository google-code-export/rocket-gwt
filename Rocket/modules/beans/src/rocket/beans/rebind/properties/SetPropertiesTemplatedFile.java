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
package rocket.beans.rebind.properties;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.util.MethodComparator;
import rocket.util.client.Checker;

/**
 * An abstraction for the set properties template
 * 
 * @author Miroslav Pokorny
 */
public class SetPropertiesTemplatedFile extends TemplatedFileCodeBlock {

	public SetPropertiesTemplatedFile() {
		super();
		this.setProperties(this.createProperties());
	}

	/**
	 * The bean having its properties set.
	 */
	private Type bean;

	protected Type getBean() {
		Checker.notNull("field:bean", bean);
		return this.bean;
	}

	public void setBean(final Type bean) {
		Checker.notNull("parameter:bean", bean);
		this.bean = bean;
	}

	private Map<Method, Value> properties;

	protected Map<Method, Value> getProperties() {
		Checker.notNull("field:properties", properties);
		return this.properties;
	}

	protected void setProperties(final Map<Method, Value> properties) {
		Checker.notNull("parameter:properties", properties);
		this.properties = properties;
	}

	protected Map<Method, Value> createProperties() {
		return new TreeMap<Method, Value>(MethodComparator.INSTANCE);
	}

	public void addProperty(final Method setter, final Value value) {
		Checker.notNull("parameter:setter", setter);
		Checker.notNull("parameter:value", value);

		this.getProperties().put(setter, value);
	}

	protected CodeBlock getPropertiesCodeBlock() {
		final SetPropertyTemplatedFile template = new SetPropertyTemplatedFile();

		return new CollectionTemplatedCodeBlock<Map.Entry<Method, Value>>() {

			@Override
			public InputStream getInputStream() {
				return template.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			@Override
			protected Collection<Map.Entry<Method, Value>> getCollection() {
				return SetPropertiesTemplatedFile.this.getProperties().entrySet();
			}

			@Override
			protected void prepareToWrite(final Map.Entry<Method, Value> entry ) {
				template.setSetter(entry.getKey());
				template.setValue(entry.getValue());
			}

			@Override
			protected void writeBetweenElements(final SourceWriter writer) {
				writer.println("");
			}
		};
	}

	@Override
	protected String getResourceName() {
		return Constants.SET_PROPERTIES_TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.SET_PROPERTIES_BEAN_TYPE.equals(name)) {
				value = this.getBean();
				break;
			}
			if (Constants.SET_PROPERTIES_SET_INDIVIDUAL_PROPERTIES.equals(name)) {
				value = this.getPropertiesCodeBlock();
				break;
			}

			break;
		}
		return value;
	}
};
