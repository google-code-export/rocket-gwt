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
package rocket.dom.client;

import rocket.util.client.Checker;
import rocket.util.client.JavaScript;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Provides a bean like view of an Object element with all standard attributes
 * given setters/getters. Beans like this would typically be used for inserting
 * java applets, audio/video controls such as Windows Media Player.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class EmbeddedObject extends ElementWrapperImpl implements ElementWrapper {
	public EmbeddedObject() {
		this.createElement();
	}

	protected void createElement() {
		this.setElement(DOM.createElement(DomConstants.OBJECT_TAG));
	}

	public String getAlign() {
		return JavaScript.getString(this.getElement(), DomConstants.ALIGN_ATTRIBUTE);
	}

	public boolean hasAlign() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.ALIGN_ATTRIBUTE);
	}

	public void setAlign(final String mimeType) {
		JavaScript.setString(this.getElement(), DomConstants.ALIGN_ATTRIBUTE, mimeType);
	}

	public String getArchive() {
		return JavaScript.getString(this.getElement(), DomConstants.ARCHIVE_ATTRIBUTE);
	}

	public boolean hasArchive() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.ARCHIVE_ATTRIBUTE);
	}

	public void setArchive(final String archive) {
		JavaScript.setString(this.getElement(), DomConstants.ARCHIVE_ATTRIBUTE, archive);
	}

	public int getBorder() {
		return JavaScript.getInteger(this.getElement(), DomConstants.BORDER_ATTRIBUTE);
	}

	public boolean hasBorder() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.BORDER_ATTRIBUTE);
	}

	public void setBorder(final int border) {
		JavaScript.setInteger(this.getElement(), DomConstants.BORDER_ATTRIBUTE, border);
	}

	public String getClassId() {
		return JavaScript.getString(this.getElement(), DomConstants.CLASS_ID_ATTRIBUTE);
	}

	public boolean hasClassId() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.CLASS_ID_ATTRIBUTE);
	}

	public void setClassId(final String classId) {
		JavaScript.setString(this.getElement(), DomConstants.CLASS_ID_ATTRIBUTE, classId);
	}

	public String getCodeBase() {
		return JavaScript.getString(this.getElement(), DomConstants.CODEBASE_ATTRIBUTE);
	}

	public boolean hasCodeBase() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.CODEBASE_ATTRIBUTE);
	}

	public void setCodeBase(final String codeBase) {
		JavaScript.setString(this.getElement(), DomConstants.CODEBASE_ATTRIBUTE, codeBase);
	}

	public String getCodeType() {
		return JavaScript.getString(this.getElement(), DomConstants.CODETYPE_ATTRIBUTE);
	}

	public boolean hasCodeType() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.CODETYPE_ATTRIBUTE);
	}

	public void setCodeType(final String codeType) {
		JavaScript.setString(this.getElement(), DomConstants.CODETYPE_ATTRIBUTE, codeType);
	}

	public String getData() {
		return JavaScript.getString(this.getElement(), DomConstants.DATA_ATTRIBUTE);
	}

	public boolean hasData() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.DATA_ATTRIBUTE);
	}

	public void setData(final String data) {
		JavaScript.setString(this.getElement(), DomConstants.DATA_ATTRIBUTE, data);
	}

	public String getDeclare() {
		return JavaScript.getString(this.getElement(), DomConstants.DECLARE_ATTRIBUTE);
	}

	public boolean hasDeclare() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.DECLARE_ATTRIBUTE);
	}

	public void setDeclare(final String declare) {
		JavaScript.setString(this.getElement(), DomConstants.DECLARE_ATTRIBUTE, declare);
	}

	public int getHeight() {
		return JavaScript.getInteger(this.getElement(), DomConstants.HEIGHT_ATTRIBUTE);
	}

	public boolean hasHeight() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.HEIGHT_ATTRIBUTE);
	}

	public void setHeight(final int height) {
		JavaScript.setInteger(this.getElement(), DomConstants.HEIGHT_ATTRIBUTE, height);
	}

	public int getHSpace() {
		return JavaScript.getInteger(this.getElement(), DomConstants.HSPACE_ATTRIBUTE);
	}

	public boolean hasHSpace() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.HSPACE_ATTRIBUTE);
	}

	public void setHSpace(final int hspace) {
		JavaScript.setInteger(this.getElement(), DomConstants.HSPACE_ATTRIBUTE, hspace);
	}

	public String getInnerHtml() {
		return DOM.getInnerHTML(this.getElement());
	}

	public void setInnerHtml(final String innerHtml) {
		Checker.notNull("parameter:html", innerHtml);
		DOM.setInnerHTML(this.getElement(), innerHtml);
	}

	public String getMimeType() {
		return JavaScript.getString(this.getElement(), DomConstants.MIME_TYPE_ATTRIBUTE);
	}

	public boolean hasMimeType() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.MIME_TYPE_ATTRIBUTE);
	}

	public void setMimeType(final String mimeType) {
		JavaScript.setString(this.getElement(), DomConstants.MIME_TYPE_ATTRIBUTE, mimeType);
	}

	public String getStandby() {
		return JavaScript.getString(this.getElement(), DomConstants.STANDBY_ATTRIBUTE);
	}

	public boolean hasStandby() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.STANDBY_ATTRIBUTE);
	}

	public void setStandby(final String standby) {
		JavaScript.setString(this.getElement(), DomConstants.STANDBY_ATTRIBUTE, standby);
	}

	public String getUsemap() {
		return JavaScript.getString(this.getElement(), DomConstants.USEMAP_ATTRIBUTE);
	}

	public boolean hasUsemap() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.USEMAP_ATTRIBUTE);
	}

	public void setUsemap(final String useMap) {
		JavaScript.setString(this.getElement(), DomConstants.USEMAP_ATTRIBUTE, useMap);
	}

	public int getVSpace() {
		return JavaScript.getInteger(this.getElement(), DomConstants.VSPACE_ATTRIBUTE);
	}

	public boolean hasVSpace() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.VSPACE_ATTRIBUTE);
	}

	public void setVSpace(final int hspace) {
		JavaScript.setInteger(this.getElement(), DomConstants.VSPACE_ATTRIBUTE, hspace);
	}

	public int getWidth() {
		return JavaScript.getInteger(this.getElement(), DomConstants.WIDTH_ATTRIBUTE);
	}

	public boolean hasWidth() {
		return JavaScript.hasProperty(this.getElement(), DomConstants.WIDTH_ATTRIBUTE);
	}

	public void setWidth(final int width) {
		JavaScript.setInteger(this.getElement(), DomConstants.WIDTH_ATTRIBUTE, width);
	}

	/**
	 * Adds a new Param to this object.
	 * 
	 * @param name
	 *            The parameter name
	 * @param value
	 *            The parameter value.
	 */
	public void addParam(final String name, final String value) {
		Checker.notEmpty("parameter:name", name);
		Checker.notNull("parameter:value ", value);

		final Element param = DOM.createElement(DomConstants.PARAM_TAG);
		DOM.appendChild(this.getElement(), param);
	}
}