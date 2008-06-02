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
import java.util.NoSuchElementException;

import rocket.util.client.Checker;

/**
 * This iterator may be used to skip elements from an underlying iterator via
 * the {@link #skip} method.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class SkippingIterator<E> extends IteratorWrapper<E> implements Iterator {
	
	public SkippingIterator(){
		super();
	}
	
	public SkippingIterator( final Iterator<E> iterator ){
		super();
		this.setIterator(iterator);
	}
	
	public boolean hasNext() {
		return this.findNext();
	}

	/**
	 * This is the principal method that controls the read ahead / skipping
	 * process.
	 * 
	 * First it checks if a cached object has been already found and exits if
	 * one has been. Otherwise it reads from the wrapped iterator one at a time
	 * asking skip if the object should be skipped. This continues until the
	 * iterator is exhausted or a non skipped element is found.
	 * 
	 * @return A flag is returned indicating whether or not an element was found.
	 */
	protected boolean findNext() {
		boolean hasMore = this.hasCache();

		if (false == hasMore) {

			// keep looping until the iterator is exhaused or an element that
			// should not be skipped is found.
			final Iterator<E> iterator = this.getIterator();
			while (true) {
				if (false == iterator.hasNext()) {
					break;
				}

				final E next = this.getIterator().next();

				// try next...
				if (this.skip(next)) {
					continue;
				}

				// found exit!
				this.setCache(next);
				hasMore = true;
				break;
			}
		}
		return hasMore;
	}

	protected abstract boolean skip(E object);

	public Object next() {
		if (false == findNext()) {
			throw new NoSuchElementException();
		}
		final Object next = this.getCache();
		this.clearCache();
		return next;
	}

	public void remove() {
		this.getIterator().remove();
	}

	/**
	 * This senitel is placed in the cache field to mark that is not set.
	 */
	final static Object CACHE_NOT_SET = new Object();

	/**
	 * This property keeps a cached copy of the element that will be returned by
	 * {@link #next()}. This is necessary as {@link #hasNext()} reads ahead
	 * finding an element that should not be skipped and stores its value here
	 * ready for {@link #next()}.
	 */
	private E cache = (E) CACHE_NOT_SET;

	protected E getCache() {
		Checker.trueValue("cache", cache != CACHE_NOT_SET);
		return cache;
	}

	protected boolean hasCache() {
		return cache != CACHE_NOT_SET;
	}

	protected void setCache(E cache) {
		this.cache = cache;
	}

	protected void clearCache() {
		this.cache = (E)CACHE_NOT_SET;
	}

	@Override
	public String toString() {
		return super.toString() + ", cache: " + cache;
	}
}
