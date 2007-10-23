package rocket.style.test.stylehelper.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rocket.dom.client.Dom;
import rocket.style.client.ComputedStyle;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.style.client.StyleHelper;
import rocket.style.client.support.StyleSupportConstants;
import rocket.testing.client.Test;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.TestRunner;
import rocket.testing.client.WebPageTestRunner;
import rocket.util.client.Colour;
import rocket.util.client.StackTrace;
import rocket.util.client.StringHelper;

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
 * A series of automated tests that occasionally prompts the user to confirm
 * some visual changes.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleHelperTest extends WebPageTestRunner implements EntryPoint {

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
				StyleHelperTest.this.executeTests((TestBuilder) GWT.create( TestMethodFinder.class ));
			}
		});
		RootPanel.get().add(button);

		button.setFocus(true);
	}
	
	static interface TestMethodFinder extends TestBuilder{
		/**
		 * @testing-testRunner rocket.style.test.stylehelper.client.StyleHelperTest
		 */
		abstract public List buildCandidates();
	}

	/**
	 * @testing-testMethodOrder 0
	 */
	protected void testHasClass0() {
		final Element element = this.createDivAndAddToDocument();
		DOM.setElementProperty(element, StyleConstants.CLASS_NAME, "apple banana carrot");

		final boolean found = StyleHelper.hasClass(element, "car");
		Test.assertFalse(found);
	}

	/**
	 * @testing-testMethodOrder 1
	 */
	protected void testHasClass1() {
		final Element element = this.createDivAndAddToDocument();
		DOM.setElementProperty(element, StyleConstants.CLASS_NAME, "apple banana carrot");

		final boolean found = StyleHelper.hasClass(element, "banana");
		Test.assertTrue(found);
	}

	/**
	 * @testing-testMethodOrder 2
	 */
	protected void testAddClass0() {
		final Element element = this.createDivAndAddToDocument();
		DOM.setElementProperty(element, StyleConstants.CLASS_NAME, "apple banana carrot");

		StyleHelper.addClass(element, "banana");

		Test.assertEquals("apple banana carrot", DOM.getElementProperty(element, StyleConstants.CLASS_NAME));
	}

	/**
	 * @testing-testMethodOrder 3
	 */
	protected void testAddClass1() {
		final Element element = this.createDivAndAddToDocument();
		DOM.setElementProperty(element, StyleConstants.CLASS_NAME, "apple banana carrot");

		StyleHelper.addClass(element, "dog");

		Test.assertEquals("apple banana carrot dog", DOM.getElementProperty(element, StyleConstants.CLASS_NAME));
	}
	/**
	 * @testing-testMethodOrder 4
	 */
	protected void testRemoveClass0() {
		final Element element = this.createDivAndAddToDocument();
		DOM.setElementProperty(element, StyleConstants.CLASS_NAME, "apple banana carrot");

		StyleHelper.removeClass(element, "banana");

		Test.assertEquals("apple carrot", DOM.getElementProperty(element, StyleConstants.CLASS_NAME));
	}

	/**
	 * @testing-testMethodOrder 5
	 */
	protected void testRemoveClass1() {
		final Element element = this.createDivAndAddToDocument();
		DOM.setElementProperty(element, StyleConstants.CLASS_NAME, "apple banana carrot");

		StyleHelper.removeClass(element, "dog");

		Test.assertEquals("apple banana carrot", DOM.getElementProperty(element, StyleConstants.CLASS_NAME));
	}

	/**
	 * @testing-testMethodOrder 6
	 */
	protected void testInlineGetStylePropertyValue() {
		final Element element = this.createDivAndAddToDocument();
		final String propertyName = StyleConstants.BACKGROUND_COLOR;
		final String propertyValue = Colour.getColour("yellow").toCssColour();
		DOM.setStyleAttribute(element, propertyName, propertyValue);
		InlineStyle.setString(element, StyleConstants.WIDTH, WIDTH + "px");
		InlineStyle.setString(element, StyleConstants.HEIGHT, HEIGHT + "px");

		String actualPropertyValue = InlineStyle.getString(element, propertyName);
		actualPropertyValue = Colour.parse(actualPropertyValue).toCssColour();
		final String expectedPropertyValue = propertyValue;
		Test.assertEquals(expectedPropertyValue, actualPropertyValue);
	}

	/**
	 * @testing-testMethodOrder 6
	 */
	protected void testInlineSetStylePropertyValue() {
		final Element element = this.createDivAndAddToDocument();
		final String propertyValue = Colour.getColour("aquamarine").toCssColour();

		InlineStyle.setString(element, StyleConstants.BACKGROUND_COLOR, propertyValue);
		InlineStyle.setString(element, StyleConstants.WIDTH, WIDTH + "px");
		InlineStyle.setString(element, StyleConstants.HEIGHT, HEIGHT + "px");

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
	 * @testing-testMethodOrder 7
	 */
	protected void testComputedGetPropertyValue() {
		final Element element = this.createDivAndAddToDocument();
		final String propertyName = StyleConstants.BACKGROUND_COLOR;
		final String propertyValue = Colour.getColour("yellow").toCssColour();
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		String actualPropertyValue = ComputedStyle.getString(element, propertyName);
		actualPropertyValue = Colour.parse(actualPropertyValue).toCssColour();
		final String expectedPropertyValue = propertyValue;
		Test.assertEquals(expectedPropertyValue, actualPropertyValue);
	}

	/**
	 * @testing-testMethodOrder 8
	 */
	protected void testInlineGetOpacity() {
		final Element containerElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(containerElement, StyleConstants.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(containerElement, StyleConstants.BACKGROUND_COLOR, "yellow");

		final Element childElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(childElement, StyleConstants.POSITION, "relative");
		DOM.setStyleAttribute(childElement, StyleConstants.LEFT, -WIDTH / 2 + "px");
		DOM.setStyleAttribute(childElement, StyleConstants.TOP, -HEIGHT / 2 + "px");
		DOM.setStyleAttribute(childElement, StyleConstants.Z_INDEX, "1");
		DOM.setStyleAttribute(childElement, StyleConstants.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(childElement, StyleConstants.BACKGROUND_COLOR, "blue");

		final float opacity = 0.5f;
		DOM.setStyleAttribute(childElement, FILTER, "alpha(opacity=" + (int) (opacity * 100) + ")");
		DOM.setStyleAttribute(childElement, StyleConstants.OPACITY, "" + opacity);

		final String actualOpacity = InlineStyle.getString(childElement, StyleConstants.OPACITY);
		final String expectedOpacity = "" + opacity;
		Test.assertEquals("actualOpacity: " + actualOpacity + ", expectedOpacity: " + expectedOpacity, Double.parseDouble(expectedOpacity),
				Double.parseDouble(actualOpacity), 0.5);
	}

	/**
	 * @testing-testMethodOrder 9
	 */
	protected void testComputedGetOpacity() {
		final Element containerElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(containerElement, StyleConstants.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(containerElement, StyleConstants.BACKGROUND_COLOR, "yellow");

		final Element childElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(childElement, StyleConstants.POSITION, "relative");
		DOM.setStyleAttribute(childElement, StyleConstants.LEFT, -WIDTH / 2 + "px");
		DOM.setStyleAttribute(childElement, StyleConstants.TOP, -HEIGHT / 2 + "px");
		DOM.setStyleAttribute(childElement, StyleConstants.Z_INDEX, "1");
		DOM.setStyleAttribute(childElement, StyleConstants.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(childElement, StyleConstants.BACKGROUND_COLOR, "blue");

		final float opacity = 0.5f;
		DOM.setStyleAttribute(childElement, FILTER, "alpha(opacity=" + (int) (opacity * 100) + ")");
		DOM.setStyleAttribute(childElement, StyleConstants.OPACITY, "" + opacity);

		final String actualOpacity = ComputedStyle.getString(childElement, StyleConstants.OPACITY);
		final String expectedOpacity = "" + opacity;
		Test.assertEquals("actualOpacity: " + actualOpacity + ", expectedOpacity: " + expectedOpacity, Double.parseDouble(expectedOpacity),
				Double.parseDouble(actualOpacity), 0.5);
	}

	/**
	 * @testing-testMethodOrder 10
	 */
	protected void testInlineSetOpacity() {
		final Element containerElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(containerElement, StyleConstants.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(containerElement, StyleConstants.BACKGROUND_COLOR, "yellow");

		final Element childElement = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(childElement, StyleConstants.POSITION, "relative");
		DOM.setStyleAttribute(childElement, StyleConstants.LEFT, -WIDTH / 2 + "px");
		DOM.setStyleAttribute(childElement, StyleConstants.TOP, -HEIGHT / 2 + "px");
		DOM.setStyleAttribute(childElement, StyleConstants.Z_INDEX, "1");
		DOM.setStyleAttribute(childElement, StyleConstants.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(containerElement, StyleConstants.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(childElement, StyleConstants.BACKGROUND_COLOR, "blue");

		this.scrollIntoView(childElement);
		TestRunner.postponeCurrentTest(POSTPONE_DELAY);

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				if (false == Window.confirm("Does a blue rectangle overlap a yellow background ?")) {
					Test.fail("The blue rectangle is not blue.");
				}

				final float opacity = 0.5f;
				final String propertyName = StyleConstants.OPACITY;
				final String propertyValue = "" + opacity;
				InlineStyle.setString(childElement, propertyName, propertyValue);

				if (false == Window.confirm("Is the rectangle that was blue now green where it overlaps the yellow background ?")) {
					Test.fail("The rectangle overlaying the yellow rectangle is not green.");
				}

				TestRunner.finishTest();
			}
		});
	}

	/**
	 * @testing-testMethodOrder 11
	 */
	protected void testComputedGetWidthWhereDivHasInlineWidthAndNoBorderOrPadding() {
		final int borderLeftWidth = 0;
		final int borderRightWidth = 0;
		final int paddingLeft = 0;
		final int paddingRight = 0;

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(element, StyleConstants.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_WIDTH, "0px");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(element, StyleConstants.PADDING, "0px");
		DOM.setStyleAttribute(element, StyleConstants.PADDING_LEFT, paddingLeft + "px");
		DOM.setStyleAttribute(element, StyleConstants.PADDING_RIGHT, paddingRight + "px");
		DOM.setStyleAttribute(element, StyleConstants.MARGIN, "0px");

		final String actualContentWidth = ComputedStyle.getString(element, StyleConstants.WIDTH);
		final String expectedContentWidth = (WIDTH - borderLeftWidth - borderRightWidth - paddingLeft - paddingRight) + "px";
		Test.assertEquals(expectedContentWidth, actualContentWidth);
	}

	/**
	 * @testing-testMethodOrder 12
	 */
	protected void testComputedGetWidthWhereDivInheritsBorderPaddingWidthFromParent0() {
		final int borderLeftWidth = 0;
		final int borderRightWidth = 0;
		final int paddingLeft = 0;
		final int paddingRight = 0;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setInnerHTML(parent, "&nbsp;");
		DOM.setStyleAttribute(parent, StyleConstants.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_LEFT, paddingLeft + "px");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_RIGHT, paddingRight + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_BOTTOM, 25 + "px");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
		DOM.appendChild(parent, child);

		final String actualContentWidth = ComputedStyle.getString(child, StyleConstants.WIDTH);
		final String expectedContentWidth = WIDTH + "px";
		Test.assertEquals(expectedContentWidth, actualContentWidth);
	}

	/**
	 * @testing-testMethodOrder 13
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
		DOM.setStyleAttribute(parent, StyleConstants.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_LEFT, paddingLeft + "px");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_RIGHT, paddingRight + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_LEFT, marginLeft + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_RIGHT, marginRight + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_BOTTOM, 25 + "px");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
		DOM.appendChild(parent, child);
		DOM.setInnerHTML(child, "&nbsp;");

		final String actualContentWidth = ComputedStyle.getString(child, StyleConstants.WIDTH);
		final String expectedContentWidth = WIDTH + "px";
		Test.assertEquals(expectedContentWidth, actualContentWidth);
	}

	/**
	 * @testing-testMethodOrder 14
	 */
	protected void testComputedGetWidthWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent() {
		final int borderLeftWidth = 11;
		final int borderRightWidth = 12;
		final int paddingLeft = 13;
		final int paddingRight = 14;
		final int marginLeft = 15;
		final int marginRight = 16;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(parent, StyleConstants.WIDTH, WIDTH + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_LEFT_WIDTH, borderLeftWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_RIGHT_WIDTH, borderRightWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_LEFT, paddingLeft + "px");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_RIGHT, paddingRight + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_LEFT, marginLeft + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_RIGHT, marginRight + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_BOTTOM, 25 + "px");
		DOM.setStyleAttribute(parent, StyleConstants.OVERFLOW, "scroll");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
		DOM.appendChild(parent, child);
		DOM.setInnerHTML(child, "CHILDtestComputedGetWidthWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent");

		final String actualContentWidth = ComputedStyle.getString(child, StyleConstants.WIDTH);
		final String expectedContentWidth = WIDTH + "px";
		Test.assertEquals(expectedContentWidth, actualContentWidth);
	}

	/**
	 * @testing-testMethodOrder 15
	 */
	protected void testComputedGetHeightWhereDivHasInlineHeightAndNoBorderOrPadding() {
		final int borderTopWidth = 0;
		final int borderBottomWidth = 0;
		final int paddingTop = 0;
		final int paddingBottom = 0;

		final Element element = this.createDivAndAddToDocument();
		DOM.setInnerHTML(element, "");
		DOM.setStyleAttribute(element, StyleConstants.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_TOP_WIDTH, borderTopWidth + "px");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(element, StyleConstants.PADDING_LEFT, paddingTop + "px");
		DOM.setStyleAttribute(element, StyleConstants.PADDING_RIGHT, paddingBottom + "px");
		DOM.setStyleAttribute(element, StyleConstants.MARGIN_BOTTOM, 25 + "px");

		final String actualContentHeight = ComputedStyle.getString(element, StyleConstants.HEIGHT);
		final String expectedContentHeight = (HEIGHT - borderTopWidth - borderBottomWidth - paddingTop - paddingBottom) + "px";
		Test.assertEquals(expectedContentHeight, actualContentHeight);
	}

	/**
	 * @testing-testMethodOrder 16
	 */
	protected void testComputedGetHeightWhereDivInheritsBorderPaddingHeightFromParent0() {
		final int height = 100;
		final int borderTopWidth = 0;
		final int borderBottomWidth = 0;
		final int paddingTop = 0;
		final int paddingBottom = 0;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setInnerHTML(parent, "");
		DOM.setStyleAttribute(parent, StyleConstants.HEIGHT, height + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_TOP_WIDTH, borderTopWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_TOP, paddingTop + "px");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_BOTTOM, paddingBottom + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN, "0px");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
		DOM.setStyleAttribute(child, StyleConstants.MARGIN, "0px");
		DOM.setStyleAttribute(child, StyleConstants.BORDER_WIDTH, "0px");
		DOM.setStyleAttribute(child, StyleConstants.PADDING, "0px");
		DOM.setStyleAttribute(child, StyleConstants.HEIGHT, "100%");
		DOM.appendChild(parent, child);
		DOM.setInnerHTML(child, "CHILD");

		final String actualContentHeight = ComputedStyle.getString(child, StyleConstants.HEIGHT);
		final String expectedContentHeight = height + "px";
		Test.assertEquals(expectedContentHeight, actualContentHeight);
	}

	/**
	 * @testing-testMethodOrder 17
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
		DOM.setStyleAttribute(parent, StyleConstants.HEIGHT, height + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_TOP_WIDTH, borderTopWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_TOP, paddingTop + "px");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_BOTTOM, paddingBottom + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_TOP, marginTop + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_BOTTOM, marginBottom + "px");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
		DOM.setStyleAttribute(child, StyleConstants.MARGIN, "0px");
		DOM.setStyleAttribute(child, StyleConstants.BORDER_WIDTH, "0px");
		DOM.setStyleAttribute(child, StyleConstants.PADDING, "0px");
		DOM.setStyleAttribute(child, StyleConstants.HEIGHT, "100%");
		DOM.appendChild(parent, child);
		DOM.setInnerHTML(child, "CHILD");

		final String actualContentHeight = ComputedStyle.getString(child, StyleConstants.HEIGHT);
		final String expectedContentHeight = height + "px";
		Test.assertEquals(expectedContentHeight, actualContentHeight);
	}
	/**
	 * @testing-testMethodOrder 18
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
		DOM.setStyleAttribute(parent, StyleConstants.HEIGHT, HEIGHT + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_TOP_WIDTH, borderTopWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_BOTTOM_WIDTH, borderBottomWidth + "px");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_STYLE, "solid");
		DOM.setStyleAttribute(parent, StyleConstants.BORDER_COLOR, "lawnGreen");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_LEFT, paddingTop + "px");
		DOM.setStyleAttribute(parent, StyleConstants.PADDING_RIGHT, paddingBottom + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_LEFT, marginLeft + "px");
		DOM.setStyleAttribute(parent, StyleConstants.MARGIN_RIGHT, marginRight + "px");
		DOM.setStyleAttribute(parent, StyleConstants.OVERFLOW, "scroll");

		final Element child = DOM.createDiv();
		DOM.setStyleAttribute(child, StyleConstants.BACKGROUND_COLOR, "lightGreen");
		DOM.appendChild(parent, child);
		DOM.setInnerHTML(child, "CHILDtestComputedGetHeightWhereDivHasScrollBarsAndInheritsBorderPaddingWidthFromParent");

		final String actualContentHeight = ComputedStyle.getString(child, StyleConstants.HEIGHT);
		final String expectedContentHeight = HEIGHT + "px";
		Test.assertEquals(expectedContentHeight, actualContentHeight);
	}

	/**
	 * @testing-testMethodOrder 19
	 */
	protected void testConvertPixelsToPixels() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "px", CssUnit.PX);
		final float expected = input;
		Test.assertEquals(expected, actual, 0.75f);
	}

	/**
	 * @testing-testMethodOrder 20
	 */
	protected void testConvertInchesToPixels() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "in", CssUnit.PX);
		final float expected = Math.round(input / 96f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	/**
	 * @testing-testMethodOrder 21
	 */
	protected void testConvertPixelsToInches() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "px", CssUnit.IN);
		final float expected = Math.round(input * 96f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	/**
	 * @testing-testMethodOrder 22
	 */
	protected void testConvertCentimetersToPixels() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "cm", CssUnit.PX);
		final float expected = Math.round(input / 96f * 2.54f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	/**
	 * @testing-testMethodOrder 23
	 */
	protected void testConvertPixelsToCentimeters() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "px", CssUnit.CM);
		final float expected = Math.round(input * 96f / 2.54f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	/**
	 * @testing-testMethodOrder 24
	 */
	protected void testConvertMillimetersToPixels() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "mm", CssUnit.PX);
		final float expected = Math.round(input / 96f * 25.4f);
		Test.assertEquals(expected, actual, 0.75f);
	}
	/**
	 * @testing-testMethodOrder 25
	 */
	protected void testConvertPixelsToMillimeters() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "px", CssUnit.MM);
		final float expected = Math.round(input * 96f / 25.4f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	/**
	 * @testing-testMethodOrder 26
	 */
	protected void testConvertPointsToPixels() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "pt", CssUnit.PX);
		final float expected = Math.round(input * 96f / 72f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	/**
	 * @testing-testMethodOrder 27
	 */
	protected void testConvertPixelsToPoints() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "px", CssUnit.PT);
		final float expected = Math.round(input / 96f * 72f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	/**
	 * @testing-testMethodOrder 28
	 */
	protected void testConvertPicasToPixels() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "pc", CssUnit.PX);
		final float expected = Math.round(input * 96f / 72f * 12f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	/**
	 * @testing-testMethodOrder 29
	 */
	protected void testConvertPixelsToPicas() {
		final float input = 1234;
		final float actual = StyleHelper.convertValue(input + "px", CssUnit.PC);
		final float expected = Math.round(input / 96f * 72f / 12f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	/**
	 * @testing-testMethodOrder 30
	 */
	public void testInlineSetBackgroundImage() {
		final String propertyName = StyleConstants.BACKGROUND_IMAGE;
		final String propertyValue = "image.gif";

		final Element element = this.createDivAndAddToDocument();

		InlineStyle.setString(element, propertyName, "url('" + propertyValue + "')");

		// check that the style got updated...
		final String expected = propertyValue;
		final String actual = DOM.getStyleAttribute(element, propertyName);
		Test.assertTrue("actual[" + actual + "], expected[" + expected + "]", actual.indexOf(expected) != -1);
	}

	/**
	 * @testing-testMethodOrder 31
	 */
	public void testInlineSetBackgroundImageWithElementAlsoContainingABackgroundColour() {
		final String propertyName = StyleConstants.BACKGROUND_IMAGE;
		final String propertyValue = "image.gif";
		final Colour colour = Colour.getColour("red");

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.BACKGROUND_COLOR, colour.toCssColour());

		InlineStyle.setString(element, propertyName, "url('" + propertyValue + "')");

		// check that the style got updated...
		final String expected = propertyValue;
		final String actual = DOM.getStyleAttribute(element, propertyName);
		Test.assertTrue("actual [" + actual + "] expected[" + expected + "]", actual.indexOf(expected) != -1);

		final String backgroundColour = InlineStyle.getString(element, StyleConstants.BACKGROUND_COLOR);
		final String expectedBackgroundColour = colour.toCssColour();
		Test.assertEquals("backgroundColor", Colour.parse(expectedBackgroundColour), Colour.parse(backgroundColour));

		final Colour actualColour = Colour.parse(backgroundColour);
		final Colour expectedColour = colour;
		Test.assertEquals(expectedColour, actualColour);
	}

	/**
	 * @testing-testMethodOrder 32
	 */
	public void testComputedGetBackgroundPositionWhenNoValueIsSet() {
		final String propertyName = StyleConstants.BACKGROUND_POSITION;

		final Element element = this.createDivAndAddToDocument();

		final String actual = ComputedStyle.getString(element, propertyName);
		Test.assertTrue("" + element + ", actual[" + actual + "]", actual == null || actual.equals("0% 0%") || actual.equals("left left")
				|| actual.equals("0px 0px"));
	}

	/**
	 * @testing-testMethodOrder 33
	 */
	public void testComputedGetBackgroundPosition() {
		final String propertyName = StyleConstants.BACKGROUND_POSITION;
		final String propertyValue = "0px 0px";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.BACKGROUND, "url('image.gif')");
		DOM.setStyleAttribute(element, StyleConstants.BACKGROUND_IMAGE, "url('image.gif')");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final String actual = ComputedStyle.getString(element, propertyName);
		final String expected = propertyValue;
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 34
	 */
	public void testComputedGetBackgroundPositionWithElementThatIncludesAllTheOtherBackgroundProperties() {
		final String propertyName = StyleConstants.BACKGROUND_POSITION;
		final String propertyValue = "0px 0px";

		final Element element = this.createDivAndAddToDocument();
		final String backgroundProperty = "url('image.gif') no-repeat fixed #123456 " + propertyValue;
		DOM.setStyleAttribute(element, StyleConstants.BACKGROUND, backgroundProperty);

		final String actual = ComputedStyle.getString(element, propertyName);
		final String expected = propertyValue;
		Test.assertEquals(backgroundProperty, expected, actual);
	}

	/**
	 * @testing-testMethodOrder 35
	 */
	public void testComputedGetFontSizeSetToXSmallValue() {
		final String propertyName = StyleConstants.FONT_SIZE;
		final String propertyValue = "x-small";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final String actual = ComputedStyle.getString(element, propertyName);
		final String expected = "10px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 36
	 */
	public void testComputedGetFontSizeSetToSmallValue() {
		final String propertyName = StyleConstants.FONT_SIZE;
		final String propertyValue = "small";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final String actual = ComputedStyle.getString(element, propertyName);
		final String expected = "13px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 37
	 */
	public void testComputedGetFontSizeSetToMediumValue() {
		final String propertyName = StyleConstants.FONT_SIZE;
		final String propertyValue = "medium";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final String actual = ComputedStyle.getString(element, propertyName);
		final String expected = "16px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 38
	 */
	public void testComputedGetFontSizeSetToLargeValue() {
		final String propertyName = StyleConstants.FONT_SIZE;
		final String propertyValue = "large";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final String actual = ComputedStyle.getString(element, propertyName);
		final String expected = "18px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 39
	 */
	public void testComputedGetFontSizeSetToXLargeValue() {
		final String propertyName = StyleConstants.FONT_SIZE;
		final String propertyValue = "x-large";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final String actual = ComputedStyle.getString(element, propertyName);
		final String expected = "24px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 40
	 */
	public void testComputedGetFontSizeSetToXXLargeValue() {
		final String propertyName = StyleConstants.FONT_SIZE;
		final String propertyValue = "xx-large";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, propertyValue);

		final String actual = ComputedStyle.getString(element, propertyName);
		final String expected = "32px";
		Test.assertEquals(expected, actual);
	}

	/**
	 * @testing-testMethodOrder 41
	 */
	public void testComputedGetFontSizeSetToSmaller() {
		final String propertyName = StyleConstants.FONT_SIZE;
		final String propertyValue = "smaller";
		final int parentFontSize = 13;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(parent, StyleConstants.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(parent, propertyName, parentFontSize + "px");

		final Element child = DOM.createSpan();
		DOM.setStyleAttribute(child, StyleConstants.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(child, propertyName, propertyValue);
		DOM.appendChild(parent, child);

		final String actualString = ComputedStyle.getString(child, propertyName);
		Test.assertNotNull(actualString);

		final float expected = Math.round(parentFontSize * StyleSupportConstants.SMALLER_SCALING_FACTOR);
		TestRunner.log("actual[" + actualString + "], expected[" + expected + "]");

		final float actual = Math.round(Double.parseDouble(actualString.substring(0, actualString.length() - 2)));
		Test.assertTrue("actual[" + actual + "] expected[" + expected + "]", actual < parentFontSize);
		Test.assertEquals("actual[" + actual + "] expected[" + expected + "]", expected, actual, 2.5f);
	}

	/**
	 * @testing-testMethodOrder 42
	 */
	public void testComputedGetFontSizeSetToLarger() {
		final String propertyName = StyleConstants.FONT_SIZE;
		final String propertyValue = "larger";
		final int parentFontSize = 13;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(parent, StyleConstants.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(parent, propertyName, parentFontSize + "px");

		final Element child = DOM.createSpan();
		DOM.setStyleAttribute(child, StyleConstants.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(child, propertyName, propertyValue);
		DOM.appendChild(parent, child);

		final String actualString = ComputedStyle.getString(child, propertyName);
		Test.assertNotNull(actualString);

		final float expected = Math.round(parentFontSize * StyleSupportConstants.LARGER_SCALING_FACTOR);
		TestRunner.log("actual[" + actualString + "], expected[" + expected + "]");

		final float actual = Math.round(Double.parseDouble(actualString.substring(0, actualString.length() - 2)));
		Test.assertTrue("actual[" + actual + "] expected[" + expected + "]", actual > parentFontSize);
		Test.assertEquals("actual[" + actual + "] expected[" + expected + "]", expected, actual, 2.5f);
	}

	/**
	 * @testing-testMethodOrder 43
	 */
	public void testComputedGetFontWeightWithMissingPropertyValue() {
		final String propertyName = StyleConstants.FONT_WEIGHT;

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");

		final String actual = ComputedStyle.getString(element, propertyName);
		Test.assertEquals("" + element, "400", actual);
	}

	/**
	 * @testing-testMethodOrder 44
	 */
	public void testComputedGetFontWeightWithNumberPropertyValue() {
		final String propertyName = StyleConstants.FONT_WEIGHT;

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
		DOM.setIntStyleAttribute(element, propertyName, 700);

		final String actual = ComputedStyle.getString(element, propertyName);
		Test.assertEquals("" + element, "700", actual);
	}

	/**
	 * @testing-testMethodOrder 45
	 */
	public void testComputedGetFontWeightSetToNormal() {
		final String propertyName = StyleConstants.FONT_WEIGHT;

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, "normal");

		final String actual = ComputedStyle.getString(element, propertyName);
		Test.assertEquals("" + element, "400", actual);
	}

	/**
	 * @testing-testMethodOrder 46
	 */
	public void testComputedGetFontWeightSetToBold() {
		final String propertyName = StyleConstants.FONT_WEIGHT;

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.FONT_FAMILY, "Verdana");
		DOM.setStyleAttribute(element, propertyName, "bold");

		final String actual = ComputedStyle.getString(element, propertyName);
		Test.assertEquals("" + element, "700", actual);
	}

	/**
	 * @testing-testMethodOrder 47
	 */
	public void testComputedGetFontWeightSetToLighter() {
		final String propertyName = StyleConstants.FONT_WEIGHT;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(parent, StyleConstants.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(parent, StyleConstants.FONT_SIZE, "20pt");
		DOM.setStyleAttribute(parent, propertyName, "bold");

		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "CHILD");
		DOM.setStyleAttribute(child, StyleConstants.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(child, StyleConstants.FONT_SIZE, "20pt");
		DOM.setStyleAttribute(child, propertyName, "lighter");
		DOM.appendChild(parent, child);

		final String actual = ComputedStyle.getString(child, propertyName);
		Test.assertEquals("" + child, "400", actual);
	}

	/**
	 * @testing-testMethodOrder 48
	 */
	public void testComputedGetFontWeightSetToBolder() {
		final String propertyName = StyleConstants.FONT_WEIGHT;

		final Element parent = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(parent, StyleConstants.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(parent, StyleConstants.FONT_SIZE, "20pt");
		DOM.setStyleAttribute(parent, propertyName, "normal");

		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "CHILD");
		DOM.setStyleAttribute(child, StyleConstants.FONT_FAMILY, "Times");
		DOM.setStyleAttribute(child, StyleConstants.FONT_SIZE, "20pt");
		DOM.setStyleAttribute(child, propertyName, "bolder");
		DOM.appendChild(parent, child);

		final String actualString = ComputedStyle.getString(child, propertyName);
		Test.assertNotNull(actualString);

		final String actual = ComputedStyle.getString(child, propertyName);
		Test.assertEquals("" + child, "700", actual);
	}

	/**
	 * @testing-testMethodOrder 49
	 */
	public void testComputedGetBorderWidthThin() {
		final String propertyName = StyleConstants.BORDER_LEFT_WIDTH;
		final String propertyValue = "thin";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, propertyName, propertyValue);
		DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_COLOR, "black");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_STYLE, "solid");

		final String actual = ComputedStyle.getString(element, propertyName);
		Test.assertNotNull(actual);
		TestRunner.log(actual);
		final int number = Integer.parseInt(actual.substring(0, actual.length() - 2));
		Test.assertTrue("actual[" + actual + "]", number == StyleSupportConstants.BORDER_WIDTH_THIN_PX
				|| number == StyleSupportConstants.BORDER_WIDTH_THIN_PX_IE6);
	}

	/**
	 * @testing-testMethodOrder 50
	 */
	public void testComputedGetBorderWidthMedium() {
		final String propertyName = StyleConstants.BORDER_LEFT_WIDTH;
		final String propertyValue = "medium";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, propertyName, propertyValue);
		DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_COLOR, "black");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_STYLE, "solid");

		final String actual = ComputedStyle.getString(element, propertyName);
		Test.assertNotNull(actual);
		final int number = Integer.parseInt(actual.substring(0, actual.length() - 2));
		Test.assertTrue("actual[" + actual + "]", number == StyleSupportConstants.BORDER_WIDTH_MEDIUM_PX
				|| number == StyleSupportConstants.BORDER_WIDTH_MEDIUM_PX_IE6);
	}

	/**
	 * @testing-testMethodOrder 51
	 */
	public void testComputedGetBorderWidthThick() {
		final String propertyName = StyleConstants.BORDER_LEFT_WIDTH;
		final String propertyValue = "thick";

		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, propertyName, propertyValue);
		DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_COLOR, "black");
		DOM.setStyleAttribute(element, StyleConstants.BORDER_LEFT_STYLE, "solid");

		final String actual = ComputedStyle.getString(element, propertyName);
		Test.assertNotNull(actual);
		final int number = Integer.parseInt(actual.substring(0, actual.length() - 2));
		Test.assertTrue("actual[" + actual + "]", number == StyleSupportConstants.BORDER_WIDTH_THICK_PX
				|| number == StyleSupportConstants.BORDER_WIDTH_THICK_PX_IE6);
	}

	/**
	 * @testing-testMethodOrder 52
	 */
	public void testComputedGetStylePropertyNames0() {
		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.CURSOR, "move");
		DOM.setStyleAttribute(element, StringHelper.toCamelCase(StyleConstants.BACKGROUND_COLOR), "aquamarine");

		final String[] propertyNames = StyleHelper.getComputedStylePropertyNames(element);
		Test.assertNotNull(propertyNames);

		final int length = propertyNames.length;
		Test.assertTrue("length: " + length, length >= 2);

		final List list = new ArrayList();
		list.addAll(Arrays.asList(propertyNames));

		Test.assertTrue(StyleConstants.CURSOR + ", list: " + list, list.contains(StyleConstants.CURSOR));
		Test.assertTrue(StyleConstants.BACKGROUND_COLOR + ", list: " + list, list.contains(StringHelper
				.toCamelCase(StyleConstants.BACKGROUND_COLOR)));
	}

	/**
	 * @testing-testMethodOrder 53
	 */
	public void testComputedGetStylePropertyNames1() {
		final Element element = this.createDivAndAddToDocument();
		DOM.setStyleAttribute(element, StyleConstants.CURSOR, "move");
		DOM.setStyleAttribute(element, StringHelper.toCamelCase(StyleConstants.BACKGROUND), "url('image .gif')");
		DOM.setStyleAttribute(element, StringHelper.toCamelCase(StyleConstants.BACKGROUND_IMAGE), "url('image.gif')");

		final String[] propertyNames = StyleHelper.getComputedStylePropertyNames(element);
		Test.assertNotNull(propertyNames);

		final int length = propertyNames.length;
		Test.assertTrue("length: " + length, length >= 2);

		final List list = new ArrayList();
		list.addAll(Arrays.asList(propertyNames));

		Test.assertTrue(StyleConstants.CURSOR + ", list: " + list, list.contains(StyleConstants.CURSOR));
		// Test.assertTrue(StyleConstants.BACKGROUND_IMAGE + ", list: " + list,
		// list.contains(StringHelper.toCamelCase(StyleConstants.BACKGROUND_IMAGE)));
	}

	/**
	 * @testing-testMethodOrder 54
	 */
	protected void testInlineSetUserSelectionTextSelectionDisabled() {
		final Element element = Dom.getBody();
		InlineStyle.setString(element, StyleConstants.USER_SELECT, StyleConstants.USER_SELECT_DISABLED);

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

				StyleHelperTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("Text selection has been disabled, try and select text anywhere and then click on CONTINUE...");
		StyleHelperTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 55
	 */
	protected void testInlineSetUserSelectionTextSelectionEnabled() {
		final Element element = Dom.getBody();
		InlineStyle.setString(element, StyleConstants.USER_SELECT, StyleConstants.USER_SELECT_ENABLED);

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
				StyleHelperTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("Text selection has been enabled, try and select text anywhere and then click on CONTINUE...");
		StyleHelperTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 56
	 */
	protected void testInlineGetUserSelection() {
		final Element element = Dom.getBody();
		final String propertyName = StyleConstants.USER_SELECT;

		InlineStyle.setString(element, propertyName, ""); // enable
		final String value0 = InlineStyle.getString(element, propertyName);
		Test.assertFalse("selection should be enabled. [" + value0 + "]", "none".equals(value0));

		InlineStyle.setString(element, propertyName, "none"); // disable
		final String value1 = InlineStyle.getString(element, propertyName);
		Test.assertEquals("selection should be disabled. [" + value1 + "]", "none", value1);

		InlineStyle.setString(element, propertyName, ""); // enable
		final String value2 = InlineStyle.getString(element, propertyName);
		Test.assertFalse("selection should be enabled. [" + value2 + "]", "none".equals(value2));

		InlineStyle.setString(element, propertyName, "none"); // disable
		final String value3 = InlineStyle.getString(element, propertyName);
		Test.assertEquals("selection should be disabled. [" + value3 + "]", "none", value3);
	}

	/**
	 * @testing-testMethodOrder 57
	 */
	protected void testComputedGetUserSelection() {
		final Element parent = Dom.getBody();
		final Element child = DOM.getChild(Dom.getBody(), 0);
		final String propertyName = StyleConstants.USER_SELECT;

		InlineStyle.setString(parent, propertyName, ""); // enable
		final String value0 = ComputedStyle.getString(child, propertyName);
		Test.assertNull("selection should be enabled. [" + value0 + "]", value0);

		InlineStyle.setString(parent, propertyName, "none"); // disable
		final String value1 = ComputedStyle.getString(child, propertyName);
		Test.assertNotNull("selection should be disabled. [" + value1 + "]", value1);

		InlineStyle.setString(parent, propertyName, ""); // enable
		final String value2 = ComputedStyle.getString(child, propertyName);
		Test.assertNull("selection should be enabled. [" + value2 + "]", value2);

		InlineStyle.setString(parent, propertyName, "none"); // disable
		final String value3 = ComputedStyle.getString(child, propertyName);
		Test.assertNotNull("selection should be disabled. [" + value3 + "]", value3);
	}

	/**
	 * @testing-testMethodOrder 58
	 */
	protected void testSetAbsolutePositionLeftTop() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testSetAbsolutePositionLeftTop");

		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "orange");
		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		InlineStyle.setString(child, StyleConstants.LEFT, "12px");
		InlineStyle.setString(child, StyleConstants.TOP, "34px");
		InlineStyle.setString(child, StyleConstants.POSITION, "absolute");

		DOM.appendChild(parent, child);
		DOM.scrollIntoView(child);

		// ask the user to scroll and verify child is always correctly
		// positioned...
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				button.removeFromParent();

				final boolean passed = Window
						.confirm("Did the absolute positioned orange element remain stuck to the top/left corner of the client window when scrolled ?");
				if (false == passed) {
					Test
							.fail("User confirmed that absolute position element was not stuck to the top/left corner of the client window when it was scrolled.");
				}
				StyleHelperTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window
				.alert("An element with a orange background has been absolutely positioned to the top/left, try scrolling and then click on CONTINUE...");
		StyleHelperTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 59
	 */
	protected void testGetAbsolutePositionLeftTop() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testGetAbsolutePositionLeftTop");

		InlineStyle.setString(child, StyleConstants.LEFT, "123px");
		InlineStyle.setString(child, StyleConstants.TOP, "45px");
		InlineStyle.setString(child, StyleConstants.POSITION, "absolute");
		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "red");
		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);

		DOM.appendChild(parent, child);

		final int left = InlineStyle.getInteger(child, StyleConstants.LEFT, CssUnit.PX, 0);
		Test.assertEquals("" + child, 123, left);

		final int top = InlineStyle.getInteger(child, StyleConstants.TOP, CssUnit.PX, 0);
		Test.assertEquals("" + child, 45, top);

		final String position = InlineStyle.getString(child, StyleConstants.POSITION);
		Test.assertEquals("" + child, "absolute", position);
	}

	/**
	 * @testing-testMethodOrder 60
	 */
	protected void testSetAbsolutePositionRightBottom() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testSetAbsolutePositionRightBottom");

		InlineStyle.setString(child, StyleConstants.RIGHT, "123px");
		InlineStyle.setString(child, StyleConstants.BOTTOM, "56px");
		InlineStyle.setString(child, StyleConstants.POSITION, "absolute");
		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "green");
		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);

		DOM.appendChild(parent, child);

		DOM.scrollIntoView(child);

		// ask the user to scroll and verify child is always correctly
		// positioned...
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignoyellow) {
				button.removeFromParent();

				final boolean passed = Window
						.confirm("Did the absolute positioned green element remain stuck to the bottom/right corner of the client window when scrolled ?");
				if (false == passed) {
					Test
							.fail("User confirmed that absolute position element was not stuck to the bottom/right corner of the client window when it was scrolled.");
				}
				StyleHelperTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window
				.alert("An element with a green background has been absolutely positioned to the bottom/right, try scrolling and then click on CONTINUE...");
		StyleHelperTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 61
	 */
	protected void testGetAbsolutePositionRightBottom() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testGetAbsolutePositionRightBottom");

		InlineStyle.setString(child, StyleConstants.RIGHT, "123px");
		InlineStyle.setString(child, StyleConstants.BOTTOM, "56px");
		InlineStyle.setString(child, StyleConstants.POSITION, "absolute");
		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "yellow");
		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);

		DOM.appendChild(parent, child);

		final int right = InlineStyle.getInteger(child, StyleConstants.RIGHT, CssUnit.PX, 0);
		Test.assertEquals("" + child, 123, right);

		final int bottom = InlineStyle.getInteger(child, StyleConstants.BOTTOM, CssUnit.PX, 0);
		Test.assertEquals("" + child, 56, bottom);

		final String position = InlineStyle.getString(child, StyleConstants.POSITION);
		Test.assertEquals("" + child, "absolute", position);
	}

	/**
	 * @testing-testMethodOrder 62
	 */
	protected void testSetFixedPositionLeftTop() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testSetFixedPositionLeftTop");

		InlineStyle.setString(child, StyleConstants.COLOR, "blue");
		InlineStyle.setString(child, StyleConstants.LEFT, "12px");
		InlineStyle.setString(child, StyleConstants.TOP, "34px");
		InlineStyle.setString(child, StyleConstants.POSITION, "fixed");

		DOM.appendChild(parent, child);

		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "red");
		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);

		DOM.scrollIntoView(child);

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
				StyleHelperTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("An element with a red background has been fixed to the top/left, try scrolling and then click on CONTINUE...");
		StyleHelperTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 63
	 */
	protected void testGetFixedPositionLeftTop() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testGetFixedPositionLeftTop");

		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		InlineStyle.setString(child, StyleConstants.LEFT, "123px");
		InlineStyle.setString(child, StyleConstants.TOP, "45px");
		InlineStyle.setString(child, StyleConstants.POSITION, "fixed");
		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "red");

		DOM.appendChild(parent, child);

		final int left = InlineStyle.getInteger(child, StyleConstants.LEFT, CssUnit.NONE, 0);
		Test.assertEquals("" + child, 123, left);

		final int top = InlineStyle.getInteger(child, StyleConstants.TOP, CssUnit.NONE, 0);
		Test.assertEquals("" + child, 45, top);

		final String position = InlineStyle.getString(child, StyleConstants.POSITION);
		Test.assertEquals("" + child, "fixed", position);
	}

	/**
	 * @testing-testMethodOrder 64
	 */
	protected void testSetFixedPositionRightBottom() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testSetFixedPositionRightBottom");

		InlineStyle.setString(child, StyleConstants.RIGHT, "12px");
		InlineStyle.setString(child, StyleConstants.BOTTOM, "34px");
		InlineStyle.setString(child, StyleConstants.POSITION, "fixed");
		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "yellow");
		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);

		DOM.appendChild(parent, child);

		DOM.scrollIntoView(child);

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
				StyleHelperTest.finishTest();
			}
		});
		RootPanel.get().add(button);

		Window.alert("An element with a yellow background has been fixed to the bottom/right, try scrolling and then click on CONTINUE...");
		StyleHelperTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 65
	 */
	protected void testGetFixedPositionRightBottom() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testGetFixedPositionRightBottom");

		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		InlineStyle.setString(child, StyleConstants.RIGHT, "123px");
		InlineStyle.setString(child, StyleConstants.BOTTOM, "45px");
		InlineStyle.setString(child, StyleConstants.POSITION, "fixed");
		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "yellow");

		DOM.appendChild(parent, child);

		final int right = InlineStyle.getInteger(child, StyleConstants.RIGHT, CssUnit.NONE, 0);
		Test.assertEquals("" + child, 123, right);

		final int bottom = InlineStyle.getInteger(child, StyleConstants.BOTTOM, CssUnit.NONE, 0);
		Test.assertEquals("" + child, 45, bottom);

		final String position = InlineStyle.getString(child, StyleConstants.POSITION);
		Test.assertEquals("" + child, "fixed", position);
	}

	/**
	 * @testing-testMethodOrder 66
	 */
	protected void testSetFixedPositionRightBottomThenSetAbsolutePosition() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testSetFixedPositionRightBottomThenSetAbsolutePosition - bottom right fixed positioned");

		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		InlineStyle.setString(child, StyleConstants.RIGHT, "234px");
		InlineStyle.setString(child, StyleConstants.BOTTOM, "56px");
		InlineStyle.setString(child, StyleConstants.POSITION, "fixed");

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				InlineStyle.setString(child, StyleConstants.POSITION, "absolute");
				DOM.setInnerHTML(child, "testSetFixedPositionRightBottomThenSetAbsolutePosition - bottom right absolute positioned");
			}
		});

		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "lightBlue");

		DOM.appendChild(parent, child);

		// ask the user to scroll and verify child is always correctly
		// positioned...
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
				StyleHelperTest.finishTest();
			};
		});
		RootPanel.get().add(button);

		Window
				.alert("An element with a lightBlue background has been absolute positioned to the bottom/right, try scrolling and then click on CONTINUE...");
		StyleHelperTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * @testing-testMethodOrder 67
	 */
	protected void testSetFixedPositionThenSetCoordinates() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testSetFixedPositionThenSetCoordinates");

		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		InlineStyle.setString(child, StyleConstants.POSITION, "fixed");
		InlineStyle.setString(child, StyleConstants.LEFT, "345px");
		InlineStyle.setString(child, StyleConstants.TOP, "67px");

		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "olive");

		DOM.appendChild(parent, child);

		// ask the user to scroll and verify child is always correctly
		// positioned...
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
				StyleHelperTest.finishTest();
			};
		});
		RootPanel.get().add(button);

		Window.alert("An element with a olive background has been fixed to the top/left try scrolling and then click on CONTINUE...");
		StyleHelperTest.postponeCurrentTest(POSTPONE_DELAY);
	}
	/**
	 * @testing-testMethodOrder 68
	 */
	protected void testSetPositionAndChangeCoordinatesTwice() {
		final Element parent = Dom.getBody();
		final Element child = DOM.createSpan();
		DOM.setInnerHTML(child, "testSetPositionAndChangeCoordinatesTwice");

		InlineStyle.setInteger(child, StyleConstants.Z_INDEX, this.nextZIndex(), CssUnit.NONE);
		InlineStyle.setString(child, StyleConstants.POSITION, "fixed");
		InlineStyle.setString(child, StyleConstants.LEFT, "34px");
		InlineStyle.setString(child, StyleConstants.BOTTOM, "89px");
		InlineStyle.remove(child, StyleConstants.LEFT);
		InlineStyle.remove(child, StyleConstants.BOTTOM);

		InlineStyle.setString(child, StyleConstants.RIGHT, "34px");
		InlineStyle.setString(child, StyleConstants.TOP, "89px");

		InlineStyle.setString(child, StyleConstants.BACKGROUND_COLOR, "wheat");

		DOM.appendChild(parent, child);

		// ask the user to scroll and verify child is always correctly
		// positioned...
		final Button button = new Button("Continue");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget ignore) {
				button.removeFromParent();

				final boolean passed = Window
						.confirm("Did the wheat element remain fixed to the top/right corner of the client window when it was scrolled?");
				if (false == passed) {
					Test
							.fail("User confirmed that fixed position element did not remain stuck to the top/right corner of the client window when it was scrolled.");
				}
				StyleHelperTest.finishTest();
			};
		});
		RootPanel.get().add(button);

		Window.alert("An element with a wheat background has been fixed to the top/right try scrolling and then click on CONTINUE...");
		StyleHelperTest.postponeCurrentTest(POSTPONE_DELAY);
	}

	/**
	 * Creates a new div and adds it to the document. No style properties or any
	 * other values are set.
	 * 
	 * @return
	 */
	protected Element createDivAndAddToDocument() {
		final Element div = DOM.createDiv();
		DOM.setInnerHTML(div, this.getCurrentTestName());
		DOM.setStyleAttribute(div, StyleConstants.BACKGROUND_COLOR, "lime");
		this.addElement(div);
		return div;
	}

	protected void scrollIntoView(final Element element) {
		DOM.scrollIntoView(element);
		Dom.setFocus(element);
	}

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