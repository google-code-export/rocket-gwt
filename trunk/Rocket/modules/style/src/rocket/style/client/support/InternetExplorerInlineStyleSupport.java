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

import rocket.browser.client.Browser;
import rocket.style.client.Css;
import rocket.util.client.Checker;
import rocket.util.client.JavaScript;
import rocket.util.client.Utilities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

public class InternetExplorerInlineStyleSupport extends InternetExplorerStyleSupport {

	public InternetExplorerInlineStyleSupport() {
		this.init();
	}

	/**
	 * This method detects if this version of IE supports fixed positioning and
	 * calls {@link #setupFixedPositionDynamicExpressionSupport()} to
	 * initializer fixed positioning emulation.
	 */
	protected void init() {
		final boolean supportsFixedPosition = this.testIfFixedPositionIsSupported();
		this.setSupportFixedPosition(supportsFixedPosition);

		if (false == supportsFixedPosition) {
			if (GWT.isScript()) {
				final String warning = Utilities.format(StyleSupportConstants.DYNAMIC_EXPRESSIONS_USED_TO_EMULATE_FIXED_POSITIONING,
						new Object[] { Browser.getUserAgent() });
				GWT.log(warning, null);
			}
			this.setupFixedPositionDynamicExpressionSupport();
		}
	}

	/**
	 * This method should only be called once during object creation and it
	 * tests if this particular version of IE supports position=fixed. If it no
	 * attempt will be made to support fixed positioning via dynamic expressions
	 * etc.
	 * 
	 * @return
	 */
	native private boolean testIfFixedPositionIsSupported()/*-{
	 var span = $doc.createElement( "SPAN" );
	 span.style.position="fixed";
	 return span.style.position=="fixed";
	 }-*/;

	/**
	 * A flag which is set at construction time which records whether or not
	 * this IE supports position: fixed. If it does skip attempting to emulate
	 * via dynamic expressions.
	 */
	private boolean supportsFixedPosition;;

	protected boolean supportsFixedPosition() {
		return this.supportsFixedPosition;
	}

	protected void setSupportFixedPosition(final boolean supportsFixedPosition) {
		this.supportsFixedPosition = supportsFixedPosition;
	}

	public void setupFixedPositionDynamicExpressionSupport() {
		this.setFixedPosition(new DynamicExpression(StyleSupportConstants.FIXED_POSITION_EXPRESSION));

		final boolean quirksMode = Browser.isQuirksMode();
		this.setFixedPositionLeft(new DynamicExpression(quirksMode ? StyleSupportConstants.FIXED_POSITION_LEFT_QUIRKSMODE_EXPRESSION
				: StyleSupportConstants.FIXED_POSITION_LEFT_EXPRESSION));
		this.setFixedPositionRight(new DynamicExpression(quirksMode ? StyleSupportConstants.FIXED_POSITION_RIGHT_QUIRKSMODE_EXPRESSION
				: StyleSupportConstants.FIXED_POSITION_RIGHT_EXPRESSION));
		this.setFixedPositionTop(new DynamicExpression(quirksMode ? StyleSupportConstants.FIXED_POSITION_TOP_QUIRKSMODE_EXPRESSION
				: StyleSupportConstants.FIXED_POSITION_TOP_EXPRESSION));
		this.setFixedPositionBottom(new DynamicExpression(quirksMode ? StyleSupportConstants.FIXED_POSITION_BOTTOM_QUIRKSMODE_EXPRESSION
				: StyleSupportConstants.FIXED_POSITION_BOTTOM_EXPRESSION));

		// FIXME should probably read the constant from a meta property.
		this.installDynamicExpressionRecalc(StyleSupportConstants.RECALC_REFRESH_IN_MILLIS);
	}

	native private void installDynamicExpressionRecalc(final int refresh)/*-{
	 $wnd.setInterval( function(){ $doc.recalc( true);}, refresh );
	 }-*/;

	protected String getPosition(final JavaScriptObject element) {
		return this.isFixedPosition(element) ? "fixed" : this.getString(element, Css.POSITION);
	}

