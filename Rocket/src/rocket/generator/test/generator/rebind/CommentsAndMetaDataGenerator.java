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

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.EmptyCodeBlock;
import rocket.generator.rebind.constructor.NewConstructor;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.initializer.Initializer;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewNestedInterfaceType;
import rocket.generator.rebind.type.NewNestedType;
import rocket.generator.rebind.type.Type;
import rocket.generator.test.generator.client.NewNestedInterface;

public class CommentsAndMetaDataGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final CodeBlock empty = new CodeBlock(){
			public boolean isEmpty(){
				return false;
			}
			public void write(final SourceWriter writer){
			}
		};
		
		final NewConcreteType newType = context.newConcreteType();
		newType.setName(newTypeName);
		newType.setSuperType(type);

		newType.setComments( "Class comments");
		newType.addMetaData( "classKey", "classValue" );
		
		// add an initializer
		final Initializer initializer = newType.newInitializer();
		initializer.setBody( empty ); 
		initializer.setComments( "Initializer comments...");
		initializer.setStatic( false );
		
		// add a constructor
		final NewConstructor constructor = newType.newConstructor();
		constructor.setBody( empty );
		constructor.setComments( "Constructor comments...");
		constructor.addMetaData( "ConstructorKey", "ConstructorValue");
		constructor.setVisibility( Visibility.PUBLIC );
		
		// add a field
		final NewField field = newType.newField();
		field.setComments( "Field comments...");
		field.addMetaData( "FieldKey", "FieldValue");
		field.setName( "field");
		field.setType( context.getObject() );
		field.setValue( EmptyCodeBlock.INSTANCE );
		field.setVisibility( Visibility.PUBLIC );
		
		// add a method
		final NewMethod method = newType.newMethod();
		method.setBody( EmptyCodeBlock.INSTANCE );
		method.setComments( "Method comments...");
		method.addMetaData( "MethodKey", "MethodValue");
		method.setName( "method");
		method.setReturnType( context.getVoid() );
		method.setVisibility( Visibility.PUBLIC );
		
		// add a nested interface
		final NewNestedInterfaceType nestedInterface = newType.newNestedInterfaceType();
		nestedInterface.setComments("Nested Interface Comments...");
		nestedInterface.setSuperType( context.getObject() );
		nestedInterface.setNestedName( "NestedInterface");
		nestedInterface.setVisibility( Visibility.PUBLIC );
		
		// add a nested concrete type
		final NewNestedType nestedConcrete = newType.newNestedType();
		nestedConcrete.setComments("Nested Type Comments...");
		nestedConcrete.setSuperType( context.getObject() );
		nestedConcrete.setNestedName( "NestedConcrete");
		nestedConcrete.setVisibility( Visibility.PUBLIC );
		
		return newType;
	}
}
