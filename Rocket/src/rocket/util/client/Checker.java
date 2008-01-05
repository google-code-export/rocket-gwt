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
 * The Checker class is a compilation of methods that check or assert that a value satisfies a particular constraint.
 * 
 * It is useful for checking incoming parameters, verifying state etc.
 * 
 * Most of the check methods that involve double( which also handles float) also accept an epsilon because testing doubles 
 * for equality is not never a good thing.
 * @author Miroslav Pokorny (mP)
 */
public class Checker {

	public static void fail(final String name, final String message) {
		if (name != null) {
			if (name.startsWith(Constants.PARAMETER)) {
				throw new IllegalArgumentException(message);
			}
			if (name.startsWith(Constants.FIELD)) {
				throw new IllegalStateException(message);
			}
		}
		throw new AssertionError(message);
	}

	public static void fail(final String message) {
		throw new AssertionError(message);
	}

	
	public static void notNull(final String name, final Object object) {
		if (object == null) {
			handleNull(name, "The " + name + " must not be null.");
		}
	}

	public static void handleNull(String name, String message) {
		fail(name, message);
	}

	public static void same(final String message, final Object object, final Object otherObject) {
		if (false == Tester.nullSafeIdentity(object, otherObject)) {
			fail(message + ", object: " + object + ", otherObject: " + otherObject);
		}
	}

	public static void notSame(final String message, final Object object, final Object otherObject) {
		if (Tester.nullSafeIdentity(object, otherObject)) {
			fail(message);
		}
	}

	public static void different(final String message, final Object object, final Object otherObject) {
		if ( Tester.nullSafeIdentity(object, otherObject)) {
			fail(message);
		}
	}

	public static void nullReference(final String name, final Object object) {
		if (object != null) {
			handleNonNull(name, "The " + name + " must be null. name: " + object);
		}
	}

	public static void handleNonNull(String name, String message) {
		fail(name, message);
	}

	public static void equals(final String message, final Object object, final Object otherObject) {
		if (false == Tester.nullSafeEquals(object, otherObject)) {
			Checker.fail(message);
		}
	}
	
	public static void notEmpty(final String message, final String string) {
		if ( Tester.isNullOrEmpty(string)) {
			Checker.fail(message + " is null or empty.");
		}
	}

	public static void equals(final String message, final String actual, final String expected) {
		if (false == Tester.nullSafeEquals(actual, expected)) {
			fail(message + ", got\"" + actual + "\", expected\"" + expected + "\".");
		}
	}

	public static void booleanValue(final String name, final boolean value, final boolean expectedValue) {
		if (value != expectedValue) {
			Checker.fail("The " + name + " value of " + value + " should be equal to " + expectedValue);
		}
	}

	public static void trueValue(final String message, final boolean booleanValue) {
		if (!booleanValue) {
			fail(message);
		}
	}

	public static void falseValue(final String message, final boolean booleanValue) {
		if (booleanValue) {
			fail(message);
		}
	}

	public static void equals(final String name, final long expectedValue, final long value) {
		if (value != expectedValue) {
			Checker.fail("The " + name + " value of " + value + " should be equal to " + expectedValue);
		}
	}

	public static void notEquals(final String name, final long expectedValue, final long value) {
		if (value == expectedValue) {
			Checker.fail("The " + name + " value of " + value + " should not be equal to " + expectedValue);
		}
	}

	public static void between(final String name, final long longValue, final long lowerBounds, final long upperBounds) {
		if (longValue < lowerBounds || longValue >= upperBounds) {
			Checker.fail(name, "The " + name + " value of " + longValue + " must be between " + lowerBounds + " and " + upperBounds);
		}
	}

	public static void greaterThan(final String name, final long greaterThan, final long longValue) {
		if (false == (longValue > greaterThan)) {
			Checker.fail(name, "The " + name + " value of " + longValue + " must be greater than " + greaterThan);
		}
	}

	public static void greaterThanOrEqual(final String name, final long greaterThanOrEqual, final long longValue) {
		if (false == (longValue >= greaterThanOrEqual)) {
			Checker.fail(name, "The " + name + " value of " + longValue + " must be greater than or equal to " + greaterThanOrEqual);
		}
	}

