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

import java.util.Iterator;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides a card container for a title bar which may have various widgets added to it and a main panel which may contain the content
 * widget.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class CollapsablePanel extends Composite implements HasWidgets {

    public CollapsablePanel() {
    	final FlexTable flexTable = this.createFlexTable();
    	this.setFlexTable(flexTable);
        this.initWidget( flexTable );
        this.setStyleName(WidgetConstants.COLLAPSABLE_PANEL_STYLE);
    }

    public void add(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final FlexTable table = this.getTitleFlexTable();
        final int column = table.getCellCount(WidgetConstants.COLLAPSABLE_PANEL_TITLE_ROW);
        table.setWidget(WidgetConstants.COLLAPSABLE_PANEL_TITLE_ROW, column, widget);
        table.getCellFormatter().setStyleName(WidgetConstants.COLLAPSABLE_PANEL_TITLE_ROW, column,
                WidgetConstants.COLLAPSABLE_PANEL_TITLE_WIDGET_STYLE);
    }

    public boolean remove(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);
        final FlexTable table = this.getTitleFlexTable();
        final boolean success = table.remove(widget);
        return success;
    }

    public int getCount() {
        return this.getTitleFlexTable().getCellCount(WidgetConstants.COLLAPSABLE_PANEL_TITLE_ROW) - 1;
    }

    public Widget getWidget(final int index) {
        return this.getTitleFlexTable().getWidget(0, index + 1);
    }

    public int getIndex(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);
        int index = -1;
        final Iterator iterator = this.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            final Widget otherWidget = (Widget) iterator.next();
            if (widget == otherWidget) {
                index = i;
                break;
            }
            i++;
        }
        return index;
    }

    public void clear() {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            this.remove((Widget) iterator.next());
        }
    }

    public Iterator iterator() {
        return this.getTitleFlexTable().iterator();
    }

    // IMPL
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * This counter should be incremented each time a modification is made to this container. It exists to help any iterators fail fast.
     */
    private int modificationCount;

    protected int getModificationCount() {
        return this.modificationCount;
    }

    public void setModificationCount(final int modificationCount) {
        this.modificationCount = modificationCount;
    }

    /**
     * This flexTable contains the title and the contents.
     */
    private FlexTable flexTable;

    protected FlexTable getFlexTable() {
        ObjectHelper.checkNotNull("field:flexTable", flexTable);
        return flexTable;
    }

    protected boolean hasFlexTable() {
        return this.flexTable != null;
    }

    protected void setFlexTable(final FlexTable flexTable) {
        ObjectHelper.checkNotNull("parameter:flexTable", flexTable);
        this.flexTable = flexTable;
    }

    /**
     * The principal factory method which is responsible for creating the entire widget ready for displaying purposes.
     * 
     * @return
     */
    protected FlexTable createFlexTable() {
        final FlexTable table = new FlexTable();
        table.setWidget(WidgetConstants.COLLAPSABLE_PANEL_TITLE_ROW, WidgetConstants.COLLAPSABLE_PANEL_TITLE_COLUMN,
                this.createTitleFlexTable());
        table.getFlexCellFormatter().setWidth(WidgetConstants.COLLAPSABLE_PANEL_TITLE_ROW,
                WidgetConstants.COLLAPSABLE_PANEL_TITLE_COLUMN, "100%");
        return table;
    }

    protected FlexTable createTitleFlexTable() {
        final FlexTable table = new FlexTable();
        table.setStyleName(WidgetConstants.COLLAPSABLE_PANEL_TITLE_FLEXTABLE_STYLE);

        table.setText(0, 0, "");
        table.getFlexCellFormatter().setWidth(0, 0, "100%");
        return table;
    }

    protected FlexTable getTitleFlexTable() {
        return (FlexTable) this.getFlexTable().getWidget(0, 0);
    }

    public String getTitle() {
        return this.getTitleFlexTable().getText(WidgetConstants.COLLAPSABLE_PANEL_TITLE_ROW,
                WidgetConstants.COLLAPSABLE_PANEL_TITLE_COLUMN);
    }

    public void setTitle(final String title) {
        StringHelper.checkNotEmpty("parameter:title", title);

        this.getTitleFlexTable().setText(WidgetConstants.COLLAPSABLE_PANEL_TITLE_ROW,
                WidgetConstants.COLLAPSABLE_PANEL_TITLE_COLUMN, title);
    }

    public Widget getContent() {
        return this.getFlexTable().getWidget(WidgetConstants.COLLAPSABLE_PANEL_CONTENT_ROW,
                WidgetConstants.COLLAPSABLE_PANEL_CONTENT_COLUMN);
    }

    public boolean hasContent() {
        final FlexTable table = this.getFlexTable();
        return table.getRowCount() == (WidgetConstants.COLLAPSABLE_PANEL_CONTENT_ROW + 1)
                && table.getCellCount(WidgetConstants.COLLAPSABLE_PANEL_CONTENT_ROW) == (WidgetConstants.COLLAPSABLE_PANEL_CONTENT_COLUMN + 1);
    }

    public void setContent(final Widget content) {
        ObjectHelper.checkNotNull("parameter:content", content);

        final FlexTable table = this.getFlexTable();
        table.setWidget(WidgetConstants.COLLAPSABLE_PANEL_CONTENT_ROW,
                WidgetConstants.COLLAPSABLE_PANEL_CONTENT_COLUMN, content);
        final FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
        formatter.addStyleName(WidgetConstants.COLLAPSABLE_PANEL_CONTENT_ROW,
                WidgetConstants.COLLAPSABLE_PANEL_CONTENT_COLUMN, WidgetConstants.COLLAPSABLE_PANEL_CONTENT_STYLE);
    }

    public void clearContent() {
        this.getFlexTable().removeRow(WidgetConstants.COLLAPSABLE_PANEL_CONTENT_ROW);
    }

    public void showContent() {
        this.getContent().setVisible(true);
    }

    public void hideContent() {
        this.getContent().setVisible(false);
    }

    /**
     * Creates a widget(actually an Image) so that when clicked it will close this CollapsablePanel. The widget however must be added to the
     * card via {@link #add}
     * 
     * @return
     */
    public Widget createClose() {
        final Image image = new Image();
        image.setUrl(WidgetConstants.COLLAPSABLE_PANEL_CLOSE_IMAGE_URL);

        final CollapsablePanel that = this;

        image.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                that.removeFromParent();
            }
        });

        return image;
    }

    /**
     * Creates a widget(actually an Image) so that when clicked it will minimize this CollapsablePanel. The widget however must be added to
     * the card via {@link #add}
     * 
     * @return
     */
    public Widget createMinimize() {
        final Image image = new Image();
        image.setUrl(WidgetConstants.COLLAPSABLE_PANEL_MINIMIZE_IMAGE_URL);

        final CollapsablePanel that = this;

        image.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                that.hideContent();
            }
        });

        return image;
    }

    /**
     * Creates a widget(actually an Image) so that when clicked it will maximize this CollapsablePanel. The widget however must be added to
     * the card via {@link #add}
     * 
     * @return
     */
    public Widget createMaximize() {
        final Image image = new Image();
        image.setUrl(WidgetConstants.COLLAPSABLE_PANEL_MAXIMIZE_IMAGE_URL);

        final CollapsablePanel that = this;

        image.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                that.showContent();
            }
        });

        return image;
    }

    public String toString() {
        return super.toString() + ", flexTable: " + flexTable;
    }
}
