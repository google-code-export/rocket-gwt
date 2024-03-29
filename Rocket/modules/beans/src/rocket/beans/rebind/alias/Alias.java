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
package rocket.beans.rebind.alias;

import rocket.util.client.Checker;

/**
 * Holds an aliased bean reference value.
 * 
 * @author Miroslav Pokorny
 */
public class Alias {

	/**
	 * The alias bean id
	 */
	private String name;

	public String getName() {
		Checker.notEmpty("field:name", name);
		return this.name;
	}

	public void setName(final String name) {
		Checker.notEmpty("parameter:name", name);
		this.name = name;
	}

	/**
	 * The aliased bean id.
	 */
	private String bean;

	public String getBean() {
		Checker.notEmpty("field:to", bean);
		return this.bean;
	}

	public void setBean(final String to) {
		Checker.notEmpty("parameter:to", to);
		this.bean = to;
	}

	public String toString() {
		return super.toString() + ", name\"" + name + "\", bean\"" + bean + "\".";
	}
}
