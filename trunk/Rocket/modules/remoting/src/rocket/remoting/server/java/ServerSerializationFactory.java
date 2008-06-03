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
package rocket.remoting.server.java;

import java.util.ArrayList;
import java.util.List;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.server.ServerObjectInputStream;
import rocket.serialization.server.ServerObjectOutputStream;
import rocket.serialization.server.ServerObjectReader;
import rocket.serialization.server.ServerObjectWriter;
import rocket.serialization.server.reader.BooleanArrayReader;
import rocket.serialization.server.reader.BooleanReader;
import rocket.serialization.server.reader.ByteArrayReader;
import rocket.serialization.server.reader.ByteReader;
import rocket.serialization.server.reader.CharArrayReader;
import rocket.serialization.server.reader.CharacterReader;
import rocket.serialization.server.reader.DateReader;
import rocket.serialization.server.reader.DoubleArrayReader;
import rocket.serialization.server.reader.DoubleReader;
import rocket.serialization.server.reader.FloatArrayReader;
import rocket.serialization.server.reader.FloatReader;
import rocket.serialization.server.reader.IntArrayReader;
import rocket.serialization.server.reader.IntegerReader;
import rocket.serialization.server.reader.ListReader;
import rocket.serialization.server.reader.LongArrayReader;
import rocket.serialization.server.reader.LongReader;
import rocket.serialization.server.reader.MapReader;
import rocket.serialization.server.reader.ReflectiveReader;
import rocket.serialization.server.reader.SetReader;
import rocket.serialization.server.reader.ShortArrayReader;
import rocket.serialization.server.reader.ShortReader;
import rocket.serialization.server.reader.ThrowableReader;
import rocket.serialization.server.reader.TreeMapReader;
import rocket.serialization.server.reader.TreeSetReader;
import rocket.serialization.server.writer.BooleanArrayWriter;
import rocket.serialization.server.writer.BooleanWriter;
import rocket.serialization.server.writer.ByteArrayWriter;
import rocket.serialization.server.writer.ByteWriter;
import rocket.serialization.server.writer.CgLibEnhancedWriter;
import rocket.serialization.server.writer.CharArrayWriter;
import rocket.serialization.server.writer.CharacterWriter;
import rocket.serialization.server.writer.DateWriter;
import rocket.serialization.server.writer.DoubleArrayWriter;
import rocket.serialization.server.writer.DoubleWriter;
import rocket.serialization.server.writer.FloatArrayWriter;
import rocket.serialization.server.writer.FloatWriter;
import rocket.serialization.server.writer.IntArrayWriter;
import rocket.serialization.server.writer.IntegerWriter;
import rocket.serialization.server.writer.ListWriter;
import rocket.serialization.server.writer.LongArrayWriter;
import rocket.serialization.server.writer.LongWriter;
import rocket.serialization.server.writer.MapWriter;
import rocket.serialization.server.writer.ReflectiveWriter;
import rocket.serialization.server.writer.SetWriter;
import rocket.serialization.server.writer.ShortArrayWriter;
import rocket.serialization.server.writer.ShortWriter;
import rocket.serialization.server.writer.ThrowableWriter;
import rocket.serialization.server.writer.TreeMapWriter;
import rocket.serialization.server.writer.TreeSetWriter;

/**
 * Factory method which provides the capability to customise how incoming and
 * outgoing objects are serialized.
 * 
 * To add support for serializing Hibernate enhanced types simply over
 * {@link #addDateObjectWriter(List)} to also include a
 * {@link CgLibEnhancedWriter}.
 * 
 * @author Miroslav Pokorny
 */
public class ServerSerializationFactory {

	public ServerSerializationFactory() {
		super();
	}

	/**
	 * Factory method which ultimately delivers a ObjectInputStream which may be
	 * used to deserialize incoming streams of objects from a Gwt client.
	 * 
	 * @param stream
	 * @return
	 */
	public ObjectInputStream createObjectInputStream(final String stream) {
		final ServerObjectInputStream inputStream = new ServerObjectInputStream(stream);
		inputStream.setObjectReaders(this.createObjectReaders());
		return inputStream;
	}

	/**
	 * Factory method which creates a list containing all ObjectReaders.
	 * 
	 * @return A list of ObjectReaders
	 */
	protected List<ServerObjectReader> createObjectReaders() {
		final List<ServerObjectReader> readers = new ArrayList<ServerObjectReader>();
		this.addCollectionObjectReaders(readers);
		this.addPrimitiveWrapperObjectReaders(readers);
		this.addPrimitiveArrayObjectReaders(readers);
		this.addDateObjectReader(readers);
		this.addThrowableObjectReader(readers);
		this.addDefaultObjectReader(readers);
		return readers;
	}

	/**
	 * Adds a ObjectReader to handle writing of {@link java.util.Date}.
	 * 
	 * @param readers
	 */
	protected void addDateObjectReader(final List<ServerObjectReader> readers) {
		readers.add(DateReader.instance);
	}

