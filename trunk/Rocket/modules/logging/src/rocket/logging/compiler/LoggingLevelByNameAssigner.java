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

import rocket.logging.client.LoggingLevel;
import rocket.logging.util.LoggingFactoryConfig;

import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JMethodCall;
import com.google.gwt.dev.jjs.ast.JNewInstance;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JStringLiteral;

public class LoggingLevelByNameAssigner extends LoggerOptimiser {

	public LoggingLevelByNameAssigner() {
		super();
	}

	@Override
	protected void adjustMethod(final JMethodCall methodCall, final Context context) {
		final JStringLiteral stringLiteral = (JStringLiteral) methodCall.getArgs().get(0);
		final String name = stringLiteral.getValue();

		final JClassType logger = this.getLevelLogger(name);
		if (null == logger) {
			throw new IllegalStateException("Unable to fetch logger for \"" + name + "\".");
		}

		final JMethod loggerConstructor = this.findLevelLoggerConstructorMethod(logger);
		if (null == loggerConstructor) {
			throw new IllegalStateException("Unable to find constructor for type: " + logger);
		}

		// replace LoggerFactory.getLogger(String) with new XXXLevelLogger( new
		// WrappedLogger );
		final JClassType targetLogger = this.getTargetLogger(name);
		if (null == logger) {
			throw new IllegalStateException("Unable to fetch logger for \"" + name + "\".");
		}

		final JMethod targetLoggerConstructor = this.findTargetLoggerConstructorMethod(targetLogger);
		if (null == targetLoggerConstructor) {
			throw new IllegalStateException("Unable to find constructor for type: " + targetLogger);
		}

		// havent built new wrapped expression!
		final JProgram program = this.getProgram();

		final JNewInstance newTargetInstance = new JNewInstance(program, methodCall.getSourceInfo(), targetLogger);
		final JMethodCall callNewTargetInstance = new JMethodCall(program, methodCall.getSourceInfo(), newTargetInstance,
				targetLoggerConstructor);
		callNewTargetInstance.getArgs().add(program.getLiteralString(name));

		// inserts a new xxxLevelLogger( Logger )
		final JNewInstance newLevelLoggerInstance = new JNewInstance(program, methodCall.getSourceInfo(), logger);
		JMethodCall call = new JMethodCall(program, methodCall.getSourceInfo(), newLevelLoggerInstance, loggerConstructor);
		call.getArgs().add(callNewTargetInstance);

		context.replaceMe(call);
	}

	protected JClassType getLevelLogger(final String name) {
		final LoggingFactoryConfig config = this.getLoggingFactoryConfig();
		final LoggingLevel level = config.getLoggingLevel(name);
		return this.getClassType("rocket.logging.client." + level.toString() + "LevelLogger");
	}

	protected JMethod findLevelLoggerConstructorMethod(final JClassType type) {
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
			if (parameters.size() != 1) {
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

	protected JClassType getTargetLogger(final String name) {
		final LoggingFactoryConfig config = this.getLoggingFactoryConfig();
		final String className = config.getTypeName(name);
		return this.getClassType(className);
	}

	protected JMethod findTargetLoggerConstructorMethod(final JClassType type) {
		JMethod constructor = null;
		final String constructorName = type.getShortName();
		final Iterator methods = type.methods.iterator();
		while (methods.hasNext()) {
			final JMethod method = (JMethod) methods.next();
			if (false == constructorName.equals(method.getName())) {
				continue;
			}
			final List parameters = method.params;
			if (parameters.size() != 1) {
				continue;
			}

			constructor = method;
			break;
		}

		if (null == constructor) {
			throw new AssertionError("Unable to find a constructor with a string parameter for the logger type: " + type);
		}
		return constructor;
	}

	private LoggingFactoryConfig loggingFactoryConfig;

	protected LoggingFactoryConfig getLoggingFactoryConfig() {
		if (null == loggingFactoryConfig) {
			throw new IllegalStateException("The field:loggingFactoryConfig is null");
		}

		return this.loggingFactoryConfig;
	}

	public void setLoggingFactoryConfig(final LoggingFactoryConfig loggingFactoryConfig) {
		if (null == loggingFactoryConfig) {
			throw new IllegalArgumentException("The parameter:loggingFactoryConfig is null");
		}

		this.loggingFactoryConfig = loggingFactoryConfig;
	}
}
