/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

/**
 * A collection of useful methods when working with objects in general. Primarily this class includes static check(assert) methods.
 * 
 * @author Miroslav Pokorny
 * @version 1.0
 */
public class ObjectHelper extends SystemHelper {
    /**
     * This helper should be used primarily by beans when verifying their state.
     * 
     * @param propertyName
     *            The name of the property
     * @param bean
     *            The bean itself
     * @param set
     *            A indicates that the property is set, false indicates it wiring etc failed.
     */
    public static void checkPropertyNotSet(final String propertyName, final Object bean, final boolean set) {
        if (set) {
            handlePropertySet(propertyName, bean);
        }
    }

    public static void handlePropertySet(final String propertyName, final Object bean) {
        StringHelper.checkNotEmpty("assert:propertyName", propertyName);
        ObjectHelper.checkNotNull("assert:bean", bean);

        SystemHelper.handleAssertFailure(propertyName, "The property [" + propertyName + "] should not be set, bean: "
                + bean);
    }

    /**
     * This helper should be used primarily by beans when verifying their state.
     * 
     * @param propertyName
     *            The name of the property
     * @param bean
     *            The bean itself
     * @param missing
     *            A indicates that the property is set, false indicates it wiring etc failed.
     */
    public static void checkPropertySet(final String propertyName, final Object bean, final boolean set) {
        if (false == set) {
            handlePropertyMissing(propertyName, bean);
        }
    }

    public static void handlePropertyMissing(final String propertyName, final Object bean) {
        StringHelper.checkNotEmpty("assert:propertyName", propertyName);
        ObjectHelper.checkNotNull("assert:bean", bean);

        SystemHelper.handleAssertFailure(propertyName, "The property [" + propertyName
                + "] has not been set, typically indicating a wiring problem, bean: " + bean);
    }

    /**
     * A null safe equals that checks that both objects are not null and then invokes first.equals( second ). If one parmeter is not null it
     * cannot return true if the other parameter is null.
     * 
     * @param first
     *            Object
     * @param second
     *            Object
     * @return True if both objects are equal or both are null
     */
    public static boolean nullSafeEquals(final Object first, final Object second) {

        boolean result = false;

        while (true) {
            if (null == first && null == second) {
                result = true;
                break;
            }
            if (null == first && null != second) {
                result = false;
                break;
            }
            if (null != first && null == second) {
                result = false;
                break;
            }
            result = first.equals(second);
            break;
        }
        return result;
    }

    /**
     * A null safe identity that checks that both objects are not null and then invokes does a first == second. If one parmeter is not null
     * it cannot return true if the other parameter is null.
     * 
     * @param first
     *            Object
     * @param second
     *            Object
     * @return True if both objects are the same or both are null.
     */
    public static boolean nullSafeIdentity(final Object first, final Object second) {
        return null == first && null == second ? true : first == second;
    }

    /**
     * Return the default java.lang.Object.toString() for the given object.
     * 
     * @param object
     *            The object to format
     * @return String the default format representation of the given object. className - the at sign - the Objects hashcode ( in hex form
     *         without the leading '0x' ) java.lang.Object@123def
     */
    public static String defaultToString(final Object object) {
        return object == null ?
        /* handle null */
        String.valueOf(object) :
        /* class name including the leading package name */
        GWT.getTypeName(object) + '@' +
        /* hashcode */
        Integer.toHexString(System.identityHashCode(object));
    } // defaultToString

    public static void checkNotNull(final String name, final Object object) {
        if (object == null) {
            handleNullEncountered(name, "The " + name + " must not be null.");
        }
    }

    /**
     * This method is only included because calls to {@link #checkNotNull(String, Object)} result in the compiled javascript including a
     * call to a function which attempts to modify the object's prototype. This fails for certain native objects that have unmodifiable
     * prototypes.
     * 
     * {@see http://code.google.com/p/google-web-toolkit/issues/detail?id=304}
     * 
     * @param name
     * @param object
     * 
     * @deprecated This method will be removed when the GWT issue is fixed.
     */
    public static void checkNotNull(final String name, final JavaScriptObject object) {
        if (object == null) {
            handleNullEncountered(name, "The " + name + " must not be null.");
        }
    }

    public static void handleNullEncountered(String name, String message) {
        handleAssertFailure(name, message);
    }

    /**
     * Asserts that the two objects are in fact the same.
     * 
     * @param message
     * @param object
     * @param otherObject
     */
    public static void checkSame(final String message, final Object object, final Object otherObject) {
        if (false == nullSafeIdentity(object, otherObject)) {
            SystemHelper.handleAssertFailure(message + ", object: " + object);
        }
    }

