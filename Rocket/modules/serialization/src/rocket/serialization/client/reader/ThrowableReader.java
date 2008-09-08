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
package rocket.serialization.client.reader;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectReader;

import com.google.gwt.core.client.GWT;

/**
 * A reader for Throwable.
 * 
 * The following components of {@link java.lang.Throwable} are serialized in the
 * following sequence.
 * <ol>
 * <li>message</li>
 * <li>cause</li>
 * <li>stack trace element count, will be 0 for null</li>
 * <li>stack trace elements.</li>
 * </ol>
 * 
 * The following components of {@link java.lang.StackTraceElement} are
 * serialized in the following sequence.
 * <ol>
 * <li>class name</li>
 * <li>method name</li>
 * <li>file name</li>
 * <li>line number</li>
 * </ol>
 * 
 * @author Miroslav Pokorny
 * 
 * @serialization-type java.lang.Throwable
 */
public class ThrowableReader extends ObjectReaderImpl implements ObjectReader {
	static public final ObjectReader instance = new ThrowableReader();

	public Object newInstance(final String typeName, final ObjectInputStream objectInputStream) {
		return new Throwable();
	}

	public void read(final Object instance, final ObjectInputStream objectInputStream) {
		this.readFields((Throwable) instance, objectInputStream);
	}

	public void readFields(final Throwable throwable, final ObjectInputStream objectInputStream) {
		// read throwable fields...
		final String message = (String) objectInputStream.readObject();
		final Throwable cause = (Throwable) objectInputStream.readObject();

		// read stack trace elements...
		final int elementCount = objectInputStream.readInt();
		final StackTraceElement[] elements = new StackTraceElement[elementCount];

		for (int i = 0; i < elementCount; i++) {
			final String className = (String) objectInputStream.readObject();
			final String methodName = (String) objectInputStream.readObject();
			final String fileName = (String) objectInputStream.readObject();
			final int lineNumber = objectInputStream.readInt();

			elements[i] = new StackTraceElement(className, methodName, fileName, lineNumber);
		}

		// dont save if in hosted mode because these fields dont exist on the
		// jdk Throwable, they only exist on the emulated version.
		if (GWT.isScript()) {
			this.updateEmulatedThrowableFields(throwable, message, cause, elements);
		}
	}

	/**
	 * This method should only be called in script mode (not hosted mode) as it
	 * attempts to update the fields on the emulated Throwable class which dont
	 * exist in hosted mode.
	 * 
	 * @param throwable
	 * @param message
	 * @param cause
	 * @param elements
	 */
	protected void updateEmulatedThrowableFields(final Throwable throwable, final String message, final Throwable cause,
			final StackTraceElement[] elements) {
		this.setMessage(throwable, message);
		this.setCause(throwable, cause);
		this.setStackTraceElements(throwable, elements);
	}

	native private void setMessage(final Throwable throwable, final String message)/*-{
		 throwable.@java.lang.Throwable::detailMessage=message;
		 }-*/;

	native private void setCause(final Throwable throwable, final Throwable cause)/*-{
		 throwable.@java.lang.Throwable::cause=cause;
		 }-*/;

	native private void setStackTraceElements(final Throwable throwable, final StackTraceElement[] elements)/*-{
		 throwable.@java.lang.Throwable::stackTrace = elements;
		 }-*/;
}
