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
import java.util.HashMap;
import java.util.Map;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * An abstraction for the invoker add template
 * 
 * @author Miroslav Pokorny
 */
public class SetPropertiesTemplatedFile extends TemplatedCodeBlock {

	public SetPropertiesTemplatedFile() {
		super();
		setNative(false);
		this.setProperties(this.createProperties());
	}

	private MethodParameter instance;

	protected MethodParameter getInstance() {
		ObjectHelper.checkNotNull("field:instance", instance);
		return this.instance;
	}

	public void setInstance(final MethodParameter instance) {
		ObjectHelper.checkNotNull("parameter:instance", instance);
		this.instance = instance;
	}

	/**
	 * The bean having its properties set.
	 */
	private Type bean;

	protected Type getBean() {
		ObjectHelper.checkNotNull("field:bean", bean);
		return this.bean;
	}

	public void setBean(final Type bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		this.bean = bean;
	}

	private Map properties;

	protected Map getProperties() {
		ObjectHelper.checkNotNull("field:properties", properties);
		return this.properties;
	}

	protected void setProperties(final Map properties) {
		ObjectHelper.checkNotNull("parameter:properties", properties);
		this.properties = properties;
	}

	protected Map createProperties() {
		return new HashMap();
	}

	public void addProperty(final Method setter, final Value value) {
		ObjectHelper.checkNotNull("parameter:setter", setter);
		ObjectHelper.checkNotNull("parameter:value", value);

		this.getProperties().put(setter, value);
	}

	protected CodeBlock getPropertiesCodeBlock() {
		final SetPropertyTemplatedFile template = new SetPropertyTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return template.getInputStream();
			}

			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			protected Collection getCollection() {
				return SetPropertiesTemplatedFile.this.getProperties()
						.entrySet();
			}

			protected void prepareToWrite(Object element) {
				final Map.Entry entry = (Map.Entry) element;

				template.setSetter((Method) entry.getKey());
				template.setValue((Value) entry.getValue());
			}

			protected void writeBetweenElements(SourceWriter writer) {
				writer.println("");
			}
		};
	}

	protected InputStream getInputStream() {
		final String filename = Constants.SET_PROPERTIES_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(
				filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException(
					"Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.SET_PROPERTIES_BEAN_TYPE.equals(name)) {
				value = this.getBean();
				break;
			}
			if (Constants.SET_PROPERTIES_INSTANCE_PARAMETER.equals(name)) {
				value = this.getInstance();
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

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name
				+ "] not found, template file ["
				+ Constants.SET_PROPERTIES_TEMPLATE + "]");
	}
};
