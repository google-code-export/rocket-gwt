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

import java.util.Set;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewNestedType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.PrimitiveHelper;

public class SubTypesGenerator extends TestGenerator {

	protected NewConcreteType assembleNewType(final Type subType, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final Set subTypesSubTypes = subType.getSubTypes();
		PrimitiveHelper.checkEquals("" + subTypesSubTypes, 0, subTypesSubTypes.size());

		final NewConcreteType newConcreteType = context.newConcreteType();
		newConcreteType.setAbstract(false);
		newConcreteType.setFinal(false);
		newConcreteType.setName( newTypeName );
		newConcreteType.setSuperType( context.getObject() );

		final NewNestedType newSubType = newConcreteType.newNestedType();
		newSubType.setAbstract(false);
		newSubType.setFinal(false);
		newSubType.setNestedName("Nested1");
		newSubType.setStatic( true );
		newSubType.setSuperType( newConcreteType );
		newSubType.setVisibility(Visibility.PUBLIC);

		final NewNestedType newSubSubType0 = newSubType.newNestedType();
		newSubSubType0.setAbstract(false);
		newSubSubType0.setFinal(false);
		newSubSubType0.setNestedName("Nested2");
		newSubSubType0.setStatic( true );
		newSubSubType0.setSuperType( newSubType );
		newSubSubType0.setVisibility(Visibility.PUBLIC);

		final NewNestedType newSubSubType1 = newSubType.newNestedType();
		newSubSubType1.setAbstract(false);
		newSubSubType1.setFinal(false);
		newSubSubType1.setNestedName("Nested3");
		newSubSubType1.setStatic( true );
		newSubSubType1.setSuperType( newSubType );
		newSubSubType1.setVisibility(Visibility.PUBLIC);
		
		final NewNestedType newSubSubSubType = newConcreteType.newNestedType();
		newSubSubSubType.setAbstract(false);
		newSubSubSubType.setFinal(false);
		newSubSubSubType.setNestedName("Nested4");
		newSubSubSubType.setStatic( true );
		newSubSubSubType.setSuperType( newSubSubType0 );
		newSubSubSubType.setVisibility(Visibility.PUBLIC);
		
		final Set objectSubTypes = context.getObject().getSubTypes();
		PrimitiveHelper.checkTrue("newConcreteType" + objectSubTypes, objectSubTypes.contains(newConcreteType));
		PrimitiveHelper.checkTrue("String", objectSubTypes.contains(context.getString()));

		final Set newConcreteSubTypes = newConcreteType.getSubTypes();
		PrimitiveHelper.checkEquals("", 1, newConcreteSubTypes.size());
		PrimitiveHelper.checkTrue("Nested1", newConcreteSubTypes.contains(newSubType));

		final Set newSubTypeSubTypes = newSubType.getSubTypes();
		PrimitiveHelper.checkEquals("", 2, newSubTypeSubTypes.size());
		PrimitiveHelper.checkTrue("Nested2", newSubTypeSubTypes.contains(newSubSubType0));
		PrimitiveHelper.checkTrue("Nested3", newSubTypeSubTypes.contains(newSubSubType1));

		final Set newSubSubSubTypeSubTypes = newSubSubType0.getSubTypes();
		PrimitiveHelper.checkEquals("", 1, newSubSubSubTypeSubTypes.size());
		PrimitiveHelper.checkTrue("Nested4", newSubSubSubTypeSubTypes.contains(newSubSubSubType));
		
		
		return newConcreteType;
	}
}
