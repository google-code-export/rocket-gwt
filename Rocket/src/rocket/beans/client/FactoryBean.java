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
package rocket.beans.client;

/**
 * A FactoryMethodBean is a class that can on demand return a bean
 * 
 * @author Miroslav Pokorny
 */
public interface FactoryBean {
	/**
	 * Returns either the singleton instance of create a new instance if the
	 * bean is a prototype.
	 */
	Object getObject();

	/**
	 * Tests if the contained bean is a singleton.
	 */
	boolean isSingleton();
}
