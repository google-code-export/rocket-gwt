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
package rocket.dom.client;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Common base class for elements belonging to a DomObjectPropertyList.
 * 
 * Before attempting to set properties the list property must be set.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class DomObjectPropertyListElement implements Destroyable {

    protected DomObjectPropertyListElement() {
        super();
    }

    public void destroy() {
        this.clearList();
    }

    /**
     * The parent list that this element belongs too.
     */
    private DomObjectPropertyList list;

    protected DomObjectPropertyList getList() {
        ObjectHelper.checkNotNull("field:list", list);
        return list;
    }

    protected boolean hasList() {
        return null != this.list;
    }

    protected void setList(final DomObjectPropertyList list) {
        ObjectHelper.checkNotNull("parameter:list", list);
        this.list = list;
    }

    protected void clearList() {
        this.list = null;
    }

    /**
     * A cached copy of the String value.
     */
    private String value;

    public String getValue() {
        if (this.hasList()) {
            this.getList().stalenessGuard();
        }
        return this.getCacheValue();
    }

    public void setValue(final String value) {
        this.setCacheValue(value);
        if (this.hasList()) {
            this.getList().updateObjectPropertyValue();
        }
    }

    /**
     * This getter should only be used by the parent List when it wishes to read the value from this element.
     * 
     * @return
     */
    protected String getCacheValue() {
        StringHelper.checkNotEmpty("field:value", value);
        return value;
    }

    /**
     * This setter merely sets the value held by this element. It does not notify the parent List. It is provided so that the parent List
     * may update the value of this Element without the element attempting to resync with the parent which would cause an infinite loop or
     * at least make things very inefficient and slow.
     * 
     * @param value
     */
    protected void setCacheValue(final String value) {
        StringHelper.checkNotEmpty("parameter:value", value);
        this.value = value;
    }

    public int hashCode() {
        return this.getCacheValue().hashCode();
    }

    public boolean equals(final Object other) {
        return other instanceof DomObjectPropertyListElement ? this.equals((DomObjectPropertyListElement) other)
                : false;
    }

    public boolean equals(final DomObjectPropertyListElement other) {
        ObjectHelper.checkNotNull("parameter:other", other);
        return this.getCacheValue().equals(other.getCacheValue());
    }

    public String toString() {
        return super.toString() + ", value[" + value + "]";
    }
}
