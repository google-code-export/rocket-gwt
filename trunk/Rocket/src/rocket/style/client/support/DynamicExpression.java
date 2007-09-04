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
	public String setValue(final String value) {
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
	 * Extracts the number at the position of the placeholder from a
	 * prepopulated expression.
	 * 
	 * @return
	 */
	public int getValue(final String expressionWithValue) {
		int value = 0;
		final String expression = this.getExpression();
		int numberStart = expression.indexOf(StyleSupportConstants.DYNAMIC_EXPRESSION_PLACEHOLDER);
		if (-1 != numberStart) {

			boolean negativeNumber = false;

			final int length = expressionWithValue.length();
			final char c = expressionWithValue.charAt(numberStart);
			if ('-' == c) {
				negativeNumber = true;
				numberStart++;
			}

			for (int i = numberStart; i < length; i++) {
				final char d = expressionWithValue.charAt(i);
				if (false == Character.isDigit(d)) {
					break;
				}
				value = value * 10 + Character.digit(d, 10);
			}
			if (negativeNumber) {
				value = -value;
			}
		}
		return value;
	}

	/**
	 * Compares a text which possibly contains a dynamic expression less the
	 * substituted values.
	 * 
	 * @param text
	 * @return
	 */
	public boolean isEqual(final String text) {
		final String expression = this.getExpression();
		final int expressionLength = expression.length();
		final int textLength = text.length();
		int j = 0;
		boolean firstChar = true;

		int i = 0;
		while (i < expressionLength && j < textLength) {
			final char c = expression.charAt(i);
			final char d = text.charAt(j);

			// found a placeholder skip number chars appearing in text.
			if (StyleSupportConstants.DYNAMIC_EXPRESSION_PLACEHOLDER == c) {
				if (firstChar && '-' == d || Character.isDigit(d)) {
					firstChar = false;

					j++;
					if (j == textLength) {
						i++;
					}
					continue;
				}
				// not a digit check next.
				i++;
				continue;
			}
			if (c != d) {
				break;
			}
			i++;
			j++;
		}

		return i == expressionLength && j == textLength;
	}
}