	protected boolean isFixedPosition(final JavaScriptObject element) {
		return null != this.getDynamicExpressionValue(element, Css.POSITION);
	}

	protected String getLeft(final JavaScriptObject element) {
		return this.getValueFromDynamicExpressThenStyle(element, Css.LEFT, this.getFixedPositionLeft());
	}

	protected String getTop(final JavaScriptObject element) {
		return this.getValueFromDynamicExpressThenStyle(element, Css.TOP, this.getFixedPositionTop());
	}

	protected String getRight(final JavaScriptObject element) {
		return this.getValueFromDynamicExpressThenStyle(element, Css.RIGHT, this.getFixedPositionRight());
	}

	protected String getBottom(final JavaScriptObject element) {
		return this.getValueFromDynamicExpressThenStyle(element, Css.BOTTOM, this.getFixedPositionBottom());
	}

	protected String getValueFromDynamicExpressThenStyle(final JavaScriptObject element, final String name,
			final DynamicExpression dynamicExpression) {
		String value = null;
		if (this.isFixedPosition(element)) {
			final String dynamicExpressionValue = this.getDynamicExpressionValue(element, name);
			value = "" + dynamicExpression.getValue(dynamicExpressionValue);
		} else {
			value = this.getString(element, name);
		}
		return value;
	}

	protected String getDynamicExpressionValue(final JavaScriptObject element, final String name) {
		return this.getDynamicExpressionValue0(element, name);
	}

	native private String getDynamicExpressionValue0(final JavaScriptObject element, final String name)/*-{
	 return element.style.getExpression( name ) || null;
	 }-*/;

	protected void setPosition(final JavaScriptObject element, final String position) {
		while (true) {
			// if was fixed and new isnt fixed remov dynamic expressions.
			final boolean oldPositionFixed = this.isFixedPosition(element);
			final boolean newPositionFixed = StyleSupportConstants.FIXED_POSITION.equalsIgnoreCase(position);

			// if both are compatible - both not fixed, or both are fixed do
			// nothing.
			if (oldPositionFixed == newPositionFixed) {
				this.setString(element, Css.POSITION, position);
				break;
			}

			// if old position was fixed and new isnt remove dynamic
			// expressions.
			if (oldPositionFixed) {

				// extract values from dynamic expressions....
				final String left = this.getString(element, Css.LEFT);
				final String top = this.getString(element, Css.TOP);
				final String right = this.getString(element, Css.RIGHT);
				final String bottom = this.getString(element, Css.BOTTOM);

				this.removeDynamicExpression(element);

				// set them back...this needs to be done because the dynamic
				// expression overrides the actual css property resulting in the
				// original value being lost.
				this.setString(element, Css.POSITION, position);
				this.setString(element, Css.LEFT, left);
				this.setString(element, Css.TOP, top);
				this.setString(element, Css.RIGHT, right);
				this.setString(element, Css.BOTTOM, bottom);
				break;
			}

			this.convertLeftTopRightBottomToDynamicExpressions(element);
			break;
		}
	}

	native private void removeDynamicExpression(final JavaScriptObject element)/*-{
	 element.style.removeExpression( "left" );
	 element.style.removeExpression( "top" );
	 element.style.removeExpression( "right" );
	 element.style.removeExpression( "bottom" );
	 }-*/;

	protected void setLeft(final JavaScriptObject element, final String value) {
		this.setDynamicExpressionIfFixedPosition(element, Css.LEFT, value, this.getFixedPositionLeft());
	}

	protected void setRight(final JavaScriptObject element, final String value) {
		this.setDynamicExpressionIfFixedPosition(element, Css.RIGHT, value, this.getFixedPositionRight());
	}

	protected void setTop(final JavaScriptObject element, final String value) {
		this.setDynamicExpressionIfFixedPosition(element, Css.TOP, value, this.getFixedPositionTop());
	}

	protected void setBottom(final JavaScriptObject element, final String value) {
		this.setDynamicExpressionIfFixedPosition(element, Css.BOTTOM, value, this.getFixedPositionBottom());
	}

