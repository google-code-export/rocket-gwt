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
package rocket.generator.test.templatedfilecodeblock.client;

import com.google.gwt.core.client.GWT;

public class TemplatedFileCodeBlockGwtTestCase extends rocket.generator.client.GeneratorGwtTestCase {

	public String getModuleName() {
		return "rocket.generator.test.templatedfilecodeblock.TemplatedFileCodeBlock";
	}

	public void testTemplatedFileCodeBlock() {
		final TemplatedFileCodeBlock instance = (TemplatedFileCodeBlock) GWT.create(TemplatedFileCodeBlock.class);
		assertNotNull(instance);

		instance.dummy();
	}

	public void testBooleanPlaceHolder() {
		final BooleanPlaceHolder instance = (BooleanPlaceHolder) GWT.create(BooleanPlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.BOOLEAN, instance.getBooleanValue());
	}

	public void testBytePlaceHolder() {
		final BytePlaceHolder instance = (BytePlaceHolder) GWT.create(BytePlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.BYTE, instance.getByteValue());
	}

	public void testShortPlaceHolder() {
		final ShortPlaceHolder instance = (ShortPlaceHolder) GWT.create(ShortPlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.SHORT, instance.getShortValue());
	}

	public void testIntPlaceHolder() {
		final IntPlaceHolder instance = (IntPlaceHolder) GWT.create(IntPlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.INT, instance.getIntValue());
	}

	public void testLongPlaceHolder() {
		final LongPlaceHolder instance = (LongPlaceHolder) GWT.create(LongPlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.LONG, instance.getLongValue());
	}

	public void testFloatPlaceHolder() {
		final FloatPlaceHolder instance = (FloatPlaceHolder) GWT.create(FloatPlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.FLOAT, instance.getFloatValue(), 1);
	}

	public void testDoublePlaceHolder() {
		final DoublePlaceHolder instance = (DoublePlaceHolder) GWT.create(DoublePlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.DOUBLE, instance.getDoubleValue(), 1);
	}

	public void testCharPlaceHolder() {
		final CharPlaceHolder instance = (CharPlaceHolder) GWT.create(CharPlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.CHAR, instance.getCharValue());
	}

	public void testStringPlaceHolder() {
		final StringPlaceHolder instance = (StringPlaceHolder) GWT.create(StringPlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.STRING, instance.getStringValue());
	}

	public void testNestedAnonymousTypePlaceHolder() {
		final NestedAnonymousTypePlaceHolder instance = (NestedAnonymousTypePlaceHolder) GWT.create(NestedAnonymousTypePlaceHolder.class);
		assertNotNull(instance);

		assertNotNull(instance.getAnonymousInstance());
	}

	public void testConstructorPlaceHolder() {
		final ConstructorPlaceHolder instance = (ConstructorPlaceHolder) GWT.create(ConstructorPlaceHolder.class);
		assertNotNull(instance);

		assertNotNull(instance.newObject());
	}

	public void testConstructorParametersPlaceHolder() {
		final ConstructorParameterPlaceHolder instance = (ConstructorParameterPlaceHolder) GWT
				.create(ConstructorParameterPlaceHolder.class);
		assertNotNull(instance);

		assertEquals(instance.newInstance().byteValue, TemplatedFileCodeBlockTestConstants.BYTE);
	}

	public void testMethodPlaceHolder() {
		final MethodPlaceHolder instance = (MethodPlaceHolder) GWT.create(MethodPlaceHolder.class);
		assertNotNull(instance);

		assertNotNull(instance.method());
	}

	public void testJsniMethodPlaceHolder() {
		final JsniMethodPlaceHolder instance = (JsniMethodPlaceHolder) GWT.create(JsniMethodPlaceHolder.class);
		assertNotNull(instance);

		assertNotNull(instance.method());
	}

	public void testMethodParameterPlaceHolder() {
		final MethodParameterPlaceHolder instance = (MethodParameterPlaceHolder) GWT.create(MethodParameterPlaceHolder.class);
		assertNotNull(instance);

		final Object object = new Object();
		assertSame(object, instance.returnParameter(object));
	}

	public void testFieldPlaceHolder() {
		final FieldPlaceHolder instance = (FieldPlaceHolder) GWT.create(FieldPlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.STRING, instance.getField());
	}

	public void testJsniFieldPlaceHolder() {
		final JsniFieldPlaceHolder instance = (JsniFieldPlaceHolder) GWT.create(JsniFieldPlaceHolder.class);
		assertNotNull(instance);

		assertEquals(TemplatedFileCodeBlockTestConstants.STRING, instance.getField());
	}
}
