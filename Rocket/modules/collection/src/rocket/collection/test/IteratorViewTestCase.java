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
package rocket.collection.test;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import rocket.collection.client.IteratorView;
import rocket.util.client.Checker;

public class IteratorViewTestCase extends TestCase {
	public void testIterator0WithNoModifications() {
		final List<String> list = new ArrayList<String>();
		final Container container = new Container();

		final String first = "1";
		final String second = "2";
		final String third = "3";

		list.add(first);
		container.add(first);
		list.add(second);
		container.add(second);
		list.add(third);
		container.add(third);

		final Iterator<String> controlIterator = list.iterator();
		final Iterator<String> iterator = container.iterator();
		while (controlIterator.hasNext()) {
			assertTrue(iterator.hasNext());

			final String expectedElement = controlIterator.next();
			final String actualElement = iterator.next();
			assertSame(expectedElement, actualElement);
		}

		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
	}

	public void testIterator1WithRemoves() {
		final List<String> list = new ArrayList<String>();
		final Container container = new Container();

		final String first = "1";
		final String second = "2";
		final String third = "3";

		list.add(first);
		container.add(first);
		list.add(second);
		container.add(second);
		list.add(third);
		container.add(third);

		final Iterator<String> controlIterator = list.iterator();
		final Iterator<String> iterator = container.iterator();
		while (controlIterator.hasNext()) {
			assertTrue(iterator.hasNext());

			final String expectedElement = controlIterator.next();
			final String actualElement = iterator.next();
			assertSame(expectedElement, actualElement);

			controlIterator.remove();
			iterator.remove();
		}

		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
	}

	public void testIterator2WithNextThrowingConcurrentModification() {
		final Container container = new Container();

		final String first = "1";
		final String second = "2";
		final String third = "3";

		container.add(first);
		container.add(second);
		container.add(third);

		final Iterator<String> iterator = container.iterator();

		final String fourth = "4";
		container.add(fourth);

		try {
			final String visited = iterator.next();
			fail("Iterator.next should have thrown an Exception and not returned \"" + visited + "\".");
		} catch (ConcurrentModificationException expected) {
		}
	}

	public void testIterator3WithNextFollowedByModificationThenRemoveThrowingConcurrentModification() {
		final Container container = new Container();

		final String first = "1";
		final String second = "2";
		final String third = "3";

		container.add(first);
		container.add(second);
		container.add(third);

		final Iterator<String> iterator = container.iterator();

		final String fourth = "4";
		iterator.next();
		container.add(fourth);

		try {
			iterator.remove();
			fail("Iterator.remove() should have thrown an Exception, container: " + container);
		} catch (ConcurrentModificationException expected) {
		}
	}

	class Container {

		public Container() {
			this.setList(new ArrayList<String>());
		}

		void add(String element) {
			this.getList().add(element);
		}

		Iterator iterator() {
			final Container that = this;
			final Iterator<String> wrapped = this.getList().iterator();

			final IteratorView iterator = new IteratorView() {
				protected boolean hasNext0() {
					return wrapped.hasNext();
				}

				protected Object next0() {
					return wrapped.next();
				}

				protected void afterNext() {
				}

				protected void remove0() {
					wrapped.remove();
				}

				protected int getModificationCounter() {
					return that.getModificationCounter();
				}
			};
			iterator.syncModificationCounters();
			return iterator;
		}

		List<String> list;

		List<String> getList() {
			Checker.notNull("field:list", list);
			return list;
		}

		void setList(final List<String> list) {
			Checker.notNull("parameter:list", list);
			this.list = list;
		}

		/**
		 * Helps keep track of concurrent modification of the parent.
		 */
		private int modificationCounter;

		int getModificationCounter() {
			return this.modificationCounter;
		}

		void setModificationCounter(final int modificationCounter) {
			this.modificationCounter = modificationCounter;
		}

		protected void incrementModificationCounter() {
			this.setModificationCounter(this.getModificationCounter() + 1);
		}
	}
}
