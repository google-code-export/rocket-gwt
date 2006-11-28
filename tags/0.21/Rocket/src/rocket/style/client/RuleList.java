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

import java.util.AbstractList;

import rocket.style.client.impl.RuleListImpl;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * A RuleList instance provides a list view of the rules belonging to a single StyleSheet.
 * 
 * Once a RuleList is no longer used it should be destroyed by calling {@link #destroy()}.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RuleList extends AbstractList {

    public RuleList() {
        super();

        this.createImplementation();
    }

    /**
     * The browser aware implementation that takes care of browser difference nasties. All List methods are delegates to the implementation.
     */
    private RuleListImpl implementation;

    protected RuleListImpl getImplementation() {
        ObjectHelper.checkNotNull("field:implementation", this.implementation);
        return implementation;
    }

    protected void setImplementation(final RuleListImpl implementation) {
        ObjectHelper.checkNotNull("parameter:implementation", implementation);
        this.implementation = implementation;
    }

    protected void createImplementation() {
        this.setImplementation((RuleListImpl) GWT.create(RuleListImpl.class));
    }

    protected StyleSheet getStyleSheet() {
        return this.getImplementation().getStyleSheet();
    }

    protected void setStyleSheet(final StyleSheet styleSheet) {
        this.getImplementation().setStyleSheet(styleSheet);
    }

    protected JavaScriptObject getObject() {
        return this.getImplementation().getObject();
    }

    protected void setObject(final JavaScriptObject object) {
        this.getImplementation().setObject(object);
    }

    protected void afterPropertiesSet() {
        this.getImplementation().afterPropertiesSet();
    }

    // read only operations....................

    public int size() {
        return this.getImplementation().size();
    }

    public boolean contains(final Object object) {
        return this.getImplementation().contains(object);
    }

    public int indexOf(final Object object) {
        return this.getImplementation().indexOf(object);
    }

    public int lastIndexOf(final Object object) {
        return this.getImplementation().lastIndexOf(object);
    }

    public Object get(int index) {
        return this.getImplementation().get(index);
    }

    // modifying operations ..........................

    public boolean add(final Object object) {
        return this.getImplementation().add(object);
    }

    public void add(final int index, final Object object) {
        this.getImplementation().add(index, object);
    }

    public Object set(final int index, final Object object) {
        return this.getImplementation().set(index, object);
    }

    // removal

    public Object remove(final int index) {
        return this.getImplementation().remove(index);
    }

    public boolean remove(final Object object) {
        return this.getImplementation().remove(object);
    }

    // DESTROYABLE ::::::::::::::::::::::::::::::::::::::

    public void destroy() {
        this.getImplementation().destroy();
    }
}
