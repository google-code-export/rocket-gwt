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

import java.util.Comparator;
import java.util.Date;

import rocket.client.util.StringComparator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SortableTableTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        try {
            final SortableFileTable table = new SortableFileTable();
            table.setAscendingSortImageSource(GWT.getModuleBaseURL() + "/up.gif");
            table.setDescendingSortImageSource(GWT.getModuleBaseURL() + "/down.gif");
            RootPanel.get().add(table);
            table.setColumnComparator( StringComparator.IGNORE_CASE_COMPARATOR, 0, true);

            table.setColumnComparator(new Comparator() {
                public int compare(Object first, Object second) {
                    final int firstValue = ((Integer) first).intValue();
                    final int secondValue = ((Integer) second).intValue();
                    return firstValue - secondValue;
                }
            }, 1, true);
            table.makeColumnUnsortable(2);
            table.makeColumnUnsortable(3);
            table.makeColumnUnsortable(4);
            table.makeColumnUnsortable(5);

            table.setSortedColumn(1);

            final File file0 = new File();
            file0.setCreateDate(new Date());
            file0.setDescription("none");
            file0.setFilename("apple.txt");
            file0.setServerId("file://apple.txt");
            file0.setSize(1000);
            table.addDownloadFile(file0);

            final File file1 = new File();
            file1.setCreateDate(new Date());
            file1.setDescription("none");
            file1.setFilename("big.txt");
            file1.setServerId("file://big.txt");
            file1.setSize(123456);
            table.addDownloadFile(file1);

            final File file2 = new File();
            file2.setCreateDate(new Date());
            file2.setDescription("none");
            file2.setFilename("small.txt");
            file2.setServerId("file://small.txt");
            file2.setSize(12);
            table.addDownloadFile(file2);

            final File file3 = new File();
            file3.setCreateDate(new Date());
            file3.setDescription("none");
            file3.setFilename("huge.txt");
            file3.setServerId("file://huge.txt");
            file3.setSize(123456789);
            table.addDownloadFile(file3);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
