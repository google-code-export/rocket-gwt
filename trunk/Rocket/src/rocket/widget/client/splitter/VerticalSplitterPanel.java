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

import java.util.Iterator;
import java.util.List;

import rocket.dom.client.Dom;
import rocket.event.client.MouseMoveEvent;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A VerticalSplitterPanel is a panel that lays out its widget in a vertical
 * manner. The user can drag the splitter to change the size allocated for each
 * widget.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class VerticalSplitterPanel extends SplitterPanel {

	public VerticalSplitterPanel() {
		super();
	}

	protected void afterCreateWidget() {
		this.setItems(createItems());
		InlineStyle.setString(DOM.getChild(this.getElement(), 0), StyleConstants.OVERFLOW_X, "hidden");
	}

	protected String getInitialStyleName() {
		return Constants.VERTICAL_SPLITTER_PANEL_STYLE;
	}

	/**
	 * This factory method creates a new splitter on demand.
	 * 
	 * @return A new Vertical Splitter
	 */
	protected Widget createSplitter() {
		return new VerticalSplitter();
	}

	class VerticalSplitter extends Splitter {

		VerticalSplitter() {
			super();
		}

		protected void afterCreateElement() {
			super.afterCreateElement();

			this.setWidth("100%");
			this.setHeight(VerticalSplitterPanel.this.getSplitterSize() + "px");
		}

		protected String getInitialStyleName() {
			return VerticalSplitterPanel.this.getSplitterStyle();
		}

		protected String getDraggingStyleName() {
			return VerticalSplitterPanel.this.getDraggingStyle();
		}
	}

	protected String getSplitterStyle() {
		return Constants.VERTICAL_SPLITTER_PANEL_SPLITTER_STYLE;
	}

	protected String getDraggingStyle() {
		return Constants.VERTICAL_SPLITTER_PANEL_SPLITTER_DRAGGING_STYLE;
	}

	/**
	 * This is the most important event handler that takes care of adjusting the
	 * widths of the widgets before and after the splitter being moved.
	 * 
	 * @param splitter
	 * @param event
	 */
	protected void handleMouseMove(final MouseMoveEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while (true) {
			final Splitter splitter = (Splitter) event.getWidget();

			// need to figure out if mouse has moved to the right or left...
			final int mouseY = event.getPageY();
			final int splitterY = Dom.getAbsoluteTop(splitter.getElement());

			// if the mouse has not moved vertically but vertically so exit...
			int delta = mouseY - splitterY;
			if (0 == delta) {
				break;
			}

			// grab the widgets before and after the splitter being dragged...
			final InternalPanel panel = this.getPanel();
			final int panelIndex = panel.indexOf(splitter);
			final Widget beforeWidget = panel.get(panelIndex - 1);
			int beforeWidgetHeight = beforeWidget.getOffsetHeight() + delta;

			final Widget afterWidget = panel.get(panelIndex + 1);
			int afterWidgetHeight = afterWidget.getOffsetHeight() - delta;

			final int heightSum = beforeWidgetHeight + afterWidgetHeight;

			final List items = this.getItems();

			// if the mouse moved left make sure the beforeWidget width is not
			// less than its minimumHeight.
			if (delta < 0) {
				final SplitterItem beforeWidgetItem = (SplitterItem) items.get(panelIndex / 2);
				final int minimumHeight = beforeWidgetItem.getMinimumSize();

				if (beforeWidgetHeight < minimumHeight) {
					delta = minimumHeight - (beforeWidgetHeight - delta);
					beforeWidgetHeight = minimumHeight;
					afterWidgetHeight = heightSum - beforeWidgetHeight;
				}
			}

			// since the mouse moved right make sure the afterWidget width is
			// not less than its minimumHeight
			if (delta > 0) {
				final SplitterItem afterWidgetItem = (SplitterItem) items.get(panelIndex / 2 + 1);
				final int minimumHeight = afterWidgetItem.getMinimumSize();
				if (afterWidgetHeight < minimumHeight) {
					delta = afterWidgetHeight + delta - minimumHeight;
					afterWidgetHeight = minimumHeight;
					beforeWidgetHeight = heightSum - afterWidgetHeight;
				}
			}

			// save!
			beforeWidget.setHeight(beforeWidgetHeight + "px");
			afterWidget.setHeight(afterWidgetHeight + "px");

			// update the coordinates of both the splitter and after widget...
			adjustYCoordinate(splitter, delta);
			adjustYCoordinate(afterWidget, delta);

			beforeWidget.setWidth("100%");
			splitter.setWidth("100%");
			afterWidget.setWidth("100%");

			// its necessary to prevent the event to stop text selection in opera.
			break;
		}
	}

	protected void adjustYCoordinate(final Widget widget, final int delta) {
		final Element element = widget.getElement();
		final int y = InlineStyle.getInteger(element, StyleConstants.TOP, CssUnit.PX, 0);

		InlineStyle.setString(element, StyleConstants.POSITION, "absolute");
		InlineStyle.setInteger(element, StyleConstants.LEFT, 0, CssUnit.PX);
		InlineStyle.setInteger(element, StyleConstants.TOP, y + delta, CssUnit.PX);
	}

	/**
	 * Lays out all added widgets summing their individual weights and then
	 * assigns widths to each.
	 */
	protected void redraw0() {
		final int weightSum = this.sumWeights();
		final InternalPanel panel = this.getPanel();
		final int availableHeight = DOM.getElementPropertyInt(panel.getParentElement(), "offsetHeight");

		final int splitterCount = (panel.getWidgetCount() - 1) / 2;
		final int splitterHeight = this.getSplitterSize();
		final int allocatedWidgetHeight = availableHeight - splitterCount * splitterHeight;
		final float ratio = (float) allocatedWidgetHeight / weightSum;

		int top = 0;
		final Iterator items = this.getItems().iterator();
		final Iterator widgets = panel.iterator();

		boolean more = widgets.hasNext();

		while (more) {
			final Widget widget = (Widget) widgets.next();
			final SplitterItem item = (SplitterItem) items.next();

			// set the widget position...
			final Element widgetElement = widget.getElement();

			InlineStyle.setString(widgetElement, StyleConstants.POSITION, "absolute");
			InlineStyle.setInteger(widgetElement, StyleConstants.LEFT, 0, CssUnit.PX);
			InlineStyle.setInteger(widgetElement, StyleConstants.TOP, top, CssUnit.PX);

			// overflow...
			InlineStyle.setString(widgetElement, StyleConstants.OVERFLOW, "hidden");

			// set the size(width/height)...
			widget.setWidth("100%");

			// is the last widget ???
			if (false == widgets.hasNext()) {
				widget.setHeight((availableHeight - top) + "px");
				break;
			}

			// calculate the new width...
			final int weight = item.getSizeShare();
			final int height = (int) (ratio * weight);
			widget.setHeight(height + "px");

			top = top + height;

			final Widget splitter = (Widget) widgets.next();

			// set the splitter position...
			final Element splitterElement = splitter.getElement();
			InlineStyle.setString(splitterElement, StyleConstants.POSITION, "absolute");
			InlineStyle.setInteger(splitterElement, StyleConstants.LEFT, 0, CssUnit.PX);
			InlineStyle.setInteger(splitterElement, StyleConstants.TOP, top, CssUnit.PX);

			// overflow...
			InlineStyle.setString(widgetElement, StyleConstants.OVERFLOW, "hidden");

			// set the splitters size...
			splitter.setWidth("100%");
			splitter.setHeight(splitterHeight + "px");

			top = top + splitterHeight;

			more = widgets.hasNext();
		}
	}
}
