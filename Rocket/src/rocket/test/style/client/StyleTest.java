/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.test.style.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rocket.client.style.Rule;
import rocket.client.style.Style;
import rocket.client.style.StyleHelper;
import rocket.client.style.StylePropertyValue;
import rocket.client.style.StyleSheet;
import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * An interactive test which includes many buttons which the user may then use to
 * 
 * @author Miroslav Pokorny (mP)
 * 
 */
public class StyleTest implements EntryPoint {

    final static int STYLE_SHEET_COUNT = 1;

    final static String APPLE_TEXT = "This area is styled with the \"apple\" class...";

    final static int APPLE_RULE_INDEX = 3;

    final static String APPLE_CLASS_NAME = "apple";

    final static int REVERSE_APPLE_RULE_INDEX = 4;

    final static String REVERSE_APPLE_CLASS_NAME = "reverseApple";

    final static String BANANA_TEXT = "This area is styled with the \"banana\" class...";

    /*
     * The BANANA_RULES index will become 5 when it is added to the end of the RulesCollection
     */
    final static int BANANA_RULE_INDEX = 5;

    final static String BANANA_CLASS_NAME = "banana";

    final static String CARROT_TEXT = "This area is styled with the \"carrot\" class...";

    final static String CARROT_CLASS_NAME = "carrot";

    /**
     * The initial stylesheet contains three rules (div/body/button/apple/reverseApple).
     */
    final static int RULE_COUNT = REVERSE_APPLE_RULE_INDEX + 1;

    final String PADDING = "padding";

    final String PADDING_VALUE = "20px";

    final String TEXT_DECORATION_PROPERTY = "textDecoration";

    final String ZEBRA_PROPERTY = "zebra";

    final String BACKGROUND_COLOUR_PROPERTY = "backgroundColor";

    final String COLOUR_PROPERTY = "color";

    final String FONT_SIZE = "fontSize";

    final String FONT_SIZE_VALUE = "40px";

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                caught.printStackTrace();

