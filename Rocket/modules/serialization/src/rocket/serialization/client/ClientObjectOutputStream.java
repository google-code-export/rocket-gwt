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

import rocket.util.client.Checker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * This type is typically never instantiated by user code but only leveraged by
 * generated SerializationFactory's to support the serialization of objects.
 * 
 * To achieve maximum performance jsni is used, eg rather than mapping typeNames
 * to ObjectWriters the generator uses a switch statement to locate an
 * ObjectWriter
 * 
 * @author Miroslav Pokorny
 */
abstract public class ClientObjectOutputStream extends ObjectOutputStreamImpl {

	public ClientObjectOutputStream() {
		super();

		this.setObjectTable(this.createObjectTable());
	}

	native protected int getStringCount() /*-{
	 return this.@rocket.serialization.client.ClientObjectOutputStream::stringTable.length;
	 }-*/;

	native protected void writeString(final Object string)/*-{
			this.@rocket.serialization.client.ObjectOutputStreamImpl::writeString(Ljava/lang/String;)(string);
		}-*/;

	native String getString(final int index)/*-{
		 return this.@rocket.serialization.client.ClientObjectOutputStream::stringTable[ index ];
		 }-*/;

	native protected int addString(final String string)/*-{
		 // add the escaped form of the String to the StringTable...
		 var length = this.@rocket.serialization.client.ClientObjectOutputStream::stringTable.length;
		 
		 // escape and add to string table.
		 var escaped = this.@rocket.serialization.client.ClientObjectOutputStream::escape(Ljava/lang/String;)(string);	 	 
		 this.@rocket.serialization.client.ClientObjectOutputStream::stringTable.push( '"' + escaped + '"' );
		 
		 // build a  that contains the backreference for the input string.
		 var reference = @rocket.serialization.client.Constants::STRING_BIAS + length;
		 
		 // prefix with colon to avoid clashes with in built properties
		 this.@rocket.serialization.client.ClientObjectOutputStream::stringReferenceTable[ ":" + string ] = reference;
		 
		  return reference;
		 }-*/;

	native protected String escape(final String unescaped)/*-{
			return unescaped.replace( /"/g, '\\"' );
		}-*/;

	native protected int findStringReference(final String string)/*-{	
		 var reference = this.@rocket.serialization.client.ClientObjectOutputStream::stringReferenceTable[ ":" + string ];
		 return reference ? reference : 0;
		 }-*/;

	/**
	 * An array that holds any encountered strings.
	 */
	private JavaScriptObject stringTable = this.createArray();

	/**
	 * This table holds recently encountered strings (in unescaped form) and
	 * their corresponding reference (an integer value).
	 */
	private JavaScriptObject stringReferenceTable = createObject();

	native private JavaScriptObject createObject()/*-{
		 return new Object();
		 }-*/;

	/**
	 * This javascript array accumulates any written primitive values.
	 */
	private JavaScriptObject values = this.createArray();

	native private JavaScriptObject createArray()/*-{
		 return new Array();
		 }-*/;

	native public void writeBoolean(final boolean booleanValue) /*-{
	 this.@rocket.serialization.client.ClientObjectOutputStream::values.push( booleanValue );
	 }-*/;

	native public void writeByte(final byte byteValue) /*-{
	 this.@rocket.serialization.client.ClientObjectOutputStream::values.push( byteValue );
	 }-*/;

	native public void writeShort(final short shortValue) /*-{
	 this.@rocket.serialization.client.ClientObjectOutputStream::values.push( shortValue );
	 }-*/;

	native public void writeInt(final int intValue) /*-{
	 this.@rocket.serialization.client.ClientObjectOutputStream::values.push( intValue );
	 }-*/;

	// native public void writeLong(final long longValue) /*-{
	// this.@rocket.serialization.client.ClientObjectOutputStream::values.push(
	// longValue );
	// }-*/;

	public void writeLong(final long longValue) {
		final int hi = (int) (longValue >> 32);
		final int lo = (int) longValue;
		this.writeInt(hi);
		this.writeInt(lo);
	}

	native public void writeFloat(final float floatValue) /*-{
	 this.@rocket.serialization.client.ClientObjectOutputStream::values.push( floatValue );
	 }-*/;

	native public void writeDouble(final double doubleValue) /*-{
	 this.@rocket.serialization.client.ClientObjectOutputStream::values.push( doubleValue );
	 }-*/;

	public void writeChar(final char charValue) {
		this.writeInt((int) charValue);
	}

	/**
	 * Hack as String.equals(Object/String) is slower than the javascript
	 * snippet below.
	 */
	protected boolean isString(final Object object) {
		return "java.lang.String".equals(object.getClass().getName());
	}

	protected void writeNewObject(final Object object) {
		Checker.notNull("parameter:object", object);

		final String typeName = object.getClass().getName();
		final ObjectWriter objectWriter = this.getObjectWriter(typeName);
		if (null == objectWriter) {
			this.throwUnableToSerializable(typeName);
		}
		objectWriter.write(object, this);
	}

	/**
	 * This method will be implemented by the SerializationFactoryGenerator to
	 * return the right ObjectWriter for the given type name.
	 * 
	 * If an ObjectWriter was not found an exception will be thrown via
	 * {@link #throwUnableToSerializable(String)}.
	 * 
	 * @param typeName
	 *            The name of the type about to be serialized.
	 * @return The located ObjectWriter
	 */
	abstract protected ObjectWriter getObjectWriter(String typeName);

	/**
	 * A javascriptobject which is used as a map.
	 * 
	 * A string containing ":" and the identity hashcode of the object is used
	 * as the key with the reference held as the value.
	 */
	private JavaScriptObject objectTable;

	protected JavaScriptObject getObjectTable() {
		return this.objectTable;
	}

	protected void setObjectTable(final JavaScriptObject objectTable) {
		this.objectTable = objectTable;
	}

	native protected JavaScriptObject createObjectTable() /*-{
		var map = {};
		map.nextReference = -1;
		return map;
	}-*/;

	native protected void addObject(final Object object)/*-{
			var objectTable = this.@rocket.serialization.client.ClientObjectOutputStream::objectTable; 
			var reference = objectTable.nextReference;
			
			// use the identityHashCode of the incoming object as the key (prefixed with colon).
			var identityHashCode = ":" + @java.lang.System::identityHashCode(Ljava/lang/Object;)(object);
			objectTable[ identityHashCode ] = reference;
			objectTable.nextReference = reference - 1;
		}-*/;

	native protected int findReference(final Object object)/*-{
			var identityHashCode = ":" + @java.lang.System::identityHashCode(Ljava/lang/Object;)(object);
			var objectTable = this.@rocket.serialization.client.ClientObjectOutputStream::objectTable;
			var reference =  objectTable[ identityHashCode ];
		
			// if the object wasnt found return 0...
			return reference ? reference : 0;
		}-*/;

	/**
	 * Builds a json string that includes the string table at the beginning
	 * followed by the values.
	 */
	native public String getText()/*-{	
		 var stringTable = this.@rocket.serialization.client.ClientObjectOutputStream::stringTable;
		 var array = new Array();
		 
		 var stringCount = stringTable.length;
		 array.push( stringCount );
		 
		 if( stringCount > 0 ){
		 	array.push( stringTable );
		 }
		 
		 var values = this.@rocket.serialization.client.ClientObjectOutputStream::values;
		 array.push( values );
		 
		 return "[" + array.toString() + "]";
		 }-*/;

	public String toString() {
		return super.toString() + "= \"" + this.getText() + "\"";
	}
}
