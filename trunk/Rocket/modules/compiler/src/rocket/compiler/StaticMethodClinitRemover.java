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

import java.util.*;
import rocket.util.client.Checker;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JArrayType;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JInterfaceType;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JMethodCall;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JReferenceType;
import com.google.gwt.dev.jjs.ast.JType;
import com.google.gwt.dev.jjs.ast.JVisitor;
import com.google.gwt.dev.jjs.ast.js.JsniMethodRef;

/**
 * This class attempts to record static methods that dont require a clint guard inserted within them primarily because they are only called by other methods which would have already ensured the clint was called.
 * 
 * If a method is private or only has call sites within the enclosing class then it doesnt require a clint to be inserted by GenerateJavaScriptAST (non public method maybeCreateClintCall(Method)).
 * 
 * @author Miroslav Pokorny
 */
public class StaticMethodClinitRemover implements JavaCompilationWorker {

	private final static boolean VISIT_CHILD_NODES = true;

	/**
	 * Kicks off the process finding types with static initializers and attempting to find internal (private and methods without external call sites).
	 * Any such methods may then have their static initializer guard removed resulting in a smaller generated javascript file.
	 */
	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		Compiler.resetStaticMethodsNotRequiringClint();
		
		final TreeLogger branch = logger.branch(TreeLogger.INFO, this.getClass().getName(), null);
		
		final Map staticMethodsWithExternalCallSites = this.getStaticMethodsWithExternalCallsites(jprogram);
		
		final JVisitor visitor = new JVisitor() {
			public boolean visit(final JClassType type, final Context context) {
				StaticMethodClinitRemover.this.visitClassType(type, staticMethodsWithExternalCallSites, branch);
				return !VISIT_CHILD_NODES;
			}
			public boolean visit( final JArrayType array, final Context context ){
				return ! VISIT_CHILD_NODES; 
			}
			public boolean visit( final JInterfaceType interfacee, final Context context){
				return ! VISIT_CHILD_NODES;
			}
		};
		visitor.accept(jprogram);

		// always returns false
		branch.log(TreeLogger.DEBUG, "No changes were committed.", null);
		return false;
	}

	/**
	 * Builds a set that contains methods with external call sites. 
	 * @param program
	 * @return
	 */
	protected Map getStaticMethodsWithExternalCallsites( final JProgram program ){
		final Map allMethodCallsites = new HashMap();
		
		final JVisitor visitor = new JVisitor(){
			
			public boolean visit( final JMethod method, final Context context ){
				this.setMethod( method );
				return VISIT_CHILD_NODES;
			}
			public void endVisit( final JMethod method, final Context context ){
				this.clearMethod();
			}
			/**
			 * The current method, this enables the visit(JMethodCall... method to be able to determine whether it is a method call is external to the declaring class.
			 */
			JMethod method;
			JMethod getMethod(){
				Checker.notNull( "field:method", method );
				return this.method;
			}
			void setMethod( final JMethod method ){
				Checker.notNull( "parameter:method", method );
				this.method = method;
			}
			void clearMethod(){
				this.method = null;
			}
			
			public boolean visit( final JMethodCall methodCall, final Context context ){
				while( true ){
					final JMethod target = methodCall.getTarget();
					
					// ignore instance methods...
					if( false == target.isStatic() ){
						break;
					}
					
					// call site is from a method in the same class as the target method dont record...
					final JMethod method = this.getMethod();
					if( StaticMethodClinitRemover.this.isInternalCallsite( method, methodCall)){
						break;
					}
					
					this.addExternalReference(methodCall);
					break;
				}				
				
				return VISIT_CHILD_NODES;
			}
			
			public boolean visit( final JsniMethodRef jsniMethodReference, final Context context){
				this.addExternalReference(jsniMethodReference);
				return VISIT_CHILD_NODES;
			}
			
			protected void addExternalReference( final JMethodCall methodCall ){
				Checker.notNull("parameter:methodCall", methodCall );
				
				final JMethod target = methodCall.getTarget();
				List callSites = (List) allMethodCallsites.get( target );					
				// first time a call site for the method has been found create an empty list...
				if( null == callSites ){
					callSites = new ArrayList();
					allMethodCallsites.put( target, callSites );
				}
				 
				callSites.add( methodCall );
			}
		};
		visitor.accept(program);
		
		return allMethodCallsites;
	}
	
