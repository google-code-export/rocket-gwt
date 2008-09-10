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

import rocket.event.client.MouseMoveEvent;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.Checker;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A HorizontalSplitterPanel is a panel that lays out its widget in a horizontal
 * manner. The user can drag the splitter to change the size allocated for each
 * widget.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HorizontalSplitterPanel extends SplitterPanel {

	public HorizontalSplitterPanel() {
		super();
	}

	@Override
	protected void afterCreateWidget() {
		this.setItems(createItems());

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle( DOM.getChild(this.getElement(), 0) ); 
		inlineStyle.setString(Css.OVERFLOW_X, "hidden");
		inlineStyle.setString(Css.OVERFLOW_Y, "hidden");
	}

	@Override
	protected String getInitialStyleName() {
		return Constants.HORIZONTAL_SPLITTER_PANEL_STYLE;
	}

	/**
	 * This factory method creates a new splitter on demand.
	 * 
	 * @return
	 */
	protected Widget createSplitter() {
		return new HorizontalSplitter();
	}

	private class HorizontalSplitter extends Splitter {

		HorizontalSplitter() {
			super();
		}

		@Override
		protected void afterCreateElement() {
			super.afterCreateElement();

			this.setWidth(HorizontalSplitterPanel.this.getSplitterSize() + "px");
			this.setHeight("100%");
		}

		@Override
		protected String getInitialStyleName() {
			return HorizontalSplitterPanel.this.getSplitterStyle();
		}

		@Override
		protected String getDraggingStyleName() {
			return HorizontalSplitterPanel.this.getDraggingStyle();
		}
	}

	protected String getSplitterStyle() {
		return Constants.HORIZONTAL_SPLITTER_PANEL_SPLITTER_STYLE;
	}

	protected String getDraggingStyle() {
		return Constants.HORIZONTAL_SPLITTER_PANEL_SPLITTER_DRAGGING_STYLE;
	}

	/**
	 * This is the most important event handler that takes care of adjusting the
	 * widths of the widgets before and after the splitter being moved.
	 * 
	 * @param event
	 */
	protected void onMouseMove(final MouseMoveEvent event) {
		Checker.notNull("parameter:event", event);

		while (true) {
			final Splitter splitter = (Splitter) event.getWidget();

			// need to figure out if mouse has moved to the right or left...
			final int mouseX = event.getPageX();
			final int splitterX = splitter.getElement().getAbsoluteLeft();

			// if the mouse has not moved horizontally but vertically so exit...
			int delta = mouseX - splitterX;
			if (0 == delta) {
				break;
			}

			// grab the widgets before and after the splitter being dragged...
			final InternalPanel panel = this.getPanel();
			final int panelIndex = panel.indexOf(splitter);
			final Widget beforeWidget = panel.get(panelIndex - 1);
			int beforeWidgetWidth = beforeWidget.getOffsetWidth() + delta;

			final Widget afterWidget = panel.get(panelIndex + 1);
			int afterWidgetWidth = afterWidget.getOffsetWidth() - delta;

			final int widthSum = beforeWidgetWidth + afterWidgetWidth;

			final List<SplitterItem> items = this.getItems();

			// if the mouse moved left make sure the beforeWidget width is not
			// less than its minimumWidth.
			if (delta < 0) {
				final SplitterItem beforeWidgetItem = items.get(panelIndex / 2);
				final int minimumWidth = beforeWidgetItem.getMinimumSize();

				if (beforeWidgetWidth < minimumWidth) {
					delta = minimumWidth - (beforeWidgetWidth - delta);
					beforeWidgetWidth = minimumWidth;
					afterWidgetWidth = widthSum - beforeWidgetWidth;
				}
			}

			// since the mouse moved right make sure the afterWidget width is
			// not less than its minimumWidth
			if (delta > 0) {
				final SplitterItem afterWidgetItem = items.get(panelIndex / 2 + 1);
				final int minimumWidth = afterWidgetItem.getMinimumSize();
				if (afterWidgetWidth < minimumWidth) {
					delta = afterWidgetWidth + delta - minimumWidth;
					afterWidgetWidth = minimumWidth;
					beforeWidgetWidth = widthSum - afterWidgetWidth;
				}
			}

			// save!
			beforeWidget.setWidth(beforeWidgetWidth + "px");
			afterWidget.setWidth(afterWidgetWidth + "px");

			// update the coordinates of both the splitter and after widget...
			adjustXCoordinate(splitter, delta);
			adjustXCoordinate(afterWidget, delta);

			beforeWidget.setHeight("100%");
			splitter.setHeight("100%");
			afterWidget.setHeight("100%");

			// its necessary to prevent the event to stop text selection in
			// opera.
			event.stop();
			break;
		}
	}

	protected void adjustXCoordinate(final Widget widget, final int delta) {
		final InlineStyle inlineStyle = InlineStyle.getInlineStyle( widget.getElement() );
		final int x = inlineStyle.getInteger(Css.LEFT, CssUnit.PX, 0);

		inlineStyle.setString(Css.POSITION, "absolute");
		inlineStyle.setInteger(Css.LEFT, x + delta, CssUnit.PX);
		inlineStyle.setInteger(Css.TOP, 0, CssUnit.PX);
	}

	/**
	 * Lays out all added widgets summing their individual weights and then
	 * assigns widths to each.
	 */
	protected void redraw0() {
		final int weightSum = this.sumWeights();
		final InternalPanel panel = this.getPanel();
		final int availableWidth = panel.getParentElement().getOffsetWidth();

		final int splitterCount = (panel.getWidgetCount() - 1) / 2;
		final int splitterWidth = this.getSplitterSize();
		final int allocatedWidgetWidth = availableWidth - splitterCount * splitterWidth;
		final float ratio = (float) allocatedWidgetWidth / weightSum;

		int left = 0;
		final Iterator<SplitterItem> items = this.getItems().iterator();
		final Iterator<Widget> widgets = panel.iterator();

		boolean more = widgets.hasNext();

		while (more) {
			final Widget widget = widgets.next();
			final SplitterItem item = items.next();

			// set the widget position...
			final Element widgetElement = widget.getElement();
			final InlineStyle widgetInlineStyle = InlineStyle.getInlineStyle(widgetElement);
			widgetInlineStyle.setString(Css.POSITION, "absolute");
			widgetInlineStyle.setInteger(Css.LEFT, left, CssUnit.PX);
			widgetInlineStyle.setInteger(Css.TOP, 0, CssUnit.PX);
			widgetInlineStyle.setString(Css.OVERFLOW, "hidden");

			// set the size(width/height)...
			widget.setHeight("100%");

			// is the last widget ???
			if (false == widgets.hasNext()) {
				widget.setWidth((availableWidth - left) + "px");
				break;
			}

			// calculate the new width...
			final int weight = item.getSizeShare();
			final int width = (int) (ratio * weight);
			widget.setWidth(width + "px");

			left = left + width;

			final Widget splitter = (Widget) widgets.next();

			// set the splitter position...
			final InlineStyle splitterInlineStyle = InlineStyle.getInlineStyle( splitter.getElement() );
			splitterInlineStyle.setString(Css.POSITION, "absolute");
			splitterInlineStyle.setInteger(Css.LEFT, left, CssUnit.PX);
			splitterInlineStyle.setInteger(Css.TOP, 0, CssUnit.PX);
			splitterInlineStyle.setString(Css.OVERFLOW, "hidden");

			// set the splitters size...
			splitter.setWidth(splitterWidth + "px");
			splitter.setHeight("100%");

			left = left + splitterWidth;

			more = widgets.hasNext();
		}
	}
}
