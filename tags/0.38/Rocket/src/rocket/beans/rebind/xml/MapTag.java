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
package rocket.beans.rebind.xml;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Provides a bean view of a map tag.
 * 
 * @author Miroslav Pokorny
 */
public class MapTag extends ValueTag {

	public Map getValues() {
		final NodeList nodeList = this.getElement().getElementsByTagName(Constants.MAP_ENTRY_TAG);

		return Collections.unmodifiableMap(new AbstractMap() {

			public Set entrySet() {

				return new AbstractSet() {
					public Iterator iterator() {
						return new Iterator() {
							public Object next() {
								if (false == this.hasNext()) {
									throw new NoSuchElementException();
								}

								final int index = this.getIndex();
								final Element mapEntryElement = (Element) nodeList.item(index);
								this.setIndex(index + 1);

								return new Map.Entry() {
									public Object getKey() {
										return MapTag.this.getAttribute(mapEntryElement, Constants.MAP_ENTRY_KEY_ATTRIBUTE);
									}

									public Object getValue() {
										final List value = MapTag.this.getElements(mapEntryElement.getChildNodes());
										return MapTag.this.getValue((Element) value.get(0));
									}

									public Object setValue(final Object value) {
										throw new UnsupportedOperationException("setValue");
									}
								};
							}

							public boolean hasNext() {
								return this.getIndex() < nodeList.getLength();
							}

							int index;

							int getIndex() {
								return this.index;
							}

							void setIndex(final int index) {
								this.index = index;
							}

							public void remove() {
								throw new UnsupportedOperationException("remove");
							}
						};
					}

					public int size() {
						return nodeList.getLength();
					};
				};
			};
		});
	}
}
