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
package rocket.logging.rebind;

import rocket.generator.rebind.GeneratorException;

/**
 * This exception is thrown whenever something goes wrong during the
 * LoggerFactory generation process.
 * 
 * @author Miroslav Pokorny
 */
public class LoggerFactoryGeneratorException extends GeneratorException {

	LoggerFactoryGeneratorException() {
		super();
	}

	LoggerFactoryGeneratorException(String message) {
		super(message);
	}

	LoggerFactoryGeneratorException(Throwable cause) {
		super(cause);
	}

	LoggerFactoryGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}
}
