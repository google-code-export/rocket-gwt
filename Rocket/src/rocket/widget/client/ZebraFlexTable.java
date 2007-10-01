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

import rocket.util.client.PrimitiveHelper;

import com.google.gwt.user.client.ui.FlexTable;

/**
 * This FlexTable adds automatic support for a zebra effect automatically
 * applying an odd / even style to a row depending on whether the row is an odd
 * or even one.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ZebraFlexTable extends FlexTable {

	public ZebraFlexTable() {
		this.setStyleName( this.getInitialStyleName());
	}
	
	protected String getInitialStyleName(){
		return WidgetConstants.ZEBRA_FLEX_TABLE_STYLE;
	}

	public void addHeadingStyleToFirstRow() {
		final RowFormatter formatter = this.getRowFormatter();
		formatter.addStyleName(0, this.getHeadingStyle() );
	}
	
	protected String getHeadingStyle(){
		return WidgetConstants.ZEBRA_FLEX_TABLE_HEADING_STYLE;	
	}
	
	 /**
	   * Ensure that the cell exists.
	   * 
	   * @param row the row to prepare.
	   * @param column the column to prepare.
	   * @throws IndexOutOfBoundsException if the row is negative
	   */
	  protected void prepareCell(final int row, final int column) {
	    prepareRow(row);
	    if (column < 0) {
	      throw new IndexOutOfBoundsException(
	          "Cannot create a column with a negative index: " + column);
	    }	   
	    
	    // Ensure that the requested column exists.
	    final int cellCount = getCellCount(row) + 1;
	    for( int i = column; i < cellCount; i++ ){
	    	this.insertCell( row, i );
	    }
	  }
	
	public void insertCell(final int row, final int cell) {
		final boolean newRow = row < this.getRowCount();

		super.insertCell(row, cell);
		// restyle all the new row and all that follow if there was a shift
		if (newRow) {
			this.updateRowBackgroundColour(row, this.getRowCount());
		}
	}

	public int insertRow(final int beforeRow) {
		final int value = super.insertRow(beforeRow);

		// restyle all rows that were shifted up including the new row.
		this.updateRowBackgroundColour(beforeRow, this.getRowCount());
		return value;
	}

	public void removeRow(final int row) {
		super.removeRow(row);

		// restyle all rows that were shifted up.
		this.updateRowBackgroundColour(row, this.getRowCount());
	}

	protected void updateRowBackgroundColour() {
		this.updateRowBackgroundColour(0, this.getRowCount());
	}

	protected void updateRowBackgroundColour(final int startRow, final int endRow) {
		for (int i = startRow; i < endRow; i++) {
			this.updateRowBackgroundColour(i);
		}
	}

	protected void updateRowBackgroundColour(final int row) {
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:row", row, 0);

		final String oddRowStyle = this.getOddRowStyle();
		final String evenRowStyle = this.getEvenRowStyle();
		
		final boolean oddRow = (row & 1) == 0;
		final RowFormatter formatter = this.getRowFormatter();

		final String addStyle = oddRow ? oddRowStyle : evenRowStyle;
		formatter.addStyleName(row, addStyle);

		final String removeStyle = oddRow ? evenRowStyle : oddRowStyle;
		formatter.removeStyleName(row, removeStyle);
	}
	
	protected String getOddRowStyle(){
		return WidgetConstants.ZEBRA_FLEX_TABLE_ODD_ROW_STYLE;
	}
	protected String getEvenRowStyle(){
		return WidgetConstants.ZEBRA_FLEX_TABLE_EVEN_ROW_STYLE;
	}
}
