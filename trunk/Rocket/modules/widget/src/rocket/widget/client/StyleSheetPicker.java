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

import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.style.client.StyleSheet;
import rocket.util.client.Checker;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget presents a series of buttons with the title of available LINKED
 * stylesheets for a web application. For a LINKed stylesheet to be recognized
 * and made a candidate it must have a title, href. Only one LINKed stylesheet
 * should be active with all others disabled.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * FIXME get rid of HorizontalPanel property
 */
public class StyleSheetPicker extends CompositeWidget {

	public StyleSheetPicker() {
		super();
	}

	@Override
	protected void beforeCreateWidget() {
		super.beforeCreateWidget();

		this.setMappings(new HashMap());
	}

	@Override
	protected Widget createWidget() {
		final HorizontalPanel horizontalPanel = this.createHorizontalPanel();
		this.setHorizontalPanel(horizontalPanel);
		return horizontalPanel;
	}

	@Override
	protected void afterCreateWidget() {
		super.afterCreateWidget();

		this.setText(WidgetConstants.STYLESHEET_PICKER_LABEL_TEXT);
	}

	@Override
	protected String getInitialStyleName() {
		return WidgetConstants.STYLESHEET_PICKER_STYLE;
	}

	@Override
	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS;
	}

	public String getText() {
		return this.getLabel().getText();
	}

	public void setText(final String text) {
		Checker.notEmpty("parameter:text", text);
		this.getLabel().setText(text);
	}

	/**
	 * This panel contains the buttons created for each of the found external
	 * stylesheets.
	 */
	private HorizontalPanel horizontalPanel;

	protected HorizontalPanel getHorizontalPanel() {
		Checker.notNull("field:horizontalPanel", horizontalPanel);
		return horizontalPanel;
	}

	protected boolean hasHorizontalPanel() {
		return null != this.horizontalPanel;
	}

	protected void setHorizontalPanel(final HorizontalPanel horizontalPanel) {
		Checker.notNull("parameter:horizontalPanel", horizontalPanel);
		this.horizontalPanel = horizontalPanel;
	}

	protected HorizontalPanel createHorizontalPanel() {
		final HorizontalPanel panel = new HorizontalPanel();

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
		Checker.notNull("field:label", label);
		return label;
	}

	public void setLabel(final Label label) {
		Checker.notNull("parameter:label", label);
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
		label.setStyleName(this.getPickerLabelStyle());
		this.setLabel(label);
		return label;
	}

	protected String getPickerLabelStyle() {
		return WidgetConstants.STYLESHEET_PICKER_LABEL_STYLE;
	}

	protected void createButtons(final HorizontalPanel panel) {
		Checker.notNull("parameter:panel", panel);

		final Collection styleSheets = StyleSheet.getStyleSheets();

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
		Checker.isPositive("parameter:index", index);
		// skip the label widget
		return (Button) this.getHorizontalPanel().getWidget(index + 1);
	}

	public int getButtonCount() {
		// take 1 because we dont want to include the label widget
		return this.getHorizontalPanel().getWidgetCount() - 1;
	}

	/**
	 * This map contains a mapping between Buttons and their corresponding
	 * StyleSheet DOM objects.
	 */
	private Map mappings;

	protected Map getMappings() {
		Checker.notNull("field:mappings", mappings);
		return mappings;
	}

	protected void setMappings(final Map mappings) {
		Checker.notNull("parameter:mappings", mappings);
		this.mappings = mappings;
	}

	public StyleSheet getStyleSheet(final int index) {
		return this.getStyleSheet(this.getButton(index));
	}

	protected StyleSheet getStyleSheet(final Button button) {
		Checker.notNull("parameter:button", button);
		final StyleSheet styleSheet = (StyleSheet) this.getMappings().get(button);
		if (null == styleSheet) {
			Checker.fail("parameter:button", "Unable find the styleSheet for the parameter:button");
		}
		return styleSheet;
	}

	protected void setStyleSheet(final Button button, final StyleSheet styleSheet) {
		Checker.notNull("parameter:button", button);
		Checker.notNull("parameter:styleSheet", styleSheet);

		final Map mappings = this.getMappings();
		if (mappings.containsKey(button)) {
			Checker.fail("parameter:button", "The parameter:button has already been mapped.");
		}
		mappings.put(button, styleSheet);
	}

	protected Button createButton(final String title) {
		Checker.notNull("parameter:title", title);

		final Button button = new Button(title);
		button.setStyleName(WidgetConstants.STYLESHEET_ITEM_STYLE);

		button.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent ignored) {
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
		Checker.notNull("parameter:button", button);

		this.unselectAllStyleSheets();
		button.addStyleName(this.getSelectedStyleSheetStyle());

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
		Checker.notNull("parameter:button", button);

		button.removeStyleName(this.getSelectedStyleSheetStyle());

		final StyleSheet styleSheet = this.getStyleSheet(button);
		styleSheet.setDisabled(true);
	}

	protected String getSelectedStyleSheetStyle() {
		return WidgetConstants.STYLESHEET_ITEM_SELECTED_STYLE;
	}
}