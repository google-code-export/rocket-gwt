package rocket.test.dom;

import java.util.Iterator;
import java.util.Stack;

import junit.framework.TestCase;
import rocket.client.dom.DomObjectPropertyList;
import rocket.client.dom.DomObjectPropertyListElement;
import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

public class DomObjectPropertyListTestCase extends TestCase {

    final static String SEPARATOR = ",";

    public void testSize0() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("apple" + SEPARATOR + "banana");
        assertEquals(2, list.size());
    }

    public void testSize1() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("");
        assertEquals(0, list.size());
    }

    public void testSize2() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("apple" + SEPARATOR + "banana" + SEPARATOR + "carrot");
        assertEquals(3, list.size());
    }

    public void testIsEmpty0() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("apple" + SEPARATOR + "banana");
        assertEquals(false, list.isEmpty());
    }

    public void testIsEmpty1() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("");
        assertEquals(true, list.isEmpty());
    }

    public void testIsEmpty2() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("apple" + SEPARATOR + "banana" + SEPARATOR + "carrot");
        assertEquals(false, list.isEmpty());
    }

    public void testGet0() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("apple" + SEPARATOR + "banana" + SEPARATOR + "carrot");
        final DomObjectPropertyListElement element = (DomObjectPropertyListElement) list.get(0);
        assertEquals("apple", element.getValue());
    }

    public void testGet1() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("apple" + SEPARATOR + "banana" + SEPARATOR + "carrot");
        final DomObjectPropertyListElement element = (DomObjectPropertyListElement) list.get(1);
        assertEquals("banana", element.getValue());
    }

    public void testGet2() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("apple" + SEPARATOR + "banana" + SEPARATOR + "carrot");
        final DomObjectPropertyListElement element = (DomObjectPropertyListElement) list.get(2);
        assertEquals("carrot", element.getValue());
    }

    public void testGet3() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("apple" + SEPARATOR + "banana" + SEPARATOR + "carrot");

        try {
            final Object returned = list.get(-1);
            fail("An exception should have been thrown by list.get() using an invalid index and not returning "
                    + returned);
        } catch (final Exception expected) {

        }
    }

    public void testGet4() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("apple" + SEPARATOR + "banana" + SEPARATOR + "carrot");

        try {
            final Object returned = list.get(100);
            fail("An exception should have been thrown by list.get() using an invalid index and not returning "
                    + returned);
        } catch (final Exception expected) {

        }
    }

    public void testGet5() {
        final TestList list = this.createList();
        list.setObjectPropertyValue("apple" + SEPARATOR + "banana" + SEPARATOR + "carrot");
        final DomObjectPropertyListElement element0 = (DomObjectPropertyListElement) list.get(2);
        assertEquals("carrot", element0.getValue());
        final DomObjectPropertyListElement element1 = (DomObjectPropertyListElement) list.get(2);
        assertEquals("carrot", element1.getValue());
        assertSame(element0, element1);
    }

    public void testSet0() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        final DomObjectPropertyListElement replaced = (DomObjectPropertyListElement) list.set(0, element);
        assertEquals("apple", replaced.getValue());

        final String actual = list.getObjectPropertyValue();
        assertEquals(initial.replace("apple", "dog"), actual);
    }

    public void testSet1() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        final DomObjectPropertyListElement replaced = (DomObjectPropertyListElement) list.set(1, element);
        assertEquals("banana", replaced.getValue());

        final String actual = list.getObjectPropertyValue();
        assertEquals(initial.replace("banana", "dog"), actual);
    }

    public void testSet2() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        final DomObjectPropertyListElement replaced = (DomObjectPropertyListElement) list.set(2, element);
        assertEquals("carrot", replaced.getValue());

        final String actual = list.getObjectPropertyValue();
        assertEquals(initial.replace("carrot", "dog"), actual);
    }

    public void testSet3() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        try {
            list.set(-1, element);
            fail("An exception should have been thrown when attempting to set using an invalid index");
        } catch (final Exception caught) {
        }
        final String actual = list.getObjectPropertyValue();
        assertEquals(initial, actual);
    }

    public void testSet4() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        try {
            list.set(123, element);
            fail("An exception should have been thrown when attempting to set using an invalid index");
        } catch (final Exception caught) {
        }
        final String actual = list.getObjectPropertyValue();
        assertEquals(initial, actual);
    }

    public void testSet5() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        final Object replaced0 = list.set(2, element);
        final Object replaced1 = list.set(2, element);

        final String actual = list.getObjectPropertyValue();
        assertEquals(initial.replace("carrot", "dog"), actual);
        assertSame(element, replaced1);
    }

    public void testAdd0() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        list.add(element);

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, initial + SEPARATOR + "dog", actual);
    }

    public void testAdd1() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element0 = this.createElement();
        element0.setValue("dog");
        list.add(element0);

        final DomObjectPropertyListElement element1 = this.createElement();
        element1.setValue("eggplant");
        list.add(element1);

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, initial + SEPARATOR + "dog" + SEPARATOR + "eggplant", actual);
    }

    public void testInsert0() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        list.add(0, element);

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, "dog" + SEPARATOR + initial, actual);
    }

    public void testInsert1() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        list.add(1, element);

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, "apple" + SEPARATOR + "dog" + SEPARATOR + "banana" + SEPARATOR + "carrot", actual);
    }

    public void testInsert2() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        list.add(2, element);

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, "apple" + SEPARATOR + "banana" + SEPARATOR + "dog" + SEPARATOR + "carrot", actual);
    }

    public void testInsert3() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");
        list.add(3, element);

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot" + SEPARATOR + "dog", actual);
    }

    public void testRemove0() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement removed = (DomObjectPropertyListElement) list.remove(0);
        assertNotNull(removed);
        assertEquals("apple", removed.getValue());

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, "banana" + SEPARATOR + "carrot", actual);
    }

    public void testRemove1() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement removed = (DomObjectPropertyListElement) list.remove(1);
        assertNotNull(removed);
        assertEquals("banana", removed.getValue());

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, "apple" + SEPARATOR + "carrot", actual);
    }

    public void testRemove2() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement removed = (DomObjectPropertyListElement) list.remove(2);
        assertNotNull(removed);
        assertEquals("carrot", removed.getValue());

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, "apple" + SEPARATOR + "banana", actual);
    }

    public void testRemove3() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement removed0 = (DomObjectPropertyListElement) list.remove(0);
        assertNotNull(removed0);
        assertEquals("apple", removed0.getValue());

        final DomObjectPropertyListElement removed1 = (DomObjectPropertyListElement) list.remove(0);
        assertNotNull(removed1);
        assertEquals("banana", removed1.getValue());

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, "carrot", actual);
    }

    public void testRemove4() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement removed0 = (DomObjectPropertyListElement) list.remove(2);
        assertNotNull(removed0);
        assertEquals("carrot", removed0.getValue());

        final DomObjectPropertyListElement removed1 = (DomObjectPropertyListElement) list.remove(1);
        assertNotNull(removed1);
        assertEquals("banana", removed1.getValue());

        final String actual = list.getObjectPropertyValue();
        assertEquals("" + list, "apple", actual);
    }

    public void testIndexOf0() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("apple");

        final int indexOf = list.indexOf(element);
        assertEquals("" + list, 0, indexOf);
    }

    public void testIndexOf1() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("carrot");

        final int indexOf = list.indexOf(element);
        assertEquals("" + list, 2, indexOf);
    }

    public void testIndexOf2() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");

        final int indexOf = list.indexOf(element);
        assertEquals("" + list, -1, indexOf);
    }

    public void testContains0() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("apple");

        final boolean contains = list.contains(element);
        assertEquals("" + list, true, contains);
    }

    public void testContains1() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("carrot");

        final boolean contains = list.contains(element);
        assertEquals("" + list, true, contains);
    }

    public void testContains2() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final DomObjectPropertyListElement element = this.createElement();
        element.setValue("dog");

        final boolean contains = list.contains(element);
        assertEquals("" + list, false, contains);
    }

    public void testIterator0() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final Iterator iterator = list.iterator();

        assertTrue(iterator.hasNext());
        final DomObjectPropertyListElement element0 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "apple", element0.getValue());

        assertTrue(iterator.hasNext());
        final DomObjectPropertyListElement element1 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "banana", element1.getValue());

        assertTrue(iterator.hasNext());
        final DomObjectPropertyListElement element2 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "carrot", element2.getValue());

        assertFalse(iterator.hasNext());
    }

    public void testIterator1() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final Iterator iterator = list.iterator();

        assertTrue(iterator.hasNext());
        final DomObjectPropertyListElement element0 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "apple", element0.getValue());

        assertTrue(iterator.hasNext());
        final DomObjectPropertyListElement element1 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "banana", element1.getValue());

        assertTrue(iterator.hasNext());
        final DomObjectPropertyListElement element2 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "carrot", element2.getValue());

        assertFalse(iterator.hasNext());
    }

    public void testIterator2() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final Iterator iterator = list.iterator();

        final DomObjectPropertyListElement element0 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "apple", element0.getValue());

        final DomObjectPropertyListElement element1 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "banana", element1.getValue());

        final DomObjectPropertyListElement element2 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "carrot", element2.getValue());

        assertFalse(iterator.hasNext());
    }

    public void testIterator3() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final Iterator iterator = list.iterator();

        assertTrue(iterator.hasNext());
        final DomObjectPropertyListElement element0 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "apple", element0.getValue());

        final DomObjectPropertyListElement element1 = this.createElement();
        element1.setValue("dog");
        list.add(element1);

        try {
            iterator.hasNext();
            fail("The iterator should have thrown an exception, iterator: " + iterator + ", list: " + list);

        } catch (final Exception expected) {

        }
    }

    public void testIterator4() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final Iterator iterator = list.iterator();

        assertTrue(iterator.hasNext());
        final DomObjectPropertyListElement element0 = (DomObjectPropertyListElement) iterator.next();
        assertEquals(list + "", "apple", element0.getValue());

        final DomObjectPropertyListElement element1 = this.createElement();
        element1.setValue("dog");
        list.add(element1);

        try {
            iterator.next();
            fail("The iterator should have thrown an exception, iterator: " + iterator + ", list: " + list);

        } catch (final Exception expected) {

        }
    }

    public void testIteratorRemove() {
        final TestList list = this.createList();
        final String initial = "apple" + SEPARATOR + "banana" + SEPARATOR + "carrot";
        list.setObjectPropertyValue(initial);

        final Iterator iterator = list.iterator();

        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.remove();

        assertEquals("" + list, 2, list.size());

        final DomObjectPropertyListElement element1 = this.createElement();
        element1.setValue("apple");
        assertFalse("" + list, list.contains(element1));
    }

    /**
     * Factory method which creates an instance of an element which may be added to the list.
     * 
     * @return
     */
    DomObjectPropertyListElement createElement() {
        return new TestElement();
    }

    /**
     * Adds no new behaviour other than sub-classing DomObjectPropertyListElement and making it non abstract.
     */
    class TestElement extends DomObjectPropertyListElement {
        TestElement() {
        }
    }

    TestList createList() {
        return new TestList();
    }

    /**
     * Subclass which provides simplist implementation of the required abstract methods.
     * 
     * It hijacks the get/setObjectPropertyValue using a field rather than a real dom object property.
     */
    class TestList extends DomObjectPropertyList {

        TestList() {
            super();

            this.setReplaceOnSet(new Stack());
            this.setPropertyValue("");
            this.setObjectPropertyValue("");
        }

        protected void checkElementType(Object object) {
            if (false == (object instanceof TestElement)) {
                SystemHelper.handleAssertFailure("Invalid element type, type[" + GWT.getTypeName(object) + "]");
            }
        }

        protected String[] createTokens(final String value) {
            return StringHelper.split(value, SEPARATOR, true);
        }

        protected DomObjectPropertyListElement createWrapper() {
            return new TestElement();
        }

        protected String rebuildStringForm() {
            return this.rebuildStringForm(SEPARATOR);
        }

        public JavaScriptObject getObject() {
            throw new UnsupportedOperationException(this.getClass() + ".getObject()");
        }

        public void setObject(final JavaScriptObject object) {
            throw new UnsupportedOperationException(this.getClass() + ".setObject()");
        }

        private String objectPropertyValue;

        protected String getObjectPropertyValue() {
            StringHelper.checkNotNull("field:objectPropertyValue", objectPropertyValue);
            return this.objectPropertyValue;
        }

        protected void setObjectPropertyValue(final String objectPropertyValue) {
            StringHelper.checkNotNull("parameter:objectPropertyValue", objectPropertyValue);

            final Stack stack = this.getReplaceOnSet();
            this.objectPropertyValue = stack.isEmpty() ? objectPropertyValue : (String) stack.pop();
            this.increaseModificationCounter();
        }

        /**
         * A stack which contains values which replace any set attempts.
         */
        private Stack replaceOnSet;

        public Stack getReplaceOnSet() {
            ObjectHelper.checkNotNull("field:replaceOnSet", this.replaceOnSet);
            return this.replaceOnSet;
        }

        public void setReplaceOnSet(final Stack replaceOnSet) {
            ObjectHelper.checkNotNull("parameter:replaceOnSet", replaceOnSet);
            this.replaceOnSet = replaceOnSet;
        }
    };
}
