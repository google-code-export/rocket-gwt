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
package rocket.widget.test.pager.demo.client;

import rocket.event.client.ChangeEvent;
import rocket.event.client.ChangeEventListener;
import rocket.util.client.StringHelper;
import rocket.widget.client.Html;
import rocket.widget.client.Pager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PagerTest implements EntryPoint {

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
			}
		});
		
		final Label summary = new Label( "");
		summary.addStyleName( "pagerTest-summary");
		
		final DockPanel dockPanel = new DockPanel();
		dockPanel.add( summary, DockPanel.NORTH );

		final String item = DOM.getInnerHTML(DOM.getElementById( "item"));
		
		final VerticalPanel list = new VerticalPanel();
		
		final Pager pager = new Pager();
		pager.setFirstItem( 0 );
		pager.setLastItem( 1000 );
		pager.setPagesAcrossCount( 10 );
		pager.setItemsPerPage( 10 );
		
		pager.addChangeEventListener(new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				final int currentPage = pager.getCurrentPage();
				final int itemsPerPage = pager.getItemsPerPage();
				final int lastItem = pager.getLastItem();
				final int last = Math.min( currentPage + itemsPerPage, lastItem ) - 1;
				
				summary.setText("Results: " + pager.getCurrentPage() + "-" + last + " of " + lastItem );
				
				list.clear();
				for( int i= currentPage; i < last; i++ ){
					final String html = StringHelper.format( item, new Object[]{ "" + i });
					list.add( new Html( html ));
				}
			}
		});

		pager.setCurrentPage( 0 );
		pager.redraw();
		dockPanel.add( pager, DockPanel.SOUTH );
		dockPanel.add( list, DockPanel.CENTER );
		RootPanel.get().add( dockPanel );
	}
}
