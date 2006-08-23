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
package rocket.client.dom;

import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Represents a handle to a single DOM element.
 * Typed setters/getters are available so that subclasses need only call the appropriate getter/setter
 * with the property name to be acccessed.
 * @author Miroslav Pokorny (mP)
 */
public abstract class ElementWrapper {
	 /**
     * The DOM element being wrapped
     */
    private Element element;

    public Element getElement(){
        ObjectHelper.checkNotNull( "field:element", element);
        return element;
    }

    public void setElement(final Element element){
        ObjectHelper.checkNotNull( "parameter:element", element);
        this.element = element;
    }

    public int hashCode(){
    	return System.identityHashCode( this.getElement());
    }

    public boolean equals( final Object other ){
    	boolean same = false;
    	if( null != other ){
    		final ElementWrapper otherWrapper =(ElementWrapper ) other;
    		same = this.getElement() == otherWrapper.getElement();
    	}
    	return same;
    }
    protected boolean hasProperty( final String name ){
    	return DomHelper.hasProperty( this.getElement(), name );
    }
    protected String getProperty( final String name ){
    	return DomHelper.getProperty( this.getElement(), name );
    }
    protected boolean getBooleanProperty( final String name ){
    	return DomHelper.getBooleanProperty( this.getElement(), name );
    }

    protected int getIntProperty( final String name ){
    	return DomHelper.getIntProperty( this.getElement(), name );
    }

    protected void setProperty( final String name, final boolean booleanValue ){
    	DomHelper.setProperty( this.getElement(), name, booleanValue );
    }

    protected void setProperty( final String name, final int intValue ){
    	DomHelper.setProperty( this.getElement(), name, intValue );
    }

    protected void setProperty( final String name, final String value ){
    	DomHelper.setProperty( this.getElement(), name, value );
    }

    public String toString(){
        return super.toString() + ", element[" + DOM.getAttribute(element, "outerHTML" ) + "]";
    }
}
