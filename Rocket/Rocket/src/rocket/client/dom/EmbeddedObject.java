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
public class EmbeddedObject extends ElementWrapper {
    public EmbeddedObject() {
        this.createElement();
    }

    protected void createElement() {
        this.setElement(DOM.createElement(DomConstants.OBJECT_TAG));
    }

    public String getAlign() {
        return this.getProperty(DomConstants.ALIGN_ATTRIBUTE);
    }

    public boolean hasAlign() {
        return this.hasProperty(DomConstants.ALIGN_ATTRIBUTE);
    }

    public void setAlign(final String mimeType) {
        this.setProperty(DomConstants.ALIGN_ATTRIBUTE, mimeType);
    }

    public String getArchive() {
        return this.getProperty(DomConstants.ARCHIVE_ATTRIBUTE);
    }

    public boolean hasArchive() {
        return this.hasProperty(DomConstants.ARCHIVE_ATTRIBUTE);
    }

    public void setArchive(final String archive) {
        this.setProperty(DomConstants.ARCHIVE_ATTRIBUTE, archive);
    }

    public int getBorder() {
        return this.getIntProperty(DomConstants.BORDER_ATTRIBUTE);
    }

    public boolean hasBorder() {
        return this.hasProperty(DomConstants.BORDER_ATTRIBUTE);
    }

    public void setBorder(final int border) {
        this.setProperty(DomConstants.BORDER_ATTRIBUTE, border);
    }

    public String getClassId() {
        return this.getProperty(DomConstants.CLASS_ID_ATTRIBUTE);
    }

    public boolean hasClassId() {
        return this.hasProperty(DomConstants.CLASS_ID_ATTRIBUTE);
    }

    public void setClassId(final String classId) {
        this.setProperty(DomConstants.CLASS_ID_ATTRIBUTE, classId);
    }

    public String getCodeBase() {
        return this.getProperty(DomConstants.CODEBASE_ATTRIBUTE);
    }

    public boolean hasCodeBase() {
        return this.hasProperty(DomConstants.CODEBASE_ATTRIBUTE);
    }

    public void setCodeBase(final String codeBase) {
        this.setProperty(DomConstants.CODEBASE_ATTRIBUTE, codeBase);
    }

    public String getCodeType() {
        return this.getProperty(DomConstants.CODETYPE_ATTRIBUTE);
    }

    public boolean hasCodeType() {
        return this.hasProperty(DomConstants.CODETYPE_ATTRIBUTE);
    }

    public void setCodeType(final String codeType) {
        this.setProperty(DomConstants.CODETYPE_ATTRIBUTE, codeType);
    }

    public String getData() {
        return this.getProperty(DomConstants.DATA_ATTRIBUTE);
    }

    public boolean hasData() {
        return this.hasProperty(DomConstants.DATA_ATTRIBUTE);
    }

    public void setData(final String data) {
        this.setProperty(DomConstants.DATA_ATTRIBUTE, data);
    }

    public String getDeclare() {
        return this.getProperty(DomConstants.DECLARE_ATTRIBUTE);
    }

    public boolean hasDeclare() {
        return this.hasProperty(DomConstants.DECLARE_ATTRIBUTE);
    }

    public void setDeclare(final String declare) {
        this.setProperty(DomConstants.DECLARE_ATTRIBUTE, declare);
    }

    public int getHeight() {
        return this.getIntProperty(DomConstants.HEIGHT_ATTRIBUTE);
    }

    public boolean hasHeight() {
        return this.hasProperty(DomConstants.HEIGHT_ATTRIBUTE);
    }

    public void setHeight(final int height) {
        this.setProperty(DomConstants.HEIGHT_ATTRIBUTE, height);
    }

    public int getHSpace() {
        return this.getIntProperty(DomConstants.HSPACE_ATTRIBUTE);
    }

    public boolean hasHSpace() {
        return this.hasProperty(DomConstants.HSPACE_ATTRIBUTE);
    }

    public void setHSpace(final int hspace) {
        this.setProperty(DomConstants.HSPACE_ATTRIBUTE, hspace);
    }

    public String getInnerHtml() {
        return DOM.getInnerHTML(this.getElement());
    }

    public void setInnerHtml(final String innerHtml) {
        StringHelper.checkNotNull("parameter:html", innerHtml);
        DOM.setInnerHTML(this.getElement(), innerHtml);
    }

    public String getMimeType() {
        return this.getProperty(DomConstants.MIME_TYPE_ATTRIBUTE);
    }

    public boolean hasMimeType() {
        return this.hasProperty(DomConstants.MIME_TYPE_ATTRIBUTE);
    }

    public void setMimeType(final String mimeType) {
        this.setProperty(DomConstants.MIME_TYPE_ATTRIBUTE, mimeType);
    }

    public String getStandby() {
        return this.getProperty(DomConstants.STANDBY_ATTRIBUTE);
    }

    public boolean hasStandby() {
        return this.hasProperty(DomConstants.STANDBY_ATTRIBUTE);
    }

    public void setStandby(final String standby) {
        this.setProperty(DomConstants.STANDBY_ATTRIBUTE, standby);
    }

    public String getUsemap() {
        return this.getProperty(DomConstants.USEMAP_ATTRIBUTE);
    }

    public boolean hasUsemap() {
        return this.hasProperty(DomConstants.USEMAP_ATTRIBUTE);
    }

    public void setUsemap(final String useMap) {
        this.setProperty(DomConstants.USEMAP_ATTRIBUTE, useMap);
    }

    public int getVSpace() {
        return this.getIntProperty(DomConstants.VSPACE_ATTRIBUTE);
    }

    public boolean hasVSpace() {
        return this.hasProperty(DomConstants.VSPACE_ATTRIBUTE);
    }

    public void setVSpace(final int hspace) {
        this.setProperty(DomConstants.VSPACE_ATTRIBUTE, hspace);
    }

    public int getWidth() {
        return this.getIntProperty(DomConstants.WIDTH_ATTRIBUTE);
    }

    public boolean hasWidth() {
        return this.hasProperty(DomConstants.WIDTH_ATTRIBUTE);
    }

    public void setWidth(final int width) {
        this.setProperty(DomConstants.WIDTH_ATTRIBUTE, width);
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