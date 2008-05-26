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
package rocket.style.client;

import rocket.dom.client.Dom;
import rocket.util.client.Checker;
import rocket.util.client.JavaScript;
import rocket.util.client.Utilities;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;

/**
 * Instances of this class may be used to calculate the width of any font. This class works best with fixed fonts, where
 * each character shares a common width/height. 
 * 
 * This class uses a hidden div along with the various font details set upon this instance. Unfortunately not all
 * browsers make the offsetWidth/offsetHeight immediately available requiring the user of a deferred command, after
 * setting properties and calling {@link #calculate()}. 
 * 
 * In cases when {@link #isReady()} returns false after setting the other properties use a deferred command.
 * 
 * @author Miroslav Pokorny
 */
public class FontMetrics {

	public FontMetrics() {
		super();
	}

	public FontMetrics(final String fontFamily, final int fontSize, final String text) {
		super();

		this.setFontFamily(fontFamily);
		this.setFontSize(fontSize);
		this.setText(text);
		this.calculate();
	}

	/**
	 * Schedules the process that will calculate the average width/height for a
	 * single character for the given font.
	 * 
	 * @param text
	 *            This text should not contain any newlines or carriage returns.
	 */
	public void calculate() {
		// needs to be calculated
		if (this.needsCalculating()) {
			this.calculate0();
		}
	}

	protected void calculate0() {
		this.setReady(CALCULATING);

		// create a span set its width /height to 1...
		final Element element = DOM.createSpan();

		InlineStyle.setString(element, Css.DISPLAY, "inline");

		InlineStyle.setInteger(element, Css.MARGIN, 0, CssUnit.PX);
		InlineStyle.setInteger(element, Css.BORDER_WIDTH, 0, CssUnit.PX);
		InlineStyle.setInteger(element, Css.PADDING, 0, CssUnit.PX);

		InlineStyle.setString(element, Css.VISIBILITY, "hidden");
		InlineStyle.setString(element, Css.POSITION, "absolute");

		InlineStyle.setString(element, Css.FONT_FAMILY, this.getFontFamily());
		InlineStyle.setInteger(element, Css.FONT_SIZE, this.getFontSize(), CssUnit.PX);

		InlineStyle.setString(element, Css.WHITE_SPACE, "pre");

		final String text = this.getText();
		DOM.setInnerText(element, text);

		final Element body = Dom.getBody();
		DOM.appendChild(body, element);

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				FontMetrics.this.finishCalculate(element);
			}
		});
	}

	protected void finishCalculate(final Element element) {
		Checker.notNull("parameter:element", element);

		try {
			final String text = this.getText();

			final int charsAcross = text.length();

			final int width = JavaScript.getInteger(element, "offsetWidth");
			final int height = JavaScript.getInteger(element, "offsetHeight");

			final int averageWidth = width / charsAcross;
			final int averageHeight = height;

			this.setWidth(averageWidth);
			this.setHeight(averageHeight);

			this.setReady(READY);
		} finally {
			// dont want element to remain regardless whether or not
			// measurements succeeded.
			Dom.removeFromParent(element);
		}
	}

	private int width;

	public int getWidth() {
		this.checkReady("width");
		return this.width;
	}

	protected void setWidth(final int width) {
		Checker.greaterThan("parameter:width", 0, width);
		this.width = width;
	}

	private int height;

	public int getHeight() {
		this.checkReady("height");
		return this.height;
	}

	protected void setHeight(final int height) {
		Checker.greaterThan("parameter:height", 0, height);
		this.height = height;
	}

	private String fontFamily;

	public String getFontFamily() {
		Checker.notEmpty("field:fontFamily", fontFamily);
		return this.fontFamily;
	}

	public void setFontFamily(final String fontFamily) {
		Checker.notEmpty("parameter:fontFamily", fontFamily);
		this.fontFamily = fontFamily;

		this.setReady(WAITING);
	}

	private int fontSize;

	public int getFontSize() {
		Checker.greaterThan("field:fontSize", 0, fontSize);
		return this.fontSize;
	}

	public void setFontSize(final int fontSize) {
		Checker.greaterThan("parameter:fontSize", 0, fontSize);
		this.fontSize = fontSize;

		this.setReady(WAITING);
	}

	private String text;

	public String getText() {
		Checker.notEmpty("field:text", text);
		return this.text;
	}

	/**
	 * The best text typically has just two lines worth of about 10 characters.
	 * 
	 * @param text
	 */
	public void setText(final String text) {
		Checker.notEmpty("parameter:text", text);
		this.text = text;

		this.setReady(WAITING);
	}

	/**
	 * This flag indicates that the width/height values of this instance are
	 * invalid because a pending calculate has not completed or begin was never
	 * called
	 */
	private final static int WAITING = 0;
	private final static int CALCULATING = WAITING + 1;
	private final static int READY = CALCULATING + 1;

	private int ready;

	public boolean isReady() {
		return READY == this.ready;
	}

	/**
	 * If the status of this FontMetrics instance is waiting and not ready and the calculate method has not been called returns false.
	 * This enables the calculate method to guard against double invocations.
	 * @return
	 */
	protected boolean needsCalculating() {
		return WAITING == this.ready;
	}

	protected void setReady(final int ready) {
		this.ready = ready;
	}

	protected void checkReady(final String property) {
		if (false == this.isReady()) {
			throw new IllegalStateException("The " + property + " cannot be immediately queried after updating any property, use a DeferredCommand to read, this: " + this);
		}
	}

	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append(super.toString());

		buf.append(", fontFamily: ");
		buf.append(Utilities.quotedEscape(this.fontFamily));

		buf.append(", fontSize: ");
		buf.append(this.fontSize);

		buf.append(", text: ");
		buf.append(Utilities.quotedEscape(this.text));

		if (this.isReady()) {
			buf.append(", width: ");
			buf.append(this.width);

			buf.append(", height: ");
			buf.append(this.height);
		}

		return buf.toString();
	}
}
