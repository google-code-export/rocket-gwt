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
package rocket.logging.compiler;

import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JMethodCall;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JStringLiteral;

/**
 * A simple visitor to the AST tree that replaces all LoggerFactory.getLogger()
 * statements whereever possible when name to logger resolution can occur at
 * compile time.
 * 
 * @author Miroslav Pokorny
 */
public abstract class LoggerOptimiser extends JModVisitor {

	public LoggerOptimiser() {
		super();
	}

	public boolean execute() {
		this.didChange = false;
		this.accept(this.getProgram());
		return this.didChange();
	}

	/**
	 * A reference to the program being assembled.
	 */
	private JProgram program;

	protected JProgram getProgram() {
		if (null == program) {
			throw new IllegalStateException("The field:program is null");
		}

		return this.program;
	}

	public void setProgram(final JProgram program) {
		if (null == program) {
			throw new IllegalArgumentException("The parameter:program is null");
		}

		this.program = program;
	}

	private JMethod getLogger;

	protected JMethod getGetLogger() {
		return this.getLogger;
	}

	public void setGetLogger(final JMethod getLogger) {
		this.getLogger = getLogger;
	}

	// @Override
	public void endVisit(final JMethodCall methodCall, final Context context) {
		while (true) {
			final JMethod method = methodCall.getTarget();
			// its definitely not the LoggerFactory.getLogger(String) method...
			if (method != this.getGetLogger()) {
				break;
			}

			// complain if the method doesnt have exactly 1 argument...
			assert (methodCall.getArgs().size() == 1);

			// check if the argument to the method is a string literal...
			final JExpression arg = (JExpression) methodCall.getArgs().get(0);

			// cant continue if argument is not a StringLiteral... this request
			// will have to be resolved at runtime.
			if (false == arg instanceof JStringLiteral) {
				break;
			}

			this.adjustMethod(methodCall, context);
			break;
		}
	}

	abstract protected void adjustMethod(final JMethodCall methodCall, final Context context);

	protected JClassType getClassType(final String typeName) {
		final JClassType logger = (JClassType) this.getProgram().getFromTypeMap(typeName);
		assert (null != logger);
		return logger;
	}

	void log(final String message) {
		// System.out.println(message);
	}
}
