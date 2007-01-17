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
package rocket.style.client.support;

import rocket.style.client.StyleConstants;

import com.google.gwt.user.client.Element;

/**
 * A specialised StyleHelperSupport that caters for some FireFox quirks.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class FireFoxStyleHelperSupport extends StyleHelperSupport {

    /**
     * Computes the value of the requested propertyName for the given element
     * 
     * <h6>Special cases</h6>
     * <ul>
     * <li>background-position (it is not possible to read the computed property value in FF)</li>
     * </ul>
     * 
     * @param element
     * @param propertyName
     * @return The value or null if the value was not found.
     */
    public String getComputedStyleProperty(final Element element, final String propertyName) {
        String propertyValue = null;
        while (true) {
            if (StyleConstants.BACKGROUND_POSITION.equals(propertyName)) {
                propertyValue = this.getComputedBackgroundPosition(element);
                break;
            }

            propertyValue = super.getComputedStyleProperty(element, propertyName);
            break;
        }

        return propertyValue;
    }

    /**
     * This hack attempts to read the background position from the inline style. If it is not found then this method gives up and thrown an
     * exception.
     * 
     * @param element
     * @return
     */
    protected String getComputedBackgroundPosition(final Element element) {
        final String value = this.getInlineStyleProperty(element, StyleConstants.BACKGROUND_POSITION);
        if (value == null) {
            throw new UnsupportedOperationException("FireFox bug 316981 ");
        }
        return value;
    }
}
