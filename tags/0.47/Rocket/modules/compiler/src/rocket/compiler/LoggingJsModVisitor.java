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
package rocket.compiler;

import rocket.util.client.Checker;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.js.ast.JsFunction;
import com.google.gwt.dev.js.ast.JsModVisitor;
import com.google.gwt.dev.js.ast.JsNode;
import com.google.gwt.dev.js.ast.JsProgram;

/**
 * Convenient visitor that visits all methods, using a delayed logger to the enclosing function name when a logging statements is made.
 * This is useful when one wishes to add the enclosing function and lazily logging it when something of interest occurs within processing of that function's child nodes.
 * @author Miroslav Pokorny
 */
abstract public class LoggingJsModVisitor extends JsModVisitor {
	
	final static boolean VISIT_CHILD_NODES = true;
	
	public void accept( final JsProgram jprogram, final TreeLogger logger ){
		this.setFunctionLogger( logger );
		this.accept(jprogram);
	}
	
	private TreeLogger functionLogger;
	
	TreeLogger getFunctionLogger(){
		Checker.notNull("field:functionLogger", functionLogger );
		return this.functionLogger;
	}

	void setFunctionLogger(final TreeLogger functionLogger ){
		Checker.notNull("parameter:functionLogger", functionLogger );
		this.functionLogger = functionLogger;
	}
	
	public boolean visit( final JsFunction function, final Context context ){
		TreeLogger branch = TreeLogger.NULL;
		
		final TreeLogger logger = this.getFunctionLogger();
		final TreeLogger.Type level = this.getLoggingLevel( function );
		if( logger.isLoggable( level )){			
			branch = TreeLoggers.delayedBranch(logger, level, function.getName().getIdent(), null );
		}
		this.setLogger( branch );	
		return VISIT_CHILD_NODES;
	}
	
	public void endVisit(final JsFunction function, final Context context){
		this.clearLogger();
	}
	
	private TreeLogger logger;
	
	protected TreeLogger getLogger(){
		TreeLogger logger = this.logger;
		if( null == logger ){
			logger = this.getFunctionLogger();
		}
		
		Checker.notNull("field:logger", logger );
		return logger;
	}

	protected void setLogger(final TreeLogger logger ){
		Checker.notNull("parameter:logger", logger );
		this.logger = logger;
	}
	
	protected void clearLogger(){
		this.logger = null;
	}
	/**
	 * Sub classes must override this to provide the logging level that all enclosing function messages are sent to.
	 * @param node
	 * @return
	 */	
	abstract protected TreeLogger.Type getLoggingLevel( JsNode node );
}
