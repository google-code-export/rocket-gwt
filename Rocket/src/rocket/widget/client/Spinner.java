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
package rocket.widget.client;

import rocket.event.client.ChangeEventListener;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.FocusEventListener;
import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A spinner is a simple widget which allows a user to increase or decrease a
 * number keeping it within a defined range. Two methods are available if
 * sub-classes wish to handle the clicking of either the up or down control
 * 
 * To display the value a separate Label needs to be created and a listener
 * registered to receive NumberValueChanged events. It is thus possible to also
 * create a separate text field and have it also update the spinner and vice
 * versa. This widget only includes and controls the up/down htmls.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Spinner extends CompositeWidget{

	public Spinner() {
	}

	protected Widget createWidget() {
		final Panel panel = this.createPanel();
		this.setPanel(panel);
		return panel;
	}

	protected void afterCreateWidget() {
		this.setDelta(1);
		this.updateValue(this.getValue());
	}

	protected String getInitialStyleName() {
		return WidgetConstants.SPINNER_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.CHANGE | EventBitMaskConstants.FOCUS_EVENTS;
	}

	/**
	 * The current value of the spinner;
	 */
	private int value;

	public int getValue() {
		return this.value;
	}

	public void setValue(final int value) {
		this.value = value;

		this.getEventListenerDispatcher().getChangeEventListeners().fireChange(this);
	}

	/**
	 * The minimum value of the spinner;
	 */
	private int lowerBounds;

	public int getLowerBounds() {
		return this.lowerBounds;
	}

	public void setLowerBounds(final int lowerBounds) {
		this.lowerBounds = lowerBounds;
	}

	/**
	 * The maximum value of the spinner.
	 */
	private int upperBounds;

	public int getUpperBounds() {
		return this.upperBounds;
	}

	public void setUpperBounds(final int upperBounds) {
		this.upperBounds = upperBounds;
	}

	/**
	 * The up html that when clicked increases the spinners value.
	 */
	private Image upWidget;

	protected Image getUpWidget() {
		ObjectHelper.checkNotNull("field:upWidget", upWidget);
		return this.upWidget;
	}

	protected void setUpWidget(final Image upWidget) {
		ObjectHelper.checkNotNull("parameter:upWidget", upWidget);
		this.upWidget = upWidget;
	}

	protected Image createUpWidget() {
		final Image image = new Image();
		image.setStyleName( this.getUpArrowStyle() );
		image.addMouseEventListener(new MouseEventAdapter() {

			public void onClick(final MouseClickEvent event) {
				Spinner.this.onUpClick();
			}
		});
		return image;
	}
	
	protected String getUpArrowStyle(){
		return WidgetConstants.SPINNER_UP_STYLE;		
	}

	/**
	 * Clicking on the up html increases the spinner's value.
	 */
	protected void onUpClick() {
		final int value = this.getValue() + this.getDelta();
		this.updateValue(value);
	}

	protected void onUpperBoundsReached() {
	}

	public String getUpImageUrl() {
		return this.getUpWidget().getUrl();
	}

	public void setUpImageUrl(final String upImageUrl) {
		StringHelper.checkNotEmpty("parameter:upImageUrl", upImageUrl);
		this.getUpWidget().setUrl(upImageUrl);
	}

	/**
	 * The down html that when clicked decreases the spinners value.
	 */
	private Image downWidget;

	protected Image getDownWidget() {
		ObjectHelper.checkNotNull("field:downWidget", downWidget);
		return this.downWidget;
	}

	protected void setDownWidget(final Image downWidget) {
		ObjectHelper.checkNotNull("parameter:downWidget", downWidget);
		this.downWidget = downWidget;
	}

	protected Image createDownWidget() {
		final Image image = new Image();
		image.setStyleName( this.getDownArrowStyle() );

		image.addMouseEventListener(new MouseEventAdapter() {

			public void onClick(final MouseClickEvent event) {
				Spinner.this.onDownClick();
			}
		});
		return image;
	}
	
	protected String getDownArrowStyle(){
		return WidgetConstants.SPINNER_DOWN_STYLE;
	}

	public String getDownImageUrl() {
		return this.getDownWidget().getUrl();
	}

	public void setDownImageUrl(final String downImageUrl) {
		StringHelper.checkNotEmpty("parameter:downImageUrl", downImageUrl);
		this.getDownWidget().setUrl(downImageUrl);
	}

	/**
	 * Clicking on the down html decreass the value.
	 * 
	 */
	protected void onDownClick() {
		final int value = this.getValue() - this.getDelta();
		this.updateValue(value);
	}

	protected void onLowerLimitReached() {
	}

	/**
	 * Updates both the value and the label showing the value to the user.
	 * 
	 * @param value
	 */
	protected void updateValue(int value) {
		while (true) {
			final int lowerBounds = this.getLowerBounds();
			if (value < lowerBounds) {
				value = lowerBounds;
			}

			final int upperBounds = this.getUpperBounds();
			if (value > upperBounds) {
				value = upperBounds;
			}
			break;
		}

		this.getEventListenerDispatcher().getChangeEventListeners().fireChange(this);
		this.setValue(value);
	}

	/**
	 * The down html that when clicked increases the spinners value.
	 */
	private Panel panel;

	protected Panel getPanel() {
		ObjectHelper.checkNotNull("field:panel", panel);
		return this.panel;
	}

	protected void setPanel(final Panel panel) {
		ObjectHelper.checkNotNull("parameter:panel", panel);
		this.panel = panel;
	}

	/**
	 * Creates a new panel which will enclose the up and down images which when
	 * clicked increase/decrease the value
	 * 
	 * @return
	 */
	protected Panel createPanel() {
		final VerticalPanel panel = new VerticalPanel();

		final Image upWidget = this.createUpWidget();
		this.setUpWidget(upWidget);
		panel.add(upWidget);

		final Image downWidget = this.createDownWidget();
		this.setDownWidget(downWidget);
		panel.add(downWidget);

		return panel;
	}

	/**
	 * The amount the value is increased/decreased each time an up or down
	 * widget is clicked.
	 */
	private int delta;

	public int getDelta() {
		return this.delta;
	}

	public void setDelta(final int delta) {
		this.delta = delta;
	}

	public void addChangeEventListener(final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().addChangeEventListener(changeEventListener);
	}

	public void removeChangeEventListener(final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().removeChangeEventListener(changeEventListener);
	}

	public void addFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().addFocusEventListener(focusEventListener);
	}

	public void removeFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().removeFocusEventListener(focusEventListener);
	}

	public String toString() {
		return super.toString() + ", value: " + value;
	}
}