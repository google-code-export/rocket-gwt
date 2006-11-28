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
package rocket.util.client;

import java.util.ArrayList;
import java.util.List;

/**
 * A variety of useful String manipulating methods including assertion checks and general utility methods.
 * 
 * @author Miroslav Pokorny (mP)
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
     * Scans the parameter:string for the parameter:test ignoring case when comparing characters.
     * 
     * @param string
     * @param search
     *            If test is empty -1 is always returned.
     * @return -1 if the string was not found or the index of the first matching character
     */
    public static int indexOfIgnoringCase(final String string, final String search) {
        ObjectHelper.checkNotNull("parameter:string", string);
        ObjectHelper.checkNotNull("parameter:search", search);

        int index = -1;
        final int stringLength = string.length();
        final int testLength = search.length();
        if (stringLength > 1 || testLength > 1) {
            final char firstCharOfTest = Character.toLowerCase(search.charAt(0));
            final int lastStringCharacterToCheck = stringLength - testLength + 1;

            for (int i = 0; i < lastStringCharacterToCheck; i++) {
                if (firstCharOfTest == Character.toLowerCase(string.charAt(i))) {
                    index = i;
                    for (int j = 1; j < testLength; j++) {
                        final char c = string.charAt(i + j);
                        final char otherChar = search.charAt(j);
                        if (Character.toLowerCase(c) != Character.toLowerCase(otherChar)) {
                            index = -1;
                            break;
                        }
                    }
                    if (-1 != index) {
                        break;
                    }
                }
            }
        }
        return index;
    } // indexOfIgnoringCase

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
        final int stringLength = input.length();
        if (stringLength > 0) {
            final char[] chars = input.toCharArray();

            int firstChar = 0;

            for (int i = 0; i < stringLength; i++) {
                final char c = chars[i];
                final int isTokenTest = delimiter.indexOf(c);

                /* token found! */
                if (isTokenTest != -1) {
                    tokens.add(input.substring(firstChar, i));

                    /* include delimiter in the output ??? */
                    if (false == ignoreDelimiters) {
                        tokens.add(String.valueOf(c));
                    }

                    /* mark the beginning of the next token... */
                    firstChar = i + 1;
                    continue;
                }
            } // for each char

            /* the last token will not be terminated.. add */
            if (firstChar != stringLength) {
                tokens.add(input.substring(firstChar));
            }
        }// if

        /* copy the splitted strings into a String array */
        final String[] array = new String[tokens.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = (String) tokens.get(i);
        }

        return array;
    } // split

    /**
     * Joins a string array into one long string with each value separated by the separator parameter. If an element of array is null it is
     * skipped (null) is not added to the built up string.
     * 
     * @param array
     * @param separator
     * @return
     */
    public static String join(final String[] array, final String separator) {
        StringHelper.checkNotNull("parameter:array", array);
        StringHelper.checkNotNull("parameter:separator", separator);

        final StringBuffer buf = new StringBuffer();
        boolean addSeparator = false;

        for (int i = 0; i < array.length; i++) {
            final String element = array[i];
            if (null == element) {
                continue;
            }
            if (addSeparator) {
                buf.append(separator);
            }

            buf.append(element);
            addSeparator = true;
        }
        return buf.toString();
    }

    /**
     * Helper which may be used to assert that a string is not or empty.
     * 
     * @param message
     * @param string
     */
    public static void checkNotEmpty(final String message, final String string) {
        if (isNullOrEmpty(string)) {
            SystemHelper.handleAssertFailure(message + " is null or empty.");
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

    /**
     * Builds a new string substituting the placeholders within messages with values from values.
     * 
     * @param message
     * @param values
     * @return
     */
    public static String format(final String message, final Object[] values) {
        StringHelper.checkNotNull("parameter:message", message);
        ObjectHelper.checkNotNull("parameter:values", values);

        final StringBuffer buf = new StringBuffer();
        int i = 0;
        final int messageLength = message.length();
        while (i < messageLength) {
            // find the start placeholder
            final int placeHolderStartIndex = message.indexOf('{', i);
            if (-1 == placeHolderStartIndex) {
                buf.append(message.substring(i, messageLength));
                break;
            }
            buf.append(message.substring(i, placeHolderStartIndex));

            // find the end placeholder
            final int placeHolderEndIndex = message.indexOf('}', placeHolderStartIndex + 1);
            if (-1 == placeHolderEndIndex) {
                StringHelper.handleAssertFailure("Unable to find placeholder end after finding start, ["
                        + message.substring(i, messageLength - i) + "]");
            }

            // extract the index in between...
            String placeHolderIndex = message.substring(1 + placeHolderStartIndex, placeHolderEndIndex);
            try {
                final int index = Integer.parseInt(placeHolderIndex);
                final String value = String.valueOf(values[index]);
                buf.append(value);
                i = placeHolderEndIndex + 1;

            } catch (final NumberFormatException badIndex) {
                StringHelper.handleAssertFailure("Placeholder index does not contain a number [" + placeHolderIndex
                        + "]");
            }
        }
        return buf.toString();
    }

    /**
     * Takes the given input(it assumes its a single word) and camel cases it... that is the first letter is capitalized with the remainder
     * converted to lower case.
     * 
     * @param in
     * @return
     */
    public static String camelCase(final String in) {
        String out = "";
        final int length = in.length();
        if (length > 0) {
            out = in.substring(0, 1).toUpperCase();
            if (length > 1) {
                out = out + in.substring(1).toLowerCase();
            }
        }

        return out;
    }

    /**
     * Asserts that the two strings are in fact the equal or both are null.
     * 
     * @param actualStringName
     * @param actual
     * @param expectedStringName
     * @param expected
     */
    public static void checkEquals(final String message, final String actual, final String expected) {
        if (false == nullSafeEquals(actual, expected)) {
            SystemHelper.handleAssertFailure(message + ", got[" + actual + "], expected[" + expected + "]");
        }
    }
    /**
     * Accepts a plain string escaping various characters so that the given string is html encoded.
     * @param plainText
     * @return
     */
    public static String htmlEncode( final String plainText ){
        StringHelper.checkNotNull( "parameter:plainText", plainText );
        
        final StringBuffer buf = new StringBuffer();        
        final int length = plainText.length();
        for( int i = 0; i < length; i++ ){
            final char c = plainText.charAt( i );

            if( '<' == c ){
                buf.append( "&lt;");
                continue;
            }
            if( '>' == c ){
                buf.append( "&gt;");
                continue;
            }
            if( '&' == c ){
                buf.append( "&amp;");
                continue;
            }
            if( '\'' == c ){
                buf.append( "&apos;");
                continue;
            }
            if( '"' == c ){
                buf.append( "&quot;");
                continue;
            }
            buf.append( c );
        }

        return buf.toString();
    }

    /**
     * Accepts a encoded string and returns the original decoded value.
     * @param htmlEncodedText
     * @return
     */
    public static String htmlDecode( final String htmlEncodedText ){
        StringHelper.checkNotNull( "parameter:htmlEncodedText", htmlEncodedText );
        
        final StringBuffer buf = new StringBuffer();
        final int length = htmlEncodedText.length();
        for( int i = 0; i < length; ){
            final char c = htmlEncodedText.charAt( i );
            i++;

            if( '&' == c ){
                final int semiColon = htmlEncodedText.indexOf( ';', i );
                final String entity = htmlEncodedText.substring( i, semiColon );
                i = semiColon + 1;

                if( entity.equals("lt")){
                    buf.append( "<");
                    continue;
                }

                if( entity.equals("gt")){
                    buf.append( ">");
                    continue;
                }

                if( entity.equals("amp")){
                    buf.append( "&");
                    continue;
                }
                if( entity.equals("apos")){
                    buf.append( '\'');
                    continue;
                }
                if( entity.equals("quot")){
                    buf.append( '"');
                    continue;
                }
                throw new RuntimeException("Unknown/unsupported html entity &" + entity + ";");
            }
            buf.append( c );
        }

        return buf.toString();
    }

    /**
     * Private so that creating instances are not possible
     */
    protected StringHelper() {
        super();
    }
}