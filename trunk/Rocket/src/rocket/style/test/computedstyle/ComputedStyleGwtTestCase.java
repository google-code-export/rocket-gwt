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
package rocket.style.test.computedstyle;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import rocket.dom.client.DomHelper;
import rocket.style.client.StyleHelper;
import rocket.style.client.StylePropertyValue;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A series of unit tests for the ComputedStyle class.
 *
 * @author Miroslav Pokorny (mP)
 */
public class ComputedStyleGwtTestCase extends GWTTestCase {

    public String getModuleName() {
        return "rocket.style.test.computedstyle.ComputedStyleGwtTestCase";
    }

    public void testSize(){
        final String propertyName = "margin";
        final String propertyValue = "1px";

        final Element element = this.createElementAndAddToDocument();
        DOM.setStyleAttribute(element, propertyName, propertyValue);

        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);
        final int size = map.size();
        TestCase.assertTrue( "" + size, size > 0 );
    }

    public void testGetExistingComputedProperty() {
        final String propertyName = "margin";
        final String propertyValue = "1px";

        final Element element = this.createElementAndAddToDocument();
        DOM.setStyleAttribute(element, propertyName, propertyValue);

        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        final StylePropertyValue mapPropertyValue = (StylePropertyValue) map.get(propertyName);
        TestCase.assertNotNull(mapPropertyValue);

        final String value = mapPropertyValue.getString();
        TestCase.assertEquals(propertyValue, value);
    }

    public void testGetNonExistingInvalidProperty() {
        final String propertyName = "zebra";

        final Element parent = this.createElementAndAddToDocument();

        final Element child = DOM.createDiv();
        DOM.appendChild(parent, child);

        final Element element = child;

        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        final StylePropertyValue mapPropertyValue = (StylePropertyValue) map.get(propertyName);
        TestCase.assertNull(mapPropertyValue);
    }
    public void testGetExistingInheritedProperty() {
        final String propertyName = "backgroundColor";
        final String propertyValue = "#000000";

        final Element parent = this.createElementAndAddToDocument();
        DOM.setStyleAttribute(parent, propertyName, propertyValue);

        final Element child = DOM.createDiv();
        DOM.appendChild(parent, child);

        final Element element = child;

        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        final StylePropertyValue mapPropertyValue = (StylePropertyValue) map.get(propertyName);
        TestCase.assertNotNull(mapPropertyValue);

        final String value = mapPropertyValue.getString();
        TestCase.assertEquals(propertyValue, value);
    }

    public void testContainsExistingKey() {
        final String propertyName = "backgroundColor";
        final String propertyValue = "#000000";

        final Element parent = this.createElementAndAddToDocument();
        DOM.setStyleAttribute(parent, propertyName, propertyValue);

        final Element child = DOM.createDiv();
        DOM.appendChild(parent, child);

        final Element element = child;

        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        final boolean actual = map.containsKey( propertyName );
        TestCase.assertTrue( actual );
    }

    public void testContainsExistingValue() {
        final String propertyName = "marginTop";
        final String propertyValue = "12px";

        final Element element = this.createElementAndAddToDocument();
        DOM.setStyleAttribute(element, propertyName, propertyValue);

        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        final StylePropertyValue value = new StylePropertyValue();
        value.setString( propertyValue );
        final boolean actual = map.containsValue( value );
        TestCase.assertTrue( actual );
    }

    public void testKeySetAdd() {
        final Element element = this.createElementAndAddToDocument();
        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        try{
            final Set keySet = map.keySet();
            keySet.add( "ExpectingException");
        } catch ( final Exception expected ){

        }
    }

    public void testKeySetContains() {
        final String propertyName = "marginTop";
        final String propertyValue = "123px";

        final Element parent = this.createElementAndAddToDocument();
        DOM.setStyleAttribute(parent, propertyName, propertyValue);

        final Element child = DOM.createDiv();
        DOM.appendChild(parent, child);

        final Element element = child;

        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        final Set keySet = map.keySet();
        final boolean actual = keySet.contains( "marginTop" );
        TestCase.assertTrue( actual );
    }

    public void testKeySetIterator() {
        final String propertyName = "marginTop";
        final String propertyValue = "123px";

        final Element parent = this.createElementAndAddToDocument();
        DOM.setStyleAttribute(parent, propertyName, propertyValue);

        final Element child = DOM.createDiv();
        DOM.appendChild(parent, child);

        final Element element = child;

        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        final Set keySet = map.keySet();

        boolean found = false;
        final Iterator iterator = keySet.iterator();
        while( iterator.hasNext() ){
            if( propertyName.equals(iterator.next() )){
                found = true;
                break;
            }
        }

        TestCase.assertTrue( "keySet.iterator find", found );
    }

    public void testValuesCollectionAdd() {
        final Element element = this.createElementAndAddToDocument();
        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        try{
            final Collection values = map.values();
            values.add( "ExpectingException");
        } catch ( final Exception expected ){

        }
    }

    public void testValuesContains() {
        final String propertyName = "marginTop";
        final String propertyValue = "12px";

        final Element element = this.createElementAndAddToDocument();
        DOM.setStyleAttribute(element, propertyName, propertyValue);
        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        final StylePropertyValue value = new StylePropertyValue();
        value.setString( propertyValue );
        final Collection values = map.values();
        final boolean actual = values.contains( value );
        TestCase.assertTrue( actual );
    }

    public void testValuesIterator() {
        final String propertyName = "marginTop";
        final String propertyValue = "12px";

        final Element element = this.createElementAndAddToDocument();
        DOM.setStyleAttribute(element, propertyName, propertyValue);
        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        final StylePropertyValue value = new StylePropertyValue();
        value.setString( propertyValue );

        final Collection values = map.values();
        boolean found = false;

        final Iterator iterator = values.iterator();
        while( iterator.hasNext() ){
            final Object otherValue = iterator.next();
            if( value.equals( otherValue )){
                found = true;
                break;
            }
        }

        TestCase.assertTrue( "values.iterator find", found );
    }

    public void testPut() {
        final Element element = this.createElementAndAddToDocument();
        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        try {
            final String propertyName = "margin-left";
            final StylePropertyValue value = new StylePropertyValue();
            map.put(propertyName, value);
            TestCase.fail("ComputedStyle cannot be modified.");
        } catch (final Exception expected) {
        }
    }

    public void testRemove() {
        final Element element = this.createElementAndAddToDocument();
        final Map map = StyleHelper.getComputedStyle(element);
        TestCase.assertNotNull(element);

        try {
            map.remove("margin-left");
            TestCase.fail("ComputedStyle cannot be modified.");
        } catch (final Exception expected) {
        }
    }


    /**
     * Factory method which creates and adds a new div element to the document's body.
     *
     * @return
     */
    protected Element createElementAndAddToDocument() {
        final Element element = DOM.createDiv();
        DOM.appendChild( DomHelper.getBody(), element );
        return element;
    }
}
