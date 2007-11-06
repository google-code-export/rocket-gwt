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
 * Classes implementing this interface are bean factories that can be used to
 * fetch beans given an id
 * 
 * @author Miroslav Pokorny
 */
public interface BeanFactory {
	/**
	 * Retrieves a bean given its name.
	 * 
	 * @param name
	 * @return
	 */
	Object getBean(String name);

	/**
	 * Tests if the bean with the given name is a singleton.
	 * 
	 * @param name
	 * @return
	 */
	boolean isSingleton(String name);
}
