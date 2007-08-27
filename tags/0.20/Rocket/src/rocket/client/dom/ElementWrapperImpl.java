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
package rocket.client.dom;

import rocket.client.util.ObjectHelper;
import rocket.client.util.ObjectWrapperImpl;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Represents a handle to a single DOM element.
 * 
 * Typed setters/getters are available so that subclasses need only call the appropriate getter/setter with the property name to be
 * acccessed.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class ElementWrapperImpl extends ObjectWrapperImpl implements ElementWrapper {

    protected ElementWrapperImpl() {
        super();
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

    public Element getElement() {
        return ObjectHelper.castToElement(this.getObject());
    }

    public void setElement(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        this.setObject(ObjectHelper.castFromElement(element));
    }

    /**
     * Returns the string representation of the element being wrapped.
     * 
     * @return
     */
    protected String toStringObject() {
        return this.hasObject() ? DOM.getAttribute(this.getElement(), "outerHTML") : "";
    }

    // OBJECT :::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public int hashCode() {
        return System.identityHashCode(this.getElement());
    }

    public boolean equals(final Object other) {
        boolean same = false;
        if (null != other) {
            final ElementWrapper otherWrapper = (ElementWrapper) other;
            same = this.getElement() == otherWrapper.getElement();
        }
        return same;
    }
}