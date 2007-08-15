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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rocket.browser.client.BrowserHelper;
import rocket.util.client.StringComparator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This test creates a SortableTable and a number of buttons that allow the user to add / remove etc files to the table for viewing.
 */
public class SortableTableTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                caught.printStackTrace();
                Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
            }
        });

        final RootPanel rootPanel = RootPanel.get();

        final SortableFileTable table = new SortableFileTable();
        rootPanel.add(table);
        table.setColumnComparator(StringComparator.IGNORE_CASE_COMPARATOR, 0, true);

        table.setColumnComparator(new Comparator() {
            public int compare(Object first, Object second) {
                final int firstValue = ((Integer) first).intValue();
                final int secondValue = ((Integer) second).intValue();
                return firstValue - secondValue;
            }
        }, 1, true);
        table.makeColumnUnsortable(2);
        table.makeColumnUnsortable(3);

        table.setSortedColumn(1);

        final List files = table.getRows();
        
        final Button adder = new Button("Add new File");
        adder.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                    final File newFile = new File();
                    newFile.setDescription(BrowserHelper.prompt("File description", "Enter description here..."));

                    final Date now = new Date();
                    String filename = now.getYear() + now.getMonth() + now.getDay() + now.getHours() + now.getMinutes() + now.getSeconds() + ".bin";
                    filename = BrowserHelper.prompt("Filename", filename);
                    newFile.setFilename(filename);

                    final String size = BrowserHelper.prompt("File size", "" + Random.nextInt(123456789));
                    newFile.setSize(Integer.parseInt(size));
                    newFile.setCreateDate(new Date());
                    files.add(newFile);
            }
        });
        rootPanel.add(adder);

        final Button removeFile = new Button("Remove from unsorted add order list");
        rootPanel.add(removeFile);
        removeFile.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                    final int index = Integer.parseInt(BrowserHelper.prompt("File index", "0"));
                    table.getRows().remove(index);
            }
        });

        final Button removeRow = new Button("Remove from sorted list (table view)");
        rootPanel.add(removeRow);
        removeRow.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                    final int index = Integer.parseInt(BrowserHelper.prompt("Remove row", "0"));
                    table.getTableRows().remove(index);

            }
        });

        final Button fileGetter = new Button("Get File from unsorted add order list");
        rootPanel.add(fileGetter);
        fileGetter.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                    final int index = Integer.parseInt(BrowserHelper.prompt("File index", "0"));
                    final Object file = table.getRows().get(index);
                    Window.alert("File Index: " + index + "\n" + file);
            }
        });

        final Button tableRowGetter = new Button("Get File from sorted list");
        rootPanel.add(tableRowGetter);
        tableRowGetter.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                    final int row = Integer.parseInt(BrowserHelper.prompt("Table row", "0"));
                    final Object file = table.getTableRows().get(row);
                    Window.alert("row: " + row + "\n" + file);
            }
        });

        final Button bulkAdder = new Button("Add n files");
        rootPanel.add(bulkAdder);
        bulkAdder.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                final String countString = BrowserHelper.prompt("Enter the number of files to add", "10");

                    final int count = Integer.parseInt(countString.trim());
                    final List newFiles = SortableTableTest.this.generateFiles(count);
                    final long start = System.currentTimeMillis();
                    table.getRows().addAll(newFiles);
                    final long end = System.currentTimeMillis();

                    Window.alert(count + " files added to table in " + (end - start) + " milliseconds. Table now has "
                            + table.getRows().size() + " rows ");
            }
        });
        
        final File file0 = new File();
        file0.setCreateDate(new Date());
        file0.setDescription("none");
        file0.setFilename("apple.txt");
        file0.setSize(1000);
        files.add(file0);
        
        final File file1 = new File();
        file1.setCreateDate(new Date());
        file1.setDescription("none");
        file1.setFilename("big.txt");
        file1.setSize(123456);
        files.add(file1);

        final File file2 = new File();
        file2.setCreateDate(new Date());
        file2.setDescription("none");
        file2.setFilename("small.txt");
        file2.setSize(12);
        files.add(file2);

        final File file3 = new File();
        file3.setCreateDate(new Date());
        file3.setDescription("none");
        file3.setFilename("huge.txt");
        file3.setSize(123456789);
        files.add(file3);
    }

    protected List generateFiles(final int count) {
        final List newFiles = new ArrayList();

        final Date now = new Date();
        final String filenamePrefix = "" + now.getYear() + now.getMonth() + now.getDay() + now.getHours()
                + now.getMinutes() + now.getSeconds();

        for (int i = 0; i < count; i++) {
            final File newFile = new File();
            newFile.setDescription("generated file " + i);
            newFile.setFilename(filenamePrefix + i + ".bin");
            newFile.setSize(Math.abs(Random.nextInt()));
            newFile.setCreateDate(new Date());
            newFiles.add(newFile);
        }
        return newFiles;
    }
}
