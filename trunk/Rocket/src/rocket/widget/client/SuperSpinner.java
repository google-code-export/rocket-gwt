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
import rocket.event.client.MouseEventAdapter;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * An extension of a regular spinner that includes two extra buttons to assist
 * with modifying the value.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 */
public class SuperSpinner extends Spinner {

	public SuperSpinner() {
		super();
	}

	protected String getInitialStyleName() {
		return WidgetConstants.SUPER_SPINNER_STYLE;
	}

	/**
	 * The left button that when clicked decreases the spinners value.
	 */
	private Image bigDownWidget;

	protected Image getBigDownWidget() {
		ObjectHelper.checkNotNull("field:bigDownWidget", bigDownWidget);
		return this.bigDownWidget;
	}

	protected void setBigDownWidget(final Image bigDownWidget) {
		ObjectHelper.checkNotNull("parameter:bigDown", bigDownWidget);
		this.bigDownWidget = bigDownWidget;
	}

	protected Image createBigDownWidget() {
		final Image image = new Image();
		image.setStyleName(this.getBigDownArrowStyle());

		image.addMouseEventListener(new MouseEventAdapter() {

			public void onClick(final MouseClickEvent event) {
				SuperSpinner.this.onBigDownClick();
			}
		});
		return image;
	}

	protected String getBigDownArrowStyle() {
		return WidgetConstants.SUPER_SPINNER_BIG_DOWN_STYLE;
	}

	protected void onDownClick() {
		this.updateValue(this.getValue() - this.getDelta());
	}

	public String getBigDownImageUrl() {
		return this.getBigDownWidget().getUrl();
	}

	public void setBigDownImageUrl(final String bigDownImageUrl) {
		StringHelper.checkNotEmpty("parameter:bigDownImageUrl", bigDownImageUrl);

		this.getBigDownWidget().setUrl(bigDownImageUrl);
	}

	/**
	 * The right button that when clicked decreases the spinners value.
	 */
	private Image bigUpWidget;

	protected Image getBigUpWidget() {
		ObjectHelper.checkNotNull("field:bigUpWidget", bigUpWidget);
		return this.bigUpWidget;
	}

	protected void setBigUpWidget(final Image bigUpWidget) {
		ObjectHelper.checkNotNull("parameter:bigUpWidget", bigUpWidget);
		this.bigUpWidget = bigUpWidget;
	}

	protected Image createBigUpWidget() {
		final Image image = new Image();
		image.setStyleName(this.getBigUpArrowStyle());

		image.addMouseEventListener(new MouseEventAdapter() {

			public void onClick(final MouseClickEvent event) {
				SuperSpinner.this.onBigUpClick();
			}
		});
		return image;
	}

	protected String getBigUpArrowStyle() {
		return WidgetConstants.SUPER_SPINNER_BIG_UP_STYLE;
	}

	public void onUpClick() {
		this.updateValue(this.getValue() + this.getDelta());
	}

	protected void onBigUpClick() {
		this.updateValue(this.getValue() + this.getBigDelta());
	}

	protected void onBigDownClick() {
		this.updateValue(this.getValue() - this.getBigDelta());
	}

	public String getBigUpImageUrl() {
		return this.getBigUpWidget().getUrl();
	}

	public void setBigUpImageUrl(final String bigUpImageUrl) {
		StringHelper.checkNotEmpty("parameter:bigUpImageUrl", bigUpImageUrl);

		this.getBigUpWidget().setUrl(bigUpImageUrl);
	}

	/**
	 * Creates a new HorizontalPanel and fills it with various clickable widgets
	 * which may be used adjust the value.
	 * 
	 * @return
	 */
	protected Panel createPanel() {
		final HorizontalPanel panel = new HorizontalPanel();

		final Image upWidget = this.createUpWidget();
		this.setUpWidget(upWidget);
		panel.add(upWidget);

		final Image downWidget = this.createDownWidget();
		this.setDownWidget(downWidget);
		panel.add(downWidget);

		final Image bigUpWidget = this.createBigUpWidget();
		this.setBigUpWidget(bigUpWidget);
		panel.add(bigUpWidget);

		final Image bigDownWidget = this.createBigDownWidget();
		this.setBigDownWidget(bigDownWidget);
		panel.add(bigDownWidget);

		return panel;
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
		return super.toString() + ", bigUpWidget: " + bigUpWidget + ", bigDownWidget: " + bigDownWidget + ", bigDelta: " + bigDelta;
	}

}