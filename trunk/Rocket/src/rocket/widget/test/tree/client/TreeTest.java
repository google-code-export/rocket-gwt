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
package rocket.widget.test.tree.client;

import java.util.Iterator;

import rocket.util.client.ObjectHelper;
import com.google.gwt.user.client.ui.HorizontalPanel;
import rocket.widget.client.tree.Tree;
import rocket.widget.client.tree.TreeItem;
import rocket.widget.client.tree.TreeListener;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This TreeTest tests all aspects of the Tree and TreeItem widgets.
 * 
 * It also includes an interactive set of buttons that creates an iterator, queries if the iterator hasNext, visits the next element from
 * the iterator and removes the last visited element. A small area on the page is allocated to provide feedback of not only the iterator but
 * any of the other buttons that modify the tree/treeItems.
 * 
 * @author (Miroslav Pokorny) mP
 * 
 */
public class TreeTest implements EntryPoint {

    static Widget lastClicked = null;

    static TreeItem lastTreeItem = null;

    static Iterator iterator = null;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                caught.printStackTrace();
                Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
            }
        });

        final RootPanel rootPanel = RootPanel.get();
        final CheckBox veto = new CheckBox("confirm prompts - onBeforeCollapse() and onBeforeExpand()");
        rootPanel.add(veto);

        rootPanel.add(new HTML("<hr/>"));

        final Tree tree = new TestTreeWidget();
        tree.setCollapserImageUrl("collapser.gif");
        tree.setExpanderImageUrl("expander.gif");
        tree.addTreeListener(new TreeListener() {
            public boolean onBeforeCollapse(Widget widget) {
                log("onBeforeCollapse&nbsp;" + toString((TreeItem) widget));
                return veto.isChecked() ? Window.confirm("Collapse ?") : true;
            }

            public void onCollapse(Widget widget) {
                log("onCollapse&nbsp;" + toString((TreeItem) widget));
                TreeTest.lastTreeItem = (TreeItem) widget;
            }

            public boolean onBeforeExpand(Widget widget) {
                log("onBeforeExpand&nbsp;" + toString((TreeItem) widget));

                return veto.isChecked() ? Window.confirm("Expand ?") : true;
            }

            public void onExpand(Widget widget) {
                log("onExpand&nbsp;" + toString((TreeItem) widget));
                TreeTest.lastTreeItem = (TreeItem) widget;
            }

            public String toString(final TreeItem treeItem) {
                return DOM.getInnerText(treeItem.getWidget().getElement());
            }
        });

        rootPanel.add(tree);

        final TreeItem grandChildren = new TestTreeItem();
        grandChildren.setWidget(new HTML("GrandChild"));

        grandChildren.add(new HTML("grandChild #1"));
        grandChildren.add(new HTML("grandChild #2"));
        grandChildren.add(new HTML("grandChild #3"));

        final TreeItem root = tree.getTreeItem();
        root.setWidget(new HTML("Root"));

        root.add(new HTML("child #1"));
        root.add(grandChildren);
        root.add(new HTML("child #3"));

        rootPanel.add(new HTML("<br/>"));

        final Button createIterator = new Button("create Iterator");
        createIterator.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                TreeTest.iterator = tree.getTreeItem().iterator(Window.confirm("Visit Descendants ?"));
                log("iterator created: " + iterator);
            }
        });
        rootPanel.add(createIterator);

        final Button iteratorHasNext = new Button("iterator.hasNext()");
        iteratorHasNext.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                Iterator iterator = TreeTest.iterator;
                if (null != iterator) {
                    String append = null;
                    try {
                        append = "iterator.hasNext() returned " + iterator.hasNext();
                    } catch (final Exception caught) {
                        append = "iterator.hasNext() threw " + caught;
                    }
                    log(append);
                }
            }
        });
        rootPanel.add(iteratorHasNext);

        final Button iteratorNext = new Button("Iterator.next()");
        iteratorNext.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                Iterator iterator = TreeTest.iterator;
                if (null != iterator) {
                    String append = null;
                    try {
                        append = "iterator.next() returned " + iterator.next();
                    } catch (final Exception caught) {
                        append = "iterator.next() threw " + caught;
                    }
                    log(append);
                }
            }
        });
        rootPanel.add(iteratorNext);

        final Button iteratorRemove = new Button("Iterator.remove()");
        iteratorRemove.addClickListener(new ClickListener() {
            public void onClick(final Widget ignored) {
                Iterator iterator = TreeTest.iterator;
                if (null != iterator) {
                    String append = null;
                    try {
                        iterator.remove();
                        append = "iterator.removed() successful.";
                    } catch (final Exception caught) {
                        append = "iterator.removed() threw " + caught;
                    }
                    log(append);
                }
            }
        });
        rootPanel.add(iteratorRemove);
    }

    static void log(final String message) {
        final Element log = DOM.getElementById("log");
        DOM.setInnerHTML(log, DOM.getInnerHTML(log) + message + "<br>");
    }

    /**
     * This Tree creates a TestTreeItem as its root TreeItem
     * 
     * @author Miroslav Pokorny (mP)
     */
    class TestTreeWidget extends Tree {
        protected TreeItem createTreeItem() {
            ObjectHelper.checkPropertyNotSet("treeItem", this, this.hasTreeItem());

            final TestTreeItem treeItem = new TestTreeItem();
            this.setTreeItem(treeItem);
            treeItem.setTree(this);
            return treeItem;
        }
    }

    /**
     * A specialised TreeItem that adds various buttons to each newly added child widget each when clicked add siblings either before or
     * after or remove the current node from its parent
     * 
     * @author Miroslav Pokorny (mP)
     */
    class TestTreeItem extends TreeItem {

        public Widget getWidget() {
            final HorizontalPanel wrapper = (HorizontalPanel) super.getWidget();
            return wrapper.getWidget(0);
        }

        public void setWidget(final Widget widget) {
            final TestTreeItem that = this;

            final HorizontalPanel wrapper = new HorizontalPanel();
            wrapper.add(widget);

            final Button addHtmlChild = new Button("addHtmlChild");
            addHtmlChild.addClickListener(new ClickListener() {
                public void onClick(final Widget widget) {
                    that.add(new HTML("Html-" + System.currentTimeMillis()));
                }
            });
            wrapper.add(addHtmlChild);

            final Button addTreeItemChild = new Button("addTreeItemChild");
            addTreeItemChild.addClickListener(new ClickListener() {
                public void onClick(final Widget widget) {
                    final TestTreeItem newChildTreeItem = new TestTreeItem();
                    newChildTreeItem.setWidget(new HTML("TreeItem-" + System.currentTimeMillis()));
                    that.add(newChildTreeItem);
                }
            });
            wrapper.add(addTreeItemChild);

            final Button remove = new Button("remove");
            remove.addClickListener(new ClickListener() {
                public void onClick(final Widget widget) {
                    that.getParentTreeItem().remove(that);
                }
            });
            wrapper.add(remove);

            final CheckBox alwaysExpanded = new CheckBox("alwaysExpanded");
            alwaysExpanded.addClickListener(new ClickListener() {
                public void onClick(final Widget widget) {
                    that.setAlwaysExpanded(alwaysExpanded.isChecked());
                }
            });
            wrapper.add(alwaysExpanded);

            super.setWidget(wrapper);
        }

        public void insert(final Widget widget, final int beforeIndex) {
            if (widget instanceof TreeItem) {
                this.insertTreeItem((TreeItem) widget, beforeIndex);
            } else {
                this.insertWidget(widget, beforeIndex);
            }
        }

        protected void insertWidget(final Widget widget, final int beforeIndex) {
            final TestTreeItem that = this;

            final HorizontalPanel wrapper = new HorizontalPanel();
            wrapper.add(widget);

            final Button insertHtmlBefore = new Button("insertHtmlBefore");
            insertHtmlBefore.addClickListener(new ClickListener() {
                public void onClick(final Widget widget) {
                    final int index = that.getIndex(wrapper);
                    that.insert(new HTML("Html-" + System.currentTimeMillis()), index);
                }
            });
            wrapper.add(insertHtmlBefore);

            final Button insertTreeItemBefore = new Button("insertTreeItemBefore");
            insertTreeItemBefore.addClickListener(new ClickListener() {
                public void onClick(final Widget widget) {
                    final int index = that.getIndex(wrapper);
                    final TestTreeItem newChild = new TestTreeItem();
                    newChild.setWidget(new HTML("TreeItem-" + System.currentTimeMillis()));
                    that.insert(newChild, index);
                }
            });
            wrapper.add(insertTreeItemBefore);

            final Button insertHtmlAfter = new Button("insertHtmlAfter");
            insertHtmlAfter.addClickListener(new ClickListener() {
                public void onClick(final Widget widget) {
                    final int index = that.getIndex(wrapper);
                    that.insert(new HTML("Html-" + System.currentTimeMillis()), index + 1);
                }
            });
            wrapper.add(insertHtmlAfter);

            final Button insertTreeItemAfter = new Button("insertTreeItemAfter");
            insertTreeItemAfter.addClickListener(new ClickListener() {
                public void onClick(final Widget widget) {
                    final TestTreeItem newChild = new TestTreeItem();
                    newChild.setWidget(new HTML("TreeItem-" + System.currentTimeMillis()));

                    final int index = that.getIndex(wrapper);
                    that.insert(newChild, index + 1);
                }
            });
            wrapper.add(insertTreeItemAfter);

            final Button remove = new Button("remove");
            remove.addClickListener(new ClickListener() {
                public void onClick(final Widget widget) {
                    that.remove(wrapper);
                }
            });
            wrapper.add(remove);

            super.insert(wrapper, beforeIndex);
        }

        public void insertTreeItem(final TreeItem childTreeItem, final int beforeIndex) {
            childTreeItem.setParentTreeItem(this);
            super.insert(childTreeItem, beforeIndex);
        }

    }
}
