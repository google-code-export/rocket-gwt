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
import rocket.util.client.JavaScript;
import rocket.util.client.Utilities;

import com.google.gwt.core.client.JavaScriptObject;

public class InternetExplorerStyleSheetSupport extends StyleSheetSupport {

	@Override
	public int getRuleCount( final StyleSheet styleSheet ){
		Checker.notNull("parameter:styleSheet", styleSheet);
		
		return this.getRuleCount0(styleSheet);
	};
	
	native private int getRuleCount0( final StyleSheet styleSheet )/*-{
		return styleSheet.rules.length;
	}-*/;
	
	@Override
	public Rule getRule( final StyleSheet styleSheet, final int index ){
		Checker.notNull("parameter:styleSheet", styleSheet);
		final Rule rule = this.getRule0(styleSheet, index);
		if( null == rule ){
			throw new IllegalArgumentException("The parameter:index is outside the range of actual stylesheets, index: " + index );
		}
		return rule;
	}
	
	native private Rule getRule0( final StyleSheet styleSheet, final int index )/*-{
		return styleSheet.rules[ index ];
	}-*/;

	
	@Override
	public Rule addRule(final StyleSheet styleSheet, final String selectorText, final String styleText) {
		Checker.notNull("parameter:styleSheet", styleSheet);
		Checker.notNull("parameter:selectorText", selectorText);
		Checker.notNull("parameter:styleText", styleText);

		return this.addRule0(styleSheet, selectorText, styleText);
	}

	private native Rule addRule0(final StyleSheet styleSheet, final String selectorText, final String styleText)/*-{        
		 var index = styleSheet.rules.length;
		 var safeStyleText = styleText.length == 0 ? ";" : styleText;

		 styleSheet.addRule( selectorText, safeStyleText, index );
		 return styleSheet.rules[ index ];         
		 }-*/;

	@Override
	public Rule insertRule(final StyleSheet styleSheet, final int index, final String selectorText, final String styleText) {
		Checker.notNull("parameter:styleSheet", styleSheet);
		Checker.notNull("parameter:selectorText", selectorText);
		Checker.notNull("parameter:styleText", styleText);

		return this.insertRule0(styleSheet, index, selectorText, styleText);
	}

	private native Rule insertRule0(final StyleSheet styleSheet, final int index, final String selectorText, final String styleText)/*-{
		 styleSheet.addRule( selectorText, styleText.length == 0 ? ";" : styleText, index );
		 return styleSheet.rules[ index ];         
		 }-*/;

	@Override
	public void removeRule(final StyleSheet styleSheet, final int index) {
		Checker.notNull("parameter:styleSheet", styleSheet);

		this.removeRule0(styleSheet, index);
	}

	/**
	 * Escapes to javascript to delete the requested rule.
	 */
	native private void removeRule0(final StyleSheet styleSheet, final int index) /*-{            
	 styleSheet.removeRule( index );
	 }-*/;

	public String[] getRuleStylePropertyNames(final Rule rule) {
		Checker.notNull("parameter:rule", rule);

		final JavaScriptObject style = JavaScript.getObject(rule, "style");
		return Utilities.split(getStylePropertyNames0(style), ",", true);
	}

	native private String getStylePropertyNames0(final JavaScriptObject style)/*-{
		 var names = new Array();
		 
		 for( n in style ){
		 names.push( n );
		 }
		 return names.join( "," );		 
		 }-*/;

	/**
	 * There is no need to normalize rules as Internet Explorer already does
	 * this when it loads stylesheets.
	 */
	@Override
	public void normalize(final StyleSheet styleSheet) {
	}
}