	public static void lessThan(final String name, final long lessThan, final long longValue) {
		if (false == (longValue < lessThan)) {
			Checker.fail(name, "The " + name + " value of " + longValue + " must be less than " + lessThan);
		}
	}

	public static void lessThanOrEqual(final String name, final long lessThanOrEqual, final long longValue) {
		if (false == (longValue <= lessThanOrEqual)) {
			Checker.fail(name, "The " + name + " value of " + longValue + " must be less than or equal to " + lessThanOrEqual);
		}
	}

	public static void notZero(final String name, final long longValue) {
		if (longValue == 0) {
			Checker.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void isZero(final String name, final long longValue) {
		if (longValue != 0) {
			Checker.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void isNegative(final String name, final long longValue) {
		if (longValue < 0) {
			Checker.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void isPositive(final String name, final long longValue) {
		if (longValue < 0) {
			Checker.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void notZero(final String name, final double doubleValue) {
		if (doubleValue == 0) {
			Checker.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void isZero(final String name, final double doubleValue) {
		if (doubleValue != 0) {
			Checker.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void isNegative(final String name, final double doubleValue) {
		if (doubleValue < 0) {
			Checker.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void isPositive(final String name, final double doubleValue) {
		if (doubleValue < 0) {
			Checker.fail(name, "The " + name + " must not be zero");
		}
	}

	public static void between(final String name, final double doubleValue, final double lowerBounds, final double upperBounds) {
		if (doubleValue < lowerBounds || doubleValue >= upperBounds) {
			Checker.fail(name, "The " + name + " value of " + doubleValue + " must be between " + lowerBounds + " and " + upperBounds);
		}
	}

	public static void greaterThan(final String name, final double greaterThan, final double doubleValue) {
		if (false == (doubleValue > greaterThan)) {
			Checker.fail(name, "The " + name + " value of " + doubleValue + " must be greater than " + greaterThan);
		}
	}

	public static void greaterThanOrEqual(final String name, final double greaterThanOrEqual, final double doubleValue) {
		if (false == (doubleValue >= greaterThanOrEqual)) {
			Checker.fail(name, "The " + name + " value of " + doubleValue + " must be greater than or equal to " + greaterThanOrEqual);
		}
	}

	public static void lessThan(final String name, final double lessThan, final double doubleValue) {
		if (false == (doubleValue < lessThan)) {
			Checker.fail(name, "The " + name + " value of " + doubleValue + " must be less than " + lessThan);
		}
	}

	public static void lessThanOrEqual(final String name, final double lessThanOrEqual, final double doubleValue) {
		if (false == (doubleValue <= lessThanOrEqual)) {
			Checker.fail(name, "The " + name + " value of " + doubleValue + " must be less than or equal to " + lessThanOrEqual);
		}
	}

	public static void path(final String name, final String path) {
		Checker.notNull("parameter:path", path);

		if (path.length() > 0 && path.charAt(0) != Constants.PATH_SEPARATOR) {
			Checker.fail(name, "The " + name + " if not empty must start with a '/', path: \"" + path + "\".");
		}
		if (path.indexOf(Constants.QUERY_STRING) != -1 || path.indexOf(Constants.ANCHOR) != -1) {
			Checker.fail(name, "The " + name + " if not empty must not include a '?' or '#', path: \"" + path + "\".");
		}
	}

	public static void httpPortNumber(final String name, final int port) {
		if (port < 0 || port > 65536) {
			Checker.fail(name, "The " + name + " must be between 0 and port, port: " + port);
		}
	}

	public static void httpProtocol(final String name, final String protocol) {
		Checker.notNull(name, protocol);
		if (false == Tester.isHttp(protocol) && false == Tester.isHttps(protocol)) {
			Checker.fail(name, "The " + name + " is not a protocol (" + Constants.HTTP + ',' + Constants.HTTPS
					+ "), protocol\"" + protocol + "\".");
		}
	}

	public static void httpMethod(final String name, final String method) {
		if (false == Tester.isGet(method) && false == Tester.isPost(method)) {
			Checker.fail(name, "The " + name + " is not a method (" + Constants.GET + ',' + Constants.POST + "), method\""
					+ method + "\".");
		}
	}
}
