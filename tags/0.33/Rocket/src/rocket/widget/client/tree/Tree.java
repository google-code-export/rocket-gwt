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
package rocket.widget.client.tree;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.ui.Composite;

/**
 * A tree is the base container for more TreeItems which may be arranged in a hierarchical fashion. TreeItems themselves may host regular
 * widgets.
 * 
 * To find out if a particular treeNode has been clicked ClickListeners must be added to the widget themselves.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Tree extends Composite {

    public Tree() {
        this.setTreeListenerCollection( createTreeListenerCollection() );
        
        final TreeItem treeItem = this.createTreeItem();
        this.setTreeItem(treeItem);
        this.initWidget(treeItem);
        
        this.setStyleName(TreeConstants.TREE_STYLE);
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
        final TreeItem treeItem = new TreeItem();
        treeItem.setTree(this);
        return treeItem;
    }

    private String collapserImageUrl;

    public String getCollapserImageUrl() {
        StringHelper.checkNotEmpty("field:collapserImageUrl", collapserImageUrl);
        return this.collapserImageUrl;
    }

    public void setCollapserImageUrl(final String collapserImageUrl) {
        StringHelper.checkNotEmpty("parameter:collapserImageUrl", collapserImageUrl);
        this.collapserImageUrl = collapserImageUrl;
    }

    private String expanderImageUrl;

    public String getExpanderImageUrl() {
        StringHelper.checkNotEmpty("field:expanderImageUrl", expanderImageUrl);
        return this.expanderImageUrl;
    }

    public void setExpanderImageUrl(final String expanderImageUrl) {
        StringHelper.checkNotEmpty("parameter:expanderImageUrl", expanderImageUrl);
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
        ObjectHelper.checkNotNull("field:treeListenerCollection", treeListenerCollection);
        return treeListenerCollection;
    }

    public void setTreeListenerCollection(final TreeListenerCollection treeListenerCollection) {
        ObjectHelper.checkNotNull("parameter:treeListenerCollection", treeListenerCollection);
        this.treeListenerCollection = treeListenerCollection;
    }

    protected TreeListenerCollection createTreeListenerCollection() {
        return new TreeListenerCollection();
    }

    public String toString() {
        return super.toString() + ", treeItem: " + treeItem + ", collapserImageUrl[" + collapserImageUrl
                + "], expanderImageUrl[" + expanderImageUrl + "]";
    }
}
