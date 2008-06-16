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
package rocket.style.test.style.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rocket.style.client.ComputedStyle;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.support.StyleSupportConstants;
import rocket.testing.client.Test;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.TestRunner;
import rocket.testing.client.WebPageTestRunner;
import rocket.util.client.Colour;
import rocket.util.client.StackTrace;
import rocket.util.client.Utilities;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A series of automated tests that changes the styling of an element and occasionally prompts the user to confirm visual changes.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleTest extends WebPageTestRunner implements EntryPoint {

	final int WIDTH = 400;

	final int HEIGHT = 50;

	final String FILTER = "filter";

	final int POSTPONE_DELAY = 10 * 60 * 1000;

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert(StackTrace.asString(caught));
				Window.alert("" + caught);
			}
		});

		final Button button = new Button("Run Tests");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StyleTest.this.executeTests((TestBuilder) GWT.create(TestMethodFinder.class));
			}
		});
		RootPanel.get().add(button);

		button.setFocus(true);
	}

	static interface TestMethodFinder extends TestBuilder {
		/**
		 * @testing-testRunner rocket.style.test.style.client.StyleTest
		 */
		abstract public List buildCandidates();
	}

	/**
	 * @testing-testMethodOrder 0
	 */
	protected void testInlineGetString() {
		final Element element = this.createDivAndAddToDocument();
		final String propertyName = Css.BACKGROUND_COLOR;
		final String propertyValue = Colour.getColour("yellow").toCssColour();
		DOM.setStyleAttribute(element, propertyName, propertyValue);
		
		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(element);
		inlineStyle.setString(Css.WIDTH, WIDTH + "px");
		inlineStyle.setString(Css.HEIGHT, HEIGHT + "px");

		String actualPropertyValue = DOM.getStyleAttribute(element, propertyName);
		actualPropertyValue = Colour.parse(actualPropertyValue).toCssColour();
		final String expectedPropertyValue = propertyValue;
		Test.assertEquals(expectedPropertyValue, actualPropertyValue);
	}

	/**
	 * @testing-testMethodOrder 1
	 */
	protected void testInlineSetString() {
		final Element element = this.createDivAndAddToDocument();
		final String propertyValue = Colour.getColour("aquamarine").toCssColour();

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(element);
		inlineStyle.setString(Css.BACKGROUND_COLOR, propertyValue);
		inlineStyle.setString(Css.WIDTH, WIDTH + "px");
		inlineStyle.setString(Css.HEIGHT, HEIGHT + "px");

		this.scrollIntoView(element);
		TestRunner.postponeCurrentTest(POSTPONE_DELAY);

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				if (false == Window.confirm("Has the background colour of the last element changed to aquamarine ?")) {
					Test.fail("The background colour did not change.");
				}
				TestRunner.finishTest();
			}
		});
	}

	/**
	 * @testing-testMethodOrder 2
	 */
	protected void testComputedGetString() {
		final Element element = this.createDivAndAddToDocument();
		final String propertyName = Css.BACKGROUND_COLOR;
		final String propertyValue = Colour.getColour("yellow").toCssColour();
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		String actualPropertyValue = computedStyle.getString(propertyName);
		actualPropertyValue = Colour.parse(actualPropertyValue).toCssColour();
		final String expectedPropertyValue = propertyValue;
		Test.assertEquals(expectedPropertyValue, actualPropertyValue);
	}

	/**
	 * @testing-testMethodOrder 3
	 */
	protected void testInlineGetOpacity() {
		final Element containerElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(containerElement, Css.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, Css.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(containerElement, Css.BACKGROUND_COLOR, "yellow");

		final Element childElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(childElement, Css.POSITION, "relative");
		DOM.setStyleAttribute(childElement, Css.LEFT, -WIDTH / 2 + "px");
		DOM.setStyleAttribute(childElement, Css.TOP, -HEIGHT / 2 + "px");
		DOM.setStyleAttribute(childElement, Css.Z_INDEX, "1");
		DOM.setStyleAttribute(childElement, Css.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, Css.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(childElement, Css.BACKGROUND_COLOR, "blue");

		final float opacity = 0.5f;
		DOM.setStyleAttribute(childElement, FILTER, "alpha(opacity=" + (int) (opacity * 100) + ")");
		DOM.setStyleAttribute(childElement, Css.OPACITY, "" + opacity);

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(childElement);
		final String actualOpacity = inlineStyle.getString(Css.OPACITY);
		final String expectedOpacity = "" + opacity;
		Test.assertEquals("actualOpacity: " + actualOpacity + ", expectedOpacity: " + expectedOpacity, Double
				.parseDouble(expectedOpacity), Double.parseDouble(actualOpacity), 0.5);
	}

	/**
	 * @testing-testMethodOrder 4
	 */
	protected void testComputedGetOpacity() {
		final Element containerElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(containerElement, Css.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, Css.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(containerElement, Css.BACKGROUND_COLOR, "yellow");

		final Element childElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(childElement, Css.POSITION, "relative");
		DOM.setStyleAttribute(childElement, Css.LEFT, -WIDTH / 2 + "px");
		DOM.setStyleAttribute(childElement, Css.TOP, -HEIGHT / 2 + "px");
		DOM.setStyleAttribute(childElement, Css.Z_INDEX, "1");
		DOM.setStyleAttribute(childElement, Css.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, Css.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(childElement, Css.BACKGROUND_COLOR, "blue");

		final float opacity = 0.5f;
		DOM.setStyleAttribute(childElement, FILTER, "alpha(opacity=" + (int) (opacity * 100) + ")");
		DOM.setStyleAttribute(childElement, Css.OPACITY, "" + opacity);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(childElement);
		final String actualOpacity = computedStyle.getString(Css.OPACITY);
		final String expectedOpacity = "" + opacity;
		Test.assertEquals("actualOpacity: " + actualOpacity + ", expectedOpacity: " + expectedOpacity, Double
				.parseDouble(expectedOpacity), Double.parseDouble(actualOpacity), 0.5);
	}

	/**
	 * @testing-testMethodOrder 5
	 */
	protected void testInlineSetOpacity() {
		final Element containerElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(containerElement, Css.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, Css.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(containerElement, Css.BACKGROUND_COLOR, "yellow");

		final Element childElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(childElement, Css.POSITION, "relative");
		DOM.setStyleAttribute(childElement, Css.LEFT, -WIDTH / 2 + "px");
		DOM.setStyleAttribute(childElement, Css.TOP, -HEIGHT / 2 + "px");
		DOM.setStyleAttribute(childElement, Css.Z_INDEX, "1");
		DOM.setStyleAttribute(childElement, Css.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, Css.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(childElement, Css.BACKGROUND_COLOR, "blue");

		this.scrollIntoView(childElement);
		TestRunner.postponeCurrentTest(POSTPONE_DELAY);

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				if (false == Window.confirm("Does a blue rectangle overlap a yellow background ?")) {
					Test.fail("The blue rectangle is not blue.");
				}

				final float opacity = 0.5f;
				final String propertyName = Css.OPACITY;
				final String propertyValue = "" + opacity;
				
				final InlineStyle inlineStyle = InlineStyle.getInlineStyle(childElement);
				inlineStyle.setString(propertyName, propertyValue);

				if (false == Window.confirm("Is the rectangle that was blue now green where it overlaps the yellow background ?")) {
					Test.fail("The rectangle overlaying the yellow rectangle is not green.");
				}

				TestRunner.finishTest();
			}
		});
	}

	/**
	 * @testing-testMethodOrder 6
	 */
	protected void testComputedGetWidthWhereDivHasInlineWidthAndNoBorderOrPadding() {
		final int borderLeftWidth = 0;
		final int borderRightWidth = 0;
		final int paddingLeft = 0;
		final int paddingRight = 0;

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(element, Css.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(element, Css.BORDER_WIDTH, "0px");
		DOM.setStyleAttribute(element, Css.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
		DOM.setStyleAttribute(element, Css.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
		DOM.setStyleAttribute(element, Css.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(element, Css.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(element, Css.PADDING, "0px");
		DOM.setStyleAttribute(element, Css.PADDING_LEFT, paddingLeft + "px");
		DOM.setStyleAttribute(element, Css.PADDING_RIGHT, paddingRight + "px");
		DOM.setStyleAttribute(element, Css.MARGIN, "0px");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actualContentWidth = computedStyle.getString(Css.WIDTH);
		final String expectedContentWidth = (WIDTH - borderLeftWidth - borderRightWidth - paddingLeft - paddingRight) + "px";
		Test.assertEquals(expectedContentWidth, actualContentWidth);
	}

	/**
	 * @testing-testMethodOrder 7
	 */
	protected void testComputedGetWidthWhereDivInheritsBorderPaddingWidthFromParent0() {
		final int borderLeftWidth = 0;
		final int borderRightWidth = 0;
		final int paddingLeft = 0;
		final int paddingRight = 0;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setInnerHTML(parent, "&nbsp;");
		DOM.setStyleAttribute(parent, Css.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, Css.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, Css.PADDING_LEFT, paddingLeft + "px");
		DOM.setStyleAttribute(parent, Css.PADDING_RIGHT, paddingRight + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_BOTTOM, 25 + "px");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, Css.BACKGROUND_COLOR, "lightGreen");
		parent.appendChild(child);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(child);
		final String actualContentWidth = computedStyle.getString(Css.WIDTH);
		final String expectedContentWidth = WIDTH + "px";
		Test.assertEquals(expectedContentWidth, actualContentWidth);
	}

	/**
	 * @testing-testMethodOrder 8
	 */
	protected void testComputedGetWidthWhereDivInheritsBorderPaddingWidthFromParent1() {
		final int borderLeftWidth = 11;
		final int borderRightWidth = 12;
		final int paddingLeft = 13;
		final int paddingRight = 14;
		final int marginLeft = 15;
		final int marginRight = 16;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setInnerHTML(parent, "&nbsp;");
		DOM.setStyleAttribute(parent, Css.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, Css.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, Css.PADDING_LEFT, paddingLeft + "px");
		DOM.setStyleAttribute(parent, Css.PADDING_RIGHT, paddingRight + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_LEFT, marginLeft + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_RIGHT, marginRight + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_BOTTOM, 25 + "px");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, Css.BACKGROUND_COLOR, "lightGreen");
		parent.appendChild(child);
		child.setInnerHTML("&nbsp;");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(child);
		final String actualContentWidth = computedStyle.getString(Css.WIDTH);
		final String expectedContentWidth = WIDTH + "px";
		Test.assertEquals(expectedContentWidth, actualContentWidth);
	}

	/**
	 * @testing-testMethodOrder 9
	 */
	protected void testComputedGetWidthWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent() {
		final int borderLeftWidth = 11;
		final int borderRightWidth = 12;
		final int paddingLeft = 13;
		final int paddingRight = 14;
		final int marginLeft = 15;
		final int marginRight = 16;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(parent, Css.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, Css.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, Css.PADDING_LEFT, paddingLeft + "px");
		DOM.setStyleAttribute(parent, Css.PADDING_RIGHT, paddingRight + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_LEFT, marginLeft + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_RIGHT, marginRight + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_BOTTOM, 25 + "px");
		DOM.setStyleAttribute(parent, Css.OVERFLOW, "scroll");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, Css.BACKGROUND_COLOR, "lightGreen");
		parent.appendChild(child);
		child.setInnerHTML("CHILDtestComputedGetWidthWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(child);
		final String actualContentWidth = computedStyle.getString(Css.WIDTH);
		final String expectedContentWidth = WIDTH + "px";
		Test.assertEquals(expectedContentWidth, actualContentWidth);
	}

	/**
	 * @testing-testMethodOrder 10
	 */
	protected void testComputedGetHeightWhereDivHasInlineHeightAndNoBorderOrPadding() {
		final int borderTopWidth = 0;
		final int borderBottomWidth = 0;
		final int paddingTop = 0;
		final int paddingBottom = 0;

		final Element element = this.createDivAndAddToDocument();
		DOM.setInnerHTML(element, "");
		DOM.setStyleAttribute(element, Css.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(element, Css.BORDER_TOP_WIDTH, borderTopWidth + "px");
		DOM.setStyleAttribute(element, Css.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
		DOM.setStyleAttribute(element, Css.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(element, Css.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(element, Css.PADDING_LEFT, paddingTop + "px");
		DOM.setStyleAttribute(element, Css.PADDING_RIGHT, paddingBottom + "px");
		DOM.setStyleAttribute(element, Css.MARGIN_BOTTOM, 25 + "px");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actualContentHeight = computedStyle.getString(Css.HEIGHT);
		final String expectedContentHeight = (HEIGHT - borderTopWidth - borderBottomWidth - paddingTop - paddingBottom) + "px";
		Test.assertEquals(expectedContentHeight, actualContentHeight);
	}

	/**
	 * @testing-testMethodOrder 11
	 */
	protected void testComputedGetHeightWhereDivInheritsBorderPaddingHeightFromParent0() {
		final int height = 100;
		final int borderTopWidth = 0;
		final int borderBottomWidth = 0;
		final int paddingTop = 0;
		final int paddingBottom = 0;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setInnerHTML(parent, "");
		DOM.setStyleAttribute(parent, Css.HEIGHT, height + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_TOP_WIDTH, borderTopWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, Css.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, Css.PADDING_TOP, paddingTop + "px");
		DOM.setStyleAttribute(parent, Css.PADDING_BOTTOM, paddingBottom + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN, "0px");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, Css.BACKGROUND_COLOR, "lightGreen");
		DOM.setStyleAttribute(child, Css.MARGIN, "0px");
		DOM.setStyleAttribute(child, Css.BORDER_WIDTH, "0px");
		DOM.setStyleAttribute(child, Css.PADDING, "0px");
		DOM.setStyleAttribute(child, Css.HEIGHT, "100%");
		parent.appendChild(child);
		child.setInnerHTML("CHILD");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(child);
		final String actualContentHeight = computedStyle.getString(Css.HEIGHT);
		final String expectedContentHeight = height + "px";
		Test.assertEquals(expectedContentHeight, actualContentHeight);
	}

	/**
	 * @testing-testMethodOrder 12
	 */
	protected void testComputedGetHeightWhereDivInheritsBorderPaddingHeightFromParent1() {
		final int height = 100;
		final int borderTopWidth = 11;
		final int borderBottomWidth = 12;
		final int paddingTop = 13;
		final int paddingBottom = 14;
		final int marginTop = 15;
		final int marginBottom = 16;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setInnerHTML(parent, "");
		DOM.setStyleAttribute(parent, Css.HEIGHT, height + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_TOP_WIDTH, borderTopWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, Css.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, Css.PADDING_TOP, paddingTop + "px");
		DOM.setStyleAttribute(parent, Css.PADDING_BOTTOM, paddingBottom + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_TOP, marginTop + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_BOTTOM, marginBottom + "px");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, Css.BACKGROUND_COLOR, "lightGreen");
		DOM.setStyleAttribute(child, Css.MARGIN, "0px");
		DOM.setStyleAttribute(child, Css.BORDER_WIDTH, "0px");
		DOM.setStyleAttribute(child, Css.PADDING, "0px");
		DOM.setStyleAttribute(child, Css.HEIGHT, "100%");
		parent.appendChild(child);
		child.setInnerHTML("CHILD");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(child);
		final String actualContentHeight = computedStyle.getString(Css.HEIGHT);
		final String expectedContentHeight = height + "px";
		Test.assertEquals(expectedContentHeight, actualContentHeight);
	}

	/**
	 * @testing-testMethodOrder 13
	 */
	protected void testComputedGetHeightWhereDivHasScrollBarsAndInheritsBorderPaddingHeightFromParent() {
		final int borderTopWidth = 11;
		final int borderBottomWidth = 12;
		final int paddingTop = 13;
		final int paddingBottom = 14;
		final int marginLeft = 15;
		final int marginRight = 16;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setInnerHTML(parent, "");
		DOM.setStyleAttribute(parent, Css.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_TOP_WIDTH, borderTopWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
		DOM.setStyleAttribute(parent, Css.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, Css.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, Css.PADDING_LEFT, paddingTop + "px");
		DOM.setStyleAttribute(parent, Css.PADDING_RIGHT, paddingBottom + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_LEFT, marginLeft + "px");
		DOM.setStyleAttribute(parent, Css.MARGIN_RIGHT, marginRight + "px");
		DOM.setStyleAttribute(parent, Css.OVERFLOW, "scroll");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, Css.BACKGROUND_COLOR, "lightGreen");
		parent.appendChild(child);
		child.setInnerHTML("CHILDtestComputedGetHeightWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(child);
		final String actualContentHeight = computedStyle.getString(Css.HEIGHT);
		final String expectedContentHeight = HEIGHT + "px";
		Test.assertEquals(expectedContentHeight, actualContentHeight);
	}

	/**
	 * @testing-testMethodOrder 14
	 */
	public void testInlineSetBackgroundImage() {
		final String propertyName = Css.BACKGROUND_IMAGE;
		final String propertyValue = "image.gif";

		final Element element = this.createDivAndAddToDocument();

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(element);
		inlineStyle.setString(propertyName, "url('" + propertyValue + "')");

		final String expected = propertyValue;
		final String actual = DOM.getStyleAttribute(element, propertyName);
		Test.assertTrue("actual\"" + actual + "\", expected\"" + expected + "\".", actual.indexOf(expected) != -1);
	}

	/**
	 * @testing-testMethodOrder 15
	 */
	public void testInlineSetBackgroundImageWithElementAlsoContainingABackgroundColour() {
		final String propertyName = Css.BACKGROUND_IMAGE;
		final String propertyValue = "image.gif";
		final Colour colour = Colour.getColour("red");

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.BACKGROUND_COLOR, colour.toCssColour());

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(element);
		inlineStyle.setString(propertyName, "url('" + propertyValue + "')");

		final String expected = propertyValue;
		final String actual = DOM.getStyleAttribute(element, propertyName);
		Test.assertTrue("actual \"" + actual + "\" expected\"" + expected + "\".", actual.indexOf(expected) != -1);

		final String backgroundColour = inlineStyle.getString(Css.BACKGROUND_COLOR);
		final String expectedBackgroundColour = colour.toCssColour();
		Test.assertEquals("backgroundColor", Colour.parse(expectedBackgroundColour), Colour.parse(backgroundColour));

		final Colour actualColour = Colour.parse(backgroundColour);
		final Colour expectedColour = colour;
		Test.assertEquals(expectedColour, actualColour);
	}

	/**
	 * @testing-testMethodOrder 16
	 */
	public void testComputedGetBackgroundPositionWhenNoValueIsSet() {
		final String propertyName = Css.BACKGROUND_POSITION;

		final Element element = this.createDivAndAddToDocument();

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		Test.assertTrue("" + element + ", actual\"" + actual + "\".", actual == null || actual.equals("0% 0%")
				|| actual.equals("left left") || actual.equals("0px 0px"));
	}

	/**
	 * @testing-testMethodOrder 17
	 */
	public void testComputedGetBackgroundPosition() {
		final String propertyName = Css.BACKGROUND_POSITION;
		final String propertyValue = "0px 0px";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.BACKGROUND, "url('image.gif')");
		DOM.setStyleAttribute(element, Css.BACKGROUND_IMAGE, "url('image.gif')");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		final String expected = propertyValue;
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 18
	 */
	public void testComputedGetBackgroundPositionWithElementThatIncludesAllTheOtherBackgroundProperties() {
		final String propertyName = Css.BACKGROUND_POSITION;
		final String propertyValue = "0px 0px";

		final Element element = this.createDivAndAddToDocument();
		final String backgroundProperty = "url('image.gif') no-repeat fixed #123456 " + propertyValue;
		DOM.setStyleAttribute(element, Css.BACKGROUND, backgroundProperty);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		final String expected = propertyValue;
		Test.assertEquals(backgroundProperty, expected, actual);
	}

	/**
	 * @testing-testMethodOrder 19
	 */
	public void testComputedGetFontSizeSetToXSmallValue() {
		final String propertyName = Css.FONT_SIZE;
		final String propertyValue = "x-small";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		final String expected = "10px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 20
	 */
	public void testComputedGetFontSizeSetToSmallValue() {
		final String propertyName = Css.FONT_SIZE;
		final String propertyValue = "small";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		final String expected = "13px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 21
	 */
	public void testComputedGetFontSizeSetToMediumValue() {
		final String propertyName = Css.FONT_SIZE;
		final String propertyValue = "medium";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		final String expected = "16px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 22
	 */
	public void testComputedGetFontSizeSetToLargeValue() {
		final String propertyName = Css.FONT_SIZE;
		final String propertyValue = "large";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		final String expected = "18px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 23
	 */
	public void testComputedGetFontSizeSetToXLargeValue() {
		final String propertyName = Css.FONT_SIZE;
		final String propertyValue = "x-large";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		final String expected = "24px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 24
	 */
	public void testComputedGetFontSizeSetToXXLargeValue() {
		final String propertyName = Css.FONT_SIZE;
		final String propertyValue = "xx-large";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		final String expected = "32px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 25
	 */
	public void testComputedGetFontSizeSetToSmaller() {
		final String propertyName = Css.FONT_SIZE;
		final String propertyValue = "smaller";
		final int parentFontSize = 13;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(parent, Css.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(parent, propertyName, parentFontSize + "px");

		final Element child = DOM.createSpan();
		DOM.setStyleAttribute(child, Css.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(child, propertyName, propertyValue);
		parent.appendChild(child);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(child);
		final String actualString = computedStyle.getString(propertyName);
		Test.assertNotNull(actualString);

		final float expected = Math.round(parentFontSize * StyleSupportConstants.SMALLER_SCALING_FACTOR);
		TestRunner.log("actual\"" + actualString + "\", expected\"" + expected + "\".");

		final float actual = Math.round(Double.parseDouble(actualString.substring(0, actualString.length() - 2)));
		Test.assertTrue("actual\"" + actual + "\" expected\"" + expected + "\".", actual < parentFontSize);
		Test.assertEquals("actual\"" + actual + "\" expected\"" + expected + "\".", expected, actual, 2.5f);
	}

	/**
	 * @testing-testMethodOrder 26
	 */
	public void testComputedGetFontSizeSetToLarger() {
		final String propertyName = Css.FONT_SIZE;
		final String propertyValue = "larger";
		final int parentFontSize = 13;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(parent, Css.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(parent, propertyName, parentFontSize + "px");

		final Element child = DOM.createSpan();
		DOM.setStyleAttribute(child, Css.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(child, propertyName, propertyValue);
		parent.appendChild(child);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(child);
		final String actualString = computedStyle.getString(propertyName);
		Test.assertNotNull(actualString);

		final float expected = Math.round(parentFontSize * StyleSupportConstants.LARGER_SCALING_FACTOR);
		TestRunner.log("actual\"" + actualString + "\", expected\"" + expected + "\".");

		final float actual = Math.round(Double.parseDouble(actualString.substring(0, actualString.length() - 2)));
		Test.assertTrue("actual\"" + actual + "\" expected\"" + expected + "\".", actual > parentFontSize);
		Test.assertEquals("actual\"" + actual + "\" expected\"" + expected + "\".", expected, actual, 2.5f);
	}

	/**
	 * @testing-testMethodOrder 27
	 */
	public void testComputedGetFontWeightWithMissingPropertyValue() {
		final String propertyName = Css.FONT_WEIGHT;

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.FONT_FAMILY, "Verdana");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		Test.assertEquals("" + element, "400", actual);
	}

	/**
	 * @testing-testMethodOrder 28
	 */
	public void testComputedGetFontWeightWithNumberPropertyValue() {
		final String propertyName = Css.FONT_WEIGHT;

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.FONT_FAMILY, "Verdana");
		DOM.setIntStyleAttribute(element, propertyName, 700);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		Test.assertEquals("" + element, "700", actual);
	}

	/**
	 * @testing-testMethodOrder 29
	 */
	public void testComputedGetFontWeightSetToNormal() {
		final String propertyName = Css.FONT_WEIGHT;

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, "normal");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		Test.assertEquals("" + element, "400", actual);
	}

	/**
	 * @testing-testMethodOrder 30
	 */
	public void testComputedGetFontWeightSetToBold() {
		final String propertyName = Css.FONT_WEIGHT;

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, "bold");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		Test.assertTrue("fontWeight should be greater than 400 for " + element + " and not " + actual, Integer.parseInt(actual) > 400);
	}

	/**
	 * @testing-testMethodOrder 31
	 */
	public void testComputedGetFontWeightSetToLighter() {
		final String propertyName = Css.FONT_WEIGHT;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(parent, Css.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(parent, Css.FONT_SIZE, "20pt");
		DOM.setStyleAttribute(parent, propertyName, "bold");

		final Element child = DOM.createSpan();
		child.setInnerHTML("CHILD");
		DOM.setStyleAttribute(child, Css.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(child, Css.FONT_SIZE, "20pt");
		DOM.setStyleAttribute(child, propertyName, "lighter");
		parent.appendChild(child);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(child);
		final String actual = computedStyle.getString(propertyName);
		Test.assertTrue("fontWeight should be less or equal to 400 for " + child + " and not " + actual, Integer.parseInt(actual) <= 400);
	}

	/**
	 * @testing-testMethodOrder 32
	 */
	public void testComputedGetFontWeightSetToBolder() {
		final String propertyName = Css.FONT_WEIGHT;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(parent, Css.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(parent, Css.FONT_SIZE, "20pt");
		DOM.setStyleAttribute(parent, propertyName, "normal");

		final Element child = DOM.createSpan();
		child.setInnerHTML("CHILD");
		DOM.setStyleAttribute(child, Css.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(child, Css.FONT_SIZE, "20pt");
		DOM.setStyleAttribute(child, propertyName, "bolder");
		parent.appendChild(child);

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(child);
		final String actualString = computedStyle.getString(propertyName);
		Test.assertNotNull(actualString);

		final String actual = computedStyle.getString( propertyName);
		Test.assertEquals("" + child, "700", actual);
	}

	/**
	 * @testing-testMethodOrder 33
	 */
	public void testComputedGetBorderWidthThin() {
		final String propertyName = Css.BORDER_LEFT_WIDTH;
		final String propertyValue = "thin";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, propertyName, propertyValue);
		DOM.setStyleAttribute(element, Css.BORDER_LEFT_COLOR, "black");
		DOM.setStyleAttribute(element, Css.BORDER_LEFT_STYLE, "solid");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		Test.assertNotNull(actual);
		TestRunner.log(actual);
		final int number = Integer.parseInt(actual.substring(0, actual.length() - 2));
		Test.assertTrue("actual\"" + actual + "\".", number == StyleSupportConstants.BORDER_WIDTH_THIN_PX
				|| number == StyleSupportConstants.BORDER_WIDTH_THIN_PX_IE6);
	}

	/**
	 * @testing-testMethodOrder 34
	 */
	public void testComputedGetBorderWidthMedium() {
		final String propertyName = Css.BORDER_LEFT_WIDTH;
		final String propertyValue = "medium";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, propertyName, propertyValue);
		DOM.setStyleAttribute(element, Css.BORDER_LEFT_COLOR, "black");
		DOM.setStyleAttribute(element, Css.BORDER_LEFT_STYLE, "solid");

		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		Test.assertNotNull(actual);
		final int number = Integer.parseInt(actual.substring(0, actual.length() - 2));
		Test.assertTrue("actual\"" + actual + "\".", number == StyleSupportConstants.BORDER_WIDTH_MEDIUM_PX
				|| number == StyleSupportConstants.BORDER_WIDTH_MEDIUM_PX_IE6);
	}

	/**
	 * @testing-testMethodOrder 35
	 */
	public void testComputedGetBorderWidthThick() {
		final String propertyName = Css.BORDER_LEFT_WIDTH;
		final String propertyValue = "thick";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, propertyName, propertyValue);
		DOM.setStyleAttribute(element, Css.BORDER_LEFT_COLOR, "black");
		DOM.setStyleAttribute(element, Css.BORDER_LEFT_STYLE, "solid");
		
		final ComputedStyle computedStyle = ComputedStyle.getComputedStyle(element);
		final String actual = computedStyle.getString(propertyName);
		Test.assertNotNull(actual);
		final int number = Integer.parseInt(actual.substring(0, actual.length() - 2));
		Test.assertTrue("actual\"" + actual + "\".", number == StyleSupportConstants.BORDER_WIDTH_THICK_PX
				|| number == StyleSupportConstants.BORDER_WIDTH_THICK_PX_IE6);
	}

	/**
	 * @testing-testMethodOrder 36
	 */
	public void testComputedGetStylePropertyNames() {
		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, Css.CURSOR, "move");
		DOM.setStyleAttribute(element, Utilities.toCamelCase(Css.BACKGROUND_COLOR), "aquamarine");

		final String[] propertyNames = ComputedStyle.getComputedStyle(element).getNames();
		Test.assertNotNull(propertyNames);

		final int length = propertyNames.length;
		Test.assertTrue("length: " + length, length >= 2);

		final List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(propertyNames));

		Test.assertTrue(Css.CURSOR + ", list: " + list, list.contains(Css.CURSOR));
		Test.assertTrue(Css.BACKGROUND_COLOR + ", list: " + list, list.contains(Utilities.toCamelCase(Css.BACKGROUND_COLOR)));
	}

	/**
	 * @testing-testMethodOrder 37
	 */
	public void testInlineSetUserSelectionTextSelectionDisabled() {
		final Element element = RootPanel.getBodyElement();
		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(element);
		inlineStyle.setString(Css.USER_SELECT, Css.USER_SELECT_DISABLED);

		// ask the user to attempt to select some text ?
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				button.removeFromParent();

				final boolean passed = Window.confirm("Was it impossible to select text anywhere within the document ?");
				if (false == passed) {
					Test
							.fail("User confirmed that text selection was still possible even though selection had been disabled for the document.");
				}

				StyleTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("Text selection has been disabled, try and select text anywhere and then click on CONTINUE...");
		StyleTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 38
	 */
	public void testInlineSetUserSelectionTextSelectionEnabled() {
		final Element element = RootPanel.getBodyElement();
		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(element);
		inlineStyle.setString(Css.USER_SELECT, Css.USER_SELECT_ENABLED);

		// ask the user to attempt to select some text ?
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				button.removeFromParent();

				final boolean passed = Window.confirm("Was it possible to select text anywhere within the document ?");
				if (false == passed) {
					Test
							.fail("User confirmed that text selection was NOT still possible even though selection had been enabled for the document.");
				}
				StyleTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("Text selection has been enabled, try and select text anywhere and then click on CONTINUE...");
		StyleTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 39
	 */
	public void testInlineGetUserSelection() {
		final Element element = RootPanel.getBodyElement();
		final String propertyName = Css.USER_SELECT;

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(element);
		inlineStyle.setString(propertyName, ""); // enable
		final String value0 = inlineStyle.getString(propertyName);
		Test.assertFalse("selection should be enabled. \"" + value0 + "\".", "none".equals(value0));

		inlineStyle.setString(propertyName, "none"); // disable
		final String value1 = inlineStyle.getString(propertyName);
		Test.assertEquals("selection should be disabled. \"" + value1 + "\".", "none", value1);

		inlineStyle.setString(propertyName, ""); // enable
		final String value2 = inlineStyle.getString(propertyName);
		Test.assertFalse("selection should be enabled. \"" + value2 + "\".", "none".equals(value2));

		inlineStyle.setString(propertyName, "none"); // disable
		final String value3 = inlineStyle.getString( propertyName);
		Test.assertEquals("selection should be disabled. \"" + value3 + "\".", "none", value3);
	}

	/**
	 * @testing-testMethodOrder 40
	 */
	public void testComputedGetUserSelection() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.getChild(RootPanel.getBodyElement(), 0);
		final String propertyName = Css.USER_SELECT;

		final InlineStyle parentInlineStyle = InlineStyle.getInlineStyle(parent);
		final ComputedStyle childComputedStyle = ComputedStyle.getComputedStyle(child);
		
		parentInlineStyle.setString(propertyName, ""); // enable
		final String value0 = childComputedStyle.getString(propertyName);
		Test.assertNull("selection should be enabled. \"" + value0 + "\".", value0);

		parentInlineStyle.setString(propertyName, "none"); // disable
		final String value1 = childComputedStyle.getString(propertyName);
		Test.assertNotNull("selection should be disabled. \"" + value1 + "\".", value1);

		parentInlineStyle.setString(propertyName, ""); // enable
		final String value2 = childComputedStyle.getString(propertyName);
		Test.assertNull("selection should be enabled. \"" + value2 + "\".", value2);

		parentInlineStyle.setString(propertyName, "none"); // disable
		final String value3 = childComputedStyle.getString(propertyName);
		Test.assertNotNull("selection should be disabled. \"" + value3 + "\".", value3);
	}

	/**
	 * @testing-testMethodOrder 41
	 */
	public void testSetAbsolutePositionLeftTop() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testSetAbsolutePositionLeftTop");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(child);
		inlineStyle.setString( Css.BACKGROUND_COLOR, "orange");
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		inlineStyle.setString( Css.LEFT, "12px");
		inlineStyle.setString( Css.TOP, "34px");
		inlineStyle.setString( Css.POSITION, "absolute");

		parent.appendChild(child);
		child.scrollIntoView();

		// ask the user to scroll and verify child is always correctly
		// positioned...
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				button.removeFromParent();

				final boolean passed = Window.confirm("Did the absolute positioned orange element remain stuck to the top/left corner of the client window when scrolled ?");
				if (false == passed) {
					Test.fail("User confirmed that absolute position element was not stuck to the top/left corner of the client window when it was scrolled.");
				}
				StyleTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("An element with a orange background has been absolutely positioned to the top/left, try scrolling and then click on CONTINUE...");
		StyleTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 42
	 */
	public void testGetAbsolutePositionLeftTop() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testGetAbsolutePositionLeftTop");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(child);
		inlineStyle.setString(Css.LEFT, "123px");
		inlineStyle.setString(Css.TOP, "45px");
		inlineStyle.setString(Css.POSITION, "absolute");
		inlineStyle.setString(Css.BACKGROUND_COLOR, "red");
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);

		parent.appendChild(child);

		final int left = inlineStyle.getInteger(Css.LEFT, CssUnit.PX, 0);
		Test.assertEquals("" + child, 123, left);

		final int top = inlineStyle.getInteger(Css.TOP, CssUnit.PX, 0);
		Test.assertEquals("" + child, 45, top);

		final String position = inlineStyle.getString(Css.POSITION);
		Test.assertEquals("" + child, "absolute", position);
	}

	/**
	 * @testing-testMethodOrder 43
	 */
	public void testSetAbsolutePositionRightBottom() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testSetAbsolutePositionRightBottom");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle( child );
		inlineStyle.setString( Css.RIGHT, "123px");
		inlineStyle.setString( Css.BOTTOM, "56px");
		inlineStyle.setString( Css.POSITION, "absolute");
		inlineStyle.setString( Css.BACKGROUND_COLOR, "green");
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);

		parent.appendChild(child);

		child.scrollIntoView();

		// ask the user to scroll and verify child is always correctly
		// positioned...
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignoyellow) {
				button.removeFromParent();

				final boolean passed = Window.confirm("Did the absolute positioned green element remain stuck to the bottom/right corner of the client window when scrolled ?");
				if (false == passed) {
					Test.fail("User confirmed that absolute position element was not stuck to the bottom/right corner of the client window when it was scrolled.");
				}
				StyleTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("An element with a green background has been absolutely positioned to the bottom/right, try scrolling and then click on CONTINUE...");
		StyleTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 44
	 */
	public void testGetAbsolutePositionRightBottom() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testGetAbsolutePositionRightBottom");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle( child );
		inlineStyle.setString(Css.RIGHT, "123px");
		inlineStyle.setString(Css.BOTTOM, "56px");
		inlineStyle.setString(Css.POSITION, "absolute");
		inlineStyle.setString(Css.BACKGROUND_COLOR, "yellow");
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);

		parent.appendChild(child);

		final int right = inlineStyle.getInteger(Css.RIGHT, CssUnit.PX, 0);
		Test.assertEquals("" + child, 123, right);

		final int bottom = inlineStyle.getInteger(Css.BOTTOM, CssUnit.PX, 0);
		Test.assertEquals("" + child, 56, bottom);

		final String position = inlineStyle.getString(Css.POSITION);
		Test.assertEquals("" + child, "absolute", position);
	}

	/**
	 * @testing-testMethodOrder 45
	 */
	public void testSetFixedPositionLeftTop() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testSetFixedPositionLeftTop");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(child);
		inlineStyle.setString( Css.COLOR, "blue");
		inlineStyle.setString( Css.LEFT, "12px");
		inlineStyle.setString( Css.TOP, "34px");
		inlineStyle.setString( Css.POSITION, "fixed");

		parent.appendChild(child);

		inlineStyle.setString( Css.BACKGROUND_COLOR, "red");
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);

		child.scrollIntoView();

		// ask the user to scroll and verify child is always correctly
		// positioned...
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				button.removeFromParent();

				final boolean passed = Window
						.confirm("Did the red element remain fixed to the top/left corner of the client window when it was scrolled?");
				if (false == passed) {
					Test
							.fail("User confirmed that fixed position element did not remain fixed to the top/left corner of the client window when it was scrolled.");
				}
				StyleTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("An element with a red background has been fixed to the top/left, try scrolling and then click on CONTINUE...");
		StyleTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 46
	 */
	public void testGetFixedPositionLeftTop() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testGetFixedPositionLeftTop");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(child);
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		inlineStyle.setString( Css.LEFT, "123px");
		inlineStyle.setString( Css.TOP, "45px");
		inlineStyle.setString( Css.POSITION, "fixed");
		inlineStyle.setString( Css.BACKGROUND_COLOR, "red");

		parent.appendChild(child);

		final int left = inlineStyle.getInteger(Css.LEFT, CssUnit.PX, 0);
		Test.assertEquals("" + child, 123, left);

		final int top = inlineStyle.getInteger(Css.TOP, CssUnit.PX, 0);
		Test.assertEquals("" + child, 45, top);

		final String position = inlineStyle.getString(Css.POSITION);
		Test.assertEquals("" + child, "fixed", position);
	}

	/**
	 * @testing-testMethodOrder 47
	 */
	public void testSetFixedPositionRightBottom() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testSetFixedPositionRightBottom");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(child);
		inlineStyle.setString( Css.RIGHT, "12px");
		inlineStyle.setString( Css.BOTTOM, "34px");
		inlineStyle.setString( Css.POSITION, "fixed");
		inlineStyle.setString( Css.BACKGROUND_COLOR, "yellow");
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);

		parent.appendChild(child);

		child.scrollIntoView();

		// ask the user to scroll and verify child is always correctly
		// positioned...
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignoyellow) {
				button.removeFromParent();

				final boolean passed = Window
						.confirm("Did the yellow element remain fixed to the bottom/right corner of the client window when it was scrolled?");
				if (false == passed) {
					Test
							.fail("User confirmed that fixed position element did not remain fixed to the bottom/right corner of the client window when it was scrolled.");
				}
				StyleTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window
				.alert("An element with a yellow background has been fixed to the bottom/right, try scrolling and then click on CONTINUE...");
		StyleTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 48
	 */
	public void testGetFixedPositionRightBottom() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testGetFixedPositionRightBottom");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(child);
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		inlineStyle.setString( Css.RIGHT, "123px");
		inlineStyle.setString( Css.BOTTOM, "45px");
		inlineStyle.setString( Css.POSITION, "fixed");
		inlineStyle.setString( Css.BACKGROUND_COLOR, "yellow");

		parent.appendChild(child);

		final int right = inlineStyle.getInteger(Css.RIGHT, CssUnit.PX, 0);
		Test.assertEquals("" + child, 123, right);

		final int bottom = inlineStyle.getInteger(Css.BOTTOM, CssUnit.PX, 0);
		Test.assertEquals("" + child, 45, bottom);

		final String position = inlineStyle.getString(Css.POSITION);
		Test.assertEquals("" + child, "fixed", position);
	}

	/**
	 * @testing-testMethodOrder 49
	 */
	public void testSetFixedPositionRightBottomThenSetAbsolutePosition() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testSetFixedPositionRightBottomThenSetAbsolutePosition - bottom right fixed positioned");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(child);
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		inlineStyle.setString( Css.RIGHT, "234px");
		inlineStyle.setString( Css.BOTTOM, "56px");
		inlineStyle.setString( Css.POSITION, "fixed");

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				inlineStyle.setString( Css.POSITION, "absolute");
				child.setInnerHTML("testSetFixedPositionRightBottomThenSetAbsolutePosition - bottom right absolute positioned");
			}
		});

		inlineStyle.setString( Css.BACKGROUND_COLOR, "lightBlue");

		parent.appendChild(child);

		// ask the user to scroll and verify child is always
		// correctlypositioned...
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignore) {
				button.removeFromParent();

				final boolean passed = Window
						.confirm("Did the lightBlue element remain stuck to the bottom/right of the browser when it was scrolled?");
				if (false == passed) {
					Test
							.fail("User confirmed that absolute position element did not remain stuck to the bottom/right corner of the browser when it was scrolled.");
				}
				StyleTest.finishTest();
			};
		});
		RootPanel.get().add(button);

		Window
				.alert("An element with a lightBlue background has been absolute positioned to the bottom/right, try scrolling and then click on CONTINUE...");
		StyleTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 50
	 */
	public void testSetFixedPositionThenSetCoordinates() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testSetFixedPositionThenSetCoordinates");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(child);
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		inlineStyle.setString( Css.POSITION, "fixed");
		inlineStyle.setString( Css.LEFT, "345px");
		inlineStyle.setString( Css.TOP, "67px");

		inlineStyle.setString( Css.BACKGROUND_COLOR, "olive");

		parent.appendChild(child);

		// ask the user to scroll and verify child is always
		// correctlypositioned...
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignore) {
				button.removeFromParent();

				final boolean passed = Window
						.confirm("Did the olive element remain fixed to the top/left corner of the client window when it was scrolled?");
				if (false == passed) {
					Test
							.fail("User confirmed that fixed position element did not remain stuck to the top/left corner of the client window when it was scrolled.");
				}
				StyleTest.finishTest();
			};
		});
		RootPanel.get().add(button);

		Window.alert("An element with a olive background has been fixed to the top/left try scrolling and then click on CONTINUE...");
		StyleTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 51
	 */
	public void testSetFixedPositionAndChangeCoordinatesTwice() {
		final Element parent = RootPanel.getBodyElement();
		final Element child = DOM.createSpan();
		child.setInnerHTML("testSetFixedPositionAndChangeCoordinatesTwice");

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(child);
		inlineStyle.setInteger(Css.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		inlineStyle.setString( Css.POSITION, "fixed");
		inlineStyle.setString( Css.LEFT, "34px");
		inlineStyle.setString( Css.BOTTOM, "89px");
		inlineStyle.remove(Css.LEFT);
		inlineStyle.remove(Css.BOTTOM);

		inlineStyle.setString( Css.RIGHT, "34px");
		inlineStyle.setString( Css.TOP, "89px");

		inlineStyle.setString( Css.BACKGROUND_COLOR, "wheat");

		parent.appendChild(child);

		// ask the user to scroll and verify child is always
		// correctlypositioned...
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignore) {
				button.removeFromParent();

				final boolean passed = Window
						.confirm("Did the wheat coloured element remain fixed to the top/right corner of the client window when it was scrolled?");
				if (false == passed) {
					Test
							.fail("User confirmed that fixed position element did not remain stuck to the top/right corner of the client window when it was scrolled.");
				}
				StyleTest.finishTest();
			};
		});
		RootPanel.get().add(button);

		Window.alert("An element with a wheat background has been fixed to the top/right try scrolling and then click on CONTINUE...");
		StyleTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * Creates a new div and adds it to the document. No style properties or any
	 * other values are set.
	 * 
	 * @return
	 */
	protected Element createDivAndAddToDocument() {
		final Element div = DOM.createDiv();
		div.setInnerHTML(this.getCurrentTestName());
		DOM.setStyleAttribute(div, Css.BACKGROUND_COLOR, "lime");
		this.addElement(div);
		return div;
	}

	protected void scrollIntoView(final Element element) {
		element.scrollIntoView();
		this.invokeSetFocus(element);
	}

	native protected void invokeSetFocus(final Element element)/*-{
			element.setFocus( true );
		}-*/;

	protected void onTestStarted(final Test test) {
		super.onTestStarted(test);

		if (false == this.getCurrentTestName().startsWith("testConvert")) {
			this.addTestNameDivider();
		}
	}

	/**
	 * This zIndex is used to ensure new absolutely or fixed positioned elements
	 * float above the remainder of the document.
	 */
	int zIndex = 100;

	int nextZIndex() {
		this.zIndex++;
		return this.zIndex;
	}
}
