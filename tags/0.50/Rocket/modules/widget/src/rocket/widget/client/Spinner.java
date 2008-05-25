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
import rocket.util.client.Checker;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A spinner is a simple widget which allows a user to increase or decrease a
 * number keeping it within a defined range. Two methods are available if
 * sub-classes wish to handle when the upper or lower bounds of the spinner value are reached.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Spinner extends CompositeWidget {

	public Spinner() {
	}

	protected Widget createWidget() {
		return this.createVerticalPanel();
	}


	protected VerticalPanel getVerticalPanel() {
		return (VerticalPanel) this.getWidget();
	}

	/**
	 * Creates a new panel which will enclose the up and down images which when
	 * clicked increase/decrease the value
	 * 
	 * @return
	 */
	protected VerticalPanel createVerticalPanel() {
		final VerticalPanel verticalPanel = new VerticalPanel();
		
		verticalPanel.add( new DummyImage() );
		verticalPanel.add( new DummyImage() );

		return verticalPanel;
	}

	
	protected void afterCreateWidget() {
		this.getEventListenerDispatcher().addMouseEventListener( new MouseEventAdapter(){
			public void onClick(final MouseClickEvent event) {
				Spinner.this.onMouseClick(event);
			}
		});
		this.setDelta(1);
		this.updateValue(this.getValue());
	}
	
	protected void onMouseClick( final MouseClickEvent event ){
		while( true ){
			final Element target = event.getTarget();
			final Element up = this.getUpImage().getElement();
			if( DOM.isOrHasChild(target, up)){
				this.onUpClick();
				break;
			}
			final Element down = this.getDownImage().getElement();
			if( DOM.isOrHasChild(target, down)){
				this.onDownClick();
				break;
			}
			
			break;
		}
	}

	protected String getInitialStyleName() {
		return WidgetConstants.SPINNER_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.CHANGE | EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.MOUSE_CLICK ;
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

	final int UP_IMAGE_INDEX = 0;
	final int DOWN_IMAGE_INDEX = UP_IMAGE_INDEX + 1;
	
	public Image getUpImage() {
		return this.getImage( UP_IMAGE_INDEX );
	}

	public void setUpImage(final Image upImage) {
		Checker.notNull("parameter:upImage", upImage);
		
		final String style = this.getUpArrowStyle();
		this.setImage( upImage, UP_IMAGE_INDEX, style );
	}

	protected String getUpArrowStyle() {
		return WidgetConstants.SPINNER_UP_STYLE;
	}

	/**
	 * This method is fired whenever the up widget or image is clicked
	 */
	protected void onUpClick() {
		final int value = this.getValue() + this.getDelta();
		this.updateValue(value);
	}

	public Image getDownImage() {
		return this.getImage( DOWN_IMAGE_INDEX );
	}

	public void setDownImage(final Image upImage) {
		Checker.notNull("parameter:upImage", upImage);
		
		final String style = this.getDownArrowStyle();
		this.setImage( upImage, DOWN_IMAGE_INDEX, style );
	}

	protected String getDownArrowStyle() {
		return WidgetConstants.SPINNER_DOWN_STYLE;
	}
	
	protected Image getImage( final int index ){
		final Image image = (Image) this.getVerticalPanel().getWidget( index );
		return image instanceof DummyImage ? null : image;
	}
	
	protected void setImage( final Image image, final int index, final String style ){
		Checker.notNull("parameter:image", image );
		final VerticalPanel verticalPanel = this.getVerticalPanel();
		verticalPanel.remove(index);
		verticalPanel.insert(image, index );
		
		image.addStyleName(style);
	}
	
	/**
	 * This method is fired whenever the down widget is clicked
	 */
	protected void onDownClick() {
		final int value = this.getValue() - this.getDelta();
		this.updateValue(value);
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
				this.onLowerLimitReached();
			}

			final int upperBounds = this.getUpperBounds();
			if (value > upperBounds) {
				value = upperBounds;
				this.onUpperLimitReached();
			}
			break;
		}

		this.setValue(value);
	}

	protected void onUpperLimitReached() {
	}

	protected void onLowerLimitReached() {
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
	
	static class DummyImage extends Image{
		
	}
}