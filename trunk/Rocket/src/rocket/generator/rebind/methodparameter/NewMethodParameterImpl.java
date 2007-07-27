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
package rocket.generator.rebind.methodparameter;

import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * Represents a parameter that belongs to a method.
 * 
 * @author Miroslav Pokorny
 */
public class NewMethodParameterImpl extends AbstractMethodParameter implements NewMethodParameter {

	public void setFinal(final boolean finall) {
		super.setFinal(finall);
	}

	public void setName(final String name) {
		super.setName(name);
	}

	public boolean hasType() {
		return super.hasType();
	}

	public void setType(final Type type) {
		super.setType(type);
	}

	public void setEnclosingMethod(final Method enclosingMethod) {
		super.setEnclosingMethod(enclosingMethod);
	}

	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		if (this.isFinal()) {
			writer.print("final ");
		}
		writer.print(this.getType().getName());
		writer.print(" ");
		writer.print(this.getName());
	}

	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("NewMethodParameter ");

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
