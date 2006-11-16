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
package rocket.client.style.impl;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A specialised form of RuleListSupport that includes a change to handle the bug(???) in the implementation of StyleSheet.insertRule(
 * ruleAsText, cursor ) ignoring the cursor and appending the rule at the end of the rules list.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RuleListOpera9Impl extends RuleListImpl {

    public RuleListOpera9Impl() {
        super();
    }

    /**
     * This method takes care of appending the new rule and appending the rules that have an cursor greater than the given cursor.
     */
    native protected void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText,
            final String styleText)/*-{
     var cssText = selectorText + "{" + styleText + "}";
     
     var rules = styleSheet.cssRules;

     // inserting a rule into an Opera9 styleSheet really appends it...
     styleSheet.insertRule( cssText, rules.length );   
     
     // need to delete all the rules with a higher cursor and append them so eventually the new "inserted" rule will be in the correct slot...         
     var lastIndex = rules.length - 1;
     
     for( var j = cursor; j < lastIndex; j++ ){
     // remember the original selector/style 
     var rule = rules[ cursor ];
     var selector = rule.selectorText;
     var styleText = rule.style.cssText;
     
     styleSheet.deleteRule( cursor );
     
     // append the rule...
     var ruleText = selector + "{" + styleText + "}";
     styleSheet.insertRule( ruleText, rules.length );
     }
     
     // when this stage is reached the rules order should be correct.
     }-*/;

    native protected void normalizeRules0(final JavaScriptObject styleSheet) /*-{
     var rules = styleSheet.cssRules;
     var i = 0;
     
     // skip until a rule with more than one selector is found....
     while( i < rules.length ){
     var rule = rules[ i ];
     var selectorText = rule.selectorText;
     if( selectorText.indexOf( ",") == -1 ){
     i++;
     continue;
     }
     
     // found a rule with more than one selector...
     var ruleCount = rules.length - i;
     for( var j = 0; j < ruleCount; j++ ){
     var rule = rules[ i ];
     var selectorText = rule.selectorText;
     var selectors = selectorText.split( "," );
     var selectorCount = selectors.length;
     var styleText = rule.style.cssText;
     
     // delete the rule...
     styleSheet.deleteRule( i );
     
     // recreate the n rules one for each selector...
     for( var k = 0; k < selectorCount; k++ ){
     var cssText = selectors[ k ] + "{" + styleText + "}";
     
     // opera 9x inserts are really appends...
     styleSheet.insertRule( cssText, rules.length );
     }
     }         
     break;
     }
     }-*/;

}
