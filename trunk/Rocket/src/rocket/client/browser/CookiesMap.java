package rocket.client.browser;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import rocket.client.collection.CollectionHelper;
import rocket.client.collection.VisitedRememberingIterator;
import rocket.client.dom.Destroyable;
import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;

import com.google.gwt.core.client.GWT;

/**
 * A live map of all cookies that are currently visible to the browser. Individual cookies must be re-put into this map to be reattached to
 * the browser itself.
 * 
 * TODO All Google Collection Iterators dont fail fast resulting in the iterators for this class also not failing fast.
 * 
 * When testing I encountered some strange behaviours when iterating using a HashMap iterator returned by keySet/entrySet/values. For some
 * strange reason after doing a iterator.next/iterator.remove the next(second) iterator.next results in a strange JavaSCriptException being
 * thrown wich includes a message complaining about something being null.
 * 
 * The iterators implemented below for {@link #keySet}, {@link #entrySet()}, {@link #values()} follow a similar pattern for implementing
 * Iterator.remove(). THe Iterator.remove() methods simply remove the cookie from the browser relying on any public method of CookieMap to
 * resynchronise its internal map as part of its staleness check {@link #checkIfStaleAndUpdateIfNecessary()}.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * TODO add destroy
 */
public class CookiesMap extends AbstractMap implements Destroyable{

    public CookiesMap() {
        this.createMap();
    }

    public int size() {
        this.checkIfStaleAndUpdateIfNecessary();
        return this.getMap().size();
    }

    public Object get(final Object cookieName) {
        this.checkIfStaleAndUpdateIfNecessary();
        return this.getMap().get(cookieName);
    }

    /**
     * Puts or updates the browsers cookies.
     * 
     * @param cookieName
     *            The cookieName or key must match the cookie name.
     * @param cookie
     * @return
     */
    public Object put(final Object cookieName, final Object cookie) {
        final String cookieName0 = (String) cookieName;
        BrowserHelper.checkCookieName("parameter:cookieName", cookieName0);

        ObjectHelper.checkNotNull("parameter:cookie", cookie);
        final Cookie cookie0 = (Cookie) cookie;

        this.checkIfStaleAndUpdateIfNecessary();

        final Cookie previous = (Cookie) this.getMap().put(cookieName0, cookie0);

        // update the browsers cookie collection.
        BrowserHelper.setCookie(cookie0.toCookieString());
        return previous;
    }

    /**
     * Removes a cookie from the browser
     */
    public Object remove(final Object cookieName) {
        this.checkIfStaleAndUpdateIfNecessary();
        BrowserHelper.removeCookie((String) cookieName);
        return this.getMap().remove(cookieName);
    }

    /**
     * Removes all cookies from the browser.
     */
    public void clear() {
        CollectionHelper.removeAll( this.keySet().iterator() );
    }

    // SETS VIEWS
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public Set keySet() {
        this.checkIfStaleAndUpdateIfNecessary();

        return new AbstractSet() {
            public int size() {
                return CookiesMap.this.size();
            }

            public boolean contains(final Object cookieName) {
                return CookiesMap.this.containsKey(cookieName);
            }

            public boolean add(final Object cookieName) {
                throw new UnsupportedOperationException("Add is not supported for " + GWT.getTypeName(CookiesMap.this) + " keySet.");
            }

            public boolean remove(final Object cookieName) {
                return null != CookiesMap.this.remove(cookieName);
            }

            public Iterator iterator() {

                final VisitedRememberingIterator iterator = new VisitedRememberingIterator() {
                    public void remove() {
                        final Map.Entry entry = (Map.Entry) this.getLastVisited();
                        this.getIterator().remove();

                        final String cookieName = (String) entry.getKey();
                        BrowserHelper.removeCookie(cookieName);
                        this.clearLastVisited();
                    }
                };
                iterator.setIterator(CookiesMap.this.getMap().keySet().iterator());

                return iterator;
            }
        };
    }

