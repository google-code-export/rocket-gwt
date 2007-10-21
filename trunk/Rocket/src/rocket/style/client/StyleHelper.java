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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A variety of helper methods related to css/stylesheets and widgets/html.
 * 
 * This helper provides support for changing styles/classes for widgets that use
 * a heirarchical manner to name their composite widgets/elements.
 * 
 * Refer to the {@link InlineStyle} and {@link ComputedStyle} to work with
 * inline and computed styles for a particular element.
 * 
 * In order to enable correct simulation of fixed positioning the coordinates
 * must be set prior to setting the position.
 * 
 * <h3>Gotchas</h3>
 * <ul>
 * <li>Fixed positioning under Internet Explorer is only supported for inline
 * css properties set via {@link InlineStyle} and not
 * {@link DOM#setStyleAttribute(Element, String, String)}.</li>
 * <li>It is not possible to disable text selection via css, it may only be
 * achieved by setting a inlineStyle belonging to an element.</li>
 * <li>A stylesheet rules list is normalized that is rules with multiple
 * selectors are normalized when the each selector given its own rule with the
 * same style properties. This is done to present a common view as Internet
 * Explorer does not allow rules to have more than one selector</li>
 * <li>
 * </ul>
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * TODO add support to converting % values into pixels and other absolute units.
 * Would require multiplying % value again contentbox width/height This is
 * definitely needed for IE.
 */
public class StyleHelper {
	
//	/**
//	 * Verifies that the given selectorText contains only a single selector.
//	 * 
//	 * @param name
//	 * @param selectorText
//	 */
//	static void checkSelector(final String name, final String selectorText) {
//		if (StringHelper.isNullOrEmpty(selectorText) | -1 != selectorText.indexOf(StyleConstants.SELECTOR_SEPARATOR)) {
//			ObjectHelper.fail("The " + name + " contains more than one selector, selectorText[" + selectorText + "]");
//		}
//	}
////
////	/**
////	 * Retrieves an inline style property by name.
////	 * 
////	 * @param rule
////	 * @param name
////	 * @return
////	 */
////	static String getRuleStyleProperty(final JavaScriptObject rule, final String name) {
////		return StyleHelper.getSupport().getRuleStyleProperty(rule, name);
////	}
////
////	/**
////	 * Sets an inline style property name with a new value.
////	 * 
////	 * @param rule
////	 * @param propertyName
////	 * @param propertyValue
////	 */
////	static void setRuleStyleProperty(final JavaScriptObject rule, final String propertyName, final String propertyValue) {
////		StyleHelper.getSupport().setRuleStyleProperty(rule, propertyName, propertyValue);
////	}
////
////	/**
////	 * This helper may be used to remove an existing Style's property. If the
////	 * property does not exist nothing happens.
////	 * 
////	 * @param rule
////	 * @param propertyName
////	 */
////	static void removeRuleStyleProperty(final JavaScriptObject rule, final String propertyName) {
////		StyleHelper.getSupport().removeRuleStyleProperty(rule, propertyName);
////	}
////
////	/**
////	 * Retrieves the names of all the computed styles available for the given
////	 * element.
////	 * 
////	 * @param element
////	 * @return
////	 */
////	static public String[] getComputedStylePropertyNames(final Element element) {
////		return StyleHelper.getSupport().getComputedStylePropertyNames(element);
////	}
//
//	/**
//	 * Adds a new class (styleName) to the given element only if it is not
//	 * present.
//	 * 
//	 * @param element
//	 * @param className
//	 */
//	static public void XXXaddClass(final Element element, final String className) {
//		ObjectHelper.checkNotNull("parameter:element", element);
//		StyleHelper.checkClass("parameter:className", className);
//
//		// only add if not already present.
//		if (false == hasClass(element, className)) {
//			String newValue = ObjectHelper.getString(element, StyleConstants.CLASS_NAME);
//			if (newValue.length() > 0) {
//				newValue = newValue + " ";
//			}
//			newValue = newValue + className;
//			ObjectHelper.setString(element, StyleConstants.CLASS_NAME, newValue);
//		}
//
//	}
//
//	/**
//	 * Tests if the given element already has the given class.
//	 * 
//	 * @param element
//	 * @param className
//	 * @return
//	 */
//	static public boolean hasClass(final Element element, final String className) {
//		return -1 != indexOfClass(element, className);
//	}
//
//	/**
//	 * Helper method used internally to locate an existing classname within an
//	 * existing classname.
//	 * 
//	 * @param element
//	 * @param className
//	 * @return
//	 */
//	static int XXXindexOfClass(final Element element, final String className) {
//		ObjectHelper.checkNotNull("parameter:element", element);
//		StyleHelper.checkClass("parameter:className", className);
//
//		int found = -1;
//
//		final String classNameAttribute = ObjectHelper.getString(element, StyleConstants.CLASS_NAME);
//		if (false == StringHelper.isNullOrEmpty(classNameAttribute)) {
//			final String[] classNames = StringHelper.split(classNameAttribute, " ", true);
//			int index = 0;
//
//			for (int i = 0; i < classNames.length; i++) {
//				final String otherClassName = classNames[i];
//				if (className.equals(otherClassName)) {
//					found = index;
//					break;
//				}
//				index = index + otherClassName.length();
//			}
//		}
//
//		return found;
//	}
//
//	/**
//	 * Removes an existing class(styleName) from the given element.
//	 * 
//	 * @param element
//	 * @param className
//	 */
//	static public void XXXremoveClass(final Element element, final String className) {
//		final int index = indexOfClass(element, className);
//		if (-1 != index) {
//			String classes = ObjectHelper.getString(element, StyleConstants.CLASS_NAME);
//
//			int beforeIndex = index;
//			int afterIndex = index + className.length();
//
//			// if NOT first remove the space before...
//			if (index > 0) {
//				afterIndex++;
//			}
//
//			// if className is the last remove the space before...
//			classes = classes.substring(0, beforeIndex) + classes.substring(afterIndex);
//			ObjectHelper.setString(element, StyleConstants.CLASS_NAME, classes);
//		}
//	}
//
//	/**
//	 * Verifies that the given class name is valid.
//	 * 
//	 * @param name
//	 * @param className
//	 */
//	static void XXXcheckClass(final String name, final String className) {
//		StringHelper.checkNotEmpty(name, className);
//	}
}