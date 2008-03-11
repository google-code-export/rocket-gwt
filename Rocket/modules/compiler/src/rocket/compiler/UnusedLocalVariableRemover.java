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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rocket.util.client.Checker;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.SourceInfo;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JArrayType;
import com.google.gwt.dev.jjs.ast.JBinaryOperation;
import com.google.gwt.dev.jjs.ast.JBinaryOperator;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JInterfaceType;
import com.google.gwt.dev.jjs.ast.JLocalDeclarationStatement;
import com.google.gwt.dev.jjs.ast.JLocalRef;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JTryStatement;
import com.google.gwt.dev.jjs.ast.JVariable;
import com.google.gwt.dev.jjs.ast.JVariableRef;
import com.google.gwt.dev.jjs.ast.JVisitor;
import com.google.gwt.dev.jjs.ast.js.JMultiExpression;

/**
 * This optimiser works by removing local variables providing they are not
 * referenced anywhere in the same method.
 * 
 * 
 * eg int foo(){ int a = 0; // declaration and assignment of "a" can be safely
 * removed int b = 0; int c = 0;
 * 
 * int d = bar();// remove the declaration of "d" but leave the call to bar().
 * 
 * return b * c; }
 * 
 * 
 * @author Miroslav Pokorny
 */
public class UnusedLocalVariableRemover implements JavaCompilationWorker{

