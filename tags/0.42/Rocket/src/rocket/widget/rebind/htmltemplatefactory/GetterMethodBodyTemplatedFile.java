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
package rocket.widget.rebind.htmltemplatefactory;

import java.io.InputStream;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;

abstract public class GetterMethodBodyTemplatedFile extends TemplatedFileCodeBlock {

	protected InputStream getInputStream() {
		final String fileName = this.getResourceName();
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

	abstract protected String getResourceName();

	abstract protected String getId();
}
