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

import java.util.Iterator;
import java.util.Map;

import rocket.style.client.support.ComputedStyleSupport;
import rocket.style.client.support.StyleSupport;
import rocket.util.client.Colour;
import rocket.util.client.Destroyable;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * Presents a Map view of all the computed styles that apply to an element.
 * 
 * Using keys is always safe whilst searching for a value is not recommended.
 * <ul>
 * <li>entrySet</li>
 * <li>keySet</li>
 * <li>containsValue</li>
 * </ul>
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ComputedStyle extends Style implements Destroyable {

	static private final StyleSupport support = (StyleSupport) GWT.create( ComputedStyleSupport.class);

	static protected StyleSupport getSupport() {
		return ComputedStyle.support;
	}

	
	/**
	 * This method retrieves a concrete value(it ignores inherited, transparent
	 * etc) given a propertyName for any element.
	 * 
	 * @param element
	 * @param propertyName
	 *            The javascript form of the css property (ie backgroundColor
	 *            NOT background-color).
	 * @return The String value of the property or null if it wasnt found.
	 *         Unless the propertyName is not a valid style some default will
	 *         always be returned.
	 */
	public static String getString(final Element element, final String propertyName) {
		return ComputedStyle.getSupport().get(element, propertyName);
	}

	public static Colour getColour(final Element element, final String propertyName) {
		Colour value = null;
		final String string = ComputedStyle.getString(element, propertyName);
		if (false == StringHelper.isNullOrEmpty(string)) {
			value = Colour.parse(string);
		}
		return value;
	}

	public static double getDouble(final Element element, final String propertyName, final CssUnit unit, final double defaultValue) {
		double value = defaultValue;
		final String string = ComputedStyle.getString(element, propertyName);
		if (false == StringHelper.isNullOrEmpty(string)) {
			value = CssUnit.convertValue(string, unit);
		}
		return value;
	}

	public static int getInteger(final Element element, final String propertyName, final CssUnit unit, final int defaultValue) {
		int value = defaultValue;
		final String string = ComputedStyle.getString(element, propertyName);
		if (false == StringHelper.isNullOrEmpty(string)) {
			value = (int) CssUnit.convertValue(string, unit);
		}
		return value;
	}

	public static String getUrl(final Element element, final String propertyName) {
		String string = ComputedStyle.getString(element, propertyName);
		if (false == StringHelper.isNullOrEmpty(string)) {
			string = Style.getUrl(string);
		}
		return string;
	}

	/**
	 * Factory method which returns a view of all current Styles for the given
	 * element. The Style object returned must be destroyed when no longer
	 * needed.
	 * 
	 * @param element
	 * @return
	 */
	static public Map get(final Element element) {
		final ComputedStyle style = new ComputedStyle();
		style.setElement(element);
		return style;
	}

	protected ComputedStyle(){
		super();
	}
	
	protected ComputedStyle( final Element element ){
		super();
		
		this.setElement(element );
	}
	
	final public String getCssText() {
		return ObjectHelper.getString(this.getElement(), Css.CSS_STYLE_TEXT_PROPERTY_NAME);
	}

	final public void setCssText(final String cssText) {
		throw new UnsupportedOperationException("setCssText");
	}

	public int size() {
		int counter = 0;
		final Iterator iterator = this.entrySet().iterator();
		while (iterator.hasNext()) {
			iterator.next();
			counter++;
		}
		return counter;
	}

	protected void putValue(String propertyName, String propertyValue) {
		throw new UnsupportedOperationException("putValue");
	}

	protected void removeValue(final String propertyName) {
		throw new UnsupportedOperationException("removeValue");
	}

	public Object get(final Object key) {
		return this.getStylePropertyValue((String) key);
	}

	protected String getValue(String propertyName) {
		return ComputedStyle.getString(this.getElement(), propertyName);
	}

	protected String[] getPropertyNames() {
		return ComputedStyle.getSupport().getPropertyNames(this.getElement());
	}

	public void destroy() {
		this.clearElement();
	}

	/**
	 * The native element being viewed as a Map
	 */
	private Element element;

	public Element getElement() {
		ObjectHelper.checkNotNull("field:element", element);
		return element;
	}

	public void setElement(final Element element) {
		ObjectHelper.checkNotNull("parameter:element", element);
		this.element = element;
	}

	public void clearElement() {
		this.element = null;
	}
}
