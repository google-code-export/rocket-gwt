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
package rocket.cookie.test.cookies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.cookie.client.Cookie;
import rocket.cookie.client.CookieConstants;
import rocket.cookie.client.Cookies;
import rocket.util.client.StringHelper;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * A series of unit tests for Cookies.
 * 
 * This TestCase exhibits strange behaviour in hosted mode(cookies dont get
 * deleted when they should etc) and thus should be run in web mode to properly
 * test.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class CookiesGwtTestCase extends GWTTestCase {

	final static String APPLE = "apple";

	final static String BANANA = "banana";

	final static String CARROT = "carrot";

	final static String NAME_OF_WHICH_DOESNT_EXIST = "DOESNT_EXIST";

	public String getModuleName() {
		return "rocket.cookie.test.cookies.CookiesGwtTestCase";
	}

	public void testFirst() {
		removeAllCookies();

		assertEquals("", getCookies());

		final String name = APPLE;
		final String value = "Green";

		setCookie(name + "=" + value);
		final String cookiesAfterSet = getCookies();
		assertFalse(cookiesAfterSet, -1 == cookiesAfterSet.indexOf(name));
		assertFalse(cookiesAfterSet, -1 == cookiesAfterSet.indexOf(value));

		removeCookie(name);
		final String cookiesAfterRemove = getCookies();
		assertEquals("Cookies string should be empty after removing \"" + cookiesAfterRemove + "\".", "", cookiesAfterRemove);
	}

	public void testGetNonExistingCookie0() {
		removeAllCookies();

		final Cookies cookies = new Cookies();
		final Cookie notFound = (Cookie) cookies.get(NAME_OF_WHICH_DOESNT_EXIST);
		assertNull(notFound);
	}

	public void testGetNonExistingCookie1() {
		removeAllCookies();

		final Cookies cookies = new Cookies();
		final Cookie notFound = (Cookie) cookies.get(APPLE + NAME_OF_WHICH_DOESNT_EXIST);
		assertNull(notFound);
	}

	public void testGetExistingCookie0() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		setCookie(appleName + CookieConstants.NAME_VALUE_SEPARATOR + appleValue);
		final Cookies cookies = new Cookies();
		final Cookie appleCookie = (Cookie) cookies.get(appleName);
		assertNotNull(appleCookie);

		assertEquals(appleName, appleCookie.getName());
		assertEquals(appleValue, appleCookie.getValue());
		assertFalse(appleCookie.hasComment());
		assertFalse(appleCookie.hasExpires());
		assertFalse(appleCookie.hasSecure());
	}

	public void testGetExistingCookie1() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		setCookie(appleName + CookieConstants.NAME_VALUE_SEPARATOR + appleValue);
		setCookie(bananaName + CookieConstants.NAME_VALUE_SEPARATOR + bananaValue);
		setCookie(carrotName + CookieConstants.NAME_VALUE_SEPARATOR + carrotValue);

		final Cookies cookies = new Cookies();
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
	}

	public void testContainsKeyWithNonExistingCookie() {
		removeAllCookies();

		final Cookies cookies = new Cookies();
		final boolean notFound = cookies.containsKey(NAME_OF_WHICH_DOESNT_EXIST);
		assertFalse(notFound);
	}

	public void testContainsKeyWithThreeCookies() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		setCookie(appleName + CookieConstants.NAME_VALUE_SEPARATOR + appleValue);
		setCookie(bananaName + CookieConstants.NAME_VALUE_SEPARATOR + bananaValue);
		setCookie(carrotName + CookieConstants.NAME_VALUE_SEPARATOR + carrotValue);

		final Cookies cookies = new Cookies();
		assertTrue(cookies.containsKey(appleName));
		assertTrue(cookies.containsKey(bananaName));
		assertTrue(cookies.containsKey(carrotName));
		assertFalse(cookies.containsKey(NAME_OF_WHICH_DOESNT_EXIST));
	}

	public void testContainsValueWithNonExistingCookie() {
		removeAllCookies();

		final Cookies cookies = new Cookies();
		final boolean notFound = cookies.containsValue(NAME_OF_WHICH_DOESNT_EXIST);
		assertFalse(notFound);
	}

	public void testContainsValueSearchingForCookieWhichExists() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		setCookie(appleName + CookieConstants.NAME_VALUE_SEPARATOR + appleValue);

		final Cookies cookies = new Cookies();
		final Cookie cookie = (Cookie) cookies.get(appleName);
		assertNotNull("cookie:" + cookie, cookie);

		assertTrue("cookie map should contain recently fetched cookie", cookies.containsValue(cookie));
	}

	public void testContainsValueSearchingForThreeValuesThatExistAndOneThatDoesnt() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		setCookie(appleName + CookieConstants.NAME_VALUE_SEPARATOR + appleValue);
		setCookie(bananaName + CookieConstants.NAME_VALUE_SEPARATOR + bananaValue);
		setCookie(carrotName + CookieConstants.NAME_VALUE_SEPARATOR + carrotValue);

		final Cookies cookies = new Cookies();
		assertTrue(cookies.containsValue(cookies.get(appleName)));
		assertTrue(cookies.containsValue(cookies.get(bananaName)));
		assertTrue(cookies.containsValue(cookies.get(carrotName)));
		assertFalse(cookies.containsValue(NAME_OF_WHICH_DOESNT_EXIST));
	}

	public void testPutWhichAddsANewCookie() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		final Cookies cookies = new Cookies();
		final Cookie appleCookie = new Cookie();
		appleCookie.setName(appleName);
		appleCookie.setValue(appleValue);
		final Object replacedByAppleCookie = cookies.put(appleName, appleCookie);

		assertNull(replacedByAppleCookie);
	}

	public void testPutWhichAddsThreeDifferentNewCookies() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();

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
	}

	public void testPutWhichAddsANewCookieAndThenReplacesIt() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		final Cookies cookies = new Cookies();

		final Cookie appleCookie = new Cookie();
		appleCookie.setName(appleName);
		appleCookie.setValue(appleValue);
		final Object replacedByAppleCookie = cookies.put(appleName, appleCookie);

		assertNull(replacedByAppleCookie);

		final Object replacedByAppleCookie2 = cookies.put(appleName, appleCookie);
		assertEquals(appleCookie, replacedByAppleCookie2);
	}

	public void testRemoveWhichAttemptsToRemoveANonExistingCookie() {
		removeAllCookies();

		final Cookies cookies = new Cookies();
		final Object removed = cookies.remove(NAME_OF_WHICH_DOESNT_EXIST);

		assertNull(removed);
	}

	public void testRemoveWhichAddsAndThenRemovesThreeDifferentCookies() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();

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
		assertEquals(appleCookie, removedByAppleCookie);

		final Object removedByBananaCookie = cookies.remove(bananaName);
		assertEquals(bananaCookie, removedByBananaCookie);

		final Object removedByCarrotCookie = cookies.remove(carrotName);
		assertEquals(carrotCookie, removedByCarrotCookie);

		final Object shouldBeNullBecauseCookieDoesntExist = cookies.remove(NAME_OF_WHICH_DOESNT_EXIST);
		assertNull(shouldBeNullBecauseCookieDoesntExist);
	}

	public void testSize() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		final Cookies cookies = new Cookies();

		assertEquals(0, cookies.size());

		final Cookie appleCookie = new Cookie();
		appleCookie.setName(appleName);
		appleCookie.setValue(appleValue);
		cookies.put(appleName, appleCookie);

		assertEquals(getCookies(), 1, cookies.size());

		cookies.put(appleName, appleCookie);
		assertEquals(getCookies(), 1, cookies.size());
	}

	public void testIsEmpty() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		final Cookies cookies = new Cookies();

		assertTrue(getCookies(), cookies.isEmpty());

		final Cookie appleCookie = new Cookie();
		appleCookie.setName(appleName);
		appleCookie.setValue(appleValue);
		cookies.put(appleName, appleCookie);

		assertFalse(getCookies(), cookies.isEmpty());

		cookies.put(appleName, appleCookie);
		assertFalse(getCookies(), cookies.isEmpty());
	}

	public void testValuesCollectionSize() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
		final Collection values = cookies.values();
		assertEquals(getCookies(), 0, values.size());

		final Cookie appleCookie = new Cookie();
		appleCookie.setName(appleName);
		appleCookie.setValue(appleValue);
		cookies.put(appleName, appleCookie);
		assertEquals(getCookies(), 1, values.size());

		final Cookie bananaCookie = new Cookie();
		bananaCookie.setName(bananaName);
		bananaCookie.setValue(bananaValue);
		cookies.put(bananaName, bananaCookie);
		assertEquals(getCookies(), 2, values.size());

		final Cookie carrotCookie = new Cookie();
		carrotCookie.setName(carrotName);
		carrotCookie.setValue(carrotValue);
		cookies.put(carrotName, carrotCookie);
		assertEquals(getCookies(), 3, values.size());
	}

	public void testValuesCollectionIsEmpty() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testValuesCollectionAddThrowsException() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		final Cookies cookies = new Cookies();
		final Collection values = cookies.values();
		assertTrue(values.isEmpty());

		final Cookie appleCookie = new Cookie();
		appleCookie.setName(appleName);
		appleCookie.setValue(appleValue);

		try {
			values.add(appleCookie);
			fail("An exception should be thrown when attempting to Cookies.values().add()");
		} catch (final Exception expected) {

		}
	}

	public void testValuesCollectionAddAllThrowsException() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		final Cookies cookies = new Cookies();
		final Collection values = cookies.values();
		assertTrue(values.isEmpty());

		final Cookie appleCookie = new Cookie();
		appleCookie.setName(appleName);
		appleCookie.setValue(appleValue);

		final List list = new ArrayList();
		list.add(appleCookie);

		try {
			values.addAll(list);
			fail("An exception should have be thrown when attempting to Cookies.values().addAll().");
		} catch (final Exception expected) {

		}
	}

	public void testValuesCollectionContains() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testValuesCollectionToArrayNoArguments() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	// public void testValuesCollectionToArrayPassingArray(){
	// removeAllCookies();
	// final String appleName = APPLE;
	// final String appleValue = "Green";
	// final String bananaName = BANANA;
	// final String bananaValue = "yellow";
	// final String carrotName = CARROT;
	// final String carrotValue = "orange";
	//
	// final Cookies cookies = new Cookies();
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
	// assertEquals( appleCookie, array[ 0 ]);
	// assertEquals( bananaCookie, array[ 1 ]);
	// assertEquals( carrotCookie, array[ 2 ]);
	// }
	public void testValuesCollectionRemove() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testValuesCollectionClear() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testValuesCollectionIteratorVisitAllUsingHasNextAndNext() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testValuesCollectionIterator1VisitAllUsingOnlyNext() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testValuesCollectionIteratorCallingExtraNextAfterIteratorIsExhaustedThrowsException() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
			fail("The Cookies.value().iterator() should have thrown an exception because it is empty, object: " + object);
		} catch (final Exception expected) {

		}
	}

	public void testValuesCollectionIteratorNextFailsFastWhenCookieMapIsModified() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
			fail("The Cookies.value().iterator() should have failed fast (throwing an exception) because the backing Cookies was modified, iterator: "
					+ iterator);
		} catch (final Exception expected) {
		}
	}

	public void testValuesCollectionIteratorRemove() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testEntrySetIsEmpty() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testEntrySetSize() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testEntryContains() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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

		assertFalse(set.contains(NAME_OF_WHICH_DOESNT_EXIST));
	}

	public void testEntrySetAddThrowsException() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		final Cookies cookies = new Cookies();
		final Set set = cookies.entrySet();

		final Cookie appleCookie = new Cookie();
		appleCookie.setName(appleName);
		appleCookie.setValue(appleValue);

		try {
			set.add(appleCookie);
			fail("An exception should be thrown when attempting to Cookies.entrySet().add()");
		} catch (final Exception expected) {

		}
	}

	public void testEntrySetAddAllThrowsException() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		final Cookies cookies = new Cookies();
		final Set set = cookies.entrySet();

		final Cookie appleCookie = new Cookie();
		appleCookie.setName(appleName);
		appleCookie.setValue(appleValue);

		final List list = new ArrayList();
		list.add(appleCookie);

		try {
			set.addAll(list);
			fail("An exception should have be thrown when attempting to Cookies.entrySet().addAll().");
		} catch (final Exception expected) {
		}
	}

	public void testEntrySetRemove() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
		assertTrue(getCookies(), values.remove(appleCookie));

		assertTrue(getCookies(), values.remove(bananaCookie));

		assertFalse(getCookies(), values.remove(carrotCookie));

		assertFalse(getCookies(), values.remove(appleCookie));

		assertTrue(getCookies(), cookies.isEmpty());
	}

	public void testEntrySetClear() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testEntrySetIteratorVisitAllUsingHasNextAndNext() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testEntrySetIteratorVisitAllUsingOnlyNext() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testEntrySetIteratorCallingExtraNextAfterIteratorIsExhaustedThrowsException() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
			fail("The Cookies.entrySet().iterator() should have thrown an exception because it is empty, object: " + object);
		} catch (final Exception expected) {

		}

	}

	public void testEntrySetIteratorNextFailsFastWhenCookieMapIsModified() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
			fail("The Cookies.entrySet().iterator() should have failed fast (throwing an exception) because the backing Cookies was modified, iterator: "
					+ iterator + ", cookies\"" + getCookies() + "\".");
		} catch (final Exception expected) {
		}

	}

	public void testEntrySetIteratorRemove() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testKeySetIsEmpty() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testKeySetSize() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testKeyContains() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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

		assertFalse(set.contains(NAME_OF_WHICH_DOESNT_EXIST));
	}

	public void testKeySetAddThrowsException() {
		final String appleName = APPLE;
		final String appleValue = "Green";

		try {
			final Cookies cookies = new Cookies();
			final Set set = cookies.keySet();

			final Cookie appleCookie = new Cookie();
			appleCookie.setName(appleName);
			appleCookie.setValue(appleValue);

			try {
				set.add(appleCookie);
				fail("An exception should be thrown when attempting to Cookies.keySet().add()");
			} catch (final Exception expected) {

			}
		} finally {
			removeCookie(appleName);
		}
	}

	public void testKeySetAddAllThrowsException() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";

		final Cookies cookies = new Cookies();
		final Set set = cookies.keySet();

		final Cookie appleCookie = new Cookie();
		appleCookie.setName(appleName);
		appleCookie.setValue(appleValue);

		final List list = new ArrayList();
		list.add(appleCookie);

		try {
			set.addAll(list);
			fail("An exception should have be thrown when attempting to Cookies.keySet().addAll().");
		} catch (final Exception expected) {
		}
	}

	public void testKeySetRemove() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testKeySetClear() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testKeySetIteratorVisitAllUsingHasNextAndNext() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testKeySetIteratorVisitAllUsingOnlyNext() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	public void testKeySetIteratorCallingExtraNextAfterIteratorIsExhaustedThrowsException() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
			fail("The Cookies.keySet().iterator() should have thrown an exception because it is empty, object: " + object);
		} catch (final Exception expected) {

		}
	}

	public void testKeySetIteratorNextFailsFastWhenCookieMapIsModified() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
			fail("The Cookies.keySet().iterator() should have failed fast (throwing an exception) because the backing Cookies was modified, iterator: "
					+ iterator);
		} catch (final Exception expected) {
		}
	}

	public void testKeySetIteratorRemove() {
		removeAllCookies();

		final String appleName = APPLE;
		final String appleValue = "Green";
		final String bananaName = BANANA;
		final String bananaValue = "yellow";
		final String carrotName = CARROT;
		final String carrotValue = "orange";

		final Cookies cookies = new Cookies();
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
	}

	static void removeAllCookies() {
		final String cookiesString = getCookies();
		final String[] cookies = StringHelper.split(cookiesString, CookieConstants.SEPARATOR_STRING, true);
		for (int i = 0; i < cookies.length; i++) {
			final String cookieString = cookies[i];
			final String[] components = StringHelper.split(cookieString, "" + CookieConstants.NAME_VALUE_SEPARATOR, true);
			final String name = components[0];
			removeCookie(name);
		}
	}

	/**
	 * JSNI method which returns all cookies for this browser as a single
	 * String.
	 */
	native static String getCookies()/*-{
	 var cookies = $doc.cookie;
	 return cookies ? cookies : "";
	 }-*/;

	static native void setCookie(final String cookie)/*-{
	 $doc.cookie = cookie;
	 }-*/;

	/**
	 * JSNI method which removes a cookie from the browser's cookie collection.
	 * This achieved by setting a cookie with an expires Date attribute set to
	 * yeseterday's timestamp.
	 * 
	 * @param name
	 */
	static void removeCookie(final String name) {
		setCookie(name + CookieConstants.REMOVE_SUFFIX);
	}

}
