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
package rocket.compiler;

/**
 * This interface is implemented by any checker that may wish to scan for specific strings in the resulting
 * javascript form of a program.
 * @author Miroslav Pokorny
 */
public interface JavaScriptSourceChecker {
	/**
	 * This method is invoked after the source code is generated.
	 * @param source
	 */
	void examine( String source );
}
