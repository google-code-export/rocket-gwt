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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.SerializationException;
import rocket.serialization.server.ServerObjectWriter;
import rocket.serialization.server.ServerObjectWriterImpl;
import rocket.util.client.Checker;

/**
 * Convenient base class for any instance who which is instrumented by a library
 * such as CGLIB.
 * 
 * This class caches the accessible properties for each and every type that it
 * sees. The first time a type is visited the {@link #findGetters(Class)} is
 * called to find or discover all getters after which these are placed in a Map
 * to cache.
 * 
 * @author Miroslav Pokorny
 */
abstract public class PropertyWriter extends ServerObjectWriterImpl implements ServerObjectWriter {

	public PropertyWriter() {
		super();

		this.setGetters(this.createGetters());
	}

	abstract public boolean canWrite(final Object instance);

	public void writeType(final Object object, final ObjectOutputStream objectOutputStream) {
		objectOutputStream.writeObject(object.getClass().getSuperclass().getName());
	}

	protected void write0(final Object object, final ObjectOutputStream objectOutputStream) {
		final Method[] getters = this.getGetters(object);
		for (int i = 0; i < getters.length; i++) {
			final Method getter = getters[i];
			this.writeProperty(getter, object, objectOutputStream);
		}
	}

	final static Object[] NO_ARGUMENTS = new Object[0];

	protected void writeProperty(final Method getter, final Object object, final ObjectOutputStream objectOutputStream) {
		try {
			while (true) {
				final Class propertyType = getter.getReturnType();
				final Object value = getter.invoke(object, NO_ARGUMENTS);

				if (propertyType.equals(Boolean.TYPE)) {
					objectOutputStream.writeBoolean(((Boolean) value).booleanValue());
					break;
				}
				if (propertyType.equals(Byte.TYPE)) {
					objectOutputStream.writeByte(((Byte) value).byteValue());
					break;
				}
				if (propertyType.equals(Short.TYPE)) {
					objectOutputStream.writeShort(((Short) value).shortValue());
					break;
				}
				if (propertyType.equals(Integer.TYPE)) {
					objectOutputStream.writeInt(((Integer) value).intValue());
					break;
				}
				if (propertyType.equals(Long.TYPE)) {
					objectOutputStream.writeLong(((Long) value).longValue());
					break;
				}
				if (propertyType.equals(Float.TYPE)) {
					objectOutputStream.writeFloat(((Float) value).floatValue());
					break;
				}
				if (propertyType.equals(Double.TYPE)) {
					objectOutputStream.writeDouble(((Double) value).doubleValue());
					break;
				}
				if (propertyType.equals(Character.TYPE)) {
					objectOutputStream.writeChar(((Character) value).charValue());
					break;
				}
				objectOutputStream.writeObject(value);
				break;
			}
		} catch (final IllegalAccessException illegalAccessException) {
			throw new SerializationException(illegalAccessException);
		} catch (final InvocationTargetException invocationTargetException) {
			final Throwable cause = invocationTargetException.getCause();
			if (cause instanceof Error) {
				final Error error = (Error) cause;
				throw new SerializationException(error);
			}
			if (cause instanceof RuntimeException) {
				final RuntimeException runtimeException = (RuntimeException) cause;
				throw new SerializationException(runtimeException);
			}
			throw new SerializationException(cause);
		}
	}

	/**
	 * Retrieves all the property getters for the given object's class.
	 * 
	 * @param object
	 * @return
	 */
	protected Method[] getGetters(final Object object) {
		final Map cache = this.getGetters();
		final Class classs = object.getClass();

		Method[] getters = (Method[]) cache.get(classs);
		if (null == getters) {
			getters = this.findGetters(classs);
			cache.put(classs, getters);
		}
		return getters;
	}

	/**
	 * Discovers all the property getters for the given class.
	 * 
	 * @param classs
	 * @return
	 */
	protected Method[] findGetters(final Class classs) {
		final List<Method> getters = new ArrayList<Method>();

		final Method[] allMethods = classs.getDeclaredMethods();
		for (int i = 0; i < allMethods.length; i++) {
			final Method method = allMethods[i];

			// skip static methods...
			final int modifers = method.getModifiers();
			if (Modifier.isStatic(modifers)) {
				continue;
			}
			// skip methods that arent public...
			if (false == Modifier.isPublic(modifers)) {
				continue;
			}

			// skip if return type is void.
			final Class returnType = method.getReturnType();
			if (returnType.equals(Void.TYPE)) {
				continue;
			}

			// skip if method has any arguments...
			if (method.getParameterTypes().length > 0) {
				continue;
			}

			// if method name starts with isXXX
			final String methodName = method.getName();
			if (methodName.startsWith("is")) {
				if (returnType.equals(Boolean.TYPE)) {
					method.setAccessible(true);
					getters.add(method);
				}
				continue;
			}
			// getter but return type must not be boolean
			if (methodName.startsWith("get")) {
				if (false == returnType.equals(Boolean.TYPE)) {
					method.setAccessible(true);
					getters.add(method);
				}
				continue;
			}
		}

		// copy the array into Method[]
		return (Method[]) getters.toArray(new Method[getters.size()]);
	}

	/**
	 * A map that contains a cache of getter methods
	 */
	private Map getters;

	protected Map getGetters() {
		Checker.notNull("field:getters", getters);
		return this.getters;
	}

	protected void setGetters(final Map getters) {
		Checker.notNull("parameter:getters", getters);
		this.getters = getters;
	}

	protected Map createGetters() {
		return new HashMap();
	}
}
