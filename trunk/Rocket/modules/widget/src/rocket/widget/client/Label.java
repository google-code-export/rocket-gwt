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

import com.google.gwt.user.client.Element;

/**
 * A simple widget that contains the same capabilities of the GWT Label widget
 * but also adds the ability to hijack any elements from the dom.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * Label.
 * 
 * ROCKET When upgrading from GWT 1.5 RC1 reapply changes
 * 
 * @author Miroslav Pokorny
 */
public class Label extends HtmlOrLabel {
	public Label() {
		super();
	}

	public Label(final String text) {
		super();

		this.setText(text);
	}

	public Label(final Element element) {
		super(element);
	}

	@Override
	protected String getInitialStyleName() {
		return WidgetConstants.LABEL_STYLE;
	}
}
