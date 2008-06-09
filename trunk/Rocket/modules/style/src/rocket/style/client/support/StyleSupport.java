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

import rocket.style.client.Css;
import rocket.util.client.Checker;
import rocket.util.client.JavaScript;
import rocket.util.client.Tester;
import rocket.util.client.Utilities;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

abstract public class StyleSupport {

	public String get(final JavaScriptObject source, final String name) {
		String value = null;

		while (true) {
			if (name.startsWith("border") && name.endsWith("Width")) {
				value = this.getBorderWidth(source, name);
				break;
			}
			if (Css.CSS_STYLE_TEXT_PROPERTY_NAME.equals(name)) {
				value = this.getCssText(source);
				break;
			}
			if (Css.USER_SELECT.equals(name)) {
				value = this.getUserSelect(source);
				break;
			}

			value = this.getString(source, name);

			// cleanup none values...
			value = this.translateNoneValuesToNull(name, value);
			break;
		}

		return value;
	}

	protected String getBorderWidth(final JavaScriptObject source, final String name) {
		final String value = this.getString(source, name);
		return value == null ? null : this.translateBorderWidth(value) + "px";
	}

	protected int translateBorderWidth(final String value) {
		Checker.notEmpty("parameter:value", value);

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
	 * Reads the special cssText property from the element's inline style. Even
	 * computed style subclasses should not attempt to read this property using
	 * {@link #getCssText(JavaScriptObject)}
	 * 
	 * @param element
	 * @return
	 */
	protected String getCssText(final JavaScriptObject element) {
		return getCssText0(element);
	}

	native private String getCssText0(final JavaScriptObject element)/*-{
			return element.style.cssText;
		}-*/;

	abstract protected String getUserSelect(JavaScriptObject elementOrRule);

	protected String getUserSelectProperty(final JavaScriptObject source) {
		final String propertyName = this.getUserSelectPropertyName();

		return this.getString(source, propertyName);
	}

	abstract protected String getUserSelectPropertyName();

	protected String getUserSelectFunction(final JavaScriptObject element, final boolean walkHeirarchy) {
		return this.getUserSelectFunction0(element, walkHeirarchy);
	}

	native private String getUserSelectFunction0(final JavaScriptObject element, final boolean walkHeirarchy)/*-{
		var value = null;
		var body = element.ownerDocument.body;
		var element0 = element;
		
		while( element0 != body ){
			var f = element0.ondrag;
	 		if( f ){
	 			value = "none";
	 			break;
	 		}
	 		
			element0 = element0.parentNode;
			
			if( false == walkHeirarchy ){
				break;
			}
		}
		
	 	return value;
		 }-*/;

	abstract protected String getString(final JavaScriptObject source, final String name);

	protected String getProperty(final JavaScriptObject source, final String name) {
		return this.getProperty0(source, name);
	}

	native protected String getProperty0(final JavaScriptObject source, final String name)/*-{
			return source.style[ name ] || null;
		}-*/;

	protected String getComputed(final JavaScriptObject element, final String name) {
		return this.getComputed0(element, Utilities.toCssPropertyName(name));
	}

	native private String getComputed0(final JavaScriptObject element, final String name)/*-{
		 var value = null;
		 var element0 = element;
		 var doc = element.ownerDocument;

		 // loop until a concrete value is found.
		 while( element0 != stop ){		 	
		 	value = document.defaultView.getComputedStyle(element0,null).getPropertyValue( name );

		 	// continue looping until a concrete value is found.
		 	if( value && value != "inherit" && value != "transparent" && value != "auto" ){
		 		break;
		 	}
		 	
		 	element0 = element0.parentNode;
		 }
		 return value;
	}-*/;

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
			final String propertyValue = getComputed(element, Css.FONT_WEIGHT);
			if (Tester.isNullOrEmpty(propertyValue)) {
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
				final Element parent = DOM.getParent(JavaScript.castToElement(element));
				final int parentWeight = this.getComputedFontWeight(parent);
				weight = parentWeight + 300;
				break;
			}
			if (StyleSupportConstants.FONT_WEIGHT_LIGHTER.equals(propertyValue)) {
				final Element parent = DOM.getParent(JavaScript.castToElement(element));
				if (null != parent) {
					final int parentWeight = this.getComputedFontWeight(parent);
					weight = parentWeight - 300;
				}
				break;
			}
			weight = Integer.parseInt(propertyValue);
			break;
		}
		return weight;
	}

