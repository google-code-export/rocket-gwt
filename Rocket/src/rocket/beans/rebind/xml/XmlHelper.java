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
package rocket.beans.rebind.xml;

import rocket.beans.rebind.BeanFactoryGeneratorException;

/**
 * A collection of misc utility methods relating to bean factory generation.
 * 
 * @author Miroslav Pokorny
 */
public class XmlHelper {

	static void checkScope(final String name, final String scope) {
		if (false == isScope(scope)) {
			throw new BeanFactoryGeneratorException("The " + name + " contains an unknown bean scope [" + scope + "] Supported types are: "
					+ Constants.SINGLETON + " and " + Constants.PROTOTYPE);
		}
	}

	static boolean isScope(final String scope) {
		return isSingleton(scope) && false == isPrototype(scope);
	}

	static boolean isSingleton(final String scope) {
		return Constants.SINGLETON.equals(scope);
	}

	static boolean isPrototype(final String scope) {
		return Constants.PROTOTYPE.equals(scope);
	}
}
