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

import rocket.style.client.support.ComputedStyleSupport;
import rocket.style.client.support.StyleSupport;
import rocket.util.client.Colour;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * Provides a browser independent means to retrieve the current or applicable types upon an element.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ComputedStyle extends Style {

	static private final StyleSupport support = (StyleSupport) GWT.create(ComputedStyleSupport.class);

	static protected StyleSupport getSupport() {
		return ComputedStyle.support;
	}

	static public ComputedStyle getComputedStyle(final Element element) {
		return element.cast();
	}

	protected ComputedStyle() {
		super();
	}

	final public String getString(final String name) {
		return ComputedStyle.getSupport().get(this, name);
	}

	final public String getCssText() {
		return this.getString(Css.CSS_STYLE_TEXT_PROPERTY_NAME);
	}

	final public Colour getColour(final String name) {
		return Style.getColour0(this.getString(name));
	}

	final public double getDouble(final String name, final CssUnit unit, final double defaultValue) {
		return Style.getDouble0(this.getString(name), unit, defaultValue);
	}

	final public int getInteger(final String name, final CssUnit unit, final int defaultValue) {
		return Style.getInteger0(this.getString(name), unit, defaultValue);
	}

	final public String getUrl(final String name) {
		return Style.getUrl0(this.getString(name));
	}

	final public String[] getNames() {
		return ComputedStyle.getSupport().getPropertyNames(this);
	}
}
