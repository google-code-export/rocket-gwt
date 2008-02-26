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
package rocket.serialization.rebind;

import rocket.generator.rebind.type.Type;

abstract public class ObjectWriterFinder extends ObjectReaderOrWriterFinder {

	protected void throwAmbiguousMatches(Type type, Type first, Type second) {
		throw new SerializationFactoryGeneratorException("Ambiguous match to more than one ObjectWriter for the type " + type + ", first: " + first + ", second: " + second );
	}
}