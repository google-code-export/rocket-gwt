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
package rocket.serialization.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import rocket.serialization.test.client.reader.BooleanArrayReaderGwtTestCase;
import rocket.serialization.test.client.reader.BooleanReaderGwtTestCase;
import rocket.serialization.test.client.reader.ByteArrayReaderGwtTestCase;
import rocket.serialization.test.client.reader.ByteReaderGwtTestCase;
import rocket.serialization.test.client.reader.CharArrayReaderGwtTestCase;
import rocket.serialization.test.client.reader.CharacterReaderGwtTestCase;
import rocket.serialization.test.client.reader.DateReaderGwtTestCase;
import rocket.serialization.test.client.reader.DoubleArrayReaderGwtTestCase;
import rocket.serialization.test.client.reader.DoubleReaderGwtTestCase;
import rocket.serialization.test.client.reader.FloatArrayReaderGwtTestCase;
import rocket.serialization.test.client.reader.FloatReaderGwtTestCase;
import rocket.serialization.test.client.reader.IntArrayReaderGwtTestCase;
import rocket.serialization.test.client.reader.IntegerReaderGwtTestCase;
import rocket.serialization.test.client.reader.ListReaderGwtTestCase;
import rocket.serialization.test.client.reader.LongArrayReaderGwtTestCase;
import rocket.serialization.test.client.reader.LongReaderGwtTestCase;
import rocket.serialization.test.client.reader.MapReaderGwtTestCase;
import rocket.serialization.test.client.reader.SetReaderGwtTestCase;
import rocket.serialization.test.client.reader.ShortArrayReaderGwtTestCase;
import rocket.serialization.test.client.reader.ShortReaderGwtTestCase;
import rocket.serialization.test.client.reader.ThrowableReaderGwtTestCase;
import rocket.serialization.test.client.writer.BooleanArrayWriterGwtTestCase;
import rocket.serialization.test.client.writer.BooleanWriterGwtTestCase;
import rocket.serialization.test.client.writer.ByteArrayWriterGwtTestCase;
import rocket.serialization.test.client.writer.ByteWriterGwtTestCase;
import rocket.serialization.test.client.writer.CharArrayWriterGwtTestCase;
import rocket.serialization.test.client.writer.CharacterWriterGwtTestCase;
import rocket.serialization.test.client.writer.DateWriterGwtTestCase;
import rocket.serialization.test.client.writer.DoubleArrayWriterGwtTestCase;
import rocket.serialization.test.client.writer.DoubleWriterGwtTestCase;
import rocket.serialization.test.client.writer.FloatArrayWriterGwtTestCase;
import rocket.serialization.test.client.writer.FloatWriterGwtTestCase;
import rocket.serialization.test.client.writer.IntArrayWriterGwtTestCase;
import rocket.serialization.test.client.writer.IntegerWriterGwtTestCase;
import rocket.serialization.test.client.writer.ListWriterGwtTestCase;
import rocket.serialization.test.client.writer.LongArrayWriterGwtTestCase;
import rocket.serialization.test.client.writer.LongWriterGwtTestCase;
import rocket.serialization.test.client.writer.MapWriterGwtTestCase;
import rocket.serialization.test.client.writer.SetWriterGwtTestCase;
import rocket.serialization.test.client.writer.ShortArrayWriterGwtTestCase;
import rocket.serialization.test.client.writer.ShortWriterGwtTestCase;
import rocket.serialization.test.client.writer.ThrowableWriterGwtTestCase;
import rocket.serialization.test.clientobjectinputstream.client.ClientObjectInputStreamGwtTestCase;
import rocket.serialization.test.clientobjectoutputstream.client.ClientObjectOutputStreamGwtTestCase;
import rocket.serialization.test.rebind.objectreaderorwriterfinder.ObjectReaderOrWriterFinderTestCase;
import rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase;
import rocket.serialization.test.rebind.typematcher.TypeMatcherTestCase;
import rocket.serialization.test.server.ServerObjectInputStreamTestCase;
import rocket.serialization.test.server.ServerObjectOutputStreamTestCase;
import rocket.serialization.test.server.reader.BooleanArrayReaderTestCase;
import rocket.serialization.test.server.reader.BooleanReaderTestCase;
import rocket.serialization.test.server.reader.ByteArrayReaderTestCase;
import rocket.serialization.test.server.reader.ByteReaderTestCase;
import rocket.serialization.test.server.reader.CharArrayReaderTestCase;
import rocket.serialization.test.server.reader.CharacterReaderTestCase;
import rocket.serialization.test.server.reader.DateReaderTestCase;
import rocket.serialization.test.server.reader.DoubleArrayReaderTestCase;
import rocket.serialization.test.server.reader.DoubleReaderTestCase;
import rocket.serialization.test.server.reader.FloatArrayReaderTestCase;
import rocket.serialization.test.server.reader.FloatReaderTestCase;
import rocket.serialization.test.server.reader.IntArrayReaderTestCase;
import rocket.serialization.test.server.reader.IntegerReaderTestCase;
import rocket.serialization.test.server.reader.ListReaderTestCase;
import rocket.serialization.test.server.reader.LongArrayReaderTestCase;
import rocket.serialization.test.server.reader.LongReaderTestCase;
import rocket.serialization.test.server.reader.MapReaderTestCase;
import rocket.serialization.test.server.reader.SetReaderTestCase;
import rocket.serialization.test.server.reader.ShortArrayReaderTestCase;
import rocket.serialization.test.server.reader.ShortReaderTestCase;
import rocket.serialization.test.server.writer.BooleanArrayWriterTestCase;
import rocket.serialization.test.server.writer.BooleanWriterTestCase;
import rocket.serialization.test.server.writer.ByteArrayWriterTestCase;
import rocket.serialization.test.server.writer.ByteWriterTestCase;
import rocket.serialization.test.server.writer.CharArrayWriterTestCase;
import rocket.serialization.test.server.writer.CharacterWriterTestCase;
import rocket.serialization.test.server.writer.DateWriterTestCase;
import rocket.serialization.test.server.writer.DoubleArrayWriterTestCase;
import rocket.serialization.test.server.writer.DoubleWriterTestCase;
import rocket.serialization.test.server.writer.FloatArrayWriterTestCase;
import rocket.serialization.test.server.writer.FloatWriterTestCase;
import rocket.serialization.test.server.writer.IntArrayWriterTestCase;
import rocket.serialization.test.server.writer.IntegerWriterTestCase;
import rocket.serialization.test.server.writer.ListWriterTestCase;
import rocket.serialization.test.server.writer.LongArrayWriterTestCase;
import rocket.serialization.test.server.writer.LongWriterTestCase;
import rocket.serialization.test.server.writer.MapWriterTestCase;
import rocket.serialization.test.server.writer.SetWriterTestCase;
import rocket.serialization.test.server.writer.ShortArrayWriterTestCase;
import rocket.serialization.test.server.writer.ShortWriterTestCase;

