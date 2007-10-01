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
package rocket.widget.test.datepicker.client;

import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.widget.client.Button;
import rocket.widget.client.DatePicker;
import rocket.widget.client.Image;
import rocket.widget.client.Label;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public class DatePickerTest implements EntryPoint {

	static final String[] DAY_NAMES = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	static final String[] MONTH_NAMES = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };

	static final String TODAY_STYLE = "today";

	static final String WEEKEND_STYLE = "weekend";

	static final int YEAR_BIAS = 1900;

	public void onModuleLoad() {
		final RootPanel rootPanel = RootPanel.get();
		
		rootPanel.add( createLargeCalendar() );
		
		final Label date = new Label("?");
		rootPanel.add( date );
		
		final Image popup = new Image( "calendar_edit.png");
		popup.addMouseEventListener( new MouseEventAdapter(){
			
			public void onClick( final MouseClickEvent event ){				
				final DialogBox dialogBoxWithDatePicker = new DialogBox(){
					public boolean XXXonEventPreview(final com.google.gwt.user.client.Event event) {
						if( DOM.eventGetType( event ) == com.google.gwt.user.client.Event.ONMOUSEOUT ){
							hide();
						}
						return super.onEventPreview(event);
					}
				};															
				final int x = event.getPageX();
				final int y = event.getPageY();
				dialogBoxWithDatePicker.setPopupPositionAndShow( new PositionCallback(){
					public void setPosition(final int offsetWidth, final int offsetHeight){
						dialogBoxWithDatePicker.setPopupPosition( x - offsetWidth / 2, y - offsetHeight / 2);
					}
				});
				
				dialogBoxWithDatePicker.setWidget( createMiniCalendar( date, dialogBoxWithDatePicker ) );
				
				dialogBoxWithDatePicker.show();
			}
		});
		rootPanel.add( popup );
	}
	
	Widget createLargeCalendar(){
		final FlexTable grid = new FlexTable();

		final Button previousMonth = new Button("Previous Month");
		grid.setWidget( 0, 0, previousMonth);

		final Label monthAndYear = new Label();
		grid.setWidget( 0, 1, monthAndYear);
		
		final Button nextMonth = new Button("Next Month");
		grid.setWidget( 0, 2, nextMonth);
		
		final DatePicker datePicker = new DatePicker() {
			protected boolean hasHeadings() {
				return true;
			}

			protected Widget createHeading(int dayOfWeek) {
				return new Label(DAY_NAMES[dayOfWeek]);
			}

			protected Widget createDateTile(final int year, final int month, final int dayOfMonth) {
				final VerticalPanel panel = new VerticalPanel();
				
				final Label label = new Label( "" + dayOfMonth );
				label.addMouseEventListener( new MouseEventAdapter(){
					public void onClick( final MouseClickEvent event ){
						Window.alert("Clicked on " + dayOfMonth  + "/" + month + "/" + year + "(dd/mm/yyyy)");
					}
				});
				panel.add( label );

				if( dayOfMonth == 1 || dayOfMonth == 2 || dayOfMonth == 4 || dayOfMonth == 8 || dayOfMonth == 16 ){
					panel.add( new Image( "anchor.png"));					
				}
				if( dayOfMonth == 2 || dayOfMonth == 4 || dayOfMonth == 8 || dayOfMonth == 16 ){
					panel.add( new Image( "chart_organisation.png"));					
				}
				if( dayOfMonth == 3 || dayOfMonth == 6 || dayOfMonth == 12 || dayOfMonth == 24  ){
					panel.add( new Image( "chart_pie.png"));					
				}
				if( dayOfMonth == 4 || dayOfMonth == 8 || dayOfMonth == 16 ){
					panel.add( new Image( "coins.png"));					
				}
				if( dayOfMonth == 5 || dayOfMonth == 10 || dayOfMonth == 15 || dayOfMonth == 20  ){
					panel.add( new Image( "bell.png"));					
				}
				return panel;
			}

			public void redraw() {
				super.redraw();

				monthAndYear.setText(MONTH_NAMES[this.getMonth()] + " " + this.getYear());
			}
		};
		grid.setWidget( 1, 0, datePicker);
		grid.getFlexCellFormatter().setColSpan( 1, 0, 3 );
		
		previousMonth.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				datePicker.setMonth(datePicker.getMonth() - 1);
				datePicker.redraw();
			}
		});

		nextMonth.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				datePicker.setMonth(datePicker.getMonth() + 1);
				datePicker.redraw();
			}
		});

		return grid;

	}
	
	Widget createMiniCalendar( final Label date, final DialogBox dialogBox ){
		final FlexTable grid = new FlexTable();

		final Button previousMonth = new Button("&lt;&lt;");
		grid.setWidget( 0, 0, previousMonth);

		final Label monthAndYear = new Label();
		grid.setWidget( 0, 1, monthAndYear);
		
		final Button nextMonth = new Button("&gt;&gt;");
		grid.setWidget( 0, 2, nextMonth);
		
		final DatePicker datePicker = new DatePicker() {
			protected boolean hasHeadings() {
				return true;
			}

			protected Widget createHeading(int dayOfWeek) {
				return new Label(DAY_NAMES[dayOfWeek].substring( 0, 1 ));
			}

			protected Widget createDateTile(final int year, final int month, final int dayOfMonth) {
				final Label label = new Label( "" + dayOfMonth );
				label.addMouseEventListener( new MouseEventAdapter(){
					public void onClick( final MouseClickEvent event ){
						date.setText( "" + dayOfMonth  + "/" + month + "/" + year + "(dd/mm/yyyy)");
						dialogBox.hide();
					}
				});
				return label;
			}

			public void redraw() {
				super.redraw();

				monthAndYear.setText(MONTH_NAMES[this.getMonth()] + " " + this.getYear());
			}
		};
		grid.setWidget( 1, 0, datePicker);
		grid.getFlexCellFormatter().setColSpan( 1, 0, 3 );
		
		previousMonth.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				datePicker.setMonth(datePicker.getMonth() - 1);
				datePicker.redraw();
			}
		});

		nextMonth.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				datePicker.setMonth(datePicker.getMonth() + 1);
				datePicker.redraw();
			}
		});

		return grid;
	}	

}
