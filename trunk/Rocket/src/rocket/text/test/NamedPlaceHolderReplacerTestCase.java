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

import java.util.HashMap;
import java.util.Map;

import rocket.text.client.IndexedPlaceHolderReplacer;
import rocket.text.client.NamedPlaceHolderReplacer;
import rocket.util.client.StringHelper;
import junit.framework.TestCase;


public class NamedPlaceHolderReplacerTestCase extends TestCase {
	
	public void testNoPlaceholders() {
        final String text = "The quick brown fox jumped over the lazy dog";        
        
    	final NamedPlaceHolderReplacer replacer = new NamedPlaceHolderReplacer();
    	final Map values = new HashMap();
        replacer.setValues( values );  
        
        final String expected = text;
        final String actual = replacer.execute( text );
        assertEquals(expected, actual);
    }

    public void testWithSinglePlaceHolders() {
        final String text = "The quick ${colour} fox jumped over the lazy dog.";
        
        final NamedPlaceHolderReplacer replacer = new NamedPlaceHolderReplacer();
        final Map values = new HashMap();
        values.put( "colour", "BROWN" );        
        replacer.setValues( values );  
        
        final String expected = "The quick BROWN fox jumped over the lazy dog.";
        final String actual = replacer.execute( text );
        assertEquals(expected, actual);
    }
    
    public void testWithManyValues() {
        final String text = "The quick ${colour} fox ${action} over the lazy dog.";
        
        final NamedPlaceHolderReplacer replacer = new NamedPlaceHolderReplacer();
        final Map values = new HashMap();
        values.put( "colour", "BROWN" );
        values.put( "action", "JUMPED" );       
        replacer.setValues( values );          
        
        final String expected = "The quick BROWN fox JUMPED over the lazy dog.";
        final String actual = replacer.execute( text );
        assertEquals(expected, actual);
    }
   }
