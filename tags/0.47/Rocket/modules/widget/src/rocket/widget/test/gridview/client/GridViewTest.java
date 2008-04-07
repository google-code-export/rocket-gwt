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
package rocket.widget.test.gridview.client;

import rocket.widget.client.GridView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class GridViewTest implements EntryPoint {

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage\"" + caught.getMessage() + "\".");
			}
		});

		final RootPanel rootPanel = RootPanel.get();

		final HTML h = new HTML("X");
		rootPanel.add(h);
		h.removeFromParent();

		final GridView gridView = new GridView() {
			protected int getFirst() {
				return 0;
			}

			protected int getLast() {
				return 10;
			}

			protected int getCount() {
				return this.getLast() - this.getFirst();
			}

			protected Widget createCellWidget(final int index) {
				return new HTML("" + index);
			}

			protected Widget createFillerWidget() {
				return new HTML();
			}
		};
		gridView.setColumns(1);
		gridView.setCursor(0);
		gridView.setRows(1);
		gridView.setAutoRedraw(true);
		gridView.redraw();

		final TextBox rows = new TextBox();
		rootPanel.add(new Label("Rows: "));
		rootPanel.add(rows);

		final TextBox columns = new TextBox();
		columns.addKeyboardListener(new KeyboardListenerAdapter() {
			public void onKeyDown(final Widget sender, final char key, final int modifier) {
				if (key == KeyboardListener.KEY_ENTER) {

				}
			}
		});
		rootPanel.add(new Label("Columns: "));
		rootPanel.add(columns);

		final TextBox cursor = new TextBox();
		rootPanel.add(new Label("Cursor: "));
		rootPanel.add(cursor);

		final Button update = new Button("Update grid");
		update.addClickListener(new ClickListener() {
			public void onClick(Widget ignored) {
				final String rowsText = rows.getText();
				try {
					gridView.setRows(Integer.parseInt(rowsText));
				} catch (final NumberFormatException nfe) {
					Window.alert("rows textBox contains an invalid number \"" + rowsText + "\".");
				}

				final String columnsText = columns.getText();
				try {
					gridView.setColumns(Integer.parseInt(columnsText));
				} catch (final NumberFormatException nfe) {
					Window.alert("columns textBox contains an invalid number \"" + columnsText + "\".");
				}

				final String cursorText = cursor.getText();
				try {
					gridView.setCursor(Integer.parseInt(cursorText));
				} catch (final NumberFormatException nfe) {
					Window.alert("cursor textBox contains an invalid number \"" + cursorText + "\".");
				}

			}
		});
		rootPanel.add(update);

		rootPanel.add(gridView);
	}
}
