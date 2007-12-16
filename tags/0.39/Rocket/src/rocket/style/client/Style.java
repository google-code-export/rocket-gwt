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
package rocket.style.client;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import rocket.util.client.StringHelper;

/**
 * A common base class for any map view of a set of styles.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class Style extends AbstractMap {

	abstract public String getCssText();

	abstract public void setCssText(String cssText);

	public abstract int size();

	public Object get(final Object key) {
		return this.getStylePropertyValue((String) key);
	}

	public boolean containsKey(final Object key) {
		return null != this.get(key);
	}

	/**
	 * Factory method which creates a new StylePropertyValue and populates it
	 * with a string value if the inline style property is available.
	 * 
	 * @param propertyName
	 * @return
	 */
	protected StylePropertyValue getStylePropertyValue(final String propertyName) {
		StylePropertyValue value = null;
		String stringValue = this.getValue(propertyName);
		if (false == StringHelper.isNullOrEmpty(stringValue)) {
			value = new StylePropertyValue();
			value.setString(stringValue);
		}
		return value;
	}

	abstract String getValue(String propertyName);

	public Object put(final Object key, final Object value) {
		return this.putStyle((String) key, (StylePropertyValue) value);
	}

	protected StylePropertyValue putStyle(final String propertyName, final StylePropertyValue newValue) {
		final StylePropertyValue replaced = this.getStylePropertyValue(propertyName);

		final String propertyValue = newValue.getString();
		this.putValue(propertyName, propertyValue);

		return replaced;
	}

	abstract protected void putValue(String propertyName, String propertyValue);

	public Object remove(final Object key) {
		return this.removeStyle((String) key);
	}

	protected StylePropertyValue removeStyle(final String propertyName) {
		final StylePropertyValue removed = this.getStylePropertyValue(propertyName);
		this.removeValue(propertyName);
		return removed;
	}

	abstract protected void removeValue(final String propertyName);

	public Set entrySet() {
		return new StyleEntrySet();
	}

	/**
	 * Implements a Set view of all the inline styles belonging to an Element.
	 */
	class StyleEntrySet extends AbstractSet {
		public int size() {
			return Style.this.size();
		}

		public Iterator iterator() {
			return new StyleEntrySetIterator();
		}
	}

	/**
	 * This iterator may be used to visit all inline style entries.
	 */
	class StyleEntrySetIterator implements Iterator {

		public boolean hasNext() {
			return this.getCursor() < this.getPropertyNames().length;
		}

		public Object next() {
			final int cursor = this.getCursor();
			final String[] propertyNames = this.getPropertyNames();
			if (cursor >= propertyNames.length) {
				throw new NoSuchElementException();
			}
			final String key = propertyNames[cursor];
			final Object value = Style.this.get(key);
			this.setCursor(cursor + 1);
			return new Map.Entry() {
				public Object getKey() {
					return key;
				}

				public Object getValue() {
					return value;
				}

				public Object setValue(final Object newValue) {
					return Style.this.put(key, newValue);
				}
			};
		}

		public void remove() {
			final int cursor = this.getCursor() - 1;
			final String[] propertyNames = this.getPropertyNames();
			final String propertyName = propertyNames[cursor];
			if (null == propertyName) {
				throw new IllegalStateException();
			}

			Style.this.remove(propertyName);
			propertyNames[cursor] = null;// mark that its already been
			// deleted.
		}

		/**
		 * An array containing all the property names for the native object.
		 */
		String[] propertyNames;

		String[] getPropertyNames() {
			if (false == this.hasPropertyNames()) {
				this.setPropertyNames(Style.this.getPropertyNames());
			}
			return this.propertyNames;
		}

		boolean hasPropertyNames() {
			return null != this.propertyNames;
		}

		void setPropertyNames(final String[] propertyNames) {
			this.propertyNames = propertyNames;
		}

		/**
		 * This cursor points to the next visitable element.
		 */
		int cursor = 0;

		int getCursor() {
			return this.cursor;
		}

		void setCursor(final int cursor) {
			this.cursor = cursor;
		}
	}

	/**
	 * Sub classes implement this method to return all the style property names.
	 * This method is required by the Style iterator.
	 * 
	 * @return
	 */
	abstract String[] getPropertyNames();

	public String toString() {
		return this.getCssText();
	}

	/**
	 * Helper which removes the decorating url, brackets and quotes from a
	 * string returning just the url.
	 * 
	 * @param value
	 * @return
	 */
	static String getUrl(final String value) {
		String url = value;
		if (null != url) {
			int first = "url(".length();
			int last = url.length() - 1 - 1;
			if (url.charAt(first) == '\'') {
				first++;
			}
			if (url.charAt(first) == '"') {
				first++;
			}
			if (url.charAt(last) == '\'') {
				last--;
			}
			if (url.charAt(last) == '"') {
				last--;
			}
			url = url.substring(first, last + 1);
		}
		return url;
	}
}
