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
package rocket.compiler.test.emptyblockremover.compiler;

import rocket.compiler.test.JavaScriptSourceChecker;

/**
 * Scans the entire generated javascript source attempting to find any empty else statements.
 * If any are found an exception is thrown
 * @author Miroslav Pokorny
 */
public class EmptyElseChecker implements JavaScriptSourceChecker {

	/**
	 * Assert that no empty else statements exist in the source.
	 */
	public void examine(String source) {
		// remove all white space then scan for else{}
		final String source0 = source.replace('\n', ' ' ).replace( '\r', ' ').replaceAll(" ", "");
		if( source0.contains( "else{}")){
			throw new AssertionError( "Empty else statement found, source: \"" + source + "\"");
		}
	}
}
