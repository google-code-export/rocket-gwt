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
import com.google.gwt.dev.jjs.ast.JArrayType;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JField;
import com.google.gwt.dev.jjs.ast.JInterfaceType;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JNode;
import com.google.gwt.dev.jjs.ast.JPrimitiveType;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JReferenceType;
import com.google.gwt.dev.jjs.ast.JType;

/**
 * Convenient visitor that visits all methods, using a delayed logger to output classes, methods
 * just in case something is found and needs to be actioned / logged
 * @author Miroslav Pokorny
 */
abstract public class LoggingJModVisitor extends JModVisitor {
	
	final static boolean VISIT_CHILD_NODES = true;
	
	public void accept( final JProgram jprogram, final TreeLogger logger ){
		this.setTypeLogger( logger );
		this.accept(jprogram);
	}
	
	private TreeLogger typeLogger;
	
	TreeLogger getTypeLogger(){
		Checker.notNull("field:typeLogger", typeLogger );
		return this.typeLogger;
	}

	void setTypeLogger(final TreeLogger typeLogger ){
		Checker.notNull("parameter:typeLogger", typeLogger );
		this.typeLogger = typeLogger;
	}
	
	public boolean visit( final JClassType type, final Context context ){
		this.logType(type);
		return VISIT_CHILD_NODES;
	}
	
	public boolean visit( final JArrayType type, final Context context ){
		this.logType(type);
		return VISIT_CHILD_NODES;
	}
	
	public boolean visit( final JInterfaceType type, final Context context ){
		this.logType(type);
		return VISIT_CHILD_NODES;
	}
	
	public boolean visit( final JPrimitiveType type, final Context context ){
		this.logType(type);
		return VISIT_CHILD_NODES;
	}
	
	protected void logType( final JType type ){
		TreeLogger branch = TreeLogger.NULL;
		final TreeLogger logger = this.getTypeLogger();
		final TreeLogger.Type level = this.getLoggingLevel( type );
		if( logger.isLoggable( level )){			
			branch = TreeLoggers.delayedBranch(logger, level, type.getName(), null );
		}
		this.setMethodLogger( branch );
		this.setLogger( branch );
	}	
	
	public boolean visit( final JMethod method, final Context context ){
		TreeLogger branch = TreeLogger.NULL;
		
		final JReferenceType enclosingType = method.getEnclosingType();		
		final TreeLogger logger = null == enclosingType ? this.getTypeLogger() : this.getMethodLogger();
		final TreeLogger.Type level = this.getLoggingLevel( method );
		if( logger.isLoggable( level )){
			branch = logger.branch( level, Compiler.getMethodName(method), null );
		}
		this.setLogger( branch );
		
		return VISIT_CHILD_NODES;
	}
	
	public void endVisit(final JMethod method, final Context context){
	}
			
	private TreeLogger methodLogger;
	
	protected TreeLogger getMethodLogger(){
		Checker.notNull("field:methodLogger", methodLogger );
		return this.methodLogger;
	}

	protected void setMethodLogger(final TreeLogger methodLogger ){
		Checker.notNull("parameter:methodLogger", methodLogger );
		this.methodLogger = methodLogger;
	}
	
	protected void clearMethodLogger(){
		this.methodLogger = null;
	}
	
	public boolean visit( final JField field, final Context context ){
		final TreeLogger logger = this.getTypeLogger();
		final TreeLogger.Type level = this.getLoggingLevel( field );
		TreeLogger branch = TreeLogger.NULL;
		if( logger.isLoggable( level )){
			branch = TreeLoggers.delayedBranch( logger, level, Compiler.getSource(field), null );
		}
		this.setLogger(logger);
		return VISIT_CHILD_NODES;
	}
	
	public void endVisit( final JField field, final Context context ){
	}
	
	private TreeLogger logger;
	
	protected TreeLogger getLogger(){
		Checker.notNull("field:logger", logger );
		return this.logger;
	}

	protected void setLogger(final TreeLogger logger ){
		Checker.notNull("parameter:logger", logger );
		this.logger = logger;
	}
	
	protected void clearLogger(){
		this.logger = null;
	}
	
	abstract protected TreeLogger.Type getLoggingLevel( JNode node );
}
