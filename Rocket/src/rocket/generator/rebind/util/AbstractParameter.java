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
package rocket.generator.rebind.util;

import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * Represents a parameter to a constructor or method.
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractParameter extends AbstractClassComponent {

	private String name;

	public String getName() {
		GeneratorHelper.checkJavaVariableName("field:name", this.name);
		return name;
	}

	protected boolean hasName() {
		return name != null;
	}

	protected void setName(final String name) {
		GeneratorHelper.checkJavaVariableName("parameter:name", name);
		this.name = name;
	}

	public String getJsniNotation() {
		return this.getType().getJsniNotation();
	}

	/**
	 * The parameter type
	 */
	private Type type;

	public Type getType() {
		Checker.notNull("field:type", type);
		return type;
	}

	protected boolean hasType() {
		return null != this.type;
	}

	protected void setType(final Type type) {
		Checker.notNull("field:type", type);
		this.type = type;
	}

	/**
	 * When true indicates that this method is final
	 */
	private boolean finall;

	public boolean isFinal() {
		return finall;
	}

	protected void setFinal(final boolean finall) {
		this.finall = finall;
	}

	public String toString() {
		return this.type + " " + this.name;
	}
}