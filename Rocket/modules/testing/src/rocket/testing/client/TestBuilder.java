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
package rocket.testing.client;

import java.util.List;

/**
 * A TestSuiteBuilder represents a factory which may be used to build suite or
 * collection of tests.
 * 
 * Because reflection is not supported the discovery of tests must be
 * accomplished in code and this is done via the {@link TestBuilder} class. It
 * is responsible for returning a List of Test(s) which are typically inner
 * classes. The Test class needs to only implement a two methods
 * <ul>
 * <li>Return the name of the test</li>
 * <li>Execute the test when asked</li>
 * </ul>
 * 
 * <pre>
 *  List tests = new ArrayList();
 *  test.add( new TestBuilder(){
 *     public String getName(){
 *        return &quot;${name-of-test}&quot;;
 *     }
 *     
 *     public void execute(){
 *        // execute test here OR call a test method on outter TestRunner subclass.
 *     }
 *  }
 * </pre>
 * 
 * @author Miroslav Pokorny (mP)
 */
public interface TestBuilder {

	/**
	 * Sub-classes must build a list containing one or more Tests. These will
	 * then be executed by the TestRunner.
	 * 
	 * @return
	 */
	abstract List<Test> buildCandidates();
}
