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
package rocket.generator.test.generator.client;

public class MethodTest {
	public boolean publicBooleanMethod(boolean parameter) {
		return true;
	}

	protected byte protectedByteMethod(byte parameter) {
		return 0;
	}

	short packagePrivateShortMethod(short parameter) {
		return 0;
	}

	private int privateIntMethod(int parameter) {
		return 0;
	}

	final static public void staticFinalMethod() throws Exception {
	}
}
