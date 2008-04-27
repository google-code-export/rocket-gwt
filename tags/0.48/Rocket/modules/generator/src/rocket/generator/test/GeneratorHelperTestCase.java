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
package rocket.generator.test;

import java.util.Map;

import junit.framework.TestCase;
import rocket.generator.rebind.GeneratorHelper;

public class GeneratorHelperTestCase extends TestCase {
	public void testisValidJavascriptIdentiferAgainstValidIdentifier() {
		assertTrue("apple", GeneratorHelper.isValidJavascriptIdentifier("apple"));
	}

	public void testisValidJavascriptIdentiferAgainstInvalidIdentifier() {
		assertFalse("for", GeneratorHelper.isValidJavascriptIdentifier("for"));
	}

	public void testIsValidFieldNameWithJavaKeyword0() {
		TestCase.assertFalse(GeneratorHelper.isValidJavaFieldName("public"));
	}

	public void testIsValidFieldNameWithJavaKeyword1() {
		TestCase.assertFalse(GeneratorHelper.isValidJavaFieldName("private"));
	}

	public void testIsValidFieldNameWithPrimitiveTypeName0() {
		TestCase.assertFalse(GeneratorHelper.isValidJavaFieldName("boolean"));
	}

	public void testIsValidFieldNameWithPrimitiveTypeName1() {
		TestCase.assertFalse(GeneratorHelper.isValidJavaFieldName("int"));
	}

	public void testIsValidFieldNameWithInvalidJavaIdentifier0() {
		TestCase.assertFalse(GeneratorHelper.isValidJavaFieldName(" "));
	}

	public void testIsValidFieldNameWithInvalidJavaIdentifier1() {
		TestCase.assertFalse(GeneratorHelper.isValidJavaFieldName("abcdef."));
	}

	public void testIsValidFieldNameWithValidJavaIdentifier() {
		TestCase.assertTrue(GeneratorHelper.isValidJavaFieldName("apple"));
	}

	public void testIsValidJavaClassname() {
		TestCase.assertTrue(GeneratorHelper.isValidJavaTypeName(Object.class.getName()));
	}

	public void testIsValidJavaClassnameWithInnerClassname() {
		TestCase.assertTrue(GeneratorHelper.isValidJavaTypeName(Map.Entry.class.getName()));
	}
}
