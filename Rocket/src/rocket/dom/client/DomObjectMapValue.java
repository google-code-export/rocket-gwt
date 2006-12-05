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

import rocket.util.client.ObjectWrapper;
import rocket.util.client.ObjectWrapperImpl;
import rocket.util.client.StringHelper;

/**
 * Represents a wrapper around a value belonging to a DomObjectMap sub-class.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * Sub-classes need to set the Object property as part of the factory create process.
 */
public abstract class DomObjectMapValue extends ObjectWrapperImpl implements ObjectWrapper, Destroyable {

    protected DomObjectMapValue() {
        super();
    }

    /**
     * The name of the property belonging to object that contains the source of the List.
     */
    private String propertyName;

    public String getPropertyName() {
        StringHelper.checkNotEmpty("field:propertyName", propertyName);
        return propertyName;
    }

    public void setPropertyName(final String propertyName) {
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        this.propertyName = propertyName;
    }

    /**
     * The cached copy of the values. This is updated each time {@link #setPropertyValue} is called and may be written later when this
     * object is attached to a {@link DomObjectMap}
     */
    private String propertyValue;

    protected String getPropertyValue() {
        String propertyValue = this.propertyValue;
        if (this.hasObject()) {
            propertyValue = this.getObjectPropertyValue();
        }

        StringHelper.checkNotNull("field:propertyValue", propertyValue);
        return propertyValue;
    }

    protected boolean hasPropertyValue() {
        return null != propertyValue;
    }

    protected void setPropertyValue(final String propertyValue) {
        StringHelper.checkNotNull("parameter:propertyValue", propertyValue);
        this.propertyValue = propertyValue;

        if (this.hasObject()) {
            this.setObjectPropertyValue(propertyValue);
        }
    }

    /**
     * Clears any cached propertyValue
     */
    protected void clearPropertyValue() {
        this.propertyValue = null;
    }

    protected String getObjectPropertyValue() {
        return this.getString(this.getPropertyName());
    }

    protected void setObjectPropertyValue(final String objectPropertyValue) {
        this.setString(this.getPropertyName(), objectPropertyValue);
    }

    // OBJECT :::::::::::::::::::::::::::::::::::::::::::::::::::

    public abstract boolean equals(Object otherObject);
}
