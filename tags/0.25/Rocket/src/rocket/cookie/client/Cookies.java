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

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;

/**
 * Represents a Map view of the cookies belonging to this browser. All map keys are of type String and the values are Cookie
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Cookies extends AbstractMap {

    /**
     * Helper which creates a Cookie object to represent a cookie with the given name if one is found.
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
     * Creates an array of Strings with each String representing a single cookie.
     * 
     * @return
     */
    protected String[] createTokens() {
        final String cookies = CookieHelper.getCookies();
        return StringHelper.split(cookies, CookieConstants.COOKIE_SEPARATOR_STRING, true);
    }

    /**
     * Creates cookies Objects for each of the cookie tokens.
     * 
     * @return
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
        StringHelper.checkNotNull("parameter:cookieString", cookieString);

        final String[] attributes = StringHelper.split(cookieString, CookieConstants.COOKIE_SEPARATOR_STRING, true);

        final String nameValue = attributes[0];
        final int nameValueSeparator = nameValue.indexOf(CookieConstants.COOKIE_NAME_VALUE_SEPARATOR);
        if (nameValueSeparator == -1) {
            SystemHelper.fail("cookieString",
                    "The parameter:cookieString does not contain a valid cookie (name/value not found), cookieString["
                            + cookieString + "]");
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
        CookieHelper.setCookie(cookie.toCookieString());
        return replaced;
    }

    public Object remove(final Object key) {
        return this.removeCookie((String) key);
    }

    protected Cookie removeCookie(final String cookieName) {
        final Cookie removed = this.getCookie(cookieName);
        CookieHelper.removeCookie(cookieName);
        return removed;
    }

    public Set entrySet() {
        final CookieEntrySet set = new CookieEntrySet();
        set.setCookies(this);
        return set;
    }

    /**
     * Represents an entryset view of this Cookies Map
     */
    class CookieEntrySet extends AbstractSet {
        public Iterator iterator() {
            final CookiesIterator iterator = new CookiesIterator() {
                /**
                 * Wraps the element (A cookie) from CookiesIterator inside a Map.Entry.
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

            iterator.setCookies(this.getCookies().createCookies());
            iterator.setCursor(0);
            iterator.syncSnapShot();

            return iterator;
        }

        public int size() {
            return this.getCookies().size();
        }

        Cookies cookies;

        Cookies getCookies() {
            ObjectHelper.checkNotNull("field:cookies", cookies);
            return this.cookies;
        }

        void setCookies(final Cookies cookies) {
            ObjectHelper.checkNotNull("parameter:cookies", cookies);
            this.cookies = cookies;
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

            CookieHelper.removeCookie(remove.getName());
            // this.setCursor( cursor );

            this.syncSnapShot();
        }

        void syncSnapShot() {
            this.setSnapShot(CookieHelper.getCookies());
        }

        /**
         * A cached copy of Cookie objects belonging to this map view
         */
        Cookie[] cookies;

        Cookie[] getCookies() {
            ObjectHelper.checkNotNull("field:cookies", cookies);
            return this.cookies;
        }

        void setCookies(final Cookie[] cookies) {
            ObjectHelper.checkNotNull("parameter:cookies", cookies);
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
         * A snapshot of the cookies document property used to detect concurrent modifications.
         */
        String snapShot;

        String getSnapShot() {
            StringHelper.checkNotNull("field:snapShot", snapShot);
            return snapShot;
        }

        void setSnapShot(final String snapShot) {
            StringHelper.checkNotNull("parameter:snapShot", snapShot);
            this.snapShot = snapShot;
        }

        /**
         * Throws an exception if the snapshot doesnt match the current document cookies property
         */
        void changeGuard() {
            final String snapShot = this.getSnapShot();
            final String cookiesNow = CookieHelper.getCookies();
            if (false == snapShot.equals(cookiesNow)) {
                throw new ConcurrentModificationException(snapShot + "/" + cookiesNow);
            }
        }
    }
}
