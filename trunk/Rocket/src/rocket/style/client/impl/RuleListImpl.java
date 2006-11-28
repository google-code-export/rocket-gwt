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
package rocket.style.client.impl;

import rocket.dom.client.DomObjectList;
import rocket.dom.client.DomObjectListElement;
import rocket.style.client.Rule;
import rocket.style.client.StyleSheet;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The standard implementation that provides the capability to manipulate the rules belonging to a styleSheet as a list.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RuleListImpl extends DomObjectList {

    public RuleListImpl() {
        super();
    }

    public JavaScriptObject getObject() {
        return this.getObject(this.getStyleSheet().getObject());
    }

    native protected JavaScriptObject getObject(final JavaScriptObject styleSheet) /*-{
     return styleSheet.cssRules ? styleSheet.cssRules : null ;
     }-*/;

    protected DomObjectListElement createWrapper(final int index) {
        final Rule rule = new Rule();
        rule.setParent(this);
        rule.setIndex(index);
        return rule;
    }

    protected boolean isValidType(Object wrapper) {
        return wrapper instanceof Rule;
    }

    protected void adopt(final DomObjectListElement object) {
        super.adopt(object);

        final Rule rule = (Rule) object;
        rule.getStyle().setRule(rule);
    }

    protected void add0(final DomObjectListElement element) {
        this.checkElementType(element);

        this.addRule((Rule) element);
    }

    protected void addRule(final Rule rule) {
        ObjectHelper.checkNotNull("parameter:rule", rule);

        final JavaScriptObject styleSheet = this.getStyleSheet().getObject();
        final String selector = rule.getSelector();
        final String style = rule.getStyle().getCssText();
        this.addRule0(styleSheet, selector, style);
    }

    protected native void addRule0(final JavaScriptObject styleSheet, final String selectorText, final String styleText)/*-{
     var cssText = selectorText + "{" + styleText + "}";    
     var index = styleSheet.cssRules.length;
     styleSheet.insertRule( cssText, index );
     }-*/;

    protected void insert0(final int index, final DomObjectListElement element) {
        this.checkElementType(element);

        this.insertRule(index, (Rule) element);
    }

    protected void insertRule(final int index, final Rule rule) {
        ObjectHelper.checkNotNull("parameter:rule", rule);

        final JavaScriptObject styleSheet = this.getStyleSheet().getObject();
        final String selector = rule.getSelector();
        final String style = rule.getStyle().getCssText();
        this.insertRule0(styleSheet, index, selector, style);
    }

    native protected void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText,
            final String styleText)/*-{
     var cssText = selectorText + "{" + styleText + "}";
     styleSheet.insertRule( cssText, index );   
     }-*/;

    protected void remove0(final DomObjectListElement element) {
        this.checkElementType(element);

        this.removeRule((Rule) element);
    }

    protected void removeRule(final Rule rule) {
        final JavaScriptObject styleSheet = this.getStyleSheet().getObject();
        final int index = rule.getIndex();
        this.removeRule0(styleSheet, index);
    }

    /**
     * Escapes to javascript to delete the requested rule.
     */
    native protected void removeRule0(final JavaScriptObject styleSheet, final int index) /*-{
     styleSheet.deleteRule( index );
     }-*/;

    /**
     * The parent stylesheet of this RulesCollection
     */
    private StyleSheet styleSheet;

    public StyleSheet getStyleSheet() {
        ObjectHelper.checkNotNull("field:styleSheet", styleSheet);
        return this.styleSheet;
    }

    public void setStyleSheet(final StyleSheet styleSheet) {
        ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
        this.styleSheet = styleSheet;
    }

    public void afterPropertiesSet() {
        this.normalizeRules();
    }

    /**
     * Normalizing rules is the process that takes a list of rules and expands rules that whose selector contains more than one individual
     * selector. For each selector a rule is duplicated with a single selector and the same original style in the same slot as the original
     * rule.
     */
    protected void normalizeRules() {
        normalizeRules0(this.getStyleSheet().getObject());
    }

    native protected void normalizeRules0(final JavaScriptObject styleSheet) /*-{
     var rules = styleSheet.cssRules;
     var i = 0;

     while( i < rules.length ){
     var rule = rules[ i ];
     var selectorText = rule.selectorText;
     var selectors = selectorText.split( "," );
     var selectorCount = selectors.length;
     if( 1 == selectorCount ){
     i++;   
     continue;
     }
     
     var styleText = rule.style.cssText;
     
     // delete the original rule...
     styleSheet.deleteRule( i );
     
     // recreate n rules one for each selector with the same style value...
     for( var j = 0; j < selectorCount; j++ ){
     var ruleText = selectors[ j ] + "{" + styleText + "}";
     styleSheet.insertRule( ruleText, i );
     i++;            
     } // for j
     } // while
     }-*/;
}
