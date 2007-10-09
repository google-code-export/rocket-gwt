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

import java.util.Map;

import rocket.util.client.Colour;
import rocket.util.client.Destroyable;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

/**
 * Presents a Map view of all the inline styles that apply to an element.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class InlineStyle extends Style implements Destroyable {

	/**
	 * Factory method which returns a view of all the inline style object for
	 * the given element. The Style object returned must be destroyed when no
	 * longer needed.
	 * 
	 * @param element
	 * @return
	 */
	static public Map get(final Element element) {
		final InlineStyle style = new InlineStyle();
		style.setElement(element);
		return style;
	}

	/**
	 * Retrieves an inline style property by name.
	 * 
	 * @param element
	 * @param name
	 * @return
	 */
	static public String getString(final Element element, final String name) {
		return StyleHelper.getSupport().getInlineStyleProperty(element, name);
	}

	public static Colour getColour(final Element element, final String propertyName) {
		Colour value = null;
		final String string = InlineStyle.getString(element, propertyName);
		if (false == StringHelper.isNullOrEmpty(string)) {
			value = Colour.parse(string);
		}
		return value;
	}

	public static double getDouble(final Element element, final String propertyName, final CssUnit unit, final double defaultValue) {
		double value = defaultValue;
		final String string = InlineStyle.getString(element, propertyName);
		if (false == StringHelper.isNullOrEmpty(string)) {
			value = StyleHelper.convertValue(string, unit);
		}
		return value;
	}

	public static int getInteger(final Element element, final String propertyName, final CssUnit unit, final int defaultValue) {
		int value = defaultValue;
		final String string = InlineStyle.getString(element, propertyName);
		if (false == StringHelper.isNullOrEmpty(string)) {
			value = (int) StyleHelper.convertValue(string, unit);
		}
		return value;
	}

	public static String getUrl(final Element element, final String propertyName) {
		String string = InlineStyle.getString(element, propertyName);
		if (false == StringHelper.isNullOrEmpty(string)) {
			string = StyleHelper.getUrl(string);
		}
		return string;
	}

	/**
	 * Sets an inline style property name with a new value.
	 * 
	 * @param element
	 * @param propertyName
	 * @param propertyValue
	 */
	static public void setString(final Element element, final String propertyName, final String propertyValue) {
		if( null != propertyValue ){
		StyleHelper.getSupport().setInlineStyleProperty(element, propertyName, propertyValue);
		}
	}

	static public void setColour(final Element element, final String propertyName, final Colour colour) {
		if( null != colour ){
		InlineStyle.setString(element, propertyName, colour.toCssColour());
		}
	}

	static public void setDouble(final Element element, final String propertyName, final double value, final CssUnit unit) {
		// drop any trailing decimal 0's.
		final String valueAsAString = Math.round(value) == value ? String.valueOf((int) value) : String.valueOf(value);

		InlineStyle.setString(element, propertyName, valueAsAString);
	}

	static public void setInteger(final Element element, final String propertyName, final int value, final CssUnit unit) {
		InlineStyle.setString(element, propertyName, "" + value + unit.getValue());
	}

	static public void setUrl(final Element element, final String propertyName, final String url) {
		if( null != url ){
		InlineStyle.setString(element, propertyName, "url('" + url + "')");
		}
	}

	/**
	 * This helper may be used to remove an existing Style's property. If the
	 * property does not exist nothing happens.
	 * 
	 * @param element
	 * @param propertyName
	 */
	static public void remove(final Element element, final String propertyName) {
		StyleHelper.getSupport().removeInlineStyleProperty(element, propertyName);
	}

	final public String getCssText() {
		return ObjectHelper.getString(this.getStyle(), StyleConstants.CSS_STYLE_TEXT_PROPERTY_NAME);
	}

	final public void setCssText(final String cssText) {
		ObjectHelper.setString(this.getStyle(), StyleConstants.CSS_STYLE_TEXT_PROPERTY_NAME, cssText);
	}

	/**
	 * Helper which retrieves the native style object
	 * 
	 * @return
	 */
	protected JavaScriptObject getStyle() {
		return ObjectHelper.getObject(this.getElement(), "style");
	}

	public int size() {
		final JavaScriptObject style = ObjectHelper.getObject(this.getElement(), "style");
		return ObjectHelper.getPropertyCount(style);
	}

	public String getValue(String propertyName) {
		return InlineStyle.getString(this.getElement(), propertyName);
	}

	protected void putValue(final String propertyName, final String propertyValue) {
		InlineStyle.setString(this.getElement(), propertyName, propertyValue);
	}

	protected void removeValue(final String propertyName) {
		InlineStyle.remove(this.getElement(), propertyName);
	}

	public void destroy() {
		this.clearElement();
	}

	protected String[] getPropertyNames() {
		final String list = this.getPropertyNames(this.getElement());
		return StringHelper.split(list, ",", true);
	}

	native private String getPropertyNames(final Element element)/*-{
	 var style = element.style;
	 var names = "";
	 for( n in style ){
	 names = names + n + ",";
	 }
	 return names;
	 }-*/;

	/**
	 * The native element whose styles are being viewed as a Map
	 */
	private Element element;

	public Element getElement() {
		ObjectHelper.checkNotNull("field:element", element);
		return element;
	}

	public boolean hasElement() {
		return null != this.element;
	}

	public void setElement(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.element = element;
	}

	public void clearElement() {
		this.element = null;
	}
}
