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
package rocket.compiler.test.trailingreturnremover.client;

import com.google.gwt.junit.client.GWTTestCase;

public class TrailingReturnRemoverGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.trailingreturnremover.TrailingReturnRemover";
	}

	public void testHasTrailingReturnStatement() {
		this.hasLoneReturn();
	}

	void hasLoneReturn() {
		dummy();
		return;
	}

	public void testCallsMethodThatReturnsLiteral() {
		this.returnsLiteral();
	}

	/**
	 * This particular return statement cant be modified.
	 * 
	 * @return
	 */
	int returnsLiteral() {
		dummy();
		return 1;
	}

	public void testReturnsValueFromAnotherMethodCall() {
		this.returnsValueFromAnotherMethodCall();
	}

	int returnsValueFromAnotherMethodCall() {
		return this.echo(1);
	}

	/**
	 * This method simply returns the given parameter, it is used to prevent
	 * inlining and other optimisations.
	 * 
	 * @param i
	 * @return
	 */
	int echo(final int i) {
		dummy();
		return i;
	}

	/**
	 * By calling this method other methods avoid being completely removed by
	 * various optimisers.
	 */
	static void dummy() {
		for (int i = 0; i < 1; i++) {
			new Object(); // stops inlining...
		}
	}
}
