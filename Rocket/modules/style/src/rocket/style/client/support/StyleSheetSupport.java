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

import rocket.style.client.Rule;
import rocket.style.client.StyleSheet;
import rocket.util.client.Checker;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;

public class StyleSheetSupport {

	/**
	 * Retrieves the style sheet collection attached to this document
	 * 
	 * @param document
	 * @return
	 */
	native public JavaScriptObject getStyleSheetCollection(final Document document)/*-{
			 return document.styleSheets || null;
		}-*/;

	public int getRuleCount(final StyleSheet styleSheet) {
		Checker.notNull("parameter:styleSheet", styleSheet);

		return this.getRuleCount0(styleSheet);
	};

	native private int getRuleCount0(final StyleSheet styleSheet)/*-{
			return styleSheet.cssRules.length;
		}-*/;

	public Rule getRule(final StyleSheet styleSheet, final int index) {
		Checker.notNull("parameter:styleSheet", styleSheet);
		
		final Rule rule = this.getRule0(styleSheet, index);
		if (null == rule) {
			throw new IllegalArgumentException("The parameter:index is outside the range of actual stylesheets, index: " + index);
		}
		return rule;
	}

	native private Rule getRule0(final StyleSheet styleSheet, final int index)/*-{
			return styleSheet.cssRules[ index ];
		}-*/;

	public Rule addRule(final StyleSheet styleSheet, final String selectorText, final String styleText) {
		Checker.notNull("parameter:styleSheet", styleSheet);
		Checker.notNull("parameter:selectorText", selectorText);
		Checker.notNull("parameter:styleText", styleText);

		return this.addRule0(styleSheet, selectorText, styleText);
	}

	native private Rule addRule0(final StyleSheet styleSheet, final String selectorText, final String styleText)/*-{
			 var cssText = selectorText + "{" + styleText + "}";    
			 var index = styleSheet.cssRules.length;
			 styleSheet.insertRule( cssText, index );
			 return styleSheet.cssRules[ index ]; 
			 }-*/;

	public Rule insertRule(final StyleSheet styleSheet, final int index, final String selectorText, final String styleText) {
		Checker.notNull("parameter:styleSheet", styleSheet);
		Checker.notNull("parameter:selectorText", selectorText);
		Checker.notNull("parameter:styleText", styleText);

		return this.insertRule0(styleSheet, index, selectorText, styleText);
	}

	native private Rule insertRule0(final StyleSheet styleSheet, final int index, final String selectorText, final String styleText)/*-{
			 var cssText = selectorText + "{" + styleText + "}";
			 styleSheet.insertRule( cssText, index );
			 return styleSheet.cssRules[ index ];   
			 }-*/;

	public void removeRule(final StyleSheet styleSheet, final int index) {
		Checker.notNull("parameter:styleSheet", styleSheet);

		this.removeRule0(styleSheet, index);
	}

	/**
	 * Escapes to javascript to delete the requested rule.
	 */
	native private void removeRule0(final StyleSheet styleSheet, final int index) /*-{
	 styleSheet.deleteRule( index );
	 }-*/;

	public void normalize(final StyleSheet styleSheet) {
		Checker.notNull("parameter:styleSheet", styleSheet);
		this.normalize0(styleSheet);
	}

	native private void normalize0(final StyleSheet styleSheet) /*-{
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
