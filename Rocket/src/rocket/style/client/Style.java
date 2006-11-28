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
package rocket.style.client;

import rocket.dom.client.DomObjectMap;
import rocket.dom.client.DomObjectMapValue;
import rocket.util.client.ObjectHelper;
import rocket.util.client.ObjectWrapper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A Style instance represents a handle to a native Style object as a Map of StylePropertyValue objects.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Style extends DomObjectMap {

    protected Style() {
        super();
    }

    public boolean hasObject() {
        boolean has = false;
        if (this.hasRule()) {
            final Rule rule = this.getRule();
            has = rule.hasParent() && rule.hasIndex();
        }

        return has;
    }

    public JavaScriptObject getObject() {
        return ObjectHelper.getObject(this.getRule().getObject(), StyleConstants.RULE_STYLE_OBJECT);
    }

    /**
     * Asserts that a Map value is a StylePropertyValue instance.
     */
    protected boolean isValidValueType(final Object value) {
        return value instanceof StylePropertyValue;
    }

    /**
     * Only properties which are not empty strings are considered values. All other propertyValue types are ignored.
     * 
     * @param propertyName
     * @return
     */
    protected boolean hasProperty(final String propertyName) {
        return this.hasProperty0(this.getObject(), propertyName);
    }

    native protected boolean hasProperty0(final JavaScriptObject object, final String propertyName)/*-{
     var propertyValue = object[ propertyName ];
     
     return typeof( propertyValue ) == "string" && propertyValue.length > 0 ;
     }-*/;

    /**
     * Because it is not possible to remove style properties the only option is too logically delete them by setting their value to "".
     */
    protected void removeProperty(final String propertyName) {
        ObjectHelper.setString(this.getObject(), propertyName, "");
    }

    /**
     * This factory method creates a StylePropertyValue object which is a handle to a single Style property value.
     */
    protected DomObjectMapValue createValueWrapper(final String propertyName) {
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);

        final StylePropertyValue wrapper = new StylePropertyValue();
        wrapper.setObject(this.getObject());
        wrapper.setPropertyName(propertyName);
        return wrapper;
    }

    /**
     * As part of the adoption process the propertyName and nativeObject are written to the value wrapper. THe wrapper should then write to
     * the nativeObject property when its own nativeObject reference is set.
     */
    protected void adopt(final String propertyName, final ObjectWrapper stylePropertyValue) {
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        ObjectHelper.checkNotNull("parameter:stylePropertyValue", stylePropertyValue);

        final StylePropertyValue stylePropertyValue0 = (StylePropertyValue) stylePropertyValue;
        stylePropertyValue0.setPropertyName(propertyName);
        stylePropertyValue0.setObject(this.getObject());
    }

    /**
     * Simply destroys the wrapper breaking any cyclic references. The StylePropertyValue is now disconnected and any updates will not
     * update the parent native Style object.
     */
    protected void disown(ObjectWrapper wrapper) {
        ObjectHelper.checkNotNull("parameter:wrapper", wrapper);

        wrapper.destroy();
    }

    /**
     * Caches the cssText value whilst the Style or parent Rule is disconnected.
     */
    private String cssText;

    public String getCssText() {
        if (this.hasObject()) {
            this.cssText = ObjectHelper.getString(this.getObject(), StyleConstants.STYLE_TEXT_PROPERTY_NAME);
        }
        if (null == this.cssText) {
            this.cssText = "";
        }
        return this.cssText;
    }

    public void setCssText(final String cssText) {
        if (this.hasObject()) {
            ObjectHelper.setString(this.getObject(), StyleConstants.STYLE_TEXT_PROPERTY_NAME, cssText);
        }
        this.cssText = cssText;
    }

    /**
     * The rule that this style belongs too.
     */
    private Rule rule;

    protected Rule getRule() {
        ObjectHelper.checkNotNull("field:rule", rule);
        return rule;
    }

    protected boolean hasRule() {
        return null != rule;
    }

    public void setRule(final Rule rule) {
        ObjectHelper.checkNotNull("parameter:rule", rule);
        this.rule = rule;
    }

    protected void clearRule() {
        this.rule = null;
    }

    public void destroy() {
        this.clearRule();
        super.destroy();
    }
}
