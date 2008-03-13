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
import com.google.gwt.dev.jjs.ast.JBinaryOperation;
import com.google.gwt.dev.jjs.ast.JBlock;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JDoStatement;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JExpressionStatement;
import com.google.gwt.dev.jjs.ast.JForStatement;
import com.google.gwt.dev.jjs.ast.JIfStatement;
import com.google.gwt.dev.jjs.ast.JLocal;
import com.google.gwt.dev.jjs.ast.JLocalDeclarationStatement;
import com.google.gwt.dev.jjs.ast.JLocalRef;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JSwitchStatement;
import com.google.gwt.dev.jjs.ast.JVariable;
import com.google.gwt.dev.jjs.ast.JWhileStatement;
import com.google.gwt.dev.jjs.ast.js.JMultiExpression;

/**
 * This optimiser attempts to locate any local variables that are only ever set once and makes them final.
 * This allows CompoundAssignmentNormalizer to work more effectively as it can only use variables that are final when precalculating
 * values at compiler time as opposed to runtime.
 * 
 * This also has the added side effect that any local variables might have no references after their value is inlined and may be removed
 * by UnusedLocalVariableRemover.
 * 
 * @author Miroslav Pokorny
 * 
 * TODO Fix up loggers.
 */
public class LocalVariableFinalMaker implements JavaCompilationWorker {

