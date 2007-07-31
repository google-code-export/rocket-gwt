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

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;

/**
 * Base class for any value be it a string literal, list, set or map.
 * 
 * @author Miroslav Pokorny
 */
class CollectionValueTag extends ValueTag {

	public List getValues() {
		final List valueElements = this.getElements(this.getElement().getChildNodes());

		return Collections.unmodifiableList(new AbstractList() {

			public Object get(final int index) {
				final Element element = (Element) valueElements.get(index);
				return CollectionValueTag.this.getValue(element);
			}

			public int size() {
				return valueElements.size();
			}
		});
	}
}
