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
package rocket.widget.test.hyperlinkpanel;

import java.util.Iterator;

import rocket.widget.client.Label;
import rocket.widget.client.Panel;
import rocket.widget.client.SimplePanel;
import rocket.widget.test.divpanel.DivPanelGwtTestCase;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class HyperlinkPanelGwtTestCase extends DivPanelGwtTestCase {

	@Override
	public String getModuleName() {
		return "rocket.widget.test.hyperlinkpanel.HyperlinkPanelGwtTestCase";
	}

	@Override
	public void testAdd() {
		final Panel panel = this.createPanel();

		panel.add( new Label() );
		
		assertEquals(1, panel.getWidgetCount());
		assertEquals(1, panel.getElement().getChildNodes().getLength());

		final Label label = (Label)panel.get( 0 );
		assertSame(panel, label.getParent());
		assertSame(panel.getElement(), getElementClosestToPanelElement(label).getParentElement());
	}

	@Override
	public void testMultipleAdds() {
		final Panel panel = this.createPanel();
		panel.add( new Label() );
		
		try {
			panel.add(new Label("banana"));
			fail("Add should have failed because this panel only excepts one widget.");
		} catch (final Exception expected) {

		}
	}

	@Override
	public void testInsertBeginning() {
		final SimplePanel panel = this.createSimplePanel();
		panel.clear();
		
		final Label label = new Label("first");
		panel.insert(label, 0);

		assertEquals(1, panel.getWidgetCount());
		assertEquals(1, panel.getElement().getChildNodes().getLength());

		assertSame(panel, label.getParent());
		assertSame(panel.getElement(), getElementClosestToPanelElement(label).getParentElement());

		// verify index.
		assertEquals(0, DOM.getChildIndex(panel.getElement(), getElementClosestToPanelElement(label)));

		try {
			panel.insert(new Label("second"), 0);
			fail("Attempting to insert for a second time on the " + panel.getClass().getName());
		} catch (final Exception expected) {
		}
	}

	@Override
	public void testInsertMiddle() {
		final SimplePanel panel = this.createSimplePanel();
		panel.clear();

		final Label label = new Label("middle");

		try {
			panel.insert(label, 2);
			fail("Attempting to insert at position 2 should have failed.");
		} catch (final Exception expected) {

		}
	}

	@Override
	public void testGet() {
		final SimplePanel panel = this.createSimplePanel();
		panel.clear();
		
		final Label first = new Label("first");

		panel.add(first);

		assertSame(first, panel.get(0));

		try {
			final Widget widget = panel.get(1);
			fail("Get with an index other than 0 should have failed.");
		} catch (final Exception expected) {

		}
	}

	@Override
	public void testIndexOf() {
		final SimplePanel panel = this.createSimplePanel();
		panel.clear();
		
		final Label first = new Label("first");

		panel.add(first);

		assertEquals(0, panel.indexOf(first));
		assertEquals(-1, panel.indexOf(new Label("-1")));
	}

	@Override
	public void testRemove() {
		final SimplePanel panel = this.createSimplePanel();
		panel.clear();
		final Label label = new Label("banana");
		panel.add(label);

		label.removeFromParent();

		assertNull(label.getParent());

		assertEquals(panel.getElement().getInnerHTML(), -1, panel.getElement().getInnerHTML().indexOf("banana"));
		assertFalse(panel.getElement().isOrHasChild(label.getElement()));
	}

	@Override
	public void testClear() {
		final SimplePanel panel = this.createSimplePanel();
		panel.clear();
		
		panel.add(new Label("x"));
		panel.clear();

		assertEquals(0, panel.getWidgetCount());
		assertEquals(0, panel.getElement().getChildNodes().getLength());

		assertEquals("", panel.getElement().getInnerHTML());
	}

	@Override
	public void testIterator() {
		final SimplePanel panel = this.createSimplePanel();
		panel.clear();
		
		final Label first = new Label("first");

		panel.add(first);

		final Iterator iterator = panel.iterator();
		assertTrue(iterator.hasNext());
		assertSame(first, iterator.next());

		iterator.remove();
		assertEquals(0, panel.getWidgetCount());
		assertEquals(0, panel.getElement().getChildNodes().getLength());

		assertFalse(iterator.hasNext());
		//		try{
		//			final Object returned = iterator.next();
		//			fail( "iterator should have failed and not returned " + returned );
		//		} catch ( final Exception expected ){
		//			
		//		}
	}
	
	public void testSetWidget(){
		final SimplePanel panel = this.createSimplePanel();
		panel.setWidget( new Label() );
		panel.setWidget( new Label() );
		
		final Label label = new Label( "third");
		panel.setWidget( label );

		assertEquals(1, panel.getWidgetCount());
		assertEquals(1, panel.getElement().getChildNodes().getLength());
		
		assertSame(panel, label.getParent());
		assertSame(panel.getElement(), getElementClosestToPanelElement(label).getParentElement());
		
		panel.setWidget( null );
		assertEquals(0, panel.getWidgetCount());
		assertEquals(0, panel.getElement().getChildNodes().getLength());		
	}
	
	protected SimplePanel createSimplePanel() {
		return (SimplePanel) this.createPanel();
	}
	
	protected Panel createPanel() {
		return new rocket.widget.client.HyperlinkPanel();
	}

	protected String outterElementTagName() {
		return "a";
	}

	protected String innerElementTagName() {
		return "div"; // label
	}
}
