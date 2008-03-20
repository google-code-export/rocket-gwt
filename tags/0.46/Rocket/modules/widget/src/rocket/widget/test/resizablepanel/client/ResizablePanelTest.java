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
package rocket.widget.test.resizablepanel.client;

import rocket.event.client.ChangeEvent;
import rocket.event.client.ChangeEventListener;
import rocket.widget.client.CheckBox;
import rocket.widget.client.Label;
import rocket.widget.client.ListBox;
import rocket.widget.client.RadioButton;
import rocket.widget.client.ResizablePanel;
import rocket.widget.client.TextBox;
import rocket.widget.client.Widgets;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class ResizablePanelTest implements EntryPoint {

	final String TEXT = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, fringilla vel, urna. Aliquam commodo ullamcorper erat. Nullam vel justo in neque porttitor laoreet. Aenean lacus dui, consequat eu, adipiscing eget, nonummy non, nisi. Morbi nunc est, dignissim non, ornare sed, luctus eu, massa.";

	final String IMAGE_URL = "snipe.jpg"; // thanx wikipedia

	final int MINIMUM_WIDTH = 150;

	final int MAXIMUM_WIDTH = 400;

	final int MINIMUM_HEIGHT = 150;

	final int MAXIMUM_HEIGHT = 400;

	final int GRID_ROW_COUNT = 3;

	final int GRID_COLUMN_COUNT = 3;

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage\"" + caught.getMessage() + "\".");
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		
		final Label changeEventCounter = new Label("?");
		rootPanel.add(changeEventCounter);

		final ChangeEventListener countUpdater = new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				counter++;
				changeEventCounter.setText("ChangeEventCounter: " + counter);
			}

			int counter = 0;
		};

		rootPanel.add(new Label("Resizable TextArea"));

		final TextArea textArea0 = new TextArea();
		textArea0.setText(TEXT);
		rootPanel.add(this.createResizablePanel(textArea0, false, countUpdater));

		rootPanel.add(new Label("Resizable TextArea (keeps aspect ratio)"));
		final TextArea textArea1 = new TextArea();
		textArea1.setText(TEXT);
		rootPanel.add(this.createResizablePanel(textArea1, true, countUpdater));

		rootPanel.add(new Label("Resizable image"));

		final Image image0 = new Image();
		image0.setUrl(IMAGE_URL);
		rootPanel.add(this.createResizablePanel(image0, false, countUpdater));

		rootPanel.add(new Label("Resizable Image (keeps aspect ratio)"));
		final Image image1 = new Image();
		image1.setUrl(IMAGE_URL);
		rootPanel.add(this.createResizablePanel(image1, true, countUpdater));

		rootPanel.add(new Label("Resizable Label"));

		final Label label0 = new Label();
		label0.setText(TEXT);
		label0.setWidth((MAXIMUM_WIDTH + MINIMUM_WIDTH) / 2 + "px");
		label0.setHeight((MAXIMUM_HEIGHT + MINIMUM_HEIGHT) / 2 + "px");
		rootPanel.add(this.createResizablePanel(label0, false, countUpdater));

		rootPanel.add(new Label("Resizable Label (keeps aspect ratio)"));
		final Label label1 = new Label();
		label1.setText(TEXT);
		label1.setWidth((MAXIMUM_WIDTH + MINIMUM_WIDTH) / 2 + "px");
		label1.setHeight((MAXIMUM_HEIGHT + MINIMUM_HEIGHT) / 2 + "px");
		rootPanel.add(this.createResizablePanel(label1, true, countUpdater));

		rootPanel.add(new Label("Resizable Grid(html table)"));
		final Grid grid0 = new Grid(GRID_ROW_COUNT, GRID_COLUMN_COUNT);
		for (int r = 0; r < GRID_ROW_COUNT; r++) {
			for (int c = 0; c < GRID_COLUMN_COUNT; c++) {
				grid0.setWidget(r, c, new Label("x"));
			}
		}
		grid0.setWidth((MAXIMUM_WIDTH + MINIMUM_WIDTH) / 2 + "px");
		grid0.setHeight((MAXIMUM_HEIGHT + MINIMUM_HEIGHT) / 2 + "px");
		rootPanel.add(this.createResizablePanel(grid0, false, countUpdater));

		rootPanel.add(new Label("Resizable Grid(html table)(keeps aspect ratio)"));
		final Grid grid1 = new Grid(GRID_ROW_COUNT, GRID_COLUMN_COUNT);
		for (int r = 0; r < GRID_ROW_COUNT; r++) {
			for (int c = 0; c < GRID_COLUMN_COUNT; c++) {
				grid1.setWidget(r, c, new Label("x"));
			}
		}
		grid1.setWidth((MAXIMUM_WIDTH + MINIMUM_WIDTH) / 2 + "px");
		grid1.setHeight((MAXIMUM_HEIGHT + MINIMUM_HEIGHT) / 2 + "px");
		rootPanel.add(this.createResizablePanel(grid1, true, countUpdater));

		rootPanel.add(new Label("Resizable FlowPanel with multiple child widgets."));

		final FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(new TextBox());
		flowPanel.add(new TextBox(true));
		flowPanel.add(new CheckBox());
		flowPanel.add(new RadioButton("group"));
		flowPanel.add(new TextBox());
		flowPanel.add(new TextBox(true));
		flowPanel.add(new ListBox());
		flowPanel.setWidth((MAXIMUM_WIDTH + MINIMUM_WIDTH) / 2 + "px");
		flowPanel.setHeight((MAXIMUM_HEIGHT + MINIMUM_HEIGHT) / 2 + "px");
		rootPanel.add(this.createResizablePanel(flowPanel, true, countUpdater));
		
		Widgets.forceDocumentContentsToScroll( 100 );
	}

	protected ResizablePanel createResizablePanel(final Widget widget, final boolean keepAspectRatio,
			final ChangeEventListener changeEventListener) {
		final ResizablePanel panel = new ResizablePanel();
		panel.setMinimumWidth(MINIMUM_WIDTH);
		panel.setMaximumWidth(MAXIMUM_WIDTH);
		panel.setWidth(MINIMUM_WIDTH + "px");

		panel.setMinimumHeight(MINIMUM_HEIGHT);
		panel.setMaximumHeight(MAXIMUM_HEIGHT);
		panel.setHeight(MINIMUM_HEIGHT + "px");

		panel.setKeepAspectRatio(keepAspectRatio);

		panel.setWidget(widget);

		panel.addChangeEventListener(changeEventListener);
		return panel;
	}
}
