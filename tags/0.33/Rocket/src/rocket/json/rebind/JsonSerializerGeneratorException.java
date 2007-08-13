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
	public JsonSerializerGeneratorException() {
	}

	/**
	 * @param message
	 */
	public JsonSerializerGeneratorException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public JsonSerializerGeneratorException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JsonSerializerGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

}
