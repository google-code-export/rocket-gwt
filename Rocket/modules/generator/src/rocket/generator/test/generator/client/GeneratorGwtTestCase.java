/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.generator.test.generator.client;

import com.google.gwt.core.client.GWT;

public class GeneratorGwtTestCase extends rocket.generator.client.GeneratorGwtTestCase {

	public String getModuleName() {
		return "rocket.generator.test.generator.Generator";
	}

	public void testClassType() {
		GWT.create(ClassType.class);
	}

	public void testBooleanType() {
		GWT.create(BooleanType.class);
	}

	public void testByteType() {
		GWT.create(ByteType.class);
	}

	public void testShortType() {
		GWT.create(ShortType.class);
	}

	public void testIntType() {
		GWT.create(IntType.class);
	}

	public void testLongType() {
		GWT.create(LongType.class);
	}

	public void testFloatType() {
		GWT.create(FloatType.class);
	}

	public void testDoubleType() {
		GWT.create(DoubleType.class);
	}

	public void testCharType() {
		GWT.create(CharType.class);
	}

	public void testVoidType() {
		GWT.create(VoidType.class);
	}

	public void testConstructor() {
		GWT.create(ConstructorTest.class);
	}

	public void testField() {
		GWT.create(FieldTest.class);
	}

	public void testMethod() {
		GWT.create(MethodTest.class);
	}

	public void testNewConcreteClass() {
		final NewConcreteClass instance = (NewConcreteClass) GWT.create(NewConcreteClass.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewConcreteClass1", instance.getClass().getName());
	}

	public void testNewConcreteClassWithInitializer() throws Exception {
		final NewConcreteClassWithInitializer instance = (NewConcreteClassWithInitializer) GWT
				.create(NewConcreteClassWithInitializer.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewConcreteClassWithInitializer1", instance.getClass().getName());

		assertTrue(NewConcreteClassWithInitializer.staticInitializerRun);
		assertTrue(NewConcreteClassWithInitializer.instanceInitializerRun);
	}

	public void testNewConcreteClassWithConstructor() throws Exception {
		final NewConcreteClassWithConstructor instance = (NewConcreteClassWithConstructor) GWT
				.create(NewConcreteClassWithConstructor.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewConcreteClassWithConstructor1", instance.getClass().getName());
	}

	public void testNewConcreteClassWithField() {
		final NewConcreteClassWithField instance = (NewConcreteClassWithField) GWT.create(NewConcreteClassWithField.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewConcreteClassWithField1", instance.getClass().getName());
	}

	public void testNewConcreteClassWithMethod() throws Exception {
		final NewConcreteClassWithMethod instance = (NewConcreteClassWithMethod) GWT.create(NewConcreteClassWithMethod.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewConcreteClassWithMethod1", instance.getClass().getName());

		assertEquals(1 + 2, instance.add(1, 2));
	}

	public void testNewConcreteClassWithConstructorsFieldsAndMethods() {
		final NewConcreteClassWithConstructorsFieldsAndMethods instance = (NewConcreteClassWithConstructorsFieldsAndMethods) GWT
				.create(NewConcreteClassWithConstructorsFieldsAndMethods.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewConcreteClassWithConstructorsFieldsAndMethods1", instance.getClass()
				.getName());
	}

	public void testNewNestedConcreteClass() {
		final NewNestedClass instance = (NewNestedClass) GWT.create(NewNestedClass.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewNestedClass1", instance.getClass().getName());

		assertNotNull(instance.getNested());
	}

	public void testNewNestedInterface() {
		final NewNestedInterface instance = (NewNestedInterface) GWT.create(NewNestedInterface.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewNestedInterface1", instance.getClass().getName());

		assertNotNull(instance.getNested());
	}

	public void testNewNestedAnonymousConcreteClass() {
		final NewNestedAnonymousClass instance = (NewNestedAnonymousClass) GWT.create(NewNestedAnonymousClass.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewNestedAnonymousClass1", instance.getClass().getName());

		assertNotNull(instance.getNested());
	}

	public void testNewNestedAnonymousInterface() {
		final NewNestedAnonymousInterface instance = (NewNestedAnonymousInterface) GWT.create(NewNestedAnonymousInterface.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewNestedAnonymousInterface1", instance.getClass().getName());

		assertNotNull(instance.getNested());
	}

	public void testNewJsniField() {
		final NewJsniField instance = (NewJsniField) GWT.create(NewJsniField.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewJsniField1", instance.getClass().getName());

		instance.setField("apple");
		assertEquals("apple", instance.getField());
	}

	public void testNewJsniMethod() {
		final NewJsniMethod instance = (NewJsniMethod) GWT.create(NewJsniMethod.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.NewJsniMethod1", instance.getClass().getName());

		assertEquals(1 + 2, instance.add(1, 2));
	}

	public void testSuperType() {
		final SuperType instance = (SuperType) GWT.create(SuperType.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.SuperType", instance.getClass().getName());
	}

	public void testSubTypes() {
		final Object instance = GWT.create(SubTypes.class); // generated class
															// is not a sub
															// class of SubTypes
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.SubTypes1", instance.getClass().getName());
	}

	public void testCommentsAndMetaData() {
		final Object instance = GWT.create(CommentsAndMetaData.class);
		assertNotNull(instance);
		assertEquals("rocket.generator.test.generator.client.CommentsAndMetaData1", instance.getClass().getName());
	}
}