	/**
	 * Adds several ObjectReaders to handle each of the core three collection
	 * interfaces.
	 * 
	 * @param readers
	 */
	protected void addCollectionObjectReaders(final List<ServerObjectReader> readers) {
		readers.add(ListReader.instance);
		readers.add(TreeSetReader.instance);
		readers.add(SetReader.instance);
		readers.add(TreeMapReader.instance);
		readers.add(MapReader.instance);
	}

	/**
	 * Adds several ObjectReaders to handle writing of all the primitive type
	 * wrappers
	 * 
	 * @param readers
	 */
	protected void addPrimitiveWrapperObjectReaders(final List<ServerObjectReader> readers) {
		readers.add(BooleanReader.instance);
		readers.add(ByteReader.instance);
		readers.add(ShortReader.instance);
		readers.add(IntegerReader.instance);
		readers.add(LongReader.instance);
		readers.add(FloatReader.instance);
		readers.add(DoubleReader.instance);
		readers.add(CharacterReader.instance);
	}

	/**
	 * Adds several ObjectReaders to handle writing of all primitive array
	 * instances.
	 * 
	 * @param readers
	 */
	protected void addPrimitiveArrayObjectReaders(final List<ServerObjectReader> readers) {
		readers.add(BooleanArrayReader.instance);
		readers.add(ByteArrayReader.instance);
		readers.add(ShortArrayReader.instance);
		readers.add(IntArrayReader.instance);
		readers.add(LongArrayReader.instance);
		readers.add(FloatArrayReader.instance);
		readers.add(DoubleArrayReader.instance);
		readers.add(CharArrayReader.instance);
	}

	protected void addThrowableObjectReader(final List<ServerObjectReader> readers) {
		readers.add(ThrowableReader.instance);
	}

	protected void addDefaultObjectReader(final List<ServerObjectReader> readers) {
		readers.add(ReflectiveReader.instance);
	}

	/**
	 * Factory method which creates a ObjectOutputStream which may be used to
	 * serialize objects to a GWT client.
	 * 
	 * @return
	 */
	public ObjectOutputStream createObjectOutputStream() {
		final ServerObjectOutputStream outputStream = new ServerObjectOutputStream();
		outputStream.setObjectWriters(this.createObjectWriters());
		return outputStream;
	}

	/**
	 * Factory method which creates a list containing all ObjectWriters.
	 * 
	 * @return
	 */
	protected List<ServerObjectWriter> createObjectWriters() {
		final List<ServerObjectWriter> writers = new ArrayList<ServerObjectWriter>();
		this.addCollectionObjectWriters(writers);
		this.addPrimitiveWrapperObjectWriters(writers);
		this.addPrimitiveArrayObjectWriters(writers);
		this.addDateObjectWriter(writers);
		this.addThrowablesObjectWriter(writers);
		this.addDefaultObjectWriter(writers);
		return writers;
	}

	protected void addThrowablesObjectWriter(final List<ServerObjectWriter> writers) {
		writers.add(ThrowableWriter.instance);
	}

	protected void addDefaultObjectWriter(final List<ServerObjectWriter> writers) {
		writers.add(ReflectiveWriter.instance);
	}

	/**
	 * Adds a ObjectWriter to handle writing of {@link java.util.Date}.
	 * 
	 * @param writers
	 */
	protected void addDateObjectWriter(final List<ServerObjectWriter> writers) {
		writers.add(DateWriter.instance);
	}

	/**
	 * Adds several ObjectWriters to handle each of the core three collection
	 * interfaces.
	 * 
	 * @param writers
	 */
	protected void addCollectionObjectWriters(final List<ServerObjectWriter> writers) {
		writers.add(ListWriter.instance);
		writers.add(TreeSetWriter.instance);
		writers.add(SetWriter.instance);
		writers.add(TreeMapWriter.instance);
		writers.add(MapWriter.instance);
	}

	/**
	 * Adds several ObjectWriters to handle writing of all the primitive type
	 * wrappers
	 * 
	 * @param writers
	 */
	protected void addPrimitiveWrapperObjectWriters(final List<ServerObjectWriter> writers) {
		writers.add(BooleanWriter.instance);
		writers.add(ByteWriter.instance);
		writers.add(ShortWriter.instance);
		writers.add(IntegerWriter.instance);
		writers.add(LongWriter.instance);
		writers.add(FloatWriter.instance);
		writers.add(DoubleWriter.instance);
		writers.add(CharacterWriter.instance);
	}

	/**
	 * Adds several ObjectWriters to handle writing of all primitive array
	 * instances.
	 * 
	 * @param writers
	 */
	protected void addPrimitiveArrayObjectWriters(final List<ServerObjectWriter> writers) {
		writers.add(BooleanArrayWriter.instance);
		writers.add(ByteArrayWriter.instance);
		writers.add(ShortArrayWriter.instance);
		writers.add(IntArrayWriter.instance);
		writers.add(LongArrayWriter.instance);
		writers.add(FloatArrayWriter.instance);
		writers.add(DoubleArrayWriter.instance);
		writers.add(CharArrayWriter.instance);
	}
}
