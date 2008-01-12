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
package rocket.beans.rebind.aop.rethrowdeclaredexception;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the rethrow declared exception template
 * 
 * @author Miroslav Pokorny
 */
public class RethrowDeclaredExceptionTemplatedFile extends TemplatedFileCodeBlock {

	public RethrowDeclaredExceptionTemplatedFile() {
		super();
	}

	/**
	 * The method exception being unwrapped
	 */
	private Type exception;

	protected Type getException() {
		Checker.notNull("field:exception", exception);
		return this.exception;
	}

	public void setException(final Type exception) {
		Checker.notNull("exception:exception", exception);
		this.exception = exception;
	}

	protected String getResourceName() {
		return Constants.TEMPLATE;
	}
	
	public InputStream getInputStream(){
		return super.getInputStream();
	}
	
	public Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.EXCEPTION_TYPE.equals(name)) {
				value = this.getException();
				break;
			}
			break;
		}
		return value;
	}
}
