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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JField;
import com.google.gwt.dev.jjs.ast.JInterfaceType;
import com.google.gwt.dev.jjs.ast.JLocal;
import com.google.gwt.dev.jjs.ast.JLongLiteral;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JParameter;
import com.google.gwt.dev.jjs.ast.JPrimitiveType;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JType;
import com.google.gwt.dev.jjs.ast.JVisitor;

/**
 * This compilation worker may be used to output any warnings about any localVariables of type long.
 * If warnings are enabled it will spit out all classes along with a breakdown of fields/methods that include references to long localVariables.
 * 
 * This compilation worker is run just before the steps that transform an optimised AST into javascript.
 * @author Miroslav Pokorny
 */
public class LongNotifier implements CompilationWorker {

	final static String ENABLED_SYSTEM_PROPERTY = LongNotifier.class.getName() + ".enabled";
	final static String ENABLED = "true";
	
	final static String HEADER ="Listing all long references within program.";
	final static String TYPES = "Types";
	final static String METHODS = "Methods";
	final static String RETURN_TYPE = "ReturnType";
	final static String FIELDS = "Fields";
	
	final static String CONSTRUCTOR_SUFFIX = " (Constructor)";
	final static String STATIC_INITIALIZER_METHOD_NAME = "$clinit";
	final static String STATIC_INITIALIZER_SUFFIX = " (static initializer)";
	final static String INITIALIZER_METHOD_NAME = "$init";
	final static String INITIALIZER_SUFFIX = " (initializer)";
	
	/**
	 * If warnings are enabled output all types, methods and fields with long references.
	 */
	public boolean work(final JProgram jprogram, final TreeLogger logger) {		
		if( this.isEnabled()){
			if (logger.isLoggable(TreeLogger.WARN)) {
				logger.log(TreeLogger.WARN, HEADER, null );
				
				final Set types = this.findTypesWithLongReferences(jprogram);
				this.outputTypes(types, logger);
			}	
		}		
		return false;
	}
	
	protected boolean isEnabled(){
		final String value = System.getProperty( ENABLED_SYSTEM_PROPERTY );
		return ENABLED.equals( value );
	}

	/**
	 * Outputs warnings for all types, methods and locals that are of type long.
	 * @param types
	 * @param logger
	 */
	protected void outputTypes(final Set types, final TreeLogger logger) {
		final int typeCount = types.size();
		final TreeLogger methodsLogger = logger.branch(TreeLogger.WARN, TYPES + " (" + typeCount + ")", null);
		
		final Iterator i = types.iterator();
		while (i.hasNext()) {
			final Type type = (Type) i.next();
			this.outputType(type, methodsLogger);
		}
	}

	/**
	 * Outputs in a heirarchical format the methods and fields that have long reference types.
	 * @param type
	 * @param logger
	 */
	protected void outputType(final Type type, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.WARN, type.getName(), null);

