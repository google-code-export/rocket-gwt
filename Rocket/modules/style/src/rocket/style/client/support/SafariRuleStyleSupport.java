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

import rocket.util.client.JavaScript;

import com.google.gwt.core.client.JavaScriptObject;

public class SafariRuleStyleSupport extends SafariStyleSupport {

	@Override
	protected String getUserSelect(final JavaScriptObject rule) {
		return this.getUserSelectProperty(rule);
	}

	@Override
	protected String getString(final JavaScriptObject rule, final String name) {
		return getProperty(rule, name);
	}

	@Override
	protected void setUserSelect(final JavaScriptObject element, final String value) {
		this.setString(element, this.getUserSelectPropertyName(), value);
	}

	@Override
	protected void setString(final JavaScriptObject rule, final String name, final String value) {
		this.setProperty(rule, name, value);
	}

	@Override
	protected void remove0(final JavaScriptObject rule, final String name) {
		this.removeProperty(rule, name);
	}

	@Override
	public String[] getPropertyNames(final JavaScriptObject rule) {
		return JavaScript.getPropertyNames(JavaScript.getObject(rule, "style"));
	}
}
