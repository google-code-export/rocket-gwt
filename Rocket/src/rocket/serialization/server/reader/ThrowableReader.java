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

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.SerializationException;
import rocket.serialization.server.ReflectionHelper;
import rocket.serialization.server.ServerObjectReader;

/**
 * A reader for uses reflection to set fields upon a new instance except for the fields belonging to Throwable.
 * 
 * @author Miroslav Pokorny
 */
public class ThrowableReader extends ReflectiveReader implements ServerObjectReader {

	static public final ServerObjectReader instance = new ThrowableReader();

	protected ThrowableReader() {
		this.cacheFields();
	}
	
	protected void cacheFields(){
		this.setMessage( ReflectionHelper.getThrowableMessageField() );
		this.setCause( ReflectionHelper.getThrowableCauseField() );
		this.setStackTraceElements( ReflectionHelper.getThrowableStackTraceElementField() );
	}

	public boolean canRead(final Class classs) {
		return Serializable.class.isAssignableFrom( classs );
	}


	protected void readFields(final Object object, final Class classs, final ObjectInputStream objectInputStream) {
		if( Throwable.class.equals( classs )){
			this.readThrowableFields( (Throwable)object, objectInputStream );
		} else {
			super.readFields(object, classs, objectInputStream);
		}
	}

	/**
	 * Handles the special case of fields belonging to the Throwable class.
	 * @param throwable The instance being reconsituted.
	 * @param classs
	 * @param objectInputStream
	 */
	protected void readThrowableFields(final Throwable throwable, final ObjectInputStream objectInputStream ){
		try{			
			final String message = (String) objectInputStream.readObject();
			this.getMessage().set( throwable, message );
			
			final Throwable cause = (Throwable) objectInputStream.readObject();
			this.getCause().set( throwable, cause);
			
			final int elementCount = objectInputStream.readInt();
			final StackTraceElement[] stackTraceElements = new StackTraceElement[ elementCount ];
			
			for( int i = 0; i < elementCount; i++ ){
				final String className = (String)objectInputStream.readObject();
				final String methodName = (String)objectInputStream.readObject();
				final String fileName = (String)objectInputStream.readObject();
				final int lineNumber = objectInputStream.readInt();
				
				stackTraceElements[ i ] = new StackTraceElement( className, methodName, fileName, lineNumber );
			}		
		} catch ( final Exception caught ){
			throw new SerializationException( "Unable to serialize Throwable instance, " + caught.getMessage() );
		}
	}
	
	/**
	 * A cached copy of the {@link java.lang.Throwable#detailMessage } field
	 */
	private Field message;
	
	protected Field getMessage(){
		return this.message;
	}
	protected void setMessage( final Field message ){
		this.message = message;
	}

	/**
	 * A cached copy of the {@link java.lang.Throwable#cause } field
	 */
	private Field cause;
	
	protected Field getCause(){
		return this.cause;
	}
	protected void setCause( final Field cause ){
		this.cause = cause;
	}
	
	/**
	 * A cached copy of the {@link java.lang.Throwable#stacktrace } field
	 */
private Field stackTraceElements;
	
	protected Field getStackTraceElements(){
		return this.stackTraceElements;
	}
	protected void setStackTraceElements( final Field stackTrace ){
		this.stackTraceElements = stackTrace;
	}
}
