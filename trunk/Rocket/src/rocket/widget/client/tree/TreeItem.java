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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A TreeItem widget contains both a main widget and child widgets which appear only if the TreeItem is expanded. A control appears next to
 * the main widget and when clicked either expand(shows) the children or collapses (hides) them.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * FIX Iterator when visiting descendants does not fail fast.
 */
public class TreeItem extends Composite {

    public TreeItem() {
        super();

        this.initWidget(this.createFlexTable());
        this.setExpanded(false);
    }

    /**
     * A flexTable is used to house the main widget and child widgets within a 2x2 flexTable.
     */
    private FlexTable flexTable;

    protected FlexTable getFlexTable() {
        ObjectHelper.checkNotNull("field:flexTable", flexTable);
        return flexTable;
    }

    protected void setFlexTable(final FlexTable flexTable) {
        ObjectHelper.checkNotNull("parameter:flexTable", flexTable);
        this.flexTable = flexTable;
    }

    protected FlexTable createFlexTable() {
        final FlexTable table = new FlexTable();
        table.setWidget(0, 0, this.createCollapserExpanderImage());
        table.setWidget(1, 1, this.createChildren());
        this.setFlexTable(table);
        table.addStyleName(TreeConstants.TREE_ITEM_STYLE);
        return table;
    }

    /**
     * When a TreeItem is alwaysExpanded its collapser/expander widget is hidden and its child widget panel is always shown.
     */
    private boolean alwaysExpanded;

    public boolean isAlwaysExpanded() {
        return this.alwaysExpanded;
    }

    public void setAlwaysExpanded(final boolean alwaysExpanded) {
        while (true) {
            final FlexTable table = this.getFlexTable();
            final int topRowCellCount = table.getCellCount(0);

            if (!alwaysExpanded && 1 == topRowCellCount) {
                table.insertCell(0, 0); // reinsert the initial first column.
                table.insertCell(1, 0);

                // set the collapser/expander widget again and update its url /
                // visibiility.
                table.setWidget(0, 0, this.getCollapserExpanderImage());

                this.updateCollapserExpanderImageUrl();
                this.updateCollapserExpanderVisibility();
                break;
            }

            if (alwaysExpanded || 2 == topRowCellCount) {
                this.expand();

                table.removeCell(0, 0); // remove the collapser/expander control
                table.removeCell(1, 0);
                break;
            }

            break;
        }
        this.alwaysExpanded = alwaysExpanded;
    }

    // COLLAPSER / EXPANDER ::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * This image allows the user to expand or collapse a tree node by clicking it.
     */
    private Image collapserExpanderImage;

    public Image getCollapserExpanderImage() {
        ObjectHelper.checkNotNull("field:collapserExpanderImage", collapserExpanderImage);
        return collapserExpanderImage;
    }

    public boolean hasCollapserExpanderImage() {
        return null != this.collapserExpanderImage;
    }

    public void setCollapserExpanderImage(final Image collapserExpanderImage) {
        ObjectHelper.checkNotNull("parameter:collapserExpanderImage", collapserExpanderImage);
        this.collapserExpanderImage = collapserExpanderImage;
    }

    /**
     * Note that the image source cannot be set until this TreeItem is attached to a Tree.
     * 
     * @return
     */
    protected Image createCollapserExpanderImage() {
        final Image image = new Image();
        image.addStyleName(TreeConstants.TREE_EXPANDER_COLLAPSER_STYLE);
        this.setCollapserExpanderImage(image);
        image.addClickListener(new ClickListener() {
            public void onClick(final Widget widget) {
                handleCollapserExpanderClick();
            }
        });
        return image;
    }

    protected void handleCollapserExpanderClick() {
        if (this.isExpanded()) {
            this.collapse();
        } else {
            this.expand();
        }
    }

