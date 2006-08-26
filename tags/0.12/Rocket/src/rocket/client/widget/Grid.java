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

import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.util.StringHelper;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * A Grid uses a Flextable to hold widgets from its enclosed WidgetProvider
 *
 * Whenever any of the properties such as rows/columns/cursor are updated the widget redraws itself,
 * @author Miroslav Pokorny (mP)
 */
public class Grid extends Composite{

    public Grid(){
        this.setAutoRedraw( false );
        this.setWidget( this.createFlexTable() );
    }

    // GRID :::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private boolean autoRedraw;

    public boolean isAutoRedraw(){
        return this.autoRedraw;
    }
    public void setAutoRedraw( final boolean autoRedraw ){
        this.autoRedraw = autoRedraw;
    }

    protected void redrawIfAutoEnabled(){
        if( this.isAutoRedraw() ){
            this.redraw();
        }
    }
    /**
     * Refreshes the grid using the WidgetProvider to fill in each cell.
     */
    public void redraw(){
        final FlexTable table = this.getFlexTable();
        table.clear();

        final WidgetProvider provider = this.getWidgetProvider();

        final int rows = this.getRows();
        final int columns = this.getColumns();

        final int first = provider.getFirst();
        final int last = provider.getLast();
        final int cursor = this.getCursor();

        final FlexCellFormatter cellFormatter = table.getFlexCellFormatter();

        int lastValidIndex = Integer.MIN_VALUE;
        for( int r = 0; r < rows; r++ ){
            for( int c = 0; c < columns; c++ ){
                Widget cellWidget = null;
                final int cellIndex = r * columns + c + cursor;

                if( cellIndex < first || cellIndex >= last ){
                    cellWidget = this.createFiller();
                } else {
                    cellWidget = provider.getWidget( cellIndex );
                    lastValidIndex = cellIndex;
                }
                cellFormatter.addStyleName( r, c, WidgetConstants.GRID_CELL_STYLE );
                table.setWidget( r, c, cellWidget );
            }
        }

        this.setLastValidIndex( lastValidIndex );
    }

    /**
     * This flexTable is the table that will display the grid of widgets
     */
    private FlexTable flexTable;

    protected FlexTable getFlexTable(){
        ObjectHelper.checkNotNull("field:flexTable", flexTable);
        return flexTable;
    }

    protected boolean hasFlexTable(){
        return null!=this.flexTable;
    }

    protected void setFlexTable( final FlexTable flexTable ){
        ObjectHelper.checkNotNull("parameter:flexTable", flexTable);
        this.flexTable = flexTable;
    }

    protected FlexTable createFlexTable(){
        final FlexTable table = new FlexTable();
        table.addStyleName( WidgetConstants.GRID_STYLE );
        table.addStyleName( WidgetConstants.GRID_FLEXTABLE_STYLE );
        this.setFlexTable( table );
        return table;
    }

    // GRID ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * The pointer which indexes the first visible item from the widgetprovider
     */
    private int cursor;

    public int getCursor(){
        return this.cursor;
    }

    public void setCursor( final int cursor ){
        this.cursor = cursor;

        this.redrawIfAutoEnabled();
    }

    /**
     * The number of visible rows
     */
    private int rows;

    public int getRows(){
        PrimitiveHelper.checkGreaterThanOrEqual( "field:rows", rows, 0 );
        return this.rows;
    }

    public void setRows( final int rows ){
        PrimitiveHelper.checkGreaterThanOrEqual( "parameter:rows", rows, 0 );
        this.rows = rows;
        this.redrawIfAutoEnabled();
    }

    /**
     * The number of visible columns
     */
    private int columns;

    public int getColumns(){
        PrimitiveHelper.checkGreaterThanOrEqual( "field:columns", columns, 0 );
        return this.columns;
    }

    public void setColumns( final int columns ){
        PrimitiveHelper.checkGreaterThanOrEqual( "parameter:columns", columns, 0 );
        this.columns = columns;
        this.redrawIfAutoEnabled();
    }

    /**
     * This html is used to fill in missing grid cells.
     * A missing cell is where a particular widget is not available for a cell because there are more grid cells than
     * the WidgetProvider can give.
     */
    private String filler;

    public String getFiller(){
        StringHelper.checkNotNull( "field:filler", filler );
        return this.filler;
    }

    public void setFiller( final String filler ){
        StringHelper.checkNotNull( "parameter:filler", filler );
        this.filler = filler;
        this.redrawIfAutoEnabled();
    }

    protected Widget createFiller(){
        final HTML html = new HTML( this.getFiller() );
        html.addStyleName( WidgetConstants.GRID_FILLER_STYLE );
        return html;
    }

    /**
     * The widgetProvider being wrapped.
     */
    private WidgetProvider widgetProvider;

    public WidgetProvider getWidgetProvider(){
        ObjectHelper.checkNotNull("field:widgetProvider", widgetProvider);
        return widgetProvider;
    }

    public void setWidgetProvider( final WidgetProvider widgetProvider ){
        ObjectHelper.checkNotNull("parameter:widgetProvider", widgetProvider);
        this.widgetProvider = widgetProvider;
        redrawIfAutoEnabled();
    }

    /**
     * Because a grid may be larger than the number of widgets that may be supplied by a WIdgetProvider
     * this method provides a means of knowing the true actual last valid index.
     */
    private int lastValidIndex;

    public int getLastValidIndex(){
    	return this.lastValidIndex;
    }

    protected void setLastValidIndex( final int lastValidIndex ){
    	this.lastValidIndex = lastValidIndex;
    }

    protected void clearLastValidIndex(){
    	this.lastValidIndex = Integer.MIN_VALUE;
    }

    public String toString(){
        return super.toString() + ", cursor: " + cursor + ", rows: " + rows + ", columns: " + columns + ", filler[" + filler + "], widgetProvider: " + widgetProvider;
    }
}