	private final static boolean VISIT_CHILD_NODES = true;

	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.INFO, this.getClass().getName(), null);

		final FinalizableLocalVariableVisitor visitor = new FinalizableLocalVariableVisitor();
		visitor.setLogger(branch);
		visitor.accept( jprogram );

		final boolean changed = visitor.didChange();
		branch.log(TreeLogger.DEBUG, changed ? "One or more changes were made." : "No changes were committed.", null);
		return changed;
	}
	
	/**
	 * This visitor is responsible for locating all local variables and double checking that they are only assigned once
	 */
	class FinalizableLocalVariableVisitor extends ClassMethodVisitor{
		
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
			final TreeLogger logger = this.getMethodLogger().branch( TreeLogger.DEBUG, Compiler.getMethodName(method), null );
			this.setLocalVariableLogger(logger);
			
			this.setScope( 0 );
			this.setLocalVariables( new HashMap() );
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
		
		/**
		 * After visiting all the child nodes of a method make any remaining local variables final.
		 */
		public void endVisitClassMethod( final JMethod method, final Context context ){
			// only variables that can be final will remain...  
			final Map localVariables = this.getLocalVariables();
			final TreeLogger logger =this.getLocalVariableLogger();
			if( LocalVariableFinalMaker.this.makeLocalVariablesFinal( method, localVariables.values(), logger )){
				this.didChange = true;
			}
			this.clearLocalVariables();
			this.clearLocalVariableLogger();
		}
		
		/**
		 * This logger is to be used exclusively to log local variable messages.
		 */
		private TreeLogger localVariableLogger;
		
		protected TreeLogger getLocalVariableLogger(){
			Checker.notNull("field:localVariableLogger", localVariableLogger );
			return this.localVariableLogger;
		}
		
		protected void setLocalVariableLogger( final TreeLogger localVariableLogger ){
			Checker.notNull("parameter:localVariableLogger", localVariableLogger );
			this.localVariableLogger = localVariableLogger;
		}
		
		protected void clearLocalVariableLogger(){
			this.localVariableLogger = null;
		}
		
		  public boolean visit(final JBlock block, final Context context) {
			  this.incrementScope();
			  return VISIT_CHILD_NODES;
		  } 
		
		  public void endVisit(final JBlock block, final Context context) {
			  this.decrementScope();
		  }
		  
		  public boolean visit(final JDoStatement doStatement, final Context context) {
			  this.incrementScope();
			  return VISIT_CHILD_NODES;
		  } 
		
		  public void endVisit(final JDoStatement doStatement, final Context context) {
			  this.decrementScope();
		  }
		  public boolean visit(final JForStatement forStatement, final Context context) {
			  this.incrementScope();
			  return VISIT_CHILD_NODES;
		  } 
		
		  public void endVisit(final JForStatement forStatement, final Context context) {
			  this.decrementScope();
		  }
		  
		  public boolean visit(final JIfStatement ifStatement, final Context context) {
			  this.incrementScope();
			  return VISIT_CHILD_NODES;
		  } 
		
		  public void endVisit(final JIfStatement ifStatement, final Context context) {
			  this.decrementScope();
		  }

		  public boolean visit(final JSwitchStatement switchStatement, final Context context) {
			  this.incrementScope();
			  return VISIT_CHILD_NODES;
		  } 
		
		  public void endVisit(final JSwitchStatement switchStatement, final Context context) {
			  this.decrementScope();
		  }
		  
		  public boolean visit(final JWhileStatement whileStatement, final Context context) {
			  this.incrementScope();
			  return VISIT_CHILD_NODES;
		  } 
		
		  public void endVisit(final JWhileStatement whileStatement, final Context context) {
			  this.decrementScope();
		  }
		
		/**
		 * Records a new local variable.
		 */
		public boolean visit( final JLocalDeclarationStatement declaration, final Context context ){
			final JVariable jvariable = declaration.getLocalRef().getTarget();
			final String name = jvariable.getName();
			
			while( true ){
				final TreeLogger logger = this.getLocalVariableLogger();
				if( jvariable.isFinal() ){
					if( logger.isLoggable( TreeLogger.DEBUG )){
						logger.log( TreeLogger.DEBUG, "Local variable \"" + name + "\" is already final.", null );
					}
					break;
				}
				
				final LocalVariable localvariable = new LocalVariable();
				localvariable.setDeclaration( declaration );
				localvariable.setScope( this.getScope() );
				localvariable.setName( name );
				
				final Map locals = this.getLocalVariables();
				locals.put( jvariable , localvariable );
				
				final JExpression initializer = declaration.getInitializer();
				if( null != initializer ){
					localvariable.setExpression( initializer );
				}				
				
				if( logger.isLoggable( TreeLogger.DEBUG )){
					logger.log( TreeLogger.DEBUG, "Declaration \"" + Compiler.getSource( declaration ) + "\"", null );
				}		
				
				break;
			}
		
			return VISIT_CHILD_NODES;
		}
		
		/**
		 * Check all expression statements to determine if they have a binary operation which is in turn an assignment.
		 */
		public boolean visit( final JBinaryOperation binaryOperation, final Context context ){	
			while( true ){				
				if( false == binaryOperation.isAssignment() ){
					break;
				}
				
				// left hand side is a local reference...
				final JExpression left = binaryOperation.getLhs();
				if( false == left instanceof JLocalRef ){
					break;
				}
				
				// find the declaration of this variable...
				final JLocalRef localRef = (JLocalRef) left;
				final JVariable jvariable = localRef.getTarget();
				
				final Map localVariables = this.getLocalVariables();
				final LocalVariable localVariable = (LocalVariable)localVariables.get( jvariable );
				
				// variable not found must have been ruled out as a finalizable candidate...
				if( null == localVariable ){
					final TreeLogger logger = this.getLocalVariableLogger();
					if( logger.isLoggable( TreeLogger.DEBUG )){
						logger.log( TreeLogger.DEBUG, "Local variable \"" + jvariable.getName() + "\" has already been ruled out as finalizable, assignment: \"" + Compiler.getSource( binaryOperation ) + "\"", null );
					}
					break;
				}
				
				// if this assignment is at a different scope as the declaration its not optimisable..
				final int currentScope = this.getScope();
				final int variableScope = localVariable.getScope();
				if( variableScope != currentScope ){
					localVariables.remove( jvariable );
					
					final TreeLogger logger = this.getLocalVariableLogger();
					if( logger.isLoggable( TreeLogger.DEBUG )){
						logger.log( TreeLogger.DEBUG, "Cannot be made final because assignment occurs in different scope, assignment: \"" + Compiler.getSource( binaryOperation ) + "\"", null );
					}
					break;
				}
				
				// if this is the second assignment then variable cannot be optimised.
				JExpression localVariableExpression = localVariable.getExpression();
				if( null != localVariableExpression ){
					localVariables.remove( jvariable );
					
					final TreeLogger logger = this.getLocalVariableLogger();
					if( logger.isLoggable( TreeLogger.DEBUG )){
						logger.log( TreeLogger.DEBUG, "Cannot be finalized because local variable is assigned values more than once, additional assignment: \"" + Compiler.getSource( binaryOperation ) +"\".", null );
					}
					break;
				}
				
				// found variable assignment...
				localVariable.setExpression( binaryOperation.getRhs());
				
				final TreeLogger logger = this.getLocalVariableLogger();
				if( logger.isLoggable( TreeLogger.DEBUG )){
					logger.log( TreeLogger.DEBUG, "Initial value assignment \"" + Compiler.getSource( binaryOperation ) + "\"", null );
				}
				break;
			}
			
			return VISIT_CHILD_NODES;
		}
		
		/**
		 * Aggregates all the local variables and will contain the variables that can be made final.
		 */
		private Map localVariables;
		
		public Map getLocalVariables(){
			Checker.notNull("field:localVariables", localVariables );
			return this.localVariables;
		}
		
		protected void setLocalVariables( final Map localVariables ){
			Checker.notNull("parameter:localVariables", localVariables );
			this.localVariables = localVariables; 
		}
		
		protected void clearLocalVariables(){
			this.localVariables = null;
		}
		
		/**
		 * The current scope
		 */
		private int scope;

		public int getScope() {
			return this.scope;
		}

		public void setScope(final int scope) {
			this.scope = scope;
		}
		
		protected void incrementScope(){
			this.setScope( this.getScope() + 1 );
		}
		protected void decrementScope(){
			this.setScope( this.getScope() - 1 );
		}
	}
	
	/**
	 * Records a local variable the scope where it was declared and the assignment count.
	 */
	static private class LocalVariable {
		/**
		 * The name of the local variable.
		 */
		String name;

		public String getName() {
			Checker.notEmpty("field:name", name);
			return this.name;
		}

		public void setName(final String name) {
			Checker.notEmpty("parameter:name", name);
			this.name = name;
		}

		/**
		 * The scope enclosing the variable
		 */
		private int scope;

		public int getScope() {
			return this.scope;
		}

		public void setScope(final int scope) {
			this.scope = scope;
		}

		/**
		 * The variable declaration
		 */
		private JLocalDeclarationStatement declaration;
		
		public JLocalDeclarationStatement getDeclaration(){
			Checker.notNull("field:declaration", declaration );
			return this.declaration;
		}
		
		public void setDeclaration( final JLocalDeclarationStatement declaration ){
			Checker.notNull("parameter:declaration", declaration );
			this.declaration = declaration;
		}
		
		/**
		 * The expression containing the initial value of the variable.
		 */
		private JExpression expression;
		
		public JExpression getExpression(){
			return this.expression;
		}
		
		public void setExpression( final JExpression expression ){
			this.expression = expression;
		}

		public String toString() {
			return super.toString() + ", variable: \"" + this.name + "\", scope: " + this.scope + ", declaration: " + this.declaration + ", expression: " + expression;
		}
	}
	
	/**
	 * Makes all the given local variables final.
	 * @param method
	 * @param localVariables
	 * @param logger
	 * @return
	 */
	protected boolean makeLocalVariablesFinal( final JMethod method, final Collection localVariables, final TreeLogger logger ){
		Checker.notNull( "parameter:method", method );
		Checker.notNull( "parameter:localVariables", localVariables );
		Checker.notNull( "parameter:logger", logger );
		
		boolean changed = false;
		
		final Iterator i = localVariables.iterator();
		while( i.hasNext() ){
			final LocalVariable localVariable = (LocalVariable) i.next();
			final JLocalDeclarationStatement declaration = localVariable.getDeclaration();
			final JExpression expression = localVariable.getExpression();			
			
			final TreeLogger branch = logger.branch( TreeLogger.DEBUG, localVariable.getName(), null);
			if( null == expression ){
				branch.log( TreeLogger.DEBUG, "Is never assigned a value, therefore cannot be made final.", null );
				continue;
			}
			this.makeLocalVariableFinal(declaration, expression, method, branch );
			changed = true;
		}
		
		return changed;
	}

	/**
	 * Makes the given local variable final.
	 */
	protected void makeLocalVariableFinal(final JLocalDeclarationStatement declaration, final JExpression valueExpression, final JMethod method, final TreeLogger logger) {
		Checker.notNull( "parameter:declaration", declaration );
		Checker.notNull( "parameter:valueExpression", valueExpression );
		Checker.notNull( "parameter:method", method );
		Checker.notNull( "parameter:logger", logger );
		
		while (true) {
			// double check variable isnt final...
			final JVariable variable = declaration.getLocalRef().getTarget();			
			final JLocal local = this.checkVariable(declaration);

			// if has initializer simply make variable final...
			final JExpression initializer = declaration.getInitializer();
			if (null != initializer) {
				if (initializer != valueExpression) {
					throw new AssertionError("The variable \"" + variable.getName()
							+ "\" initializer doesnt match the given expression, initializer: \"" + Compiler.getSource(initializer)
							+ "\", value: \"" + Compiler.getSource(valueExpression));
				}

				// simply make variable final...
				local.setFinal(true);
				
				if (logger.isLoggable(TreeLogger.DEBUG)) {
					logger.log(TreeLogger.DEBUG, "Already has initializer, making variable final \"" + Compiler.getSource( declaration ) + "\"" , null);
				}
				break;
			}

			// assign initializer of declaration to value and replace value with declaration... 
			this.removeLocalDeclaration(declaration, method, logger);
			this.moveDeclarationToValueAssignment(declaration, valueExpression, method, logger);
			local.setFinal(true);
			break;
		}
	}

	/**
	 * Removes the declaration 
	 * @param declaration
	 * @param method
	 * @param logger
	 */
	protected void removeLocalDeclaration(final JLocalDeclarationStatement declaration, final JMethod method, final TreeLogger logger) {
		Checker.notNull( "parameter:declaration", declaration );
		Checker.notNull( "parameter:method", method );
		Checker.notNull( "parameter:logger", logger );
		
		this.checkVariable(declaration);

		final JModVisitor declarationRemover = new JModVisitor() {
			public boolean visit(final JLocalDeclarationStatement visitedDeclaration, final Context context) {
				boolean visitChildNodes = VISIT_CHILD_NODES;

				if (visitedDeclaration == declaration) {

					if (context.canRemove()) {
						context.removeMe();
						logger.log(TreeLogger.DEBUG, "Removing declaration from original position.", null);

					} else {
						final JProgram program = declaration.getJProgram();
						final SourceInfo sourceInfo = declaration.getSourceInfo();
						final JMultiExpression emptyBlock = new JMultiExpression(program, sourceInfo);
						context.replaceMe(emptyBlock);

						logger.log(TreeLogger.DEBUG, "Replacing declaration with empty block.", null);
					}

					visitChildNodes = !VISIT_CHILD_NODES;
				}
				return visitChildNodes;
			}
		};
		declarationRemover.accept(method);
		
		final boolean changed = declarationRemover.didChange();
		if( false == changed ){
			throw new AssertionError( "Failed to remove declaration, declaration: \"" + Compiler.getSource(declaration) + "\".");
		}
	}
	
	/**
	 * This method replaces the given value expression with the declaration and sets the initializer of the declaration.
	 * @param declaration
	 * @param valueExpression
	 * @param expressionStatement
	 * @param method
	 * @param logger
	 */
	protected void moveDeclarationToValueAssignment( final JLocalDeclarationStatement declaration, final JExpression valueExpression, final JMethod method, final TreeLogger logger ){
		Checker.notNull( "parameter:declaration", declaration );
		Checker.notNull( "parameter:valueExpression", valueExpression );
		Checker.notNull( "parameter:method", method );
		Checker.notNull( "parameter:logger", logger );
		
		final JModVisitor visitor = new JModVisitor(){
			public boolean visit( final JBinaryOperation binaryOperation, final Context context ){
				boolean visitChildNodes = VISIT_CHILD_NODES;
				
				if( binaryOperation.getRhs() == valueExpression ){
					LocalVariableFinalMaker.this.replaceExpressionWithDeclaration(declaration, valueExpression, context, logger);
					
					visitChildNodes = ! VISIT_CHILD_NODES;					
				}
				
				return visitChildNodes;
			}
			
			public boolean visit( final JExpressionStatement expressionStatement, final Context context ){
				boolean visitChildNodes = VISIT_CHILD_NODES;
				
				while( true ){
					final JExpression expression = expressionStatement.getExpr();
					if( false == expression instanceof JBinaryOperation ){
						break;
					}
					
					final JBinaryOperation binaryOperation = (JBinaryOperation) expression;
					if( binaryOperation.getRhs() != valueExpression ){
						break;
					}
					
					LocalVariableFinalMaker.this.replaceExpressionWithDeclaration(declaration, valueExpression, context, logger);
					
					visitChildNodes = ! VISIT_CHILD_NODES;
					break;
				}				
				
				return visitChildNodes;
			}
		};
		visitor.accept( method );
		
		// check that the visitor worked...
		final boolean changed = visitor.didChange();
		if( false == changed ){
			throw new AssertionError( "Failed to replace value with declaration, declaration: \"" + Compiler.getSource(declaration) + "\" and value \"" + Compiler.getSource( valueExpression ) + "\".");
		}
	}

	/**
	 * Asserts that the given variable is not final.
	 * @param declaration
	 * @throws AssertionError
	 */
	protected JLocal checkVariable(final JLocalDeclarationStatement declaration) throws AssertionError {
		final JVariable variable = declaration.getLocalRef().getTarget();
		if (variable.isFinal()) {
			throw new AssertionError("The variable \"" + variable.getName()
					+ "\" is already final and should have been left alone, declaration: " + Compiler.getSource(declaration));
		}

		return (JLocal) variable;
	}
	
	protected void replaceExpressionWithDeclaration( final JLocalDeclarationStatement declaration, final JExpression valueExpression, final Context context, final TreeLogger logger ){
		final JProgram program = valueExpression.getJProgram();
		final SourceInfo sourceInfo = valueExpression.getSourceInfo();
		final JLocalRef localRef = declaration.getLocalRef();
		final JExpression initializer = valueExpression;
		final JLocalDeclarationStatement newDeclaration = new JLocalDeclarationStatement( program, sourceInfo, localRef, initializer );

		final JLocal variable = (JLocal) localRef.getTarget();
		variable.setFinal( true );
		
		context.replaceMe( newDeclaration );
		
		if( logger.isLoggable( TreeLogger.DEBUG )){
			logger.log(TreeLogger.DEBUG, "@JES@Created a new declaration that is final with an initializer replacing the original assignment expression, declaration: " + Compiler.getSource( newDeclaration ), null);
		}		
	}
}