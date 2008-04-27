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
 * This simple logger sends all messages to the firebug console.
 * 
 * For browsers other than Firefox consider using firebug lite. The application
 * will fall over if firebug lite does not accompany the application. {see
 * Http://www.getfirebug.com/lite.html}
 * 
 * @author Miroslav Pokorny
 */
public class FirebugLogger extends LoggerImpl {

	public FirebugLogger() {
		super();
	}

	public void debug(final String message) {
		this.debug0(message);
	}

	public void debug(final String message, final Throwable throwable) {
		this.debug0(message);
	}

	native private void debug0(final String message)/*-{
	 $wnd.console.debug( message );
	 }-*/;

	public void info(final String message) {
		this.info0(message);
	}

	public void info(final String message, final Throwable throwable) {
		this.info0(message);
	}

	native private void info0(final String message)/*-{
	 $wnd.console.info( message );
	 }-*/;

	public void warn(final String message) {
		this.warn0(message);
	}

	public void warn(final String message, final Throwable throwable) {
		this.warn0(message);
	}

	native private void warn0(final String message)/*-{
	 $wnd.console.warn( message );
	 }-*/;

	public void error(final String message) {
		this.log(LoggingLevel.ERROR, message);
	}

	public void error(final String message, final Throwable throwable) {
		this.log(LoggingLevel.ERROR, message, throwable);
	}

	native private void error0(final String message)/*-{
	 $wnd.console.error( message );
	 }-*/;

	public void fatal(final String message) {
		this.fatal0(message);
	}

	public void fatal(final String message, final Throwable throwable) {
		this.fatal0(message);
	}

	/**
	 * All fatal messages appear as error messages in the firebug log.
	 * 
	 * @param message
	 */
	native private void fatal0(final String message)/*-{
	 $wnd.console.error( message );
	 }-*/;

	protected void log(final LoggingLevel level, final String message) {
		throw new UnsupportedOperationException();
	}

	protected void log(final LoggingLevel level, final String message, final Throwable throwable) {
		throw new UnsupportedOperationException();
	}
}
