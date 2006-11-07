package rocket.client.dom;

import rocket.client.util.StringHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Provides a bean like view of an Object element with all standard attributes given setters/getters. Beans like this would typically be
 * used for inserting java applets, audio/video controls such as Windows Media Player.
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
        return this.getString(DomConstants.ALIGN_ATTRIBUTE);
    }

    public boolean hasAlign() {
        return this.hasProperty(DomConstants.ALIGN_ATTRIBUTE);
    }

    public void setAlign(final String mimeType) {
        this.setString(DomConstants.ALIGN_ATTRIBUTE, mimeType);
    }

    public String getArchive() {
        return this.getString(DomConstants.ARCHIVE_ATTRIBUTE);
    }

    public boolean hasArchive() {
        return this.hasProperty(DomConstants.ARCHIVE_ATTRIBUTE);
    }

    public void setArchive(final String archive) {
        this.setString(DomConstants.ARCHIVE_ATTRIBUTE, archive);
    }

    public int getBorder() {
        return this.getInteger(DomConstants.BORDER_ATTRIBUTE);
    }

    public boolean hasBorder() {
        return this.hasProperty(DomConstants.BORDER_ATTRIBUTE);
    }

    public void setBorder(final int border) {
        this.setInteger(DomConstants.BORDER_ATTRIBUTE, border);
    }

    public String getClassId() {
        return this.getString(DomConstants.CLASS_ID_ATTRIBUTE);
    }

    public boolean hasClassId() {
        return this.hasProperty(DomConstants.CLASS_ID_ATTRIBUTE);
    }

    public void setClassId(final String classId) {
        this.setString(DomConstants.CLASS_ID_ATTRIBUTE, classId);
    }

    public String getCodeBase() {
        return this.getString(DomConstants.CODEBASE_ATTRIBUTE);
    }

    public boolean hasCodeBase() {
        return this.hasProperty(DomConstants.CODEBASE_ATTRIBUTE);
    }

    public void setCodeBase(final String codeBase) {
        this.setString(DomConstants.CODEBASE_ATTRIBUTE, codeBase);
    }

    public String getCodeType() {
        return this.getString(DomConstants.CODETYPE_ATTRIBUTE);
    }

    public boolean hasCodeType() {
        return this.hasProperty(DomConstants.CODETYPE_ATTRIBUTE);
    }

    public void setCodeType(final String codeType) {
        this.setString(DomConstants.CODETYPE_ATTRIBUTE, codeType);
    }

    public String getData() {
        return this.getString(DomConstants.DATA_ATTRIBUTE);
    }

    public boolean hasData() {
        return this.hasProperty(DomConstants.DATA_ATTRIBUTE);
    }

    public void setData(final String data) {
        this.setString(DomConstants.DATA_ATTRIBUTE, data);
    }

    public String getDeclare() {
        return this.getString(DomConstants.DECLARE_ATTRIBUTE);
    }

    public boolean hasDeclare() {
        return this.hasProperty(DomConstants.DECLARE_ATTRIBUTE);
    }

    public void setDeclare(final String declare) {
        this.setString(DomConstants.DECLARE_ATTRIBUTE, declare);
    }

    public int getHeight() {
        return this.getInteger(DomConstants.HEIGHT_ATTRIBUTE);
    }

    public boolean hasHeight() {
        return this.hasProperty(DomConstants.HEIGHT_ATTRIBUTE);
    }

    public void setHeight(final int height) {
        this.setInteger(DomConstants.HEIGHT_ATTRIBUTE, height);
    }

    public int getHSpace() {
        return this.getInteger(DomConstants.HSPACE_ATTRIBUTE);
    }

    public boolean hasHSpace() {
        return this.hasProperty(DomConstants.HSPACE_ATTRIBUTE);
    }

    public void setHSpace(final int hspace) {
        this.setInteger(DomConstants.HSPACE_ATTRIBUTE, hspace);
    }

    public String getInnerHtml() {
        return DOM.getInnerHTML(this.getElement());
    }

    public void setInnerHtml(final String innerHtml) {
        StringHelper.checkNotNull("parameter:html", innerHtml);
        DOM.setInnerHTML(this.getElement(), innerHtml);
    }

    public String getMimeType() {
        return this.getString(DomConstants.MIME_TYPE_ATTRIBUTE);
    }

    public boolean hasMimeType() {
        return this.hasProperty(DomConstants.MIME_TYPE_ATTRIBUTE);
    }

    public void setMimeType(final String mimeType) {
        this.setString(DomConstants.MIME_TYPE_ATTRIBUTE, mimeType);
    }

    public String getStandby() {
        return this.getString(DomConstants.STANDBY_ATTRIBUTE);
    }

    public boolean hasStandby() {
        return this.hasProperty(DomConstants.STANDBY_ATTRIBUTE);
    }

    public void setStandby(final String standby) {
        this.setString(DomConstants.STANDBY_ATTRIBUTE, standby);
    }

    public String getUsemap() {
        return this.getString(DomConstants.USEMAP_ATTRIBUTE);
    }

    public boolean hasUsemap() {
        return this.hasProperty(DomConstants.USEMAP_ATTRIBUTE);
    }

    public void setUsemap(final String useMap) {
        this.setString(DomConstants.USEMAP_ATTRIBUTE, useMap);
    }

    public int getVSpace() {
        return this.getInteger(DomConstants.VSPACE_ATTRIBUTE);
    }

    public boolean hasVSpace() {
        return this.hasProperty(DomConstants.VSPACE_ATTRIBUTE);
    }

    public void setVSpace(final int hspace) {
        this.setInteger(DomConstants.VSPACE_ATTRIBUTE, hspace);
    }

    public int getWidth() {
        return this.getInteger(DomConstants.WIDTH_ATTRIBUTE);
    }

    public boolean hasWidth() {
        return this.hasProperty(DomConstants.WIDTH_ATTRIBUTE);
    }

    public void setWidth(final int width) {
        this.setInteger(DomConstants.WIDTH_ATTRIBUTE, width);
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