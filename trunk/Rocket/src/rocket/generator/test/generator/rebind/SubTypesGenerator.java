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

	protected NewConcreteType assembleNewType(final Type subType0, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();

		final Set subTypes0 = subType0.getSubTypes();
		PrimitiveHelper.checkEquals("", 0, subTypes0.size());

		final NewConcreteType subType1 = context.newConcreteType();
		subType1.setAbstract(false);
		subType1.setFinal(false);
		subType1.setName(newTypeName);
		subType1.setSuperType(subType0);

		final NewNestedType subType2 = subType1.newNestedType();
		subType2.setAbstract(false);
		subType2.setFinal(false);
		subType2.setName("Nested1");
		subType2.setSuperType(subType1);
		subType2.setVisibility( Visibility.PUBLIC );

		final NewNestedType subType3 = subType1.newNestedType();
		subType3.setAbstract(false);
		subType3.setFinal(false);
		subType3.setName("Nested2");
		subType3.setSuperType(subType2);
		subType3.setVisibility( Visibility.PUBLIC );
		
		final NewNestedType subType4 = subType1.newNestedType();
		subType4.setAbstract(false);
		subType4.setFinal(false);
		subType4.setName("Nested3");
		subType4.setSuperType(subType3);
		subType4.setVisibility( Visibility.PUBLIC );
		
		final Set subTypes4 = subType4.getSubTypes();
		PrimitiveHelper.checkEquals("", 0, subTypes4.size());

		final Set subTypes3 = subType3.getSubTypes();
		PrimitiveHelper.checkEquals("", 1, subTypes3.size());
		PrimitiveHelper.checkTrue("SubType4", subTypes3.contains(subType4));

		final Set subTypes2 = subType2.getSubTypes();
		PrimitiveHelper.checkEquals("", 2, subTypes2.size());
		PrimitiveHelper.checkTrue("SubType4", subTypes2.contains(subType4));
		PrimitiveHelper.checkTrue("SubType3", subTypes2.contains(subType3));

		final Set subTypes1 = subType1.getSubTypes();
		PrimitiveHelper.checkEquals("", 3, subTypes1.size());
		PrimitiveHelper.checkTrue("SubType4", subTypes1.contains(subType4));
		PrimitiveHelper.checkTrue("SubType3", subTypes1.contains(subType3));
		PrimitiveHelper.checkTrue("SubType2", subTypes1.contains(subType2));

		final Set subTypes0b = subType0.getSubTypes();
		PrimitiveHelper.checkEquals("", 4, subTypes0b.size());
		PrimitiveHelper.checkTrue("SubType4", subTypes0b.contains(subType4));
		PrimitiveHelper.checkTrue("SubType3", subTypes0b.contains(subType3));
		PrimitiveHelper.checkTrue("SubType2", subTypes0b.contains(subType2));
		PrimitiveHelper.checkTrue("SubType1", subTypes0b.contains(subType1));

		final Set objectSubTypes = context.getObject().getSubTypes();
		PrimitiveHelper.checkTrue("SubType4", objectSubTypes.contains(subType4));
		PrimitiveHelper.checkTrue("SubType3", objectSubTypes.contains(subType3));
		PrimitiveHelper.checkTrue("SubType2", objectSubTypes.contains(subType2));
		PrimitiveHelper.checkTrue("SubType1", objectSubTypes.contains(subType1));
		PrimitiveHelper.checkTrue("SubType0", objectSubTypes.contains(subType0));
		PrimitiveHelper.checkTrue("String", objectSubTypes.contains(context.getString()));

		return subType1;
	}
}
