/**
 * 
 */
package rocket.json.rebind;

import rocket.generator.rebind.GeneratorException;

/**
 * Common base class for all exceptions that occur whilst generating a
 * Serializer.
 * 
 * @author Miroslav Pokorny
 * 
 */
public class JsonSerializerGeneratorException extends GeneratorException {

	/**
	 * 
	 */
	JsonSerializerGeneratorException() {
	}

	/**
	 * @param message
	 */
	JsonSerializerGeneratorException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	JsonSerializerGeneratorException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	JsonSerializerGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}
}
