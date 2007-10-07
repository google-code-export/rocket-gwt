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

/**
 * A variety of miscellaneous methods that deal with primitive types.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class PrimitiveHelper extends SystemHelper {

	/**
	 * Tests if two double values are close enough to each other to be
	 * considered equal
	 * 
	 * @param value
	 *            The first value
	 * @param otherValue
	 *            The value being compared against
	 * @param epsilon
	 * @return True if equal or close enough otherwise false.
	 */
	static public boolean equals(final double value, final double otherValue, final double epsilon) {
		return (value - epsilon <= otherValue) && (value + epsilon >= otherValue);
	}

	static public boolean equals(final long value, final long otherValue, final long epsilon) {
		return (value - epsilon <= otherValue) && (value + epsilon >= otherValue);
	}

	public static void checkBoolean(final String name, final boolean value, final boolean expectedValue) {
		if (value != expectedValue) {
			SystemHelper.fail("The " + name + " value of " + value + " should be equal to " + expectedValue);
		}
	}

	/**
	 * Checks that the test boolean value is true otherwise an assertion failure
	 * is reported.
	 * 
	 * @param message
	 * @param booleanValue
	 */
	public static void checkTrue(final String message, final boolean booleanValue) {
		if (!booleanValue) {
			fail(message);
		}
	}

	/**
	 * Checks that the test boolean value is false otherwise an assertion
	 * failure is reported.
	 * 
	 * @param message
	 * @param booleanValue
	 */
	public static void checkFalse(final String message, final boolean booleanValue) {
		if (booleanValue) {
			fail(message);
		}
	}

	public static void checkEquals(final String name, final long expectedValue, final long value) {
		if (value != expectedValue) {
			SystemHelper.fail("The " + name + " value of " + value + " should be equal to " + expectedValue);
		}
	}

	public static void checkNotEquals(final String name, final long expectedValue, final long value) {
		if (value == expectedValue) {
			SystemHelper.fail("The " + name + " value of " + value + " should not be equal to " + expectedValue);
		}
	}

	public static void checkBetween(final String name, final long longValue, final long lowerBounds, final long upperBounds) {
		if (longValue < lowerBounds || longValue >= upperBounds) {
			SystemHelper.fail(name, "The " + name + " value of " + longValue + " must be between " + lowerBounds + " and " + upperBounds);
		}
	}

	public static void checkGreaterThan(final String name, final long greaterThan, final long longValue) {
		if (false == (longValue > greaterThan)) {
			SystemHelper.fail(name, "The " + name + " value of " + longValue + " must be greater than " + greaterThan);
		}
	}

	public static void checkGreaterThanOrEqual(final String name, final long greaterThanOrEqual, final long longValue) {
		if (false == (longValue >= greaterThanOrEqual)) {
			SystemHelper.fail(name, "The " + name + " value of " + longValue + " must be greater than or equal to " + greaterThanOrEqual);
		}
	}

	public static void checkLessThan(final String name, final long lessThan, final long longValue) {
		if (false == (longValue < lessThan)) {
			SystemHelper.fail(name, "The " + name + " value of " + longValue + " must be less than " + lessThan);
		}
	}

	public static void checkLessThanOrEqual(final String name, final long lessThanOrEqual, final long longValue) {
		if (false == (longValue <= lessThanOrEqual)) {
			SystemHelper.fail(name, "The " + name + " value of " + longValue + " must be less than or equal to " + lessThanOrEqual);
		}
	}

	public static void checkNotZero(final String name, final long longValue) {
		if (longValue == 0) {
			SystemHelper.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void checkIsZero(final String name, final long longValue) {
		if (longValue != 0) {
			SystemHelper.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void checkIsNegative(final String name, final long longValue) {
		if (longValue < 0) {
			SystemHelper.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void checkIsPositive(final String name, final long longValue) {
		if (longValue < 0) {
			SystemHelper.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void checkNotZero(final String name, final double doubleValue) {
		if (doubleValue == 0) {
			SystemHelper.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void checkIsZero(final String name, final double doubleValue) {
		if (doubleValue != 0) {
			SystemHelper.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void checkIsNegative(final String name, final double doubleValue) {
		if (doubleValue < 0) {
			SystemHelper.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void checkIsPositive(final String name, final double doubleValue) {
		if (doubleValue < 0) {
			SystemHelper.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void checkBetween(final String name, final double doubleValue, final double lowerBounds, final double upperBounds) {
		if (doubleValue < lowerBounds || doubleValue >= upperBounds) {
			SystemHelper.fail(name, "The " + name + " value of " + doubleValue + " must be between " + lowerBounds + " and " + upperBounds);
		}
	}

	public static void checkGreaterThan(final String name, final double greaterThan, final double doubleValue) {
		if (false == (doubleValue > greaterThan)) {
			SystemHelper.fail(name, "The " + name + " value of " + doubleValue + " must be greater than " + greaterThan);
		}
	}

	public static void checkGreaterThanOrEqual(final String name, final double greaterThanOrEqual, final double doubleValue) {
		if (false == (doubleValue >= greaterThanOrEqual)) {
			SystemHelper.fail(name, "The " + name + " value of " + doubleValue + " must be greater than or equal to " + greaterThanOrEqual);
		}
	}

	public static void checkLessThan(final String name, final double lessThan, final double doubleValue) {
		if (false == (doubleValue < lessThan)) {
			SystemHelper.fail(name, "The " + name + " value of " + doubleValue + " must be less than " + lessThan);
		}
	}

	public static void checkLessThanOrEqual(final String name, final double lessThanOrEqual, final double doubleValue) {
		if (false == (doubleValue <= lessThanOrEqual)) {
			SystemHelper.fail(name, "The " + name + " value of " + doubleValue + " must be less than or equal to " + lessThanOrEqual);
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