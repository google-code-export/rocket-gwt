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
 * A container for constants used through out this package.
 */
abstract public class Constants {
	static final String INVOKE_CLINITS_AT_STARTUP_FILENAME = InvokeClinitsAtStartupMover.class.getName() + ".txt";
	static final String INCLUDE_SUGGESTED_TYPES = "includeSuggestedTypes";
	
	static final String COMMENT_LINE = "#";
	
	static final String ON_MODULE_LOAD = "onModuleLoad";
	
	static final String CLINIT = "$clinit";
	
	static final String PACKAGE = Constants.class.getPackage().getName();
}