	final static boolean VISIT_CHILD_NODES = true;

	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.INFO, this.getClass().getName(), null);
		final boolean changed = this.visitAllLocalVariables(jprogram, logger);
		branch.log(TreeLogger.DEBUG, changed ? "One or more changes were made." : "No changes were committed.", null);
		return changed;
	}

	/**
	 * Visits all local variables and determines which may / should be removed
	 * along with any pointless expression assignments.
	 * 
	 * @param program
	 * @param logger
	 * @return Returns true if the AST was modified, otherwise returns false.
	 */
	protected boolean visitAllLocalVariables(final JProgram program, final TreeLogger logger) {

		// this visitor is used to count the number of assignments, and
		// references for each local variable within a particular method.
		final LocalVariableFinder visitor = new LocalVariableFinder();
		visitor.setLogger(logger);
		visitor.accept(program);

		return visitor.didChange();
	}

	/**
	 * This visitor may be used to find all local variables within a method.
	 */
	private class LocalVariableFinder extends ClassMethodVisitor {
		
		public void setLogger( final TreeLogger logger ){
			this.setClassTypeLogger(logger);
			this.setMethodLogger( logger );
		}
		
//		public boolean visit( final JArrayType array, final Context context ){
//			return !VISIT_CHILD_NODES;
//		}
//		
//		public boolean visit( final JInterfaceType interfacee, final Context context ){
//			return !VISIT_CHILD_NODES;
//		}
		
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
		 * Resets the local variables within this method.
		 */
		public boolean visitClassMethod(final JMethod method, final Context context) {
			this.setMethod(method);
			this.setLocalVariables(new HashMap());

			final TreeLogger logger = this.getMethodLogger();
			final TreeLogger branch = logger.isLoggable(TreeLogger.DEBUG) ? logger.branch(TreeLogger.DEBUG, Compiler.getMethodName(method), null) : TreeLogger.NULL;
			this.setLocalVariableLogger(branch);

			return VISIT_CHILD_NODES;
		}

		/**
		 * Add the given variable to the set of local variables. If it remains
		 * in the set when the end method visitor occurs then it can be safely
		 * removed.
		 */
		public boolean visit(final JLocalDeclarationStatement localDeclaration, final Context context) {
			final JVariable variable = localDeclaration.getLocalRef().getTarget();

			// a declaration does not contain a binary operation.
			this.declareLocalVariable(variable);
			this.incrementAssignmentCount(variable);
			return VISIT_CHILD_NODES;
		}

		/**
		 * If the given binary operation is actually an assignment of a local
		 * variable then increment its assignment counter...
		 */
		public boolean visit(final JBinaryOperation binaryOperation, final Context context) {

			while (true) {
				// left hand side is not a variable reference...
				final JExpression leftHandSide = binaryOperation.getLhs();
				if (false == leftHandSide instanceof JLocalRef) {
					break;
				}
				// not an assignment...
				if (false == binaryOperation.getOp().equals(JBinaryOperator.ASG)) {
					break;
				}

				final JLocalRef reference = (JLocalRef) leftHandSide;
				final JVariable variable = reference.getTarget();
				this.incrementAssignmentCount(variable);

				final TreeLogger branch = this.getLocalVariableLogger();
				if (branch.isLoggable(TreeLogger.DEBUG)) {
					branch.log(TreeLogger.DEBUG, Compiler.getSource(binaryOperation), null);
				}
				break;
			}

			return VISIT_CHILD_NODES;
		}

		/**
		 * Handles the special case where a catch defines a local variable for
		 * the caught exception.
		 */
		public boolean visit(final JTryStatement tryStatement, final Context context) {
			final JVisitor visitor = new JVisitor() {
				public boolean visit(final JLocalRef reference, final Context context) {
					final JVariable variable = reference.getTarget();

					LocalVariableFinder.this.declareLocalVariable(variable);
					LocalVariableFinder.this.incrementAssignmentCount(variable);
					LocalVariableFinder.this.incrementReferenceCount(variable);

					return VISIT_CHILD_NODES;
				}
			};
			visitor.accept(tryStatement.getCatchArgs());

			return VISIT_CHILD_NODES;
		}

		/**
		 * Found a reference increment its reference count.
		 * 
		 * @param reference
		 * @param context
		 * @return
		 */
		public boolean visit(final JLocalRef reference, final Context context) {
			final JVariable variable = reference.getTarget();
			this.incrementReferenceCount(variable);

			return VISIT_CHILD_NODES;
		}

		/**
		 * Removes any un needed local variables.
		 * 
		 * @param method
		 */
		public void endVisitClassMethod(final JMethod method, final Context context){
			final Map variables = this.getLocalVariables();
			final TreeLogger logger = this.getLocalVariableLogger();
			UnusedLocalVariableRemover.this.processLocalVariables(method, variables.values(), logger);

			this.clearMethod();
			this.clearLocalVariables();
			this.clearLocalVariableLogger();
		}

		/**
		 * The current method being visited.
		 */
		private JMethod method;

		JMethod getMethod() {
			Checker.notNull("field:method", method);
			return this.method;
		}

		void setMethod(final JMethod method) {
			Checker.notNull("parameter:method", method);
			this.method = method;
		}

		void clearMethod() {
			this.method = null;
		}
		
		/**
		 * This logger is used exclusively to log method messages.
		 */
		TreeLogger methodLogger;

		TreeLogger getMethodLogger() {
			Checker.notNull("field:methodLogger", methodLogger);
			return this.methodLogger;
		}

		void setMethodLogger(TreeLogger methodLogger) {
			Checker.notNull("parameter:methodLogger", methodLogger);
			this.methodLogger = methodLogger;
		}
		
		void clearMethodLogger(){
			this.methodLogger = null;
		}

		/**
		 * This map aggregates and will when the end visit of the method occurs
		 * contain local variables that can be safely removed.
		 */
		private Map localVariables;

		Map getLocalVariables() {
			Checker.notNull("field:localVariables", localVariables);
			return this.localVariables;
		}

		void setLocalVariables(final Map localVariables) {
			Checker.notNull("parameter:localVariables", localVariables);
			this.localVariables = localVariables;
		}

		void clearLocalVariables() {
			this.localVariables = null;
		}

		/**
		 * Records that a local variable was found.
		 * 
		 * @param jvariable
		 */
		void declareLocalVariable(final JVariable jvariable) {
			Checker.notNull("parameter:jvariable", jvariable);

			final LocalVariable local = new LocalVariable();
			local.setVariable(jvariable);
			local.setAssignmentCount(0);
			local.setReferenceCount(0);

			final String name = jvariable.getName();
			this.getLocalVariables().put( jvariable, local);
		}

		void incrementAssignmentCount(final JVariable jvariable) {
			Checker.notNull("parameter:jvariable", jvariable);
			
			final LocalVariable local = (LocalVariable) this.getLocalVariables().get( jvariable );
			if (null == local) {
				final String name = jvariable.getName();
				throw new AssertionError("Unable to find local variable \"" + name + "\".");
			}
			local.setAssignmentCount(local.getAssignmentCount() + 1);
		}

		void incrementReferenceCount(final JVariable jvariable) {
			Checker.notNull("parameter:jvariable", jvariable);
		
			final LocalVariable local = (LocalVariable) this.getLocalVariables().get( jvariable );
			if (null == local) {
				final String name = jvariable.getName();
				throw new AssertionError("Unable to find local variable \"" + name + "\".");
			}
			local.setReferenceCount(local.getReferenceCount() + 1);
		}

		/**
		 * This logger is used exclusively to log local variables.
		 */
		TreeLogger localVariableLogger;

		TreeLogger getLocalVariableLogger() {
			Checker.notNull("field:localVariableLogger", localVariableLogger);
			return this.localVariableLogger;
		}

		void setLocalVariableLogger(TreeLogger localVariableLogger) {
			Checker.notNull("parameter:localVariableLogger", localVariableLogger);
			this.localVariableLogger = localVariableLogger;
		}
		
		void clearLocalVariableLogger(){
			this.localVariableLogger = null;
		}

	}

	/**
	 * Keeps track of various stats about a particular local variable.
	 */
	static class LocalVariable {
		JVariable variable;

		public JVariable getVariable() {
			Checker.notNull("field:variable", variable);
			return this.variable;
		}

		public void setVariable(final JVariable variable) {
			Checker.notNull("parameter:variable", variable);
			this.variable = variable;
		}

		private int referenceCount;

		public int getReferenceCount() {
			return this.referenceCount;
		}

		public void setReferenceCount(final int referenceCount) {
			this.referenceCount = referenceCount;
		}

		private int assignmentCount;

		public int getAssignmentCount() {
			return this.assignmentCount;
		}

		public void setAssignmentCount(final int assignmentCount) {
			this.assignmentCount = assignmentCount;
		}

		public String toString() {
			return super.toString() + ", variable: \"" + this.getVariable().getName() + "\", referenceCount: " + this.referenceCount
					+ ", assignmentCount: " + this.assignmentCount;
		}
	}

	/**
	 * After visiting a method attempt to remove and simply expressions
	 * involving any of the given variable whereever possible.
	 * 
	 * @param method
	 * @param localVariables
	 * @param logger
	 * @return
	 */
	protected boolean processLocalVariables(final JMethod method, final Collection localVariables, final TreeLogger logger) {
		Checker.notNull("parameter:method", method);
		Checker.notNull("parameter:localVariables", localVariables);
		Checker.notNull("parameter:logger", logger);

		boolean changed = false;

		final Iterator i = localVariables.iterator();
		while (i.hasNext()) {
			final LocalVariable localVariable = (LocalVariable) i.next();
			final int assignmentCount = localVariable.getAssignmentCount();
			final int referenceCount = localVariable.getReferenceCount();
			final String name = localVariable.getVariable().getName();

			if (referenceCount > 0 && assignmentCount == 0) {
				throw new AssertionError("If reference count for \"" + name + "\" is " + referenceCount
						+ " the assignment count cant be 0.");
			}

			final TreeLogger branch = logger.branch(TreeLogger.DEBUG, name, null);

			final int nonAssignmentReferences = referenceCount - assignmentCount;
			if (nonAssignmentReferences > 0) {

				if (branch.isLoggable(TreeLogger.DEBUG)) {
					branch.log(TreeLogger.DEBUG, "Cannot be removed because appears in " + nonAssignmentReferences
							+ " expressions, assignments: " + assignmentCount + ", references: " + referenceCount, null);
				}
				continue;
			}

			// variable may be assigned more than once but never is part of the
			// right hand side of an expression
			changed = changed || this.removeLocalVariable(method, localVariable.getVariable(), branch);
		}

		return changed;
	}

	/**
	 * Attempts to remove a local variable's declaration and any remove or
	 * simply any assignments.
	 * 
	 * @param method
	 * @param variable
	 * @param logger
	 * @return
	 */
	protected boolean removeLocalVariable(final JMethod method, final JVariable variable, final TreeLogger logger) {
		Checker.notNull("parameter:method", method);
		Checker.notNull("parameter:jvariable", variable);
		Checker.notNull("parameter:logger", logger);

		final String name = variable.getName();
		final TreeLogger branch = logger.isLoggable(TreeLogger.DEBUG) ? logger.branch(TreeLogger.DEBUG,
				"Attempting to remove all occurances of local variable \"" + name + "\".", null) : TreeLogger.NULL;

		final JModVisitor visitor = new JModVisitor() {
			public boolean visit(final JLocalDeclarationStatement declaration, final Context context) {
				UnusedLocalVariableRemover.this.processDeclaration(declaration, context, variable, branch);

				return VISIT_CHILD_NODES;
			}

			public boolean visit(final JBinaryOperation binaryOperation, final Context context) {
				UnusedLocalVariableRemover.this.processBinaryOperation(binaryOperation, context, variable, branch);
				return VISIT_CHILD_NODES;
			}
		};
		visitor.accept(method);
		return visitor.didChange();
	}

	/**
	 * If the declaration has no initializer or the initializer has no side
	 * effects then remove the declaration altogether. If the initializer has
	 * side effects then replace the declaration with just the initializer
	 * expression.
	 * 
	 * @param declaration
	 * @param context
	 * @param variable
	 * @param logger
	 */
	protected void processDeclaration(final JLocalDeclarationStatement declaration, final Context context, final JVariable variable,
			final TreeLogger logger) {
		Checker.notNull("parameter:declaration", declaration);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:variable", variable);
		Checker.notNull("parameter:logger", logger);

		while (true) {
			final JVariable declaredVariable = declaration.getLocalRef().getTarget();
			// wrong variable...
			if (declaredVariable != variable) {
				break;
			}

			final JExpression initializer = declaration.getInitializer();
			if (null == initializer) {
				this.removeDeclaration(declaration, context, logger);
				break;
			}

			if (false == initializer.hasSideEffects()) {
				this.removeDeclaration(declaration, context, logger);
				break;
			}

			// cant lose initializer need to replace declaration with
			// initializer.
			this.removeLocalVariableDeclarationButKeepInitializerExpression(declaration, context, logger);
			break;
		}
	}

	/**
	 * Removes a declaration which may have an initializer with no side effects
	 * which is also removed.
	 * 
	 * @param declaration
	 * @param context
	 * @param logger
	 */
	protected void removeDeclaration(final JLocalDeclarationStatement declaration, final Context context, final TreeLogger logger) {
		Checker.notNull("parameter:declaration", declaration);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:logger", logger);

		final boolean hasInitializerWithNoSideEffects = declaration.getInitializer() != null;

		if (context.canRemove()) {

			if (logger.isLoggable(TreeLogger.DEBUG)) {
				final String name = declaration.getLocalRef().getTarget().getName();

				if (hasInitializerWithNoSideEffects) {
					logger.log(TreeLogger.DEBUG, "Removing declaration of \"" + name + "\" which does not have an initializer, source: "
							+ Compiler.getSource(declaration), null);
				} else {
					logger.log(TreeLogger.DEBUG, "Removing declaration of \"" + name
							+ "\" along with initializer which has no side effects., source: " + Compiler.getSource(declaration), null);
				}
			}

			context.removeMe();

		} else {
			// replace with an empty block.
			if (logger.isLoggable(TreeLogger.DEBUG)) {
				final String name = declaration.getLocalRef().getTarget().getName();
				if (hasInitializerWithNoSideEffects) {
					logger.log(TreeLogger.DEBUG, "Removing declaration of \"" + name
							+ "\" which does not have an initializer (replacing with an empty block), source: "
							+ Compiler.getSource(declaration), null);
				} else {
					logger.log(TreeLogger.DEBUG, "Removing declaration of \"" + name
							+ "\" along with initializer which has no side effects (replacing with an empty block), source: "
							+ Compiler.getSource(declaration), null);
				}
			}

			final JProgram program = declaration.getJProgram();
			final SourceInfo sourceInfo = declaration.getSourceInfo();
			final JMultiExpression empty = new JMultiExpression(program, sourceInfo);
			context.replaceMe(empty.makeStatement());
		}
	}

	/**
	 * Eliminates a declaration but leaves the initializer statement/expression
	 * because it has side effects.
	 * 
	 * @param declaration
	 * @param context
	 * @param logger
	 */
	protected void removeLocalVariableDeclarationButKeepInitializerExpression(final JLocalDeclarationStatement declaration,
			final Context context, final TreeLogger logger) {
		Checker.notNull("parameter:declaration", declaration);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:logger", logger);

		if (logger.isLoggable(TreeLogger.DEBUG)) {
			final String name = declaration.getLocalRef().getTarget().getName();
			logger.log(TreeLogger.DEBUG, "Replacing declaration of \"" + name
					+ "\" with just the initializer which cant be eliminated because it has side effects, source: "
					+ Compiler.getSource(declaration), null);
		}

		final JExpression initializer = declaration.getInitializer();
		if (false == initializer.hasSideEffects()) {
			throw new AssertionError(
					"The declaration containing an initializer with no side effects should have been eliminated and not kept.");
		}
		context.replaceMe(initializer.makeStatement());
	}

	/**
	 * If the binary operation is an assignment to the given local variable and
	 * the right hand side has no side effect then remove it entirely. If the
	 * right hand side has effect then replace the binary operation ( aka the
	 * assignment) with the right hand side expression.
	 * 
	 * @param binaryOperation
	 * @param context
	 * @param variable
	 * @param logger
	 */
	protected void processBinaryOperation(final JBinaryOperation binaryOperation, final Context context, final JVariable variable,
			final TreeLogger logger) {
		Checker.notNull("parameter:binaryOperation", binaryOperation);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:variable", variable);
		Checker.notNull("parameter:logger", logger);

		while (true) {
			// if the left hand side is not a variable reference...
			final JExpression leftHandSide = binaryOperation.getLhs();
			if (false == leftHandSide instanceof JVariableRef) {
				break;
			}

			// wrong variable ?
			final JVariableRef reference = (JVariableRef) leftHandSide;
			final JVariable leftHandSideVariable = reference.getTarget();
			if (false == leftHandSideVariable.equals(variable)) {
				break;
			}

			// if this binary operation is not an assignment...
			final JBinaryOperator binaryOperator = binaryOperation.getOp();
			if (false == JBinaryOperator.EQ.equals(binaryOperator)) {
				break;
			}

			// if the right hand side expression has side effects keep it
			// overwriting binary operation...
			final JExpression rightHandSide = binaryOperation.getRhs();
			if (rightHandSide.hasSideEffects()) {
				this.replaceAssignmentWithExpression(binaryOperation, context, rightHandSide, logger);
				break;
			}

			this.removeAssignment(binaryOperation, context, logger);
			break;
		}
	}

	/**
	 * Removes an assignment entirely.
	 * 
	 * @param binaryOperation
	 * @param context
	 * @param logger
	 */
	protected void removeAssignment(final JBinaryOperation binaryOperation, final Context context, final TreeLogger logger) {
		Checker.notNull("parameter:binaryOperation", binaryOperation);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:logger", logger);

		if (context.canRemove()) {

			if (logger.isLoggable(TreeLogger.DEBUG)) {
				logger.log(TreeLogger.DEBUG,
						"Removing expression because it references unused variable and has no side effects, source: "
								+ Compiler.getSource(binaryOperation), null);
			}
			context.removeMe();

		} else {
			if (logger.isLoggable(TreeLogger.DEBUG)) {
				logger.log(TreeLogger.DEBUG,
						"Removing expression because it references unused variable and has no side effects with an empty expression, source: "
								+ Compiler.getSource(binaryOperation), null);
			}

			// replace with an empty block.

			final JProgram program = binaryOperation.getJProgram();
			final SourceInfo sourceInfo = binaryOperation.getSourceInfo();
			final JMultiExpression empty = new JMultiExpression(program, sourceInfo);
			context.replaceMe(empty);
		}
	}

	/**
	 * Removes an assignment but keeps the expression because it has side
	 * effects.
	 * 
	 * @param binaryOperation
	 * @param context
	 * @param expression
	 */
	protected void replaceAssignmentWithExpression(final JBinaryOperation binaryOperation, final Context context,
			final JExpression expression, final TreeLogger logger) {
		Checker.notNull("parameter:binaryOperation", binaryOperation);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:expression", expression);
		Checker.notNull("parameter:logger", logger);

		if (logger.isLoggable(TreeLogger.DEBUG)) {
			logger.log(TreeLogger.DEBUG, "Removing assignment but keeping expression due to it having side effects, source: "
					+ Compiler.getSource(binaryOperation), null);
		}

		context.replaceMe(expression);
	}

}
