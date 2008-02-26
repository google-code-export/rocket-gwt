/*
 * Copyright 2007 Google Inc.
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
package com.google.gwt.dev.jjs.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dev.jjs.InternalCompilerException;
import com.google.gwt.dev.jjs.SourceInfo;

/**
 * A Java method implementation.
 */
public final class JMethod extends JNode implements HasEnclosingType, HasName, HasType, HasSettableType, CanBeAbstract, CanBeFinal,
		CanBeSetFinal, CanBeNative, CanBeStatic {

	/**
	 * References to any methods which this method overrides. This should be an
	 * EXHAUSTIVE list, that is, if C overrides B overrides A, then C's
	 * overrides list will contain both A and B.
	 */
	public final List/* <JMethod> */overrides = new ArrayList/* <JMethod> */();

	public final ArrayList/* <JParameter> */params = new ArrayList/* <JParameter> */();
	public final ArrayList/* <JClassType> */thrownExceptions = new ArrayList/* <JClassType> */();
	private JAbstractMethodBody body = null;
	private final JReferenceType enclosingType;
	private final boolean isAbstract;
	private boolean isFinal;
	private final boolean isPrivate;
	private final boolean isStatic;
	private final String name;
	private ArrayList/* <JType> */originalParamTypes;
	private JType returnType;

	/**
	 * These are only supposed to be constructed by JProgram.
	 */
	public JMethod(JProgram program, SourceInfo info, String name, JReferenceType enclosingType, JType returnType, boolean isAbstract,
			boolean isStatic, boolean isFinal, boolean isPrivate) {
		super(program, info);
		this.name = name;
		this.enclosingType = enclosingType;
		this.returnType = returnType;
		this.isAbstract = isAbstract;
		this.isStatic = isStatic;
		this.isFinal = isFinal;
		this.isPrivate = isPrivate;
	}

	public void freezeParamTypes() {
		if (originalParamTypes != null) {
			throw new InternalCompilerException("Param types already frozen");
		}
		originalParamTypes = new ArrayList/* <JType> */();
		for (int i = 0; i < params.size(); ++i) {
			JParameter param = (JParameter) params.get(i);
			originalParamTypes.add(param.getType());
		}
	}

	public JAbstractMethodBody getBody() {
		return body;
	}

	public JReferenceType getEnclosingType() {
		return enclosingType;
	}

	public String getName() {
		return name;
	}

	public List/* <JType> */getOriginalParamTypes() {
		if (originalParamTypes == null) {
			return null;
		}
		return originalParamTypes;
	}

	public JType getType() {
		return returnType;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public boolean isNative() {
		if (body == null) {
			return false;
		} else {
			return body.isNative();
		}
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setBody(JAbstractMethodBody body) {
		if (body != null) {
			body.setMethod(null);
		}
		this.body = body;
		body.setMethod(this);
	}

	public void setFinal(boolean b) {
		isFinal = b;
	}

	public void setType(JType newType) {
		returnType = newType;
	}

	public void traverse(JVisitor visitor, Context ctx) {
		if (visitor.visit(this, ctx)) {
			visitor.accept(params);
			if (body != null) {
				body = (JAbstractMethodBody) visitor.accept(body);
			}
		}
		visitor.endVisit(this, ctx);
	}

	// ROCKET: Reapply changes when upgrading GWT
	/**
	 * This method may be used to detect all methods that make up a constructor.
	 * TODO Is this the best way to test if a method is a constructor ???
	 */
	public boolean isConstructor() {
		boolean constructor = false;

		// if( false == this.isStatic()){
		final String expectedConstructorName = this.getEnclosingType().getShortName();
		constructor = this.getName().equals(expectedConstructorName);
		// }

		return constructor;
	}

	/**
	 * Checks that both methods have the same signature including whether or not
	 * return types match.
	 * 
	 * @param otherMethod
	 * @return True if the signatures match otherwise returns false.
	 * 
	 * NB: Had to write my own because JProgram.methodsDoMatch failed to match
	 * static methods even if they were the same method.
	 */
	public boolean hasSameSignature(final JMethod otherMethod) {
		boolean match = false;

		while (true) {
			if (this == otherMethod) {
				match = true;
				break;
			}

			// if not matching static dispatches the staticness of both must
			// match...
			if (this.isStatic() != otherMethod.isStatic()) {
				break;
			}

			// names must match
			if (false == this.getName().equals(otherMethod.getName())) {
				break;
			}

			// parameter types must match...
			if (false == this.getOriginalParamTypes().equals(otherMethod.getOriginalParamTypes())) {
				break;
			}
			// return types must match...
			if (false == this.getType().equals(otherMethod.getType())) {
				break;
			}

			// name, params, return type match report signatures are the same...
			match = true;
			break;
		}

		return match;
	}

	/**
	 * Tests if this method is virtual (eg not a constructor, static or
	 * private).
	 * 
	 * @return
	 */
	public boolean isVirtual() {
		boolean virtual = false;

		while (true) {
			if (this.isStatic()) {
				break;
			}
			if (this.isPrivate()) {
				break;
			}
			if (this.isConstructor()) {
				break;
			}
			// if this method is a static dispatcher cant be virtual.
			if (this.isStaticDispatcher()) {
				break;
			}

			// if this method has a static dispatcher it cant be virtual...
			if (this.hasStaticDispatcher()) {
				break;
			}

			// if its overridden its virtual.
			if (this.isOverridden()) {
				virtual = true;
				break;
			}
			// if it overrides another method its virtual
			if (this.isOverrider()) {
				virtual = true;
				break;
			}

			// not virtual!
			virtual = false;
			break;
		}

		return virtual;
	}

	/**
	 * This helper may be used to determine if this method is overridden in any
	 * sub class of the enclosing type.
	 * 
	 * @return
	 */
	public boolean isOverridden() {
		boolean overridden = false;

		while (true) {
			if (this.isStatic()) {
				break;
			}
			if (this.isPrivate()) {
				break;
			}
			if (this.isFinal()) {
				break;
			}
			if (this.isConstructor()) {
				break;
			}

			// need to asktype oracle for overrides.
			overridden = this.getOverridenMethods().length > 0;
			break;
		}

		return overridden;
	}

	/**
	 * Finds all overridden methods for this method.
	 * 
	 * @return
	 */
	public JMethod[] getOverridenMethods() {
		final List overridden = new ArrayList();

		// visit sub types
		while (true) {
			// static private and constructors cant be overridden...
			if (this.isStatic()) {
				break;
			}

			if (this.isPrivate()) {
				break;
			}

			if (this.isConstructor()) {
				break;
			}

			final JReferenceType enclosingType = this.getEnclosingType();
			if (false == (enclosingType instanceof JClassType)) {
				break;
			}

			final JClassType enclosingClassType = (JClassType) enclosingType;
			final JClassType[] allSubTypes = enclosingClassType.getAllSubTypes();
			for (int i = 0; i < allSubTypes.length; i++) {
				final JClassType subType = allSubTypes[i];
				final Iterator subTypeMethods = subType.methods.iterator();
				while (subTypeMethods.hasNext()) {
					final JMethod subTypeMethod = (JMethod) subTypeMethods.next();
					if (false == this.hasSameSignature(subTypeMethod)) {
						continue;
					}
					overridden.add(subTypeMethod);
				}
			}

			break;
		}

		final Iterator types = this.program.getDeclaredTypes().iterator();
		assert types.hasNext(); // cant remember but i think this was none
								// sometimes.

		while (types.hasNext()) {
			final JReferenceType type = (JReferenceType) types.next();

			// skip non class types...
			if (false == (type instanceof JClassType)) {
				continue;
			}
		}

		// copy into array...
		return (JMethod[]) overridden.toArray(new JMethod[0]);
	}

	/**
	 * Tests if this method overrides any method by searching its super type
	 * heirarchy for matching methods.
	 * 
	 * @return True if it overrides any other method, false otherwise.
	 */
	public boolean isOverrider() {
		// dont bother to test if static / private etc... lets rely on overrides
		return this.overrides.size() > 0;
	}

	/**
	 * Queries whether or not this method is a static dispatch method for
	 * another.
	 * 
	 * @return
	 */
	public boolean isStaticDispatcher() {
		return this.program.isStaticImpl(this);
	}

	/**
	 * If this is a static dispatch method retrieves the target method that has
	 * been made non virtual.
	 * 
	 * @return
	 */
	public JMethod getStaticDispatcherTarget() {
		return this.program.staticImplFor(this);
	}

	/**
	 * Tests whether or not this method has a static dispatcher...
	 * 
	 * @return
	 */
	public boolean hasStaticDispatcher() {
		return this.getStaticDispatcher() != null;
	}

	/**
	 * Fetches the static dispatcher for this method.
	 * 
	 * @return
	 */
	public JMethod getStaticDispatcher() {
		return this.program.getStaticImpl(this);
	}
}
