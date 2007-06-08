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
        table.setAscendingSortImageSource("./up.gif");
        table.setDescendingSortImageSource("./down.gif");
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
        table.makeColumnUnsortable(4);
        table.makeColumnUnsortable(5);
        table.makeColumnUnsortable(6);

        table.setSortedColumn(1);

        final List files = table.getRows();

        final File file0 = new File();
        file0.setCreateDate(new Date());
        file0.setDescription("none");
        file0.setFilename("apple.txt");
        file0.setServerId("file://apple.txt");
        file0.setSize(1000);
        files.add(file0);

        final File file1 = new File();
        file1.setCreateDate(new Date());
        file1.setDescription("none");
        file1.setFilename("big.txt");
        file1.setServerId("file://big.txt");
        file1.setSize(123456);
        files.add(file1);

        final File file2 = new File();
        file2.setCreateDate(new Date());
        file2.setDescription("none");
        file2.setFilename("small.txt");
        file2.setServerId("file://small.txt");
        file2.setSize(12);
        files.add(file2);

        final File file3 = new File();
        file3.setCreateDate(new Date());
        file3.setDescription("none");
        file3.setFilename("huge.txt");
        file3.setServerId("file://huge.txt");
        file3.setSize(123456789);
        files.add(file3);

        final Button adder = new Button("Add new File");
        adder.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                try {
                    final File newFile = new File();

                    final String description = BrowserHelper.prompt("File description", "{description}");
                    newFile.setDescription(description);

                    final Date now = new Date();
                    String filename = now.getYear() + now.getMonth() + now.getDay() + now.getHours() + now.getMinutes()
                            + now.getSeconds() + ".bin";
                    filename = BrowserHelper.prompt("Filename", filename);
                    newFile.setFilename(filename);
                    newFile.setServerId(filename);

                    final String size = BrowserHelper.prompt("File size", "" + Random.nextInt(123456789));
                    newFile.setSize(Integer.parseInt(size));
                    newFile.setCreateDate(new Date());
                    files.add(newFile);

                } catch (Exception caught) {
                    caught.printStackTrace();
                    Window.alert("" + caught);
                }
            }
        });
        rootPanel.add(adder);

        final Button removeFile = new Button("Remove File");
        rootPanel.add(removeFile);
        removeFile.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                try {
                    final int index = Integer.parseInt(BrowserHelper.prompt("File index", "0"));
                    table.getRows().remove(index);

                } catch (Exception caught) {
                    caught.printStackTrace();
                    Window.alert("" + caught);
                }
            }
        });

        final Button removeRow = new Button("Remove Row");
        rootPanel.add(removeRow);
        removeRow.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                try {
                    final int index = Integer.parseInt(BrowserHelper.prompt("Remove row", "0"));
                    table.getRows().remove(index);

                } catch (Exception caught) {
                    caught.printStackTrace();
                    Window.alert("" + caught);
                }
            }
        });

        final Button fileGetter = new Button("Get File");
        rootPanel.add(fileGetter);
        fileGetter.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                try {
                    final int index = Integer.parseInt(BrowserHelper.prompt("File index", "0"));
                    final Object file = table.getRows().get(index);
                    Window.alert("File Index: " + index + "\n" + file);

                } catch (Exception caught) {
                    caught.printStackTrace();
                    Window.alert("" + caught);
                }
            }
        });

        final Button tableRowGetter = new Button("Get Table Row");
        rootPanel.add(tableRowGetter);
        tableRowGetter.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                try {
                    final int row = Integer.parseInt(BrowserHelper.prompt("Table row", "0"));
                    final Object file = table.getTableRows().get(row);
                    Window.alert("row: " + row + "\n" + file);

                } catch (Exception caught) {
                    caught.printStackTrace();
                    Window.alert("" + caught);
                }
            }
        });

        final Button bulkAdder = new Button("Add n files");
        rootPanel.add(bulkAdder);
        bulkAdder.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                final String countString = BrowserHelper.prompt("Enter the number of files to add", "10");
                try {
                    final int count = Integer.parseInt(countString.trim());
                    final List newFiles = SortableTableTest.this.generateFiles(count);
                    final long start = System.currentTimeMillis();
                    table.getRows().addAll(newFiles);
                    final long end = System.currentTimeMillis();

                    Window.alert(count + " files added to table in " + (end - start) + " milliseconds. Table now has "
                            + table.getRows().size() + " rows ");

                } catch (final Exception caught) {
                    caught.printStackTrace();
                    Window.alert("" + caught);
                }
            }
        });
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
            newFile.setServerId("unknown");
            newFile.setSize(Math.abs(Random.nextInt()));
            newFile.setCreateDate(new Date());
            newFiles.add(newFile);
        }
        return newFiles;
    }
}
