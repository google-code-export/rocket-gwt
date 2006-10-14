package rocket.client.widget;

import java.util.Iterator;

import rocket.client.collection.IteratorView;
import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

/**
 * Convenient base class for any Panel implementation.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class AbstractPanel extends Panel implements HasWidgets {

    protected AbstractPanel() {
        this.createWidgetCollection();
    }

    /**
     * Returns the number of widgets within this panel.
     * 
     * @return
     */
    public int getWidgetCount() {
        return this.getWidgetCollection().size();
    }

    /**
     * Retrieves a widget within this panel.
     * 
     * @param index
     * @return
     */
    public Widget get(final int index) {
        return this.getWidgetCollection().get(index);
    }

    /**
     * Adds a new widget to the end of this panel.
     */
    public void add(Widget widget) {
        this.insert(widget, this.getWidgetCount());
    }

    /**
     * Sub-classes need to insert the given widget into the
     * 
     * @param widget
     * @param indexBefore
     */
    public void insert(Widget widget, int indexBefore) {
        final Element parentElement = this.insert0(widget, indexBefore);
        this.adopt(widget, parentElement);
        this.getWidgetCollection().insert(widget, indexBefore);
        this.incrementModificationCounter();
    }

    protected Element insert0(final Widget widget, int indexBefore) {
        ObjectHelper.checkNotNull("parameter:widget", widget);
        return this.insert0(widget.getElement(), indexBefore);
    }

    /**
     * Sub-classes need to create/find the element which will become the parent of the Widget's element
     * 
     * @param element
     * @param indexBefore
     * @return Element the parent element of the new widget.
     */
    protected abstract Element insert0(Element element, int indexBefore);

    /**
     * Attempts to remove an existing widget from this panel if it is a child.
     * 
     * @return true if the widget was a child and was successfully removed, otehrwise returns false.
     */
    public boolean remove(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        boolean removed = false;
        final WidgetCollection widgets = this.getWidgetCollection();
        final int index = widgets.indexOf(widget);
        if (-1 != index) {
            this.remove(index);
            removed = true;
        }

        return removed;
    }

    protected void remove0(final Widget widget, final int index) {
        ObjectHelper.checkNotNull("parameter:widget", widget);
        this.remove0(widget.getElement(), index);
        this.incrementModificationCounter();
    }

    /**
     * Cleanup opportunity for sub-classes to remove other outstanding elements from the dom.
     * 
     * @param element
     * @param index
     */
    protected abstract void remove0(Element element, int index);

    /**
     * Removes the widget at the given slot.
     * 
     * @param index
     */
    public void remove(final int index) {
        final WidgetCollection widgets = this.getWidgetCollection();

        final Widget widget = widgets.get(index);
        this.disown(widget);
        this.remove0(widget.getElement(), index);// cleanup opportunity
        // container elements etc.
        widgets.remove(index);
    }

    /**
     * Clears or removes all widgets from this panel.
     */
    public void clear() {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    /**
     * Returns an iterator that may be used to visit and possibly remove widgets belonging to this iterator.
     */
    public Iterator iterator() {
        final AbstractPanel that = this;
        final IteratorView iterator = new IteratorView() {

            protected boolean hasNext0() {
                return this.getIndex() < that.getWidgetCount();
            }

            protected Object next0(int type) {
                return that.get(this.getIndex());
            }

            protected void leavingNext() {
                this.setIndex(this.getIndex() + 1);
            }

            protected void remove0() {
                final int index = this.getIndex() - 1;
                that.remove(index);
                this.setIndex(index);
            }

            protected int getParentModificationCounter() {
                return that.getModificationCounter();
            }

            int index;

            int getIndex() {
                return this.index;
            }

            void setIndex(final int index) {
                this.index = index;
            }
        };

        iterator.syncModificationCounters();
        return iterator;
    }

    /**
     * This collection includes all the widgest that belong to each of the individual cells.
     */
    private WidgetCollection widgetCollection;

    protected WidgetCollection getWidgetCollection() {
        ObjectHelper.checkNotNull("field:widgetCollection", widgetCollection);
        return widgetCollection;
    }

    protected void setWidgetCollection(final WidgetCollection widgetCollection) {
        ObjectHelper.checkNotNull("parameter:widgetCollection", widgetCollection);
        this.widgetCollection = widgetCollection;
    }

    protected void createWidgetCollection() {
        this.setWidgetCollection(new WidgetCollection(this));
    }

    /**
     * The modificationCounter changes each time the panel changes. It allows iterators to keep track of structural changes and to fail
     * fast.
     */
    private int modificationCounter;

    protected int getModificationCounter() {
        return this.modificationCounter;
    }

    protected void setModificationCounter(final int modificationCounter) {
        this.modificationCounter = modificationCounter;
    }

    protected void incrementModificationCounter() {
        this.setModificationCounter(this.getModificationCounter() + 1);
    }
}