    public static void checkNotSame(final String message, final Object object, final Object otherObject) {
        if (nullSafeIdentity(object, otherObject)) {
            SystemHelper.handleAssertFailure(message);
        }
    }

    /**
     * Asserts that the two objects are in fact different objects.
     * 
     * @param message
     * @param object
     * @param otherObject
     */
    public static void checkDifferent(final String message, final Object object, final Object otherObject) {
        if (nullSafeIdentity(object, otherObject)) {
            SystemHelper.handleAssertFailure(message);
        }
    }

    public static void checkNull(final String name, final Object object) {
        if (object != null) {
            handleNonNullEncountered(name, "The " + name + " must be null. name: " + object);
        }
    }

    public static void handleNonNullEncountered(String name, String message) {
        handleAssertFailure(name, message);
    }

    /**
     * Asserts that the two objects are in fact the equal or both are null.
     * 
     * @param message
     * @param object
     * @param otherObject
     */
    public static void checkEquals(final String message, final Object object, final Object otherObject) {
        if (false == nullSafeEquals(object, otherObject)) {
            SystemHelper.handleAssertFailure(message);
        }
    }

    /**
     * Tests if a particular property is present on the given object.
     * 
     * @param object
     * @param propertyName
     * @return
     */
    public static boolean hasProperty(final JavaScriptObject object, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return hasProperty0(object, propertyName);
    }

    protected static native boolean hasProperty0(final JavaScriptObject object, final String propertyName)/*-{
     var value = object[ propertyName ];
     
     return typeof( value ) != "undefined"; 
     }-*/;

