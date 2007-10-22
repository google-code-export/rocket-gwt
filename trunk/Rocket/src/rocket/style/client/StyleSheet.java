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
package rocket.style.client;

import java.util.List;

import rocket.dom.client.DomConstants;
import rocket.style.client.support.StyleSheetSupport;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Each instance represents a single stylesheet attached to the document.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleSheet {

	/**
	 * An StyleSheetSupport instance is used to provide support of Browser specific
	 * features.
	 */
	private static final StyleSheetSupport support = (StyleSheetSupport) GWT.create(StyleSheetSupport.class);

	protected static StyleSheetSupport getSupport() {
		return StyleSheet.support;
	}
	
	static JavaScriptObject getStyleSheetCollection() {
		return StyleSheet.getSupport().getStyleSheetCollection();
	}	
	

	/**
	 * A cached copy of the StyleSheetList.
	 */
	private static StyleSheetList styleSheets;

	/**
	 * Factory method which creates a StyleSheetCollection.
	 * 
	 * @return
	 */
	public static List getStyleSheets() {
		StyleSheetList styleSheets = null;
		if (StyleSheet.hasStyleSheets()) {
			styleSheets = StyleSheet.styleSheets;
		} else {
			styleSheets = new StyleSheetList();
			StyleSheet.setStyleSheets(styleSheets);
		}
		return styleSheets;
	}

	protected static boolean hasStyleSheets() {
		return null != styleSheets;
	}

	protected static void setStyleSheets(final StyleSheetList styleSheets) {
		ObjectHelper.checkNotNull("parameter:styleSheets", styleSheets);
		StyleSheet.styleSheets = styleSheets;
	}
	
	/**
	 * A cache of the parent StyleSheetList that this list belongs too.
	 */
	private StyleSheetList styleSheetList;

	public StyleSheetList getStyleSheetsList() {
		ObjectHelper.checkNotNull("field:styleSheetList", styleSheetList);
		return this.styleSheetList;
	}

	protected void setStyleSheetList(final StyleSheetList styleSheetList) {
		ObjectHelper.checkNotNull("parameter:styleSheetList", styleSheetList);
		this.styleSheetList = styleSheetList;
	}	
	
	/**
	 * Adds a new rule to the given stylesheet.
	 * 
	 * @param styleSheet
	 * @param selectorText
	 * @param styleText
	 */
	static void addRule(final JavaScriptObject styleSheet, final String selectorText, final String styleText) {
		StyleSheet.getSupport().addRule(styleSheet, selectorText, styleText);
	}

	/**
	 * Inserts a new rule at the given slot in the given stylesheet
	 * 
	 * @param styleSheet
	 * @param index
	 * @param selectorText
	 * @param styleText
	 */
	static void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText) {
		StyleSheet.getSupport().insertRule(styleSheet, index, selectorText, styleText);
	}

	/**
	 * Removes an existing rule from the given stylesheet
	 * 
	 * @param styleSheet
	 * @param index
	 */
	static void removeRule(final JavaScriptObject styleSheet, final int index) {
		StyleSheet.getSupport().removeRule(styleSheet, index);
	}

	/**
	 * Normalizes all the rules belonging to the given stylesheet. Normalizing
	 * is the process whereby any rules with more than one selector are
	 * duplicated so that each rule has only one selector.
	 * 
	 * @param styleSheet
	 */
	static void normalize(final JavaScriptObject styleSheet) {
		StyleSheet.getSupport().normalize(styleSheet);
	}

	/**
	 * Retrieves the collection of rules belonging to a stylesheet.
	 * 
	 * @param styleSheet
	 * @return
	 */
	static JavaScriptObject getRules(final JavaScriptObject styleSheet) {
		return StyleSheet.getSupport().getRulesCollection(styleSheet);
	}

	/**
	 * Retrieves the individual names of all the css styles for the given rule.
	 * 
	 * @param rule
	 * @return
	 */
	static String[] getRuleStylePropertyNames(final JavaScriptObject rule) {
		return RuleStyle.getSupport().getPropertyNames(rule);
	}


	/**
	 * Helper which retrieves the native stylesheet object
	 * 
	 * @return
	 */
	protected JavaScriptObject getNativeStyleSheet() {
		return this.getStyleSheetsList().getNativeStyleSheet( this.getIndex() );
	}
	
	/**
	 * The index of the native StyleSheet within the StyleSheet collection.
	 */
	private int index;

	protected int getIndex() {
		return index;
	}

	protected void setIndex(final int index) {
		this.index = index;
	}

	/**
	 * A copy of the rules list belonging to this stylesheet
	 */
	private RuleList ruleList;

	protected RuleList getRuleList() {
		if (false == this.hasRuleList()) {
			this.setRuleList(this.createRuleList());
		}
		ObjectHelper.checkNotNull("field:ruleList", ruleList);
		return this.ruleList;
	}

	protected boolean hasRuleList() {
		return null != this.ruleList;
	}

	protected void setRuleList(final RuleList ruleList) {
		ObjectHelper.checkNotNull("parameter:ruleList", ruleList);
		this.ruleList = ruleList;
	}

	protected RuleList createRuleList() {
		final RuleList list = new RuleList();
		list.setStyleSheet(this);
		return list;
	}

	public List getRules() {
		return this.getRuleList();
	}

	// STYLESHEET ELEMENT :::::::::::::::::::::::::::::::::
	/**
	 * Tests if this style sheet was loaded from an external file.
	 * 
	 * @return
	 */
	public boolean isExternalFile() {
		boolean external = false;
		while (true) {
			if (!this.hasTitle()) {
				break;
			}

			if (StringHelper.isNullOrEmpty(this.getTitle())) {
				break;
			}
			external = true;
			break;
		}

		return external;
	}

	public String getUrl() {
		return (String) this.getString(DomConstants.HREF_ATTRIBUTE);
	}

	public boolean hasUrl() {
		return this.hasProperty(DomConstants.HREF_ATTRIBUTE);
	}

	public void setUrl(final String href) {
		this.setString(DomConstants.HREF_ATTRIBUTE, href);
	}

	public String getType() {
		return (String) this.getString(DomConstants.TYPE_ATTRIBUTE);
	}

	public boolean hasType() {
		return this.hasProperty(DomConstants.TYPE_ATTRIBUTE);
	}

	public void setType(final String type) {
		this.setString(DomConstants.TYPE_ATTRIBUTE, type);
	}

	public boolean isDisabled() {
		return this.hasProperty(Constants.DISABLED_ATTRIBUTE) ? this.getBoolean(Constants.DISABLED_ATTRIBUTE) : false;
	}

	public void setDisabled(final boolean disabled) {
		this.setBoolean(Constants.DISABLED_ATTRIBUTE, disabled);
	}

	public String getId() {
		return this.getString(DomConstants.ID_ATTRIBUTE);
	}

	public boolean hasId() {
		return this.hasProperty(DomConstants.ID_ATTRIBUTE);
	}

	public void setId(final String id) {
		this.setString(DomConstants.ID_ATTRIBUTE, id);
	}

	public String getName() {
		return this.getString(DomConstants.NAME_ATTRIBUTE);
	}

	public boolean hasName() {
		return this.hasProperty(DomConstants.NAME_ATTRIBUTE);
	}

	public void setName(final String name) {
		this.setString(DomConstants.NAME_ATTRIBUTE, name);
	}

	public String getTitle() {
		return this.getString(DomConstants.TITLE_ATTRIBUTE);
	}

	public boolean hasTitle() {
		return this.hasProperty(DomConstants.TITLE_ATTRIBUTE);
	}

	public void setTitle(final String title) {
		this.setString(DomConstants.TITLE_ATTRIBUTE, title);
	}

	// A VARIETY OF CONVENIENT TYPED PROPERTY METHODS.

	protected boolean hasProperty(final String propertyName) {
		return ObjectHelper.hasProperty(this.getNativeStyleSheet(), propertyName);
	}

	// BOOLEAN :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	protected boolean getBoolean(final String propertyName) {
		return ObjectHelper.getBoolean(this.getNativeStyleSheet(), propertyName);
	}

	protected void setBoolean(final String propertyName, final boolean value) {
		ObjectHelper.setBoolean(this.getNativeStyleSheet(), propertyName, value);
	}

	// STRING :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	protected String getString(final String propertyName) {
		return ObjectHelper.getString(this.getNativeStyleSheet(), propertyName);
	}

	protected void setString(final String propertyName, final String value) {
		ObjectHelper.setString(this.getNativeStyleSheet(), propertyName, value);
	}

	protected void removeProperty(final String propertyName) {
		ObjectHelper.removeProperty(this.getNativeStyleSheet(), propertyName);
	}
}
