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
import com.google.gwt.user.client.ui.Widget;

/**
 * This panel enforces the rule that this panel can only contain a single
 * widget.
 * 
 * @author Miroslav Pokorny
 */
abstract public class SimplePanel extends Panel {

	public SimplePanel() {
		super();
	}

	public SimplePanel(final Element element) {
		super(element);
	}

	@Override
	public void add(final Widget widget) {
		this.oneWidgetOnlyGuard(this.getWidgetCount());

		super.add(widget);
	}

	@Override
	public void insert(final Widget widget, final int indexBefore) {
		this.oneWidgetOnlyGuard(indexBefore);

		super.insert(widget, indexBefore);
	}

	protected void oneWidgetOnlyGuard(final int index) {
		if (this.getWidgetCount() > 0 || index > 0) {
			throw new UnsupportedOperationException("Unable to add more than one widget to this " + this.getClass().getName());
		}
	}

	public Widget getWidget() {
		Widget widget = null;
		if (this.getWidgetCount() == 1) {
			widget = this.get(0);
		}
		return widget;
	}

	public void setWidget(final Widget widget) {
		if (this.getWidgetCount() == 1) {
			this.remove(0);
		}
		if (null != widget) {
			this.add(widget);
		}
	}
}
