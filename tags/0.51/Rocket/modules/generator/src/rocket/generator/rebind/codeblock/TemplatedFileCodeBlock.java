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
package rocket.generator.rebind.codeblock;

import java.io.InputStream;

import rocket.generator.rebind.CodeGenerator;

/**
 * An abstract class that loads a template as a resource using the classloader.
 * 
 * @author Miroslav Pokorny
 */
abstract public class TemplatedFileCodeBlock extends TemplatedCodeBlock implements CodeBlock, CodeGenerator {

	public TemplatedFileCodeBlock() {
		super();
	}

	/**
	 * Sub classes must override this to return the fully qualified name of the
	 * resource
	 * 
	 * @return The name.
	 */
	abstract protected String getResourceName();

	/**
	 * Locates a resource taking its name from {@link #getResourceName()}
	 * throwing an exception if the InputStream cannot be found.
	 * 
	 * @return The located InputStream
	 */
	protected InputStream getInputStream() {
		final String resourceName = this.getResourceName();
		final InputStream inputStream = this.getClass().getResourceAsStream(resourceName);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + resourceName + "\".");
		}
		return inputStream;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found in template \"" + this.getResourceName()
				+ "\".");
	}

}
