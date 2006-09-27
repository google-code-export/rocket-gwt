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

/**
* A variety of check / assertion methods for primitive types.
* @author Miroslav Pokorny (mP)
*/
public class PrimitiveHelper {

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

    public static void checkGreaterThanOrEqual(final String name, final double doubleValue, final double greaterThanOrEqual) {
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

    public PrimitiveHelper() {
    }
}