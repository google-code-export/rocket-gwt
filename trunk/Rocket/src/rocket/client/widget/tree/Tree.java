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

import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.ui.Composite;

/**
 * TreeItems house a widget themselves and other TreeItems. TreeItem may be
 * added to other TreeItems or the Tree themselves.
 * 
 * To find out if a particular treeNode has been clicked ClickListeners must be
 * added to the widget themselves.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Tree extends Composite {

	public Tree() {
		this.createTreeListenerCollection();
		this.setWidget(this.createTreeItem());
	}

	/**
	 * The root treeItem.
	 */
	private TreeItem treeItem;

	public TreeItem getTreeItem() {
		ObjectHelper.checkNotNull("field:treeItem", treeItem);
		return treeItem;
	}

	public boolean hasTreeItem() {
		return null != treeItem;
	}

	public void setTreeItem(final TreeItem treeItem) {
		ObjectHelper.checkNotNull("parameter:treeItem", treeItem);
		this.treeItem = treeItem;
	}

	protected TreeItem createTreeItem() {
		WidgetHelper.checkNotAlreadyCreated("treeItem", this.hasTreeItem());

		final TreeItem treeItem = new TreeItem();
		this.setTreeItem(treeItem);
		treeItem.setTree(this);

		this.addStyleName(TreeConstants.TREE_STYLE);
		return treeItem;
	}

	private String collapserImageUrl;

	public String getCollapserImageUrl() {
		StringHelper
				.checkNotEmpty("field:collapserImageUrl", collapserImageUrl);
		return this.collapserImageUrl;
	}

	public void setCollapserImageUrl(final String collapserImageUrl) {
		StringHelper.checkNotEmpty("parameter:collapserImageUrl",
				collapserImageUrl);
		this.collapserImageUrl = collapserImageUrl;
	}

	private String expanderImageUrl;

	public String getExpanderImageUrl() {
		StringHelper.checkNotEmpty("field:expanderImageUrl", expanderImageUrl);
		return this.expanderImageUrl;
	}

	public void setExpanderImageUrl(final String expanderImageUrl) {
		StringHelper.checkNotEmpty("parameter:expanderImageUrl",
				expanderImageUrl);
		this.expanderImageUrl = expanderImageUrl;
	}

	// LISTENERS
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	public void addTreeListener(final TreeListener listener) {
		this.getTreeListenerCollection().add(listener);
	}

	public void removeTreeListener(final TreeListener listener) {
		this.getTreeListenerCollection().remove(listener);
	}

	/**
	 * A collection of TreeListeners.
	 */
	private TreeListenerCollection treeListenerCollection;

	public TreeListenerCollection getTreeListenerCollection() {
		ObjectHelper.checkNotNull("field:treeListenerCollection",
				treeListenerCollection);
		return treeListenerCollection;
	}

	public void setTreeListenerCollection(
			final TreeListenerCollection treeListenerCollection) {
		ObjectHelper.checkNotNull("parameter:treeListenerCollection",
				treeListenerCollection);
		this.treeListenerCollection = treeListenerCollection;
	}

	protected void createTreeListenerCollection() {
		this.setTreeListenerCollection(new TreeListenerCollection());
	}

	public String toString() {
		return super.toString() + ", treeItem: " + treeItem
				+ ", collapserImageUrl[" + collapserImageUrl
				+ "], expanderImageUrl[" + expanderImageUrl + "]";
	}
}
