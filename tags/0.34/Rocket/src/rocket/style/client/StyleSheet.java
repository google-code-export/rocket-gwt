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
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Each instance represents a single stylesheet attached to the document.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleSheet {

	/**
	 * Helper which retrieves the native stylesheet object
	 * 
	 * @return
	 */
	protected JavaScriptObject getStyleSheet() {
		return this.getStyleSheets().getStyleSheet(this.getIndex());
	}

	/**
	 * A cache of the parent StyleSheetList that this list belongs too.
	 */
	private StyleSheetList styleSheetList;

	protected StyleSheetList getStyleSheets() {
		ObjectHelper.checkNotNull("field:styleSheetList", styleSheetList);
		return this.styleSheetList;
	}

	protected void setStyleSheetList(final StyleSheetList styleSheetList) {
		ObjectHelper.checkNotNull("parameter:styleSheetList", styleSheetList);
		this.styleSheetList = styleSheetList;
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
		return this.hasProperty(StyleConstants.DISABLED_ATTRIBUTE) ? this.getBoolean(StyleConstants.DISABLED_ATTRIBUTE) : false;
	}

	public void setDisabled(final boolean disabled) {
		this.setBoolean(StyleConstants.DISABLED_ATTRIBUTE, disabled);
	}

	// OBJECT WRAPPER IMPL :::::::::::::::::::::

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
		return ObjectHelper.hasProperty(this.getStyleSheet(), propertyName);
	}

	// BOOLEAN :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	protected boolean getBoolean(final String propertyName) {
		return ObjectHelper.getBoolean(this.getStyleSheet(), propertyName);
	}

	protected void setBoolean(final String propertyName, final boolean value) {
		ObjectHelper.setBoolean(this.getStyleSheet(), propertyName, value);
	}

	// STRING :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	protected String getString(final String propertyName) {
		return ObjectHelper.getString(this.getStyleSheet(), propertyName);
	}

	protected void setString(final String propertyName, final String value) {
		ObjectHelper.setString(this.getStyleSheet(), propertyName, value);
	}

	protected void removeProperty(final String propertyName) {
		ObjectHelper.removeProperty(this.getStyleSheet(), propertyName);
	}
}
