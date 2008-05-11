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

import rocket.event.client.MouseClickEvent;
import rocket.util.client.Checker;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * An extension of a regular spinner that includes two extra controls to assist
 * with modifying the value.
 * 
 * @author Miroslav Pokorny (mP)
 * @deprecated This widget will be removed from future releases. 
 */
public class SuperSpinner extends Spinner {

	public SuperSpinner() {
		super();
	}
	
	protected Widget createWidget() {
		return this.createHorizontalPanel();
	}

	/**
	 * Creates an empty HorizontalPanel which will become the container of all images
	 * 
	 * @return
	 */
	protected HorizontalPanel createHorizontalPanel() {
		final HorizontalPanel panel = new HorizontalPanel();
		
		panel.add( new DummyImage() );
		panel.add( new DummyImage() );
		panel.add( new DummyImage() );
		panel.add( new DummyImage() );
		return panel;
	}
	
	protected HorizontalPanel getHorizontalPanel() {
		return (HorizontalPanel) this.getWidget();
	}
	
	protected String getInitialStyleName() {
		return WidgetConstants.SUPER_SPINNER_STYLE;
	}

	final int BIG_UP_IMAGE_INDEX = DOWN_IMAGE_INDEX + 1;
	final int BIG_DOWN_IMAGE_INDEX = BIG_UP_IMAGE_INDEX + 1;

	public Image getBigUpImage() {
		return this.getImage( BIG_UP_IMAGE_INDEX );
	}

	public void setBigUpImage(final Image bigUpImage) {
		final String style = this.getBigUpArrowStyle();
		this.setImage(bigUpImage, BIG_UP_IMAGE_INDEX, style );
	}

	protected String getBigUpArrowStyle() {
		return WidgetConstants.SUPER_SPINNER_BIG_UP_STYLE;
	}
	
	public Image getBigDownImage() {
		return this.getImage( BIG_DOWN_IMAGE_INDEX );
	}

	public void setBigDownImage(final Image bigDownImage) {
		final String style = this.getBigDownArrowStyle();
		this.setImage(bigDownImage, BIG_DOWN_IMAGE_INDEX, style );
	}

	protected String getBigDownArrowStyle() {
		return WidgetConstants.SUPER_SPINNER_BIG_DOWN_STYLE;
	}

	protected Image getImage( final int index ){
		final Image image = (Image) this.getHorizontalPanel().getWidget( index );
		return image instanceof DummyImage ? null : image;
	}
	
	protected void setImage( final Image image, final int index, final String style ){
		Checker.notNull("parameter:image", image );
	
		final HorizontalPanel horizontalPanel = this.getHorizontalPanel();
		horizontalPanel.remove(index);
		horizontalPanel.insert(image, index );
		
		image.addStyleName(style);
	}

	protected void onMouseClick( final MouseClickEvent event ){
		while( true ){
			final Element target = event.getTarget();
			final Element up = this.getBigUpImage().getElement();
			if( DOM.isOrHasChild(target, up)){
				this.onBigUpClick();
				break;
			}
			final Element down = this.getBigDownImage().getElement();
			if( DOM.isOrHasChild(target, down)){
				this.onBigDownClick();
				break;
			}
			super.onMouseClick(event);
			break;
		}
	}
	
	protected void onBigUpClick() {
		this.updateValue(this.getValue() + this.getBigDelta());
	}

	protected void onBigDownClick() {
		this.updateValue(this.getValue() - this.getBigDelta());
	}

	/**
	 * The amount the value is increased/decreased each time an up or down
	 * button is clicked.
	 */
	private int bigDelta;

	public int getBigDelta() {
		return this.bigDelta;
	}

	public void setBigDelta(final int bigDelta) {
		this.bigDelta = bigDelta;
	}

	public String toString() {
		return super.toString() + ", bigDelta: " + bigDelta;
	}

}