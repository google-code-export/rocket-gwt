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

    public static void handleNullEncountered(String name, String message) {
        handleAssertFailure(name, message);
    }

    /**
     * Asserts that the two objects are in fact the same.
     * 
     * @param firstName
     * @param firstObject
     * @param secondName
     * @param secondObject
     */
    public static void checkSame(final String firstName, final Object firstObject, final String secondName,
            final Object secondObject) {
        if (false == nullSafeIdentity(firstObject, secondObject)) {
            SystemHelper.handleUnsupportedOperation("The " + firstName + " is not the same object object as "
                    + secondName + " firstObject: " + firstObject + ", secondObject: " + secondObject);
        }
    }

    public static void checkNotSame(final String firstName, final Object firstObject, final String secondName,
            final Object secondObject) {
        if (nullSafeIdentity(firstObject, secondObject)) {
            SystemHelper.handleUnsupportedOperation("The " + firstName + " is the same object object as " + secondName
                    + " firstObject: " + firstObject + ", secondObject: " + secondObject);
        }
    }

    /**
     * Asserts that the two objects are in fact different objects.
     * 
     * @param firstName
     * @param firstObject
     * @param secondName
     * @param secondObject
     */
    public static void checkDifferent(final String firstName, final Object firstObject, final String secondName,
            final Object secondObject) {
        if (nullSafeIdentity(firstObject, secondObject)) {
            SystemHelper.handleUnsupportedOperation("The " + firstName + " is the same object object as " + secondName
                    + " firstObject: " + firstObject + ", secondObject: " + secondObject);
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
     * @param firstName
     * @param firstObject
     * @param secondName
     * @param secondObject
     */
    public static void checkEquals(final String firstName, final Object firstObject, final String secondName,
            final Object secondObject) {
        if (false == nullSafeEquals(firstObject, secondObject)) {
            SystemHelper.handleUnsupportedOperation("The " + firstName + " is not equal to " + secondName
                    + " firstObject: " + firstObject + ", secondObject: " + secondObject);
        }
    }

    protected ObjectHelper() {
    }
}