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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.serialization.client.Constants;
import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.ObjectOutputStreamImpl;
import rocket.serialization.client.SerializationException;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

public class ServerObjectOutputStream extends ObjectOutputStreamImpl implements ObjectOutputStream {

	public ServerObjectOutputStream() {
		this.setValues(this.createValues());
		this.setStringTable(this.createStringTable());
		this.setObjectWriters(this.createObjectWriters());
		this.setObjectTable( this.createObjectTable() );
	}

	protected void addObject( Object object ){
		StringHelper.checkNotNull("parameter:object", object);

		final Map objects = this.getObjectTable();
		final int reference = (-1 - objects.size());
		objects.put(object, new Integer( reference));		
	}

		protected int findReference(final Object object){
			final Integer integer = (Integer)this.getObjectTable().get( object );
			return integer == null ? 0 : integer.intValue();			
		}
	
	
	/**
	 * A map that uses the identity of an object as the key and with the object as the value. 
	 * key: Objects value2: reference
	 */
	private Map objectTable;

	protected Map getObjectTable() {
		StringHelper.checkNotNull("field:objectTable", objectTable);
		return this.objectTable;
	}

	protected void setObjectTable(final Map objectTable) {
		StringHelper.checkNotNull("parameter:objectTable", objectTable);
		this.objectTable = objectTable;
	}

	protected Map createObjectTable() {
		return new java.util.IdentityHashMap();
	}

	public void writeBoolean(final boolean booleanValue) {
		final StringBuffer buf = this.getValues();
		if (buf.length() > 0) {
			buf.append(',');
		}
		buf.append(booleanValue);
	}

	public void writeByte(final byte byteValue) {
		final StringBuffer buf = this.getValues();
		if (buf.length() > 0) {
			buf.append(',');
		}
		buf.append(byteValue);
	}

	public void writeShort(final short shortValue) {
		final StringBuffer buf = this.getValues();
		if (buf.length() > 0) {
			buf.append(',');
		}
		buf.append(shortValue);
	}

	public void writeInt(final int intValue) {
		final StringBuffer buf = this.getValues();
		if (buf.length() > 0) {
			buf.append(',');
		}
		buf.append(intValue);
	}

	public void writeLong(final long longValue) {
		final StringBuffer buf = this.getValues();
		if (buf.length() > 0) {
			buf.append(',');
		}
		buf.append(longValue);
	}

	public void writeFloat(final float floatValue) {
		final StringBuffer buf = this.getValues();
		if (buf.length() > 0) {
			buf.append(',');
		}
		buf.append(floatValue);
	}

	public void writeDouble(final double doubleValue) {
		final StringBuffer buf = this.getValues();
		if (buf.length() > 0) {
			buf.append(',');
		}
		buf.append(doubleValue);
	}

	public void writeChar(final char charValue) {
		final StringBuffer buf = this.getValues();
		if (buf.length() > 0) {
			buf.append(',');
		}
		buf.append((int) charValue);
	}

	/**
	 * key: String value2: reference# Strings are not added to the object
	 * cache...Any ObjectInputStream must know whether it is reading a String or
	 * Object and use the reference for the appropriate cache.
	 */
	private Map stringTable;

	protected Map getStringTable() {
		StringHelper.checkNotNull("field:stringTable", stringTable);
		return this.stringTable;
	}

	protected void setStringTable(final Map stringTable) {
		StringHelper.checkNotNull("parameter:stringTable", stringTable);
		this.stringTable = stringTable;
	}

	protected Map createStringTable() {
		return new LinkedHashMap();
	}

	protected int getStringCount() {
		return this.getStringTable().size();
	}

	protected Iterator processedStrings() {
		return this.getStringTable().keySet().iterator();
	}

	protected int findStringReference(final String string) {
		int reference = 0;
		final Integer integer = (Integer) this.getStringTable().get(string);
		if( null != integer ){
			reference = integer.intValue();
		}
		return reference; 
	}

