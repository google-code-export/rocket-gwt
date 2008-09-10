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

import java.util.Iterator;
import java.util.List;

import rocket.logging.client.NoneLevelLogger;

import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JMethodCall;
import com.google.gwt.dev.jjs.ast.JNewInstance;
import com.google.gwt.dev.jjs.ast.JProgram;

public class NoneLoggingFactoryGetLoggerOptimiser extends LoggerOptimiser {

	public NoneLoggingFactoryGetLoggerOptimiser() {
		super();
	}

	@Override
	protected void adjustMethod(final JMethodCall methodCall, final Context context) {
		final JClassType logger = this.getNoneLevelLogger();
		final JMethod loggerConstructor = this.findLevelLoggerConstructorMethod(logger);
		log("loggerConstructor -> " + loggerConstructor);

		// inserts a new xxxLevelLogger( Logger )
		final JProgram program = this.getProgram();
		final JNewInstance noneLevelLogger = new JNewInstance(program, methodCall.getSourceInfo(), logger);
		JMethodCall call = new JMethodCall(program, methodCall.getSourceInfo(), noneLevelLogger, loggerConstructor);

		context.replaceMe(call);
	}

	protected JClassType getNoneLevelLogger() {
		return this.getClassType(NoneLevelLogger.class.getName());
	}

	protected JMethod findLevelLoggerConstructorMethod(final JClassType type) {
		log("" + type);

		JMethod constructor = null;
		final String constructorName = type.getShortName();
		final Iterator methods = type.methods.iterator();
		while (methods.hasNext()) {
			final JMethod method = (JMethod) methods.next();
			log("" + method);

			if (false == constructorName.equals(method.getName())) {
				continue;
			}
			final List parameters = method.params;
			if (parameters.size() != 0) {
				continue;
			}

			constructor = method;
			break;
		}

		if (null == constructor) {
			throw new AssertionError("Unable to find a constructor with a Logger parameter for the logger type: " + type);
		}
		return constructor;
	}
}
