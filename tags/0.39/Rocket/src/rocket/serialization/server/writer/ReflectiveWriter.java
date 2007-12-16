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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.SerializationException;
import rocket.serialization.server.ReflectionHelper;
import rocket.serialization.server.ServerObjectWriter;
import rocket.util.client.ObjectHelper;

/**
 * This writer should be considered the default writer and uses reflection to write all
 * serializable fields from an instance
 * @author Miroslav Pokorny
 */
public class ReflectiveWriter implements ServerObjectWriter{
	
	static public final ServerObjectWriter instance = new ReflectiveWriter();
	
	protected ReflectiveWriter(){
		super();
	}
	
	public boolean canWrite(final Object object){
		return object instanceof Serializable;
	}
	
	public void write(final Object object, final ObjectOutputStream objectOutputStream){
		try {
			final Class classs = object.getClass();
			objectOutputStream.writeObject(classs.getName());
			this.writeFields(object, classs, objectOutputStream );

		} catch (IllegalAccessException illegalAccess) {
			throw new SerializationException(illegalAccess);
		}
	}

	/**
	 * Writes all the serializable fields from the object instance for the given class.
	 * THis method continues to call itself recursively until java.lang.Object is reached.
	 * @param object A non null instance being serialized
	 * @param classs
	 * @param objectOutputStream
	 * @throws IllegalAccessException
	 */
	protected void writeFields(final Object object, final Class classs, final ObjectOutputStream objectOutputStream ) throws IllegalAccessException {
		ObjectHelper.checkNotNull("parameter:object", object);
		ObjectHelper.checkNotNull("parameter:classs", classs);
		ObjectHelper.checkNotNull("parameter:objectOutputStream", objectOutputStream );

		final Set serializableFields = ReflectionHelper.buildSerializableFields(object, classs);

		// serialize fields in alphabetical order
		final Iterator iterator = serializableFields.iterator();
		while (iterator.hasNext()) {
			final Field field = (Field) iterator.next();
			final Class fieldType = field.getType();

			if (fieldType.equals(Boolean.TYPE)) {
				objectOutputStream.writeBoolean(field.getBoolean(object));
				continue;
			}
			if (fieldType.equals(Byte.TYPE)) {
				objectOutputStream.writeByte(field.getByte(object));
				continue;
			}
			if (fieldType.equals(Short.TYPE)) {
				objectOutputStream.writeShort(field.getShort(object));
				continue;
			}
			if (fieldType.equals(Integer.TYPE)) {
				objectOutputStream.writeInt(field.getInt(object));
				continue;
			}
			if (fieldType.equals(Long.TYPE)) {
				objectOutputStream.writeLong(field.getLong(object));
				continue;
			}
			if (fieldType.equals(Float.TYPE)) {
				objectOutputStream.writeFloat(field.getFloat(object));
				continue;
			}
			if (fieldType.equals(Double.TYPE)) {
				objectOutputStream.writeDouble(field.getDouble(object));
				continue;
			}
			if (fieldType.equals(Character.TYPE)) {
				objectOutputStream.writeChar(field.getChar(object));
				continue;
			}
			objectOutputStream.writeObject(field.get(object));
		}

		// serialize the fields belonging to the super type...
		final Class superType = classs.getSuperclass();
		if (false == superType.equals(Object.class)) {
			this.writeFields(object, superType, objectOutputStream );
		}
	}
}
