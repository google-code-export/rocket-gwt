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
package rocket.testing.rebind;


class Constants{

	final static String TEST_BUILDER_SUFFIX = "__TestBuilder";

	final static String BUILD_CANDIDATES_TEMPLATE = "build-candidates.txt";
	final static String BUILD_CANDIDATES_ADD_TESTS = "addTests";
	
	final static String ADD_TEST_METHOD_TEMPLATE = "add-test-method.txt";
	final static String ADD_TEST_METHOD_TEST_RUNNER = "testRunner";
	final static String ADD_TEST_METHOD_METHOD = "method";
	final static String ADD_TEST_METHOD_TEST_NAME = "testName";
	
	final static String TEST_METHOD_NAME_PREFIX = "test";
	
	final static String BUILD_CANDIDATES_METHOD = "buildCandidates";
	final static String TEST_ANNOTATION = "testing-testRunner";
	final static String ORDER_ANNOTATION = "testing-testMethodOrder";
}
