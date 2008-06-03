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
package rocket.collection.client;

import java.util.Iterator;

import rocket.util.client.Checker;

/**
 * This iterator provides automatic support that may be queried for the last
 * element to visited via {@link #next}. This is particularly useful within the
 * a subclass implementation of {@link #remove}.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class VisitedRememberingIterator<E> extends IteratorWrapper<E> implements Iterator {

	// ITERATOR
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public boolean hasNext() {
		return this.getIterator().hasNext();
	}

	public E next() {
		final E lastVisited = this.getIterator().next();
		this.setLastVisited(lastVisited);
		return lastVisited;
	}

	public void remove() {
		this.getIterator().remove();
		this.clearLastVisited();
	}

	// IMPL
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This senitel value is used to detect if the lastVisited field is unset.
	 */
	final Object LAST_VISITED_SET = "{lastVisitedNotSet}";

	/**
	 * The object that was last visited ie the object returned by the last call
	 * to {@link #next}
	 */
	private E lastVisited = (E) LAST_VISITED_SET;

	public E getLastVisited() {
		Checker.trueValue("lastVisited", LAST_VISITED_SET != this.lastVisited);
		return lastVisited;
	}

	public boolean hasLastVisited() {
		return this.lastVisited == LAST_VISITED_SET;
	}

	public void setLastVisited(final E lastVisited) {
		this.lastVisited = lastVisited;
	}

	public void clearLastVisited() {
		this.lastVisited = (E) LAST_VISITED_SET;
	}

	@Override
	public String toString() {
		return super.toString() + ", lastVisited: " + lastVisited;
	}
}
