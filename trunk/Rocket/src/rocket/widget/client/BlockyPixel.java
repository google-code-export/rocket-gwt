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

import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.Colour;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget is a panel made up of blocks which can be individually addressed.
 * The actual size of the panel is determined by the space allocated by its
 * parent. Individual blocks may be addressed set/get
 * 
 * Whenever either the rows/columns are set the colours of the grid are lost and
 * all cells must be recoloured/set.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class BlockyPixel extends Composite implements PixelGrid {

	public BlockyPixel() {
	}

	protected Widget createWidget() {
		final Grid grid = this.createGrid();
		this.setGrid(grid);
		return grid;
	}

	protected int getSunkEventsBitMask() {
		return 0;
	}

	/**
	 * Clears the entire canvas so that it shares a common colour.
	 * 
	 * @param colour
	 */
	public void clear(final Colour colour) {
		ObjectHelper.checkNotNull("parameter:colour", colour);

		final int rows = this.getRows();
		final int columns = this.getColumns();
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				this.setColour(x, y, colour);
			}
		}
	}

	/**
	 * A cache of all the colours currently within the table that is the blocky
	 * pixels.
	 */
	private Colour[] colours;

	protected Colour[] getColours() {
		ObjectHelper.checkNotNull("parameter:colours", colours);
		return this.colours;
	}

	protected void setColours(final Colour[] colours) {
		ObjectHelper.checkNotNull("parameter:colours", colours);
		this.colours = colours;
	}

	protected Element getCell(final int x, final int y) {
		return this.getGrid().getCellFormatter().getElement(y, x);
	}

	/**
	 * Retrieves the colour of the blocky pixel at the given coordinates.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Colour getColour(final int x, final int y) {
		return getColours()[x + y * this.getColumns()];
	}

	/**
	 * Sets the blocky pixel at the given coordinates a new colour
	 * 
	 * The corresponding table cell is only updated if the parameter:colour is
	 * different!
	 * 
	 * @param x
	 * @param y
	 * @param colour
	 */
	public void setColour(final int x, final int y, final Colour colour) {
		final Colour previousColour = this.getColour(x, y);
		if (previousColour != colour) {
			final Element cell = getCell(x, y);
			InlineStyle.setColour(cell, StyleConstants.BACKGROUND_COLOR, colour);
			getColours()[x + y * this.getColumns()] = colour;
		}
	}

	/**
	 * This grid contains the blocky pixels.
	 */
	private Grid grid;

	protected Grid getGrid() {
		ObjectHelper.checkNotNull("field:grid", grid);
		return grid;
	}

	protected boolean hasGrid() {
		return null != grid;
	}

	protected void setGrid(final Grid grid) {
		ObjectHelper.checkNotNull("parameter:grid", grid);

		this.grid = grid;
	}

	protected Grid createGrid() {
		final Grid grid = new Grid();
		grid.setStyleName(WidgetConstants.BLOCKY_PIXEL_STYLE);
		grid.setCellPadding(0);
		grid.setCellSpacing(0);
		return grid;
	}

	public int getColumns() {
		return this.getGrid().getColumnCount();
	}

	public void setColumns(final int columns) {
		this.getGrid().resizeColumns(columns);

		this.setColours(new Colour[columns * this.getRows()]);
	}

	public int getRows() {
		return this.getGrid().getRowCount();
	}

	public void setRows(final int rows) {
		this.getGrid().resizeRows(rows);

		this.setColours(new Colour[rows * this.getColumns()]);
	}

	public String toString() {
		return super.toString() + ", grid: " + grid;
	}
}