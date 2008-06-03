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
package rocket.serialization.server;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import rocket.serialization.client.SerializationException;

public class ReflectionHelper {

	final static Comparator<Field> FIELD_COMPARATOR = new Comparator<Field>() {

		public int compare(final Field field, final Field otherField) {
			return field.getName().compareTo(otherField.getName());
		}
	};

	/**
	 * Builds a set that contains all the serializable fields sorted in
	 * alphabetical order
	 * 
	 * @param object
	 * @param classs
	 * @return A set containing all the fields.
	 */
	static public Set<Field> buildSerializableFields(final Object object, final Class classs) {
		final Field[] fields = classs.getDeclaredFields();
		final Set<Field> serializableFields = new TreeSet<Field>(ReflectionHelper.FIELD_COMPARATOR);

		for (int i = 0; i < fields.length; i++) {
			final Field field = fields[i];
			field.setAccessible(true);

			final int modifiers = field.getModifiers();

			// skip static fields
			if (Modifier.isFinal(modifiers)) {
				continue;
			}
			// skip transient fields
			if (Modifier.isTransient(modifiers)) {
				continue;
			}

			final String name = field.getName();
			// complain if final
			if (Modifier.isFinal(modifiers)) {
				throw new SerializationException("Unable to serialize type because of final field \"" + name + "\" belonging to type: "
						+ classs.getName());
			}

			serializableFields.add(field);
		} // for

		return serializableFields;
	}

	static public Field getThrowableMessageField() {
		return getThrowableField(Constants.MESSAGE_FIELD);
	}

	static public Field getThrowableCauseField() {
		return getThrowableField(Constants.CAUSE_FIELD);
	}

	static public Field getThrowableStackTraceElementField() {
		return getThrowableField(Constants.STACK_TRACE_FIELD);
	}

	static protected Field getThrowableField(final String fieldName) {
		try {
			final Class throwable = Throwable.class;

			final Field field = throwable.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;

		} catch (final Exception exception) {
			throw new RuntimeException(exception);
		}
	}
}
