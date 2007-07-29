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

import java.util.List;

import org.w3c.dom.Element;

/**
 * A nice abstraction to read a property tag
 * 
 * @author Miroslav Pokorny
 */
public class PropertyTag extends XmlDocumentComponent {

	public String getPropertyName() {
		return this.getAttribute(Constants.PROPERTY_NAME_ATTRIBUTE);
	}

	public ValueTag getValue() {
		final List values = this.getElements(this.getElement().getChildNodes());
		final Element element = (Element) values.get(0);
		return this.getValue(element);
	}
}
