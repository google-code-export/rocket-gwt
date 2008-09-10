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
package rocket.widget.test.divpanel;

import java.util.Iterator;

import rocket.dom.client.Dom;
import rocket.widget.client.DivPanel;
import rocket.widget.client.Label;
import rocket.widget.client.Panel;
import rocket.widget.client.Widget;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;


public class DivPanelGwtTestCase extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "rocket.widget.test.divpanel.DivPanelGwtTestCase";
	}

	public void testNewPanel(){
		final Panel panel = this.createPanel();
	
		this.assertTagName(panel.getElement(), this.outterElementTagName());
		
		// no widgets and no children
		assertEquals( 0, panel.getWidgetCount() );
		assertEquals( 0, panel.getElement().getChildNodes().getLength() );
	}
	
	public void testAdd(){
		final Panel panel = this.createPanel();
	
		final Label label = new Label("apple");
		panel.add( label );
		
		assertEquals( 1, panel.getWidgetCount() );
		assertEquals( 1, panel.getElement().getChildNodes().getLength() );
		
		assertSame( panel, label.getParent() );
		assertSame( panel.getElement(), getElementClosestToPanelElement( label ).getParentElement());
	}
	
	public void testMultipleAdds(){
		final Panel panel = this.createPanel();
	
		panel.add( new Label("apple") );
		panel.add( new Label( "banana") );
		final Label label = new Label("carrot");
		panel.add( label );
		
		assertEquals( 3, panel.getWidgetCount() );
		assertEquals( 3, panel.getElement().getChildNodes().getLength() );
		
		assertSame( panel, label.getParent() );
		assertTrue( panel.getElement().isOrHasChild( label.getElement() ));
		assertTagName( (Element)label.getElement().getParentElement(), this.innerElementTagName() );
	}
	

	public void testInsertBeginning(){
		final Panel panel = this.createPanel();
	
		panel.add( new Label( "apple"));
		panel.add( new Label( "banana"));
		panel.add( new Label( "carrot"));
		
		final Label label = new Label("first");
		panel.insert( label, 0 );
		
		assertEquals( 4, panel.getWidgetCount() );
		assertEquals( 4, panel.getElement().getChildNodes().getLength() );
		
		assertSame( panel, label.getParent() );
		assertSame( panel.getElement(), getElementClosestToPanelElement( label ).getParentElement());
		
		// verify index.
		assertEquals( 0, DOM.getChildIndex( panel.getElement(), getElementClosestToPanelElement( label ) ));
	}
	

	public void testInsertMiddle(){
		final Panel panel = this.createPanel();
	
		panel.add( new Label( "apple"));
		panel.add( new Label( "banana"));
		panel.add( new Label( "carrot"));
		
		final Label label = new Label("middle");
		panel.insert( label, 2 );
		
		assertEquals( 4, panel.getWidgetCount() );
		assertEquals( 4, panel.getElement().getChildNodes().getLength() );
		
		assertSame( panel, label.getParent() );
		assertSame( panel.getElement(), getElementClosestToPanelElement( label ).getParentElement());
		
		// verify index.
		assertEquals( 2, DOM.getChildIndex( panel.getElement(), getElementClosestToPanelElement( label )));
	}


	public void testGet(){
		final Panel panel = this.createPanel();
	
		final Label first = new Label("first");
		final Label second = new Label("second");
		final Label third = new Label("third");
		
		panel.add( first );
		panel.add( second );
		panel.add( third );
		
		assertSame( first, panel.get( 0 ));
		assertSame( second, panel.get( 1 ));
		assertSame( third, panel.get( 2 ));
	}

	public void testIndexOf(){
		final Panel panel = this.createPanel();
	
		final Label first = new Label("first");
		final Label second = new Label("second");
		final Label third = new Label("third");
		
		panel.add( first );
		panel.add( second );
		panel.add( third );
		
		assertEquals( 0, panel.indexOf( first ));
		assertEquals( 1, panel.indexOf(second));
		assertEquals( 2, panel.indexOf(third));
		assertEquals( -1, panel.indexOf( new Label("-1")));
	}
	
	public void testRemove(){
		final Panel panel = this.createPanel();
	
		panel.add( new Label("apple") );
		final Label label = new Label("banana");
		panel.add( label );
		
		label.removeFromParent();
		
		assertNull( label.getParent() );
		
		assertEquals( panel.getElement().getInnerHTML(), -1, panel.getElement().getInnerHTML().indexOf( "banana") );
		assertFalse( panel.getElement().isOrHasChild( label.getElement() ));		
	}
	
	public void testClear(){
		final Panel panel = this.createPanel();
	
		for( int i = 0; i < 10; i++ ){
			panel.add( new Label( "" + i ));
		}
		
		panel.clear();
		
		assertEquals( 0, panel.getWidgetCount() );
		assertEquals( 0, panel.getElement().getChildNodes().getLength() );
		
		assertEquals( "", panel.getElement().getInnerHTML() );		
	}
	
	public void testIterator(){
		final Panel panel = this.createPanel();
		
		final Label first = new Label("first");
		final Label second = new Label("second");
		final Label third = new Label("third");
		
		panel.add( first );
		panel.add( second );
		panel.add( third );
		
		final Iterator iterator = panel.iterator();
		assertTrue( iterator.hasNext() );
		assertSame( first, iterator.next() );
		
		assertTrue( iterator.hasNext() );
		assertSame( second, iterator.next() );
		
		iterator.remove();
		assertEquals( 2, panel.getWidgetCount() );
		assertEquals( 2, panel.getElement().getChildNodes().getLength() );
		
		assertTrue( iterator.hasNext() );
		assertTrue( iterator.hasNext() );
		assertSame( third, iterator.next() );
		
		assertFalse( iterator.hasNext() );
//		try{
//			final Object returned = iterator.next();
//			fail( "iterator should have failed and not returned " + returned );
//		} catch ( final Exception expected ){
//			
//		}
	}
	
	protected void assertTagName( final Element element, final String tagName ){
		if( false == Dom.isTag(element, tagName)){
			fail( "Element was of the wrong tag expected \"" + tagName + "\" but got \"" + element.getTagName() + "\" .");
		}
	}
	
	protected Panel createPanel(){
		return new DivPanel();
	}
	
	protected String outterElementTagName(){
		return "div";
	}
	protected String innerElementTagName(){
		return "div";
	}
	
	protected Element getElementClosestToPanelElement( final Widget widget ){
		Element element = widget.getElement();
		final Element parent = widget.getParent().getElement();
		
		while( true ){
			if( element.getParentElement() == parent ){
				break;
			}
			element = element.getParentElement().cast();
		}
		
		return element;
	}
}
