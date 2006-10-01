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
package rocket.client.widget.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.ui.Widget;

/**
 * A collection of TreeListeners as well as some helpers to help notify them of
 * various TreeListener events.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class TreeListenerCollection {

	public TreeListenerCollection() {
		this.createTreeListenerCollection();
	}

	public void add(final TreeListener treeListener) {
		ObjectHelper.checkNotNull("parameter:treeListener", treeListener);
		this.getTreeListenerCollection().add(treeListener);
	}

	public void remove(final TreeListener treeListener) {
		ObjectHelper.checkNotNull("parameter:treeListener", treeListener);
		this.getTreeListenerCollection().remove(treeListener);
	}

	protected boolean fireBeforeCollapse(Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		boolean collapse = true;
		final Iterator iterator = this.getTreeListenerCollection().iterator();
		while (iterator.hasNext()) {
			final TreeListener listener = (TreeListener) iterator.next();
			if (false == listener.onBeforeCollapse(widget)) {
				collapse = false;
				break;
			}
		}

		return collapse;
	}

	protected void fireCollapsed(Widget widget) {
		final Iterator iterator = this.getTreeListenerCollection().iterator();
		while (iterator.hasNext()) {
			final TreeListener listener = (TreeListener) iterator.next();
			listener.onCollapse(widget);
		}
	}

	protected boolean fireBeforeExpand(Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		boolean expand = true;
		final Iterator iterator = this.getTreeListenerCollection().iterator();
		while (iterator.hasNext()) {
			final TreeListener listener = (TreeListener) iterator.next();
			if (false == listener.onBeforeExpand(widget)) {
				expand = false;
				break;
			}
		}

		return expand;
	}

	protected void fireExpanded(Widget widget) {
		final Iterator iterator = this.getTreeListenerCollection().iterator();
		while (iterator.hasNext()) {
			final TreeListener listener = (TreeListener) iterator.next();
			listener.onExpand(widget);
		}
	}

	private List treeListenerCollection;

	protected List getTreeListenerCollection() {
		ObjectHelper.checkNotNull("field:treeListenerCollection",
				treeListenerCollection);
		return treeListenerCollection;
	}

	protected void setTreeListenerCollection(final List treeListenerCollection) {
		ObjectHelper.checkNotNull("parameter:treeListenerCollection",
				treeListenerCollection);
		this.treeListenerCollection = treeListenerCollection;
	}

	protected void createTreeListenerCollection() {
		this.setTreeListenerCollection(new ArrayList());
	}

	public String toString() {
		return super.toString() + ", treeListenerCollection: "
				+ treeListenerCollection;
	}
}
