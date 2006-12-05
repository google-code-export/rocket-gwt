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
package rocket.dom.test.domcollectionlist.test;

import rocket.dom.client.DomCollectionList;
import rocket.dom.client.DomHelper;
import rocket.dom.client.ElementWrapperImpl;
import rocket.util.client.ObjectHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Unit tests for DomCollectionList.
 * 
 * TODO create tests for add/insert/remove
 */
public class DomCollectionListTestCase extends GWTTestCase {

    final static String NAME = "name";

    final static String VALUE = "value";

    final static String FIRST_NAME = "first";

    final static String FIRST_VALUE = "firstValue";

    final static String SECOND_NAME = "second";

    final static String SECOND_VALUE = "secondValue";

    final static String THIRD_NAME = "third";

    final static String THIRD_VALUE = "thirdValue";

    public String getModuleName() {
        return "rocket.dom.test.domcollectionlist.DomCollectionList";
    }

    public void testSize0() {
        final TestDomCollectionList list = new TestDomCollectionList();
        list.setCollection(getFormElements());

        assertEquals("list.size", 3, list.size());
    }

    public void testGet0OnlyTestsForNotNull() {
        final TestDomCollectionList list = new TestDomCollectionList();
        list.setCollection(getFormElements());

        final Object firstWrapper = list.get(0);
        assertNotNull("list.get(0)", firstWrapper);

        final Object secondWrapper = list.get(1);
        assertNotNull("list.get(1)", secondWrapper);

        final Object thirdWrapper = list.get(2);
        assertNotNull("list.get(2)", thirdWrapper);
    }

    public void testGet1WithCasts() {
        final TestDomCollectionList list = new TestDomCollectionList();
        list.setCollection(getFormElements());

        final TestDomElementWrapper firstWrapper = (TestDomElementWrapper) list.get(0);
        assertNotNull("list.get(0)", firstWrapper);

        final TestDomElementWrapper secondWrapper = (TestDomElementWrapper) list.get(1);
        assertNotNull("list.get(1)", secondWrapper);

        final TestDomElementWrapper thirdWrapper = (TestDomElementWrapper) list.get(2);
        assertNotNull("list.get(2)", thirdWrapper);
    }

    public void testGet2WithCastsAndChecksIsCorrectElement() {
        final TestDomCollectionList list = new TestDomCollectionList();
        list.setCollection(getFormElements());

        final TestDomElementWrapper firstWrapper = (TestDomElementWrapper) list.get(0);
        assertNotNull("list.get(0)", firstWrapper);
        assertEquals("list.get(0).value", FIRST_VALUE, firstWrapper.getValue());

        final TestDomElementWrapper secondWrapper = (TestDomElementWrapper) list.get(1);
        assertNotNull("list.get(1)", secondWrapper);
        assertEquals("list.get(1).value", SECOND_VALUE, secondWrapper.getValue());

        final TestDomElementWrapper thirdWrapper = (TestDomElementWrapper) list.get(2);
        assertNotNull("list.get(2)", thirdWrapper);
        assertEquals("list.get(2).value", THIRD_VALUE, thirdWrapper.getValue());
    }

    public void testSet0WhichTestsReplacedElement() {
        final TestDomCollectionList list = new TestDomCollectionList();
        list.setCollection(getFormElements());

        final TestDomElementWrapper originalFirstWrapper = (TestDomElementWrapper) list.get(0);

        final Element newFirstElement = DOM.createInputText();
        DOM.setAttribute(newFirstElement, NAME, "newFirst");
        DOM.setAttribute(newFirstElement, VALUE, "newFirstValue");
        final TestDomElementWrapper newFirstWrapper = new TestDomElementWrapper();
        newFirstWrapper.setElement(newFirstElement);

        this.addCheckpoint("" + DOM.getAttribute(newFirstElement, "outerHTML"));

        final TestDomElementWrapper replacedWrapper = (TestDomElementWrapper) list.set(0, newFirstWrapper);
        assertNotNull("set(0) should have returned a Wrapper", replacedWrapper);
        assertEquals("set(0) replaced.value", "firstValue", replacedWrapper.getValue());

        this.addCheckpoint("" + list.get(0));
        assertSame("list.get(0)", newFirstWrapper, list.get(0));
        assertSame("list.get(1)", list.get(1), list.get(1));
        assertSame("list.get(2)", list.get(2), list.get(2));
    }

    /**
     * Factory which creates a form with three elements.
     * 
     * @return
     */
    static JavaScriptObject getFormElements() {
        final Element form = DOM.createElement("FORM");
        DOM.appendChild(RootPanel.getBodyElement(), form);

        final Element first = DOM.createInputText();
        DOM.setAttribute(first, NAME, FIRST_NAME);
        DOM.setAttribute(first, VALUE, FIRST_VALUE);
        DOM.appendChild(form, first);

        final Element second = DOM.createInputText();
        DOM.setAttribute(second, NAME, SECOND_NAME);
        DOM.setAttribute(second, VALUE, SECOND_VALUE);
        DOM.appendChild(form, second);

        final Element third = DOM.createInputText();
        DOM.setAttribute(third, NAME, THIRD_NAME);
        DOM.setAttribute(third, VALUE, THIRD_VALUE);
        DOM.appendChild(form, third);

        return ObjectHelper.getObject(ObjectHelper.castFromElement(form), "elements");
    }

    class TestDomCollectionList extends DomCollectionList {

        protected Object createWrapper(final JavaScriptObject object) {
            final Element element = ObjectHelper.castToElement(object);
            DomHelper.checkTagName("parameter:element", element, "INPUT");

            final TestDomElementWrapper wrapper = new TestDomElementWrapper();
            wrapper.setElement(element);
            return wrapper;
        }

        protected void checkElementType(Object wrapper) {
            if (false == (wrapper instanceof TestDomElementWrapper)) {
                SystemHelper.handleAssertFailure("element", "Unknown element type, type[" + GWT.getTypeName(wrapper));
            }
        }

        protected void add1(final JavaScriptObject collection, final JavaScriptObject element) {
            throw new UnsupportedOperationException("add");
        }

        protected void insert1(final JavaScriptObject collection, final int index, final JavaScriptObject element) {
            throw new UnsupportedOperationException("insert");
        }

        protected JavaScriptObject remove0(final JavaScriptObject collection, final int index) {
            throw new UnsupportedOperationException("remove");
        }

        /**
         * Does nothing
         * 
         * @param object
         */
        protected void adopt(final Object object) {
        }

        /**
         * Does nothing
         * 
         * @param object
         */
        protected void disown(final Object object) {
        }
    }

    class TestDomElementWrapper extends ElementWrapperImpl {
        public String getValue() {
            return this.getString(VALUE);
        }
    }

}
