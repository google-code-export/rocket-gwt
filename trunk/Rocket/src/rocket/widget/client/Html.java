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
package rocket.widget.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A simple widget that contains the same capabilities of the GWT HTML widget
 * but also adds the ability to hijack any elements from the dom. Not that
 * unlike the GWT HTML widget rocket's HTML does not extend Label.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * HTML widget.
 * 
 * TODO When upgrading GWT version reapply changes to this class.
 * 
 * @author Miroslav Pokorny
 */
public class Html extends HtmlOrLabel {
	public Html() {
		super();
	}

	public Html(final String html) {
		this();

		this.setHtml(html);
	}

	public Html(final Element element) {
		super(element);
	}

	protected String getInitialStyleName() {
		return WidgetConstants.HTML;
	}

	public String getHtml() {
		return DOM.getInnerHTML(getElement());
	}

	public void setHtml(final String text) {
		DOM.setInnerHTML(getElement(), text);
	}
}
