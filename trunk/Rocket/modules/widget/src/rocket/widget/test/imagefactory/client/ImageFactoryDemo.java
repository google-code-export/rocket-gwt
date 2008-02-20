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
package rocket.widget.test.imagefactory.client;

import rocket.util.client.StackTrace;
import rocket.widget.client.Html;
import rocket.widget.client.Image;
import rocket.widget.client.ImageFactory;
import rocket.widget.client.ZebraFlexTable;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public class ImageFactoryDemo implements EntryPoint {

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();

				Window.alert("Caught " + StackTrace.asString(caught));
			}
		});

		final TestImageFactory factory = (TestImageFactory)GWT.create(TestImageFactory.class);

		final ZebraFlexTable table = new ZebraFlexTable();
		
		int row = 0;
		
		table.setText( row, 0, "Image" );
		table.setText( row, 1, "Url" );
		table.setText( row, 2, "Local/Server" );
		table.setText( row, 3, "Server lazy/eager" );		
		row++;
		
		table.addHeadingStyleToFirstRow();
		
		final Image smallLocal = factory.createLocalSmallImage();
		table.setWidget(row, 0, smallLocal);
		table.setWidget(row, 1, this.createWidget(smallLocal.getUrl()));
		table.setText( row, 2, "local" );
		table.setText(row, 3, "lazy");
		row++;
		
		final Image smallServer = factory.createServerSmallImage();
		table.setWidget(row, 0, smallServer);
		table.setWidget(row, 1, this.createWidget(smallServer.getUrl()));
		table.setText( row, 2, "server" );
		table.setText(row, 3, "lazy");
		row++;
		
		final Image medium = factory.createLocalMediumImage();
		table.setWidget(row, 0, medium);
		table.setWidget(row, 1, this.createWidget(medium.getUrl()));
		table.setText( row, 2, "local" );
		table.setText(row, 3, "lazy");
		row++;
		
		final Image largeLocal = factory.createLocalLargeImage();
		table.setWidget(row, 0, largeLocal);
		table.setWidget(row, 1, this.createWidget(largeLocal.getUrl()));
		table.setText( row, 2, "local" );
		table.setText(row, 3, "lazy");
		row++;
		
		final Image largeLazyServer = factory.createLazyServerLargeImage();
		table.setWidget(row, 0, largeLazyServer);
		table.setWidget(row, 1, this.createWidget(largeLazyServer.getUrl()));
		table.setText( row, 2, "server" );
		table.setText(row, 3, "eager");
		row++;
		
		final CellFormatter cellFormatter = table.getCellFormatter();
		for( int r = 0; r < row; r++ ){
			for( int c = 0; c < 4; c++ ){
				cellFormatter.setAlignment(r, c, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE );
			}
		}
		
		final RootPanel rootPanel = RootPanel.get();
		rootPanel.add(table);
	}
	
	protected Widget createWidget( final String url ){
		String url0 = url;
		
		if( url.startsWith( "data")){
			final StringBuffer buf = new StringBuffer();
			buf.append( "<pre>");
			
			final int length = url.length();
			for( int i = 0; i < length; i=i+ 64 ){
				final int end = Math.min( i + 64, length );
				buf.append( url.substring( i, end ));
				buf.append( "\n");
			}
			buf.append( "</pre>");
			url0 = buf.toString();
		}		
		
		return new ScrollPanel( new Html( url0 ));
	}
	
	public interface TestImageFactory extends ImageFactory{
		/**
		 * @file small.png
		 * @location local
		 * @serverRequest lazy
		 */
		Image createLocalSmallImage();

		/**
		 * @file small.png
		 * @location server
		 * @serverRequest lazy
		 */
		Image createServerSmallImage();
		
		/**
		 * @file medium.jpg
		 * @location local
		 * @serverRequest eager
		 */
		Image createLocalMediumImage();
		/**
		 * @file large.jpg
		 * @location local
		 * @serverRequest eager
		 */
		Image createLocalLargeImage();
		/**
		 * @file large.jpg
		 * @location server
		 * @serverRequest eager
		 */
		Image createLazyServerLargeImage();
	}
}
