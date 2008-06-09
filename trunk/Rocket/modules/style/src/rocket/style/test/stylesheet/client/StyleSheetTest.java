package rocket.style.test.stylesheet.client;

import java.util.ArrayList;
import java.util.List;

import rocket.style.client.Css;
import rocket.style.client.Rule;
import rocket.style.client.RuleStyle;
import rocket.style.client.StyleSheet;
import rocket.testing.client.Test;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.TestRunner;
import rocket.testing.client.WebPageTestRunner;
import rocket.util.client.Checker;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This test creates a number of buttons which run a set of related tests.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleSheetTest extends WebPageTestRunner implements EntryPoint {

	final static int STYLE_SHEET_COUNT = 2;
	final static int STYLE_SHEET_INDEX = 0;

	final static String APPLE_TEXT = "apple";
	final static int APPLE_RULE_INDEX = 3;
	final static String APPLE_CLASS_NAME = "apple";
	final static String APPLE_IMAGE_URL = "apple.png";

	final static int LEMON_RULE_INDEX = 4;
	final static String LEMON_TEXT = "lemon";
	final static String LEMON_CLASS_NAME = "lemon";
	final static String LEMON_IMAGE_URL = "lemon.png";

	final static String CAPSICUM_TEXT = "capsicum";
	final static String CAPSICUM_CLASS_NAME = "capsicum";
	final static String CAPSICUM_IMAGE_URL = "capsicum.png";

	/**
	 * The initial stylesheet contains three rules
	 * (div/body/button/apple/apple).
	 */
	final static int INITIAL_RULE_COUNT = APPLE_RULE_INDEX + 1;

	final String PADDING = Css.PADDING;

	final String PADDING_VALUE = "20px";

	final String TEXT_DECORATION_PROPERTY = Css.TEXT_DECORATION;

	final String UNKNOWN_PROPERTY = "zebra";

	final String COLOUR_PROPERTY = Css.COLOR;

	public void onModuleLoad() {
		StyleSheet.getStyleSheet( Document.get(), STYLE_SHEET_INDEX).prepare();
		
		this.createTestLabels();
		this.addButtons();
	}

	protected void createTestLabels() {
		final RootPanel rootPanel = RootPanel.get();

		rootPanel.add(this.createLabel(APPLE_TEXT, APPLE_CLASS_NAME, APPLE_IMAGE_URL));
		rootPanel.add(this.createLabel(LEMON_TEXT, LEMON_CLASS_NAME, LEMON_IMAGE_URL));
		rootPanel.add(this.createLabel(CAPSICUM_TEXT, CAPSICUM_CLASS_NAME, CAPSICUM_IMAGE_URL));
	}

	/**
	 * Factory method which creates a label that will be assigned a style and
	 * some text.
	 * 
	 * @param text
	 * @param styleName
	 * @return
	 */
	protected Widget createLabel(final String text, final String styleName, final String backgroundImage) {
		Checker.notEmpty("parameter:text", text);
		Checker.notEmpty("parameter:styleName", styleName);
		Checker.notEmpty("parameter:backgroundImage", backgroundImage);

		final Label label = new Label(text);
		label.setStyleName(styleName);
		final Element element = label.getElement();

		DOM.setStyleAttribute(element, "borderStyle", "dotted");
		DOM.setStyleAttribute(element, "borderWidth", "1px");
		DOM.setStyleAttribute(element, "borderColor", "black");
		DOM.setStyleAttribute(element, "backgroundImage", "url('" + backgroundImage + "')");
		DOM.setStyleAttribute(element, "backgroundRepeat", "no-repeat");
		DOM.setStyleAttribute(element, "width", "95%");
		DOM.setStyleAttribute(element, "height", "100px");
		return label;
	}

	protected void addButtons() {
		final RootPanel rootPanel = RootPanel.get();

		final Button testStyleSheet = new Button("StyleSheet");
		testStyleSheet.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StyleSheetTest.this.executeTests(new StyleSheetTestBuilder());
			}
		});
		rootPanel.add(testStyleSheet);

		final Button testRule = new Button("Rule");
		testRule.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StyleSheetTest.this.executeTests(new RuleTestBuilder());
			}
		});
		rootPanel.add(testRule);

		final Button testSelector = new Button("Selector");
		testSelector.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StyleSheetTest.this.executeTests(new SelectorTestBuilder());
			}
		});
		rootPanel.add(testSelector);

		final Button testRuleStyle = new Button("RuleStyle");
		testRuleStyle.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StyleSheetTest.this.executeTests(new StyleTestBuilder());
			}
		});
		rootPanel.add(testRuleStyle);
	}

	protected void onTestFailed(final Test test) {
		if (Window.confirm("Skip remaining tests")) {
			TestRunner.skipRemainingTests();
		}
		super.onTestFailed(test);
	}

	protected void onTestAborted(final Test test) {
		if (Window.confirm("Skip remaining tests")) {
			TestRunner.skipRemainingTests();
		}
		super.onTestAborted(test);
	}

	class StyleSheetTestBuilder implements TestBuilder {
		public List<Test> buildCandidates() {
			final List<Test> tests = new ArrayList<Test>();

			tests.add(new Test() {

				@Override
				public String getName() {
					return "testStyleSheetGetStyleSheetCount";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testStyleSheetGetStyleSheetCount();
				}
			});

			tests.add(new Test() {
				@Override
				public String getName() {
					return "testStyleSheetGetStyleSheet";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testStyleSheetGetStyleSheet();
				}
			});

			tests.add(new Test() {
				@Override
				public String getName() {
					return "testStyleSheetGetStyleSheetWithInvalidIndex";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testStyleSheetGetStyleSheetWithInvalidIndex();
				}
			});

			return tests;
		}
	}

	protected void testStyleSheetGetStyleSheetCount() {
		final int styleSheetCount = StyleSheet.getStyleSheetCount(Document.get());
		Test.assertEquals("styleSheetCount", STYLE_SHEET_COUNT, styleSheetCount);
	}

	protected void testStyleSheetGetStyleSheet() {
		final StyleSheet styleSheet = StyleSheet.getStyleSheet(Document.get(), STYLE_SHEET_INDEX);
		Test.assertNotNull(styleSheet);
	}

	protected void testStyleSheetGetStyleSheetWithInvalidIndex() {
		final Document document = Document.get();
		try {
			final StyleSheet styleSheet = StyleSheet.getStyleSheet(document, -1);
			Test.fail("StyleSheets.getStyleSheet(invalidIndex) should have thrown an exception and not returned " + styleSheet);
		} catch (final Exception expected) {
		}
		try {
			final StyleSheet styleSheet = StyleSheet.getStyleSheet(document, 12345);
			Test.fail("StyleSheets.getStyleSheet(invalidIndex) should have thrown an exception and not returned " + styleSheet);
		} catch (final Exception expected) {
		}
	}

	class RuleTestBuilder implements TestBuilder {
		public List<Test> buildCandidates() {
			final List<Test> tests = new ArrayList<Test>();

			tests.add(new Test() {
				@Override
				public String getName() {
					return "testStyleSheetGetRuleCount";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testStyleSheetGetRuleCount();
				}
			});

			tests.add(new Test() {
				@Override
				public String getName() {
					return "testStyleSheetGetRule";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testStyleSheetGetRule();
				}
			});

			tests.add(new Test() {
				@Override
				public String getName() {
					return "testStyleSheetGetRuleWithInvalidIndex";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testStyleSheetGetRuleWithInvalidIndex();
				}
			});

			tests.add(new Test() {

				@Override
				public String getName() {
					return "testStyleSheetAddThenRemoveRule";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testStyleSheetAddThenRemoveRule();
				}
			});

			return tests;
		}
	}

	protected void testStyleSheetGetRuleCount() {
		final int ruleCount = this.getStyleSheet().getRuleCount();
		Test.assertEquals("rules count", ruleCount, INITIAL_RULE_COUNT);
	}

	protected void testStyleSheetGetRule() {
		final Rule rule = this.getStyleSheet().getRule(APPLE_RULE_INDEX);
		Test.assertNotNull("appleRule", rule);
		Test.assertEquals("appleRuleSelector", "." + APPLE_CLASS_NAME, rule.getSelector());
	}

	protected void testStyleSheetGetRuleWithInvalidIndex() {
		final StyleSheet styleSheet = this.getStyleSheet();

		try {
			final Rule rule = styleSheet.getRule(-1);
			Test.fail("An exception should have been thrown by StyleSheet.getRule(invalidIndex) and not " + rule);
		} catch (final Exception expected) {
		}
	}

	protected void testStyleSheetAddThenRemoveRule() {
		final StyleSheet styleSheet = this.getStyleSheet();

		final Rule rule = styleSheet.addRule("." + LEMON_CLASS_NAME, "");
		Test.assertEquals("." + LEMON_CLASS_NAME, rule.getSelector());
		Test.assertEquals("new rules count", INITIAL_RULE_COUNT + 1, styleSheet.getRuleCount());

		// update the style portion of the rule...
		final RuleStyle style = rule.getRuleStyle();
		style.setString(Css.COLOR, "orange");
		style.setString(Css.BACKGROUND_COLOR, "purple");

		if (false == Window.confirm("Does the lemon area now have orange text on a purple background")) {
			Test.fail("The lemon area does not have orange text on a purple background.");
		}

		styleSheet.removeRule(rule);

		final int index = styleSheet.getRuleIndex(rule);
		Test.assertEquals( -1, index );
		
		// test that the rule size has returned back to normal.
		Test.assertEquals("rules", INITIAL_RULE_COUNT, styleSheet.getRuleCount());

		if (false == Window.confirm("Has lemon area has reverted back to black text on a white background (inherited from body)?")) {
			Test.fail("The lemon area did not revert back to black text on a white background after removing the newly added rule.");
		}
	}

	class SelectorTestBuilder implements TestBuilder {
		public List<Test> buildCandidates() {
			final List<Test> tests = new ArrayList<Test>();

			tests.add(new Test() {

				@Override
				public String getName() {
					return "testRuleGetSelector";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.RuleGetSelector();
				}
			});

			return tests;
		}
	}

	protected void RuleGetSelector() {
		final StyleSheet styleSheet = this.getStyleSheet();
		final Rule rule = styleSheet.getRule(APPLE_RULE_INDEX);
		final String selectorValue = rule.getSelector();
		Test.assertEquals("rule.selector", "." + APPLE_CLASS_NAME, selectorValue);
	}

	class StyleTestBuilder implements TestBuilder {
		public List<Test> buildCandidates() {
			final List<Test> tests = new ArrayList<Test>();

			tests.add(new Test() {

				@Override
				public String getName() {
					return "testRuleStyleGetString";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testRuleStyleGetString();
				}
			});
			tests.add(new Test() {

				@Override
				public String getName() {
					return "testRuleStyleGetStringThatDoesntExist";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testRuleStyleGetStringThatDoesntExist();
				}
			});
			tests.add(new Test() {

				@Override
				public String getName() {
					return "testRuleStyleSetString";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testRuleStyleSetString();
				}
			});
			tests.add(new Test() {

				@Override
				public String getName() {
					return "testRuleStyleRemove";
				}

				@Override
				public void execute() {
					StyleSheetTest.this.testRuleStyleRemove();
				}
			});
			return tests;
		}
	}

	public void testRuleStyleGetString() {
		final RuleStyle style = this.getAppleRuleStyle();
		final String value = style.getString(PADDING);
		Test.assertNotNull(value);
	}

	public void testRuleStyleGetStringThatDoesntExist() {
		final RuleStyle style = this.getAppleRuleStyle();
		final String value = style.getString(UNKNOWN_PROPERTY);
		Test.assertNull(value);
	}

	public void testRuleStyleSetString() {
		final RuleStyle style = this.getAppleRuleStyle();
		style.setString(TEXT_DECORATION_PROPERTY, "underline");

		if (false == Window.confirm("Is apple area text now underlined ?")) {
			Test.fail("Unable to add a style to the apple rule to make text underlined");
		}

		style.remove(TEXT_DECORATION_PROPERTY);
	}

	public void testRuleStyleRemove() {
		final RuleStyle style = this.getAppleRuleStyle();
		style.remove(TEXT_DECORATION_PROPERTY);

		if (false == Window.confirm("Is apple area text now NOT underlined ?")) {
			Test.fail("Unable to remove the underline style from the apple rule.");
		}
	}

	protected StyleSheet getStyleSheet() {
		return StyleSheet.getStyleSheet(Document.get(), STYLE_SHEET_INDEX);
	}

	protected Rule getAppleRule() {
		return (Rule) this.getStyleSheet().getRule(APPLE_RULE_INDEX);
	}

	protected RuleStyle getAppleRuleStyle() {
		return this.getAppleRule().getRuleStyle();
	}
}
