/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this fruit except in compliance with the License. You may obtain a copy of
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
import java.util.List;

import rocket.util.client.Checker;
import rocket.util.client.StringComparator;
import rocket.widget.client.SortableTable;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This test creates a SortableTable and a number of buttons that allow the user
 * to add / remove etc fruits to the table for viewing.
 */
public class SortableTableTest implements EntryPoint {

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage\"" + caught.getMessage() + "\".");
			}
		});

		final RootPanel rootPanel = RootPanel.get();

		final SortableTable table = new SortableTable() {
			protected void afterCreateWidget() {
				super.afterCreateWidget();

				this.setHeadings();
			}

			protected void onColumnSortingClick(final Widget widget) {
				final long started = System.currentTimeMillis();

				super.onColumnSortingClick(widget);

				final long ended = System.currentTimeMillis();

				Window.alert("Sorting of " + this.getRows().size() + " rows took " + (ended - started) + " milli(s) ");
			}

			protected Object getValue(final Object row, final int column) {
				Checker.notNull("parameter:row", row);
				this.checkColumn("parameter:column", column);

				final Fruit fruit = (Fruit) row;

				Object value = null;
				switch (column) {
				case 0: {
					value = fruit.name;
					break;
				}
				case 1: {
					value = fruit.colour;
					break;
				}
				case 2: {
					value = new Integer(fruit.seedCount);
					break;
				}
				case 4: {
					value = new Integer(fruit.weight);
					break;
				}
				}
				return value;
			}

			protected Widget getWidget(final Object value, final int column) {
				Checker.notNull("parameter:value", value);
				this.checkColumn("parameter:column", column);

				System.out.println("SortableFruitTable - getting widget for column: " + column + " value: " + value);

				final Fruit fruit = (Fruit) value;
				Widget widget = null;
				switch (column) {
				case 0: {
					widget = new Label(fruit.name);
					break;
				}
				case 1: {
					widget = new Label(fruit.colour);
					break;
				}
				case 2: {
					widget = new Label("" + fruit.seedCount);
					break;
				}
				case 3: {
					widget = new Label("" + fruit.shiny);
					break;
				}
				case 4: {
					widget = new Label("" + fruit.weight + " g");
					break;
				}
				}
				return widget;
			}

			protected void setHeadings() {
				int row = 0;
				int i = 0;
				this.setWidget(row, i, this.createHeader("Name", i));
				i++;
				this.setWidget(row, i, this.createHeader("Colour", i));
				i++;
				this.setWidget(row, i, this.createHeader("Seed count", i));
				i++;
				this.setWidget(row, i, this.createHeader("Shiny", i));
				i++;
				this.setWidget(row, i, this.createHeader("Average weight", i));
			}

			protected int getColumnCount() {
				return 5;
			}

			protected String getAscendingSortImageSource() {
				return "up-arrow.gif";
			}

			protected String getDescendingSortImageSource() {
				return "down-arrow.gif";
			}
		};
		rootPanel.add(table);
		table.setColumnComparator(StringComparator.IGNORE_CASE_COMPARATOR, 0, true);
		table.setColumnComparator(StringComparator.IGNORE_CASE_COMPARATOR, 1, true);

		final Comparator integerComparator = new Comparator() {
			public int compare(Object first, Object second) {
				final int firstValue = ((Integer) first).intValue();
				final int secondValue = ((Integer) second).intValue();
				return firstValue - secondValue;
			}
		};

		table.setColumnComparator(integerComparator, 2, true);
		table.makeColumnUnsortable(3);
		table.setColumnComparator(integerComparator, 4, true);
		table.setSortedColumn(1);

		final Fruit apple = new Fruit();
		apple.name = "Apple";
		apple.colour = "red";
		apple.seedCount = 5;
		apple.shiny = true;
		apple.weight = 300;
		table.getRows().add(apple);

		final Fruit banana = new Fruit();
		banana.name = "Banana";
		banana.colour = "yellow";
		banana.seedCount = 1;
		banana.shiny = true;
		banana.weight = 200;
		table.getRows().add(banana);

		final Fruit carrot = new Fruit();
		carrot.name = "Carrot";
		carrot.colour = "orange";
		carrot.seedCount = 0;
		carrot.shiny = false;
		carrot.weight = 100;
		table.getRows().add(carrot);

		table.setAutoRedraw(true);

		final Button adder = new Button("Add new Fruit");
		adder.addClickListener(new ClickListener() {
			public void onClick(final Widget ignore) {
				final Fruit fruit = new Fruit();
				fruit.name = Window.prompt("Fruit name", "");
				fruit.colour = Window.prompt("Colour", "red");
				fruit.seedCount = Integer.parseInt(Window.prompt("Seed count", "10"));
				fruit.shiny = "true".equalsIgnoreCase(Window.prompt("Shiny ?(true/false)", "false"));
				fruit.weight = Integer.parseInt(Window.prompt("Avg weight", "10"));

				table.getRows().add(fruit);
			}
		});
		rootPanel.add(adder);

		final Button removeFruit = new Button("Remove from unsorted add order list");
		rootPanel.add(removeFruit);
		removeFruit.addClickListener(new ClickListener() {
			public void onClick(final Widget ignore) {
				final int index = Integer.parseInt(Window.prompt("Fruit index", "0"));
				table.getRows().remove(index);
			}
		});

		final Button removeRow = new Button("Remove nth fruit from (table view)");
		rootPanel.add(removeRow);
		removeRow.addClickListener(new ClickListener() {
			public void onClick(final Widget ignore) {
				final int index = Integer.parseInt(Window.prompt("Remove row", "0"));
				table.getTableRows().remove(index);

			}
		});

		final Button fruitGetter = new Button("Get Fruit (add order list)");
		rootPanel.add(fruitGetter);
		fruitGetter.addClickListener(new ClickListener() {
			public void onClick(final Widget ignore) {
				final int index = Integer.parseInt(Window.prompt("Fruit index", "0"));
				final Object fruit = table.getRows().get(index);
				Window.alert("Fruit Index: " + index + "\n" + fruit);
			}
		});

		final Button tableRowGetter = new Button("Get Fruit (table row)");
		rootPanel.add(tableRowGetter);
		tableRowGetter.addClickListener(new ClickListener() {
			public void onClick(final Widget ignore) {
				final int row = Integer.parseInt(Window.prompt("Table row", "0"));
				final Object fruit = table.getTableRows().get(row);
				Window.alert("row: " + row + "\n" + fruit);
			}
		});

		final Button bulkAdder = new Button("Add n fruit");
		rootPanel.add(bulkAdder);
		bulkAdder.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final String countString = Window.prompt("Enter the number of fruits to add", "10");

				final int count = Integer.parseInt(countString.trim());
				final List newFruits = SortableTableTest.this.generateFruit(count);
				final long start = System.currentTimeMillis();
				table.getRows().addAll(newFruits);
				final long end = System.currentTimeMillis();

				Window.alert(count + " fruit(s) added to table in " + (end - start) + " milliseconds. Table now has "
						+ table.getRows().size() + " rows ");
			}
		});
	}

	protected List generateFruit(final int count) {
		final List fruits = new ArrayList();

		for (int i = 0; i < count; i++) {
			final Fruit fruit = new Fruit();

			final String colour = COLOURS[i % COLOURS.length];

			fruit.name = Character.toUpperCase(colour.charAt(0)) + colour.substring(1) + "berry";
			fruit.colour = colour;
			fruit.seedCount = Random.nextInt(23);
			fruit.shiny = Random.nextBoolean();
			fruit.weight = 12 + Random.nextInt(345);
			fruits.add(fruit);
		}

		return fruits;
	}

	static class Fruit {
		String name;

		String colour;

		int seedCount;

		boolean shiny;

		int weight;

		public String toString() {
			return "" + name;
		}
	}

	static String[] COLOURS = new String[] { "red", "orange", "yellow", "blue", "straw", "green", "pink", "purple", "goose", "rock",
			"grass" };
}
