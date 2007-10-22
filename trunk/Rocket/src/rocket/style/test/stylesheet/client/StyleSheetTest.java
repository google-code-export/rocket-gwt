package rocket.style.test.stylesheet.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.style.client.CssUnit;
import rocket.style.client.Rule;
import rocket.style.client.Style;
import rocket.style.client.Css;
import rocket.style.client.StylePropertyValue;
import rocket.style.client.StyleSheet;
import rocket.testing.client.Test;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.TestRunner;
import rocket.testing.client.WebPageTestRunner;
import rocket.util.client.Colour;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.EntryPoint;
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

	final static int STYLE_SHEET_COUNT = 1;

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

	final String PADDING = "padding";

	final String PADDING_VALUE = "20px";

	final String TEXT_DECORATION_PROPERTY = "textDecoration";

	final String ZEBRA_PROPERTY = "zebra";

	final String BACKGROUND_COLOUR_PROPERTY = "backgroundColor";

	final String COLOUR_PROPERTY = "color";

	final String FONT_SIZE = "fontSize";

	final String FONT_SIZE_VALUE = "40px";

	public void onModuleLoad() {
		this.createTestLabels();
		this.addButtons();
	}
	
	protected void createTestLabels() {
		final RootPanel rootPanel = RootPanel.get();

		rootPanel.add(this.createLabel(APPLE_TEXT, APPLE_CLASS_NAME, APPLE_IMAGE_URL ));
		rootPanel.add(this.createLabel(LEMON_TEXT, LEMON_CLASS_NAME, LEMON_IMAGE_URL ));
		rootPanel.add(this.createLabel(CAPSICUM_TEXT, CAPSICUM_CLASS_NAME, CAPSICUM_IMAGE_URL ));
	}

	/**
	 * Factory method which creates a label that will be assigned a style and
	 * some text.
	 * 
	 * @param text
	 * @param styleName
	 * @return
	 */
	protected Widget createLabel(final String text, final String styleName, final String backgroundImage ) {
		StringHelper.checkNotEmpty("parameter:text", text);
		StringHelper.checkNotEmpty("parameter:styleName", styleName);
		StringHelper.checkNotEmpty("parameter:backgroundImage", backgroundImage);

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

		final Button testStyleSheets = new Button("StyleSheets(List)");
		testStyleSheets.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StyleSheetTest.this.executeTests(new StyleSheetsTestBuilder());
			}
		});
		rootPanel.add(testStyleSheets);

		final Button testStyleSheet = new Button("StyleSheet");
		testStyleSheet.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StyleSheetTest.this.executeTests(new StyleSheetTestBuilder());
			}
		});
		rootPanel.add(testStyleSheet);

		final Button testRuleList = new Button("Rules(List)");
		testRuleList.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StyleSheetTest.this.executeTests(new RulesTestBuilder());
			}
		});
		rootPanel.add(testRuleList);

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

		final Button testRuleStyle = new Button("Style(Map)");
		testRuleStyle.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StyleSheetTest.this.executeTests(new StyleTestBuilder());
			}
		});
		rootPanel.add(testRuleStyle);

		final Button testStylePropertyValue = new Button("StylePropertyValue");
		testStylePropertyValue.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StyleSheetTest.this.executeTests(new StylePropertyValueTestBuilder());
			}
		});
		rootPanel.add(testStylePropertyValue);
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

	class StyleSheetsTestBuilder implements TestBuilder {
		public List buildCandidates() {
			final List tests = new ArrayList();

			tests.add(new Test() {

				public String getName() {
					return "testGetStyleSheetList";
				}

				public void execute() {
					StyleSheetTest.this.testGetStyleSheetList();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListCached";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListCached();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListSize";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListSize();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListIsEmpty";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListIsEmpty();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListAddObject";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListAddObject();
				}
			});
			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListAddIntObject";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListAddIntObject();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListGet";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListGet();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListRepeatedGet";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListRepeatedGet();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListGetWithInvalidIndex";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListGetWithInvalidIndex();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListSet";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListSet();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListRemoveInt";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListRemoveInt();
				}
			});
			tests.add(new Test() {
				public String getName() {
					return "testStyleSheetListRemoveObject";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetListRemoveObject();
				}
			});

			return tests;
		}
	}

	protected void testGetStyleSheetList() {
		final List styleSheets = StyleSheet.getStyleSheets();
		Test.assertEquals("StyleSheets size", 1, styleSheets.size());
	}

	protected void testStyleSheetListCached() {
		final List first = StyleSheet.getStyleSheets();
		final List second = StyleSheet.getStyleSheets();
		Test.assertSame("StyleSheetsList cached", first, second);
	}

	protected void testStyleSheetListSize() {
		final List styleSheets = StyleSheet.getStyleSheets();
		final int expected = 1;
		final int actual = styleSheets.size();
		Test.assertEquals("StyleSheets List", actual, expected);
	}

	protected void testStyleSheetListIsEmpty() {
		final List styleSheets = StyleSheet.getStyleSheets();
		final boolean expected = false;
		final boolean actual = styleSheets.isEmpty();
		Test.assertEquals("empty test", actual, expected);
	}

	protected void testStyleSheetListAddObject() {
		final List styleSheets = StyleSheet.getStyleSheets();
		try {
			styleSheets.add(null);
			Test.fail("StyleSheets.add(Object) should have thrown an exception...");
		} catch (final Exception expected) {
		}
	}

	protected void testStyleSheetListAddIntObject() {
		final List styleSheets = StyleSheet.getStyleSheets();
		try {
			styleSheets.add(0, null);
			Test.fail("StyleSheets.add(int,Object) should have thrown an exception...");
		} catch (final Exception expected) {
		}
	}

	protected void testStyleSheetListGet() {
		final List styleSheets = StyleSheet.getStyleSheets();
		final Object rule = styleSheets.get(0);
		Test.assertNotNull(rule);
	}

	protected void testStyleSheetListRepeatedGet() {
		final List styleSheets = StyleSheet.getStyleSheets();
		final Object firstRule = styleSheets.get(0);
		final Object secondRule = styleSheets.get(0);
		Test.assertSame(firstRule, secondRule);
	}

	protected void testStyleSheetListGetWithInvalidIndex() {
		final List styleSheets = StyleSheet.getStyleSheets();
		try {
			styleSheets.get(100);
			Test.fail("StyleSheets.get(invalidIndex) should have thrown an exception...");
		} catch (final Exception expected) {
		}
	}

	protected void testStyleSheetListSet() {
		final List styleSheets = StyleSheet.getStyleSheets();
		try {
			styleSheets.set(0, null);
			Test.fail("StyleSheets.set(int,StyleSheet) should have thrown an exception...");
		} catch (final Exception expected) {
		}
	}

	protected void testStyleSheetListRemoveInt() {
		final List styleSheets = StyleSheet.getStyleSheets();
		try {
			styleSheets.remove(0);
			Test.fail("StyleSheets.remove(int) should have thrown an exception...");
		} catch (final Exception expected) {
		}
	}

	protected void testStyleSheetListRemoveObject() {
		final List styleSheets = StyleSheet.getStyleSheets();
		try {
			final Object styleSheet = styleSheets.get(0);
			styleSheets.remove(styleSheet);
			Test.fail("StyleSheets.remove(Object) should have thrown an exception...");
		} catch (final Exception expected) {
		}
	}

	class StyleSheetTestBuilder implements TestBuilder {
		public List buildCandidates() {
			final List tests = new ArrayList();

			tests.add(new Test() {

				public String getName() {
					return "testStyleSheetGetRules";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSheetGetRules();
				}
			});

			return tests;
		}
	}

	protected void testStyleSheetGetRules() {
		final StyleSheet styleSheet = (StyleSheet) StyleSheet.getStyleSheets().get(0);
		final List rules = styleSheet.getRules();
		Test.assertNotNull(rules);
	}

	class RulesTestBuilder implements TestBuilder {
		public List buildCandidates() {
			final List tests = new ArrayList();

			tests.add(new Test() {

				public String getName() {
					return "testRulesSize";
				}

				public void execute() {
					StyleSheetTest.this.testRulesSize();
				}
			});

			tests.add(new Test() {

				public String getName() {
					return "testRulesIsEmpty";
				}

				public void execute() {
					StyleSheetTest.this.testRulesIsEmpty();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testRulesGet0";
				}

				public void execute() {
					StyleSheetTest.this.testRulesGet0();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testRulesGet1";
				}

				public void execute() {
					StyleSheetTest.this.testRulesGet1();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testRulesContains";
				}

				public void execute() {
					StyleSheetTest.this.testRulesContains();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testRulesIndexOf";
				}

				public void execute() {
					StyleSheetTest.this.testRulesIndexOf();
				}
			});

			tests.add(new Test() {
				public String getName() {
					return "testRulesIterator";
				}

				public void execute() {
					StyleSheetTest.this.testRulesIterator();
				}
			});

			return tests;
		}
	}

	protected void testRulesSize() {
		final List rules = getRules();
		Test.assertEquals("rules.size()", rules.size(), INITIAL_RULE_COUNT);
	}

	protected void testRulesIsEmpty() {
		final List rules = getRules();
		Test.assertEquals("rules.isEmpty()", rules.isEmpty(), false);
	}

	protected void testRulesGet0() {
		final List rules = getRules();
		final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
		Test.assertNotNull("appleRule", rule);
		Test.assertEquals("appleRuleSelector", rule.getSelector(), "." + APPLE_CLASS_NAME);
	}

	protected void testRulesGet1() {
		final List rules = getRules();

		try {
			final Rule someRule = (Rule) rules.get(100);
			Test.fail("An exception should have been thrown when attempting to retrieving a Rule using an invalid index and not: "
					+ someRule);
		} catch (final Exception expected) {
		}
	}

	protected void testRulesContains() {
		final List rules = getRules();

		final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
		Test.assertNotNull("appleRule", rule);

		final boolean found = rules.contains(rule);
		Test.assertEquals("rules.contains()", found, true);
	}

	protected void testRulesIndexOf() {
		final List rules = getRules();

		final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
		Test.assertNotNull("appleRule", rule);

		final int index = rules.indexOf(rule);
		Test.assertEquals("appleIndex", index, APPLE_RULE_INDEX);
	}

	protected void testRulesIterator() {
		final List rules = getRules();
		final Iterator iterator = rules.iterator();
		Test.assertNotNull("rules.iterator", iterator);

		final String[] selectors = new String[] { "body", "div", "button", ".apple" };
		for (int i = 0; i < selectors.length; i++) {
			Test.assertEquals("rules.iterator.hasNext() for element " + i, iterator.hasNext(), true);

			final Rule iteratorRule = (Rule) iterator.next();
			final String selector = iteratorRule.getSelector();
			Test.assertEquals("rule(" + i + ")selector", selector.toLowerCase(), selectors[i].toLowerCase());
		}

		Test.assertEquals("rules.iterator.hasNext() for exhausted iterator", iterator.hasNext(), false);
	}

	class RuleTestBuilder implements TestBuilder {
		public List buildCandidates() {
			final List tests = new ArrayList();

			tests.add(new Test() {

				public String getName() {
					return "testRuleAddAndRemove";
				}

				public void execute() {
					StyleSheetTest.this.testRuleAddAndRemove();
				}
			});

			return tests;
		}
	}

	protected void testRuleAddAndRemove() {
		final List rules = this.getRules();

		final Rule rule = new Rule();
		rule.setSelector("." + LEMON_CLASS_NAME);
		rules.add(rule);

		Test.assertEquals("new rules count", rules.size(), INITIAL_RULE_COUNT + 1);

		// verify that it was appended by checking lemonRules index.
		final int newRuleIndex = rules.indexOf(rule);
		PrimitiveHelper.checkEquals("lemonRule indexOf ", INITIAL_RULE_COUNT, newRuleIndex);

		// update the style portion of the rule...
		final Style style = (Style) rule.getStyle();
		style.setCssText("color: orange; background-color: purple;");

		if (false == Window.confirm("Does the lemon area now have orange text on a purple background")) {
			Test.fail("The lemon area does not have orange text on a purple background.");
		}

		final boolean removed = rules.remove(rule);
		Test.assertEquals("rule removed", removed, true);

		// test that the rule size has returned back to normal.
		Test.assertEquals("rules", rules.size(), INITIAL_RULE_COUNT);

		// verify that it was appended by checking lemonRules index.
		final int indexOfRemovedRule = rules.indexOf(rule);
		Test.assertEquals("indexOf( lemonRule ) should be -1 after removing the newRule", indexOfRemovedRule, -1);

		if (false == Window.confirm("Has lemon area has reverted back to black text on a white background (inherited from body)?")) {
			Test.fail("The lemon area did not revert back to black text on a white background after removing the newly added rule.");
		}
	}

	class SelectorTestBuilder implements TestBuilder {
		public List buildCandidates() {
			final List tests = new ArrayList();

			tests.add(new Test() {

				public String getName() {
					return "testSelectorGet";
				}

				public void execute() {
					StyleSheetTest.this.testSelectorGet();
				}
			});

			tests.add(new Test() {

				public String getName() {
					return "testSelectorSet";
				}

				public void execute() {
					StyleSheetTest.this.testSelectorSet();
				}
			});

			return tests;
		}
	}

	protected void testSelectorGet() {
		final List rules = this.getRules();

		final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
		final String selectorValue = rule.getSelector();
		Test.assertEquals("rule.selector", selectorValue, "." + APPLE_CLASS_NAME);
	}

	protected void testSelectorSet() {
		final List rules = this.getRules();
		final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
		rule.setSelector("." + CAPSICUM_CLASS_NAME);

		// rule size shouldnt have changed...
		Test.assertEquals("rules", rules.size(), INITIAL_RULE_COUNT);

		// index should not have changed...
		final int index = rules.indexOf(rule);
		Test.assertEquals("The index of rule", index, APPLE_RULE_INDEX);

		if (false == Window.confirm("Does the apple area have black text on a white background ?\n")) {
			Test.fail("The apple area does not have black text on white background.");
		}
		if (false == Window.confirm("Does the capsicum area have green text on a yellow background ?\n")) {
			Test.fail("The capsicum area does not have green text on yellow background.");
		}

		rule.setSelector("." + APPLE_CLASS_NAME);

		if (false == Window.confirm("Does the apple area have green text on a yellow background ?\n")) {
			Test.fail("The apple area does not have green text on yellow background.");
		}
		if (false == Window.confirm("Does the capsicum area have black text on a white background ?\n")) {
			Test.fail("The capsicum area does not have black text on white background.");
		}
	}

	class StyleTestBuilder implements TestBuilder {
		public List buildCandidates() {
			final List tests = new ArrayList();

			tests.add(new Test() {

				public String getName() {
					return "testStyleSize";
				}

				public void execute() {
					StyleSheetTest.this.testStyleSize();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleIsEmpty";
				}

				public void execute() {
					StyleSheetTest.this.testStyleIsEmpty();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleContainsKey0";
				}

				public void execute() {
					StyleSheetTest.this.testStyleContainsKey0();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleContainsKey1";
				}

				public void execute() {
					StyleSheetTest.this.testStyleContainsKey1();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleContainsValue0";
				}

				public void execute() {
					StyleSheetTest.this.testStyleContainsValue0();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleContainsValue1";
				}

				public void execute() {
					StyleSheetTest.this.testStyleContainsValue1();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleGetStyle0";
				}

				public void execute() {
					StyleSheetTest.this.testStyleGetStyle0();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleGetStyle1";
				}

				public void execute() {
					StyleSheetTest.this.testStyleGetStyle1();
				}
			});
			tests.add(new Test() {
				public String getName() {
					return "testStylePutStyle0";
				}

				public void execute() {
					StyleSheetTest.this.testStylePutStyle0();
				}
			});
			tests.add(new Test() {
				public String getName() {
					return "testStylePutStyle1";
				}

				public void execute() {
					StyleSheetTest.this.testStylePutStyle1();
				}
			});

			tests.add(new Test() {

				public String getName() {
					return "testStyleRemoveStyle0";
				}

				public void execute() {
					StyleSheetTest.this.testStyleRemoveStyle();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleKeySetSize";
				}

				public void execute() {
					StyleSheetTest.this.testStyleKeySetSize();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleKeySetIsEmpty";
				}

				public void execute() {
					StyleSheetTest.this.testStyleKeySetIsEmpty();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleKeySetContains0";
				}

				public void execute() {
					StyleSheetTest.this.testStyleKeySetContains0();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleKeySetContains1";
				}

				public void execute() {
					StyleSheetTest.this.testStyleKeySetContains1();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleKeySetIterator";
				}

				public void execute() {
					StyleSheetTest.this.testStyleKeySetIterator();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleValuesCollectionSize";
				}

				public void execute() {
					StyleSheetTest.this.testStyleValuesCollectionSize();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleValuesCollectionIsEmpty";
				}

				public void execute() {
					StyleSheetTest.this.testStyleValuesCollectionIsEmpty();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleValuesCollectionContains0";
				}

				public void execute() {
					StyleSheetTest.this.testStyleValuesCollectionContains0();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleValuesCollectionContains1";
				}

				public void execute() {
					StyleSheetTest.this.testStyleValuesCollectionContains1();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStyleValuesCollectionIterator";
				}

				public void execute() {
					StyleSheetTest.this.testStyleValuesCollectionIterator();
				}
			});
			return tests;
		}
	}

	public void testStyleSize() {
		final Map style = this.getAppleRuleStyle();

		final int size = style.size();
		Test.assertTrue(size + " > 5", size > 5);
	}

	public void testStyleIsEmpty() {
		final Map style = this.getAppleRuleStyle();

		Test.assertFalse("Style is empty", style.isEmpty());
	}

	public void testStyleContainsKey0() {
		final Map style = this.getAppleRuleStyle();

		final boolean containsKey = style.containsKey(BACKGROUND_COLOUR_PROPERTY);
		Test.assertTrue("Style.containsKey( \"" + BACKGROUND_COLOUR_PROPERTY + "\" )", containsKey);
	}

	public void testStyleContainsKey1() {
		final Map style = this.getAppleRuleStyle();

		final boolean containsKey = style.containsKey(ZEBRA_PROPERTY);
		Test.assertFalse("Style.containsKey( \"" + ZEBRA_PROPERTY + "\" )", containsKey);
	}

	public void testStyleGetStyle0() {
		final Map style = this.getAppleRuleStyle();
		final StylePropertyValue value = (StylePropertyValue) style.get(PADDING);
		Test.assertNotNull(value);
	}

	public void testStyleContainsValue0() {
		final Map style = this.getAppleRuleStyle();

		final StylePropertyValue value = new StylePropertyValue();
		value.setString(PADDING_VALUE);
		style.put(Css.PADDING_RIGHT, value);

		final boolean containsValue = style.containsValue(value);
		Test.assertTrue("Map.containsValue(\"" + PADDING_VALUE + "\")", containsValue);
	}

	public void testStyleContainsValue1() {
		final Map style = this.getAppleRuleStyle();

		final StylePropertyValue value = new StylePropertyValue();
		value.setString("xxx");

		final boolean containsValue = style.containsValue(value);
		Test.assertFalse("Map.containsValue(\"xxx\")", containsValue);
	}

	public void testStyleGetStyle1() {
		final Map style = this.getAppleRuleStyle();
		final StylePropertyValue value = (StylePropertyValue) style.get(ZEBRA_PROPERTY);
		Test.assertNull(value);
	}

	public void testStylePutStyle0() {
		final Map style = this.getAppleRuleStyle();
		final int sizeBefore = style.size();

		final StylePropertyValue value = new StylePropertyValue();
		value.setString("underline");
		final StylePropertyValue replaced = (StylePropertyValue) style.put(TEXT_DECORATION_PROPERTY, value);

		Test.assertNull("The replaced value should be null", replaced);

		final int sizeAfter = style.size();
		Test.assertTrue("Size should have increased by at least one, sizeBefore: " + sizeBefore + ", sizeAfter: " + sizeAfter,
				sizeBefore < sizeAfter || sizeBefore == sizeAfter);

		if (false == Window.confirm("Is apple area text now underlined ?")) {
			Test.fail("Unable to add a style to the apple rule to make text underlined");
		}

		final Object removed = style.remove(TEXT_DECORATION_PROPERTY);
	}

	public void testStylePutStyle1() {
		final Map style = this.getAppleRuleStyle();
		final int sizeBefore = style.size();

		final StylePropertyValue value = new StylePropertyValue();
		value.setString("underline");
		final StylePropertyValue replaced0 = (StylePropertyValue) style.put(TEXT_DECORATION_PROPERTY, value);
		Test.assertNull(replaced0);

		final StylePropertyValue replaced1 = (StylePropertyValue) style.put(TEXT_DECORATION_PROPERTY, value);
		Test.assertNotNull(replaced1);

		final int sizeAfter = style.size();
		Test.assertTrue("Size should have increased by at least one, sizeBefore: " + sizeBefore + ", sizeAfter: " + sizeAfter,
				sizeBefore < sizeAfter || sizeBefore == sizeAfter);

		if (false == Window.confirm("Is apple area text now underlined ?")) {
			Test.fail("Unable to add a style to the apple rule to make text underlined");
		}

		final Object removed = style.remove(TEXT_DECORATION_PROPERTY);
	}

	public void testStyleRemoveStyle() {
		final Map style = this.getAppleRuleStyle();
		final int sizeBefore = style.size();

		final StylePropertyValue value = (StylePropertyValue) style.get(BACKGROUND_COLOUR_PROPERTY);

		style.remove(BACKGROUND_COLOUR_PROPERTY);

		if (false == Window.confirm("Is the background of the apple area white and not yellow ?")) {
			Test.fail("Unable to remove apple rule backgroundColour style");
		}

		final int sizeAfter = style.size();
		Test.assertTrue("Size should have decreased by at least one, sizeBefore: " + sizeBefore + ", sizeAfter: " + sizeAfter,
				sizeBefore > sizeAfter || sizeBefore == sizeAfter);
		// restore
		style.put(BACKGROUND_COLOUR_PROPERTY, value);
	}

	public void testStyleKeySetSize() {
		final Map style = this.getAppleRuleStyle();
		final Set keys = style.keySet();
		final int size = keys.size();
		final int expectedCount = style.size();
		Test.assertEquals("StyleKeySet size", size, expectedCount);
	}

	public void testStyleKeySetIsEmpty() {
		final Map style = this.getAppleRuleStyle();
		final Set keys = style.keySet();
		Test.assertFalse("StyleKeySet is empty", keys.isEmpty());
	}

	public void testStyleKeySetContains0() {
		final Map style = this.getAppleRuleStyle();
		final Set keys = style.keySet();
		final boolean contains = keys.contains(BACKGROUND_COLOUR_PROPERTY);
		Test.assertEquals("StyleKeySet.contains( \"" + BACKGROUND_COLOUR_PROPERTY + "\" )", contains, true);
	}

	public void testStyleKeySetContains1() {
		final Map style = this.getAppleRuleStyle();
		final Set keys = style.keySet();
		final boolean contains = keys.contains(ZEBRA_PROPERTY);
		Test.assertEquals("StyleKeySet.contains( \"" + ZEBRA_PROPERTY + "\" )", contains, false);
	}

	public void testStyleKeySetIterator() {
		final Map style = this.getAppleRuleStyle();
		final Set keys = style.keySet();
		final Iterator iterator = keys.iterator();
		int countedSize = 0;
		while (iterator.hasNext()) {
			iterator.next();
			countedSize++;
		}
		final int expectedCount = style.size();
		Test.assertEquals("StyleKeySet iterator. ", countedSize, expectedCount);
	}

	public void testStyleValuesCollectionSize() {
		final Map style = this.getAppleRuleStyle();
		final Collection values = style.values();
		final int size = values.size();
		final int expectedCount = style.size();
		Test.assertEquals("StyleKeySet size", size, expectedCount);
	}

	public void testStyleValuesCollectionIsEmpty() {
		final Map style = this.getAppleRuleStyle();
		final Collection values = style.values();
		Test.assertFalse("StyleValuesCollection is empty", values.isEmpty());
	}

	public void testStyleValuesCollectionContains0() {
		final Map style = this.getAppleRuleStyle();
		final Collection values = style.values();

		final StylePropertyValue value = new StylePropertyValue();
		value.setString(PADDING_VALUE);
		style.put(Css.PADDING, value);

		final boolean contains = values.contains(value);
		Test.assertTrue("StyleValuesCollection.contains( \"" + value + "\" )", contains);
	}

	public void testStyleValuesCollectionContains1() {
		final Map style = this.getAppleRuleStyle();
		final Collection values = style.values();

		final StylePropertyValue value = new StylePropertyValue();
		value.setString("" + System.currentTimeMillis());

		final boolean contains = values.contains(value);
		Test.assertFalse("StyleValuesCollection.contains( \"" + value + "\" )", contains);
	}

	public void testStyleValuesCollectionIterator() {
		final Map style = this.getAppleRuleStyle();
		final Collection values = style.values();
		final Iterator iterator = values.iterator();
		int countedSize = 0;
		while (iterator.hasNext()) {
			iterator.next();
			countedSize++;
		}
		final int expectedCount = style.size();
		Test.assertEquals("StyleValuesCollection iterator", countedSize, expectedCount);
	}

	class StylePropertyValueTestBuilder implements TestBuilder {
		public List buildCandidates() {
			final List tests = new ArrayList();

			tests.add(new Test() {

				public String getName() {
					return "testStylePropertyValueGetValue";
				}

				public void execute() {
					StyleSheetTest.this.testStylePropertyValueGetValue();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStylePropertyValueGetColour";
				}

				public void execute() {
					StyleSheetTest.this.testStylePropertyValueGetColour();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStylePropertyValueSetValue";
				}

				public void execute() {
					StyleSheetTest.this.testStylePropertyValueSetValue();
				}
			});
			tests.add(new Test() {

				public String getName() {
					return "testStylePropertyValueGetUnit";
				}

				public void execute() {
					StyleSheetTest.this.testStylePropertyValueGetUnit();
				}
			});

			return tests;
		}
	}

	public void testStylePropertyValueGetValue() {
		final StylePropertyValue value = (StylePropertyValue) this.getAppleRuleStyle().get(FONT_SIZE);
		Test.assertNotNull(value);

		final int fontSize = value.getInteger(CssUnit.PX);
		Test.assertEquals("apple rule style fontSize", fontSize, 10);
	}

	public void testStylePropertyValueGetColour() {
		final StylePropertyValue value = (StylePropertyValue) this.getAppleRuleStyle().get(BACKGROUND_COLOUR_PROPERTY);
		Test.assertNotNull(value);

		final Colour colour = value.getColour();
		final Colour expectedColour = new Colour(0xffffdd);
		Test.assertEquals("apple rule style backgroundColour(should be yellow)", colour, expectedColour);
	}

	public void testStylePropertyValueSetValue() {
		final StylePropertyValue value = (StylePropertyValue) this.getAppleRuleStyle().get(FONT_SIZE);
		Test.assertNotNull(value);

		final int fontSize = value.getInteger(CssUnit.PX);
		final int originalFontSize = 10;
		Test.assertEquals("apple rule style fontSize", fontSize, originalFontSize);

		final Map inlineStyles = this.getAppleRuleStyle();
		final int newFontSizeValue = 40;
		value.setInteger(newFontSizeValue, CssUnit.PX);
		inlineStyles.put(FONT_SIZE, value);

		if (false == Window.confirm("The apple area should now have text that is " + newFontSizeValue + "px in size (was "
				+ originalFontSize + "px)")) {
			Test.fail("The apple area text fontSize is not " + newFontSizeValue + "px");
		}
		value.setInteger(originalFontSize, CssUnit.PX);
		inlineStyles.put(FONT_SIZE, value);
	}

	public void testStylePropertyValueGetUnit() {
		final StylePropertyValue value = (StylePropertyValue) this.getAppleRuleStyle().get(FONT_SIZE);
		final CssUnit unit = value.getUnit();
		Test.assertEquals("apple rule style fontSize unit", unit, CssUnit.PX);
	}

	protected List getRules() {
		final StyleSheet styleSheet = (StyleSheet) StyleSheet.getStyleSheets().get(0);
		return styleSheet.getRules();
	}

	protected Rule getAppleRule() {
		return (Rule) this.getRules().get(APPLE_RULE_INDEX);
	}

	protected Map getAppleRuleStyle() {
		return (Map) this.getAppleRule().getStyle();
	}
}
