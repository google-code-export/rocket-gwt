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
package rocket.generator.client;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * This class is part of a package of several classes that assist with
 * processing exceptions thrown by a generator in a deferred manner.
 * 
 * <ul>
 * <li>Gwt test cases must extend GeneratorGwtTestCase</li>
 * <li>GWT.create() must be surrounded by a call to
 * GeneratorGwtTestCase.assertBindingFailed()</li>
 * <li>must catch FailedGenerateAttemptException and process accordingly.</li>
 * </ul>
 * 
 * <pre>
 * public void testXXX() {
 * 	try {
 * 		Object object = assertBindingFailed(GWT.create(YYY.class));
 * 	} catch (FailedGenerateAttemptException expected) {
 * 		// test causeType etc...
 * 	}
 * }
 * </pre>
 * 
 * @author Miroslav Pokorny
 */
abstract public class GeneratorGwtTestCase extends GWTTestCase {

	protected void assertBindingFailed(Object object) {
		if (object instanceof FailedGenerateAttemptException) {
			throw (FailedGenerateAttemptException) object;
		}
		fail("Deferred exception expected during generation attempt.");
	}
}
