/*
 * Copyright Miroslav Pokorny
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
package rocket.style.client;

import rocket.style.client.support.StyleSheetSupport;
import rocket.util.client.JavaScript;
import rocket.util.client.Tester;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;

/**
 * Each instance represents a single stylesheet attached to the document.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleSheet extends JavaScriptObject {

	/**
	 * An StyleSheetSupport instance is used to provide support of Browser
	 * specific features.
	 */
	private static final StyleSheetSupport support = (StyleSheetSupport) GWT.create(StyleSheetSupport.class);

	protected static StyleSheetSupport getSupport() {
		return StyleSheet.support;
	}
	
	static public int getStyleSheetCount( final Document document ){
		return JavaScript.getPropertyCount(StyleSheet.getSupport().getStyleSheetCollection( document ) );
	}
	
	static public StyleSheet getStyleSheet( final Document document, final int index ){
		final JavaScriptObject styleSheets = StyleSheet.getSupport().getStyleSheetCollection( document );
		return JavaScript.getObject( styleSheets, index ).cast();
	}

	protected StyleSheet(){
		super();
	}
	
	final public void prepare(){
		StyleSheet.getSupport().normalize(this);
	}
	
	final public int getRuleCount(){
		return StyleSheet.getSupport().getRuleCount(this);
	}
	
	final public Rule getRule( final int index ){
		return StyleSheet.getSupport().getRule( this, index );
	}
	
	final public Rule addRule( final String selector, final String cssText ){
		return StyleSheet.getSupport().addRule( this, selector, cssText );
	}
	
	final public Rule insertRule( final String selector, final String cssText, final int index ){
		return StyleSheet.getSupport().insertRule( this, index, selector, cssText );
	}
	
	final public void removeRule( final Rule rule ){
		StyleSheet.getSupport().removeRule( this, this.getRuleIndex( rule ));
	}
	
	final public int getRuleIndex( final Rule rule ){
		int index = -1;
		
		final int ruleCount = this.getRuleCount();		
		for( int i = 0; i < ruleCount; i++ ){
			final Rule otherRule = this.getRule(i);
			if( rule == otherRule ){
				index = i;
				break;
			}
		}
		
		return index;
	}
	

	 // STYLESHEET ELEMENT :::::::::::::::::::::::::::::::::
	/**
	 * Tests if this style sheet was loaded from an external file.
	 * 
	 * @return
	 */
	final public boolean isExternalFile() {
		return false == Tester.isNullOrEmpty( this.getTitle() );
	}

	final public String getUrl() {
		return (String) JavaScript.getString( this, Constants.HREF_ATTRIBUTE);
	}

	final public void setUrl(final String href) {
		JavaScript.setString( this, Constants.HREF_ATTRIBUTE, href);
	}

	final public String getType() {
		return (String) JavaScript.getString( this, Constants.TYPE_ATTRIBUTE);
	}

	final public void setType(final String type) {
		JavaScript.setString( this, Constants.TYPE_ATTRIBUTE, type);
	}

	final public boolean isDisabled() {
		return JavaScript.hasProperty(this, Constants.DISABLED_ATTRIBUTE) ? JavaScript.getBoolean(this, Constants.DISABLED_ATTRIBUTE) : false;
	}

	final public void setDisabled(final boolean disabled) {
		JavaScript.setBoolean( this, Constants.DISABLED_ATTRIBUTE, disabled);
	}

	final public String getId() {
		return JavaScript.getString( this, Constants.ID_ATTRIBUTE);
	}

	final public void setId(final String id) {
		JavaScript.setString( this, Constants.ID_ATTRIBUTE, id);
	}

	final public String getName() {
		return JavaScript.getString( this, Constants.NAME_ATTRIBUTE);
	}

	final public void setName(final String name) {
		JavaScript.setString( this, Constants.NAME_ATTRIBUTE, name);
	}

	final public String getTitle() {
		return JavaScript.getString( this, Constants.TITLE_ATTRIBUTE);
	}

	final public void setTitle(final String title) {
		JavaScript.setString( this, Constants.TITLE_ATTRIBUTE, title);
	}
}