    public Collection values() {
        this.checkIfStaleAndUpdateIfNecessary();

        return new AbstractCollection() {

            public boolean add(final Object cookie) {
                throw new UnsupportedOperationException("Add is not supported for " + GWT.getTypeName(CookiesMap.this) + " values collection.");
            }

            public Iterator iterator() {
                final VisitedRememberingIterator wrapper = new VisitedRememberingIterator() {

                    public void remove() {
                        this.getIterator().remove();
                        final Cookie lastVisited = (Cookie) this.getLastVisited();
                        BrowserHelper.removeCookie(lastVisited.getName());
                        this.clearLastVisited();
                    }
                };
                wrapper.setIterator(CookiesMap.this.getMap().values().iterator());

                return wrapper;
            }

            public boolean remove(final Object cookie) {
                boolean removed = false;
                if (cookie instanceof Cookie) {
                    final Cookie cookie0 = (Cookie) cookie;
                    final Object removedCookie = CookiesMap.this.remove(cookie0.getName());
                    removed = (null != removedCookie);
                }
                return removed;
            }

            public int size() {
                return CookiesMap.this.size();
            }
        };
    }

    public Set entrySet() {
        this.checkIfStaleAndUpdateIfNecessary();

        return new AbstractSet() {
            public int size() {
                return CookiesMap.this.size();
            }

            public boolean contains(final Object cookieName) {
                return CookiesMap.this.containsKey(cookieName);
            }

            public boolean add(final Object cookieName) {
                throw new UnsupportedOperationException("Cannot call add() upon a " + GWT.getTypeName(CookiesMap.this) + ".entrySet() Set");
            }

            public boolean remove(final Object cookieName) {
                return null != CookiesMap.this.remove(cookieName);
            }

            public Iterator iterator() {

                final VisitedRememberingIterator iterator = new VisitedRememberingIterator() {
                    public void remove() {
                        final Map.Entry entry = (Map.Entry) this.getLastVisited();
                        this.getIterator().remove();

                        final Cookie cookie = (Cookie) entry.getKey();
                        BrowserHelper.removeCookie(cookie.getName());
                        this.clearLastVisited();
                    }
                };
                iterator.setIterator(CookiesMap.this.getMap().entrySet().iterator());

                return iterator;
            }
        };
    }

    // CONCRETE
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * This method performs a stale check between this Map and the browser itself.
     * 
     * All public method should preceed a call to the map with a checkIfStaleAndUpdateIfNecessary so ensure that the map is up to date.
     */
    protected void checkIfStaleAndUpdateIfNecessary() {
        if (false == this.hasCookieStringSnapshot()
                || false == BrowserHelper.getCookies().equals(this.getCookieStringSnapshot())) {
            this.syncWithBrowserCookies();
        }
    }

    /**
     * Repopulates the map with Cookie objects. If a cookie Object already exists that object has its attributes (bean properties) updated.
     */
    protected void syncWithBrowserCookies() {
        final Cookie[] cookies = this.mergeBrowserCookiesWithMap();
        final Map map = this.getMap();

        // clear the map
        map.clear();

        // populate the map.
        for (int i = 0; i < cookies.length; i++) {
            final Cookie cookie = cookies[i];
            map.put(cookie.getName(), cookie);
        }

        // update the string copy of the current cookies belonging to this
        // browser.
        this.setCookieStringSnapshot(BrowserHelper.getCookies());
    }