	public void set(final JavaScriptObject source, final String name, final String value) {
		while (true) {
			if (Css.USER_SELECT.equals(name)) {
				this.setUserSelect(source, value);
				break;
			}
			this.setString(source, name, value);
			break;
		}
	}

	abstract protected void setUserSelect(JavaScriptObject source, String value);

	protected void setUserSelectProperty(final JavaScriptObject source, final String value) {
		this.setString(source, this.getUserSelectPropertyName(), value);
	}

	protected void setUserSelectFunction(final JavaScriptObject element, final String value) {
		final boolean enable = false == "none".equals(value);
		this.setUserSelectFunction0(element, enable);
	}

	native private void setUserSelectFunction0(final JavaScriptObject element, final boolean enable)/*-{
		 var f = enable ? null : function(){ return false };
		 element.ondrag = f;
		 element.onselectstart = f;
		 }-*/;

	abstract protected void setString(JavaScriptObject source, final String name, final String value);

	protected void setProperty(final JavaScriptObject elementOrRule, final String name, final String value) {
		this.setProperty0(elementOrRule, name, value);
	}

	native private void setProperty0(final JavaScriptObject elementOrRule, final String name, final String value)/*-{
			elementOrRule.style[ name ] = value;
		}-*/;

	public void remove(final JavaScriptObject source, final String name) {
		while (true) {
			if (Css.USER_SELECT.equals(name)) {
				this.removeUserSelect(source);
			}
			break;
		}
		this.remove0(source, name);
	}

	protected void removeUserSelect(final JavaScriptObject source) {
		this.remove0(source, this.getUserSelectPropertyName());
	}

	abstract protected void remove0(JavaScriptObject source, final String name);

	protected void removeProperty(final JavaScriptObject element, final String propertyName) {
		this.removeProperty0(element, Utilities.toCssPropertyName(propertyName));
	}

	native private void removeProperty0(final JavaScriptObject element, final String propertyName)/*-{
		 	element.style.removeProperty( propertyName );
		 }-*/;

	protected String[] getPropertyNamesFromCssText(final JavaScriptObject elementOrRule) {
		final String cssText = this.get(elementOrRule, Css.CSS_STYLE_TEXT_PROPERTY_NAME);

		// remove any quotes...
		final StringBuffer names = new StringBuffer();
		final String[] tokens = Utilities.split(cssText, " ", true);

		for (int i = 0; i < tokens.length; i++) {
			final String property = tokens[i];
			if (property.endsWith(":")) {
				names.append(Utilities.toCamelCase(property));
			}
		}

		return Utilities.split(names.toString(), ":", true);
	}

	protected String buildCssText(final JavaScriptObject element) {
		return this.buildCssText0(element);
	}

	native private String buildCssText0(final JavaScriptObject element)/*-{
		 var names = new Array();
		 var style = element.style;

		 for( name in style ){
		 	var value = style[ name ];
		 	names.push( name + ": " + value );
		 }
		 return names.join( ", " );
		}-*/;

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

	protected String translateNoneValuesToNull(final String name, final String value) {
		// if value== none and not usertextselect return null else return value.
		return false == Css.USER_SELECT.equals(name) && "none".equals(value) ? null : value;
	}

	/**
	 * Depending on the type of Style this method should delegate to one of the
	 * following methods.
	 * <ul>
	 * <li>{@link #getPropertyNamesFromCssText(JavaScriptObject)} used by
	 * computed FireFox, Opera and Safari styles.</li>
	 * <li>{@link #getPropertyNamesByIndex(JavaScriptObject)} used by all
	 * inline styles and computed Internet Explorer style.</li>
	 * </ul>
	 */
	abstract public String[] getPropertyNames(JavaScriptObject elementOrRule);

	/**
	 * Checks and that the given style property name is valid, throwing an
	 * exception if it is not
	 * 
	 * @param name
	 * @param propertyName
	 */
	protected void checkPropertyName(final String name, final String propertyName) {
		Checker.notEmpty(name, propertyName);
	}
}
