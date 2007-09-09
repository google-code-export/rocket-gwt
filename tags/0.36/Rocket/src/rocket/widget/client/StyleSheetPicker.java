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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rocket.style.client.StyleHelper;
import rocket.style.client.StyleSheet;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget presents a series of buttons with the title of available LINKED
 * stylesheets for a web application. For a LINKed stylesheet to be recognized
 * and made a candidate it must have a title, href. Only one LINKed stylesheet
 * should be active with all others disabled.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleSheetPicker extends Composite {

	public StyleSheetPicker() {
		super();

		this.setText(WidgetConstants.STYLESHEET_PICKER_LABEL_TEXT);
	}

	protected void beforeCreateWidget() {
		this.setMappings(new HashMap());
	}

	protected Widget createWidget() {
		final HorizontalPanel horizontalPanel = this.createHorizontalPanel();
		this.setHorizontalPanel(horizontalPanel);
		return horizontalPanel;
	}

	protected void afterCreateWidget() {
		this.setFocusListeners(this.createFocusListeners());
	}

	protected int getSunkEventsBitMask() {
		return Event.FOCUSEVENTS;
	}

	public String getText() {
		return this.getLabel().getText();
	}

	public void setText(final String text) {
		StringHelper.checkNotEmpty("parameter:text", text);
		this.getLabel().setText(text);
	}

	/**
	 * This panel contains the buttons created for each of the found external
	 * stylesheets.
	 */
	private HorizontalPanel horizontalPanel;

	protected HorizontalPanel getHorizontalPanel() {
		ObjectHelper.checkNotNull("field:horizontalPanel", horizontalPanel);
		return horizontalPanel;
	}

	protected boolean hasHorizontalPanel() {
		return null != this.horizontalPanel;
	}

	protected void setHorizontalPanel(final HorizontalPanel horizontalPanel) {
		ObjectHelper.checkNotNull("parameter:horizontalPanel", horizontalPanel);
		this.horizontalPanel = horizontalPanel;
	}

	protected HorizontalPanel createHorizontalPanel() {
		final HorizontalPanel panel = new HorizontalPanel();
		panel.setStyleName(WidgetConstants.STYLESHEET_PICKER_STYLE);

		final Widget label = this.createLabel();
		panel.add(label);

		this.createButtons(panel);
		this.selectStyleSheet((Button) panel.getWidget(0 + 1));

		return panel;
	}

	/**
	 * The label container for the picker text.
	 */
	private Label label;

	public Label getLabel() {
		ObjectHelper.checkNotNull("field:label", label);
		return label;
	}

	public void setLabel(final Label label) {
		ObjectHelper.checkNotNull("parameter:label", label);
		this.label = label;
	}

	/**
	 * Creates the label that preceeds the available css stylesheet
	 * boxes/buttons
	 * 
	 * @return
	 */
	protected Widget createLabel() {
		final Label label = new Label("");
		label.setStyleName(WidgetConstants.STYLESHEET_PICKER_LABEL_STYLE);
		this.setLabel(label);
		return label;
	}

	protected void createButtons(final HorizontalPanel panel) {
		ObjectHelper.checkNotNull("parameter:panel", panel);

		final Collection styleSheets = StyleHelper.getStyleSheets();

		// loop thru creating a button for each available stylesheet.
		final Iterator iterator = styleSheets.iterator();
		while (iterator.hasNext()) {
			final StyleSheet styleSheet = (StyleSheet) iterator.next();
			if (false == styleSheet.isExternalFile()) {
				continue;
			}

			final Button button = this.createButton(styleSheet.getTitle());
			this.setStyleSheet(button, styleSheet);
			panel.add(button);
			panel.setCellVerticalAlignment(button, HasVerticalAlignment.ALIGN_BOTTOM);
		}
	}

	public Button getButton(final int index) {
		PrimitiveHelper.checkIsPositive("parameter:index", index);
		return (Button) this.getHorizontalPanel().getWidget(index + 1);// skip
		// the
		// first
		// Label
		// widget
	}

	public int getButtonCount() {
		return this.getHorizontalPanel().getWidgetCount() - 1; // less 1
		// because the
		// count
		// shouldnt
		// include the
		// Label widget
	}

	/**
	 * This map contains a mapping between Buttons and their corresponding
	 * StyleSheet DOM objects.
	 */
	private Map mappings;

	protected Map getMappings() {
		ObjectHelper.checkNotNull("field:mappings", mappings);
		return mappings;
	}

	protected void setMappings(final Map mappings) {
		ObjectHelper.checkNotNull("parameter:mappings", mappings);
		this.mappings = mappings;
	}

	public StyleSheet getStyleSheet(final int index) {
		return this.getStyleSheet(this.getButton(index));
	}

	protected StyleSheet getStyleSheet(final Button button) {
		ObjectHelper.checkNotNull("parameter:button", button);
		final StyleSheet styleSheet = (StyleSheet) this.getMappings().get(button);
		if (null == styleSheet) {
			ObjectHelper.fail("parameter:button", "Unable find the styleSheet for the parameter:button");
		}
		return styleSheet;
	}

	protected void setStyleSheet(final Button button, final StyleSheet styleSheet) {
		ObjectHelper.checkNotNull("parameter:button", button);
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);

		final Map mappings = this.getMappings();
		if (mappings.containsKey(button)) {
			ObjectHelper.fail("parameter:button", "The parameter:button has already been mapped.");
		}
		mappings.put(button, styleSheet);
	}

	protected Button createButton(final String title) {
		StringHelper.checkNotNull("parameter:title", title);

		final Button button = new Button(title);
		button.setStyleName(WidgetConstants.STYLESHEET_ITEM_STYLE);

		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				StyleSheetPicker.this.selectStyleSheet(button);
			}
		});

		return button;
	}

	public void selectStyleSheet(final int index) {
		this.selectStyleSheet(this.getButton(index));
	}

	/**
	 * Makes a stylesheet active and also changes its corresponding button to
	 * show this.
	 * 
	 * @param button
	 */
	protected void selectStyleSheet(final Button button) {
		ObjectHelper.checkNotNull("parameter:button", button);

		this.unselectAllStyleSheets();
		button.addStyleName(WidgetConstants.STYLESHEET_ITEM_SELECTED_STYLE);

		final StyleSheet styleSheet = this.getStyleSheet(button);
		styleSheet.setDisabled(false);
	}

	/**
	 * Unselects all stylesheets and deselects their corresponding button
	 */
	protected void unselectAllStyleSheets() {
		final Iterator iterator = this.getMappings().keySet().iterator();
		while (iterator.hasNext()) {
			final Button button = (Button) iterator.next();
			this.unselectStyleSheet(button);
		}
	}

	/**
	 * Unselects the given stylesheet and its corresponding button
	 * 
	 * @param button
	 */
	protected void unselectStyleSheet(final Button button) {
		ObjectHelper.checkNotNull("parameter:button", button);

		button.removeStyleName(WidgetConstants.STYLESHEET_ITEM_SELECTED_STYLE);

		final StyleSheet styleSheet = this.getStyleSheet(button);
		styleSheet.setDisabled(true);
	}

	public String toString() {
		return super.toString() + ", horizontalPanel: " + horizontalPanel + ", mappings: " + mappings;
	}
}