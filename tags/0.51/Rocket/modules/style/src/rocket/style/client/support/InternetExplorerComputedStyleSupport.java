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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

public class InternetExplorerComputedStyleSupport extends InternetExplorerStyleSupport {

	@Override
	public String get(final JavaScriptObject element, final String propertyName) {
		String propertyValue = null;
		while (true) {
			if (Css.BACKGROUND_POSITION.equals(propertyName)) {
				propertyValue = this.getBackgroundPosition(JavaScript.castToElement(element));
				break;
			}
			if (propertyName.equals(Css.FONT_SIZE)) {
				final int fontSize = this.getFontSize(JavaScript.castToElement(element));
				if (-1 != fontSize) {
					propertyValue = fontSize + "px";
				}
				break;
			}
			if (Css.HEIGHT.equals(propertyName)) {
				propertyValue = this.getHeight(JavaScript.castToElement(element));
				break;
			}
			if (Css.OPACITY.equals(propertyName)) {
				propertyValue = this.getOpacity(element);
				break;
			}
			if (Css.WIDTH.equals(propertyName)) {
				propertyValue = this.getWidth(JavaScript.castToElement(element));
				break;
			}
			propertyValue = super.get(element, propertyName);
			break;
		}

		return propertyValue;
	}

	protected String getUserSelect(final JavaScriptObject element) {
		return this.getUserSelectFunction(element, true);
	}

	protected String getHeight(final Element element) {
		final int offsetHeight = element.getOffsetHeight();
		final int borderTopHeight = this.getBorderWidth(element, Css.BORDER_TOP_WIDTH);
		final int paddingTop = this.getPixelProperty(element, Css.PADDING_TOP);
		final int paddingBottom = this.getPixelProperty(element, Css.PADDING_BOTTOM);
		final int borderBottomHeight = this.getBorderWidth(element, Css.BORDER_BOTTOM_WIDTH);

		return (offsetHeight - borderTopHeight - paddingTop - paddingBottom - borderBottomHeight) + "px";
	}

	protected String getWidth(final Element element) {
		final int offsetWidth = element.getOffsetWidth();
		final int borderTopWidth = this.getBorderWidth(element, Css.BORDER_TOP_WIDTH);
		final int paddingTop = this.getPixelProperty(element, Css.PADDING_TOP);
		final int paddingBottom = this.getPixelProperty(element, Css.PADDING_BOTTOM);
		final int borderBottomWidth = this.getBorderWidth(element, Css.BORDER_BOTTOM_WIDTH);

		return (offsetWidth - borderTopWidth - paddingTop - paddingBottom - borderBottomWidth) + "px";
	}

	/**
	 * This method covers a special case only returning non zero values if a
	 * border style is also applicable.
	 * 
	 * @param element
	 * @param borderPropertyName
	 * @return
	 */
	protected int getBorderWidth(final Element element, final String borderPropertyName) {
		Checker.notNull("parameter:element", element);
		this.checkPropertyName("parameter:propertyName", borderPropertyName);

		return this.getBorderWidth0(element, borderPropertyName);
	}

	native private int getBorderWidth0(final Element element, final String borderPropertyName)/*-{
		 var value = 0;

		 while( true ){
		 var width = element.currentStyle[ borderPropertyName ];
		 if( ! width ){
		 value = 0;
		 break;
		 }

		 // if a width value is found check if a style is also set. if not return 0...
		 var styleName = borderPropertyName.substring( 0, borderPropertyName.length - 5 ) + "Style";
		 var borderStyle = element.currentStyle[ styleName ];

		 if( "none" == borderStyle ){
		 value = 0;
		 break;
		 }


		 if( isNaN( width )){
		 value = this.@rocket.style.client.support.StyleSupport::translateBorderWidth(Ljava/lang/String;)( width );
		 break;
		 }

		 value = 0 + width;
		 break;
		 }
		 return value;
		 }-*/;

	protected int getPixelProperty(final Element element, final String propertyName) {
		Checker.notNull("parameter:element", element);
		this.checkPropertyName("parameter:propertyName", propertyName);

		return this.getPixelProperty0(element, propertyName);
	}

	native private int getPixelProperty0(final Element element, final String propertyName)/*-{
		 var value = element.currentStyle[ propertyName ];
		 return isNaN( value ) ? parseInt( value ) : 0;
		 }-*/;

	/**
	 * Retrieves the computed background position of the given element.
	 * 
	 * @param element
	 * @return A string containing the combined values of both the x and y
	 *         positions.
	 */
	public String getBackgroundPosition(final JavaScriptObject element) {
		String value = null;

		while (true) {
			String x = this.getString(element, StyleSupportConstants.BACKGROUND_POSITION_X_IE6);
			String y = this.getString(element, StyleSupportConstants.BACKGROUND_POSITION_Y_IE6);

			final boolean xMissing = Tester.isNullOrEmpty(x);
			final boolean yMissing = Tester.isNullOrEmpty(y);
			if (xMissing && yMissing) {
				break;
			}
			if (xMissing) {
				x = StyleSupportConstants.DEFAULT_BACKGROUND_POSITION_X_IE6;
			}
			if (yMissing) {
				y = StyleSupportConstants.DEFAULT_BACKGROUND_POSITION_Y_IE6;
			}
			value = x + ' ' + y;
			break;
		}

		return value;
	}

	protected String getString(final JavaScriptObject element, final String name) {
		return this.getString0(element, name);
	}

	private native String getString0(final JavaScriptObject element, final String name)/*-{
		 var value = null;
		 var element0 = element;

		 while( element0 && element0.currentStyle ){
		 value = element0.currentStyle[ name ];

		 // continue looping until a concrete value is found.
		 if( value && value != "inherit" && value != "transparent" ){
		 break;
		 }
		 element0 = element0.parentNode;
		 }

		 return value ? "" + value : null;
		 }-*/;

	protected String getCssText(final JavaScriptObject element) {
		return this.buildCssText(element);
	}

	protected String buildCssText(final JavaScriptObject element) {
		return this.buildCssText0(element);
	}

	native private String buildCssText0(final JavaScriptObject element)/*-{
		 var names = new Array();
		 var style = element.currentStyle;

		 for( name in style ){
		 	var value = style[ name ];
		 	names.push( name + ": " + value );
		 }
		 return names.join( ", " );
		}-*/;

	protected void setUserSelect(final JavaScriptObject element, final String value) {
		throw new UnsupportedOperationException("setUserSelect");
	}

	protected void setString(JavaScriptObject element, final String name, final String value) {
		throw new UnsupportedOperationException("setString");
	}

	protected void remove0(JavaScriptObject element, final String name) {
		throw new UnsupportedOperationException("remove0");
	}

	public String[] getPropertyNames(final JavaScriptObject element) {
		return JavaScript.getPropertyNames(JavaScript.getObject(element, "currentStyle"));
	}
}