/** 
 * Helper which tests if the given callsite is within the same class as the given method.
 * @param method
 * @param methodCall
 * @return
 */
	protected boolean isInternalCallsite( final JMethod method, final JMethodCall methodCall ){
		Checker.notNull("parameter:method", method );
		Checker.notNull("parameter:methodCall", methodCall );
		
		boolean internal = false;
		
		while( true ){
			final JReferenceType methodEnclosingType = method.getEnclosingType();
			if( null == methodEnclosingType ){
				break;
			}
			
			final JMethod target = methodCall.getTarget();
			final JReferenceType targetEnclosingType = target.getEnclosingType();
			if( null == targetEnclosingType ){
				break;
			}
			
			if( methodEnclosingType.equals( targetEnclosingType )){
				internal = true;
			}
			break;
		}
		
		return internal;
	}
	
	/**
	 * Processes each encountered type
	 * @param type
	 * @param staticMethodsWithExternalCallSites
	 * @param logger
	 */
	protected void visitClassType(final JClassType type, final Map staticMethodsWithExternalCallSites, final TreeLogger logger) {
		while (true) {
			final TreeLogger branch = logger.branch(TreeLogger.DEBUG, type.getName(), null);

			// if the given type hasnt got any static initializers skip processing this type.
			if (false == this.hasStaticInitializer(type)) {
				branch.log(TreeLogger.DEBUG, "Has no static initializer(s)", null);
				break;
			}

			// process static methods attempting to remove unnecessary clint calls.
			this.processStaticMethods(type, staticMethodsWithExternalCallSites, branch);
			break;
		}
	}

	/**
	 * Tests if the given type has a static initializer.
	 * @param type
	 */
	protected boolean hasStaticInitializer(final JReferenceType type) {
		Checker.notNull("parameter:type", type);

		final StaticInitializerFinder finder = new StaticInitializerFinder();
		finder.accept(type);
		return finder.hasStaticInitializer();
	}

	/**
	 * This visitor visits all methods belonging to a concrete type and attempts to find a static initializer method.
	 */
	static class StaticInitializerFinder extends JVisitor {

		public boolean visit(final JMethod method, final Context context) {
			if (method.isStaticInitializer()) {
				this.setStaticInitializer(true);
			}
			return !VISIT_CHILD_NODES;
		}

		private boolean staticInitializer;

		boolean hasStaticInitializer() {
			return this.staticInitializer;
		}

		private void setStaticInitializer(final boolean staticInitializer) {
			this.staticInitializer = staticInitializer;
		}
	}

	/**
	 * Processes all the static methods belonging to the given type attempting to find those that are either private
	 * or not accessed outside the class. In such cases the static initializer call may be eliminated.
	 * @param type
	 * @param staticMethodsWithExternalCallSites
	 * @param logger
	 */
	protected void processStaticMethods(final JReferenceType type, final Map staticMethodsWithExternalCallSites, final TreeLogger logger) {
		Checker.notNull("parameter:type", type);
		Checker.notNull("parameter:logger", logger);

		// print method
		final JVisitor visitor = new JVisitor() {
			public boolean visit(final JMethod method, final Context context) {
				boolean optimise = false;

				TreeLogger branch = TreeLogger.NULL;
				if (logger.isLoggable(TreeLogger.DEBUG)) {
					branch = logger.branch(TreeLogger.DEBUG, Compiler.getMethodName(method), null);
				}
				while (true) {
					// must be static
					if (false == method.isStatic()) {
						branch.log(TreeLogger.DEBUG, "Instance method - left alone.", null);
						break;
					}

					if (method.isStaticDispatcher()) {
						branch.log(TreeLogger.DEBUG, "Static dispatcher - left alone.", null);
						break;
					}

					// cant be the initializer itself.
					if (method.isStaticInitializer()) {
						branch.log(TreeLogger.DEBUG, "Static initializer - left alone.", null);
						break;
					}

					// if private is a candidate...
					if (method.isPrivate()) {
						branch.log(TreeLogger.DEBUG, "Private", null);
						optimise = true;
						break;
					}

					// check that all call sites are sourced from within this same class.
					final List callSites = (List)staticMethodsWithExternalCallSites.get(method);
					if ( null != callSites ) {
						StaticMethodClinitRemover.this.listExternalCallSites(callSites, branch );
						break;
					}

					// all call sites are internal remove guard...
					branch.log(TreeLogger.DEBUG, "Contains no external call sites.", null);
					optimise = true;
					break;
				}

				if (optimise) {
					branch.log(TreeLogger.DEBUG, "Marking as not requiring clinit calls to be inserted by GenerateJavaScriptAST.", null);
					StaticMethodClinitRemover.this.markAsNotRequiringClint(method, context, branch);
				}

				return !VISIT_CHILD_NODES; // dont need to visit body.
			}
		};
		visitor.accept(type);
	}
	
	/**
	 * Providing the log level is set to DEBUG or greater lists all the call sites for a given method.
	 * @param callSites
	 * @param logger
	 */
	protected void listExternalCallSites( final List callSites, final TreeLogger logger ){
		if( logger.isLoggable( TreeLogger.DEBUG )){
			final TreeLogger branch = logger.branch( TreeLogger.DEBUG, "Clinit cannot be removed due to external callsites", null );
			
			final Iterator i = callSites.iterator();
			while( i.hasNext()){
				final JMethodCall methodCall = (JMethodCall) i.next();
				branch.log( TreeLogger.DEBUG, methodCall.getSourceInfo().toString(), null );
			}
		}
	}

	/**
	 * Marks the given method as not requiring a clint. The GenerateJavaScriptAST class which runs after this adds clints to all static methods if the enclosing class has a static initializer or clint.
	 * This method simply records the given method as not requiring one which results in the GenerateJavaScriptAST.maybeCreateClintCall method not inserting the clint.
	 * 
	 * @param method
	 * @param context
	 * @param logger
	 */
	protected void markAsNotRequiringClint(final JMethod method, final Context context, final TreeLogger logger) {
		Checker.notNull("parameter:method", method);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:logger", logger);

		Compiler.addStaticMethodNotRequiringClint(method);
	}
}
