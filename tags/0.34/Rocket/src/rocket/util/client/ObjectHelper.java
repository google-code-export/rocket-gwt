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
package rocket.util.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

/**
 * A collection of useful methods when working with objects in general.
 * Primarily this class includes static check(assert) methods.
 * 
 * @author Miroslav Pokorny
 * @version 1.0
 */
public class ObjectHelper extends SystemHelper {
	/**
	 * A safe way of checking if both objects are equal or both are null.
	 * 
	 * @param first
	 *            Object
	 * @param second
	 *            Object
	 * @return True if they are both null or equal.
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
	 * A null safe identity that checks that both objects are not null and then
	 * invokes does a first == second. If one parmeter is not null it cannot
	 * return true if the other parameter is null.
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
	 * @return String the default format representation of the given object.
	 *         className - the at sign - the Objects hashcode ( in hex form
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

	public static void handleNullEncountered(String name, String message) {
		fail(name, message);
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
			fail(message + ", object: " + object);
		}
	}

	public static void checkNotSame(final String message, final Object object, final Object otherObject) {
		if (nullSafeIdentity(object, otherObject)) {
			fail(message);
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
			fail(message);
		}
	}

	public static void checkNull(final String name, final Object object) {
		if (object != null) {
			handleNonNullEncountered(name, "The " + name + " must be null. name: " + object);
		}
	}

	public static void handleNonNullEncountered(String name, String message) {
		fail(name, message);
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
			SystemHelper.fail(message);
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

	private static native boolean hasProperty0(final JavaScriptObject object, final String propertyName)/*-{
	 var value = object[ propertyName ];
	 
	 return typeof( value ) != "undefined"; 
	 }-*/;

