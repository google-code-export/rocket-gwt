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
package rocket.serialization.client.writer;

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.ObjectWriter;

import com.google.gwt.core.client.GWT;

/**
 * Custom ObjectWriter for throwable.
 * 
 * It includes a special case if the environment is running hosted mode, ie the
 * message, cause and stack trace elements will not be read from the instance
 * but dummy null values will be written in their place.
 * 
 * This is because these fields do not and cannot be accessed via jsni.
 * 
 * {@see rocket.serialization.client.reader.ThrowableReader}
 * 
 * @author Miroslav Pokorny
 * @serialization-type java.lang.Throwable
 */
public class ThrowableWriter extends ObjectWriterImpl {

	static public final ObjectWriter instance = new ThrowableWriter();

	protected ThrowableWriter() {
	}

	protected void write0(final Object object, final ObjectOutputStream objectOutputStream) {
		writeFields((Throwable) object, objectOutputStream);
	}

	protected void writeFields(final Throwable throwable, final ObjectOutputStream objectOutputStream) {
		if (GWT.isScript()) {
			this.writeScriptModeFields(throwable, objectOutputStream);
		} else {
			this.writeHostedModeFields(throwable, objectOutputStream);
		}
	}

	/**
	 * This method handles the writing of Throwable in hosted mode by writing
	 * null entries for each of the emulated Throwable fields.
	 * 
	 * @param throwable
	 *            The instance being serialized
	 * @param objectOutputStream
	 *            The outputstream being written too
	 */
	protected void writeHostedModeFields(final Throwable throwable, final ObjectOutputStream objectOutputStream) {
		objectOutputStream.writeObject(null);
		objectOutputStream.writeObject(null);
		objectOutputStream.writeInt(0);
	}

	/**
	 * This method is only invoked in script mode and uses jsni to read various
	 * emulated Throwable fields before serializing them.
	 * 
	 * @param throwable
	 *            The instance being serialized
	 * @param objectOutputStream
	 *            The outputstream being written too
	 */
	protected void writeScriptModeFields(final Throwable throwable, final ObjectOutputStream objectOutputStream) {
		final String message = this.getMessage(throwable);
		objectOutputStream.writeObject(message);

		final Throwable cause = this.getCause(throwable);
		objectOutputStream.writeObject(cause);

		// if stack trace elements is null write an empty array.
		final StackTraceElement[] elements = this.getStackTraceElements(throwable);
		final int count = elements == null ? 0 : elements.length;
		objectOutputStream.writeInt(count);

		for (int i = 0; i < count; i++) {
			final StackTraceElement element = elements[i];

			objectOutputStream.writeObject(element.getClassName());
			objectOutputStream.writeObject(element.getMethodName());
			objectOutputStream.writeObject(element.getFileName());
			objectOutputStream.writeInt(element.getLineNumber());
		}
	}

	native private String getMessage(final Throwable throwable)/*-{
			return throwable.@java.lang.Throwable::fMessage || null;
		}-*/;

	native private Throwable getCause(final Throwable throwable)/*-{
			return throwable.@java.lang.Throwable::fCause || null;
		}-*/;

	native private StackTraceElement[] getStackTraceElements(final Throwable throwable)/*-{
			return throwable.@java.lang.Throwable::stackTrace || null;
	}-*/;
}
