package rocket.cookie.client;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import rocket.util.client.Checker;
import rocket.util.client.Utilities;

/**
 * Represents a Map view of the cookies belonging to this browser. All map keys
 * are of type String and the values are Cookie
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Cookies extends AbstractMap {

	/**
	 * This method may be invoked to query whether the browser has cookies
	 * enabled.
	 * 
	 * @return True if cookies are enabled otherwise false.
	 */
	native public static boolean areEnabled()/*-{
	 return $wnd.navigator.cookieEnabled;
	 }-*/;

	/**
	 * JSNI method which returns all cookies for this browser as a single
	 * String.
	 */
	native static String getCookiesAsString()/*-{
	 var cookies = $doc.cookie;
	 return cookies ? cookies : "";
	 }-*/;

	/**
	 * Escapes to javascript to set a cookie value.
	 * 
	 * @param cookie
	 */
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

	static Cookies cookies = new Cookies();

	/**
	 * Retrieves the Cookies singleton
	 * 
	 * @return The cookies singleton
	 */
	public Cookies getCookies() {
		return Cookies.cookies;
	}

	/**
	 * Helper which creates a Cookie object to represent a cookie with the given
	 * name if one is found.
	 * 
	 * @param cookieName
	 * @return The corresponding cookie object or null if one was not found.
	 */
	protected Cookie getCookie(final String cookieName) {
		Cookie cookie = null;
		final Cookie[] cookies = this.createCookies();
		for (int i = 0; i < cookies.length; i++) {
			final Cookie otherCookie = (Cookie) cookies[i];
			if (cookieName.equals(otherCookie.getName())) {
				cookie = otherCookie;
				break;
			}
		}
		return cookie;
	}

	/**
	 * Creates an array of Strings with each String representing a single
	 * cookie.
	 * 
	 * @return A String array of cookies.
	 */
	protected String[] createTokens() {
		final String cookies = Cookies.getCookiesAsString();
		return Utilities.split(cookies, CookieConstants.SEPARATOR_STRING, true);
	}

	/**
	 * Creates Cookies objects for each of the cookie tokens.
	 * 
	 * @return An array of Cookie objects
	 */
	protected Cookie[] createCookies() {
		final String[] tokens = this.createTokens();

		final int cookieCount = tokens.length;
		final Cookie[] cookies = new Cookie[cookieCount];

		for (int i = 0; i < cookieCount; i++) {
			final String cookieString = tokens[i];
			final Cookie cookie = this.createCookie(cookieString);
			cookies[i] = cookie;
		}
		return cookies;
	}

	/**
	 * Factory method which creates a CookieObject given its string form.
	 * 
	 * @param cookieString
	 * @return The cookie object.
	 */
	protected Cookie createCookie(final String cookieString) {
		Checker.notNull("parameter:cookieString", cookieString);

		final String[] attributes = Utilities.split(cookieString, CookieConstants.SEPARATOR_STRING, true);

		final String nameValue = attributes[0];
		final int nameValueSeparator = nameValue.indexOf(CookieConstants.NAME_VALUE_SEPARATOR);
		if (nameValueSeparator == -1) {
			Checker.fail("cookieString",
							"The parameter:cookieString does not contain a valid cookie (name/value not found), cookieString\""
									+ cookieString + "\".");
		}
		final String name = nameValue.substring(0, nameValueSeparator).trim();
		final String value = nameValue.substring(nameValueSeparator + 1).trim();

		final Cookie cookie = new Cookie();
		cookie.setName(name);
		cookie.setValue(value);
		return cookie;
	}

	public int size() {
		return this.createTokens().length;
	}

	public Object get(final Object key) {
		return this.getCookie((String) key);
	}

	public Object put(final Object key, final Object value) {
		return this.putCookie((String) key, (Cookie) value);
	}

	protected Cookie putCookie(final String cookieName, final Cookie cookie) {
		final Cookie replaced = this.getCookie(cookieName);
		Cookies.setCookie(cookie.toCookieString());
		return replaced;
	}

	public Object remove(final Object key) {
		final String cookieName = (String) key;
		final Cookie removed = this.getCookie(cookieName);
		Cookies.removeCookie(cookieName);
		return removed;
	}

	public Set entrySet() {
		return new CookieEntrySet();
	}

	/**
	 * Represents an entryset view of this Cookies Map
	 */
	class CookieEntrySet extends AbstractSet {
		public Iterator iterator() {
			final CookiesIterator iterator = new CookiesIterator() {
				/**
				 * Wraps the element (A cookie) from CookiesIterator inside a
				 * Map.Entry.
				 */
				public Object next() {
					final Cookie cookie = (Cookie) super.next();

					final CookiesIterator that = this;

					return new Map.Entry() {
						public Object getKey() {
							return cookie.getName();
						}

						public Object getValue() {
							return cookie;
						}

						public Object setValue(final Object value) {
							final Cookie cookie = (Cookie) value;
							final String name = cookie.getName();
							final Object previous = Cookies.this.put(name, cookie);
							that.syncSnapShot();
							return previous;
						}
					};
				}
			};

			iterator.setCookies(Cookies.this.createCookies());
			iterator.setCursor(0);
			iterator.syncSnapShot();

			return iterator;
		}

		public int size() {
			return Cookies.this.size();
		}
	}

	public Collection values() {
		return new AbstractCollection() {

			public boolean add(final Object newElement) {
				throw new UnsupportedOperationException();
			}

			public Iterator iterator() {
				final CookiesIterator iterator = new CookiesIterator();
				iterator.setCookies(Cookies.this.createCookies());
				iterator.setCursor(0);
				iterator.syncSnapShot();
				return iterator;
			}

			public int size() {
				return Cookies.this.size();
			}
		};
	}

	/**
	 * This iterator loops over all the cookies belonging to the browser.
	 */
	static class CookiesIterator implements Iterator {
		public boolean hasNext() {
			this.changeGuard();

			return this.getCursor() < this.getCookies().length;
		}

		public Object next() {
			this.changeGuard();

			final Cookie[] cookies = this.getCookies();
			final int cursor = this.getCursor();
			if (cursor >= cookies.length) {
				throw new NoSuchElementException();
			}
			final Cookie cookie = cookies[cursor];
			this.setCursor(cursor + 1);
			return cookie;
		}

		public void remove() {
			this.changeGuard();

			final int cursor = this.getCursor() - 1;
			if (cursor < 0) {
				throw new IllegalStateException();
			}

			final Cookie[] cookies = this.getCookies();
			final Cookie remove = cookies[cursor];

			// will be null if the element has already been removed...
			if (null == remove) {
				throw new IllegalStateException();
			}
			cookies[cursor] = null;

			Cookies.removeCookie(remove.getName());
			// this.setCursor( cursor );

			this.syncSnapShot();
		}

		void syncSnapShot() {
			this.setSnapShot(Cookies.getCookiesAsString());
		}

		/**
		 * A cached copy of Cookie objects belonging to this view
		 */
		Cookie[] cookies;

		Cookie[] getCookies() {
			Checker.notNull("field:cookies", cookies);
			return this.cookies;
		}

		void setCookies(final Cookie[] cookies) {
			Checker.notNull("parameter:cookies", cookies);
			this.cookies = cookies;
		}

		/**
		 * Points to the next cursor in the Cookies array.
		 */
		int cursor;

		int getCursor() {
			return cursor;
		}

		void setCursor(final int cursor) {
			this.cursor = cursor;
		}

		/**
		 * A snapshot of the cookies document property used to detect concurrent
		 * modifications.
		 */
		String snapShot;

		String getSnapShot() {
			Checker.notNull("field:snapShot", snapShot);
			return snapShot;
		}

		void setSnapShot(final String snapShot) {
			Checker.notNull("parameter:snapShot", snapShot);
			this.snapShot = snapShot;
		}

		/**
		 * Throws an exception if the snapshot doesnt match the current document
		 * cookies property
		 */
		void changeGuard() {
			final String snapShot = this.getSnapShot();
			final String cookiesNow = Cookies.getCookiesAsString();
			if (false == snapShot.equals(cookiesNow)) {
				throw new ConcurrentModificationException(snapShot + "/" + cookiesNow);
			}
		}
	}
}
