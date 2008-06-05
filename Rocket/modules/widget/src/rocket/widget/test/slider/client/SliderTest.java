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
package rocket.widget.test.slider.client;

import rocket.event.client.ChangeEvent;
import rocket.event.client.ChangeEventListener;
import rocket.style.client.Css;
import rocket.style.client.InlineStyle;
import rocket.util.client.StackTrace;
import rocket.widget.client.Image;
import rocket.widget.client.slider.FloatingSlider;
import rocket.widget.client.slider.HorizontalSlider;
import rocket.widget.client.slider.VerticalSlider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

public class SliderTest implements EntryPoint {

	static final int DELTA = 1;

	static final int VALUE = 50;

	static final int MAXIMUM_VALUE = 100;

	static final int MOUSEDOWN_REPEAT_RATE = 20;

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert(StackTrace.asString(caught));
			}
		});

		final RootPanel rootPanel = RootPanel.get( "main");

		final Grid grid = new Grid(4, 3);
		grid.setCellPadding(2);
		grid.setCellSpacing(2);
		grid.setBorderWidth(0);

		rootPanel.add(grid);

		final CellFormatter cellFormatter = grid.getCellFormatter();
		cellFormatter.setWidth(0, 0, "33%");
		cellFormatter.setWidth(0, 1, "33%");
		cellFormatter.setWidth(0, 2, "33%");

		this.buildHorizontalSlider(grid);
		this.buildVerticalSlider(grid);
		this.buildFloatingSlider(grid);

		rootPanel.getElement().scrollIntoView();
	}

	void buildFloatingSlider(final Grid grid) {
		final int column = 2;

		grid.setText(0, column, "FloatingSlider");

		final Label value = new Label();
		grid.setWidget(1, column, value);

		final Label changeCounter = new Label();
		grid.setWidget(2, column, changeCounter);

		final FloatingSlider slider = new FloatingSlider();
		slider.setDeltaX(DELTA);
		slider.setDeltaY(DELTA);
		slider.setMaximumXValue(MAXIMUM_VALUE);
		slider.setMaximumYValue(MAXIMUM_VALUE);
		slider.setHandle(createHandle());
		slider.setBackground(createBackground());
		slider.setMouseDownRepeatRate(MOUSEDOWN_REPEAT_RATE);

		slider.addChangeEventListener(new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				value.setText("Values: " + slider.getXValue() + "," + slider.getYValue() + " of " + slider.getMaximumXValue() + ","
						+ slider.getMaximumYValue());

				counter++;
				changeCounter.setText("ChangeEvent counter: " + counter);
			}

			int counter = 0;
		});

		slider.setXValue(VALUE);
		slider.setYValue(VALUE);

		grid.setWidget(3, column, slider);
	}

	void buildVerticalSlider(final Grid grid) {
		final int column = 1;

		grid.setText(0, column, "VerticalSlider");

		final Label value = new Label();
		grid.setWidget(1, column, value);

		final Label changeCounter = new Label();
		grid.setWidget(2, column, changeCounter);

		final VerticalSlider slider = new VerticalSlider();
		slider.setDelta(DELTA);
		slider.setMaximumValue(MAXIMUM_VALUE);
		slider.setHandle(createHandle());
		slider.setBackground(createBackground());
		slider.setMouseDownRepeatRate(MOUSEDOWN_REPEAT_RATE);

		slider.addChangeEventListener(new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				value.setText("Value: " + slider.getValue() + " of " + slider.getMaximumValue());

				counter++;
				changeCounter.setText("ChangeEvent counter: " + counter);
			}

			int counter = 0;
		});

		slider.setValue(VALUE);

		grid.setWidget(3, column, slider);
	}

	void buildHorizontalSlider(final Grid grid) {
		final int column = 0;

		grid.setText(0, column, "HorizontalSlider");

		final Label value = new Label();
		grid.setWidget(1, column, value);

		final Label changeCounter = new Label();
		grid.setWidget(2, column, changeCounter);

		final HorizontalSlider slider = new HorizontalSlider();
		slider.setDelta(DELTA);
		slider.setMaximumValue(MAXIMUM_VALUE);
		slider.setHandle(createHandle());
		slider.setBackground(createBackground());
		slider.setMouseDownRepeatRate(MOUSEDOWN_REPEAT_RATE);

		slider.addChangeEventListener(new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				value.setText("Value: " + slider.getValue() + " of " + slider.getMaximumValue());

				counter++;
				changeCounter.setText("ChangeEvent counter: " + counter);
			}

			int counter = 0;
		});

		slider.setValue(VALUE);

		grid.setWidget(3, column, slider);
	}

	Widget createHandle() {
		final Image widget = new Image("star.png");
		widget.addStyleName("handle");
		return widget;
	}

	Widget createBackground() {
		final int rows = 10;
		final int columns = 10;

		final Grid grid = new Grid(rows, columns);

		final Element gridElement = grid.getElement();
		InlineStyle.setString(gridElement, Css.BORDER_COLLAPSE, "collapse");
		InlineStyle.setString(gridElement, Css.BORDER_SPACING, "0");

		final CellFormatter cellFormatter = grid.getCellFormatter();

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				final Element element = cellFormatter.getElement(r, c);
				DOM.setInnerHTML(element, "");
				final int red = 224 - r * 4;
				final int green = 224 - c * 4;
				final int blue = 224 ^ r ^ c;
				final int rgb = red * 256 * 256 + green * 256 + blue;
				InlineStyle.setString(element, Css.BACKGROUND_COLOR, "#" + Integer.toHexString(rgb));
			}
		}
		return grid;
	}
}
