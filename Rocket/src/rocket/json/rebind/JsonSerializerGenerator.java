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
package rocket.json.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * This code generator is responsible for generating stubs that can and do
 * serialize to and from json any serializable java object.
 * 
 * @author Miroslav Pokorny
 */
public class JsonSerializerGenerator extends Generator {

	/**
	 * Begins the code generation process that will eventually guarantee that a
	 * JsonSerializer exists for the given typeName.
	 * 
	 * @see com.google.gwt.core.ext.Generator#generate(com.google.gwt.core.ext.TreeLogger,
	 *      com.google.gwt.core.ext.GeneratorContext, java.lang.String)
	 */
	public String generate(final TreeLogger logger, final com.google.gwt.core.ext.GeneratorContext generatorContext, final String typeName)
			throws UnableToCompleteException {
		logger.log(TreeLogger.INFO, "generating stub for " + typeName, null);

		final JsonSerializerGeneratorContext context = new JsonSerializerGeneratorContext();
		context.setGeneratorContext(generatorContext);
		context.setLogger(logger);

		try {
			final TypeSerializerGenerator generator = new TypeSerializerGenerator();
			generator.setJsonSerializerGeneratorContext(context);
			generator.setType((JClassType) context.getType(typeName));
			generator.generate();
			final String generatedClassName = generator.getGeneratedSerializerClassname();
			logger.log(TreeLogger.INFO, "returning JsonSerializer [" + generatedClassName + "] for type " + typeName + "...", null);
			return generatedClassName;

		} catch (final JsonSerializerGeneratorException rethrow) {
			logger.log(TreeLogger.ERROR, rethrow.getMessage(), rethrow);
			throw rethrow;
		} catch (final Throwable caught) {
			logger.log(TreeLogger.ERROR, caught.getMessage(), caught);
			throw new UnableToCompleteException();
		}
	}
}
