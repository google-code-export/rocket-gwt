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
package rocket.cookie.client;

import java.util.Date;

import rocket.util.client.HttpHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;

/**
 * Represents a single browser cookie.
 * 
 * Note that setting any of the methods does not actually update the corresponding browser cookie. To update the browsers cookies this
 * cookie must be re-put back into the Cookies Map
 * 
 * Note that some properties (all except for name/value) are lost when the cookie Object is not created by the user but created as part of
 * an enquiry using a cookie name.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Cookie {

    public Cookie() {
    }

    /**
     * The name of the cookie.
     */
    private String name;

    public String getName() {
        CookieHelper.checkCookieName("field:name", this.name);

        return this.name;
    }

    public boolean hasName() {
        return this.name != null;
    }

    public void setName(final String name) {
        CookieHelper.checkCookieName("parameter:name", name);

        this.name = name;
    }

    /**
     * The cookie value
     */
    private String value;

    public String getValue() {
        StringHelper.checkNotEmpty("field:value", this.value);

        return this.value;
    }

    public boolean hasValue() {
        return this.value != null;
    }

    public void setValue(final String value) {
        StringHelper.checkNotEmpty("parameter:value", value);

        this.value = value;
    }

    /**
     * Any comment that may be attached with the cookie
     */
    private String comment;

    public String getComment() {
        StringHelper.checkNotEmpty("field:comment", this.comment);

        return this.comment;
    }

    public boolean hasComment() {
        return this.comment != null;
    }

    public void setComment(final String comment) {
        StringHelper.checkNotEmpty("parameter:comment", comment);

        this.comment = comment;
    }

    public void clearComment() {
        this.comment = null;
    }

    private String domain;

    public String getDomain() {
        StringHelper.checkNotEmpty("field:domain", this.domain);

        return this.domain;
    }

    public boolean hasDomain() {
        return this.domain != null;
    }

    public void setDomain(final String domain) {
        StringHelper.checkNotEmpty("parameter:domain", domain);

        this.domain = domain;
    }

    public void clearDomain() {
        this.domain = null;
    }

    /**
     * When the cookie expires which should be a ISO formatted date.
     */
    private Date expires;

    public Date getExpires() {
        ObjectHelper.checkNotNull("field:expires", expires);
        return this.expires;
    }

    public boolean hasExpires() {
        return null != this.expires;
    }

    public void setExpires(final Date expires) {
        ObjectHelper.checkNotNull("parameter:expires", expires);
        this.expires = expires;
    }

    private String path;

    public String getPath() {
        HttpHelper.checkPath("field:path", this.path);

        return this.path;
    }

    public boolean hasPath() {
        return this.path != null;
    }

    public void setPath(final String path) {
        HttpHelper.checkPath("parameter:path", path);

        this.path = path;
    }

    public void clearPath() {
        this.path = null;
    }

    private boolean secure;

    private boolean secureSet;

    public boolean isSecure() {
        PrimitiveHelper.checkBoolean("field:secure", this.hasSecure(), true);
        return this.secure;
    }

    public boolean hasSecure() {
        return secureSet;
    }

    public void setSecure(final boolean secure) {
        this.secure = secure;
        this.secureSet = true;
    }

    private int version = Integer.MIN_VALUE;

    private boolean versionSet;

    public int getVersion() {
        PrimitiveHelper.checkBoolean("field:version", this.hasVersion(), true);
        return this.version;
    }

    public boolean hasVersion() {
        return this.versionSet;
    }

    public void setVersion(final int version) {
        this.version = version;
        this.versionSet = true;
    }

    public void clearVersion() {
        this.versionSet = false;
    }

    /**
     * Converts this cookie into its string form.
     * 
     * @return String
     */
    public String toCookieString() {
        final StringBuffer buffer = new StringBuffer();

        buffer.append(this.getName());
        buffer.append('=');
        buffer.append(this.getValue());

        if (this.hasComment()) {
            buffer.append(CookieConstants.COOKIE_COMMENT);
            buffer.append(this.getComment());
        }
        if (this.hasDomain()) {
            buffer.append(CookieConstants.COOKIE_DOMAIN);
            buffer.append(this.getDomain());
        }
        if (this.hasExpires()) {
            buffer.append(CookieConstants.COOKIE_EXPIRES);
            buffer.append(this.getExpires().toGMTString());
        }
        if (this.hasPath()) {
            buffer.append(CookieConstants.COOKIE_PATH);
            buffer.append(this.getPath());
        }
        if (this.hasSecure()) {
            if (this.isSecure()) {
                buffer.append(CookieConstants.COOKIE_SECURE);
            }
        }
        if (this.hasVersion()) {
            buffer.append(CookieConstants.COOKIE_VERSION);
            buffer.append(this.getVersion());
        }

        return buffer.toString();
    }

    public boolean equals(final Object otherObject) {
        return otherObject instanceof Cookie ? this.equals((Cookie) otherObject) : false;
    }

    public boolean equals(final Cookie otherCookie) {
        return this.getName().equals(otherCookie.getName()) && this.getValue().equals(otherCookie.getValue());
    }

    /*
     * OBJECT :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    public String toString() {
        return super.toString() + ", name[" + name + "], value[" + value + "], comment[" + comment + "], domain["
                + domain + "], expires: " + expires + ", path: [" + path + "], secure: " + secure + ", secureSet: "
                + secureSet + ", version: " + version;
    }

}
