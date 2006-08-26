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

import java.util.ArrayList;
import java.util.List;

/**
 * A variety of useful String manipulating methods including assertion checks and general utility methods.
 *
 * @author Miroslav Pokorny
 * @version 1.0
 */
public class StringHelper extends ObjectHelper {
    /**
     * Invokes {@link #padLeft{ String, int, char }with a default space pad character.
     *
     * @param text
     * @param length
     * @return
     */
    public static String padLeft(final String text, final int length) {
        return padLeft(text, length, ' ');
    }

    /**
     * Pads the rightmost characters of text with $pad so that its length matches that of the parameter:length.
     *
     * @param text
     * @param length
     * @param pad
     * @return
     */
    public static String padLeft(final String text, final int length, final char pad) {
        StringHelper.checkNotNull("parameter:text", text);

        final int textLength = text.length();
        final int requiredPadding = length - textLength;
        if (requiredPadding < 0) {
            SystemHelper.handleAssertFailure("parameter:text",
                    "THe parameter:text is longer than the given lineLength which is used to determine the required padding, text["
                            + text + "], text.length: " + textLength + ", length: " + length);
        }

        final StringBuffer buf = new StringBuffer(textLength + requiredPadding);

        for (int i = 0; i < requiredPadding; i++) {
            buf.append(pad);
        }
        buf.append(text);

        return buf.toString();
    }

    /**
     * Invokes {@link #padRight{ String, int, char }with a default space pad character.
     *
     * @param text
     * @param length
     * @return
     */
    public static String padRight(final String text, final int length) {
        return padRight(text, length, ' ');
    }

    /**
     * Pads the rightmost characters of text with $pad so that its length matches that of the parameter:length.
     *
     * @param text
     * @param length
     * @param pad
     * @return
     */
    public static String padRight(final String text, final int length, final char pad) {
        StringHelper.checkNotNull("parameter:text", text);

        final int textLength = text.length();
        final int requiredPadding = length - textLength;
        if (requiredPadding < 0) {
            SystemHelper.handleAssertFailure("parameter:text",
                    "THe parameter:text is longer than the given lineLength which is used to determine the required padding, text["
                            + text + "], text.length: " + textLength + ", length: " + length);
        }

        final StringBuffer buf = new StringBuffer(textLength + requiredPadding);
        buf.append(text);

        for (int i = 0; i < requiredPadding; i++) {
            buf.append(pad);
        }
        return buf.toString();
    }

    /**
     * If the input parameter is null return empty string, all other values are returned verbatim.
     *
     * @param input
     * @return
     */
    public static String nullToEmpty(final String input) {
        return input == null ? "" : input;
    }

    /**
     * Tests if the first string starts with the second ignoring case. This is equalivalent to String.startsWith() but ignores case.
     *
     * @param first
     *            The first string
     * @param second
     *            The second string
     * @return True if the first string starts with the second.
     */
    public static boolean startsWithIgnoringCase(final String first, final String second) {
        ObjectHelper.checkNotNull("parameter:first", first);
        ObjectHelper.checkNotNull("parameter:second", second);

        boolean startsWith = false;

        final int secondLength = second.length();
        if (secondLength <= first.length()) {
            startsWith = true;

            for (int i = 0; i < secondLength; i++) {
                final char c = first.charAt(i);
                final char d = second.charAt(i);
                if (Character.toLowerCase(c) != Character.toLowerCase(d)) {
                    startsWith = false;
                    break;
                }
            }
        }

        return startsWith;
    } // startsWithIgnoringCase

    /**
     * Tests if the first string ends with the second ignoring case. This is equalivalent to String.endsWith() but ignores case.
     *
     * @param first
     *            The first string
     * @param second
     *            The second string
     * @return True if the first string ends with the second.
     */
    public static boolean endsWithIgnoringCase(final String first, final String second) {
        ObjectHelper.checkNotNull("parameter:first", first);
        ObjectHelper.checkNotNull("parameter:second", second);

        boolean startsWith = false;

        final int firstLength = first.length();
        final int secondLength = second.length();
        if (secondLength <= firstLength) {
            startsWith = true;

            for (int i = 0; i < secondLength; i++) {
                final char c = first.charAt(firstLength - 1 - i);
                final char d = second.charAt(secondLength - 1 - i);
                if (Character.toLowerCase(c) != Character.toLowerCase(d)) {
                    startsWith = false;
                    break;
                }
            }
        }

        return startsWith;
    } // endsWithIgnoringCase

