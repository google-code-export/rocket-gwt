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
package rocket.compiler.test.variableassignedtoselfremover.client;

import com.google.gwt.junit.client.GWTTestCase;

public class VariableAssignedToSelfRemoverGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.variableassignedtoselfremover.VariableAssignedToSelfRemover";
	}

	/**
	 * Contains a local variable that is assigned itself. This particular
	 * statement should be removed.
	 */
	public void testContainsSelfAssignment() {
		int variableAssignedToSelf = echo(1);
		variableAssignedToSelf = variableAssignedToSelf; // this should get
															// removed

		assertEquals(1, variableAssignedToSelf);
	}

	/**
	 * Contains a number of local variables that are updated with values from
	 * various sources, none of these statements should be removed.
	 */
	public void testVariableUpdatedWithOtherValues() {
		final int leaveThisAssignmentAlone0 = echo(0);
		final int leaveThisAssignmentAlone1 = echo(1);

		int leaveThisAssignmentAlone2 = leaveThisAssignmentAlone0;
		int leaveThisAssignmentAlone3 = leaveThisAssignmentAlone1;

		assertEquals(leaveThisAssignmentAlone2, 0);
		assertEquals(leaveThisAssignmentAlone3, 1);
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
