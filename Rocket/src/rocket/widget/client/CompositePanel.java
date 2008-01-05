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

import java.util.Iterator;

import rocket.collection.client.CollectionsHelper;
import rocket.event.client.EventListener;
import rocket.util.client.Checker;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A base class for any panel that is in fact a composite that makes it easier
 * to author new composite panel.
 * 
 * This class includes event listener collections for all the basic events. On
 * top of sinking events sub classes must also make the corresponding
 * addXXXListener and removeXXXListener methods public.
 * 
 * Creating a new widget including a new element.
 * <ul>
 * <li>{@link #beforeCreatePanel()}</li>
 * <li>{@link #initWidget( createPanel() )}</li>
 * <li>{@link #afterCreatePanel()}</li>
 * <li>{@link #applyStyleName()} Override this to do nothing if this widget has
 * no initial style.</li>
 * </li>
 * 
 * The initial style of the root element from this composite widget is taken
 * from {@link #getInitialStyleName()}
 * 
 * @author Miroslav Pokorny
 */
abstract public class CompositePanel extends rocket.widget.client.Panel implements HasWidgets, EventListener {

	protected CompositePanel() {
		super(true);

		this.prepare();
	}

	protected void prepare() {
		this.beforeCreatePanel();
		this.setPanel(this.createPanel());
		this.afterCreatePanel();

		this.applyStyleName();
	}

	protected CompositePanel(final Panel panel) {
		super(true);

		prepare(panel);
	}

	protected void prepare(final Panel panel) {
		this.checkPanel(panel);

		this.beforeCreatePanel();
		this.setPanel(panel);
		this.afterCreatePanel();

		this.applyStyleName();
	}

	protected void beforeCreatePanel() {
		super.beforeCreatePanelElement();
	}

	abstract protected void checkPanel(final Panel panel);

	/**
	 * This method is called when necessary to create a new panel.
	 * 
	 * @return The new as yet unwrapped panel
	 */
	abstract protected Panel createPanel();

	abstract protected void afterCreatePanel();

	/**
	 * The embedded composite panel.
	 */
	private Panel panel;

	protected Panel getPanel() {
		Checker.notNull("field:panel", panel);
		return panel;
	}

	protected boolean hasPanel() {
		return null != panel;
	}

	protected void setPanel(final Panel panel) {
		Checker.notNull("parameter:panel", panel);

		this.setElement(panel.getElement());

		this.panel = panel;
	}

	protected void onAttach() {
		if (isAttached()) {
			throw new IllegalStateException("Should only call onAttach when the widget is detached from the browser's document");
		}
		this.setAttachFlag(this, true);
		this.invokePanelOnAttach(this.getPanel());
		this.doSinkEvents();
	}

	native private void setAttachFlag(final Widget widget, final boolean attached)/*-{
	 widget.@com.google.gwt.user.client.ui.Widget::attached=attached;
	 }-*/;

	native private void invokePanelOnAttach(final Panel panel)/*-{
	 panel.@com.google.gwt.user.client.ui.Widget::onAttach()();
	 }-*/;

	protected void onDetach() {
		if (false == isAttached()) {
			throw new IllegalStateException("Should only call onDetach when the widget is attached to the browser's document");
		}

		try {
			onUnload();
			this.setAttachFlag(this, false);
		} finally {
			this.invokePanelOnDetach(this.getPanel());

			if (0 != this.getSunkEventsBitMask()) {
				DOM.setEventListener(this.getSunkEventsTarget(), this);
			}
		}
	}

	native private void invokePanelOnDetach(final Panel panel)/*-{
	 panel.@com.google.gwt.user.client.ui.Widget::onDetach()();
	 }-*/;

	abstract public void insert(final Widget widget, final int indexBefore);

	public Widget get(final int index) {
		Widget found = null;

		int count = 0;
		final Iterator iterator = this.iterator();
		while (iterator.hasNext()) {
			final Widget widget = (Widget) iterator.next();
			if (index == count) {
				found = widget;
				break;
			}
			count++;
		}
		return found;
	}

	public int indexOf(final Widget widget) {
		Checker.notNull("parameter:widget", widget);

		int index = -1;
		final Iterator iterator = this.iterator();
		while (iterator.hasNext()) {
			final Widget otherWidget = (Widget) iterator.next();
			if (widget.equals(otherWidget)) {
				break;
			}
		}
		return index;
	}

	public int getWidgetCount() {
		int count = 0;
		final Iterator iterator = this.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			count++;
		}
		return count;
	}

	public Iterator iterator() {
		return this.getPanel().iterator();
	}

	abstract public boolean remove(final Widget widget);

	public void clear() {
		CollectionsHelper.removeAll(this.iterator());
	}

	// THE METHODS BELOW SHOULD BE IGNORED
	final protected void checkElement(final Element element) {
		throw new UnsupportedOperationException("checkElement");
	}

	final protected Element createPanelElement() {
		throw new UnsupportedOperationException("createPanelElement");
	}

	final protected void insert0(final Element element, final int indexBefore) {
		throw new UnsupportedOperationException("insert0");
	}

	final protected void remove0(final Element element, final int index) {
		throw new UnsupportedOperationException("remove0");
	}
}
