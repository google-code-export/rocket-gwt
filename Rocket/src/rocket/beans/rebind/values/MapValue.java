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
package rocket.beans.rebind.values;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rocket.beans.client.MapBuilder;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * Contains a Map property value for a bean.
 * 
 * @author Miroslav Pokorny
 */
public class MapValue extends Value {

	public MapValue() {
		this.setMap(new HashMap());
	}

	public void addMapEntry(final String key, final Value value) {
		final Map entries = this.getMap();
		if (entries.containsKey(key)) {
			this.throwMapEntryAlreadyUsedException(key);
		}

		entries.put(key, value);
	}

	protected void throwMapEntryAlreadyUsedException(final String key) {
		throw new MapEntryAlreadyUsedException("A map entry with a key of [" + key + "] has already been defined");
	}

	/**
	 * A map that accumulates map entries.
	 */
	private Map map;

	public Map getMap() {
		ObjectHelper.checkNotNull("field:map", map);
		return this.map;
	}

	protected void setMap(final Map map) {
		ObjectHelper.checkNotNull("parameter:map", map);
		this.map = map;
	}

	/**
	 * If the property is a not Map report false
	 * 
	 * @return
	 */
	public boolean isCompatibleWith() {
		return this.getType().getQualifiedSourceName().equals(Map.class.getName());
	}

	public String generateValue() {
		final StringBuilder builder = new StringBuilder();
		builder.append("new ");
		builder.append(MapBuilder.class.getName());
		builder.append("()");

		final Iterator entries = this.getMap().entrySet().iterator();
		while (entries.hasNext()) {
			final Map.Entry entry = (Map.Entry) entries.next();
			final String key = (String) entry.getKey();
			final Value value = (Value) entry.getValue();

			builder.append(".add( \"");
			builder.append(Generator.escape(key));
			builder.append("\", ");
			builder.append(value.generateValue());
			builder.append(")");
		}

		builder.append(".getMap()");

		return builder.toString();
	}
	
	public String toString(){
		return super.toString() + ", map: " + map;
	}
}
