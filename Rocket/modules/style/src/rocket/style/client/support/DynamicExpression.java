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
package rocket.style.client.support;

/**
 * This class exists primarily to assist the InternetExplorer6StyleSupport class
 * with building and extracting expressions to simulate fixed positioning for
 * IE.
 * 
 * This class is only public to enable it to be tested by
 * {@link rocket.style.test.dynamicexpression.DynamicExpressionTestCase}
 * 
 * @author Miroslav Pokorny
 */
public class DynamicExpression {
	public DynamicExpression(final String expression) {
		super();

		this.setExpression(expression);
	}

	private String expression;

	protected String getExpression() {
		return this.expression;
	}

	protected void setExpression(final String expression) {
		this.expression = expression;
	}

	/**
	 * Replaces the placeholders within the expression with the given value and
	 * returned the final result.
	 * 
	 * @param value
	 * @return
	 */
	public String buildExpression(final String value) {
		String value0 = value;
		if (value.endsWith("px")) {
			value0 = value0.substring(0, value0.length() - 2);
		}

		final String expression = this.getExpression();
		final StringBuffer buf = new StringBuffer();

		int pos = 0;
		final int length = expression.length();
		while (pos < length) {
			final int placeHolder = expression.indexOf(StyleSupportConstants.DYNAMIC_EXPRESSION_PLACEHOLDER, pos);
			if (-1 == placeHolder) {
				buf.append(expression.substring(pos));
				break;
			}

			buf.append(expression.substring(pos, placeHolder));
			buf.append(value0);

			pos = placeHolder + 1;
		}

		// remove the leading expression( and trialing )
		return buf.toString();
	}

	/**
	 * Extracts the numeric value assuming it is a pixel value.
	 * 
	 * @return
	 */
	public String getValue(final String expressionWithValue) {
		// String value = null;
		//		
		// if( expressionWithValue.startsWith("/*") &&
		// expressionWithValue.length() > 4 ){
		// final int starSlash = expressionWithValue.indexOf( "*/", 2 );
		// if( -1 != starSlash ){
		// value = expressionWithValue.substring( 2, starSlash );
		// }
		// }
		//		
		//		
		// return value;
		//		
		final String expression = this.getExpression();
		String value = null;

		final int valueStart = expression.indexOf(StyleSupportConstants.DYNAMIC_EXPRESSION_PLACEHOLDER);
		if (-1 != valueStart) {

			final int plus = expressionWithValue.indexOf('+', valueStart + 1);
			final int minus = expressionWithValue.indexOf('-', valueStart + 1);
			int endOfValue = expressionWithValue.length();

			if (plus != -1) {
				endOfValue = plus;
			}
			if (minus != -1 & minus < endOfValue) {
				endOfValue = minus;
			}

			value = expressionWithValue.substring(valueStart, endOfValue) + "px";
		}
		return value;
	}

	/**
	 * Compares a text which possibly contains a dynamic expression less the
	 * substituted values.
	 * 
	 * @param expressionWithValue
	 * @return
	 */
	public boolean isEqual(final String expressionWithValue) {
		boolean equal = false;

		if (null != expressionWithValue) {
			equal = true;

			final String expression = this.getExpression();
			final int expressionLength = expression.length();

			final int expressionWithValueLength = expressionWithValue.length();
			int expressionIndex = 0;
			int expressionWithValueIndex = 0;

			while (true) {
				boolean endOfExpressionReached = expressionIndex == expressionLength;
				boolean endOfExpressionWithValueReached = expressionWithValueIndex == expressionWithValueLength;
				if (endOfExpressionReached || endOfExpressionWithValueReached) {
					equal = endOfExpressionReached == endOfExpressionWithValueReached;
					break;
				}

				while (expressionIndex < expressionLength && expressionWithValueIndex < expressionWithValueLength) {
					final char c = expression.charAt(expressionIndex);
					expressionIndex++;

					if (c == StyleSupportConstants.DYNAMIC_EXPRESSION_PLACEHOLDER) {
						break;
					}

					// chars should match...
					final char d = expressionWithValue.charAt(expressionWithValueIndex);
					if (c != d) {
						equal = false;
						break;
					}
					expressionWithValueIndex++;
				}

				boolean skipMinus = true;
				while (expressionWithValueIndex < expressionWithValueLength) {
					char d = expressionWithValue.charAt(expressionWithValueIndex);
					// skip along until a + or - is hit...
					if (d == '+') {
						break;
					}
					if (skipMinus == false && d == '-') {
						break;
					}

					skipMinus = false;
					expressionWithValueIndex++;
				}
			}
		}

		return equal;
	}
}