    public static boolean hasProperty(final JavaScriptObject object, final int index) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return hasProperty0(object, index);
    }

    protected static native boolean hasProperty0(final JavaScriptObject object, final int index)/*-{
     var value = object[ cursor ];
     
     return typeof( value ) != "undefined"; 
     }-*/;

    /**
     * Retrieves an object property as a String.
     * 
     * @param object
     * @param propertyName
     * @return
     */
    public static String getString(final JavaScriptObject object, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return getString0(object, propertyName);
    }

    protected static native String getString0(final JavaScriptObject object, final String propertyName)/*-{
     var value = object[ propertyName ];
     if( typeof( value ) == "undefined" ){
     throw "The object does not contain a property called [" + propertyName + "], object: " + object; 
     }
     return value;
     }-*/;

    public static String getString(final JavaScriptObject object, final int index) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return getString0(object, index);
    }

    protected static native String getString0(final JavaScriptObject object, final int index)/*-{
     var value = object[ cursor ];
     if( typeof( value ) == "undefined" ){
     throw "The object does not contain a property called [" + cursor + "], object: " + object; 
     }
     return value;
     }-*/;

    /**
     * Writes a String value to an Object's property
     * 
     * @param object
     * @param propertyName
     * @param value
     * @return
     */
    public static Object setString(final JavaScriptObject object, final String propertyName, final String value) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return setString0(object, propertyName, value);
    }

    protected static native String setString0(final JavaScriptObject object, final String propertyName,
            final String value)/*-{
     var previousValue = object[ propertyName ];
     object[ propertyName ] = value;
     return previousValue ? previousValue : null;
     }-*/;

    public static Object setString(final JavaScriptObject object, final int index, final String value) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return setString0(object, index, value);
    }

    protected static native String setString0(final JavaScriptObject object, final int index, final String value)/*-{
     var previousValue = object[ cursor ];
     object[ cursor ] = value;
     return previousValue ? previousValue : null;
     }-*/;

    /**
     * Reads an object's property as a double.
     * 
     * @param object
     * @param propertyName
     * @return
     */
    public static double getDouble(final JavaScriptObject object, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return getDouble0(object, propertyName);
    }

    public native static double getDouble0(final JavaScriptObject object, final String propertyName)/*-{
     var value = object[ propertyName ];
     if( typeof( value ) == "undefined" ){
     throw "The object does not contain a property called [" + propertyName + "], object: " + object; 
     }
     return value;
     }-*/;

    public static double getDouble(final JavaScriptObject object, final int index) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return getDouble0(object, index);
    }

    public native static double getDouble0(final JavaScriptObject object, final int index)/*-{
     var value = object[ cursor ];
     if( typeof( value ) == "undefined" ){
     throw "The object does not contain a property called [" + cursor + "], object: " + object; 
     }
     return value;
     }-*/;

    /**
     * Writes a double value to an object's property
     * 
     * @param object
     * @param propertyName
     * @param value
     * @return
     */
    public static double setDouble(final JavaScriptObject object, final String propertyName, final double value) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return setDouble0(object, propertyName, value);
    }

    protected static native double setDouble0(final JavaScriptObject object, final String propertyName,
            final double value)/*-{
     var previousValue = object[ propertyName ];
     object[ propertyName ] = value;
     return previousValue ? previousValue : 0.0;
     }-*/;

    public static double setDouble(final JavaScriptObject object, final int index, final double value) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return setDouble0(object, index, value);
    }

    protected static native double setDouble0(final JavaScriptObject object, final int index, final double value)/*-{
     var previousValue = object[ cursor ];
     object[ cursor ] = value;
     return previousValue ? previousValue : 0.0;
     }-*/;

    /**
     * Reads an object's property as a boolean value.
     * 
     * @param object
     * @param propertyName
     * @return
     */
    public static boolean getBoolean(final JavaScriptObject object, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return getBoolean0(object, propertyName);
    }

    protected native static boolean getBoolean0(final JavaScriptObject object, final String propertyName)/*-{
     var value = object[ propertyName ];
     if( typeof( value ) == "undefined" ){
     throw "The object does not contain a property called [" + propertyName + "], object: " + object; 
     }
     return value;
     }-*/;

    public static boolean getBoolean(final JavaScriptObject object, final int index) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return getBoolean0(object, index);
    }

    protected native static boolean getBoolean0(final JavaScriptObject object, final int index)/*-{
     var value = object[ cursor ];
     if( typeof( value ) == "undefined" ){
     throw "The object does not contain a property called [" + cursor + "], object: " + object; 
     }
     return value;
     }-*/;

    /**
     * Writes a boolean value to an object's property.
     * 
     * @param object
     * @param propertyName
     * @param booleanValue
     */
    public static void setBoolean(final JavaScriptObject object, final String propertyName, final boolean booleanValue) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        setBoolean0(object, propertyName, booleanValue);
    }

    protected static native boolean setBoolean0(final JavaScriptObject object, final String propertyName,
            final boolean booleanValue)/*-{
     var previousValue = object[ propertyName ];
     object[ propertyName ] = booleanValue;
     return previousValue ? true : false;
     }-*/;

    public static void setBoolean(final JavaScriptObject object, final int index, final boolean booleanValue) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        setBoolean0(object, index, booleanValue);
    }

    protected static native boolean setBoolean0(final JavaScriptObject object, final int index,
            final boolean booleanValue)/*-{
     var previousValue = object[ cursor ];
     object[ cursor ] = booleanValue;
     return previousValue ? true : false;
     }-*/;

    /**
     * Reads an object's property as an integer value.
     * 
     * @param object
     * @param propertyName
     * @return
     */
    public static int getInteger(final JavaScriptObject object, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return getInteger0(object, propertyName);
    }

    public native static int getInteger0(final JavaScriptObject object, final String propertyName)/*-{
     var value = object[ propertyName ];
     if( typeof( value ) == "undefined" ){
     throw "The object does not contain a property called [" + propertyName + "], object: " + object; 
     }
     return value;
     }-*/;

    public static int getInteger(final JavaScriptObject object, final int index) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return getInteger0(object, index);
    }

    public native static int getInteger0(final JavaScriptObject object, final int index)/*-{
     var value = object[ cursor ];
     if( typeof( value ) == "undefined" ){
     throw "The object does not contain a property called [" + cursor + "], object: " + object; 
     }
     return value;
     }-*/;

    /**
     * Writes an integer value to an object's property
     * 
     * @param object
     * @param propertyName
     * @param intValue
     * @return
     */
    public static int setInteger(final JavaScriptObject object, final String propertyName, final int intValue) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return setInteger0(object, propertyName, intValue);
    }

    protected static native int setInteger0(final JavaScriptObject object, final String propertyName, final int intValue)/*-{
     var previousValue = object[ propertyName ];
     object[ propertyName ] = intValue;
     return previousValue;
     }-*/;

    public static int setInteger(final JavaScriptObject object, final int index, final int intValue) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return setInteger0(object, index, intValue);
    }

    protected static native int setInteger0(final JavaScriptObject object, final int index, final int intValue)/*-{
     var previousValue = object[ cursor ];
     object[ cursor ] = intValue;
     return previousValue;
     }-*/;

    /**
     * Reads an object's property as an Object
     * 
     * @param object
     * @param propertyName
     * @return
     */
    public static JavaScriptObject getObject(final JavaScriptObject object, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return getObject0(object, propertyName);
    }

    protected static native JavaScriptObject getObject0(final JavaScriptObject object, final String propertyName)/*-{
     var value = object[ propertyName ];
     // value shouldnt be undefined or null.
     if( typeof( value ) == "undefined" ){
     throw "The object does not contain a property called [" + propertyName + "], object: " + object;      
     }
     return value;
     }-*/;

    public static JavaScriptObject getObject(final JavaScriptObject object, final int index) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return getObject0(object, index);
    }

    protected static native JavaScriptObject getObject0(final JavaScriptObject object, final int index)/*-{
     var value = object[ cursor ];
     // value shouldnt be undefined or null.
     if( typeof( value ) == "undefined" ){
     throw "The object does not contain a property called [" + cursor + "], object: " + object;      
     }
     return value;
     }-*/;

    /**
     * Writes an object to an object's property.
     * 
     * @param object
     * @param propertyName
     * @param value
     * @return
     */
    public static JavaScriptObject setObject(final JavaScriptObject object, final String propertyName,
            final JavaScriptObject value) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return setObject0(object, propertyName, value);
    }

    protected static native JavaScriptObject setObject0(final JavaScriptObject object, final String propertyName,
            final JavaScriptObject value)/*-{
     var previousValue = object[ propertyName ];
     object[ propertyName ] = value;
     return previousValue ? previousValue : null;
     }-*/;

    public static JavaScriptObject setObject(final JavaScriptObject object, final int index,
            final JavaScriptObject value) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return setObject0(object, index, value);
    }

    protected static native JavaScriptObject setObject0(final JavaScriptObject object, final int index,
            final JavaScriptObject value)/*-{
     var previousValue = object[ cursor ];
     object[ cursor ] = value;
     return previousValue ? previousValue : null;
     }-*/;

    /**
     * Removes or deletes a property from the given object.
     * 
     * @param object
     * @param propertyName
     * @return The properties previous value.
     */
    public static JavaScriptObject removeProperty(final JavaScriptObject object, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return removeProperty0(object, propertyName);
    }

    protected static native JavaScriptObject removeProperty0(final JavaScriptObject object, final String propertyName)/*-{
     var previousValue = object[ propertyName ];
     delete object[ propertyName ];
     return previousValue ? previousValue : null;
     }-*/;

    public static JavaScriptObject removeProperty(final JavaScriptObject object, final int index) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return removeProperty0(object, index);
    }

    protected static native JavaScriptObject removeProperty0(final JavaScriptObject object, final int index)/*-{
     var previousValue = object[ cursor ];
     delete object[ cursor ];
     return previousValue ? previousValue : null;
     }-*/;

    /**
     * Retrieves the actual javascript type for the property value.
     * 
     * @param object
     * @param propertyName
     * @return
     */
    public static String getType(final JavaScriptObject object, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        return ObjectHelper.getType0(object, propertyName);
    }

    native protected static String getType0(final JavaScriptObject object, final String propertyName)/*-{
     return typeof( object[ propertyName ] );
     }-*/;

    /**
     * Retrieves the actual javascript type for the property value at the given slot.
     * 
     * @param object
     * @param cursor
     * @return
     */
    public static String getType(final JavaScriptObject object, final int index) {
        ObjectHelper.checkNotNull("parameter:object", object);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:cursor", index, 0);
        return ObjectHelper.getType0(object, index);
    }

    native protected static String getType0(final JavaScriptObject object, final int index)/*-{
     return typeof( object[ cursor ] );
     }-*/;

    public static int getPropertyCount(final JavaScriptObject object) {
        ObjectHelper.checkNotNull("parameter:object", object);

        return getPropertyCount0(object);
    }

    /**
     * Retrieves the property count for the given native object. If the length property is present that value is returned otherwise a for
     * each loop is used to count all the properties
     * 
     * @param nativeObject
     * @return
     */
    native protected static int getPropertyCount0(final JavaScriptObject nativeObject)/*-{
     var propertyCount = nativeObject.length;
     if( typeof( propertyCount ) != "number" ){
     
     // length not found need to count properties...
     propertyCount = 0;
     for( propertyName in nativeObject ){
     propertyCount++;
     }
     }
     return propertyCount;
     }-*/;

    /**
     * Convenience method which takes a JavaScriptObject and casts it to an Element.
     * 
     * This is provided purely as a mechanism to make a JSO reference into an Element.
     * 
     * @param object
     * @return
     */
    public native static Element castToElement(final JavaScriptObject object)/*-{
     return object;
     }-*/;

    /**
     * Convenience method which takes a Element and returns a JavaScriptObject
     * 
     * @param object
     * @return
     */
    public native static JavaScriptObject castFromElement(final Element element)/*-{
     return element;
     }-*/;

    protected ObjectHelper() {
    }
}