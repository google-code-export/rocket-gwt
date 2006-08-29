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
package rocket.client.widget.form;

import java.util.Iterator;
import java.util.Map;

import rocket.client.dom.DomConstants;
import rocket.client.dom.DomHelper;
import rocket.client.util.HttpHelper;
import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
* A variety of methods that deal with submitting of forms using RPC, and other useful use cases.
* @author Miroslav Pokorny (mP)
*/
public class FormHelper extends DomHelper{
    /**
     * Populates the given map with the values of the elements belonging to the given form.
     * The element name becomes the key and teh value the entry value.
     * @param map
     * @param form
     */
    public static void populateMapFromForm(final Map map, final Element form) {
        ObjectHelper.checkNotNull( "parameter:map", map );
        ObjectHelper.checkNotNull( "parameter:form", form );

        final Iterator formElements = FormHelper.getFormElements( form );
        while( formElements.hasNext() ){
        	final Element formElement = (Element) castToElement( formElements.next() );
        	final String name = DOM.getAttribute( formElement, DomConstants.NAME );
        	final String value = FormHelper.getFormSubmitValue( formElement );

        	map.put( name, value );
        }
    }

    /**
     * Encodes all the elements belonging to form into a url encoded safe String.
     *
     * @param form
     * @return
     */
    public static String urlEncodeForm(final Element form) {
        ObjectHelper.checkNotNull("parameter:form", form);

        final StringBuffer urlEncoded = new StringBuffer();
        boolean addSeparator = false;

        final Iterator formElements = FormHelper.getFormElements( form );
        while( formElements.hasNext() ){
        	if( addSeparator ){
        		urlEncoded.append( '&');
        	}

        	final Element formElement = (Element) castToElement( formElements.next() );
        	final String name = DomHelper.getProperty( formElement, DomConstants.NAME );
        	final String value = HttpHelper.urlEncode( FormHelper.getFormSubmitValue( formElement ));
        	urlEncoded.append( name );
        	urlEncoded.append( '=' );
        	urlEncoded.append( value );

        	addSeparator = true;
        }

        return urlEncoded.toString();
    }
	/**
     * This method is smart in that it tests the tag type of the given element and reads the appropriate attribute
     * that contains the textual value of this element.
     * This is the value that would have been submitted for this field if its parent
     * form was submitted.
     * @param element
     * @return
     */
    public static String getFormSubmitValue( final Element element ){
    	ObjectHelper.checkNotNull( "parameter:element", element );

    	String value = null;
    	while( true ){
            if( isTag( element, FormConstants.LIST_TAG) ){
                value = DOM.getAttribute( element, DomConstants.VALUE);
                break;
            }
    		if( isTag( element, FormConstants.TEXTAREA_TAG) ){
    			value = DOM.getInnerText( element );
    			break;
    		}

    		if( DomHelper.isTag( element, DomConstants.INPUT_TAG)){
    			value = DOM.getAttribute( element, DomConstants.VALUE);
    			break;
    		}

    		throw new UnsupportedOperationException( "Cannot get the formSubmitValue for the element, element: " + toString( element ));
    	}

    	return value;
    }

    /**
     * Returns an iterator which may be used to visit all the elements for a particular form.
     * Because form.elements are cannot have elements added / removed this iterator is read only(aka the remove() ) doesnt work.
     * @param form
     * @return
     */
    public static Iterator getFormElements( final Element form ){
    	DomHelper.checkTagName("parameter:form", form, DomConstants.FORM_TAG);

    	return new Iterator(){
    		public boolean hasNext(){
    			return this.getIndex() < DOM.getIntAttribute( form, DomConstants.LENGTH_PROPERTY);
    		}

    		public Object next(){
    			final int index = this.getIndex();
    			final Object nextt = this.next0( form, index );
    			this.setIndex( index+ 1);
    			return nextt;
    		}

    		protected native Object next0( final Element form, final int index )/*-{
				return form.elements[ index ];
    		}-*/;

    		public void remove(){
    			throw new UnsupportedOperationException("Unable to remove a form element from a FormIterator");
    		}

    		int index = 0;

    		int getIndex(){
    			return index;
    		}

    		void setIndex( final int index ){
    			this.index = index;
    		}
    	};
    }
}
