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
package rocket.remoting.client;

import java.util.Iterator;
import java.util.List;

import rocket.collection.client.MultiValueMap;
import rocket.util.client.Checker;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A simple implementation container for headers.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Headers implements IsSerializable {

	public Headers() {
		this.setMultiValueMap(new MultiValueMap());
	}

	/**
	 * A special map that stores one or more values for a key.
	 */
	private MultiValueMap multiValueMap;

	protected MultiValueMap getMultiValueMap() {
		Checker.notNull("field:map", multiValueMap);

		return multiValueMap;
	}

	protected void setMultiValueMap(final MultiValueMap multiValueMap) {
		Checker.notNull("parameter:multiValueMap", multiValueMap);

		this.multiValueMap = multiValueMap;
	}

	public boolean contains(final String name) {
		return this.getMultiValueMap().contains(name.toLowerCase());
	}

	public String getValue(final String name) {
		return (String) this.getMultiValueMap().getFirstValue(name.toLowerCase());
	}

	protected List getValueAsList(final String name) {
		Checker.notEmpty("parameter:name", name);
		return (List) this.getMultiValueMap().getValuesList(name.toLowerCase());
	}

	public String[] getValues(final String name) {
		final Object[] values = this.getMultiValueMap().getValues(name.toLowerCase());
		String[] array = null;
		if (null != values) {
			final int size = values.length;
			array = new String[size];
			for (int i = 0; i < size; i++) {
				array[i] = (String) values[i];
			}
		}
		return array;
	}

	public void add(final String name, final String value) {
		Checker.notEmpty("parameter:name", name);

		this.getMultiValueMap().add(name.toLowerCase(), value);
	}

	public Iterator names() {
		return this.getMultiValueMap().keys();
	}

	public void clear() {
		this.getMultiValueMap().clear();
	}

	public String toString() {
		return super.toString() + ", multiValueMap:" + multiValueMap;
	}
}
