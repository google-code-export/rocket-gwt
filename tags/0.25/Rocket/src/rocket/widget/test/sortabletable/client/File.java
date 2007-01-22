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
package rocket.widget.test.sortabletable.client;

import java.util.Date;

import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;

/**
 * Represents a single file on the server.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 */
public class File {

    /**
     * The name of the file
     */
    private String filename;

    public String getFilename() {
        StringHelper.checkNotEmpty("field:filename", filename);
        return filename;
    }

    public void setFilename(final String filename) {
        StringHelper.checkNotEmpty("parameter:filename", filename);
        this.filename = filename;
    }

    /**
     * The size of the file in bytes.
     */
    private int size;

    public int getSize() {
        PrimitiveHelper.checkGreaterThanOrEqual("field:size", size, 0);
        return size;
    }

    public void setSize(final int size) {
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:size", size, 0);
        this.size = size;
    }

    /**
     * A description accompanying the file
     */
    private String description;

    public String getDescription() {
        StringHelper.checkNotEmpty("field:description", description);
        return description;
    }

    public boolean hasDescription() {
        return null != description;
    }

    public void setDescription(final String description) {
        StringHelper.checkNotEmpty("parameter:description", description);
        this.description = description;
    }

    /**
     * The date the file was created.
     */
    private Date createDate;

    public Date getCreateDate() {
        ObjectHelper.checkNotNull("field:createDate", createDate);
        return createDate;
    }

    public void setCreateDate(final Date createDate) {
        ObjectHelper.checkNotNull("parameter:createDate", createDate);
        this.createDate = createDate;
    }

    /**
     * THe date/time the file was downloaded. Will be null if the file has not been downloaded.
     */
    private Date downloadDate;

    public Date getDownloadDate() {
        ObjectHelper.checkNotNull("field:downloadDate", downloadDate);
        return downloadDate;
    }

    public boolean hasDownloadDate() {
        return null != downloadDate;
    }

    public void setDownloadDate(final Date downloadDate) {
        ObjectHelper.checkNotNull("parameter:downloadDate", downloadDate);
        this.downloadDate = downloadDate;
    }

    /**
     * A long string which identifies this file on the server.
     */
    private String serverId;

    public String getServerId() {
        StringHelper.checkNotEmpty("field:serverId", serverId);
        return serverId;
    }

    public void setServerId(final String serverId) {
        StringHelper.checkNotEmpty("parameter:serverId", serverId);
        this.serverId = serverId;
    }

    public String toString() {
        return super.toString() + ", filename[" + filename + "], size: " + size + ", description[" + description
                + "], createDate: " + createDate + ", downloadDate: " + downloadDate + ", serverId[" + serverId + "]";
    }
}
