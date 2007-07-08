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
package rocket.beans.rebind.init;

import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This class represents a holder for a custom init method.
 * 
 * @author Miroslav Pokorny
 */
public class CustomInitMethod extends InitMethod {

	public void write(final SourceWriter writer) {
		this.checkMethod();

		final String instance = "instance";
		writer.println("protected void satisfyInit(final Object " + instance
				+ ") throws Exception {");
		writer.indent();

		// cast to type
		final String type = this.getBean().getTypeName();
		final String instance0 = "instance0";
		writer.println("final " + type + " " + instance0 + " = (" + type + ") "
				+ instance + ";");

		// call init method...
		writer.println(instance0 + "." + this.getMethodName() + "();");

		writer.outdent();
		writer.println("}");
	}

	protected void checkMethod() {
		final String methodName = this.getMethodName();
		final JClassType type = (JClassType)this.getBeanFactoryGeneratorContext().getType( this.getBean().getTypeName() );
		
		final JMethod method = type.findMethod(methodName, new JType[0]);
		if (null == method) {
			throwInitMethodNotFoundException("Unable to find init method ["
					+ methodName + "]");
		}
		if (false == method.isPublic()) {
			throwInitMethodNotFoundException(methodName, "must be public");
		}
		if (method.isStatic()) {
			throwInitMethodNotFoundException(methodName, "must be static");
		}
		if (method.isAbstract()) {
			throwInitMethodNotFoundException(methodName, "must not be abstract");
		}
	}

	/**
	 * The name of an init method.
	 */
	private String methodName;

	protected String getMethodName() {
		StringHelper.checkNotEmpty("field:methodName", methodName);
		return methodName;
	}

	public void setMethodName(final String methodName) {
		StringHelper.checkNotEmpty("parameter:methodName", methodName);
		this.methodName = methodName;
	}

	protected void throwInitMethodNotFoundException(final String methodName,
			final String message) {
		throw new InitMethodNotFoundException("The init method [" + methodName
				+ "]" + message + " on " + this.getBean().getTypeName());
	}

	protected void throwInitMethodNotFoundException(final String message) {
		throw new InitMethodNotFoundException(message + " on "
				+ this.getBean().getTypeName());
	}
}