    /**
     * Performs a similar to task to the J2SE java.lang.StringTokenizer class splitting a larger string into many smaller ones. An ideal
     * example is splitting up a comma delimited string "apple,banana,carrot" becomes three strings "apple", "banana", "carrot"
     *
     * @param input
     *            The initial string
     * @param delimiter
     *            A string containing delimiter characters.
     * @param ignoreDelimiters
     *            A flag indicating whether delimiters should be included in the returned Strings
     * @return An array of the tokens found
     */
    public static String[] split(final String input, final String delimiter, final boolean ignoreDelimiters) {
        ObjectHelper.checkNotNull("parameter:input", input);
        StringHelper.checkNotEmpty("parameter:delimiter", delimiter);

        final List tokens = new ArrayList();

        final char[] chars = input.toCharArray();
        final int stringLength = chars.length;
        int firstChar = 0;

        for (int i = 0; i < stringLength; i++) {
            final char c = chars[i];
            final int isTokenTest = delimiter.indexOf(c);

            /* token found! */
            if (isTokenTest != -1) {
            	/* special test if input starts with delimiter */
//            	if( i == 0 & ignoreDelimiters ){
//            		firstChar = i + 1;
//            		continue;
//            	}

                /* add the string only if its not empty */
                tokens.add(input.substring(firstChar, i));

                /* include delimiters in the output */
                if (false == ignoreDelimiters) {
                    tokens.add(String.valueOf(c));
                }

                /* mark the beginning of the next token... */
                firstChar = i + 1;
            }
        } // for each char

        /* the last token will not be terminated.. check and add if necessary */
        //if (firstChar != stringLength) {
            tokens.add(input.substring(firstChar));
        //}

        /* copy the splitted strings into a String array */
        final String[] array = new String[tokens.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = (String) tokens.get(i);
        }

        return array;
    } // split

    /**
     * Joins a string array into one long string with each value separated by the separator parameter.
     * If an element of array is null it is skipped (null) is not added to the built up string.
     * @param array
     * @param separator
     * @return
     */
    public static String join( final String[] array, final String separator ){
    	StringHelper.checkNotNull( "parameter:array", array );
    	StringHelper.checkNotNull( "parameter:separator", separator );

    	final StringBuffer buf = new StringBuffer();
    	boolean addSeparator = false;

    	for( int i = 0; i < array.length; i++ ){
    		final String element = array[ i ];
    		if( null == element ){
    			continue;
    		}
    		if( addSeparator ){
    			buf.append( separator );
    		}

    		buf.append( element );
    		addSeparator = true;
    	}
    	return buf.toString();
    }

    /**
     * Helper which may be used to assert that a string is not or empty.
     *
     * @param name
     *            The name of the string
     * @param string
     *            The string value being tested.
     */
    public static void checkNotEmpty(final String name, final String string) {
        if (isNullOrEmpty(string)) {
            SystemHelper.handleAssertFailure(name, "The " + name + " must not be null or empty.");
        }
    }

    /**
     * Helper that tests whether the given string is null or empty.
     *
     * @param string
     *            String
     * @return true if the string is empty or null.
     */
    public static boolean isNullOrEmpty(final String string) {
        return string == null || string.length() == 0;
    }

    public static void checkIndex(final String name, final int index, final String string) {
        checkNotNull("assert:string", string);

        final int length = string.length();
        if (index < 0 || index > length) {
            handleAssertFailure(name, "The " + name + " must be between 0 and the length (" + length
                    + " )of the string, index: " + index + ", string: " + string);
        }
    }

    /**
     * Private so that creating instances are not possible
     */
    protected StringHelper() {
        super();
    }
}