/**
 * TestSuite that executes all unit tests relating to the rocket.Serialization module
 * 
 * @author Miroslav Pokorny
 */
public class SerializationTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestSuite for rocket.Serialization");
		addTests( suite );
		return suite;
	}


	public static void addTests(TestSuite suite) {
		addClientReaderTests(suite);
		addClientWriterTests(suite);
		addRebindTests(suite);
		addServerTests(suite);
		addServerReaderTests(suite);
		addServerWriterTests(suite);
	}
	
	static void addClientReaderTests(TestSuite suite) {
		suite.addTestSuite(ClientObjectInputStreamGwtTestCase.class);
		suite.addTestSuite(ClientObjectOutputStreamGwtTestCase.class);
		
		suite.addTestSuite(ListReaderGwtTestCase.class);
		suite.addTestSuite(SetReaderGwtTestCase.class);
		suite.addTestSuite(MapReaderGwtTestCase.class);
		suite.addTestSuite(DateReaderGwtTestCase.class);

		suite.addTestSuite(ThrowableReaderGwtTestCase.class);
		
		suite.addTestSuite(BooleanReaderGwtTestCase.class);
		suite.addTestSuite(ByteReaderGwtTestCase.class);
		suite.addTestSuite(ShortReaderGwtTestCase.class);
		suite.addTestSuite(IntegerReaderGwtTestCase.class);
		suite.addTestSuite(LongReaderGwtTestCase.class);
		suite.addTestSuite(FloatReaderGwtTestCase.class);
		suite.addTestSuite(DoubleReaderGwtTestCase.class);
		suite.addTestSuite(CharacterReaderGwtTestCase.class);

		suite.addTestSuite(BooleanArrayReaderGwtTestCase.class);
		suite.addTestSuite(ByteArrayReaderGwtTestCase.class);
		suite.addTestSuite(ShortArrayReaderGwtTestCase.class);
		suite.addTestSuite(IntArrayReaderGwtTestCase.class);
		suite.addTestSuite(LongArrayReaderGwtTestCase.class);
		suite.addTestSuite(FloatArrayReaderGwtTestCase.class);
		suite.addTestSuite(DoubleArrayReaderGwtTestCase.class);
		suite.addTestSuite(CharArrayReaderGwtTestCase.class);
	}

	static void addClientWriterTests(TestSuite suite) {
		suite.addTestSuite(ListWriterGwtTestCase.class);
		suite.addTestSuite(SetWriterGwtTestCase.class);
		suite.addTestSuite(MapWriterGwtTestCase.class);

		suite.addTestSuite(DateWriterGwtTestCase.class);

		suite.addTestSuite(ThrowableWriterGwtTestCase.class);
		
		suite.addTestSuite(BooleanWriterGwtTestCase.class);
		suite.addTestSuite(ByteWriterGwtTestCase.class);
		suite.addTestSuite(ShortWriterGwtTestCase.class);
		suite.addTestSuite(IntegerWriterGwtTestCase.class);
		suite.addTestSuite(LongWriterGwtTestCase.class);
		suite.addTestSuite(FloatWriterGwtTestCase.class);
		suite.addTestSuite(DoubleWriterGwtTestCase.class);
		suite.addTestSuite(CharacterWriterGwtTestCase.class);

		suite.addTestSuite(BooleanArrayWriterGwtTestCase.class);
		suite.addTestSuite(ByteArrayWriterGwtTestCase.class);
		suite.addTestSuite(ShortArrayWriterGwtTestCase.class);
		suite.addTestSuite(IntArrayWriterGwtTestCase.class);
		suite.addTestSuite(LongArrayWriterGwtTestCase.class);
		suite.addTestSuite(FloatArrayWriterGwtTestCase.class);
		suite.addTestSuite(DoubleArrayWriterGwtTestCase.class);
		suite.addTestSuite(CharArrayWriterGwtTestCase.class);
	}

	static void addRebindTests(TestSuite suite) {
		suite.addTestSuite(ObjectReaderOrWriterFinderTestCase.class);
		suite.addTestSuite(SerializationFactoryGeneratorGwtTestCase.class);
		suite.addTestSuite(TypeMatcherTestCase.class);
	}

	static void addServerReaderTests(TestSuite suite) {
		suite.addTestSuite(BooleanReaderTestCase.class);
		suite.addTestSuite(ByteReaderTestCase.class);
		suite.addTestSuite(ShortReaderTestCase.class);
		suite.addTestSuite(IntegerReaderTestCase.class);
		suite.addTestSuite(LongReaderTestCase.class);
		suite.addTestSuite(FloatReaderTestCase.class);
		suite.addTestSuite(DoubleReaderTestCase.class);
		suite.addTestSuite(CharacterReaderTestCase.class);

		suite.addTestSuite(BooleanArrayReaderTestCase.class);
		suite.addTestSuite(ByteArrayReaderTestCase.class);
		suite.addTestSuite(ShortArrayReaderTestCase.class);
		suite.addTestSuite(IntArrayReaderTestCase.class);
		suite.addTestSuite(LongArrayReaderTestCase.class);
		suite.addTestSuite(FloatArrayReaderTestCase.class);
		suite.addTestSuite(DoubleArrayReaderTestCase.class);
		suite.addTestSuite(CharArrayReaderTestCase.class);

		
		suite.addTestSuite(ListReaderTestCase.class);
		suite.addTestSuite(SetReaderTestCase.class);
		suite.addTestSuite(MapReaderTestCase.class);
		suite.addTestSuite(DateReaderTestCase.class);		
	}

	static void addServerTests(TestSuite suite) {
		suite.addTestSuite(ServerObjectOutputStreamTestCase.class);
		suite.addTestSuite(ServerObjectInputStreamTestCase.class);
	}

	static void addServerWriterTests(TestSuite suite) {
		suite.addTestSuite(ListWriterTestCase.class);
		suite.addTestSuite(SetWriterTestCase.class);
		suite.addTestSuite(MapWriterTestCase.class);
		suite.addTestSuite(DateWriterTestCase.class);
		
		suite.addTestSuite(BooleanWriterTestCase.class);
		suite.addTestSuite(ByteWriterTestCase.class);
		suite.addTestSuite(ShortWriterTestCase.class);
		suite.addTestSuite(IntegerWriterTestCase.class);
		suite.addTestSuite(LongWriterTestCase.class);
		suite.addTestSuite(FloatWriterTestCase.class);
		suite.addTestSuite(DoubleWriterTestCase.class);
		suite.addTestSuite(CharacterWriterTestCase.class);

		suite.addTestSuite(BooleanArrayWriterTestCase.class);
		suite.addTestSuite(ByteArrayWriterTestCase.class);
		suite.addTestSuite(ShortArrayWriterTestCase.class);
		suite.addTestSuite(IntArrayWriterTestCase.class);
		suite.addTestSuite(LongArrayWriterTestCase.class);
		suite.addTestSuite(FloatArrayWriterTestCase.class);
		suite.addTestSuite(DoubleArrayWriterTestCase.class);
		suite.addTestSuite(CharArrayWriterTestCase.class);
	}
}
