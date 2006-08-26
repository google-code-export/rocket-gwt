/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

/**
 * A Tree like structure which contains directories which in turn may have other
 * directories and branches. Deleting a branch also deletes all children and
 * branches underneath it.
 *
 * The root of the tree is named "" and must always be present. As such it
 * cannot be removed and therefore the tree always has a minimum size of 1.
 * There is no requirement to separate tree levels using a specific character such as '/'
 *
 * Iterators are provided to walk the tree with views of paths(keys), values or
 * entries. The entries are of course updatable. A flag may be passed when requesting the iterator to control whether or not sub branchse are also visited.
 *
 * @author Miroslav Pokorny (mP)
 *
 */
public class Tree {
    /**
     * Tests if the first path is a parent of the second path.
     *
     * @param path
     * @param otherPath
     * @return
     */
    public static boolean isParentOf(final String path, final String otherPath) {
        StringHelper.checkNotNull("parameter:path", path);

        return otherPath.startsWith(path);
    }

	public Tree() {
		this.createRoot();
		this.setModificationCounter(0);
	}

	/**
	 * REtrieves the size or the total number of branches including the root
	 * with this tree.
	 *
	 * @return
	 */
	public int size() {
		return this.getRoot().size();
	}

	/**
	 * Tests if the tree contains a branch with the given path.
	 *
	 * @param path
	 * @return
	 */
	public boolean hasValue(final String path) {
		return this.getRoot().hasValue(path);
	}

	public boolean hasChildren(final String path) {
		return this.getRoot().hasChildren(path);
	}

	/**
	 * Retrieves the value of teh branch at the given path. If the path does not
	 * exist null is returned.
	 *
	 * @param path
	 * @return
	 */
	public Object getValue(final String path) {
		return this.getRoot().get(path);
	}

	/**
	 * Sets or replaces the value at the given path returning the previous value
	 * in the case of a replace. If a replace occurs the original children are
	 * not lost
	 *
	 * @param path
	 * @param value
	 * @return
	 */
	public Object setValue(final String path, final Object value) {
		StringHelper.checkNotNull("parameter:path", path);
		ObjectHelper.checkNotNull("parameter:value", value);

		return this.getRoot().set(path, value);
	}

	/**
	 * Removes the branch and its children if one is found. If the branch does
	 * not exist nothing occurs.
	 *
	 * @param path
	 */
	public void removePath(final String path) {
		this.getRoot().removeBranch(path);
	}

	/**
	 * Removes a branch value if one exists. If the branch does not exist
	 * nothing occurs.
	 *
	 * @param path
	 * @return
	 */
	public Object removeValue(final String path) {
		return this.getRoot().removeValue(path);
	}

	/**
	 * Returns an iterator which may be used to iterate over current paths for all entries.
	 * Corresponding iterators may be fetched to iterate over values and entries.
	 *
	 * @return
	 */
	public Iterator pathIterator(final String path,
			final boolean visitSubBranches) {
		return this.createIterator(path, PATH, visitSubBranches);
	}

	public Iterator valueIterator(final String path,
			final boolean visitSubBranches) {
		return this.createIterator(path, VALUE, visitSubBranches);
	}

	public Iterator entryIterator(final String path,
			final boolean visitSubBranches) {
		return this.createIterator(path, ENTRY, visitSubBranches);
	}

    /**
     * Factory method which creates an iterator which may be used to transverse this tree.
     * The type parameter controls whether paths/values or entries are returned by the iterator.
     * @param path
     * @param type
     * @param visitSubBranches WHen true sub-branches are visited, otherwise when false they are ignored.
     * @return
     */
	protected Iterator createIterator(final String path, final int type,
			final boolean visitSubBranches) {
		final TreeIteratorView iterator = new TreeIteratorView();
		iterator.setViewType(type);
		iterator.setTree(this);
		iterator.syncModificationCounters();

        final Branch root = this.getRoot();
        final Branch branch = root.getBranch(path);
		iterator.addChildren( null != branch ? branch : root );
		iterator.setVisitSubBranches(visitSubBranches);

		return iterator;
	}

	/**
	 * Set of enums for the different possible types of views
	 */
	final static int PATH = 0;

	final static int VALUE = PATH + 1;

	final static int ENTRY = VALUE + 1;

	private class TreeIteratorView extends IteratorView {

		public TreeIteratorView() {
			super();
			this.setUnvisited(new ArrayList());
		}

		protected boolean hasNext0() {
			return !this.getUnvisited().isEmpty();
		}

