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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A powerful easy to use table that includes support for sorting individual columns using Comparators provided by the user. This class uses
 * the template approach to facilitate mapping between value objects and columns.
 * 
 * The {@link #getValue(Object, int)} method is required to return the value for a particular column for the given row. The
 * {@link #getWidget(Object, int)} method is required to return the Widget that will appear at the given column for the given row.
 * 
 * <h6>Gotchas</h6>
 * <ul>
 * 
 * <li>Column comparators should be set before adding any headers. Once headers have been created it is possible to set the order
 * (ascending/descending) for any sortable column Refer to the bundle test that shows how simple it is to subclass and use this
 * implementation. </li>
 * 
 * <li> if autoRedraw (which may be set via {@link #setAutoRedraw(boolean)}) is true the table will be redrawn each time the rows list is
 * modified. </li>
 * 
 * <li> if autoRedraw (which may be set via {@link #setAutoRedraw(boolean)}) is false the user must force redraws using {@link #redraw() }
 * </li>
 * 
 * </ul>
 * {@link #redrawIfAutoEnabled()} and {@link #redrawIfAutoEnabled()} </li>
 * 
 * <li> User code should not use the {@link #insertRow(int)} and {@link #removeRow(int)} methods as they potentially may corrupt the
 * SortableTable. </li>
 * 
 * </ul>
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class SortableTable extends Composite {

    public SortableTable() {
        super();

        final FlexTable flexTable = this.createFlexTable();
        this.setFlexTable(flexTable);
        this.initWidget( flexTable );

        this.addStyleName(WidgetConstants.SORTABLE_TABLE_STYLE);
        this.setColumnComparators(new ArrayList());
        this.setRows(this.createRows());
        this.setAutoRedraw(true);
    }

    /**
     * A FlexTable or sub-class is the target table that is used to house the sorted table
     */
    private FlexTable flexTable;

    public FlexTable getFlexTable() {
        ObjectHelper.checkNotNull("field:flexTable", flexTable);
        return flexTable;
    }

    public void setFlexTable(final FlexTable flexTable) {
        ObjectHelper.checkNotNull("parameter:flexTable", flexTable);
        this.flexTable = flexTable;
    }

    protected FlexTable createFlexTable() {
        return new ZebraFlexTable();
    }

    protected void setWidget(final int row, final int column, final Widget widget) {
        this.getFlexTable().setWidget(row, column, widget);
    }

    protected void setText(final int row, final int column, final String text) {
        this.getFlexTable().setText(row, column, text);
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
     * TODO remove unsorted style / set style
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

        final HorizontalPanel panel = (HorizontalPanel) this.getFlexTable().getWidget(0, column);
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

        final FlexTable table = this.getFlexTable();
        final int columnCount = this.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            final HorizontalPanel panel = (HorizontalPanel) table.getWidget(0, i);
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
     * Marks a particular column as not being sortable.
     * 
     * @param column
     */
    public void makeColumnUnsortable(final int column) {
        this.checkColumn("parameter:column", column);

        this.getColumnComparators().add(column, null);

        final HorizontalPanel panel = (HorizontalPanel) this.getFlexTable().getWidget(0, column);
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
        if (false == this.isColumnSortable(sortedColumn)) {
            SystemHelper.fail("parameter:sortedColumn",
                    "The parameter:sortedColumn is not a sortable column. sortedColumn: " + sortedColumn);
        }

        if (this.isAutoRedraw()) {
            this.getRowsList().setUnsorted(true);

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

        final FlexTable table = this.getFlexTable();
        final FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
        final int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            formatter.addStyleName(i, column, WidgetConstants.SORTABLE_TABLE_SORTED_COLUMN_STYLE);
        }
    }

    protected void removeSortedColumnStyle(final int column) {
        this.checkColumn("parameter:column", column);

        final FlexTable table = this.getFlexTable();
        final FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
        final int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            formatter.removeStyleName(i, column, WidgetConstants.SORTABLE_TABLE_SORTED_COLUMN_STYLE);
        }
    }

    /**
     * Rows of value objects one for each row of the table.
     */
    private RowsList rows;

    public List getRows() {
        ObjectHelper.checkNotNull("field:rows", rows);
        return rows;
    }

    public void setRows(final RowsList rows) {
        ObjectHelper.checkNotNull("parameter:rows", rows);
        this.rows = rows;
    }

    protected RowsList getRowsList() {
        ObjectHelper.checkNotNull("field:rows", rows);
        return this.rows;
    }

    /**
     * This factory returns a list that automatically takes care of updating the table as rows are added, removed etc.
     */
    protected RowsList createRows() {
        final RowsList rows = new RowsList();
        rows.setSortableTable(this);
        return rows;
    };

    /**
     * Returns a list view that doesnt accept adds of the sorted rows.
     * 
     * @return
     */
    public List getTableRows() {
        final RowsList rows = this.getRowsList();
        final List sorted = rows.getSorted();

        if (rows.isUnsorted()) {
            // if the sizes dont match copy everything over...
            if (rows.size() != sorted.size()) {
                sorted.clear();
                sorted.addAll(rows);
            }

            // sort the (un)sorted rows...
            final int sortedColumn = this.getSortedColumn();
            final Comparator columnComparator = this.getColumnComparator(sortedColumn);
            Collections.sort(sorted, new Comparator() {

                public int compare(final Object first, final Object second) {
                    final Object firstColumn = SortableTable.this.getValue(first, sortedColumn);
                    final Object secondColumn = SortableTable.this.getValue(second, sortedColumn);
                    return columnComparator.compare(firstColumn, secondColumn);
                }
            });
        }

        return new AbstractList() {
            public Object get(final int index) {
                return sorted.get(index);
            }

            public Object remove(final int index) {
                final Object aboutToBeRemoved = sorted.get(index);
                final boolean removed = rows.remove(aboutToBeRemoved);
                return removed ? aboutToBeRemoved : null;
            }

            public int size() {
                return sorted.size();
            }
        };
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
        removeUnnecessaryTableRows();
        final List sorted = this.getTableRows();
        this.repaintRows(sorted);
    }

    /**
     * This method does the actual translation or painting of the sorted rows.
     * 
     * @param rows
     */
    protected void repaintRows(final List rows) {
        ObjectHelper.checkNotNull("parameter:rows", rows);

        final FlexTable table = this.getFlexTable();
        final int columnCount = this.getColumnCount();

        final Iterator iterator = rows.iterator();
        int rowNumber = 1;
        while (iterator.hasNext()) {
            final Object row = iterator.next();

            for (int column = 0; column < columnCount; column++) {
                final Widget cell = this.getWidget(row, column);
                table.setWidget(rowNumber, column, cell);
            }
            rowNumber++;
        }

        this.addSortedColumnStyle(this.getSortedColumn());
    }

    /**
     * This method removes any excess rows from the table widget.
     */
    protected void removeUnnecessaryTableRows() {
        final FlexTable table = this.getFlexTable();
        final int rowObjectCount = this.getRows().size() - 1;
        final int tableRowCount = table.getRowCount();

        int i = tableRowCount;
        i--;

        while (i > rowObjectCount && i > 0) {
            table.removeRow(i);
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

    /**
     * This list automatically takes care of refreshing of the parent SortableTable
     * 
     * @author Miroslav Pokorny (mP)
     */
    class RowsList extends ArrayList {

        /**
         * The parent SortableTable that this list is linked with.
         */
        private SortableTable sortableTable;

        SortableTable getSortableTable() {
            ObjectHelper.checkNotNull("field:sortableTable", sortableTable);
            return this.sortableTable;
        }

        void setSortableTable(final SortableTable sortableTable) {
            ObjectHelper.checkNotNull("parameter:sortableTable", sortableTable);
            this.sortableTable = sortableTable;
        }

        private List sorted = new ArrayList();

        List getSorted() {
            ObjectHelper.checkNotNull("field:sorted", sorted);
            return sorted;
        }

        void setSorted(final List sorted) {
            ObjectHelper.checkNotNull("parameter:sorted", sorted);
            this.sorted = sorted;
        }

        /**
         * This flag indicates that the sorted list is out of sync with the rows belonging to this list.
         */
        boolean unsorted = true;

        boolean isUnsorted() {
            return this.unsorted;
        }

        void setUnsorted(final boolean unsorted) {
            this.unsorted = unsorted;
        }

        public boolean add(final Object element) {
            ObjectHelper.checkNotNull("parameter:element", element);

            // should probably not allow the element to appear more than once in the list.
            final int index = super.indexOf(element);
            if (-1 != index) {
                SystemHelper.fail("parameter:element",
                        "The given element may only be added once to this list, element: " + element);
            }

            super.add(element);
            this.setUnsorted(true);
            this.getSortableTable().redrawIfAutoEnabled();
            return true;
        }

        public void add(final int index, final Object element) {
            ObjectHelper.checkNotNull("parameter:element", element);

            super.add(index, element);
            this.setUnsorted(true);
            this.getSortableTable().redrawIfAutoEnabled();
        }

        public boolean addAll(final Collection collection) {
            return this.addAll(this.size(), collection);
        }

        public boolean addAll(final int index, final Collection collection) {
            ObjectHelper.checkNotNull("parameter:collection", collection);
            final List sorted = this.getSorted();

            boolean modified = false;
            final Iterator iterator = collection.iterator();
            int i = index;
            while (iterator.hasNext()) {
                final Object element = iterator.next();

                super.add(i, element);
                sorted.add(i, element);
                i++;

                modified = true;
            }
            if (modified) {
                this.setUnsorted(true);
                this.getSortableTable().redrawIfAutoEnabled();
            }

            return modified;
        }

        public void clear() {
            final SortableTable table = this.getSortableTable();

            // backup autoRedraw flag
            final boolean autoRedraw = table.isAutoRedraw();
            table.setAutoRedraw(false);

            super.clear();

            // take care of the sorted list.
            this.getSorted().clear();
            this.setUnsorted(false);

            // restore autoRedraw
            table.setAutoRedraw(autoRedraw);
            table.redrawIfAutoEnabled();
        }

        public Object remove(final int index) {
            final Object removed = super.remove(index);

            // wasnt found skip updating sorted list...
            if (null != removed) {
                this.getSorted().remove(removed);
                // the unsorted flag doesnt change...

                // redraw if necessary
                this.getSortableTable().redrawIfAutoEnabled();
            }
            return removed;
        }

        public Object set(final int index, final Object element) {
            final Object replaced = super.set(index, element);

            // the list has changed so the sorted list must now be unsorted.
            this.setUnsorted(false);
            return replaced;
        }
    };
}
