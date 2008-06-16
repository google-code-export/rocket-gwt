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
package rocket.beans.rebind.beanreference;

import rocket.beans.rebind.value.Value;

/**
 * Holds a bean reference value be it a named or nested bean reference.
 * 
 * @author Miroslav Pokorny
 */
public interface BeanReference extends Value {

	/**
	 * Returns the id of the referenced bean
	 * 
	 * @return The bean name.
	 */
	String getId();
}