	protected void setDynamicExpressionIfFixedPosition(final JavaScriptObject element, final String name, final String value,
			final DynamicExpression dynamicExpression) {

		final boolean fixed = this.isFixedPosition(element);
		this.remove0(element, name);
		this.setString(element, name, value);

		if (fixed) {
			this.convertLeftTopRightBottomToDynamicExpressions(element);
		}
	}

	protected void convertLeftTopRightBottomToDynamicExpressions(final JavaScriptObject element) {
		final String cssText = this.getCssText(element);
		String prefix = Css.POSITION + ": expression(" + this.getFixedPosition().buildExpression("fixed") + ");";

		// set dynamic expressions using the original value (if it was present).
		final String left = this.getString(element, Css.LEFT);
		if (null != left) {
			prefix = prefix + Css.LEFT + ": expression(" + this.getFixedPositionLeft().buildExpression(left) + "); ";
		}
		final String top = this.getString(element, Css.TOP);
		if (null != top) {
			prefix = prefix + Css.TOP + ": expression(" + this.getFixedPositionTop().buildExpression(top) + "); ";
		}
		final String right = this.getString(element, Css.RIGHT);
		if (null != right) {
			prefix = prefix + Css.RIGHT + ": expression(" + this.getFixedPositionRight().buildExpression(right) + "); ";
		}
		final String bottom = this.getString(element, Css.BOTTOM);
		if (null != bottom) {
			prefix = prefix + Css.BOTTOM + ": expression(" + this.getFixedPositionBottom().buildExpression(bottom) + "); ";
		}
		this.setString(element, Css.CSS_STYLE_TEXT_PROPERTY_NAME, prefix + cssText);
	}

	/**
	 * This dynamic expression simply returns 'absolute' but is used primarily
	 * as a marker to note that fixed position is active.
	 */
	private DynamicExpression fixedPosition;

	protected DynamicExpression getFixedPosition() {
		return this.fixedPosition;
	}

	protected void setFixedPosition(final DynamicExpression fixedPosition) {
		Checker.notNull("parameter:fixedPosition", fixedPosition);
		this.fixedPosition = fixedPosition;
	}

	private DynamicExpression fixedPositionLeft;

	protected DynamicExpression getFixedPositionLeft() {
		return this.fixedPositionLeft;
	}

	protected void setFixedPositionLeft(final DynamicExpression fixedPositionLeft) {
		Checker.notNull("parameter:fixedPositionLeft", fixedPositionLeft);
		this.fixedPositionLeft = fixedPositionLeft;
	}

	private DynamicExpression fixedPositionRight;

	protected DynamicExpression getFixedPositionRight() {
		return this.fixedPositionRight;
	}

	protected void setFixedPositionRight(final DynamicExpression fixedPositionRight) {
		Checker.notNull("parameter:fixedPositionRight", fixedPositionRight);
		this.fixedPositionRight = fixedPositionRight;
	}

	private DynamicExpression fixedPositionTop;

	protected DynamicExpression getFixedPositionTop() {
		return this.fixedPositionTop;
	}

	protected void setFixedPositionTop(final DynamicExpression fixedPositionTop) {
		Checker.notNull("parameter:fixedPositionTop", fixedPositionTop);
		this.fixedPositionTop = fixedPositionTop;
	}

	private DynamicExpression fixedPositionBottom;

	protected DynamicExpression getFixedPositionBottom() {
		return this.fixedPositionBottom;
	}

	protected void setFixedPositionBottom(final DynamicExpression fixedPositionBottom) {
		Checker.notNull("parameter:fixedPositionBottom", fixedPositionBottom);
		this.fixedPositionBottom = fixedPositionBottom;
	}

