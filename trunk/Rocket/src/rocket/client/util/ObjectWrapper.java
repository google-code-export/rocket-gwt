package rocket.client.util;

import rocket.client.dom.DomHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Convenient base class for any JavascriptObject wrapper. It provides typed methods that make it easy to read or write to object
 * properties.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class ObjectWrapper {

    protected ObjectWrapper() {
    }

    /**
     * The javascript object being wrapped
     */
    private JavaScriptObject object;

    public JavaScriptObject getObject() {
        ObjectHelper.checkNotNull("field:object", object);
        return object;
    }

    public boolean hasObject() {
        return null != this.object;
    }

    public void setObject(final JavaScriptObject object) {
        ObjectHelper.checkNotNull("parameter:object", object);
        this.object = object;
    }

    /**
     * Returns the string form of the object being wrapped.
     * 
     * @return
     */
    protected native String toStringObject()/*-{
     return object ? object.toString() : "null";
     }-*/;

    // A VARIETY OF CONVENIENT TYPED PROPERTY METHODS.

    protected boolean hasProperty(final String name) {
        return DomHelper.hasProperty(this.getObject(), name);
    }

    protected String getProperty(final String name) {
        return DomHelper.getProperty(this.getObject(), name);
    }

    protected boolean getBooleanProperty(final String name) {
        return DomHelper.getBooleanProperty(this.getObject(), name);
    }

    protected int getIntProperty(final String name) {
        return DomHelper.getIntProperty(this.getObject(), name);
    }

    protected void setProperty(final String name, final boolean booleanValue) {
        DomHelper.setProperty(this.getObject(), name, booleanValue);
    }

    protected void setProperty(final String name, final int intValue) {
        DomHelper.setProperty(this.getObject(), name, intValue);
    }

    protected void setProperty(final String name, final String value) {
        DomHelper.setProperty(this.getObject(), name, value);
    }

    public String toString() {
        return super.toString() + ", object[" + this.toStringObject() + "]";
    }
}
