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

import rocket.client.util.PrimitiveHelper;

import com.google.gwt.user.client.ui.FlexTable;

/**
 * This FlexTable adds automatic support for a zebra effect automatically applying an odd / even style to a row depending on whether the row
 * is an odd or even one.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ZebraFlexTable extends FlexTable {

    public ZebraFlexTable() {
        this.addStyleName(WidgetConstants.ZEBRA_FLEX_TABLE_STYLE);
    }

    public void addHeadingStyleToFirstRow() {
        final RowFormatter formatter = this.getRowFormatter();
        formatter.addStyleName(0, WidgetConstants.ZEBRA_FLEX_TABLE_HEADING_STYLE);
    }

    public void insertCell(final int row, final int cell) {
        final boolean newRow = row < this.getRowCount();

        super.insertCell(row, cell);
        // restyle all the new row and all that follow if there was a shift
        if (newRow) {
            this.updateRowBackgroundColour(row, this.getRowCount());
        }
    }

    public int insertRow(int beforeRow) {
        final int value = super.insertRow(beforeRow);

        // restyle all rows that were shifted up including the new row.
        this.updateRowBackgroundColour(beforeRow, this.getRowCount());
        return value;
    }

    public void removeRow(int row) {
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

        final boolean oddRow = (row & 1) == 0;
        final RowFormatter formatter = this.getRowFormatter();

        final String addStyle = oddRow ? WidgetConstants.ZEBRA_FLEX_TABLE_ODD_ROW_STYLE
                : WidgetConstants.ZEBRA_FLEX_TABLE_EVEN_ROW_STYLE;
        formatter.addStyleName(row, addStyle);

        final String removeStyle = oddRow ? WidgetConstants.ZEBRA_FLEX_TABLE_EVEN_ROW_STYLE
                : WidgetConstants.ZEBRA_FLEX_TABLE_ODD_ROW_STYLE;
        formatter.removeStyleName(row, removeStyle);
    }
}
