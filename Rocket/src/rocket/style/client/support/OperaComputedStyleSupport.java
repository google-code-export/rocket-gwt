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
import rocket.style.client.CssUnit;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;

public class OperaComputedStyleSupport extends OperaStyleSupport {
	
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
	 * @param name
	 * @return
	 *
	 * @override
	 */
	public String get(final JavaScriptObject element, final String name) {
		String value = null;
		while (true) {
			if (Css.WIDTH.equals(name)) {
				value = this.getWidth(element) + "px";
				break;
			}
			if (Css.HEIGHT.equals(name)) {
				value = this.getHeight(element) + "px";
				break;
			}
			if (Css.FONT_WEIGHT.equals( name)) {
				value = "" + this.getComputedFontWeight( ObjectHelper.castToElement( element));
				break;
			}

			value = super.get(element, name);
			break;
		}
		return value;
	}
	
	protected String getUserSelect( final JavaScriptObject element ){
		return this.getUserSelectFunction(element, true );
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
	protected int getWidth(final JavaScriptObject element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final int offsetWidth = ObjectHelper.getInteger(element, "offsetWidth");

		final int borderLeft = this.getPixelProperty(element, Css.BORDER_LEFT_WIDTH);
		final int borderRight = this.getPixelProperty(element, Css.BORDER_RIGHT_WIDTH);

		final int paddingLeft = this.getPixelProperty(element, Css.PADDING_LEFT);
		final int paddingRight = this.getPixelProperty(element, Css.PADDING_RIGHT);

		return offsetWidth - borderLeft - borderRight - paddingLeft - paddingRight;
	}

	/**
	 * Retrieves the content height of the given element
	 *
	 * @param element
	 * @return
	 */
	protected int getHeight(final JavaScriptObject element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final int offsetHeight = ObjectHelper.getInteger(element, "offsetHeight");

		final int borderTop = this.getPixelProperty(element, Css.BORDER_TOP_WIDTH);
		final int borderBottom = this.getPixelProperty(element, Css.BORDER_BOTTOM_WIDTH);

		final int paddingTop = this.getPixelProperty(element, Css.PADDING_TOP);
		final int paddingBottom = this.getPixelProperty(element, Css.PADDING_BOTTOM);

		return offsetHeight - borderTop - borderBottom - paddingTop - paddingBottom;
	}

	protected int getPixelProperty(final JavaScriptObject element, final String name) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.checkPropertyName("parameter:name", name);

		final String value = this.get(element, name);
		return (int) CssUnit.convertValue(value, CssUnit.PX);
	}
	
	protected String getString( final JavaScriptObject element, final String name ){
		return this.getComputed( element, name);
	}
	
	protected void setUserSelect( final JavaScriptObject element, final String value ){
		throw new UnsupportedOperationException("setString");
	}
	
	protected void setString( JavaScriptObject element, final String name, final String value ){
		throw new UnsupportedOperationException( "setString");
	}
	
	protected void remove0( JavaScriptObject element, final String name ){
		throw new UnsupportedOperationException("remove0");
	}
	public String[] getPropertyNames( final JavaScriptObject element ){
		return this.getPropertyNamesFromCssText(element);
	}
}
