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
package rocket.widget.client.splitter;

import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

import com.google.gwt.user.client.ui.Widget;

/**
 * A SplitterItem is a holder for a widget which appears within a SplitterPanel.
 * 
 * It contains the widget and its minimum width.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class SplitterItem {

	public SplitterItem() {
		super();
	}

	/**
	 * The parent SplitterPanel that includes this item.
	 */
	private SplitterPanel splitterPanel;

	protected SplitterPanel getSplitterPanel() {
		ObjectHelper.checkNotNull("field:splitterPanel", splitterPanel);
		return this.splitterPanel;
	}

	protected boolean hasSplitterPanel() {
		return null != this.splitterPanel;
	}

	protected void setSplitterPanel(final SplitterPanel splitterPanel) {
		ObjectHelper.checkNotNull("parameter:splitterPanel", splitterPanel);
		this.splitterPanel = splitterPanel;
	}

	protected void clearSplitterPanel() {
		this.splitterPanel = null;
	}

	/**
	 * The widget itself
	 */
	private Widget widget;

	public Widget getWidget() {
		ObjectHelper.checkNotNull("field:widget", widget);
		return this.widget;
	}

	public void setWidget(final Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);
		this.widget = widget;
	}

	/**
	 * THe minimum width in pixels that maybe allocated to this widget within
	 * the parent SplitterPanel
	 */
	private int minimumSize;

	public int getMinimumSize() {
		PrimitiveHelper.checkGreaterThan("field:minimumSize", 0, minimumSize);
		return this.minimumSize;
	}

	public void setMinimumSize(final int minimumSize) {
		PrimitiveHelper.checkGreaterThan("parameter:minimumSize", 0, minimumSize);
		this.minimumSize = minimumSize;
	}

	/**
	 * The sizeShare value is used to determine how much space a new widget is
	 * allocated when added to a SplitterPanel.
	 */
	private int sizeShare;

	public int getSizeShare() {
		PrimitiveHelper.checkGreaterThan("field:sizeShare", 0, sizeShare);
		return this.sizeShare;
	}

	public void setSizeShare(final int sizeShare) {
		PrimitiveHelper.checkGreaterThan("parameter:sizeShare", 0, sizeShare);
		this.sizeShare = sizeShare;
	}

	/**
	 * Removes this splitterItem from its parent Splitter provided it has
	 * already been added.
	 */
	public void remove() {
		if (false == this.hasSplitterPanel()) {
			throw new UnsupportedOperationException(
					"This splitterItem cannot be removed because it has not yet been added to a SplitterPanel");
		}
		this.getSplitterPanel().remove(this);
		this.clearSplitterPanel();
	}

	public String toString() {
		return super.toString() + ", minimumSize: " + minimumSize + ", sizeShare: " + sizeShare + ", widget: " + widget;
	}
}
