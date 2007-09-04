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

import rocket.util.client.ObjectHelper;

/**
 * Useful base class for any iterator which is actually a view around another
 * Iterator
 * 
 * @author Miroslav Pokorny
 */
public abstract class IteratorWrapper implements Iterator {
	/**
	 * The iterator being wrapped.
	 */
	private Iterator iterator;

	public Iterator getIterator() {
		ObjectHelper.checkNotNull("field:iterator", iterator);
		return iterator;
	}

	public void setIterator(final Iterator iterator) {
		ObjectHelper.checkNotNull("field:iterator", iterator);
		this.iterator = iterator;
	}

	public String toString() {
		return super.toString() + ", iterator: " + iterator;
	}
}
