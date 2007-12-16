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
package rocket.serialization.server.reader;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.SerializationException;
import rocket.serialization.server.ReflectionHelper;
import rocket.serialization.server.ServerObjectReader;

/**
 * A reader for uses reflection to set fields upon a new instance.
 * 
 * @author Miroslav Pokorny
 */
public class ReflectiveReader implements ServerObjectReader {

	static public final ServerObjectReader instance = new ReflectiveReader();

	protected ReflectiveReader() {
	}

	/**
	 * This class can read anything thats serializable.
	 */
	public boolean canRead(final Class classs) {
		return Serializable.class.isAssignableFrom( classs );
	}

	public Object newInstance(final String name, final ObjectInputStream objectInputStream) {
		try {
			final Class classs = Class.forName( name );
			return classs.newInstance();
		} catch (final ClassNotFoundException classNotFound) {
			throw new SerializationException("Unable to find class \"" + name + "\".", classNotFound);
		} catch (final InstantiationException noDefaultConstructor) {
			throw new SerializationException("Unable to instantiate \"" + name + "\".", noDefaultConstructor);
		} catch (final IllegalAccessException illegalAccessException) {
			throw new SerializationException("Unable to instantiate \"" + name + "\".", illegalAccessException);
		}
	}

	public void read(final Object instance, final ObjectInputStream objectInputStream){
		this.readFields(instance, instance.getClass(), objectInputStream );
	}

	protected void readFields(final Object object, final Class classs, final ObjectInputStream objectInputStream) {
		try {
			this.readFields0(object, classs, objectInputStream );
		} catch (final IllegalAccessException illegalAccessException) {
			throw new SerializationException(illegalAccessException);
		}
	}

	protected void readFields0(final Object object, final Class classs, final ObjectInputStream objectInputStream ) throws IllegalAccessException {
		final Set serializableFields = ReflectionHelper.buildSerializableFields(object, classs);

		// serialize fields in alphabetical order
		final Iterator iterator = serializableFields.iterator();
		while (iterator.hasNext()) {
			final Field field = (Field) iterator.next();
			field.setAccessible(true);

			final Class fieldType = field.getType();

			if (fieldType.equals(Boolean.TYPE)) {
				field.setBoolean(object, objectInputStream.readBoolean());
				continue;
			}
			if (fieldType.equals(Byte.TYPE)) {
				field.setByte(object, objectInputStream.readByte());
				continue;
			}
			if (fieldType.equals(Short.TYPE)) {
				field.setShort(object, objectInputStream.readShort());
				continue;
			}
			if (fieldType.equals(Integer.TYPE)) {
				field.setInt(object, objectInputStream.readInt());
				continue;
			}
			if (fieldType.equals(Long.TYPE)) {
				field.setLong(object, objectInputStream.readLong());
				continue;
			}
			if (fieldType.equals(Float.TYPE)) {
				field.setFloat(object, objectInputStream.readFloat());
				continue;
			}
			if (fieldType.equals(Double.TYPE)) {
				field.setDouble(object, objectInputStream.readDouble());
				continue;
			}
			if (fieldType.equals(Character.TYPE)) {
				field.setChar(object, objectInputStream.readChar());
				continue;
			}
			field.set(object, objectInputStream.readObject());
		}

		final Class superType = classs.getSuperclass();
		if (false == superType.equals(Object.class)) {
			this.readFields(object, superType, objectInputStream );
		}
	}

}
