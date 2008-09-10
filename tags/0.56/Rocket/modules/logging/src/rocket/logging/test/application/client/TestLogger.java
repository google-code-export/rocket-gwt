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
package rocket.logging.test.application.client;

import rocket.logging.client.Logger;
import rocket.logging.client.LoggerImpl;
import rocket.logging.client.LoggingLevel;
import rocket.widget.client.Html;

import com.google.gwt.user.client.ui.RootPanel;

public class TestLogger extends LoggerImpl implements Logger {

	public TestLogger() {
		super();
	}

	public TestLogger(final String category) {
		super(category);
	}

	protected void log(final LoggingLevel level, final String message) {
		RootPanel.get().add(new Html("<div><img src=\"" + getIconUrl(level) + "\" />" + message + "</div>"));
	}

	protected void log(final LoggingLevel level, final String message, final Throwable throwable) {
		throwable.printStackTrace();
		RootPanel.get().add(new Html("<div><img src=\"" + getIconUrl(level) + "\" />" + message + "</div>"));
	}

	String getIconUrl(final LoggingLevel level) {
		return level.toString() + ".png;";
	}
}
