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

import rocket.util.client.Destroyable;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

/**
 * Presents a Map view of all the inline styles that apply to an element.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class InlineStyle extends Style implements Destroyable {

    /**
     * Helper which retrieves the native style object
     * 
     * @return
     */
    protected JavaScriptObject getStyle() {
        return ObjectHelper.getObject(this.getElement(), "style");
    }

    public int size() {
        final JavaScriptObject style = ObjectHelper.getObject(this.getElement(), "style");
        return ObjectHelper.getPropertyCount(style);
    }

    public String getValue(String propertyName) {
        return StyleHelper.getInlineStyleProperty(this.getElement(), propertyName);
    }

    protected void putValue(final String propertyName, final String propertyValue) {
        StyleHelper.setInlineStyleProperty(this.getElement(), propertyName, propertyValue);
    }

    protected void removeValue(final String propertyName) {
        StyleHelper.removeInlineStyleProperty(this.getElement(), propertyName);
    }

    public void destroy() {
        this.clearElement();
    }

    protected String getPropertyNames() {
        return this.getPropertyNames(this.getElement());
    }

    native private String getPropertyNames(final Element element)/*-{
     var style = element.style;
     var names = "";
     for( n in style ){
     names = names + n + ",";
     }
     return names;
     }-*/;

    /**
     * The native element being viewed as a Map
     */
    private Element element;

    public Element getElement() {
        ObjectHelper.checkNotNull("field:element", element);
        return element;
    }

    public boolean hasElement() {
        return null != this.element;
    }

    public void setElement(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        this.element = element;
    }

    public void clearElement() {
        this.element = null;
    }

}