		final Set methods = type.getMethods();
		final int methodCount = methods.size();
		if ( methodCount > 0 ) {
			final Iterator methodsIterator = methods.iterator();
			
			final TreeLogger methodsLogger = branch.branch(TreeLogger.WARN, METHODS + " (" + methodCount + ")", null);
			while (methodsIterator.hasNext()) {
				final Method method = (Method) methodsIterator.next();
				this.outputMethod(method, methodsLogger);
			}
		}

		
		final Set fields = type.getFields();
		final int fieldCount = fields.size();
		if ( fieldCount > 0 ) {
			final TreeLogger fieldsLogger = branch.branch(TreeLogger.WARN, FIELDS + " (" + fieldCount + ")", null);
		
			final Iterator fieldIterator = type.getFields().iterator();
			while (fieldIterator.hasNext()) {
				final Field field = (Field) fieldIterator.next();
				this.outputField(field, fieldsLogger);
			}
		}
	}

	/**
	 * Writes the method name, Return Type if the method returns long, and then outputs any parameters, local variables or literals.
	 * @param method
	 * @param logger
	 */
	protected void outputMethod(final Method method, final TreeLogger logger) {
		String methodName = method.getName();
		if( methodName.equals( STATIC_INITIALIZER_METHOD_NAME)){
			methodName = methodName + STATIC_INITIALIZER_SUFFIX;
		}
		if( methodName.equals( INITIALIZER_METHOD_NAME)){
			methodName = methodName + INITIALIZER_SUFFIX;
		}
		final JMethod jmethod = method.getJMethod();
		if( jmethod.isConstructor() ){
			methodName = methodName + CONSTRUCTOR_SUFFIX;
		}
		
		final TreeLogger branch = logger.branch(TreeLogger.WARN, methodName, null);

		final JType longType = jmethod.getJProgram().getTypePrimitiveLong();

		// if the return type is long write return type...
		if (jmethod.getType() == longType) {
			branch.log(TreeLogger.WARN, RETURN_TYPE, null);
		}
		this.outputLocals(method.getLocals(), branch);
	}

	/**
	 * Outputs a field of type long followed by any parametersLocalsAndLiterals variable references within its initializer that are of type long.
	 * @param field
	 * @param logger
	 */
	protected void outputField(final Field field, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.WARN, field.getName(), null);
		this.outputLocals( field.getLiterals(), branch );
	}

	/**
	 * Outputs all parameters, parametersLocalsAndLiterals variables and literals under a common title.
	 * @param locals
	 * @param logger
	 */
	protected void outputLocals(final List locals, final TreeLogger logger) {
		final Iterator i = locals.iterator();

			while (i.hasNext()) {
				final String item = (String) i.next();
				logger.log(TreeLogger.WARN, item, null);
			}
	}

	/**
	 * Visits the entire tree attempting to find types that contain methods or fields that have long type references.
	 * @param program
	 * @return
	 */
	protected Set findTypesWithLongReferences(final JProgram program) {
		final Set types = new TreeSet(HAS_NAME_COMPARATOR);

		// visits all types/fields/methods etc.
		final JType longType = program.getTypePrimitiveLong();
		final JVisitor visitor = new JVisitor() {
			/**
			 * Leave primitive types alone.
			 */
			public boolean visit(JPrimitiveType x, Context ctx) {
				return false;
			}

			/**
			 * Records and visits all the nodes hanging of class and interface types.
			 */
			public boolean visit(final JClassType jClassType, final Context context) {
				this.setType(new Type(jClassType));
				return true;// visit all child nodes.
			}

			public void endVisit(final JClassType jClassType, final Context context) {
				this.endVisit(this.getType());
				this.clearType();
			}

			public boolean visit(final JInterfaceType jInterfaceType, final Context context) {
				this.setType(new Type(jInterfaceType));
				return true;// visit all child nodes.
			}

			public void endVisit(final JInterfaceType jInterfaceType, final Context context) {
				this.endVisit(this.getType());
				this.clearType();
			}

			/**
			 * Only add the type if it has any long references.
			 * @param type
			 */
			protected void endVisit(final Type type) {
				boolean longReferenceFound = false;

				while (true) {
					if (type.getFields().size() > 0) {
						longReferenceFound = true;
						break;
					}
					if (type.getMethods().size() > 0) {
						longReferenceFound = true;
						break;
					}

					break;
				}

				if (longReferenceFound) {
					types.add(type);
				}
			}

			/**
			 * Records the method being visited...
			 */
			public boolean visit(final JMethod jmethod, final Context context) {
				boolean visitChildNodes = false;
				
				if( jmethod.getName().equals("methodWithLongParameter")){
					int i = 0;
				}
				
				if( /*false == jmethod.isStaticDispatcher() && */ jmethod.getEnclosingType() != null ){
					final Method method = new Method(jmethod);
					this.setMethod(method);
					
					visitChildNodes = true;
				}				
				return visitChildNodes;// visit all child nodes.
			}

			/**
			 * If the Method is of type long or has any long references in its initializer keep it...
			 */
			public void endVisit(final JMethod jMethod, final Context context) {
				if( jMethod.getEnclosingType() != null ){
				
				boolean longReferenceFound = false;
				final Method method = this.getMethod();

				while (true) {
					// if the return type is long keep the method.
					if (method.getJMethod().getType() == longType) {
						longReferenceFound = true;
						break;
					}

					if (method.getLocals().size() > 0) {
						longReferenceFound = true;
						break;
					}
					break;
				}
				if (longReferenceFound) {
					this.getType().getMethods().add(method);
				}

				this.clearMethod();
				}
			}

			/**
			 * Records any parameters of type long.
			 */
			public boolean visit(final JParameter parameter, final Context context) {				
				if (parameter.getType() == longType) {
					this.getMethod().getLocals().add(parameter.getName());
				}
				return true;
			}

			/**
			 * Records the field being visited.
			 */
			public boolean visit(final JField jField, final Context context) {
				final Field Field = new Field(jField);
				this.setField(Field);
				return true;// visit all child nodes.
			}

			/**
			 * If the field is of type long or has any long references in its initializer keep it...
			 */
			public void endVisit(final JField jField, final Context context) {
				boolean longReferenceFound = false;
				final Field field = this.getField();

				while (true) {
					if (field.getJField().getType() == longType) {
						longReferenceFound = true;
						break;
					}
					if (field.getLiterals().size() > 0) {
						longReferenceFound = true;
						break;
					}
					break;
				}
				if (longReferenceFound) {
					this.getType().getFields().add(field);
				}

				this.clearField();
			}

			/**
			 * If the parametersLocalsAndLiterals variable type is primitive long add it to the list of long parametersLocalsAndLiterals variables for the method or field we are inside.
			 */
			public boolean visit(final JLocal local, final Context context) {
				// if the parametersLocalsAndLiterals variable type isnt long dont record...
				final JType localType = local.getType();
				if (localType == longType) {
					final String name = local.getName();
					this.getMethod().getLocals().add(name);					
				}
				return true;
			}

			public boolean visit(final JLongLiteral literal, final Context context) {
				if (literal.getType() == longType) {

					final String longValue = literal.toSource(); 
					if (this.insideMethod()) {
						this.getMethod().getLocals().add(longValue);
					} else {
						this.getField().getLiterals().add(longValue);
					}
				}
				return true;// visit children...
			}

			/**
			 * The current type being processed.
			 */
			Type type;

			Type getType() {
				return this.type;
			}

			void setType(final Type type) {
				this.type = type;
			}

			void clearType() {
				this.type = null;
			}

			/**
			 * The current method being processed
			 */
			Method method;

			Method getMethod() {
				return this.method;
			}

			void setMethod(final Method method) {
				this.method = method;
			}

			void clearMethod() {
				this.method = null;
			}

			/**
			 * The current field being processed.
			 */
			Field field;

			Field getField() {
				return this.field;
			}

			void setField(final Field field) {
				this.field = field;
			}

			void clearField() {
				this.field = null;
			}

			boolean insideMethod() {
				return this.method != null;
			}
		};
		visitor.accept(program);

		return types;
	}

	/**
	 * Represents a type that contains a long field, method or parametersLocalsAndLiterals variable reference.
	 */
	static public class Type implements HasName {

		public Type(final JType jType) {
			super();
			this.setJType(jType);
		}

		private JType jType;

		protected JType getJType() {
			return this.jType;
		}

		protected void setJType(final JType jType) {
			this.jType = jType;
		}

		public String getName() {
			return this.getJType().getName();
		}

		/**
		 * Aggregates all methods that have a return type, parameter or parametersLocalsAndLiterals localVariables that are of type long.
		 */
		private Set methods = new TreeSet( METHOD_COMPARATOR );

		public Set getMethods() {
			return this.methods;
		}

		/**
		 * Aggregates all fields that are of type long or have a long type reference as part of an initializer.
		 */
		private Set fields = new TreeSet(HAS_NAME_COMPARATOR);

		public Set getFields() {
			return this.fields;
		}
	}

	/**
	 * Represents a method that contains a long return type, parameter or parametersLocalsAndLiterals variable reference.
	 */
	static public class Method implements HasName {
		public Method(final JMethod jMethod) {
			this.setJMethod(jMethod);
		}

		public String getName() {
			return this.getJMethod().getName();
		}

		private JMethod jMethod;

		public JMethod getJMethod() {
			return this.jMethod;
		}

		protected void setJMethod(final JMethod jMethod) {
			this.jMethod = jMethod;
		}

		/**
		 * Aggregates the names of any parameters, parametersLocalsAndLiterals variables or literals of type long
		 */
		List parametersLocalsAndLiterals = new ArrayList();

		public List getLocals() {
			return this.parametersLocalsAndLiterals;
		}
	}

	/**
	 * Represents a field that is of type long or its initializer contains a long variable references.
	 */
	static public class Field implements HasName {

		public Field(final JField jfield) {
			this.setJField(jfield);
		}

		public String getName() {
			return this.getJField().getName();
		}

		private JField jfield;

		public JField getJField() {
			return this.jfield;
		}

		protected void setJField(final JField jfield) {
			this.jfield = jfield;
		}
		
		/**
		 * Aggregates long literals
		 */
		List literals = new ArrayList();

		public List getLiterals() {
			return this.literals;
		}
	}

	/**
	 * Interface that makes it easy to provide sorting by name capabilities for Type,Method and Field.
	 */
	static interface HasName {
		String getName();
	}

	/**
	 * This comparator may be used to sort Types, Methods or Fields
	 */
	static private Comparator HAS_NAME_COMPARATOR = new Comparator() {
		public int compare(final Object hasName, final Object otherHasName) {
			return this.compare((HasName) hasName, (HasName) otherHasName);
		}

		int compare(final HasName hasName, final HasName otherHasName) {
			return hasName.getName().compareTo(otherHasName.getName());
		}
	};


	/**
	 * This comparator may be used to sort Types, Methods or Fields
	 */
	static private Comparator METHOD_COMPARATOR = new Comparator() {
		public int compare(final Object method, final Object otherMethod) {
			return this.compare((Method) method, (Method) otherMethod);
		}

		int compare(final Method method, final Method otherMethod) {
			int value = method.getName().compareTo( otherMethod.getName() );
				
			if( value != 0 ){
				final Iterator parameters = method.getJMethod().getOriginalParamTypes().iterator();
				final Iterator otherParameters = otherMethod.getJMethod().getOriginalParamTypes().iterator();
				
				while( true){			
					if( false == parameters.hasNext() || false == otherParameters.hasNext() ){
						break;
					}
					
					String parameterTypeName = "";
					if( parameters.hasNext() ){
						final JType type = (JType) parameters.next();
						parameterTypeName = type.getName();
					}
					
					String otherParameterTypeName = "";
					if( otherParameters.hasNext() ){
						final JType type = (JType) otherParameters.next();
						otherParameterTypeName = type.getName();					
					}										
					
					value = parameterTypeName.compareTo( otherParameterTypeName );
					if( value != 0 ){
						break;
					}
				} // while
			} // if
			
			return value;			
		}
	};
}