	protected int addString(final String string) {
		final Map strings = this.getStringTable();
		final int reference = strings.size() + Constants.STRING_BIAS;
		strings.put(string, new Integer( reference));
		return reference;
	}

	
	/**
	 * Accumulates any values including references that are written.
	 */
	private StringBuffer values;

	protected StringBuffer getValues() {
		return this.values;
	}

	protected void setValues(final StringBuffer values) {
		this.values = values;
	}

	protected StringBuffer createValues() {
		return new StringBuffer();
	}

	protected String getValuesText() {
		return this.getValues().toString();
	}

//	public String getTypeName(final Object object) {
//		ObjectHelper.checkNotNull("parameter:object", object);
//		return object.getClass().getName();
//	}

	public String getText() {
		final StringBuffer text = new StringBuffer();

		text.append( '[');
		
		// size ... comma
		text.append(this.getStringCount());
		text.append(',');

		// string table (comma separated)
		final Iterator stringIterator = this.processedStrings();
		while (stringIterator.hasNext()) {
			String string = (String) stringIterator.next();
			string = this.escape(string);
			text.append('"');
			text.append(string);
			text.append("\",");
		}

		// values (comma separated)
		text.append(this.getValuesText());

		text.append( ']');
		
		return text.toString();
	}

	protected void writeString( final Object string ){
		this.writeString((String) string );
	}
	
	protected String escape(final String string) {
		StringHelper.checkNotNull("parameter:string", string);

		final StringBuffer buf = new StringBuffer();
		final int length = string.length();
		for (int i = 0; i < length; i++) {
			final char c = string.charAt(i);
			if (c == '\"') {
				buf.append("\\\"");
				continue;
			}
			if (c == '\\') {
				buf.append("\\\\");
				continue;
			}
			if (c >= ' ') {
				buf.append(c);
				continue;
			}
			if (c == '\0' && c < 0x100 ) {
				buf.append("\\\0");
				continue;
			}
			if (c == '\b') {
				buf.append("\\\b");
				continue;
			}
			if (c == '\f') {
				buf.append("\\\f");
				continue;
			}
			if (c == '\t') {
				buf.append("\\\t");
				continue;
			}
			if (c == '\n') {
				buf.append("\\\n");
				continue;
			}
			if (c == '\r') {
				buf.append("\\\r");
				continue;
			}

			final int j = c;

			final String hex = Integer.toHexString((int) c);						
			if (j < 0x10) {
				buf.append("\\x0");
				buf.append(hex);
				buf.append(';');
				continue;
			}
			if (j < 0x100) {
				buf.append("\\x");
				buf.append(hex);
				buf.append(';');
				continue;
			}
			if (j < 0x1000) {
				buf.append("\\u0");
				buf.append(hex);
				buf.append(';');
				continue;
			}
			buf.append( "\\u");
			buf.append(j);
			buf.append(';');

		}
		return buf.toString();
	}
	
	protected boolean isString(final Object object){
		return object instanceof String;
	}

	protected void writeNewObject(final Object object) {
		boolean written = false;
		final Iterator writers = this.getObjectWriters().iterator();
		while (writers.hasNext()) {
			final ServerObjectWriter writer = (ServerObjectWriter) writers.next();
			if (writer.canWrite(object)) {
				writer.write(object, this);
				written = true;
				break;
			}
		}
		if (false == written) {
			throwUnableToSerialize( object );
		}
	}
	
	protected void throwUnableToSerialize( final Object object ){
		throw new SerializationException("Unable to serialize " + object.getClass().getName() );
	}

	private List objectWriters;

	protected List getObjectWriters() {
		ObjectHelper.checkNotNull("field:objectWriters", objectWriters);
		return this.objectWriters;
	}

	public void setObjectWriters(final List objectWriters) {
		ObjectHelper.checkNotNull("parameter:objectWriters", objectWriters);
		this.objectWriters = objectWriters;
	}

	protected List createObjectWriters() {
		return new ArrayList();
	}
}
