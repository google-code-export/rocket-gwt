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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.dev.jjs.SourceInfo;

/**
 * Java class type reference expression.
 */
public class JClassType extends JReferenceType implements CanBeSetFinal {

	private final boolean isAbstract;
	private boolean isFinal;

	public JClassType(JProgram program, SourceInfo info, String name, boolean isAbstract, boolean isFinal) {
		super(program, info, name);
		this.isAbstract = isAbstract;
		this.isFinal = isFinal;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean b) {
		isFinal = b;
	}

	public void traverse(JVisitor visitor, Context ctx) {
		if (visitor.visit(this, ctx)) {
			visitor.acceptWithInsertRemove(fields);
			visitor.acceptWithInsertRemove(methods);
		}
		visitor.endVisit(this, ctx);
	}

	/**
	 * Finds all sub types of this type
	 * 
	 * @return
	 * 
	 * TODO Reapply changes when upgrading GWT
	 */
	public JClassType[] getAllSubTypes() {
		final Set subTypes = new HashSet();

		if (false == this.isFinal()) {
			final Iterator iterator = this.program.getDeclaredTypes().iterator();
			final JTypeOracle typeOracle = this.program.typeOracle;

			while (iterator.hasNext()) {
				final JReferenceType otherType = (JReferenceType) iterator.next();

				// ignore non JClassTypes
				if (false == (otherType instanceof JClassType)) {
					continue;
				}
				// ignore $otherType if its not a sub class of this class.
				final JClassType otherClassType = (JClassType) otherType;
				if (false == typeOracle.isSubClass(this, otherClassType)) {
					continue;
				}

				// save!
				subTypes.add(otherClassType);
			}
		}

		// copy into an array.
		return (JClassType[]) subTypes.toArray(new JClassType[0]);
	}

	public JClassType getSuperType() {
		return this.extnds;
	}
}
