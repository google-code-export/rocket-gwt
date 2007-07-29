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
package rocket.text.test;

import junit.framework.TestCase;
import rocket.text.client.IndexedPlaceHolderReplacer;


public class IndexedPlaceHolderReplacerTestCase extends TestCase {
	
    public void testNoPlaceholders() {        
        final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[0] );
        
        final String text = "The quick brown fox jumped over the lazy dog";
        final String expected = text;
        final String actual = replacer.execute( text );
        assertEquals(expected, actual);
    }

    public void testWithSinglePlaceHolder() {        
        final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[] { "BROWN" } );
        
        final String expected = "The quick BROWN fox jumped over the lazy dog.";
        final String text = "The quick ${0} fox jumped over the lazy dog.";        
        final String actual = replacer.execute( text );
        assertEquals(expected, actual);
    }

    public void testWithSingleValueUsedTwice() {        
        final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[] { "BROWN" } );
        
        final String expected = "The quick BROWN fox jumped over the BROWN lazy dog.";
        final String text = "The quick ${0} fox jumped over the ${0} lazy dog.";        
        final String actual = replacer.execute( text );
        assertEquals(expected, actual);
    }

    public void testWithTwoValuesSubstitutedTwice() {
        final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[] { "BROWN", "GREEN" } );
        
        final String expected = "The GREEN quick BROWN fox jumped over the GREENBROWN lazy dog.";
        final String text = "The ${1} quick ${0} fox jumped over the ${1}${0} lazy dog.";
        final String actual = replacer.execute( text );
        assertEquals(expected, actual);
    }

    public void testWithPlaceHolderAtTheEndOfFormatString() {        
        final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[] { "DOG!" } );
        
        final String text = "The quick brown fox jumped over the lazy ${0}";
        final String actual = replacer.execute( text );
        
        final String expected = "The quick brown fox jumped over the lazy DOG!";
        assertEquals(expected, actual);
    }

    public void testWithPlaceHolderAtTheStartOfFormatString() {                
        final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[] { "THE" } );
        
        final String text = "${0} quick brown fox jumped over the lazy dog!";
        final String actual = replacer.execute( text );
        
        final String expected = "THE quick brown fox jumped over the lazy dog!";
        assertEquals(expected, actual);
    }

    public void testWithBrokenFormatString() {
        try {
        	final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
            replacer.setValues( new Object[] { "BROWN", "GREEN" } );
        
            final String text = "The ${1} quick ${0}${";
        	final String returned = replacer.execute( text );
            fail("An exception should have been thrown when formatting text[" + text + "] but returned [" + returned + "]");
        } catch (final Throwable expected) {

        }
    }

    public void testWithInvalidAlphaPlaceHolderIndex() {
        final String text = "The quick brown fox jumped over the lazy ${NOTANINDEX}";
        
    	final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[] { "BROWN", "GREEN" } );  
        try {          
        	final String returned = replacer.execute( text );
            fail("An exception should have been thrown when formatting text[" + text + "] but returned [" + returned + "]");
        } catch (final Throwable expected) {

        }
    }

    public void testWithInvalidNumberPlaceHolderIndex() {
        final String text = "The quick brown fox jumped over the lazy ${3}";
        
        final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[] { "BROWN", "GREEN" } );        
        try {
            final String returned = replacer.execute( text );
            fail("An exception should have been thrown when formatting text[" + text + "] but returned [" + returned + "]");
        } catch (final Throwable expected) {

        }
    }

    public void testWithEscapedLeftParenthesis(){
        final String text = "before \\$ ${0} after";
        
    	final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[]{ "VALUE" } );  
        final String actual = replacer.execute( text );

        assertEquals( "before $ VALUE after", actual );
    }

    public void testWithEscapedBackslash(){
        final String text = "before \\\\ ${0} after";
        
    	final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[]{ "VALUE" } );  
        final String actual = replacer.execute( text );

        assertEquals( "before \\ VALUE after", actual );
    }

    public void testMessageHasTrailingBackslash(){
        final String text = "before ${0} after \\";        
        
    	final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( new Object[]{ "VALUE" } );  
        try{
            final String actual = replacer.execute( text );
            fail( "An exception and not [" + actual + "] should have failed.");
        } catch ( final Exception expected ){            
        }
    }

    public void testMessageHasInvalidEscapedCharacter(){
        final String text = "before \\X after";
        final Object[] values = new Object[]{ "VALUE" };
        
    	final IndexedPlaceHolderReplacer replacer = new IndexedPlaceHolderReplacer();
        replacer.setValues( values );  
        try{
            final String actual = replacer.execute( text );
            fail( "An exception and not [" + actual + "] should have failed.");
        } catch ( final AssertionError expected ){            
        }
    }


}
