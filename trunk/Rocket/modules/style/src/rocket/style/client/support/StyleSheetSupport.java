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

import rocket.style.client.Css;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;

public class StyleSheetSupport {
	/**
	 * Retrieves the style sheet collection attached to this document
	 * 
	 * @return
	 */
	native public JavaScriptObject getStyleSheetCollection()/*-{
	 var styleSheet = $doc.styleSheets;
	 return styleSheet ? styleSheet : null;
	 }-*/;
	
	/**
	 * Helper which retrieves the native rules collection that this instance is
	 * presenting as a List
	 * 
	 * @return
	 */
	public JavaScriptObject getRulesCollection(final JavaScriptObject styleSheet) {
		return ObjectHelper.getObject(styleSheet, Css.RULES_LIST_PROPERTY);
	}

	public void addRule(final JavaScriptObject styleSheet, final String selectorText, final String styleText) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		StringHelper.checkNotNull("parameter:selectorText", selectorText);
		StringHelper.checkNotNull("parameter:styleText", styleText);

		this.addRule0(styleSheet, selectorText, styleText);
	}

	native private void addRule0(final JavaScriptObject styleSheet, final String selectorText, final String styleText)/*-{
	 var cssText = selectorText + "{" + styleText + "}";    
	 var index = styleSheet.cssRules.length;
	 styleSheet.insertRule( cssText, index );
	 }-*/;

	public void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		StringHelper.checkNotNull("parameter:selectorText", selectorText);
		StringHelper.checkNotNull("parameter:styleText", styleText);

		this.insertRule0(styleSheet, index, selectorText, styleText);
	}

	native private void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText)/*-{
	 var cssText = selectorText + "{" + styleText + "}";
	 styleSheet.insertRule( cssText, index );   
	 }-*/;

	public void removeRule(final JavaScriptObject styleSheet, final int index) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);

		this.removeRule0(styleSheet, index);
	}

	/**
	 * Escapes to javascript to delete the requested rule.
	 */
	native private void removeRule0(final JavaScriptObject styleSheet, final int index) /*-{
	 styleSheet.deleteRule( index );
	 }-*/;

	public void normalize(final JavaScriptObject styleSheet) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		this.normalize0(styleSheet);
	}

	native private void normalize0(final JavaScriptObject styleSheet) /*-{
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
