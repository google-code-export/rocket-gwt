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
package rocket.serialization.server.writer;

import java.lang.reflect.Field;

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.SerializationException;
import rocket.serialization.server.ReflectionHelper;
import rocket.serialization.server.ServerObjectWriter;

/**
 * A custom writer that writes all fields above Throwable using reflection but
 * writes fields belonging to the emulated Throwable in a format that is
 * compatible with the {@link rocket.serialization.client.reader.ObjectReader}.
 * 
 * @author Miroslav Pokorny
 */
public class ThrowableWriter extends ReflectiveWriter implements ServerObjectWriter {
	static public final ServerObjectWriter instance = new ThrowableWriter();

	protected ThrowableWriter() {
		this.cacheFields();
	}

	protected void cacheFields() {
		this.setMessage(ReflectionHelper.getThrowableMessageField());
		this.setCause(ReflectionHelper.getThrowableCauseField());
		this.setStackTraceElements(ReflectionHelper.getThrowableStackTraceElementField());
	}

	public boolean canWrite(final Object object) {
		return object instanceof Throwable;
	}

	protected void writeFields(final Object throwable, final Class classs, final ObjectOutputStream objectOutputStream)
			throws IllegalAccessException {
		if (classs.equals(Throwable.class)) {
			this.writeThrowableFields(throwable, objectOutputStream);
		} else {
			super.writeFields(throwable, classs, objectOutputStream);
		}
	}

	protected void writeThrowableFields(final Object throwable, final ObjectOutputStream objectOutputStream) {
		try {
			final String message = (String) this.getMessage().get(throwable);
			objectOutputStream.writeObject(message);

			final Throwable cause = (Throwable) this.getCause().get(throwable);
			objectOutputStream.writeObject(cause);

			// if stacktracelements is null write 0...should never be null.
			final StackTraceElement[] stackTraceElements = (StackTraceElement[]) this.getStackTraceElements().get(throwable);
			final int elementCount = stackTraceElements == null ? 0 : stackTraceElements.length;
			objectOutputStream.writeInt(elementCount);

			for (int i = 0; i < elementCount; i++) {
				final StackTraceElement element = stackTraceElements[i];

				objectOutputStream.writeObject(element.getClass().getName());
				objectOutputStream.writeObject(element.getMethodName());
				objectOutputStream.writeObject(element.getFileName());
				objectOutputStream.writeInt(element.getLineNumber());
			}

		} catch (final Exception caught) {
			throw new SerializationException("Unable to serialize Throwable instance, " + caught.getMessage());
		}
	}

	/**
	 * A cached copy of the {@link java.lang.Throwable#detailMessage } field
	 */
	private Field message;

	protected Field getMessage() {
		return this.message;
	}

	protected void setMessage(final Field message) {
		this.message = message;
	}

	/**
	 * A cached copy of the {@link java.lang.Throwable#cause } field
	 */
	private Field cause;

	protected Field getCause() {
		return this.cause;
	}

	protected void setCause(final Field cause) {
		this.cause = cause;
	}

	/**
	 * A cached copy of the {@link java.lang.Throwable#stacktrace } field
	 */
	private Field stackTraceElements;

	protected Field getStackTraceElements() {
		return this.stackTraceElements;
	}

	protected void setStackTraceElements(final Field stackTrace) {
		this.stackTraceElements = stackTrace;
	}
}