                final String text = "FAIL Caught:" + caught + "\nmessage[" + caught.getMessage() + "]";
                StyleTest.this.log(text);
            }
        });

        final RootPanel rootPanel = RootPanel.get();

        rootPanel.add(this.createLabel(APPLE_TEXT, APPLE_CLASS_NAME));
        rootPanel.add(this.createLabel(BANANA_TEXT, BANANA_CLASS_NAME));
        rootPanel.add(this.createLabel(CARROT_TEXT, CARROT_CLASS_NAME));

        final Button testStyleSheets = new Button("TestStyleSheets(List)");
        testStyleSheets.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                StyleTest.this.testStyleSheets();
            }
        });
        rootPanel.add(testStyleSheets);

        final Button testStyleSheet = new Button("TestStyleSheet");
        testStyleSheet.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                StyleTest.this.testStyleSheet();
            }
        });
        rootPanel.add(testStyleSheet);

        final Button testRuleList = new Button("TestRules(List)");
        testRuleList.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                StyleTest.this.testRules();
            }
        });
        rootPanel.add(testRuleList);

        final Button testRule = new Button("TestRule");
        testRule.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                StyleTest.this.testRule();
            }
        });
        rootPanel.add(testRule);

        final Button testSelector = new Button("TestSelector");
        testSelector.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                StyleTest.this.testRuleSelector();
            }
        });
        rootPanel.add(testSelector);

        final Button testRuleStyle = new Button("TestStyle(Map)");
        testRuleStyle.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                StyleTest.this.testRuleStyle();
            }
        });
        rootPanel.add(testRuleStyle);

        final Button testStylePropertyValueGetColour = new Button("TestStylePropertyValue.getColour()");
        testStylePropertyValueGetColour.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                StyleTest.this.testStylePropertyValueGetColour();
            }
        });
        rootPanel.add(testStylePropertyValueGetColour);

        final Button testAll = new Button("TestAll");
        testAll.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                StyleTest.this.testStyleSheets();
                StyleTest.this.testStyleSheet();
                StyleTest.this.testRules();
                StyleTest.this.testRule();
                StyleTest.this.testRuleSelector();
                StyleTest.this.testRuleStyle();
                StyleTest.this.testStylePropertyValueGetColour();

                log("SUCCESS All tests...");
                rootPanel.remove(testAll);
            }
        });
        rootPanel.add(testAll);

        rootPanel.add(new HTML("<br>"));
    }

    protected Widget createLabel(final String text, final String styleName) {
        StringHelper.checkNotEmpty("parameter:text", text);
        StringHelper.checkNotEmpty("parameter:styleName", styleName);

        final Label label = new Label(text);
        label.setStyleName(styleName);
        final Element element = label.getElement();
        DOM.setStyleAttribute(element, "borderStyle", "dotted");
        DOM.setStyleAttribute(element, "borderWidth", "1px");
        DOM.setStyleAttribute(element, "borderColor", "black");
        return label;
    }

    /**
     * Adds a new log message to the log div's contents...
     * 
     * @param message
     */
    protected void log(final String message) {
        final Element log = DOM.getElementById("log");
        DOM.setInnerHTML(log, DOM.getInnerHTML(log) + message + "<br>");
    }

    /**
     * This test involves testing the behaviour of the StyleSheetCollection.
     */
    protected void testStyleSheets() {
        final List styleSheets = StyleHelper.getStyleSheets();
        PrimitiveHelper.checkEquals("StyleSheetsCollection.size", 1, styleSheets.size());
        
        final List styleSheets0 = StyleHelper.getStyleSheets();
        ObjectHelper.checkSame( "repeated styleSheets.get(0)", styleSheets, styleSheets0);

        // test all public methods of the StyleSheets list.

        // size
        PrimitiveHelper.checkEquals("styleSheets.size()", styleSheets.size(), STYLE_SHEET_COUNT);
        this.log("PASS styleSheets.size()");

        // isEmpty
        PrimitiveHelper.checkBoolean("rules.isEmpty()", styleSheets.isEmpty(), false);
        this.log("PASS styleSheets.isEmpty()");

        try {
            styleSheets.add(null);
            SystemHelper.handleAssertFailure("StyleSheet.add(Object) should have thrown an exception...");
        } catch (final Exception expected) {
        }
        this.log("PASS StyleSheets.add(Object)");

        try {
            styleSheets.add(0, null);
            SystemHelper.handleAssertFailure("StyleSheet.add(int,Object) should have thrown an exception...");
        } catch (final Exception expected) {
        }
        this.log("PASS StyleSheets.add(int,Object)");

        final StyleSheet styleSheet = (StyleSheet) styleSheets.get(0);
        ObjectHelper.checkNotNull("styleSheet(0)", styleSheet);
        this.log("PASS StyleSheets.get( 0 )");

        final StyleSheet styleSheet0 = (StyleSheet) styleSheets.get(0);
        ObjectHelper.checkSame("repeated StyleSheets.get(0)", styleSheet, styleSheet0 );
        this.log("PASS StyleSheets.get( 0 ) returns same wrapper");
        
        try {
            final StyleSheet styleSheet100 = (StyleSheet) styleSheets.get(100);
            SystemHelper
                    .handleAssertFailure("StyleSheet.get(100) should have thrown an exception because the index value is invalid...and not returned "
                            + styleSheet100);
        } catch (final Exception expected) {
        }
        this.log("PASS StyleSheet.get(invalidIndex)");

        try {
            styleSheets.set(0, null);
            SystemHelper.handleAssertFailure("StyleSheet.set()");
        } catch (final Exception expected) {
        }
        this.log("PASS StyleSheets.set( 0, Object )");

        // try {
        // styleSheets.remove(null);
        // SystemHelper.handleAssertFailure("StyleSheet.remove(Object) should have thrown an exception...");
        // } catch (final Exception expected) {
        // }
        try {
            styleSheets.remove(0);
            SystemHelper.handleAssertFailure("StyleSheet.remove(int) should have thrown an exception...");
        } catch (final Exception expected) {
        }
        this.log("PASS StyleSheets.remove( int )");

        this.log("SUCCESS StyleSheetCollection");
    }

    /**
     * This test involves accessing the first stylesheet and retrieving the embedded RuleList which is then tested.
     */
    protected void testStyleSheet() {
        final StyleSheet styleSheet = (StyleSheet) StyleHelper.getStyleSheets().get(0);
        this.log( "DEBUG got stylesheet");
        
        final List rules = styleSheet.getRules();
        ObjectHelper.checkNotNull("styleSheet.rules", rules);
        this.log("PASS StyleSheet.getRules()");
        this.log("SUCCESS StyleSheet");
    }

    /**
     * This test involves testing the rule collection belonging to the first stylesheet.
     */
    protected void testRules() {
        final StyleSheet styleSheet = (StyleSheet) StyleHelper.getStyleSheets().get(0);
        this.log( "DEBUG got stylesheet");
        final List rules = styleSheet.getRules();
        this.log( "DEBUG got rules");
        
        this.testRulesSize(rules);
        this.testRulesIsEmpty(rules);
        this.testRulesGet0(rules);
        this.testRulesGet1(rules);
        this.testRulesContains(rules);
        this.testRulesIndexOf(rules);
        this.testRulesIterator(rules);

        this.log("SUCCESS testRules");
    }

    protected void testRulesSize(final List rules) {
        ObjectHelper.checkNotNull("parameter:rules", rules);

        PrimitiveHelper.checkEquals("rules.size()", rules.size(), RULE_COUNT);
        this.log("PASS rules.size()");
    }

    protected void testRulesIsEmpty(final List rules) {
        ObjectHelper.checkNotNull("parameter:rules", rules);

        PrimitiveHelper.checkBoolean("rules.isEmpty()", rules.isEmpty(), false);
        this.log("PASS rules.isEmpty()");
    }

    protected void testRulesGet0(final List rules) {
        ObjectHelper.checkNotNull("parameter:rules", rules);

        final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
        ObjectHelper.checkNotNull("appleRule", rule);
        StringHelper.checkEquals("appleRuleSelector", rule.getSelector(), "." + APPLE_CLASS_NAME);

        this.log("PASS rules.get()");
    }

    protected void testRulesGet1(final List rules) {
        ObjectHelper.checkNotNull("parameter:rules", rules);

        try {
            final Rule someRule = (Rule) rules.get(100);
            SystemHelper
                    .handleAssertFailure("An exception should have been thrown when attempting to retrieving a Rule using an invalid index and not: "
                            + someRule);
        } catch (final Exception expected) {
        }
        this.log("PASS rules.get( invalidIndex )");
    }

    protected void testRulesContains(final List rules) {
        ObjectHelper.checkNotNull("parameter:rules", rules);

        final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
        ObjectHelper.checkNotNull("appleRule", rule);

        final boolean found = rules.contains(rule);
        PrimitiveHelper.checkBoolean("rules.contains()", found, true);

        this.log("PASS rules.contains()");
    }

    protected void testRulesIndexOf(final List rules) {
        ObjectHelper.checkNotNull("parameter:rules", rules);

        final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
        ObjectHelper.checkNotNull("appleRule", rule);

        final int index = rules.indexOf(rule);
        PrimitiveHelper.checkEquals("appleIndex", index, APPLE_RULE_INDEX);

        this.log("PASS rules.indexOf()");
    }

    protected void testRulesIterator(final List rules) {
        ObjectHelper.checkNotNull("parameter:rules", rules);
        final Iterator iterator = rules.iterator();
        ObjectHelper.checkNotNull("rules.iterator", iterator);

        final String[] selectors = new String[] { "body", "div", "button", ".apple", ".reverseApple" };
        for (int i = 0; i < selectors.length; i++) {
            PrimitiveHelper.checkBoolean("rules.iterator.hasNext() for element " + i, iterator.hasNext(), true);

            final Rule iteratorRule = (Rule) iterator.next();
            final String selector = iteratorRule.getSelector();
            StringHelper.checkEquals("rule(" + i + ")selector", selector.toLowerCase(), selectors[i].toLowerCase());
        }

        PrimitiveHelper.checkBoolean("rules.iterator.hasNext() for exhausted iterator", iterator.hasNext(), false);

        this.log("PASS iterator(), iterator.hasNext(), iterator.next()");
    }

    /**
     * This method tests the rule / rule collection modification methods using a wizard like approach to ask the user a question requiring a
     * button to be clicked to advance to the next step.
     */
    protected void testRule() {
        final StyleSheet styleSheet = (StyleSheet) StyleHelper.getStyleSheets().get(0);
        this.log( "DEBUG got stylesheet");
        final List rules = styleSheet.getRules();
        this.log( "DEBUG got rules");
        
        // Rules.add
        final Rule rule = new Rule();
        rule.setSelector("." + BANANA_CLASS_NAME);
        rules.add(rule);

        this.log( "DEBUG rule added");
        
        // test that the rule size has increased by one...
        PrimitiveHelper.checkEquals("rules", rules.size(), RULE_COUNT + 1);

        // verify that it was appended by checking bananaRules index.
        final int index = rules.indexOf(rule);
        PrimitiveHelper.checkEquals("bananaRule indexOf ", index, RULE_COUNT);

        // update the style portion of the rule...
        final Style style = rule.getStyle();
        style.setCssText("color: orange; background-color: purple;");

        this.log( "DEBUG style.cssText set.");
        
        // ask the user to confirm that the banana widget now has orange text on a purple background...
        final RootPanel rootPanel = RootPanel.get();
        final Button continueButton = new Button("Continue");
        continueButton.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                rootPanel.remove(continueButton);
                StyleTest.this.testRule0(rule);

            }
        });
        rootPanel.add(continueButton);

        this
                .log("CONFIRM Please confirm that the banana style area contains orange text on a purple background after adding the \"banana\" rule, by clicking \"Continue\"...");
    }

    /**
     * This method should be invoked after completing {@link #rule}. It performs additional tests after removing the given rule...
     */
    protected void testRule0(final Rule rule) {
        ObjectHelper.checkNotNull("parameter:rule", rule);

        this.log("PASS rules.add( Rule )");

        // remove the rule...
        final StyleSheet styleSheet = (StyleSheet) StyleHelper.getStyleSheets().get(0);
        final List rules = styleSheet.getRules();

        final boolean removed = rules.remove(rule);
        PrimitiveHelper.checkBoolean("rule removed", removed, true);

        // test that the rule size has returned back to normal.
        PrimitiveHelper.checkEquals("rules", rules.size(), RULE_COUNT);

        // verify that it was appended by checking bananaRules index.
        final int index = rules.indexOf(rule);
        PrimitiveHelper.checkEquals("indexOf( bananaRule ) should return to -1 because the rule was removed... ",
                index, -1);

        final RootPanel rootPanel = RootPanel.get();
        final Button continueButton = new Button("Continue");
        continueButton.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                rootPanel.remove(continueButton);
                StyleTest.this.testRule1();

            }
        });
        rootPanel.add(continueButton);

        this
                .log("CONFIRM Click on [continue] to confirm the banana style area has reverted back to black text on a white background (inherited from body)");
    }

    protected void testRule1() {
        this.log("SUCCESS testRule");
    }

    protected void testRuleSelector() {
        this.testRuleGetSelector();
        this.testRuleSetSelector0();
    }

    protected void testRuleGetSelector() {
        final StyleSheet styleSheet = (StyleSheet) StyleHelper.getStyleSheets().get(0);
        this.log( "DEBUG got stylesheet");
        
        final List rules = styleSheet.getRules();
        this.log( "DEBUG got rules");
        
        final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
        this.log( "DEBUG got rule");
        
        final String selectorValue = rule.getSelector();
        StringHelper.checkEquals("rule.selector", selectorValue, "." + APPLE_CLASS_NAME);

        this.log("PASS Rule.getSelector()");
    }

    protected void testRuleSetSelector0() {
        final StyleSheet styleSheet = (StyleSheet) StyleHelper.getStyleSheets().get(0);
        this.log( "DEBUG got stylesheet");
        
        final List rules = styleSheet.getRules();
        this.log( "DEBUG got rules");
        
        final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
        this.log( "DEBUG got rule");
        
        // change the selector text from to carrot.
        rule.setSelector("." + CARROT_CLASS_NAME);
        this.log("DEBUG Changed apple rule selector from \"." + APPLE_CLASS_NAME + "\" to \"" + CARROT_CLASS_NAME + "\"");

        // rule size shouldnt have changed...
        PrimitiveHelper.checkEquals("rules", rules.size(), RULE_COUNT);
        this.log("PASS rule count has not changed.");

        // index should not have changed...
        final int index = rules.indexOf(rule);
        PrimitiveHelper.checkEquals("The index of rule", index, APPLE_RULE_INDEX);
        this.log("PASS rule index remains constant.");

        // prompt the user to confirm that the selector was updated...
        final RootPanel rootPanel = RootPanel.get();
        final Button continueButton = new Button("Continue");
        continueButton.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                rootPanel.remove(continueButton);
                StyleTest.this.testRuleSetSelector1(rule);

            }
        });
        rootPanel.add(continueButton);

        this.log("CONFIRM Click [Continue] to confirm that the apple style area has black text on a white backtround "
                + "and that the carrot style area has green text on a red background.");
    }

    protected void testRuleSetSelector1(final Rule rule) {
        ObjectHelper.checkNotNull("parameter:rule", rule);

        rule.setSelector("." + APPLE_CLASS_NAME);

        final RootPanel rootPanel = RootPanel.get();
        final Button continueButton = new Button("Continue");
        continueButton.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                rootPanel.remove(continueButton);
                StyleTest.this.log("SUCCESS Rule.selector changed and changed back successfully...");
            }
        });
        rootPanel.add(continueButton);
        this
                .log("CONFIRM Click [Continue] to confirm that the apple style area has reverted back to having green text on a red background"
                        + "and that the carrot style area has black text on a white background.");
    }

    /**
     * The first part of the testing the style object involves accessing various read methods and verifying their result using the Style
     * object belonging to the apple Rule.
     */
    protected void testRuleStyle() {
        final StyleSheet styleSheet = (StyleSheet) StyleHelper.getStyleSheets().get(0);
        this.log( "DEBUG got stylesheet");
        
        final List rules = styleSheet.getRules();
        this.log( "DEBUG got rules");
        
        final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
        this.log( "DEBUG got rule");
        
        final Style style = rule.getStyle();
        this.log( "DEBUG got style");
        
        this.testRuleStyleContainsKey0(style);
        this.testRuleStyleContainsKey1(style);
        this.testRuleStyleContainsValue0(style);
        this.testRuleStyleContainsValue1(style);
        this.testRuleStyleGet0(style);
        this.testRuleStyleGet1(style);

        final Set keys = style.keySet();
        this.testRuleStyleKeySetContains0(keys);
        this.testRuleStyleKeySetContains1(keys);
        this.testRuleStyleKeySetIterator(keys);

        final Collection values = style.values();
        this.testRuleStyleValuesSize(values);
        this.testRuleStyleValuesIsEmpty(values);
        this.testRuleStyleValuesContains0(values);
        this.testRuleStyleValuesContains1(values);
        this.testRuleStyleValuesIterator(values);

        this.testRuleStylePutAndRemove0(style);       
    }

    protected void testRuleStyleContainsKey0(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final boolean containsKey = style.containsKey(BACKGROUND_COLOUR_PROPERTY);
        PrimitiveHelper.checkBoolean("Style.containsKey( \"" + BACKGROUND_COLOUR_PROPERTY + "\" )", containsKey, true);
        this.log("PASS Style.containsKey( existingKey )");
    }

    protected void testRuleStyleContainsKey1(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final boolean containsKey = style.containsKey(ZEBRA_PROPERTY);
        PrimitiveHelper.checkBoolean("Style.containsKey( \"" + ZEBRA_PROPERTY + "\" )", containsKey, false);
        this.log("PASS Style.containsKey( nonExistantKey )");
    }

    protected void testRuleStyleContainsValue0(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        value.setString(PADDING_VALUE);
        final boolean containsValue = style.containsValue(value);
        PrimitiveHelper.checkBoolean("Style.containsValue(\"" + PADDING_VALUE + "\")", containsValue, true);
        this.log("PASS Style.containsValue( existingValue )");
    }

    protected void testRuleStyleContainsValue1(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        value.setString("xxx");
        final boolean containsValue = style.containsValue(value);
        PrimitiveHelper.checkBoolean("Style.containsValue(\"xxx\")", containsValue, false);
        this.log("PASS Style.containsValue( nonExistantValue )");
    }

    protected void testRuleStyleGet0(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue propertyValue = (StylePropertyValue) style.get(ZEBRA_PROPERTY);
        ObjectHelper.checkNull("Style.get(\"" + ZEBRA_PROPERTY + "\")", propertyValue);
        this.log("PASS Style.get( nonExistantKey )");
    }

    protected void testRuleStyleGet1(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue propertyValue = (StylePropertyValue) style.get(PADDING);
        ObjectHelper.checkNotNull("Style.get(\"" + PADDING + "\")", propertyValue);
        this.log("PASS Style.get( existingKey )");
    }

    protected void testRuleStyleStylePropertyValueGetUnit(final StylePropertyValue value) {
        ObjectHelper.checkNotNull("parameter:value", value);

        final String unit = value.getUnit();
        StringHelper.checkEquals("padding unit", unit, "px");
        this.log("PASS StylePropertyValue().getUnit()");
    }

    protected void testRuleStyleKeySetContains0(final Set keys) {
        ObjectHelper.checkNotNull("parameter:keys", keys);

        final boolean found = keys.contains(PADDING);
        PrimitiveHelper.checkBoolean("Style.keySet().contains(" + PADDING + ")", found, true);
        this.log("PASS Style.keySet().contains( existingKey )");
    }

    protected void testRuleStyleKeySetContains1(final Set keys) {
        final boolean found = keys.contains(ZEBRA_PROPERTY);
        PrimitiveHelper.checkBoolean("Style.contains(" + ZEBRA_PROPERTY + ")", found, false);
        this.log("PASS Style.keySet().contains( nonExistantKey )");
    }

    protected void testRuleStyleKeySetIterator(final Set keys) {
        ObjectHelper.checkNotNull("parameter:keys ", keys);

        final Iterator iterator = keys.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }

        PrimitiveHelper.checkGreaterThan("keySet.iterator.entry count", count, 4);
        this.log("PASS Style.keySet().iterator()");
    }

    protected void testRuleStyleValuesSize(final Collection values) {
        ObjectHelper.checkNotNull("parameter:values", values);

        final int size = values.size();
        PrimitiveHelper.checkGreaterThanOrEqual("Style.values.size", size, 4);
        this.log("PASS Style.values().size()");

    }

    protected void testRuleStyleValuesIsEmpty(final Collection values) {
        ObjectHelper.checkNotNull("parameter:values", values);

        final boolean empty = values.isEmpty();
        PrimitiveHelper.checkBoolean("Style.values.isEmpty", empty, false);
        this.log("PASS Style.values().isEmpty()");
    }

    protected void testRuleStyleValuesContains0(final Collection values) {
        ObjectHelper.checkNotNull("parameter:values", values);

        final StylePropertyValue value = new StylePropertyValue();
        value.setString(PADDING_VALUE);
        final boolean containsValue = values.contains(value);
        PrimitiveHelper.checkBoolean("Style.containsValue(\"" + PADDING_VALUE + "\")", containsValue, true);
        this.log("PASS Style.values().contains( existingValue )");
    }

    protected void testRuleStyleValuesContains1(final Collection values) {
        ObjectHelper.checkNotNull("parameter:values", values);

        final StylePropertyValue value = new StylePropertyValue();
        value.setString("xxx");
        final boolean containsValue = values.contains(value);
        PrimitiveHelper.checkBoolean("Style.containsValue(\"xxx\")", containsValue, false);
        this.log("PASS Style.values().contains( nonExistantValue )");
    }

    protected void testRuleStyleValuesIterator(final Collection values) {
        ObjectHelper.checkNotNull("parameter:values", values);

        boolean found = false;
        final Iterator iterator = values.iterator();
        while (iterator.hasNext()) {
            final StylePropertyValue value = (StylePropertyValue) iterator.next();
            try {
                found = value.getString().equals(PADDING_VALUE);
                if (found) {
                    found = true;
                    break;
                }
            } finally {
                value.destroy();
            }
        }
        PrimitiveHelper.checkBoolean("Style.values().iterator()", found, true);
        this.log("PASS Style.iterator()");
    }

    protected void testRuleStylePutAndRemove0(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        value.setString(FONT_SIZE_VALUE);
        style.put(FONT_SIZE, value);

        final RootPanel rootPanel = RootPanel.get();
        final Button continueButton = new Button("Continue");
        continueButton.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                rootPanel.remove(continueButton);
                StyleTest.this.log("PASS Style.put()");
                StyleTest.this.testRuleStylePutAndRemove1(style);
            }
        });
        rootPanel.add(continueButton);
        this.log("CONFIRM Click [Continue] to confirm that the apple style area [" + FONT_SIZE + "] is now large/["
                + FONT_SIZE_VALUE + "]");
    }

    protected void testRuleStylePutAndRemove1(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        style.remove(FONT_SIZE);
        this.log("DEBUG removed style[" + FONT_SIZE + "]");

        final RootPanel rootPanel = RootPanel.get();
        final Button continueButton = new Button("Continue");
        continueButton.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                rootPanel.remove(continueButton);
                StyleTest.this.log("PASS Style.remove()");
                StyleTest.this.testRuleStylePutAndRemove2(style);
            }
        });
        rootPanel.add(continueButton);
        this
                .log("CONFIRM Click [Continue] to confirm that the apple style area text has reverted back to its original size.");
    }

    protected void testRuleStylePutAndRemove2(final Style style) {
        this.testRuleStyleKeySetIteratorRemove0(style);
    }

    protected void testRuleStyleKeySetIteratorRemove0(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        value.setString(FONT_SIZE_VALUE);
        style.put(FONT_SIZE, value);
        this.log("DEBUG Added style[" + FONT_SIZE + "]=[" + FONT_SIZE_VALUE + "]");

        final Iterator keys = style.keySet().iterator();
        while (keys.hasNext()) {
            final String key = (String) keys.next();
            if (key.equals(FONT_SIZE)) {
                this.log("DEBUG Style.keySet().iterator() found key[" + FONT_SIZE + "]");
                keys.remove();
                break;
            }
        }

        final boolean containsKey = style.containsKey(FONT_SIZE);
        PrimitiveHelper.checkBoolean("iterator.remove()", containsKey, false);
        style.remove(FONT_SIZE);
        this.log("DEBUG removed style[" + FONT_SIZE + "]");

        final RootPanel rootPanel = RootPanel.get();
        final Button continueButton = new Button("Continue");
        continueButton.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                rootPanel.remove(continueButton);
                StyleTest.this.log("PASS Style.remove()");
                StyleTest.this.testRuleStyleKeySetIteratorRemove1(style);
            }
        });
        rootPanel.add(continueButton);
        this
                .log("CONFIRM Click [Continue] to confirm that the apple style area text has reverted back to its original size.");
    }

    protected void testRuleStyleKeySetIteratorRemove1(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        testRuleStyleValuesCollectionIteratorRemove0(style);
    }

    protected void testRuleStyleValuesCollectionIteratorRemove0(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

            final StylePropertyValue value = new StylePropertyValue();
            value.setString(FONT_SIZE_VALUE);
            style.put(FONT_SIZE, value);
            this.log("DEBUG Added style[" + FONT_SIZE + "]=[" + FONT_SIZE_VALUE + "]");

        final Iterator values = style.values().iterator();
        while (values.hasNext()) {
            final StylePropertyValue iteratorValue = (StylePropertyValue) values.next();
            try{
                if (iteratorValue.getPropertyName().equals(FONT_SIZE)) {
                    values.remove();                
                    break;
                }   
            }finally {
                iteratorValue.destroy();
            }
        }

        final boolean containsKey = style.containsKey(FONT_SIZE);
        PrimitiveHelper.checkBoolean("iterator.remove()", containsKey, false);
        style.remove(FONT_SIZE);

        final RootPanel rootPanel = RootPanel.get();
        final Button continueButton = new Button("Continue");
        continueButton.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                rootPanel.remove(continueButton);
                StyleTest.this.log("PASS Style.remove()");
                StyleTest.this.testRuleStyleValuesCollectionIteratorRemove1(style);
            }
        });
        rootPanel.add(continueButton);
        this
                .log("CONFIRM Click [Continue] to confirm that the apple style area text has reverted back to its original size.");
    }

    protected void testRuleStyleValuesCollectionIteratorRemove1(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);
        
        this.log( "SUCCESS Style");
    }

    protected void testStylePropertyValueGetColour() {
        final StyleSheet styleSheet = (StyleSheet) StyleHelper.getStyleSheets().get(0);
        this.log( "DEBUG got stylesheet");
        
        final List rules = styleSheet.getRules();
        this.log( "DEBUG got rules");
        
        final Rule rule = (Rule) rules.get(APPLE_RULE_INDEX);
        this.log( "DEBUG got rule");
        
        final Style style = rule.getStyle();
        this.log( "DEBUG got style");
        
        this.testRuleStyleGetColourHashRGB0(style);
        this.testRuleStyleGetColourHashRGB1(style);
        this.testRuleStyleGetColourHashRRGGBB0(style);
        this.testRuleStyleGetColourHashRRGGBB1(style);
        this.testRuleStyleGetColourNamedColour0(style);
        this.testRuleStyleGetColourNamedColour1(style);
        this.testRuleStyleGetColourRgbTriplet0(style);
        this.testRuleStyleGetColourRgbTriplet1(style);

        this.log("SUCCESS StylePropertyValue.getColour()");
    }

    protected void testRuleStyleGetColourHashRGB0(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        final String input = "#123";
        value.setString(input);

        final int actual = value.getColour();
        final int expected = 0x112233;
        PrimitiveHelper.checkEquals("Convertion of \"" + input + "\" to 0x" + expected + " via Style.getColour()",
                actual, expected);

        this.log("PASS StylePropertyValue.getColour(#rgb)");
    }

    protected void testRuleStyleGetColourHashRGB1(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        final String input = "#0fe";
        value.setString(input);

        final int actual = value.getColour();
        final int expected = 0x00ffee;
        PrimitiveHelper.checkEquals("Convertion of \"" + input + "\" to 0x" + expected + " via Style.getColour()",
                actual, expected);

        this.log("PASS StylePropertyValue.getColour(#rgb)");
    }

    protected void testRuleStyleGetColourHashRRGGBB0(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        final String input = "#123456";
        value.setString(input);

        final int actual = value.getColour();
        final int expected = 0x123456;
        PrimitiveHelper.checkEquals("Convertion of \"" + input + "\" to 0x" + expected + " via Style.getColour()",
                actual, expected);

        this.log("PASS StylePropertyValue.getColour(#rrggbb)");
    }

    protected void testRuleStyleGetColourHashRRGGBB1(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        final String input = "#00ffed";
        value.setString(input);

        final int actual = value.getColour();
        final int expected = 0x00ffed;
        PrimitiveHelper.checkEquals("Convertion of \"" + input + "\" to 0x" + expected + " via Style.getColour()",
                actual, expected);

        this.log("PASS StylePropertyValue.getColour(#rrggbb)");
    }

    protected void testRuleStyleGetColourNamedColour0(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        final String input = "white";
        value.setString(input);

        final int actual = value.getColour();
        final int expected = 0xffffff;
        PrimitiveHelper.checkEquals("Convertion of \"" + input + "\" to 0x" + expected + " via Style.getColour()",
                actual, expected);

        this.log("PASS StylePropertyValue.getColour(named)");
    }

    protected void testRuleStyleGetColourNamedColour1(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        final String input = "red";
        value.setString(input);

        final int actual = value.getColour();
        final int expected = 0xff0000;
        PrimitiveHelper.checkEquals("Convertion of \"" + input + "\" to 0x" + expected + " via Style.getColour()",
                actual, expected);

        this.log("PASS StylePropertyValue.getColour(named)");
    }

    protected void testRuleStyleGetColourRgbTriplet0(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        final String input = "rgb(12,34,56)";
        value.setString(input);

        final int actual = value.getColour();
        final int expected = 12 * 0x10000 + 34 * 0x100 + 56;
        PrimitiveHelper.checkEquals("Convertion of \"" + input + "\" to 0x" + expected + " via Style.getColour()",
                actual, expected);

        this.log("PASS StylePropertyValue.getColour( rgb(rr,gg,bb) )");
    }

    protected void testRuleStyleGetColourRgbTriplet1(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);

        final StylePropertyValue value = new StylePropertyValue();
        final String input = "rgb(0,1,234)";
        value.setString(input);

        final int actual = value.getColour();
        final int expected = 1 * 0x100 + 234;
        PrimitiveHelper.checkEquals("Convertion of \"" + input + "\" to 0x" + expected + " via Style.getColour()",
                actual, expected);

        this.log("PASS StylePropertyValue.getColour( rgb(rr,gg,bb) )");
    }

}