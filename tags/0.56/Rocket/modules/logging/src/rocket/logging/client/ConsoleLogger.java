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
package rocket.logging.client;

/**
 * Logs all messages to the browsers console.
 * 
 * If the browser does not include support for window.console or
 * window.console.log messages will be consumed into the ether.
 * 
 * @author Miroslav Pokorny
 */
public class ConsoleLogger extends LoggerImpl {

	@Override
	protected void log(final LoggingLevel level, final String message) {
		this.log0(message);
	}

	@Override
	protected void log(final LoggingLevel level, final String message, final Throwable throwable) {
		this.log0(message);
	}

	native private void log0(final String message)/*-{
		 if( $wnd.console && $wnd.console.log ){
		 $wnd.console.log( message );
		 }
		 }-*/;

}
