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
package rocket.generator.rebind.codeblock;

import java.util.Iterator;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.util.client.ObjectHelper;

/**
 * This CodeBlock inserts a single statement that passes all the parameters it
 * receives to the super type.
 * 
 * For the Example class below the line containing the super statement would be
 * code generated by this class.
 * 
 * <pre>
 * public Example(boolean booleanValue, byte byteValue, short shortValue) {
 * 	super(booleanValue, byteValue, shortValue);
 * }
 * </pre>
 * 
 * @author Miroslav Pokorny
 */
public class InvokeSuperTypeConstructorCodeBlock implements Literal {

	public InvokeSuperTypeConstructorCodeBlock() {
		super();
	}

	public InvokeSuperTypeConstructorCodeBlock(final Constructor constructor) {
		this.setConstructor(constructor);
	}

	public boolean isEmpty() {
		return false;
	}

	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final StringBuilder builder = new StringBuilder();

		builder.append("super(");

		final Iterator parameters = this.getConstructor().getParameters().iterator();
		while (parameters.hasNext()) {
			final ConstructorParameter parameter = (ConstructorParameter) parameters.next();

			builder.append(parameter.getName());

			if (parameters.hasNext()) {
				builder.append(", ");
			}
		}

		builder.append(')');

		writer.print(builder.toString());
	}

	private Constructor constructor;

	public Constructor getConstructor() {
		ObjectHelper.checkNotNull("field:constructor", constructor);
		return this.constructor;
	}

	public void setConstructor(final Constructor constructor) {
		ObjectHelper.checkNotNull("parameter:constructor", constructor);
		this.constructor = constructor;
	}

}