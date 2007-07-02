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
package rocket.beans.rebind.newinstance;

import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This class generates code that calls a factory method to provide a new bean instance
 * @author Miroslav Pokorny
 */
public class FactoryMethod extends NewInstance {

	protected void write0(final SourceWriter writer) {
		writer.println( "return " + this.getBeanType().getQualifiedSourceName() + '.' + this.getMethodName() + "();");
	}

	/**
	 * The name of an factory method that will create a new bean instance
	 */
	private String methodName;

	protected String getMethodName() {
		StringHelper.checkNotEmpty("field:methodName", methodName);
		return methodName;
	}

	public void setMethodName(final String methodName) {
		StringHelper.checkNotEmpty("parameter:methodName", methodName);

		final JMethod method = this.getBeanType().findMethod(methodName, new JType[0]);
		if (null == method) {
			throwFactoryMethodNotFoundException("Unable to find factory method [" + methodName + "]");
		}
		if (false == method.isPublic()) {
			throwFactoryMethodNotFoundException(methodName, "must be public");
		}
		if (false == method.isStatic()) {
			throwFactoryMethodNotFoundException(methodName, "must be static");
		}
		if (method.isAbstract()) {
			throwFactoryMethodNotFoundException(methodName, "must not be abstract");
		}

		this.methodName = methodName;
	}

	protected void throwFactoryMethodNotFoundException(final String methodName, final String message) {
		throw new FactoryMethodNotFoundException("The factory method [" + methodName + "]" + message + " on " + this.getBeanType().getQualifiedSourceName());
	}

	protected void throwFactoryMethodNotFoundException(final String message) {
		throw new FactoryMethodNotFoundException(message + " on " + this.getBeanType().getQualifiedSourceName());
	}
}
