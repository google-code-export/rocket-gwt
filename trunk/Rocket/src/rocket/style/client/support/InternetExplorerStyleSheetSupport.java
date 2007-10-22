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

public class InternetExplorerStyleSheetSupport extends StyleSheetSupport {
	/**
	 * Helper which retrieves the native rules collection that this instance is
	 * presenting as a List
	 * 
	 * @return
	 */
	public JavaScriptObject getRulesCollection(final JavaScriptObject styleSheet) {
		return ObjectHelper.getObject(styleSheet, Css.RULES_LIST_PROPERTY_IE6);
	}

	public void addRule(final JavaScriptObject styleSheet, final String selectorText, final String styleText) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		StringHelper.checkNotNull("parameter:selectorText", selectorText);
		StringHelper.checkNotNull("parameter:styleText", styleText);

		this.addRule0(styleSheet, selectorText, styleText);
	}

	private native void addRule0(final JavaScriptObject styleSheet, final String selectorText, final String styleText)/*-{        
	 var index = styleSheet.rules.length;
	 var safeStyleText = styleText.length == 0 ? ";" : styleText;

	 styleSheet.addRule( selectorText, safeStyleText, index );         
	 }-*/;

	public void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
		StringHelper.checkNotNull("parameter:selectorText", selectorText);
		StringHelper.checkNotNull("parameter:styleText", styleText);

		this.insertRule0(styleSheet, index, selectorText, styleText);
	}

	private native void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText)/*-{
	 styleSheet.addRule( selectorText, styleText.length == 0 ? ";" : styleText, index );         
	 }-*/;

	public void removeRule(final JavaScriptObject styleSheet, final int index) {
		ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);

		this.removeRule0(styleSheet, index);
	}

	/**
	 * Escapes to javascript to delete the requested rule.
	 */
	native private void removeRule0(final JavaScriptObject styleSheet, final int index) /*-{            
	 styleSheet.removeRule( index );
	 }-*/;

	public String[] getRuleStylePropertyNames(final JavaScriptObject rule) {
		ObjectHelper.checkNotNull("parameter:rule", rule);
		
		final JavaScriptObject style = ObjectHelper.getObject(rule, "style"); 
		return StringHelper.split(getStylePropertyNames0(style), ",", true);
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
	public void normalize(final JavaScriptObject styleSheet) {
	}
}
