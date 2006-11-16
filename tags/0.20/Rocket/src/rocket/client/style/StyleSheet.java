/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.style;

import java.util.List;

import rocket.client.dom.DomConstants;
import rocket.client.dom.DomObjectListElement;
import rocket.client.util.ObjectHelper;
import rocket.client.util.ObjectWrapper;
import rocket.client.util.StringHelper;

/**
 * Each instance of StyleSheet represents a handle to a StyleSheet. Methods are available to retrieve the Rules list associated with the
 * stylesheet instance.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleSheet extends DomObjectListElement {

    public StyleSheet() {
        super();
    }

    /**
     * A list containing all the rules associated with this StyleSheet.
     */
    private List rules;

    public List getRules() {
        if (false == this.hasRules()) {
            this.createRules();
        }

        ObjectHelper.checkNotNull("field:rules", rules);
        return this.rules;
    }

    public boolean hasRules() {
        return null != this.rules;
    }

    protected void setRules(final List rules) {
        ObjectHelper.checkNotNull("parameter:rules", rules);
        this.rules = rules;
    }

    /**
     * Creates a new RuleList.
     */
    protected void createRules() {
        final RuleList rules = new RuleList();
        rules.setStyleSheet(this);
        // rules.setObject(this.getRules(this.getObject()));
        rules.afterPropertiesSet();
        this.setRules(rules);
    }

    // TDO DELETE
    // /**
    // * Retrieves the rules array of Rules objects from the given styleSheet in a browser independent manner.
    // *
    // * @param styleSheet
    // * @return
    // */
    // protected static JavaScriptObject getRules(final JavaScriptObject styleSheet) {
    // ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
    // return getRules0(styleSheet);
    // }
    //
    // protected native static JavaScriptObject getRules0(final JavaScriptObject styleSheet)/*-{
    // // ie6 uses rules whilst the standard mandates cssRules...
    // var rules = styleSheet.rules;
    // if( ! rules ){
    // rules = styleSheet.cssRules;
    // }
    // return rules ? rules : null;
    // }-*/;

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
        return this.hasProperty(StyleConstants.DISABLED_ATTRIBUTE) ? this.getBoolean(StyleConstants.DISABLED_ATTRIBUTE)
                : false;
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
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return ObjectHelper.hasProperty(this.getObject(), propertyName);
    }

    // BOOLEAN :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    protected boolean getBoolean(final String propertyName) {
        return ObjectHelper.getBoolean(this.getObject(), propertyName);
    }

    protected void setBoolean(final String propertyName, final boolean value) {
        ObjectHelper.setBoolean(this.getObject(), propertyName, value);
    }

    // STRING :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    protected String getString(final String propertyName) {
        return ObjectHelper.getString(this.getObject(), propertyName);
    }

    protected void setString(final String propertyName, final String value) {
        ObjectHelper.setString(this.getObject(), propertyName, value);
    }

    protected void removeProperty(final String propertyName) {
        ObjectHelper.removeProperty(this.getObject(), propertyName);
    }

    public boolean equals(final Object otherObject) {
        return otherObject instanceof ObjectWrapper ? this.equals((ObjectWrapper) otherObject) : false;
    }

    public boolean equals(final ObjectWrapper otherWrapper) {
        ObjectHelper.checkNotNull("parameter:otherWrapper", otherWrapper);

        boolean same = false;
        while (true) {
            // if nativeObjectWrapper is missing cant be equal to anything...
            if (false == this.hasObject()) {
                break;
            }

            // if other rule hasnt got a native rule object it cant be equal...
            if (false == otherWrapper.hasObject()) {
                break;
            }

            same = this.getObject().equals(otherWrapper.getObject());
            break;
        }
        return same;
    }
}
