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
package rocket.collection.test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;
import rocket.collection.client.Tree;

/**
 * @author Miroslav Pokorny (mP)
 */
public class TreeTestCase extends TestCase {

    public void testSet0SimpleRootSet() {
        final Tree tree = new Tree();

        final String firstPath = "";
        final Object firstValue = "root";
        final Object firstPrevious = tree.setValue(firstPath, firstValue);

        assertNull("Null should be returned as first is a new key [" + firstPath + "]", firstPrevious);

        assertEquals("Size after 1 set", 1, tree.size());
    }

    public void testSet1SimpleSet() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        final Object firstPrevious = tree.setValue(firstPath, firstValue);

        assertNull("Null should be returned as first is a new key [" + firstPath + "]", firstPrevious);

        assertEquals("Size after 2 set", 2, tree.size());
    }

    public void testSet2ASetFollowedByAReplacingSet() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        final Object firstPrevious = tree.setValue(firstPath, firstValue);

        assertNull("Null should be returned as first is a new key [" + firstPath + "]", firstPrevious);
        assertEquals("Size after 2 set", 2, tree.size());

        final String secondPath = firstPath;
        final Object secondValue = "secondValue";
        final Object secondPrevious = tree.setValue(secondPath, secondValue);
        TestCase.assertSame("Did not return replaced value", firstValue, secondPrevious);

        assertEquals("Size after 2 set and 1 replace", 2, tree.size());
    }

    public void testSet3TwoSetsWithDifferentPaths() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        final Object firstPrevious = tree.setValue(firstPath, firstValue);

        assertNull("Null should be returned as first is a new key [" + firstPath + "]", firstPrevious);
        assertEquals("Size after 2 set", 2, tree.size());

        final String secondPath = "/second";
        final Object secondValue = "secondValue";
        final Object secondPrevious = tree.setValue(secondPath, secondValue);
        assertNull("Null should be returned as second is a new key [" + secondPath + "]", secondPrevious);
        assertEquals("Size after 3 sets", 3, tree.size());
    }

    public void testSet4TestsGrandChildCreatedFromGrandchildPath() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        final Object firstPrevious = tree.setValue(firstPath, firstValue);

        assertNull("Null should be returned as first is a new key [" + firstPath + "]", firstPrevious);
        assertEquals("Size after 2 set", 2, tree.size());

        final String secondPath = firstPath + "/second";
        final Object secondValue = "secondValue";
        final Object secondPrevious = tree.setValue(secondPath, secondValue);
        assertNull("Null should be returned as second is a new key [" + secondPath + "]", secondPrevious);
        assertEquals("Size after 3 sets", 3, tree.size());
    }

    public void testSet5TheThirdSetReplacesTheValueOfTheFirstSetWhichIsAGrandchildOfTheFirst() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        final Object firstPrevious = tree.setValue(firstPath, firstValue);

        assertNull("Null should be returned as first is a new key [" + firstPath + "]", firstPrevious);
        assertEquals("Size after 2 set", 2, tree.size());

        final String secondPath = firstPath + "/second";
        final Object secondValue = "secondValue";
        final Object secondPrevious = tree.setValue(secondPath, secondValue);
        assertNull("Null should be returned as second is a new key [" + secondPath + "]", secondPrevious);
        assertEquals("Size after 3 sets", 3, tree.size());

        final String thirdPath = firstPath;
        final Object thirdValue = "thirdValue";
        final Object thirdPrevious = tree.setValue(thirdPath, thirdValue);
        TestCase.assertSame("Did not return replaced value of " + thirdPath, firstValue, thirdPrevious);
        assertEquals("Size after 3 sets", 3, tree.size());
    }

    public void testSet6TheThirdSetReplacesTheValueOfTheFirstSetWhichIsAGrandchildOfTheFirst() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        final Object firstPrevious = tree.setValue(firstPath, firstValue);

        assertNull("Null should be returned as first is a new key [" + firstPath + "]", firstPrevious);
        assertEquals("Size after 2 set", 2, tree.size());

        final String secondPath = firstPath + "/second";
        final Object secondValue = "secondValue";
        final Object secondPrevious = tree.setValue(secondPath, secondValue);
        assertNull("Null should be returned as second is a new key [" + secondPath + "]", secondPrevious);
        assertEquals("Size after 3 sets", 3, tree.size());

        final String thirdPath = secondPath;
        final Object thirdValue = "thirdValue";
        final Object thirdPrevious = tree.setValue(thirdPath, thirdValue);
        TestCase.assertSame("Did not return replaced value of " + thirdPath, secondValue, thirdPrevious);
        assertEquals("Size after 3 sets", 3, tree.size());
    }

    public void testSet7TheSecondSetBecomesParentOfFirst() {
        final Tree tree = new Tree();

        final String firstPath = "/second/first";
        final Object firstValue = "firstValue";
        final Object firstPrevious = tree.setValue(firstPath, firstValue);

        assertNull("Null should be returned as first is a new key [" + firstPath + "]", firstPrevious);
        assertEquals("Size after 2 set", 2, tree.size());

        final String secondPath = "/first";
        final Object secondValue = "secondValue";
        final Object secondPrevious = tree.setValue(secondPath, secondValue);
        assertNull("Null should be returned as second is a new key [" + secondPath + "]", secondPrevious);
        assertEquals("Size after 3 sets", 3, tree.size());
    }

    public void testSet8TheSecondSetBecomesParentOfTheFirstFollowedByAReplaceOfFirstsValue() {
        final Tree tree = new Tree();

        final String firstPath = "/second/first";
        final Object firstValue = "firstValue";
        final Object firstPrevious = tree.setValue(firstPath, firstValue);

        assertNull("Null should be returned as first is a new key [" + firstPath + "]", firstPrevious);
        assertEquals("Size after 2 set", 2, tree.size());

        final String secondPath = "/first";
        final Object secondValue = "secondValue";
        final Object secondPrevious = tree.setValue(secondPath, secondValue);
        assertNull("Null should be returned as second is a new key [" + secondPath + "]", secondPrevious);
        assertEquals("Size after 3 sets", 3, tree.size());

        final String thirdPath = firstPath;
        final Object thirdValue = "thirdValue";
        final Object thirdPrevious = tree.setValue(thirdPath, thirdValue);
        TestCase.assertSame("Did not return replaced value of " + thirdPath, firstValue, thirdPrevious);
        assertEquals("Size after 3 sets", 3, tree.size());
    }

    public void testSet9WithManySubPaths() {
        final Tree tree = new Tree();

        String path = "/";
        for (int i = 0; i < 10; i++) {
            path = path + i;
            tree.setValue(path, path + "-value");

            assertEquals("Tree.size", i + 1 + 1, tree.size());

            path = path + "/";
        }
    }

    public void TestHasValue0() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        assertTrue("testing for recently set[" + firstPath + "]", tree.hasValue(firstPath));

        final String secondPath = "/second";
        assertFalse("testing for unset[" + secondPath + "]", tree.hasValue(secondPath));
    }

    public void TestHasValue2() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        assertTrue("testing for recently set[" + firstPath + "]", tree.hasValue(firstPath));

        final String secondPath = "/first/second";
        final Object secondValue = "secondValue";
        tree.setValue(secondPath, secondValue);

        assertTrue("testing for recently set[" + secondPath + "]", tree.hasValue(secondPath));
    }

    public void TestHasValue3() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        assertTrue("testing for recently set[" + firstPath + "]", tree.hasValue(firstPath));

        final String secondPath = firstPath;
        final Object secondValue = "secondValue";
        tree.setValue(secondPath, secondValue);

        assertTrue("testing for recently set[" + secondPath + "]", tree.hasValue(secondPath));
    }

    public void TestHasValue4WithManySubPaths() {
        final Tree tree = new Tree();

        String path = "/";
        for (int i = 0; i < 10; i++) {
            path = path + i;
            tree.setValue(path, path + "-value");
            assertTrue("recently added Path, path[" + path + "]", tree.hasValue(path));

            path = path + "/";
        }
    }

    public void TestHasChildren0() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        tree.setValue("/apple/banana", "/yellow");
        tree.setValue("/apple/apple", "/red");
        tree.setValue("/ape", "/ugly");

        assertTrue(tree.hasChildren("/apple"));
        assertFalse(tree.hasChildren("/apple/banana"));
        assertFalse(tree.hasChildren("/apple/apple"));
        assertTrue(tree.hasChildren("/"));
    }

    public void testGet0() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        assertSame("testing get for recently set[" + firstPath + "]", firstValue, tree.getValue(firstPath));
    }

    public void testGet2() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        assertSame("testing get for recently set[" + firstPath + "]", firstValue, tree.getValue(firstPath));

        final String secondPath = "/second";
        final Object secondValue = "secondValue";
        tree.setValue(secondPath, secondValue);

        assertSame("testing get for recently set[" + secondPath + "]", secondValue, tree.getValue(secondPath));
    }

    public void testGet3() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        assertSame("testing get for recently set[" + firstPath + "]", firstValue, tree.getValue(firstPath));

        final String secondPath = firstPath;
        final Object secondValue = "secondValue";
        tree.setValue(secondPath, secondValue);

        assertSame("testing get for recently set[" + secondPath + "]", secondValue, tree.getValue(secondPath));
    }

    public void testGet4OfGrandparentBranch() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        assertSame("testing get for recently set[" + firstPath + "]", firstValue, tree.getValue(firstPath));

        final String secondPath = firstPath + "/second";
        final Object secondValue = "secondValue";
        tree.setValue(secondPath, secondValue);

        assertSame("testing get for recently set[" + secondPath + "]", secondValue, tree.getValue(secondPath));
    }

    public void testGet5ReturnsNullForNonExistingPath() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        assertSame("testing get for recently set[" + firstPath + "]", firstValue, tree.getValue(firstPath));

        final String path = "/other";
        assertNull(tree.getValue(path));
    }

    public void testGet6WithManySubPaths() {
        final Tree tree = new Tree();

        String path = "/";
        for (int i = 0; i < 10; i++) {
            path = path + i;
            final String value = path + "-value";
            tree.setValue(path, value);
            assertSame("getting recently added Path, path[" + path + "]", value, tree.getValue(path));

            path = path + "/";
        }
    }

    public void testRemoveValue0OfNonExistingDoesntThrowsAnException() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        tree.removeValue(firstPath);
    }

    public void testRemoveValue1() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        assertSame("testing remove on recently set[" + firstPath + "]", firstValue, tree.removeValue(firstPath));

        assertEquals(1, tree.size());

        tree.removeValue(firstPath);

        assertEquals("size remains 1 after removing the same path twice", 1, tree.size());
    }

    public void testRemoveValue2DoesntLoseChildBranches() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        final String secondPath = firstPath + "/second";
        final Object secondValue = "secondValue";
        tree.setValue(secondPath, secondValue);

        assertSame("testing remove on recently set[" + firstPath + "]", firstValue, tree.removeValue(firstPath));
        assertSame("check that [" + secondPath + "] was not lost after removing its parent [" + firstPath + "]",
                secondValue, tree.getValue(secondPath));
    }

    public void testRemoveValue3FromGrandchildBranch() {
        final Tree tree = new Tree();

        final String firstPath = "/first";
        final Object firstValue = "firstValue";
        tree.setValue(firstPath, firstValue);

        final String secondPath = firstPath + "/second";
        final Object secondValue = "secondValue";
        tree.setValue(secondPath, secondValue);

        assertSame("testing remove on recently set[" + secondPath + "]", secondValue, tree.removeValue(secondPath));
        assertSame("Double check parent was not modified[" + firstPath + "]", firstValue, tree.getValue(firstPath));

        assertEquals(2, tree.size());
    }

    public void testRemovePath0RemovesAPathThatOnlyMatchesALoneLeaf() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        tree.setValue("/apple/banana", "/yellow");
        tree.setValue("/apple/apple", "/red");
        tree.setValue("/ape", "/ugly");
        tree.removePath("/apple/banana");
        assertEquals("Tree after 4 sets and a remove of a branch", 5 - 1, tree.size());
    }

    public void testRemovePath1RemovesAPathWIthSeveralSubBranches() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        tree.setValue("/apple/banana", "/yellow");
        tree.setValue("/apple/apple", "/red");
        tree.setValue("/ape", "/ugly");
        tree.removePath("/apple");
        assertEquals("Tree after 4 sets and a remove of a branch", 5 - 3, tree.size());
    }

    public void testRemovePath3RemovesBranchesWithMatchingPrefix() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        tree.setValue("/apple/banana", "/yellow");
        tree.setValue("/carrot", "/orange");
        tree.setValue("/apple/apple", "/red");
        tree.setValue("/ape", "/ugly");
        tree.setValue("/banana", "/yellow");
        tree.removePath("/ap");
        assertEquals("Tree after a sets and a remove of several matching children", 3, tree.size());
    }

    public void testSize0() {
        final Tree tree = new Tree();
        assertEquals("Tree should be empty.", 1, tree.size());
    }

    public void testSize1() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        assertEquals("Tree after 2 sets.", 2, tree.size());
    }

    public void testSize2() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        tree.setValue("/banana", "/yellow");
        assertEquals("Tree after 3 set - tree is still flat no multiple branches.", 3, tree.size());
    }

    public void testSize3() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        tree.setValue("/apple", "/red");
        assertEquals("Tree after 2 set and 1 replace - tree is still flat no multiple branches.", 2, tree.size());
    }

    public void testSize4() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        tree.setValue("/apple/banana", "/yellow");
        tree.setValue("/apple/apple", "/red");
        assertEquals("Tree after 3 sets", 4, tree.size());
    }

    public void testSize5() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        tree.setValue("/apple/banana", "/yellow");
        tree.setValue("/apple/apple", "/red");
        tree.setValue("/ape", "/ugly");
        assertEquals("Tree after 4 sets", 5, tree.size());
    }

    public void testSize6() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        tree.setValue("/apple/banana", "/yellow");
        tree.setValue("/apple/apple", "/red");
        tree.setValue("/ape", "/ugly");
        tree.removePath("/apple/banana");
        assertEquals("Tree after 4 sets and a remove of a branch", 5 - 1, tree.size());
    }

    public void testSize7() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "/green");
        tree.setValue("/apple/banana", "/yellow");
        tree.setValue("/apple/apple", "/red");
        tree.setValue("/ape", "/ugly");
        tree.removePath("/apple");
        assertEquals("Tree after 4 sets and a remove of a branch", 5 - 3, tree.size());
    }

    public void testPathIterator0OnlyNextingNoRemovesAlsoVerifiesPathsAreReturnedSorted() {
        final String[] paths = new String[] { "/apple/banana/carrot", "/apple/banana", "/zebra", "/apple" };

        final Tree tree = new Tree();
        for (int i = 0; i < paths.length; i++) {
            final String path = paths[i];
            tree.setValue(path, path + "-value");
        }

        Arrays.sort(paths);

        int i = 0;
        final Iterator iterator = tree.pathIterator("", true);
        while (iterator.hasNext()) {
            final String otherPath = (String) iterator.next();
            final String expectedPath = paths[i];
            assertEquals("element " + i, otherPath, expectedPath);

            i++;
        }
    }

    public void testPathIterator1WithRemoves() {
        final String[] paths = new String[] { "/apple", "/apple/banana", "/apple/banana/carrot", "/zebra" };

        final Tree tree = new Tree();
        for (int i = 0; i < paths.length; i++) {
            final String path = paths[i];
            tree.setValue(path, path + "-value");
        }

        final Iterator iterator = tree.pathIterator("/", true);
        final String otherPath = (String) iterator.next();
        final String expectedPath = paths[0];
        assertEquals(expectedPath, otherPath);

        iterator.remove();
        assertEquals("Remove also removes a few branches", 2, tree.size());

        final String otherPath1 = (String) iterator.next();
        final String expectedPath1 = paths[3];
        assertEquals(expectedPath1, otherPath1);

        iterator.remove();
        assertEquals("Removes last", 1, tree.size());
    }

    public void testPathIterator2WithManySubBranches() {
        final Tree tree = new Tree();

        String path = "/";
        for (int i = 0; i < 10; i++) {
            path = path + i;
            final String value = path + "-value";
            tree.setValue(path, value);
            path = path + "/";
        }

        final Iterator pathIterator = tree.pathIterator("/", true);
        assertTrue(pathIterator.hasNext());
        pathIterator.next();
        pathIterator.remove();

        assertEquals(1, tree.size());
    }

    public void testPathIterator3WithManySubBranchesAndSkippingSubBranchesTrue() {
        final String[] paths = new String[] { "/apple", "/apple/banana", "/apple/banana/carrot", "/zebra" };

        final Tree tree = new Tree();

        String path = "/";
        for (int i = 0; i < paths.length; i++) {
            path = paths[i];
            final String value = path + "-value";
            tree.setValue(path, value);
            path = path + "/";
        }

        // should return /apple & zebra
        final Iterator pathIterator = tree.pathIterator("", false);
        assertTrue(pathIterator.hasNext());
        assertSame(paths[0], pathIterator.next());

        assertTrue(pathIterator.hasNext());
        assertSame(paths[paths.length - 1], pathIterator.next());

        assertFalse(pathIterator.hasNext());
    }

    public void testValueIterator0NextsAndRemoves() {
        final String[] paths = new String[] { "/apple", "/banana", "/carrot", "/zebra" };
        final String[] values = new String[] { "green", "yellow", "orange", "black & white" };

        final Tree tree = new Tree();
        for (int i = 0; i < paths.length; i++) {
            tree.setValue(paths[i], values[i]);
        }

        int i = 0;
        final Iterator iterator = tree.valueIterator("/", true);
        while (iterator.hasNext()) {
            final String otherValue = (String) iterator.next();
            final String expectedValue = values[i];
            assertEquals("element " + i, otherValue, expectedValue);

            iterator.remove();
            i++;
        }

        assertEquals(1, tree.size());
    }

    public void testEntryIterator0NextsAndRemoves() {
        final String[] paths = new String[] { "/apple", "/banana", "/carrot", "/zebra" };
        final String[] values = new String[] { "green", "yellow", "orange", "black & white" };

        final Tree tree = new Tree();
        for (int i = 0; i < paths.length; i++) {
            tree.setValue(paths[i], values[i]);
        }

        int i = 0;
        final Iterator iterator = tree.entryIterator("/", true);
        while (iterator.hasNext()) {
            final Map.Entry entry = (Map.Entry) iterator.next();

            final String otherPath = (String) entry.getKey();
            final String expectedPath = paths[i];
            assertEquals("element " + i, otherPath, expectedPath);

            final String otherValue = (String) entry.getValue();
            final String expectedValue = values[i];
            assertEquals("element " + i, otherValue, expectedValue);

            iterator.remove();
            i++;
        }

        assertEquals(1, tree.size());
    }

    public void testEntryIterator1NextsAndEntryPutValue() {
        final String[] paths = new String[] { "/apple", "/banana", "/carrot", "/zebra" };
        final String[] values = new String[] { "green", "yellow", "orange", "black & white" };

        final Tree tree = new Tree();
        for (int i = 0; i < paths.length; i++) {
            tree.setValue(paths[i], values[i]);
        }

        int i = 0;
        final Iterator iterator = tree.entryIterator("/", true);
        while (iterator.hasNext()) {
            final Map.Entry entry = (Map.Entry) iterator.next();

            final String otherPath = (String) entry.getKey();
            final String expectedPath = paths[i];
            assertEquals("element " + i, otherPath, expectedPath);

            final String otherValue = (String) entry.getValue();
            final String expectedValue = values[i];
            assertEquals("element " + i, otherValue, expectedValue);

            entry.setValue(otherValue + "*");
            i++;
        }

        for (int j = 0; j < paths.length; j++) {
            final String path = paths[j];
            final String expectedValue = values[j] + "*";
            final String actualValue = (String) tree.getValue(path);
            assertEquals(expectedValue, actualValue);
        }
    }

    public void testIteratorFailsFast() {
        final Tree tree = new Tree();
        tree.setValue("/apple", "green");
        tree.setValue("/banana", "yellow");
        tree.setValue("/carrot", "orange");

        final Iterator iterator = tree.pathIterator("/", true);
        tree.setValue("/zebra", "black & white");

        try {
            final Object nexted = iterator.next();
            fail("Iterator failed to fail fast and returned " + nexted);
        } catch (final Exception expected) {

        }
    }
}