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

import java.util.HashMap;
import java.util.Map;

import rocket.dom.client.Dom;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

/**
 * A GridView uses a table to hold widgets which are sourced from an enclosed
 * WidgetProvider
 * 
 * Whenever any of the properties such as rows/columns/cursor are updated the
 * widget redraws itself,
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class GridView extends CompositeWidget {

	public GridView() {
		this.setAutoRedraw(false);
	}

	protected Widget createWidget() {
		final Grid grid = this.createGrid();
		this.setGrid(grid);
		return grid;
	}

	protected String getInitialStyleName() {
		return WidgetConstants.GRIDVIEW_STYLE;
	}

	public int getSunkEventsBitMask() {
		return 0;
	}

	private boolean autoRedraw;

	public boolean isAutoRedraw() {
		return this.autoRedraw;
	}

	public void setAutoRedraw(final boolean autoRedraw) {
		this.autoRedraw = autoRedraw;
	}

	protected void redrawIfAutoEnabled() {
		if (this.isAutoRedraw()) {
			this.redraw();
		}
	}

	/**
	 * Refreshes the grid using the WidgetProvider to fill in each cell.
	 * Wherever possible cells are reused with only new widgets created for
	 * newly introduced cells.
	 */
	public void redraw() {
		final Grid table = this.getGrid();
		final int tableRowCount = this.getRows();

		// copy widgets to a map using their index as the key. this will enable
		// widget to reuse widget rather than recreate.
		final Map cache = new HashMap();

		for (int r = 0; r < tableRowCount; r++) {
			final int columns = table.getCellCount(r);

			for (int c = 0; c < columns; c++) {
				final Widget widget = table.getWidget(r, c);
				if (widget instanceof GridCell) {
					final GridCell cell = (GridCell) widget;
					cache.put(buildKey(cell.getIndex()), widget);
				}

				if (null != widget) {
					widget.removeFromParent();
				}
			}
		}

		// table.clear();

		final int rows = this.getRows();
		final int columns = this.getColumns();
		final int first = this.getFirst();
		final int last = this.getLast();
		final int cursor = this.getCursor();

		final Grid.CellFormatter cellFormatter = table.getCellFormatter();
		final String gridViewCellStyle = this.getCellStyle();

		int lastValidIndex = Integer.MIN_VALUE;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				Widget cellWidget = null;
				final int cellIndex = r * columns + c + cursor;

				while (true) {
					cellWidget = (Widget) cache.get(buildKey(cellIndex));
					if (null != cellWidget) {
						break;
					}

					if (cellIndex < first || cellIndex >= last) {
						cellWidget = this.createFillerWidget();
						cellWidget.addStyleName(this.getFillerStyle());
						cellWidget = new GridCell(cellWidget, cellIndex);
						break;
					}

					cellWidget = this.createCellWidget(cellIndex);
					cellWidget = new GridCell(cellWidget, cellIndex);
					lastValidIndex = cellIndex;
					break;
				}
				cellFormatter.addStyleName(r, c, gridViewCellStyle);
				table.setWidget(r, c, cellWidget);
			}
		}

		this.setLastValidIndex(lastValidIndex);
	}

	private String buildKey(final int cellIndex) {
		return "" + cellIndex;
	}

	protected String getCellStyle() {
		return WidgetConstants.GRIDVIEW_CELL_STYLE;
	}

	abstract protected int getFirst();

	abstract protected int getLast();

	abstract protected Widget createCellWidget(int index);

	/**
	 * This panel encloses the widget placed in a cell. It contains an extra
	 * property that records the index of the cell.
	 */
	static class GridCell extends SimplePanel {
		GridCell(final Widget widget, final int index) {
			super();
			this.setWidget(widget);
			this.setIndex(index);
		}

		protected Element createPanelElement() {
			return DOM.createSpan();
		}

		protected void checkElement(final Element element) {
		}

		protected String getInitialStyleName() {
			return "";
		}

		protected int getSunkEventsBitMask() {
			return 0;
		}

		protected void insert0(final Element element, int indexBefore) {
			DOM.insertChild(this.getElement(), element, indexBefore);
		}

		protected void remove0(final Element element, int index) {
			Dom.removeFromParent(element);
		}

		/**
		 * Records the index for the enclosed widget. This allows the redraw
		 * method to reuse widgets where ever possible.
		 */
		int index;

		int getIndex() {
			return index;
		}

		void setIndex(final int index) {
			this.index = index;
		}
	}

	/**
	 * This grid is the table that will display the grid of widgets
	 */
	private Grid grid;

	protected Grid getGrid() {
		ObjectHelper.checkNotNull("field:grid", grid);
		return grid;
	}

	protected void setGrid(final Grid grid) {
		ObjectHelper.checkNotNull("parameter:grid", grid);
		this.grid = grid;
	}

	protected Grid createGrid() {
		return new Grid();
	}

	// GRID ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * The pointer which indexes the first visible item from the widgetprovider
	 */
	private int cursor;

	public int getCursor() {
		return this.cursor;
	}

	public void setCursor(final int cursor) {
		this.cursor = cursor;

		this.redrawIfAutoEnabled();
	}

	/**
	 * The number of visible rows
	 */
	public int getRows() {
		return this.getGrid().getRowCount();
	}

	public void setRows(final int rows) {
		this.getGrid().resizeRows(rows);
		this.redrawIfAutoEnabled();
	}

	public int getColumns() {
		return this.getGrid().getCellCount(0);
	}

	public void setColumns(final int columns) {
		this.getGrid().resizeColumns(columns);
		this.redrawIfAutoEnabled();
	}

	abstract protected Widget createFillerWidget();

	protected String getFillerStyle() {
		return WidgetConstants.GRIDVIEW_FILLER_STYLE;
	}

	/**
	 * Because a grid may be larger than the number of widgets that may be
	 * supplied by a WidgetProvider this method provides a means of knowing the
	 * true actual last valid index.
	 */
	private int lastValidIndex;

	public int getLastValidIndex() {
		return this.lastValidIndex;
	}

	protected void setLastValidIndex(final int lastValidIndex) {
		this.lastValidIndex = lastValidIndex;
	}

	protected void clearLastValidIndex() {
		this.lastValidIndex = Integer.MIN_VALUE;
	}

	public void clear() {
		this.getGrid().clear();
	}

	public String toString() {
		return super.toString() + ", cursor: " + cursor;
	}
}
