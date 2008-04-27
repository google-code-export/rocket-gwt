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
package rocket.generator.test.generator.rebind;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.gwt.TypeOracleGeneratorContext;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;

import com.google.gwt.core.ext.TreeLogger;

/**
 * Base Generator for all Generators belonging to this package
 * 
 * @author Miroslav Pokorny
 */
abstract public class TestGenerator extends Generator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The hardcoded suffix that gets appended to each generated type
	 * 
	 * @return
	 */
	protected String getGeneratedTypeNameSuffix(){
		return "1";
	}

	
	protected GeneratorContext createGeneratorContext( final com.google.gwt.core.ext.GeneratorContext generatorContext, final TreeLogger logger){
		final TypeOracleGeneratorContext context = new TypeOracleGeneratorContext();
		context.setGenerator( this );
		context.setGeneratorContext( generatorContext );
		context.setLogger( logger );
		
		return context;
	}

}
