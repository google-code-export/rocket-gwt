package rocket.widget.rebind.htmltemplatefactory;

import rocket.generator.rebind.GeneratorException;

/**
 * This exception is thrown whenever the html template generator fails.
 * 
 * @author Miroslav Pokorny
 */
public class HtmlTemplateGeneratorException extends GeneratorException {

	public HtmlTemplateGeneratorException() {
	}

	public HtmlTemplateGeneratorException(String message) {
		super(message);
	}

	public HtmlTemplateGeneratorException(Throwable cause) {
		super(cause);
	}

	public HtmlTemplateGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}
}
