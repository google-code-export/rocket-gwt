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

import rocket.style.client.CssUnit;
import rocket.style.client.StyleConstants;
import rocket.style.client.StyleHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

/**
 * Modifies some methods to cater for some Opera quirks.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class OperaStyleSupport extends StyleSupport {

	/**
	 * Retrieves the computed style property value.
	 * 
	 * <h6>Special cases</h6>
	 * <ul>
	 * <li>width</li>
	 * <li>height</li>
	 * </ul>
	 * 
	 * @param element
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 * 
	 * @override
	 */
	public String getComputedStyleProperty(final Element element, final String propertyName) {
		String value = null;
		while (true) {
			if (StyleConstants.WIDTH.equals(propertyName)) {
				value = this.getComputedWidth(element) + "px";
				break;
			}
			if (StyleConstants.HEIGHT.equals(propertyName)) {
				value = this.getComputedHeight(element) + "px";
				break;
			}
			value = super.getComputedStyleProperty(element, propertyName);
			break;
		}
		return value;
	}

	/**
	 * Retrieves the content width of the given element
	 * 
	 * The content width may be calculated using the following formula:
	 * 
	 * contentWidth = offsetWidth - paddingLeft - paddingRight - borderLeftWidth -
	 * borderRightWidth
	 * 
	 * @param element
	 * @return
	 */
	protected int getComputedWidth(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final int offsetWidth = ObjectHelper.getInteger(element, "offsetWidth");

		final int borderLeft = this.getComputedStyleInPixels(element, StyleConstants.BORDER_LEFT_WIDTH);
		final int borderRight = this.getComputedStyleInPixels(element, StyleConstants.BORDER_RIGHT_WIDTH);

		final int paddingLeft = this.getComputedStyleInPixels(element, StyleConstants.PADDING_LEFT);
		final int paddingRight = this.getComputedStyleInPixels(element, StyleConstants.PADDING_RIGHT);

		return offsetWidth - borderLeft - borderRight - paddingLeft - paddingRight;
	}

	/**
	 * Retrieves the content height of the given element
	 * 
	 * @param element
	 * @return
	 */
	protected int getComputedHeight(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final int offsetHeight = ObjectHelper.getInteger(element, "offsetHeight");

		final int borderTop = this.getComputedStyleInPixels(element, StyleConstants.BORDER_TOP_WIDTH);
		final int borderBottom = this.getComputedStyleInPixels(element, StyleConstants.BORDER_BOTTOM_WIDTH);

		final int paddingTop = this.getComputedStyleInPixels(element, StyleConstants.PADDING_TOP);
		final int paddingBottom = this.getComputedStyleInPixels(element, StyleConstants.PADDING_BOTTOM);

		return offsetHeight - borderTop - borderBottom - paddingTop - paddingBottom;
	}

	protected int getComputedStyleInPixels(final Element element, final String propertyName) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.checkPropertyName("parameter:propertyName", propertyName);

		final String value = this.getComputedStyleProperty(element, propertyName);
		return (int) StyleHelper.convertValue(value, CssUnit.PX);
	}

	public void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		StringHelper.checkNotNull("parameter:selectorText", selectorText);
		StringHelper.checkNotNull("parameter:styleText", styleText);

		this.insertRule0(styleSheet, index, selectorText, styleText);
	}

	/**
	 * This method takes care of appending the new rule and appending the rules
	 * that have an index greater than the given index.
	 */
	native private void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText)/*-{
	 var cssText = selectorText + "{" + styleText + "}";
	 
	 var rules = styleSheet.cssRules;

	 // inserting a rule into an Opera9 styleSheet really appends it...
	 styleSheet.insertRule( cssText, rules.length );   
	 
	 // need to delete all the rules with a higher index and append them so eventually the new "inserted" rule will be in the correct slot...         
	 var lastIndex = rules.length - 1;
	 
	 for( var j = index; j < lastIndex; j++ ){
	 // remember the original selector/style 
	 var rule = rules[ index ];
	 var selector = rule.selectorText;
	 var styleText = rule.style.cssText;
	 
	 styleSheet.deleteRule( index );
	 
	 // append the rule...
	 var ruleText = selector + "{" + styleText + "}";
	 styleSheet.insertRule( ruleText, rules.length );
	 }
	 
	 // when this stage is reached the rules order should be correct.
	 }-*/;

	public void normalize(final JavaScriptObject styleSheet) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		this.normalize0(styleSheet);
	}

	native private void normalize0(final JavaScriptObject styleSheet) /*-{
	 var rules = styleSheet.cssRules;
	 var i = 0;
	 
	 // skip until a rule with more than one selector is found....
	 while( i < rules.length ){
	 var rule = rules[ i ];
	 var selectorText = rule.selectorText;
	 if( selectorText.indexOf( ",") == -1 ){
	 i++;
	 continue;
	 }
	 
	 // found a rule with more than one selector...
	 var ruleCount = rules.length - i;
	 for( var j = 0; j < ruleCount; j++ ){
	 var rule = rules[ i ];
	 var selectorText = rule.selectorText;
	 var selectors = selectorText.split( "," );
	 var selectorCount = selectors.length;
	 var styleText = rule.style.cssText;
	 
	 // delete the rule...
	 styleSheet.deleteRule( i );
	 
	 // recreate the n rules one for each selector...
	 for( var k = 0; k < selectorCount; k++ ){
	 var cssText = selectors[ k ] + "{" + styleText + "}";
	 
	 // opera 9x inserts are really appends...
	 styleSheet.insertRule( cssText, rules.length );
	 }
	 }         
	 break;
	 }
	 }-*/;
}
