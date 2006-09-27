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
package rocket.test.widget.sortabletable.client;

import java.util.Date;

import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;
import rocket.client.widget.SortableTable;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * This table is displays all download files, it may be sorted and also includes actions such as downloading or removing a file.
 *
 * @author Miroslav Pokorny (mP)
 *
 */
public class SortableFileTable extends SortableTable {

    public SortableFileTable() {
        super();

        this.setHeadings();
        this.addHeadingStyleToFirstRow();
    }

    protected Object getValue(final Object row, final int column) {
        ObjectHelper.checkNotNull("parameter:row", row);
        this.checkColumn( "parameter:column", column );

        final File downloadFile = (File) row;

        Object value = null;
        switch (column) {
        case 0: {
            value = downloadFile.getFilename();
            break;
        }
        case 1: {
            value = new Integer(downloadFile.getSize());
            break;
        }
        case 3: {
            value = downloadFile.getCreateDate();
            break;
        }
        case 4: {
            value = downloadFile.hasDownloadDate() ? downloadFile.getDownloadDate() : null;
            break;
        }
        default: {
            SystemHelper.handleAssertFailure("parameter:column",
                    "The parameter:column contains an invalid value. row: " + row);
        }
        }
        return value;
    }

    protected Widget getWidget(final Object value, final int column) {
        ObjectHelper.checkNotNull("parameter:value", value);
        this.checkColumn( "parameter:column", column );

        final File file = (File) value;
        Widget widget = null;
        switch (column) {
        case 0: {
            widget = visitFilename(file.getFilename());
            break;
        }
        case 1: {
            widget = visitSize(file.getSize());
            break;
        }
        case 2: {
            widget = visitDescription(file.getDescription());
            break;
        }
        case 3: {
            widget = visitCreateDate(file.getCreateDate());
            break;
        }
        case 4: {
            widget = file.hasDownloadDate() ? visitDownloadDate(file.getDownloadDate()) : this
                    .visitMissingDownloadDate();
            break;
        }
        case 5: {
            widget = this.createDownloadLink(file.getServerId());
            break;
        }
        case 6: {
            widget = this.createRemoveLink(file.getServerId());
            break;
        }
        default: {
            SystemHelper.handleAssertFailure("parameter:column",
                    "The parameter:column contains an invalid value. column: " + column);
        }
        }
        return widget;
    }

    protected void setHeadings() {
        int row = 0;
        int i = 0;
        this.setWidget(row, i, this.createHeader("Filename", i));
        i++;
        this.setWidget(row, i, this.createHeader("size(bytes)", i));
        i++;
        this.setWidget(row, i, this.createHeader("Description", i));
        i++;
        this.setWidget(row, i, this.createHeader("Create Date/Time", i));
        ;
        i++;
        this.setWidget(row, i, this.createHeader("Download Date/Time", i));
        i++;
        this.setWidget(row, i, this.createHeader("Download", i));
        i++;
        this.setWidget(row, i, this.createHeader("Remove", i));
    }

    protected int getColumnCount() {
        return 7;
    }

    public void addDownloadFile(final File downloadFile) {
        ObjectHelper.checkNotNull("parameter:downloadFile", downloadFile);
        this.getRows().add(downloadFile);

        this.redrawIfAutoEnabled();
    }

    public void removeDownloadFile(final File downloadFile) {
        ObjectHelper.checkNotNull("parameter:downloadFile", downloadFile);
        this.getRows().remove(downloadFile);

        this.redrawIfAutoEnabled();
    }

    public File getDownloadFile(final int index) {
        return (File) this.getRows().get(index);
    }

    public int getIndex(final File downloadFile) {
        ObjectHelper.checkNotNull("parameter:downloadFile", downloadFile);
        return this.getRows().indexOf(downloadFile);
    }

    protected Hyperlink createDownloadLink(final String serverId) {
        StringHelper.checkNotEmpty("parameter:serverId", serverId);
        final Hyperlink link = new Hyperlink();
        link.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                doDownload(serverId);
            }

        });
        link.setText("download");
        return link;
    }

    protected void doDownload(final String serverId) {
        Window.alert("downloading " + serverId);
    }

    protected Hyperlink createRemoveLink(final String serverId) {
        StringHelper.checkNotEmpty("parameter:serverId", serverId);
        final Hyperlink link = new Hyperlink();
        link.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                doRemove(serverId);
            }

        });
        link.setText("remove");
        return link;
    }

    protected void doRemove(final String serverId) {
        Window.alert("removing " + serverId);
    }

    protected Widget visitFilename(final String filename) {
        StringHelper.checkNotEmpty("parameter:filename", filename);
        return new Label(filename);
    }

    protected Widget visitSize(final int size) {
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:size", size, 0);
        return new Label(PrimitiveHelper.formatKiloOrMegabytes(size));
    }

    protected Widget visitDescription(final String description) {
        StringHelper.checkNotEmpty("parameter:description", description);
        return new Label(description);
    }

    protected Widget visitCreateDate(final Date createDate) {
        ObjectHelper.checkNotNull("parameter:createDate", createDate);
        return new Label(createDate.toGMTString());
    }

    protected Widget visitDownloadDate(final Date downloadDate) {
        ObjectHelper.checkNotNull("parameter:downloadDate", downloadDate);
        return new Label(downloadDate.toGMTString());
    }

    protected Widget visitMissingDownloadDate() {
        return new Label("");
    }
}
