/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.server.rpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

import rocket.client.util.PrimitiveHelper;
import rocket.server.util.ObjectHelper;

/**
 * This ServletOutputStream supports capturing any bytes written to it. These may be retrieved later using toByteArray();
 *
 * @author Miroslav Pokorny (mP)
 */
public class ByteArrayServletOutputStream extends ServletOutputStream {

	public ByteArrayServletOutputStream(final int bufferSize) {
		this.setBufferSize(bufferSize);
		this.createByteArrayOutputStream();
	}

	private int bufferSize;

	protected int getBufferSize() {
		PrimitiveHelper.checkGreaterThan("field:bufferSize", this.bufferSize, 0);
		return this.bufferSize;
	}

	protected void setBufferSize(final int bufferSize) {
		PrimitiveHelper.checkGreaterThan("parameter:bufferSize", bufferSize, 0);
		this.bufferSize = bufferSize;
	}

	private final String NULL = "null";

	public void print( final String string ) throws IOException{
		final String write = string == null ? NULL : string;
		this.getByteArrayOutputStream().write( write.getBytes() );
	}

	public void print( final boolean b ) throws IOException{
		this.print( String.valueOf( b ));
	}

	public void print( final char c )throws IOException{
		this.print( String.valueOf( c ));
	}

	public void print( final int intValue )throws IOException{
		this.print( String.valueOf( intValue ));
	}

	public void print( final long longValue )throws IOException{
		this.print( String.valueOf( longValue ));
	}

	public void print( final float floatValue )throws IOException{
		this.print( String.valueOf( floatValue ));
	}
	public void print( final double doubleValue )throws IOException{
		this.print( String.valueOf( doubleValue ));
	}

	static final String EOL = System.getProperty( "line.separator");

	public void println()throws IOException{
		this.print( EOL );
	}

	public void println( final String string )throws IOException{
		this.print( string );
		this.println();
	}

	public void println( final boolean b ) throws IOException{
		this.println( String.valueOf( b ));
	}

	public void println( final char c )throws IOException{
		this.println( String.valueOf( c ));
	}

	public void println( final int intValue )throws IOException{
		this.println( String.valueOf( intValue ));
	}

	public void println( final long longValue )throws IOException{
		this.println( String.valueOf( longValue ));
	}

	public void println( final float floatValue )throws IOException{
		this.println( String.valueOf( floatValue ));
	}
	public void println( final double doubleValue )throws IOException{
		this.println( String.valueOf( doubleValue ));
	}

	public void write(final int intValue)throws IOException {
		this.getByteArrayOutputStream().write(intValue);
	}

	public void write( final byte[] bytes )throws IOException{
		ObjectHelper.checkNotNull( "parameter:bytes", bytes );
		this.write( bytes, 0, bytes.length );
	}

	public void write( final byte[] bytes, final int offset, final int length )throws IOException{
		ObjectHelper.checkNotNull( "parameter:bytes", bytes );
		final int bytesCount = bytes.length;
		PrimitiveHelper.checkBetween( "parameter:offset", offset, 0,bytesCount );
		PrimitiveHelper.checkBetween( "parameter:length", length, offset, bytesCount - offset + 1);

		this.getByteArrayOutputStream().write( bytes, offset, length );
	}

	public void reset() {
		if (this.isCommitted()) {
			throw new IllegalStateException("Unable to reset because buffer has already been committed.");
		}
		this.getByteArrayOutputStream().reset();
	}

	public void flush() throws IOException {
		this.setCommitted(true);
		this.getByteArrayOutputStream().flush();
	}

	private boolean committed = false;

	public boolean isCommitted() {
		return committed || this.getByteArrayOutputStream().size() > this.getBufferSize();
	}

	protected void setCommitted(final boolean committed) {
		this.committed = committed;
	}

	public byte[] toByteArray() {
		return this.getByteArrayOutputStream().toByteArray();
	}

	/**
	 * The byte array that is written too. When either flush or the size of the buffer exceeds bufferSize it is deemed to have been
	 * committed.
	 */
	private ByteArrayOutputStream byteArrayOutputStream;

	protected ByteArrayOutputStream getByteArrayOutputStream() {
		ObjectHelper.checkNotNull("field:byteArrayOutputStream", byteArrayOutputStream);
		return this.byteArrayOutputStream;
	}

	protected void setByteArrayOutputStream(final ByteArrayOutputStream byteArrayOutputStream) {
		ObjectHelper.checkNotNull("parameter:byteArrayOutputStream", byteArrayOutputStream);
		this.byteArrayOutputStream = byteArrayOutputStream;
	}

	protected void createByteArrayOutputStream(){
		final ByteArrayOutputStream baos = new ByteArrayOutputStream( this.getBufferSize() );
		this.setByteArrayOutputStream(baos);
	}

	public String toString() {
		return super.toString() + ", byteArrayOutputStream: " + byteArrayOutputStream + ", committed: " + committed + ", bufferSize: "
				+ bufferSize;
	}
}
