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

import rocket.util.client.Colour;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.Random;

/**
 * Strictly not a widget but rather a controls another.
 * 
 * Uses a BlockyGrid to display a life simulation. Rather than repainting each and every block on each iteration only changes are painted
 * via {@link #setLiveCell} and {@link #setDeadCell}.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Life {

    /**
     * The colour used to paint blocks that represent live cells
     */
    private Colour liveCellColour;

    public Colour getLiveCellColour() {
        ObjectHelper.checkNotNull("field:liveCellColour", liveCellColour);
        return this.liveCellColour;
    }

    public void setLiveCellColour(final Colour liveCellColour) {
        ObjectHelper.checkNotNull("parameter:liveCellColour", liveCellColour);
        this.liveCellColour = liveCellColour;
    }

    /**
     * The colour used to paint background / dead cells
     */
    private Colour deadCellColour;

    public Colour getDeadCellColour() {
        ObjectHelper.checkNotNull("field:deadCellColour", deadCellColour);
        return this.deadCellColour;
    }

    public void setDeadCellColour(final Colour deadCellColour) {
        ObjectHelper.checkNotNull("parameter:deadCellColour", deadCellColour);
        this.deadCellColour = deadCellColour;
    }

    /**
     * The target that gets updated during each pass.
     */
    private PixelGrid pixelGrid;

    public PixelGrid getPixelGrid() {
        ObjectHelper.checkNotNull("field:pixelGrid", pixelGrid);
        return pixelGrid;
    }

    public void setPixelGrid(final PixelGrid pixelGrid) {
        ObjectHelper.checkNotNull("parameter:pixelGrid", pixelGrid);
        this.pixelGrid = pixelGrid;

        this.syncWithPixelGrid();
    }

    protected void syncWithPixelGrid() {
        final PixelGrid grid = this.getPixelGrid();
        this.setCells(new boolean[grid.getColumns() * grid.getRows()]);
    }

    /**
     * Randonly populates the cells grid with live/dead cells.
     * 
     * @param trueBias
     *            The higher the value the more live cells A good starting number is -1234567890
     */
    public void createCells(final int trueBias) {
        final boolean[] cells = this.getCells();
        final PixelGrid grid = this.getPixelGrid();
        final int rows = grid.getRows();
        final int columns = grid.getColumns();

        int i = 0;
        int j = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                final boolean bit = Random.nextInt() < trueBias;
                ;
                cells[i++] = bit;
                if (bit) {
                    j++;
                }
            }
        }
    }

    /**
     * Updates the grid of live/dead cells.
     * 
     * @return The number of individual cells which changed from being live to dead and vice versa.
     */
    public int update() {
        int deltas = 0;

        final boolean[] cells = this.getCells();
        final PixelGrid grid = this.getPixelGrid();
        final int rows = grid.getRows();
        final int columns = grid.getColumns();
        final int rowsLessOne = rows - 1;
        final int columnsLessOne = columns - 1;

        final boolean[] newCells = new boolean[cells.length];

        int rowOffset = 0;
        int rowAboveOffset = rowsLessOne * columns;

        for (int y = 0; y < rows; y++) {

            final int rowBelowOffset = y < rowsLessOne ? rowOffset + columns : 0;

            for (int x = 0; x < columns; x++) {
                int surroundingLiveCellCount = 0;

                final int columnBeforeOffset = (x == 0 ? columnsLessOne : x - 1);
                final int columnAfterOffset = (x == columnsLessOne ? 0 : x + 1);

                if (cells[rowAboveOffset + columnBeforeOffset]) {
                    surroundingLiveCellCount++;
                }
                if (cells[rowAboveOffset + x]) {
                    surroundingLiveCellCount++;
                }
                if (cells[rowAboveOffset + columnAfterOffset]) {
                    surroundingLiveCellCount++;
                }
                if (cells[rowOffset + columnBeforeOffset]) {
                    surroundingLiveCellCount++;
                }
                if (cells[rowOffset + columnAfterOffset]) {
                    surroundingLiveCellCount++;
                }
                if (cells[rowBelowOffset + columnBeforeOffset]) {
                    surroundingLiveCellCount++;
                }
                if (cells[rowBelowOffset + x]) {
                    surroundingLiveCellCount++;
                }
                if (cells[rowBelowOffset + columnAfterOffset]) {
                    surroundingLiveCellCount++;
                }

                final boolean previousBit = cells[rowOffset + x];
                final boolean bit = processCell(previousBit, surroundingLiveCellCount);
                newCells[rowOffset + x] = bit;

                // dont bother to recolour cells that havent changed only
                // repaint those that have changed.
                if (bit != previousBit) {
                    if (bit) {
                        this.setLiveCell(x, y);
                    } else {
                        this.setDeadCell(x, y);
                    }
                    deltas++;
                }
            }
            rowAboveOffset = rowOffset;
            rowOffset = rowOffset + columns;
        }

        this.setCells(newCells);

        return deltas;
    }

    protected void setDeadCell(final int x, final int y) {
        this.getPixelGrid().setColour(x, y, this.getDeadCellColour());
    }

    protected void setLiveCell(final int x, final int y) {
        this.getPixelGrid().setColour(x, y, this.getLiveCellColour());
    }

    /**
     * The rules which update the cells of the bit are taken from wikipedia
     * 
     * http://en.wikipedia.org/wiki/Conway's_Game_of_Life
     * 
     * The universe of the Game of Life is an infinite two-dimensional grid of cells, each of which is either alive or dead. Cells interact
     * with their eight neighbours, which are the cells that are directly horizontally, vertically, or diagonally adjacent. At each step in
     * time, the following effects occur:
     * 
     * 1. Any live cell with fewer than two neighbours dies, as if by loneliness.
     * 
     * 2. Any live cell with more than three neighbours dies, as if by overcrowding.
     * 
     * 3. Any live cell with two or three neighbours lives, unchanged, to the next generation.
     * 
     * 4. Any dead cell with exactly three neighbours comes to life.
     * 
     * The initial pattern constitutes the first generation of the system. The second generation is created by applying the above rules
     * simultaneously to every cell in the first generation -- births and deaths happen simultaneously, and the discrete moment at which
     * this happens is called a tick. The rules continue to be applied repeatedly to create further generations.
     * 
     * @param bit
     * @param surroundingLiveCellCount
     * @return
     */
    protected boolean processCell(boolean bit, final int surroundingLiveCellCount) {
        while (true) {
            if (bit) {
                // # Any live cell with fewer than two neighbours dies, as if by
                // loneliness.
                if (surroundingLiveCellCount < 2) {
                    bit = false;
                    break;
                }
                // Any live cell with more than three neighbours dies, as if by
                // overcrowding
                if (surroundingLiveCellCount > 3) {
                    bit = false;
                    break;
                }
                // Any live cell with two or three neighbours lives, unchanged,
                // to the next generation.
                if (surroundingLiveCellCount == 2 || surroundingLiveCellCount == 3) {
                    break;
                }
                break;
            }
            // Any dead cell with exactly three neighbours comes to life.
            if (surroundingLiveCellCount == 3) {
                bit = true;
            }
            break;
        }

        return bit;
    }

    /**
     * A local copy of the live/dead cells of each individual cell.
     */
    private boolean[] cells;

    public boolean[] getCells() {
        return cells;
    }

    public void setCells(final boolean[] cells) {
        this.cells = cells;
    }

    public String toString() {
        return super.toString() + ", pixelGrid: " + pixelGrid + ", cells: " + cells + ", liveCellColour: "
                + liveCellColour + ", deadCellColour: " + deadCellColour;
    }
}