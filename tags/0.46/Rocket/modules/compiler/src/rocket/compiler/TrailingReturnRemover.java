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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rocket.util.client.Checker;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JMethodBody;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JReturnStatement;
import com.google.gwt.dev.jjs.ast.JType;

/**
 * This optimiser attempts to remove any return statements that appear as the
 * last statement within a method.
 * 
 * public void foo(){ a() b() return; // <- can safely be removed. }
 * 
 * @author Miroslav Pokorny
 */
public class TrailingReturnRemover implements JavaCompilationWorker {

	private final static boolean VISIT_CHILD_NODES = true;
	
	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.INFO, this.getClass().getName(), null);

		final TrailingReturnMethodVisitor visitor = new TrailingReturnMethodVisitor();
		visitor.setLogger( branch );
		visitor.accept(jprogram);

		final boolean changed = visitor.didChange();
		logger.log(TreeLogger.DEBUG, changed ? "One or more changes were made." : "No changes were committed.", null);
		return changed;
	}
	
	/**
	 * This visitor visits all methods attempting to locate and remove those with unnecessary trailing return statements.
	 */
	class TrailingReturnMethodVisitor extends ClassMethodVisitor{
		
		public void setLogger( final TreeLogger logger ){
			this.setClassTypeLogger( logger );
		}
		
		public boolean visit( final JClassType type, final Context context ){
			final TreeLogger logger = this.getClassTypeLogger().branch( TreeLogger.DEBUG, type.getName(), null );
			this.setMethodLogger( logger );
			return VISIT_CHILD_NODES;
		}
		
		public void endVisit( final JClassType type, final Context context ){
			this.clearMethodLogger();
		}
		
		/**
		 * This logger is to be used exclusively to log local variable messages.
		 */
		private TreeLogger classTypeLogger;
		
		protected TreeLogger getClassTypeLogger(){
			Checker.notNull("field:classTypeLogger", classTypeLogger );
			return this.classTypeLogger;
		}
		
		protected void setClassTypeLogger( final TreeLogger classTypeLogger ){
			Checker.notNull("parameter:classTypeLogger", classTypeLogger );
			this.classTypeLogger = classTypeLogger;
		}
		
		/**
		 * A new method resets local variables.
		 */
		public boolean visitClassMethod( final JMethod method, final Context context ){
			TreeLogger logger = this.getMethodLogger();
			TreeLogger branch = TreeLogger.NULL;
			if (logger.isLoggable(TreeLogger.DEBUG)) {
				branch = logger.branch(TreeLogger.DEBUG, Compiler.getMethodName(method), null);
			}

			if (TrailingReturnRemover.this.isConcreteMethod(method, branch )) {
				if (TrailingReturnRemover.this.attemptToRemoveReturnStatements(method, context, branch )) {
					this.didChange = true;
				}
			}
			return VISIT_CHILD_NODES;			
		}		
		
		/**
		 * This logger is to used to log method names as they are encountered.
		 */
		private TreeLogger methodLogger;
		
		protected TreeLogger getMethodLogger(){
			Checker.notNull("field:methodLogger", methodLogger );
			return this.methodLogger;
		}
		
		protected void setMethodLogger( final TreeLogger methodLogger ){
			Checker.notNull("parameter:methodLogger", methodLogger );
			this.methodLogger = methodLogger;
		}
		
		protected void clearMethodLogger(){
			this.methodLogger = null;
		}
		
		public void endVisitClassMethod( final JMethod method, final Context context ){
		}
	}
		

	/**
	 * Tests if a given method potentially have a body and therefore be
	 * optimisable.
	 * 
	 * @param method
	 * @param logger
	 * @return
	 */
	protected boolean isConcreteMethod(final JMethod method, final TreeLogger logger) {
		boolean removable = false;

		while (true) {
			if (method.isAbstract()) {
				logger.branch(TreeLogger.DEBUG, "Abstract methods have no body", null);
				break;
			}
			if (method.isNative()) {
				logger.branch(TreeLogger.DEBUG, "Native methods are left alone.", null);
				break;
			}
			if (method.isConstructor()) {
				logger.branch(TreeLogger.DEBUG, "Constructors are left alone.", null);
				break;
			}

			removable = true;
			break;
		}

		return removable;
	}

	/**
	 * Attempts to locate all the return statements that may be removed.
	 * 
	 * @param method
	 * @param context
	 * @param logger
	 * @return Returns true if a return statement was removed successfully,
	 *         false otherwise.
	 */
	protected boolean attemptToRemoveReturnStatements(final JMethod method, final Context context, final TreeLogger logger) {
		boolean removed = false;

		while (true) {
			final JMethodBody methodBody = (JMethodBody) method.getBody();
			final List list = methodBody.getStatements();
			final int statementCount = list.size();
			if (statementCount == 0) {
				logger.log(TreeLogger.DEBUG, "Empty method left alone - not modified.", null);
				break;
			}

			final Object lastStatement = list.get(statementCount - 1);
			if (false == lastStatement instanceof JReturnStatement) {
				logger.log(TreeLogger.DEBUG, "Method does not contain trailing return statement - not modified.", null);
				break;
			}

			final JReturnStatement returnStatement = (JReturnStatement) lastStatement;
			final JExpression expression = returnStatement.getExpr();
			if (null != expression) {
				logger.log(TreeLogger.DEBUG, "Method contains expression - not modified.", null);
				break;
			}

			final JModVisitor visitor = new JModVisitor() {
				public boolean visit(final JReturnStatement visitingReturnStatement, final Context context) {
					if (visitingReturnStatement == returnStatement) {
						TrailingReturnRemover.this.removeReturnStatement(returnStatement, context, logger);
					}
					return false; // dont visit children...
				}
			};
			visitor.accept(method);

			// did the remove work ?
			removed = visitor.didChange();
			break;
		}

		return removed;
	}

	/**
	 * Removes the actual return statement...
	 * 
	 * @param returnStatement
	 * @param context
	 * @param method
	 */
	protected void removeReturnStatement(final JReturnStatement returnStatement, final Context context, final TreeLogger logger) {
		if (context.canRemove()) {
			context.removeMe();
			logger.log(TreeLogger.DEBUG, "Removed trailing return statement - modified", null);
		} else {
			logger.log(TreeLogger.DEBUG, "Unable to remove trailing return statement - not modified.", null);
		}
	}
}