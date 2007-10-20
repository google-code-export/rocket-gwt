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
package rocket.widget.test.pager.unit;

import java.util.ArrayList;
import java.util.List;

import rocket.widget.client.Pager;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class PagerGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.widget.test.pager.unit.PagerGwtTestCase";
	}

	public void testSmallNumberOfPages() {
		final List pages = new ArrayList();
		
		final Pager pager = new Pager() {

			protected Widget createPage(final String label, final int itemNumber) {
				pages.add( new Integer( itemNumber ));
				return super.createPage(label, itemNumber);
			}
		};

		pager.setFirstItem(0);
		pager.setLastItem(10);
		pager.setItemsPerPage(10);
		pager.setPagesAcrossCount(10);

		pager.setCurrentPage(0);

		RootPanel.get().add(pager);
		pager.redraw();
		
		assertEquals( "" + pages, 1, pages.size() );
		assertTrue( "" + pages, pages.remove( new Integer( 0 )));
		assertTrue( "" + pages, pages.isEmpty() );
	}
	
	public void testSetToFirstPossibleItem() {
		final List pages = new ArrayList();
		
		final Pager pager = new Pager() {

			protected Widget createPage(final String label, final int itemNumber) {
				pages.add( new Integer( itemNumber ));
				return super.createPage(label, itemNumber);
			}
		};
		pager.setFirstItem(0);
		pager.setLastItem(1000);
		pager.setItemsPerPage(10);
		pager.setPagesAcrossCount(10);

		pager.setCurrentPage(0);

		RootPanel.get().add(pager);
		pager.redraw();
		
		assertEquals( "" + pages, 10, pages.size() );
		assertTrue( "" + pages, pages.remove( new Integer( 0 )));
		assertTrue( "" + pages, pages.remove( new Integer( 10 )));
		assertTrue( "" + pages, pages.remove( new Integer( 20 )));
		assertTrue( "" + pages, pages.remove( new Integer( 30 )));
		assertTrue( "" + pages, pages.remove( new Integer( 40 )));
		assertTrue( "" + pages, pages.remove( new Integer( 50 )));
		assertTrue( "" + pages, pages.remove( new Integer( 60 )));
		assertTrue( "" + pages, pages.remove( new Integer( 70 )));
		assertTrue( "" + pages, pages.remove( new Integer( 80 )));
		assertTrue( "" + pages, pages.remove( new Integer( 90 )));
		assertTrue( "" + pages, pages.isEmpty() );
	}

	public void testSetToSomewhereOnFirstPage() {
		final List pages = new ArrayList();
		
		final Pager pager = new Pager() {

			protected Widget createPage(final String label, final int itemNumber) {
				pages.add( new Integer( itemNumber ));
				return super.createPage(label, itemNumber);
			}
		};
		
		pager.setFirstItem(0);
		pager.setLastItem(1000);
		pager.setItemsPerPage(10);
		pager.setPagesAcrossCount(10);

		pager.setCurrentPage(2);

		RootPanel.get().add(pager);
		pager.redraw();

		assertEquals( "" + pages, 10, pages.size() );
		assertTrue( "" + pages, pages.remove( new Integer( 0 )));
		assertTrue( "" + pages, pages.remove( new Integer( 10 )));
		assertTrue( "" + pages, pages.remove( new Integer( 20 )));
		assertTrue( "" + pages, pages.remove( new Integer( 30 )));
		assertTrue( "" + pages, pages.remove( new Integer( 40 )));
		assertTrue( "" + pages, pages.remove( new Integer( 50 )));
		assertTrue( "" + pages, pages.remove( new Integer( 60 )));
		assertTrue( "" + pages, pages.remove( new Integer( 70 )));
		assertTrue( "" + pages, pages.remove( new Integer( 80 )));
		assertTrue( "" + pages, pages.remove( new Integer( 90 )));
		assertTrue( "" + pages, pages.isEmpty() );
	}

	public void testInMiddle() {
		final List pages = new ArrayList();
		
		final Pager pager = new Pager() {

			protected Widget createPage(final String label, final int itemNumber) {
				pages.add( new Integer( itemNumber ));
				return super.createPage(label, itemNumber);
			}
		};
		pager.setFirstItem(0);
		pager.setLastItem(1000);
		pager.setItemsPerPage(10);
		pager.setPagesAcrossCount(10);

		pager.setCurrentPage(500);

		RootPanel.get().add(pager);
		pager.redraw();
		
		assertEquals( "" + pages, 10, pages.size() );
		assertTrue( "" + pages, pages.remove( new Integer( 450 )));
		assertTrue( "" + pages, pages.remove( new Integer( 460 )));
		assertTrue( "" + pages, pages.remove( new Integer( 470 )));
		assertTrue( "" + pages, pages.remove( new Integer( 480 )));
		assertTrue( "" + pages, pages.remove( new Integer( 490 )));
		assertTrue( "" + pages, pages.remove( new Integer( 500 )));
		assertTrue( "" + pages, pages.remove( new Integer( 510 )));
		assertTrue( "" + pages, pages.remove( new Integer( 520 )));
		assertTrue( "" + pages, pages.remove( new Integer( 530 )));
		assertTrue( "" + pages, pages.remove( new Integer( 540 )));
		assertTrue( "" + pages, pages.isEmpty() );
	}

	public void testSetToSomewhereOnLastPage() {
		final List pages = new ArrayList();
		
		final Pager pager = new Pager() {

			protected Widget createPage(final String label, final int itemNumber) {
				pages.add( new Integer( itemNumber ));
				return super.createPage(label, itemNumber);
			}
		};
		pager.setFirstItem(0);
		pager.setLastItem(1000);
		pager.setItemsPerPage(10);
		pager.setPagesAcrossCount(10);

		pager.setCurrentPage(961);

		RootPanel.get().add(pager);
		pager.redraw();
		
		assertEquals( "" + pages, 10, pages.size() );
		assertTrue( "" + pages, pages.remove( new Integer( 900 )));
		assertTrue( "" + pages, pages.remove( new Integer( 910 )));
		assertTrue( "" + pages, pages.remove( new Integer( 920 )));
		assertTrue( "" + pages, pages.remove( new Integer( 930 )));
		assertTrue( "" + pages, pages.remove( new Integer( 940 )));
		assertTrue( "" + pages, pages.remove( new Integer( 950 )));
		assertTrue( "" + pages, pages.remove( new Integer( 960 )));
		assertTrue( "" + pages, pages.remove( new Integer( 970 )));
		assertTrue( "" + pages, pages.remove( new Integer( 980 )));
		assertTrue( "" + pages, pages.remove( new Integer( 990 )));
		assertTrue( "" + pages, pages.isEmpty() );
	}

	public void testSetToLastPossibleItem() {
		final List pages = new ArrayList();
		
		final Pager pager = new Pager() {

			protected Widget createPage(final String label, final int itemNumber) {
				pages.add( new Integer( itemNumber ));
				return super.createPage(label, itemNumber);
			}
		};
		pager.setFirstItem(0);
		pager.setLastItem(1000);
		pager.setItemsPerPage(10);
		pager.setPagesAcrossCount(10);

		pager.setCurrentPage(999);

		RootPanel.get().add(pager);
		pager.redraw();
		
		assertEquals( "" + pages, 10, pages.size() );
		assertTrue( "" + pages, pages.remove( new Integer( 900 )));
		assertTrue( "" + pages, pages.remove( new Integer( 910 )));
		assertTrue( "" + pages, pages.remove( new Integer( 920 )));
		assertTrue( "" + pages, pages.remove( new Integer( 930 )));
		assertTrue( "" + pages, pages.remove( new Integer( 940 )));
		assertTrue( "" + pages, pages.remove( new Integer( 950 )));
		assertTrue( "" + pages, pages.remove( new Integer( 960 )));
		assertTrue( "" + pages, pages.remove( new Integer( 970 )));
		assertTrue( "" + pages, pages.remove( new Integer( 980 )));
		assertTrue( "" + pages, pages.remove( new Integer( 990 )));
		assertTrue( "" + pages, pages.isEmpty() );
	}
}
