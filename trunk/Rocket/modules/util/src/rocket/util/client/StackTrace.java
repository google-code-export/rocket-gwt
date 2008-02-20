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
package rocket.util.client;

import com.google.gwt.core.client.GWT;

/**
 * This class helps with converting stack traces - regardless of source into a
 * string that is similar to the familiar java stacktrace string produced by
 * {@link java.lang.Throwable#printStackTrace() }.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StackTrace {
	/**
	 * Converts the stackTrace of the given Throwable into a String that looks
	 * similar to a printed stackTrace.
	 * 
	 * ThrowableClassName at className.methodName.level0 at
	 * className.methodName.level1
	 * 
	 * @param throwable
	 * @return
	 */
	public static String asString(final Throwable throwable) {
		ObjectHelper.checkNotNull("parameter:throwable ", throwable);

		final StringBuffer buf = new StringBuffer();

		buf.append(GWT.getTypeName(throwable));
		buf.append(':');

		final String message = throwable.getMessage();
		if (!StringHelper.isNullOrEmpty(message)) {
			buf.append(' ');
			buf.append(throwable.getMessage());
		}
		buf.append('\n');

		final StackTraceElement[] elements = throwable.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			buf.append("\tat ");

			final StackTraceElement element = elements[i];
			buf.append(element.getClassName());

			final String methodName = element.getMethodName();
			if (methodName.length() > 0) {
				buf.append('.');
				buf.append(methodName);
			}
			buf.append('(');

			String fileName = element.getFileName();
			if (null == fileName) {
				fileName = "unknown";
			}
			buf.append(fileName);

			final int lineNumber = element.getLineNumber();
			if (false == (fileName.equals("unknown") || fileName.equals("native"))) {
				if (-1 != lineNumber) {
					buf.append(':').append(lineNumber);
				}
			}
			buf.append(")\n");
		}
		return buf.toString();
	}
}
