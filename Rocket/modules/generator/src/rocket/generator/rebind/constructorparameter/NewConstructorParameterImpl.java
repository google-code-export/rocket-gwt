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
package rocket.generator.rebind.constructorparameter;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * Represents a parameter that belongs to a constructor.
 * 
 * @author Miroslav Pokorny
 */
public class NewConstructorParameterImpl extends AbstractConstructorParameter implements NewConstructorParameter {

	public void setFinal(final boolean finall) {
		super.setFinal(finall);
	}

	public void setName(final String name) {
		super.setName(name);
	}

	public void setType(final Type type) {
		super.setType(type);
	}

	public void setEnclosingConstructor(final Constructor enclosingConstructor) {
		super.setEnclosingConstructor(enclosingConstructor);
	}

	public void write(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		if (this.isFinal()) {
			writer.print("final ");
		}
		writer.print(this.getType().getName());
		writer.print(" ");
		writer.print(this.getName());
	}

	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("NewConstructorParameter ");

		if (this.hasType()) {
			builder.append(this.getType().getName());
			builder.append(" ");
		}
		if (this.hasName()) {
			builder.append(this.getName());
		}
		return builder.toString();
	}
}
