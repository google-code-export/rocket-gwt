package rocket.style.test.dynamicexpression;

import junit.framework.TestCase;
import rocket.style.client.support.DynamicExpression;

public class DynamicExpressionTestCase extends TestCase {

	public void testIsEqual0() {
		final String text = "apple";
		final DynamicExpression expression = new DynamicExpression("banana");

		assertFalse(expression.isEqual(text));
	}

	public void testIsEqual1() {
		final String text = "apple";
		final DynamicExpression expression = new DynamicExpression("apple 2");

		assertFalse(expression.isEqual(text));
	}

	public void testIsEqual2() {
		final String text = "apple+2";
		final DynamicExpression expression = new DynamicExpression("apple#");

		assertFalse(expression.isEqual(text));
	}

	public void testIsEqual3() {
		final String text = "123+apple";
		final DynamicExpression expression = new DynamicExpression("#+apple");

		assertTrue(expression.isEqual(text));

	}

	public void testIsEqual4() {
		final String text = "apple+123+banana";
		final DynamicExpression expression = new DynamicExpression("apple+#+banana");

		assertTrue(expression.isEqual(text));
	}

	public void testIsEqual5() {
		final String text = "apple+123";
		final DynamicExpression expression = new DynamicExpression("apple+#");

		assertTrue(expression.isEqual(text));
	}

	public void testIsEqual6() {
		final String text = "-123+apple";
		final DynamicExpression expression = new DynamicExpression("#+apple");

		assertTrue(expression.isEqual(text));
	}

	public void testIsEqual7() {
		final String text = "apple+-123+banana";
		final DynamicExpression expression = new DynamicExpression("apple+#+banana");

		assertTrue(expression.isEqual(text));
	}

	public void testIsEqual8() {
		final String text = "apple-123";
		final DynamicExpression expression = new DynamicExpression("apple#");

		assertTrue(expression.isEqual(text));
	}

	public void testGetValue0() {
		final String textWithValue = "123+apple";
		final DynamicExpression expression = new DynamicExpression("#+apple");

		assertEquals("123px", expression.getValue(textWithValue));
	}

	public void testGetValue1() {
		final String textWithValue = "apple+123+banana";
		final DynamicExpression expression = new DynamicExpression("apple+#+banana");

		assertEquals("123px", expression.getValue(textWithValue));
	}

	public void testGetValue2() {
		final String textWithValue = "apple+123";
		final DynamicExpression expression = new DynamicExpression("apple+#");

		assertEquals("123px", expression.getValue(textWithValue));
	}

	public void testGetValue3() {
		final String textWithValue = "-123+apple";
		final DynamicExpression expression = new DynamicExpression("#+apple");

		assertEquals("-123px", expression.getValue(textWithValue));
	}

	public void testGetValue4() {
		final String textWithValue = "apple+-123+banana";
		final DynamicExpression expression = new DynamicExpression("apple+#+banana");

		assertEquals("-123px", expression.getValue(textWithValue));
	}

	public void testGetValue5() {
		final String textWithValue = "apple-123";
		final DynamicExpression expression = new DynamicExpression("apple#");

		assertEquals("-123px", expression.getValue(textWithValue));
	}

	public void testGetValue6() {
		final String textWithValue = "apple+123+banana+123+carrot";
		final DynamicExpression expression = new DynamicExpression("apple+#+banana+#+carrot");

		assertEquals("123px", expression.getValue(textWithValue));
	}

	public void testGetValue7() {
		final String textWithValue = "apple+123+banana+123+carrot";
		final DynamicExpression expression = new DynamicExpression("apple+#+banana+#+carrot");

		assertEquals("123px", expression.getValue(textWithValue));
	}

	public void testGetValue8() {
		final String textWithValue = "123-apple";
		final DynamicExpression expression = new DynamicExpression("#apple");

		assertEquals("123px", expression.getValue(textWithValue));
	}

	public void testSet0() {
		final String value = "123";
		final DynamicExpression expression = new DynamicExpression("# apple");

		assertEquals("123 apple", expression.buildExpression(value));
	}

	public void testSet1() {
		final String value = "123";
		final DynamicExpression expression = new DynamicExpression("apple # banana");

		assertEquals("apple 123 banana", expression.buildExpression(value));
	}

	public void testSet2() {
		final String value = "123";
		final DynamicExpression expression = new DynamicExpression("apple #");

		assertEquals("apple 123", expression.buildExpression(value));
	}

	public void testSet3() {
		final String value = "123";
		final DynamicExpression expression = new DynamicExpression("apple # banana # carrot");

		assertEquals("apple 123 banana 123 carrot", expression.buildExpression(value));
	}
}
