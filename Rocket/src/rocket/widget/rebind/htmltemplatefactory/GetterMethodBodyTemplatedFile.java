package rocket.widget.rebind.htmltemplatefactory;

import java.io.InputStream;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;

abstract public class GetterMethodBodyTemplatedFile extends TemplatedCodeBlock {

	protected InputStream getInputStream() {
		final String fileName = this.getTemplateFilename();
		final Generator generator = this.getGeneratorContext().getGenerator();
		return generator.getResource(generator.getResourceNameFromGeneratorPackage(fileName));
	}

	abstract protected GeneratorContext getGeneratorContext();

	protected Object getValue0(final String name) {
		Object value = null;

		if (Constants.ID.equals(name)) {
			value = new StringLiteral(this.getId());
		}
		return value;
	}

	abstract protected String getTemplateFilename();

	abstract protected String getId();
}
