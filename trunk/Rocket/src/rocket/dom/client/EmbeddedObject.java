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

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

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
		return ObjectHelper.getString(this.getElement(), DomConstants.ALIGN_ATTRIBUTE);
	}

	public boolean hasAlign() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.ALIGN_ATTRIBUTE);
	}

	public void setAlign(final String mimeType) {
		ObjectHelper.setString(this.getElement(), DomConstants.ALIGN_ATTRIBUTE, mimeType);
	}

	public String getArchive() {
		return ObjectHelper.getString(this.getElement(), DomConstants.ARCHIVE_ATTRIBUTE);
	}

	public boolean hasArchive() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.ARCHIVE_ATTRIBUTE);
	}

	public void setArchive(final String archive) {
		ObjectHelper.setString(this.getElement(), DomConstants.ARCHIVE_ATTRIBUTE, archive);
	}

	public int getBorder() {
		return ObjectHelper.getInteger(this.getElement(), DomConstants.BORDER_ATTRIBUTE);
	}

	public boolean hasBorder() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.BORDER_ATTRIBUTE);
	}

	public void setBorder(final int border) {
		ObjectHelper.setInteger(this.getElement(), DomConstants.BORDER_ATTRIBUTE, border);
	}

	public String getClassId() {
		return ObjectHelper.getString(this.getElement(), DomConstants.CLASS_ID_ATTRIBUTE);
	}

	public boolean hasClassId() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.CLASS_ID_ATTRIBUTE);
	}

	public void setClassId(final String classId) {
		ObjectHelper.setString(this.getElement(), DomConstants.CLASS_ID_ATTRIBUTE, classId);
	}

	public String getCodeBase() {
		return ObjectHelper.getString(this.getElement(), DomConstants.CODEBASE_ATTRIBUTE);
	}

	public boolean hasCodeBase() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.CODEBASE_ATTRIBUTE);
	}

	public void setCodeBase(final String codeBase) {
		ObjectHelper.setString(this.getElement(), DomConstants.CODEBASE_ATTRIBUTE, codeBase);
	}

	public String getCodeType() {
		return ObjectHelper.getString(this.getElement(), DomConstants.CODETYPE_ATTRIBUTE);
	}

	public boolean hasCodeType() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.CODETYPE_ATTRIBUTE);
	}

	public void setCodeType(final String codeType) {
		ObjectHelper.setString(this.getElement(), DomConstants.CODETYPE_ATTRIBUTE, codeType);
	}

	public String getData() {
		return ObjectHelper.getString(this.getElement(), DomConstants.DATA_ATTRIBUTE);
	}

	public boolean hasData() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.DATA_ATTRIBUTE);
	}

	public void setData(final String data) {
		ObjectHelper.setString(this.getElement(), DomConstants.DATA_ATTRIBUTE, data);
	}

	public String getDeclare() {
		return ObjectHelper.getString(this.getElement(), DomConstants.DECLARE_ATTRIBUTE);
	}

	public boolean hasDeclare() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.DECLARE_ATTRIBUTE);
	}

	public void setDeclare(final String declare) {
		ObjectHelper.setString(this.getElement(), DomConstants.DECLARE_ATTRIBUTE, declare);
	}

	public int getHeight() {
		return ObjectHelper.getInteger(this.getElement(), DomConstants.HEIGHT_ATTRIBUTE);
	}

	public boolean hasHeight() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.HEIGHT_ATTRIBUTE);
	}

	public void setHeight(final int height) {
		ObjectHelper.setInteger(this.getElement(), DomConstants.HEIGHT_ATTRIBUTE, height);
	}

	public int getHSpace() {
		return ObjectHelper.getInteger(this.getElement(), DomConstants.HSPACE_ATTRIBUTE);
	}

	public boolean hasHSpace() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.HSPACE_ATTRIBUTE);
	}

	public void setHSpace(final int hspace) {
		ObjectHelper.setInteger(this.getElement(), DomConstants.HSPACE_ATTRIBUTE, hspace);
	}

	public String getInnerHtml() {
		return DOM.getInnerHTML(this.getElement());
	}

	public void setInnerHtml(final String innerHtml) {
		StringHelper.checkNotNull("parameter:html", innerHtml);
		DOM.setInnerHTML(this.getElement(), innerHtml);
	}

	public String getMimeType() {
		return ObjectHelper.getString(this.getElement(), DomConstants.MIME_TYPE_ATTRIBUTE);
	}

	public boolean hasMimeType() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.MIME_TYPE_ATTRIBUTE);
	}

	public void setMimeType(final String mimeType) {
		ObjectHelper.setString(this.getElement(), DomConstants.MIME_TYPE_ATTRIBUTE, mimeType);
	}

	public String getStandby() {
		return ObjectHelper.getString(this.getElement(), DomConstants.STANDBY_ATTRIBUTE);
	}

	public boolean hasStandby() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.STANDBY_ATTRIBUTE);
	}

	public void setStandby(final String standby) {
		ObjectHelper.setString(this.getElement(), DomConstants.STANDBY_ATTRIBUTE, standby);
	}

	public String getUsemap() {
		return ObjectHelper.getString(this.getElement(), DomConstants.USEMAP_ATTRIBUTE);
	}

	public boolean hasUsemap() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.USEMAP_ATTRIBUTE);
	}

	public void setUsemap(final String useMap) {
		ObjectHelper.setString(this.getElement(), DomConstants.USEMAP_ATTRIBUTE, useMap);
	}

	public int getVSpace() {
		return ObjectHelper.getInteger(this.getElement(), DomConstants.VSPACE_ATTRIBUTE);
	}

	public boolean hasVSpace() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.VSPACE_ATTRIBUTE);
	}

	public void setVSpace(final int hspace) {
		ObjectHelper.setInteger(this.getElement(), DomConstants.VSPACE_ATTRIBUTE, hspace);
	}

	public int getWidth() {
		return ObjectHelper.getInteger(this.getElement(), DomConstants.WIDTH_ATTRIBUTE);
	}

	public boolean hasWidth() {
		return ObjectHelper.hasProperty(this.getElement(), DomConstants.WIDTH_ATTRIBUTE);
	}

	public void setWidth(final int width) {
		ObjectHelper.setInteger(this.getElement(), DomConstants.WIDTH_ATTRIBUTE, width);
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
		StringHelper.checkNotEmpty("parameter:name", name);
		StringHelper.checkNotNull("parameter:value ", value);

		final Element param = DOM.createElement(DomConstants.PARAM_TAG);
		DOM.appendChild(this.getElement(), param);
	}
}