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
package rocket.collection.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * A holder for a branch within a Tree
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Branch {
    public Branch() {
        this.setChildren(new ArrayList());
    }

    /**
     * Tests if this branch has any immediate children not any additional sub-branches.
     * 
     * @param path
     * @return
     */
    public boolean hasChildren(final String path) {
        boolean containsChildren = false;

        while (true) {
            // path matches this branch check children list.
            if (this.isSame(path)) {
                containsChildren = false == this.getChildren().isEmpty();
                break;
            }

            // found a sub=branch ask it.
            final Branch branch = this.findChildWhichIsParentOf(path);
            if (null != branch) {
                containsChildren = branch.hasChildren(path);
            }
            break;
        }
        return containsChildren;
    }

    /**
     * Returns the total number of branches under this branch.
     * 
     * @return
     */
    public int size() {
        int size = 1;
        final Iterator children = this.getChildren().iterator();
        while (children.hasNext()) {
            final Branch branch = (Branch) children.next();
            size = size + branch.size();
        }
        return size;
    }

    /**
     * Puts the given value into this branch or a sub-branch. This may result in a previous branch having its value replaced or a new branch
     * added to this or another sub-branch.
     * 
     * @param path
     * @param value
     * @return The previous / now lost value
     */
    public Object set(final String path, final Object value) {
        StringHelper.checkNotNull("parameter:path", path);

        Object previous = null;
        while (true) {
            final String thisPath = this.getPath();

            // special test if path matches the path of this branch.
            if (thisPath.equals(path)) {
                previous = this.setValue(value);
                break;
            }

            // if the new path is a parent of this branch ???
            if (Tree.isParentOf(path, thisPath)) {
                // make this path a child of this branch
                final Branch newChild = new Branch();
                newChild.setPath(thisPath);
                previous = newChild.setValue(this.getValue());

                // replace path/value with new values.
                this.setPath(path);
                this.setValue(value);

                // the old children of the old this are now children of the new
                // this
                // newChild is now a child of the new this.
                this.addChild(newChild);

                final Tree tree = this.getTree();
                newChild.setTree(tree);
                tree.increaseModificationCounter();
                break;
            }

            // are any of the children of this branch a parent or actually match
            // this path ?
            final Branch branch = this.findChildWhichIsParentOf(path);
            if (null != branch) {
                // found exact match replace its value
                if (branch.isSame(path)) {
                    previous = branch.setValue(value); // DONT update Tree
                    // modificationCount
                    break;
                }

                // found a parent or at best ancestor. get them to do the add.
                previous = branch.set(path, value);
                break;
            }

            // didnt find a parent simply create a new branch and make it a
            // child of this.
            final Branch newChild = new Branch();
            newChild.setPath(path);
            previous = newChild.setValue(value);
            this.addChild(newChild);

            final Tree tree = this.getTree();
            newChild.setTree(tree);
            tree.increaseModificationCounter();
            break;
        }

        return previous;
    }

    /**
     * Tests if a branch exists at the given path.
     * 
     * @param path
     * @return
     */
    public boolean hasValue(final String path) {
        return this.findBranch(path) != null;
    }

    /**
     * Gets the value of the branch at the given path. If the path does not previously exist an exception will be thrown.
     * 
     * @param path
     * @return
     */
    public Object get(final String path) {
        final Branch branch = this.getBranch(path);
        Object value = null;
        if (branch != null) {
            value = branch.getValue();
        }
        return value;
    }

    /**
     * Scans recursively until a branch that matches the given path is found.
     * 
     * @param path
     * @return
     */
    protected Branch findBranch(final String path) {
        StringHelper.checkNotNull("parameter:path", path);

        Branch branch = null;
        while (true) {
            // is there any branch that matches the parameter;path ?
            branch = this.findChild(path);
            if (null != branch) {
                break;
            }

            // try and find a parent of path.
            branch = this.findChildWhichIsParentOf(path);
            if (null == branch) {
                break;
            }

            // ask parent to find a mathc.
            if (branch instanceof Branch) {
                final Branch parent = branch.findBranch(path);
                if (null != parent) {
                    branch = parent;
                }
            }
            break;
        }

        return branch;
    }

    /**
     * Attempts to get a branch with the given a path
     * 
     * @param path
     * @return
     */
    protected Branch getBranch(final String path) {
        StringHelper.checkNotNull("parameter:path", path);

        return path.equals(this.getPath()) ? this : this.findBranch(path);
    }

    /**
     * Removes any branch or branch that matches the given path. If a branch is removed its children and those underneath them are also
     * removed.
     * 
     * @param path
     */
    public void removeBranch(final String path) {
        StringHelper.checkNotNull("parameter:path", path);

        final Iterator iterator = this.getChildren().iterator();
        while (iterator.hasNext()) {
            final Branch branch = (Branch) iterator.next();
            if (Tree.isParentOf(path, branch.getPath())) {
                iterator.remove();
                this.getTree().increaseModificationCounter();
                continue;
            }

            if (branch.isParentOf(path)) {
                branch.removeBranch(path);
            }
        }
    }

    /**
     * Removes only the branch holding the path. No chidren of teh branch are lost, they are simply moved to a new branch.
     * 
     * @param path
     * @return The value of the removed branch
     */
    public Object removeValue(final String path) {
        Object removed = null;

        final Branch branch = this.findChildWhichIsParentOf(path);
        if (null != branch) {
            if (branch.isSame(path)) {
                removed = branch.getValue();
                removeChild(branch);
                this.getChildren().addAll(branch.getChildren());
            } else {
                removed = branch.removeValue(path);
            }
        }

        this.getTree().increaseModificationCounter();
        return removed;
    }

    /**
     * Scans the children of this branch for a child using the given path. It ignores any branches that are parents.
     * 
     * @param path
     * @return
     */
    protected Branch findChild(final String path) {
        StringHelper.checkNotNull("parameter:path", path);
        Branch found = null;

        final Iterator iterator = this.getChildren().iterator();
        while (iterator.hasNext()) {
            final Branch branch = (Branch) iterator.next();
            if (branch.isSame(path)) {
                found = branch;
                break;
            }
        }

        return found;
    }

    /**
     * Scans the children of this branch for a parent of the path.
     * 
     * @param path
     * @return
     */
    protected Branch findChildWhichIsParentOf(final String path) {
        StringHelper.checkNotNull("parameter:path", path);
        Branch found = null;

        final Iterator iterator = this.getChildren().iterator();
        while (iterator.hasNext()) {
            final Branch branch = (Branch) iterator.next();
            if (branch.isParentOf(path)) {
                found = branch;
                break;
            }
        }

        return found;
    }

    /**
     * THe path that this branch is located at.
     */
    private String path;

    public String getPath() {
        StringHelper.checkNotNull("field:path", path);
        return path;
    }

    public void setPath(final String path) {
        StringHelper.checkNotNull("parameter:path", path);
        this.path = path;
    }

    /**
     * Tests if this branch /branch is a parent of the given path
     * 
     * @param path
     * @return
     */
    protected boolean isParentOf(final String path) {
        return Tree.isParentOf(getPath(), path);
    }

    /**
     * Tests if this branch is at the same path as the parameter:path
     * 
     * @param path
     * @return
     */
    protected boolean isSame(final String path) {
        StringHelper.checkNotNull("parameter:path", path);

        return this.getPath().equals(path);
    }

    private Object value;

    public Object getValue() {
        return value;
    }

    public Object setValue(final Object value) {
        final Object previousValue = this.value;
        this.value = value;
        return previousValue;
    }

    /**
     * Contains all the immediate children of this branch.
     */
    private List children;

    public List getChildren() {
        ObjectHelper.checkNotNull("field:children", children);
        return children;
    }

    public void setChildren(final List children) {
        ObjectHelper.checkNotNull("parameter:children", children);
        this.children = children;
    }

    protected void addChild(final Branch branch) {
        ObjectHelper.checkNotNull("parameter:branch", branch);
        this.getChildren().add(branch);
    }

    protected void removeChild(final Branch branch) {
        ObjectHelper.checkNotNull("parameter:branch", branch);
        this.getChildren().remove(branch);
    }

    /**
     * A reference to the parent Tree
     */
    private Tree tree;

    public Tree getTree() {
        ObjectHelper.checkNotNull("field:tree", tree);
        return tree;
    }

    public void setTree(final Tree tree) {
        ObjectHelper.checkNotNull("parameter:tree", tree);
        this.tree = tree;
    }

    public String toString() {
        return super.toString() + ", path[" + path + ", value: " + value + ", children: " + children + ", tree: "
                + ObjectHelper.defaultToString(tree);
    }
}