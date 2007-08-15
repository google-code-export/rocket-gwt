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
 * Two additional methods must be implemented each of which fetches the {@link #getAscendingSortImageSource() ascending }/ {@link #getDescendingSortImageSource() descending }sort images.  
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
		this.initWidget(flexTable);

		this.setStyleName(WidgetConstants.SORTABLE_TABLE_STYLE);
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
			SystemHelper.fail("parameter:sortedColumn", "The parameter:sortedColumn is not a sortable column. sortedColumn: "
					+ sortedColumn);
		}

		if (this.isAutoRedraw()) {
			this.getRowsList().getSorted().setUnsorted(true);

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
	private RowList rows;

	public List getRows() {
		ObjectHelper.checkNotNull("field:rows", rows);
		return rows;
	}

	public void setRows(final RowList rows) {
		ObjectHelper.checkNotNull("parameter:rows", rows);
		this.rows = rows;
	}

	protected RowList getRowsList() {
		ObjectHelper.checkNotNull("field:rows", rows);
		return this.rows;
	}

	/**
	 * This factory returns a list that automatically takes care of updating the table as rows are added, removed etc.
	 */
	protected RowList createRows() {
		return new RowList();
	};

	/**
	 * Returns a read only list view of the sorted rows.
	 * 
	 * This list takes a lazy approach to sorting the list of rows.
	 * 
	 * @return
	 */
	public List getTableRows() {
		
		final RowList rowList = this.getRowsList();
		final SortedRowList sortedRowList = rowList.getSorted();
		
		return new AbstractList(){
			public int size(){
				return rowList.size();
			}
			public boolean add( final Object element ){
				rowList.add( element );
				return true;
			}
			public void add( final int index, final Object element ){
				throw new RuntimeException("Adding in a specific slot is not supported, add with add( Object )");
			}
			
			public Object get( final int index ){
				return sortedRowList.get(index);
			}
			
			public boolean remove( final Object row ){
				return rowList.remove(row);
			}
			public Object remove( final int index ){
				final Object removing = this.get(index);
				rowList.remove( removing );
				return removing;
			}
			public Object set( final int index, final Object element ){
				throw new RuntimeException("Replacing a specific slot is not supported.");
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
		this.removeUnnecessaryTableRows();

		final SortedRowList sorted = this.getRowsList().getSorted();
		sorted.sort();
		this.repaintRows(sorted);
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
	 * This method does the actual translation or painting of the sorted rows.
	 * 
	 * @param rows A sorted list ready to be painted.
	 */
	protected void repaintRows(final SortedRowList rows) {
		ObjectHelper.checkNotNull("parameter:rows", rows);

		final FlexTable table = this.getFlexTable();
		final int columnCount = this.getColumnCount();

		int rowIndex = 1;
		final int size = rows.size();
		for (int i = 0; i < size; i++) {
			final SortedRowListElement row = (SortedRowListElement) rows.getSortedRowListElement(i);

			for (int column = 0; column < columnCount; column++) {
				final Widget cell = row.getWidget(column);
				table.setWidget(rowIndex, column, cell);
			}
			rowIndex++;
		}

		this.addSortedColumnStyle(this.getSortedColumn());
	}

	/**
	 * Creates the widget that will house the header cell
	 * 
	 * @param text The header text
	 * @param index The column
	 * @return The new widget
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

	abstract protected String getAscendingSortImageSource();
	
	abstract protected String getDescendingSortImageSource();
	
	public String toString() {
		return super.toString() + ", columnComparators: " + columnComparators + ", rows: " + rows + ", sortedColumn: " + sortedColumn;
	}

	/**
	 * This object contains both the comparator and sorting option for a particular colu@author Miroslav Pokorny (mP)
	 * 
	 * @author Miroslav Pokorny (mP)
	 */
	static private class ColumnSorting {
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
	class RowList extends ArrayList {

		/**
		 * This list maintains a a list of sorted rows along with their widgets.
		 */
		private SortedRowList sorted = new SortedRowList();

		SortedRowList getSorted() {
			ObjectHelper.checkNotNull("field:sorted", sorted);
			return sorted;
		}

		void setSorted(final SortedRowList sorted) {
			ObjectHelper.checkNotNull("parameter:sorted", sorted);
			this.sorted = sorted;
		}

		public boolean add(final Object element) {
			ObjectHelper.checkNotNull("parameter:element", element);

			if (super.contains(element)) {
				SystemHelper.fail("parameter:element", "The given element may only be added once to this list, element: " + element);
			}
			super.add(element);

			this.getSorted().add(element);

			SortableTable.this.redrawIfAutoEnabled();
			return true;
		}

		public void add(final int index, final Object element) {
			ObjectHelper.checkNotNull("parameter:element", element);

			if (super.contains(element)) {
				SystemHelper.fail("parameter:element", "The given element may only be added once to this list, element: " + element);
			}
			super.add(index, element);

			this.getSorted().add(element);

			SortableTable.this.redrawIfAutoEnabled();
		}

		public boolean addAll(final Collection collection) {
			final boolean redraw = SortableTable.this.isAutoRedraw();
			SortableTable.this.setAutoRedraw(false);

			boolean modified = false;
			final Iterator iterator = collection.iterator();
			while (iterator.hasNext()) {
				this.add(iterator.next());
				modified = true;
			}

			SortableTable.this.setAutoRedraw(redraw);
			if (modified) {
				SortableTable.this.redrawIfAutoEnabled();
			}

			return modified;
		}

		public boolean addAll(final int index, final Collection collection) {
			final boolean redraw = SortableTable.this.isAutoRedraw();
			SortableTable.this.setAutoRedraw(false);

			int index0 = index;
			final Iterator iterator = collection.iterator();
			while (iterator.hasNext()) {
				this.add(index, iterator.next());
				index0++;
			}

			SortableTable.this.setAutoRedraw(redraw);

			final boolean modified = index != index0;
			if (modified) {
				SortableTable.this.redrawIfAutoEnabled();
			}

			return modified;
		}

		public void clear() {
			// backup autoRedraw flag
			final boolean autoRedraw = SortableTable.this.isAutoRedraw();
			SortableTable.this.setAutoRedraw(false);

			super.clear();
			this.getSorted().clear();

			// restore autoRedraw
			SortableTable.this.setAutoRedraw(autoRedraw);
			SortableTable.this.redrawIfAutoEnabled();
		}

		public Object remove(final int index) {
			final Object removed = super.remove(index);

			// wasnt found skip updating sorted list...
			if (null != removed) {
				final SortedRowList sorted = this.getSorted();
				sorted.remove(removed);

				// redraw if necessary
				SortableTable.this.redrawIfAutoEnabled();
			}
			return removed;
		}
		
		public boolean remove( final Object row ){
			final boolean removed = super.remove( row );
			
			if( removed ){
				final SortedRowList sorted = this.getSorted();
				sorted.remove( row );

				// redraw if necessary
				SortableTable.this.redrawIfAutoEnabled();				
			}
			
			return removed;
		}

		public Object set(final int index, final Object element) {
			final Object replaced = super.set(index, element);
			this.getSorted().set(index, element);
			return replaced;
		}
	};

	/**
	 * This list is a sorted view of the rows that belong to RowList.
	 * Each row is actually wrapped inside a SortedRowListElement, therefore all the public methods wrap/unwrap the row, whilst a few additional
	 * methods are available to get at the wrapper itself.
	 * This class also includes a method to sort if necessary.
	 */
	class SortedRowList extends ArrayList {
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

		void sort() {
			if (this.isUnsorted()) {
				final int sortedColumn = SortableTable.this.getSortedColumn();
				final Comparator columnComparator = SortableTable.this.getColumnComparator(sortedColumn);
				Collections.sort(
						/**
						 * THis list returns SortedRowListElement rather than the default behaviour which unwraps the row object.
						 */						
						new AbstractList() {
					public Object get(final int index) {
						return SortedRowList.this.getSortedRowListElement(index);
					}

					public Object set(final int index, final Object element) {
						return SortedRowList.this.setSortedRowListElement(index, (SortedRowListElement) element);
					}

					public int size() {
						return SortedRowList.this.size();
					}
				}, new Comparator() {
					/** 
					 * Retrieve the row property from each SortedRowListElement and then pass that to the comparator.
					 */
					public int compare(final Object first, final Object second) {
						final SortedRowListElement firstElement = (SortedRowListElement) first;
						final SortedRowListElement secondElement = (SortedRowListElement) second;
						final Object firstValue = SortableTable.this.getValue(firstElement.getRow(), sortedColumn);
						final Object secondValue = SortableTable.this.getValue(secondElement.getRow(), sortedColumn);
						return columnComparator.compare(firstValue, secondValue);
					}
				});
			}
		}

		public boolean add(final Object row) {
			final SortedRowListElement sortedRowListElement = new SortedRowListElement();
			sortedRowListElement.setRow(row);

			super.add(sortedRowListElement);
			this.setUnsorted(true);

			return true;
		}

		public void add(final int index, final Object row) {
			throw new RuntimeException("Adding an element in a specific slot is not supported."); 
		}

		public void add(final int index, final Collection collection ) {
			throw new RuntimeException("Adding a collection in a specific slot is not supported.");
		}
		
		public Object get(final int index) {
			final SortedRowListElement sortedRowListElement = (SortedRowListElement) super.get(index);
			return null == sortedRowListElement ? null : sortedRowListElement.getRow();
		}

		public SortedRowListElement getSortedRowListElement(final int index) {
			return (SortedRowListElement) super.get(index);
		}

		/**
		 * Assumes that the list is sorted so be careful when removing...
		 */
		public Object remove(final int index) {
			final SortedRowListElement sortedRowListElement = (SortedRowListElement) super.remove(index);
			sortedRowListElement.clear();
			return sortedRowListElement.getRow();
		}

		public boolean remove(final Object row) {
			
			int index = -1;
				final int size = this.size();
				for (int i = 0; i < size; i++) {
					final SortedRowListElement element = this.getSortedRowListElement(i);
					if (row.equals(element.getRow())) {
						index = i;
						super.remove( index );
						element.clear();
						break;
					}
				}
						
			return -1 != index;
		}

		public Object set(final int index, final Object row) {
			throw new RuntimeException("Replacing an element in a specific slot is not supported.");
		}

		public Object setSortedRowListElement(final int index, final SortedRowListElement element) {
			return super.set(index, element);
		}	
	}

	/**
	 * This object maintains a cache between a row, individual values for each column, and widgets for the same column.
	 */
	class SortedRowListElement {

		public SortedRowListElement() {
			super();

			this.setWidgets(new Widget[SortableTable.this.getColumnCount()]);
		}

		/**
		 * Lazily creates the widget when requested and caches for future requests.
		 * @param column The column
		 * @return The widget
		 */
		Widget getWidget(final int column) {
			final Widget[] widgets = this.getWidgets();
			Widget widget = widgets[column];
			if (null == widget) {
				widget = SortableTable.this.getWidget(this.getRow(), column);
				widgets[column] = widget;
			}
			return widget;
		}

		/**
		 * This method should be called when this row is removed from the table.
		 */
		void clear() {
			final Widget[] widgets = this.getWidgets();
			for (int i = 0; i < widgets.length; i++) {
				widgets[i] = null;
			}
		}

		/**
		 * The source value object 
		 */
		private Object row;

		Object getRow() {
			ObjectHelper.checkNotNull("field:row", row);
			return this.row;
		}

		void setRow(final Object row) {
			ObjectHelper.checkNotNull("parameter:row", row);
			this.row = row;
		}

		/**
		 * This array holds a cache of widgets previously created for this row.
		 * The SortableTable will reuse these until the row is removed.
		 * If the array contains null elements it means the table has not yet been redrawn.
		 */
		private Widget[] widgets;

		Widget[] getWidgets() {
			ObjectHelper.checkNotNull("field:widgets", widgets);
			return this.widgets;
		}

		void setWidgets(final Widget[] widgets) {
			ObjectHelper.checkNotNull("parameter:widgets", widgets);
			this.widgets = widgets;
		}
	}
}
