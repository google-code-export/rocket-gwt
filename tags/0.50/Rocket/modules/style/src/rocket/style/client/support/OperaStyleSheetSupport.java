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
package rocket.style.client.support;

import rocket.util.client.Checker;

import com.google.gwt.core.client.JavaScriptObject;

public class OperaStyleSheetSupport extends StyleSheetSupport {
	public void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText) {
		Checker.notNull("parameter:styleSheet", styleSheet);
		Checker.notNull("parameter:selectorText", selectorText);
		Checker.notNull("parameter:styleText", styleText);

		this.insertRule0(styleSheet, index, selectorText, styleText);
	}

	/**
	 * This method takes care of appending the new rule and appending the rules
	 * that have an index greater than the given index.
	 */
	native private void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText)/*-{
	 var cssText = selectorText + "{" + styleText + "}";
	 
	 var rules = styleSheet.cssRules;

	 // inserting a rule into an Opera9 styleSheet really appends it...
	 styleSheet.insertRule( cssText, rules.length );   
	 
	 // need to delete all the rules with a higher index and append them so eventually the new "inserted" rule will be in the correct slot...         
	 var lastIndex = rules.length - 1;
	 
	 for( var j = index; j < lastIndex; j++ ){
	 // remember the original selector/style 
	 var rule = rules[ index ];
	 var selector = rule.selectorText;
	 var styleText = rule.style.cssText;
	 
	 styleSheet.deleteRule( index );
	 
	 // append the rule...
	 var ruleText = selector + "{" + styleText + "}";
	 styleSheet.insertRule( ruleText, rules.length );
	 }
	 
	 // when this stage is reached the rules order should be correct.
	 }-*/;

	public void normalize(final JavaScriptObject styleSheet) {
		Checker.notNull("parameter:styleSheet", styleSheet);
		this.normalize0(styleSheet);
	}

	native private void normalize0(final JavaScriptObject styleSheet) /*-{
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
