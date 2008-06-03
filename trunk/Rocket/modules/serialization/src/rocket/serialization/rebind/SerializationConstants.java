/*
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
package rocket.serialization.rebind;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.serialization.client.ClientObjectInputStream;
import rocket.serialization.client.ClientObjectOutputStream;
import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.ObjectReader;
import rocket.serialization.client.ObjectWriter;
import rocket.serialization.client.SerializationFactory;
import rocket.serialization.client.reader.ObjectArrayReader;
import rocket.serialization.client.reader.ObjectReaderImpl;
import rocket.serialization.client.writer.ArrayWriter;
import rocket.serialization.client.writer.ObjectWriterImpl;

public class SerializationConstants {

	private final static String ANNOTATION_PREFIX = "serialization";

	public final static String SERIALIZABLE_READABLE_TYPES = ANNOTATION_PREFIX + "-readableTypes";
	public final static String SERIALIZABLE_WRITABLE_TYPES = ANNOTATION_PREFIX + "-writableTypes";
	public final static String SERIALIZABLE_TYPE = ANNOTATION_PREFIX + "-type";

	public final static String CONTAINER_TYPE = ANNOTATION_PREFIX + "-type";
	final static String LIST = List.class.getName();
	final static String SET = Set.class.getName();
	final static String MAP = Map.class.getName();

	final static String SERIALIZATION_FACTORY_GENERATED_TYPE_SUFFIX = "__SerializationFactory";

	final static String SERIALIZATION_FACTORY = SerializationFactory.class.getName();

	final static String SERIALIZABLE = Serializable.class.getName();

	final static String OBJECT_READER = ObjectReader.class.getName();
	final static String ARRAY_READER = ObjectArrayReader.class.getName();

	final static String OBJECT_READER_IMPL = ObjectReaderImpl.class.getName();

	final static String OBJECT_READER_GENERATED_TYPE_SUFFIX = "__ObjectReader";

	final static String CLIENT_OBJECT_INPUT_STREAM = ClientObjectInputStream.class.getName();

	final static String CLIENT_OBJECT_READER_IMPL = ObjectReaderImpl.class.getName();

	final static String CLIENT_OBJECT_READER_IMPL_NEW_INSTANCE_METHOD = "newInstance";

	final static String CLIENT_OBJECT_READER_IMPL_READ_METHOD = "read";

	final static String CLIENT_OBJECT_READER_IMPL_READ_INSTANCE_PARAMETER = "instance";

	final static String CLIENT_OBJECT_READER_IMPL_READ_OBJECT_INPUT_STREAM_PARAMETER = "objectInputStream";

	final static String CLIENT_OBJECT_READER_IMPL_READ_FIELDS_METHOD = "readFields";

	final static String CLIENT_OBJECT_READER_IMPL_READ_FIELDS_INSTANCE_PARAMETER = "instance";

	final static String CLIENT_OBJECT_READER_IMPL_READ_FIELDS_OBJECT_INPUT_STREAM_PARAMETER = "objectInputStream";

	final static String CLIENT_OBJECT_READER_IMPL_FIELD_SETTER_INSTANCE_PARAMETER = "instance";

	final static String CLIENT_OBJECT_READER_IMPL_FIELD_SETTER_VALUE_PARAMETER = "value";

	final static String OBJECT_INPUT_STREAM = ObjectInputStream.class.getName();

	final static String OBJECT_WRITER = ObjectWriter.class.getName();

	final static String OBJECT_WRITER_IMPL = ObjectWriterImpl.class.getName();

	final static String OBJECT_WRITER_GENERATED_TYPE_SUFFIX = "__ObjectWriter";

	final static String CLIENT_OBJECT_OUTPUT_STREAM = ClientObjectOutputStream.class.getName();

	final static String CLIENT_OBJECT_WRITER_IMPL = ObjectWriterImpl.class.getName();

	final static String CLIENT_OBJECT_WRITER_IMPL_WRITE0_METHOD = "write0";

	final static String CLIENT_OBJECT_WRITER_IMPL_WRITE0_INSTANCE_PARAMETER = "instance";

	final static String CLIENT_OBJECT_WRITER_IMPL_WRITE0_OBJECT_OUTPUT_STREAM_PARAMETER = "objectOutputStream";

	final static String CLIENT_OBJECT_WRITER_IMPL_WRITE_FIELDS_METHOD = "writeFields";

	final static String CLIENT_OBJECT_WRITER_IMPL_WRITE_FIELDS_INSTANCE_PARAMETER = "instance";

	final static String CLIENT_OBJECT_WRITER_IMPL_WRITE_FIELDS_OBJECT_OUTPUT_STREAM_PARAMETER = "objectOutputStream";

	final static String CLIENT_OBJECT_WRITER_IMPL_FIELD_GETTER_INSTANCE_PARAMETER = "instance";

	final static String OBJECT_OUTPUT_STREAM = ObjectOutputStream.class.getName();

	final static String SINGLETON = "instance";

	final static String SERIALIZATION_FACTORY_GET_OBJECT_READER = "getObjectReader";
	final static String SERIALIZATION_FACTORY_GET_OBJECT_WRITER = "getObjectWriter";

	public final static String BLACKLIST_FILENAME = "rocket-Serialization.txt";

	final static String ARRAY_WRITER = ArrayWriter.class.getName();

	final static String JAVA_LANG = "java.lang";
}