	/**
	 * Tests if the given javascript object has a property at the given slot
	 * 
	 * @param object
	 * @param index
	 * @return
	 */
	public static boolean hasProperty(final JavaScriptObject object, final int index) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return hasProperty0(object, index);
	}

	private static native boolean hasProperty0(final JavaScriptObject object, final int index)/*-{
	 var value = object[ index ];     
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

	private static native String getString0(final JavaScriptObject object, final String propertyName)/*-{
	 var value = object[ propertyName ];
	 return value || null;
	 }-*/;

	public static String getString(final JavaScriptObject object, final int index) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return getString0(object, index);
	}

	private static native String getString0(final JavaScriptObject object, final int index)/*-{
	 var value = object[ index ];
	 return value || null;
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

	private static native String setString0(final JavaScriptObject object, final String propertyName, final String value)/*-{
	 var previousValue = object[ propertyName ];
	 object[ propertyName ] = value;
	 return previousValue || null;
	 }-*/;

	public static Object setString(final JavaScriptObject object, final int index, final String value) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return setString0(object, index, value);
	}

	private static native String setString0(final JavaScriptObject object, final int index, final String value)/*-{
	 var previousValue = object[ index ];
	 object[ index ] = value;
	 return previousValue || null;
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

	private native static double getDouble0(final JavaScriptObject object, final String propertyName)/*-{
	 var value = object[ propertyName ];
	 if( typeof( value ) == "undefined" ){
	 throw "The object does not contain a property called [" + propertyName + "], object: " + object; 
	 }
	 return value;
	 }-*/;

	public static double getDouble(final JavaScriptObject object, final int index) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return getDouble0(object, index);
	}

	private native static double getDouble0(final JavaScriptObject object, final int index)/*-{
	 var value = object[ index ];
	 if( typeof( value ) == "undefined" ){
	 throw "The object does not contain a property called [" + index + "], object: " + object; 
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

	private static native double setDouble0(final JavaScriptObject object, final String propertyName, final double value)/*-{
	 var previousValue = object[ propertyName ];
	 object[ propertyName ] = value;
	 return previousValue || 0.0;
	 }-*/;

	public static double setDouble(final JavaScriptObject object, final int index, final double value) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return setDouble0(object, index, value);
	}

	private static native double setDouble0(final JavaScriptObject object, final int index, final double value)/*-{
	 var previousValue = object[ index ];
	 object[ index ] = value;
	 return previousValue || 0.0;
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

	private native static boolean getBoolean0(final JavaScriptObject object, final String propertyName)/*-{
	 var value = object[ propertyName ];
	 if( typeof( value ) == "undefined" ){
	 throw "The object does not contain a property called [" + propertyName + "], object: " + object; 
	 }
	 return value;
	 }-*/;

	public static boolean getBoolean(final JavaScriptObject object, final int index) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return getBoolean0(object, index);
	}

	private native static boolean getBoolean0(final JavaScriptObject object, final int index)/*-{
	 var value = object[ index ];
	 if( typeof( value ) == "undefined" ){
	 throw "The object does not contain a property called [" + index + "], object: " + object; 
	 }
	 return value;
	 }-*/;

	/**
	 * Writes a boolean value to an object's property.
	 * 
	 * @param object
	 *            The object
	 * @param propertyName
	 *            The property name
	 * @param booleanValue
	 *            THe new value
	 */
	public static void setBoolean(final JavaScriptObject object, final String propertyName, final boolean booleanValue) {
		ObjectHelper.checkNotNull("parameter:object", object);
		StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
		setBoolean0(object, propertyName, booleanValue);
	}

	private static native boolean setBoolean0(final JavaScriptObject object, final String propertyName, final boolean booleanValue)/*-{
	 var previousValue = object[ propertyName ];
	 object[ propertyName ] = booleanValue;
	 return previousValue || false;
	 }-*/;

	public static void setBoolean(final JavaScriptObject object, final int index, final boolean booleanValue) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		setBoolean0(object, index, booleanValue);
	}

	private static native boolean setBoolean0(final JavaScriptObject object, final int index, final boolean booleanValue)/*-{
	 var previousValue = object[ index ];
	 object[ index ] = booleanValue;
	 return previousValue || false;
	 }-*/;

	/**
	 * Reads an object's property as an integer value.
	 * 
	 * @param object
	 *            The object
	 * @param propertyName
	 *            The name of the property being read
	 * @return The value
	 */
	public static int getInteger(final JavaScriptObject object, final String propertyName) {
		ObjectHelper.checkNotNull("parameter:object", object);
		StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
		return getInteger0(object, propertyName);
	}

	private native static int getInteger0(final JavaScriptObject object, final String propertyName)/*-{
	 var value = object[ propertyName ];
	 if( typeof( value ) == "undefined" ){
	 throw "The object does not contain a property called [" + propertyName + "], object: " + object; 
	 }
	 return value;
	 }-*/;

	public static int getInteger(final JavaScriptObject object, final int index) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return getInteger0(object, index);
	}

	private native static int getInteger0(final JavaScriptObject object, final int index)/*-{
	 var value = object[ index ];
	 if( typeof( value ) == "undefined" ){
	 throw "The object does not contain a property called [" + index + "], object: " + object; 
	 }
	 return value;
	 }-*/;

	/**
	 * Writes an integer value to an object's property
	 * 
	 * @param object
	 *            The object
	 * @param propertyName
	 *            The name of the property being set
	 * @param intValue
	 *            The new value
	 * @return The previous value
	 */
	public static int setInteger(final JavaScriptObject object, final String propertyName, final int intValue) {
		ObjectHelper.checkNotNull("parameter:object", object);
		StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
		return setInteger0(object, propertyName, intValue);
	}

	private static native int setInteger0(final JavaScriptObject object, final String propertyName, final int intValue)/*-{
	 var previousValue = object[ propertyName ];     
	 object[ propertyName ] = intValue;
	 return previousValue || 0;
	 }-*/;

	public static int setInteger(final JavaScriptObject object, final int index, final int intValue) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return setInteger0(object, index, intValue);
	}

	private static native int setInteger0(final JavaScriptObject object, final int index, final int intValue)/*-{
	 var previousValue = object[ index ];
	 object[ index ] = intValue;
	 return previousValue || 0;
	 }-*/;

	/**
	 * Reads an object given a property and returns it as a JavaScriptObject
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

	native private static JavaScriptObject getObject0(final JavaScriptObject object, final String propertyName)/*-{
	 var value = object[ propertyName ];
	 return value || null;
	 }-*/;

	public static JavaScriptObject getObject(final JavaScriptObject object, final int index) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return getObject0(object, index);
	}

	native private static JavaScriptObject getObject0(final JavaScriptObject object, final int index)/*-{
	 var value = object[ index ];
	 return value || null;
	 }-*/;

	/**
	 * Writes an object to an object's property.
	 * 
	 * @param object
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static JavaScriptObject setObject(final JavaScriptObject object, final String propertyName, final JavaScriptObject value) {
		ObjectHelper.checkNotNull("parameter:object", object);
		StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
		return setObject0(object, propertyName, value);
	}

	native private static JavaScriptObject setObject0(final JavaScriptObject object, final String propertyName, final JavaScriptObject value)/*-{
	 var previousValue = object[ propertyName ];
	 object[ propertyName ] = value;
	 return previousValue || null;
	 }-*/;

	public static JavaScriptObject setObject(final JavaScriptObject object, final int index, final JavaScriptObject value) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return setObject0(object, index, value);
	}

	native private static JavaScriptObject setObject0(final JavaScriptObject object, final int index, final JavaScriptObject value)/*-{
	 var previousValue = object[ index ];
	 object[ index ] = value;
	 return previousValue || null;
	 }-*/;

	/**
	 * Reads an object given a property and returns it as a Element
	 * 
	 * @param object
	 * @param propertyName
	 * @return
	 */
	public static Element getElement(final JavaScriptObject object, final String propertyName) {
		ObjectHelper.checkNotNull("parameter:object", object);
		StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
		return getElement0(object, propertyName);
	}

	native private static Element getElement0(final JavaScriptObject object, final String propertyName)/*-{
	 var value = object[ propertyName ];
	 return value || null;
	 }-*/;

	public static Element getElement(final JavaScriptObject object, final int index) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return getElement0(object, index);
	}

	native private static Element getElement0(final JavaScriptObject object, final int index)/*-{
	 var value = object[ index ];
	 return value || null;
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

	native private static JavaScriptObject removeProperty0(final JavaScriptObject object, final String propertyName)/*-{
	 var previousValue = object[ propertyName ];
	 delete object[ propertyName ];
	 return previousValue || null;
	 }-*/;

	public static JavaScriptObject removeProperty(final JavaScriptObject object, final int index) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return removeProperty0(object, index);
	}

	native private static JavaScriptObject removeProperty0(final JavaScriptObject object, final int index)/*-{
	 var previousValue = object[ index ];
	 delete object[ index ];
	 return previousValue || null;
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

	native private static String getType0(final JavaScriptObject object, final String propertyName)/*-{
	 return typeof( object[ propertyName ] );
	 }-*/;

	/**
	 * Retrieves the actual javascript type for the property value at the given
	 * slot.
	 * 
	 * @param object
	 * @param index
	 * @return
	 */
	public static String getType(final JavaScriptObject object, final int index) {
		ObjectHelper.checkNotNull("parameter:object", object);
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:index", index, 0);
		return ObjectHelper.getType0(object, index);
	}

	native private static String getType0(final JavaScriptObject object, final int index)/*-{
	 return typeof( object[ index ] );
	 }-*/;

	/**
	 * Retrieves the property count for the given javascript object.
	 * 
	 * @param object
	 * @return
	 */
	public static int getPropertyCount(final JavaScriptObject object) {
		ObjectHelper.checkNotNull("parameter:object", object);

		return getPropertyCount0(object);
	}

	/**
	 * Retrieves the property count for the given native object. If the length
	 * property is present that value is returned otherwise a for each loop is
	 * used to count all the properties
	 * 
	 * @param nativeObject
	 * @return
	 */
	native private static int getPropertyCount0(final JavaScriptObject nativeObject)/*-{
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
	 * Convenience method which takes a JavaScriptObject and casts it to an
	 * Element.
	 * 
	 * This is provided purely as a mechanism to make a JSO reference into an
	 * Element.
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

	/**
	 * Calls the destroy method on the given object if it is destroyable.
	 * 
	 * @param object
	 */
	public static void destroyIfNecessary(final Object object) {
		if (object instanceof Destroyable) {
			final Destroyable destroyable = (Destroyable) object;
			destroyable.destroy();
		}
	}

	/**
	 * Searches the given array for an element returning the index of the first
	 * match
	 * 
	 * @param object
	 * @param element
	 * @return
	 */
	public static int indexOf(final JavaScriptObject array, final JavaScriptObject element) {
		return ObjectHelper.indexOf0(array, element);
	}

	native private static int indexOf0(final JavaScriptObject array, final JavaScriptObject element)/*-{
	 var index = -1;
	 for( var i = 0; i < array.length; i++ ){
	 if( array[ i ] == element ){
	 index = i;
	 break;
	 }
	 }
	 return index;    
	 }-*/;

	/**
	 * Searches the given array for an element starting with the last element
	 * until a match is found and then returns that index.
	 * 
	 * @param array
	 * @param element
	 * @return
	 */
	public static int lastIndexOf(final JavaScriptObject array, final JavaScriptObject element) {
		return ObjectHelper.lastIndexOf0(array, element);
	}

	native private static int lastIndexOf0(final JavaScriptObject array, final JavaScriptObject element)/*-{
	 var index = -1;
	 for( var i = array.length -1; i >= 0; i-- ){
	 if( array[ i ] == element ){
	 index = i;
	 break;
	 }
	 }
	 return index;    
	 }-*/;

	protected ObjectHelper() {
	}
}