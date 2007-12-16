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

import java.util.Iterator;

import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

abstract public class ObjectOutputStreamImpl implements ObjectOutputStream {

	public ObjectOutputStreamImpl() {
		super();
	}
		
	abstract protected int getStringCount();

	abstract protected int findStringReference(String string);

	abstract protected int addString(String string);

	abstract protected void writeString( Object string );
	
	public void writeString(final String string) {
		int reference = this.findStringReference(string);
		if (0 == reference) {
			reference = this.addString(string);
		}

		this.writeInt(reference);
	}
	
	abstract public void writeBoolean(boolean booleanValue);

	abstract public void writeByte(byte byteValue);

	abstract public void writeShort(short shortValue);

	abstract public void writeInt(int intValue);

	abstract public void writeLong(long longValue);

	abstract public void writeFloat(float floatValue);

	abstract public void writeDouble(double doubleValue);

	abstract public void writeChar(char charValue);

	protected void writeNull() {
		this.writeInt(Constants.NULL);
	}

	abstract protected void addObject( Object object );

	abstract protected int findReference(final Object object);
	
	public void writeObject(final Object object) throws SerializationException {

		while (true) {
			// if object is null push an empty string / token...
			if (null == object) {
				this.writeNull();
				break;
			}

			// special case for string...saves a instanceof
			if( this.isString( object )){
				this.writeString( object );
				break;
			}

			// is it serializable ?
			// this.checkIsSerializable(object);

			// check object cache...
			final int reference = this.findReference(object);
			if ( 0 != reference) {
				this.writeInt(reference);
				break;
			}

			this.addObject(object);
			this.writeNewObjectHeader();
			this.writeNewObject(object);
			break;
		}
	}

	abstract protected boolean isString(Object object);

	abstract protected void writeNewObject(final Object object);

	protected void writeNewObjectHeader() {
		this.writeInt(Constants.NEW_OBJECT);
	}

	protected void throwUnableToSerializable( final String typeName ){
		throw new SerializationException("Unable to find ObjectWriter for \"" + typeName + "\".");
	}
}
