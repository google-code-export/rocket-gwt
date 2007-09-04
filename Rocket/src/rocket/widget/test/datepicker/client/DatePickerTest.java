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

import java.util.Date;

import rocket.widget.client.DatePicker;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class DatePickerTest implements EntryPoint {

	static final String[] DAY_NAMES = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	static final String[] MONTH_NAMES = new String[] { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };

	static final String TODAY_STYLE = "today";

	static final String WEEKEND_STYLE = "weekend";

	static final int YEAR_BIAS = 1900;

	public void onModuleLoad() {
		final RootPanel rootPanel = RootPanel.get();

		final Label monthYear = new Label();
		rootPanel.add(monthYear);

		final Button previousMonth = new Button("Previous Month");
		rootPanel.add(previousMonth);

		final Button nextMonth = new Button("Next Month");
		rootPanel.add(nextMonth);

		final DatePicker datePicker = new DatePicker() {
			protected boolean hasHeadings() {
				return true;
			}

			protected Widget createHeading(int dayOfWeek) {
				return new Label(DAY_NAMES[dayOfWeek]);
			}

			protected Widget createDateTile(final int year, final int month, final int dayOfMonth) {
				final Date day = new Date(year - YEAR_BIAS, month, dayOfMonth, 0, 1);

				final Label label = new Label("" + dayOfMonth);
				label.addClickListener(new ClickListener() {
					public void onClick(final Widget sender) {
						Window.alert("You have clicked on " + DAY_NAMES[day.getDay()] + " " + day.getDate() + "/" + (1 + day.getMonth())
								+ "/" + (YEAR_BIAS + day.getYear()) + "(DD/MM/YYYY)");
					}
				});

				final Date today = new Date();

				if (today.getYear() + YEAR_BIAS == year && today.getMonth() == month && today.getDate() == dayOfMonth) {
					label.addStyleName(TODAY_STYLE);
				}
				final int dayOfWeek = day.getDay();
				if (dayOfWeek == 0 || dayOfWeek == 6) {
					label.addStyleName(WEEKEND_STYLE);
				}
				return label;
			}

			public void redraw() {
				super.redraw();

				monthYear.setText(MONTH_NAMES[this.getMonth()] + " " + this.getYear());
			}
		};
		rootPanel.add(datePicker);

		previousMonth.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				datePicker.setMonth(datePicker.getMonth() - 1);
				datePicker.redraw();
			}
		});

		nextMonth.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				datePicker.setMonth(datePicker.getMonth() + 1);
				datePicker.redraw();
			}
		});

		datePicker.redraw();
	}
}
