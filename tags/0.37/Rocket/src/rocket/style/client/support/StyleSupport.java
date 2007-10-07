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
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * This and sub-classes contain the browser specific jsni stuff for StyleHelper.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class StyleSupport {

	public StyleSupport() {
		super();
	}

	/**
	 * Retrieves the style sheet collection attached to this document
	 * 
	 * @return
	 */
	native public JavaScriptObject getStyleSheetCollection()/*-{
	 var styleSheet = $doc.styleSheets;
	 return styleSheet ? styleSheet : null;
	 }-*/;

	/**
	 * Retrieves a property value from the style belonging to a Rule
	 * 
	 * @param element
	 * @param propertyName
	 * @return
	 */
	public String getRuleStyleProperty(final JavaScriptObject rule, final String propertyName) {
		return this.getRuleStyleProperty0(rule, propertyName);
	}

	protected String getRuleStyleProperty0(final JavaScriptObject rule, final String propertyName) {
		final String value = ObjectHelper.getString(this.getStyle(rule), propertyName);
		return this.translateNoneValuesToNull(propertyName, value);
	}

	/**
	 * Sets a style property belonging to a rule with a new value.
	 * 
	 * @param element
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setRuleStyleProperty(final JavaScriptObject rule, final String propertyName, final String propertyValue) {
		this.setRuleStyleProperty0(rule, propertyName, propertyValue);
	}

	protected void setRuleStyleProperty0(final JavaScriptObject rule, final String propertyName, final String propertyValue) {
		this.checkPropertyName("parameter:propertyName", propertyName);

		ObjectHelper.setString(this.getStyle(rule), propertyName, propertyValue);
	}

	/**
	 * Removes a style property belonging to a Rule
	 * 
	 * @param rule
	 * @param propertyName
	 */
	public void removeRuleStyleProperty(final JavaScriptObject rule, final String propertyName) {
		this.removeRuleStyleProperty0(rule, propertyName);
	}

	protected void removeRuleStyleProperty0(final JavaScriptObject rule, final String propertyName) {
		this.removeRuleStyleProperty1(rule, this.toCssPropertyName(propertyName));
	}

	native private void removeRuleStyleProperty1(final JavaScriptObject rule, final String propertyName)/*-{
	 rule.style.removeProperty( propertyName );
	 }-*/;

	public String[] getRuleStylePropertyNames(final JavaScriptObject rule) {
		final String list = getRuleStylePropertyNames0(rule);
		return StringHelper.split(list, ",", true);
	}

	protected String getRuleStylePropertyNames0(final JavaScriptObject rule) {
		return getRuleStylePropertyNames1(rule);
	}

	native private String getRuleStylePropertyNames1(final JavaScriptObject rule)/*-{
	 var style = rule.style;
	 
	 var names = new Array();
	 var i = 0;
	 while( true ){	 	
	 var n = style[ i ];
	 if( ! n ){
	 break;
	 }

	 names.push( @rocket.util.client.StringHelper::toCamelCase(Ljava/lang/String;)( n ));
	 i++;	 				
	 }
	 
	 return names.join( "," );
	 }-*/;

	/**
	 * Retrieves an inline style property by name.
	 * 
	 * @param element
	 * @param propertyName
	 * @return
	 */
	public String getInlineStyleProperty(final Element element, final String propertyName) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.checkPropertyName("parameter:propertyName", propertyName);

		String propertyValue = null;
		while (true) {
			if (propertyName.startsWith("border") && propertyName.endsWith("Width")) {
				propertyValue = this.getInlineStyleBorderWidth(element, propertyName);
				break;
			}
			if (StyleConstants.USER_SELECT.equals(propertyName)) {
				propertyValue = this.getInlineStyleUserSelect(element);
				break;
			}
			propertyValue = this.getInlineStyleProperty0(element, propertyName);
			propertyValue = this.translateNoneValuesToNull(propertyName, propertyValue);
			break;
		}

		return propertyValue;
	}

	protected String getInlineStyleProperty0(final Element element, final String propertyName) {
		final JavaScriptObject style = this.getStyle(element);
		return ObjectHelper.getString(style, propertyName);
	}

	protected String getInlineStyleBorderWidth(final Element element, final String propertyName) {
		final String propertyValue = this.getInlineStyleProperty0(element, propertyName);
		return propertyValue == null ? null : this.translateBorderWidthValue(propertyValue) + "px";
	}

	protected String getInlineStyleUserSelect(final Element element) {
		final String propertyName = this.getUserSelectPropertyName();
		final String propertyValue = this.getInlineStyleProperty0(element, propertyName);
		return "auto".equals(propertyValue) ? null : propertyValue;
	}

	/**
	 * Sets an inline style property name with a new value.
	 * 
	 * @param element
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setInlineStyleProperty(final Element element, final String propertyName, final String propertyValue) {
		while (true) {
			if (StyleConstants.USER_SELECT.equals(propertyName)) {
				this.setInlineUserSelect(element, propertyValue);
				break;
			}

			this.setInlineStyleProperty0(element, propertyName, propertyValue);
			break;
		}
	}

	protected void setInlineUserSelect(final Element element, final String propertyValue) {
		final String propertyName0 = this.getUserSelectPropertyName();
		this.setInlineStyleProperty0(element, propertyName0, propertyValue);
	}

	protected void setInlineStyleProperty0(final Element element, final String propertyName, final String propertyValue) {
		final JavaScriptObject style = this.getStyle(element);
		ObjectHelper.setString(style, propertyName, propertyValue);
	}

	public void removeInlineStyleProperty(final Element element, final String propertyName) {
		while (true) {
			if (StyleConstants.USER_SELECT.equals(propertyName)) {
				this.removeInlineUserSelect(element);
				break;
			}
			this.removeInlineStyleProperty0(element, propertyName);
			break;
		}
	}

	protected void removeInlineStyleProperty0(final Element element, final String propertyName) {
		this.removeInlineStyleProperty1(element, this.toCssPropertyName(propertyName));
	}

	native private void removeInlineStyleProperty1(final Element element, final String propertyName)/*-{
	 element.style.removeProperty( propertyName );
	 }-*/;

	protected void removeInlineUserSelect(final Element element) {
		final String propertyName = this.toCssPropertyName(this.getUserSelectPropertyName());
		this.removeInlineStyleProperty0(element, propertyName);
	}

	/**
	 * Computes the value of the requested propertyName for the given element
	 * 
	 * @param element
	 * @param propertyName
	 * @return The value or null if the value was not found.
	 */
	public String getComputedStyleProperty(final Element element, final String propertyName) {
		String propertyValue = null;
		while (true) {
			if (propertyName.startsWith("border") && propertyName.endsWith("Width")) {
				propertyValue = this.getComputedBorderWidth(element, propertyName);
				break;
			}
			if (StyleConstants.FONT_WEIGHT.equals(propertyName)) {
				propertyValue = "" + this.getComputedFontWeight(element);
				break;
			}
			if (StyleConstants.USER_SELECT.equals(propertyName)) {
				propertyValue = this.getComputedUserSelect(element);
				break;
			}

			propertyValue = this.getComputedStyleProperty0(element, propertyName);
			propertyValue = this.translateNoneValuesToNull(propertyName, propertyValue);
			break;
		}

		return propertyValue;
	}

	/**
	 * Returns the computed style property for a given element
	 * 
	 * @param element
	 *            The css property name being queried.
	 * @param propertyName
	 * @return
	 */
	protected String getComputedStyleProperty0(final Element element, final String propertyName) {
		final String propertyName0 = this.toCssPropertyName(propertyName);
		return this.getComputedStyleProperty1(element, propertyName0);
	}

	native private String getComputedStyleProperty1(final Element element, final String propertyName)/*-{
	 var value = null;

	 var element0 = element;
	 var stop = $doc.documentElement;

	 // loop until a concrete value is found.
	 while( element0 ){
	 //value = $wnd.getComputedStyle(element0,null).getPropertyValue( propertyName );
	 value = $doc.defaultView.getComputedStyle(element0,null).getPropertyValue( propertyName );

	 // continue looping until a concrete value is found.
	 if( value && value != "inherit" && value != "transparent" ){
	 break;
	 }
	 if( element0 == stop ){
	 value = null;
	 break;
	 }
	 element0 = element0.parentNode;
	 } 
	 return value;
	 }-*/;

	protected String getComputedBorderWidth(final Element element, final String propertyName) {
		final String propertyValue = this.getComputedStyleProperty0(element, propertyName);
		return propertyValue == null ? null : this.translateBorderWidthValue(propertyValue) + "px";
	}

	/**
	 * Calculates the font weight for the given element translating named values
	 * into numeric values.
	 * 
	 * @param element
	 * @return
	 */
	protected int getComputedFontWeight(final Element element) {
		int weight = -1;

		while (true) {
			final String propertyValue = getComputedStyleProperty0(element, StyleConstants.FONT_WEIGHT);
			if (StringHelper.isNullOrEmpty(propertyValue)) {
				weight = StyleSupportConstants.FONT_WEIGHT_NORMAL_VALUE;
				break;
			}
			// absolute weights...
			if (StyleSupportConstants.FONT_WEIGHT_NORMAL.equals(propertyValue)) {
				weight = StyleSupportConstants.FONT_WEIGHT_NORMAL_VALUE;
				break;
			}
			if (StyleSupportConstants.FONT_WEIGHT_BOLD.equals(propertyValue)) {
				weight = StyleSupportConstants.FONT_WEIGHT_BOLD_VALUE;
				break;
			}
			// relative weights...
			if (StyleSupportConstants.FONT_WEIGHT_BOLDER.equals(propertyValue)) {
				final Element parent = DOM.getParent(element);
				final int parentWeight = this.getComputedFontWeight(parent);
				weight = parentWeight + 300;
				break;
			}
			if (StyleSupportConstants.FONT_WEIGHT_LIGHTER.equals(propertyValue)) {
				final Element parent = DOM.getParent(element);
				final int parentWeight = this.getComputedFontWeight(parent);
				weight = parentWeight - 300;
				break;
			}
			weight = Integer.parseInt(propertyValue);
			break;
		}
		return weight;
	}

	/**
	 * Retrieves the computed font weight for the parent of the given element.
	 * 
	 * This method should only be called by
	 * {@link #getComputedFontWeight(Element)} when it encounters a font-weight
	 * of lighter or bolder.
	 * 
	 * @param element
	 * @return
	 */
	protected int getComputedFontWeightOfParent(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		Element parent = DOM.getParent(element);
		return this.getComputedFontWeight(parent);
	}

	/**
	 * @param element
	 * @return
	 */
	protected String getComputedUserSelect(final Element element) {
		final String propertyName = this.getUserSelectPropertyName();
		final String propertyValue = this.getComputedStyleProperty0(element, propertyName);
		return "auto".equals(propertyValue) ? null : propertyValue;
	}

	/**
	 * Retrieves the names of all the computed styles available for the given
	 * element.
	 * 
	 * @param element
	 * @return
	 */
	public String[] getComputedStylePropertyNames(final Element element) {
		final JavaScriptObject style = this.getStyle(element);
		final String cssText = ObjectHelper.getString(style, "cssText");

		// remove any quotes...
		final StringBuffer names = new StringBuffer();
		final String[] tokens = StringHelper.split(cssText, " ", false);

		for (int i = 0; i < tokens.length; i++) {
			final String property = tokens[i];
			if (property.endsWith(":")) {
				final String nameLessColon = property.substring(0, property.length() - 1);
				names.append(StringHelper.toCamelCase(nameLessColon));
			}
		}

		return StringHelper.split(names.toString(), ",", true);
	}

	/**
	 * Translate one of the three word border values into a string containing
	 * the pixel value
	 * 
	 * @param value
	 * @return
	 */
	protected int translateBorderWidthValue(final String value) {
		StringHelper.checkNotEmpty("parameter:value", value);

		int number = 0;
		while (true) {
			if (StyleSupportConstants.BORDER_WIDTH_THIN.equals(value)) {
				number = this.getBorderWidthThin();
				break;
			}
			if (StyleSupportConstants.BORDER_WIDTH_MEDIUM.equals(value)) {
				number = this.getBorderWidthMedium();
				break;
			}
			if (StyleSupportConstants.BORDER_WIDTH_THICK.equals(value)) {
				number = this.getBorderWidthThick();
				break;
			}
			number = Integer.parseInt(value.endsWith("px") ? value.substring(0, value.length() - 2) : value);
			break;
		}
		return number;
	}

	/**
	 * The three methods below are overridden in the InternetExplorer
	 * implementation as the three constants have different pixel values.
	 * 
	 * @return
	 */
	protected int getBorderWidthThin() {
		return StyleSupportConstants.BORDER_WIDTH_THIN_PX;
	}

	protected int getBorderWidthMedium() {
		return StyleSupportConstants.BORDER_WIDTH_MEDIUM_PX;
	}

	protected int getBorderWidthThick() {
		return StyleSupportConstants.BORDER_WIDTH_THICK_PX;
	}

	/**
	 * This method transforms any none values into null. Many image related
	 * properties such as
	 * <ul>
	 * <li>background-image</li>
	 * <li>list-image</li>
	 * </ul>
	 * return none when the property is not set.
	 * 
	 * @param name
	 *            The name of the property
	 * @param value
	 * @return
	 */
	protected String translateNoneValuesToNull(final String name, final String value) {
		// if value== none and not usertextselect return null else return value.
		return false == StyleConstants.USER_SELECT.equals(name) && "none".equals(value) ? null : value;
	}

	/**
	 * Helper which retrieves the style object belonging to an Element or Rule.
	 * 
	 * @param elementOrRule
	 * @return
	 */
	protected JavaScriptObject getStyle(final JavaScriptObject elementOrRule) {
		return ObjectHelper.getObject(elementOrRule, "style");
	}

	/**
	 * This method takes care of translating a javascript styled propertyName
	 * into a css property name.
	 * 
	 * It also translates the non standard UserSelect property into the
	 * appropriate value for each respective browser.
	 * 
	 * @param propertyName
	 * @return
	 */
	protected String toCssPropertyName(final String propertyName) {
		return StringHelper.toCssPropertyName(propertyName);
	}

	/**
	 * Sub-classes will need to override this method to return the css property
	 * name for their respective browser.
	 * 
	 * @return
	 */
	protected String getUserSelectPropertyName() {
		return StyleConstants.USER_SELECT;
	}

	/**
	 * Helper which retrieves the native rules collection that this instance is
	 * presenting as a List
	 * 
	 * @return
	 */
	public JavaScriptObject getRulesCollection(final JavaScriptObject styleSheet) {
		return ObjectHelper.getObject(styleSheet, StyleConstants.RULES_LIST_PROPERTY);
	}

	public void addRule(final JavaScriptObject styleSheet, final String selectorText, final String styleText) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		StringHelper.checkNotNull("parameter:selectorText", selectorText);
		StringHelper.checkNotNull("parameter:styleText", styleText);

		this.addRule0(styleSheet, selectorText, styleText);
	}

	native private void addRule0(final JavaScriptObject styleSheet, final String selectorText, final String styleText)/*-{
	 var cssText = selectorText + "{" + styleText + "}";    
	 var index = styleSheet.cssRules.length;
	 styleSheet.insertRule( cssText, index );
	 }-*/;

	public void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		StringHelper.checkNotNull("parameter:selectorText", selectorText);
		StringHelper.checkNotNull("parameter:styleText", styleText);

		this.insertRule0(styleSheet, index, selectorText, styleText);
	}

	native private void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText)/*-{
	 var cssText = selectorText + "{" + styleText + "}";
	 styleSheet.insertRule( cssText, index );   
	 }-*/;

	public void removeRule(final JavaScriptObject styleSheet, final int index) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);

		this.removeRule0(styleSheet, index);
	}

	/**
	 * Escapes to javascript to delete the requested rule.
	 */
	native private void removeRule0(final JavaScriptObject styleSheet, final int index) /*-{
	 styleSheet.deleteRule( index );
	 }-*/;

	public void normalize(final JavaScriptObject styleSheet) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		this.normalize0(styleSheet);
	}

	native private void normalize0(final JavaScriptObject styleSheet) /*-{
	 var rules = styleSheet.cssRules;
	 var i = 0;

	 while( i < rules.length ){
	 var rule = rules[ i ];
	 var selectorText = rule.selectorText;
	 var selectors = selectorText.split( "," );
	 var selectorCount = selectors.length;
	 if( 1 == selectorCount ){
	 i++;   
	 continue;
	 }
	 
	 var styleText = rule.style.cssText;
	 
	 // delete the original rule...
	 styleSheet.deleteRule( i );
	 
	 // recreate n rules one for each selector with the same style value...
	 for( var j = 0; j < selectorCount; j++ ){
	 var ruleText = selectors[ j ] + "{" + styleText + "}";
	 styleSheet.insertRule( ruleText, i );
	 i++;            
	 } // for j
	 } // while
	 }-*/;

	/**
	 * Checks and that the given style property name is valid, throwing an
	 * exception if it is not
	 * 
	 * @param name
	 * @param propertyName
	 */
	protected void checkPropertyName(final String name, final String propertyName) {
		StringHelper.checkNotEmpty(name, propertyName);
	}

	/**
	 * Checks that the style property value is valid, throwing an exception if
	 * it is not
	 * 
	 * @param name
	 * @param propertyValue
	 */
	protected void checkPropertyValue(final String name, final String propertyValue) {
		StringHelper.checkNotNull(name, propertyValue);
	}
}
