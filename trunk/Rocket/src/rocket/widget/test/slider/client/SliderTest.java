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
import rocket.widget.client.slider.HorizontalSlider;
import rocket.widget.client.slider.HorizontalVerticalSlider;
import rocket.widget.client.slider.VerticalSlider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SliderTest implements EntryPoint {

	static final int DELTA = 1;

	static final int VALUE = 50;

	static final int MAXIMUM_VALUE = 100;

	static final int MOUSEDOWN_REPEAT_RATE = 20;

	static final int WIDTH = 20;

	static final int HEIGHT = 20;

	static final int LENGTH = 200;

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		final HorizontalPanel panel = new HorizontalPanel();
		rootPanel.add(panel);

		final Label changeEventCounter = new Label("?");
		rootPanel.add(changeEventCounter);

		final ChangeEventListener changeEventListener = new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				this.counter++;

				changeEventCounter.setText("ChangeEvent counter: " + counter + ", lastWidget: " + GWT.getTypeName(event.getWidget()));
			}

			int counter;
		};

		final Label horizontalSliderValue = new Label();
		panel.add(horizontalSliderValue);

		final HorizontalSlider horizontalSlider = new HorizontalSlider();
		horizontalSlider.setWidth(LENGTH + "px");
		horizontalSlider.setHeight(HEIGHT + "px");
		horizontalSlider.setDelta(DELTA);
		horizontalSlider.setMaximumValue(MAXIMUM_VALUE);
		horizontalSlider.setHandle(createHandle());
		horizontalSlider.setMouseDownRepeatRate(MOUSEDOWN_REPEAT_RATE);

		horizontalSlider.addChangeEventListener(new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				horizontalSliderValue.setText("" + horizontalSlider.getValue());
			}
		});
		horizontalSlider.addChangeEventListener(changeEventListener);
		horizontalSlider.setValue(VALUE);

		panel.add(horizontalSlider);

		final Label verticalSliderValue = new Label();
		panel.add(verticalSliderValue);

		final VerticalSlider verticalSlider = new VerticalSlider();
		verticalSlider.setWidth(WIDTH + "px");
		verticalSlider.setHeight(LENGTH + "px");
		verticalSlider.setDelta(DELTA);
		verticalSlider.setMaximumValue(MAXIMUM_VALUE);
		verticalSlider.setHandle(this.createHandle());
		verticalSlider.setValue(VALUE);
		verticalSlider.setMouseDownRepeatRate(MOUSEDOWN_REPEAT_RATE);

		verticalSlider.addChangeEventListener(new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				verticalSliderValue.setText("" + verticalSlider.getValue());
			}
		});
		verticalSlider.addChangeEventListener(changeEventListener);

		panel.add(verticalSlider);

		final Label hvSliderValue = new Label();
		panel.add(hvSliderValue);

		final HorizontalVerticalSlider slider = new HorizontalVerticalSlider();
		slider.setWidth(LENGTH + "px");
		slider.setHeight(LENGTH + "px");
		slider.setDeltaX(DELTA);
		slider.setDeltaY(DELTA);
		slider.setMaximumXValue(MAXIMUM_VALUE);
		slider.setMaximumYValue(MAXIMUM_VALUE);
		slider.setHandle(this.createHandle());
		slider.setMouseDownRepeatRate(MOUSEDOWN_REPEAT_RATE);

		slider.addChangeEventListener(new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				hvSliderValue.setText("" + slider.getXValue() + "," + slider.getYValue());
			}
		});
		slider.addChangeEventListener(changeEventListener);

		slider.setXValue(VALUE);
		slider.setYValue(VALUE);

		panel.add(slider);

		rootPanel.add(new HTML("Value<br>"));

		final TextBox value = new TextBox();
		value.setText(String.valueOf(VALUE));
		value.addKeyboardListener(new KeyboardListenerAdapter() {
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				if (KeyboardListener.KEY_ENTER == keyCode) {
					final int newValue = Integer.parseInt(value.getText());
					horizontalSlider.setValue(newValue);
					verticalSlider.setValue(newValue);
					slider.setXValue(newValue);
					slider.setYValue(newValue);
				}
			}
		});
		rootPanel.add(value);

		rootPanel.add(new HTML("MouseDownRepeatRate<br>"));

		final TextBox mouseDownRepeatRate = new TextBox();
		mouseDownRepeatRate.setText(String.valueOf(MOUSEDOWN_REPEAT_RATE));
		mouseDownRepeatRate.addKeyboardListener(new KeyboardListenerAdapter() {
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				if (KeyboardListener.KEY_ENTER == keyCode) {
					final int newValue = Integer.parseInt(mouseDownRepeatRate.getText());
					horizontalSlider.setMouseDownRepeatRate(newValue);
					verticalSlider.setMouseDownRepeatRate(newValue);
				}
			}
		});
		rootPanel.add(mouseDownRepeatRate);
	}

	Widget createHandle() {
		final HTML widget = new HTML("&nbsp;");
		widget.setWidth(WIDTH + "px");
		widget.setHeight(HEIGHT + "px");
		widget.addStyleName("handle");
		return widget;
	}
}