    protected void updateCollapserExpanderImageUrl() {
        final Image image = this.getCollapserExpanderImage();
        image.setUrl(this.isExpanded() ? this.getCollapsedImageUrl() : this.getExpandedImageUrl());
    }

    protected void updateCollapserExpanderVisibility() {
        this.getCollapserExpanderImage().setVisible(this.getChildren().getWidgetCount() > 0);
    }

    protected void onLoad() {
        super.onLoad();

        updateCollapserExpanderImageUrl();
        updateCollapserExpanderVisibility();
    }

    protected String getCollapsedImageUrl() {
        return this.getTree().getCollapserImageUrl();
    }

    protected String getExpandedImageUrl() {
        return this.getTree().getExpanderImageUrl();
    }

    // MAIN :::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * The main or parent widget
     */
    private Widget widget;

    public Widget getWidget() {
        ObjectHelper.checkNotNull("field:widget", widget);
        return widget;
    }

    public boolean hasWidget() {
        return null != this.widget;
    }

    public void setWidget(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        widget.addStyleName(TreeConstants.TREE_WIDGET_STYLE);
        this.widget = widget;

        final FlexTable table = this.getFlexTable();
        table.setWidget(0, table.getCellCount(0) == 0 ? 0 : 1, widget);
    }

    // COLLAPSE / EXPAND
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void collapse() {
        final TreeListenerCollection listeners = this.getTree().getTreeListenerCollection();
        if (listeners.fireBeforeCollapse(this)) {
            this.setExpanded(false);
            listeners.fireCollapsed(this);
        }
    }

    public void expand() {
        final TreeListenerCollection listeners = this.getTree().getTreeListenerCollection();
        if (listeners.fireBeforeExpand(this)) {
            this.setExpanded(true);
            listeners.fireExpanded(this);
        }
    }

    public boolean isExpanded() {
        return this.getChildren().isVisible();
    }

    protected void setExpanded(final boolean expanded) {
        this.getChildren().setVisible(expanded);
        if (this.isAttached()) {
            this.updateCollapserExpanderImageUrl();
        }
    }

    // CHILD :::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * A children contains any child widgets.
     */
    private VerticalPanel children;

    protected VerticalPanel getChildren() {
        ObjectHelper.checkNotNull("field:children", children);
        return children;
    }

    protected boolean hasChildren() {
        return null != this.children;
    }

    protected void setChildren(final VerticalPanel children) {
        ObjectHelper.checkNotNull("parameter:children", children);
        this.children = children;
    }

    protected VerticalPanel createChildren() {
        final VerticalPanel panel = new VerticalPanel();
        panel.addStyleName(TreeConstants.TREE_EXPANDER_COLLAPSER_STYLE);
        this.setChildren(panel);
        return panel;
    }

    // WIDGET CONTAINER
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void add(final Widget widget) {
        this.insert(widget, this.getCount());
    }

    /**
     * Inserts a new widget to this treeItem.
     * 
     * @param widget
     * @param beforeIndex
     */
    public void insert(final Widget widget, final int beforeIndex) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        if (widget instanceof TreeItem) {
            final TreeItem treeItem = (TreeItem) widget;
            treeItem.setParentTreeItem(this);
        }