    /**
     * Creates an array of Cookie Objects that reflect the browsers current set of cookies. If the map already contains A cookie with that
     * name then that Cookie Object is updated.
     * 
     * @return An array of cookies including a merger with any updated Map cookies.
     */
    protected Cookie[] mergeBrowserCookiesWithMap() {
        final long now = System.currentTimeMillis();
        final Cookie[] browserCookies = this.createCookies();
        final Map mapCookies = this.getMap();

        // first find any existing cookie objects and update their attributes.
        for (int i = 0; i < browserCookies.length; i++) {
            final Cookie browserCookie = browserCookies[i];

            // check if the cookie has been expired. If so ignore it.
            final boolean hasExpires = browserCookie.hasExpires();
            Date expires = null;
            if (hasExpires) {
                expires = browserCookie.getExpires();
                if (expires.getTime() < now) {
                    continue;
                }
            }

            final String name = browserCookie.getName();
            final Cookie mapCookie = (Cookie) mapCookies.get(name);
            if (null == mapCookie) {
                continue;
            }

            // copy values from browserCookie to mapCookie...
            mapCookie.setValue(browserCookie.getValue());

            // comment
            if (browserCookie.hasComment()) {
                mapCookie.setComment(browserCookie.getComment());
            } else {
                mapCookie.clearComment();
            }
            // domain
            if (browserCookie.hasDomain()) {
                mapCookie.setDomain(browserCookie.getDomain());
            } else {
                mapCookie.clearDomain();
            }
            // expires
            if (hasExpires) {
                mapCookie.setExpires(expires);
            }

            // path
            if (browserCookie.hasPath()) {
                mapCookie.setPath(browserCookie.getPath());
            } else {
                mapCookie.clearPath();
            }
            // secure
            if (browserCookie.hasSecure()) {
                mapCookie.setSecure(browserCookie.isSecure());
            }

            // version
            if (browserCookie.hasVersion()) {
                mapCookie.setVersion(browserCookie.getVersion());
            } else {
                mapCookie.clearVersion();
            }
            // override the browserCookie element.
            browserCookies[i] = mapCookie;
        }
        return browserCookies;
    }

    /**
     * Factory method which creates Cookie objects for each and every cookie token within the browsers cookie collection String.
     * 
     * @return An array of cookies.
     */
    protected Cookie[] createCookies() {
        final String cookiesString = BrowserHelper.getCookies();

        final String[] cookies = StringHelper.split(cookiesString, BrowserConstants.COOKIE_SEPARATOR_STRING, true);

        final int cookieCount = cookies.length;
        final Cookie[] cookieObjects = new Cookie[cookieCount];

        for (int i = 0; i < cookieCount; i++) {
            final String cookieString = cookies[i];
            final Cookie cookie = this.createCookie(cookieString);
            cookieObjects[i] = cookie;
        }
        return cookieObjects;
    }

    /**
     * Factory method which creates a CookieObject given its string form.
     * 
     * @param cookieString
     * @return The cookie object.
     */
    protected Cookie createCookie(final String cookieString) {
        StringHelper.checkNotNull("parameter:cookieString", cookieString);

        final String[] attributes = StringHelper.split(cookieString, BrowserConstants.COOKIE_SEPARATOR_STRING, true);

        final String nameValue = attributes[0];
        final int nameValueSeparator = nameValue.indexOf(BrowserConstants.COOKIE_NAME_VALUE_SEPARATOR);
        if (nameValueSeparator == -1) {
            BrowserHelper.handleAssertFailure("cookieString",
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

    /**
     * A cached copy of the documents cookie string when the map was last updated. If this property doesnt match the actual browsers cookies
     * String the map must be resynched.
     */
    private String cookieStringSnapshot;

    protected String getCookieStringSnapshot() {
        StringHelper.checkNotNull("field:cookieStringSnapshot", cookieStringSnapshot);
        return this.cookieStringSnapshot;
    }

    protected boolean hasCookieStringSnapshot() {
        return null != this.cookieStringSnapshot;
    }

    protected void setCookieStringSnapshot(final String cookieStringSnapshot) {
        StringHelper.checkNotNull("parameter:cookieStringSnapshot", cookieStringSnapshot);
        this.cookieStringSnapshot = cookieStringSnapshot;
    }

    /**
     * A map that contains Cookie objects for each of the browser cookies. key= cookieName value = Cookie
     */
    private Map map;

    protected Map getMap() {
        ObjectHelper.checkNotNull("field:map", map);
        return map;
    }

    protected void setMap(final Map map) {
        ObjectHelper.checkNotNull("parameter:map", map);
        this.map = map;
    }

    protected void createMap() {
        this.setMap(new HashMap());
    }

    protected void clearMap() {
        this.map = null;
    }

    /**
     * Destroys the cache of Cookie wrappers. After calling this method this CookiesMap
     * should be considered invalid and no longer used.
     */
    public void destroy() {
        this.clearMap();
    }

    public String toString() {
        return super.toString() + ", map: " + map + ", cookieStringSnapshot[" + cookieStringSnapshot + "]";
    }
}
