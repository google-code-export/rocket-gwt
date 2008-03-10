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
import com.google.gwt.user.client.ui.impl.FocusImpl;

/**
 * Convenient form element that includes some common functionality found within
 * all simple widgets.
 * 
 * @author Miroslav Pokorny
 */
abstract class FocusWidget extends Widget {

	public FocusWidget() {
		super();
	}

	public FocusWidget(final Element element) {
		super(element);
	}

	FocusImpl getFocusSupport() {
		return FocusImpl.getFocusImplForWidget();
	}

	public boolean isEnabled() {
		return !DOM.getElementPropertyBoolean(getElement(), "disabled");
	}

	public void setEnabled(boolean enabled) {
		DOM.setElementPropertyBoolean(getElement(), "disabled", !enabled);
	}

	public void setAccessKey(final char key) {
		DOM.setElementProperty(getElement(), "accessKey", "" + key);
	}

	public void setFocus(final boolean focused) {
		final FocusImpl support = this.getFocusSupport();
		final Element element = this.getElement();
		if (focused) {
			support.focus(element);
		} else {
			support.blur(element);
		}
	}

	public String getName() {
		return DOM.getElementProperty(getElement(), "name");
	}

	public void setName(String name) {
		DOM.setElementProperty(getElement(), "name", name);
	}

	public int getTabIndex() {
		return this.getFocusSupport().getTabIndex(getElement());
	}

	public void setTabIndex(final int tabIndex) {
		this.getFocusSupport().setTabIndex(getElement(), tabIndex);
	}
}
