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
import rocket.style.client.CssUnit;
import rocket.style.client.StyleConstants;
import rocket.style.client.StyleHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Provides the IE6 specific implementation of various StyleHelper support
 * methods.
 * 
 * Each of the public getters/setters include workarounds to
 * successfully/accurately retrieve a particular value.
 * 
 * <h6>Gotchas</h6>
 * <ul>
 * <li> In order to simulate the UserSelect css property text selection is
 * disabled by controlling an elements ondrag and onselectstart event listeners.
 * </li>
 * <li>It is not possible to disable text selection via css, it may only be
 * achieved by setting a inlineStyle belonging to an element.</li>
 * <li>Fixed positioning is only supported for inline css properties set via
 * {@link StyleHelper} and not
 * {@link DOM#setStyleAttribute(Element, String, String)}.
 * </ul>
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * TODO add support for min-width, max-width, min-height, max-height,
 */
public class InternetExplorer6StyleSupport extends StyleSupport {

	public InternetExplorer6StyleSupport() {
		super();

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

		this.installDynamicExpressionRecalc(StyleSupportConstants.RECALC_REFRESH_IN_MILLIS);
	}

	protected native void installDynamicExpressionRecalc(final int refresh)/*-{
	 $wnd.setInterval( function(){ $doc.recalc( true);}, refresh );
	 }-*/;

	/**
	 * Helper which retrieves the native rules collection that this instance is
	 * presenting as a List
	 * 
	 * @return
	 */
	public JavaScriptObject getRulesCollection(final JavaScriptObject styleSheet) {
		return ObjectHelper.getObject(styleSheet, StyleConstants.RULES_LIST_PROPERTY_IE6);
	}

	public void addRule(final JavaScriptObject styleSheet, final String selectorText, final String styleText) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		StringHelper.checkNotNull("parameter:selectorText", selectorText);
		StringHelper.checkNotNull("parameter:styleText", styleText);

		this.addRule0(styleSheet, selectorText, styleText);
	}

	private native void addRule0(final JavaScriptObject styleSheet, final String selectorText, final String styleText)/*-{        
	 var index = styleSheet.rules.length;
	 var safeStyleText = styleText.length == 0 ? ";" : styleText;

	 styleSheet.addRule( selectorText, safeStyleText, index );         
	 }-*/;

	public void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		StringHelper.checkNotNull("parameter:selectorText", selectorText);
		StringHelper.checkNotNull("parameter:styleText", styleText);

		this.insertRule0(styleSheet, index, selectorText, styleText);
	}

	private native void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText)/*-{
	 styleSheet.addRule( selectorText, styleText.length == 0 ? ";" : styleText, index );         
	 }-*/;

	public void removeRule(final JavaScriptObject styleSheet, final int index) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);

		this.removeRule0(styleSheet, index);
	}

	/**
	 * Escapes to javascript to delete the requested rule.
	 */
	native private void removeRule0(final JavaScriptObject styleSheet, final int index) /*-{            
	 styleSheet.removeRule( index );
	 }-*/;

	public String[] getRuleStylePropertyNames(final JavaScriptObject rule) {
		return getStylePropertyNames(ObjectHelper.getObject(rule, "style"));
	}

	/**
	 * This method should never happen as the {@link #getRu
	 */
	protected String getUserSelectPropertyName() {
		throw new UnsupportedOperationException("getUserSelectPropertyName");
	}

	/**
	 * Retrieves a style property from the given style
	 * 
	 * @param element
	 * @param propertyName
	 */
	public String getRuleStyleProperty(final JavaScriptObject rule, final String propertyName) {
		String propertyValue = null;
		while (true) {
			final JavaScriptObject style = this.getStyle(rule);
			if (StyleConstants.OPACITY.equals(propertyName)) {
				propertyValue = this.getStyleOpacity(style);
				break;
			}
			if (StyleConstants.USER_SELECT.equals(propertyName)) {
				propertyValue = this.getRuleStyleUserSelect(style);
				break;
			}

			propertyValue = super.getRuleStyleProperty(rule, propertyName);
			break;
		}
		return propertyValue;
	}

	protected String getRuleStyleProperty0(final JavaScriptObject rule, final String propertyName) {
		return this.getStyleProperty0(this.getStyle(rule), propertyName);
	}

	protected String getRuleStyleUserSelect(final JavaScriptObject rule) {
		throw new RuntimeException("InternetExplorer does not support the " + StyleConstants.USER_SELECT + " css property");
	}

	public void setRuleStyleProperty(final JavaScriptObject rule, final String propertyName, final String propertyValue) {
		while (true) {
			final JavaScriptObject style = this.getStyle(rule);
			if (StyleConstants.BACKGROUND_IMAGE.equals(propertyName)) {
				this.setStyleBackgroundImage(style, propertyValue);
				break;
			}
			if (StyleConstants.OPACITY.equals(propertyName)) {
				this.setStyleOpacity(style, propertyValue);
				break;
			}
			if (StyleConstants.USER_SELECT.equals(propertyName)) {
				this.setRuleStyleUserSelect(rule, propertyValue);
				break;
			}

			super.setRuleStyleProperty(rule, propertyName, propertyValue);
			break;
		}
	}

	protected void setRuleStyleProperty0(final JavaScriptObject rule, final String propertyName, final String propertyValue) {
		this.setStyleProperty0(this.getStyle(rule), propertyName, propertyValue);
	}

	protected void setRuleStyleUserSelect(final JavaScriptObject rule, final String propertyValue) {
		throw new UnsupportedOperationException("setRuleUserSelect");
	}

	public void removeRuleStyleProperty(final JavaScriptObject rule, final String propertyName) {
		while (true) {
			final JavaScriptObject style = this.getStyle(rule);
			if (StyleConstants.OPACITY.equals(propertyName)) {
				this.removeStyleOpacity(style);
				break;
			}
			if (StyleConstants.USER_SELECT.equals(propertyName)) {
				this.removeRuleStyleUserSelect(style);
				break;
			}

			super.removeRuleStyleProperty(rule, propertyName);
			break;
		}
	}

	protected void removeRuleStyleProperty0(final JavaScriptObject rule, final String propertyName) {
		this.removeStyleProperty(this.getStyle(rule), propertyName);
	}

	protected void removeRuleStyleUserSelect(final JavaScriptObject rule) {
		throw new UnsupportedOperationException("removeRuleStyleUserSelect");
	}

	/**
	 * Retrieves a style property from the given style
	 * 
	 * @param element
	 * @param propertyName
	 */
	public String getInlineStyleProperty(final Element element, final String propertyName) {
		String propertyValue = null;

		while (true) {
			final JavaScriptObject style = this.getStyle(element);
			if (StyleConstants.POSITION.equals(propertyName)) {
				propertyValue = this.getInlineStylePosition(style);
				break;
			}
			if (StyleConstants.LEFT.equals(propertyName)) {
				propertyValue = this.getInlineStyleLeft(style);
				break;
			}
			if (StyleConstants.TOP.equals(propertyName)) {
				propertyValue = this.getInlineStyleTop(style);
				break;
			}
			if (StyleConstants.RIGHT.equals(propertyName)) {
				propertyValue = this.getInlineStyleRight(style);
				break;
			}
			if (StyleConstants.BOTTOM.equals(propertyName)) {
				propertyValue = this.getInlineStyleBottom(style);
				break;
			}
			if (StyleConstants.OPACITY.equals(propertyName)) {
				propertyValue = this.getStyleOpacity(style);
				break;
			}

			propertyValue = super.getInlineStyleProperty(element, propertyName);
			break;
		}
		return propertyValue;
	}

	protected String getInlineStylePosition(final JavaScriptObject style) {
		String position = null;

		while (true) {

			// check if a dynamic expression exists...
			position = this.getDynamicExpression(style, StyleConstants.POSITION);
			if (false == StringHelper.isNullOrEmpty(position)) {
				position = StyleSupportConstants.FIXED_POSITION;
				break;
			}

			// no dynamic expression found retrieve the style property.
			position = this.getStyleProperty0(style, StyleConstants.POSITION);
			break;
		}
		return position;
	}

	/**
	 * Sets a Internet Explorer inline style and also includes a number of
	 * special cases.
	 * <ul>
	 * <li>background-image</li>
	 * <li>position</li>
	 * <li>left</li>
	 * <li>right</li>
	 * <li>top</li>
	 * <li>bottom</li>
	 * <li>opacity</li>
	 * </ul>
	 */
	public void setInlineStyleProperty(final Element element, final String propertyName, final String propertyValue) {
		while (true) {
			final JavaScriptObject style = this.getStyle(element);
			if (StyleConstants.BACKGROUND_IMAGE.equals(propertyName)) {
				this.setStyleBackgroundImage(style, propertyValue);
				break;
			}
			if (StyleConstants.POSITION.equals(propertyName)) {
				this.setInlineStylePosition(style, propertyValue);
				break;
			}
			if (StyleConstants.LEFT.equals(propertyName)) {
				this.setInlineStyleLeft(style, propertyValue);
				break;
			}
			if (StyleConstants.RIGHT.equals(propertyName)) {
				this.setInlineStyleRight(style, propertyValue);
				break;
			}
			if (StyleConstants.TOP.equals(propertyName)) {
				this.setInlineStyleTop(style, propertyValue);
				break;
			}
			if (StyleConstants.BOTTOM.equals(propertyName)) {
				this.setInlineStyleBottom(style, propertyValue);
				break;
			}
			if (StyleConstants.OPACITY.equals(propertyName)) {
				this.setStyleOpacity(style, propertyValue);
				break;
			}
			super.setInlineStyleProperty(element, propertyName, propertyValue);
			break;
		}
	}

	/**
	 * Special case to handle the setting of a background-image upon an element.
	 * 
	 * @param element
	 * @param url
	 */
	protected void setStyleBackgroundImage(final JavaScriptObject style, final String url) {
		ObjectHelper.checkNotNull("parameter:style", style);
		StringHelper.checkNotEmpty("parameter:url", url);

		// backup other background properties that will get lost when background
		// shortcut with image is written.
		final String colour = getStyleProperty0(style, StyleConstants.BACKGROUND_COLOR);
		final String attachment = getStyleProperty0(style, StyleConstants.BACKGROUND_ATTACHMENT);
		final String position = getStyleProperty0(style, StyleConstants.BACKGROUND_POSITION);
		final String repeat = getStyleProperty0(style, StyleConstants.BACKGROUND_REPEAT);

		this.setStyleProperty0(style, StyleConstants.BACKGROUND, url);

		// restore other background properties...
		if (false == StringHelper.isNullOrEmpty(colour)) {
			this.setStyleProperty0(style, StyleConstants.BACKGROUND_COLOR, colour);
		}
		if (false == StringHelper.isNullOrEmpty(attachment)) {
			this.setStyleProperty0(style, StyleConstants.BACKGROUND_ATTACHMENT, attachment);
		}
		if (false == StringHelper.isNullOrEmpty(position)) {
			this.setStyleProperty0(style, StyleConstants.BACKGROUND_POSITION, position);
		}
		if (false == StringHelper.isNullOrEmpty(repeat)) {
			this.setStyleProperty0(style, StyleConstants.BACKGROUND_REPEAT, repeat);
		}
	}

	protected void setInlineUserSelect(final Element element, final String value) {
		final boolean enable = false == "none".equals(value);
		this.setInlineUserSelect0(element, enable);
	}

	native private void setInlineUserSelect0(final Element element, final boolean enable)/*-{    
	 var f = function(){ return enable };
	 element.ondrag = f;
	 element.onselectstart = f;
	 }-*/;

	protected void setInlineStylePosition(final JavaScriptObject style, final String position) {
		final StringBuffer cssTextBuilder = new StringBuffer();
		String appendCssText = null;

		while (true) {
			final boolean newPositionIsFixed = StyleSupportConstants.FIXED_POSITION.equalsIgnoreCase(position);

			final DynamicExpression leftDynamicExpression = this.getFixedPositionLeft();
			final DynamicExpression rightDynamicExpression = this.getFixedPositionRight();
			final DynamicExpression topDynamicExpression = this.getFixedPositionTop();
			final DynamicExpression bottomDynamicExpression = this.getFixedPositionBottom();

			// this forgets unit if coming from
			final String left = this.getInlineStylePropertyValuePossiblyFromDynamicExpression(style, StyleConstants.LEFT,
					leftDynamicExpression);
			final String right = this.getInlineStylePropertyValuePossiblyFromDynamicExpression(style, StyleConstants.RIGHT,
					rightDynamicExpression);
			final String top = this.getInlineStylePropertyValuePossiblyFromDynamicExpression(style, StyleConstants.TOP,
					topDynamicExpression);
			final String bottom = this.getInlineStylePropertyValuePossiblyFromDynamicExpression(style, StyleConstants.BOTTOM,
					bottomDynamicExpression);

			this.removeInlineStyleProperty1(style, StyleConstants.POSITION);
			this.removeInlineStyleProperty1(style, StyleConstants.LEFT);
			this.removeInlineStyleProperty1(style, StyleConstants.RIGHT);
			this.removeInlineStyleProperty1(style, StyleConstants.TOP);
			this.removeInlineStyleProperty1(style, StyleConstants.BOTTOM);

			appendCssText = this.getStyleProperty0(style, "cssText");

			if (false == newPositionIsFixed) {
				cssTextBuilder.append(StyleConstants.POSITION);
				cssTextBuilder.append(":");
				cssTextBuilder.append(position);
				cssTextBuilder.append(";");

				if (false == StringHelper.isNullOrEmpty(left)) {
					cssTextBuilder.append(StyleConstants.LEFT);
					cssTextBuilder.append(':');
					cssTextBuilder.append(addPixelUnitIfUnitIsMissing(left));
					cssTextBuilder.append(';');
				}

				if (false == StringHelper.isNullOrEmpty(right)) {
					cssTextBuilder.append(StyleConstants.RIGHT);
					cssTextBuilder.append(':');
					cssTextBuilder.append(addPixelUnitIfUnitIsMissing(right));
					cssTextBuilder.append(';');
				}

				if (false == StringHelper.isNullOrEmpty(top)) {
					cssTextBuilder.append(StyleConstants.TOP);
					cssTextBuilder.append(':');
					cssTextBuilder.append(addPixelUnitIfUnitIsMissing(top));
					cssTextBuilder.append(';');
				}

				if (false == StringHelper.isNullOrEmpty(bottom)) {
					cssTextBuilder.append(StyleConstants.BOTTOM);
					cssTextBuilder.append(':');
					cssTextBuilder.append(addPixelUnitIfUnitIsMissing(bottom));
					cssTextBuilder.append(';');
				}
				break;
			}

			cssTextBuilder.append(StyleConstants.POSITION + ':');
			cssTextBuilder.append("expression(");
			cssTextBuilder.append(this.getFixedPosition().setValue(""));
			cssTextBuilder.append(");");

			if (false == StringHelper.isNullOrEmpty(left)) {
				cssTextBuilder.append(StyleConstants.LEFT);
				cssTextBuilder.append(":expression(");
				cssTextBuilder.append(leftDynamicExpression.setValue(left));
				cssTextBuilder.append(");");
			}

			if (false == StringHelper.isNullOrEmpty(right)) {
				cssTextBuilder.append(StyleConstants.RIGHT);
				cssTextBuilder.append(":expression(");
				cssTextBuilder.append(rightDynamicExpression.setValue(right));
				cssTextBuilder.append(");");
			}

			if (false == StringHelper.isNullOrEmpty(top)) {
				cssTextBuilder.append(StyleConstants.TOP);
				cssTextBuilder.append(":expression(");
				cssTextBuilder.append(topDynamicExpression.setValue(top));
				cssTextBuilder.append(");");
			}

			if (false == StringHelper.isNullOrEmpty(bottom)) {
				cssTextBuilder.append(StyleConstants.BOTTOM);
				cssTextBuilder.append(":expression(");
				cssTextBuilder.append(bottomDynamicExpression.setValue(bottom));
				cssTextBuilder.append(");");
			}
			break;
		}

		// append rather than prepend, this way the toString() etc junk that
		// GWT's wrapJSO() function adds doesnt result in a broken cssText
		if (false == StringHelper.isNullOrEmpty(appendCssText)) {
			cssTextBuilder.append(appendCssText);
			cssTextBuilder.append(";");
		}
		this.setCssText(style, cssTextBuilder.toString());
	}

	protected String addPixelUnitIfUnitIsMissing(final String value) {
		try {
			Integer.parseInt(value);
			return value + "px";
		} catch (final NumberFormatException e) {
			return value;
		}
	}

	native protected void setCssText(final JavaScriptObject style, final String value)/*-{
	 style.setAttribute( 'cssText', value, 0 );
	 }-*/;

	protected String getInlineStyleLeft(final JavaScriptObject style) {
		return this.getInlineStylePropertyValuePossiblyFromDynamicExpression(style, StyleConstants.LEFT, this.getFixedPositionLeft());
	}

	protected void setInlineStyleLeft(final JavaScriptObject style, final String value) {
		this.setInlineStylePropertyValueOrDynamicExpression(style, StyleConstants.LEFT, value, this.getFixedPositionLeft());
	}

	protected String getInlineStyleRight(final JavaScriptObject style) {
		return this.getInlineStylePropertyValuePossiblyFromDynamicExpression(style, StyleConstants.RIGHT, this.getFixedPositionRight());
	}

	protected void setInlineStyleRight(final JavaScriptObject style, final String value) {
		this.setInlineStylePropertyValueOrDynamicExpression(style, StyleConstants.RIGHT, value, this.getFixedPositionRight());
	}

	protected String getInlineStyleTop(final JavaScriptObject style) {
		return this.getInlineStylePropertyValuePossiblyFromDynamicExpression(style, StyleConstants.TOP, this.getFixedPositionTop());
	}

	protected void setInlineStyleTop(final JavaScriptObject style, final String value) {
		this.setInlineStylePropertyValueOrDynamicExpression(style, StyleConstants.TOP, value, this.getFixedPositionTop());
	}

	protected String getInlineStyleBottom(final JavaScriptObject style) {
		return this.getInlineStylePropertyValuePossiblyFromDynamicExpression(style, StyleConstants.BOTTOM, this.getFixedPositionBottom());
	}

	protected void setInlineStyleBottom(final JavaScriptObject style, final String value) {
		this.setInlineStylePropertyValueOrDynamicExpression(style, StyleConstants.BOTTOM, value, this.getFixedPositionBottom());
	}

	/**
	 * This method is potentially invoked by
	 * {@link StyleSupport#getInlineStyleProperty(Element, String)} if the
	 * property is user-select. Unlike other browsers InternetExplorer does not
	 * use a css property but rather sets or removes event handlers.
	 * 
	 * @param element
	 * @return
	 */
	protected String getInlineStyleUserSelect(final Element element) {
		return this.getInlineStyleUserSelect0(element);
	}

	native String getInlineStyleUserSelect0(final Element element)/*-{
	 var result = true;

	 var f = element.ondrag;
	 if( f ){
	 result = f();
	 }

	 // disabled == "none" // enabled = null
	 return result ? null : "none";        
	 }-*/;

	/**
	 * This dynamic expression simply returns 'absolute' but is used primarily
	 * as a marker to note that fixed position is active.
	 */
	private DynamicExpression fixedPosition;

	protected DynamicExpression getFixedPosition() {
		return this.fixedPosition;
	}

	protected void setFixedPosition(final DynamicExpression fixedPosition) {
		this.fixedPosition = fixedPosition;
	}

	private DynamicExpression fixedPositionLeft;

	protected DynamicExpression getFixedPositionLeft() {
		return this.fixedPositionLeft;
	}

	protected void setFixedPositionLeft(final DynamicExpression fixedPositionLeft) {
		this.fixedPositionLeft = fixedPositionLeft;
	}

	private DynamicExpression fixedPositionRight;

	protected DynamicExpression getFixedPositionRight() {
		return this.fixedPositionRight;
	}

	protected void setFixedPositionRight(final DynamicExpression fixedPositionRight) {
		this.fixedPositionRight = fixedPositionRight;
	}

	private DynamicExpression fixedPositionTop;

	protected DynamicExpression getFixedPositionTop() {
		return this.fixedPositionTop;
	}

	protected void setFixedPositionTop(final DynamicExpression fixedPositionTop) {
		this.fixedPositionTop = fixedPositionTop;
	}

	private DynamicExpression fixedPositionBottom;

	protected DynamicExpression getFixedPositionBottom() {
		return this.fixedPositionBottom;
	}

	protected void setFixedPositionBottom(final DynamicExpression fixedPositionBottom) {
		this.fixedPositionBottom = fixedPositionBottom;
	}

	protected String getInlineStylePropertyValuePossiblyFromDynamicExpression(final JavaScriptObject style, final String propertyName,
			final DynamicExpression dynamicExpression) {
		String value = null;

		while (true) {
			// if a dynamic expression get the value it contains...
			value = this.getDynamicExpression(style, propertyName);
			if (false == StringHelper.isNullOrEmpty(value) && dynamicExpression.isEqual(value)) {
				value = "" + dynamicExpression.getValue(value);
				break;
			}

			// otherwise query its property
			value = this.getStyleProperty0(style, propertyName);
			break;
		}

		return value;
	}

	protected void setInlineStylePropertyValueOrDynamicExpression(final JavaScriptObject style, final String propertyName,
			final String propertyValue, final DynamicExpression dynamicExpression) {
		while (true) {
			boolean fixed = false;
			final String expression = this.getDynamicExpression(style, StyleConstants.POSITION);
			if (false == StringHelper.isNullOrEmpty(expression)) {
				fixed = this.getFixedPosition().isEqual(expression);
			}
			this.removeInlineStyleProperty1(style, propertyName);
			if (false == fixed) {
				this.setStyleProperty0(style, propertyName, propertyValue);
				break;
			}

			// fixed position remove an existing propertyName set up the dynamic
			// expression.
			this.setDynamicExpression(style, propertyName, dynamicExpression.setValue(propertyValue));
			break;
		}
	}

	protected String getDynamicExpression(final JavaScriptObject style, final String propertyName) {
		return this.getDynamicExpression0(style, propertyName);
	}

	native private String getDynamicExpression0(final JavaScriptObject style, final String propertyName)/*-{
	 var expression = style.getExpression( propertyName );
	 return expression ? expression : null;
	 }-*/;

	protected void setDynamicExpression(final JavaScriptObject style, final String propertyName, final String propertyValue) {
		final String cssText = this.getStyleProperty0(style, "cssText");
		this.setCssText(style, propertyName + ": expression(" + propertyValue + ");" + cssText + ";");
	}

	/**
	 * Removes a possibly existing style by setting its value to an empty
	 * String.
	 * 
	 * @param element
	 * @param propertyName
	 */
	public void removeInlineStyleProperty(final Element element, final String propertyName) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.checkPropertyName("parameter:propertyName", propertyName);

		while (true) {
			if (StyleConstants.OPACITY.equals(propertyName)) {
				this.removeStyleOpacity(this.getStyle(element));
				break;
			}
			if (StyleConstants.USER_SELECT.equals(propertyName)) {
				this.removeInlineStyleUserSelect(element);
				break;
			}
			super.removeInlineStyleProperty(element, propertyName);
			break;
		}
	}

	protected void removeInlineStyleProperty0(final Element element, final String propertyName) {
		this.removeInlineStyleProperty1(this.getStyle(element), propertyName);
	}

	protected void removeInlineStyleProperty1(final JavaScriptObject style, final String propertyName) {
		this.removeInlineStyleProperty2(style, propertyName);
	}

	private native void removeInlineStyleProperty2(final JavaScriptObject style, final String propertyName)/*-{
	 style[ propertyName ] = "";
	 style.removeExpression( propertyName );
	 }-*/;

	protected void removeInlineStyleUserSelect(final Element element) {
		this.removeInlineStyleUserSelect0(element);
	}

	native protected void removeInlineStyleUserSelect0(final Element element)/*-{
	 element.ondrag=null;
	 element.onselectstart=null;
	 }-*/;

	/**
	 * Retrieves the computed style for the given element.
	 * 
	 * <h6>Special cases</h6>
	 * <ul>
	 * <li>Attempts to retrieve the css opacity property is converted into
	 * filter, the value is converted back into a decimal value. </li>
	 * <li>If requests for width or height are made and auto returned calculate
	 * the actual width/height in pixels using
	 * {@link #getComputedWidth(Element)} and
	 * {@link #getComputedHeight(Element)}</li>
	 * </ul>
	 */
	public String getComputedStyleProperty(final Element element, final String propertyName) {
		String propertyValue = null;
		while (true) {
			if (StyleConstants.BACKGROUND_POSITION.equals(propertyName)) {
				propertyValue = this.getComputedBackgroundPosition(element);
				break;
			}
			if (propertyName.equals(StyleConstants.FONT_SIZE)) {
				final int fontSize = this.getComputedFontSize(element);
				if (-1 != fontSize) {
					propertyValue = fontSize + "px";
				}
				break;
			}
			if (StyleConstants.HEIGHT.equals(propertyName)) {
				propertyValue = this.getComputedHeight(element);
				break;
			}
			// opacity is a special case...
			if (StyleConstants.OPACITY.equals(propertyName)) {
				propertyValue = this.getComputedOpacity(element);
				break;
			}
			if (StyleConstants.WIDTH.equals(propertyName)) {
				propertyValue = this.getComputedWidth(element);
				break;
			}
			propertyValue = super.getComputedStyleProperty(element, propertyName);
			break;
		}

		return propertyValue;
	}

	protected String getComputedStyleProperty0(final Element element, final String propertyName) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.checkPropertyName("parameter:propertyName", propertyName);

		return this.getComputedStyleProperty1(element, propertyName);
	}

	/**
	 * Uses jsni to search for a concrete value for the given element.
	 * 
	 * Current property values that are skipped include
	 * <ul>
	 * <li>inherit</li>
	 * <li>transparent</li>
	 * </ul>
	 * 
	 * @param element
	 * @param propertyName
	 * @return
	 */
	private native String getComputedStyleProperty1(final Element element, final String propertyName)/*-{
	 var value = null;
	 var element0 = element;

	 while( element0 && element0.currentStyle ){
	 value = element0.currentStyle[ propertyName ];         

	 // continue looping until a concrete value is found.
	 if( value && value != "inherit" && value != "transparent" ){
	 break;
	 }
	 element0 = element0.parentNode;
	 }

	 return value ? "" + value : null;
	 }-*/;

	protected String getComputedWidth(final Element element) {
		return ObjectHelper.getInteger(element, StyleSupportConstants.OFFSET_WIDTH) + "px";
	}

	protected String getComputedOpacity(final Element element) {
		String value = this.getComputedStyleProperty0(element, StyleSupportConstants.FILTER);
		if (null != value) {
			value = this.translateFromOpacity(value);
		}
		return value;
	}

	protected String getComputedHeight(final Element element) {
		return ObjectHelper.getInteger(element, StyleSupportConstants.OFFSET_HEIGHT) + "px";
	}

	/**
	 * @param element
	 * @return
	 */
	protected String getComputedUserSelect(final Element element) {
		return this.getComputedUserSelect0(element);
	}

	native private String getComputedUserSelect0(final Element element)/*-{
	 var result = true;

	 var element0 = element;
	 while( null != element0 ){
	 var f = element0.ondrag;
	 if( f ){
	 result = f();
	 break;
	 }

	 element0 = element0.parentNode;
	 }
	 // enabled = null // disabled = "none"
	 return result ? null : "none";
	 }-*/;

	/**
	 * This method covers a special case only returning non zero values if a
	 * border style is also applicable.
	 * 
	 * @param element
	 * @param propertyName
	 * @return
	 */
	protected int getComputedBorderWidthInPixels(final Element element, final String propertyName) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.checkPropertyName("parameter:propertyName", propertyName);

		return this.getComputedBorderWidthInPixels0(element, propertyName);
	}

	native private int getComputedBorderWidthInPixels0(final Element element, final String propertyName)/*-{
	 var value = 0;

	 while( true ){
	 var width = element.currentStyle[ propertyName ];
	 if( ! width ){
	 value = 0;
	 break;
	 }

	 // if a width value is found check if a style is also set. if not return 0...
	 var styleName = propertyName.substring( 0, propertyName.length - 5 ) + "Style";
	 var borderStyle = element.currentStyle[ styleName ];

	 if( "none" == borderStyle ){
	 value = 0;
	 break;
	 }


	 if( isNaN( width )){
	 value = this.@rocket.style.client.support.StyleSupport::translateBorderWidthValue(Ljava/lang/String;)( width );
	 break;
	 }

	 value = 0 + width;
	 break;
	 }
	 return value;
	 }-*/;

	protected int getComputedStylePropertyInPixels(final Element element, final String propertyName) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.checkPropertyName("parameter:propertyName", propertyName);

		return this.getComputedStylePropertyInPixels0(element, propertyName);
	}

	native private int getComputedStylePropertyInPixels0(final Element element, final String propertyName)/*-{
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
	public String getComputedBackgroundPosition(final Element element) {
		String value = null;

		while (true) {
			String x = this.getComputedStyleProperty(element, StyleSupportConstants.BACKGROUND_POSITION_X_IE6);
			String y = this.getComputedStyleProperty(element, StyleSupportConstants.BACKGROUND_POSITION_Y_IE6);

			final boolean xMissing = StringHelper.isNullOrEmpty(x);
			final boolean yMissing = StringHelper.isNullOrEmpty(y);
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

	/**
	 * Retrieves the computed font size for the given element taking care of
	 * absolute and relative sizes.
	 * 
	 * @param element
	 * @return
	 */
	protected int getComputedFontSize(final Element element) {
		int size = -1;
		while (true) {
			final String propertyValue = getComputedStyleProperty0(element, StyleConstants.FONT_SIZE);
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

			size = (int) StyleHelper.convertValue(propertyValue, CssUnit.PX);
			break;
		}
		return size;
	}

	/**
	 * Retrieves the computed font size for the parent of the given element.
	 * 
	 * This method should only be called by
	 * {@link #getComputedFontSize(Element)} when it encounters a font-size of
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

		int parentSize = this.getComputedFontSize(parent);
		if (-1 == parentSize) {
			parentSize = StyleSupportConstants.FONT_SIZE_MEDIUM_PX;
		}

		return Math.round(parentSize * scalingFactor);
	}

	/**
	 * Retrieves the names of all the computed styles available for the given
	 * element.
	 * 
	 * @param element
	 * @return
	 */
	public String[] getComputedStylePropertyNames(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);

		return this.getStylePropertyNames(ObjectHelper.getObject(element, "currentStyle"));
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

	protected String getStyleOpacity(final JavaScriptObject style) {
		final String propertyValue = getStyleProperty0(style, StyleSupportConstants.FILTER);
		return this.translateFromOpacity(propertyValue);
	}

	/**
	 * Translates an css opacity value to an InternetExplorer 6.x filter style
	 * property. Does the opposite of {@link #translateFromOpacity(String)} an
	 * opacity value.
	 * 
	 * @param value
	 * @return
	 */
	protected String translateToOpacity(final String value) {
		StringHelper.checkNotEmpty("parameter:value", value);

		final double doubleValue = Double.parseDouble(value);
		final int percentageValue = (int) (doubleValue * 100);

		return "alpha(opacity=" + percentageValue + ")";
	}

	protected void setStyleOpacity(final JavaScriptObject style, final String value) {
		this.setStyleProperty0(style, StyleSupportConstants.FILTER, this.translateToOpacity(value));
	}

	/**
	 * Translates an InternetExplorer 6.x filter style property from
	 * alpha(opacity=xxx) to an opacity value.
	 * 
	 * This is necessary in order to present a w3c standards compatible view of
	 * all browsers including Internet Explorer.
	 * 
	 * @param value
	 * @return
	 */
	protected String translateFromOpacity(final String value) {
		String number = null;
		if (false == StringHelper.isNullOrEmpty(value)) {
			number = value.substring("alpha(opacity=".length(), value.length() - 1);
			if (number.length() < 3) {
				number = "0." + number;
			} else {
				number = number.substring(0, 1) + '.' + number.substring(1, 3);
			}
		}
		return number;
	}

	protected void removeStyleOpacity(final JavaScriptObject style) {
		this.removeStyleProperty(style, StyleSupportConstants.FILTER);
	}

	protected String getStyleProperty0(final JavaScriptObject style, final String propertyName) {
		return ObjectHelper.getString(style, propertyName);
	}

	protected void setStyleProperty0(final JavaScriptObject style, final String propertyName, final String propertyValue) {
		ObjectHelper.setString(style, propertyName, propertyValue);
	}

	protected void removeStyleProperty(final JavaScriptObject style, final String propertyName) {
		ObjectHelper.setString(style, propertyName, "");
	}

	/**
	 * There is no need to normalize rules as Internet Explorer already does
	 * this when it loads stylesheets.
	 */
	public void normalize(final JavaScriptObject styleSheet) {
	}

	protected String[] getStylePropertyNames(final JavaScriptObject style) {
		ObjectHelper.checkNotNull("parameter:style", style);

		return StringHelper.split(getStylePropertyNames0(style), ",", true);
	}

	native private String getStylePropertyNames0(final JavaScriptObject style)/*-{
	 var names = new Array();
	 
	 for( n in style ){
	 names.push( n );
	 }
	 return names.join( "," );
	 
	 }-*/;
}