        this.getChildren().insert(widget, beforeIndex);
        updateCollapserExpanderVisibility();
    }

    public void remove(final Widget widget) {
        this.getChildren().remove(widget);
        updateCollapserExpanderVisibility();
    }

    public Widget get(final int index) {
        return this.getChildren().getWidget(index);
    }

    public int getIndex(final Widget widget) {
        return this.getChildren().getWidgetIndex(widget);
    }

    public int getCount() {
        return this.getChildren().getWidgetCount();
    }

    /**
     * Returns an iterator that visits all child widgets
     */
    public Iterator iterator() {
        return this.iterator(false);
    }

    /**
     * This iterator not only visits the widgets belonging to this TreeItem but also visits the children of all child TreeItems.
     * 
     * @param visitDescendants
     *            when true visits children and descendants, false visits only the direct children.
     * @return
     */
    public Iterator iterator(final boolean visitDescendants) {
        return visitDescendants ? this.iteratorWhichVisitsOnlyChildren() : this.iteratorWhichVisitsDescendants();
    }

    /**
     * Returns an iterator which visits only the immediate children of this TreeItem ignoring any children.
     * 
     * @return
     */
    protected Iterator iteratorWhichVisitsOnlyChildren() {
        return this.getChildren().iterator();
    }

    /**
     * The iterator returned by this method not only visits the children of this TreeItem but also visits the children of any child
     * TreeItem. If a TreeItem child is found its children and their children recursively are visited before the next sibling of the
     * original TreeItem.
     * 
     * @return
     */
    protected Iterator iteratorWhichVisitsDescendants() {
        final Stack iterators = new Stack();
        iterators.push(this.iterator());

        return new Iterator() {
            public boolean hasNext() {
                boolean more = false;
                while (false == this.isEmpty()) {
                    more = this.iterator().hasNext();
                    if (more) {
                        break;
                    }
                    this.pop();
                }
                return more;
            }

            public Object next() {
                if (false == this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final Iterator iterator = this.iterator();
                final Object object = iterator.next();
                this.setRemoveIterator(iterator);
                if (object instanceof TreeItem) {
                    final TreeItem treeItem = (TreeItem) object;
                    this.push(treeItem.iterator(true));
                }
                return object;
            }

            public void remove() {
                if (!this.hasRemoveIterator()) {
                    throw new RuntimeException(GWT.getTypeName(this) + ".remove() called without previous next()");
                }

                this.getRemoveIterator().remove();
                this.clearRemoveIterator();
            }

            Iterator iterator() {
                return (Iterator) iterators.peek();
            }

            boolean isEmpty() {
                return iterators.isEmpty();
            }

            void push(final Iterator iterator) {
                iterators.push(iterator);
            }

            void pop() {
                iterators.pop();
            }

            Iterator removeIterator;

            Iterator getRemoveIterator() {
                ObjectHelper.checkNotNull("field:removeIterator", this.removeIterator);
                return this.removeIterator;
            }

            boolean hasRemoveIterator() {
                return null != this.removeIterator;
            }

            void setRemoveIterator(final Iterator removeIterator) {
                ObjectHelper.checkNotNull("parameter:removeIterator", removeIterator);
                this.removeIterator = removeIterator;
            }

            void clearRemoveIterator() {
                this.removeIterator = null;
            }
        };
    }

    /**
     * The parent TreeItem that this widget is added too. This will not be null unless this treeItem is the root node, in which case the
     * tree property will be set.
     */
    private TreeItem parentTreeItem;

    public TreeItem getParentTreeItem() {
        ObjectHelper.checkNotNull("field:parentTreeItem", parentTreeItem);
        return parentTreeItem;
    }

    public boolean hasParentTreeItem() {
        return null != this.parentTreeItem;
    }

    public void setParentTreeItem(final TreeItem parentTreeItem) {
        ObjectHelper.checkNotNull("parameter:parentTreeItem", parentTreeItem);
        this.parentTreeItem = parentTreeItem;
    }

    /**
     * If this treeItem is attached to Tree this property will not be null, otherwise it will be null.
     */
    private Tree tree;

    public Tree getTree() {
        Tree tree = this.tree;
        if (false == hasTree()) {
            tree = this.getParentTreeItem().getTree();
        }

        ObjectHelper.checkNotNull("tree", tree);
        return tree;
    }

    public boolean hasTree() {
        return null != this.tree;
    }

    public void setTree(final Tree tree) {
        ObjectHelper.checkNotNull("parameter:tree", tree);
        this.tree = tree;
    }

    public String toString() {
        return super.toString() + ", widget: " + widget + ", children: " + this.children;
    }
}
