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

import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * Visits all the sub types belonging to the type..
 * 
 * @author Miroslav Pokorny
 */
abstract public class SubTypesVisitor {

	protected SubTypesVisitor(){
		super();
		
		this.setVisited( this.createVisited() );
	}
	
	public void start(final Type type) {
		ObjectHelper.checkNotNull("type:type", type);

		while (true) {
			if (false == this.skipInitialType()) {
				if (this.visit(type)) {
					break;
				}
			}

			this.visitSubTypes(type);
			break;
		}
	}

	/**
	 * Handles the logic involved in visiting all the sub types for a given type without ever visiting the same type more than once.
	 * @param type A type 
	 */
	protected void visitSubTypes(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		final Set visited = this.getVisited();
		
		final Iterator subTypes = type.getSubTypes().iterator();
		while (subTypes.hasNext()) {
			final Type subType = (Type) subTypes.next();
			if( visited.contains( subType )){
				continue;
			}			
			
			if (this.visit(subType)) {
				break;
			}
			this.visitSubTypes(subType);
		}
	}

	/**
	 * Each type belonging to the given super type is presented to this type.
	 * 
	 * @param type
	 * @return return true to skip remaining types.
	 */
	abstract protected boolean visit(final Type type);

	/**
	 * If this method returns true the initial type passed to
	 * {@link #start(Type)} is skipped.
	 * 
	 * @return
	 */
	abstract protected boolean skipInitialType();
	
	/**
	 * A set which keeps track of types that have already been visited. 
	 * This guarantees that types are only ever seen once by the {@link #visit(Type)} method.
	 */
	private Set visited;
	
	protected Set getVisited(){
		ObjectHelper.checkNotNull("field:visited", visited );
		return this.visited;
	}
	
	protected void setVisited( final Set visited ){
		ObjectHelper.checkNotNull("parameter:visited", visited );
		this.visited = visited;
	}
	
	protected Set createVisited(){
		return new HashSet();
	}
}
