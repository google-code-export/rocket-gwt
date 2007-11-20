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
package rocket.generator.rebind.visitor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

/**
 * This visitor may be used to visit all types that are reachable from a particular starting type.
 * All super/sub classes of the every type will be scanned.
 * @author Miroslav Pokorny
 */
abstract public class ReachableTypesVisitor {
	/**
	 * Starts the visiting process.
	 * @param type
	 */
	public void start(final Type type) {
		this.setConcreteTypes(this.createConcreteTypes());
		this.setTypes(this.createTypes());

		this.visitType(type);
	}

	/**
	 * Attempts to find all reachable types for the given type.
	 * 
	 * This method is smart and will skip and do nothing for types that have already been visited previously. Without this this visitor could potentially
	 * loop forever.
	 * @param type
	 */
	protected void visitType(final Type type) {
		PrimitiveHelper.checkFalse("The parameter:type must be an object not a primitive type, type: " + type, type.isPrimitive());

		while( true ){
			if (this.hasAlreadyBeenVisited(type)) {
				break;
			}
			
			this.addType(type);
			
			if (this.skipType(type)) {
				break;
			}
			
			if (type.isInterface()) {
				this.processInterface(type);
				break;
			}
			
			this.addConcreteType(type);
			this.processType(type);
			break;
		}
	}

	/**
	 * Processes an encountered type, first by finding reachable types from the super types and then sub types of the given type.
	 * @param type
	 */
	protected void processType(final Type type) {
		PrimitiveHelper.checkTrue("The parameter:interface is not a type, type: " + type, false == type.isInterface());		
		
		this.visitSuperTypes(type);
		this.visitFields(type);
		this.visitSubTypes(type);
	}

	/**
	 * Finds all types that are implemented by the given interface.
	 * @param interfacee
	 */
	protected void processInterface(final Type interfacee) {
		PrimitiveHelper.checkTrue("The parameter:interface is not an interface, interface: " + interfacee, interfacee.isInterface());
		
		final ConcreteTypesImplementingInterfaceVisitor implementedVisitor = new ConcreteTypesImplementingInterfaceVisitor() {
			protected boolean visit(final Type type) {
				if (false == ReachableTypesVisitor.this.hasAlreadyBeenVisited(type)) {
					ReachableTypesVisitor.this.visitTypeThatImplementsInterface(type, interfacee);
				}
				return false;
			}

			protected boolean skipAbstractTypes() {
				return ReachableTypesVisitor.this.skipAbstractTypesImplementingInterface();
			}
		};
		implementedVisitor.start(interfacee);
	}

	protected void visitTypeThatImplementsInterface(final Type type, final Type interfacee) {
		this.visitType(type);
	}

	protected boolean skipAbstractTypesImplementingInterface() {
		return false;
	}

	protected void visitSuperTypes(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		if (false == type.getName().equals(OBJECT)) {
			this.visitSuperTypes0(type);
		}
	}

	private final String OBJECT = Object.class.getName();

	protected void visitSuperTypes0(final Type type) {
		final SuperTypesVisitor superTypes = new SuperTypesVisitor() {
			protected boolean visit(final Type superType) {
				ReachableTypesVisitor.this.processSuperType(superType);
				return false;
			}

			protected boolean skipInitialType() {
				return true;
			}
		};
		superTypes.start(type);
	}

	protected void processSuperType( final Type superType ){
		if( false == ReachableTypesVisitor.this.hasAlreadyBeenVisited(superType)){
			if( false == ReachableTypesVisitor.this.skipSuperType( superType )){					
				this.addConcreteType(superType);
				this.addType(superType);
				this.visitSuperType(superType);
			}
		}
	}
	
	abstract protected boolean skipSuperType( Type type );
	
	protected void visitSuperType(final Type superType) {		
		this.visitFields(superType);
	}

	/**
	 * Triggers the visiting of all immediate sub types of the given type.
	 * @param type
	 */
	protected void visitSubTypes(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		final SubTypesVisitor subTypes = new SubTypesVisitor() {			
			
			protected boolean visit(final Type subType) {
				ReachableTypesVisitor.this.processSubType(subType);
				return false;
			}

			protected boolean skipInitialType() {
				return ReachableTypesVisitor.this.skipInitialSubType();
			}
		};
		subTypes.start(type);
	}
	
	protected void processSubType( final Type subType ){
		if( false == ReachableTypesVisitor.this.hasAlreadyBeenVisited(subType)){
			if( false == ReachableTypesVisitor.this.skipSubType(subType)){
				this.addConcreteType(subType);
				this.addType(subType);		
				this.visitSubType(subType);
			}
		}
	}

	abstract protected boolean skipSubType( Type subType );
	
	protected void visitSubType(final Type subType) {		
		this.visitFields(subType);
	}

	protected boolean skipInitialSubType() {
		return true;
	}

	protected void visitFields(final Type type) {
		final Iterator fields = type.getFields().iterator();
		while (fields.hasNext()) {
			final Field field = (Field) fields.next();
			if (this.skipField(field)) {
				continue;
			}
			// primitive fields cant have fields so simply record...
			final Type fieldType = field.getType();
			if (fieldType.isPrimitive()) {
				continue;
			}
			this.visitField(field);
		}
	}

	protected void visitField(final Field field) {
		ObjectHelper.checkNotNull("parameter:field", field);

		this.visitType(field.getType());
	}

	/**
	 * Provides an opportunity to skip processing of a particular type.
	 * @param type
	 * @return
	 */
	abstract protected boolean skipType(Type type);

	/**
	 * Provides an opportunity to skip processing of a particular field.
	 * This makes it easy to skip static, transient fields as would be required by a serialization generator.
	 * @param field
	 * @return Return true to skip processing this field.
	 */
	abstract protected boolean skipField(Field field);

	/**
	 * Accumulates all types that are reachable from the starting type after being potentially filtered by {@link #skipType(Type)} and {@link #skipField(Field)}.
	 * Because this is a set no duplicates are recorded.
	 */
	private Set concreteTypes;

	public Set getConcreteTypes() {
		ObjectHelper.checkNotNull("field:types", concreteTypes);
		return this.concreteTypes;
	}

	protected void setConcreteTypes(final Set concreteTypes) {
		ObjectHelper.checkNotNull("parameter:types", concreteTypes);
		this.concreteTypes = concreteTypes;
	}

	protected Set createConcreteTypes() {
		return new HashSet();
	}

	protected void addConcreteType( final Type type ){
		ObjectHelper.checkNotNull("parameter:type", type );
		PrimitiveHelper.checkFalse( "The type " + type + " has is an interface", type.isInterface());
		
		this.getConcreteTypes().add(type);
	}
	
	/**
	 * This set records types that have already been visited, avoiding the need to repeatedly revisit the same type
	 */
	private Set types;

	protected Set getTypes() {
		ObjectHelper.checkNotNull("field:types", types);
		return this.types;
	}

	protected void setTypes(final Set types) {
		ObjectHelper.checkNotNull("parameter:types", types);
		this.types = types;
	}

	protected Set createTypes() {
		return new HashSet();
	}

	protected void addType(final Type type) {
		PrimitiveHelper.checkFalse( "The type " + type + " has already been visited", this.hasAlreadyBeenVisited(type));
		
		this.getTypes().add(type);
	}
	
	protected boolean hasAlreadyBeenVisited(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);
		return this.getTypes().contains(type);
	}
}