		protected Object next0(final int viewType) {
			if (!this.hasNext0()) {
				throw new NoSuchElementException();
			}
			final Branch branch = this.first();
			if (this.isVisitSubBranches()) {
				this.addChildren(branch);
			}
			this.setLastVisited(branch);

			Object nextValue = null;
			while (true) {
				if (viewType == PATH) {
					nextValue = branch.getPath();
					break;
				}
				if (viewType == VALUE) {
					nextValue = branch.getValue();
					break;
				}
				if (viewType == ENTRY) {
					nextValue = new Map.Entry() {
						public Object getKey() {
							return branch.getPath();
						}

						public Object getValue() {
							return branch.getValue();
						}

						public Object setValue(final Object newValue) {
							return branch.setValue(newValue);
						}
					};
					break;
				}
				SystemHelper.handleAssertFailure("Unknown viewType, viewType: "+ viewType); // shouldnt happen
				break;
			}
			return nextValue;
		}

		protected void leavingNext() {
		}

		protected void remove0() {
			if (!this.hasLastVisited()) {
				SystemHelper.handleAssertFailure("Attempt to call remove without next()");
			}

			// remove the most recently visited...
			final Branch removed = this.getLastVisited();
			final Tree tree = this.getTree();
			tree.removePath(removed.getPath());
			this.clearLastVisited();

			// check if any of its siblines also need to be removed...
			final Iterator unvisited = this.getUnvisited().iterator();
			while (unvisited.hasNext()) {
				final Branch branch = (Branch) unvisited.next();
				if (!removed.isParentOf(branch.getPath())) {
					break;
				}
				unvisited.remove();
				tree.removePath(branch.getPath());
			}
		}

		private Tree tree;

		Tree getTree() {
			ObjectHelper.checkNotNull("field:tree", tree);
			return tree;
		}

		void setTree(final Tree tree) {
			ObjectHelper.checkNotNull("parameter:tree", tree);
			this.tree = tree;
		}

		/**
		 * This list contains remaining unvisited branches
		 */
		private List unvisited = null;

		List getUnvisited() {
			ObjectHelper.checkNotNull("field:unvisited", unvisited);
			return this.unvisited;
		}

		void setUnvisited(final List unvisited) {
			ObjectHelper.checkNotNull("parameter:unvisited", unvisited);
			this.unvisited = unvisited;
		}

		Branch first() {
			return (Branch) this.getUnvisited().remove(0);
		}

		void addChildren(final Branch branch) {
			ObjectHelper.checkNotNull("parameter:branch", branch);

			final List unvisited = this.getUnvisited();
			final List children = branch.getChildren();
			// sort them by path alphabetically

			final List sorted = new ArrayList();
			sorted.addAll(children);
			Collections.sort(sorted, new Comparator() {

				public int compare(final Object object, final Object otherObject) {
					final Branch branch = (Branch) object;
					final Branch otherBranch = (Branch) otherObject;
					return branch.getPath().compareTo(otherBranch.getPath());
				}

			});

			unvisited.addAll(0, sorted);
		}

		/**
		 * This last visited branch
		 */
		private Branch lastVisited = null;

		Branch getLastVisited() {
			ObjectHelper.checkNotNull("field:lastVisited", lastVisited);
			return this.lastVisited;
		}

		boolean hasLastVisited() {
			return null != this.lastVisited;
		}

		void setLastVisited(final Branch lastVisited) {
			ObjectHelper.checkNotNull("parameter:lastVisited", lastVisited);
			this.lastVisited = lastVisited;
		}

		void clearLastVisited() {
			this.lastVisited = null;
		}

		boolean visitSubBranches;

		boolean isVisitSubBranches() {
			return visitSubBranches;
		}

		void setVisitSubBranches(final boolean visitSubBranches) {
			this.visitSubBranches = visitSubBranches;
		}

		protected int getParentModificationCounter() {
			return this.getTree().getModificationCounter();
		}
	}

	private Branch root;

	protected Branch getRoot() {
		ObjectHelper.checkNotNull("field:root", root);
		return this.root;
	}

	protected void setRoot(final Branch root) {
		ObjectHelper.checkNotNull("parameter:root", root);
		this.root = root;
	}

	protected void createRoot() {
		final Branch root = new Branch();
		root.setPath("");
		root.setTree(this);
		this.setRoot(root);
	}

	/**
	 * Helps keep track of concurrent modification of the parent.
	 */
	private int modificationCounter;

	protected int getModificationCounter() {
		return this.modificationCounter;
	}

	public void setModificationCounter(final int modificationCounter) {
		this.modificationCounter = modificationCounter;
	}

	protected void increaseModificationCounter() {
		this.setModificationCounter(this.getModificationCounter() + 1);
	}

	public String toString() {
		return super.toString() + this.root + ", modificationCounter: "
				+ modificationCounter;
	}
}