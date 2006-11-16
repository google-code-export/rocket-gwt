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
 * A variety of check / assertion methods for primitive types.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class PrimitiveHelper extends SystemHelper {

    public static void checkBoolean(final String name, final boolean value, final boolean expectedValue) {
        if (value != expectedValue) {
            SystemHelper.handleAssertFailure("The " + name + " value of " + value + " should be equal to "
                    + expectedValue);
        }
    }

    public static void checkEquals(final String name, final long value, final long expectedValue) {
        if (value != expectedValue) {
            SystemHelper.handleAssertFailure("The " + name + " value of " + value + " should be equal to "
                    + expectedValue);
        }
    }

    public static void checkNotEquals(final String name, final long value, final long expectedValue) {
        if (value == expectedValue) {
            SystemHelper.handleAssertFailure("The " + name + " value of " + value + " should not be equal to "
                    + expectedValue);
        }
    }

    public static void checkBetween(final String name, final long longValue, final long lowerBounds,
            final long upperBounds) {
        if (longValue < lowerBounds || longValue >= upperBounds) {
            SystemHelper.handleAssertFailure(name, "The " + name + " value of " + longValue + " must be between "
                    + lowerBounds + " and " + upperBounds);
        }
    }

    public static void checkGreaterThan(final String name, final long longValue, final long greaterThan) {
        if (false == (longValue > greaterThan)) {
            SystemHelper.handleAssertFailure(name, "The " + name + " value of " + longValue + " must be greater than "
                    + greaterThan);
        }
    }

    public static void checkGreaterThanOrEqual(final String name, final long longValue, final long greaterThanOrEqual) {
        if (false == (longValue >= greaterThanOrEqual)) {
            SystemHelper.handleAssertFailure(name, "The " + name + " value of " + longValue
                    + " must be greater than or equal to " + greaterThanOrEqual);
        }
    }

    public static void checkLessThan(final String name, final long longValue, final long lessThan) {
        if (false == (longValue < lessThan)) {
            SystemHelper.handleAssertFailure(name, "The " + name + " value of " + longValue + " must be less than "
                    + lessThan);
        }
    }

    public static void checkLessThanOrEqual(final String name, final long longValue, final long lessThanOrEqual) {
        if (false == (longValue <= lessThanOrEqual)) {
            SystemHelper.handleAssertFailure(name, "The " + name + " value of " + longValue
                    + " must be less than or equal to " + lessThanOrEqual);
        }
    }

    public static void checkNotZero(final String name, final long longValue) {
        if (longValue == 0) {
            SystemHelper.handleAssertFailure(name, "The " + name + " must not be zero");
        }
    }

    public static void checkIsZero(final String name, final long longValue) {
        if (longValue != 0) {
            SystemHelper.handleAssertFailure(name, "The " + name + " must not be zero");
        }
    }

    public static void checkIsNegative(final String name, final long longValue) {
        if (longValue < 0) {
            SystemHelper.handleAssertFailure(name, "The " + name + " must not be zero");
        }
    }

    public static void checkIsPositive(final String name, final long longValue) {
        if (longValue < 0) {
            SystemHelper.handleAssertFailure(name, "The " + name + " must not be zero");
        }
    }

    public static void checkNotZero(final String name, final double doubleValue) {
        if (doubleValue == 0) {
            SystemHelper.handleAssertFailure(name, "The " + name + " must not be zero");
        }
    }

    public static void checkIsZero(final String name, final double doubleValue) {
        if (doubleValue != 0) {
            SystemHelper.handleAssertFailure(name, "The " + name + " must not be zero");
        }
    }

    public static void checkIsNegative(final String name, final double doubleValue) {
        if (doubleValue < 0) {
            SystemHelper.handleAssertFailure(name, "The " + name + " must not be zero");
        }
    }

    public static void checkIsPositive(final String name, final double doubleValue) {
        if (doubleValue < 0) {
            SystemHelper.handleAssertFailure(name, "The " + name + " must not be zero");
        }
    }

    public static void checkBetween(final String name, final double doubleValue, final double lowerBounds,
            final double upperBounds) {
        if (doubleValue < lowerBounds || doubleValue >= upperBounds) {
            SystemHelper.handleAssertFailure(name, "The " + name + " value of " + doubleValue + " must be between "
                    + lowerBounds + " and " + upperBounds);
        }
    }

    public static void checkGreaterThan(final String name, final double doubleValue, final double greaterThan) {
        if (false == (doubleValue > greaterThan)) {
            SystemHelper.handleAssertFailure(name, "The " + name + " value of " + doubleValue
                    + " must be greater than " + greaterThan);
        }
    }

    public static void checkGreaterThanOrEqual(final String name, final double doubleValue,
            final double greaterThanOrEqual) {
        if (false == (doubleValue >= greaterThanOrEqual)) {
            SystemHelper.handleAssertFailure(name, "The " + name + " value of " + doubleValue
                    + " must be greater than or equal to " + greaterThanOrEqual);
        }
    }

    public static void checkLessThan(final String name, final double doubleValue, final double lessThan) {
        if (false == (doubleValue < lessThan)) {
            SystemHelper.handleAssertFailure(name, "The " + name + " value of " + doubleValue + " must be less than "
                    + lessThan);
        }
    }

    public static void checkLessThanOrEqual(final String name, final double doubleValue, final double lessThanOrEqual) {
        if (false == (doubleValue <= lessThanOrEqual)) {
            SystemHelper.handleAssertFailure(name, "The " + name + " value of " + doubleValue
                    + " must be less than or equal to " + lessThanOrEqual);
        }
    }

    final static int KILOBYTES = 1024;

    final static int MEGABYTES = 1024 * 1024;

    public static String formatKiloOrMegabytes(final int number) {
        String formatted = null;
        while (true) {
            if (number < KILOBYTES) {
                formatted = String.valueOf(number);
                break;
            }

            if (number < MEGABYTES) {
                formatted = String.valueOf((int) (number / KILOBYTES)) + " K";
                break;
            }

            formatted = String.valueOf((int) (number / MEGABYTES)) + " M";
            break;
        }
        return formatted;
    }

    /**
     * Fix for the GWT implementation of Character.digit( char, int ).
     * 
     * @param c
     * @param radix
     * @return
     * 
     * @deprecated use until fixed and released by GWT. {@see http://code.google.com/p/google-web-toolkit/issues/detail?id=302}
     */
    public static int characterDigit(final char c, final int radix) {
        int value = -1;
        while (true) {
            // if not in script mode use java's Character.digit()
            if (!GWT.isScript()) {
                value = Character.digit(c, radix);
                break;
            }

            if (radix < 2 || radix > 36) {
                break;
            }
            if (c >= '0' && c <= '9') {
                value = c - '0';
                break;
            }
            if (c >= 'a' && c < ('a' + radix)) {
                value = 10 + c - 'a';
                break;
            }
            if (c >= 'A' && c < ('A' + radix)) {
                value = 10 + c - 'A';
                break;
            }
            break;
        }
        return value;
    }

    public PrimitiveHelper() {
    }
}