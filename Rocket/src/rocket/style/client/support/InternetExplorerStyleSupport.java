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
import rocket.style.client.Css;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

abstract public class InternetExplorerStyleSupport extends StyleSupport{

	public String get( final JavaScriptObject source, final String name ){
		String value = null;
		
		while( true ){
			if( Css.OPACITY.equals( name )){
				value = this.getOpacity(source);
				break;
			}			
			value = super.get(source, name);			
			break;
		}
		
		return value;
	}
	
	protected String getOpacity( final JavaScriptObject source ){
		final String value = this.getString( source, StyleSupportConstants.FILTER );
		
		String opacity = null;
		if (false == StringHelper.isNullOrEmpty(value)) {
			opacity = value.substring("alpha(opacity=".length(), value.length() - 1);
			if (opacity.length() < 3) {
				opacity = "0." + opacity;
			} else {
				opacity = opacity.substring(0, 1) + '.' + opacity.substring(1, 3);
			}
		}
		return opacity;
	}
	
	protected int getBorderWidthThin() {
		return StyleSupportConstants.BORDER_WIDTH_THIN_PX_IE6;
	}

	protected int getBorderWidthMedium() {
		return StyleSupportConstants.BORDER_WIDTH_MEDIUM_PX_IE6;
	}

	protected int getBorderWidthThick() {
		return StyleSupportConstants.BORDER_WIDTH_THICK_PX_IE6;
	}
	

	/**
	 * Retrieves the computed font size for the given element taking care of
	 * absolute and relative sizes.
	 *
	 * @param element
	 * @return
	 */
	protected int getFontSize(final Element element) {
		int size = -1;
		while (true) {
			final String propertyValue = getString(element, Css.FONT_SIZE);
			if (StringHelper.isNullOrEmpty(propertyValue)) {
				size = -1;
				break;
			}
			// absolute sizes...
			if (StyleSupportConstants.FONT_SIZE_X_SMALL.equals(propertyValue)) {
				size = StyleSupportConstants.FONT_SIZE_X_SMALL_PX;
				break;
			}
			if (StyleSupportConstants.FONT_SIZE_SMALL.equals(propertyValue)) {
				size = StyleSupportConstants.FONT_SIZE_SMALL_PX;
				break;
			}
			if (StyleSupportConstants.FONT_SIZE_MEDIUM.equals(propertyValue)) {
				size = StyleSupportConstants.FONT_SIZE_MEDIUM_PX;
				break;
			}
			if (StyleSupportConstants.FONT_SIZE_LARGE.equals(propertyValue)) {
				size = StyleSupportConstants.FONT_SIZE_LARGE_PX;
				break;
			}
			if (StyleSupportConstants.FONT_SIZE_X_LARGE.equals(propertyValue)) {
				size = StyleSupportConstants.FONT_SIZE_X_LARGE_PX;
				break;
			}
			if (StyleSupportConstants.FONT_SIZE_XX_LARGE.equals(propertyValue)) {
				size = StyleSupportConstants.FONT_SIZE_XX_LARGE_PX;
				break;
			}

			// relative sizes.. get size of parent and scale that...
			if (StyleSupportConstants.FONT_SIZE_SMALLER.equals(propertyValue)) {
				size = this.getComputedFontSizeOfParent(element, 1 * StyleSupportConstants.SMALLER_SCALING_FACTOR);
				break;
			}
			if (StyleSupportConstants.FONT_SIZE_LARGER.equals(propertyValue)) {
				size = this.getComputedFontSizeOfParent(element, StyleSupportConstants.LARGER_SCALING_FACTOR);
				break;
			}

			size = (int) CssUnit.convertValue(propertyValue, CssUnit.PX);
			break;
		}
		return size;
	}

	/**
	 * Retrieves the computed font size for the parent of the given element.
	 *
	 * This method should only be called by
	 * {@link #getFontSize(Element)} when it encounters a font-size of
	 * larger or smaller. This method will then attempt to locate a pixel value
	 * for the font-size property of a parent(ancestor if recursive). This
	 * parent value is then multiplied against the scalingFactor to give the
	 * final value.
	 *
	 * @param element
	 * @param scalingFactor
	 * @return
	 */
	protected int getComputedFontSizeOfParent(final Element element, final float scalingFactor) {
		ObjectHelper.checkNotNull("parameter:element", element);

		Element parent = DOM.getParent(element);

		int parentSize = this.getFontSize(parent);
		if (-1 == parentSize) {
			parentSize = StyleSupportConstants.FONT_SIZE_MEDIUM_PX;
		}

		return Math.round(parentSize * scalingFactor);
	}
	
	
	public void set( final JavaScriptObject source, final String name, final String value ){
		while( true ){
			if( Css.OPACITY.equals( name )){
				this.setOpacity( source, value);
				break;
			}
			if( Css.BACKGROUND_IMAGE.equals( name )){
				this.setBackgroundImage( source, value );
			}
			super.set(source, name, value);
			break;
		}
	}
	
	protected void setBackgroundImage( final JavaScriptObject ruleOrElement, final String url ){
		ObjectHelper.checkNotNull("parameter:ruleOrElement", ruleOrElement);
		StringHelper.checkNotEmpty("parameter:url", url);

		// backup other background properties that will get lost when background
		// shortcut with image is written.
		final String colour = getString(ruleOrElement, Css.BACKGROUND_COLOR);
		final String attachment = getString(ruleOrElement, Css.BACKGROUND_ATTACHMENT);
		final String position = getString(ruleOrElement, Css.BACKGROUND_POSITION);
		final String repeat = getString(ruleOrElement, Css.BACKGROUND_REPEAT);

		this.setString(ruleOrElement, Css.BACKGROUND, url);

		// restore other background properties...
		if (false == StringHelper.isNullOrEmpty(colour)) {
			this.setString(ruleOrElement, Css.BACKGROUND_COLOR, colour);
		}
		if (false == StringHelper.isNullOrEmpty(attachment)) {
			this.setString(ruleOrElement, Css.BACKGROUND_ATTACHMENT, attachment);
		}
		if (false == StringHelper.isNullOrEmpty(position)) {
			this.setString(ruleOrElement, Css.BACKGROUND_POSITION, position);
		}
		if (false == StringHelper.isNullOrEmpty(repeat)) {
			this.setString(ruleOrElement, Css.BACKGROUND_REPEAT, repeat);
		}
	}
	
	protected void setOpacity( final JavaScriptObject elementOrRule, final String value ){
		final double doubleValue = Double.parseDouble(value);
		final int percentageValue = (int) (doubleValue * 100);

		final String translated = "alpha(opacity=" + percentageValue + ")";
		
		this.setString(elementOrRule, StyleSupportConstants.FILTER, translated );
	}
	
	public void remove( final JavaScriptObject source, final String name ){
		while( true ){
			if( Css.OPACITY.equals( name ) ){
				this.removeUserSelect( source);
			}
			super.remove( source, name );
			break;
		}
		this.remove0( source, name);
	}
	
	protected String getUserSelectPropertyName(){
		throw new UnsupportedOperationException("getUserSelectPropertyName");
	}
	

	protected void removeUserSelect( final JavaScriptObject source ){
		this.setUserSelect(source, "none");
	}	
}
