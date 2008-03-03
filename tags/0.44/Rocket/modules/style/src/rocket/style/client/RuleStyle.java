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
import rocket.util.client.Checker;
import rocket.util.client.JavaScript;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * A style that belongs to a rule which in turn belongs to a StyleSheet.
 * 
 * @author Miroslav Pokorny (mP)
 */
class RuleStyle extends Style {

	static private final StyleSupport support = (StyleSupport) GWT.create( RuleStyleSupport.class);

	static protected StyleSupport getSupport() {
		return RuleStyle.support;
	}
	
	final public String getCssText() {
		return JavaScript.getString(this.getStyle(), Css.CSS_STYLE_TEXT_PROPERTY_NAME);
	}

	final public void setCssText(final String cssText) {
		JavaScript.setString(this.getStyle(), Css.CSS_STYLE_TEXT_PROPERTY_NAME, cssText);
	}

	public int size() {
		return JavaScript.getPropertyCount(this.getStyle());
	}

	/**
	 * Helper which retrieves the native style object
	 * 
	 * @return
	 */
	protected JavaScriptObject getStyle() {
		return JavaScript.getObject(this.getRule().getNativeRule(), "style");
	}

	public String getValue(final String propertyName) {
		return RuleStyle.getSupport().get(this.getRule().getNativeRule(), propertyName);
	}

	protected void putValue(final String propertyName, final String propertyValue) {
		RuleStyle.getSupport().set(this.getRule().getNativeRule(), propertyName, propertyValue);
	}

	protected void removeValue(final String propertyName) {
		RuleStyle.getSupport().remove(this.getRule().getNativeRule(), propertyName);
	}

	protected String[] getPropertyNames() {
		return StyleSheet.getRuleStylePropertyNames(this.getRule().getNativeRule());
	}

	/**
	 * A copy of the parent rule that this RuleStyle belongs too.
	 */
	private Rule rule;

	protected Rule getRule() {
		Checker.notNull("field:rule", rule);
		return this.rule;
	}

	protected boolean hasRule() {
		return null != rule;
	}

	protected void setRule(final Rule rule) {
		Checker.notNull("parameter:rule", rule);
		this.rule = rule;
	}

	protected void clearRule() {
		this.rule = null;
	}
}
