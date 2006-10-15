/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A powerful easy sortable table to use. The basic idea is to provide value objects one for each row. THe main purpose of the sub-class is
 * to provide a switch statement that returns values from a value object aka the row given an index ( the column). Once the table has been
 * sorted based on the current sort column the sub-class is again queried for the widget that should be present at a particular cell. This
 * allows one to sort based on a particular value but to present another to the client. THe entire redraw lifecycle and selection of what
 * value object appears on what row and so on is managed by the widget.
 * 
 * Column comparators should be set before adding any headers. Once headers have been created it is possible to set the order
 * (ascending/descending) for any sortable column Refer to the bundle test that shows how simple it is to subclass and use this
 * implementation.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class SortableTable extends ZebraFlexTable {

    public SortableTable() {
        super();
        this.addStyleName(WidgetConstants.SORTABLE_TABLE_STYLE);
        this.setColumnComparators(new ArrayList());
        this.setRows(new ArrayList());
        this.setAutoRedraw(true);
    }

    /**
     * A list of comparators one for each column or null if a column is not sortable.
     */
    private List columnComparators;

    protected List getColumnComparators() {
        ObjectHelper.checkNotNull("field:columnComparators", columnComparators);
        return columnComparators;
    }

    protected boolean hasColumnComparators() {
        return this.columnComparators != null;
    }

    protected void setColumnComparators(final List columnComparators) {
        ObjectHelper.checkNotNull("parameter:columnComparators", columnComparators);
        this.columnComparators = columnComparators;
    }

    /**
     * TODO remove unsroted style / set style
     * 
     * @param columnComparator
     * @param column
     * @param ascending
     */
    public void setColumnComparator(final Comparator columnComparator, final int column, final boolean ascending) {
        ObjectHelper.checkNotNull("parameter:columnComparator", columnComparator);
        this.checkColumn("parameter:column", column);

        final ColumnSorting sorting = new ColumnSorting();
        sorting.setAscendingSort(ascending);
        sorting.setComparator(columnComparator);

        this.getColumnComparators().add(column, sorting);

        final HorizontalPanel panel = (HorizontalPanel) this.getWidget(0, column);
        final Label label = (Label) panel.getWidget(0);
        Image image = null;

        if (panel.getWidgetCount() == 1) {
            label.addStyleName(WidgetConstants.SORTABLE_TABLE_SORTABLE_COLUMN_HEADER_STYLE);
            image = this.createSortDirectionImage();

            final SortableTable that = this;
            image.addClickListener(new ClickListener() {
                public void onClick(final Widget sender) {
                    that.onColumnSortingClick(sender);
                }
            });
            panel.add(image);
        } else {
            label.removeStyleName(WidgetConstants.SORTABLE_TABLE_SORTABLE_COLUMN_HEADER_STYLE);
            image = (Image) panel.getWidget(1);
        }
        final String url = ascending ? this.getAscendingSortImageSource() : this.getDescendingSortImageSource();
        image.setUrl(url);
    }

    protected int getColumnIndex(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        int columnIndex = -1;

        final int columnCount = this.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            final HorizontalPanel panel = (HorizontalPanel) this.getWidget(0, i);
            final int index = panel.getWidgetIndex(widget);
            if (-1 != index) {
                columnIndex = i;
                break;
            }
        }

        return columnIndex;
    }

    protected void onColumnSortingClick(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final Image image = (Image) widget;
        final int column = this.getColumnIndex(image);

        final ColumnSorting sorting = (ColumnSorting) this.getColumnComparators().get(column);
        final boolean newSortingOrder = !sorting.isAscendingSort();
        sorting.setAscendingSort(newSortingOrder);

        final String url = newSortingOrder ? this.getAscendingSortImageSource() : this.getDescendingSortImageSource();
        image.setUrl(url);

        this.setSortedColumn(column);
        this.redraw();
    }

    /**
     * TODO set style
     * 
     * @param column
     */
    public void makeColumnUnsortable(final int column) {
        this.checkColumn("parameter:column", column);

        this.getColumnComparators().add(column, null);

        final HorizontalPanel panel = (HorizontalPanel) this.getWidget(0, column);
        if (panel.getWidgetCount() > 1) {
            // the second image is the sort direction icon.
            panel.remove(panel.getWidget(1));
        }
    }

    protected boolean isColumnSortable(final int column) {
        this.checkColumn("parameter:column", column);
        return this.getColumnComparators().get(column) != null;
    }

    /**
     * Returns a comparator which may be used to sort a particular column. It also factors into whether the column is sorted in ascending or
     * descending mode.
     * 
     * @param column
     * @return
     */
    protected Comparator getColumnComparator(final int column) {
        this.checkColumn("parameter:column", column);

        final ColumnSorting sorting = (ColumnSorting) this.getColumnComparators().get(column);
        ObjectHelper.checkNotNull("sorting", sorting);

        Comparator comparator = sorting.getComparator();

        if (false == sorting.isAscendingSort()) {
            final Comparator originalComparator = comparator;
            comparator = new Comparator() {
                public int compare(final Object first, final Object second) {
                    return originalComparator.compare(second, first);
                }
            };
        }

        return comparator;
    }

    protected boolean hasColumnComparator(final int column) {
        return null != this.getColumnComparators().get(column);
    }

    /**
     * The currently selected sorted column
     */
    private int sortedColumn = -1;

    public int getSortedColumn() {
        PrimitiveHelper.checkGreaterThanOrEqual("field:sortedColumn", this.sortedColumn, 0);
        return this.sortedColumn;
    }

    public boolean hasSortedColumn() {
        return sortedColumn != -1;
    }

    public void setSortedColumn(final int sortedColumn) {
        if (!this.isColumnSortable(sortedColumn)) {
            SystemHelper.handleAssertFailure("parameter:sortedColumn",
                    "The parameter:sortedColumn is not a sortable column. sortedColumn: " + sortedColumn);
        }

        if (this.isAutoRedraw()) {
            // remove style from the previous column
            if (this.hasSortedColumn()) {
                this.removeSortedColumnStyle(this.getSortedColumn());
            }
            // add style to highlight the new sorted column.
            this.addSortedColumnStyle(sortedColumn);
        }
        this.sortedColumn = sortedColumn;
    }

    protected void addSortedColumnStyle(final int column) {
        this.checkColumn("parameter:column", column);

        final FlexTable.FlexCellFormatter formatter = this.getFlexCellFormatter();
        final int rowCount = this.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            formatter.addStyleName(i, column, WidgetConstants.SORTABLE_TABLE_SORTED_COLUMN_STYLE);
        }
    }

    protected void removeSortedColumnStyle(final int column) {
        this.checkColumn("parameter:column", column);

        final FlexTable.FlexCellFormatter formatter = this.getFlexCellFormatter();
        final int rowCount = this.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            formatter.removeStyleName(i, column, WidgetConstants.SORTABLE_TABLE_SORTED_COLUMN_STYLE);
        }
    }

    /**
     * Rows of value objects one for each row of the table.
     */
    private List rows;

    protected List getRows() {
        ObjectHelper.checkNotNull("field:rows", rows);
        return rows;
    }

    protected boolean hasRows() {
        return this.rows != null;
    }

    protected void setRows(final List rows) {
        ObjectHelper.checkNotNull("parameter:rows", rows);
        this.rows = rows;
        this.clearSortedRows();
    }

    public void addRow(final Object row) {
        ObjectHelper.checkNotNull("parameter:row", row);
        this.getRows().add(row);
        this.clearSortedRows();
        this.redrawIfAutoEnabled();
    }

    public Object getRow(final int index) {
        return this.getRows().get(index);
    }

    public int getRowIndex(final Object row) {
        ObjectHelper.checkNotNull("parameter:row", row);
        return this.getRows().indexOf(row);
    }

    public void setRow(final Object row, final int index) {
        ObjectHelper.checkNotNull("parameter:row", row);
        this.getRows().add(index, row);
        this.clearSortedRows();
        this.redrawIfAutoEnabled();
    }

    public void removeRow(final int row) {
        this.getRows().remove(row);
        this.clearSortedRows();
        this.redrawIfAutoEnabled();
    }

    public void clear() {
        this.getRows().clear();
        this.clearSortedRows();
        this.redrawIfAutoEnabled();
    }

    /**
     * Removes all row value objects from this table. This method does not turn auto redrawing off to improve batch modifications operations
     * nor does it address any need to request a repaint.
     */
    public void removeAllRows() {
        int rowCount = this.getRows().size();
        while (rowCount > 0) {
            rowCount--;
            this.removeRow(rowCount);
        }
    }

    /**
     * This list is a cached copy of the sorted rows property. It is cleared each time the rows property is modified.
     */
    private List sortedRows;

    protected List getSortedRows() {
        List rows = this.sortRows();
        this.setSortedRows(rows);
        return rows;
    }

    protected boolean hasSortedRows() {
        return null != this.sortedRows;
    }

    protected void setSortedRows(final List sortedRows) {
        ObjectHelper.checkNotNull("parameter:sortedRows", sortedRows);
        this.sortedRows = sortedRows;
    }

    protected void clearSortedRows() {
        this.sortedRows = null;
    }

    /**
     * Retrieves the value object associated with a given table row index.
     * 
     * @param row
     * @return
     */
    public Object getTableRow(final int row) {
        this.checkColumn("parameter:tableRow", row);
        return this.getSortedRows().get(row);
    }

    /**
     * This method must be implemented by sub-classes. It provides a method of addressing properties for an object using an index. These
     * details are implemented by the sub-class.
     * 
     * @param row
     * @param column
     * @return
     */
    protected abstract Object getValue(final Object row, final int column);

    /**
     * This method returns the appropriate widget for the given value using its column to distinguish the type of widget etc.
     * 
     * @param row
     * @param column
     * @return
     */
    protected abstract Widget getWidget(final Object row, final int column);

    /**
     * Sub-classes must override this method and return the number of columns the table will display.
     * 
     * @return
     */
    protected abstract int getColumnCount();

    /**
     * Simply asserts that the column value is valid. If not an exception is thrown.
     * 
     * @param name
     * @param column
     */
    protected void checkColumn(final String name, final int column) {
        PrimitiveHelper.checkBetween(name, column, 0, this.getColumnCount());
    }

    /**
     * This flag controls whether this table is repainted each time a new row is added or removed etc. if set to false the user must call
     * {@link #redraw} themselves.
     */
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

    public void redraw() {
        final List sorted = this.getSortedRows();
        removeUnnecessaryTableRows();
        this.repaintRows(sorted);
    }

    /**
     * THis sorts all the current rows returning a list in sorted order.
     * 
     * @return A new list containing the property rows sorted. The original list is not modified.
     */
    protected List sortRows() {
        final List sorted = new ArrayList();
        sorted.addAll(this.getRows());

        // sort the list...
        final SortableTable that = this;
        final int sortedColumn = this.getSortedColumn();
        final Comparator columnComparator = this.getColumnComparator(sortedColumn);
        Collections.sort(sorted, new Comparator() {

            public int compare(final Object first, final Object second) {
                final Object firstColumn = that.getValue(first, sortedColumn);
                final Object secondColumn = that.getValue(second, sortedColumn);
                return columnComparator.compare(firstColumn, secondColumn);
            }
        });

        return sorted;
    }

    /**
     * This method does the actual translation or painting of the sorted rows.
     * 
     * @param rows
     */
    protected void repaintRows(final List rows) {
        ObjectHelper.checkNotNull("parameter:rows", rows);

        final int columnCount = this.getColumnCount();

        final Iterator iterator = rows.iterator();
        int rowNumber = 1;
        while (iterator.hasNext()) {
            final Object row = iterator.next();

            for (int column = 0; column < columnCount; column++) {
                final Widget cell = this.getWidget(row, column);
                this.setWidget(rowNumber, column, cell);
            }
            rowNumber++;
        }

        this.addSortedColumnStyle(this.getSortedColumn());
    }

    /**
     * This method removes any excess rows from the table widget.
     */
    protected void removeUnnecessaryTableRows() {
        final int rowObjectCount = this.getRows().size() - 1;
        final int tableRowCount = this.getRowCount();

        int i = tableRowCount;
        i--;

        while (i > rowObjectCount && i > 0) {
            super.removeRow(i);
            i--;
        }
    }

    /**
     * Creates the widget that will house the header cell
     * 
     * @param text
     * @param index
     * @return
     */
    protected Widget createHeader(final String text, final int index) {
        final HorizontalPanel panel = new HorizontalPanel();
        panel.add(this.createLabel(text));
        return panel;
    }

    protected Label createLabel(final String text) {
        StringHelper.checkNotEmpty("parameter:text", text);

        final Label label = new Label();
        label.addStyleName(WidgetConstants.SORTABLE_TABLE_COLUMN_HEADER_STYLE);
        label.setText(text);
        label.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                onHeaderClick(sender);
            }
        });
        return label;
    }

    /**
     * Handles whenever a header is clicked (this should only be possible with sortable headers).
     * 
     * @param widget
     */
    protected void onHeaderClick(final Widget widget) {
        final int column = this.getColumnIndex(widget);
        if (this.isColumnSortable(column)) {
            this.setSortedColumn(column);
            this.redraw();
        }
    }

    protected Image createSortDirectionImage() {
        final Image image = new Image();
        image.addStyleName(WidgetConstants.SORTABLE_TABLE_SORT_DIRECTIONS_ARROWS_STYLE);
        image.setUrl(this.getAscendingSortImageSource());
        return image;
    }

    /**
     * The url of an iimage which will be made clickable and represent an ascending sort for a column.
     */
    private String ascendingSortImageSource;

    protected String getAscendingSortImageSource() {
        StringHelper.checkNotEmpty("field:ascendingSortImageSource", ascendingSortImageSource);
        return ascendingSortImageSource;
    }

    public void setAscendingSortImageSource(final String ascendingSortImageSource) {
        StringHelper.checkNotEmpty("parameter:ascendingSortImageSource", ascendingSortImageSource);
        this.ascendingSortImageSource = ascendingSortImageSource;
    }

    /**
     * The url of an image which will be made clickable and represent an descending sort for a column.
     */
    private String descendingSortImageSource;

    protected String getDescendingSortImageSource() {
        StringHelper.checkNotEmpty("field:descendingSortImageSource", descendingSortImageSource);
        return descendingSortImageSource;
    }

    public void setDescendingSortImageSource(final String descendingSortImageSource) {
        StringHelper.checkNotEmpty("parameter:descendingSortImageSource", descendingSortImageSource);
        this.descendingSortImageSource = descendingSortImageSource;
    }

    public String toString() {
        return super.toString() + ", columnComparators: " + columnComparators + ", rows: " + rows + ", sortedColumn: "
                + sortedColumn + ", ascendingSortImageSource[" + ascendingSortImageSource
                + "], descendingSortImageSource[" + descendingSortImageSource + "]";
    }

    /**
     * This object contains both the comparator and sorting option for a particular colu@author Miroslav Pokorny (mP)
     * 
     * @author Miroslav Pokorny (mP)
     */
    private class ColumnSorting {
        /**
         * The comparator that will be used to sort this column
         */
        private Comparator comparator;

        public Comparator getComparator() {
            ObjectHelper.checkNotNull("field:comparator", this.comparator);
            return this.comparator;
        }

        public void setComparator(final Comparator comparator) {
            ObjectHelper.checkNotNull("parameter:comparator", comparator);
            this.comparator = comparator;
        }

        /**
         * This flag when true indicates that this column is in ascending sort mode.
         */
        private boolean ascendingSort;

        public boolean isAscendingSort() {
            return this.ascendingSort;
        }

        public void setAscendingSort(final boolean ascendingSort) {
            this.ascendingSort = ascendingSort;
        }

        public String toString() {
            return super.toString() + ", comparator:" + comparator + ", ascendingSort: " + ascendingSort;
        }
    }
}
