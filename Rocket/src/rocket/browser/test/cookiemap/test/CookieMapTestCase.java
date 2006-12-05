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
package rocket.browser.test.cookiemap.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.browser.client.BrowserConstants;
import rocket.browser.client.BrowserHelper;
import rocket.browser.client.Cookie;
import rocket.browser.client.CookieMap;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Window;

/**
 * A series of unit tests for CookieMap.
 * 
 * TODO All Google Collection Iterators dont fail fast resulting in the iterators for this class also not failing fast.
 * 
 * When testing I encountered some strange behaviours when iterating using a HashMap iterator returned by keySet/entrySet/values. For some
 * strange reason after doing a iterator.next/iterator.remove the next(second) iterator.next results in a strange JavaSCriptException being
 * thrown wich includes a message complaining about something being null.
 * 
 * The tests which are affected by the iterator.next/remove bug/issue below typically have addCheckPoint statements where I have attempted
 * to figure things out.
 * 
 * This TestCase exhibits strange behaviour in hosted mode(cookies dont get deleted when they should etc) and thus should be run in web mode
 * to properly test.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class CookieMapTestCase extends GWTTestCase {

    final static String APPLE = "apple";

    final static String BANANA = "banana";

    final static String CARROT = "carrot";

    final static String DOESNT_EXIST = "DOESNT_EXIST";

    public String getModuleName() {
        return "rocket.browser.test.cookiemap.CookieMap";
    }

    public void testBrowserHelperSetGetRemoveCookie() {
        assertEquals("", BrowserHelper.getCookies());

        final String name = APPLE;
        final String value = "Green";

        BrowserHelper.setCookie(name + "=" + value);
        final String cookiesAfterSet = BrowserHelper.getCookies();
        assertFalse(cookiesAfterSet, -1 == cookiesAfterSet.indexOf(name));
        assertFalse(cookiesAfterSet, -1 == cookiesAfterSet.indexOf(value));

        BrowserHelper.removeCookie(name);
        final String cookiesAfterRemove = BrowserHelper.getCookies();
        assertEquals("Cookies string should be empty after removing [" + cookiesAfterRemove + "]", "",
                cookiesAfterRemove);
    }

    public void testGet0NonExistingCookie() {
        final CookieMap cookies = new CookieMap();
        final Cookie notFound = (Cookie) cookies.get(DOESNT_EXIST);
        assertNull(notFound);
    }

    public void testGet1NonExistingCookie() {
        final CookieMap cookies = new CookieMap();
        final Cookie notFound = (Cookie) cookies.get(APPLE + DOESNT_EXIST);
        assertNull(notFound);
    }

    public void testGet2ExistingCookie() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            BrowserHelper.setCookie(appleName + BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR + appleValue);
            final CookieMap cookies = new CookieMap();
            final Cookie appleCookie = (Cookie) cookies.get(appleName);
            assertNotNull(appleCookie);

            assertEquals(appleName, appleCookie.getName());
            assertEquals(appleValue, appleCookie.getValue());
            assertFalse(appleCookie.hasComment());
            assertFalse(appleCookie.hasExpires());
            assertFalse(appleCookie.hasSecure());

        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testGet3ExistingCookie() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            BrowserHelper.setCookie(appleName + BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR + appleValue);
            BrowserHelper.setCookie(bananaName + BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR + bananaValue);
            BrowserHelper.setCookie(carrotName + BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR + carrotValue);

            final CookieMap cookies = new CookieMap();
            final Cookie appleCookie = (Cookie) cookies.get(appleName);
            assertNotNull(appleCookie);

            assertEquals(appleName, appleCookie.getName());
            assertEquals(appleValue, appleCookie.getValue());
            assertFalse(appleCookie.hasComment());
            assertFalse(appleCookie.hasExpires());
            assertFalse(appleCookie.hasSecure());

            final Cookie bananaCookie = (Cookie) cookies.get(bananaName);
            assertNotNull(bananaCookie);

            assertEquals(bananaName, bananaCookie.getName());
            assertEquals(bananaValue, bananaCookie.getValue());
            assertFalse(bananaCookie.hasComment());
            assertFalse(bananaCookie.hasExpires());
            assertFalse(bananaCookie.hasSecure());

            final Cookie carrotCookie = (Cookie) cookies.get(carrotName);
            assertNotNull(carrotCookie);

            assertEquals(carrotName, carrotCookie.getName());
            assertEquals(carrotValue, carrotCookie.getValue());
            assertFalse(carrotCookie.hasComment());
            assertFalse(carrotCookie.hasExpires());
            assertFalse(carrotCookie.hasSecure());

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testContainsKey0NonExistingCookie() {
        final CookieMap cookies = new CookieMap();
        final boolean notFound = cookies.containsKey(DOESNT_EXIST);
        assertFalse(notFound);
    }

    public void testContainsKey1With3Cookies() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            BrowserHelper.setCookie(appleName + BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR + appleValue);
            BrowserHelper.setCookie(bananaName + BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR + bananaValue);
            BrowserHelper.setCookie(carrotName + BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR + carrotValue);

            final CookieMap cookies = new CookieMap();
            assertTrue(cookies.containsKey(appleName));
            assertTrue(cookies.containsKey(bananaName));
            assertTrue(cookies.containsKey(carrotName));
            assertFalse(cookies.containsKey(DOESNT_EXIST));

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testContainsValue0NonExistingCookie() {
        final CookieMap cookies = new CookieMap();
        final boolean notFound = cookies.containsValue(DOESNT_EXIST);
        assertFalse(notFound);
    }

    public void testContainsValue1SearchingFor3ValuesThatExistAnd1ThatDoesnt() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            BrowserHelper.setCookie(appleName + BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR + appleValue);
            BrowserHelper.setCookie(bananaName + BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR + bananaValue);
            BrowserHelper.setCookie(carrotName + BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR + carrotValue);

            final CookieMap cookies = new CookieMap();
            assertTrue(cookies.containsValue(cookies.get(appleName)));
            assertTrue(cookies.containsValue(cookies.get(bananaName)));
            assertTrue(cookies.containsValue(cookies.get(carrotName)));
            assertFalse(cookies.containsValue(DOESNT_EXIST));

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testPut0WhichAddsANewCookie() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            final CookieMap cookies = new CookieMap();
            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            final Object replacedByAppleCookie = cookies.put(appleName, appleCookie);

            assertNull(replacedByAppleCookie);
        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testPut1WhichAddsThreeDifferentNewCookies() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            final Object replacedByAppleCookie = cookies.put(appleName, appleCookie);

            assertNull(replacedByAppleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            final Object replacedByBananaCookie = cookies.put(bananaName, bananaCookie);

            assertNull(replacedByBananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            final Object replacedByCarrotCookie = cookies.put(carrotName, carrotCookie);

            assertNull(replacedByCarrotCookie);
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testPut2WhichAddsANewCookieAndThenReplacesIt() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            final CookieMap cookies = new CookieMap();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            final Object replacedByAppleCookie = cookies.put(appleName, appleCookie);

            assertNull(replacedByAppleCookie);

            final Object replacedByAppleCookie2 = cookies.put(appleName, appleCookie);
            assertSame(appleCookie, replacedByAppleCookie2);
        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testRemove0WhichAttemptsToRemoveANonExistingCookie() {
        final CookieMap cookies = new CookieMap();
        final Object removed = cookies.remove(DOESNT_EXIST);

        assertNull(removed);
    }

    public void testRemove1WhichAddsAndThenRemovesThreeDifferentCookies() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final Cookie removedByAppleCookie = (Cookie) cookies.remove(appleName);
            assertSame(appleCookie, removedByAppleCookie);

            final Object removedByBananaCookie = cookies.remove(bananaName);
            assertSame(bananaCookie, removedByBananaCookie);

            final Object removedByCarrotCookie = cookies.remove(carrotName);
            assertSame(carrotCookie, removedByCarrotCookie);

            final Object shouldBeNullBecauseCookieDoesntExist = cookies.remove(DOESNT_EXIST);
            assertNull(shouldBeNullBecauseCookieDoesntExist);

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testSize() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            final CookieMap cookies = new CookieMap();

            assertEquals(0, cookies.size());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            assertEquals(1, cookies.size());

            cookies.put(appleName, appleCookie);
            assertEquals(1, cookies.size());
        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testIsEmpty() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            final CookieMap cookies = new CookieMap();

            assertTrue(cookies.isEmpty());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            assertFalse(cookies.isEmpty());

            cookies.put(appleName, appleCookie);
            assertFalse(cookies.isEmpty());
        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testValuesCollectionSize() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();
            assertEquals(0, values.size());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);
            assertEquals(1, values.size());

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);
            assertEquals(2, values.size());

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);
            assertEquals(3, values.size());

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testValuesCollectionIsEmpty() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();
            assertTrue(values.isEmpty());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);
            assertFalse(values.isEmpty());

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);
            assertFalse(values.isEmpty());

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);
            assertFalse(values.isEmpty());

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testValuesCollectionAddThrowException() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();
            assertTrue(values.isEmpty());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);

            try {
                values.add(appleCookie);
                fail("An exception should be thrown when attempting to CookieMap.values().add()");
            } catch (final Exception expected) {

            }
        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testValuesCollectionAddAllThrowException() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();
            assertTrue(values.isEmpty());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);

            final List list = new ArrayList();
            list.add(appleCookie);

            try {
                values.addAll(list);
                fail("An exception should have be thrown when attempting to CookieMap.values().addAll().");
            } catch (final Exception expected) {

            }
        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testValuesCollectionContains() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);

            assertTrue(values.contains(appleCookie));
            assertTrue(values.contains(bananaCookie));
            assertFalse(values.contains(carrotCookie));

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testValuesCollectionToArrayNoArguments() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final Object[] array = values.toArray();
            assertEquals(3, array.length);

            final List control = new ArrayList();
            control.add(appleCookie);
            control.add(bananaCookie);
            control.add(carrotCookie);

            assertTrue(control.remove(array[0]));
            assertTrue(control.remove(array[1]));
            assertTrue(control.remove(array[2]));

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    // public void testValuesCollectionToArrayPassingArray(){
    // final String appleName = APPLE;
    // final String appleValue = "Green";
    // final String bananaName = BANANA;
    // final String bananaValue = "yellow";
    // final String carrotName = CARROT;
    // final String carrotValue = "orange";
    //
    // try {
    // final CookieMap cookies = new CookieMap();
    // final Collection values = cookies.values();
    //			
    // final Cookie appleCookie = new Cookie();
    // appleCookie.setName( appleName );
    // appleCookie.setValue( appleValue );
    // cookies.put( appleName, appleCookie );
    //			
    // final Cookie bananaCookie = new Cookie();
    // bananaCookie.setName( bananaName );
    // bananaCookie.setValue( bananaValue );
    // cookies.put( bananaName, bananaCookie );
    //			
    // final Cookie carrotCookie = new Cookie();
    // carrotCookie.setName( carrotName );
    // carrotCookie.setValue( carrotValue );
    //
    // final Object[] array = values.toArray( new Object[ 3 ]);
    // assertEquals( 3, array.length );
    //			
    // assertSame( appleCookie, array[ 0 ]);
    // assertSame( bananaCookie, array[ 1 ]);
    // assertSame( carrotCookie, array[ 2 ]);
    //			
    // } finally {
    // BrowserHelper.removeCookie(appleName);
    // BrowserHelper.removeCookie(bananaName);
    // BrowserHelper.removeCookie(carrotName);
    // }
    // }
    public void testValuesCollectionRemove() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);

            assertTrue(values.remove(appleCookie));
            assertTrue(values.remove(bananaCookie));
            assertFalse(values.remove(carrotCookie));

            assertFalse(values.remove(appleCookie));

            assertTrue(cookies.isEmpty());

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testValuesCollectionClear() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            values.clear();

            assertTrue(cookies.isEmpty());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testValuesCollectionIterator0VisitAllUsingHasNextAndNext() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final List control = new ArrayList();
            control.add(appleCookie);
            control.add(bananaCookie);
            control.add(carrotCookie);

            final Iterator iterator = values.iterator();
            assertTrue(iterator.hasNext());
            assertTrue(control.remove(iterator.next()));

            assertTrue(iterator.hasNext());
            assertTrue(control.remove(iterator.next()));

            assertTrue(iterator.hasNext());
            assertTrue(control.remove(iterator.next()));

            assertFalse(iterator.hasNext());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testValuesCollectionIterator1VisitAllUsingOnlyNext() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final List control = new ArrayList();
            control.add(appleCookie);
            control.add(bananaCookie);
            control.add(carrotCookie);

            final Iterator iterator = values.iterator();
            assertTrue(iterator.hasNext());
            assertTrue(control.remove(iterator.next()));

            assertTrue(iterator.hasNext());
            assertTrue(control.remove(iterator.next()));

            assertTrue(iterator.hasNext());
            assertTrue(control.remove(iterator.next()));

            assertFalse(iterator.hasNext());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testValuesCollectionIterator2CallingExtraNextAfterIteratorIsExhaustedThrowsException() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final Iterator iterator = values.iterator();
            iterator.next();
            iterator.next();
            iterator.next();

            try {
                final Object object = iterator.next();
                fail("The CookieMap.value().iterator() should have thrown an exception because it is empty, object: "
                        + object);
            } catch (final Exception expected) {

            }

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testValuesCollectionIterator3NextFailsFastWhenCookieMapIsModified() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);

            final Iterator iterator = values.iterator();
            cookies.put(carrotName, carrotCookie);

            try {
                iterator.next();
                fail("The CookieMap.value().iterator() should have failed fast (throwing an exception) because the backing CookieMap was modified, iterator: "
                        + iterator);
            } catch (final Exception expected) {
            }

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testValuesCollectionIterator4Remove() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final Iterator iterator = values.iterator();

            iterator.next();
            this.addCheckpoint("about to remove first element");
            iterator.remove();
            this.addCheckpoint("about to check size of cookies map");
            assertEquals("Cookies:" + cookies, 2, cookies.size());

            this.addCheckpoint("about to call iterator.next or 2nd element");

            iterator.next();
            addCheckpoint("about to remove second element");
            iterator.remove();
            assertEquals("Cookies:" + cookies, 1, cookies.size());

            iterator.next();
            this.addCheckpoint("about to remove third element");
            iterator.remove();
            assertEquals("Cookies:" + cookies, 0, cookies.size());
        } catch (Exception e) {
            e.printStackTrace();
            Window.alert("values().iterator.remove" + e);
            throw new RuntimeException(e.getMessage());

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testEntrySetIsEmpty() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();
            assertTrue(set.isEmpty());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);
            assertFalse(set.isEmpty());

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);
            assertFalse(set.isEmpty());

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);
            assertFalse(set.isEmpty());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testEntrySetSize() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();
            assertEquals(0, set.size());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);
            assertEquals(1, set.size());

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);
            assertEquals(2, set.size());

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);
            assertEquals(3, set.size());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testEntryContains() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();
            assertEquals(0, set.size());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);
            assertFalse(set.contains(appleName));

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);
            assertFalse(set.contains(bananaName));

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);
            assertFalse(set.contains(carrotName));

            assertFalse(set.contains(DOESNT_EXIST));
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testEntrySetAddThrowException() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);

            try {
                set.add(appleCookie);
                fail("An exception should be thrown when attempting to CookieMap.entrySet().add()");
            } catch (final Exception expected) {

            }
        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testEntrySetAddAllThrowException() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);

            final List list = new ArrayList();
            list.add(appleCookie);

            try {
                set.addAll(list);
                fail("An exception should have be thrown when attempting to CookieMap.entrySet().addAll().");
            } catch (final Exception expected) {
            }
        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testEntrySetRemove() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);

            assertFalse(values.remove(appleCookie));
            assertFalse(values.remove(bananaCookie));
            assertFalse(values.remove(carrotCookie));

            assertFalse(values.remove(appleCookie));

            assertFalse(cookies.isEmpty());

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testEntrySetClear() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            set.clear();

            assertTrue(cookies.isEmpty());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testEntrySetIterator0VisitAllUsingHasNextAndNext() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final List control = new ArrayList();
            control.add(appleName);
            control.add(bananaName);
            control.add(carrotName);

            final Iterator iterator = set.iterator();
            assertTrue(iterator.hasNext());
            final Map.Entry first = (Map.Entry) iterator.next();
            assertTrue(control.remove(first.getKey()));

            assertTrue(iterator.hasNext());
            final Map.Entry second = (Map.Entry) iterator.next();
            assertTrue(control.remove(second.getKey()));

            assertTrue(iterator.hasNext());
            final Map.Entry third = (Map.Entry) iterator.next();
            assertTrue(control.remove(third.getKey()));

            assertFalse(iterator.hasNext());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testEntrySetIterator1VisitAllUsingOnlyNext() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final List control = new ArrayList();
            control.add(appleName);
            control.add(bananaName);
            control.add(carrotName);

            final Iterator iterator = set.iterator();
            final Map.Entry first = (Map.Entry) iterator.next();
            assertTrue(control.remove(first.getKey()));

            final Map.Entry second = (Map.Entry) iterator.next();
            assertTrue(control.remove(second.getKey()));

            final Map.Entry third = (Map.Entry) iterator.next();
            assertTrue(control.remove(third.getKey()));

            assertFalse(iterator.hasNext());
            assertFalse(iterator.hasNext());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testEntrySetIterator2CallingExtraNextAfterIteratorIsExhaustedThrowsException() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final Iterator iterator = set.iterator();
            iterator.next();
            iterator.next();
            iterator.next();
            try {
                final Object object = iterator.next();
                fail("The CookieMap.entrySet().iterator() should have thrown an exception because it is empty, object: "
                        + object);
            } catch (final Exception expected) {

            }

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testEntrySetIterator3NextFailsFastWhenCookieMapIsModified() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);

            final Iterator iterator = set.iterator();
            cookies.put(carrotName, carrotCookie);

            try {
                iterator.next();
                fail("The CookieMap.entrySet().iterator() should have failed fast (throwing an exception) because the backing CookieMap was modified, iterator: "
                        + iterator);
            } catch (final Exception expected) {
            }

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testEntrySetIterator4Remove() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.entrySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final Iterator iterator = set.iterator();

            iterator.next();
            this.addCheckpoint("about to remove first element");
            iterator.remove();
            this.addCheckpoint("about to check size of cookies map");
            assertEquals("Cookies:" + cookies, 2, cookies.size());

            this.addCheckpoint("about to call iterator.next or 2nd element");

            iterator.next();
            addCheckpoint("about to remove second element");
            iterator.remove();
            assertEquals("Cookies:" + cookies, 1, cookies.size());

            iterator.next();
            this.addCheckpoint("about to remove third element");
            iterator.remove();
            assertEquals("Cookies:" + cookies, 0, cookies.size());
        } catch (Exception e) {
            e.printStackTrace();
            Window.alert("values().iterator.remove" + e);
            throw new RuntimeException(e.getMessage());

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testKeySetIsEmpty() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();
            assertTrue(set.isEmpty());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);
            assertFalse(set.isEmpty());

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);
            assertFalse(set.isEmpty());

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);
            assertFalse(set.isEmpty());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testKeySetSize() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();
            assertEquals(0, set.size());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);
            assertEquals(1, set.size());

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);
            assertEquals(2, set.size());

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);
            assertEquals(3, set.size());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testKeyContains() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();
            assertEquals(0, set.size());

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);
            assertTrue(set.contains(appleName));

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);
            assertTrue(set.contains(bananaName));

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);
            assertTrue(set.contains(carrotName));

            assertFalse(set.contains(DOESNT_EXIST));
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testKeySetAddThrowException() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);

            try {
                set.add(appleCookie);
                fail("An exception should be thrown when attempting to CookieMap.keySet().add()");
            } catch (final Exception expected) {

            }
        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testKeySetAddAllThrowException() {
        final String appleName = APPLE;
        final String appleValue = "Green";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);

            final List list = new ArrayList();
            list.add(appleCookie);

            try {
                set.addAll(list);
                fail("An exception should have be thrown when attempting to CookieMap.keySet().addAll().");
            } catch (final Exception expected) {
            }
        } finally {
            BrowserHelper.removeCookie(appleName);
        }
    }

    public void testKeySetRemove() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Collection values = cookies.values();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);

            assertFalse(values.remove(appleName));
            assertFalse(values.remove(bananaName));
            assertFalse(values.remove(carrotName));

            assertFalse(values.remove(appleName));

            assertFalse(cookies.isEmpty());

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testKeySetClear() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            set.clear();

            assertTrue(cookies.isEmpty());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testKeySetIterator0VisitAllUsingHasNextAndNext() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final List control = new ArrayList();
            control.add(appleName);
            control.add(bananaName);
            control.add(carrotName);

            final Iterator iterator = set.iterator();
            assertTrue(iterator.hasNext());
            assertTrue(control.remove(iterator.next()));

            assertTrue(iterator.hasNext());
            assertTrue(control.remove(iterator.next()));

            assertTrue(iterator.hasNext());
            assertTrue(control.remove(iterator.next()));

            assertFalse(iterator.hasNext());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testKeySetIterator1VisitAllUsingOnlyNext() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final List control = new ArrayList();
            control.add(appleName);
            control.add(bananaName);
            control.add(carrotName);

            final Iterator iterator = set.iterator();

            assertTrue(control.remove(iterator.next()));
            assertTrue(control.remove(iterator.next()));
            assertTrue(control.remove(iterator.next()));

            assertFalse(iterator.hasNext());
        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testKeySetIterator2CallingExtraNextAfterIteratorIsExhaustedThrowsException() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final Iterator iterator = set.iterator();
            iterator.next();
            iterator.next();
            iterator.next();
            try {
                final Object object = iterator.next();
                fail("The CookieMap.keySet().iterator() should have thrown an exception because it is empty, object: "
                        + object);
            } catch (final Exception expected) {

            }

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testKeySetIterator3NextFailsFastWhenCookieMapIsModified() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);

            final Iterator iterator = set.iterator();
            cookies.put(carrotName, carrotCookie);

            try {
                iterator.next();
                fail("The CookieMap.keySet().iterator() should have failed fast (throwing an exception) because the backing CookieMap was modified, iterator: "
                        + iterator);
            } catch (final Exception expected) {
            }

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

    public void testKeySetIterator4Remove() {
        final String appleName = APPLE;
        final String appleValue = "Green";
        final String bananaName = BANANA;
        final String bananaValue = "yellow";
        final String carrotName = CARROT;
        final String carrotValue = "orange";

        try {
            final CookieMap cookies = new CookieMap();
            final Set set = cookies.keySet();

            final Cookie appleCookie = new Cookie();
            appleCookie.setName(appleName);
            appleCookie.setValue(appleValue);
            cookies.put(appleName, appleCookie);

            final Cookie bananaCookie = new Cookie();
            bananaCookie.setName(bananaName);
            bananaCookie.setValue(bananaValue);
            cookies.put(bananaName, bananaCookie);

            final Cookie carrotCookie = new Cookie();
            carrotCookie.setName(carrotName);
            carrotCookie.setValue(carrotValue);
            cookies.put(carrotName, carrotCookie);

            final Iterator iterator = set.iterator();

            iterator.next();
            this.addCheckpoint("about to remove first element");
            iterator.remove();
            this.addCheckpoint("about to check size of cookies map");
            assertEquals("Cookies:" + cookies, 2, cookies.size());

            this.addCheckpoint("about to call iterator.next or 2nd element");

            iterator.next();
            addCheckpoint("about to remove second element");
            iterator.remove();
            assertEquals("Cookies:" + cookies, 1, cookies.size());

            iterator.next();
            this.addCheckpoint("about to remove third element");
            iterator.remove();
            assertEquals("Cookies:" + cookies, 0, cookies.size());
        } catch (Exception e) {
            e.printStackTrace();
            Window.alert("values().iterator.remove" + e);
            throw new RuntimeException(e.getMessage());

        } finally {
            BrowserHelper.removeCookie(appleName);
            BrowserHelper.removeCookie(bananaName);
            BrowserHelper.removeCookie(carrotName);
        }
    }

}
