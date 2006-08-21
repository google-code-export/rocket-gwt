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
package rocket.test.dom;

import rocket.client.dom.AbstractElementList;
import rocket.client.dom.DomHelper;
import rocket.client.dom.ElementWrapper;
import rocket.client.util.SystemHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class AbstractElementListTest extends GWTTestCase {

    final static String NAME = "name";
    final static String VALUE = "value";


   final static String FIRST_NAME = "first";
   final static String FIRST_VALUE = "firstValue";

   final static String SECOND_NAME = "second";
   final static String SECOND_VALUE = "secondValue";

   final static String THIRD_NAME = "third";
   final static String THIRD_VALUE = "thirdValue";
   /**
   * Must refer to a valid module that inherits from com.google.gwt.junit.JUnit
   */
  public String getModuleName() {
    return "rocket.test.Test";
  }

  public void testSize0(){
      final TestAbstractDomElementList list = new TestAbstractDomElementList();
      list.setCollection( getFormElements() );

      assertEquals( "list.size", 3, list.size());
  }

  public void testGet0OnlyTestsForNotNull(){
      final TestAbstractDomElementList list = new TestAbstractDomElementList();
      list.setCollection( getFormElements() );

      final Object firstWrapper = list.get( 0 );
      assertNotNull( "list.get(0)", firstWrapper );

      final Object secondWrapper = list.get( 1 );
      assertNotNull( "list.get(1)", secondWrapper );

      final Object thirdWrapper = list.get( 2 );
      assertNotNull( "list.get(2)", thirdWrapper );
  }

  public void testGet1WithCasts(){
      final TestAbstractDomElementList list = new TestAbstractDomElementList();
      list.setCollection( getFormElements() );

      final TestDomElementWrapper firstWrapper = (TestDomElementWrapper)list.get( 0 );
      assertNotNull( "list.get(0)", firstWrapper );

      final TestDomElementWrapper secondWrapper = (TestDomElementWrapper)list.get( 1 );
      assertNotNull( "list.get(1)", secondWrapper );

      final TestDomElementWrapper thirdWrapper = (TestDomElementWrapper)list.get( 2 );
      assertNotNull( "list.get(2)", thirdWrapper );
   }

  public void testGet2WithCastsAndChecksIsCorrectElement(){
      final TestAbstractDomElementList list = new TestAbstractDomElementList();
      list.setCollection( getFormElements() );

      final TestDomElementWrapper firstWrapper = (TestDomElementWrapper)list.get( 0 );
      assertNotNull( "list.get(0)", firstWrapper );
      assertEquals( "list.get(0).value", FIRST_VALUE, firstWrapper.getValue());

      final TestDomElementWrapper secondWrapper = (TestDomElementWrapper)list.get( 1 );
      assertNotNull( "list.get(1)", secondWrapper );
      assertEquals( "list.get(1).value", SECOND_VALUE, secondWrapper.getValue());

      final TestDomElementWrapper thirdWrapper = (TestDomElementWrapper)list.get( 2 );
      assertNotNull( "list.get(2)", thirdWrapper );
      assertEquals( "list.get(2).value", THIRD_VALUE, thirdWrapper.getValue());
   }

  public void testGet3VerifiesSameWrapperIsReturnedWhenSameIndexIsUsed(){
      final TestAbstractDomElementList list = new TestAbstractDomElementList();
      list.setCollection( getFormElements() );

      assertSame( "list.get(0)", list.get( 0 ), list.get( 0 ) );
      assertSame( "list.get(1)", list.get( 1 ), list.get( 1 ) );
      assertSame( "list.get(2)", list.get( 2 ), list.get( 2 ) );
      assertSame( "list.get(0)", list.get( 0 ), list.get( 0 ) );
      assertSame( "list.get(1)", list.get( 1 ), list.get( 1 ) );
      assertSame( "list.get(2)", list.get( 2 ), list.get( 2 ) );
  }

  public void testSet0WhichTestsReplacedElement(){
      final TestAbstractDomElementList list = new TestAbstractDomElementList();
      list.setCollection( getFormElements() );

      final TestDomElementWrapper originalFirstWrapper = (TestDomElementWrapper)list.get( 0 );

      final Element newFirstElement = DOM.createInputText();
      DOM.setAttribute( newFirstElement, NAME, "newFirst");
      DOM.setAttribute( newFirstElement, VALUE, "newFirstValue");
      final TestDomElementWrapper newFirstWrapper = new TestDomElementWrapper();
      newFirstWrapper.setElement( newFirstElement );

      this.addCheckpoint( "" + DOM.getAttribute( newFirstElement, "outerHTML" ));

      final TestDomElementWrapper replacedWrapper = (TestDomElementWrapper)list.set( 0, newFirstWrapper );
      assertNotNull( "set(0) should have returned a Wrapper", replacedWrapper );
      assertEquals( "set(0) replaced.value", "firstValue", replacedWrapper.getValue());

      this.addCheckpoint( "" + list.get( 0 ));
      assertSame( "list.get(0)", newFirstWrapper, list.get( 0 ) );
      assertSame( "list.get(1)", list.get( 1 ), list.get( 1 ) );
      assertSame( "list.get(2)", list.get( 2 ), list.get( 2 ) );
  }

  /**
   * Factory which creates a form with three elements.
   * @return
   */
  static JavaScriptObject getFormElements(){
      final Element form = DOM.createElement( "FORM");
      DOM.appendChild( RootPanel.getBodyElement(), form );

      final Element first = DOM.createInputText();
      DOM.setAttribute( first, NAME, FIRST_NAME);
      DOM.setAttribute( first, VALUE, FIRST_VALUE);
      DOM.appendChild( form, first );

      final Element second = DOM.createInputText();
      DOM.setAttribute( second, NAME, SECOND_NAME);
      DOM.setAttribute( second, VALUE, SECOND_VALUE);
      DOM.appendChild( form, second );

      final Element third = DOM.createInputText();
      DOM.setAttribute( third, NAME, THIRD_NAME);
      DOM.setAttribute( third, VALUE, THIRD_VALUE);
      DOM.appendChild( form, third );

      return DomHelper.getPropertyAsJavaScriptObject( form, "elements");
  }

  class TestAbstractDomElementList extends AbstractElementList {

      protected Object createWrapper(final Element element) {
          DomHelper.checkTagName( "parameter:element", element, "INPUT");

          final TestDomElementWrapper wrapper = new TestDomElementWrapper();
          wrapper.setElement( element );
          return wrapper;
      }

      protected void checkElementType(Object wrapper) {
          if(false==( wrapper instanceof TestDomElementWrapper)){
              SystemHelper.handleAssertFailure( "element", "Unknown element type, type[" + GWT.getTypeName( wrapper ));
          }
      }
  }

  class TestDomElementWrapper extends ElementWrapper {
      public String getValue(){
          return this.getProperty( VALUE);
      }
  }

}