	public String get(final JavaScriptObject element, final String name) {
		String value = null;

		while (true) {
			if (false == this.supportsFixedPosition()) {
				if (Css.POSITION.equals(name)) {
					value = this.getPosition(element);
					break;
				}
				if (Css.LEFT.equals(name)) {
					value = this.getLeft(element);
					break;
				}
				if (Css.TOP.equals(name)) {
					value = this.getTop(element);
					break;
				}
				if (Css.RIGHT.equals(name)) {
					value = this.getRight(element);
					break;
				}
				if (Css.BOTTOM.equals(name)) {
					value = this.getBottom(element);
					break;
				}
			}
			if (Css.WIDTH.equals(name)) {
				value = this.getWidth(element);
				break;
			}
			if (Css.HEIGHT.equals(name)) {
				value = this.getHeight(element);
				break;
			}

			value = super.get(element, name);
			break;
		}
		return value;
	}

	protected String getCssText(final JavaScriptObject element) {
		return this.buildCssText(element);
	}

	protected String getUserSelect(final JavaScriptObject element) {
		return this.getUserSelectFunction(element, false);
	}

	protected String getWidth(final JavaScriptObject element) {
		String width = this.getString(element, "pixelWidth");
		if( null != width ){
			width = width + "px";
		}
		return width;
	}

	protected String getHeight(final JavaScriptObject element) {
		String height = this.getString(element, "pixelHeight");
		if( null != height ){
			height = height + "px";
		}
		return height;
	}

	protected String getString(final JavaScriptObject element, final String name) {
		return this.getString0(element, name);
	}

	private native String getString0(final JavaScriptObject element, final String name)/*-{
	 var value = null;
	 var element0 = element;

	 while( element0 && element0.style ){
	 value = element0.style[ name ];

	 // continue looping until a concrete value is found.
	 if( value && value != "inherit" && value != "transparent" ){
	 break;
	 }
	 element0 = element0.parentNode;
	 }

	 return value ? "" + value : null;
	 }-*/;

	public void set(final JavaScriptObject element, final String name, final String value) {
		while (true) {
			if (Css.BACKGROUND_IMAGE.equals(name)) {
				this.setBackgroundImage(element, value);
				break;
			}
			if (false == this.supportsFixedPosition()) {
				if (Css.POSITION.equals(name)) {
					this.setPosition(element, value);
					break;
				}
				if (Css.LEFT.equals(name)) {
					this.setLeft(element, value);
					break;
				}
				if (Css.RIGHT.equals(name)) {
					this.setRight(element, value);
					break;
				}
				if (Css.TOP.equals(name)) {
					this.setTop(element, value);
					break;
				}
				if (Css.BOTTOM.equals(name)) {
					this.setBottom(element, value);
					break;
				}
			}
			if (Css.OPACITY.equals(name)) {
				this.setOpacity(element, value);
				break;
			}
			if (Css.HEIGHT.equals(name)) {
				this.setHeight(element, value);
				break;
			}
			super.set(element, name, value);
			break;
		}
	}

	protected void setHeight(final JavaScriptObject element, final String value) {
		String value0 = value;
		if (value.equals("inherits")) {
			value0 = "100%";
		}
		this.setString(element, Css.HEIGHT, value0);
	}

	protected void setUserSelect( final JavaScriptObject element, final String value ){
		this.setUserSelectFunction(element, value);
	}
	
	protected void setString(final JavaScriptObject element, final String name, final String value) {
		if (value != null) {
			this.setString0(element, name, value);
		}
	}

	native private void setString0(final JavaScriptObject element, final String name, final String value) /*-{
	 element.style[ name ] = value;
	 }-*/;

	protected void removeUserSelect(final JavaScriptObject element) {
		this.removeUserSelect(element);
	}

	native protected void removeUserSelect0(final JavaScriptObject element)/*-{
	 element.ondrag=null;
	 element.onselectstart=null;
	 }-*/;

	protected void remove0(final JavaScriptObject element, final String name) {
		this.remove1(element, name);
	}

	native private void remove1(final JavaScriptObject element, final String name) /*-{
	 var style = element.style;
	 style[ name ] = "";
	 if( false == this.@rocket.style.client.support.InternetExplorerInlineStyleSupport::supportsFixedPosition()() ){
	 style.removeExpression( name );
	 }
	 }-*/;

	public String[] getPropertyNames(final JavaScriptObject element) {
		return JavaScript.getPropertyNames(JavaScript.getObject(element, "style"));
	}
}