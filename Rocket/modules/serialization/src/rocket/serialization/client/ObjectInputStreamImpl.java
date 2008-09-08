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
package rocket.serialization.client;

abstract public class ObjectInputStreamImpl implements ObjectInputStream {

	public ObjectInputStreamImpl() {
	}

	abstract protected void prepare(String stream);

	abstract protected int addObject(Object object);

	abstract protected void replaceObject(int reference, Object object);

	abstract protected Object getObject(int reference);

	abstract public boolean readBoolean();

	abstract public byte readByte();

	abstract public short readShort();

	abstract public int readInt();

	abstract public long readLong();

	abstract public float readFloat();

	abstract public double readDouble();

	abstract public char readChar();

	abstract protected String getString(int reference);

	protected void throwInvalidStringReference(final int reference) {
		throw new SerializationException("Encountered invalid reference " + reference + " whilst reading stream.");
	}

	protected boolean isNull(final int reference) {
		return Constants.NULL == reference;
	}

	protected boolean isNewObject(final int reference) {
		return Constants.NEW_OBJECT == reference;
	}

	protected boolean isStringReference(final int reference) {
		return reference >= Constants.STRING_BIAS;
	}

	public Object readObject() {
		Object object = null;

		while (true) {
			final int reference = this.readInt();
			// empty string is used to encode null..
			if (this.isNull(reference)) {
				break;
			}

			// if encountered 1 then process new object...
			if (this.isNewObject(reference)) {
				object = this.readNewObject();
				break;
			}

			if (this.isStringReference(reference)) {
				object = this.getString(reference);
				break;
			}

			// found an object back reference
			object = this.getObject(reference);
			break;
		}

		return object;
	}

	protected Object readNewObject() {
		final int classNameReference = this.readInt();
		final String className = this.getString(classNameReference);

		final Object object = readNewObject0(className);
		return object;
	}

	abstract protected Object readNewObject0(String className);

	protected void throwUnableToDeserialize(final String typeName) {
		throw new SerializationException("Unable to find ObjectReader for \"" + typeName + "\".");
	}

	protected void throwInvalidObjectReference(final int reference) {
		throw new SerializationException("Invalid object reference " + reference + " encountered within stream.");
	}
}
