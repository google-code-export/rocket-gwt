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

import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides a bean like view of a map tag.
 * 
 * @author Miroslav Pokorny
 */
class MapTag extends ValueTag {

	public Map<String,Element> getValues() {
		final Map<String,Element> entries = new TreeMap<String,Element>();

		final NodeList nodeList = this.getElement().getElementsByTagName(Constants.MAP_ENTRY_TAG);
		final int count = nodeList.getLength();
		for (int i = 0; i < count; i++) {
			final Node node = nodeList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			final Element element = (Element) node;
			final String key = this.getAttribute(element, Constants.MAP_ENTRY_KEY_ATTRIBUTE);

			final NodeList valueNodeList = element.getElementsByTagName(Constants.MAP_ENTRY_KEY_ATTRIBUTE);
			final Element value = (Element) valueNodeList.item(0);

			entries.put(key, value);
		}

		return entries;
	}
}
