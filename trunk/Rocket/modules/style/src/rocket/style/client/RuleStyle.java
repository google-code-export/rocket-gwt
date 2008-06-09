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

import rocket.style.client.support.RuleStyleSupport;
import rocket.style.client.support.StyleSupport;
import rocket.util.client.Colour;

import com.google.gwt.core.client.GWT;

/**
 * A style that belongs to a rule which in turn belongs to a StyleSheet.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RuleStyle extends Style {

	static private final StyleSupport support = (StyleSupport) GWT.create(RuleStyleSupport.class);

	static protected StyleSupport getSupport() {
		return RuleStyle.support;
	}

	static RuleStyle getRule( final Rule rule ){
		return rule.cast();
	}
	
	protected RuleStyle() {
		super();
	}

	final public String getString(final String name) {
		return RuleStyle.getSupport().get(this, name);
	}

	final public void setString(final String name, final String value) {
		RuleStyle.getSupport().set(this, name, value);
	}

	final public String getCssText() {
		return this.getString(Css.CSS_STYLE_TEXT_PROPERTY_NAME);
	}

	final public Colour getColour(final String name) {
		return Style.getColour0(this.getString(name));
	}

	final public void setColour(final String name, final Colour colour) {
		this.setString(name, colour.toCssColour());
	}

	final public double getDouble(final String name, final CssUnit unit, final double defaultValue) {
		return Style.getDouble0(this.getString(name), unit, defaultValue);
	}

	final public void setDouble(final String name, final double value, final CssUnit unit) {
		this.setString(name, value + unit.getSuffix());
	}

	final public int getInteger(final String name, final CssUnit unit, final int defaultValue) {
		return Style.getInteger0(this.getString(name), unit, defaultValue);
	}

	final public void setInteger(final String name, final int value, final CssUnit unit) {
		this.setString(name, value + unit.getSuffix());
	}

	final public String getUrl(final String name) {
		return Style.getUrl0(this.getString(name));
	}

	final public void setUrl(final String name, final String url) {
		this.setString(name, Style.buildUrl(url));
	}

	final public void remove(final String name) {
		RuleStyle.getSupport().remove(this, name);
	}

	final public String[] getNames() {
		return RuleStyle.getSupport().getPropertyNames(this);
	}
}
