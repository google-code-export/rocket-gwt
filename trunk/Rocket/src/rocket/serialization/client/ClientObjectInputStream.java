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

import java.util.Map;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * This type is typically never instantiated by user code but only leveraged by generated SerializationFactory's to support the
 * deserialization of instances.
 * @author Miroslav Pokorny
 */
abstract public class ClientObjectInputStream extends ObjectInputStreamImpl implements ObjectInputStream {

	public ClientObjectInputStream() {
		super();
		
		this.setObjects( this.createObjects() );
	}

	public void prepare(final String stream) {
		this.prepare0(stream);
	}

	native private void prepare0(final String stream)/*-{
	 var array = eval( stream );
	 
	 // set the array that contains the string table and values...
	 this.@rocket.serialization.client.ClientObjectInputStream::stream=array;
	 
	 // set index so that it points to the first value...
	 this.@rocket.serialization.client.ClientObjectInputStream::valueIndex=array[ 0 ] + 1;
	 }-*/;

	/**
	 * This JSO holds a javascript array that contains the result of evaling the given stream.
	 */
	private JavaScriptObject stream;

	/**
	 * An index into the stream that is used fetch the next token to be consumed.
	 */
	private int valueIndex;

	native public boolean readBoolean() /*-{
	 return this.@rocket.serialization.client.ClientObjectInputStream::stream[ this.@rocket.serialization.client.ClientObjectInputStream::valueIndex++ ];
	 }-*/;

	native public byte readByte() /*-{
	 return this.@rocket.serialization.client.ClientObjectInputStream::stream[ this.@rocket.serialization.client.ClientObjectInputStream::valueIndex++ ];
	 }-*/;

	native public short readShort() /*-{
	 return this.@rocket.serialization.client.ClientObjectInputStream::stream[ this.@rocket.serialization.client.ClientObjectInputStream::valueIndex++ ];
	 }-*/;

	native public int readInt() /*-{
	 return this.@rocket.serialization.client.ClientObjectInputStream::stream[ this.@rocket.serialization.client.ClientObjectInputStream::valueIndex++ ];
	 }-*/;

	native public long readLong() /*-{
	 return this.@rocket.serialization.client.ClientObjectInputStream::stream[ this.@rocket.serialization.client.ClientObjectInputStream::valueIndex++ ];
	 }-*/;

	native public float readFloat() /*-{
	 return this.@rocket.serialization.client.ClientObjectInputStream::stream[ this.@rocket.serialization.client.ClientObjectInputStream::valueIndex++ ];
	 }-*/;

	native public double readDouble() /*-{
	 return this.@rocket.serialization.client.ClientObjectInputStream::stream[ this.@rocket.serialization.client.ClientObjectInputStream::valueIndex++ ];
	 }-*/;

	public char readChar() {
		return (char) this.readInt();
	}

	protected native String getString(final int reference)/*-{
 	 var index = reference - @rocket.serialization.client.Constants::STRING_BIAS + 1;
	 var string = this.@rocket.serialization.client.ClientObjectInputStream::stream[ index ];
	 if( ! string ){
	 	this.@rocket.serialization.client.ObjectInputStreamImpl::throwInvalidStringReference(I)(reference);
	 }
	 
	 return string;
	 }-*/;
	
	native public int addObject( final Object object )/*-{
		var objects = this.@rocket.serialization.client.ClientObjectInputStream::getObjects()();
		var index = objects.length;
		objects[ index ] = object; 
		return - index - 1;

	}-*/;
	public void replaceObject( final int reference, final Object object ){
		this.replaceObject0( reference, object );
	}
	
	native private void replaceObject0( final int reference, final Object object )/*-{
  		var objects = this.@rocket.serialization.client.ClientObjectInputStream::getObjects()();
  		var index = - reference - 1;
 		objects[index] = object;
	}-*/;
	
	native protected Object getObject( final int reference )/*-{
		var index = - reference - 1;
		var objects = this.@rocket.serialization.client.ClientObjectInputStream::getObjects()();
		
		var object = objects[ index ];		
		if( null == object ){
			this.@rocket.serialization.client.ObjectInputStreamImpl::throwInvalidObjectReference(I)(reference);
		}
		
		return object;
	}-*/;
	
	private JavaScriptObject objects;
	
	protected JavaScriptObject getObjects(){
		return this.objects;
	}
	protected void setObjects( final JavaScriptObject objects ){
		this.objects = objects;
	}
	
	native protected JavaScriptObject createObjects()/*-{
		return [];
	}-*/;
	 
	protected Object readNewObject0(final String typeName) {
		final ObjectReader reader = this.getObjectReader(typeName);
		if (null == reader) {
			throwUnableToDeserialize( typeName );
		}
		final Object instance = reader.newInstance(typeName, this);
		this.addObject(instance);
		reader.read(instance, this);
		return instance;
	}

	/**
	 * This method is implemented by the SerializationFactoryGenerator.
	 * @param typeName
	 * @return
	 */
	abstract protected ObjectReader getObjectReader( String typeName );
}
